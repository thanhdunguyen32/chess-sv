package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.AwardItem;
import ws.WsMessageBase.IOOccTask1;
import ws.WsMessageBase.IOOcctaskPackinfo;
import ws.WsMessageBase.RewardInfo;

public final class WsMessageMission{
	public static final class C2SMissionDailyAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SMissionDailyAward.class);
		public int mission_index;
		@Override
		public String toString() {
			return "C2SMissionDailyAward [mission_index="+mission_index+",]";
		}
		public static final int id = 551;

		public static C2SMissionDailyAward parse(MyRequestMessage request){
			C2SMissionDailyAward retObj = new C2SMissionDailyAward();
			try{
			retObj.mission_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMissionDailyAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CMissionDailyAward.class);
		public List<AwardItem> rewards;
		@Override
		public String toString() {
			return "S2CMissionDailyAward [rewards="+rewards+",]";
		}
		public static final int msgCode = 552;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 552);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(AwardItem rewards1 : rewards){
					retMsg.writeInt(rewards1.gsid);
					retMsg.writeInt(rewards1.count);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMissionAchieveAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SMissionAchieveAward.class);
		public int mission_type;
		public int mission_index;
		@Override
		public String toString() {
			return "C2SMissionAchieveAward [mission_type="+mission_type+",mission_index="+mission_index+",]";
		}
		public static final int id = 553;

		public static C2SMissionAchieveAward parse(MyRequestMessage request){
			C2SMissionAchieveAward retObj = new C2SMissionAchieveAward();
			try{
			retObj.mission_type=request.readInt();
			retObj.mission_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMissionAchieveAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CMissionAchieveAward.class);
		public List<AwardItem> rewards;
		@Override
		public String toString() {
			return "S2CMissionAchieveAward [rewards="+rewards+",]";
		}
		public static final int msgCode = 554;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 554);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(AwardItem rewards1 : rewards){
					retMsg.writeInt(rewards1.gsid);
					retMsg.writeInt(rewards1.count);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetNoviceTrainAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetNoviceTrainAward.class);
		public String exid;
		@Override
		public String toString() {
			return "C2SGetNoviceTrainAward [exid="+exid+",]";
		}
		public static final int id = 555;

		public static C2SGetNoviceTrainAward parse(MyRequestMessage request){
			C2SGetNoviceTrainAward retObj = new C2SGetNoviceTrainAward();
			try{
			retObj.exid=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGetNoviceTrainAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetNoviceTrainAward.class);
		public List<AwardItem> rewards;
		@Override
		public String toString() {
			return "S2CGetNoviceTrainAward [rewards="+rewards+",]";
		}
		public static final int msgCode = 556;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 556);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(AwardItem rewards1 : rewards){
					retMsg.writeInt(rewards1.gsid);
					retMsg.writeInt(rewards1.count);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SOccTaskInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SOccTaskInfo.class);
		public static final int id = 557;
	}
	public static final class S2COccTaskInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2COccTaskInfo.class);
		public int index;
		public int occtype;
		public List<RewardInfo> rewards;
		public List<IOOccTask1> list;
		public List<RewardInfo> reward;
		public RewardInfo refcost;
		public IOOcctaskPackinfo packinfo;
		public List<Integer> prewards;
		@Override
		public String toString() {
			return "S2COccTaskInfo [index="+index+",occtype="+occtype+",rewards="+rewards+",list="+list+",reward="+reward+",refcost="+refcost+",packinfo="+packinfo+",prewards="+prewards+",]";
		}
		public static final int msgCode = 558;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 558);
			retMsg.writeInt(index);
			retMsg.writeInt(occtype);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(RewardInfo rewards1 : rewards){
					retMsg.writeInt(rewards1.GSID);
					retMsg.writeInt(rewards1.COUNT);
				}
			}
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOOccTask1 list1 : list){
					retMsg.writeInt(list1.status);
					if(list1.rewards == null || list1.rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.rewards.size());
				for(RewardInfo list1_rewards1 : list1.rewards){
					retMsg.writeInt(list1_rewards1.GSID);
					retMsg.writeInt(list1_rewards1.COUNT);
				}
			}
					retMsg.writeString(list1.intro);
					retMsg.writeInt(list1.mark);
					retMsg.writeInt(list1.limit);
					retMsg.writeString(list1.page);
					retMsg.writeInt(list1.num);
				}
			}
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(RewardInfo reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
				}
			}
			if(refcost == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(refcost.GSID);
					retMsg.writeInt(refcost.COUNT);
			}
			if(packinfo == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(packinfo.ID);
					if(packinfo.ITEMS == null || packinfo.ITEMS.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(packinfo.ITEMS.size());
				for(RewardInfo packinfo_ITEMS1 : packinfo.ITEMS){
					retMsg.writeInt(packinfo_ITEMS1.GSID);
					retMsg.writeInt(packinfo_ITEMS1.COUNT);
				}
			}
					retMsg.writeInt(packinfo.TYPE);
					retMsg.writeInt(packinfo.VALUE);
					retMsg.writeInt(packinfo.WEIGHT);
			}
			if(prewards == null || prewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(prewards.size());
				for(Integer prewards1 : prewards){
			retMsg.writeInt(prewards1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SOccTaskFree{
		private static final Logger logger = LoggerFactory.getLogger(C2SOccTaskFree.class);
		public static final int id = 559;
	}
	public static final class S2COccTaskFree{
		private static final Logger logger = LoggerFactory.getLogger(S2COccTaskFree.class);
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2COccTaskFree [rewards="+rewards+",]";
		}
		public static final int msgCode = 560;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 560);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(RewardInfo rewards1 : rewards){
					retMsg.writeInt(rewards1.GSID);
					retMsg.writeInt(rewards1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SOccTaskJobSelect{
		private static final Logger logger = LoggerFactory.getLogger(C2SOccTaskJobSelect.class);
		public int occtype;
		@Override
		public String toString() {
			return "C2SOccTaskJobSelect [occtype="+occtype+",]";
		}
		public static final int id = 561;

		public static C2SOccTaskJobSelect parse(MyRequestMessage request){
			C2SOccTaskJobSelect retObj = new C2SOccTaskJobSelect();
			try{
			retObj.occtype=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2COccTaskJobSelect{
		private static final Logger logger = LoggerFactory.getLogger(S2COccTaskJobSelect.class);
		public static final int msgCode = 562;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 562);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SOccTaskGiftOne{
		private static final Logger logger = LoggerFactory.getLogger(C2SOccTaskGiftOne.class);
		public int level_index;
		@Override
		public String toString() {
			return "C2SOccTaskGiftOne [level_index="+level_index+",]";
		}
		public static final int id = 563;

		public static C2SOccTaskGiftOne parse(MyRequestMessage request){
			C2SOccTaskGiftOne retObj = new C2SOccTaskGiftOne();
			try{
			retObj.level_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2COccTaskGiftOne{
		private static final Logger logger = LoggerFactory.getLogger(S2COccTaskGiftOne.class);
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2COccTaskGiftOne [rewards="+rewards+",]";
		}
		public static final int msgCode = 564;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 564);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(RewardInfo rewards1 : rewards){
					retMsg.writeInt(rewards1.GSID);
					retMsg.writeInt(rewards1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SOccTaskGiftAll{
		private static final Logger logger = LoggerFactory.getLogger(C2SOccTaskGiftAll.class);
		public static final int id = 565;
	}
	public static final class S2COccTaskGiftAll{
		private static final Logger logger = LoggerFactory.getLogger(S2COccTaskGiftAll.class);
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2COccTaskGiftAll [rewards="+rewards+",]";
		}
		public static final int msgCode = 566;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 566);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(RewardInfo rewards1 : rewards){
					retMsg.writeInt(rewards1.GSID);
					retMsg.writeInt(rewards1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SOccTaskPackRefresh{
		private static final Logger logger = LoggerFactory.getLogger(C2SOccTaskPackRefresh.class);
		public static final int id = 567;
	}
	public static final class S2COccTaskPackRefresh{
		private static final Logger logger = LoggerFactory.getLogger(S2COccTaskPackRefresh.class);
		public RewardInfo refcost;
		public IOOcctaskPackinfo packinfo;
		@Override
		public String toString() {
			return "S2COccTaskPackRefresh [refcost="+refcost+",packinfo="+packinfo+",]";
		}
		public static final int msgCode = 568;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 568);
			if(refcost == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(refcost.GSID);
					retMsg.writeInt(refcost.COUNT);
			}
			if(packinfo == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(packinfo.ID);
					if(packinfo.ITEMS == null || packinfo.ITEMS.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(packinfo.ITEMS.size());
				for(RewardInfo packinfo_ITEMS1 : packinfo.ITEMS){
					retMsg.writeInt(packinfo_ITEMS1.GSID);
					retMsg.writeInt(packinfo_ITEMS1.COUNT);
				}
			}
					retMsg.writeInt(packinfo.TYPE);
					retMsg.writeInt(packinfo.VALUE);
					retMsg.writeInt(packinfo.WEIGHT);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SOccTaskPackRPay{
		private static final Logger logger = LoggerFactory.getLogger(C2SOccTaskPackRPay.class);
		public int task_id;
		@Override
		public String toString() {
			return "C2SOccTaskPackRPay [task_id="+task_id+",]";
		}
		public static final int id = 569;

		public static C2SOccTaskPackRPay parse(MyRequestMessage request){
			C2SOccTaskPackRPay retObj = new C2SOccTaskPackRPay();
			try{
			retObj.task_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2COccTaskPackRPay{
		private static final Logger logger = LoggerFactory.getLogger(S2COccTaskPackRPay.class);
		public static final int msgCode = 570;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 570);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
