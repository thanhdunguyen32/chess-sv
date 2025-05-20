package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.Map;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOLegionInfo;
import ws.WsMessageBase.IOLegionDonation;
import ws.WsMessageBase.IOLegionMember;
import ws.WsMessageBase.IOLegionApplyReview;
import ws.WsMessageBase.IORewardItem;
import ws.WsMessageBase.IOLegionFactoryDonation;
import ws.WsMessageBase.IOLegionFactoryMission;
import ws.WsMessageBase.IOLegionLog;
import ws.WsMessageBase.IOLegionBossSelf;
import ws.WsMessageBase.IOLegionBoss;
import ws.WsMessageBase.IOGeneralLegion;
import ws.WsMessageBase.IOLegionRank;
import ws.WsMessageBase.IOWorldBossLegion;
import ws.WsMessageBase.IOWorldBossInfo;
import ws.WsMessageBase.IOWorldBossSelf;
import ws.WsMessageBase.IOWorldBossRank;
import ws.WsMessageBase.IOWorldBossWorldRank;

public final class WsMessageLegion{
	public static final class C2SLegionCreate{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionCreate.class);
		public String name;
		public String notice;
		public int minlv;
		public boolean ispass;
		@Override
		public String toString() {
			return "C2SLegionCreate [name="+name+",notice="+notice+",minlv="+minlv+",ispass="+ispass+",]";
		}
		public static final int id = 621;

		public static C2SLegionCreate parse(MyRequestMessage request){
			C2SLegionCreate retObj = new C2SLegionCreate();
			try{
			retObj.name=request.readString();
			retObj.notice=request.readString();
			retObj.minlv=request.readInt();
			retObj.ispass=request.readBool();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionCreate{
		private static final java.util.logging.Logger logger = LoggerFactory.getLogger(S2CLegionCreate.class);
		public long legion_id;
		public S2CLegionCreate(long plegion_id){
			legion_id=plegion_id;
		}
		public S2CLegionCreate(){}
		@Override
		public String toString() {
			return "S2CLegionCreate [legion_id="+legion_id+",]";
		}
		public static final int msgCode = 622;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 622);
			retMsg.writeLong(legion_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionSet{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionSet.class);
		public String notice;
		public int minlv;
		public int ispass;
		@Override
		public String toString() {
			return "C2SLegionSet [notice="+notice+",minlv="+minlv+",ispass="+ispass+",]";
		}
		public static final int id = 623;

		public static C2SLegionSet parse(MyRequestMessage request){
			C2SLegionSet retObj = new C2SLegionSet();
			try{
			retObj.notice=request.readString();
			retObj.minlv=request.readInt();
			retObj.ispass=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionSet{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionSet.class);
		public static final int msgCode = 624;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 624);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionList{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionList.class);
		public static final int id = 625;
	}
	public static final class S2CLegionList{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionList.class);
		public List<IOLegionInfo> legionlist;
		@Override
		public String toString() {
			return "S2CLegionList [legionlist="+legionlist+",]";
		}
		public static final int msgCode = 626;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 626);
			if(legionlist == null || legionlist.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(legionlist.size());
				for(IOLegionInfo legionlist1 : legionlist){
					retMsg.writeLong(legionlist1.id);
					retMsg.writeInt(legionlist1.lv);
					retMsg.writeString(legionlist1.name);
					retMsg.writeInt(legionlist1.npnum);
					retMsg.writeInt(legionlist1.mpnum);
					retMsg.writeInt(legionlist1.minlv);
					retMsg.writeBool(legionlist1.ispass);
					retMsg.writeString(legionlist1.ceo);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionSearch{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionSearch.class);
		public String name;
		@Override
		public String toString() {
			return "C2SLegionSearch [name="+name+",]";
		}
		public static final int id = 627;

		public static C2SLegionSearch parse(MyRequestMessage request){
			C2SLegionSearch retObj = new C2SLegionSearch();
			try{
			retObj.name=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionSearch{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionSearch.class);
		public IOLegionInfo legion1;
		@Override
		public String toString() {
			return "S2CLegionSearch [legion1="+legion1+",]";
		}
		public static final int msgCode = 628;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 628);
			if(legion1 == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(legion1.id);
					retMsg.writeInt(legion1.lv);
					retMsg.writeString(legion1.name);
					retMsg.writeInt(legion1.npnum);
					retMsg.writeInt(legion1.mpnum);
					retMsg.writeInt(legion1.minlv);
					retMsg.writeBool(legion1.ispass);
					retMsg.writeString(legion1.ceo);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionInfo.class);
		public static final int id = 629;
	}
	public static final class S2CLegionInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionInfo.class);
		public String notice;
		public int power;
		public int minlv;
		public long lastmail;
		public boolean ispass;
		public int secretplace;
		public int wbosshistroyrank;
		public int servid;
		public long lastactive;
		public String name;
		public Map<Integer,IOLegionDonation> donation;
		public int exp;
		public int fexp;
		public int flevel;
		public boolean kceo;
		public int level;
		public int maxexp;
		public int mpnum;
		public long id;
		public List<IOLegionMember> members;
		public String ceo;
		public long nextmail;
		public int pos;
		@Override
		public String toString() {
			return "S2CLegionInfo [notice="+notice+",power="+power+",minlv="+minlv+",lastmail="+lastmail+",ispass="+ispass+",secretplace="+secretplace+",wbosshistroyrank="+wbosshistroyrank+",servid="+servid+",lastactive="+lastactive+",name="+name+",donation="+donation+",exp="+exp+",fexp="+fexp+",flevel="+flevel+",kceo="+kceo+",level="+level+",maxexp="+maxexp+",mpnum="+mpnum+",id="+id+",members="+members+",ceo="+ceo+",nextmail="+nextmail+",pos="+pos+",]";
		}
		public static final int msgCode = 630;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 630);
			retMsg.writeString(notice);
			retMsg.writeInt(power);
			retMsg.writeInt(minlv);
			retMsg.writeLong(lastmail);
			retMsg.writeBool(ispass);
			retMsg.writeInt(secretplace);
			retMsg.writeInt(wbosshistroyrank);
			retMsg.writeInt(servid);
			retMsg.writeLong(lastactive);
			retMsg.writeString(name);
			if(donation == null || donation.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(donation.size());
				for(Map.Entry<Integer,IOLegionDonation> donation1 : donation.entrySet()){
			retMsg.writeInt(donation1.getKey());
					retMsg.writeInt(donation1.getValue().count);
					retMsg.writeLong(donation1.getValue().last);
				}
			}
			retMsg.writeInt(exp);
			retMsg.writeInt(fexp);
			retMsg.writeInt(flevel);
			retMsg.writeBool(kceo);
			retMsg.writeInt(level);
			retMsg.writeInt(maxexp);
			retMsg.writeInt(mpnum);
			retMsg.writeLong(id);
			if(members == null || members.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(members.size());
				for(IOLegionMember members1 : members){
					retMsg.writeInt(members1.id);
					retMsg.writeString(members1.name);
					retMsg.writeInt(members1.icon);
					retMsg.writeInt(members1.headid);
					retMsg.writeInt(members1.frameid);
					retMsg.writeInt(members1.lv);
					retMsg.writeLong(members1.lastest);
					retMsg.writeInt(members1.pos);
					retMsg.writeInt(members1.score);
					retMsg.writeInt(members1.power);
					retMsg.writeLong(members1.time);
				}
			}
			retMsg.writeString(ceo);
			retMsg.writeLong(nextmail);
			retMsg.writeInt(pos);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionApply{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionApply.class);
		public long legion_id;
		@Override
		public String toString() {
			return "C2SLegionApply [legion_id="+legion_id+",]";
		}
		public static final int id = 631;

		public static C2SLegionApply parse(MyRequestMessage request){
			C2SLegionApply retObj = new C2SLegionApply();
			try{
			retObj.legion_id=request.readLong();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionApply{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionApply.class);
		public long id;
		public S2CLegionApply(long pid){
			id=pid;
		}
		public S2CLegionApply(){}
		@Override
		public String toString() {
			return "S2CLegionApply [id="+id+",]";
		}
		public static final int msgCode = 632;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 632);
			retMsg.writeLong(id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionApplyList{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionApplyList.class);
		public static final int id = 633;
	}
	public static final class S2CLegionApplyList{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionApplyList.class);
		public List<IOLegionMember> applylist;
		@Override
		public String toString() {
			return "S2CLegionApplyList [applylist="+applylist+",]";
		}
		public static final int msgCode = 634;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 634);
			if(applylist == null || applylist.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(applylist.size());
				for(IOLegionMember applylist1 : applylist){
					retMsg.writeInt(applylist1.id);
					retMsg.writeString(applylist1.name);
					retMsg.writeInt(applylist1.icon);
					retMsg.writeInt(applylist1.headid);
					retMsg.writeInt(applylist1.frameid);
					retMsg.writeInt(applylist1.lv);
					retMsg.writeLong(applylist1.lastest);
					retMsg.writeInt(applylist1.pos);
					retMsg.writeInt(applylist1.score);
					retMsg.writeInt(applylist1.power);
					retMsg.writeLong(applylist1.time);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionApplyReview{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionApplyReview.class);
		public int[] player_ids;
		public boolean is_accept;
		@Override
		public String toString() {
			return "C2SLegionApplyReview [player_ids="+java.util.Arrays.toString(player_ids)+",is_accept="+is_accept+",]";
		}
		public static final int id = 635;

		public static C2SLegionApplyReview parse(MyRequestMessage request){
			C2SLegionApplyReview retObj = new C2SLegionApplyReview();
			try{
			int player_ids_size = request.readInt();
				retObj.player_ids = new int[player_ids_size];
				for(int i=0;i<player_ids_size;i++){
					retObj.player_ids[i]=request.readInt();
				}
			retObj.is_accept=request.readBool();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionApplyReview{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionApplyReview.class);
		public List<IOLegionApplyReview> ret;
		@Override
		public String toString() {
			return "S2CLegionApplyReview [ret="+ret+",]";
		}
		public static final int msgCode = 636;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 636);
			if(ret == null || ret.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(ret.size());
				for(IOLegionApplyReview ret1 : ret){
					retMsg.writeString(ret1.error);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionQuit{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionQuit.class);
		public static final int id = 637;
	}
	public static final class S2CLegionQuit{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionQuit.class);
		public static final int msgCode = 638;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 638);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionAppoint{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionAppoint.class);
		public int rid;
		public int type;
		@Override
		public String toString() {
			return "C2SLegionAppoint [rid="+rid+",type="+type+",]";
		}
		public static final int id = 639;

		public static C2SLegionAppoint parse(MyRequestMessage request){
			C2SLegionAppoint retObj = new C2SLegionAppoint();
			try{
			retObj.rid=request.readInt();
			retObj.type=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionAppoint{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionAppoint.class);
		public static final int msgCode = 640;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 640);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionDismiss{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionDismiss.class);
		public int rid;
		@Override
		public String toString() {
			return "C2SLegionDismiss [rid="+rid+",]";
		}
		public static final int id = 641;

		public static C2SLegionDismiss parse(MyRequestMessage request){
			C2SLegionDismiss retObj = new C2SLegionDismiss();
			try{
			retObj.rid=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionDismiss{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionDismiss.class);
		public static final int msgCode = 642;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 642);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionDestroy{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionDestroy.class);
		public static final int id = 643;
	}
	public static final class S2CLegionDestroy{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionDestroy.class);
		public static final int msgCode = 644;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 644);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionSign{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionSign.class);
		public static final int id = 645;
	}
	public static final class S2CLegionSign{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionSign.class);
		public List<IORewardItem> reward;
		public int exp;
		@Override
		public String toString() {
			return "S2CLegionSign [reward="+reward+",exp="+exp+",]";
		}
		public static final int msgCode = 646;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 646);
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(IORewardItem reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
				}
			}
			retMsg.writeInt(exp);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionTeckLv{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionTeckLv.class);
		public int tech_id;
		@Override
		public String toString() {
			return "C2SLegionTeckLv [tech_id="+tech_id+",]";
		}
		public static final int id = 647;

		public static C2SLegionTeckLv parse(MyRequestMessage request){
			C2SLegionTeckLv retObj = new C2SLegionTeckLv();
			try{
			retObj.tech_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionTeckLv{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionTeckLv.class);
		public static final int msgCode = 648;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 648);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionTechReset{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionTechReset.class);
		public int occu;
		@Override
		public String toString() {
			return "C2SLegionTechReset [occu="+occu+",]";
		}
		public static final int id = 649;

		public static C2SLegionTechReset parse(MyRequestMessage request){
			C2SLegionTechReset retObj = new C2SLegionTechReset();
			try{
			retObj.occu=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionTechReset{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionTechReset.class);
		public List<IORewardItem> items;
		@Override
		public String toString() {
			return "S2CLegionTechReset [items="+items+",]";
		}
		public static final int msgCode = 650;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 650);
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
	public static final class C2SLegionFactoryLv{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionFactoryLv.class);
		public int type;
		@Override
		public String toString() {
			return "C2SLegionFactoryLv [type="+type+",]";
		}
		public static final int id = 651;

		public static C2SLegionFactoryLv parse(MyRequestMessage request){
			C2SLegionFactoryLv retObj = new C2SLegionFactoryLv();
			try{
			retObj.type=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionFactoryLv{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionFactoryLv.class);
		public int lv;
		public int exp;
		public int newman;
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2CLegionFactoryLv [lv="+lv+",exp="+exp+",newman="+newman+",rewards="+rewards+",]";
		}
		public static final int msgCode = 652;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 652);
			retMsg.writeInt(lv);
			retMsg.writeInt(exp);
			retMsg.writeInt(newman);
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
	public static final class C2SLegionFactoryDonateGet{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionFactoryDonateGet.class);
		public static final int id = 653;
	}
	public static final class S2CLegionFactoryDonateGet{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionFactoryDonateGet.class);
		public List<IOLegionFactoryDonation> items;
		@Override
		public String toString() {
			return "S2CLegionFactoryDonateGet [items="+items+",]";
		}
		public static final int msgCode = 654;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 654);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IOLegionFactoryDonation items1 : items){
					retMsg.writeString(items1.name);
					retMsg.writeInt(items1.icon);
					retMsg.writeInt(items1.headid);
					retMsg.writeInt(items1.frameid);
					retMsg.writeInt(items1.score);
					retMsg.writeInt(items1.pos);
					retMsg.writeLong(items1.last);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionFactoryMissionList{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionFactoryMissionList.class);
		public boolean isupdate;
		@Override
		public String toString() {
			return "C2SLegionFactoryMissionList [isupdate="+isupdate+",]";
		}
		public static final int id = 655;

		public static C2SLegionFactoryMissionList parse(MyRequestMessage request){
			C2SLegionFactoryMissionList retObj = new C2SLegionFactoryMissionList();
			try{
			retObj.isupdate=request.readBool();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionFactoryMissionList{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionFactoryMissionList.class);
		public long time;
		public List<IOLegionFactoryMission> list;
		@Override
		public String toString() {
			return "S2CLegionFactoryMissionList [time="+time+",list="+list+",]";
		}
		public static final int msgCode = 656;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 656);
			retMsg.writeLong(time);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOLegionFactoryMission list1 : list){
					retMsg.writeLong(list1.key);
					retMsg.writeInt(list1.id);
					retMsg.writeLong(list1.stime);
					retMsg.writeLong(list1.etime);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionFactoryMissionUp{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionFactoryMissionUp.class);
		public long key;
		@Override
		public String toString() {
			return "C2SLegionFactoryMissionUp [key="+key+",]";
		}
		public static final int id = 657;

		public static C2SLegionFactoryMissionUp parse(MyRequestMessage request){
			C2SLegionFactoryMissionUp retObj = new C2SLegionFactoryMissionUp();
			try{
			retObj.key=request.readLong();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionFactoryMissionUp{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionFactoryMissionUp.class);
		public int id;
		public S2CLegionFactoryMissionUp(int pid){
			id=pid;
		}
		public S2CLegionFactoryMissionUp(){}
		@Override
		public String toString() {
			return "S2CLegionFactoryMissionUp [id="+id+",]";
		}
		public static final int msgCode = 658;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 658);
			retMsg.writeInt(id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionFactoryMissionStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionFactoryMissionStart.class);
		public long key;
		@Override
		public String toString() {
			return "C2SLegionFactoryMissionStart [key="+key+",]";
		}
		public static final int id = 659;

		public static C2SLegionFactoryMissionStart parse(MyRequestMessage request){
			C2SLegionFactoryMissionStart retObj = new C2SLegionFactoryMissionStart();
			try{
			retObj.key=request.readLong();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionFactoryMissionStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionFactoryMissionStart.class);
		public long stime;
		public long etime;
		public S2CLegionFactoryMissionStart(long pstime,long petime){
			stime=pstime;
			etime=petime;
		}
		public S2CLegionFactoryMissionStart(){}
		@Override
		public String toString() {
			return "S2CLegionFactoryMissionStart [stime="+stime+",etime="+etime+",]";
		}
		public static final int msgCode = 660;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 660);
			retMsg.writeLong(stime);
			retMsg.writeLong(etime);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionFactoryMissionFinish{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionFactoryMissionFinish.class);
		public long key;
		@Override
		public String toString() {
			return "C2SLegionFactoryMissionFinish [key="+key+",]";
		}
		public static final int id = 661;

		public static C2SLegionFactoryMissionFinish parse(MyRequestMessage request){
			C2SLegionFactoryMissionFinish retObj = new C2SLegionFactoryMissionFinish();
			try{
			retObj.key=request.readLong();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionFactoryMissionFinish{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionFactoryMissionFinish.class);
		public List<IORewardItem> items;
		@Override
		public String toString() {
			return "S2CLegionFactoryMissionFinish [items="+items+",]";
		}
		public static final int msgCode = 662;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 662);
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
	public static final class C2SLegionLog{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionLog.class);
		public static final int id = 663;
	}
	public static final class S2CLegionLog{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionLog.class);
		public List<IOLegionLog> list;
		@Override
		public String toString() {
			return "S2CLegionLog [list="+list+",]";
		}
		public static final int msgCode = 664;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 664);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOLegionLog list1 : list){
					if(list1.params == null || list1.params.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.params.size());
				for(String list1_params1 : list1.params){
			retMsg.writeString(list1_params1);
				}
			}
					retMsg.writeString(list1.event);
					retMsg.writeLong(list1.create);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionGetBossInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionGetBossInfo.class);
		public int chapter_index;
		@Override
		public String toString() {
			return "C2SLegionGetBossInfo [chapter_index="+chapter_index+",]";
		}
		public static final int id = 665;

		public static C2SLegionGetBossInfo parse(MyRequestMessage request){
			C2SLegionGetBossInfo retObj = new C2SLegionGetBossInfo();
			try{
			retObj.chapter_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionGetBossInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionGetBossInfo.class);
		public IOLegionBoss boss;
		public List<IOLegionRank> rank;
		public IOLegionBossSelf self;
		@Override
		public String toString() {
			return "S2CLegionGetBossInfo [boss="+boss+",rank="+rank+",self="+self+",]";
		}
		public static final int msgCode = 666;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 666);
			if(boss == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(boss.chapter);
					retMsg.writeString(boss.name);
					if(boss.rewards == null || boss.rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(boss.rewards.size());
				for(IORewardItem boss_rewards1 : boss.rewards){
					retMsg.writeInt(boss_rewards1.GSID);
					retMsg.writeInt(boss_rewards1.COUNT);
				}
			}
					if(boss.bset == null || boss.bset.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(boss.bset.size());
				for(Map.Entry<Integer,IOGeneralLegion> boss_bset1 : boss.bset.entrySet()){
			retMsg.writeInt(boss_bset1.getKey());
					retMsg.writeInt(boss_bset1.getValue().gsid);
					retMsg.writeInt(boss_bset1.getValue().level);
					retMsg.writeInt(boss_bset1.getValue().hpcover);
					retMsg.writeInt(boss_bset1.getValue().pclass);
					retMsg.writeLong(boss_bset1.getValue().exhp);
					retMsg.writeInt(boss_bset1.getValue().exatk);
				}
			}
					retMsg.writeLong(boss.maxhp);
					retMsg.writeLong(boss.nowhp);
			}
			if(rank == null || rank.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rank.size());
				for(IOLegionRank rank1 : rank){
					retMsg.writeString(rank1.name);
					retMsg.writeLong(rank1.damge);
					retMsg.writeInt(rank1.power);
				}
			}
			if(self == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(self.lastdamge);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionBossFarm{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionBossFarm.class);
		public int chapter_index;
		@Override
		public String toString() {
			return "C2SLegionBossFarm [chapter_index="+chapter_index+",]";
		}
		public static final int id = 667;

		public static C2SLegionBossFarm parse(MyRequestMessage request){
			C2SLegionBossFarm retObj = new C2SLegionBossFarm();
			try{
			retObj.chapter_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CLegionBossFarm{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionBossFarm.class);
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2CLegionBossFarm [rewards="+rewards+",]";
		}
		public static final int msgCode = 668;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 668);
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
	public static final class C2SWorldBossInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SWorldBossInfo.class);
		public static final int id = 669;
	}
	public static final class S2CWorldBossInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CWorldBossInfo.class);
		public IOWorldBossLegion legion;
		public IOWorldBossInfo boss;
		public IOWorldBossSelf self;
		public List<IOWorldBossRank> rank;
		public List<IOWorldBossWorldRank> worldrank;
		@Override
		public String toString() {
			return "S2CWorldBossInfo [legion="+legion+",boss="+boss+",self="+self+",rank="+rank+",worldrank="+worldrank+",]";
		}
		public static final int msgCode = 670;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 670);
			if(legion == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(legion.rank);
					retMsg.writeInt(legion.maxrank);
					retMsg.writeLong(legion.damage);
			}
			if(boss == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(boss.id);
					retMsg.writeLong(boss.endtime);
					retMsg.writeLong(boss.lastdamage);
					if(boss.bset == null || boss.bset.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(boss.bset.size());
				for(Map.Entry<Integer,IOGeneralLegion> boss_bset1 : boss.bset.entrySet()){
			retMsg.writeInt(boss_bset1.getKey());
					retMsg.writeInt(boss_bset1.getValue().gsid);
					retMsg.writeInt(boss_bset1.getValue().level);
					retMsg.writeInt(boss_bset1.getValue().hpcover);
					retMsg.writeInt(boss_bset1.getValue().pclass);
					retMsg.writeLong(boss_bset1.getValue().exhp);
					retMsg.writeInt(boss_bset1.getValue().exatk);
				}
			}
					retMsg.writeInt(boss.hasgift);
			}
			if(self == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(self.rank);
					retMsg.writeLong(self.damage);
			}
			if(rank == null || rank.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rank.size());
				for(IOWorldBossRank rank1 : rank){
					retMsg.writeInt(rank1.headid);
					retMsg.writeInt(rank1.frameid);
					retMsg.writeInt(rank1.icon);
					retMsg.writeInt(rank1.level);
					retMsg.writeInt(rank1.power);
					retMsg.writeLong(rank1.damge);
					retMsg.writeString(rank1.name);
					retMsg.writeInt(rank1.rid);
				}
			}
			if(worldrank == null || worldrank.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(worldrank.size());
				for(IOWorldBossWorldRank worldrank1 : worldrank){
					retMsg.writeInt(worldrank1.sid);
					retMsg.writeLong(worldrank1.legion);
					retMsg.writeString(worldrank1.lname);
					retMsg.writeLong(worldrank1.total);
					retMsg.writeInt(worldrank1.level);
					retMsg.writeInt(worldrank1.flag);
					retMsg.writeInt(worldrank1.index);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SWorldBossFarm{
		private static final Logger logger = LoggerFactory.getLogger(C2SWorldBossFarm.class);
		public static final int id = 671;
	}
	public static final class S2CWorldBossFarm{
		private static final Logger logger = LoggerFactory.getLogger(S2CWorldBossFarm.class);
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2CWorldBossFarm [rewards="+rewards+",]";
		}
		public static final int msgCode = 672;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 672);
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
}
