package game.module.guozhan.processor;

import db.proto.ProtoMessageGuozhan.DBGuoZhanNation;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOffice;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.battle.dao.BattlePlayerBase;
import game.module.battle.logic.BattleManager;
import game.module.chapter.bean.BattleFormation;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.CityJoinTemplateCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuoZhanManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.logic.GeneralManager;
import game.module.mine.bean.DBMinePoint;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.processor.MythicalListProcessor;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBase.GuozhanOfficePointPlayer;
import ws.WsMessageGuozhan.C2SGuozhanOfficeDetail;
import ws.WsMessageGuozhan.S2CGuozhanOfficeDetail;
import ws.WsMessageHall.S2CErrorCode;

import java.util.HashMap;
import java.util.Map;

@MsgCodeAnn(msgcode = C2SGuozhanOfficeDetail.id, accessLimit = 500)
public class GuozhanOfficeDetailProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GuozhanOfficeDetailProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        C2SGuozhanOfficeDetail reqMsg = C2SGuozhanOfficeDetail.parse(request);
        int playerId = playingRole.getId();
        int officeIndex = reqMsg.office_index;
        logger.info("guo zhan office detail!playerId={},officeIndex={}", playerId, officeIndex);
        GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
        // 还没有国家
        if (guozhanPlayer == null || guozhanPlayer.getNation() == 0) {
            S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanOfficeDetail.msgCode, 130);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        // 是否已经通关
        int citySize = CityJoinTemplateCache.getInstance().getSize();
        if (guozhanPlayer != null && guozhanPlayer.getDbGuozhanPlayer() != null
                && guozhanPlayer.getDbGuozhanPlayer().getPassCityIndexCount() < citySize) {
            S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanOfficeDetail.msgCode, 1006);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //
        DBGuoZhanOffice guoZhanOffice = GuozhanCache.getInstance().getDBGuoZhanOffice();
        int targetPlayerId = 0;
        if (guoZhanOffice != null) {
            int myNationId = guozhanPlayer.getNation();
            DBGuoZhanNation guoZhanNation = guoZhanOffice.getNations(myNationId - 1);
            if (guoZhanNation.getPlayerOfficesCount() > 0) {
                targetPlayerId = guoZhanNation.getPlayerOffices(officeIndex).getPlayerId();
            }
        }
        //ret
        S2CGuozhanOfficeDetail respMsg = new S2CGuozhanOfficeDetail();
        respMsg.office_index = officeIndex;
        if (targetPlayerId > 0) {
            PlayingRole targetPr = SessionManager.getInstance().getPlayer(targetPlayerId);
            PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(targetPlayerId);
            respMsg.base_info = new GuozhanOfficePointPlayer(officeIndex, targetPlayerId, poc.getName(), poc.getLevel(),
                    poc.getIconid(), poc.getFrameid(), poc.getPower(), 100);
            //PVP阵容
            BattleFormation battleFormation = PlayerOfflineManager.getInstance().getBattleFormation(targetPlayerId);
            if (battleFormation != null) {
                respMsg.battleset = new WsMessageBase.IOBattlesetEnemy();
                int pvpAttFormationType = 3;
                if (battleFormation.getMythics() != null && battleFormation.getMythics().containsKey(pvpAttFormationType)) {
                    WsMessageBase.IOMythicalAnimal ioMythicalAnimal = new WsMessageBase.IOMythicalAnimal();
                    respMsg.battleset.mythic = ioMythicalAnimal;
                    Map<Integer, MythicalAnimal> mythicalAll = PlayerOfflineManager.getInstance().getMythicalAll(targetPlayerId);
                    int mythicalId = battleFormation.getMythics().get(pvpAttFormationType);
                    MythicalAnimal mythicalAnimal = mythicalAll.get(mythicalId);
                    MythicalListProcessor.buildIOMythicalAnimal(mythicalAnimal, mythicalId, ioMythicalAnimal);
                }
                Map<Integer, Long> enemyDefenceFormation = battleFormation.getPvpatt() != null ? battleFormation.getPvpatt() : battleFormation.getNormal();
                respMsg.battleset.team = new HashMap<>();
                Map<Long, GeneralBean> generalAll = PlayerOfflineManager.getInstance().getGeneralAll(targetPlayerId);
                for (Map.Entry<Integer, Long> aEntry : enemyDefenceFormation.entrySet()) {
                    GeneralBean generalBean = generalAll.get(aEntry.getValue());
                    if (generalBean == null) {
                        continue;
                    }
                    WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
                    Integer formationPos = aEntry.getKey();
                    respMsg.battleset.team.put(formationPos, ioGeneralBean);
                }
            }
        } else {
            //机器人阵容
            DBMinePoint minePointEntity = GuoZhanManager.getInstance().generateRobot(officeIndex);
            Map<Integer, BattlePlayerBase> battlePlayerMap = minePointEntity.getBattlePlayerMap();
            respMsg.battleset = new WsMessageBase.IOBattlesetEnemy();
            respMsg.battleset.team = new HashMap<>();
            for (Map.Entry<Integer, BattlePlayerBase> aEntry : battlePlayerMap.entrySet()) {
                BattlePlayerBase battlePlayer = aEntry.getValue();
                WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(battlePlayer);
                Integer formationPos = aEntry.getKey();
                respMsg.battleset.team.put(formationPos, ioGeneralBean);
            }
            BattleManager.getInstance().tmpSaveRobot(playerId, minePointEntity);
            respMsg.base_info = new GuozhanOfficePointPlayer(officeIndex, 0, minePointEntity.getRname(), minePointEntity.getLevel(),
                    minePointEntity.getIconid(), minePointEntity.getFrameid(), minePointEntity.getPower(), 100);
        }
        // ret
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

}
