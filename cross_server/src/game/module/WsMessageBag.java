package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.KvIntPair;
import ws.WsMessageBase.SimpleItemInfo;
import ws.WsMessageBase.RewardInfo;

public final class WsMessageBag{
	public static final class C2SItemList{
		private static final Logger logger = LoggerFactory.getLogger(C2SItemList.class);
		public static final int id = 301;
	}
	public static final class S2CItemList{
		private static final Logger logger = LoggerFactory.getLogger(S2CItemList.class);
		public List<SimpleItemInfo> item_list;
		@Override
		public String toString() {
			return "S2CItemList [item_list="+item_list+",]";
		}
		public static final int msgCode = 302;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 302);
			if(item_list == null || item_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list.size());
				for(SimpleItemInfo item_list1 : item_list){
					retMsg.writeInt(item_list1.gsid);
					retMsg.writeInt(item_list1.count);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SUseItem{
		private static final Logger logger = LoggerFactory.getLogger(C2SUseItem.class);
		public int gsid;
		public int count;
		public int param;
		@Override
		public String toString() {
			return "C2SUseItem [gsid="+gsid+",count="+count+",param="+param+",]";
		}
		public static final int id = 303;

		public static C2SUseItem parse(MyRequestMessage request){
			C2SUseItem retObj = new C2SUseItem();
			try{
			retObj.gsid=request.readInt();
			retObj.count=request.readInt();
			retObj.param=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CUseItem{
		private static final Logger logger = LoggerFactory.getLogger(S2CUseItem.class);
		public int status;
		public List<RewardInfo> reward;
		@Override
		public String toString() {
			return "S2CUseItem [status="+status+",reward="+reward+",]";
		}
		public static final int msgCode = 304;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 304);
			retMsg.writeInt(status);
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
	public static final class C2SItemSell{
		private static final Logger logger = LoggerFactory.getLogger(C2SItemSell.class);
		public int gsid;
		public int count;
		@Override
		public String toString() {
			return "C2SItemSell [gsid="+gsid+",count="+count+",]";
		}
		public static final int id = 305;

		public static C2SItemSell parse(MyRequestMessage request){
			C2SItemSell retObj = new C2SItemSell();
			try{
			retObj.gsid=request.readInt();
			retObj.count=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CItemSell{
		private static final Logger logger = LoggerFactory.getLogger(S2CItemSell.class);
		public int status;
		public List<RewardInfo> reward;
		@Override
		public String toString() {
			return "S2CItemSell [status="+status+",reward="+reward+",]";
		}
		public static final int msgCode = 306;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 306);
			retMsg.writeInt(status);
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
	public static final class C2SEquipComp{
		private static final Logger logger = LoggerFactory.getLogger(C2SEquipComp.class);
		public int equip_id;
		public int count;
		@Override
		public String toString() {
			return "C2SEquipComp [equip_id="+equip_id+",count="+count+",]";
		}
		public static final int id = 307;

		public static C2SEquipComp parse(MyRequestMessage request){
			C2SEquipComp retObj = new C2SEquipComp();
			try{
			retObj.equip_id=request.readInt();
			retObj.count=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CEquipComp{
		private static final Logger logger = LoggerFactory.getLogger(S2CEquipComp.class);
		public int equip_id;
		public int count;
		public S2CEquipComp(int pequip_id,int pcount){
			equip_id=pequip_id;
			count=pcount;
		}
		public S2CEquipComp(){}
		@Override
		public String toString() {
			return "S2CEquipComp [equip_id="+equip_id+",count="+count+",]";
		}
		public static final int msgCode = 308;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 308);
			retMsg.writeInt(equip_id);
			retMsg.writeInt(count);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SBagExpand{
		private static final Logger logger = LoggerFactory.getLogger(C2SBagExpand.class);
		public static final int id = 309;
	}
	public static final class S2CBagExpand{
		private static final Logger logger = LoggerFactory.getLogger(S2CBagExpand.class);
		public static final int msgCode = 310;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 310);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SBuyItem{
		private static final Logger logger = LoggerFactory.getLogger(C2SBuyItem.class);
		public int gsid;
		public int count;
		@Override
		public String toString() {
			return "C2SBuyItem [gsid="+gsid+",count="+count+",]";
		}
		public static final int id = 311;

		public static C2SBuyItem parse(MyRequestMessage request){
			C2SBuyItem retObj = new C2SBuyItem();
			try{
			retObj.gsid=request.readInt();
			retObj.count=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CBuyItem{
		private static final Logger logger = LoggerFactory.getLogger(S2CBuyItem.class);
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2CBuyItem [rewards="+rewards+",]";
		}
		public static final int msgCode = 312;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 312);
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
	public static final class C2SItemExchange{
		private static final Logger logger = LoggerFactory.getLogger(C2SItemExchange.class);
		public String EXID;
		public int count;
		public int other;
		@Override
		public String toString() {
			return "C2SItemExchange [EXID="+EXID+",count="+count+",other="+other+",]";
		}
		public static final int id = 313;

		public static C2SItemExchange parse(MyRequestMessage request){
			C2SItemExchange retObj = new C2SItemExchange();
			try{
			retObj.EXID=request.readString();
			retObj.count=request.readInt();
			retObj.other=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CItemExchange{
		private static final Logger logger = LoggerFactory.getLogger(S2CItemExchange.class);
		public List<RewardInfo> reward;
		@Override
		public String toString() {
			return "S2CItemExchange [reward="+reward+",]";
		}
		public static final int msgCode = 314;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 314);
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
}
