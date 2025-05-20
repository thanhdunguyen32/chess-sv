package game.module.guozhan.processor;

import db.proto.ProtoMessageGuozhan.DBGuoZhanFight;
import db.proto.ProtoMessageGuozhan.DBGuoZhanHistory;
import db.proto.ProtoMessageGuozhan.DBGuoZhanRole;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.guozhan.dao.GuozhanCache;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageGuozhan.C2SGuozhanHistory;
import ws.WsMessageGuozhan.S2CGuozhanHistory;
import ws.WsMessageHall.S2CErrorCode;

import java.util.ArrayList;
import java.util.List;

@MsgCodeAnn(msgcode = C2SGuozhanHistory.id, accessLimit = 500)
public class GuozhanHistoryRecordProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(GuozhanHistoryRecordProcessor.class);

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
		logger.info("guo zhan history record!playerId={}", playerId);
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		if(guoZhanFight == null) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanHistory.msgCode, 130);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		DBGuoZhanRole guoZhanRole = guoZhanFight.getPlayersOrDefault(playerId, null);
		if(guoZhanRole == null) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CGuozhanHistory.msgCode,130);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		//do
		List<DBGuoZhanHistory> recordAll = guoZhanRole.getMyRecordList();
		S2CGuozhanHistory respMsg = new S2CGuozhanHistory();
		respMsg.records = new ArrayList<>(recordAll.size());
		int i=0;
		for (DBGuoZhanHistory dbGuoZhanHistory : recordAll) {
			WsMessageBase.IOGuozhanHistory ioGuozhanHistory = new WsMessageBase.IOGuozhanHistory();
			respMsg.records.add(ioGuozhanHistory);
			ioGuozhanHistory.action_type = dbGuoZhanHistory.getType();
			ioGuozhanHistory.add_time = (int) (dbGuoZhanHistory.getAddTime() / 1000);
			ioGuozhanHistory.params = new ArrayList<>(dbGuoZhanHistory.getParamsCount());
			for (int j = 0; j < dbGuoZhanHistory.getParamsCount(); j++) {
				ioGuozhanHistory.params.add(dbGuoZhanHistory.getParams(j));
			}
			int targetPlayerId = dbGuoZhanHistory.getTargetPlayerId();
			if (targetPlayerId > 0) {
				PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(targetPlayerId);
				if (poc != null) {
					ioGuozhanHistory.target_player_name = poc.getName();
				}
			}
		}
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
