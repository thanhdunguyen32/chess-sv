package game.module.exped.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.battle.dao.BattlePlayer;
import game.module.battle.dao.BattlePlayerBase;
import game.module.chapter.bean.DbBattleset;
import game.module.exped.bean.ExpedBean;
import game.module.exped.bean.ExpedPlayer;
import game.module.exped.dao.ExpedCache;
import game.module.exped.dao.ExpedDaoHelper;
import game.module.exped.logic.ExpedConstants;
import game.module.exped.logic.ExpedManager;
import game.module.exped.logic.FormationRobotManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.hero.logic.GeneralManager;
import game.module.item.logic.ItemManager;
import game.module.mythical.processor.MythicalListProcessor;
import game.module.template.GeneralTemplate;
import game.module.user.logic.PlayerHeadManager;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;

import java.util.HashMap;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageBattle.C2SExpedBattleInfo.id, accessLimit = 200)
public class ExpedInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ExpedInfoProcessor.class);

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
        logger.info("exped battle info,playerId={}", playerId);
        //exped reset check
        ExpedManager.getInstance().expedResetCheck(playingRole);
        //ret
        ExpedBean expedBean = ExpedCache.getInstance().getExped(playerId);
        //generate enemy
        boolean needUpdate = false;
        if (expedBean == null) {
            needUpdate = true;
            expedBean = ExpedManager.getInstance().createExped(playerId);
        }
        if (expedBean.getCheckpointEnemy() == null) {
            needUpdate = true;
            ExpedPlayer expedPlayer = ExpedManager.getInstance().generateCheckpointEnemy(playingRole);
            expedBean.setCheckpointEnemy(expedPlayer);
        }
        if (needUpdate) {
            if (expedBean.getId() == null) {
                ExpedDaoHelper.asyncInsertExped(expedBean);
                ExpedCache.getInstance().addExped(expedBean);
            } else {
                ExpedDaoHelper.asyncUpdateExped(expedBean);
            }
        }
        //ret
        WsMessageBattle.S2CExpedBattleInfo respmsg = new WsMessageBattle.S2CExpedBattleInfo();
        respmsg.wish = expedBean.getWishCount();
        if (expedBean.getMyHp() != null) {
            respmsg.hp = expedBean.getMyHp();
        }
        respmsg.mapkey = SessionManager.getInstance().generateSessionId();
        WsMessageBase.IOExpedPlayer ioExpedPlayer = new WsMessageBase.IOExpedPlayer();
        respmsg.map = ioExpedPlayer;
        //ret
        ExpedPlayer checkpointEnemy = expedBean.getCheckpointEnemy();
        ioExpedPlayer.rname = checkpointEnemy.getRname();
        ioExpedPlayer.level = checkpointEnemy.getLevel();
        ioExpedPlayer.iconid = checkpointEnemy.getIconid();
        ioExpedPlayer.headid = checkpointEnemy.getHeadid();
        ioExpedPlayer.frameid = checkpointEnemy.getFrameid();
        ioExpedPlayer.power = checkpointEnemy.getPower();
        //enemy hp
        Map<Integer, Integer> enemyHp = null;
        if (expedBean.getEnemyHp() != null) {
            enemyHp = expedBean.getEnemyHp();
        }
        if (checkpointEnemy.getBattlePlayerMap() != null) {
            Map<Integer, BattlePlayerBase> battlePlayerMap = checkpointEnemy.getBattlePlayerMap();
            ioExpedPlayer.battleset = new WsMessageBase.IOBattlesetEnemy();
            ioExpedPlayer.battleset.team = new HashMap<>();
            for (Map.Entry<Integer, BattlePlayerBase> aEntry : battlePlayerMap.entrySet()) {
                BattlePlayerBase battlePlayer = aEntry.getValue();
                WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(battlePlayer);
                Integer formationPos = aEntry.getKey();
                ioExpedPlayer.battleset.team.put(formationPos, ioGeneralBean);
                //hp percent
                if (enemyHp != null && enemyHp.containsKey(formationPos)) {
                    ioGeneralBean.hppercent = enemyHp.get(formationPos);
                }
            }
        } else if (checkpointEnemy.getDbBattleset() != null) {
            DbBattleset dbBattleset = checkpointEnemy.getDbBattleset();
            ioExpedPlayer.battleset = new WsMessageBase.IOBattlesetEnemy();
            if (dbBattleset.getMythic() != null) {
                WsMessageBase.IOMythicalAnimal ioMythicalAnimal = new WsMessageBase.IOMythicalAnimal();
                ioExpedPlayer.battleset.mythic = ioMythicalAnimal;
                MythicalListProcessor.buildIOMythicalAnimal(dbBattleset.getMythic(), 101, ioMythicalAnimal);
            }
            ioExpedPlayer.battleset.team = new HashMap<>();
            Map<Integer, GeneralBean> battlesetTeam = dbBattleset.getTeam();
            for (Map.Entry<Integer, GeneralBean> aEntry : battlesetTeam.entrySet()) {
                GeneralBean generalBean = aEntry.getValue();
                if (generalBean == null) {
                    continue;
                }
                WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
                Integer formationPos = aEntry.getKey();
                ioExpedPlayer.battleset.team.put(formationPos, ioGeneralBean);
                //hp percent
                if (enemyHp != null && enemyHp.containsKey(formationPos)) {
                    ioGeneralBean.hppercent = enemyHp.get(formationPos);
                }
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
