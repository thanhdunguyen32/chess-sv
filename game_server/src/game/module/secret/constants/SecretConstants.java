package game.module.secret.constants;

/**
 * 秘密基地常量
 * 
 * @author zhangning
 * 
 * @Date 2015年1月26日 下午3:06:44
 */
public class SecretConstants {

	public static final byte NO = 0;

	public static final byte YES = 1;

	/**
	 * 中断
	 */
	public static final byte INTERRUPT = -1;

	/**
	 * 模块编号
	 */
	public static final byte MODULE_NUM = 18;

	/**
	 * 活动次数
	 */
	public static final byte GAME_CNT = 1;

	/**
	 * 消耗体力
	 */
	public static final byte DEFAULT_LOSE_STRENGTH = 2;

	/**
	 * 重置时间 TODO --暂定, 以后读配置表
	 */
	public static final byte RESET_TIME = 5;

	/**
	 * 英雄
	 */
	public static final byte HERO = 1;

	/**
	 * 士兵
	 */
	public static final byte NPC = 2;

	/**
	 * 士兵开始位置1
	 */
	public static final byte NPC_START_SITE = 1;

	/**
	 * 士兵结束位置8
	 */
	public static final byte NPC_END_SITE = 8;

	/**
	 * 上阵
	 */
	public static final byte UP_FORMATION = 0;

	/**
	 * 下阵
	 */
	public static final byte DOWN_FORMATION = 1;

	/**
	 * 上阵人数上限
	 */
	public static final byte FORMATION_MAX_CNT = 5;

	/**
	 * 英雄上限个数编号
	 */
	public static final int MAX_HERO_CNT_NUM = 1571;

	/**
	 * 钻石消耗编号
	 */
	public static final int NEED_DIAMOND_COIN_NUM = 1574;

	/**
	 * 选择副本
	 */
	public static final byte CHOOSE_COPY = 1;

	/**
	 * 开始战斗
	 */
	public static final byte START_WAR = 2;

}
