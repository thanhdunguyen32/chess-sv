package game.module.guozhan.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.BattleFormationDaoHelper;
import game.module.chapter.logic.BattleFormationManager;
import game.module.chapter.logic.PowerFormationManager;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.CityJoinTemplateCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuozhanConstants;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.item.logic.ItemConstants;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.season.dao.SeasonCache;
import game.module.user.logic.PlayerInfoManager;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;
import ws.WsMessageHall.S2CErrorCode;

import java.util.HashMap;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageBattle.C2SGuozhanBattleStart.id, accessLimit = 500)
public class GuozhanBattleStartProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(GuozhanBattleStartProcessor.class);

	@Override
	public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		WsMessageBattle.C2SGuozhanBattleStart reqmsg = WsMessageBattle.C2SGuozhanBattleStart.parse(request);
		int playerId = playingRole.getId();
		int city_index = reqmsg.city_index;
		logger.info("GuoZhan PVE battle start!playerId={},city_index={}", playerId, city_index);
		// 能否打
		GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
		if (guozhanPlayer == null || guozhanPlayer.getDbGuozhanPlayer() == null
				|| guozhanPlayer.getDbGuozhanPlayer().getPassCityIndexCount() == 0) {
			// 国战地图必须从洛阳开始攻打！
			if (city_index != 0) {
				S2CErrorCode respMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanBattleStart.msgCode, 1001);
				playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				return;
			}
		}
		// 是否已经通关
		if (guozhanPlayer != null && guozhanPlayer.getDbGuozhanPlayer() != null
				&& guozhanPlayer.getDbGuozhanPlayer().containsPassCityIndex(city_index)) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanBattleStart.msgCode, 1002);
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
				S2CErrorCode respMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanBattleStart.msgCode, 1003);
				playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				return;
			}
		}
        //cost 演武令
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), ItemConstants.PVP_COIN, 1)) {
			S2CErrorCode retMsg = new S2CErrorCode(WsMessageBattle.S2CGuozhanBattleStart.msgCode, 2105);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
        //is general exist
        WsMessageBase.IOFormationGeneralPos[] items = reqmsg.items;
        Map<Long, GeneralBean> generalMap = GeneralCache.getInstance().getHeros(playerId);
        for (WsMessageBase.IOFormationGeneralPos ioFormationGeneralPos : items) {
            if (!generalMap.containsKey(ioFormationGeneralPos.general_uuid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleStart.msgCode, 1432);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            if (ioFormationGeneralPos.pos < 0 || ioFormationGeneralPos.pos > 32) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleStart.msgCode, 1433);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
		// do
        //save mine formation
        BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
        if (battleFormation == null) {
            battleFormation = BattleFormationManager.getInstance().createBattleFormation(playerId);
        }
        int mineFormationIndex = 3;
        int mythic = reqmsg.mythic;
        if (mythic > 0) {
            //check mythic exist
            boolean containsTemplate = MythicalTemplateCache.getInstance().containsTemplate(mythic);
            if (!containsTemplate) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMineBattleStart.msgCode, 1420);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            Map<Integer, Integer> mythicsMap = battleFormation.getMythics();
            if (mythicsMap == null) {
                mythicsMap = new HashMap<>();
                battleFormation.setMythics(mythicsMap);
            }
            mythicsMap.put(mineFormationIndex, mythic);
        }
        //team
        Map<Integer, Long> mineMap = BattleFormationManager.getInstance().getFormationByType(mineFormationIndex, battleFormation);
        if (mineMap == null) {
            mineMap = new HashMap<>();
            BattleFormationManager.getInstance().setFormationByType(mineFormationIndex, battleFormation, mineMap);
        } else {
            mineMap.clear();
        }
        for (WsMessageBase.IOFormationGeneralPos aitem : reqmsg.items) {
            mineMap.put(aitem.pos, aitem.general_uuid);
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
        for (WsMessageBase.IOFormationGeneralPos aitem : reqmsg.items) {
            long general_uuid = aitem.general_uuid;
            GeneralBean generalBean = generalMap.get(general_uuid);
            if (generalBean != null) {
                sumPower += generalBean.getPower();
            }
        }
        PowerFormationManager.getInstance().refreshPowerFormation(playingRole.getPlayerBean().getPower(),sumPower,mythic,mineMap,playingRole);
        PlayerInfoManager.getInstance().saveMaxPower(playingRole, sumPower);
        //cost
        AwardUtils.changeRes(playingRole, ItemConstants.PVP_COIN, -1, LogConstants.MODULE_MINE);
        //ret
        WsMessageBattle.S2CGuozhanBattleStart respmsg = new WsMessageBattle.S2CGuozhanBattleStart();
        respmsg.battleid = SessionManager.getInstance().generateSessionId();
        BattleManager.getInstance().saveBattleId(playerId, respmsg.battleid);
        BattleManager.getInstance().tmpSaveGuozhanCache(playerId,city_index);
        respmsg.seed = SessionManager.getInstance().generateSessionId();
        respmsg.season = SeasonCache.getInstance().getBattleSeason().getSeason();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
	}

}
