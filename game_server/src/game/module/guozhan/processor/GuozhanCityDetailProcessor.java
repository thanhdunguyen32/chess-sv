package game.module.guozhan.processor;

import db.proto.ProtoMessageGuozhan.DBGuoZhanCity;
import db.proto.ProtoMessageGuozhan.DBGuoZhanFight;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.chapter.bean.BattleFormation;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.CityJoinTemplateCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuoZhanFightManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.logic.GeneralManager;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.processor.MythicalListProcessor;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBase.GuozhanOfficePointPlayer;
import ws.WsMessageGuozhan.C2SGuoZhanCityDetail;
import ws.WsMessageGuozhan.S2CGuoZhanCityDetail;
import ws.WsMessageHall.S2CErrorCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = C2SGuoZhanCityDetail.id, accessLimit = 500)
public class GuozhanCityDetailProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GuozhanCityDetailProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        C2SGuoZhanCityDetail reqMsg = C2SGuoZhanCityDetail.parse(request);
        int playerId = playingRole.getId();
        int city_index = reqMsg.city_index;
        logger.info("guo zhan city detail!playerId={},city_index={}", playerId, city_index);
        GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
        // 还没有国家
        if (guozhanPlayer == null || guozhanPlayer.getNation() == 0) {
            S2CErrorCode respMsg = new S2CErrorCode(S2CGuoZhanCityDetail.msgCode, 130);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        // 是否已经通关
        int citySize = CityJoinTemplateCache.getInstance().getSize();
        if (guozhanPlayer != null && guozhanPlayer.getDbGuozhanPlayer() != null
                && guozhanPlayer.getDbGuozhanPlayer().getPassCityIndexCount() < citySize) {
            S2CErrorCode respMsg = new S2CErrorCode(S2CGuoZhanCityDetail.msgCode, 1006);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //还未生成
        DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
        if (guoZhanFight == null) {
            S2CErrorCode respMsg = new S2CErrorCode(S2CGuoZhanCityDetail.msgCode, 130);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        DBGuoZhanCity guoZhanCity = guoZhanFight.getCitys(city_index);
        if (guoZhanCity == null) {
            S2CErrorCode respMsg = new S2CErrorCode(S2CGuoZhanCityDetail.msgCode, 130);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //
        S2CGuoZhanCityDetail respMsg = new S2CGuoZhanCityDetail();
        respMsg.city_index = city_index;
        List<Integer> playerIdList = guoZhanCity.getOccupyPlayerList();
        respMsg.players = new ArrayList<>(playerIdList.size());
        for (Integer targetPlayerId : playerIdList) {
            int hpPerc = GuoZhanFightManager.getInstance().getPlayerHpPercent(targetPlayerId);
            PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(targetPlayerId);
            WsMessageBase.IOGuoZhanPvpPlayer ioGuoZhanPvpPlayer = new WsMessageBase.IOGuoZhanPvpPlayer();
            respMsg.players.add(ioGuoZhanPvpPlayer);
            ioGuoZhanPvpPlayer.base_info = new GuozhanOfficePointPlayer(0, targetPlayerId, poc.getName(), poc.getLevel(), poc.getIconid(),
                    poc.getFrameid(), poc.getPower(), hpPerc);
            ioGuoZhanPvpPlayer.battleset = new WsMessageBase.IOBattlesetEnemy();
            BattleFormation battleFormation = PlayerOfflineManager.getInstance().getBattleFormation(targetPlayerId);
            if (battleFormation != null) {
                int pvpAttFormationType = 3;
                if (battleFormation.getMythics() != null && battleFormation.getMythics().containsKey(pvpAttFormationType)) {
                    WsMessageBase.IOMythicalAnimal ioMythicalAnimal = new WsMessageBase.IOMythicalAnimal();
                    ioGuoZhanPvpPlayer.battleset.mythic = ioMythicalAnimal;
                    Map<Integer, MythicalAnimal> mythicalAll = PlayerOfflineManager.getInstance().getMythicalAll(targetPlayerId);
                    int mythicalId = battleFormation.getMythics().get(pvpAttFormationType);
                    MythicalAnimal mythicalAnimal = mythicalAll.get(mythicalId);
                    MythicalListProcessor.buildIOMythicalAnimal(mythicalAnimal, mythicalId, ioMythicalAnimal);
                }
                Map<Integer, Long> battleFormationMap = battleFormation.getPvpatt() != null ? battleFormation.getPvpatt() : battleFormation.getNormal();
                ioGuoZhanPvpPlayer.battleset.team = new HashMap<>();
                Map<Long, GeneralBean> generalAll = PlayerOfflineManager.getInstance().getGeneralAll(targetPlayerId);
                for (Map.Entry<Integer, Long> aEntry : battleFormationMap.entrySet()) {
                    GeneralBean generalBean = generalAll.get(aEntry.getValue());
                    if (generalBean == null) {
                        continue;
                    }
                    WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
                    ioGeneralBean.hppercent = hpPerc * 100;
                    Integer formationPos = aEntry.getKey();
                    ioGuoZhanPvpPlayer.battleset.team.put(formationPos, ioGeneralBean);
                }
            }
        }
        respMsg.my_hp_perc = GuoZhanFightManager.getInstance().getPlayerHpPercent(playerId);
        // ret
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

}
