package game.module.activity.constants;

/**
 * 活动常量
 * 
 * @author zhangning
 * 
 * @Date 2015年7月21日 下午2:14:24
 */
public class ActivityConstants {

	public static final byte NO = 0;

	public static final byte YES = 1;
	
	public static final int DAILY_RESET_TIME = 5;

	/**
	 * 定时器: 每日答题类型
	 */
	public static final int TIMER_TYPE_ANSWER_DAILY = 0;

	/**
	 * 题目状态: 未答、正确、错误
	 */
	public static final int ANSWER_STATE_UNANSWERED = 0;
	public static final int ANSWER_STATE_RIGHT = 1;
	public static final int ANSWER_STATE_WRONG = 2;

	/**
	 * 奖励可领
	 */
	public static final int TOTAL_AWARD_CAN_GET = 1;

	/**
	 * 奖励已领
	 */
	public static final int TOTAL_AWARD_GETTED = 2;

	public static final int ACTIVITY_BORN = 1;

	public static final int ACTIVITY_TIME_INDISCIPLINE = 1;
	public static final int ACTIVITY_TIME_RANGE = 2;
	public static final int ACTIVITY_TIME_FOREVER = 3;

	/**
	 * 随机问题的个数
	 */
	public static final int ANSWER_RANDOM_CNT = 10;

	/**
	 * 答题额外奖励类型为2
	 */
	public static final int ANSWER_TOTAL_AWARD_TYPE = 2;

	/**
	 * 活动奖励发放邮件
	 */
	public static final int ACTIVITY_MAIL = 1003;

	/**
	 * 答题奖励: 1每题奖励, 2额外奖励
	 */
	public static final int ANSWER_COMMON_AWARD = 1;
	public static final int ANSWER_TOTAL_AWARD = 2;

	// --------------------------------------------------------------//
	// 活动类型 (1充值奖励, 2消费反馈, 3登录奖励, 4答题活动, 5冲级奖励, 6奖励翻倍, 7钻石矿井, 8折扣季)
	/**
	 * 1累计充值奖励
	 */
	public static final int DYNAMIC_TYPE_RECHARGE = 11;
	
	public static final int DYNAMIC_TYPE_DAILY_PAY = 14;

	/**
	 * 2消费反馈
	 */
	public static final int TYPE_CONSUMPTION = 15;

	/**
	 * 3登录奖励
	 */
	public static final int TYPE_LOGIN = 1;
	
	public static final int TYPE_STAGE = 4;
	
	public static final int TYPE_ELITE_STAGE = 5;
	
	public static final int TYPE_ARENA_WIN = 6;
	
	public static final int TYPE_FREE_FUND = 7;

	/**
	 * 4答题活动
	 */
	public static final int TYPE_ANSWER = 4;

	/**
	 * 5冲级奖励
	 */
	public static final int TYPE_LEVEL_GO = 2;

	/**
	 * 6奖励翻倍
	 */
	public static final int TYPE_AWARD_DOUBLE = 106;

	/**
	 * 7钻石矿井
	 */
	public static final int TYPE_MINE = 7;

	/**
	 * 折扣季
	 */
	public static final int TYPE_SALE = 8;
	
	public static final int DYNAMIC_TYPE_LOGIN = 16;
	
	public static final int DYNAMIC_TYPE_BUY_DUKANG = 22;
	
	public static final int DYNAMIC_TYPE_BUY_DUKANG10 = 23;
	/**
	 * 冲级榜
	 */
	public static final int DYNAMIC_TYPE_LEVEL_RANK = 24;
	
	public static final int TYPE_ACTIVITY_TYPE_KAI_FU_JING_SAI = 109;
	public static final int TYPE_ACTIVITY_TYPE_CHENG_ZHANG_JI_JIN = 111;
	public static final int TYPE_ACTIVITY_TYPE_QUAN_MIN_FU_LI = 112;
	public static final int TYPE_ACTIVITY_TYPE_VIP_LIBAO = 113;
	public static final int TYPE_ACTIVITY_TYPE_DAILY_ACTIVE = 114;
	public static final int TYPE_ACTIVITY_TYPE_3DAY_FIRST_RECHARGE = 115;
	public static final int TYPE_ACTIVITY_TYPE_HERO_LIBAO = 116;
	public static final int TYPE_ACTIVITY_TYPE_LOTTERY_WHEEL = 117;
	public static final int TYPE_ACTIVITY_TYPE_FIRST_RECHARGE_TRIPLE = 118;
	public static final int TYPE_ACTIVITY_TYPE_QI_ZHEN_YI_BAO = 119;
	public static final int TYPE_ACTIVITY_TYPE_MEI_RI_SHOU_CHONG = 120;
	public static final int TYPE_ACTIVITY_TYPE_DAN_BI_CHONG_ZHI = 121;
	public static final int TYPE_ACTIVITY_TYPE_LIAN_XU_CHONG_ZHI = 122;
	public static final int TYPE_ACTIVITY_TYPE_SHI_LIAN_JIANG_LI = 123;
	public static final int TYPE_ACTIVITY_TYPE_CHONG_ZHI_BANG = 124;
	public static final int TYPE_ACTIVITY_TYPE_XIAO_FEI_BANG = 125;
	
	public static final int TYPE_ACTIVITY_TYPE_CROSS_BOSS = 126;
	
	public static final int TYPE_ACTIVITY_TYPE_TIAN_TIAN_HAO_LI = 127;
	
	public static final int TYPE_ACTIVITY_TYPE_QING_CHUN_JI_JIN = 128;
	public static final int TYPE_GM = 129;


	// --------------------------------------------------------------//
	// 奖励加倍类型 (381普通关卡, 382精英关卡, 7竞技场, 5疯狂实验室, 19秘密基地, 12天空塔)
	/**
	 * 1普通关卡
	 */
	public static final int AWARD_DOUBLE_STAGE = 1;
	/**
	 * 过关斩将
	 */
	public static final int AWARD_DOUBLE_OVERCOME = 2;

	/**
	 * 3竞技场
	 */
	public static final int AWARD_DOUBLE_ARENA = 3;

	/**
	 * 活动副本
	 */
	public static final int AWARD_DOUBLE_ACTIVITY_COPY = 4;

	/**
	 * 5秘密基地
	 */
	public static final int AWARD_DOUBLE_SECRET = 5;

	/**
	 * 矿场
	 */
	public static final int AWARD_DOUBLE_MINE = 6;
	
	public static final int AWARD_DOUBLE_GUOZHAN = 7;

	/**
	 * PVP奖励翻倍
	 */
	public static final int AWARD_DOUBLE_PVP = 101;
	/**
	 * 诸神争霸奖励翻倍
	 */
	public static final int AWARD_DOUBLE_CRAFT = 102;
	
	public static final int ACTIVE_DAILY_TASK = 50;
	
	public static final int ACTIVE_RECHARGE = 1;
	
	public static final float ACTIVE_CONSUME = 0.5f;

	public static final int GXDB_PROGRESS_MARK = 90310;

	public static final int DJJF_PROGRESS_MARK = 90283;

	public static final int DJRW_ALL_REWARD_MARK = 90332;

	public static final int ZMJF_PROGRESS_MARK = 90276;

}
