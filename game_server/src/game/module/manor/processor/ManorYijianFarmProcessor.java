package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.BattleFormationDaoHelper;
import game.module.chapter.logic.BattleFormationManager;
import game.module.chapter.logic.PowerFormationManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.manor.bean.DbBattleGeneral;
import game.module.manor.bean.ManorBean;
import game.module.manor.dao.ManorCache;
import game.module.manor.dao.ManorDaoHelper;
import game.module.manor.dao.ManorTemplateCache;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.template.ManorTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.logic.PlayerInfoManager;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.*;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SManorYijianFarm.id, accessLimit = 200)
public class ManorYijianFarmProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ManorYijianFarmProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageBattle.C2SManorYijianFarm reqmsg = WsMessageBattle.C2SManorYijianFarm.parse(request);
        logger.info("manor yijian farm!player={},req={}", playerId, reqmsg);
        //粮草是否够
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), GameConfig.PLAYER.FOOD, 1)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CManorYijianFarm.msgCode, 1359);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //save manor formation
        WsMessageBase.IOFormationGeneralPos[] items = reqmsg.items;
        Map<Long, GeneralBean> generalMap = GeneralCache.getInstance().getHeros(playerId);
        BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
        if (battleFormation == null) {
            battleFormation = BattleFormationManager.getInstance().createBattleFormation(playerId);
        }
        int manorFormationIndex = 0;
        int mythic = reqmsg.mythic;
        if (mythic > 0) {
            //check mythic exist
            boolean containsTemplate = MythicalTemplateCache.getInstance().containsTemplate(mythic);
            if (!containsTemplate) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CManorYijianFarm.msgCode, 1420);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            Map<Integer, Integer> mythicsMap = battleFormation.getMythics();
            if (mythicsMap == null) {
                mythicsMap = new HashMap<>();
                battleFormation.setMythics(mythicsMap);
            }
            mythicsMap.put(manorFormationIndex, mythic);
        }
        //team
        Map<Integer, Long> formationMap = BattleFormationManager.getInstance().getFormationByType(manorFormationIndex, battleFormation);
        if (formationMap == null) {
            formationMap = new HashMap<>();
            BattleFormationManager.getInstance().setFormationByType(manorFormationIndex, battleFormation, formationMap);
        } else {
            formationMap.clear();
        }
        for (WsMessageBase.IOFormationGeneralPos aitem : items) {
            formationMap.put(aitem.pos, aitem.general_uuid);
        }
        //save formation
        if (battleFormation.getId() != null) {
            BattleFormationDaoHelper.asyncUpdateBattleFormation(battleFormation);
        } else {
            BattleFormationCache.getInstance().addBattleFormation(battleFormation);
            BattleFormationDaoHelper.asyncInsertBattleFormation(battleFormation);
        }
        //update player power
        int sumPower = 0;
        for (WsMessageBase.IOFormationGeneralPos aitem : items) {
            long general_uuid = aitem.general_uuid;
            GeneralBean generalBean = generalMap.get(general_uuid);
            if (generalBean != null) {
                sumPower += generalBean.getPower();
            }
        }
        PowerFormationManager.getInstance().refreshPowerFormation(playingRole.getPlayerBean().getPower(),sumPower,mythic,formationMap,playingRole);
        PlayerInfoManager.getInstance().saveMaxPower(playingRole, sumPower);
        //do
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        ManorBean.DbManorField manorField = manorBean.getManorField();
        List<ManorBean.DbManorEnemy> enemys = manorField.getEnemys();
        Map<Integer, Integer> dropItems = new HashMap<>();
        int foodCount = PlayerManager.getInstance().getHideCount(playerId, GameConfig.PLAYER.FOOD);
        int costFood = 0;
        for (ManorBean.DbManorEnemy dbManorEnemy : enemys) {
            Map<Integer, DbBattleGeneral> formationHeros = dbManorEnemy.getFormationHeros();
            //check all die
            boolean isAllDie = checkisAllDie(formationHeros);
            while (costFood < foodCount && !isAllDie) {
                //血量扣除40%
                WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateTemplateHp(playerId, battleFormation.getNormal(),
                        formationHeros);
                for (Map.Entry<Integer, DbBattleGeneral> aEntry : formationHeros.entrySet()) {
                    int formationPos = aEntry.getKey();
                    DbBattleGeneral dbBattleGeneral = aEntry.getValue();
                    int oldHp = dbBattleGeneral.getNowhp();
                    Long nowhp = battleRet.rhp.get(formationPos);
                    dbBattleGeneral.setNowhp(nowhp.intValue());
                    if (oldHp > 0 && dbBattleGeneral.getNowhp() <= 0) {
                        dbBattleGeneral.setNowhp(0);
                    }
                }
                costFood++;
                //check is all die
                isAllDie = checkisAllDie(formationHeros);
                if (isAllDie) {
                    //drop
                    Integer manorLevel = manorBean.getLevel();
                    ManorTemplate.ManorEnemyTemplate enemyTemplate = ManorTemplateCache.getInstance().getEnemyTemplate(manorLevel - 1);
                    for (RewardTemplateSimple rewardTemplateSimple : enemyTemplate.getREWARD()) {
                        putReward(dropItems, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                    }
                }
                putReward(dropItems, GameConfig.PLAYER.WOOD, 20);
            }
            if (costFood >= foodCount) {
                break;
            }
        }
        //cost food
        if (costFood > 0) {
            AwardUtils.changeRes(playingRole, GameConfig.PLAYER.FOOD, -costFood, LogConstants.MODULE_MANOR);
            Date now = new Date();
            manorField.setFarmEndTime(DateUtils.addSeconds(now, costFood * 2));
            //awards
            manorField.setMop(2);
            List<RewardTemplateSimple> rewards = new ArrayList<>();
            for (Map.Entry<Integer, Integer> aEntry : dropItems.entrySet()) {
                rewards.add(new RewardTemplateSimple(aEntry.getKey(), aEntry.getValue()));
            }
            manorField.setFarmRewards(rewards);
            //save bean
            ManorDaoHelper.asyncUpdateManor(manorBean);
        }
        //ret
        WsMessageBattle.S2CManorYijianFarm respmsg = new WsMessageBattle.S2CManorYijianFarm();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private boolean checkisAllDie(Map<Integer, DbBattleGeneral> formationHeros) {
        boolean isAllDie = true;
        for (DbBattleGeneral dbBattleGeneral : formationHeros.values()) {
            if (dbBattleGeneral.getNowhp() > 0) {
                isAllDie = false;
                break;
            }
        }
        return isAllDie;
    }

    private void putReward(Map<Integer, Integer> dropItems, Integer gsid, Integer count) {
        if (dropItems.containsKey(gsid)) {
            dropItems.put(gsid, dropItems.get(gsid) + count);
        } else {
            dropItems.put(gsid, count);
        }
    }

}
