package game.module.worldboss.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.legion.logic.LegionManager;
import game.module.log.constants.LogConstants;
import game.module.manor.processor.ManorBattleEndProcessor;
import game.module.season.bean.BattleSeason;
import game.module.season.dao.SeasonCache;
import game.module.template.*;
import game.module.worldboss.dao.LegionPracticeTemplateCache;
import game.module.worldboss.dao.WorldBossTemplateCache;
import game.module.worldboss.logic.WorldBossManager;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
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
@MsgCodeAnn(msgcode = WsMessageBattle.C2SWorldBossBattleEnd.id, accessLimit = 200)
public class WorldBossBattleEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(WorldBossBattleEndProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageBattle.C2SWorldBossBattleEnd reqmsg = WsMessageBattle.C2SWorldBossBattleEnd.parse(request);
        logger.info("legion boss battle end!player={},req={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if(!battleIdValid){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CWorldBossBattleEnd.msgCode,1468);
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
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CWorldBossBattleEnd.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
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
            BattleSeason battleSeason = SeasonCache.getInstance().getBattleSeason();
            Integer seasonId = battleSeason.getSeason();
            WorldBossTemplate worldBossTemplate = WorldBossTemplateCache.getInstance().getWorldBossTemplate(seasonId - 1);
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateTemplate(playerId, battleFormation.getNormal(),
                    worldBossTemplate.getBset());
            logger.info("legion world boss battle result:{}", battleRet);
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
            List<WsMessageBase.IORewardItem> dropItems = new ArrayList<>();
            //reward
            LegionPracticeTemplate.LegionPracticeBossTemplate legionPracticeBossTemplate =
                    LegionPracticeTemplateCache.getInstance().getWorldBossTemplate(seasonId);
            for (RewardTemplateSimple rewardTemplateSimple : legionPracticeBossTemplate.getFIGHT_REWARD()) {
                AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_LEGION);
                dropItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            //random reward
            List<RewardTemplateRandomRate> random_rate = legionPracticeBossTemplate.getRANDOM_RATE();
            RandomDispatcher<Integer> rd1 = new RandomDispatcher<>();
            for(RewardTemplateRandomRate rewardTemplateRandomRate : random_rate){
                if(rewardTemplateRandomRate.getCHANCE() >0) {
                    rd1.put(rewardTemplateRandomRate.getCHANCE(), rewardTemplateRandomRate.getNUM());
                }
            }
            Integer randCount = rd1.random();
            if(randCount >0){
                List<RewardTemplateChance> random_reward = legionPracticeBossTemplate.getRANDOM_REWARD();
                RandomDispatcher<RewardTemplateChance> rd2 = new RandomDispatcher<>();
                for(RewardTemplateChance rewardTemplateChance : random_reward){
                    if(rewardTemplateChance.getCHANCE() >0) {
                        rd2.put(rewardTemplateChance.getCHANCE(), rewardTemplateChance);
                    }
                }
                for (int i = randCount; i > 0; i--) {
                    RewardTemplateChance rewardTemplateChance = rd2.randomRemove();
                    AwardUtils.changeRes(playingRole, rewardTemplateChance.getGSID(), rewardTemplateChance.getCOUNT(), LogConstants.MODULE_LEGION);
                    dropItems.add(new WsMessageBase.IORewardItem(rewardTemplateChance.getGSID(), rewardTemplateChance.getCOUNT()));
                }
            }
            //伤害统计
            long hurmSum = 0;
            if (reqmsg.df != null && reqmsg.df.length > 0) {
                hurmSum = ManorBattleEndProcessor.getClientHurmSum(reqmsg.df);
            } else {
                hurmSum = getMyHurm(battleRet);
            }
            WorldBossManager.getInstance().addHurtVal(playerId,legionId,hurmSum);
            //ret
            WsMessageBattle.S2CWorldBossBattleEnd respmsg = new WsMessageBattle.S2CWorldBossBattleEnd();
            respmsg.result = battleRet;
            respmsg.rewards = dropItems;
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
