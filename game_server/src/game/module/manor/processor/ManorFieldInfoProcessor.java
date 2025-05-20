package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.manor.bean.DbBattleGeneral;
import game.module.manor.bean.ManorBean;
import game.module.manor.dao.ManorCache;
import game.module.manor.dao.ManorDaoHelper;
import game.module.manor.logic.ManorManager;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageManor;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageManor.C2SManorFieldInfo.id, accessLimit = 200)
public class ManorFieldInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ManorFieldInfoProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("manor field info,playerId={}", playerId);
        WsMessageManor.S2CManorFieldInfo respmsg = new WsMessageManor.S2CManorFieldInfo();
        //一键farm结束
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        ManorBean.DbManorField dbManorField = manorBean.getManorField();
        if (dbManorField.getMop() == 2 && dbManorField.getFarmEndTime().before(new Date())) {
            logger.info("manor field mop end!player={}", playerId);
            respmsg.mop = 3;
            dbManorField.setMop(1);
            dbManorField.setFarmEndTime(null);
            ManorDaoHelper.asyncUpdateManor(manorBean);
            //send rewards
            if (dbManorField.getFarmRewards() != null && dbManorField.getFarmRewards().size() > 0) {
                respmsg.items = new ArrayList<>(dbManorField.getFarmRewards().size());
                for (RewardTemplateSimple rewardTemplateSimple : dbManorField.getFarmRewards()) {
                    AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_MANOR);
                    respmsg.items.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
                dbManorField.setFarmRewards(null);
            }
        } else {
            respmsg.mop = dbManorField.getMop();
        }
        //manor field boss and enemy reset
        ManorManager.getInstance().fieldReset(playingRole, false);
        //manor send food
        respmsg.btime = dbManorField.getBossRefreshTime().getTime();
        respmsg.etime = dbManorField.getEnemyRefreshTime().getTime();
        respmsg.blv = manorBean.getLevel();
        respmsg.elv = manorBean.getLevel();
        respmsg.boss = new WsMessageBase.IOManorFieldBoss(dbManorField.getBossState(), dbManorField.getBossLastDamage(),
                dbManorField.getBossMaxHp(), dbManorField.getBossNowHp());
        respmsg.enemy = new ArrayList<>(dbManorField.getEnemys().size());
        for (ManorBean.DbManorEnemy dbManorEnemy : dbManorField.getEnemys()) {
            WsMessageBase.IOManorFieldEnemy ioManorFieldEnemy = new WsMessageBase.IOManorFieldEnemy();
            if (dbManorEnemy.getBoxItem() != null && dbManorEnemy.getBoxItem().size() > 0 && dbManorEnemy.getHasBoxOpen() != null && !dbManorEnemy.getHasBoxOpen()) {
                ioManorFieldEnemy.boxItem = new ArrayList<>(dbManorEnemy.getBoxItem().size());
                for (RewardTemplateSimple rewardTemplateSimple : dbManorEnemy.getBoxItem()) {
                    ioManorFieldEnemy.boxItem.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
            }
            ioManorFieldEnemy.id = dbManorEnemy.getId();
            //是否死光
            Map<Integer, DbBattleGeneral> formationHeros = dbManorEnemy.getFormationHeros();
            boolean isAllDie = true;
            for (DbBattleGeneral dbBattleGeneral : formationHeros.values()) {
                if (dbBattleGeneral.getNowhp() > 0) {
                    isAllDie = false;
                    break;
                }
            }
            if (isAllDie) {
                ioManorFieldEnemy.state = 1;
            } else {
                ioManorFieldEnemy.state = 2;
            }
            respmsg.enemy.add(ioManorFieldEnemy);
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
