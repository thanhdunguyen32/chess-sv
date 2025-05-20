package game.module.kingpvp.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.battle.dao.BattlePlayerBase;
import game.module.chapter.bean.DbBattleset;
import game.module.hero.bean.GeneralBean;
import game.module.hero.logic.GeneralManager;
import game.module.kingpvp.bean.KingPvp;
import game.module.kingpvp.bean.KingPvpPlayer;
import game.module.kingpvp.dao.KingPvpCache;
import game.module.kingpvp.dao.KingPvpDaoHelper;
import game.module.kingpvp.logic.KingPvpManager;
import game.module.mythical.processor.MythicalListProcessor;
import game.module.offline.processor.GetOtherPlayerInfoProcessor;
import game.module.season.dao.SeasonCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessagePvp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessagePvp.C2SKpSearchEnemy.id, accessLimit = 250)
public class KpSearchEnemyProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(KpSearchEnemyProcessor.class);

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
        WsMessagePvp.C2SKpSearchEnemy reqmsg = WsMessagePvp.C2SKpSearchEnemy.parse(request);
        logger.info("king pvp search enemy,playerId={}", playerId);
        //是否赛季
        Integer season = SeasonCache.getInstance().getBattleSeason().getSeason();
        if (season == 4) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessagePvp.S2CKpSearchEnemy.msgCode, 1477);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        int force = reqmsg.force;
        KingPvp kingPvp = KingPvpCache.getInstance().getKingPvp(playerId);
        if (kingPvp == null) {
            kingPvp = KingPvpManager.getInstance().createKingPvp(playerId);
            KingPvpCache.getInstance().addKingPvp(kingPvp);
            KingPvpDaoHelper.asyncInsertKingPvp(kingPvp);
        }
        //生成对手
        if (kingPvp.getTmpTargetPlayer() == null || force == 1) {
            KingPvpPlayer kingPvpPlayer = KingPvpManager.getInstance().searchEnemy(playingRole);
            kingPvp.setTmpTargetPlayer(kingPvpPlayer);
        }
        //ret
        WsMessagePvp.S2CKpSearchEnemy respmsg = new WsMessagePvp.S2CKpSearchEnemy();
        KingPvpPlayer targetPlayer = kingPvp.getTmpTargetPlayer();
        if (targetPlayer.getId() != null && targetPlayer.getId() > 0) {
            KingPvp targetKingPvp = KingPvpCache.getInstance().getKingPvp(targetPlayer.getId());
            if (targetKingPvp != null) {
                respmsg.stage = targetKingPvp.getStage();
                respmsg.star = targetKingPvp.getStar();
            }
        }
        respmsg.hide = targetPlayer.getHide();
        respmsg.rid = targetPlayer.getId();
        respmsg.rname = targetPlayer.getRname();
        respmsg.power = targetPlayer.getPower();
        respmsg.iconid = targetPlayer.getIconid();
        respmsg.frameid = targetPlayer.getFrameid();
        respmsg.level = targetPlayer.getLevel();
        respmsg.vip = targetPlayer.getVip();
        respmsg.time = targetPlayer.getCreateTime();
        respmsg.battleset = new ArrayList<>();
        if (targetPlayer.getBattlePlayerMap() != null) {
            Map<Integer, BattlePlayerBase> battlePlayerMap = targetPlayer.getBattlePlayerMap();
            WsMessageBase.IOBattlesetEnemy ioBattlesetEnemy = new WsMessageBase.IOBattlesetEnemy();
            ioBattlesetEnemy.team = new HashMap<>();
            for (Map.Entry<Integer, BattlePlayerBase> aEntry : battlePlayerMap.entrySet()) {
                BattlePlayerBase battlePlayer = aEntry.getValue();
                WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(battlePlayer);
                Integer formationPos = aEntry.getKey();
                ioBattlesetEnemy.team.put(formationPos, ioGeneralBean);
            }
            respmsg.battleset.add(ioBattlesetEnemy);
        } else if (targetPlayer.getDbBattlesetList() != null) {
            for (DbBattleset dbBattleset : targetPlayer.getDbBattlesetList()) {
                WsMessageBase.IOBattlesetEnemy ioBattlesetEnemy = new WsMessageBase.IOBattlesetEnemy();
                if (dbBattleset.getMythic() != null) {
                    WsMessageBase.IOMythicalAnimal ioMythicalAnimal = new WsMessageBase.IOMythicalAnimal();
                    ioBattlesetEnemy.mythic = ioMythicalAnimal;
                    MythicalListProcessor.buildIOMythicalAnimal(dbBattleset.getMythic(), 101, ioMythicalAnimal);
                }
                ioBattlesetEnemy.team = new HashMap<>();
                Map<Integer, GeneralBean> battlesetTeam = dbBattleset.getTeam();
                for (Map.Entry<Integer, GeneralBean> aEntry : battlesetTeam.entrySet()) {
                    GeneralBean generalBean = aEntry.getValue();
                    if (generalBean == null) {
                        continue;
                    }
                    generalBean = GetOtherPlayerInfoProcessor.generalTmpUpgrade(generalBean);
                    WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
                    Integer formationPos = aEntry.getKey();
                    ioBattlesetEnemy.team.put(formationPos, ioGeneralBean);
                }
                respmsg.battleset.add(ioBattlesetEnemy);
            }
        }
        //send
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
