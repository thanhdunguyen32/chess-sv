package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOShopItem;
import ws.WsMessageBase.SimpleItemInfo;

public final class WsMessageShop{
	public static final class C2SShopList{
		private static final Logger logger = LoggerFactory.getLogger(C2SShopList.class);
		public String shop_type;
		@Override
		public String toString() {
			return "C2SShopList [shop_type="+shop_type+",]";
		}
		public static final int id = 321;

		public static C2SShopList parse(MyRequestMessage request){
			C2SShopList retObj = new C2SShopList();
			try{
			retObj.shop_type=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CShopList{
		private static final Logger logger = LoggerFactory.getLogger(S2CShopList.class);
		public List<IOShopItem> item_list;
		@Override
		public String toString() {
			return "S2CShopList [item_list="+item_list+",]";
		}
		public static final int msgCode = 322;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 322);
			if(item_list == null || item_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list.size());
				for(IOShopItem item_list1 : item_list){
					retMsg.writeInt(item_list1.GSID);
					retMsg.writeInt(item_list1.COUNT);
					retMsg.writeInt(item_list1.BUYTIME);
					retMsg.writeInt(item_list1.COIN);
					retMsg.writeInt(item_list1.PRICE);
					retMsg.writeInt(item_list1.DISCOUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SShopBuy{
		private static final Logger logger = LoggerFactory.getLogger(C2SShopBuy.class);
		public String shop_type;
		public int item_index;
		@Override
		public String toString() {
			return "C2SShopBuy [shop_type="+shop_type+",item_index="+item_index+",]";
		}
		public static final int id = 323;

		public static C2SShopBuy parse(MyRequestMessage request){
			C2SShopBuy retObj = new C2SShopBuy();
			try{
			retObj.shop_type=request.readString();
			retObj.item_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CShopBuy{
		private static final Logger logger = LoggerFactory.getLogger(S2CShopBuy.class);
		public List<SimpleItemInfo> awards;
		@Override
		public String toString() {
			return "S2CShopBuy [awards="+awards+",]";
		}
		public static final int msgCode = 324;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 324);
			if(awards == null || awards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(awards.size());
				for(SimpleItemInfo awards1 : awards){
					retMsg.writeInt(awards1.gsid);
					retMsg.writeInt(awards1.count);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SShopRefresh{
		private static final Logger logger = LoggerFactory.getLogger(C2SShopRefresh.class);
		public String shop_type;
		@Override
		public String toString() {
			return "C2SShopRefresh [shop_type="+shop_type+",]";
		}
		public static final int id = 325;

		public static C2SShopRefresh parse(MyRequestMessage request){
			C2SShopRefresh retObj = new C2SShopRefresh();
			try{
			retObj.shop_type=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CShopRefresh{
		private static final Logger logger = LoggerFactory.getLogger(S2CShopRefresh.class);
		public String shop_type;
		public List<IOShopItem> item_list;
		@Override
		public String toString() {
			return "S2CShopRefresh [shop_type="+shop_type+",item_list="+item_list+",]";
		}
		public static final int msgCode = 326;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 326);
			retMsg.writeString(shop_type);
			if(item_list == null || item_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(item_list.size());
				for(IOShopItem item_list1 : item_list){
					retMsg.writeInt(item_list1.GSID);
					retMsg.writeInt(item_list1.COUNT);
					retMsg.writeInt(item_list1.BUYTIME);
					retMsg.writeInt(item_list1.COIN);
					retMsg.writeInt(item_list1.PRICE);
					retMsg.writeInt(item_list1.DISCOUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
