package game.module.user.processor;

import java.util.Map;

import game.module.award.bean.GameConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.user.dao.OfflineCache;
import game.module.user.dao.OfflineCache.OfflineAwardBean;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import ws.WsMessageHall.C2SOfflineAwardDouble;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageHall.S2COfflineAwardDouble;

@MsgCodeAnn(msgcode = C2SOfflineAwardDouble.id, accessLimit = 200)
public class OfflineAwardDoubleProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(OfflineAwardDoubleProcessor.class);

	@Override
	public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		int playerId = playingRole.getId();
		logger.info("offline get award double,playerId={}", playerId);
		OfflineAwardBean offlineAwardBean = OfflineCache.getInstance().getOfflineAward(playerId);
		if (offlineAwardBean == null) {
			S2CErrorCode respMsg = new S2CErrorCode(S2COfflineAwardDouble.msgCode, 140);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 金块不足
		int offlineMinutes = offlineAwardBean.offlineMinutes;
		int costJinKuai = offlineMinutes*10;
		if (playingRole.getPlayerBean().getMoney() < costJinKuai) {
			S2CErrorCode respMsg = new S2CErrorCode(S2COfflineAwardDouble.msgCode, 132);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// do
		// 扣金块
		AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -costJinKuai,
				LogConstants.MODULE_OFFLINE_AWARD);
		// 发奖励
		int addLevel = 0;
		Map<Integer, Integer> awardsMap = offlineAwardBean.awards;
		for (Map.Entry<Integer, Integer> aPair : awardsMap.entrySet()) {
			int itemTplId = aPair.getKey();
			Object ret = AwardUtils.changeRes(playingRole, itemTplId, aPair.getValue(),LogConstants.MODULE_CHAPTER);
			if (itemTplId == GameConfig.PLAYER.EXP) {
				addLevel = (Integer)ret;
			}
		}
		// save
		OfflineCache.getInstance().removeOfflineAward(playerId);
		// ret
		S2COfflineAwardDouble respMsg = new S2COfflineAwardDouble(addLevel);
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
