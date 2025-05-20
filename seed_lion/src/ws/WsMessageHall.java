package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.Map;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.WanbaLoginGift;
import ws.WsMessageBase.IOSpecial;
import ws.WsMessageBase.IOOcctask;
import ws.WsMessageBase.IOOccTask1;
import ws.WsMessageBase.IOOcctaskPackinfo;
import ws.WsMessageBase.IODungeonTop;
import ws.WsMessageBase.IOBattleFormation;
import ws.WsMessageBase.IOFormationGeneralPos;
import ws.WsMessageBase.IORecruitFree;
import ws.WsMessageBase.SimpleItemInfo;
import ws.WsMessageBase.GuideStepInfo;
import ws.WsMessageBase.RewardInfo;
import ws.WsMessageBase.IOTeamInfo;
import ws.WsMessageBase.IORankPlayer;
import ws.WsMessageBase.IOSeason;
import ws.WsMessageBase.IOOtherPlayer;
import ws.WsMessageBase.IOBattleSet;
import ws.WsMessageBase.IOBattleLine;
import ws.WsMessageBase.IOGeneralBean;
import ws.WsMessageBase.IOProperty;
import ws.WsMessageBase.IOExclusive;
import ws.WsMessageBase.KvStringPair;

public final class WsMessageHall {
    
	public static final class C2SGmCmd{
		private static final Logger logger = LoggerFactory.getLogger(C2SGmCmd.class);
		public String cmd;
		@Override
		public String toString() {
			return "C2SGmCmd [cmd="+cmd+",]";
		}
		public static final int id = 101;

		public static C2SGmCmd parse(MyRequestMessage request){
			C2SGmCmd retObj = new C2SGmCmd();
			try{
			retObj.cmd=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGmCmd{
		private static final Logger logger = LoggerFactory.getLogger(S2CGmCmd.class);
		public int ret_code;
		public S2CGmCmd(int pret_code){
			ret_code=pret_code;
		}
		public S2CGmCmd(){}
		@Override
		public String toString() {
			return "S2CGmCmd [ret_code="+ret_code+",]";
		}
		public static final int msgCode = 102;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 102);
			retMsg.writeInt(ret_code);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class S2CErrorCode{
		private static final Logger logger = LoggerFactory.getLogger(S2CErrorCode.class);
		public int msg_num;
		public int ret_code;
		public S2CErrorCode(int pmsg_num,int pret_code){
			msg_num=pmsg_num;
			ret_code=pret_code;
		}
		public S2CErrorCode(){}
		@Override
		public String toString() {
			return "S2CErrorCode [msg_num="+msg_num+",ret_code="+ret_code+",]";
		}
		public static final int msgCode = 100;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 100);
			retMsg.writeInt(msg_num);
			retMsg.writeInt(ret_code);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SQueryHasRole{
		private static final Logger logger = LoggerFactory.getLogger(C2SQueryHasRole.class);
		public long session_id;
		public WanbaLoginGift qq_open_data;
		@Override
		public String toString() {
			return "C2SQueryHasRole [session_id="+session_id+",qq_open_data="+qq_open_data+",]";
		}
		public static final int id = 103;

		public static C2SQueryHasRole parse(MyRequestMessage request){
			C2SQueryHasRole retObj = new C2SQueryHasRole();
			try{
			retObj.session_id=request.readLong();
			boolean qq_open_data_exist = request.readBool();
			if(qq_open_data_exist){
				retObj.qq_open_data = new WanbaLoginGift();
					retObj.qq_open_data.wanba_gift_id=request.readInt();
					retObj.qq_open_data.appid=request.readString();
					retObj.qq_open_data.openid=request.readString();
					retObj.qq_open_data.openkey=request.readString();
					retObj.qq_open_data.pf=request.readString();
			}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CQueryHasRole{
		private static final Logger logger = LoggerFactory.getLogger(S2CQueryHasRole.class);
		public boolean has_role;
		public String qq_nickname;
		public S2CQueryHasRole(boolean phas_role,String pqq_nickname){
			has_role=phas_role;
			qq_nickname=pqq_nickname;
		}
		public S2CQueryHasRole(){}
		@Override
		public String toString() {
			return "S2CQueryHasRole [has_role="+has_role+",qq_nickname="+qq_nickname+",]";
		}
		public static final int msgCode = 104;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 104);
			retMsg.writeBool(has_role);
			retMsg.writeString(qq_nickname);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SCreateCharacter{
		private static final Logger logger = LoggerFactory.getLogger(C2SCreateCharacter.class);
		public long session_id;
		public String name;
		@Override
		public String toString() {
			return "C2SCreateCharacter [session_id="+session_id+",name="+name+",]";
		}
		public static final int id = 105;

		public static C2SCreateCharacter parse(MyRequestMessage request){
			C2SCreateCharacter retObj = new C2SCreateCharacter();
			try{
			retObj.session_id=request.readLong();
			retObj.name=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CCreateCharacter{
		private static final Logger logger = LoggerFactory.getLogger(S2CCreateCharacter.class);
		public int ret;
		public int character_id;
		public S2CCreateCharacter(int pret,int pcharacter_id){
			ret=pret;
			character_id=pcharacter_id;
		}
		public S2CCreateCharacter(){}
		@Override
		public String toString() {
			return "S2CCreateCharacter [ret="+ret+",character_id="+character_id+",]";
		}
		public static final int msgCode = 106;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 106);
			retMsg.writeInt(ret);
			retMsg.writeInt(character_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SEnterGame{
		private static final Logger logger = LoggerFactory.getLogger(C2SEnterGame.class);
		public long session_id;
		public WanbaLoginGift wanba_gift;
		@Override
		public String toString() {
			return "C2SEnterGame [session_id="+session_id+",wanba_gift="+wanba_gift+",]";
		}
		public static final int id = 107;

		public static C2SEnterGame parse(MyRequestMessage request){
			C2SEnterGame retObj = new C2SEnterGame();
			try{
			retObj.session_id=request.readLong();
			boolean wanba_gift_exist = request.readBool();
			if(wanba_gift_exist){
				retObj.wanba_gift = new WanbaLoginGift();
					retObj.wanba_gift.wanba_gift_id=request.readInt();
					retObj.wanba_gift.appid=request.readString();
					retObj.wanba_gift.openid=request.readString();
					retObj.wanba_gift.openkey=request.readString();
					retObj.wanba_gift.pf=request.readString();
			}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CEnterGame{
		private static final Logger logger = LoggerFactory.getLogger(S2CEnterGame.class);
		public int current_time_seconds;
		public long game_session_id;
		public S2CEnterGame(int pcurrent_time_seconds,long pgame_session_id){
			current_time_seconds=pcurrent_time_seconds;
			game_session_id=pgame_session_id;
		}
		public S2CEnterGame(){}
		@Override
		public String toString() {
			return "S2CEnterGame [current_time_seconds="+current_time_seconds+",game_session_id="+game_session_id+",]";
		}
		public static final int msgCode = 108;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 108);
			retMsg.writeInt(current_time_seconds);
			retMsg.writeLong(game_session_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class S2CUserInfoStruct{
		private static final Logger logger = LoggerFactory.getLogger(S2CUserInfoStruct.class);
		public int id;
		public String uid;
		public int servid;
		public String rname;
		public int sex;
		public int iconid;
		public int headid;
		public int frameid;
		public int imageid;
		public int gold;
		public int yb;
		public int level;
		public int exp;
		public int vip;
		public int vipexp;
		public int power;
		public long firstcharge;
		public long create;
		public long time;
		public int guideProgress;
		public int maxmapid;
		public int nowmapid;
		public int tower;
		public int bagspace;
		public long online;
		public List<SimpleItemInfo> hides;
		public List<SimpleItemInfo> others;
		public IORecruitFree recruitfree;
		public long lastgain;
		public List<IOBattleFormation> battlearr;
		public int pvpscore;
		public long legion;
		public int gnum;
		public int fbossphys;
		public Map<Integer,Integer> tech;
		public IODungeonTop dgtop;
		public long occtaskend;
		public IOOcctask occtask;
		public IOSpecial special;
		public boolean guozhan_pvp;
		public long ydend;
		@Override
		public String toString() {
			return "S2CUserInfoStruct [id="+id+",uid="+uid+",servid="+servid+",rname="+rname+",sex="+sex+",iconid="+iconid+",headid="+headid+",frameid="+frameid+",imageid="+imageid+",gold="+gold+",yb="+yb+",level="+level+",exp="+exp+",vip="+vip+",vipexp="+vipexp+",power="+power+",firstcharge="+firstcharge+",create="+create+",time="+time+",guideProgress="+guideProgress+",maxmapid="+maxmapid+",nowmapid="+nowmapid+",tower="+tower+",bagspace="+bagspace+",online="+online+",hides="+hides+",others="+others+",recruitfree="+recruitfree+",lastgain="+lastgain+",battlearr="+battlearr+",pvpscore="+pvpscore+",legion="+legion+",gnum="+gnum+",fbossphys="+fbossphys+",tech="+tech+",dgtop="+dgtop+",occtaskend="+occtaskend+",occtask="+occtask+",special="+special+",guozhan_pvp="+guozhan_pvp+",ydend="+ydend+",]";
		}
		public static final int msgCode = 110;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 110);
			retMsg.writeInt(id);
			retMsg.writeString(uid);
			retMsg.writeInt(servid);
			retMsg.writeString(rname);
			retMsg.writeInt(sex);
			retMsg.writeInt(iconid);
			retMsg.writeInt(headid);
			retMsg.writeInt(frameid);
			retMsg.writeInt(imageid);
			retMsg.writeInt(gold);
			retMsg.writeInt(yb);
			retMsg.writeInt(level);
			retMsg.writeInt(exp);
			retMsg.writeInt(vip);
			retMsg.writeInt(vipexp);
			retMsg.writeInt(power);
			retMsg.writeLong(firstcharge);
			retMsg.writeLong(create);
			retMsg.writeLong(time);
			retMsg.writeInt(guideProgress);
			retMsg.writeInt(maxmapid);
			retMsg.writeInt(nowmapid);
			retMsg.writeInt(tower);
			retMsg.writeInt(bagspace);
			retMsg.writeLong(online);
			if(hides == null || hides.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(hides.size());
				for(SimpleItemInfo hides1 : hides){
					retMsg.writeInt(hides1.gsid);
					retMsg.writeInt(hides1.count);
				}
			}
			if(others == null || others.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(others.size());
				for(SimpleItemInfo others1 : others){
					retMsg.writeInt(others1.gsid);
					retMsg.writeInt(others1.count);
				}
			}
			if(recruitfree == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(recruitfree.normal);
					retMsg.writeLong(recruitfree.premium);
			}
			retMsg.writeLong(lastgain);
			if(battlearr == null || battlearr.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battlearr.size());
				for(IOBattleFormation battlearr1 : battlearr){
					retMsg.writeString(battlearr1.f_type);
					retMsg.writeInt(battlearr1.mythic);
					if(battlearr1.items == null || battlearr1.items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battlearr1.items.size());
				for(IOFormationGeneralPos battlearr1_items1 : battlearr1.items){
					retMsg.writeInt(battlearr1_items1.pos);
					retMsg.writeLong(battlearr1_items1.general_uuid);
				}
			}
				}
			}
			retMsg.writeInt(pvpscore);
			retMsg.writeLong(legion);
			retMsg.writeInt(gnum);
			retMsg.writeInt(fbossphys);
			if(tech == null || tech.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(tech.size());
				for(Map.Entry<Integer,Integer> tech1 : tech.entrySet()){
			retMsg.writeInt(tech1.getKey());
			retMsg.writeInt(tech1.getValue());
				}
			}
			if(dgtop == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(dgtop.chapter);
					retMsg.writeInt(dgtop.node);
			}
			retMsg.writeLong(occtaskend);
			if(occtask == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(occtask.index);
					retMsg.writeInt(occtask.occtype);
					if(occtask.rewards == null || occtask.rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(occtask.rewards.size());
				for(RewardInfo occtask_rewards1 : occtask.rewards){
					retMsg.writeInt(occtask_rewards1.GSID);
					retMsg.writeInt(occtask_rewards1.COUNT);
				}
			}
					if(occtask.list == null || occtask.list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(occtask.list.size());
				for(IOOccTask1 occtask_list1 : occtask.list){
					retMsg.writeInt(occtask_list1.status);
					if(occtask_list1.rewards == null || occtask_list1.rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(occtask_list1.rewards.size());
				for(RewardInfo occtask_list1_rewards1 : occtask_list1.rewards){
					retMsg.writeInt(occtask_list1_rewards1.GSID);
					retMsg.writeInt(occtask_list1_rewards1.COUNT);
				}
			}
					retMsg.writeString(occtask_list1.intro);
					retMsg.writeInt(occtask_list1.mark);
					retMsg.writeInt(occtask_list1.limit);
					retMsg.writeString(occtask_list1.page);
					retMsg.writeInt(occtask_list1.num);
				}
			}
					if(occtask.reward == null || occtask.reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(occtask.reward.size());
				for(RewardInfo occtask_reward1 : occtask.reward){
					retMsg.writeInt(occtask_reward1.GSID);
					retMsg.writeInt(occtask_reward1.COUNT);
				}
			}
					if(occtask.refcost == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(occtask.refcost.GSID);
					retMsg.writeInt(occtask.refcost.COUNT);
			}
					if(occtask.packinfo == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(occtask.packinfo.ID);
					if(occtask.packinfo.ITEMS == null || occtask.packinfo.ITEMS.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(occtask.packinfo.ITEMS.size());
				for(RewardInfo occtask_packinfo_ITEMS1 : occtask.packinfo.ITEMS){
					retMsg.writeInt(occtask_packinfo_ITEMS1.GSID);
					retMsg.writeInt(occtask_packinfo_ITEMS1.COUNT);
				}
			}
					retMsg.writeInt(occtask.packinfo.TYPE);
					retMsg.writeInt(occtask.packinfo.VALUE);
					retMsg.writeInt(occtask.packinfo.WEIGHT);
			}
					if(occtask.prewards == null || occtask.prewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(occtask.prewards.size());
				for(Integer occtask_prewards1 : occtask.prewards){
			retMsg.writeInt(occtask_prewards1);
				}
			}
			}
			if(special == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(special.bw);
					retMsg.writeLong(special.qz);
					retMsg.writeLong(special.zx);
					retMsg.writeLong(special.lm);
					retMsg.writeLong(special.yd);
					retMsg.writeLong(special.cz);
					retMsg.writeLong(special.gz);
					retMsg.writeLong(special.zz);
			}
			retMsg.writeBool(guozhan_pvp);
			retMsg.writeLong(ydend);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class S2CPlayerKickOff{
		private static final Logger logger = LoggerFactory.getLogger(S2CPlayerKickOff.class);
		public static final int msgCode = 112;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 112);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class PushPropChange{
		private static final Logger logger = LoggerFactory.getLogger(PushPropChange.class);
		public int gsid;
		public long count;
		public PushPropChange(int pgsid,long pcount){
			gsid=pgsid;
			count=pcount;
		}
		public PushPropChange(){}
		@Override
		public String toString() {
			return "PushPropChange [gsid="+gsid+",count="+count+",]";
		}
		public static final int msgCode = 114;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 114);
			retMsg.writeInt(gsid);
			retMsg.writeLong(count);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class PushGuideInfo{
		private static final Logger logger = LoggerFactory.getLogger(PushGuideInfo.class);
		public List<GuideStepInfo> guideInfo;
		@Override
		public String toString() {
			return "PushGuideInfo [guideInfo="+guideInfo+",]";
		}
		public static final int msgCode = 116;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 116);
			if(guideInfo == null || guideInfo.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(guideInfo.size());
				for(GuideStepInfo guideInfo1 : guideInfo){
					retMsg.writeInt(guideInfo1.module);
					retMsg.writeInt(guideInfo1.step);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuideStepSet{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuideStepSet.class);
		public int module;
		public int step;
		@Override
		public String toString() {
			return "C2SGuideStepSet [module="+module+",step="+step+",]";
		}
		public static final int id = 117;

		public static C2SGuideStepSet parse(MyRequestMessage request){
			C2SGuideStepSet retObj = new C2SGuideStepSet();
			try{
			retObj.module=request.readInt();
			retObj.step=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuideStepSet{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuideStepSet.class);
		public static final int msgCode = 118;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 118);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class S2CChargeInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CChargeInfo.class);
		public boolean is_first_pay;
		public boolean first_award_get;
		public List<Integer> payment_level;
		public List<Integer> vip_gift_get;
		public boolean is_long_yueka;
		public boolean long_yueka_get;
		public int yueka_left_day;
		public boolean yueka_get;
		@Override
		public String toString() {
			return "S2CChargeInfo [is_first_pay="+is_first_pay+",first_award_get="+first_award_get+",payment_level="+payment_level+",vip_gift_get="+vip_gift_get+",is_long_yueka="+is_long_yueka+",long_yueka_get="+long_yueka_get+",yueka_left_day="+yueka_left_day+",yueka_get="+yueka_get+",]";
		}
		public static final int msgCode = 120;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 120);
			retMsg.writeBool(is_first_pay);
			retMsg.writeBool(first_award_get);
			if(payment_level == null || payment_level.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(payment_level.size());
				for(Integer payment_level1 : payment_level){
			retMsg.writeInt(payment_level1);
				}
			}
			if(vip_gift_get == null || vip_gift_get.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(vip_gift_get.size());
				for(Integer vip_gift_get1 : vip_gift_get){
			retMsg.writeInt(vip_gift_get1);
				}
			}
			retMsg.writeBool(is_long_yueka);
			retMsg.writeBool(long_yueka_get);
			retMsg.writeInt(yueka_left_day);
			retMsg.writeBool(yueka_get);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetFirstPayAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetFirstPayAward.class);
		public int extype;
		public int markid;
		@Override
		public String toString() {
			return "C2SGetFirstPayAward [extype="+extype+",markid="+markid+",]";
		}
		public static final int id = 121;

		public static C2SGetFirstPayAward parse(MyRequestMessage request){
			C2SGetFirstPayAward retObj = new C2SGetFirstPayAward();
			try{
			retObj.extype=request.readInt();
			retObj.markid=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGetFirstPayAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetFirstPayAward.class);
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2CGetFirstPayAward [rewards="+rewards+",]";
		}
		public static final int msgCode = 122;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 122);
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
	public static final class C2SGetVipGift{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetVipGift.class);
		public int vip_level;
		@Override
		public String toString() {
			return "C2SGetVipGift [vip_level="+vip_level+",]";
		}
		public static final int id = 123;

		public static C2SGetVipGift parse(MyRequestMessage request){
			C2SGetVipGift retObj = new C2SGetVipGift();
			try{
			retObj.vip_level=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGetVipGift{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetVipGift.class);
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2CGetVipGift [rewards="+rewards+",]";
		}
		public static final int msgCode = 124;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 124);
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
	public static final class C2SDownlineReconnect{
		private static final Logger logger = LoggerFactory.getLogger(C2SDownlineReconnect.class);
		public long in_game_session_id;
		@Override
		public String toString() {
			return "C2SDownlineReconnect [in_game_session_id="+in_game_session_id+",]";
		}
		public static final int id = 125;

		public static C2SDownlineReconnect parse(MyRequestMessage request){
			C2SDownlineReconnect retObj = new C2SDownlineReconnect();
			try{
			retObj.in_game_session_id=request.readLong();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CDownlineReconnect{
		private static final Logger logger = LoggerFactory.getLogger(S2CDownlineReconnect.class);
		public int ret;
		public S2CDownlineReconnect(int pret){
			ret=pret;
		}
		public S2CDownlineReconnect(){}
		@Override
		public String toString() {
			return "S2CDownlineReconnect [ret="+ret+",]";
		}
		public static final int msgCode = 126;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 126);
			retMsg.writeInt(ret);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SListRedPoints{
		private static final Logger logger = LoggerFactory.getLogger(C2SListRedPoints.class);
		public static final int id = 127;
	}
	public static final class S2CListRedPoints{
		private static final Logger logger = LoggerFactory.getLogger(S2CListRedPoints.class);
		public List<Integer> has_redpoint;
		public List<Integer> activity_redpoints;
		@Override
		public String toString() {
			return "S2CListRedPoints [has_redpoint="+has_redpoint+",activity_redpoints="+activity_redpoints+",]";
		}
		public static final int msgCode = 128;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 128);
			if(has_redpoint == null || has_redpoint.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(has_redpoint.size());
				for(Integer has_redpoint1 : has_redpoint){
			retMsg.writeInt(has_redpoint1);
				}
			}
			if(activity_redpoints == null || activity_redpoints.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(activity_redpoints.size());
				for(Integer activity_redpoints1 : activity_redpoints){
			retMsg.writeInt(activity_redpoints1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SQunheiPayPre{
		private static final Logger logger = LoggerFactory.getLogger(C2SQunheiPayPre.class);
		public int charge_index;
		public String goodsName;
		@Override
		public String toString() {
			return "C2SQunheiPayPre [charge_index="+charge_index+",goodsName="+goodsName+",]";
		}
		public static final int id = 129;

		public static C2SQunheiPayPre parse(MyRequestMessage request){
			C2SQunheiPayPre retObj = new C2SQunheiPayPre();
			try{
			retObj.charge_index=request.readInt();
			retObj.goodsName=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CQunheiPayPre{
		private static final Logger logger = LoggerFactory.getLogger(S2CQunheiPayPre.class);
		public int charge_index;
		public String ext;
		public String sign;
		public String goodsName;
		public int rmb;
		public S2CQunheiPayPre(int pcharge_index,String pext,String psign,String pgoodsName,int prmb){
			charge_index=pcharge_index;
			ext=pext;
			sign=psign;
			goodsName=pgoodsName;
			rmb=prmb;
		}
		public S2CQunheiPayPre(){}
		@Override
		public String toString() {
			return "S2CQunheiPayPre [charge_index="+charge_index+",ext="+ext+",sign="+sign+",goodsName="+goodsName+",rmb="+rmb+",]";
		}
		public static final int msgCode = 130;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 130);
			retMsg.writeInt(charge_index);
			retMsg.writeString(ext);
			retMsg.writeString(sign);
			retMsg.writeString(goodsName);
			retMsg.writeInt(rmb);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class PushPaymentResult{
		private static final Logger logger = LoggerFactory.getLogger(PushPaymentResult.class);
		public int yb;
		public String pid;
		public int vipExp;
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "PushPaymentResult [yb="+yb+",pid="+pid+",vipExp="+vipExp+",rewards="+rewards+",]";
		}
		public static final int msgCode = 132;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 132);
			retMsg.writeInt(yb);
			retMsg.writeString(pid);
			retMsg.writeInt(vipExp);
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
	public static final class PushFirstPay{
		private static final Logger logger = LoggerFactory.getLogger(PushFirstPay.class);
		public static final int msgCode = 134;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 134);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetYuekaAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetYuekaAward.class);
		public int type;
		@Override
		public String toString() {
			return "C2SGetYuekaAward [type="+type+",]";
		}
		public static final int id = 135;

		public static C2SGetYuekaAward parse(MyRequestMessage request){
			C2SGetYuekaAward retObj = new C2SGetYuekaAward();
			try{
			retObj.type=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGetYuekaAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetYuekaAward.class);
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2CGetYuekaAward [rewards="+rewards+",]";
		}
		public static final int msgCode = 136;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 136);
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
	public static final class C2SGetQunheiWxShareAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetQunheiWxShareAward.class);
		public static final int id = 137;
	}
	public static final class S2CGetQunheiWxShareAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetQunheiWxShareAward.class);
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2CGetQunheiWxShareAward [rewards="+rewards+",]";
		}
		public static final int msgCode = 138;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 138);
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
	public static final class PushMarquee{
		private static final Logger logger = LoggerFactory.getLogger(PushMarquee.class);
		public int template_id;
		public int order;
		public List<String> parameters;
		public String content;
		public int count;
		@Override
		public String toString() {
			return "PushMarquee [template_id="+template_id+",order="+order+",parameters="+parameters+",content="+content+",count="+count+",]";
		}
		public static final int msgCode = 140;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 140);
			retMsg.writeInt(template_id);
			retMsg.writeInt(order);
			if(parameters == null || parameters.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(parameters.size());
				for(String parameters1 : parameters){
			retMsg.writeString(parameters1);
				}
			}
			retMsg.writeString(content);
			retMsg.writeInt(count);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STeamsInfoGet{
		private static final Logger logger = LoggerFactory.getLogger(C2STeamsInfoGet.class);
		public static final int id = 141;
	}
	public static final class S2CTeamsInfoGet{
		private static final Logger logger = LoggerFactory.getLogger(S2CTeamsInfoGet.class);
		public List<IOTeamInfo> teams;
		@Override
		public String toString() {
			return "S2CTeamsInfoGet [teams="+teams+",]";
		}
		public static final int msgCode = 142;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 142);
			if(teams == null || teams.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(teams.size());
				for(IOTeamInfo teams1 : teams){
					retMsg.writeInt(teams1.type);
					if(teams1.pos_card_uuid == null || teams1.pos_card_uuid.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(teams1.pos_card_uuid.size());
				for(Long teams1_pos_card_uuid1 : teams1.pos_card_uuid){
			retMsg.writeLong(teams1_pos_card_uuid1);
				}
			}
					retMsg.writeInt(teams1.pet_id);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SRankView{
		private static final Logger logger = LoggerFactory.getLogger(C2SRankView.class);
		public String rtype;
		@Override
		public String toString() {
			return "C2SRankView [rtype="+rtype+",]";
		}
		public static final int id = 143;

		public static C2SRankView parse(MyRequestMessage request){
			C2SRankView retObj = new C2SRankView();
			try{
			retObj.rtype=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CRankView{
		private static final Logger logger = LoggerFactory.getLogger(S2CRankView.class);
		public String rtype;
		public int selfrank;
		public int my_rank_change;
		public List<IORankPlayer> list;
		@Override
		public String toString() {
			return "S2CRankView [rtype="+rtype+",selfrank="+selfrank+",my_rank_change="+my_rank_change+",list="+list+",]";
		}
		public static final int msgCode = 144;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 144);
			retMsg.writeString(rtype);
			retMsg.writeInt(selfrank);
			retMsg.writeInt(my_rank_change);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IORankPlayer list1 : list){
					retMsg.writeInt(list1.rid);
					retMsg.writeString(list1.rname);
					retMsg.writeInt(list1.iconid);
					retMsg.writeInt(list1.headid);
					retMsg.writeInt(list1.frameid);
					retMsg.writeInt(list1.level);
					retMsg.writeInt(list1.power);
					retMsg.writeInt(list1.vip);
					retMsg.writeInt(list1.rank_change);
					retMsg.writeInt(list1.hero_stars);
					retMsg.writeInt(list1.win_count);
					retMsg.writeInt(list1.score);
					retMsg.writeInt(list1.damage);
					retMsg.writeInt(list1.tower);
					retMsg.writeInt(list1.like_count);
					retMsg.writeInt(list1.chapter);
					retMsg.writeInt(list1.node);
					retMsg.writeInt(list1.stage);
					retMsg.writeInt(list1.star);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SZMPayCheck{
		private static final Logger logger = LoggerFactory.getLogger(C2SZMPayCheck.class);
		public int fee_id;
		public String goodsName;
		@Override
		public String toString() {
			return "C2SZMPayCheck [fee_id="+fee_id+",goodsName="+goodsName+",]";
		}
		public static final int id = 147;

		public static C2SZMPayCheck parse(MyRequestMessage request){
			C2SZMPayCheck retObj = new C2SZMPayCheck();
			try{
			retObj.fee_id=request.readInt();
			retObj.goodsName=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CZMPayCheck{
		private static final Logger logger = LoggerFactory.getLogger(S2CZMPayCheck.class);
		public String fee_id;
		public String check;
		public String extradata;
		public String goodsName;
		public int rmb;
		public S2CZMPayCheck(String pfee_id,String pcheck,String pextradata,String pgoodsName,int prmb){
			fee_id=pfee_id;
			check=pcheck;
			extradata=pextradata;
			goodsName=pgoodsName;
			rmb=prmb;
		}
		public S2CZMPayCheck(){}
		@Override
		public String toString() {
			return "S2CZMPayCheck [fee_id="+fee_id+",check="+check+",extradata="+extradata+",goodsName="+goodsName+",rmb="+rmb+",]";
		}
		public static final int msgCode = 148;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 148);
			retMsg.writeString(fee_id);
			retMsg.writeString(check);
			retMsg.writeString(extradata);
			retMsg.writeString(goodsName);
			retMsg.writeInt(rmb);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SOfflineAwardDouble{
		private static final Logger logger = LoggerFactory.getLogger(C2SOfflineAwardDouble.class);
		public static final int id = 149;
	}
	public static final class S2COfflineAwardDouble{
		private static final Logger logger = LoggerFactory.getLogger(S2COfflineAwardDouble.class);
		public int add_level;
		public S2COfflineAwardDouble(int padd_level){
			add_level=padd_level;
		}
		public S2COfflineAwardDouble(){}
		@Override
		public String toString() {
			return "S2COfflineAwardDouble [add_level="+add_level+",]";
		}
		public static final int msgCode = 150;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 150);
			retMsg.writeInt(add_level);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2S333PayPre{
		private static final Logger logger = LoggerFactory.getLogger(C2S333PayPre.class);
		public int gameId;
		public String goodsName;
		public int goodsId;
		@Override
		public String toString() {
			return "C2S333PayPre [gameId="+gameId+",goodsName="+goodsName+",goodsId="+goodsId+",]";
		}
		public static final int id = 151;

		public static C2S333PayPre parse(MyRequestMessage request){
			C2S333PayPre retObj = new C2S333PayPre();
			try{
			retObj.gameId=request.readInt();
			retObj.goodsName=request.readString();
			retObj.goodsId=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2C333PayPre{
		private static final Logger logger = LoggerFactory.getLogger(S2C333PayPre.class);
		public int time;
		public String server;
		public String role;
		public String goodsId;
		public String goodsName;
		public int money;
		public String cpOrderId;
		public String sign;
		public S2C333PayPre(int ptime,String pserver,String prole,String pgoodsId,String pgoodsName,int pmoney,String pcpOrderId,String psign){
			time=ptime;
			server=pserver;
			role=prole;
			goodsId=pgoodsId;
			goodsName=pgoodsName;
			money=pmoney;
			cpOrderId=pcpOrderId;
			sign=psign;
		}
		public S2C333PayPre(){}
		@Override
		public String toString() {
			return "S2C333PayPre [time="+time+",server="+server+",role="+role+",goodsId="+goodsId+",goodsName="+goodsName+",money="+money+",cpOrderId="+cpOrderId+",sign="+sign+",]";
		}
		public static final int msgCode = 152;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 152);
			retMsg.writeInt(time);
			retMsg.writeString(server);
			retMsg.writeString(role);
			retMsg.writeString(goodsId);
			retMsg.writeString(goodsName);
			retMsg.writeInt(money);
			retMsg.writeString(cpOrderId);
			retMsg.writeString(sign);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SChongChongPayPre{
		private static final Logger logger = LoggerFactory.getLogger(C2SChongChongPayPre.class);
		public int goodsId;
		public String goodsName;
		@Override
		public String toString() {
			return "C2SChongChongPayPre [goodsId="+goodsId+",goodsName="+goodsName+",]";
		}
		public static final int id = 153;

		public static C2SChongChongPayPre parse(MyRequestMessage request){
			C2SChongChongPayPre retObj = new C2SChongChongPayPre();
			try{
			retObj.goodsId=request.readInt();
			retObj.goodsName=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CChongChongPayPre{
		private static final Logger logger = LoggerFactory.getLogger(S2CChongChongPayPre.class);
		public int goodsId;
		public String egretOrderId;
		public int money;
		public String ext;
		public String goodsName;
		public S2CChongChongPayPre(int pgoodsId,String pegretOrderId,int pmoney,String pext,String pgoodsName){
			goodsId=pgoodsId;
			egretOrderId=pegretOrderId;
			money=pmoney;
			ext=pext;
			goodsName=pgoodsName;
		}
		public S2CChongChongPayPre(){}
		@Override
		public String toString() {
			return "S2CChongChongPayPre [goodsId="+goodsId+",egretOrderId="+egretOrderId+",money="+money+",ext="+ext+",goodsName="+goodsName+",]";
		}
		public static final int msgCode = 154;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 154);
			retMsg.writeInt(goodsId);
			retMsg.writeString(egretOrderId);
			retMsg.writeInt(money);
			retMsg.writeString(ext);
			retMsg.writeString(goodsName);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2S4399PayPre{
		private static final Logger logger = LoggerFactory.getLogger(C2S4399PayPre.class);
		public int goodsId;
		public String goodsName;
		@Override
		public String toString() {
			return "C2S4399PayPre [goodsId="+goodsId+",goodsName="+goodsName+",]";
		}
		public static final int id = 155;

		public static C2S4399PayPre parse(MyRequestMessage request){
			C2S4399PayPre retObj = new C2S4399PayPre();
			try{
			retObj.goodsId=request.readInt();
			retObj.goodsName=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2C4399PayPre{
		private static final Logger logger = LoggerFactory.getLogger(S2C4399PayPre.class);
		public int goodsId;
		public int rmb;
		public String cpOrderId;
		public String extra;
		public String goodsName;
		public S2C4399PayPre(int pgoodsId,int prmb,String pcpOrderId,String pextra,String pgoodsName){
			goodsId=pgoodsId;
			rmb=prmb;
			cpOrderId=pcpOrderId;
			extra=pextra;
			goodsName=pgoodsName;
		}
		public S2C4399PayPre(){}
		@Override
		public String toString() {
			return "S2C4399PayPre [goodsId="+goodsId+",rmb="+rmb+",cpOrderId="+cpOrderId+",extra="+extra+",goodsName="+goodsName+",]";
		}
		public static final int msgCode = 156;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 156);
			retMsg.writeInt(goodsId);
			retMsg.writeInt(rmb);
			retMsg.writeString(cpOrderId);
			retMsg.writeString(extra);
			retMsg.writeString(goodsName);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SRankLike{
		private static final Logger logger = LoggerFactory.getLogger(C2SRankLike.class);
		public int targetPlayerId;
		@Override
		public String toString() {
			return "C2SRankLike [targetPlayerId="+targetPlayerId+",]";
		}
		public static final int id = 157;

		public static C2SRankLike parse(MyRequestMessage request){
			C2SRankLike retObj = new C2SRankLike();
			try{
			retObj.targetPlayerId=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CRankLike{
		private static final Logger logger = LoggerFactory.getLogger(S2CRankLike.class);
		public int targetPlayerId;
		public int likeCount;
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2CRankLike [targetPlayerId="+targetPlayerId+",likeCount="+likeCount+",rewards="+rewards+",]";
		}
		public static final int msgCode = 158;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 158);
			retMsg.writeInt(targetPlayerId);
			retMsg.writeInt(likeCount);
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
	public static final class PushSmallTips{
		private static final Logger logger = LoggerFactory.getLogger(PushSmallTips.class);
		public int colorType;
		public String content;
		public PushSmallTips(int pcolorType,String pcontent){
			colorType=pcolorType;
			content=pcontent;
		}
		public PushSmallTips(){}
		@Override
		public String toString() {
			return "PushSmallTips [colorType="+colorType+",content="+content+",]";
		}
		public static final int msgCode = 160;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 160);
			retMsg.writeInt(colorType);
			retMsg.writeString(content);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SWanBaGetBalance{
		private static final Logger logger = LoggerFactory.getLogger(C2SWanBaGetBalance.class);
		public String appid;
		public String openid;
		public String openkey;
		public String pf;
		public int os_platfrom;
		public int goodsId;
		public String goodsName;
		@Override
		public String toString() {
			return "C2SWanBaGetBalance [appid="+appid+",openid="+openid+",openkey="+openkey+",pf="+pf+",os_platfrom="+os_platfrom+",goodsId="+goodsId+",goodsName="+goodsName+",]";
		}
		public static final int id = 161;

		public static C2SWanBaGetBalance parse(MyRequestMessage request){
			C2SWanBaGetBalance retObj = new C2SWanBaGetBalance();
			try{
			retObj.appid=request.readString();
			retObj.openid=request.readString();
			retObj.openkey=request.readString();
			retObj.pf=request.readString();
			retObj.os_platfrom=request.readInt();
			retObj.goodsId=request.readInt();
			retObj.goodsName=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CWanBaGetBalance{
		private static final Logger logger = LoggerFactory.getLogger(S2CWanBaGetBalance.class);
		public boolean is_balance_enough;
		public int defaultScore;
		public int itemid;
		public String cpOrderId;
		public String goodsName;
		public S2CWanBaGetBalance(boolean pis_balance_enough,int pdefaultScore,int pitemid,String pcpOrderId,String pgoodsName){
			is_balance_enough=pis_balance_enough;
			defaultScore=pdefaultScore;
			itemid=pitemid;
			cpOrderId=pcpOrderId;
			goodsName=pgoodsName;
		}
		public S2CWanBaGetBalance(){}
		@Override
		public String toString() {
			return "S2CWanBaGetBalance [is_balance_enough="+is_balance_enough+",defaultScore="+defaultScore+",itemid="+itemid+",cpOrderId="+cpOrderId+",goodsName="+goodsName+",]";
		}
		public static final int msgCode = 162;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 162);
			retMsg.writeBool(is_balance_enough);
			retMsg.writeInt(defaultScore);
			retMsg.writeInt(itemid);
			retMsg.writeString(cpOrderId);
			retMsg.writeString(goodsName);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SWanBaPay{
		private static final Logger logger = LoggerFactory.getLogger(C2SWanBaPay.class);
		public String appid;
		public String openid;
		public String openkey;
		public String pf;
		public int os_platfrom;
		public int itemid;
		@Override
		public String toString() {
			return "C2SWanBaPay [appid="+appid+",openid="+openid+",openkey="+openkey+",pf="+pf+",os_platfrom="+os_platfrom+",itemid="+itemid+",]";
		}
		public static final int id = 163;

		public static C2SWanBaPay parse(MyRequestMessage request){
			C2SWanBaPay retObj = new C2SWanBaPay();
			try{
			retObj.appid=request.readString();
			retObj.openid=request.readString();
			retObj.openkey=request.readString();
			retObj.pf=request.readString();
			retObj.os_platfrom=request.readInt();
			retObj.itemid=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SAddDesktopShortcutAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SAddDesktopShortcutAward.class);
		public static final int id = 165;
	}
	public static final class S2CAddDesktopShortcutAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CAddDesktopShortcutAward.class);
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2CAddDesktopShortcutAward [rewards="+rewards+",]";
		}
		public static final int msgCode = 166;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 166);
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
	public static final class C2SChangeName{
		private static final Logger logger = LoggerFactory.getLogger(C2SChangeName.class);
		public String name;
		@Override
		public String toString() {
			return "C2SChangeName [name="+name+",]";
		}
		public static final int id = 167;

		public static C2SChangeName parse(MyRequestMessage request){
			C2SChangeName retObj = new C2SChangeName();
			try{
			retObj.name=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CChangeName{
		private static final Logger logger = LoggerFactory.getLogger(S2CChangeName.class);
		public String name;
		public S2CChangeName(String pname){
			name=pname;
		}
		public S2CChangeName(){}
		@Override
		public String toString() {
			return "S2CChangeName [name="+name+",]";
		}
		public static final int msgCode = 168;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 168);
			retMsg.writeString(name);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SHeadsFramesImagesList{
		private static final Logger logger = LoggerFactory.getLogger(C2SHeadsFramesImagesList.class);
		public static final int id = 169;
	}
	public static final class S2CHeadsFramesImagesList{
		private static final Logger logger = LoggerFactory.getLogger(S2CHeadsFramesImagesList.class);
		public String _icons;
		public List<Integer> heads;
		public List<Integer> frames;
		public List<Integer> images;
		@Override
		public String toString() {
			return "S2CHeadsFramesImagesList [_icons="+_icons+",heads="+heads+",frames="+frames+",images="+images+",]";
		}
		public static final int msgCode = 170;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 170);
			retMsg.writeString(_icons);
			if(heads == null || heads.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(heads.size());
				for(Integer heads1 : heads){
			retMsg.writeInt(heads1);
				}
			}
			if(frames == null || frames.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(frames.size());
				for(Integer frames1 : frames){
			retMsg.writeInt(frames1);
				}
			}
			if(images == null || images.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(images.size());
				for(Integer images1 : images){
			retMsg.writeInt(images1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SChangeHeadIcon{
		private static final Logger logger = LoggerFactory.getLogger(C2SChangeHeadIcon.class);
		public int head_id;
		@Override
		public String toString() {
			return "C2SChangeHeadIcon [head_id="+head_id+",]";
		}
		public static final int id = 171;

		public static C2SChangeHeadIcon parse(MyRequestMessage request){
			C2SChangeHeadIcon retObj = new C2SChangeHeadIcon();
			try{
			retObj.head_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CChangeHeadIcon{
		private static final Logger logger = LoggerFactory.getLogger(S2CChangeHeadIcon.class);
		public int head_id;
		public S2CChangeHeadIcon(int phead_id){
			head_id=phead_id;
		}
		public S2CChangeHeadIcon(){}
		@Override
		public String toString() {
			return "S2CChangeHeadIcon [head_id="+head_id+",]";
		}
		public static final int msgCode = 172;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 172);
			retMsg.writeInt(head_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SChangeHeadFrame{
		private static final Logger logger = LoggerFactory.getLogger(C2SChangeHeadFrame.class);
		public int head_frame_id;
		@Override
		public String toString() {
			return "C2SChangeHeadFrame [head_frame_id="+head_frame_id+",]";
		}
		public static final int id = 173;

		public static C2SChangeHeadFrame parse(MyRequestMessage request){
			C2SChangeHeadFrame retObj = new C2SChangeHeadFrame();
			try{
			retObj.head_frame_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CChangeHeadFrame{
		private static final Logger logger = LoggerFactory.getLogger(S2CChangeHeadFrame.class);
		public int head_frame_id;
		public S2CChangeHeadFrame(int phead_frame_id){
			head_frame_id=phead_frame_id;
		}
		public S2CChangeHeadFrame(){}
		@Override
		public String toString() {
			return "S2CChangeHeadFrame [head_frame_id="+head_frame_id+",]";
		}
		public static final int msgCode = 174;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 174);
			retMsg.writeInt(head_frame_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SChangeHeadImage{
		private static final Logger logger = LoggerFactory.getLogger(C2SChangeHeadImage.class);
		public int image_id;
		@Override
		public String toString() {
			return "C2SChangeHeadImage [image_id="+image_id+",]";
		}
		public static final int id = 175;

		public static C2SChangeHeadImage parse(MyRequestMessage request){
			C2SChangeHeadImage retObj = new C2SChangeHeadImage();
			try{
			retObj.image_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CChangeHeadImage{
		private static final Logger logger = LoggerFactory.getLogger(S2CChangeHeadImage.class);
		public int image_id;
		public S2CChangeHeadImage(int pimage_id){
			image_id=pimage_id;
		}
		public S2CChangeHeadImage(){}
		@Override
		public String toString() {
			return "S2CChangeHeadImage [image_id="+image_id+",]";
		}
		public static final int msgCode = 176;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 176);
			retMsg.writeInt(image_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SIosIAPVerify{
		private static final Logger logger = LoggerFactory.getLogger(C2SIosIAPVerify.class);
		public String receipt;
		@Override
		public String toString() {
			return "C2SIosIAPVerify [receipt="+receipt+",]";
		}
		public static final int id = 177;

		public static C2SIosIAPVerify parse(MyRequestMessage request){
			C2SIosIAPVerify retObj = new C2SIosIAPVerify();
			try{
			retObj.receipt=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CIosIAPVerify{
		private static final Logger logger = LoggerFactory.getLogger(S2CIosIAPVerify.class);
		public static final int msgCode = 178;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 178);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class PushIosIAPVerify{
		private static final Logger logger = LoggerFactory.getLogger(PushIosIAPVerify.class);
		public int ret;
		public PushIosIAPVerify(int pret){
			ret=pret;
		}
		public PushIosIAPVerify(){}
		@Override
		public String toString() {
			return "PushIosIAPVerify [ret="+ret+",]";
		}
		public static final int msgCode = 180;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 180);
			retMsg.writeInt(ret);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SQedjList{
		private static final Logger logger = LoggerFactory.getLogger(C2SQedjList.class);
		public static final int id = 181;
	}
	public static final class S2CQedjList{
		private static final Logger logger = LoggerFactory.getLogger(S2CQedjList.class);
		public boolean is_award_get;
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2CQedjList [is_award_get="+is_award_get+",rewards="+rewards+",]";
		}
		public static final int msgCode = 182;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 182);
			retMsg.writeBool(is_award_get);
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
	public static final class C2SQedjAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SQedjAward.class);
		public static final int id = 183;
	}
	public static final class S2CQedjAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CQedjAward.class);
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2CQedjAward [rewards="+rewards+",]";
		}
		public static final int msgCode = 184;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 184);
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
	public static final class C2STeamsInfoSet{
		private static final Logger logger = LoggerFactory.getLogger(C2STeamsInfoSet.class);
		public int type;
		public long[] pos_card_uuid;
		public int pet_id;
		@Override
		public String toString() {
			return "C2STeamsInfoSet [type="+type+",pos_card_uuid="+java.util.Arrays.toString(pos_card_uuid)+",pet_id="+pet_id+",]";
		}
		public static final int id = 185;

		public static C2STeamsInfoSet parse(MyRequestMessage request){
			C2STeamsInfoSet retObj = new C2STeamsInfoSet();
			try{
			retObj.type=request.readInt();
			int pos_card_uuid_size = request.readInt();
				retObj.pos_card_uuid = new long[pos_card_uuid_size];
				for(int i=0;i<pos_card_uuid_size;i++){
					retObj.pos_card_uuid[i]=request.readLong();
				}
			retObj.pet_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CTeamsInfoSet{
		private static final Logger logger = LoggerFactory.getLogger(S2CTeamsInfoSet.class);
		public static final int msgCode = 186;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 186);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGoldBuyList{
		private static final Logger logger = LoggerFactory.getLogger(C2SGoldBuyList.class);
		public static final int id = 187;
	}
	public static final class S2CGoldBuyList{
		private static final Logger logger = LoggerFactory.getLogger(S2CGoldBuyList.class);
		public List<Integer> buy_seconds;
		@Override
		public String toString() {
			return "S2CGoldBuyList [buy_seconds="+buy_seconds+",]";
		}
		public static final int msgCode = 188;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 188);
			if(buy_seconds == null || buy_seconds.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(buy_seconds.size());
				for(Integer buy_seconds1 : buy_seconds){
			retMsg.writeInt(buy_seconds1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGoldBuy{
		private static final Logger logger = LoggerFactory.getLogger(C2SGoldBuy.class);
		public int buy_type;
		@Override
		public String toString() {
			return "C2SGoldBuy [buy_type="+buy_type+",]";
		}
		public static final int id = 189;

		public static C2SGoldBuy parse(MyRequestMessage request){
			C2SGoldBuy retObj = new C2SGoldBuy();
			try{
			retObj.buy_type=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGoldBuy{
		private static final Logger logger = LoggerFactory.getLogger(S2CGoldBuy.class);
		public int buy_type;
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2CGoldBuy [buy_type="+buy_type+",rewards="+rewards+",]";
		}
		public static final int msgCode = 190;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 190);
			retMsg.writeInt(buy_type);
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
	public static final class C2SDrawList{
		private static final Logger logger = LoggerFactory.getLogger(C2SDrawList.class);
		public static final int id = 191;
	}
	public static final class S2CDrawList{
		private static final Logger logger = LoggerFactory.getLogger(S2CDrawList.class);
		public static final int msgCode = 192;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 192);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDrawRecruit{
		private static final Logger logger = LoggerFactory.getLogger(C2SDrawRecruit.class);
		public int buy_type;
		public int times;
		@Override
		public String toString() {
			return "C2SDrawRecruit [buy_type="+buy_type+",times="+times+",]";
		}
		public static final int id = 193;

		public static C2SDrawRecruit parse(MyRequestMessage request){
			C2SDrawRecruit retObj = new C2SDrawRecruit();
			try{
			retObj.buy_type=request.readInt();
			retObj.times=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CDrawRecruit{
		private static final Logger logger = LoggerFactory.getLogger(S2CDrawRecruit.class);
		public int buy_type;
		public int times;
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2CDrawRecruit [buy_type="+buy_type+",times="+times+",rewards="+rewards+",]";
		}
		public static final int msgCode = 194;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 194);
			retMsg.writeInt(buy_type);
			retMsg.writeInt(times);
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

    public static final class C2SConfigJson{
		private static final Logger logger = LoggerFactory.getLogger(C2SConfigJson.class);
		public static final int id = 10001;

        public String filename;
        public int part;
		@Override
		public String toString() {
			return "C2SConfigJson [filename="+filename+",part="+part+",]";
		}

		public static C2SConfigJson parse(MyRequestMessage request){
			C2SConfigJson retObj = new C2SConfigJson();
			try{
			retObj.filename=request.readString();
			retObj.part=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}

    public static final class S2CConfigJson{
		private static final Logger logger = LoggerFactory.getLogger(S2CConfigJson.class);
		public String json;
		@Override
		public String toString() {
			return "S2CConfigJson [json="+json+",]";
		}
		public static final int msgCode = 10002;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
                MySendToMessage retMsg = new MySendToMessage(alloc, 10002);
                retMsg.writeString(json);
                return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}

	public static final class C2SScrollAnno{
		private static final Logger logger = LoggerFactory.getLogger(C2SScrollAnno.class);
		public static final int id = 195;
	}
	public static final class S2CScrollAnno{
		private static final Logger logger = LoggerFactory.getLogger(S2CScrollAnno.class);
		public List<String> annos;
		@Override
		public String toString() {
			return "S2CScrollAnno [annos="+annos+",]";
		}
		public static final int msgCode = 196;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 196);
			if(annos == null || annos.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(annos.size());
				for(String annos1 : annos){
			retMsg.writeString(annos1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SBattleSeason{
		private static final Logger logger = LoggerFactory.getLogger(C2SBattleSeason.class);
		public static final int id = 197;
	}
	public static final class S2CBattleSeason{
		private static final Logger logger = LoggerFactory.getLogger(S2CBattleSeason.class);
		public List<Integer> vals;
		public IOSeason info;
		@Override
		public String toString() {
			return "S2CBattleSeason [vals="+vals+",info="+info+",]";
		}
		public static final int msgCode = 198;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 198);
			if(vals == null || vals.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(vals.size());
				for(Integer vals1 : vals){
			retMsg.writeInt(vals1);
				}
			}
			if(info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(info.etime);
					retMsg.writeInt(info.year);
					retMsg.writeInt(info.season);
					if(info.pos == null || info.pos.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(info.pos.size());
				for(Integer info_pos1 : info.pos){
			retMsg.writeInt(info_pos1);
				}
			}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetOtherPlayerInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetOtherPlayerInfo.class);
		public int[] ids;
		public boolean pvpdef;
		public String battleset;
		@Override
		public String toString() {
			return "C2SGetOtherPlayerInfo [ids="+java.util.Arrays.toString(ids)+",pvpdef="+pvpdef+",battleset="+battleset+",]";
		}
		public static final int id = 199;

		public static C2SGetOtherPlayerInfo parse(MyRequestMessage request){
			C2SGetOtherPlayerInfo retObj = new C2SGetOtherPlayerInfo();
			try{
			int ids_size = request.readInt();
				retObj.ids = new int[ids_size];
				for(int i=0;i<ids_size;i++){
					retObj.ids[i]=request.readInt();
				}
			retObj.pvpdef=request.readBool();
			retObj.battleset=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGetOtherPlayerInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetOtherPlayerInfo.class);
		public List<IOOtherPlayer> items;
		@Override
		public String toString() {
			return "S2CGetOtherPlayerInfo [items="+items+",]";
		}
		public static final int msgCode = 200;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 200);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IOOtherPlayer items1 : items){
					retMsg.writeInt(items1.rid);
					retMsg.writeInt(items1.rno);
					retMsg.writeString(items1.rname);
					retMsg.writeInt(items1.sex);
					retMsg.writeInt(items1.power);
					retMsg.writeInt(items1.iconid);
					retMsg.writeInt(items1.headid);
					retMsg.writeInt(items1.frameid);
					retMsg.writeInt(items1.imageid);
					retMsg.writeInt(items1.level);
					retMsg.writeInt(items1.vip);
					retMsg.writeInt(items1.office_index);
					if(items1.bestgeneral == null || items1.bestgeneral.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1.bestgeneral.size());
				for(IOGeneralBean items1_bestgeneral1 : items1.bestgeneral){
					retMsg.writeLong(items1_bestgeneral1.guid);
					retMsg.writeInt(items1_bestgeneral1.gsid);
					retMsg.writeInt(items1_bestgeneral1.level);
					retMsg.writeInt(items1_bestgeneral1.star);
					retMsg.writeInt(items1_bestgeneral1.camp);
					retMsg.writeInt(items1_bestgeneral1.occu);
					retMsg.writeInt(items1_bestgeneral1.pclass);
					retMsg.writeInt(items1_bestgeneral1.power);
					if(items1_bestgeneral1.talent == null || items1_bestgeneral1.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_bestgeneral1.talent.size());
				for(Integer items1_bestgeneral1_talent1 : items1_bestgeneral1.talent){
			retMsg.writeInt(items1_bestgeneral1_talent1);
				}
			}
					retMsg.writeInt(items1_bestgeneral1.affairs);
					retMsg.writeInt(items1_bestgeneral1.treasure);
					if(items1_bestgeneral1.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(items1_bestgeneral1.property.hp);
					retMsg.writeInt(items1_bestgeneral1.property.atk);
					retMsg.writeInt(items1_bestgeneral1.property.def);
					retMsg.writeInt(items1_bestgeneral1.property.mdef);
					retMsg.writeFloat(items1_bestgeneral1.property.atktime);
					retMsg.writeInt(items1_bestgeneral1.property.range);
					retMsg.writeInt(items1_bestgeneral1.property.msp);
					retMsg.writeInt(items1_bestgeneral1.property.pasp);
					retMsg.writeInt(items1_bestgeneral1.property.pcri);
					retMsg.writeInt(items1_bestgeneral1.property.pcrid);
					retMsg.writeInt(items1_bestgeneral1.property.pdam);
					retMsg.writeInt(items1_bestgeneral1.property.php);
					retMsg.writeInt(items1_bestgeneral1.property.patk);
					retMsg.writeInt(items1_bestgeneral1.property.pdef);
					retMsg.writeInt(items1_bestgeneral1.property.pmdef);
					retMsg.writeInt(items1_bestgeneral1.property.ppbs);
					retMsg.writeInt(items1_bestgeneral1.property.pmbs);
					retMsg.writeInt(items1_bestgeneral1.property.pefc);
					retMsg.writeInt(items1_bestgeneral1.property.ppthr);
					retMsg.writeInt(items1_bestgeneral1.property.patkdam);
					retMsg.writeInt(items1_bestgeneral1.property.pskidam);
					retMsg.writeInt(items1_bestgeneral1.property.pckatk);
					retMsg.writeInt(items1_bestgeneral1.property.pmthr);
					retMsg.writeInt(items1_bestgeneral1.property.pdex);
					retMsg.writeInt(items1_bestgeneral1.property.pmdex);
					retMsg.writeInt(items1_bestgeneral1.property.pmsatk);
					retMsg.writeInt(items1_bestgeneral1.property.pmps);
					retMsg.writeInt(items1_bestgeneral1.property.pcd);
			}
					if(items1_bestgeneral1.equip == null || items1_bestgeneral1.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_bestgeneral1.equip.size());
				for(Integer items1_bestgeneral1_equip1 : items1_bestgeneral1.equip){
			retMsg.writeInt(items1_bestgeneral1_equip1);
				}
			}
					if(items1_bestgeneral1.skill == null || items1_bestgeneral1.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_bestgeneral1.skill.size());
				for(Integer items1_bestgeneral1_skill1 : items1_bestgeneral1.skill){
			retMsg.writeInt(items1_bestgeneral1_skill1);
				}
			}
					if(items1_bestgeneral1.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(items1_bestgeneral1.exclusive.level);
					if(items1_bestgeneral1.exclusive.skill == null || items1_bestgeneral1.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_bestgeneral1.exclusive.skill.size());
				for(Integer items1_bestgeneral1_exclusive_skill1 : items1_bestgeneral1.exclusive.skill){
			retMsg.writeInt(items1_bestgeneral1_exclusive_skill1);
				}
			}
					retMsg.writeInt(items1_bestgeneral1.exclusive.gsid);
					if(items1_bestgeneral1.exclusive.property == null || items1_bestgeneral1.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_bestgeneral1.exclusive.property.size());
				for(KvStringPair items1_bestgeneral1_exclusive_property1 : items1_bestgeneral1.exclusive.property){
					retMsg.writeString(items1_bestgeneral1_exclusive_property1.key);
					retMsg.writeInt(items1_bestgeneral1_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(items1_bestgeneral1.hppercent);
				}
			}
					if(items1.battleset == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(items1.battleset.mythic);
					retMsg.writeInt(items1.battleset.power);
					if(items1.battleset.team == null || items1.battleset.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1.battleset.team.size());
				for(Map.Entry<Integer,IOGeneralBean> items1_battleset_team1 : items1.battleset.team.entrySet()){
			retMsg.writeInt(items1_battleset_team1.getKey());
					retMsg.writeLong(items1_battleset_team1.getValue().guid);
					retMsg.writeInt(items1_battleset_team1.getValue().gsid);
					retMsg.writeInt(items1_battleset_team1.getValue().level);
					retMsg.writeInt(items1_battleset_team1.getValue().star);
					retMsg.writeInt(items1_battleset_team1.getValue().camp);
					retMsg.writeInt(items1_battleset_team1.getValue().occu);
					retMsg.writeInt(items1_battleset_team1.getValue().pclass);
					retMsg.writeInt(items1_battleset_team1.getValue().power);
					if(items1_battleset_team1.getValue().talent == null || items1_battleset_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_battleset_team1.getValue().talent.size());
				for(Integer items1_battleset_team1_getValue_talent1 : items1_battleset_team1.getValue().talent){
			retMsg.writeInt(items1_battleset_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(items1_battleset_team1.getValue().affairs);
					retMsg.writeInt(items1_battleset_team1.getValue().treasure);
					if(items1_battleset_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(items1_battleset_team1.getValue().property.hp);
					retMsg.writeInt(items1_battleset_team1.getValue().property.atk);
					retMsg.writeInt(items1_battleset_team1.getValue().property.def);
					retMsg.writeInt(items1_battleset_team1.getValue().property.mdef);
					retMsg.writeFloat(items1_battleset_team1.getValue().property.atktime);
					retMsg.writeInt(items1_battleset_team1.getValue().property.range);
					retMsg.writeInt(items1_battleset_team1.getValue().property.msp);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pasp);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pcri);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pcrid);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pdam);
					retMsg.writeInt(items1_battleset_team1.getValue().property.php);
					retMsg.writeInt(items1_battleset_team1.getValue().property.patk);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pdef);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pmdef);
					retMsg.writeInt(items1_battleset_team1.getValue().property.ppbs);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pmbs);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pefc);
					retMsg.writeInt(items1_battleset_team1.getValue().property.ppthr);
					retMsg.writeInt(items1_battleset_team1.getValue().property.patkdam);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pskidam);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pckatk);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pmthr);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pdex);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pmdex);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pmsatk);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pmps);
					retMsg.writeInt(items1_battleset_team1.getValue().property.pcd);
			}
					if(items1_battleset_team1.getValue().equip == null || items1_battleset_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_battleset_team1.getValue().equip.size());
				for(Integer items1_battleset_team1_getValue_equip1 : items1_battleset_team1.getValue().equip){
			retMsg.writeInt(items1_battleset_team1_getValue_equip1);
				}
			}
					if(items1_battleset_team1.getValue().skill == null || items1_battleset_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_battleset_team1.getValue().skill.size());
				for(Integer items1_battleset_team1_getValue_skill1 : items1_battleset_team1.getValue().skill){
			retMsg.writeInt(items1_battleset_team1_getValue_skill1);
				}
			}
					if(items1_battleset_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(items1_battleset_team1.getValue().exclusive.level);
					if(items1_battleset_team1.getValue().exclusive.skill == null || items1_battleset_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_battleset_team1.getValue().exclusive.skill.size());
				for(Integer items1_battleset_team1_getValue_exclusive_skill1 : items1_battleset_team1.getValue().exclusive.skill){
			retMsg.writeInt(items1_battleset_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(items1_battleset_team1.getValue().exclusive.gsid);
					if(items1_battleset_team1.getValue().exclusive.property == null || items1_battleset_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_battleset_team1.getValue().exclusive.property.size());
				for(KvStringPair items1_battleset_team1_getValue_exclusive_property1 : items1_battleset_team1.getValue().exclusive.property){
					retMsg.writeString(items1_battleset_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(items1_battleset_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(items1_battleset_team1.getValue().hppercent);
				}
			}
			}
					retMsg.writeInt(items1.points);
					if(items1.battlelines == null || items1.battlelines.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1.battlelines.size());
				for(IOBattleLine items1_battlelines1 : items1.battlelines){
					retMsg.writeInt(items1_battlelines1.power);
				}
			}
					if(items1.kpBattleset == null || items1.kpBattleset.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1.kpBattleset.size());
				for(IOBattleSet items1_kpBattleset1 : items1.kpBattleset){
					retMsg.writeInt(items1_kpBattleset1.mythic);
					retMsg.writeInt(items1_kpBattleset1.power);
					if(items1_kpBattleset1.team == null || items1_kpBattleset1.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_kpBattleset1.team.size());
				for(Map.Entry<Integer,IOGeneralBean> items1_kpBattleset1_team1 : items1_kpBattleset1.team.entrySet()){
			retMsg.writeInt(items1_kpBattleset1_team1.getKey());
					retMsg.writeLong(items1_kpBattleset1_team1.getValue().guid);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().gsid);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().level);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().star);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().camp);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().occu);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().pclass);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().power);
					if(items1_kpBattleset1_team1.getValue().talent == null || items1_kpBattleset1_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_kpBattleset1_team1.getValue().talent.size());
				for(Integer items1_kpBattleset1_team1_getValue_talent1 : items1_kpBattleset1_team1.getValue().talent){
			retMsg.writeInt(items1_kpBattleset1_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().affairs);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().treasure);
					if(items1_kpBattleset1_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.hp);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.atk);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.def);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.mdef);
					retMsg.writeFloat(items1_kpBattleset1_team1.getValue().property.atktime);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.range);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.msp);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pasp);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pcri);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pcrid);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pdam);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.php);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.patk);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pdef);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pmdef);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.ppbs);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pmbs);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pefc);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.ppthr);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.patkdam);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pskidam);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pckatk);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pmthr);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pdex);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pmdex);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pmsatk);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pmps);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().property.pcd);
			}
					if(items1_kpBattleset1_team1.getValue().equip == null || items1_kpBattleset1_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_kpBattleset1_team1.getValue().equip.size());
				for(Integer items1_kpBattleset1_team1_getValue_equip1 : items1_kpBattleset1_team1.getValue().equip){
			retMsg.writeInt(items1_kpBattleset1_team1_getValue_equip1);
				}
			}
					if(items1_kpBattleset1_team1.getValue().skill == null || items1_kpBattleset1_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_kpBattleset1_team1.getValue().skill.size());
				for(Integer items1_kpBattleset1_team1_getValue_skill1 : items1_kpBattleset1_team1.getValue().skill){
			retMsg.writeInt(items1_kpBattleset1_team1_getValue_skill1);
				}
			}
					if(items1_kpBattleset1_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().exclusive.level);
					if(items1_kpBattleset1_team1.getValue().exclusive.skill == null || items1_kpBattleset1_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_kpBattleset1_team1.getValue().exclusive.skill.size());
				for(Integer items1_kpBattleset1_team1_getValue_exclusive_skill1 : items1_kpBattleset1_team1.getValue().exclusive.skill){
			retMsg.writeInt(items1_kpBattleset1_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().exclusive.gsid);
					if(items1_kpBattleset1_team1.getValue().exclusive.property == null || items1_kpBattleset1_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items1_kpBattleset1_team1.getValue().exclusive.property.size());
				for(KvStringPair items1_kpBattleset1_team1_getValue_exclusive_property1 : items1_kpBattleset1_team1.getValue().exclusive.property){
					retMsg.writeString(items1_kpBattleset1_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(items1_kpBattleset1_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(items1_kpBattleset1_team1.getValue().hppercent);
				}
			}
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SPlayerBaseData{
		private static final Logger logger = LoggerFactory.getLogger(C2SPlayerBaseData.class);
		public static final int id = 1001;
	}
	public static final class S2CPlayerBaseData{
		private static final Logger logger = LoggerFactory.getLogger(S2CPlayerBaseData.class);
		public int maxmapid;
		public int tower;
		public int pvpscore;
		public int gnum;
		public int bagspace;
		public List<SimpleItemInfo> hiddens;
		public IOSpecial special;
		@Override
		public String toString() {
			return "S2CPlayerBaseData [maxmapid="+maxmapid+",tower="+tower+",pvpscore="+pvpscore+",gnum="+gnum+",bagspace="+bagspace+",hiddens="+hiddens+",special="+special+",]";
		}
		public static final int msgCode = 1002;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1002);
			retMsg.writeInt(maxmapid);
			retMsg.writeInt(tower);
			retMsg.writeInt(pvpscore);
			retMsg.writeInt(gnum);
			retMsg.writeInt(bagspace);
			if(hiddens == null || hiddens.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(hiddens.size());
				for(SimpleItemInfo hiddens1 : hiddens){
					retMsg.writeInt(hiddens1.gsid);
					retMsg.writeInt(hiddens1.count);
				}
			}
			if(special == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(special.bw);
					retMsg.writeLong(special.qz);
					retMsg.writeLong(special.zx);
					retMsg.writeLong(special.lm);
					retMsg.writeLong(special.yd);
					retMsg.writeLong(special.cz);
					retMsg.writeLong(special.gz);
					retMsg.writeLong(special.zz);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuideStep{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuideStep.class);
		public static final int id = 1003;
	}
	public static final class S2CGuideStep{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuideStep.class);
		public static final int msgCode = 1004;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1004);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuideOne{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuideOne.class);
		public int akey;
		@Override
		public String toString() {
			return "C2SGuideOne [akey="+akey+",]";
		}
		public static final int id = 1005;

		public static C2SGuideOne parse(MyRequestMessage request){
			C2SGuideOne retObj = new C2SGuideOne();
			try{
			retObj.akey=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuideOne{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuideOne.class);
		public boolean isHas;
		public S2CGuideOne(boolean pisHas){
			isHas=pisHas;
		}
		public S2CGuideOne(){}
		@Override
		public String toString() {
			return "S2CGuideOne [isHas="+isHas+",]";
		}
		public static final int msgCode = 1006;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1006);
			retMsg.writeBool(isHas);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuideSave{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuideSave.class);
		public int step;
		public long time;
		public String mark;
		@Override
		public String toString() {
			return "C2SGuideSave [step="+step+",time="+time+",mark="+mark+",]";
		}
		public static final int id = 1007;

		public static C2SGuideSave parse(MyRequestMessage request){
			C2SGuideSave retObj = new C2SGuideSave();
			try{
			retObj.step=request.readInt();
			retObj.time=request.readLong();
			retObj.mark=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuideSave{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuideSave.class);
		public static final int msgCode = 1008;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1008);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuideChooseReward{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuideChooseReward.class);
		public String choose_str;
		@Override
		public String toString() {
			return "C2SGuideChooseReward [choose_str="+choose_str+",]";
		}
		public static final int id = 1009;

		public static C2SGuideChooseReward parse(MyRequestMessage request){
			C2SGuideChooseReward retObj = new C2SGuideChooseReward();
			try{
			retObj.choose_str=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuideChooseReward{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuideChooseReward.class);
		public List<SimpleItemInfo> rewards;
		@Override
		public String toString() {
			return "S2CGuideChooseReward [rewards="+rewards+",]";
		}
		public static final int msgCode = 1010;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1010);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(SimpleItemInfo rewards1 : rewards){
					retMsg.writeInt(rewards1.gsid);
					retMsg.writeInt(rewards1.count);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuideEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuideEnd.class);
		public int add_step;
		@Override
		public String toString() {
			return "C2SGuideEnd [add_step="+add_step+",]";
		}
		public static final int id = 1011;

		public static C2SGuideEnd parse(MyRequestMessage request){
			C2SGuideEnd retObj = new C2SGuideEnd();
			try{
			retObj.add_step=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuideEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuideEnd.class);
		public static final int msgCode = 1012;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1012);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SHeroChoose1in3{
		private static final Logger logger = LoggerFactory.getLogger(C2SHeroChoose1in3.class);
		public int index;
		@Override
		public String toString() {
			return "C2SHeroChoose1in3 [index="+index+",]";
		}
		public static final int id = 1013;

		public static C2SHeroChoose1in3 parse(MyRequestMessage request){
			C2SHeroChoose1in3 retObj = new C2SHeroChoose1in3();
			try{
			retObj.index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CHeroChoose1in3{
		private static final Logger logger = LoggerFactory.getLogger(S2CHeroChoose1in3.class);
		public List<SimpleItemInfo> rewards;
		@Override
		public String toString() {
			return "S2CHeroChoose1in3 [rewards="+rewards+",]";
		}
		public static final int msgCode = 1014;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1014);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(SimpleItemInfo rewards1 : rewards){
					retMsg.writeInt(rewards1.gsid);
					retMsg.writeInt(rewards1.count);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SPlayerByParam{
		private static final Logger logger = LoggerFactory.getLogger(C2SPlayerByParam.class);
		public String[] arr;
		@Override
		public String toString() {
			return "C2SPlayerByParam [arr="+java.util.Arrays.toString(arr)+",]";
		}
		public static final int id = 1015;

		public static C2SPlayerByParam parse(MyRequestMessage request){
			C2SPlayerByParam retObj = new C2SPlayerByParam();
			try{
			int arr_size = request.readInt();
				retObj.arr = new String[arr_size];
				for(int i=0;i<arr_size;i++){
					retObj.arr[i]=request.readString();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CPlayerByParam{
		private static final Logger logger = LoggerFactory.getLogger(S2CPlayerByParam.class);
		public long legion;
		public int gnum;
		public int bagspace;
		public Map<Integer,Integer> tech;
		public IODungeonTop dgtop;
		public int frameid;
		@Override
		public String toString() {
			return "S2CPlayerByParam [legion="+legion+",gnum="+gnum+",bagspace="+bagspace+",tech="+tech+",dgtop="+dgtop+",frameid="+frameid+",]";
		}
		public static final int msgCode = 1016;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1016);
			retMsg.writeLong(legion);
			retMsg.writeInt(gnum);
			retMsg.writeInt(bagspace);
			if(tech == null || tech.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(tech.size());
				for(Map.Entry<Integer,Integer> tech1 : tech.entrySet()){
			retMsg.writeInt(tech1.getKey());
			retMsg.writeInt(tech1.getValue());
				}
			}
			if(dgtop == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(dgtop.chapter);
					retMsg.writeInt(dgtop.node);
			}
			retMsg.writeInt(frameid);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class PushScrollAnno{
		private static final Logger logger = LoggerFactory.getLogger(PushScrollAnno.class);
		public int msg_id;
		public List<String> params;
		
		@Override
		public String toString() {
			return "PushScrollAnno [msg_id="+msg_id+",params="+params+",]";
		}
		public static final int msgCode = 1018;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1018);
			retMsg.writeInt(msg_id);
			if(params == null || params.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(params.size());
				for(String params1 : params){
			retMsg.writeString(params1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SBuyRight{
		private static final Logger logger = LoggerFactory.getLogger(C2SBuyRight.class);
		public String pid;
		@Override
		public String toString() {
			return "C2SBuyRight [pid="+pid+",]";
		}
		public static final int id = 1019;

		public static C2SBuyRight parse(MyRequestMessage request){
			C2SBuyRight retObj = new C2SBuyRight();
			try{
			retObj.pid=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CBuyRight{
		private static final Logger logger = LoggerFactory.getLogger(S2CBuyRight.class);
		public static final int msgCode = 1020;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1020);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}

    public static final class PushPvpRankChange {
        private static final Logger logger = LoggerFactory.getLogger(PushPvpRankChange.class);
        public int rank;
        
        @Override
        public String toString() {
            return "PushPvpRankChange [rank=" + rank + "]";
        }
        
        public static final int msgCode = 546;
        
        public MySendToMessage build(ByteBufAllocator alloc) {
            try {
                MySendToMessage retMsg = new MySendToMessage(alloc, msgCode);
                retMsg.writeInt(rank);
                return retMsg;
            } catch(Exception e) {
                logger.error("build protocol error!", e);
                return null;
            }
        }
    }
}
