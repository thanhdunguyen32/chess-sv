package game.module.exped.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity_month.logic.ActivityMonthManager;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.exped.bean.ExpedBean;
import game.module.exped.bean.ExpedPlayer;
import game.module.exped.dao.ExpedCache;
import game.module.exped.dao.ExpedDaoHelper;
import game.module.exped.dao.ExpeditionTemplateCache;
import game.module.exped.logic.ExpedConstants;
import game.module.exped.logic.ExpedManager;
import game.module.item.logic.ItemConstants;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.pay.logic.ChargeInfoManager;
import game.module.template.ExpeditionTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.logic.PlayerManager;
import game.module.user.logic.ScrollAnnoManager;
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
import ws.WsMessageBigbattle;
import ws.WsMessageHall;

import java.util.*;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SExpedBattleEnd.id, accessLimit = 200)
public class ExpedBattleEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ExpedBattleEndProcessor.class);

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
        WsMessageBattle.C2SExpedBattleEnd reqmsg = WsMessageBattle.C2SExpedBattleEnd.parse(request);
        logger.info("exped battle end!player={},req={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if(!battleIdValid){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CExpedBattleEnd.msgCode,1468);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Map<Integer,WsMessageBase.IOBHurt> leftHpMap = new HashMap<>();
        Map<Integer,WsMessageBase.IOBHurt> rightHpMap = new HashMap<>();
        Map<Integer,Integer> leftHpPercMap = new HashMap<>();
        Map<Integer,Integer> rightHpPercMap = new HashMap<>();
        if(reqmsg.as != null && reqmsg.as.length>0){
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.as){
                rightHpMap.put(iobHurt.gsid,iobHurt);
                rightHpPercMap.put(iobHurt.born,iobHurt.hpperc);
            }
        }
        if(reqmsg.df != null && reqmsg.df.length>0){
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.df){
                leftHpMap.put(iobHurt.gsid,iobHurt);
                leftHpPercMap.put(iobHurt.born,iobHurt.hpperc);
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
            //init exped bean
            Map<Integer, Long> expeditionFormation = battleFormation.getExpedition();
            ExpedBean expedBean = ExpedCache.getInstance().getExped(playerId);
            if (expedBean == null) {
                expedBean = ExpedManager.getInstance().createExped(playerId);
            }
            Map<Long, Integer> hpMap = expedBean.getMyHp();
            if (hpMap == null) {
                hpMap = new HashMap<>();
                expedBean.setMyHp(hpMap);
            }
            Map<Integer, Integer> enemyHpMap = expedBean.getEnemyHp();
            //simulate battle
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateExped(playerId, battleFormation.getExpedition(), hpMap,
                    expedBean.getCheckpointEnemy(), enemyHpMap);
            logger.info("exped battle result:{}", battleRet);
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
            if (battleRet.ret.equals("win")) {
                //win add progress
                //save my hp
                for (Map.Entry<Integer, Long> aEntry : expeditionFormation.entrySet()) {
                    long generalUuid = aEntry.getValue();
                    int formationPos = aEntry.getKey();
                    Integer savedHp = 0;
                    if(leftHpPercMap.containsKey(formationPos)){
                        savedHp = leftHpPercMap.get(formationPos);
                    }else {
                        savedHp = battleRet.lper.get(formationPos);
                    }
                    hpMap.put(generalUuid, savedHp);
                }
                //clear enemy hp
                expedBean.setEnemyHp(null);
                //clear enemy
                expedBean.setCheckpointEnemy(null);
                //wish reset
                List<Integer> wishCount = expedBean.getWishCount();
                for (int i = 0; i < 3; i++) {
                    wishCount.set(i, 0);
                }
                //save bean
                if (expedBean.getId() == null) {
                    ExpedDaoHelper.asyncInsertExped(expedBean);
                    ExpedCache.getInstance().addExped(expedBean);
                } else {
                    ExpedDaoHelper.asyncUpdateExped(expedBean);
                }
                //drop
                int expedProgress = PlayerManager.getInstance().getOtherCount(playerId, ExpedConstants.EXPED_PROGRESS_MARK);
                ExpeditionTemplate expeditionTemplate = ExpeditionTemplateCache.getInstance().getExpeditionTemplate(expedProgress + 1);
                List<WsMessageBase.IORewardItem> dropItems = new ArrayList<>();
                for (RewardTemplateSimple rewardTemplateSimple : expeditionTemplate.getREWARD()) {
                    AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_EXPED);
                    dropItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
                //add ji tan bi
                AwardUtils.changeRes(playingRole, ItemConstants.JI_TAN_COIN, 4, LogConstants.MODULE_EXPED);
                dropItems.add(new WsMessageBase.IORewardItem(ItemConstants.JI_TAN_COIN, 4));
                //random reward
                List<WsMessageBase.IORewardItemSelect> rewardItems = new ArrayList<>();
                RandomDispatcher<Integer> rd = new RandomDispatcher<>();
                for (int i = 0; i < ExpedConstants.EXPED_REWARD.length; i++) {
                    rd.put(1, i);
                }
                int selectSize = 3;
                boolean isBwAddon = ChargeInfoManager.getInstance().isBwAddon(playerId);
                for (int i = 0; i < selectSize; i++) {
                    Integer aIndex = rd.randomRemove();
                    int[] rewardConfig = ExpedConstants.EXPED_REWARD[aIndex];
                    int gsid = rewardConfig[0];
                    int countMin = rewardConfig[1];
                    int countMax = rewardConfig[2];
                    int gsCount = RandomUtils.nextInt(countMin, countMax + 1);
                    boolean isSelect = i == 0;
                    if (isBwAddon) {
                        isSelect = true;
                    }
                    if (isSelect) {
                        AwardUtils.changeRes(playingRole, gsid, gsCount, LogConstants.MODULE_EXPED);
                    }
                    rewardItems.add(new WsMessageBase.IORewardItemSelect(gsid, gsCount, isSelect));
                }
                //save progress
                AwardUtils.changeRes(playingRole, ExpedConstants.EXPED_PROGRESS_MARK, 1, LogConstants.MODULE_EXPED);
                ActivityMonthManager.getInstance().expedFinish(playingRole, expedProgress + 1);
                //reset wish count
                AwardUtils.setRes(playingRole, ExpedConstants.EXPED_USE_COUNT_WISH_MARK, 0, true);
                //reset treat and relive count
                AwardUtils.setRes(playingRole, ExpedConstants.EXPED_USE_COUNT_TREAT_MARK, 0, true);
                AwardUtils.setRes(playingRole, ExpedConstants.EXPED_USE_COUNT_REVIVE_MARK, 0, true);
                //update mission progress
                int oldProgress = PlayerManager.getInstance().getOtherCount(playerId, MissionConstants.MARK_EXPED);
                if (expedProgress + 1 > oldProgress) {
                    AwardUtils.setRes(playingRole, MissionConstants.MARK_EXPED, expedProgress + 1, true);
                }
                //ret
                WsMessageBattle.S2CExpedBattleEnd respmsg = new WsMessageBattle.S2CExpedBattleEnd();
                respmsg.drop = dropItems;
                respmsg.result = battleRet;
                respmsg.reward = rewardItems;
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                //
                ScrollAnnoManager.getInstance().exped(playingRole, expedProgress + 1);
            } else {
                //save enemy hp
                if(enemyHpMap == null){
                    enemyHpMap = new HashMap<>();
                    expedBean.setEnemyHp(enemyHpMap);
                }
                ExpedPlayer checkpointEnemy = expedBean.getCheckpointEnemy();
                Set<Integer> formationPosAll;
                if(checkpointEnemy.getDbBattleset() != null) {
                    formationPosAll = checkpointEnemy.getDbBattleset().getTeam().keySet();
                }else if(checkpointEnemy.getBattlePlayerMap() != null){
                    formationPosAll = checkpointEnemy.getBattlePlayerMap().keySet();
                }else {
                    formationPosAll = null;
                }
                for (int formationPos : formationPosAll) {
                    Integer savedHp = 0;
                    if(rightHpPercMap.containsKey(formationPos)){
                        savedHp = rightHpPercMap.get(formationPos);
                    }else {
                        savedHp = battleRet.rper.get(formationPos);
                    }
                    enemyHpMap.put(formationPos, savedHp);
                }
                //clear my hp
                for (Map.Entry<Integer, Long> aEntry : expeditionFormation.entrySet()) {
                    long generalUuid = aEntry.getValue();
                    hpMap.put(generalUuid, 0);
                }
                //save bean
                if (expedBean.getId() == null) {
                    ExpedDaoHelper.asyncInsertExped(expedBean);
                    ExpedCache.getInstance().addExped(expedBean);
                } else {
                    ExpedDaoHelper.asyncUpdateExped(expedBean);
                }
//                //random reward
//                List<WsMessageBase.IORewardItemSelect> rewardItems = new ArrayList<>();
//                RandomDispatcher<Integer> rd = new RandomDispatcher<>();
//                for (int i = 0; i < ExpedConstants.EXPED_REWARD.length; i++) {
//                    rd.put(1, i);
//                }
//                int selectSize = 3;
//                for (int i = 0; i < selectSize; i++) {
//                    Integer aIndex = rd.randomRemove();
//                    int[] rewardConfig = ExpedConstants.EXPED_REWARD[aIndex];
//                    int gsid = rewardConfig[0];
//                    int countMin = rewardConfig[1];
//                    int countMax = rewardConfig[2];
//                    int gsCount = RandomUtils.nextInt(countMin, countMax + 1);
//                    boolean isSelect = i == 0;
//                    if (isSelect) {
//                        AwardUtils.changeRes(playingRole, gsid, gsCount, LogConstants.MODULE_EXPED);
//                    }
//                    rewardItems.add(new WsMessageBase.IORewardItemSelect(gsid, gsCount, isSelect));
//                }
                //ret
                WsMessageBattle.S2CExpedBattleEnd respmsg = new WsMessageBattle.S2CExpedBattleEnd();
                respmsg.result = battleRet;
//                respmsg.reward = rewardItems;
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            }
        });
    }

}
