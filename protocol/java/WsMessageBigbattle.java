package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.Map;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOFormationGeneralPos;
import ws.WsMessageBase.IOBattleResult;
import ws.WsMessageBase.IOBattleRetSide;
import ws.WsMessageBase.IOBattleRecordInfo;
import ws.WsMessageBase.IOBattleRetSet;
import ws.WsMessageBase.IOGeneralBean;
import ws.WsMessageBase.IOProperty;
import ws.WsMessageBase.IOExclusive;
import ws.WsMessageBase.KvStringPair;
import ws.WsMessageBase.IOBattleReport;
import ws.WsMessageBase.IOBattleReportItem;
import ws.WsMessageBase.IORewardItem;
import ws.WsMessageBase.IOGeneralSimple;
import ws.WsMessageBase.IOBHurt;

public final class WsMessageBigbattle{
	public static final class C2SBigBattleInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SBigBattleInfo.class);
		public int mapid;
		@Override
		public String toString() {
			return "C2SBigBattleInfo [mapid="+mapid+",]";
		}
		public static final int id = 571;

		public static C2SBigBattleInfo parse(MyRequestMessage request){
			C2SBigBattleInfo retObj = new C2SBigBattleInfo();
			try{
			retObj.mapid=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CBigBattleInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CBigBattleInfo.class);
		public List<IOGeneralSimple> items;
		@Override
		public String toString() {
			return "S2CBigBattleInfo [items="+items+",]";
		}
		public static final int msgCode = 572;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 572);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(IOGeneralSimple items1 : items){
					retMsg.writeInt(items1.pos);
					retMsg.writeInt(items1.gsid);
					retMsg.writeInt(items1.level);
					retMsg.writeInt(items1.hpcover);
					retMsg.writeInt(items1.pclass);
					retMsg.writeLong(items1.nowhp);
					retMsg.writeFloat(items1.exhp);
					retMsg.writeFloat(items1.exatk);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SBigBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SBigBattleStart.class);
		public int mapid;
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SBigBattleStart [mapid="+mapid+",mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 573;

		public static C2SBigBattleStart parse(MyRequestMessage request){
			C2SBigBattleStart retObj = new C2SBigBattleStart();
			try{
			retObj.mapid=request.readInt();
			retObj.mythic=request.readInt();
			int items_size = request.readInt();
				retObj.items = new IOFormationGeneralPos[items_size];
				for(int i=0;i<items_size;i++){
						retObj.items[i] = new IOFormationGeneralPos();
					retObj.items[i].pos=request.readInt();
					retObj.items[i].general_uuid=request.readLong();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CBigBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CBigBattleStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CBigBattleStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CBigBattleStart(){}
		@Override
		public String toString() {
			return "S2CBigBattleStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 574;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 574);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SBigBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SBigBattleEnd.class);
		public int mapid;
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SBigBattleEnd [mapid="+mapid+",battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 575;

		public static C2SBigBattleEnd parse(MyRequestMessage request){
			C2SBigBattleEnd retObj = new C2SBigBattleEnd();
			try{
			retObj.mapid=request.readInt();
			retObj.battleid=request.readLong();
			int as_size = request.readInt();
				retObj.as = new IOBHurt[as_size];
				for(int i=0;i<as_size;i++){
						retObj.as[i] = new IOBHurt();
					retObj.as[i].gsid=request.readInt();
					retObj.as[i].hurm=request.readLong();
					retObj.as[i].heal=request.readLong();
					retObj.as[i].hp=request.readLong();
					retObj.as[i].born=request.readInt();
					retObj.as[i].hpperc=request.readInt();
					retObj.as[i].hpmax=request.readLong();
				}
			int df_size = request.readInt();
				retObj.df = new IOBHurt[df_size];
				for(int i=0;i<df_size;i++){
						retObj.df[i] = new IOBHurt();
					retObj.df[i].gsid=request.readInt();
					retObj.df[i].hurm=request.readLong();
					retObj.df[i].heal=request.readLong();
					retObj.df[i].hp=request.readLong();
					retObj.df[i].born=request.readInt();
					retObj.df[i].hpperc=request.readInt();
					retObj.df[i].hpmax=request.readLong();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CBigBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CBigBattleEnd.class);
		public IOBattleResult result;
		public List<IORewardItem> rewards;
		public int times;
		@Override
		public String toString() {
			return "S2CBigBattleEnd [result="+result+",rewards="+rewards+",times="+times+",]";
		}
		public static final int msgCode = 576;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 576);
			if(result == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(result.ret);
					retMsg.writeInt(result.round);
					if(result.lhp == null || result.lhp.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.lhp.size());
				for(Map.Entry<Integer,Long> result_lhp1 : result.lhp.entrySet()){
			retMsg.writeInt(result_lhp1.getKey());
			retMsg.writeLong(result_lhp1.getValue());
				}
			}
					if(result.rhp == null || result.rhp.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.rhp.size());
				for(Map.Entry<Integer,Long> result_rhp1 : result.rhp.entrySet()){
			retMsg.writeInt(result_rhp1.getKey());
			retMsg.writeLong(result_rhp1.getValue());
				}
			}
					if(result.lper == null || result.lper.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.lper.size());
				for(Map.Entry<Integer,Integer> result_lper1 : result.lper.entrySet()){
			retMsg.writeInt(result_lper1.getKey());
			retMsg.writeInt(result_lper1.getValue());
				}
			}
					if(result.rper == null || result.rper.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.rper.size());
				for(Map.Entry<Integer,Integer> result_rper1 : result.rper.entrySet()){
			retMsg.writeInt(result_rper1.getKey());
			retMsg.writeInt(result_rper1.getValue());
				}
			}
					retMsg.writeInt(result.ltper);
					retMsg.writeInt(result.rtper);
					retMsg.writeInt(result.rlosthp);
					if(result.report == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(result.report.left == null || result.report.left.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.report.left.size());
				for(IOBattleReportItem result_report_left1 : result.report.left){
					retMsg.writeInt(result_report_left1.gsid);
					retMsg.writeLong(result_report_left1.hurm);
					retMsg.writeLong(result_report_left1.heal);
					retMsg.writeInt(result_report_left1.level);
					retMsg.writeInt(result_report_left1.pclass);
				}
			}
					if(result.report.right == null || result.report.right.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.report.right.size());
				for(IOBattleReportItem result_report_right1 : result.report.right){
					retMsg.writeInt(result_report_right1.gsid);
					retMsg.writeLong(result_report_right1.hurm);
					retMsg.writeLong(result_report_right1.heal);
					retMsg.writeInt(result_report_right1.level);
					retMsg.writeInt(result_report_right1.pclass);
				}
			}
			}
					retMsg.writeLong(result.version);
					if(result.left == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(result.left.info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(result.left.info.rname);
					retMsg.writeInt(result.left.info.level);
					retMsg.writeInt(result.left.info.iconid);
					retMsg.writeInt(result.left.info.headid);
					retMsg.writeInt(result.left.info.frameid);
			}
					if(result.left.set == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(result.left.set.index);
					if(result.left.set.team == null || result.left.set.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.left.set.team.size());
				for(Map.Entry<Integer,IOGeneralBean> result_left_set_team1 : result.left.set.team.entrySet()){
			retMsg.writeInt(result_left_set_team1.getKey());
					retMsg.writeLong(result_left_set_team1.getValue().guid);
					retMsg.writeInt(result_left_set_team1.getValue().gsid);
					retMsg.writeInt(result_left_set_team1.getValue().level);
					retMsg.writeInt(result_left_set_team1.getValue().star);
					retMsg.writeInt(result_left_set_team1.getValue().camp);
					retMsg.writeInt(result_left_set_team1.getValue().occu);
					retMsg.writeInt(result_left_set_team1.getValue().pclass);
					retMsg.writeInt(result_left_set_team1.getValue().power);
					if(result_left_set_team1.getValue().talent == null || result_left_set_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_left_set_team1.getValue().talent.size());
				for(Integer result_left_set_team1_getValue_talent1 : result_left_set_team1.getValue().talent){
			retMsg.writeInt(result_left_set_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(result_left_set_team1.getValue().affairs);
					retMsg.writeInt(result_left_set_team1.getValue().treasure);
					if(result_left_set_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(result_left_set_team1.getValue().property.hp);
					retMsg.writeInt(result_left_set_team1.getValue().property.atk);
					retMsg.writeInt(result_left_set_team1.getValue().property.def);
					retMsg.writeInt(result_left_set_team1.getValue().property.mdef);
					retMsg.writeFloat(result_left_set_team1.getValue().property.atktime);
					retMsg.writeInt(result_left_set_team1.getValue().property.range);
					retMsg.writeInt(result_left_set_team1.getValue().property.msp);
					retMsg.writeInt(result_left_set_team1.getValue().property.pasp);
					retMsg.writeInt(result_left_set_team1.getValue().property.pcri);
					retMsg.writeInt(result_left_set_team1.getValue().property.pcrid);
					retMsg.writeInt(result_left_set_team1.getValue().property.pdam);
					retMsg.writeInt(result_left_set_team1.getValue().property.php);
					retMsg.writeInt(result_left_set_team1.getValue().property.patk);
					retMsg.writeInt(result_left_set_team1.getValue().property.pdef);
					retMsg.writeInt(result_left_set_team1.getValue().property.pmdef);
					retMsg.writeInt(result_left_set_team1.getValue().property.ppbs);
					retMsg.writeInt(result_left_set_team1.getValue().property.pmbs);
					retMsg.writeInt(result_left_set_team1.getValue().property.pefc);
					retMsg.writeInt(result_left_set_team1.getValue().property.ppthr);
					retMsg.writeInt(result_left_set_team1.getValue().property.patkdam);
					retMsg.writeInt(result_left_set_team1.getValue().property.pskidam);
					retMsg.writeInt(result_left_set_team1.getValue().property.pckatk);
					retMsg.writeInt(result_left_set_team1.getValue().property.pmthr);
					retMsg.writeInt(result_left_set_team1.getValue().property.pdex);
					retMsg.writeInt(result_left_set_team1.getValue().property.pmdex);
					retMsg.writeInt(result_left_set_team1.getValue().property.pmsatk);
					retMsg.writeInt(result_left_set_team1.getValue().property.pmps);
					retMsg.writeInt(result_left_set_team1.getValue().property.pcd);
			}
					if(result_left_set_team1.getValue().equip == null || result_left_set_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_left_set_team1.getValue().equip.size());
				for(Integer result_left_set_team1_getValue_equip1 : result_left_set_team1.getValue().equip){
			retMsg.writeInt(result_left_set_team1_getValue_equip1);
				}
			}
					if(result_left_set_team1.getValue().skill == null || result_left_set_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_left_set_team1.getValue().skill.size());
				for(Integer result_left_set_team1_getValue_skill1 : result_left_set_team1.getValue().skill){
			retMsg.writeInt(result_left_set_team1_getValue_skill1);
				}
			}
					if(result_left_set_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(result_left_set_team1.getValue().exclusive.level);
					if(result_left_set_team1.getValue().exclusive.skill == null || result_left_set_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_left_set_team1.getValue().exclusive.skill.size());
				for(Integer result_left_set_team1_getValue_exclusive_skill1 : result_left_set_team1.getValue().exclusive.skill){
			retMsg.writeInt(result_left_set_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(result_left_set_team1.getValue().exclusive.gsid);
					if(result_left_set_team1.getValue().exclusive.property == null || result_left_set_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_left_set_team1.getValue().exclusive.property.size());
				for(KvStringPair result_left_set_team1_getValue_exclusive_property1 : result_left_set_team1.getValue().exclusive.property){
					retMsg.writeString(result_left_set_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(result_left_set_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(result_left_set_team1.getValue().hppercent);
				}
			}
			}
			}
					if(result.right == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(result.right.info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(result.right.info.rname);
					retMsg.writeInt(result.right.info.level);
					retMsg.writeInt(result.right.info.iconid);
					retMsg.writeInt(result.right.info.headid);
					retMsg.writeInt(result.right.info.frameid);
			}
					if(result.right.set == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(result.right.set.index);
					if(result.right.set.team == null || result.right.set.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.right.set.team.size());
				for(Map.Entry<Integer,IOGeneralBean> result_right_set_team1 : result.right.set.team.entrySet()){
			retMsg.writeInt(result_right_set_team1.getKey());
					retMsg.writeLong(result_right_set_team1.getValue().guid);
					retMsg.writeInt(result_right_set_team1.getValue().gsid);
					retMsg.writeInt(result_right_set_team1.getValue().level);
					retMsg.writeInt(result_right_set_team1.getValue().star);
					retMsg.writeInt(result_right_set_team1.getValue().camp);
					retMsg.writeInt(result_right_set_team1.getValue().occu);
					retMsg.writeInt(result_right_set_team1.getValue().pclass);
					retMsg.writeInt(result_right_set_team1.getValue().power);
					if(result_right_set_team1.getValue().talent == null || result_right_set_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_right_set_team1.getValue().talent.size());
				for(Integer result_right_set_team1_getValue_talent1 : result_right_set_team1.getValue().talent){
			retMsg.writeInt(result_right_set_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(result_right_set_team1.getValue().affairs);
					retMsg.writeInt(result_right_set_team1.getValue().treasure);
					if(result_right_set_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(result_right_set_team1.getValue().property.hp);
					retMsg.writeInt(result_right_set_team1.getValue().property.atk);
					retMsg.writeInt(result_right_set_team1.getValue().property.def);
					retMsg.writeInt(result_right_set_team1.getValue().property.mdef);
					retMsg.writeFloat(result_right_set_team1.getValue().property.atktime);
					retMsg.writeInt(result_right_set_team1.getValue().property.range);
					retMsg.writeInt(result_right_set_team1.getValue().property.msp);
					retMsg.writeInt(result_right_set_team1.getValue().property.pasp);
					retMsg.writeInt(result_right_set_team1.getValue().property.pcri);
					retMsg.writeInt(result_right_set_team1.getValue().property.pcrid);
					retMsg.writeInt(result_right_set_team1.getValue().property.pdam);
					retMsg.writeInt(result_right_set_team1.getValue().property.php);
					retMsg.writeInt(result_right_set_team1.getValue().property.patk);
					retMsg.writeInt(result_right_set_team1.getValue().property.pdef);
					retMsg.writeInt(result_right_set_team1.getValue().property.pmdef);
					retMsg.writeInt(result_right_set_team1.getValue().property.ppbs);
					retMsg.writeInt(result_right_set_team1.getValue().property.pmbs);
					retMsg.writeInt(result_right_set_team1.getValue().property.pefc);
					retMsg.writeInt(result_right_set_team1.getValue().property.ppthr);
					retMsg.writeInt(result_right_set_team1.getValue().property.patkdam);
					retMsg.writeInt(result_right_set_team1.getValue().property.pskidam);
					retMsg.writeInt(result_right_set_team1.getValue().property.pckatk);
					retMsg.writeInt(result_right_set_team1.getValue().property.pmthr);
					retMsg.writeInt(result_right_set_team1.getValue().property.pdex);
					retMsg.writeInt(result_right_set_team1.getValue().property.pmdex);
					retMsg.writeInt(result_right_set_team1.getValue().property.pmsatk);
					retMsg.writeInt(result_right_set_team1.getValue().property.pmps);
					retMsg.writeInt(result_right_set_team1.getValue().property.pcd);
			}
					if(result_right_set_team1.getValue().equip == null || result_right_set_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_right_set_team1.getValue().equip.size());
				for(Integer result_right_set_team1_getValue_equip1 : result_right_set_team1.getValue().equip){
			retMsg.writeInt(result_right_set_team1_getValue_equip1);
				}
			}
					if(result_right_set_team1.getValue().skill == null || result_right_set_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_right_set_team1.getValue().skill.size());
				for(Integer result_right_set_team1_getValue_skill1 : result_right_set_team1.getValue().skill){
			retMsg.writeInt(result_right_set_team1_getValue_skill1);
				}
			}
					if(result_right_set_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(result_right_set_team1.getValue().exclusive.level);
					if(result_right_set_team1.getValue().exclusive.skill == null || result_right_set_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_right_set_team1.getValue().exclusive.skill.size());
				for(Integer result_right_set_team1_getValue_exclusive_skill1 : result_right_set_team1.getValue().exclusive.skill){
			retMsg.writeInt(result_right_set_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(result_right_set_team1.getValue().exclusive.gsid);
					if(result_right_set_team1.getValue().exclusive.property == null || result_right_set_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_right_set_team1.getValue().exclusive.property.size());
				for(KvStringPair result_right_set_team1_getValue_exclusive_property1 : result_right_set_team1.getValue().exclusive.property){
					retMsg.writeString(result_right_set_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(result_right_set_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(result_right_set_team1.getValue().hppercent);
				}
			}
			}
			}
			}
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(IORewardItem rewards1 : rewards){
					retMsg.writeInt(rewards1.GSID);
					retMsg.writeInt(rewards1.COUNT);
				}
			}
			retMsg.writeInt(times);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SBigBattleFarm{
		private static final Logger logger = LoggerFactory.getLogger(C2SBigBattleFarm.class);
		public int mapid;
		@Override
		public String toString() {
			return "C2SBigBattleFarm [mapid="+mapid+",]";
		}
		public static final int id = 577;

		public static C2SBigBattleFarm parse(MyRequestMessage request){
			C2SBigBattleFarm retObj = new C2SBigBattleFarm();
			try{
			retObj.mapid=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CBigBattleFarm{
		private static final Logger logger = LoggerFactory.getLogger(S2CBigBattleFarm.class);
		public int times;
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2CBigBattleFarm [times="+times+",rewards="+rewards+",]";
		}
		public static final int msgCode = 578;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 578);
			retMsg.writeInt(times);
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
	public static final class C2SMonthBossInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SMonthBossInfo.class);
		public int monthIndex;
		@Override
		public String toString() {
			return "C2SMonthBossInfo [monthIndex="+monthIndex+",]";
		}
		public static final int id = 579;

		public static C2SMonthBossInfo parse(MyRequestMessage request){
			C2SMonthBossInfo retObj = new C2SMonthBossInfo();
			try{
			retObj.monthIndex=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMonthBossInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CMonthBossInfo.class);
		public int index;
		public int general;
		public int level;
		public List<IOGeneralSimple> bset;
		public List<IORewardItem> rewards;
		public long lastdamge;
		public long maxhp;
		public long nowhp;
		@Override
		public String toString() {
			return "S2CMonthBossInfo [index="+index+",general="+general+",level="+level+",bset="+bset+",rewards="+rewards+",lastdamge="+lastdamge+",maxhp="+maxhp+",nowhp="+nowhp+",]";
		}
		public static final int msgCode = 580;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 580);
			retMsg.writeInt(index);
			retMsg.writeInt(general);
			retMsg.writeInt(level);
			if(bset == null || bset.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(bset.size());
				for(IOGeneralSimple bset1 : bset){
					retMsg.writeInt(bset1.pos);
					retMsg.writeInt(bset1.gsid);
					retMsg.writeInt(bset1.level);
					retMsg.writeInt(bset1.hpcover);
					retMsg.writeInt(bset1.pclass);
					retMsg.writeLong(bset1.nowhp);
					retMsg.writeFloat(bset1.exhp);
					retMsg.writeFloat(bset1.exatk);
				}
			}
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(IORewardItem rewards1 : rewards){
					retMsg.writeInt(rewards1.GSID);
					retMsg.writeInt(rewards1.COUNT);
				}
			}
			retMsg.writeLong(lastdamge);
			retMsg.writeLong(maxhp);
			retMsg.writeLong(nowhp);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMonthBossBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SMonthBossBattleStart.class);
		public int index;
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SMonthBossBattleStart [index="+index+",mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 581;

		public static C2SMonthBossBattleStart parse(MyRequestMessage request){
			C2SMonthBossBattleStart retObj = new C2SMonthBossBattleStart();
			try{
			retObj.index=request.readInt();
			retObj.mythic=request.readInt();
			int items_size = request.readInt();
				retObj.items = new IOFormationGeneralPos[items_size];
				for(int i=0;i<items_size;i++){
						retObj.items[i] = new IOFormationGeneralPos();
					retObj.items[i].pos=request.readInt();
					retObj.items[i].general_uuid=request.readLong();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMonthBossBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CMonthBossBattleStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CMonthBossBattleStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CMonthBossBattleStart(){}
		@Override
		public String toString() {
			return "S2CMonthBossBattleStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 582;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 582);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMonthBossBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SMonthBossBattleEnd.class);
		public int monthIndex;
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SMonthBossBattleEnd [monthIndex="+monthIndex+",battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 583;

		public static C2SMonthBossBattleEnd parse(MyRequestMessage request){
			C2SMonthBossBattleEnd retObj = new C2SMonthBossBattleEnd();
			try{
			retObj.monthIndex=request.readInt();
			retObj.battleid=request.readLong();
			int as_size = request.readInt();
				retObj.as = new IOBHurt[as_size];
				for(int i=0;i<as_size;i++){
						retObj.as[i] = new IOBHurt();
					retObj.as[i].gsid=request.readInt();
					retObj.as[i].hurm=request.readLong();
					retObj.as[i].heal=request.readLong();
					retObj.as[i].hp=request.readLong();
					retObj.as[i].born=request.readInt();
					retObj.as[i].hpperc=request.readInt();
					retObj.as[i].hpmax=request.readLong();
				}
			int df_size = request.readInt();
				retObj.df = new IOBHurt[df_size];
				for(int i=0;i<df_size;i++){
						retObj.df[i] = new IOBHurt();
					retObj.df[i].gsid=request.readInt();
					retObj.df[i].hurm=request.readLong();
					retObj.df[i].heal=request.readLong();
					retObj.df[i].hp=request.readLong();
					retObj.df[i].born=request.readInt();
					retObj.df[i].hpperc=request.readInt();
					retObj.df[i].hpmax=request.readLong();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMonthBossBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CMonthBossBattleEnd.class);
		public IOBattleResult result;
		public List<IORewardItem> items;
		public long damge;
		public int kill;
		@Override
		public String toString() {
			return "S2CMonthBossBattleEnd [result="+result+",items="+items+",damge="+damge+",kill="+kill+",]";
		}
		public static final int msgCode = 584;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 584);
			if(result == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(result.ret);
					retMsg.writeInt(result.round);
					if(result.lhp == null || result.lhp.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.lhp.size());
				for(Map.Entry<Integer,Long> result_lhp1 : result.lhp.entrySet()){
			retMsg.writeInt(result_lhp1.getKey());
			retMsg.writeLong(result_lhp1.getValue());
				}
			}
					if(result.rhp == null || result.rhp.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.rhp.size());
				for(Map.Entry<Integer,Long> result_rhp1 : result.rhp.entrySet()){
			retMsg.writeInt(result_rhp1.getKey());
			retMsg.writeLong(result_rhp1.getValue());
				}
			}
					if(result.lper == null || result.lper.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.lper.size());
				for(Map.Entry<Integer,Integer> result_lper1 : result.lper.entrySet()){
			retMsg.writeInt(result_lper1.getKey());
			retMsg.writeInt(result_lper1.getValue());
				}
			}
					if(result.rper == null || result.rper.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.rper.size());
				for(Map.Entry<Integer,Integer> result_rper1 : result.rper.entrySet()){
			retMsg.writeInt(result_rper1.getKey());
			retMsg.writeInt(result_rper1.getValue());
				}
			}
					retMsg.writeInt(result.ltper);
					retMsg.writeInt(result.rtper);
					retMsg.writeInt(result.rlosthp);
					if(result.report == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(result.report.left == null || result.report.left.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.report.left.size());
				for(IOBattleReportItem result_report_left1 : result.report.left){
					retMsg.writeInt(result_report_left1.gsid);
					retMsg.writeLong(result_report_left1.hurm);
					retMsg.writeLong(result_report_left1.heal);
					retMsg.writeInt(result_report_left1.level);
					retMsg.writeInt(result_report_left1.pclass);
				}
			}
					if(result.report.right == null || result.report.right.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.report.right.size());
				for(IOBattleReportItem result_report_right1 : result.report.right){
					retMsg.writeInt(result_report_right1.gsid);
					retMsg.writeLong(result_report_right1.hurm);
					retMsg.writeLong(result_report_right1.heal);
					retMsg.writeInt(result_report_right1.level);
					retMsg.writeInt(result_report_right1.pclass);
				}
			}
			}
					retMsg.writeLong(result.version);
					if(result.left == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(result.left.info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(result.left.info.rname);
					retMsg.writeInt(result.left.info.level);
					retMsg.writeInt(result.left.info.iconid);
					retMsg.writeInt(result.left.info.headid);
					retMsg.writeInt(result.left.info.frameid);
			}
					if(result.left.set == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(result.left.set.index);
					if(result.left.set.team == null || result.left.set.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.left.set.team.size());
				for(Map.Entry<Integer,IOGeneralBean> result_left_set_team1 : result.left.set.team.entrySet()){
			retMsg.writeInt(result_left_set_team1.getKey());
					retMsg.writeLong(result_left_set_team1.getValue().guid);
					retMsg.writeInt(result_left_set_team1.getValue().gsid);
					retMsg.writeInt(result_left_set_team1.getValue().level);
					retMsg.writeInt(result_left_set_team1.getValue().star);
					retMsg.writeInt(result_left_set_team1.getValue().camp);
					retMsg.writeInt(result_left_set_team1.getValue().occu);
					retMsg.writeInt(result_left_set_team1.getValue().pclass);
					retMsg.writeInt(result_left_set_team1.getValue().power);
					if(result_left_set_team1.getValue().talent == null || result_left_set_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_left_set_team1.getValue().talent.size());
				for(Integer result_left_set_team1_getValue_talent1 : result_left_set_team1.getValue().talent){
			retMsg.writeInt(result_left_set_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(result_left_set_team1.getValue().affairs);
					retMsg.writeInt(result_left_set_team1.getValue().treasure);
					if(result_left_set_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(result_left_set_team1.getValue().property.hp);
					retMsg.writeInt(result_left_set_team1.getValue().property.atk);
					retMsg.writeInt(result_left_set_team1.getValue().property.def);
					retMsg.writeInt(result_left_set_team1.getValue().property.mdef);
					retMsg.writeFloat(result_left_set_team1.getValue().property.atktime);
					retMsg.writeInt(result_left_set_team1.getValue().property.range);
					retMsg.writeInt(result_left_set_team1.getValue().property.msp);
					retMsg.writeInt(result_left_set_team1.getValue().property.pasp);
					retMsg.writeInt(result_left_set_team1.getValue().property.pcri);
					retMsg.writeInt(result_left_set_team1.getValue().property.pcrid);
					retMsg.writeInt(result_left_set_team1.getValue().property.pdam);
					retMsg.writeInt(result_left_set_team1.getValue().property.php);
					retMsg.writeInt(result_left_set_team1.getValue().property.patk);
					retMsg.writeInt(result_left_set_team1.getValue().property.pdef);
					retMsg.writeInt(result_left_set_team1.getValue().property.pmdef);
					retMsg.writeInt(result_left_set_team1.getValue().property.ppbs);
					retMsg.writeInt(result_left_set_team1.getValue().property.pmbs);
					retMsg.writeInt(result_left_set_team1.getValue().property.pefc);
					retMsg.writeInt(result_left_set_team1.getValue().property.ppthr);
					retMsg.writeInt(result_left_set_team1.getValue().property.patkdam);
					retMsg.writeInt(result_left_set_team1.getValue().property.pskidam);
					retMsg.writeInt(result_left_set_team1.getValue().property.pckatk);
					retMsg.writeInt(result_left_set_team1.getValue().property.pmthr);
					retMsg.writeInt(result_left_set_team1.getValue().property.pdex);
					retMsg.writeInt(result_left_set_team1.getValue().property.pmdex);
					retMsg.writeInt(result_left_set_team1.getValue().property.pmsatk);
					retMsg.writeInt(result_left_set_team1.getValue().property.pmps);
					retMsg.writeInt(result_left_set_team1.getValue().property.pcd);
			}
					if(result_left_set_team1.getValue().equip == null || result_left_set_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_left_set_team1.getValue().equip.size());
				for(Integer result_left_set_team1_getValue_equip1 : result_left_set_team1.getValue().equip){
			retMsg.writeInt(result_left_set_team1_getValue_equip1);
				}
			}
					if(result_left_set_team1.getValue().skill == null || result_left_set_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_left_set_team1.getValue().skill.size());
				for(Integer result_left_set_team1_getValue_skill1 : result_left_set_team1.getValue().skill){
			retMsg.writeInt(result_left_set_team1_getValue_skill1);
				}
			}
					if(result_left_set_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(result_left_set_team1.getValue().exclusive.level);
					if(result_left_set_team1.getValue().exclusive.skill == null || result_left_set_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_left_set_team1.getValue().exclusive.skill.size());
				for(Integer result_left_set_team1_getValue_exclusive_skill1 : result_left_set_team1.getValue().exclusive.skill){
			retMsg.writeInt(result_left_set_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(result_left_set_team1.getValue().exclusive.gsid);
					if(result_left_set_team1.getValue().exclusive.property == null || result_left_set_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_left_set_team1.getValue().exclusive.property.size());
				for(KvStringPair result_left_set_team1_getValue_exclusive_property1 : result_left_set_team1.getValue().exclusive.property){
					retMsg.writeString(result_left_set_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(result_left_set_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(result_left_set_team1.getValue().hppercent);
				}
			}
			}
			}
					if(result.right == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(result.right.info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(result.right.info.rname);
					retMsg.writeInt(result.right.info.level);
					retMsg.writeInt(result.right.info.iconid);
					retMsg.writeInt(result.right.info.headid);
					retMsg.writeInt(result.right.info.frameid);
			}
					if(result.right.set == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(result.right.set.index);
					if(result.right.set.team == null || result.right.set.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result.right.set.team.size());
				for(Map.Entry<Integer,IOGeneralBean> result_right_set_team1 : result.right.set.team.entrySet()){
			retMsg.writeInt(result_right_set_team1.getKey());
					retMsg.writeLong(result_right_set_team1.getValue().guid);
					retMsg.writeInt(result_right_set_team1.getValue().gsid);
					retMsg.writeInt(result_right_set_team1.getValue().level);
					retMsg.writeInt(result_right_set_team1.getValue().star);
					retMsg.writeInt(result_right_set_team1.getValue().camp);
					retMsg.writeInt(result_right_set_team1.getValue().occu);
					retMsg.writeInt(result_right_set_team1.getValue().pclass);
					retMsg.writeInt(result_right_set_team1.getValue().power);
					if(result_right_set_team1.getValue().talent == null || result_right_set_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_right_set_team1.getValue().talent.size());
				for(Integer result_right_set_team1_getValue_talent1 : result_right_set_team1.getValue().talent){
			retMsg.writeInt(result_right_set_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(result_right_set_team1.getValue().affairs);
					retMsg.writeInt(result_right_set_team1.getValue().treasure);
					if(result_right_set_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(result_right_set_team1.getValue().property.hp);
					retMsg.writeInt(result_right_set_team1.getValue().property.atk);
					retMsg.writeInt(result_right_set_team1.getValue().property.def);
					retMsg.writeInt(result_right_set_team1.getValue().property.mdef);
					retMsg.writeFloat(result_right_set_team1.getValue().property.atktime);
					retMsg.writeInt(result_right_set_team1.getValue().property.range);
					retMsg.writeInt(result_right_set_team1.getValue().property.msp);
					retMsg.writeInt(result_right_set_team1.getValue().property.pasp);
					retMsg.writeInt(result_right_set_team1.getValue().property.pcri);
					retMsg.writeInt(result_right_set_team1.getValue().property.pcrid);
					retMsg.writeInt(result_right_set_team1.getValue().property.pdam);
					retMsg.writeInt(result_right_set_team1.getValue().property.php);
					retMsg.writeInt(result_right_set_team1.getValue().property.patk);
					retMsg.writeInt(result_right_set_team1.getValue().property.pdef);
					retMsg.writeInt(result_right_set_team1.getValue().property.pmdef);
					retMsg.writeInt(result_right_set_team1.getValue().property.ppbs);
					retMsg.writeInt(result_right_set_team1.getValue().property.pmbs);
					retMsg.writeInt(result_right_set_team1.getValue().property.pefc);
					retMsg.writeInt(result_right_set_team1.getValue().property.ppthr);
					retMsg.writeInt(result_right_set_team1.getValue().property.patkdam);
					retMsg.writeInt(result_right_set_team1.getValue().property.pskidam);
					retMsg.writeInt(result_right_set_team1.getValue().property.pckatk);
					retMsg.writeInt(result_right_set_team1.getValue().property.pmthr);
					retMsg.writeInt(result_right_set_team1.getValue().property.pdex);
					retMsg.writeInt(result_right_set_team1.getValue().property.pmdex);
					retMsg.writeInt(result_right_set_team1.getValue().property.pmsatk);
					retMsg.writeInt(result_right_set_team1.getValue().property.pmps);
					retMsg.writeInt(result_right_set_team1.getValue().property.pcd);
			}
					if(result_right_set_team1.getValue().equip == null || result_right_set_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_right_set_team1.getValue().equip.size());
				for(Integer result_right_set_team1_getValue_equip1 : result_right_set_team1.getValue().equip){
			retMsg.writeInt(result_right_set_team1_getValue_equip1);
				}
			}
					if(result_right_set_team1.getValue().skill == null || result_right_set_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_right_set_team1.getValue().skill.size());
				for(Integer result_right_set_team1_getValue_skill1 : result_right_set_team1.getValue().skill){
			retMsg.writeInt(result_right_set_team1_getValue_skill1);
				}
			}
					if(result_right_set_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(result_right_set_team1.getValue().exclusive.level);
					if(result_right_set_team1.getValue().exclusive.skill == null || result_right_set_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_right_set_team1.getValue().exclusive.skill.size());
				for(Integer result_right_set_team1_getValue_exclusive_skill1 : result_right_set_team1.getValue().exclusive.skill){
			retMsg.writeInt(result_right_set_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(result_right_set_team1.getValue().exclusive.gsid);
					if(result_right_set_team1.getValue().exclusive.property == null || result_right_set_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(result_right_set_team1.getValue().exclusive.property.size());
				for(KvStringPair result_right_set_team1_getValue_exclusive_property1 : result_right_set_team1.getValue().exclusive.property){
					retMsg.writeString(result_right_set_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(result_right_set_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(result_right_set_team1.getValue().hppercent);
				}
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
			retMsg.writeLong(damge);
			retMsg.writeInt(kill);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMonthBossFarm{
		private static final Logger logger = LoggerFactory.getLogger(C2SMonthBossFarm.class);
		public int monthIndex;
		public int times;
		@Override
		public String toString() {
			return "C2SMonthBossFarm [monthIndex="+monthIndex+",times="+times+",]";
		}
		public static final int id = 585;

		public static C2SMonthBossFarm parse(MyRequestMessage request){
			C2SMonthBossFarm retObj = new C2SMonthBossFarm();
			try{
			retObj.monthIndex=request.readInt();
			retObj.times=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMonthBossFarm{
		private static final Logger logger = LoggerFactory.getLogger(S2CMonthBossFarm.class);
		public List<IORewardItem> items;
		@Override
		public String toString() {
			return "S2CMonthBossFarm [items="+items+",]";
		}
		public static final int msgCode = 586;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 586);
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
}
