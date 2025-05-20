package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOAffairItem;
import ws.WsMessageBase.IORewardItem;

public final class WsMessageAffair{
	public static final class C2SAffairList{
		private static final Logger logger = LoggerFactory.getLogger(C2SAffairList.class);
		public static final int id = 331;
	}
	public static final class S2CAffairList{
		private static final Logger logger = LoggerFactory.getLogger(S2CAffairList.class);
		public List<IOAffairItem> item_list;
		@Override
		public String toString() {
			return "S2CAffairList [item_list="+item_list+",]";
		}
		public static final int msgCode = 332;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 332);
			if(item_list == null || item_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list.size());
				for(IOAffairItem item_list1 : item_list){
					retMsg.writeInt(item_list1.id);
					retMsg.writeInt(item_list1.gnum);
					retMsg.writeInt(item_list1.gstar);
					retMsg.writeInt(item_list1.hour);
					if(item_list1.cond == null || item_list1.cond.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.cond.size());
				for(Integer item_list1_cond1 : item_list1.cond){
			retMsg.writeInt(item_list1_cond1);
				}
			}
					if(item_list1.reward == null || item_list1.reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.reward.size());
				for(IORewardItem item_list1_reward1 : item_list1.reward){
					retMsg.writeInt(item_list1_reward1.GSID);
					retMsg.writeInt(item_list1_reward1.COUNT);
				}
			}
					retMsg.writeInt(item_list1.lock);
					retMsg.writeLong(item_list1.create);
					retMsg.writeLong(item_list1.etime);
					if(item_list1.arr == null || item_list1.arr.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.arr.size());
				for(Long item_list1_arr1 : item_list1.arr){
			retMsg.writeLong(item_list1_arr1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SAffairRefresh{
		private static final Logger logger = LoggerFactory.getLogger(C2SAffairRefresh.class);
		public static final int id = 333;
	}
	public static final class S2CAffairRefresh{
		private static final Logger logger = LoggerFactory.getLogger(S2CAffairRefresh.class);
		public List<IOAffairItem> item_list;
		@Override
		public String toString() {
			return "S2CAffairRefresh [item_list="+item_list+",]";
		}
		public static final int msgCode = 334;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 334);
			if(item_list == null || item_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list.size());
				for(IOAffairItem item_list1 : item_list){
					retMsg.writeInt(item_list1.id);
					retMsg.writeInt(item_list1.gnum);
					retMsg.writeInt(item_list1.gstar);
					retMsg.writeInt(item_list1.hour);
					if(item_list1.cond == null || item_list1.cond.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.cond.size());
				for(Integer item_list1_cond1 : item_list1.cond){
			retMsg.writeInt(item_list1_cond1);
				}
			}
					if(item_list1.reward == null || item_list1.reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.reward.size());
				for(IORewardItem item_list1_reward1 : item_list1.reward){
					retMsg.writeInt(item_list1_reward1.GSID);
					retMsg.writeInt(item_list1_reward1.COUNT);
				}
			}
					retMsg.writeInt(item_list1.lock);
					retMsg.writeLong(item_list1.create);
					retMsg.writeLong(item_list1.etime);
					if(item_list1.arr == null || item_list1.arr.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.arr.size());
				for(Long item_list1_arr1 : item_list1.arr){
			retMsg.writeLong(item_list1_arr1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SAffairReel{
		private static final Logger logger = LoggerFactory.getLogger(C2SAffairReel.class);
		public int scroll_id;
		@Override
		public String toString() {
			return "C2SAffairReel [scroll_id="+scroll_id+",]";
		}
		public static final int id = 335;

		public static C2SAffairReel parse(MyRequestMessage request){
			C2SAffairReel retObj = new C2SAffairReel();
			try{
			retObj.scroll_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CAffairReel{
		private static final Logger logger = LoggerFactory.getLogger(S2CAffairReel.class);
		public int scroll_id;
		public S2CAffairReel(int pscroll_id){
			scroll_id=pscroll_id;
		}
		public S2CAffairReel(){}
		@Override
		public String toString() {
			return "S2CAffairReel [scroll_id="+scroll_id+",]";
		}
		public static final int msgCode = 336;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 336);
			retMsg.writeInt(scroll_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SAffairLock{
		private static final Logger logger = LoggerFactory.getLogger(C2SAffairLock.class);
		public int affair_index;
		@Override
		public String toString() {
			return "C2SAffairLock [affair_index="+affair_index+",]";
		}
		public static final int id = 337;

		public static C2SAffairLock parse(MyRequestMessage request){
			C2SAffairLock retObj = new C2SAffairLock();
			try{
			retObj.affair_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CAffairLock{
		private static final Logger logger = LoggerFactory.getLogger(S2CAffairLock.class);
		public int affair_index;
		public List<IOAffairItem> item_list;
		@Override
		public String toString() {
			return "S2CAffairLock [affair_index="+affair_index+",item_list="+item_list+",]";
		}
		public static final int msgCode = 338;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 338);
			retMsg.writeInt(affair_index);
			if(item_list == null || item_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list.size());
				for(IOAffairItem item_list1 : item_list){
					retMsg.writeInt(item_list1.id);
					retMsg.writeInt(item_list1.gnum);
					retMsg.writeInt(item_list1.gstar);
					retMsg.writeInt(item_list1.hour);
					if(item_list1.cond == null || item_list1.cond.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.cond.size());
				for(Integer item_list1_cond1 : item_list1.cond){
			retMsg.writeInt(item_list1_cond1);
				}
			}
					if(item_list1.reward == null || item_list1.reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.reward.size());
				for(IORewardItem item_list1_reward1 : item_list1.reward){
					retMsg.writeInt(item_list1_reward1.GSID);
					retMsg.writeInt(item_list1_reward1.COUNT);
				}
			}
					retMsg.writeInt(item_list1.lock);
					retMsg.writeLong(item_list1.create);
					retMsg.writeLong(item_list1.etime);
					if(item_list1.arr == null || item_list1.arr.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.arr.size());
				for(Long item_list1_arr1 : item_list1.arr){
			retMsg.writeLong(item_list1_arr1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SAffairStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SAffairStart.class);
		public int affair_index;
		public long[] arr;
		@Override
		public String toString() {
			return "C2SAffairStart [affair_index="+affair_index+",arr="+java.util.Arrays.toString(arr)+",]";
		}
		public static final int id = 339;

		public static C2SAffairStart parse(MyRequestMessage request){
			C2SAffairStart retObj = new C2SAffairStart();
			try{
			retObj.affair_index=request.readInt();
			int arr_size = request.readInt();
				retObj.arr = new long[arr_size];
				for(int i=0;i<arr_size;i++){
					retObj.arr[i]=request.readLong();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CAffairStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CAffairStart.class);
		public int affair_index;
		public List<IOAffairItem> item_list;
		@Override
		public String toString() {
			return "S2CAffairStart [affair_index="+affair_index+",item_list="+item_list+",]";
		}
		public static final int msgCode = 340;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 340);
			retMsg.writeInt(affair_index);
			if(item_list == null || item_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list.size());
				for(IOAffairItem item_list1 : item_list){
					retMsg.writeInt(item_list1.id);
					retMsg.writeInt(item_list1.gnum);
					retMsg.writeInt(item_list1.gstar);
					retMsg.writeInt(item_list1.hour);
					if(item_list1.cond == null || item_list1.cond.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.cond.size());
				for(Integer item_list1_cond1 : item_list1.cond){
			retMsg.writeInt(item_list1_cond1);
				}
			}
					if(item_list1.reward == null || item_list1.reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.reward.size());
				for(IORewardItem item_list1_reward1 : item_list1.reward){
					retMsg.writeInt(item_list1_reward1.GSID);
					retMsg.writeInt(item_list1_reward1.COUNT);
				}
			}
					retMsg.writeInt(item_list1.lock);
					retMsg.writeLong(item_list1.create);
					retMsg.writeLong(item_list1.etime);
					if(item_list1.arr == null || item_list1.arr.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.arr.size());
				for(Long item_list1_arr1 : item_list1.arr){
			retMsg.writeLong(item_list1_arr1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SAffairAcce{
		private static final Logger logger = LoggerFactory.getLogger(C2SAffairAcce.class);
		public int affair_index;
		@Override
		public String toString() {
			return "C2SAffairAcce [affair_index="+affair_index+",]";
		}
		public static final int id = 341;

		public static C2SAffairAcce parse(MyRequestMessage request){
			C2SAffairAcce retObj = new C2SAffairAcce();
			try{
			retObj.affair_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CAffairAcce{
		private static final Logger logger = LoggerFactory.getLogger(S2CAffairAcce.class);
		public int affair_index;
		public List<IOAffairItem> item_list;
		@Override
		public String toString() {
			return "S2CAffairAcce [affair_index="+affair_index+",item_list="+item_list+",]";
		}
		public static final int msgCode = 342;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 342);
			retMsg.writeInt(affair_index);
			if(item_list == null || item_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list.size());
				for(IOAffairItem item_list1 : item_list){
					retMsg.writeInt(item_list1.id);
					retMsg.writeInt(item_list1.gnum);
					retMsg.writeInt(item_list1.gstar);
					retMsg.writeInt(item_list1.hour);
					if(item_list1.cond == null || item_list1.cond.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.cond.size());
				for(Integer item_list1_cond1 : item_list1.cond){
			retMsg.writeInt(item_list1_cond1);
				}
			}
					if(item_list1.reward == null || item_list1.reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.reward.size());
				for(IORewardItem item_list1_reward1 : item_list1.reward){
					retMsg.writeInt(item_list1_reward1.GSID);
					retMsg.writeInt(item_list1_reward1.COUNT);
				}
			}
					retMsg.writeInt(item_list1.lock);
					retMsg.writeLong(item_list1.create);
					retMsg.writeLong(item_list1.etime);
					if(item_list1.arr == null || item_list1.arr.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.arr.size());
				for(Long item_list1_arr1 : item_list1.arr){
			retMsg.writeLong(item_list1_arr1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SAffairAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SAffairAward.class);
		public int affair_index;
		@Override
		public String toString() {
			return "C2SAffairAward [affair_index="+affair_index+",]";
		}
		public static final int id = 343;

		public static C2SAffairAward parse(MyRequestMessage request){
			C2SAffairAward retObj = new C2SAffairAward();
			try{
			retObj.affair_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CAffairAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CAffairAward.class);
		public int affair_index;
		public List<IORewardItem> rewards;
		public List<IOAffairItem> item_list;
		@Override
		public String toString() {
			return "S2CAffairAward [affair_index="+affair_index+",rewards="+rewards+",item_list="+item_list+",]";
		}
		public static final int msgCode = 344;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 344);
			retMsg.writeInt(affair_index);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(IORewardItem rewards1 : rewards){
					retMsg.writeInt(rewards1.GSID);
					retMsg.writeInt(rewards1.COUNT);
				}
			}
			if(item_list == null || item_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list.size());
				for(IOAffairItem item_list1 : item_list){
					retMsg.writeInt(item_list1.id);
					retMsg.writeInt(item_list1.gnum);
					retMsg.writeInt(item_list1.gstar);
					retMsg.writeInt(item_list1.hour);
					if(item_list1.cond == null || item_list1.cond.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.cond.size());
				for(Integer item_list1_cond1 : item_list1.cond){
			retMsg.writeInt(item_list1_cond1);
				}
			}
					if(item_list1.reward == null || item_list1.reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.reward.size());
				for(IORewardItem item_list1_reward1 : item_list1.reward){
					retMsg.writeInt(item_list1_reward1.GSID);
					retMsg.writeInt(item_list1_reward1.COUNT);
				}
			}
					retMsg.writeInt(item_list1.lock);
					retMsg.writeLong(item_list1.create);
					retMsg.writeLong(item_list1.etime);
					if(item_list1.arr == null || item_list1.arr.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list1.arr.size());
				for(Long item_list1_arr1 : item_list1.arr){
			retMsg.writeLong(item_list1_arr1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SAffairRedNotice{
		private static final Logger logger = LoggerFactory.getLogger(C2SAffairRedNotice.class);
		public static final int id = 345;
	}
	public static final class S2CAffairRedNotice{
		private static final Logger logger = LoggerFactory.getLogger(S2CAffairRedNotice.class);
		public boolean ret;
		public S2CAffairRedNotice(boolean pret){
			ret=pret;
		}
		public S2CAffairRedNotice(){}
		@Override
		public String toString() {
			return "S2CAffairRedNotice [ret="+ret+",]";
		}
		public static final int msgCode = 346;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 346);
			retMsg.writeBool(ret);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
