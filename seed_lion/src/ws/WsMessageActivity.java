package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.Map;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.FixedActivityInfo;
import ws.WsMessageBase.DynamicActivityInfo;
import ws.WsMessageBase.IoLotteryWheelConfig;
import ws.WsMessageBase.IoActivityHeroLiBao;
import ws.WsMessageBase.ActivityInfoOne;
import ws.WsMessageBase.ActivitiesItem;
import ws.WsMessageBase.QiZhenYiBaoPlayer;
import ws.WsMessageBase.IOLevelGift;
import ws.WsMessageBase.RewardInfo;
import ws.WsMessageBase.IOStarGift;
import ws.WsMessageBase.IOLiBao1;
import ws.WsMessageBase.IOCjxg2;
import ws.WsMessageBase.IOCzlb;
import ws.WsMessageBase.IODjjfMission;
import ws.WsMessageBase.IOCjxg1;
import ws.WsMessageBase.IOSzhc;
import ws.WsMessageBase.IOTnqwEvent;
import ws.WsMessageBase.IOTnqwBosslist;
import ws.WsMessageBase.IOGeneralLegion;
import ws.WsMessageBase.IOXsdh1;
import ws.WsMessageBase.IODjrw1;
import ws.WsMessageBase.IODjrwChk;
import ws.WsMessageBase.IOJfbxBox;
import ws.WsMessageBase.IOJfbxEvent;
import ws.WsMessageBase.IOMjbgItem;
import ws.WsMessageBase.IOMjbgFinal;
import ws.WsMessageBase.IOMjbgFinal1;
import ws.WsMessageBase.IOMjbgSource;
import ws.WsMessageBase.IOCxryZf;
import ws.WsMessageBase.IOCxryGenerals;

public final class WsMessageActivity{
	public static final class C2SActivityList{
		private static final Logger logger = LoggerFactory.getLogger(C2SActivityList.class);
		public static final int id = 1201;
	}
	public static final class S2CActivityList{
		private static final Logger logger = LoggerFactory.getLogger(S2CActivityList.class);
		public List<FixedActivityInfo> fixed_activities;
		public List<DynamicActivityInfo> dynamic_activities;
		@Override
		public String toString() {
			return "S2CActivityList [fixed_activities="+fixed_activities+",dynamic_activities="+dynamic_activities+",]";
		}
		public static final int msgCode = 1202;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1202);
			if(fixed_activities == null || fixed_activities.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(fixed_activities.size());
				for(FixedActivityInfo fixed_activities1 : fixed_activities){
					retMsg.writeInt(fixed_activities1.id);
					retMsg.writeInt(fixed_activities1.level_index);
					retMsg.writeInt(fixed_activities1.progress);
					retMsg.writeBool(fixed_activities1.can_get_award);
				}
			}
			if(dynamic_activities == null || dynamic_activities.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(dynamic_activities.size());
				for(DynamicActivityInfo dynamic_activities1 : dynamic_activities){
					retMsg.writeInt(dynamic_activities1.activeBigID);
					retMsg.writeInt(dynamic_activities1.priority);
					retMsg.writeString(dynamic_activities1.des);
					retMsg.writeInt(dynamic_activities1.StartTime);
					retMsg.writeInt(dynamic_activities1.EndTime);
					retMsg.writeInt(dynamic_activities1.nComplete);
					if(dynamic_activities1.sub_activities == null || dynamic_activities1.sub_activities.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(dynamic_activities1.sub_activities.size());
				for(ActivityInfoOne dynamic_activities1_sub_activities1 : dynamic_activities1.sub_activities){
					retMsg.writeInt(dynamic_activities1_sub_activities1.nSubCount);
					retMsg.writeInt(dynamic_activities1_sub_activities1.nNeedLevel);
					retMsg.writeInt(dynamic_activities1_sub_activities1.nNeedVipLevel);
					retMsg.writeInt(dynamic_activities1_sub_activities1.nNeedPassNoChapID);
					retMsg.writeInt(dynamic_activities1_sub_activities1.nState);
					retMsg.writeInt(dynamic_activities1_sub_activities1.nComplete);
					retMsg.writeString(dynamic_activities1_sub_activities1.szSubDes);
					if(dynamic_activities1_sub_activities1.reward == null || dynamic_activities1_sub_activities1.reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(dynamic_activities1_sub_activities1.reward.size());
				for(ActivitiesItem dynamic_activities1_sub_activities1_reward1 : dynamic_activities1_sub_activities1.reward){
					retMsg.writeInt(dynamic_activities1_sub_activities1_reward1.nID);
					retMsg.writeInt(dynamic_activities1_sub_activities1_reward1.nNum);
				}
			}
				}
			}
					if(dynamic_activities1.hero_libao_config == null || dynamic_activities1.hero_libao_config.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(dynamic_activities1.hero_libao_config.size());
				for(IoActivityHeroLiBao dynamic_activities1_hero_libao_config1 : dynamic_activities1.hero_libao_config){
					retMsg.writeInt(dynamic_activities1_hero_libao_config1.buy_count);
					retMsg.writeInt(dynamic_activities1_hero_libao_config1.buy_count_total);
					retMsg.writeInt(dynamic_activities1_hero_libao_config1.favor_rate);
					retMsg.writeInt(dynamic_activities1_hero_libao_config1.price);
					retMsg.writeInt(dynamic_activities1_hero_libao_config1.recharge_id);
					retMsg.writeInt(dynamic_activities1_hero_libao_config1.extra_diamond);
					if(dynamic_activities1_hero_libao_config1.reward == null || dynamic_activities1_hero_libao_config1.reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(dynamic_activities1_hero_libao_config1.reward.size());
				for(ActivitiesItem dynamic_activities1_hero_libao_config1_reward1 : dynamic_activities1_hero_libao_config1.reward){
					retMsg.writeInt(dynamic_activities1_hero_libao_config1_reward1.nID);
					retMsg.writeInt(dynamic_activities1_hero_libao_config1_reward1.nNum);
				}
			}
				}
			}
					if(dynamic_activities1.lottery_wheel_config == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(dynamic_activities1.lottery_wheel_config.my_free_count);
					retMsg.writeInt(dynamic_activities1.lottery_wheel_config.my_pay_count);
					retMsg.writeInt(dynamic_activities1.lottery_wheel_config.daily_free_count);
					retMsg.writeInt(dynamic_activities1.lottery_wheel_config.pay_count);
					retMsg.writeInt(dynamic_activities1.lottery_wheel_config.pay_price);
					if(dynamic_activities1.lottery_wheel_config.reward == null || dynamic_activities1.lottery_wheel_config.reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(dynamic_activities1.lottery_wheel_config.reward.size());
				for(ActivitiesItem dynamic_activities1_lottery_wheel_config_reward1 : dynamic_activities1.lottery_wheel_config.reward){
					retMsg.writeInt(dynamic_activities1_lottery_wheel_config_reward1.nID);
					retMsg.writeInt(dynamic_activities1_lottery_wheel_config_reward1.nNum);
				}
			}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SActivitiesGetAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SActivitiesGetAward.class);
		public int activeIdType;
		public int item_index;
		@Override
		public String toString() {
			return "C2SActivitiesGetAward [activeIdType="+activeIdType+",item_index="+item_index+",]";
		}
		public static final int id = 1203;

		public static C2SActivitiesGetAward parse(MyRequestMessage request){
			C2SActivitiesGetAward retObj = new C2SActivitiesGetAward();
			try{
			retObj.activeIdType=request.readInt();
			retObj.item_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CActivitiesGetAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CActivitiesGetAward.class);
		public int activeIdType;
		public int item_index;
		public List<ActivitiesItem> gain;
		public boolean lottery_is_pay;
		@Override
		public String toString() {
			return "S2CActivitiesGetAward [activeIdType="+activeIdType+",item_index="+item_index+",gain="+gain+",lottery_is_pay="+lottery_is_pay+",]";
		}
		public static final int msgCode = 1204;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1204);
			retMsg.writeInt(activeIdType);
			retMsg.writeInt(item_index);
			if(gain == null || gain.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(gain.size());
				for(ActivitiesItem gain1 : gain){
					retMsg.writeInt(gain1.nID);
					retMsg.writeInt(gain1.nNum);
				}
			}
			retMsg.writeBool(lottery_is_pay);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class PushActivityProgressUpdate{
		private static final Logger logger = LoggerFactory.getLogger(PushActivityProgressUpdate.class);
		public int activeIdType;
		public int progress;
		public PushActivityProgressUpdate(int pactiveIdType,int pprogress){
			activeIdType=pactiveIdType;
			progress=pprogress;
		}
		public PushActivityProgressUpdate(){}
		@Override
		public String toString() {
			return "PushActivityProgressUpdate [activeIdType="+activeIdType+",progress="+progress+",]";
		}
		public static final int msgCode = 1206;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1206);
			retMsg.writeInt(activeIdType);
			retMsg.writeInt(progress);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SQiZhenYiBaoView{
		private static final Logger logger = LoggerFactory.getLogger(C2SQiZhenYiBaoView.class);
		public static final int id = 1207;
	}
	public static final class S2CQiZhenYiBaoView{
		private static final Logger logger = LoggerFactory.getLogger(S2CQiZhenYiBaoView.class);
		public List<ActivitiesItem> fixed_award;
		public ActivitiesItem big_award;
		public int my_count;
		public int end_cd_time;
		public int player_count_kaijiang;
		public int cost_diamond;
		public int big_award_value;
		public List<QiZhenYiBaoPlayer> players;
		public List<QiZhenYiBaoPlayer> history_players;
		@Override
		public String toString() {
			return "S2CQiZhenYiBaoView [fixed_award="+fixed_award+",big_award="+big_award+",my_count="+my_count+",end_cd_time="+end_cd_time+",player_count_kaijiang="+player_count_kaijiang+",cost_diamond="+cost_diamond+",big_award_value="+big_award_value+",players="+players+",history_players="+history_players+",]";
		}
		public static final int msgCode = 1208;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1208);
			if(fixed_award == null || fixed_award.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(fixed_award.size());
				for(ActivitiesItem fixed_award1 : fixed_award){
					retMsg.writeInt(fixed_award1.nID);
					retMsg.writeInt(fixed_award1.nNum);
				}
			}
			if(big_award == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(big_award.nID);
					retMsg.writeInt(big_award.nNum);
			}
			retMsg.writeInt(my_count);
			retMsg.writeInt(end_cd_time);
			retMsg.writeInt(player_count_kaijiang);
			retMsg.writeInt(cost_diamond);
			retMsg.writeInt(big_award_value);
			if(players == null || players.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(players.size());
				for(QiZhenYiBaoPlayer players1 : players){
					retMsg.writeInt(players1.player_id);
					retMsg.writeString(players1.player_name);
					retMsg.writeInt(players1.count);
				}
			}
			if(history_players == null || history_players.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(history_players.size());
				for(QiZhenYiBaoPlayer history_players1 : history_players){
					retMsg.writeInt(history_players1.player_id);
					retMsg.writeString(history_players1.player_name);
					retMsg.writeInt(history_players1.count);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SQiZhenYiBaoJoin{
		private static final Logger logger = LoggerFactory.getLogger(C2SQiZhenYiBaoJoin.class);
		public static final int id = 1209;
	}
	public static final class S2CQiZhenYiBaoJoin{
		private static final Logger logger = LoggerFactory.getLogger(S2CQiZhenYiBaoJoin.class);
		public static final int msgCode = 1210;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1210);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class PushQiZhenYiBaoKaiJiang{
		private static final Logger logger = LoggerFactory.getLogger(PushQiZhenYiBaoKaiJiang.class);
		public QiZhenYiBaoPlayer award_player;
		public int hero_tpl_id;
		@Override
		public String toString() {
			return "PushQiZhenYiBaoKaiJiang [award_player="+award_player+",hero_tpl_id="+hero_tpl_id+",]";
		}
		public static final int msgCode = 1212;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1212);
			if(award_player == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(award_player.player_id);
					retMsg.writeString(award_player.player_name);
					retMsg.writeInt(award_player.count);
			}
			retMsg.writeInt(hero_tpl_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLevelGiftList{
		private static final Logger logger = LoggerFactory.getLogger(C2SLevelGiftList.class);
		public static final int id = 1213;
	}
	public static final class S2CLevelGiftList{
		private static final Logger logger = LoggerFactory.getLogger(S2CLevelGiftList.class);
		public List<IOLevelGift> ret_list;
		@Override
		public String toString() {
			return "S2CLevelGiftList [ret_list="+ret_list+",]";
		}
		public static final int msgCode = 1214;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1214);
			if(ret_list == null || ret_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(ret_list.size());
				for(IOLevelGift ret_list1 : ret_list){
					retMsg.writeInt(ret_list1.level);
					retMsg.writeInt(ret_list1.price);
					retMsg.writeLong(ret_list1.end);
					retMsg.writeInt(ret_list1.buytime);
					if(ret_list1.items == null || ret_list1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(ret_list1.items.size());
				for(RewardInfo ret_list1_items1 : ret_list1.items){
					retMsg.writeInt(ret_list1_items1.GSID);
					retMsg.writeInt(ret_list1_items1.COUNT);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SStarGiftList{
		private static final Logger logger = LoggerFactory.getLogger(C2SStarGiftList.class);
		public static final int id = 1215;
	}
	public static final class S2CStarGiftList{
		private static final Logger logger = LoggerFactory.getLogger(S2CStarGiftList.class);
		public List<IOStarGift> ret_list;
		@Override
		public String toString() {
			return "S2CStarGiftList [ret_list="+ret_list+",]";
		}
		public static final int msgCode = 1216;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1216);
			if(ret_list == null || ret_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(ret_list.size());
				for(IOStarGift ret_list1 : ret_list){
					retMsg.writeInt(ret_list1.gstar);
					retMsg.writeInt(ret_list1.price);
					retMsg.writeLong(ret_list1.end);
					retMsg.writeInt(ret_list1.buytime);
					if(ret_list1.items == null || ret_list1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(ret_list1.items.size());
				for(RewardInfo ret_list1_items1 : ret_list1.items){
					retMsg.writeInt(ret_list1_items1.GSID);
					retMsg.writeInt(ret_list1_items1.COUNT);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMzlbList{
		private static final Logger logger = LoggerFactory.getLogger(C2SMzlbList.class);
		public static final int id = 1217;
	}
	public static final class S2CMzlbList{
		private static final Logger logger = LoggerFactory.getLogger(S2CMzlbList.class);
		public long end;
		public List<IOLiBao1> items;
		@Override
		public String toString() {
			return "S2CMzlbList [end="+end+",items="+items+",]";
		}
		public static final int msgCode = 1218;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1218);
			retMsg.writeLong(end);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IOLiBao1 items1 : items){
					retMsg.writeInt(items1.price);
					retMsg.writeInt(items1.buytime);
					if(items1.items == null || items1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1.items.size());
				for(RewardInfo items1_items1 : items1.items){
					retMsg.writeInt(items1_items1.GSID);
					retMsg.writeInt(items1_items1.COUNT);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMylbList{
		private static final Logger logger = LoggerFactory.getLogger(C2SMylbList.class);
		public static final int id = 1219;
	}
	public static final class S2CMylbList{
		private static final Logger logger = LoggerFactory.getLogger(S2CMylbList.class);
		public long end;
		public List<IOLiBao1> items;
		@Override
		public String toString() {
			return "S2CMylbList [end="+end+",items="+items+",]";
		}
		public static final int msgCode = 1220;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1220);
			retMsg.writeLong(end);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IOLiBao1 items1 : items){
					retMsg.writeInt(items1.price);
					retMsg.writeInt(items1.buytime);
					if(items1.items == null || items1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1.items.size());
				for(RewardInfo items1_items1 : items1.items){
					retMsg.writeInt(items1_items1.GSID);
					retMsg.writeInt(items1_items1.COUNT);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SZhdTime{
		private static final Logger logger = LoggerFactory.getLogger(C2SZhdTime.class);
		public static final int id = 1221;
	}
	public static final class S2CZhdTime{
		private static final Logger logger = LoggerFactory.getLogger(S2CZhdTime.class);
		public Map<String,List<Long>> ret;
		@Override
		public String toString() {
			return "S2CZhdTime [ret="+ret+",]";
		}
		public static final int msgCode = 1222;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1222);
			if(ret == null || ret.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(ret.size());
				for(Map.Entry<String,List<Long>> ret1 : ret.entrySet()){
			retMsg.writeString(ret1.getKey());
			if(ret1.getValue() == null || ret1.getValue().size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(ret1.getValue().size());
				for(Long ret1_getValue1 : ret1.getValue()){
			retMsg.writeLong(ret1_getValue1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetCjxg2{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetCjxg2.class);
		public static final int id = 1223;
	}
	public static final class S2CGetCjxg2{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetCjxg2.class);
		public List<IOCjxg2> list;
		@Override
		public String toString() {
			return "S2CGetCjxg2 [list="+list+",]";
		}
		public static final int msgCode = 1224;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1224);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOCjxg2 list1 : list){
					retMsg.writeInt(list1.value);
					retMsg.writeInt(list1.price);
					if(list1.items == null || list1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.items.size());
				for(RewardInfo list1_items1 : list1.items){
					retMsg.writeInt(list1_items1.GSID);
					retMsg.writeInt(list1_items1.COUNT);
				}
			}
					retMsg.writeInt(list1.buytime);
					retMsg.writeString(list1.icon);
					if(list1.special == null || list1.special.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.special.size());
				for(RewardInfo list1_special1 : list1.special){
					retMsg.writeInt(list1_special1.GSID);
					retMsg.writeInt(list1_special1.COUNT);
				}
			}
					retMsg.writeString(list1.bg1);
					retMsg.writeString(list1.bg2);
					retMsg.writeString(list1.hero);
					retMsg.writeString(list1.heroname);
					retMsg.writeString(list1.txbig);
					retMsg.writeString(list1.normal);
					retMsg.writeString(list1.check);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGxCzlbList{
		private static final Logger logger = LoggerFactory.getLogger(C2SGxCzlbList.class);
		public static final int id = 1225;
	}
	public static final class S2CGxCzlbList{
		private static final Logger logger = LoggerFactory.getLogger(S2CGxCzlbList.class);
		public List<IOCzlb> list;
		@Override
		public String toString() {
			return "S2CGxCzlbList [list="+list+",]";
		}
		public static final int msgCode = 1226;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1226);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOCzlb list1 : list){
					retMsg.writeInt(list1.value);
					retMsg.writeInt(list1.price);
					if(list1.items == null || list1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.items.size());
				for(RewardInfo list1_items1 : list1.items){
					retMsg.writeInt(list1_items1.GSID);
					retMsg.writeInt(list1_items1.COUNT);
				}
			}
					retMsg.writeInt(list1.buytime);
					retMsg.writeInt(list1.limit);
					if(list1.special == null || list1.special.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.special.size());
				for(RewardInfo list1_special1 : list1.special){
					retMsg.writeInt(list1_special1.GSID);
					retMsg.writeInt(list1_special1.COUNT);
				}
			}
					retMsg.writeInt(list1.exp);
					retMsg.writeString(list1.path);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGjCzlbList{
		private static final Logger logger = LoggerFactory.getLogger(C2SGjCzlbList.class);
		public static final int id = 1227;
	}
	public static final class S2CGjCzlbList{
		private static final Logger logger = LoggerFactory.getLogger(S2CGjCzlbList.class);
		public List<IOCzlb> list;
		@Override
		public String toString() {
			return "S2CGjCzlbList [list="+list+",]";
		}
		public static final int msgCode = 1228;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1228);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOCzlb list1 : list){
					retMsg.writeInt(list1.value);
					retMsg.writeInt(list1.price);
					if(list1.items == null || list1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.items.size());
				for(RewardInfo list1_items1 : list1.items){
					retMsg.writeInt(list1_items1.GSID);
					retMsg.writeInt(list1_items1.COUNT);
				}
			}
					retMsg.writeInt(list1.buytime);
					retMsg.writeInt(list1.limit);
					if(list1.special == null || list1.special.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.special.size());
				for(RewardInfo list1_special1 : list1.special){
					retMsg.writeInt(list1_special1.GSID);
					retMsg.writeInt(list1_special1.COUNT);
				}
			}
					retMsg.writeInt(list1.exp);
					retMsg.writeString(list1.path);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SCzlbList{
		private static final Logger logger = LoggerFactory.getLogger(C2SCzlbList.class);
		public static final int id = 1229;
	}
	public static final class S2CCzlbList{
		private static final Logger logger = LoggerFactory.getLogger(S2CCzlbList.class);
		public List<IOCzlb> list;
		@Override
		public String toString() {
			return "S2CCzlbList [list="+list+",]";
		}
		public static final int msgCode = 1230;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1230);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOCzlb list1 : list){
					retMsg.writeInt(list1.value);
					retMsg.writeInt(list1.price);
					if(list1.items == null || list1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.items.size());
				for(RewardInfo list1_items1 : list1.items){
					retMsg.writeInt(list1_items1.GSID);
					retMsg.writeInt(list1_items1.COUNT);
				}
			}
					retMsg.writeInt(list1.buytime);
					retMsg.writeInt(list1.limit);
					if(list1.special == null || list1.special.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.special.size());
				for(RewardInfo list1_special1 : list1.special){
					retMsg.writeInt(list1_special1.GSID);
					retMsg.writeInt(list1_special1.COUNT);
				}
			}
					retMsg.writeInt(list1.exp);
					retMsg.writeString(list1.path);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGzCzlbList{
		private static final Logger logger = LoggerFactory.getLogger(C2SGzCzlbList.class);
		public static final int id = 1231;
	}
	public static final class S2CGzCzlbList{
		private static final Logger logger = LoggerFactory.getLogger(S2CGzCzlbList.class);
		public List<IOCzlb> list;
		@Override
		public String toString() {
			return "S2CGzCzlbList [list="+list+",]";
		}
		public static final int msgCode = 1232;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1232);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOCzlb list1 : list){
					retMsg.writeInt(list1.value);
					retMsg.writeInt(list1.price);
					if(list1.items == null || list1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.items.size());
				for(RewardInfo list1_items1 : list1.items){
					retMsg.writeInt(list1_items1.GSID);
					retMsg.writeInt(list1_items1.COUNT);
				}
			}
					retMsg.writeInt(list1.buytime);
					retMsg.writeInt(list1.limit);
					if(list1.special == null || list1.special.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.special.size());
				for(RewardInfo list1_special1 : list1.special){
					retMsg.writeInt(list1_special1.GSID);
					retMsg.writeInt(list1_special1.COUNT);
				}
			}
					retMsg.writeInt(list1.exp);
					retMsg.writeString(list1.path);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDjjfInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SDjjfInfo.class);
		public static final int id = 1233;
	}
	public static final class S2CDjjfInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CDjjfInfo.class);
		public int looplimit;
		public int currentloop;
		public List<IODjjfMission> missions;
		@Override
		public String toString() {
			return "S2CDjjfInfo [looplimit="+looplimit+",currentloop="+currentloop+",missions="+missions+",]";
		}
		public static final int msgCode = 1234;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1234);
			retMsg.writeInt(looplimit);
			retMsg.writeInt(currentloop);
			if(missions == null || missions.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(missions.size());
				for(IODjjfMission missions1 : missions){
					retMsg.writeInt(missions1.NUM);
					retMsg.writeInt(missions1.cur);
					retMsg.writeString(missions1.NAME);
					if(missions1.ITEMS == null || missions1.ITEMS.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(missions1.ITEMS.size());
				for(RewardInfo missions1_ITEMS1 : missions1.ITEMS){
					retMsg.writeInt(missions1_ITEMS1.GSID);
					retMsg.writeInt(missions1_ITEMS1.COUNT);
				}
			}
					retMsg.writeInt(missions1.status);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGxdbInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SGxdbInfo.class);
		public static final int id = 1235;
	}
	public static final class S2CGxdbInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CGxdbInfo.class);
		public int looplimit;
		public int currentloop;
		public List<IODjjfMission> missions;
		@Override
		public String toString() {
			return "S2CGxdbInfo [looplimit="+looplimit+",currentloop="+currentloop+",missions="+missions+",]";
		}
		public static final int msgCode = 1236;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1236);
			retMsg.writeInt(looplimit);
			retMsg.writeInt(currentloop);
			if(missions == null || missions.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(missions.size());
				for(IODjjfMission missions1 : missions){
					retMsg.writeInt(missions1.NUM);
					retMsg.writeInt(missions1.cur);
					retMsg.writeString(missions1.NAME);
					if(missions1.ITEMS == null || missions1.ITEMS.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(missions1.ITEMS.size());
				for(RewardInfo missions1_ITEMS1 : missions1.ITEMS){
					retMsg.writeInt(missions1_ITEMS1.GSID);
					retMsg.writeInt(missions1_ITEMS1.COUNT);
				}
			}
					retMsg.writeInt(missions1.status);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetCjxg1{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetCjxg1.class);
		public static final int id = 1237;
	}
	public static final class S2CGetCjxg1{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetCjxg1.class);
		public List<IOCjxg1> list;
		@Override
		public String toString() {
			return "S2CGetCjxg1 [list="+list+",]";
		}
		public static final int msgCode = 1238;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1238);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOCjxg1 list1 : list){
					retMsg.writeInt(list1.viple);
					if(list1.consume == null || list1.consume.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.consume.size());
				for(RewardInfo list1_consume1 : list1.consume){
					retMsg.writeInt(list1_consume1.GSID);
					retMsg.writeInt(list1_consume1.COUNT);
				}
			}
					if(list1.items == null || list1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.items.size());
				for(RewardInfo list1_items1 : list1.items){
					retMsg.writeInt(list1_items1.GSID);
					retMsg.writeInt(list1_items1.COUNT);
				}
			}
					retMsg.writeInt(list1.buytime);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetSzhc{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetSzhc.class);
		public static final int id = 1239;
	}
	public static final class S2CGetSzhc{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetSzhc.class);
		public List<IOSzhc> list;
		@Override
		public String toString() {
			return "S2CGetSzhc [list="+list+",]";
		}
		public static final int msgCode = 1240;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1240);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOSzhc list1 : list){
					if(list1.consume == null || list1.consume.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.consume.size());
				for(RewardInfo list1_consume1 : list1.consume){
					retMsg.writeInt(list1_consume1.GSID);
					retMsg.writeInt(list1_consume1.COUNT);
				}
			}
					if(list1.demand == null || list1.demand.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.demand.size());
				for(RewardInfo list1_demand1 : list1.demand){
					retMsg.writeInt(list1_demand1.GSID);
					retMsg.writeInt(list1_demand1.COUNT);
				}
			}
					retMsg.writeInt(list1.buytime);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetTnqwInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetTnqwInfo.class);
		public static final int id = 1241;
	}
	public static final class S2CGetTnqwInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetTnqwInfo.class);
		public List<IOTnqwEvent> event;
		public List<IOTnqwBosslist> bosslist;
		@Override
		public String toString() {
			return "S2CGetTnqwInfo [event="+event+",bosslist="+bosslist+",]";
		}
		public static final int msgCode = 1242;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1242);
			if(event == null || event.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(event.size());
				for(IOTnqwEvent event1 : event){
					retMsg.writeInt(event1.mark);
					retMsg.writeInt(event1.limit);
					retMsg.writeString(event1.intro);
				}
			}
			if(bosslist == null || bosslist.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(bosslist.size());
				for(IOTnqwBosslist bosslist1 : bosslist){
					retMsg.writeInt(bosslist1.status);
					retMsg.writeInt(bosslist1.actscore);
					if(bosslist1.rewardgsids == null || bosslist1.rewardgsids.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(bosslist1.rewardgsids.size());
				for(String bosslist1_rewardgsids1 : bosslist1.rewardgsids){
			retMsg.writeString(bosslist1_rewardgsids1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetTnqwBossInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetTnqwBossInfo.class);
		public int boss_index;
		@Override
		public String toString() {
			return "C2SGetTnqwBossInfo [boss_index="+boss_index+",]";
		}
		public static final int id = 1243;

		public static C2SGetTnqwBossInfo parse(MyRequestMessage request){
			C2SGetTnqwBossInfo retObj = new C2SGetTnqwBossInfo();
			try{
			retObj.boss_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGetTnqwBossInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetTnqwBossInfo.class);
		public String name;
		public int gsid;
		public int level;
		public List<RewardInfo> challrewards;
		public List<RewardInfo> killrewards;
		public long nowhp;
		public long maxhp;
		public long lastdamge;
		public long last;
		public Map<Integer,IOGeneralLegion> bset;
		@Override
		public String toString() {
			return "S2CGetTnqwBossInfo [name="+name+",gsid="+gsid+",level="+level+",challrewards="+challrewards+",killrewards="+killrewards+",nowhp="+nowhp+",maxhp="+maxhp+",lastdamge="+lastdamge+",last="+last+",bset="+bset+",]";
		}
		public static final int msgCode = 1244;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1244);
			retMsg.writeString(name);
			retMsg.writeInt(gsid);
			retMsg.writeInt(level);
			if(challrewards == null || challrewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(challrewards.size());
				for(RewardInfo challrewards1 : challrewards){
					retMsg.writeInt(challrewards1.GSID);
					retMsg.writeInt(challrewards1.COUNT);
				}
			}
			if(killrewards == null || killrewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(killrewards.size());
				for(RewardInfo killrewards1 : killrewards){
					retMsg.writeInt(killrewards1.GSID);
					retMsg.writeInt(killrewards1.COUNT);
				}
			}
			retMsg.writeLong(nowhp);
			retMsg.writeLong(maxhp);
			retMsg.writeLong(lastdamge);
			retMsg.writeLong(last);
			if(bset == null || bset.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(bset.size());
				for(Map.Entry<Integer,IOGeneralLegion> bset1 : bset.entrySet()){
			retMsg.writeInt(bset1.getKey());
					retMsg.writeInt(bset1.getValue().gsid);
					retMsg.writeInt(bset1.getValue().level);
					retMsg.writeInt(bset1.getValue().hpcover);
					retMsg.writeInt(bset1.getValue().pclass);
					retMsg.writeLong(bset1.getValue().exhp);
					retMsg.writeInt(bset1.getValue().exatk);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STnqwBossSweep{
		private static final Logger logger = LoggerFactory.getLogger(C2STnqwBossSweep.class);
		public int boss_index;
		public int times;
		@Override
		public String toString() {
			return "C2STnqwBossSweep [boss_index="+boss_index+",times="+times+",]";
		}
		public static final int id = 1245;

		public static C2STnqwBossSweep parse(MyRequestMessage request){
			C2STnqwBossSweep retObj = new C2STnqwBossSweep();
			try{
			retObj.boss_index=request.readInt();
			retObj.times=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CTnqwBossSweep{
		private static final Logger logger = LoggerFactory.getLogger(S2CTnqwBossSweep.class);
		public long nowhp;
		public List<RewardInfo> reward;
		@Override
		public String toString() {
			return "S2CTnqwBossSweep [nowhp="+nowhp+",reward="+reward+",]";
		}
		public static final int msgCode = 1246;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1246);
			retMsg.writeLong(nowhp);
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(RewardInfo reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SZmjfInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SZmjfInfo.class);
		public static final int id = 1247;
	}
	public static final class S2CZmjfInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CZmjfInfo.class);
		public int looplimit;
		public int currentloop;
		public List<IODjjfMission> missions;
		@Override
		public String toString() {
			return "S2CZmjfInfo [looplimit="+looplimit+",currentloop="+currentloop+",missions="+missions+",]";
		}
		public static final int msgCode = 1248;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1248);
			retMsg.writeInt(looplimit);
			retMsg.writeInt(currentloop);
			if(missions == null || missions.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(missions.size());
				for(IODjjfMission missions1 : missions){
					retMsg.writeInt(missions1.NUM);
					retMsg.writeInt(missions1.cur);
					retMsg.writeString(missions1.NAME);
					if(missions1.ITEMS == null || missions1.ITEMS.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(missions1.ITEMS.size());
				for(RewardInfo missions1_ITEMS1 : missions1.ITEMS){
					retMsg.writeInt(missions1_ITEMS1.GSID);
					retMsg.writeInt(missions1_ITEMS1.COUNT);
				}
			}
					retMsg.writeInt(missions1.status);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SXsdhList{
		private static final Logger logger = LoggerFactory.getLogger(C2SXsdhList.class);
		public static final int id = 1249;
	}
	public static final class S2CXsdhList{
		private static final Logger logger = LoggerFactory.getLogger(S2CXsdhList.class);
		public List<IOXsdh1> list;
		@Override
		public String toString() {
			return "S2CXsdhList [list="+list+",]";
		}
		public static final int msgCode = 1250;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1250);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOXsdh1 list1 : list){
					retMsg.writeInt(list1.grid);
					if(list1.grch == null || list1.grch.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.grch.size());
				for(RewardInfo list1_grch1 : list1.grch){
					retMsg.writeInt(list1_grch1.GSID);
					retMsg.writeInt(list1_grch1.COUNT);
				}
			}
					if(list1.consume == null || list1.consume.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.consume.size());
				for(RewardInfo list1_consume1 : list1.consume){
					retMsg.writeInt(list1_consume1.GSID);
					retMsg.writeInt(list1_consume1.COUNT);
				}
			}
					retMsg.writeInt(list1.buytime);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetGjdl{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetGjdl.class);
		public static final int id = 1251;
	}
	public static final class S2CGetGjdl{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetGjdl.class);
		public List<IOCjxg1> list;
		@Override
		public String toString() {
			return "S2CGetGjdl [list="+list+",]";
		}
		public static final int msgCode = 1252;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1252);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOCjxg1 list1 : list){
					retMsg.writeInt(list1.viple);
					if(list1.consume == null || list1.consume.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.consume.size());
				for(RewardInfo list1_consume1 : list1.consume){
					retMsg.writeInt(list1_consume1.GSID);
					retMsg.writeInt(list1_consume1.COUNT);
				}
			}
					if(list1.items == null || list1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.items.size());
				for(RewardInfo list1_items1 : list1.items){
					retMsg.writeInt(list1_items1.GSID);
					retMsg.writeInt(list1_items1.COUNT);
				}
			}
					retMsg.writeInt(list1.buytime);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDjrwInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SDjrwInfo.class);
		public static final int id = 1253;
	}
	public static final class S2CDjrwInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CDjrwInfo.class);
		public List<IODjrw1> list;
		@Override
		public String toString() {
			return "S2CDjrwInfo [list="+list+",]";
		}
		public static final int msgCode = 1254;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1254);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IODjrw1 list1 : list){
					retMsg.writeInt(list1.knark);
					if(list1.chk == null || list1.chk.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.chk.size());
				for(IODjrwChk list1_chk1 : list1.chk){
					retMsg.writeInt(list1_chk1.MARK);
					retMsg.writeInt(list1_chk1.NUM);
				}
			}
					retMsg.writeInt(list1.cnum);
					retMsg.writeString(list1.name);
					if(list1.items == null || list1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.items.size());
				for(RewardInfo list1_items1 : list1.items){
					retMsg.writeInt(list1_items1.GSID);
					retMsg.writeInt(list1_items1.COUNT);
				}
			}
					retMsg.writeInt(list1.status);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetJfbx{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetJfbx.class);
		public static final int id = 1255;
	}
	public static final class S2CGetJfbx{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetJfbx.class);
		public List<IOJfbxBox> box;
		public List<IOJfbxEvent> event;
		@Override
		public String toString() {
			return "S2CGetJfbx [box="+box+",event="+event+",]";
		}
		public static final int msgCode = 1256;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1256);
			if(box == null || box.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(box.size());
				for(IOJfbxBox box1 : box){
					retMsg.writeInt(box1.SCORE);
					retMsg.writeInt(box1.state);
					if(box1.REWARD == null || box1.REWARD.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(box1.REWARD.size());
				for(RewardInfo box1_REWARD1 : box1.REWARD){
					retMsg.writeInt(box1_REWARD1.GSID);
					retMsg.writeInt(box1_REWARD1.COUNT);
				}
			}
				}
			}
			if(event == null || event.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(event.size());
				for(IOJfbxEvent event1 : event){
					retMsg.writeInt(event1.MARK);
					retMsg.writeInt(event1.LIMIT);
					retMsg.writeString(event1.intro);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SBuySzhc{
		private static final Logger logger = LoggerFactory.getLogger(C2SBuySzhc.class);
		public int num_index;
		@Override
		public String toString() {
			return "C2SBuySzhc [num_index="+num_index+",]";
		}
		public static final int id = 1257;

		public static C2SBuySzhc parse(MyRequestMessage request){
			C2SBuySzhc retObj = new C2SBuySzhc();
			try{
			retObj.num_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CBuySzhc{
		private static final Logger logger = LoggerFactory.getLogger(S2CBuySzhc.class);
		public static final int msgCode = 1258;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1258);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SPayCjxg1{
		private static final Logger logger = LoggerFactory.getLogger(C2SPayCjxg1.class);
		public int num_index;
		@Override
		public String toString() {
			return "C2SPayCjxg1 [num_index="+num_index+",]";
		}
		public static final int id = 1259;

		public static C2SPayCjxg1 parse(MyRequestMessage request){
			C2SPayCjxg1 retObj = new C2SPayCjxg1();
			try{
			retObj.num_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CPayCjxg1{
		private static final Logger logger = LoggerFactory.getLogger(S2CPayCjxg1.class);
		public static final int msgCode = 1260;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1260);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMjbgInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SMjbgInfo.class);
		public static final int id = 1261;
	}
	public static final class S2CMjbgInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CMjbgInfo.class);
		public int djgsid;
		public int djcount;
		public int index;
		public List<IOMjbgItem> items;
		public String strbox;
		public List<IOMjbgFinal> finallist;
		public List<IOMjbgSource> list;
		public IOMjbgItem _final;
		@Override
		public String toString() {
			return "S2CMjbgInfo [djgsid="+djgsid+",djcount="+djcount+",index="+index+",items="+items+",strbox="+strbox+",finallist="+finallist+",list="+list+",_final="+_final+",]";
		}
		public static final int msgCode = 1262;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1262);
			retMsg.writeInt(djgsid);
			retMsg.writeInt(djcount);
			retMsg.writeInt(index);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IOMjbgItem items1 : items){
					retMsg.writeInt(items1.gsid);
					retMsg.writeInt(items1.count);
					retMsg.writeInt(items1.num);
				}
			}
			retMsg.writeString(strbox);
			if(finallist == null || finallist.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(finallist.size());
				for(IOMjbgFinal finallist1 : finallist){
					if(finallist1.list == null || finallist1.list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(finallist1.list.size());
				for(IOMjbgFinal1 finallist1_list1 : finallist1.list){
					retMsg.writeInt(finallist1_list1.gsid);
					retMsg.writeInt(finallist1_list1.count);
					retMsg.writeInt(finallist1_list1.maxnum);
				}
			}
				}
			}
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOMjbgSource list1 : list){
					retMsg.writeString(list1.intro);
					retMsg.writeString(list1.page);
				}
			}
			if(_final == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(_final.gsid);
					retMsg.writeInt(_final.count);
					retMsg.writeInt(_final.num);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMjbgChange{
		private static final Logger logger = LoggerFactory.getLogger(C2SMjbgChange.class);
		public int index;
		public int itemindex;
		@Override
		public String toString() {
			return "C2SMjbgChange [index="+index+",itemindex="+itemindex+",]";
		}
		public static final int id = 1263;

		public static C2SMjbgChange parse(MyRequestMessage request){
			C2SMjbgChange retObj = new C2SMjbgChange();
			try{
			retObj.index=request.readInt();
			retObj.itemindex=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMjbgChange{
		private static final Logger logger = LoggerFactory.getLogger(S2CMjbgChange.class);
		public int gsid;
		public int count;
		public S2CMjbgChange(int pgsid,int pcount){
			gsid=pgsid;
			count=pcount;
		}
		public S2CMjbgChange(){}
		@Override
		public String toString() {
			return "S2CMjbgChange [gsid="+gsid+",count="+count+",]";
		}
		public static final int msgCode = 1264;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1264);
			retMsg.writeInt(gsid);
			retMsg.writeInt(count);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMjbgReward{
		private static final Logger logger = LoggerFactory.getLogger(C2SMjbgReward.class);
		public int index;
		@Override
		public String toString() {
			return "C2SMjbgReward [index="+index+",]";
		}
		public static final int id = 1265;

		public static C2SMjbgReward parse(MyRequestMessage request){
			C2SMjbgReward retObj = new C2SMjbgReward();
			try{
			retObj.index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMjbgReward{
		private static final Logger logger = LoggerFactory.getLogger(S2CMjbgReward.class);
		public int gsid;
		public int count;
		public S2CMjbgReward(int pgsid,int pcount){
			gsid=pgsid;
			count=pcount;
		}
		public S2CMjbgReward(){}
		@Override
		public String toString() {
			return "S2CMjbgReward [gsid="+gsid+",count="+count+",]";
		}
		public static final int msgCode = 1266;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1266);
			retMsg.writeInt(gsid);
			retMsg.writeInt(count);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMjbgNext{
		private static final Logger logger = LoggerFactory.getLogger(C2SMjbgNext.class);
		public static final int id = 1267;
	}
	public static final class S2CMjbgNext{
		private static final Logger logger = LoggerFactory.getLogger(S2CMjbgNext.class);
		public static final int msgCode = 1268;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1268);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SPayGjdl{
		private static final Logger logger = LoggerFactory.getLogger(C2SPayGjdl.class);
		public int item_index;
		public int buynum;
		@Override
		public String toString() {
			return "C2SPayGjdl [item_index="+item_index+",buynum="+buynum+",]";
		}
		public static final int id = 1269;

		public static C2SPayGjdl parse(MyRequestMessage request){
			C2SPayGjdl retObj = new C2SPayGjdl();
			try{
			retObj.item_index=request.readInt();
			retObj.buynum=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CPayGjdl{
		private static final Logger logger = LoggerFactory.getLogger(S2CPayGjdl.class);
		public static final int msgCode = 1270;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1270);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STgslInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2STgslInfo.class);
		public static final int id = 1271;
	}
	public static final class S2CTgslInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CTgslInfo.class);
		public int gsid;
		public String name;
		public int level;
		public int mark;
		public List<Integer> skill;
		public List<Integer> challrewards;
		public long hp;
		public Map<Integer,IOGeneralLegion> bset;
		@Override
		public String toString() {
			return "S2CTgslInfo [gsid="+gsid+",name="+name+",level="+level+",mark="+mark+",skill="+skill+",challrewards="+challrewards+",hp="+hp+",bset="+bset+",]";
		}
		public static final int msgCode = 1272;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1272);
			retMsg.writeInt(gsid);
			retMsg.writeString(name);
			retMsg.writeInt(level);
			retMsg.writeInt(mark);
			if(skill == null || skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(skill.size());
				for(Integer skill1 : skill){
			retMsg.writeInt(skill1);
				}
			}
			if(challrewards == null || challrewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(challrewards.size());
				for(Integer challrewards1 : challrewards){
			retMsg.writeInt(challrewards1);
				}
			}
			retMsg.writeLong(hp);
			if(bset == null || bset.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(bset.size());
				for(Map.Entry<Integer,IOGeneralLegion> bset1 : bset.entrySet()){
			retMsg.writeInt(bset1.getKey());
					retMsg.writeInt(bset1.getValue().gsid);
					retMsg.writeInt(bset1.getValue().level);
					retMsg.writeInt(bset1.getValue().hpcover);
					retMsg.writeInt(bset1.getValue().pclass);
					retMsg.writeLong(bset1.getValue().exhp);
					retMsg.writeInt(bset1.getValue().exatk);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SXsdhDh{
		private static final Logger logger = LoggerFactory.getLogger(C2SXsdhDh.class);
		public int item_index;
		@Override
		public String toString() {
			return "C2SXsdhDh [item_index="+item_index+",]";
		}
		public static final int id = 1273;

		public static C2SXsdhDh parse(MyRequestMessage request){
			C2SXsdhDh retObj = new C2SXsdhDh();
			try{
			retObj.item_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CXsdhDh{
		private static final Logger logger = LoggerFactory.getLogger(S2CXsdhDh.class);
		public static final int msgCode = 1274;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1274);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetCxryInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetCxryInfo.class);
		public static final int id = 1275;
	}
	public static final class S2CGetCxryInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetCxryInfo.class);
		public IOCxryZf zf;
		public List<IOCxryGenerals> savegenerals;
		public int gnummax;
		@Override
		public String toString() {
			return "S2CGetCxryInfo [zf="+zf+",savegenerals="+savegenerals+",gnummax="+gnummax+",]";
		}
		public static final int msgCode = 1276;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1276);
			if(zf == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(zf.cur);
					retMsg.writeInt(zf.max);
					retMsg.writeInt(zf.prob);
			}
			if(savegenerals == null || savegenerals.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(savegenerals.size());
				for(IOCxryGenerals savegenerals1 : savegenerals){
					retMsg.writeInt(savegenerals1.gsid);
					retMsg.writeInt(savegenerals1.isget);
				}
			}
			retMsg.writeInt(gnummax);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetCxryGeneralList{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetCxryGeneralList.class);
		public static final int id = 1277;
	}
	public static final class S2CGetCxryGeneralList{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetCxryGeneralList.class);
		public List<Integer> generallist;
		@Override
		public String toString() {
			return "S2CGetCxryGeneralList [generallist="+generallist+",]";
		}
		public static final int msgCode = 1278;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1278);
			if(generallist == null || generallist.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(generallist.size());
				for(Integer generallist1 : generallist){
			retMsg.writeInt(generallist1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSetCxryGeneralList{
		private static final Logger logger = LoggerFactory.getLogger(C2SSetCxryGeneralList.class);
		public int[] gsids;
		@Override
		public String toString() {
			return "C2SSetCxryGeneralList [gsids="+java.util.Arrays.toString(gsids)+",]";
		}
		public static final int id = 1279;

		public static C2SSetCxryGeneralList parse(MyRequestMessage request){
			C2SSetCxryGeneralList retObj = new C2SSetCxryGeneralList();
			try{
			int gsids_size = request.readInt();
				retObj.gsids = new int[gsids_size];
				for(int i=0;i<gsids_size;i++){
					retObj.gsids[i]=request.readInt();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSetCxryGeneralList{
		private static final Logger logger = LoggerFactory.getLogger(S2CSetCxryGeneralList.class);
		public static final int msgCode = 1280;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1280);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
