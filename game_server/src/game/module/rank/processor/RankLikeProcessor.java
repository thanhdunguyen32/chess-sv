package game.module.rank.processor;

import game.common.CommonUtils;
import game.common.DateCommonUtils;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.rank.bean.DbRankLike;
import game.module.rank.dao.RankLikeCache;
import game.module.user.dao.CommonTemplateCache;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageHall.C2SRankLike;
import ws.WsMessageHall.PushSmallTips;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageHall.S2CRankLike;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@MsgCodeAnn(msgcode = C2SRankLike.id, accessLimit = 200)
public class RankLikeProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(RankLikeProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		C2SRankLike reqMsg = C2SRankLike.parse(request);
		int targetPlayerId = reqMsg.targetPlayerId;
		int playerId = playingRole.getId();
		logger.info("rank like,playerId={},targetPlayerId={}", playerId, targetPlayerId);
		// 今日次数
		DbRankLike.DbLikePlayerInfo myLikeInfo = RankLikeCache.getInstance().getMyRankLikeInfo(playerId);
		int todayCount = 0;
		if (myLikeInfo != null && DateCommonUtils.isSameDay(myLikeInfo.getLastVisitTime(), CommonUtils.RESET_HOUR)) {
			todayCount = myLikeInfo.getTodayLikePlayers().size();
		}
		if (todayCount >= 3) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CRankLike.msgCode, 207);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 已经被点赞
		if (myLikeInfo != null && DateCommonUtils.isSameDay(myLikeInfo.getLastVisitTime(), CommonUtils.RESET_HOUR)) {
			if (myLikeInfo.getTodayLikePlayers().contains(targetPlayerId)) {
				S2CErrorCode respMsg = new S2CErrorCode(S2CRankLike.msgCode, 208);
				playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				return;
			}
		}
		// do
		S2CRankLike respMsg = new S2CRankLike();
		if (todayCount == 0) {
			List<List<Integer>> shareAwardConfig = (List<List<Integer>>) CommonTemplateCache.getInstance()
					.getConfig("rank_like_award");
			respMsg.rewards = new ArrayList<>(shareAwardConfig.size());
			for (List<Integer> list : shareAwardConfig) {
				AwardUtils.changeRes(playingRole, list.get(0), list.get(1),LogConstants.MODULE_RANK_LIKE);
				respMsg.rewards.add(new WsMessageBase.RewardInfo(list.get(0), list.get(1)));
			}
		}
		// 保存信息
		RankLikeCache.getInstance().updatePlayerBeLike(targetPlayerId);
		long nowTime = System.currentTimeMillis();
		if (myLikeInfo == null) {
			myLikeInfo = new DbRankLike.DbLikePlayerInfo();
			myLikeInfo.setPlayerId(playerId);
			myLikeInfo.setLastVisitTime(nowTime);
			myLikeInfo.setTodayLikePlayers(new HashSet<>());
			myLikeInfo.getTodayLikePlayers().add(targetPlayerId);
		} else {
			if (!DateCommonUtils.isSameDay(myLikeInfo.getLastVisitTime(), CommonUtils.RESET_HOUR)) {
				if(myLikeInfo.getTodayLikePlayers() != null && myLikeInfo.getTodayLikePlayers().size()>0){
					myLikeInfo.getTodayLikePlayers().clear();
				}
			}
			myLikeInfo.setLastVisitTime(nowTime);
			if(myLikeInfo.getTodayLikePlayers() == null){
				myLikeInfo.setTodayLikePlayers(new HashSet<>());
			}
			myLikeInfo.getTodayLikePlayers().add(targetPlayerId);
		}
		RankLikeCache.getInstance().updateMyRankLike(playerId, myLikeInfo);
		//push
		PlayingRole targetPr = SessionManager.getInstance().getPlayer(targetPlayerId);
		if (targetPr != null) {
			PushSmallTips pushSmallTips = new PushSmallTips(1,
					"点赞：Lv." + targetPr.getPlayerBean().getLevel() + "【" + targetPr.getPlayerBean().getName() + "】+1");
			targetPr.writeAndFlush(pushSmallTips.build(targetPr.alloc()));
		}
		// ret
		respMsg.targetPlayerId = targetPlayerId;
		respMsg.likeCount = RankLikeCache.getInstance().getPlayerBeLikeCount(targetPlayerId);
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
