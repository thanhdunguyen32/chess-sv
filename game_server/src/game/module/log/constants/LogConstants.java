package game.module.log.constants;

/**
 * 日志常量
 * 
 * @author zhangning
 * 
 * @Date 2015年5月28日 上午11:20:04
 */
public class LogConstants {

	public static final byte NO = 0;

	public static final byte YES = 1;

	// ----------------------------------货币消耗与收益, 模块类型---------------------------------------//

	/**
	 * 其他模块
	 */
	public static final int MODULE_ALL = 0;

	/**
	 * 装备合成
	 */
	public static final int MODULE_EQUIP_HECHENG = 1;

	/**
	 * 商店购买
	 */
	public static final int MODULE_STORE_BUY = 2;

	/**
	 * 商店刷新
	 */
	public static final int MODULE_STORE_REFRESH = 3;

	/**
	 * 购买体力
	 */
	public static final int MODULE_BUY_STRENGTH = 4;

	/**
	 * 购买技能点
	 */
	public static final int MODULE_BUY_SKILL = 5;

	/**
	 * 过往之球
	 */
	public static final int MODULE_PAST_BALL = 6;

	/**
	 * 现在之球
	 */
	public static final int MODULE_NOW_BALL = 7;

	/**
	 * 未来之球
	 */
	public static final int MODULE_FUTURE_BALL = 8;

	/**
	 * 精英副本
	 */
	public static final int MODULE_STAGE_ELITE = 9;

	/**
	 * 活动模块
	 */
	public static final int MODULE_ACTIVITY = 10;
	
	public static final int MODULE_ARENA = 11;
	
	public static final int MODULE_ENEMY_INVADE = 12;
	
	public static final int MODULE_PAYMENT = 13;
	
	public static final int MODULE_BUY_COINS = 14;
	
	public static final int MODULE_FUMO = 15;
	
	public static final int MODULE_GUILD = 16;
	
	public static final int MODULE_TOWER = 17;
	
	public static final int MODULE_CHANGE_NAME = 18;
	
	public static final int MODULE_GENERAL = 19;
	
	public static final int MODULE_CHAPTER = 20;
	
	public static final int MODULE_FRIEND = 22;
	
	public static final int MODULE_HERO_SKILL = 23;
	
	public static final int MODULE_LIBAO = 24;
	
	public static final int MODULE_MAIL = 25;
	
	public static final int MODULE_NEW_GUIDE = 26;
	
	public static final int MODULE_ONLINE_AWARD = 27;
	
	public static final int MODULE_SIGN = 28;
	
	public static final int MODULE_TASK = 29;
	
	public static final int MODULE_BAG = 30;
	
	public static final int MODULE_INVITE_CODE = 31;
	
	public static final int MODULE_FRIEND_BUILDING = 32;
	
	public static final int MODULE_FRIEND_POTION = 33;
	
	public static final int MODULE_FRIEND_HELP = 34;
	
	public static final int MODULE_CROSS_BOSS = 35;
	
	public static final int MODULE_HONB_BAO = 36;
	
	public static final int MODULE_SECRET = 37;
	
	public static final int MODULE_PUB = 38;
	
	public static final int MODULE_DAILY_MISSION = 39;
	
	public static final int MODULE_FORGE = 40;
	
	public static final int MODULE_OVERCOME = 41;
	
	public static final int MODULE_ACTIVITY_BATTLE = 42;
	
	public static final int MODULE_TREASURE_ROB = 43;
	
	public static final int MODULE_OFFLINE_AWARD = 44;
	
	public static final int MODULE_RANK_LIKE = 45;
	
	public static final int MODULE_CARD_COMPOSE = 46;
	
	public static final int MODULE_PLAYER_INIT = 47;
	
	public static final int MODULE_CARD_ADD_LEVEL = 48;
	
	public static final int MODULE_TREASURE_COMPOSE = 49;
	
	public static final int MODULE_BUILDING = 50;
	public static final int MODULE_TECH = 51;
	public static final int MODULE_FACTORY = 52;

	// ----------------------------------次数类型---------------------------------------//

	/**
	 * 1购买体力
	 */
	public static final int CNT_BUY_STRENGTH = 1;

	/**
	 * 2过往之球1连
	 */
	public static final int CNT_PAST_BALL_1 = 2;

	/**
	 * 3过往之球10连
	 */
	public static final int CNT_PAST_BALL_10 = 3;

	/**
	 * 4过往之球30连
	 */
	public static final int CNT_PAST_BALL_30 = 4;

	/**
	 * 5当下之球1连
	 */
	public static final int CNT_NOW_BALL_1 = 5;

	/**
	 * 6当下之球10连
	 */
	public static final int CNT_NOW_BALL_10 = 6;

	/**
	 * 7未来之球1连
	 */
	public static final int CNT_FUTURE_1 = 7;

	/**
	 * 8购买精英副本次数
	 */
	public static final int CNT_ELITE_STAGE = 8;

	/**
	 * 9 充值
	 */
	public static final int MODULE_RECHARGE = 9;

	/**
	 * 10激活码
	 */
	public static final int MODULE_CDK_AWARD = 10;
	public static final int MODULE_WEIXIN_SHARE = 11;

	public static final int CNT_OFFLINE_AWARD = 12;
	
	public static final int MODULE_WANBA_GIFT = 13;

	public static final int MODULE_RUNE = 50;
	
	public static final int MODULE_RUNE_COMBINE = 51;

	public static final int MODULE_MINE = 52;
	
	public static final int MODULE_BOSS = 53;
	
	public static final int MODULE_GUOZHAN = 54;

    public static final int MODULE_GM = 55;

	public static final int MODULE_COMBAT = 56;

	public static final int MODULE_EQUIP = 57;
    public static final int MODULE_VIP = 58;
	public static final int MODULE_MYTHICAL = 59;
	public static final int MODULE_SHOP = 60;
	public static final int MODULE_AFFAIR = 61;//内政任务
	public static final int MODULE_GENERAL_DECOMP = 62;
	public static final int SPIN = 63;
	public static final int SUMMON = 64;
	public static final int MODULE_GENERAL_EXCHANGE = 65;
	public static final int MODULE_GENERAL_EXCLUSIVE = 66;
	public static final int MODULE_TREASURE = 67;
	public static final int MODULE_ACHIEVE_MISSION = 68;
	public static final int MODULE_GENERAL_REBORN = 69;
	public static final int BIG_BATTLE = 70;
	public static final int MODULE_PVP = 71;
	public static final int MODULE_EXPED = 72;
    public static final int MODULE_MANOR = 73;
	public static final int MODULE_MAP_EVENT = 74;
	public static final int MODULE_LEGION = 75;
	public static final int MODULE_DUNGEON = 76;
	public static final int MODULE_ACTIVITY_MONTH = 77;
	public static final int MODULE_OCC_TASK = 78;
	public static final int MODULE_KING_PVP = 79;
}
