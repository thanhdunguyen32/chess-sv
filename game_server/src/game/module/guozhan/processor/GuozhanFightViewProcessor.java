package game.module.guozhan.processor;

import db.proto.ProtoMessageGuozhan.DBGuoZhanCity;
import db.proto.ProtoMessageGuozhan.DBGuoZhanFight;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOffice;
import db.proto.ProtoMessageGuozhan.DBGuoZhanRole;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.CityJoinTemplateCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuoZhanFightManager;
import game.module.guozhan.logic.GuozhanConstants;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageGuozhan.C2SGuozhanFightView;
import ws.WsMessageGuozhan.S2CGuozhanFightView;
import ws.WsMessageHall.S2CErrorCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@MsgCodeAnn(msgcode = C2SGuozhanFightView.id, accessLimit = 500)
public class GuozhanFightViewProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(GuozhanFightViewProcessor.class);

	@Override
	public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		int playerId = playingRole.getId();
		logger.info("guo zhan fight view!playerId={}", playerId);
		sendResponse(playingRole,playerId);
	}

	public static void sendResponse(PlayingRole playingRole, int playerId) {
		logger.info("send guozhan city fight list!");
		//
		GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
		if (guozhanPlayer == null) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanFightView.msgCode, 130);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 是否已经通关
		int citySize = CityJoinTemplateCache.getInstance().getSize();
		if (guozhanPlayer != null && guozhanPlayer.getDbGuozhanPlayer() != null
				&& guozhanPlayer.getDbGuozhanPlayer().getPassCityIndexCount() < citySize) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanFightView.msgCode,1006);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 我的信息
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		if (guoZhanFight == null) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanFightView.msgCode,130);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		//当前没有占领城池的话，随机位置
		DBGuoZhanRole guoZhanRole = guoZhanFight.getPlayersOrDefault(playerId, null);
		if (guoZhanRole != null && guoZhanRole.getCityIndex() == -1) {
			guoZhanFight = GuoZhanFightManager.getInstance().viewPlayerRandPos(guoZhanFight, playerId,
					guozhanPlayer.getNation());
			guoZhanRole = guoZhanFight.getPlayersOrDefault(playerId, null);
		}
		S2CGuozhanFightView respMsg = new S2CGuozhanFightView();
		respMsg.my_city_index = -1;
		if (guoZhanRole != null) {
			respMsg.my_city_index = guoZhanRole.getCityIndex();
			respMsg.move_step = GuoZhanFightManager.getInstance().getMyMoveStep(guoZhanRole);
		}
		respMsg.nation_city_count = guoZhanFight.getNationOwnCityList();
		// 城市列表
		List<DBGuoZhanCity> cityAll = guoZhanFight.getCitysList();
		respMsg.city_list = new ArrayList<>(cityAll.size());
		for (DBGuoZhanCity dbGuoZhanCity : cityAll) {
			boolean pin_battle = dbGuoZhanCity.getInBattle();
			int pnation_id = dbGuoZhanCity.getOwnNationId();
			int pplayer_size = dbGuoZhanCity.getOccupyPlayerCount();
			String pplayer_name = "";
			int ownPlayerId = 0;
			if (pplayer_size > 0) {
				ownPlayerId = dbGuoZhanCity.getOccupyPlayer(0);
				PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(ownPlayerId);
				if (poc != null) {
					pplayer_name = poc.getName();
				}
			}
			respMsg.city_list.add(new WsMessageBase.IOGuoZhanCity(ownPlayerId, pplayer_name, pplayer_size,pnation_id,pin_battle));
		}
		//血量
		respMsg.hp_perc = GuoZhanFightManager.getInstance().getPlayerHpPercent(playerId);
		respMsg.change_nation_cd = 0;
		respMsg.my_nation = guozhanPlayer.getNation();
		Date nationChangeTime = guozhanPlayer.getNationChangeTime();
		if(nationChangeTime != null) {
			Date cdEndTime = DateUtils.addSeconds(nationChangeTime, GuozhanConstants.CHANGE_NATION_CD_TIME);
			Date now = new Date();
			if(cdEndTime.after(now)) {
				int cdSeconds = (int)((cdEndTime.getTime()-now.getTime())/1000);
				respMsg.change_nation_cd = cdSeconds;
			}
		}
		// 官职
		respMsg.my_office = -1;
		DBGuoZhanOffice guoZhanOffice = GuozhanCache.getInstance().getDBGuoZhanOffice();
		if (guoZhanOffice != null) {
			respMsg.my_office = guoZhanOffice.getPlayerPointOrDefault(playerId, -1);
		}
		// ret
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
