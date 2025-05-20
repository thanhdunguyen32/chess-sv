package game.module.user.processor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.logic.ActivityManager;
import game.module.user.logic.PlayerManager;
import lion.common.SimpleTextConvert;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import ws.WsMessageHall.C2SListRedPoints;
import ws.WsMessageHall.S2CListRedPoints;

@MsgCodeAnn(msgcode = C2SListRedPoints.id, accessLimit = 200)
public class ListRedPointsProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(ListRedPointsProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		int playerId = playingRole.getId();
		logger.info("list red points!playerId={}", playerId);
		S2CListRedPoints respMsg = new S2CListRedPoints();
		int[] red_points = PlayerManager.getInstance().getRedPointInfo(playingRole);
//		respMsg.has_redpoint = red_points;
		// 活动
		List<Integer> activityRedpoints = ActivityManager.getInstance().checkRedPoints(playerId, playingRole);
		Integer[] activityRedPoints = activityRedpoints.toArray(new Integer[] {});
		int[] activityRedPointsRaw = SimpleTextConvert.intObj2Raw(activityRedPoints);
//		respMsg.activity_redpoints = activityRedPointsRaw;
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
