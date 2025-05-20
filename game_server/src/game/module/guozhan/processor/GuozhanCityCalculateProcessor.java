package game.module.guozhan.processor;

import db.proto.ProtoMessageGuozhan;
import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.bean.TimerTaskGuozhanCityReset;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuoZhanFightManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import io.netty.util.Timeout;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageBattle.C2SGuozhanCityCalculate;
import ws.WsMessageHall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = C2SGuozhanCityCalculate.id, accessLimit = 500)
public class GuozhanCityCalculateProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GuozhanCityCalculateProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {

    }

    @Override
    public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        C2SGuozhanCityCalculate reqmsg = C2SGuozhanCityCalculate.parse(request);
        logger.info("guozhan city battle calculate!player={},req={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if (!battleIdValid) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CGuozhanCityCalculate.msgCode, 1468);
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
            //enemy
            int cityIndex = BattleManager.getInstance().getTmpGuozhanCache(playerId);
            ProtoMessageGuozhan.DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
            ProtoMessageGuozhan.DBGuoZhanCity guoZhanCity = guoZhanFight.getCitys(cityIndex);
            List<Integer> playerIdList = guoZhanCity.getOccupyPlayerList();
            //max power
            int maxPowerPlayerId = playerIdList.get(0);
            int maxPower = 0;
            for (int aPlayerId : playerIdList) {
                PlayerBaseBean pbb = PlayerOfflineManager.getInstance().getPlayerOfflineCache(aPlayerId);
                if (pbb.getPower() > maxPower) {
                    maxPowerPlayerId = aPlayerId;
                    maxPower = pbb.getPower();
                }
            }
            //my
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            //敌方阵容
            Map<Integer, GeneralBean> enemyPlayerFormationMap = new HashMap<>();
            BattleFormation enemyPlayerFormation = PlayerOfflineManager.getInstance().getBattleFormation(maxPowerPlayerId);
            Map<Integer, Long> enemyDefenceFormation = enemyPlayerFormation.getPvpatt() != null ? enemyPlayerFormation.getPvpatt() :
                    enemyPlayerFormation.getNormal();
            Map<Long, GeneralBean> generalAllEnemy = PlayerOfflineManager.getInstance().getGeneralAll(maxPowerPlayerId);
            for (Map.Entry<Integer, Long> aEntry : enemyDefenceFormation.entrySet()) {
                int formationPos = aEntry.getKey();
                GeneralBean generalBean = generalAllEnemy.get(aEntry.getValue());
                if (generalBean == null) {
                    continue;
                }
                enemyPlayerFormationMap.put(formationPos, generalBean);
            }
            //simulate battle
            int myHpPerc = GuoZhanFightManager.getInstance().getPlayerHpPercent(playerId);
            int enemyHpPerc = GuoZhanFightManager.getInstance().getPlayerHpPercent(maxPowerPlayerId);
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateGuozhanPvp(playerId, battleFormation.getPvpatt(), myHpPerc,
                    enemyPlayerFormationMap, enemyHpPerc);
            logger.info("GuoZhan city pvp battle result:{}", battleRet);
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
            //取消定时任务
            Timeout timeout = GuoZhanFightManager.getInstance().getScheduleTimeout(cityIndex);
            boolean setNotInBattle = false;
            if (timeout != null) {
                TimerTaskGuozhanCityReset myTask = (TimerTaskGuozhanCityReset) timeout.task();
                myTask.decreaseTaskSize();
                if (myTask.getTaskSize() <= 0) {
                    timeout.cancel();
                    setNotInBattle = true;
                    GuoZhanFightManager.getInstance().removeScheduleTimeout(cityIndex);
                }
            }
            // 每日任务进度
//            DailyMissionManager.getInstance().gzChangeProgress(playingRole);
            //血量
            int[] hp_perc = null;
            Map<Long, GeneralBean> generalAllMy = GeneralCache.getInstance().getHeros(playerId);
            if (battleRet.ret.equals("win")) {
                //我方血量
                if (leftHpMap.size() > 0) {
                    long hpSum = 0;
                    long hpMaxSum = 0;
                    for (WsMessageBase.IOBHurt iobHurt : leftHpMap.values()) {
                        hpSum += iobHurt.hp;
                        hpMaxSum += iobHurt.hpmax;
                    }
                    myHpPerc = (int) (hpSum * 100 / hpMaxSum);
                    hp_perc = new int[]{myHpPerc};
                }else{
                    long hpSum = 0;
                    long hpMaxSum = 0;
                    for(Map.Entry<Integer,Long> aEntry : battleRet.lhp.entrySet()){
                        int formationPos = aEntry.getKey();
                        long generalHp = aEntry.getValue();
                        hpSum += generalHp;
                        Long generalUuid = battleFormation.getPvpatt().get(formationPos);
                        GeneralBean generalBean = generalAllMy.get(generalUuid);
                        hpMaxSum += generalBean.getHp();
                    }
                    myHpPerc = (int) (hpSum * 100 / hpMaxSum);
                    hp_perc = new int[]{myHpPerc};
                }
            } else {
                //敌方血量
                if (rightHpMap.size() > 0) {
                    long hpSum = 0;
                    long hpMaxSum = 0;
                    for (WsMessageBase.IOBHurt iobHurt : rightHpMap.values()) {
                        hpSum += iobHurt.hp;
                        hpMaxSum += iobHurt.hpmax;
                    }
                    enemyHpPerc = (int) (hpSum * 100 / hpMaxSum);
                    hp_perc = new int[]{enemyHpPerc};
                }else{
                    long hpSum = 0;
                    long hpMaxSum = 0;
                    for(Map.Entry<Integer,Long> aEntry : battleRet.rhp.entrySet()){
                        int formationPos = aEntry.getKey();
                        long generalHp = aEntry.getValue();
                        hpSum += generalHp;
                        GeneralBean generalBean = enemyPlayerFormationMap.get(formationPos);
                        hpMaxSum += generalBean.getHp();
                    }
                    enemyHpPerc = (int) (hpSum * 100 / hpMaxSum);
                    hp_perc = new int[]{enemyHpPerc};
                }
            }
            GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
            int myNationId = guozhanPlayer.getNation();
            GuoZhanFightManager.getInstance().processorCityCalculate(playingRole, battleRet, myNationId, playerId, cityIndex,
                    hp_perc, setNotInBattle);
        });
    }

}
