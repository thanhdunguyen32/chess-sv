package game.module.bigbattle.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.bigbattle.dao.BigBattleTemplateCache;
import game.module.bigbattle.dao.BigChapterTemplateCache;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.ChapterBattleTemplateCache;
import game.module.log.constants.LogConstants;
import game.module.season.logic.SeasonManager;
import game.module.template.BigChapterTemplate;
import game.module.template.ChapterBattleTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageBigbattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBigbattle.C2SBigBattleEnd.id, accessLimit = 200)
public class BigBattleEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(BigBattleEndProcessor.class);

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
        WsMessageBigbattle.C2SBigBattleEnd reqmsg = WsMessageBigbattle.C2SBigBattleEnd.parse(request);
        logger.info("big battle end!player={},req={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if(!battleIdValid){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CBigBattleEnd.msgCode,1468);
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
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            int mapid = reqmsg.mapid;
            Map<Integer, ChapterBattleTemplate> bigBattleTeam = BigBattleTemplateCache.getInstance().getBigBattleByMapId(mapid);
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateTemplate(playerId, battleFormation.getNormal(), bigBattleTeam);
            logger.info("big battle result:{}", battleRet);
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
                //win,progress+1
                BigChapterTemplate bigChapterTemplate = BigChapterTemplateCache.getInstance().getBigChapterTemplateById(mapid);
                AwardUtils.changeRes(playingRole, bigChapterTemplate.getPASSID(), 1, LogConstants.BIG_BATTLE);
                //award
                List<RewardTemplateSimple> rewards = bigChapterTemplate.getREWARDS();
                int seasonAddon = SeasonManager.getInstance().getBigBattleAddon();
                for (RewardTemplateSimple rewardTemplateSimple : rewards) {
                    int itemCount = (int)(rewardTemplateSimple.getCOUNT() * (1 + seasonAddon/100f));
                    AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), itemCount, LogConstants.BIG_BATTLE);
                    rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
            }
            //ret
            WsMessageBigbattle.S2CBigBattleEnd respmsg = new WsMessageBigbattle.S2CBigBattleEnd();
            respmsg.result = battleRet;
            respmsg.rewards = rewardItems;
            respmsg.times = 1;
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });
    }

}
