package game.module.guozhan.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.module.offline.bean.PlayerBaseBean;
import lion.netty4.message.RequestByteMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.proto.ProtoMessageGuozhan.DBGuoZhanNation;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOffice;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOfficePoint;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.guozhan.bean.GuozhanOfficeTemplate;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.CityJoinTemplateCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.dao.GuozhanOfficeTemplateCache;
import game.module.offline.logic.PlayerOfflineManager;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestProtoMessage;
import ws.WsMessageBase;
import ws.WsMessageGuozhan;
import ws.WsMessageGuozhan.C2SGuozhanOfficeView;
import ws.WsMessageGuozhan.S2CGuozhanOfficeView;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2SGuozhanOfficeView.id, accessLimit = 500)
public class GuozhanOfficeViewProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(GuozhanOfficeViewProcessor.class);

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
		logger.info("guo zhan office view!playerId={}", playerId);
		GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
		// 还没有国家
		if (guozhanPlayer == null || guozhanPlayer.getNation() == 0) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuozhanOfficeView.msgCode, 130);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 是否已经通关
		int citySize = CityJoinTemplateCache.getInstance().getSize();
		if (guozhanPlayer != null && guozhanPlayer.getDbGuozhanPlayer() != null
				&& guozhanPlayer.getDbGuozhanPlayer().getPassCityIndexCount() < citySize) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageGuozhan.S2CGuozhanOfficeView.msgCode, 1006);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// ret
		S2CGuozhanOfficeView respMsg = new S2CGuozhanOfficeView();
		respMsg.player_list = getPlayerList(guozhanPlayer.getNation());
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

	public static List<WsMessageBase.GuozhanOfficePointPlayer> getPlayerList(int myNationId) {
        List<WsMessageBase.GuozhanOfficePointPlayer> retlist = null;
		DBGuoZhanOffice guoZhanOffice = GuozhanCache.getInstance().getDBGuoZhanOffice();
		if (guoZhanOffice != null) {
			DBGuoZhanNation guiZhanNation = guoZhanOffice.getNations(myNationId - 1);
			List<DBGuoZhanOfficePoint> playerList = guiZhanNation.getPlayerOfficesList();
			List<GuozhanOfficeTemplate> officeTemplates = GuozhanOfficeTemplateCache.getInstance().getTemplates();
			Map<Integer, Integer> playerMap = new HashMap<>();
			if (playerList.size() > 0) {
				int levelIndex = 0;
				for (GuozhanOfficeTemplate guozhanOfficeTemplate : officeTemplates) {
					int levelSize = guozhanOfficeTemplate.getCount();
					for (int i = 0; i < levelSize; i++) {
						int bigIndex = levelIndex * 10 + i;
						DBGuoZhanOfficePoint guoZhanOfficePoint = playerList.get(bigIndex);
						if (guoZhanOfficePoint.getPlayerId() > 0) {
							playerMap.put(bigIndex, guoZhanOfficePoint.getPlayerId());
						}
					}
					levelIndex++;
				}
			}

			retlist = new ArrayList<>(playerMap.size());
			for (Map.Entry<Integer, Integer> playerEntry : playerMap.entrySet()) {
				int officePos = playerEntry.getKey();
				int targetPlayerId = playerEntry.getValue();
				PlayingRole targetPr = SessionManager.getInstance().getPlayer(targetPlayerId);
				PlayerBaseBean poc = null;
                if (targetPr == null) {
                    poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(targetPlayerId);
                    retlist.add(new WsMessageBase.GuozhanOfficePointPlayer(officePos, targetPlayerId, poc.getName(), poc.getLevel(), poc.getIconid()
                            , poc.getFrameid(), poc.getPower(), 100));
                } else {
                    retlist.add(new WsMessageBase.GuozhanOfficePointPlayer(officePos, targetPlayerId, targetPr.getPlayerBean().getName(),
                            targetPr.getPlayerBean().getLevel(), targetPr.getPlayerBean().getIconid(), targetPr.getPlayerBean().getFrameid(),
                            targetPr.getPlayerBean().getPower(), 100));
                }
			}
		}
		return retlist;
	}

}
