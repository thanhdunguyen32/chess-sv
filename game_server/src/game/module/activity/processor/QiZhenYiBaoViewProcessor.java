package game.module.activity.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActivityBean;
import game.module.activity.bean.Award;
import game.module.activity.bean.DBActivityQiZhenYiBao;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.dao.ActivityCache;
import game.module.activity.dao.ActivityQZYBCache;
import game.module.activity.logic.ActivityTemplateCache;
import game.module.db.bean.DBQZYBPlayer1;
import game.module.template.ActivityTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import ws.WsMessageBase.ActivitiesItem;
import ws.WsMessageActivity.C2SQiZhenYiBaoView;
import ws.WsMessageBase.QiZhenYiBaoPlayer;
import ws.WsMessageActivity.S2CQiZhenYiBaoView;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2SQiZhenYiBaoView.id, accessLimit = 400)
public class QiZhenYiBaoViewProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(QiZhenYiBaoViewProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		logger.info("qi zhen yi bao view panel!,playerId={}", playingRole.getId());
		ActivityTemplate activityTemplate = ActivityTemplateCache.getInstance()
				.getActivityTemplateById(ActivityConstants.TYPE_ACTIVITY_TYPE_QI_ZHEN_YI_BAO);
		if (activityTemplate == null) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CQiZhenYiBaoView.msgCode, 201);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		ActivityBean activityBean = ActivityCache.getInstance()
				.getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_QI_ZHEN_YI_BAO);
		if (activityBean == null || activityBean.getActivityQiZhenYiBao() == null) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CQiZhenYiBaoView.msgCode, 201);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 时间不对
		Date now = new Date();
		if (activityBean.getStartTime().after(now) || activityBean.getEndTime().before(now)) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CQiZhenYiBaoView.msgCode, 202);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 发送信息
		DBActivityQiZhenYiBao qiZhenYiBaoConfig = activityBean.getActivityQiZhenYiBao();
		int myCount = 0;
		DBQZYBPlayer1 myBuyInfo = ActivityQZYBCache.getInstance().getMyBuyInfo(playingRole.getId());
		if (myBuyInfo != null) {
			myCount = myBuyInfo.getCount();
		}
		S2CQiZhenYiBaoView respMsg = new S2CQiZhenYiBaoView();
		respMsg.player_count_kaijiang = qiZhenYiBaoConfig.getPlayerCount();
		respMsg.cost_diamond = qiZhenYiBaoConfig.getCostDiamond();
		respMsg.my_count = myCount;
		respMsg.end_cd_time = (int) ((activityBean.getEndTime().getTime()-System.currentTimeMillis()) / 1000);
		respMsg.big_award_value = qiZhenYiBaoConfig.getBigAwardValue();
		respMsg.fixed_award = new ArrayList<>(qiZhenYiBaoConfig.getFixedAwardList().size());
		int i = 0;
		for (Award aAward : qiZhenYiBaoConfig.getFixedAwardList()) {
			respMsg.fixed_award.add(new ActivitiesItem(aAward.getItemId(), aAward.getItemCount()));
		}
		respMsg.big_award = new ActivitiesItem(qiZhenYiBaoConfig.getBigAward().getItemId(),
				qiZhenYiBaoConfig.getBigAward().getItemCount());
		// 参与信息
		Collection<DBQZYBPlayer1> buyInfoList = ActivityQZYBCache.getInstance().getPlayerBuyInfo();
		if (buyInfoList != null) {
			respMsg.players = new ArrayList<>(buyInfoList.size());
			int j = 0;
			for (DBQZYBPlayer1 dbqzybPlayer1 : buyInfoList) {
				respMsg.players.add(new QiZhenYiBaoPlayer(dbqzybPlayer1.getPlayerId(), dbqzybPlayer1.getPlayerName(), dbqzybPlayer1.getCount()));
			}
		}
		// 开奖信息
		List<String> kaijiangList = ActivityQZYBCache.getInstance().getKaiJiangLog();
		if (kaijiangList != null) {
			int roundIndex = 0;
			respMsg.history_players = new ArrayList<>(kaijiangList.size());
			for (String winPlayerName : kaijiangList) {
				respMsg.history_players.add(new QiZhenYiBaoPlayer(0, winPlayerName,roundIndex + 1));
				roundIndex++;
			}
		}
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
