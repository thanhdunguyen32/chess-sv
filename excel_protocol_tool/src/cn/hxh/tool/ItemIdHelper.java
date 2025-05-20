package cn.hxh.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemIdHelper {

	public static Map<String, String> itemNameIdMap = new HashMap<String, String>();

	static {
		itemNameIdMap.put("树枝", "22010001");
		itemNameIdMap.put("力量拳套", "22010002");
		itemNameIdMap.put("敏捷丝袜", "22010003");
		itemNameIdMap.put("智力之篷", "22010004");
		itemNameIdMap.put("防御指环", "22010005");
		itemNameIdMap.put("贵族头环", "22010006");
		itemNameIdMap.put("小魔棒", "22010007");
		itemNameIdMap.put("补刀斧", "22010008");
		itemNameIdMap.put("小圆盾", "22010009");
		itemNameIdMap.put("艺人面罩", "22010010");
		itemNameIdMap.put("回复戒指", "22010011");
		itemNameIdMap.put("鞋", "22010012");
		itemNameIdMap.put("攻击利爪", "22010013");
		itemNameIdMap.put("力量护腕", "22010014");
		itemNameIdMap.put("敏捷系带", "22010015");
		itemNameIdMap.put("智力挂件", "22010016");
		itemNameIdMap.put("圣殿指环", "22010017");
		itemNameIdMap.put("屌丝盾", "22010018");
		itemNameIdMap.put("回复之巾", "22010019");
		itemNameIdMap.put("小毒球", "22020001");
		itemNameIdMap.put("力量腰带", "22020002");
		itemNameIdMap.put("敏捷皮靴", "22020003");
		itemNameIdMap.put("智力长袍", "22020004");
		itemNameIdMap.put("加速手套", "22020005");
		itemNameIdMap.put("锁甲", "22020006");
		itemNameIdMap.put("魔抗斗篷", "22020007");
		itemNameIdMap.put("治疗指环", "22020008");
		itemNameIdMap.put("虚空宝石", "22020009");
		itemNameIdMap.put("吸血面具", "22020010");
		itemNameIdMap.put("短棍", "22020011");
		itemNameIdMap.put("武士头盔", "22020012");
		itemNameIdMap.put("生命之球", "22020013");
		itemNameIdMap.put("法力之球", "22020014");
		itemNameIdMap.put("元气之球", "22020015");
		itemNameIdMap.put("大魔杖", "22020016");
		itemNameIdMap.put("魂戒", "22020017");
		itemNameIdMap.put("玄铁盾牌", "22020018");
		itemNameIdMap.put("骨灰盒", "22020019");
		itemNameIdMap.put("命匣", "22020019");
		itemNameIdMap.put("绿鞋", "22020020");
		itemNameIdMap.put("天鹰之戒", "22020021");
		itemNameIdMap.put("勇气勋章", "22020022");
		itemNameIdMap.put("相位鞋", "22020023");
		itemNameIdMap.put("力量假腿", "22020024");
		itemNameIdMap.put("敏捷假腿", "22020025");
		itemNameIdMap.put("智力假腿", "22020026");
		itemNameIdMap.put("秘法鞋", "22020027");
		itemNameIdMap.put("坚韧法球", "22020028");
		itemNameIdMap.put("古之耐歌", "22020029");
		itemNameIdMap.put("空明禅杖", "22020030");
		itemNameIdMap.put("支配", "22020031");
		itemNameIdMap.put("疯脸", "22020032");
		itemNameIdMap.put("祭品", "22020033");
		itemNameIdMap.put("魔抗头巾", "22020034");
		itemNameIdMap.put("冲锋盾", "22020035");
		itemNameIdMap.put("梅肯", "22020036");
		itemNameIdMap.put("力量之斧", "22030001");
		itemNameIdMap.put("敏捷之刃", "22030002");
		itemNameIdMap.put("智力法杖", "22030003");
		itemNameIdMap.put("阔剑", "22030004");
		itemNameIdMap.put("大剑", "22030005");
		itemNameIdMap.put("板甲", "22030006");
		itemNameIdMap.put("标枪", "22030007");
		itemNameIdMap.put("秘银锤", "22030008");
		itemNameIdMap.put("暗影符", "22030009");
		itemNameIdMap.put("绿杖", "22030010");
		itemNameIdMap.put("散华", "22030011");
		itemNameIdMap.put("夜叉", "22030012");
		itemNameIdMap.put("水晶剑", "22030013");
		itemNameIdMap.put("反伤甲", "22030014");
		itemNameIdMap.put("推推棒", "22030015");
		itemNameIdMap.put("飞鞋", "22030016");
		itemNameIdMap.put("鬼手", "22030017");
		itemNameIdMap.put("小电锤", "22030018");
		itemNameIdMap.put("风杖", "22030019");
		itemNameIdMap.put("洛萨", "22030020");
		itemNameIdMap.put("碎骨锤", "22030021");
		itemNameIdMap.put("纷争", "22030022");
		itemNameIdMap.put("阿托斯", "22030023");
		itemNameIdMap.put("振魂玫瑰", "22030024");
		itemNameIdMap.put("黑黄", "22030025");
		itemNameIdMap.put("双刀", "22030026");
		itemNameIdMap.put("蓝杖", "22030027");
		itemNameIdMap.put("狂战斧", "22030028");
		itemNameIdMap.put("暗灭", "22030029");
		itemNameIdMap.put("梅肯2阶", "22030030");
		itemNameIdMap.put("小人书", "22030031");
		itemNameIdMap.put("红杖", "22030032");
		itemNameIdMap.put("散失", "22030033");
		itemNameIdMap.put("血晶石", "22030034");
		itemNameIdMap.put("闪避项链", "22040001");
		itemNameIdMap.put("极限球", "22040002");
		itemNameIdMap.put("振奋石", "22040003");
		itemNameIdMap.put("魔王刀锋", "22040004");
		itemNameIdMap.put("掠夺之斧", "22040005");
		itemNameIdMap.put("角鹰弓", "22040006");
		itemNameIdMap.put("水晶法杖", "22040007");
		itemNameIdMap.put("远古遗物", "22040008");
		itemNameIdMap.put("长笛", "22040009");
		itemNameIdMap.put("天堂", "22040010");
		itemNameIdMap.put("分身斧", "22040011");
		itemNameIdMap.put("林肯", "22040012");
		itemNameIdMap.put("强袭", "22040013");
		itemNameIdMap.put("大电锤", "22040014");
		itemNameIdMap.put("冰眼", "22040015");
		itemNameIdMap.put("金箍棒", "22040016");
		itemNameIdMap.put("羊刀", "22040017");
		itemNameIdMap.put("冰甲", "22040018");
		itemNameIdMap.put("虚灵", "22040019");
		itemNameIdMap.put("光耀", "22040020");
		itemNameIdMap.put("龙心", "22040021");
		itemNameIdMap.put("蝴蝶", "22040022");
		itemNameIdMap.put("紫苑", "22040023");
		itemNameIdMap.put("大炮", "22040024");
		itemNameIdMap.put("撒旦", "22040025");
		itemNameIdMap.put("深渊之刃", "22040026");
		itemNameIdMap.put("梅肯3阶", "22040027");
		itemNameIdMap.put("梅肯4阶", "22040028");
		itemNameIdMap.put("小人书2阶", "22040029");
		itemNameIdMap.put("小人书3阶", "22040030");
		itemNameIdMap.put("红杖2阶", "22040031");
		itemNameIdMap.put("红杖3阶", "22040032");
		itemNameIdMap.put("红杖4阶", "22040033");
		itemNameIdMap.put("散失2阶", "22040034");
		itemNameIdMap.put("散失3阶", "22040035");
		itemNameIdMap.put("血晶石2阶", "22040036");
		itemNameIdMap.put("血晶石3阶", "22040037");
		itemNameIdMap.put("刷新珠", "22040038");
		itemNameIdMap.put("刷新珠2阶", "22040039");
		itemNameIdMap.put("巫师之冠", "22040040");
		itemNameIdMap.put("女王的浴衣", "22040041");
		itemNameIdMap.put("水晶之塔", "22050001");
		itemNameIdMap.put("魔龙之鳞", "22050002");
		itemNameIdMap.put("泰坦战斧", "22050003");
		itemNameIdMap.put("水晶球", "22050004");
		itemNameIdMap.put("疾行鞋", "22050005");
		itemNameIdMap.put("射手指环", "22050006");
		itemNameIdMap.put("欺诈者之剑", "22050007");
		itemNameIdMap.put("神圣种子", "22050008");
		itemNameIdMap.put("法师抄本", "22050009");
		itemNameIdMap.put("无尽长夜法杖", "22050010");
		itemNameIdMap.put("鹰羽指环", "22050011");
		itemNameIdMap.put("大天使之剑", "22050012");
		itemNameIdMap.put("虚无法杖", "22050013");
		itemNameIdMap.put("群星之怒", "22050014");
		itemNameIdMap.put("十字军巨盾", "22050015");
		itemNameIdMap.put("毁灭护符", "22050016");
		itemNameIdMap.put("阴影之书", "22050017");
		itemNameIdMap.put("翡翠之吻", "22050018");
		itemNameIdMap.put("不朽之守护", "22050019");
		itemNameIdMap.put("红宝石吊坠", "22050020");
		itemNameIdMap.put("禁卫军胸甲", "22050021");
		itemNameIdMap.put("逐日者法典", "22050022");
		itemNameIdMap.put("雷霆之怒", "22050023");
		itemNameIdMap.put("恶魔之矢", "22050024");
		itemNameIdMap.put("永恒冰柱", "22050025");
		itemNameIdMap.put("银月长矛", "22050026");
		itemNameIdMap.put("地覆", "22050027");
		itemNameIdMap.put("麒麟弯刀", "22050028");
		itemNameIdMap.put("暗月卡牌", "22050029");
		itemNameIdMap.put("蓝宝石法杖", "22050030");
	}

	public static void main(String[] args) {
		String sourceStr = "智力之篷-贵族头环-艺人面罩-小魔棒-树枝-树枝-力量护腕-小魔棒-鞋-生命之球-法力之球-智力挂件-冲锋盾-艺人面罩-绿鞋-智力法杖-古之耐歌-大魔杖-蓝杖-振魂玫瑰-魔抗头巾-生命之球-力量护腕-秘法鞋-古之耐歌-玄铁盾牌-秘法鞋-阿托斯-风杖-智力法杖-冲锋盾-元气之球-智力假腿-羊刀-小人书-黑黄-红杖-水晶法杖-纷争-蓝杖-智力假腿-小人书2阶-冰甲-红杖2阶-极限球-推推棒-智力长袍-飞鞋-飞鞋-红杖3阶-冰眼-小人书-紫苑-智力法杖-红杖4阶-羊刀-虚灵-血晶石-黑黄-飞鞋-无尽长夜法杖-水晶球-女王的浴衣-蓝杖-极限球-疾行鞋-羊刀-疾行鞋-神怒之翼-翡翠之吻-巫师之冠-毁灭护符";
		String[] strs = sourceStr.split("-");
		List<String> retStrs = new ArrayList<>();
		for (String aStr : strs) {
			String itemIdStr = itemNameIdMap.get(aStr);
			retStrs.add(itemIdStr);
		}
		// print
		StringBuilder sb = new StringBuilder();
		int i = 1;
		for (String aIdStr : retStrs) {
			sb.append(aIdStr);
			if (i % 6 == 0) {
				sb.append("\t");
			} else {
				sb.append("-");
			}
			i++;
		}
		System.out.println(sb.toString());
	}

}
