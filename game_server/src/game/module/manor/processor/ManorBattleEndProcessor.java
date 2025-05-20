package game.module.manor.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.log.constants.LogConstants;
import game.module.manor.bean.DbBattleGeneral;
import game.module.manor.bean.ManorBean;
import game.module.manor.dao.ManorCache;
import game.module.manor.dao.ManorDaoHelper;
import game.module.manor.dao.ManorTemplateCache;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.template.ManorTemplate;
import game.module.template.RewardTemplateSimple;
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
@MsgCodeAnn(msgcode = WsMessageBattle.C2SManorBattleEnd.id, accessLimit = 200)
public class ManorBattleEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ManorBattleEndProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageBattle.C2SManorBattleEnd reqmsg = WsMessageBattle.C2SManorBattleEnd.parse(request);
        logger.info("manor battle end!player={},req={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if(!battleIdValid){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CManorBattleEnd.msgCode,1468);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Map<Integer,WsMessageBase.IOBHurt> leftHpMap = new HashMap<>();
        Map<Integer,WsMessageBase.IOBHurt> rightHpMap = new HashMap<>();
        Map<Integer, Long> leftHpValMap = new HashMap<>();
        Map<Integer, Long> rightHpValMap = new HashMap<>();
        if(reqmsg.as != null && reqmsg.as.length>0){
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.as){
                rightHpMap.put(iobHurt.gsid,iobHurt);
                rightHpValMap.put(iobHurt.born, iobHurt.hp);
            }
        }
        if(reqmsg.df != null && reqmsg.df.length>0){
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.df){
                leftHpMap.put(iobHurt.gsid,iobHurt);
                leftHpValMap.put(iobHurt.born, iobHurt.hp);
            }
        }
        int enemy_index = reqmsg.index;
        //save enemy hp
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        ManorBean.DbManorField manorField = manorBean.getManorField();
        if (enemy_index >= manorField.getEnemys().size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CManorBattleEnd.msgCode, 1386);
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
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            WsMessageBase.IOBattleResult battleRet;
            if (enemy_index == -1) {//boss info
                Map<Integer, DbBattleGeneral> bossFormationHeros = manorField.getBossFormationHeros();
                Map<Integer, Long> myFormation = battleFormation.getNormal();
                Integer tmpFriendId = manorBean.getTmpFriendId();
                int leftPlayerId = playerId;
                if (tmpFriendId != null && tmpFriendId > 0) {
                    BattleFormation battleFormationFriend = PlayerOfflineManager.getInstance().getBattleFormation(tmpFriendId);
                    myFormation = battleFormationFriend.getNormal();
                    leftPlayerId = tmpFriendId;
                }
                battleRet = BattleSimulator.getInstance().simulateTemplateHp(leftPlayerId, myFormation, bossFormationHeros);
                logger.info("manor boss battle result:{}", battleRet);
            } else {
                ManorBean.DbManorEnemy enemyMap = manorField.getEnemys().get(enemy_index);
                Map<Integer, DbBattleGeneral> formationHeros = enemyMap.getFormationHeros();
                battleRet = BattleSimulator.getInstance().simulateTemplateHp(playerId, battleFormation.getNormal(), formationHeros);
                logger.info("manor battle result:{}", battleRet);
            }
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
            List<WsMessageBase.IORewardItem> dropItems = null;
            int isAllkill = 0;
            if (enemy_index == -1) {//boss
                //血量扣除
                int bossOldHp = manorField.getBossNowHp();
                int lastDamage = 0;
                if (reqmsg.df != null && reqmsg.df.length > 0) {
                    long bossNowHp = ManorBattleEndProcessor.getEnemyNowHp(reqmsg.as);
                    lastDamage = (int) Math.max(manorField.getBossMaxHp() - bossNowHp, 0);
                } else {
                    lastDamage = (int) getMyHurm(battleRet);
                }
                manorField.setBossNowHp(bossOldHp - lastDamage);
                manorField.setBossLastDamage(lastDamage);
                //木材奖励
                AwardUtils.changeRes(playingRole, GameConfig.PLAYER.WOOD, 20, LogConstants.MODULE_MANOR);
                dropItems = new ArrayList<>();
                dropItems.add(new WsMessageBase.IORewardItem(GameConfig.PLAYER.WOOD, 20));
                //die
                if (bossOldHp > 0 && manorField.getBossNowHp() <= 0) {
                    manorField.setBossNowHp(0);
                    manorField.setBossState(1);
                    isAllkill = 1;
                    //drop
                    Integer manorLevel = manorBean.getLevel();
                    ManorTemplate.ManorEnemyTemplate bossTemplate = ManorTemplateCache.getInstance().getBossTemplate(manorLevel - 1);
                    for (RewardTemplateSimple rewardTemplateSimple : bossTemplate.getREWARD()) {
                        AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_MANOR);
                        dropItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                    }
                }
                //save bean
                ManorDaoHelper.asyncUpdateManor(manorBean);
            } else {
                ManorBean.DbManorEnemy dbManorEnemy = manorField.getEnemys().get(enemy_index);
                //血量扣除
                Map<Integer, DbBattleGeneral> formationHeros = dbManorEnemy.getFormationHeros();
                for (Map.Entry<Integer, DbBattleGeneral> aEntry : formationHeros.entrySet()) {
                    int formationPos = aEntry.getKey();
                    DbBattleGeneral dbBattleGeneral = aEntry.getValue();
                    int oldHp = dbBattleGeneral.getNowhp();
                    long nowhp = rightHpValMap.containsKey(formationPos) ? rightHpValMap.get(formationPos) : battleRet.rhp.get(formationPos);
                    dbBattleGeneral.setNowhp((int) nowhp);
                    if (oldHp > 0 && dbBattleGeneral.getNowhp() <= 0) {
                        dbBattleGeneral.setNowhp(0);
                    }
                }
                //check all die
                boolean isAllDie = true;
                for (DbBattleGeneral dbBattleGeneral : formationHeros.values()) {
                    if (dbBattleGeneral.getNowhp() > 0) {
                        isAllDie = false;
                        break;
                    }
                }
                if (isAllDie) {
                    isAllkill = 1;
                    //drop
                    Integer manorLevel = manorBean.getLevel();
                    ManorTemplate.ManorEnemyTemplate enemyTemplate = ManorTemplateCache.getInstance().getEnemyTemplate(manorLevel - 1);
                    dropItems = new ArrayList<>();
                    for (RewardTemplateSimple rewardTemplateSimple : enemyTemplate.getREWARD()) {
                        AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_MANOR);
                        dropItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                    }
                }
                //save bean
                ManorDaoHelper.asyncUpdateManor(manorBean);
            }
            //ret
            WsMessageBattle.S2CManorBattleEnd respmsg = new WsMessageBattle.S2CManorBattleEnd();
            respmsg.kill = isAllkill;
            respmsg.result = battleRet;
            respmsg.reward = dropItems;
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });
    }

    public static long getClientHurmSum(WsMessageBase.IOBHurt[] leftlist) {
        long hpSum = 0;
        for (WsMessageBase.IOBHurt iobHurt : leftlist){
            hpSum += iobHurt.hurm;
        }
        return Math.max(hpSum,0);
    }

    public static long getEnemyNowHp(WsMessageBase.IOBHurt[] rightlist) {
        long hpSum = 0;
        for (WsMessageBase.IOBHurt iobHurt : rightlist){
            hpSum += iobHurt.hp;
        }
        return Math.max(hpSum,0);
    }

    private long getMyHurm(WsMessageBase.IOBattleResult battleRet) {
        long ret = 0;
        for (WsMessageBase.IOBattleReportItem ioBattleReportItem : battleRet.report.left) {
            ret += ioBattleReportItem.hurm;
        }
        return ret;
    }

}
