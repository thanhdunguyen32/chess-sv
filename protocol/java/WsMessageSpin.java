package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOSpinItem;

public final class WsMessageSpin{
	public static final class C2SSpinList{
		private static final Logger logger = LoggerFactory.getLogger(C2SSpinList.class);
		public int type;
		public boolean is_force;
		@Override
		public String toString() {
			return "C2SSpinList [type="+type+",is_force="+is_force+",]";
		}
		public static final int id = 401;

		public static C2SSpinList parse(MyRequestMessage request){
			C2SSpinList retObj = new C2SSpinList();
			try{
			retObj.type=request.readInt();
			retObj.is_force=request.readBool();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSpinList{
		private static final Logger logger = LoggerFactory.getLogger(S2CSpinList.class);
		public int type;
		public boolean is_force;
		public List<IOSpinItem> items;
		public long free;
		@Override
		public String toString() {
			return "S2CSpinList [type="+type+",is_force="+is_force+",items="+items+",free="+free+",]";
		}
		public static final int msgCode = 402;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 402);
			retMsg.writeInt(type);
			retMsg.writeBool(is_force);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IOSpinItem items1 : items){
					retMsg.writeInt(items1.GSID);
					retMsg.writeInt(items1.COUNT);
					retMsg.writeInt(items1.REPEAT);
				}
			}
			retMsg.writeLong(free);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSpinRoll{
		private static final Logger logger = LoggerFactory.getLogger(C2SSpinRoll.class);
		public int type;
		public int times;
		@Override
		public String toString() {
			return "C2SSpinRoll [type="+type+",times="+times+",]";
		}
		public static final int id = 403;

		public static C2SSpinRoll parse(MyRequestMessage request){
			C2SSpinRoll retObj = new C2SSpinRoll();
			try{
			retObj.type=request.readInt();
			retObj.times=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSpinRoll{
		private static final Logger logger = LoggerFactory.getLogger(S2CSpinRoll.class);
		public int type;
		public int times;
		public int pos;
		public List<IOSpinItem> items;
		@Override
		public String toString() {
			return "S2CSpinRoll [type="+type+",times="+times+",pos="+pos+",items="+items+",]";
		}
		public static final int msgCode = 404;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 404);
			retMsg.writeInt(type);
			retMsg.writeInt(times);
			retMsg.writeInt(pos);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IOSpinItem items1 : items){
					retMsg.writeInt(items1.GSID);
					retMsg.writeInt(items1.COUNT);
					retMsg.writeInt(items1.REPEAT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSpinBoxOpen{
		private static final Logger logger = LoggerFactory.getLogger(C2SSpinBoxOpen.class);
		public int type;
		public int box_index;
		@Override
		public String toString() {
			return "C2SSpinBoxOpen [type="+type+",box_index="+box_index+",]";
		}
		public static final int id = 405;

		public static C2SSpinBoxOpen parse(MyRequestMessage request){
			C2SSpinBoxOpen retObj = new C2SSpinBoxOpen();
			try{
			retObj.type=request.readInt();
			retObj.box_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSpinBoxOpen{
		private static final Logger logger = LoggerFactory.getLogger(S2CSpinBoxOpen.class);
		public int type;
		public int box_index;
		public List<IOSpinItem> rewards;
		@Override
		public String toString() {
			return "S2CSpinBoxOpen [type="+type+",box_index="+box_index+",rewards="+rewards+",]";
		}
		public static final int msgCode = 406;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 406);
			retMsg.writeInt(type);
			retMsg.writeInt(box_index);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(IOSpinItem rewards1 : rewards){
					retMsg.writeInt(rewards1.GSID);
					retMsg.writeInt(rewards1.COUNT);
					retMsg.writeInt(rewards1.REPEAT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSpinBuy{
		private static final Logger logger = LoggerFactory.getLogger(C2SSpinBuy.class);
		public int count;
		@Override
		public String toString() {
			return "C2SSpinBuy [count="+count+",]";
		}
		public static final int id = 407;

		public static C2SSpinBuy parse(MyRequestMessage request){
			C2SSpinBuy retObj = new C2SSpinBuy();
			try{
			retObj.count=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSpinBuy{
		private static final Logger logger = LoggerFactory.getLogger(S2CSpinBuy.class);
		public static final int msgCode = 408;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 408);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSpinBoxReset{
		private static final Logger logger = LoggerFactory.getLogger(C2SSpinBoxReset.class);
		public int type;
		@Override
		public String toString() {
			return "C2SSpinBoxReset [type="+type+",]";
		}
		public static final int id = 409;

		public static C2SSpinBoxReset parse(MyRequestMessage request){
			C2SSpinBoxReset retObj = new C2SSpinBoxReset();
			try{
			retObj.type=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSpinBoxReset{
		private static final Logger logger = LoggerFactory.getLogger(S2CSpinBoxReset.class);
		public int type;
		public S2CSpinBoxReset(int ptype){
			type=ptype;
		}
		public S2CSpinBoxReset(){}
		@Override
		public String toString() {
			return "S2CSpinBoxReset [type="+type+",]";
		}
		public static final int msgCode = 410;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 410);
			retMsg.writeInt(type);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
