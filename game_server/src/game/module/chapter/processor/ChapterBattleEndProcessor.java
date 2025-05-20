package game.module.chapter.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.bean.ChapterBean;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.ChapterBattleTemplateCache;
import game.module.chapter.dao.ChapterCache;
import game.module.chapter.dao.ChapterTemplateCache;
import game.module.chapter.logic.ChapterDaoHelper;
import game.module.chapter.logic.ChapterManager;
import game.module.log.constants.LogConstants;
import game.module.manor.logic.SurrenderPersuadeManager;
import game.module.template.ChapterBattleTemplate;
import game.module.template.ChapterTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.*;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SChapterBattleEnd.id, accessLimit = 200)
public class ChapterBattleEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ChapterBattleEndProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 加载所有邮件
     */
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageBattle.C2SChapterBattleEnd reqmsg = WsMessageBattle.C2SChapterBattleEnd.parse(request);
        logger.info("chapter battle end!player={},req={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if(!battleIdValid){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CChapterBattleEnd.msgCode,1468);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Map<Integer,WsMessageBase.IOBHurt> leftHpMap = new HashMap<>();
        Map<Integer,WsMessageBase.IOBHurt> rightHpMap = new HashMap<>();
        if(reqmsg.as != null && reqmsg.as.length>0){
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.as){
                rightHpMap.put(iobHurt.gsid,iobHurt);
            }
        }
        if(reqmsg.df != null && reqmsg.df.length>0){
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.df){
                leftHpMap.put(iobHurt.gsid,iobHurt);
            }
        }
        //do
        GameServer.executorService.execute(() -> {
            //胜负判断
            WsMessageBase.IOBHurt[] rightHp = reqmsg.as;
            String battleResult = null;
            if(rightHp != null && rightHp.length>0) {
                boolean isRightAllDie = true;
                for (WsMessageBase.IOBHurt iobHurt : rightHp){
                    if(iobHurt.hp>0){
                        isRightAllDie = false;
                        break;
                    }
                }
                if(isRightAllDie){
                    battleResult = "win";
                }else{
                    battleResult = "lose";
                }
            }
            ChapterBean chapterBean = ChapterCache.getInstance().getPlayerChapter(playerId);
            Integer maxMapId = 0;
            if (chapterBean == null) {
                maxMapId = ChapterManager.getInstance().getInitMapId();
            } else {
                maxMapId = chapterBean.getMaxMapId();
            }
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            Map<Integer, ChapterBattleTemplate> chapterBattleItem = ChapterBattleTemplateCache.getInstance().getChapterBattleById(maxMapId);
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateTemplate(playerId, battleFormation.getNormal(), chapterBattleItem);
            logger.info("chapter battle result:{}", battleRet);
            //战斗结果修正
            if(battleResult != null){
                battleRet.ret = battleResult;
            }
            for(WsMessageBase.IOBattleReportItem ioBattleReportItem : battleRet.report.left){
                if(leftHpMap.get(ioBattleReportItem.gsid) != null){
                    WsMessageBase.IOBHurt iobHurt = leftHpMap.get(ioBattleReportItem.gsid);
                    ioBattleReportItem.hurm = iobHurt.hurm;
                    ioBattleReportItem.heal = iobHurt.heal;
                }
            }
            for(WsMessageBase.IOBattleReportItem ioBattleReportItem : battleRet.report.right){
                if(rightHpMap.get(ioBattleReportItem.gsid) != null){
                    WsMessageBase.IOBHurt iobHurt = rightHpMap.get(ioBattleReportItem.gsid);
                    ioBattleReportItem.hurm = iobHurt.hurm;
                    ioBattleReportItem.heal = iobHurt.heal;
                }
            }
            List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
            if (battleRet.ret.equals("win")) {
                //ret
                ChapterTemplate chapterTemplate = ChapterTemplateCache.getInstance().getChapterTemplateById(maxMapId);
                List<RewardTemplateSimple> rewards = chapterTemplate.getREWARDS();
                //send reward
                for (RewardTemplateSimple rewardTemplateSimple : rewards) {
                    Integer gsid = rewardTemplateSimple.getGSID();
                    int itemCount = rewardTemplateSimple.getCOUNT();
                    AwardUtils.changeRes(playingRole, gsid, itemCount, LogConstants.MODULE_CHAPTER);
                    rewardItems.add(new WsMessageBase.IORewardItem(gsid, itemCount));
                }
                //add surrender
                SurrenderPersuadeManager.getInstance().passChapter(playerId, maxMapId);
                //save bean
                int nextMapId = ChapterManager.getInstance().getNextMapId(maxMapId);
                if (chapterBean == null) {
                    chapterBean = ChapterManager.getInstance().createChapterBean(playerId, nextMapId, new Date());
                    ChapterDaoHelper.asyncInsertChapterBean(chapterBean);
                    ChapterCache.getInstance().addChapterBean(chapterBean);
                } else {
                    chapterBean.setMaxMapId(nextMapId);
                    ChapterDaoHelper.asyncUpdateChapterBean(chapterBean);
                }
            }
            //push
//                WsMessageHall.PushPropChange pushmsg = new WsMessageHall.PushPropChange(100013, nextMapId);
//                playingRole.write(pushmsg.build(playingRole.alloc()));
            //ret
            WsMessageBattle.S2CChapterBattleEnd respmsg = new WsMessageBattle.S2CChapterBattleEnd();
            respmsg.items = rewardItems;
            respmsg.result = battleRet;
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });

    }

}
