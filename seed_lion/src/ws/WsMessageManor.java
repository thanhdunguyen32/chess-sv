package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.Map;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IORewardItem;
import ws.WsMessageBase.IOManorBuilding;
import ws.WsMessageBase.IOManorFieldBoss;
import ws.WsMessageBase.IOManorFieldEnemy;
import ws.WsMessageBase.IOSrenderState;
import ws.WsMessageBase.IOManorFriend;

public final class WsMessageManor{
	public static final class C2SOfficialSalary{
		private static final Logger logger = LoggerFactory.getLogger(C2SOfficialSalary.class);
		public static final int id = 851;
	}
	public static final class S2COfficialSalary{
		private static final Logger logger = LoggerFactory.getLogger(S2COfficialSalary.class);
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2COfficialSalary [rewards="+rewards+",]";
		}
		public static final int msgCode = 852;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 852);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(IORewardItem rewards1 : rewards){
					retMsg.writeInt(rewards1.GSID);
					retMsg.writeInt(rewards1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SOfficialItemReward{
		private static final Logger logger = LoggerFactory.getLogger(C2SOfficialItemReward.class);
		public int official_id;
		public int item_index;
		@Override
		public String toString() {
			return "C2SOfficialItemReward [official_id="+official_id+",item_index="+item_index+",]";
		}
		public static final int id = 853;

		public static C2SOfficialItemReward parse(MyRequestMessage request){
			C2SOfficialItemReward retObj = new C2SOfficialItemReward();
			try{
			retObj.official_id=request.readInt();
			retObj.item_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2COfficialItemReward{
		private static final Logger logger = LoggerFactory.getLogger(S2COfficialItemReward.class);
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2COfficialItemReward [rewards="+rewards+",]";
		}
		public static final int msgCode = 854;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 854);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(IORewardItem rewards1 : rewards){
					retMsg.writeInt(rewards1.GSID);
					retMsg.writeInt(rewards1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SOfficialPromo{
		private static final Logger logger = LoggerFactory.getLogger(C2SOfficialPromo.class);
		public static final int id = 855;
	}
	public static final class S2COfficialPromo{
		private static final Logger logger = LoggerFactory.getLogger(S2COfficialPromo.class);
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2COfficialPromo [rewards="+rewards+",]";
		}
		public static final int msgCode = 856;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 856);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(IORewardItem rewards1 : rewards){
					retMsg.writeInt(rewards1.GSID);
					retMsg.writeInt(rewards1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SManorInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SManorInfo.class);
		public static final int id = 857;
	}
	public static final class S2CManorInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CManorInfo.class);
		public int manorlv;
		public List<IOManorBuilding> buildings;
		public List<IORewardItem> items;
		@Override
		public String toString() {
			return "S2CManorInfo [manorlv="+manorlv+",buildings="+buildings+",items="+items+",]";
		}
		public static final int msgCode = 858;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 858);
			retMsg.writeInt(manorlv);
			if(buildings == null || buildings.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(buildings.size());
				for(IOManorBuilding buildings1 : buildings){
					retMsg.writeInt(buildings1.lv);
					retMsg.writeLong(buildings1.rid);
					retMsg.writeInt(buildings1.id);
					retMsg.writeInt(buildings1.type);
					retMsg.writeInt(buildings1.pos);
					retMsg.writeLong(buildings1.lastgain);
					if(buildings1.items == null || buildings1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(buildings1.items.size());
				for(IORewardItem buildings1_items1 : buildings1.items){
					retMsg.writeInt(buildings1_items1.GSID);
					retMsg.writeInt(buildings1_items1.COUNT);
				}
			}
				}
			}
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IORewardItem items1 : items){
					retMsg.writeInt(items1.GSID);
					retMsg.writeInt(items1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SManorBuild{
		private static final Logger logger = LoggerFactory.getLogger(C2SManorBuild.class);
		public int pos;
		public int bid;
		@Override
		public String toString() {
			return "C2SManorBuild [pos="+pos+",bid="+bid+",]";
		}
		public static final int id = 859;

		public static C2SManorBuild parse(MyRequestMessage request){
			C2SManorBuild retObj = new C2SManorBuild();
			try{
			retObj.pos=request.readInt();
			retObj.bid=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CManorBuild{
		private static final Logger logger = LoggerFactory.getLogger(S2CManorBuild.class);
		public static final int msgCode = 860;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 860);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SManorReset{
		private static final Logger logger = LoggerFactory.getLogger(C2SManorReset.class);
		public int pos;
		@Override
		public String toString() {
			return "C2SManorReset [pos="+pos+",]";
		}
		public static final int id = 861;

		public static C2SManorReset parse(MyRequestMessage request){
			C2SManorReset retObj = new C2SManorReset();
			try{
			retObj.pos=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CManorReset{
		private static final Logger logger = LoggerFactory.getLogger(S2CManorReset.class);
		public List<IORewardItem> items;
		@Override
		public String toString() {
			return "S2CManorReset [items="+items+",]";
		}
		public static final int msgCode = 862;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 862);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IORewardItem items1 : items){
					retMsg.writeInt(items1.GSID);
					retMsg.writeInt(items1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SManorReward{
		private static final Logger logger = LoggerFactory.getLogger(C2SManorReward.class);
		public int pos;
		@Override
		public String toString() {
			return "C2SManorReward [pos="+pos+",]";
		}
		public static final int id = 863;

		public static C2SManorReward parse(MyRequestMessage request){
			C2SManorReward retObj = new C2SManorReward();
			try{
			retObj.pos=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CManorReward{
		private static final Logger logger = LoggerFactory.getLogger(S2CManorReward.class);
		public List<IORewardItem> items;
		@Override
		public String toString() {
			return "S2CManorReward [items="+items+",]";
		}
		public static final int msgCode = 864;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 864);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IORewardItem items1 : items){
					retMsg.writeInt(items1.GSID);
					retMsg.writeInt(items1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SManorFieldInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SManorFieldInfo.class);
		public static final int id = 865;
	}
	public static final class S2CManorFieldInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CManorFieldInfo.class);
		public long etime;
		public long btime;
		public List<IORewardItem> items;
		public int mop;
		public int blv;
		public int elv;
		public IOManorFieldBoss boss;
		public List<IOManorFieldEnemy> enemy;
		@Override
		public String toString() {
			return "S2CManorFieldInfo [etime="+etime+",btime="+btime+",items="+items+",mop="+mop+",blv="+blv+",elv="+elv+",boss="+boss+",enemy="+enemy+",]";
		}
		public static final int msgCode = 866;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 866);
			retMsg.writeLong(etime);
			retMsg.writeLong(btime);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IORewardItem items1 : items){
					retMsg.writeInt(items1.GSID);
					retMsg.writeInt(items1.COUNT);
				}
			}
			retMsg.writeInt(mop);
			retMsg.writeInt(blv);
			retMsg.writeInt(elv);
			if(boss == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(boss.state);
					retMsg.writeInt(boss.lastdamage);
					retMsg.writeInt(boss.maxhp);
					retMsg.writeInt(boss.nowhp);
			}
			if(enemy == null || enemy.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(enemy.size());
				for(IOManorFieldEnemy enemy1 : enemy){
					if(enemy1.boxItem == null || enemy1.boxItem.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(enemy1.boxItem.size());
				for(IORewardItem enemy1_boxItem1 : enemy1.boxItem){
					retMsg.writeInt(enemy1_boxItem1.GSID);
					retMsg.writeInt(enemy1_boxItem1.COUNT);
				}
			}
					if(enemy1.reward == null || enemy1.reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(enemy1.reward.size());
				for(IORewardItem enemy1_reward1 : enemy1.reward){
					retMsg.writeInt(enemy1_reward1.GSID);
					retMsg.writeInt(enemy1_reward1.COUNT);
				}
			}
					retMsg.writeInt(enemy1.id);
					retMsg.writeInt(enemy1.state);
					retMsg.writeInt(enemy1.hasOpen);
					if(enemy1.cachehp == null || enemy1.cachehp.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(enemy1.cachehp.size());
				for(Integer enemy1_cachehp1 : enemy1.cachehp){
			retMsg.writeInt(enemy1_cachehp1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SManorGetBox{
		private static final Logger logger = LoggerFactory.getLogger(C2SManorGetBox.class);
		public int index;
		@Override
		public String toString() {
			return "C2SManorGetBox [index="+index+",]";
		}
		public static final int id = 867;

		public static C2SManorGetBox parse(MyRequestMessage request){
			C2SManorGetBox retObj = new C2SManorGetBox();
			try{
			retObj.index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CManorGetBox{
		private static final Logger logger = LoggerFactory.getLogger(S2CManorGetBox.class);
		public List<IORewardItem> items;
		public List<IORewardItem> boxItem;
		@Override
		public String toString() {
			return "S2CManorGetBox [items="+items+",boxItem="+boxItem+",]";
		}
		public static final int msgCode = 868;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 868);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IORewardItem items1 : items){
					retMsg.writeInt(items1.GSID);
					retMsg.writeInt(items1.COUNT);
				}
			}
			if(boxItem == null || boxItem.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(boxItem.size());
				for(IORewardItem boxItem1 : boxItem){
					retMsg.writeInt(boxItem1.GSID);
					retMsg.writeInt(boxItem1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SManorPatrol{
		private static final Logger logger = LoggerFactory.getLogger(C2SManorPatrol.class);
		public static final int id = 869;
	}
	public static final class S2CManorPatrol{
		private static final Logger logger = LoggerFactory.getLogger(S2CManorPatrol.class);
		public static final int msgCode = 870;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 870);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SManorMop{
		private static final Logger logger = LoggerFactory.getLogger(C2SManorMop.class);
		public int times;
		@Override
		public String toString() {
			return "C2SManorMop [times="+times+",]";
		}
		public static final int id = 871;

		public static C2SManorMop parse(MyRequestMessage request){
			C2SManorMop retObj = new C2SManorMop();
			try{
			retObj.times=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CManorMop{
		private static final Logger logger = LoggerFactory.getLogger(S2CManorMop.class);
		public List<IORewardItem> reward;
		public int nowhp;
		@Override
		public String toString() {
			return "S2CManorMop [reward="+reward+",nowhp="+nowhp+",]";
		}
		public static final int msgCode = 872;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 872);
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(IORewardItem reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
				}
			}
			retMsg.writeInt(nowhp);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSrenderList{
		private static final Logger logger = LoggerFactory.getLogger(C2SSrenderList.class);
		public static final int id = 873;
	}
	public static final class S2CSrenderList{
		private static final Logger logger = LoggerFactory.getLogger(S2CSrenderList.class);
		public Map<Integer,IOSrenderState> list;
		@Override
		public String toString() {
			return "S2CSrenderList [list="+list+",]";
		}
		public static final int msgCode = 874;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 874);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(Map.Entry<Integer,IOSrenderState> list1 : list.entrySet()){
			retMsg.writeInt(list1.getKey());
					retMsg.writeInt(list1.getValue().gsid);
					retMsg.writeInt(list1.getValue().loyal);
					retMsg.writeInt(list1.getValue().state);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSrenderPersua{
		private static final Logger logger = LoggerFactory.getLogger(C2SSrenderPersua.class);
		public int gsid;
		@Override
		public String toString() {
			return "C2SSrenderPersua [gsid="+gsid+",]";
		}
		public static final int id = 875;

		public static C2SSrenderPersua parse(MyRequestMessage request){
			C2SSrenderPersua retObj = new C2SSrenderPersua();
			try{
			retObj.gsid=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSrenderPersua{
		private static final Logger logger = LoggerFactory.getLogger(S2CSrenderPersua.class);
		public int loyal;
		public IORewardItem reward;
		@Override
		public String toString() {
			return "S2CSrenderPersua [loyal="+loyal+",reward="+reward+",]";
		}
		public static final int msgCode = 876;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 876);
			retMsg.writeInt(loyal);
			if(reward == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(reward.GSID);
					retMsg.writeInt(reward.COUNT);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSrenderBehead{
		private static final Logger logger = LoggerFactory.getLogger(C2SSrenderBehead.class);
		public int gsid;
		@Override
		public String toString() {
			return "C2SSrenderBehead [gsid="+gsid+",]";
		}
		public static final int id = 877;

		public static C2SSrenderBehead parse(MyRequestMessage request){
			C2SSrenderBehead retObj = new C2SSrenderBehead();
			try{
			retObj.gsid=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSrenderBehead{
		private static final Logger logger = LoggerFactory.getLogger(S2CSrenderBehead.class);
		public List<IORewardItem> awards;
		@Override
		public String toString() {
			return "S2CSrenderBehead [awards="+awards+",]";
		}
		public static final int msgCode = 878;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 878);
			if(awards == null || awards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(awards.size());
				for(IORewardItem awards1 : awards){
					retMsg.writeInt(awards1.GSID);
					retMsg.writeInt(awards1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SManorFriendList{
		private static final Logger logger = LoggerFactory.getLogger(C2SManorFriendList.class);
		public static final int id = 879;
	}
	public static final class S2CManorFriendList{
		private static final Logger logger = LoggerFactory.getLogger(S2CManorFriendList.class);
		public List<IOManorFriend> list;
		@Override
		public String toString() {
			return "S2CManorFriendList [list="+list+",]";
		}
		public static final int msgCode = 880;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 880);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOManorFriend list1 : list){
					retMsg.writeInt(list1.icon);
					retMsg.writeInt(list1.frameid);
					retMsg.writeInt(list1.headid);
					retMsg.writeString(list1.name);
					retMsg.writeInt(list1.id);
					retMsg.writeInt(list1.level);
					retMsg.writeInt(list1.power);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
