package game.module.item.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemConstants {

	public static final int INIT_GENERAL_BAG_SIZE = 60;

	public static final int GENERAL_BAG_PER_SIZE = 5;

	public static final int SUMMON_COIN = 30011;//点将令
	public static final int JIANG_HUN = 30012;//将魂
	public static final int LONG_YU = 30013;//龙玉

	public static final int FRIENDSHIP_ICON = 30019;//好友间相互赠送爱心

	public static final int[] GENERAL_EXCHANGE_COST = {20, 100};//龙玉

	public static final int GENERAL_BAG_BUY_COUNT = 90158;//背包扩展购买次数

	public static final int BAG_SPACE = 100000;//背包空间

	public static final int BUY_ITEM_COST_YB = 50;//

	public static final int BING_FU = 30024;//

	public static final int PVP_COIN = 30028;//

	public static final int TOWER_ID = 100012;//

	public static final int FRIEND_BOSS_ATTACK_COUNT_MARK = 100014;//

	/**
	 * 充值卡
	 */
	public static final int PAY_CARD_RMB_YUAN = 100016;//

	public static final int JI_TAN_COIN = 38009;//

	public static final int CHANGE_NAME_1_MARK = 90157;//

	public static final Map<Integer,Integer> BUY_ITEM_COST = new HashMap<>();

	static {
		BUY_ITEM_COST.put(30024,50);
		BUY_ITEM_COST.put(38009,50);
		BUY_ITEM_COST.put(30028,20);
		BUY_ITEM_COST.put(38007,50);
	}

	public static final int GZ_YUEKA_GET_MARK = 101000;

	public static final int ZZ_YUEKA_GET_MARK = 101001;

}
