package game.module.mine.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.log.constants.LogConstants;
import game.module.mine.bean.DBMine;
import game.module.mine.bean.DBMinePoint;
import game.module.mine.dao.MineCache;
import game.module.mine.logic.MineManager;
import game.module.mission.constants.MissionConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SMineBattleEnd.id, accessLimit = 200)
public class MineBattleEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MineBattleEndProcessor.class);

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
        WsMessageBattle.C2SMineBattleEnd reqmsg = WsMessageBattle.C2SMineBattleEnd.parse(request);
        logger.info("mine battle end!player={},req={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if (!battleIdValid) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleEnd.msgCode, 1468);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Map<Integer, WsMessageBase.IOBHurt> leftHpMap = new HashMap<>();
        Map<Integer, WsMessageBase.IOBHurt> rightHpMap = new HashMap<>();
        Map<Integer, Integer> leftHpPercMap = new HashMap<>();
        Map<Integer, Integer> rightHpPercMap = new HashMap<>();
        if (reqmsg.as != null && reqmsg.as.length > 0) {
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.as) {
                rightHpMap.put(iobHurt.gsid, iobHurt);
                rightHpPercMap.put(iobHurt.born, iobHurt.hpperc);
            }
        }
        if (reqmsg.df != null && reqmsg.df.length > 0) {
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.df) {
                leftHpMap.put(iobHurt.gsid, iobHurt);
                leftHpPercMap.put(iobHurt.born, iobHurt.hpperc);
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
            int[] tmpMineCache = BattleManager.getInstance().getTmpMineCache(playerId);
            int level_index = tmpMineCache[0];
            int point_index = tmpMineCache[1];
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            //get mine point
            DBMine mineEntity = MineCache.getInstance().getDBMine();
            DBMinePoint minePointEntity = mineEntity.getLevels().get(level_index).getMinePoints().get(point_index);
            //simulate battle
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateMine(playerId, battleFormation.getPvpatt(), minePointEntity);
            logger.info("mine battle result:{}", battleRet);
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
            AwardUtils.changeRes(playingRole, MissionConstants.MINE_BATTLE_PMARK, 1, LogConstants.MODULE_MINE);
            //
            int minePointVal = MineManager.getInstance().genereteMintPoint(level_index, point_index);
            WsMessageBattle.S2CMineBattleEnd respmsg = MineManager.getInstance().battleCalculate(playingRole, battleRet.ret.equals("win") ? 1 : 0, level_index,
                    point_index, minePointVal);
            respmsg.result = battleRet;
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });
    }

}
