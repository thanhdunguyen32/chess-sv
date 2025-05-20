package game.module.activity.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.log.constants.LogConstants;
import game.module.manor.processor.ManorBattleEndProcessor;
import game.module.template.ZhdTgslTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2STgslBossEnd.id, accessLimit = 200)
public class TgslBossEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(TgslBossEndProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageBattle.C2STgslBossEnd reqmsg = WsMessageBattle.C2STgslBossEnd.parse(request);
        logger.info("tgsl boss battle end!player={},req={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if(!battleIdValid){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTgslBossEnd.msgCode,1468);
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
        //
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
            ZhdTgslTemplate tgslTemplate = ActivityWeekTemplateCache.getInstance().getTgslTemplate();
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateTemplate(playerId, battleFormation.getNormal(),
                    tgslTemplate.getBoss().getBset());
            logger.info("tgsl boss battle result:{}", battleRet);
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
            //do
            //伤害统计
            long hurmSum = 0;
//            if (reqmsg.as != null) {
//                long nowHp = getRightHp(reqmsg.as);
//                hurmSum = tgslTemplate.getBoss().getHp() - nowHp;
//                hurmSum = Math.max(hurmSum,0);
//            } else {
            if (reqmsg.df != null && reqmsg.df.length > 0) {
                hurmSum = ManorBattleEndProcessor.getClientHurmSum(reqmsg.df);
            } else {
                hurmSum = getMyHurm(battleRet);
            }
            int rewardCount = 0;
            List<List<Integer>> rewards = tgslTemplate.getReward();
            for (List<Integer> aPair : rewards) {
                if (hurmSum >= aPair.get(0)) {
                    rewardCount = aPair.get(1);
                    break;
                }
            }
            int rewardGsid = tgslTemplate.getBoss().getChallrewards().get(0);
            AwardUtils.changeRes(playingRole, rewardGsid, rewardCount, LogConstants.MODULE_ACTIVITY);
            List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
            rewardItems.add(new WsMessageBase.IORewardItem(rewardGsid, rewardCount));
            //ret
            WsMessageBattle.S2CTgslBossEnd respmsg = new WsMessageBattle.S2CTgslBossEnd();
            respmsg.result = battleRet;
            respmsg.reward = rewardItems;
            respmsg.damge = hurmSum;
            respmsg.kill = battleRet.ret.equals("win") ? 1 : 0;
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });
    }

    private long getMyHurm(WsMessageBase.IOBattleResult battleRet) {
        long ret = 0;
        for (WsMessageBase.IOBattleReportItem ioBattleReportItem : battleRet.report.left) {
            ret += ioBattleReportItem.hurm;
        }
        return ret;
    }

}
