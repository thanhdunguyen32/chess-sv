package game.module.guozhan.processor;

import db.proto.ProtoMessageGuozhan.DBGuozhanPlayer;
import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.battle.dao.BattlePlayerBase;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuozhanConstants;
import game.module.guozhan.logic.GuozhanDaoHelper;
import game.module.guozhan.logic.GuozhanPlayerManager;
import game.module.log.constants.LogConstants;
import game.module.mine.bean.DBMinePoint;
import game.module.mission.constants.MissionConstants;
import game.module.pvp.logic.PvpConstants;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageBattle.C2SGuozhanBattleEnd.id, accessLimit = 500)
public class GuozhanBattleEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GuozhanBattleEndProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageBattle.C2SGuozhanBattleEnd reqmsg = WsMessageBattle.C2SGuozhanBattleEnd.parse(request);
        int playerId = playingRole.getId();
        logger.info("GuoZhan pve battle end!playerId={},msg={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if (!battleIdValid) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleEnd.msgCode, 1468);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Map<Integer, WsMessageBase.IOBHurt> leftHpMap = new HashMap<>();
        Map<Integer, WsMessageBase.IOBHurt> rightHpMap = new HashMap<>();
        if (reqmsg.as != null && reqmsg.as.length > 0) {
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.as) {
                rightHpMap.put(iobHurt.gsid, iobHurt);
            }
        }
        if (reqmsg.df != null && reqmsg.df.length > 0) {
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.df) {
                leftHpMap.put(iobHurt.gsid, iobHurt);
            }
        }
        GameServer.executorService.execute(() -> {
            //胜负判断
            WsMessageBase.IOBHurt[] rightHp = reqmsg.as;
            String battleResult = null;
            if (rightHp != null && rightHp.length > 0) {
                boolean isRightAllDie = true;
                for (WsMessageBase.IOBHurt iobHurt : rightHp) {
                    if (iobHurt.hp > 0) {
                        isRightAllDie = false;
                        break;
                    }
                }
                if (isRightAllDie) {
                    battleResult = "win";
                } else {
                    battleResult = "lose";
                }
            }
            //tmp param
            int city_index = BattleManager.getInstance().getTmpGuozhanCache(playerId);
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            //get city robot
            DBMinePoint robotFormation = BattleManager.getInstance().getRobotFormation(playerId, true);
            Map<Integer, BattlePlayerBase> robotFormationMap = robotFormation.getBattlePlayerMap();
            //simulate battle
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateGuoZhan(playerId, battleFormation.getPvpatt(), robotFormationMap);
            logger.info("GuoZhan pve battle result:{}", battleRet);
            //战斗结果修正
            if (battleResult != null) {
                battleRet.ret = battleResult;
            }
            for (WsMessageBase.IOBattleReportItem ioBattleReportItem : battleRet.report.left) {
                if (leftHpMap.get(ioBattleReportItem.gsid) != null) {
                    WsMessageBase.IOBHurt iobHurt = leftHpMap.get(ioBattleReportItem.gsid);
                    ioBattleReportItem.hurm = iobHurt.hurm;
                    ioBattleReportItem.heal = iobHurt.heal;
                }
            }
            for (WsMessageBase.IOBattleReportItem ioBattleReportItem : battleRet.report.right) {
                if (rightHpMap.get(ioBattleReportItem.gsid) != null) {
                    WsMessageBase.IOBHurt iobHurt = rightHpMap.get(ioBattleReportItem.gsid);
                    ioBattleReportItem.hurm = iobHurt.hurm;
                    ioBattleReportItem.heal = iobHurt.heal;
                }
            }
            //do
            //update mission progress
            AwardUtils.changeRes(playingRole, MissionConstants.GUOZHAN_BATTLE_PMARK, 1, LogConstants.MODULE_GUOZHAN);
            //
            GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
            if (guozhanPlayer == null) {
                guozhanPlayer = GuozhanPlayerManager.getInstance().createGuozhanPlayer(playerId);
                GuozhanCache.getInstance().addGuozhanPlayer(guozhanPlayer);
            }
            //save
            DBGuozhanPlayer dbGuozhanPlayer = guozhanPlayer.getDbGuozhanPlayer();
            DBGuozhanPlayer.Builder guozhanBuilder;
            if (dbGuozhanPlayer == null) {
                guozhanBuilder = DBGuozhanPlayer.newBuilder();
            } else {
                guozhanBuilder = dbGuozhanPlayer.toBuilder();
            }
            dbGuozhanPlayer = guozhanBuilder.putPassCityIndex(city_index, true).build();
            guozhanPlayer.setDbGuozhanPlayer(dbGuozhanPlayer);
            guozhanPlayer.setStay_city_index(city_index);
            if (guozhanPlayer.getId() == 0) {
                GuozhanDaoHelper.asyncInsertGuozhanPlayer(guozhanPlayer);
            } else {
                GuozhanDaoHelper.asyncUpdateGuozhanPlayer(guozhanPlayer);
            }
            // 奖励铜币
            int rewardCoins = GuozhanConstants.REWARD_BASE_COIN + 2000 * guozhanBuilder.getPassCityIndexCount();
            // 道具奖励
            List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
            RandomDispatcher<Integer> rd = new RandomDispatcher<>();
            for (int i = 0; i < PvpConstants.REWARD_RATE.length; i++) {
                rd.put(PvpConstants.REWARD_RATE[i], i);
            }
            Integer aIndex = rd.randomRemove();
            int[] rewardConfig = PvpConstants.PVP_REWARD[aIndex];
            int gsid = rewardConfig[0];
            int countMin = rewardConfig[1];
            int countMax = rewardConfig[2];
            int gsCount = RandomUtils.nextInt(countMin, countMax + 1);
            if (gsid == GameConfig.PLAYER.GOLD) {
                gsCount += rewardCoins;
            } else {
                AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, rewardCoins, LogConstants.MODULE_GUOZHAN);
                rewardItems.add(new WsMessageBase.IORewardItem(GameConfig.PLAYER.GOLD, rewardCoins));
            }
            AwardUtils.changeRes(playingRole, gsid, gsCount, LogConstants.MODULE_GUOZHAN);
            rewardItems.add(new WsMessageBase.IORewardItem(gsid, gsCount));
            //ret
            WsMessageBattle.S2CGuozhanBattleEnd respmsg = new WsMessageBattle.S2CGuozhanBattleEnd();
            respmsg.result = battleRet;
            respmsg.city_index = city_index;
            respmsg.reward = rewardItems;
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });
    }

}
