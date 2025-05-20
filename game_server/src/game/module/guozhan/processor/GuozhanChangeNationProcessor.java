package game.module.guozhan.processor;

import db.proto.ProtoMessageGuozhan.DBGuoZhanOffice;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.chat.logic.NationChatCache;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.CityJoinTemplateCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuoZhanFightManager;
import game.module.guozhan.logic.GuozhanConstants;
import game.module.guozhan.logic.GuozhanDaoHelper;
import game.module.log.constants.LogConstants;
import game.module.user.dao.CommonTemplateCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageGuozhan.C2SGuozhanChangeNation;
import ws.WsMessageGuozhan.S2CGuozhanChangeNation;
import ws.WsMessageHall;
import ws.WsMessageHall.S2CErrorCode;

import java.util.Date;

@MsgCodeAnn(msgcode = C2SGuozhanChangeNation.id, accessLimit = 500)
public class GuozhanChangeNationProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(GuozhanChangeNationProcessor.class);

	@Override
	public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		C2SGuozhanChangeNation reqMsg = C2SGuozhanChangeNation.parse(request);
		int playerId = playingRole.getId();
		int target_nation = reqMsg.target_nation;
		logger.info("guo zhan change nation!playerId={},target_nation={}", playerId, target_nation);
		//参数检测
		if(target_nation <=0 || target_nation >3) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanChangeNation.msgCode, 130);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		//
		GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
		if (guozhanPlayer == null) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanChangeNation.msgCode, 130);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 是否已经通关
		int citySize = CityJoinTemplateCache.getInstance().getSize();
		if (guozhanPlayer != null && guozhanPlayer.getDbGuozhanPlayer() != null
				&& guozhanPlayer.getDbGuozhanPlayer().getPassCityIndexCount() < citySize) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanChangeNation.msgCode, 1006);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		//cd
		Date now = new Date();
		if (guozhanPlayer != null && guozhanPlayer.getNationChangeTime() != null) {
			Date nationChangeTime = guozhanPlayer.getNationChangeTime();
			Date cdEndTime = DateUtils.addSeconds(nationChangeTime, GuozhanConstants.CHANGE_NATION_CD_TIME);
			if(cdEndTime.after(now)) {
				S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanChangeNation.msgCode, 1007);
				playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				return;
			}
		}
		//消耗金块
		int costJinkuai = 0;
		if(guozhanPlayer != null && guozhanPlayer.getNation() > 0) {
			costJinkuai = CommonTemplateCache.getInstance().getIntConfig("guozhan_change_nation_cost");
			if(playingRole.getPlayerBean().getMoney()<costJinkuai) {
				S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanChangeNation.msgCode, 1008);
				playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				return;
			}
		}
		//do
		//老数据清理
		int oldNation = guozhanPlayer.getNation();
		if (oldNation > 0) {
			DBGuoZhanOffice guoZhanOffice = GuozhanCache.getInstance().getDBGuoZhanOffice();
			if (guoZhanOffice != null) {
				DBGuoZhanOffice.Builder builder = guoZhanOffice.toBuilder();
				int officeIndex = guoZhanOffice.getPlayerPointOrDefault(playerId, -1);
				if (officeIndex > -1) {
					builder.getNationsBuilder(oldNation - 1).getPlayerOfficesBuilder(officeIndex).setPlayerId(0);
				}
				builder.removePlayerPoint(playerId);
				guoZhanOffice = builder.build();
				GuozhanCache.getInstance().setGuozhanOffice(guoZhanOffice);
			}
			// 城池信息
			GuoZhanFightManager.getInstance().changeNation(playerId, oldNation, target_nation);
			//聊天
			NationChatCache.getInstance().removePlayer(oldNation, playingRole);
			NationChatCache.getInstance().addPlayer(target_nation, playingRole);
		}else {
			//城池信息
			GuoZhanFightManager.getInstance().playerInit(playerId, target_nation);
			//聊天
			NationChatCache.getInstance().addPlayer(target_nation, playingRole);
			//奖励头像框
			AwardUtils.changeRes(playingRole,51014,1,LogConstants.MODULE_GUOZHAN);
			//push
			WsMessageHall.PushPropChange pushmsg = new WsMessageHall.PushPropChange(100015,1);
			playingRole.write(pushmsg.build(playingRole.alloc()));
		}
		//
		guozhanPlayer.setNation(target_nation);
		guozhanPlayer.setNationChangeTime(now);
		GuozhanDaoHelper.asyncUpdateGuozhanNation(guozhanPlayer);
		if (oldNation > 0) {
			// 发送更新的国战城池争夺信息
			GuozhanFightViewProcessor.sendResponse(playingRole, playerId);
		}
		//
		if (costJinkuai > 0) {
			AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -costJinkuai, LogConstants.MODULE_GUOZHAN);
		}
		//更新缓存
		playingRole.getPlayerBean().setNationId(target_nation);
		//ret
		S2CGuozhanChangeNation respMsg = new S2CGuozhanChangeNation(oldNation,target_nation, GuozhanConstants.CHANGE_NATION_CD_TIME);
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
