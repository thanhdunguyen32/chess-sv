package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.Map;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOFriendEntity;
import ws.WsMessageBase.IOpstatus;
import ws.WsMessageBase.IORewardItem;
import ws.WsMessageBase.IOFriendChapter;
import ws.WsMessageBase.IOFriendBoss;
import ws.WsMessageBase.IOGeneralSimple;

public final class WsMessageFriend{
	public static final class C2SFriendList{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendList.class);
		public static final int id = 451;
	}
	public static final class S2CFriendList{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendList.class);
		public int maxcount;
		public int surplusgiftmax;
		public List<IOFriendEntity> arrfriend;
		@Override
		public String toString() {
			return "S2CFriendList [maxcount="+maxcount+",surplusgiftmax="+surplusgiftmax+",arrfriend="+arrfriend+",]";
		}
		public static final int msgCode = 452;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 452);
			retMsg.writeInt(maxcount);
			retMsg.writeInt(surplusgiftmax);
			if(arrfriend == null || arrfriend.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(arrfriend.size());
				for(IOFriendEntity arrfriend1 : arrfriend){
					retMsg.writeInt(arrfriend1.id);
					retMsg.writeString(arrfriend1.rname);
					retMsg.writeInt(arrfriend1.iconid);
					retMsg.writeInt(arrfriend1.headid);
					retMsg.writeInt(arrfriend1.frameid);
					retMsg.writeInt(arrfriend1.level);
					retMsg.writeInt(arrfriend1.vipLevel);
					retMsg.writeInt(arrfriend1.power);
					retMsg.writeLong(arrfriend1.lasttime);
					if(arrfriend1.pstatus == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(arrfriend1.pstatus.send);
					retMsg.writeInt(arrfriend1.pstatus.receive);
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SFriendRecommandList{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendRecommandList.class);
		public static final int id = 453;
	}
	public static final class S2CFriendRecommandList{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendRecommandList.class);
		public List<IOFriendEntity> items;
		@Override
		public String toString() {
			return "S2CFriendRecommandList [items="+items+",]";
		}
		public static final int msgCode = 454;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 454);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IOFriendEntity items1 : items){
					retMsg.writeInt(items1.id);
					retMsg.writeString(items1.rname);
					retMsg.writeInt(items1.iconid);
					retMsg.writeInt(items1.headid);
					retMsg.writeInt(items1.frameid);
					retMsg.writeInt(items1.level);
					retMsg.writeInt(items1.vipLevel);
					retMsg.writeInt(items1.power);
					retMsg.writeLong(items1.lasttime);
					if(items1.pstatus == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(items1.pstatus.send);
					retMsg.writeInt(items1.pstatus.receive);
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SFriendSearch{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendSearch.class);
		public String rname;
		@Override
		public String toString() {
			return "C2SFriendSearch [rname="+rname+",]";
		}
		public static final int id = 455;

		public static C2SFriendSearch parse(MyRequestMessage request){
			C2SFriendSearch retObj = new C2SFriendSearch();
			try{
			retObj.rname=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CFriendSearch{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendSearch.class);
		public String rname;
		public List<IOFriendEntity> items;
		@Override
		public String toString() {
			return "S2CFriendSearch [rname="+rname+",items="+items+",]";
		}
		public static final int msgCode = 456;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 456);
			retMsg.writeString(rname);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IOFriendEntity items1 : items){
					retMsg.writeInt(items1.id);
					retMsg.writeString(items1.rname);
					retMsg.writeInt(items1.iconid);
					retMsg.writeInt(items1.headid);
					retMsg.writeInt(items1.frameid);
					retMsg.writeInt(items1.level);
					retMsg.writeInt(items1.vipLevel);
					retMsg.writeInt(items1.power);
					retMsg.writeLong(items1.lasttime);
					if(items1.pstatus == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(items1.pstatus.send);
					retMsg.writeInt(items1.pstatus.receive);
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SFriendApply{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendApply.class);
		public int[] role_ids;
		@Override
		public String toString() {
			return "C2SFriendApply [role_ids="+java.util.Arrays.toString(role_ids)+",]";
		}
		public static final int id = 457;

		public static C2SFriendApply parse(MyRequestMessage request){
			C2SFriendApply retObj = new C2SFriendApply();
			try{
			int role_ids_size = request.readInt();
				retObj.role_ids = new int[role_ids_size];
				for(int i=0;i<role_ids_size;i++){
					retObj.role_ids[i]=request.readInt();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CFriendApply{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendApply.class);
		public List<Integer> role_ids;
		@Override
		public String toString() {
			return "S2CFriendApply [role_ids="+role_ids+",]";
		}
		public static final int msgCode = 458;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 458);
			if(role_ids == null || role_ids.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(role_ids.size());
				for(Integer role_ids1 : role_ids){
			retMsg.writeInt(role_ids1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SFriendDel{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendDel.class);
		public int role_id;
		@Override
		public String toString() {
			return "C2SFriendDel [role_id="+role_id+",]";
		}
		public static final int id = 459;

		public static C2SFriendDel parse(MyRequestMessage request){
			C2SFriendDel retObj = new C2SFriendDel();
			try{
			retObj.role_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CFriendDel{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendDel.class);
		public int role_id;
		public S2CFriendDel(int prole_id){
			role_id=prole_id;
		}
		public S2CFriendDel(){}
		@Override
		public String toString() {
			return "S2CFriendDel [role_id="+role_id+",]";
		}
		public static final int msgCode = 460;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 460);
			retMsg.writeInt(role_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SFriendApplyList{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendApplyList.class);
		public static final int id = 461;
	}
	public static final class S2CFriendApplyList{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendApplyList.class);
		public List<IOFriendEntity> items;
		@Override
		public String toString() {
			return "S2CFriendApplyList [items="+items+",]";
		}
		public static final int msgCode = 462;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 462);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IOFriendEntity items1 : items){
					retMsg.writeInt(items1.id);
					retMsg.writeString(items1.rname);
					retMsg.writeInt(items1.iconid);
					retMsg.writeInt(items1.headid);
					retMsg.writeInt(items1.frameid);
					retMsg.writeInt(items1.level);
					retMsg.writeInt(items1.vipLevel);
					retMsg.writeInt(items1.power);
					retMsg.writeLong(items1.lasttime);
					if(items1.pstatus == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(items1.pstatus.send);
					retMsg.writeInt(items1.pstatus.receive);
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SFriendApplyHandle{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendApplyHandle.class);
		public boolean is_agree;
		public int[] role_ids;
		@Override
		public String toString() {
			return "C2SFriendApplyHandle [is_agree="+is_agree+",role_ids="+java.util.Arrays.toString(role_ids)+",]";
		}
		public static final int id = 463;

		public static C2SFriendApplyHandle parse(MyRequestMessage request){
			C2SFriendApplyHandle retObj = new C2SFriendApplyHandle();
			try{
			retObj.is_agree=request.readBool();
			int role_ids_size = request.readInt();
				retObj.role_ids = new int[role_ids_size];
				for(int i=0;i<role_ids_size;i++){
					retObj.role_ids[i]=request.readInt();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CFriendApplyHandle{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendApplyHandle.class);
		public boolean is_agree;
		public List<Integer> role_ids;
		@Override
		public String toString() {
			return "S2CFriendApplyHandle [is_agree="+is_agree+",role_ids="+role_ids+",]";
		}
		public static final int msgCode = 464;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 464);
			retMsg.writeBool(is_agree);
			if(role_ids == null || role_ids.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(role_ids.size());
				for(Integer role_ids1 : role_ids){
			retMsg.writeInt(role_ids1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SFriendshipGive{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendshipGive.class);
		public int role_id;
		@Override
		public String toString() {
			return "C2SFriendshipGive [role_id="+role_id+",]";
		}
		public static final int id = 465;

		public static C2SFriendshipGive parse(MyRequestMessage request){
			C2SFriendshipGive retObj = new C2SFriendshipGive();
			try{
			retObj.role_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CFriendshipGive{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendshipGive.class);
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2CFriendshipGive [rewards="+rewards+",]";
		}
		public static final int msgCode = 466;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 466);
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
	public static final class C2SFriendshipReceive{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendshipReceive.class);
		public int role_id;
		@Override
		public String toString() {
			return "C2SFriendshipReceive [role_id="+role_id+",]";
		}
		public static final int id = 467;

		public static C2SFriendshipReceive parse(MyRequestMessage request){
			C2SFriendshipReceive retObj = new C2SFriendshipReceive();
			try{
			retObj.role_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CFriendshipReceive{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendshipReceive.class);
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2CFriendshipReceive [rewards="+rewards+",]";
		}
		public static final int msgCode = 468;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 468);
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
	public static final class C2SFriendshipOnekey{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendshipOnekey.class);
		public static final int id = 469;
	}
	public static final class S2CFriendshipOnekey{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendshipOnekey.class);
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2CFriendshipOnekey [rewards="+rewards+",]";
		}
		public static final int msgCode = 470;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 470);
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
	public static final class C2SFriendExploreData{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendExploreData.class);
		public static final int id = 471;
	}
	public static final class S2CFriendExploreData{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendExploreData.class);
		public int maxbphys;
		public int maxqphys;
		public IOFriendBoss boss;
		public List<IOFriendChapter> chapters;
		public int kill;
		@Override
		public String toString() {
			return "S2CFriendExploreData [maxbphys="+maxbphys+",maxqphys="+maxqphys+",boss="+boss+",chapters="+chapters+",kill="+kill+",]";
		}
		public static final int msgCode = 472;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 472);
			retMsg.writeInt(maxbphys);
			retMsg.writeInt(maxqphys);
			if(boss == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(boss.ownPlayerId);
					retMsg.writeInt(boss.id);
					retMsg.writeInt(boss.gsid);
					retMsg.writeString(boss.name);
					retMsg.writeInt(boss.level);
					if(boss.rewards == null || boss.rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(boss.rewards.size());
				for(IORewardItem boss_rewards1 : boss.rewards){
					retMsg.writeInt(boss_rewards1.GSID);
					retMsg.writeInt(boss_rewards1.COUNT);
				}
			}
					retMsg.writeLong(boss.etime);
					retMsg.writeLong(boss.last);
					retMsg.writeLong(boss.maxhp);
					retMsg.writeLong(boss.nowhp);
					retMsg.writeInt(boss.lastdamges);
					if(boss.bset == null || boss.bset.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(boss.bset.size());
				for(Map.Entry<Integer,IOGeneralSimple> boss_bset1 : boss.bset.entrySet()){
			retMsg.writeInt(boss_bset1.getKey());
					retMsg.writeInt(boss_bset1.getValue().pos);
					retMsg.writeInt(boss_bset1.getValue().gsid);
					retMsg.writeInt(boss_bset1.getValue().level);
					retMsg.writeInt(boss_bset1.getValue().hpcover);
					retMsg.writeInt(boss_bset1.getValue().pclass);
					retMsg.writeLong(boss_bset1.getValue().nowhp);
					retMsg.writeFloat(boss_bset1.getValue().exhp);
					retMsg.writeFloat(boss_bset1.getValue().exatk);
				}
			}
			}
			if(chapters == null || chapters.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(chapters.size());
				for(IOFriendChapter chapters1 : chapters){
					retMsg.writeInt(chapters1.status);
					retMsg.writeInt(chapters1.chapterid);
					retMsg.writeInt(chapters1.power);
					retMsg.writeInt(chapters1.exploremin);
					if(chapters1.friends == null || chapters1.friends.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(chapters1.friends.size());
				for(IOFriendEntity chapters1_friends1 : chapters1.friends){
					retMsg.writeInt(chapters1_friends1.id);
					retMsg.writeString(chapters1_friends1.rname);
					retMsg.writeInt(chapters1_friends1.iconid);
					retMsg.writeInt(chapters1_friends1.headid);
					retMsg.writeInt(chapters1_friends1.frameid);
					retMsg.writeInt(chapters1_friends1.level);
					retMsg.writeInt(chapters1_friends1.vipLevel);
					retMsg.writeInt(chapters1_friends1.power);
					retMsg.writeLong(chapters1_friends1.lasttime);
					if(chapters1_friends1.pstatus == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(chapters1_friends1.pstatus.send);
					retMsg.writeInt(chapters1_friends1.pstatus.receive);
			}
				}
			}
					retMsg.writeLong(chapters1.etime);
				}
			}
			retMsg.writeInt(kill);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SFriendOpenExplore{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendOpenExplore.class);
		public int chapter_id;
		public int[] friends;
		@Override
		public String toString() {
			return "C2SFriendOpenExplore [chapter_id="+chapter_id+",friends="+java.util.Arrays.toString(friends)+",]";
		}
		public static final int id = 473;

		public static C2SFriendOpenExplore parse(MyRequestMessage request){
			C2SFriendOpenExplore retObj = new C2SFriendOpenExplore();
			try{
			retObj.chapter_id=request.readInt();
			int friends_size = request.readInt();
				retObj.friends = new int[friends_size];
				for(int i=0;i<friends_size;i++){
					retObj.friends[i]=request.readInt();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CFriendOpenExplore{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendOpenExplore.class);
		public long etime;
		public S2CFriendOpenExplore(long petime){
			etime=petime;
		}
		public S2CFriendOpenExplore(){}
		@Override
		public String toString() {
			return "S2CFriendOpenExplore [etime="+etime+",]";
		}
		public static final int msgCode = 474;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 474);
			retMsg.writeLong(etime);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SFriendReceiveExplore{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendReceiveExplore.class);
		public int chapter_id;
		@Override
		public String toString() {
			return "C2SFriendReceiveExplore [chapter_id="+chapter_id+",]";
		}
		public static final int id = 475;

		public static C2SFriendReceiveExplore parse(MyRequestMessage request){
			C2SFriendReceiveExplore retObj = new C2SFriendReceiveExplore();
			try{
			retObj.chapter_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CFriendReceiveExplore{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendReceiveExplore.class);
		public List<IORewardItem> rewards;
		public String bossid;
		@Override
		public String toString() {
			return "S2CFriendReceiveExplore [rewards="+rewards+",bossid="+bossid+",]";
		}
		public static final int msgCode = 476;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 476);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(IORewardItem rewards1 : rewards){
					retMsg.writeInt(rewards1.GSID);
					retMsg.writeInt(rewards1.COUNT);
				}
			}
			retMsg.writeString(bossid);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SFriendBossFarm{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendBossFarm.class);
		public int boss_owner_id;
		@Override
		public String toString() {
			return "C2SFriendBossFarm [boss_owner_id="+boss_owner_id+",]";
		}
		public static final int id = 477;

		public static C2SFriendBossFarm parse(MyRequestMessage request){
			C2SFriendBossFarm retObj = new C2SFriendBossFarm();
			try{
			retObj.boss_owner_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CFriendBossFarm{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendBossFarm.class);
		public S2CFriendExploreData ret;
		@Override
		public String toString() {
			return "S2CFriendBossFarm [ret="+ret+",]";
		}
		public static final int msgCode = 478;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 478);
			if(ret == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(ret.maxbphys);
					retMsg.writeInt(ret.maxqphys);
					if(ret.boss == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(ret.boss.ownPlayerId);
					retMsg.writeInt(ret.boss.id);
					retMsg.writeInt(ret.boss.gsid);
					retMsg.writeString(ret.boss.name);
					retMsg.writeInt(ret.boss.level);
					if(ret.boss.rewards == null || ret.boss.rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(ret.boss.rewards.size());
				for(IORewardItem ret_boss_rewards1 : ret.boss.rewards){
					retMsg.writeInt(ret_boss_rewards1.GSID);
					retMsg.writeInt(ret_boss_rewards1.COUNT);
				}
			}
					retMsg.writeLong(ret.boss.etime);
					retMsg.writeLong(ret.boss.last);
					retMsg.writeLong(ret.boss.maxhp);
					retMsg.writeLong(ret.boss.nowhp);
					retMsg.writeInt(ret.boss.lastdamges);
					if(ret.boss.bset == null || ret.boss.bset.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(ret.boss.bset.size());
				for(Map.Entry<Integer,IOGeneralSimple> ret_boss_bset1 : ret.boss.bset.entrySet()){
			retMsg.writeInt(ret_boss_bset1.getKey());
					retMsg.writeInt(ret_boss_bset1.getValue().pos);
					retMsg.writeInt(ret_boss_bset1.getValue().gsid);
					retMsg.writeInt(ret_boss_bset1.getValue().level);
					retMsg.writeInt(ret_boss_bset1.getValue().hpcover);
					retMsg.writeInt(ret_boss_bset1.getValue().pclass);
					retMsg.writeLong(ret_boss_bset1.getValue().nowhp);
					retMsg.writeFloat(ret_boss_bset1.getValue().exhp);
					retMsg.writeFloat(ret_boss_bset1.getValue().exatk);
				}
			}
			}
					if(ret.chapters == null || ret.chapters.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(ret.chapters.size());
				for(IOFriendChapter ret_chapters1 : ret.chapters){
					retMsg.writeInt(ret_chapters1.status);
					retMsg.writeInt(ret_chapters1.chapterid);
					retMsg.writeInt(ret_chapters1.power);
					retMsg.writeInt(ret_chapters1.exploremin);
					if(ret_chapters1.friends == null || ret_chapters1.friends.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(ret_chapters1.friends.size());
				for(IOFriendEntity ret_chapters1_friends1 : ret_chapters1.friends){
					retMsg.writeInt(ret_chapters1_friends1.id);
					retMsg.writeString(ret_chapters1_friends1.rname);
					retMsg.writeInt(ret_chapters1_friends1.iconid);
					retMsg.writeInt(ret_chapters1_friends1.headid);
					retMsg.writeInt(ret_chapters1_friends1.frameid);
					retMsg.writeInt(ret_chapters1_friends1.level);
					retMsg.writeInt(ret_chapters1_friends1.vipLevel);
					retMsg.writeInt(ret_chapters1_friends1.power);
					retMsg.writeLong(ret_chapters1_friends1.lasttime);
					if(ret_chapters1_friends1.pstatus == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(ret_chapters1_friends1.pstatus.send);
					retMsg.writeInt(ret_chapters1_friends1.pstatus.receive);
			}
				}
			}
					retMsg.writeLong(ret_chapters1.etime);
				}
			}
					retMsg.writeInt(ret.kill);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
