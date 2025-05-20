package game.module.pvp.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity_month.logic.ActivityMonthManager;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.log.constants.LogConstants;
import game.module.occtask.logic.OccTaskManager;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.pay.logic.ChargeInfoManager;
import game.module.pvp.bean.PvpBean;
import game.module.pvp.bean.PvpPlayer;
import game.module.pvp.dao.PvpCache;
import game.module.pvp.logic.PvpConstants;
import game.module.pvp.logic.PvpManager;
import game.module.pvp.logic.PvpRecordManager;
import game.session.SessionManager;
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

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SPvpBattleEnd.id, accessLimit = 200)
public class PvpBattleEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(PvpBattleEndProcessor.class);

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
        WsMessageBattle.C2SPvpBattleEnd reqmsg = WsMessageBattle.C2SPvpBattleEnd.parse(request);
        logger.info("pvp battle end!player={},req={}", playerId, reqmsg);
        
        // Kiểm tra null và validate dữ liệu
        if (reqmsg == null || reqmsg.as == null || reqmsg.df == null) {
            logger.error("Invalid battle end request data");
            return;
        }
        
        // battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if(!battleIdValid) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CPvpBattleEnd.msgCode,1468);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }

        GameServer.executorService.execute(() -> {
            try {
                // Xử lý kết quả trận đấu
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
                //enemy player id
                PvpBean pvpBean = PvpCache.getInstance().getPvpBean();
                if (pvpBean == null || pvpBean.getPvpPlayerInfo() == null) {
                    logger.error("PvpBean or player info is null for player {}", playerId);
                    return;
                }

                PvpPlayer myPvpInfo = pvpBean.getPvpPlayerInfo().get(playerId);
                if (myPvpInfo == null || myPvpInfo.getAgainstPlayers() == null) {
                    logger.error("Player PVP info not found for player {}", playerId);
                    return;
                }

                Integer enemyPlayerId = myPvpInfo.getAgainstPlayers().get(myPvpInfo.getEnemyIndex());
                if (enemyPlayerId == null) {
                    logger.error("Enemy player ID is null for player {}", playerId);
                    return;
                }

                BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
                if (battleFormation == null) {
                    logger.error("Battle formation not found for player {}", playerId);
                    return;
                }

                BattleFormation rightBattleFormation = PlayerOfflineManager.getInstance().getBattleFormation(enemyPlayerId);
                if (rightBattleFormation == null) {
                    logger.error("Right battle formation not found for enemy player {}", enemyPlayerId);
                    return;
                }

                if (battleFormation.getPvpatt() == null) {
                    logger.error("PVP attack formation is null for player {}", playerId);
                    return;
                }

                if (rightBattleFormation.getPvpdef() == null) {
                    logger.error("PVP defense formation is null for enemy player {}", enemyPlayerId);
                    return;
                }

                WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulatePvp(playerId, battleFormation.getPvpatt(), enemyPlayerId,
                        rightBattleFormation.getPvpdef());
                if (battleRet == null) {
                    logger.error("Battle simulation returned null result");
                    return;
                }
                logger.info("pvp battle result:{}", battleRet);
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
                //win add score
                boolean isWin = battleRet.ret.equals("win");
                int myScore = PvpManager.getInstance().getPvpScore(playerId);
                int enemyScore = PvpManager.getInstance().getPvpScore(enemyPlayerId);
                //挑战比自己排名高的
                Integer myRankIndex = pvpBean.getMyRankMap().get(playerId);
                Integer enemyRankIndex = pvpBean.getMyRankMap().get(enemyPlayerId);
                int myScoreChange;
                if (((myRankIndex == null && enemyRankIndex != null) || (myRankIndex != null && enemyRankIndex != null && enemyRankIndex < myRankIndex)) && isWin) {
                    myScoreChange = RandomUtils.nextInt(15, 30);
                } else if (((myRankIndex != null && enemyRankIndex == null) || (myRankIndex != null && enemyRankIndex != null && enemyRankIndex > myRankIndex)) && !isWin) {
                    myScoreChange = RandomUtils.nextInt(15, 30) * -1;
                } else {
                    myScoreChange = RandomUtils.nextInt(10, 20) * (isWin ? 1 : -1);
                }
                myScore += myScoreChange;
                int enemyScoreChange = -myScoreChange;
                enemyScore += enemyScoreChange;
                PvpRecordManager.getInstance().addPvpRecord(playingRole.getPlayerBean(), battleFormation.getPvpatt(), enemyPlayerId,
                        rightBattleFormation.getPvpdef(), battleRet, myScore, myScoreChange, enemyScore, enemyScoreChange);
                //save score
                PvpManager.getInstance().saveScore(playerId, myScore);
                PvpManager.getInstance().saveScore(enemyPlayerId, enemyScore);
                long battleid = reqmsg.battleid;
                //award
                List<WsMessageBase.IORewardItemSelect> rewardItems = new ArrayList<>();
                RandomDispatcher<Integer> rd = new RandomDispatcher<>();
                for (int i = 0; i < PvpConstants.REWARD_RATE.length; i++) {
                    rd.put(PvpConstants.REWARD_RATE[i], i);
                }
                int selectSize = 3;
                boolean isBwAddon = ChargeInfoManager.getInstance().isBwAddon(playerId);
                for (int i = 0; i < selectSize; i++) {
                    Integer aIndex = rd.randomRemove();
                    int[] rewardConfig = PvpConstants.PVP_REWARD[aIndex];
                    int gsid = rewardConfig[0];
                    int countMin = rewardConfig[1];
                    int countMax = rewardConfig[2];
                    int gsCount = RandomUtils.nextInt(countMin, countMax + 1);
                    boolean isSelect = i == 0;
                    if (isBwAddon) {
                        isSelect = true;
                    }
                    if (isSelect) {
                        AwardUtils.changeRes(playingRole, gsid, gsCount, LogConstants.MODULE_PVP);
                    }
                    rewardItems.add(new WsMessageBase.IORewardItemSelect(gsid, gsCount, isSelect));
                }
                //remove in battle info
                myPvpInfo.setInBattle(false);
                myPvpInfo.setAgainstPlayers(null);
                PvpPlayer enemyPvpInfo = pvpBean.getPvpPlayerInfo().get(enemyPlayerId);
                enemyPvpInfo.setInBattle(false);
                enemyPvpInfo.setAgainstPlayers(null);
                //month activity
                ActivityMonthManager.getInstance().pvpBattleEnd(playingRole, isWin, false);
                OccTaskManager.getInstance().pvpAddOccTaskMark(playingRole);
                //ret
                WsMessageBattle.S2CPvpBattleEnd respmsg = new WsMessageBattle.S2CPvpBattleEnd();
                respmsg.result = battleRet;
                respmsg.reward = rewardItems;
                respmsg.spoints = myScore;
                respmsg.schange = myScoreChange;
                respmsg.epoints = enemyScore;
                respmsg.echange = enemyScoreChange;
                respmsg.videoid = SessionManager.getInstance().generateSessionId();
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            } catch (Exception e) {
                logger.error("Error processing battle end", e);
            }
        });
    }

}
