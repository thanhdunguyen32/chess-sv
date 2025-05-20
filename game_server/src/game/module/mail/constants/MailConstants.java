package game.module.mail.constants;

/**
 * 邮件常量
 * 
 * @author zhangning
 * @Date 2014年12月29日 上午11:53:56
 *
 */
public class MailConstants {

	public static final byte NO = 0;

	public static final byte YES = 1;

	public static final byte MAIL_STATUS_NEW = 0;
	public static final byte MAIL_STATUS_IS_READ = 1;
	public static final byte MAIL_STATUS_ATTACH_GET = 2;

	public static final byte MAIL_TYPE_PLAYER = 0;
	public static final byte MAIL_TYPE_LEGION = 1;
	public static final byte MAIL_TYPE_SYS = 2;

	/**
	 * 标题字数上限：20
	 */
	public static final int MAX_TITLE_CNT = 20;

	/**
	 * 内容字数上限：256
	 */
	public static final int MAX_CONTANT_CNT = 256;

	/**
	 * 邮件个数上限：50
	 */
	public static final int MAX_MAIL_CNT = 60;

	/**
	 * 系统邮件
	 */
	public static final byte SYS_MAIL = 0;

	/**
	 * 个人邮件
	 */
	public static final byte PERSON_MAIL = 1;

	/**
	 * 系统邮件发件人昵称：system
	 */
	public static final String SYS_MAIL_NAME = "system";

	/**
	 * 系统邮件最长有效期30天
	 */
	public static final int SYS_MAIL_MAX_DELTIME = 30;

	/**
	 * 参数类型: 0发件人, 1标题, 2内容
	 */
	public static final int PARAM_NO = -1;
	public static final int PARAM_SEND_NAME = 0;
	public static final int PARAM_TITLE = 1;
	public static final int PARAM_CONTENT = 2;

	// --------------------------------个人系统邮件----------------------------------//

	/**
	 * 激活码邮件
	 */
	public static final int MAIL_CDKEY = 111;

	public static final int MAIL_TYPE_ARENA_DAILY_REWARD = 1000;

	public static final int MAIL_TYPE_ARENA_NEW_RANK_REWARD = 1001;
	
	public static final int MAIL_TYPE_ARENA_LUCKY_RANK_REWARD = 1020;

	/**
	 * 入会通知邮件
	 */
	public static final int MAIL_GUILD_JOIN = 1002;
	/**
	 * 踢出公会邮件
	 */
	public static final int MAIL_GUILD_KICK = 1004;
	/**
	 * 公会解散邮件
	 */
	public static final int MAIL_GUILD_DISSOLVE = 1005;
	/**
	 * PVP房间退出惩罚
	 */
	public static final int MAIL_PVP_PUNISH = 1006;

	public static final int MAIL_PVP_REWARD = 1007;
	/**
	 * 诸神争霸房间退出惩罚
	 */
	public static final int MAIL_CRAFT_PUNISH = 1008;
	/**
	 * 诸神争霸奖励
	 */
	public static final int MAIL_CRAFT_REWARD = 1009;
	/**
	 * 首冲奖励
	 */
	public static final int MAIL_FIRST_PAY_REWARD = 1010;
	public static final int MAIL_KAIFU_JINJICHANG = 1011;
	public static final int MAIL_KAIFU_ZHUSHEN = 1012;
	public static final int MAIL_VIP_LIBAO = 1013;
	public static final int MAIL_ACTIVITY_QIZHENYIBAO = 1014;
	public static final int MAIL_ACTIVITY_HERO_LIBAO = 1015;
	public static final int MAIL_ACTIVITY_CROSS_BOSS = 1016;
	public static final int MAIL_ACTIVITY_CHONG_ZHI_BANG = 1017;
	public static final int MAIL_ACTIVITY_XIAO_FEI_BANG = 1018;
	public static final int MAIL_ACTIVITY_QING_CHUN_JI_JIN = 1019;
	public static final int MAIL_ACTIVITY_CHONG_JI_BANG = 1021;
	public static final int MAIL_VIP_AWARD = 1022;
	public static final int MAIL_WELCOME = 1023;
	public static final int MAIL_GUOZHAN = 1024;
	public static final int MAIL_GUOZHAN_OCCUPY = 1025;
	public static final int MAIL_GUOZHAN_PASS = 1026;
	
	/**
	 * GM 发邮件 (1指定玩家, 2所有玩家)
	 */
	public static final byte NOMINEE = 1;
	public static final byte EVERYONE = 2;
}
