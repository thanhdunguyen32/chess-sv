package game.module.guozhan.processor;

import db.proto.ProtoMessageGuozhan;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOffice;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOfficePoint;
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
import game.module.guozhan.logic.GuoZhanManager;
import game.module.hero.bean.GeneralBean;
import game.module.log.constants.LogConstants;
import game.module.mine.bean.DBMinePoint;
import game.module.mission.constants.MissionConstants;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.pvp.logic.PvpConstants;
import io.netty.util.Timeout;
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
import ws.WsMessageBattle.C2SGuozhanOfficCalculate;
import ws.WsMessageBattle.S2CGuozhanOfficCalculate;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = C2SGuozhanOfficCalculate.id, accessLimit = 500)
public class GuozhanOfficeCalculateProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(GuozhanOfficeCalculateProcessor.class);

	@Override
	public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {

	}

	@Override
	public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		int playerId = playingRole.getId();
		C2SGuozhanOfficCalculate reqmsg = C2SGuozhanOfficCalculate.parse(request);
		logger.info("guozhan office battle calculate!player={},req={}", playerId, reqmsg);

        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if (!battleIdValid) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CGuozhanOfficCalculate.msgCode, 1468);
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
            int officeIndex = BattleManager.getInstance().getTmpGuozhanCache(playerId);
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            //
            GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
            DBGuoZhanOffice guoZhanOffice = GuozhanCache.getInstance().getDBGuoZhanOffice();
            int targetPlayerId = 0;
            int myNationId = guozhanPlayer.getNation();
            if (guoZhanOffice != null) {
                ProtoMessageGuozhan.DBGuoZhanNation guoZhanNation = guoZhanOffice.getNations(myNationId - 1);
                if (guoZhanNation.getPlayerOfficesCount() > 0) {
                    targetPlayerId = guoZhanNation.getPlayerOffices(officeIndex).getPlayerId();
                }
            }
            Map<Integer,GeneralBean> enemyPlayerFormationMap = null;
            Map<Integer, BattlePlayerBase> robotFormationMap = null;
            if(targetPlayerId == 0) {
                //get office robot
                DBMinePoint robotFormation = BattleManager.getInstance().getRobotFormation(playerId, true);
                robotFormationMap = robotFormation.getBattlePlayerMap();
            }else{
                enemyPlayerFormationMap = new HashMap<>();
                BattleFormation enemyPlayerFormation = PlayerOfflineManager.getInstance().getBattleFormation(targetPlayerId);
                Map<Integer, Long> enemyDefenceFormation = enemyPlayerFormation.getPvpatt() != null ? enemyPlayerFormation.getPvpatt() : enemyPlayerFormation.getNormal();
                Map<Long, GeneralBean> generalAll = PlayerOfflineManager.getInstance().getGeneralAll(targetPlayerId);
                for (Map.Entry<Integer, Long> aEntry : enemyDefenceFormation.entrySet()) {
                    int formationPos = aEntry.getKey();
                    GeneralBean generalBean = generalAll.get(aEntry.getValue());
                    if (generalBean == null) {
                        continue;
                    }
                    enemyPlayerFormationMap.put(formationPos, generalBean);
                }
            }
            //simulate battle
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateGuozhanOffice(playerId, battleFormation.getPvpatt(),
                    enemyPlayerFormationMap, robotFormationMap);
            logger.info("GuoZhan office battle result:{}", battleRet);
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
            //定时任务取消
            int pointVal = (myNationId-1)*100+officeIndex;
            Timeout timeout = GuoZhanManager.getInstance().getScheduleTimeout(pointVal);
            if(timeout != null) {
                timeout.cancel();
                GuoZhanManager.getInstance().removeScheduleTimeout(pointVal);
            }
            // 关卡id
            int oldOfficeIndex = -1;
            if (guoZhanOffice != null) {
                oldOfficeIndex = guoZhanOffice.getPlayerPointOrDefault(playerId, -1);
            }
            //体力
            S2CGuozhanOfficCalculate respmsg = new S2CGuozhanOfficCalculate();
            respmsg.office_index = officeIndex;
            respmsg.result = battleRet;
            DBGuoZhanOffice.Builder builder = guoZhanOffice.toBuilder();
            if (battleRet.ret.equals("win")) {
                // 老位置
                if (oldOfficeIndex > -1) {
                    builder.getNationsBuilder(myNationId - 1).getPlayerOfficesBuilder(oldOfficeIndex).setIsFighting(false)
                            .setPlayerId(0);
                }
                // 新位置
                DBGuoZhanOfficePoint.Builder newOfficeBuilder = builder.getNationsBuilder(myNationId - 1)
                        .getPlayerOfficesBuilder(officeIndex);
                int replacePlayerId = newOfficeBuilder.getPlayerId();
                newOfficeBuilder.setIsFighting(false).setPlayerId(playerId);
                builder.putPlayerPoint(playerId, officeIndex);
                // 替换玩家
                if (replacePlayerId > 0) {
                    if(oldOfficeIndex > -1) {
                        builder.getNationsBuilder(myNationId - 1).getPlayerOfficesBuilder(oldOfficeIndex).setPlayerId(replacePlayerId);
                        builder.putPlayerPoint(replacePlayerId, oldOfficeIndex);
                    }else {
                        builder.removePlayerPoint(replacePlayerId);
                    }
                }
                // 奖励铜币
                int rewardCoins = 200000 + 100000 * (10 - officeIndex / 10);
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
                respmsg.reward = rewardItems;
            } else {
                builder.getNationsBuilder(myNationId - 1).getPlayerOfficesBuilder(officeIndex).setIsFighting(false);
            }
            guoZhanOffice = builder.build();
            GuozhanCache.getInstance().setGuozhanOffice(guoZhanOffice);
            // 每日任务进度
//            DailyMissionManager.getInstance().gzChangeProgress(playingRole);
            // send
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });
	}

}
