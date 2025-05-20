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
import game.module.activity.logic.ActivityTemplateCache;
import game.entity.TemplateList;
import game.module.award.bean.GameConfig;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.pay.logic.PaymentManager;
import game.module.rank.bean.DBPlayerLevelRank1;
import game.module.rank.dao.RankCache;
import game.module.template.ActivityTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity.*;
import ws.WsMessageBase.*;

import java.util.*;

@MsgCodeAnn(msgcode = 5401, accessLimit = 400)
public class ActivityListProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(ActivityListProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		logger.info("activity list!");
		int playerId = playingRole.getId();
		S2CActivityList respMsg = new S2CActivityList();
		TemplateList fixedActivitiesList = TestActivitiesNewTemplateCache.getInstance().getFixedActivitiesAll();
		respMsg.fixed_activities = new ArrayList<>(fixedActivitiesList.size() - 1);
		for (int i = 0; i < fixedActivitiesList.size(); i++) {
			int activityId = i + 1;
			// 激活码兑换,基金活动 不开放
			if (activityId == 3) {
				continue;
			}
			int levelIndex = 0;
			ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId, activityId);
			if (activityPlayer != null) {
				levelIndex = activityPlayer.getProgress();
			}
			boolean canFixedAwardGet = ActivityPlayerManager.getInstance().canGetFixedAward(playerId, activityId,
					playingRole);
			// 任务进度
			int activityProgress = 0;
			if (activityId == ActivityConstants.TYPE_ARENA_WIN) {
//				ArenaBean arenaBean = ArenaPlayerCache.getInstance().getArenaBean(playerId);
//				if (arenaBean != null) {
//					activityProgress = arenaBean.getWinSum();
//				}
			}
			respMsg.fixed_activities.add(new FixedActivityInfo(levelIndex, activityProgress, activityId,
					canFixedAwardGet));
		}
		// 动态活动
		Date now = new Date();
		Collection<ActivityBean> dynamicActivities = ActivityCache.getInstance().getAllActivity();
		List<ActivityBean> openDynamicActivities = new ArrayList<>();
		for (ActivityBean activityBean : dynamicActivities) {
			// 未开放
			if (activityBean.getIsOpen() == 0) {
				continue;
			}
			// 活动过期
			int templateId = activityBean.getType();
			// 奇珍异宝活动有自己的入口
			if (templateId == ActivityConstants.TYPE_ACTIVITY_TYPE_QI_ZHEN_YI_BAO) {
				continue;
			}
			ActivityTemplate activityTemplate = ActivityTemplateCache.getInstance().getActivityTemplateById(templateId);
			Date endTime = getActivityEndTime(playingRole, activityBean);
			if (activityTemplate.getType() != ActivityConstants.ACTIVITY_TIME_FOREVER && endTime != null
					&& endTime.before(now)) {
				continue;
			}
			openDynamicActivities.add(activityBean);
		}
		openDynamicActivities.sort(new Comparator<ActivityBean>() {
			@Override
			public int compare(ActivityBean o1, ActivityBean o2) {
				ActivityTemplate temp1 = ActivityTemplateCache.getInstance().getActivityTemplateById(o1.getType());
				ActivityTemplate temp2 = ActivityTemplateCache.getInstance().getActivityTemplateById(o2.getType());
				return temp1.getPriority()-temp2.getPriority();
			}
		});
		respMsg.dynamic_activities = new ArrayList<>(openDynamicActivities.size());
		for (ActivityBean activityBean : openDynamicActivities) {
			int templateId = activityBean.getType();
			DynamicActivityInfo dynamicActivityInfo = new DynamicActivityInfo();
			respMsg.dynamic_activities.add(dynamicActivityInfo);
			dynamicActivityInfo.des = activityBean.getDescription();
			dynamicActivityInfo.activeBigID = activityBean.getType();
			dynamicActivityInfo.priority = 1;
			if (activityBean.getStartTime() != null) {
				dynamicActivityInfo.StartTime = (int) (activityBean.getStartTime().getTime() / 1000);
			}
			if (activityBean.getEndTime() != null) {
				dynamicActivityInfo.EndTime = (int) (activityBean.getEndTime().getTime() / 1000);
			}
			switch (templateId) {
			case ActivityConstants.DYNAMIC_TYPE_DAILY_PAY:
				buildDailyPay(dynamicActivityInfo, activityBean, playerId);
				break;
			case ActivityConstants.TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN:
				buildQingChunJiJin(dynamicActivityInfo, activityBean, playerId);
				break;
			case ActivityConstants.TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN:
				buildChengZhangJiJin(dynamicActivityInfo, activityBean, playerId);
				break;
			case ActivityConstants.TYPE_ACTIVITY_TYPE_QUAN_MIN_FU_LI:
				buildQuanMinFuLi(dynamicActivityInfo, activityBean, playerId);
				break;
			case ActivityConstants.DYNAMIC_TYPE_LEVEL_RANK:
				buildChongJiBang(dynamicActivityInfo, activityBean, playerId);
				break;
			case ActivityConstants.TYPE_ACTIVITY_TYPE_LOTTERY_WHEEL:
				buildLotteryWheel(dynamicActivityInfo, activityBean, playerId);
				break;
			case ActivityConstants.TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI:
				buildDanBiChongZhi(dynamicActivityInfo, activityBean, playerId);
				break;
			case ActivityConstants.TYPE_ACTIVITY_TYPE_HERO_LIBAO:
				buildHeroLiBao(dynamicActivityInfo, activityBean, playerId);
				break;
			case ActivityConstants.TYPE_AWARD_DOUBLE:
				buildAwardDouble(dynamicActivityInfo, activityBean, playerId);
				break;
			default:
				buildCommon(dynamicActivityInfo, activityBean, playerId);
				break;
			}
		}
		// ret
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

	private void buildHeroLiBao(DynamicActivityInfo dynamicActivityInfo, ActivityBean activityBean, int playerId) {
		DBActivityHeroLibao dbActivityLevelAward = activityBean.getActivityHeroLibao();
		if (dbActivityLevelAward == null) {
			logger.error("GM没有配置 英雄礼包 活动! DBActivityHeroLibao为NULL");
			return;
		}
		dynamicActivityInfo.nComplete = -1;
		// 青春基金每天重置
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				activityBean.getType());
		List<ActivityConfigHeroLiBao> configList = dbActivityLevelAward.getItemsList();
		dynamicActivityInfo.hero_libao_config = new ArrayList<>(configList.size());
		for (ActivityConfigHeroLiBao levelAward : configList) {
			IoActivityHeroLiBao aHeroLiBao = new IoActivityHeroLiBao();
			dynamicActivityInfo.hero_libao_config.add(aHeroLiBao);
			// 可购买次数
			aHeroLiBao.buy_count = levelAward.getBuyCount();
			aHeroLiBao.buy_count_total = levelAward.getBuyCount();
			aHeroLiBao.favor_rate = levelAward.getFavorRate();
			aHeroLiBao.price = levelAward.getPrice();
			int rechargeId = levelAward.getRechargeId();
			aHeroLiBao.recharge_id = rechargeId;
			aHeroLiBao.extra_diamond = 0;
			List<Award> awards = levelAward.getAwardList();
			if (awards != null && !awards.isEmpty()) {
				aHeroLiBao.reward = new ArrayList<>(awards.size());
				for (Award award : awards) {
					aHeroLiBao.reward.add(new ActivitiesItem(award.getItemId(), award.getItemCount()));
				}
			}
			// 购买次数设置
			if (activityPlayer != null) {
				DBActivityPlayerHeroLibao activityPlayerGet = activityPlayer.getHeroLibaoBuy();
				if (activityPlayerGet != null && activityPlayerGet.getRechargeIdList() != null) {
					List<Integer> rechargeIdList = activityPlayerGet.getRechargeIdList();
					if (rechargeIdList.contains(rechargeId)) {
						int rechargeIdIdx = rechargeIdList.indexOf(rechargeId);
						int existBuyCount = activityPlayerGet.getBuyCountList().get(rechargeIdIdx);
						aHeroLiBao.buy_count = levelAward.getBuyCount() - existBuyCount;
					}
				}
			}
		}
	}

	private void buildDanBiChongZhi(DynamicActivityInfo dynamicActivityInfo, ActivityBean activityBean, int playerId) {
		int curProgress = 0;
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				activityBean.getType());
		dynamicActivityInfo.nComplete = curProgress;
		List<ActivityConfigOne> subItemList = activityBean.getActivityCommon().getItemsList();
		dynamicActivityInfo.sub_activities = new ArrayList<>(subItemList.size());
		// 玩家进度信息
		List<Integer> playerGetList = new ArrayList<>();
		List<Integer> playerGetCountList = new ArrayList<>();
		if (activityPlayer != null && activityPlayer.getActivityPlayerGet() != null
				&& activityPlayer.getActivityPlayerGet().getLevelList() != null) {
			playerGetList = activityPlayer.getActivityPlayerGet().getLevelList();
			playerGetCountList = activityPlayer.getActivityPlayerGet().getVipLevelList();
		}
		for (ActivityConfigOne activityConfigOne : subItemList) {
			ActivityInfoOne activityInfoOne = new ActivityInfoOne();
			dynamicActivityInfo.sub_activities.add(activityInfoOne);
			activityInfoOne.nComplete = 0;
			//能否领取奖励
			int targetLevel = activityConfigOne.getLevel();
			if (playerGetList.contains(targetLevel)) {
				int levelIdx = playerGetList.indexOf(targetLevel);
				int couldGetCount = playerGetCountList.get(levelIdx);
				if (couldGetCount > 0) {
					activityInfoOne.nComplete = couldGetCount;
				}
			}
			activityInfoOne.nState = 0;
			activityInfoOne.nSubCount = targetLevel;
			activityInfoOne.reward = new ArrayList<>(activityConfigOne.getAwardList().size());
			for (Award award : activityConfigOne.getAwardList()) {
				activityInfoOne.reward.add(new ActivitiesItem(award.getItemId(), award.getItemCount()));
			}
		}
	}

	private void buildLotteryWheel(DynamicActivityInfo dynamicActivityInfo, ActivityBean activityBean, int playerId) {
		int curProgress = 0;
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				activityBean.getType());
		if (activityPlayer != null) {
			Date lastVisitTime = activityPlayer.getResetTime();
			if (lastVisitTime != null && DateCommonUtils.isSameDay(lastVisitTime, CommonUtils.RESET_HOUR)) {
				curProgress = activityPlayer.getProgress();
			}
		}
		dynamicActivityInfo.nComplete = curProgress;
		DBActivityLotteryWheel dbActivityLevelAward = activityBean.getActivityLotteryWheel();
		if (dbActivityLevelAward == null) {
			logger.error("GM没有配置 转盘抽奖 活动! DBActivityLotteryWheel为NULL");
			return;
		}
		dynamicActivityInfo.lottery_wheel_config = new IoLotteryWheelConfig();
		dynamicActivityInfo.lottery_wheel_config.daily_free_count = dbActivityLevelAward.getDailyFreeCount();
		dynamicActivityInfo.lottery_wheel_config.pay_count = dbActivityLevelAward.getDailyPayCount();
		dynamicActivityInfo.lottery_wheel_config.pay_price = dbActivityLevelAward.getPayPrice();
		
		dynamicActivityInfo.lottery_wheel_config.my_free_count = dbActivityLevelAward.getDailyFreeCount()-curProgress;
		int myExistPayCount = 0;
		if (activityPlayer != null && activityPlayer.getActivityPlayerGet() != null
				&& activityPlayer.getActivityPlayerGet().getLevelList() != null && activityPlayer.getResetTime() != null
				&& DateCommonUtils.isSameDay(activityPlayer.getResetTime(), CommonUtils.RESET_HOUR)) {
			myExistPayCount = activityPlayer.getActivityPlayerGet().getLevelList().get(0);
		}
		dynamicActivityInfo.lottery_wheel_config.my_pay_count = dbActivityLevelAward.getDailyPayCount()-myExistPayCount;
		//
		List<DBActivityLotteryWheelOne> levelAwards = dbActivityLevelAward.getAwardItemList();
		dynamicActivityInfo.lottery_wheel_config.reward = new ArrayList<>(levelAwards.size());
		for (DBActivityLotteryWheelOne dbActivityLotteryWheelOne : levelAwards) {
			dynamicActivityInfo.lottery_wheel_config.reward.add(new ActivitiesItem(
					dbActivityLotteryWheelOne.getTemplateId(), dbActivityLotteryWheelOne.getCount()));
		}
	}

	private void buildChongJiBang(DynamicActivityInfo dynamicActivityInfo, ActivityBean activityBean, int playerId) {
		int curProgress = 0;
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				activityBean.getType());
		if (activityPlayer != null) {
			curProgress = activityPlayer.getProgress();
		}
		dynamicActivityInfo.nComplete = curProgress;
		List<ActivityConfigOne> subItemList = activityBean.getActivityCommon().getItemsList();
		dynamicActivityInfo.sub_activities = new ArrayList<>(subItemList.size());
		// 玩家进度信息
		List<Integer> playerGetList = new ArrayList<>();
		if (activityPlayer != null && activityPlayer.getActivityPlayerGet() != null
				&& activityPlayer.getActivityPlayerGet().getLevelList() != null) {
			playerGetList = activityPlayer.getActivityPlayerGet().getLevelList();
		}
		List<DBPlayerLevelRank1> levelRankList = RankCache.getInstance().getPlayerLevelRanks();
		int itemIdx = 0;
		for (ActivityConfigOne activityConfigOne : subItemList) {
			ActivityInfoOne activityInfoOne = new ActivityInfoOne();
			dynamicActivityInfo.sub_activities.add(activityInfoOne);
			activityInfoOne.nComplete = 0;
			activityInfoOne.nState = playerGetList.contains(activityConfigOne.getLevel()) ? 2 : 0;
			activityInfoOne.nSubCount = activityConfigOne.getLevel();
			int levelConfig = activityConfigOne.getLevel();
			activityInfoOne.reward = new ArrayList<>(activityConfigOne.getAwardList().size());
			if (levelRankList != null && levelConfig - 1 < levelRankList.size()) {
				// 排行榜名字
				int targetPlayerId = levelRankList.get(levelConfig - 1).getPlayerId();
				if (itemIdx == 0) {
					activityInfoOne.szSubDes = getOfflinePlayerName(targetPlayerId);
				} else {
					int prevLevelConfig = subItemList.get(itemIdx - 1).getLevel();
					if (prevLevelConfig == levelConfig - 1) {
						activityInfoOne.szSubDes = getOfflinePlayerName(targetPlayerId);
					} else {
						targetPlayerId = levelRankList.get(prevLevelConfig).getPlayerId();
						activityInfoOne.szSubDes = getOfflinePlayerName(targetPlayerId) + "等";
					}
				}
			}
			int l = 0;
			for (Award award : activityConfigOne.getAwardList()) {
				activityInfoOne.reward.add(new ActivitiesItem(award.getItemId(), award.getItemCount()));
			}
			itemIdx++;
		}
	}

	private String getOfflinePlayerName(int playerId) {
		String ret = "";
		PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(playerId);
		if (poc != null) {
			ret = poc.getName();
		}
		return ret;
	}

	private void buildChengZhangJiJin(DynamicActivityInfo dynamicActivityInfo, ActivityBean activityBean,
			int playerId) {
		int curProgress = 0;
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				activityBean.getType());
		if (activityPlayer != null) {
			Date startTime = activityPlayer.getResetTime();
			int dayDiff = DateCommonUtils.dayDiff(startTime, new Date());
			curProgress = dayDiff;
		} else {
			curProgress = -1;// 还没购买过成长基金
		}
		dynamicActivityInfo.nComplete = curProgress;
		dynamicActivityInfo.StartTime = 0;
		dynamicActivityInfo.EndTime = 0;
		DBActivityChengZhangJiJin dbActivityLevelAward = activityBean.getDbActivityChengZhangJiJin();
		if (dbActivityLevelAward == null) {
			logger.error("GM没有配置 成长基金 活动! DBActivityChengZhangJiJin为NULL");
			return;
		}
		List<Integer> levelAwards = dbActivityLevelAward.getDiamondDailyList();
		dynamicActivityInfo.sub_activities = new ArrayList<>(levelAwards.size());
		// 玩家进度信息
		List<Integer> playerGetList = new ArrayList<>();
		if (activityPlayer != null && activityPlayer.getActivityPlayerGet() != null
				&& activityPlayer.getActivityPlayerGet().getLevelList() != null) {
			playerGetList = activityPlayer.getActivityPlayerGet().getLevelList();
		}
		int dayCount = 0;
		for (Integer alevelAward : levelAwards) {
			ActivityInfoOne activityInfoOne = new ActivityInfoOne();
			dynamicActivityInfo.sub_activities.add(activityInfoOne);
			activityInfoOne.nComplete = 0;
			activityInfoOne.nState = playerGetList.contains(dayCount) ? 2 : 0;
			activityInfoOne.nSubCount = alevelAward;
			activityInfoOne.reward = Arrays.asList(
					new ActivitiesItem(GameConfig.PLAYER.YB, alevelAward) );
			dayCount++;
		}
	}
	
	private static void buildQuanMinFuLi(DynamicActivityInfo dynamicActivityInfo, ActivityBean activityBean,
			int playerId) {
		int curProgress = PaymentManager.getInstance().getPayPlayers().size();
		dynamicActivityInfo.nComplete = curProgress;
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				activityBean.getType());
		DBActivityQuanMinFuLi dbActivityLevelAward = activityBean.getActivityQuanMinFuLi();
		if (dbActivityLevelAward == null) {
			logger.error("GM没有配置 全民福利 活动! DBActivityQuanMinFuLi为NULL");
			return;
		}
		// send msg
		List<ActivityConfigOneFuLi> levelAwards = dbActivityLevelAward.getItemsList();
		if (levelAwards != null && !levelAwards.isEmpty()) {
			dynamicActivityInfo.sub_activities = new ArrayList<>(levelAwards.size());
			for (ActivityConfigOneFuLi levelAward : levelAwards) {
				ActivityInfoOne activityInfoOne = new ActivityInfoOne();
				dynamicActivityInfo.sub_activities.add(activityInfoOne);
				activityInfoOne.nComplete = levelAward.getVipLevel();
				activityInfoOne.nSubCount = levelAward.getLevel();
				List<Award> awards = levelAward.getAwardList();
				if (awards != null && !awards.isEmpty()) {
					activityInfoOne.reward = new ArrayList<>(awards.size());
					for (Award award : awards) {
						activityInfoOne.reward.add(new ActivitiesItem(award.getItemId(), award.getItemCount()));
					}
				}
				// 是否已经领取
				activityInfoOne.nState = 0;
				if (activityPlayer != null) {
					DBActivityPlayerGet activityPlayerGet = activityPlayer.getActivityPlayerGet();
					if (activityPlayerGet != null) {
						List<Integer> levelList = activityPlayerGet.getLevelList();
						List<Integer> vipLevelList = activityPlayerGet.getVipLevelList();
						int configLevel = levelAward.getLevel();
						int configVipLevel = levelAward.getVipLevel();
						for (int aIndex = 0; aIndex < levelList.size(); aIndex++) {
							if (configLevel == levelList.get(aIndex) && configVipLevel == vipLevelList.get(aIndex)) {
								activityInfoOne.nState = 2;
							}
						}
					}
				}
			}
		}
	}

	private void buildQingChunJiJin(DynamicActivityInfo dynamicActivityInfo, ActivityBean activityBean, int playerId) {
		DBActivityQCJJ dbActivityLevelAward = activityBean.getActivityQCJJ();
		if (dbActivityLevelAward == null) {
			logger.error("GM没有配置 青春基金 活动! DBActivityHeroLibao为NULL");
			return;
		}
		dynamicActivityInfo.nComplete = -1;
		// 青春基金每天重置
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				activityBean.getType());
		if (activityPlayer != null) {
			DBActivityPlayerGet heroLibaoBuy = activityPlayer.getActivityPlayerGet();
			Date oldResetTime = activityPlayer.getResetTime();
			if (oldResetTime == null || !DateCommonUtils.isSameDay(oldResetTime, CommonUtils.RESET_HOUR)) {
				if (heroLibaoBuy != null && heroLibaoBuy.getLevelList() != null) {
					heroLibaoBuy.getLevelList().clear();
				}
			}
		}
		List<ActivityConfigQCJJ> configList = dbActivityLevelAward.getItemsList();
		dynamicActivityInfo.hero_libao_config = new ArrayList<>(configList.size());
		for (ActivityConfigQCJJ levelAward : configList) {
			IoActivityHeroLiBao aHeroLiBao = new IoActivityHeroLiBao();
			dynamicActivityInfo.hero_libao_config.add(aHeroLiBao);
			// 可购买次数
			aHeroLiBao.buy_count = levelAward.getBuyCount();
			aHeroLiBao.buy_count_total = levelAward.getBuyCount();
			aHeroLiBao.favor_rate = levelAward.getTotalValue();
			aHeroLiBao.price = levelAward.getPrice();
			aHeroLiBao.recharge_id = levelAward.getRechargeId();
			aHeroLiBao.extra_diamond = levelAward.getExtraDiamond();
			List<Award> awards = levelAward.getAwardList();
			if (awards != null && !awards.isEmpty()) {
				aHeroLiBao.reward = new ArrayList<>(awards.size());
				for (Award award : awards) {
					aHeroLiBao.reward.add(new ActivitiesItem(award.getItemId(), award.getItemCount()));
				}
			}
			// 购买次数设置
			if (activityPlayer != null) {
				DBActivityPlayerGet activityPlayerGet = activityPlayer.getActivityPlayerGet();
				if (activityPlayerGet != null && activityPlayerGet.getLevelList() != null) {
					if (activityPlayerGet.getLevelList().contains(levelAward.getRechargeId())) {
						aHeroLiBao.buy_count = 0;
					}
				}
			}
		}
	}

	private void buildDailyPay(DynamicActivityInfo dynamicActivityInfo, ActivityBean activityBean, int playerId) {
		int curProgress = 0;
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				activityBean.getType());
		if (activityPlayer != null && activityPlayer.getResetTime() != null
				&& DateCommonUtils.isSameDay(activityPlayer.getResetTime(), CommonUtils.RESET_HOUR)) {
			curProgress = activityPlayer.getProgress();
		}
		dynamicActivityInfo.nComplete = curProgress;
		List<ActivityConfigOne> subItemList = activityBean.getActivityCommon().getItemsList();
		dynamicActivityInfo.sub_activities = new ArrayList<>(subItemList.size());
		// 玩家进度信息
		List<Integer> playerGetList = new ArrayList<>();
		if (activityPlayer != null && activityPlayer.getActivityPlayerGet() != null
				&& activityPlayer.getActivityPlayerGet().getLevelList() != null && activityPlayer.getResetTime() != null
				&& DateCommonUtils.isSameDay(activityPlayer.getResetTime(), CommonUtils.RESET_HOUR)) {
			playerGetList = activityPlayer.getActivityPlayerGet().getLevelList();
		}
		for (ActivityConfigOne activityConfigOne : subItemList) {
			ActivityInfoOne activityInfoOne = new ActivityInfoOne();
			dynamicActivityInfo.sub_activities.add(activityInfoOne);
			activityInfoOne.nComplete = 0;
			activityInfoOne.nState = playerGetList.contains(activityConfigOne.getLevel()) ? 2 : 0;
			activityInfoOne.nSubCount = activityConfigOne.getLevel();
			activityInfoOne.reward = new ArrayList<>(activityConfigOne.getAwardList().size());
			for (Award award : activityConfigOne.getAwardList()) {
				activityInfoOne.reward.add(new ActivitiesItem(award.getItemId(), award.getItemCount()));
			}
		}
	}
	
	private void buildAwardDouble(DynamicActivityInfo dynamicActivityInfo, ActivityBean activityBean, int playerId) {
		int curProgress = 0;
		DBActivityAwardDouble dbActivityAwardDouble = activityBean.getDbActivityAwardDouble();
		if (dbActivityAwardDouble != null) {
			List<AwardDouble> awardDoubles = dbActivityAwardDouble.getAwardDoubleList();
			if (awardDoubles != null && !awardDoubles.isEmpty()) {
				long passDays = DateUtils.getFragmentInDays(new Date(), Calendar.YEAR);
				long idx = (passDays%awardDoubles.size())/2;
				curProgress = (int)idx;
			}
		}
		dynamicActivityInfo.nComplete = curProgress;
		List<AwardDouble> awardDoubleList = dbActivityAwardDouble.getAwardDoubleList();
		dynamicActivityInfo.sub_activities = new ArrayList<>(awardDoubleList.size());
		for (AwardDouble activityConfigOne : awardDoubleList) {
			ActivityInfoOne activityInfoOne = new ActivityInfoOne();
			dynamicActivityInfo.sub_activities.add(activityInfoOne);
			activityInfoOne.nComplete = 0;
			activityInfoOne.nState = 0;
			activityInfoOne.nSubCount = activityConfigOne.getMoudelType();
		}
	}

	private void buildCommon(DynamicActivityInfo dynamicActivityInfo, ActivityBean activityBean, int playerId) {
		int curProgress = 0;
		ActivityPlayer activityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
				activityBean.getType());
		if (activityPlayer != null) {
			curProgress = activityPlayer.getProgress();
		}
		dynamicActivityInfo.nComplete = curProgress;
		List<ActivityConfigOne> subItemList = activityBean.getActivityCommon().getItemsList();
		dynamicActivityInfo.sub_activities = new ArrayList<>(subItemList.size());
		// 玩家进度信息
		List<Integer> playerGetList = new ArrayList<>();
		if (activityPlayer != null && activityPlayer.getActivityPlayerGet() != null
				&& activityPlayer.getActivityPlayerGet().getLevelList() != null) {
			playerGetList = activityPlayer.getActivityPlayerGet().getLevelList();
		}
		int k = 0;
		for (ActivityConfigOne activityConfigOne : subItemList) {
			ActivityInfoOne activityInfoOne = new ActivityInfoOne();
			dynamicActivityInfo.sub_activities.add(activityInfoOne);
			activityInfoOne.nComplete = 0;
			activityInfoOne.nState = playerGetList.contains(activityConfigOne.getLevel()) ? 2 : 0;
			activityInfoOne.nSubCount = activityConfigOne.getLevel();
			activityInfoOne.reward = new ArrayList<>(activityConfigOne.getAwardList().size());
			int l = 0;
			for (Award award : activityConfigOne.getAwardList()) {
				activityInfoOne.reward.add(new ActivitiesItem(award.getItemId(), award.getItemCount()));
			}
		}
	}

	/**
	 * 活动结束时间
	 * 
	 * @param playingRole
	 * @param activityBean
	 * @param type
	 * @return
	 */
	public static Date getActivityEndTime(PlayingRole playingRole, ActivityBean activityBean) {
		Date registTime = playingRole.getPlayerBean().getCreateTime();
		int lastDays = 0;
		if (activityBean.getType() == ActivityConstants.TYPE_LEVEL_GO) {
			lastDays = activityBean.getDbActivityLevelAward().getDayCount();
			Date endTime1 = DateUtils.addDays(registTime, lastDays - 1);
			Calendar endTimeCal = Calendar.getInstance();
			endTimeCal.setTime(endTime1);
			endTimeCal.set(Calendar.HOUR_OF_DAY, 23);
			endTimeCal.set(Calendar.MINUTE, 59);
			endTimeCal.set(Calendar.SECOND, 59);
			endTimeCal.set(Calendar.MILLISECOND, 59);
			Date endTime = endTimeCal.getTime();
			return endTime;
		} else {
			return activityBean.getEndTime();
		}

	}

}
