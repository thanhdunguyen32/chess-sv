package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOMythicalAnimal;
import ws.WsMessageBase.IORewardItem;

public final class WsMessageMythical{
	public static final class C2SMythicalList{
		private static final Logger logger = LoggerFactory.getLogger(C2SMythicalList.class);
		public static final int id = 751;
	}
	public static final class S2CMythicalList{
		private static final Logger logger = LoggerFactory.getLogger(S2CMythicalList.class);
		public List<IOMythicalAnimal> mythical_list;
		@Override
		public String toString() {
			return "S2CMythicalList [mythical_list="+mythical_list+",]";
		}
		public static final int msgCode = 752;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 752);
			if(mythical_list == null || mythical_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mythical_list.size());
				for(IOMythicalAnimal mythical_list1 : mythical_list){
					retMsg.writeInt(mythical_list1.tid);
					retMsg.writeInt(mythical_list1.pclass);
					retMsg.writeInt(mythical_list1.level);
					if(mythical_list1.pskill == null || mythical_list1.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mythical_list1.pskill.size());
				for(Integer mythical_list1_pskill1 : mythical_list1.pskill){
			retMsg.writeInt(mythical_list1_pskill1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMythicalLevelUp{
		private static final Logger logger = LoggerFactory.getLogger(C2SMythicalLevelUp.class);
		public int mythical_id;
		@Override
		public String toString() {
			return "C2SMythicalLevelUp [mythical_id="+mythical_id+",]";
		}
		public static final int id = 753;

		public static C2SMythicalLevelUp parse(MyRequestMessage request){
			C2SMythicalLevelUp retObj = new C2SMythicalLevelUp();
			try{
			retObj.mythical_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMythicalLevelUp{
		private static final Logger logger = LoggerFactory.getLogger(S2CMythicalLevelUp.class);
		public List<IOMythicalAnimal> mythicals;
		@Override
		public String toString() {
			return "S2CMythicalLevelUp [mythicals="+mythicals+",]";
		}
		public static final int msgCode = 754;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 754);
			if(mythicals == null || mythicals.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mythicals.size());
				for(IOMythicalAnimal mythicals1 : mythicals){
					retMsg.writeInt(mythicals1.tid);
					retMsg.writeInt(mythicals1.pclass);
					retMsg.writeInt(mythicals1.level);
					if(mythicals1.pskill == null || mythicals1.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mythicals1.pskill.size());
				for(Integer mythicals1_pskill1 : mythicals1.pskill){
			retMsg.writeInt(mythicals1_pskill1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMythicalClassUp{
		private static final Logger logger = LoggerFactory.getLogger(C2SMythicalClassUp.class);
		public int mythical_id;
		@Override
		public String toString() {
			return "C2SMythicalClassUp [mythical_id="+mythical_id+",]";
		}
		public static final int id = 755;

		public static C2SMythicalClassUp parse(MyRequestMessage request){
			C2SMythicalClassUp retObj = new C2SMythicalClassUp();
			try{
			retObj.mythical_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMythicalClassUp{
		private static final Logger logger = LoggerFactory.getLogger(S2CMythicalClassUp.class);
		public List<IOMythicalAnimal> mythicals;
		@Override
		public String toString() {
			return "S2CMythicalClassUp [mythicals="+mythicals+",]";
		}
		public static final int msgCode = 756;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 756);
			if(mythicals == null || mythicals.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mythicals.size());
				for(IOMythicalAnimal mythicals1 : mythicals){
					retMsg.writeInt(mythicals1.tid);
					retMsg.writeInt(mythicals1.pclass);
					retMsg.writeInt(mythicals1.level);
					if(mythicals1.pskill == null || mythicals1.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mythicals1.pskill.size());
				for(Integer mythicals1_pskill1 : mythicals1.pskill){
			retMsg.writeInt(mythicals1_pskill1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMythicalPskillUp{
		private static final Logger logger = LoggerFactory.getLogger(C2SMythicalPskillUp.class);
		public int mythical_id;
		public int pskill_index;
		@Override
		public String toString() {
			return "C2SMythicalPskillUp [mythical_id="+mythical_id+",pskill_index="+pskill_index+",]";
		}
		public static final int id = 757;

		public static C2SMythicalPskillUp parse(MyRequestMessage request){
			C2SMythicalPskillUp retObj = new C2SMythicalPskillUp();
			try{
			retObj.mythical_id=request.readInt();
			retObj.pskill_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMythicalPskillUp{
		private static final Logger logger = LoggerFactory.getLogger(S2CMythicalPskillUp.class);
		public List<IOMythicalAnimal> mythicals;
		@Override
		public String toString() {
			return "S2CMythicalPskillUp [mythicals="+mythicals+",]";
		}
		public static final int msgCode = 758;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 758);
			if(mythicals == null || mythicals.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mythicals.size());
				for(IOMythicalAnimal mythicals1 : mythicals){
					retMsg.writeInt(mythicals1.tid);
					retMsg.writeInt(mythicals1.pclass);
					retMsg.writeInt(mythicals1.level);
					if(mythicals1.pskill == null || mythicals1.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mythicals1.pskill.size());
				for(Integer mythicals1_pskill1 : mythicals1.pskill){
			retMsg.writeInt(mythicals1_pskill1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMythicalReset{
		private static final Logger logger = LoggerFactory.getLogger(C2SMythicalReset.class);
		public int mythical_id;
		@Override
		public String toString() {
			return "C2SMythicalReset [mythical_id="+mythical_id+",]";
		}
		public static final int id = 759;

		public static C2SMythicalReset parse(MyRequestMessage request){
			C2SMythicalReset retObj = new C2SMythicalReset();
			try{
			retObj.mythical_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMythicalReset{
		private static final Logger logger = LoggerFactory.getLogger(S2CMythicalReset.class);
		public List<IORewardItem> rewards;
		public List<IOMythicalAnimal> mythicals;
		@Override
		public String toString() {
			return "S2CMythicalReset [rewards="+rewards+",mythicals="+mythicals+",]";
		}
		public static final int msgCode = 760;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 760);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(IORewardItem rewards1 : rewards){
					retMsg.writeInt(rewards1.GSID);
					retMsg.writeInt(rewards1.COUNT);
				}
			}
			if(mythicals == null || mythicals.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mythicals.size());
				for(IOMythicalAnimal mythicals1 : mythicals){
					retMsg.writeInt(mythicals1.tid);
					retMsg.writeInt(mythicals1.pclass);
					retMsg.writeInt(mythicals1.level);
					if(mythicals1.pskill == null || mythicals1.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mythicals1.pskill.size());
				for(Integer mythicals1_pskill1 : mythicals1.pskill){
			retMsg.writeInt(mythicals1_pskill1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
