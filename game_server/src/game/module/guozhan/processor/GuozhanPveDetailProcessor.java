package game.module.guozhan.processor;

import db.proto.ProtoMessageGuozhan;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.battle.dao.BattlePlayerBase;
import game.module.battle.logic.BattleManager;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.CityJoinTemplateCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuozhanConstants;
import game.module.guozhan.logic.GuozhanPlayerManager;
import game.module.hero.logic.GeneralManager;
import game.module.mine.bean.DBMinePoint;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageGuozhan;
import ws.WsMessageHall.S2CErrorCode;

import java.util.HashMap;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageGuozhan.C2SGuozhanBattleView.id, accessLimit = 500)
public class GuozhanPveDetailProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GuozhanPveDetailProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageGuozhan.C2SGuozhanBattleView reqMsg = WsMessageGuozhan.C2SGuozhanBattleView.parse(request);
        int playerId = playingRole.getId();
        int city_index = reqMsg.city_index;
        logger.info("guo zhan pve detail!playerId={},city_index={}", playerId, city_index);
        // 能否打
        GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
        if (guozhanPlayer == null || guozhanPlayer.getDbGuozhanPlayer() == null
                || guozhanPlayer.getDbGuozhanPlayer().getPassCityIndexCount() == 0) {
            // 国战地图必须从洛阳开始攻打！
            if (city_index != 0) {
                S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuozhanBattleView.msgCode, 1001);
                playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
                return;
            }
        }
        // 是否已经通关
        if (guozhanPlayer != null && guozhanPlayer.getDbGuozhanPlayer() != null
                && guozhanPlayer.getDbGuozhanPlayer().containsPassCityIndex(city_index)) {
            S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuozhanBattleView.msgCode, 1002);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        // 是否可以有路攻打
        if (city_index != 0) {
            Map<String, Object> cityJoinTemplate = CityJoinTemplateCache.getInstance().getSecretTemp(city_index + 1);
            Map<Integer, Boolean> passCityIndexMap = guozhanPlayer.getDbGuozhanPlayer().getPassCityIndexMap();
            boolean findJoin = false;
            for (int i = 0; i < GuozhanConstants.CITY_JOIN_ID_NAMES.length; i++) {
                String aIdName = GuozhanConstants.CITY_JOIN_ID_NAMES[i];
                int aId = (int) cityJoinTemplate.get(aIdName);
                if (aId > 0 && passCityIndexMap.containsKey(aId - 1)) {
                    findJoin = true;
                    break;
                }
            }
            if (!findJoin) {
                S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuozhanBattleView.msgCode, 1003);
                playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
                return;
            }
        }
        // ret
        WsMessageGuozhan.S2CGuozhanBattleView respMsg = new WsMessageGuozhan.S2CGuozhanBattleView();
        //
        respMsg.city_index = city_index;
        ProtoMessageGuozhan.DBGuozhanPlayer dbGuozhanPlayer = null;
        if (guozhanPlayer != null) {
            dbGuozhanPlayer = guozhanPlayer.getDbGuozhanPlayer();
        }
        //
        respMsg.enemy_level = GuozhanPlayerManager.getInstance().getEnemyLevel(dbGuozhanPlayer);
        //机器人阵型
//        DBMinePoint cityPointRobots = BattleManager.getInstance().getRobotFormation(playerId, false);
//        if (cityPointRobots == null) {
        DBMinePoint cityPointRobots = GuozhanPlayerManager.getInstance().createCityPointRobots(dbGuozhanPlayer);
        BattleManager.getInstance().tmpSaveRobot(playerId, cityPointRobots);
//        }
        //机器人阵容
        Map<Integer, BattlePlayerBase> battlePlayerMap = cityPointRobots.getBattlePlayerMap();
        respMsg.battleset = new WsMessageBase.IOBattlesetEnemy();
        respMsg.battleset.team = new HashMap<>();
        for (Map.Entry<Integer, BattlePlayerBase> aEntry : battlePlayerMap.entrySet()) {
            BattlePlayerBase battlePlayer = aEntry.getValue();
            WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(battlePlayer);
            Integer formationPos = aEntry.getKey();
            respMsg.battleset.team.put(formationPos, ioGeneralBean);
        }
        respMsg.base_info = new WsMessageBase.IOGuozhanPointPlayer(0, cityPointRobots.getRname(), cityPointRobots.getLevel(),
                cityPointRobots.getIconid(), cityPointRobots.getFrameid(), cityPointRobots.getPower());
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

}
