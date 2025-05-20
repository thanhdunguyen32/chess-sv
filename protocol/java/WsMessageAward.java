package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.AwardItem;

public final class WsMessageAward{
	public static final class C2SListSignIn{
		private static final Logger logger = LoggerFactory.getLogger(C2SListSignIn.class);
		public static final int id = 801;
	}
	public static final class S2CListSignIn{
		private static final Logger logger = LoggerFactory.getLogger(S2CListSignIn.class);
		public int sign_in_count;
		public boolean is_get;
		public S2CListSignIn(int psign_in_count,boolean pis_get){
			sign_in_count=psign_in_count;
			is_get=pis_get;
		}
		public S2CListSignIn(){}
		@Override
		public String toString() {
			return "S2CListSignIn [sign_in_count="+sign_in_count+",is_get="+is_get+",]";
		}
		public static final int msgCode = 802;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 802);
			retMsg.writeInt(sign_in_count);
			retMsg.writeBool(is_get);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetSignAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetSignAward.class);
		public static final int id = 803;
	}
	public static final class S2CGetSignAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetSignAward.class);
		public List<AwardItem> awards;
		@Override
		public String toString() {
			return "S2CGetSignAward [awards="+awards+",]";
		}
		public static final int msgCode = 804;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 804);
			if(awards == null || awards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(awards.size());
				for(AwardItem awards1 : awards){
					retMsg.writeInt(awards1.gsid);
					retMsg.writeInt(awards1.count);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SListOnlineAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SListOnlineAward.class);
		public static final int id = 805;
	}
	public static final class S2CListOnlineAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CListOnlineAward.class);
		public long need_online_time;
		public int reward_index;
		public S2CListOnlineAward(long pneed_online_time,int preward_index){
			need_online_time=pneed_online_time;
			reward_index=preward_index;
		}
		public S2CListOnlineAward(){}
		@Override
		public String toString() {
			return "S2CListOnlineAward [need_online_time="+need_online_time+",reward_index="+reward_index+",]";
		}
		public static final int msgCode = 806;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 806);
			retMsg.writeLong(need_online_time);
			retMsg.writeInt(reward_index);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetOnlineAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetOnlineAward.class);
		public static final int id = 807;
	}
	public static final class S2CGetOnlineAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetOnlineAward.class);
		public List<AwardItem> reward;
		@Override
		public String toString() {
			return "S2CGetOnlineAward [reward="+reward+",]";
		}
		public static final int msgCode = 808;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 808);
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(AwardItem reward1 : reward){
					retMsg.writeInt(reward1.gsid);
					retMsg.writeInt(reward1.count);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SKeyConvert{
		private static final Logger logger = LoggerFactory.getLogger(C2SKeyConvert.class);
		public String key;
		@Override
		public String toString() {
			return "C2SKeyConvert [key="+key+",]";
		}
		public static final int id = 809;

		public static C2SKeyConvert parse(MyRequestMessage request){
			C2SKeyConvert retObj = new C2SKeyConvert();
			try{
			retObj.key=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CKeyConvert{
		private static final Logger logger = LoggerFactory.getLogger(S2CKeyConvert.class);
		public int result_id;
		public List<AwardItem> reward_item;
		@Override
		public String toString() {
			return "S2CKeyConvert [result_id="+result_id+",reward_item="+reward_item+",]";
		}
		public static final int msgCode = 810;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 810);
			retMsg.writeInt(result_id);
			if(reward_item == null || reward_item.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward_item.size());
				for(AwardItem reward_item1 : reward_item){
					retMsg.writeInt(reward_item1.gsid);
					retMsg.writeInt(reward_item1.count);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
