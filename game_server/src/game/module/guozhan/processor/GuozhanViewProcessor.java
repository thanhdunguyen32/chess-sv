package game.module.guozhan.processor;

import db.proto.ProtoMessageGuozhan.DBGuoZhanFight;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOffice;
import db.proto.ProtoMessageGuozhan.DBGuozhanPlayer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.CityJoinTemplateCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuozhanConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageGuozhan.C2SGuozhanView;
import ws.WsMessageGuozhan.S2CGuozhanView;

import java.util.ArrayList;
import java.util.Date;

@MsgCodeAnn(msgcode = C2SGuozhanView.id, accessLimit = 500)
public class GuozhanViewProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(GuozhanViewProcessor.class);

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
		logger.info("guo zhan view!playerId={}", playerId);
		//已经进入城池争夺
		// 是否已经通关
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		if (guoZhanFight != null && guoZhanFight.getPlayersOrDefault(playerId, null) != null) {
			GuozhanFightViewProcessor.sendResponse(playingRole, playerId);
			return;
		}
		GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
		int citySize = CityJoinTemplateCache.getInstance().getSize();
		S2CGuozhanView respMsg = new S2CGuozhanView();
		// 默认洛阳
		respMsg.player_city_index = 0;
		respMsg.my_nation = 0;
		respMsg.change_nation_cd = 0;
		if (guozhanPlayer != null) {
			respMsg.player_city_index = guozhanPlayer.getStay_city_index();
			DBGuozhanPlayer dbGuozhanPlayer = guozhanPlayer.getDbGuozhanPlayer();
			if (dbGuozhanPlayer != null) {
				respMsg.pass_city_index = new ArrayList<>(dbGuozhanPlayer.getPassCityIndexMap().keySet());
			}
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
		}
		// 官职
		respMsg.my_office = -1;
		DBGuoZhanOffice guoZhanOffice = GuozhanCache.getInstance().getDBGuoZhanOffice();
		if (guoZhanOffice != null) {
			respMsg.my_office = guoZhanOffice.getPlayerPointOrDefault(playerId, -1);
		}
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
