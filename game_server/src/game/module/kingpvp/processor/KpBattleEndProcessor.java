package game.module.kingpvp.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity_month.logic.ActivityMonthManager;
import game.module.award.logic.AwardUtils;
import game.module.battle.dao.BattlePlayerBase;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.bean.DbBattleset;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.logic.BattleFormationManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.logic.GeneralManager;
import game.module.item.logic.ItemConstants;
import game.module.item.logic.ItemManager;
import game.module.kingpvp.bean.KingPvp;
import game.module.kingpvp.bean.KingPvpPlayer;
import game.module.kingpvp.dao.KingPvpCache;
import game.module.kingpvp.dao.KingPvpDaoHelper;
import game.module.kingpvp.logic.KingPvpManager;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.offline.processor.GetOtherPlayerInfoProcessor;
import game.module.pay.logic.ChargeInfoManager;
import game.module.pvp.logic.PvpConstants;
import game.module.season.dao.SeasonCache;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageBattle.C2SKpBattleEnd.id, accessLimit = 250)
public class KpBattleEndProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(KpBattleEndProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageBattle.C2SKpBattleEnd reqmsg = WsMessageBattle.C2SKpBattleEnd.parse(request);
        logger.info("king pvp battle end,playerId={}", playerId);
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
            KingPvp kingPvp = KingPvpCache.getInstance().getKingPvp(playerId);
            //do
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            //simulate battle
            int myIndex = 0;
            int enemyIndex = 0;
            KingPvpPlayer tmpTargetPlayer = kingPvp.getTmpTargetPlayer();
            Map<Integer, Integer> myHpMap = new HashMap<>();
            Map<Integer, Integer> enemyHpMap = new HashMap<>();
            List<WsMessageBase.IOBattleResult> resultlist = new ArrayList<>();
            int myKpFormationNum = KingPvpManager.getInstance().getMyKpFormationNum(kingPvp.getStage());
            //robot
            boolean finalRet = false;
            if (tmpTargetPlayer.getBattlePlayerMap() != null) {
                while (myIndex < 3) {
                    String myKpFormation = "kingpvp" + (myIndex + 1);
                    int myFormationIndex = ArrayUtils.indexOf(BattleFormationManager.FormationTypeNameMap, myKpFormation);
                    Map<Integer, Long> myKpFormationSet = BattleFormationManager.getInstance().getFormationByType(myFormationIndex, battleFormation);
                    if (myIndex + 1 > myKpFormationNum || myKpFormationSet == null || myKpFormationSet.size() == 0) {
                        myIndex++;
                        continue;
                    }
                    Map<Integer, BattlePlayerBase> enemyFormationMap = tmpTargetPlayer.getBattlePlayerMap();
                    WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateKingPvpRobot(playerId, myKpFormationSet, myHpMap,
                            enemyFormationMap, enemyHpMap);
                    logger.info("king pvp battle result:{}", battleRet);
                    battleRet.left = new WsMessageBase.IOBattleRetSide();
                    battleRet.right = new WsMessageBase.IOBattleRetSide();
                    battleRet.left.info = new WsMessageBase.IOBattleRecordInfo(playingRole.getPlayerBean().getName(), playingRole.getPlayerBean().getLevel(),
                            playingRole.getPlayerBean().getIconid(),playingRole.getPlayerBean().getHeadid(),playingRole.getPlayerBean().getFrameid());
                    battleRet.right.info = new WsMessageBase.IOBattleRecordInfo(tmpTargetPlayer.getRname(),tmpTargetPlayer.getLevel(),tmpTargetPlayer.getIconid()
                            ,tmpTargetPlayer.getHeadid(),tmpTargetPlayer.getFrameid());
                    battleRet.left.set = new WsMessageBase.IOBattleRetSet();
                    battleRet.right.set = new WsMessageBase.IOBattleRetSet();
                    battleRet.left.set.index = myIndex;
                    battleRet.right.set.index = enemyIndex;
                    Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
                    //my formation
                    battleRet.left.set.team = new HashMap<>();
                    for (Map.Entry<Integer, Long> aEntry : myKpFormationSet.entrySet()) {
                        GeneralBean generalBean = generalAll.get(aEntry.getValue());
                        //可能英雄已经被删除
                        if (generalBean == null) {
                            continue;
                        }
                        try {
                            generalBean = GetOtherPlayerInfoProcessor.generalTmpUpgrade(generalBean);
                        } catch (Exception e) {
                            logger.error("",e);
                        }
                        WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
                        battleRet.left.set.team.put(aEntry.getKey(), ioGeneralBean);
                    }
                    //enemy team formation
                    battleRet.right.set.team = new HashMap<>();
                    for (Map.Entry<Integer, BattlePlayerBase> aEntry : enemyFormationMap.entrySet()) {
                        BattlePlayerBase battlePlayer = aEntry.getValue();
                        WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(battlePlayer);
                        Integer formationPos = aEntry.getKey();
                        battleRet.right.set.team.put(formationPos, ioGeneralBean);
                    }
                    resultlist.add(battleRet);
                    //result
                    if (battleRet.ret.equals("win")) {
                        finalRet = true;
                        break;
                    } else {
                        myIndex++;
                        //血量保存
                        enemyHpMap = battleRet.rper;
                    }
                }
            } else {
                while (myIndex < 3 && enemyIndex < 3) {
                    String myKpFormation = "kingpvp" + (myIndex + 1);
                    int myFormationIndex = ArrayUtils.indexOf(BattleFormationManager.FormationTypeNameMap, myKpFormation);
                    Map<Integer, Long> myKpFormationSet = BattleFormationManager.getInstance().getFormationByType(myFormationIndex, battleFormation);
                    if (myIndex + 1 > myKpFormationNum || myKpFormationSet == null || myKpFormationSet.size()==0) {
                        myIndex++;
                        continue;
                    }
                    List<DbBattleset> dbBattlesetList = tmpTargetPlayer.getDbBattlesetList();
                    if (enemyIndex >= dbBattlesetList.size() || dbBattlesetList.get(enemyIndex).getTeam() == null || dbBattlesetList.get(enemyIndex).getTeam().size() == 0) {
                        enemyIndex++;
                        continue;
                    }
                    DbBattleset enemyBattleset = dbBattlesetList.get(enemyIndex);
                    WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateKingPvpPlayer(playerId, myKpFormationSet, myHpMap,
                            enemyBattleset, enemyHpMap);
                    logger.info("king pvp battle result:{}", battleRet);
                    battleRet.left = new WsMessageBase.IOBattleRetSide();
                    battleRet.right = new WsMessageBase.IOBattleRetSide();
                    //info
                    battleRet.left.set = new WsMessageBase.IOBattleRetSet();
                    battleRet.right.set = new WsMessageBase.IOBattleRetSet();
                    battleRet.left.info = new WsMessageBase.IOBattleRecordInfo(playingRole.getPlayerBean().getName(), playingRole.getPlayerBean().getLevel(),
                            playingRole.getPlayerBean().getIconid(),playingRole.getPlayerBean().getHeadid(),playingRole.getPlayerBean().getFrameid());
                    battleRet.right.info = new WsMessageBase.IOBattleRecordInfo(tmpTargetPlayer.getRname(),tmpTargetPlayer.getLevel(),tmpTargetPlayer.getIconid()
                            ,tmpTargetPlayer.getHeadid(),tmpTargetPlayer.getFrameid());
                    battleRet.left.set.index = myIndex;
                    battleRet.right.set.index = enemyIndex;
                    Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
                    //my formation
                    battleRet.left.set.team = new HashMap<>();
                    for (Map.Entry<Integer, Long> aEntry : myKpFormationSet.entrySet()) {
                        GeneralBean generalBean = generalAll.get(aEntry.getValue());
                        //可能英雄已经被删除
                        if (generalBean == null) {
                            continue;
                        }
                        try {
                            generalBean = GetOtherPlayerInfoProcessor.generalTmpUpgrade(generalBean);
                        } catch (Exception e) {
                            logger.error("",e);
                        }
                        WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
                        battleRet.left.set.team.put(aEntry.getKey(), ioGeneralBean);
                    }
                    //enemy team formation
                    battleRet.right.set.team = new HashMap<>();
                    Map<Integer, GeneralBean> battlesetTeam = enemyBattleset.getTeam();
                    for (Map.Entry<Integer, GeneralBean> aEntry : battlesetTeam.entrySet()) {
                        GeneralBean generalBean = aEntry.getValue();
                        if (generalBean == null) {
                            continue;
                        }
                        WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
                        Integer formationPos = aEntry.getKey();
                        battleRet.right.set.team.put(formationPos, ioGeneralBean);
                    }
                    resultlist.add(battleRet);
                    //result
                    if (battleRet.ret.equals("win")) {
                        enemyIndex++;
                        myHpMap = battleRet.lper;
                        enemyHpMap.clear();
                    } else {
                        myIndex++;
                        //血量保存
                        enemyHpMap = battleRet.rper;
                        myHpMap.clear();
                    }
                }
                if (myIndex >= 3) {
                    finalRet = false;
                } else {
                    finalRet = true;
                }
            }
            //战斗结果修正
            WsMessageBase.IOBattleResult ioBattleResult = resultlist.get(0);
            if (battleResult != null) {
                ioBattleResult.ret = battleResult;
                finalRet = battleResult.equals("win");
            }
            for (WsMessageBase.IOBattleReportItem ioBattleReportItem : ioBattleResult.report.left) {
                if (leftHpMap.get(ioBattleReportItem.gsid) != null) {
                    WsMessageBase.IOBHurt iobHurt = leftHpMap.get(ioBattleReportItem.gsid);
                    ioBattleReportItem.hurm = iobHurt.hurm;
                    ioBattleReportItem.heal = iobHurt.heal;
                }
            }
            for (WsMessageBase.IOBattleReportItem ioBattleReportItem : ioBattleResult.report.right) {
                if (rightHpMap.get(ioBattleReportItem.gsid) != null) {
                    WsMessageBase.IOBHurt iobHurt = rightHpMap.get(ioBattleReportItem.gsid);
                    ioBattleReportItem.hurm = iobHurt.hurm;
                    ioBattleReportItem.heal = iobHurt.heal;
                }
            }
            //结果保存
            int starChange = 0;
            Integer isHide = tmpTargetPlayer.getHide();
            //定位赛
            if (kingPvp.getLocate() == null || kingPvp.getLocate().size() < 5) {
                if (kingPvp.getLocate() == null) {
                    List<Integer> locateList = new ArrayList<>();
                    kingPvp.setLocate(locateList);
                }
                kingPvp.getLocate().add(finalRet ? 1 : 0);
                if (kingPvp.getLocate().size() == 5) {
                    //定位赛结束
                    int winNum = 0;
                    for (int locateRet : kingPvp.getLocate()) {
                        if (locateRet > 0) {
                            winNum++;
                        }
                    }
                    int locateStage = 101 + winNum;
                    locateStage = Math.min(105, locateStage);
                    kingPvp.setStage(locateStage);
                    kingPvp.setStar(0);
                    kingPvp.setHstage(locateStage);
                    KingPvpManager.getInstance().stageChange(0, locateStage, kingPvp);
                }
            } else {
                starChange = finalRet ? (isHide > 0 ? 2 : 1) : -1;
                //
                Integer stage = kingPvp.getStage();
                int stageBig = stage / 100;
                int stageSmall = stage % 100;
                if (stageBig >= 6) {//当玩家段位晋升到大师后之后，不再有段位区分，星级可以无限堆叠
                    int newStar = kingPvp.getStar() + starChange;
                    newStar = Math.max(newStar, 0);
                    kingPvp.setStar(newStar);
                } else if (stageSmall == 5 && kingPvp.getStar() == 5) {//晋级赛
                    starChange = 0;
                    if (kingPvp.getPromotion() == null) {
                        List<Integer> promotionList = new ArrayList<>();
                        kingPvp.setPromotion(promotionList);
                    }
                    kingPvp.getPromotion().add(finalRet ? 1 : 0);
                    //5 5局3胜，<5，3局2胜
                    int promotionNum = getPromotionNum(stage);
                    if (kingPvp.getPromotion().size() >= promotionNum) {
                        int needWinNum = promotionNum / 2 + 1;
                        int myWinNum = 0;
                        for (int aret : kingPvp.getPromotion()) {
                            if (aret > 0) {
                                myWinNum++;
                            }
                        }
                        if (myWinNum >= needWinNum) {
                            //晋级
                            int nextStage = getNextBigStage(stage);
                            kingPvp.setStage(nextStage);
                            kingPvp.setStar(0);
                            KingPvpManager.getInstance().stageChange(stage, nextStage, kingPvp);
                            starChange = 0;
                            if (kingPvp.getHstage() == null || kingPvp.getHstage() < nextStage) {
                                kingPvp.setHstage(nextStage);
                            }
                        } else {//未达成晋升条件，则损失3颗星级
                            kingPvp.setStar(2);
                            starChange = -3;
                        }
                        kingPvp.setPromotion(null);
                    }
                } else {
                    int newStar = kingPvp.getStar() + starChange;
                    if (newStar > 5) {//小段位升级
                        int nextStage = getNextSmallStage(stage);
                        if (stage == nextStage) {
                            newStar = 5;
                        } else {
                            kingPvp.setStage(nextStage);
                            KingPvpManager.getInstance().stageChange(stage, nextStage, kingPvp);
                            newStar -= 6;
                            if (kingPvp.getHstage() == null || kingPvp.getHstage() < nextStage) {
                                kingPvp.setHstage(nextStage);
                            }
                        }
                    } else if (newStar < 0) {//小段位降级
                        int prevStage = getPrevSmallStage(stage);
                        if (stage == prevStage) {
                            newStar = 0;
                        } else {
                            kingPvp.setStage(prevStage);
                            KingPvpManager.getInstance().stageChange(stage, prevStage, kingPvp);
                            newStar = 5;
                        }
                    }
                    kingPvp.setStar(newStar);
                }
            }
            //ret
            WsMessageBattle.S2CKpBattleEnd respmsg = new WsMessageBattle.S2CKpBattleEnd();
            //award
            List<WsMessageBase.IORewardItemSelect> rewardItems = new ArrayList<>();
            RandomDispatcher<Integer> rd = new RandomDispatcher<>();
            for (int i = 0; i < PvpConstants.KING_PVP_REWARD.length; i++) {
                rd.put(PvpConstants.KING_PVP_REWARD[i][3], i);
            }
            int selectSize = 3;
            boolean isBwAddon = ChargeInfoManager.getInstance().isBwAddon(playerId);
            for (int i = 0; i < selectSize; i++) {
                Integer aIndex = rd.randomRemove();
                int[] rewardConfig = PvpConstants.KING_PVP_REWARD[aIndex];
                int gsid = rewardConfig[0];
                int countMin = rewardConfig[1];
                int countMax = rewardConfig[2];
                int gsCount = RandomUtils.nextInt(countMin, countMax + 1) * (isHide > 0 ? 2 : 1);
                boolean isSelect = i == 0;
                if (isBwAddon) {
                    isSelect = true;
                }
                if (isSelect) {
                    AwardUtils.changeRes(playingRole, gsid, gsCount, LogConstants.MODULE_KING_PVP);
                }
                rewardItems.add(new WsMessageBase.IORewardItemSelect(gsid, gsCount, isSelect));
            }
            //clear enemy
            kingPvp.setTmpTargetPlayer(null);
            //save 2 db
            KingPvpDaoHelper.asyncUpdateKingPvp(kingPvp);
            //mission mark
            //完成1次战斗
            AwardUtils.changeRes(playingRole, 90319, 1, LogConstants.MODULE_KING_PVP);
            //累积胜利1次
            if (finalRet) {
                AwardUtils.changeRes(playingRole, 90320, 1, LogConstants.MODULE_KING_PVP);
            }
            //与隐藏对手战斗1次
            if (isHide > 0) {
                AwardUtils.changeRes(playingRole, 90321, 1, LogConstants.MODULE_KING_PVP);
            }
            //月度任务
            ActivityMonthManager.getInstance().pvpBattleEnd(playingRole,finalRet,true);
            //成就任务
            AwardUtils.changeRes(playingRole, MissionConstants.KING_PVP_ATTACK, 1, LogConstants.MODULE_KING_PVP);
            //ret
        if (kingPvp.getStage() > 0) {
            respmsg.stageinfo = new WsMessageBase.IOStageInfo(starChange, kingPvp.getStage(), kingPvp.getStar());
        }
        respmsg.resultlist = resultlist;
        respmsg.reward = rewardItems;
            //send
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });


    }

    private int getPromotionNum(int stage) {
        int stageBig = stage / 100;
        if (stageBig >= 5) {
            return 5;
        } else {
            return 3;
        }
    }

    private int getNextBigStage(int stage) {
        int stageBig = stage / 100;
        return (stageBig + 1) * 100 + 1;
    }

    private int getNextSmallStage(int stage) {
        int stageBig = stage / 100;
        int ret = stage + 1;
        return Math.min(ret, stageBig * 100 + 5);
    }

    private int getPrevSmallStage(int stage) {
        int stageBig = stage / 100;
        int ret = stage - 1;
        return Math.max(ret, stageBig * 100 + 1);
    }

}
