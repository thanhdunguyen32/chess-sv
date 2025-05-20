package game.module.activity.processor;

import game.common.CommonUtils;
import game.common.DateCommonUtils;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.*;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.dao.ActivityCache;
import game.module.activity.dao.ActivityPlayerCache;
import game.module.activity.dao.TestActivitiesNewTemplateCache;
import game.module.activity.logic.ActivityPlayerManager;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.pay.logic.PaymentManager;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase.ActivitiesItem;
import ws.WsMessageActivity.C2SActivitiesGetAward;
import ws.WsMessageActivity.S2CActivitiesGetAward;
import ws.WsMessageHall.S2CErrorCode;

import java.util.*;

@MsgCodeAnn(msgcode = C2SActivitiesGetAward.id, accessLimit = 400)
public class ActivityGetAwardProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(ActivityGetAwardProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		int playerId = playingRole.getId();
		C2SActivitiesGetAward reqMsg = C2SActivitiesGetAward.parse(request);
		int activeIdType = reqMsg.activeIdType;
		int item_index = reqMsg.item_index;
		logger.info("activity get award,palyerId={},activeIdType={},item_index={}", playerId, activeIdType, item_index);
		if (activeIdType <= 10) {
			// 固定任务奖励
			getFixedAward(activeIdType, playingRole);
		} else {
			// 活动是否有效
			ActivityBean activityBean = ActivityCache.getInstance().getOneActivity(activeIdType);
			if (activityBean == null || activityBean.getIsOpen() == ActivityConstants.NO) {
				S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 201);
				playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
				return;
			}
			// 活动是否在时间内
			if (activityBean.getType() == ActivityConstants.ACTIVITY_TIME_RANGE) {
				Date startTime = activityBean.getStartTime();
				Date endTime = activityBean.getEndTime();
				if (!DateCommonUtils.betweenDate(startTime, endTime)) {
					S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 202);
					playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
					return;
				}
			}
			switch (activeIdType) {
			case ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN:
				getChengZhangJJAward(activeIdType, playingRole, activityBean);
				break;
			case ActivityConstants.TYPE_ACTIVITY_TYPE_QUAN_MIN_FU_LI:
				quanMinFuLiGetAward(playingRole, activityBean,activeIdType, item_index);
				break;
			case ActivityConstants.TYPE_ACTIVITY_TYPE_LOTTERY_WHEEL:
				getLotteryWheelAward(activeIdType, playingRole, activityBean);
				break;
			case ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI:
				getDanBiChongZhiAward(activeIdType, playingRole,item_index, activityBean);
				break;
			default:
				getDynamicAward(activeIdType, playingRole, item_index, activityBean);
				break;
			}
		}
	}

	private void getDanBiChongZhiAward(int activeIdType, PlayingRole playingRole,int item_index, ActivityBean activityTemplate) {
		int playerId = playingRole.getId();
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId, activeIdType);
		// 活动不存在或者未开启
		if (activityPlayer == null) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 130);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		// 能否领取
		List<ActivityConfigOne> subItemList = activityTemplate.getActivityCommon().getItemsList();
		if (item_index >= subItemList.size()) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 130);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		ActivityConfigOne activityConfigOne = subItemList.get(item_index);
		int needLevel = activityConfigOne.getLevel();
		// 当前level
		DBActivityPlayerGet dbActivityPlayerGet = activityPlayer.getActivityPlayerGet();
		if(dbActivityPlayerGet == null || dbActivityPlayerGet.getLevelList() == null) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 203);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		//是否达到条件
		List<Integer> playerCanGetList = dbActivityPlayerGet.getLevelList();
		if (!playerCanGetList.contains(needLevel)) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 203);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		// 已经领取
		List<Integer> canGetCountList = dbActivityPlayerGet.getVipLevelList();
		int levelIdx = playerCanGetList.indexOf(needLevel);
		int canGetCount = canGetCountList.get(levelIdx);
		if (canGetCount <= 0) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 204);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		// 保存记录
		activityPlayer.getActivityPlayerGet().getVipLevelList().set(levelIdx, --canGetCount);
		ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		// 发奖励
		Map<Integer, Integer> rewardAll = new HashMap<>();
		List<Award> awardList = activityConfigOne.getAwardList();
		for (Award award : awardList) {
			AwardUtils.changeRes(playingRole, award.getItemId(), award.getItemCount(),
					LogConstants.MODULE_ACTIVITY);
			rewardAll.put(award.getItemId(), award.getItemCount());
		}
		// ret
		S2CActivitiesGetAward respMsg = new S2CActivitiesGetAward();
		respMsg.activeIdType = activeIdType;
		respMsg.item_index = item_index;
		respMsg.gain = new ArrayList<>(rewardAll.size());
		for (Map.Entry<Integer, Integer> map : rewardAll.entrySet()) {
			respMsg.gain.add(new ActivitiesItem(map.getKey(), map.getValue()));
		}
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

	private boolean getLotteryWheelAward(int activityType, PlayingRole playingRole, ActivityBean activityBean) {
		DBActivityLotteryWheel dbActivityLotteryWheel = activityBean.getActivityLotteryWheel();
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playingRole.getId(),
				activityType);
		//免费or付费
		int progress = 0;
		if (activityPlayer != null && activityPlayer.getResetTime() != null
				&& DateCommonUtils.isSameDay(activityPlayer.getResetTime(), CommonUtils.RESET_HOUR)) {
			progress = activityPlayer.getProgress();
		}
		boolean isPay = false;
		if(progress>= dbActivityLotteryWheel.getDailyFreeCount()) {
			isPay = true;
		}
		if(isPay) {
			//是否次数用完
			if(activityPlayer.getActivityPlayerGet() != null && activityPlayer.getActivityPlayerGet().getLevelList() != null) {
				int payCount = activityPlayer.getActivityPlayerGet().getLevelList().get(0);
				if(payCount>=dbActivityLotteryWheel.getDailyPayCount()) {
					S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 206);
					playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
					return false;
				}
			}
			//金块不足
			if(playingRole.getPlayerBean().getMoney()<dbActivityLotteryWheel.getPayPrice()) {
				S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 132);
				playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
				return false;
			}
		}
		//do
		List<DBActivityLotteryWheelOne> rewardList = dbActivityLotteryWheel.getAwardItemList();
		RandomDispatcher<Integer> rd = new RandomDispatcher<>();
		int i=0;
		for (DBActivityLotteryWheelOne dbActivityLotteryWheelOne : rewardList) {
			rd.put(dbActivityLotteryWheelOne.getRate(), i++);
		}
		int awardIdx = rd.random();
		DBActivityLotteryWheelOne awardOne = rewardList.get(awardIdx);
		// 发送奖励
		AwardUtils.changeRes(playingRole, awardOne.getTemplateId(),awardOne.getCount(), 100 + activityType);
		//保存记录
		if(isPay) {
			DBActivityPlayerGet activityPlayerGet = activityPlayer.getActivityPlayerGet();
			if (activityPlayerGet == null || activityPlayerGet.getLevelList() == null) {
				activityPlayerGet = new DBActivityPlayerGet();
				activityPlayerGet.setLevelList(new ArrayList<Integer>());
				activityPlayer.setActivityPlayerGet(activityPlayerGet);
				activityPlayerGet.getLevelList().add(1);
			}else {
				int oldLevel = activityPlayerGet.getLevelList().get(0);
				oldLevel++;
				activityPlayerGet.getLevelList().set(0, oldLevel);
			}
		}else {
			if(activityPlayer == null) {
				activityPlayer = new ActivityPlayer();
				activityPlayer.setPlayerId(playingRole.getId());
				activityPlayer.setProgress(++progress);
				activityPlayer.setType(activityType);
				activityPlayer.setResetTime(new Date());
			} else {
				activityPlayer.setProgress(++progress);
				// 清空付费购买记录
				DBActivityPlayerGet activityPlayerGet = activityPlayer.getActivityPlayerGet();
				if (!DateCommonUtils.isSameDay(activityPlayer.getResetTime(), CommonUtils.RESET_HOUR)) {
					if (activityPlayerGet != null && activityPlayerGet.getLevelList() != null) {
						activityPlayerGet.getLevelList().set(0, 0);
					}
				}
				activityPlayer.setResetTime(new Date());
			}
		}
		//扣金块
		if (isPay) {
			AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -dbActivityLotteryWheel.getPayPrice(), 100 + activityType);
		}
		// 保存记录
		if(activityPlayer.getId() == null) {
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		}else {
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
		// ret
		S2CActivitiesGetAward respMsg = new S2CActivitiesGetAward();
		respMsg.activeIdType = activityType;
		respMsg.item_index = awardIdx;
		respMsg.lottery_is_pay = isPay;
		respMsg.gain = new ArrayList<>(1);
		respMsg.gain.add(new ActivitiesItem(awardOne.getTemplateId(),awardOne.getCount()));
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
		return true;
	}

	private boolean getChengZhangJJAward(int activityType, PlayingRole playingRole, ActivityBean activityBean) {
		DBActivityChengZhangJiJin dbActivityChengZhangJiJin = activityBean.getDbActivityChengZhangJiJin();
		List<Integer> daimondDailyList = dbActivityChengZhangJiJin.getDiamondDailyList();
		// 奖励已经领取
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playingRole.getId(),
				activityType);
		if (activityPlayer == null) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 203);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return false;
		}
		// 是否达到条件
		Date startDay = activityPlayer.getResetTime();
		int progress = DateCommonUtils.dayDiff(startDay, new Date());
		if (progress >= daimondDailyList.size() || progress < 0) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 205);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return false;
		}
		// 是否已经领取
		DBActivityPlayerGet dbActivityPlayerGet = activityPlayer.getActivityPlayerGet();
		if (dbActivityPlayerGet != null) {
			List<Integer> dayList = dbActivityPlayerGet.getLevelList();
			if (dayList != null && dayList.contains(progress)) {
				S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 204);
				playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
				return false;
			}
		}
		// 领取操作
		DBActivityPlayerGet activityPlayerGet = activityPlayer.getActivityPlayerGet();
		if (activityPlayerGet == null || activityPlayerGet.getLevelList() == null) {
			activityPlayerGet = new DBActivityPlayerGet();
			activityPlayerGet.setLevelList(new ArrayList<Integer>());
			activityPlayer.setActivityPlayerGet(activityPlayerGet);
		}
		activityPlayerGet.getLevelList().add(progress);
		ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		// 发送奖励
		int rewardDiamond = daimondDailyList.get(progress);
		AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, rewardDiamond, 100 + activityType);
		// ret
		S2CActivitiesGetAward respMsg = new S2CActivitiesGetAward();
		respMsg.activeIdType = activityType;
		respMsg.item_index = progress;
		respMsg.gain = new ArrayList<>(1);
		respMsg.gain.add(new ActivitiesItem(GameConfig.PLAYER.YB, rewardDiamond));
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
		return true;
	}
	
	private boolean quanMinFuLiGetAward(PlayingRole playingRole, ActivityBean activityBean, int activityType,
			int levelIndex) {
		DBActivityQuanMinFuLi dbActivityQuanMinFuLi = activityBean.getActivityQuanMinFuLi();
		List<ActivityConfigOneFuLi> configList = dbActivityQuanMinFuLi.getItemsList();
		// 奖励已经领取
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playingRole.getId(),
				activityType);
		// 获取目标项
		if(levelIndex >= configList.size()) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 130);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return false;
		}
		// 目标项
		ActivityConfigOneFuLi target = configList.get(levelIndex);
		int vipLevel = target.getVipLevel();
		// VIP是否满足
		if (target.getVipLevel() != null && target.getVipLevel() > 0
				&& playingRole.getPlayerBean().getVipLevel() < target.getVipLevel()) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 209);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return false;
		}
		// 是否达到条件
		int payCount = PaymentManager.getInstance().getPayPlayers().size();
		if (payCount < target.getLevel()) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 203);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return false;
		}
		// 是否已经领取
		if (activityPlayer != null) {
			DBActivityPlayerGet dbActivityPlayerGet = activityPlayer.getActivityPlayerGet();
			if (dbActivityPlayerGet != null) {
				List<Integer> levelList = dbActivityPlayerGet.getLevelList();
				for (int i = 0; i < levelList.size(); i++) {
					int payCountConfig = levelList.get(i);
					int vipLevelConfig = dbActivityPlayerGet.getVipLevelList().get(i);
					if (payCountConfig == target.getLevel() && vipLevelConfig == vipLevel) {
						S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 204);
						playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
						return false;
					}
				}
			}
		}
		// 领取操作
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playingRole.getId());
			activityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_QUAN_MIN_FU_LI);
			activityPlayer.setProgress(1);
			DBActivityPlayerGet activityPlayerGet = new DBActivityPlayerGet();
			List<Integer> levelList = new ArrayList<>();
			levelList.add(target.getLevel());
			activityPlayerGet.setLevelList(levelList);
			List<Integer> vipList = new ArrayList<>();
			vipList.add(vipLevel);
			activityPlayerGet.setVipLevelList(vipList);
			activityPlayer.setActivityPlayerGet(activityPlayerGet);

			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
		} else {
			DBActivityPlayerGet activityPlayerGet = activityPlayer.getActivityPlayerGet();
			if (activityPlayerGet == null || activityPlayerGet.getLevelList() == null) {
				activityPlayerGet = new DBActivityPlayerGet();
				activityPlayerGet.setLevelList(new ArrayList<Integer>());
				activityPlayerGet.setVipLevelList(new ArrayList<Integer>());
				activityPlayer.setActivityPlayerGet(activityPlayerGet);
			}
			activityPlayerGet.getLevelList().add(target.getLevel());
			activityPlayerGet.getVipLevelList().add(vipLevel);
			ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		}
		// 发送奖励
		Map<Integer, Integer> rewardAll = new HashMap<>();
		List<Award> awards = target.getAwardList();
		for (Award award : awards) {
			AwardUtils.changeRes(playingRole, award.getItemId(), award.getItemCount(), 100 + activityType);
			rewardAll.put(award.getItemId(), award.getItemCount());
		}
		// ret
		S2CActivitiesGetAward respMsg = new S2CActivitiesGetAward();
		respMsg.activeIdType = activityType;
		respMsg.item_index = levelIndex;
		respMsg.gain = new ArrayList<>(rewardAll.size());
		for (Map.Entry<Integer, Integer> map : rewardAll.entrySet()) {
			respMsg.gain.add(new ActivitiesItem(map.getKey(), map.getValue()));
		}
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
		return true;
	}

	private void getDynamicAward(int activeIdType, PlayingRole playingRole, int item_index,
			ActivityBean activityTemplate) {
		int playerId = playingRole.getId();
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId, activeIdType);
		// 活动不存在或者未开启
		if (activityPlayer == null) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 130);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		// 能否领取
		List<ActivityConfigOne> subItemList = activityTemplate.getActivityCommon().getItemsList();
		if (item_index >= subItemList.size()) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 130);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		ActivityConfigOne activityConfigOne = subItemList.get(item_index);
		int needLevel = activityConfigOne.getLevel();
		// 当前level
		int curProgress = activityPlayer.getProgress();
		if (curProgress < needLevel) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 130);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		// 已经领取
		if (activityPlayer.getActivityPlayerGet() != null
				&& activityPlayer.getActivityPlayerGet().getLevelList() != null
				&& activityPlayer.getActivityPlayerGet().getLevelList().contains(needLevel)) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 130);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		// 保存记录
		if (activityPlayer.getActivityPlayerGet() == null) {
			DBActivityPlayerGet activityPlayerGet = new DBActivityPlayerGet();
			activityPlayerGet.setLevelList(new ArrayList<Integer>());
			activityPlayerGet.getLevelList().add(needLevel);
			activityPlayer.setActivityPlayerGet(activityPlayerGet);
		} else if (activityPlayer.getActivityPlayerGet().getLevelList() == null) {
			DBActivityPlayerGet activityPlayerGet = activityPlayer.getActivityPlayerGet();
			activityPlayerGet.setLevelList(new ArrayList<Integer>());
			activityPlayerGet.getLevelList().add(needLevel);
		} else {
			activityPlayer.getActivityPlayerGet().getLevelList().add(needLevel);
		}
		ActivityPlayerManager.getInstance().asyncUpdateActivityAll(activityPlayer);
		// 发奖励
		Map<Integer, Integer> rewardAll = new HashMap<>();
		List<Award> awardList = activityConfigOne.getAwardList();
		for (Award award : awardList) {
			AwardUtils.changeRes(playingRole, award.getItemId(), award.getItemCount(),
					LogConstants.MODULE_ACTIVITY);
			rewardAll.put(award.getItemId(), award.getItemCount());
		}
		// ret
		S2CActivitiesGetAward respMsg = new S2CActivitiesGetAward();
		respMsg.activeIdType = activeIdType;
		respMsg.item_index = item_index;
		respMsg.gain = new ArrayList<>(rewardAll.size());
		for (Map.Entry<Integer, Integer> map : rewardAll.entrySet()) {
			respMsg.gain.add(new ActivitiesItem(map.getKey(), map.getValue()));
		}
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

	private void getFixedAward(int activeIdType, PlayingRole playingRole) {
		int playerId = playingRole.getId();
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId, activeIdType);
		Map<String, Object> activity1Config = TestActivitiesNewTemplateCache.getInstance()
				.getFixedActivity(activeIdType);
		boolean canGetFixedAward = ActivityPlayerManager.getInstance().canGetFixedAward(playerId, activeIdType,
				playingRole);
		if (!canGetFixedAward) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CActivitiesGetAward.msgCode, 130);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		// do
		Date now = new Date();
		if (activityPlayer == null) {
			activityPlayer = new ActivityPlayer();
			activityPlayer.setPlayerId(playerId);
			activityPlayer.setType(activeIdType);
			activityPlayer.setProgress(1);
			// 生成重置时间
			if (activeIdType == ActivityConstants.TYPE_LOGIN) {
				Date newDate = DateUtils.addHours(now, -CommonUtils.RESET_HOUR);
				newDate = DateUtils.truncate(newDate, Calendar.DATE);
				newDate = DateUtils.addDays(newDate, 1);
				newDate = DateUtils.addHours(newDate, CommonUtils.RESET_HOUR);
				activityPlayer.setResetTime(newDate);
			}
			ActivityPlayerManager.getInstance().asyncInsertActivityPlayer(activityPlayer);
			ActivityPlayerCache.getInstance().addActivityPlayer(activityPlayer);
		} else {
			activityPlayer.setProgress(activityPlayer.getProgress() + 1);
			if (activeIdType == ActivityConstants.TYPE_LOGIN) {
				// 生成重置时间
				Date newDate = DateUtils.addHours(now, -CommonUtils.RESET_HOUR);
				newDate = DateUtils.truncate(newDate, Calendar.DATE);
				newDate = DateUtils.addDays(newDate, 1);
				Date resetTime = DateUtils.addHours(newDate, CommonUtils.RESET_HOUR);
				activityPlayer.setResetTime(resetTime);
			}
			ActivityPlayerManager.getInstance().asyncUpdateActivityProgerss(activityPlayer);
		}
		// 发奖励
		Map<Integer, Integer> rewardAll = new HashMap<>();
		List<Map<String, Object>> rewardList = (List<Map<String, Object>>) activity1Config.get("rewards");
		int rewardIndex = activityPlayer.getProgress() - 1;
		if (rewardIndex >= 0 && rewardIndex < rewardList.size()) {
			Map<String, Object> reward1Config = rewardList.get(rewardIndex);
			int moneyConfig = (int) reward1Config.get("money");
			int gamemoneyConfig = (int) reward1Config.get("gamemoney");
			List<List<Integer>> itemsConfig = (List<List<Integer>>) reward1Config.get("items");
			if (moneyConfig > 0) {
				AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, moneyConfig,
						LogConstants.MODULE_ACTIVITY);
				rewardAll.put(GameConfig.PLAYER.YB, moneyConfig);
			}
			if (gamemoneyConfig > 0) {
				AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, gamemoneyConfig,
						LogConstants.MODULE_ACTIVITY);
				rewardAll.put(GameConfig.PLAYER.GOLD, gamemoneyConfig);
			}
			if (itemsConfig.size() > 0) {
				for (List<Integer> list : itemsConfig) {
					AwardUtils.changeRes(playingRole, list.get(0), list.get(1), LogConstants.MODULE_ACTIVITY);
					rewardAll.put(list.get(0), list.get(1));
				}
			}
		}
		S2CActivitiesGetAward respMsg = new S2CActivitiesGetAward();
		respMsg.activeIdType = activeIdType;
		respMsg.item_index = activityPlayer.getProgress();
		respMsg.gain = new ArrayList<>(rewardAll.size());
		for (Map.Entry<Integer, Integer> map : rewardAll.entrySet()) {
			respMsg.gain.add(new ActivitiesItem(map.getKey(), map.getValue()));
		}
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
