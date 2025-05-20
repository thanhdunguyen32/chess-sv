package game.module.activity.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActTnqw;
import game.module.activity.dao.ActTnqwCache;
import game.module.activity.dao.ActTnqwDaoHelper;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.log.constants.LogConstants;
import game.module.manor.processor.ManorBattleEndProcessor;
import game.module.template.RewardTemplateSimple;
import game.module.template.ZhdTnqwBossTemplate;
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
@MsgCodeAnn(msgcode = WsMessageBattle.C2STnqwBossEnd.id, accessLimit = 200)
public class TnqwBossEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(TnqwBossEndProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageBattle.C2STnqwBossEnd reqmsg = WsMessageBattle.C2STnqwBossEnd.parse(request);
        logger.info("tnqw boss battle end!player={},req={}", playerId, reqmsg);
        int boss_index = reqmsg.boss_index;
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if (!battleIdValid) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTnqwBossEnd.msgCode, 1468);
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
            List<ZhdTnqwBossTemplate> tnqwBossTemplates = ActivityWeekTemplateCache.getInstance().getTnqwBossTemplates();
            ZhdTnqwBossTemplate zhdTnqwBossTemplate = tnqwBossTemplates.get(boss_index);
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateTemplate(playerId, battleFormation.getNormal(),
                    zhdTnqwBossTemplate.getBset());
            logger.info("tnqw boss battle result:{}", battleRet);
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
            ActTnqw actTnqw = ActTnqwCache.getInstance().getActTnqw(playerId);
            long hurmSum = 0;
            if (reqmsg.df != null && reqmsg.df.length > 0) {
                hurmSum = ManorBattleEndProcessor.getClientHurmSum(reqmsg.df);
            } else {
                hurmSum = getMyHurm(battleRet);
            }
            if (actTnqw == null) {
                actTnqw = new ActTnqw();
                actTnqw.setPlayerId(playerId);
                Map<Integer, Long> nowHpMap = new HashMap<>();
                long nowHp = zhdTnqwBossTemplate.getMaxhp() - hurmSum;
                nowHp = Math.max(0, nowHp);
                nowHpMap.put(boss_index, nowHp);
                actTnqw.setNowHp(nowHpMap);
                Map<Integer, Long> lastDamageMap = new HashMap<>();
                lastDamageMap.put(boss_index, hurmSum);
                actTnqw.setLastDamage(lastDamageMap);
                ActTnqwDaoHelper.asyncInsertActTnqw(actTnqw);
                ActTnqwCache.getInstance().addActTnqw(actTnqw);
            } else {
                Long nowHp = actTnqw.getNowHp().get(boss_index);
                if (nowHp == null) {
                    nowHp = zhdTnqwBossTemplate.getMaxhp();
                }
                nowHp -= hurmSum;
                nowHp = Math.max(0, nowHp);
                actTnqw.getNowHp().put(boss_index, nowHp);
                actTnqw.getLastDamage().put(boss_index, hurmSum);
                ActTnqwDaoHelper.asyncUpdateActTnqw(actTnqw);
            }
            //reward
            List<RewardTemplateSimple> challrewards = zhdTnqwBossTemplate.getChallrewards();
            List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : challrewards) {
                AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_ACTIVITY);
                rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            //check die
            if (actTnqw.getNowHp().get(boss_index) <= 0) {
                List<RewardTemplateSimple> killrewards = zhdTnqwBossTemplate.getKillrewards();
                for (RewardTemplateSimple rewardTemplateSimple : killrewards) {
                    AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_ACTIVITY);
                    rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
            }
            //ret
            WsMessageBattle.S2CTnqwBossEnd respmsg = new WsMessageBattle.S2CTnqwBossEnd();
            respmsg.result = battleRet;
            respmsg.reward = rewardItems;
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
