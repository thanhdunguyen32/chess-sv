package game.module.secret.constants;

public class SecretProtoConstants {

	/**
	 * 客户端请求---加载秘密基地
	 */
	public static final int C2S_LIST_SECRET = 60001;

	/**
	 * 服务端响应---加载秘密基地
	 */
	public static final int S2C_LIST_SECRET = 60002;

	/**
	 * 客户端请求--加载上阵英雄+士兵
	 */
	public static final int C2S_SECRET_HERO = 60003;

	/**
	 * 服务端响应--加载上阵英雄+士兵
	 */
	public static final int S2C_SECRET_HERO = 60004;

	/**
	 * 客户端请求---加载已经用过的英雄+士兵
	 */
	public static final int C2S_SECRET_USED_HERO = 60005;

	/**
	 * 服务端响应--加载已经用过的英雄+士兵
	 */
	public static final int S2C_SECRET_USED_HERO = 60006;

	/**
	 * 客户端请求---选择选择副本关卡, 开始战斗
	 */
	public static final int C2S_SECRET_WAR = 60007;

	/**
	 * 服务端端响应--选择选择副本关卡, 开始战斗
	 */
	public static final int S2C_SECRET_WAR = 60008;

	/**
	 * 客户端请求--战斗结束
	 */
	public static final int C2S_SECRET_WAR_END = 60009;

	/**
	 * 服务端端响应--战斗结束
	 */
	public static final int S2C_SECRET_WAR_END = 60010;

	/**
	 * 客户端请求--随机奖励
	 */
	public static final int C2S_SECRET_RANDOM_AWARD = 60011;

	/**
	 * 服务端端响应--随机奖励
	 */
	public static final int S2C_SECRET_RANDOM_AWARD = 60012;

	/**
	 * 客户端请求--选定士兵, 查看信息
	 */
	public static final int C2S_SECRET_SEL_NPC_INFO = 60013;

	/**
	 * 服务端端响应--选定士兵, 查看信息
	 */
	public static final int S2C_SECRET_SEL_NPC_INFO = 60014;

	/**
	 * 客户端请求--复活士兵
	 */
	public static final int C2S_SECRET_NPC_RESURRECT = 60015;

	/**
	 * 服务端端响应--复活士兵
	 */
	public static final int S2C_SECRET_NPC_RESURRECT = 60016;

	/**
	 * 客户端请求--选定士兵, 查看信息
	 */
	public static final int C2S_SECRET_FORMATION = 60017;

	/**
	 * 服务端端响应--选定士兵, 查看信息
	 */
	public static final int S2C_SECRET_FORMATION = 60018;

}
