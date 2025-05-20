package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.Map;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOBHurt;
import ws.WsMessageBase.IORewardItem;
import ws.WsMessageBase.IOBattleResult;
import ws.WsMessageBase.IOBattleRetSide;
import ws.WsMessageBase.IOBattleRetSet;
import ws.WsMessageBase.IOBattleReport;
import ws.WsMessageBase.IOBattleReportItem;
import ws.WsMessageBase.IOFormationGeneralPos;
import ws.WsMessageBase.IORewardItemSelect;
import ws.WsMessageBase.IOGeneralSimple;
import ws.WsMessageBase.IOExpedPlayer;
import ws.WsMessageBase.IOBattlesetEnemy;
import ws.WsMessageBase.IOMythicalAnimal;
import ws.WsMessageBase.IOGeneralBean;
import ws.WsMessageBase.IOProperty;
import ws.WsMessageBase.IOExclusive;
import ws.WsMessageBase.KvStringPair;
import ws.WsMessageBase.IOManorBoss;
import ws.WsMessageBase.IOMapEvent;
import ws.WsMessageBase.IOPvpRecord;
import ws.WsMessageBase.IOBattleRecordSeason;
import ws.WsMessageBase.IOBattleRecordSide;
import ws.WsMessageBase.IOBattleRecordSet;
import ws.WsMessageBase.IOBattleRecordInfo;
import ws.WsMessageBase.IOStageInfo;
import ws.WsMessageBase.GuozhanOfficePointPlayer;

public final class WsMessageBattle{
	public static final class C2SChapterInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SChapterInfo.class);
		public static final int id = 501;
	}
	public static final class S2CChapterInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CChapterInfo.class);
		public List<IOGeneralSimple> items;
		@Override
		public String toString() {
			return "S2CChapterInfo [items="+items+",]";
		}
		public static final int msgCode = 502;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 502);
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
	public static final class C2SChapterReap{
		private static final Logger logger = LoggerFactory.getLogger(C2SChapterReap.class);
		public static final int id = 503;
	}
	public static final class S2CChapterReap{
		private static final Logger logger = LoggerFactory.getLogger(S2CChapterReap.class);
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2CChapterReap [rewards="+rewards+",]";
		}
		public static final int msgCode = 504;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 504);
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
	public static final class C2SChapterChange{
		private static final Logger logger = LoggerFactory.getLogger(C2SChapterChange.class);
		public int mapid;
		@Override
		public String toString() {
			return "C2SChapterChange [mapid="+mapid+",]";
		}
		public static final int id = 505;

		public static C2SChapterChange parse(MyRequestMessage request){
			C2SChapterChange retObj = new C2SChapterChange();
			try{
			retObj.mapid=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CChapterChange{
		private static final Logger logger = LoggerFactory.getLogger(S2CChapterChange.class);
		public int mapid;
		public S2CChapterChange(int pmapid){
			mapid=pmapid;
		}
		public S2CChapterChange(){}
		@Override
		public String toString() {
			return "S2CChapterChange [mapid="+mapid+",]";
		}
		public static final int msgCode = 506;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 506);
			retMsg.writeInt(mapid);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SChapterBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SChapterBattleStart.class);
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SChapterBattleStart [mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 507;

		public static C2SChapterBattleStart parse(MyRequestMessage request){
			C2SChapterBattleStart retObj = new C2SChapterBattleStart();
			try{
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
	public static final class S2CChapterBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CChapterBattleStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CChapterBattleStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CChapterBattleStart(){}
		@Override
		public String toString() {
			return "S2CChapterBattleStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 508;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 508);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SChapterBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SChapterBattleEnd.class);
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SChapterBattleEnd [battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 509;

		public static C2SChapterBattleEnd parse(MyRequestMessage request){
			C2SChapterBattleEnd retObj = new C2SChapterBattleEnd();
			try{
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
	public static final class S2CChapterBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CChapterBattleEnd.class);
		public IOBattleResult result;
		public List<IORewardItem> items;
		@Override
		public String toString() {
			return "S2CChapterBattleEnd [result="+result+",items="+items+",]";
		}
		public static final int msgCode = 510;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 510);
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
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SBattleFormationSave{
		private static final Logger logger = LoggerFactory.getLogger(C2SBattleFormationSave.class);
		public String formation_name;
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SBattleFormationSave [formation_name="+formation_name+",mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 511;

		public static C2SBattleFormationSave parse(MyRequestMessage request){
			C2SBattleFormationSave retObj = new C2SBattleFormationSave();
			try{
			retObj.formation_name=request.readString();
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
	public static final class S2CBattleFormationSave{
		private static final Logger logger = LoggerFactory.getLogger(S2CBattleFormationSave.class);
		public static final int msgCode = 512;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 512);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SPvpBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SPvpBattleStart.class);
		public int pvp_index;
		public int role_id;
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SPvpBattleStart [pvp_index="+pvp_index+",role_id="+role_id+",mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 513;

		public static C2SPvpBattleStart parse(MyRequestMessage request){
			C2SPvpBattleStart retObj = new C2SPvpBattleStart();
			try{
			retObj.pvp_index=request.readInt();
			retObj.role_id=request.readInt();
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
	public static final class S2CPvpBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CPvpBattleStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CPvpBattleStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CPvpBattleStart(){}
		@Override
		public String toString() {
			return "S2CPvpBattleStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 514;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 514);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SPvpBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SPvpBattleEnd.class);
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SPvpBattleEnd [battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 515;

		public static C2SPvpBattleEnd parse(MyRequestMessage request){
			C2SPvpBattleEnd retObj = new C2SPvpBattleEnd();
			try{
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
	public static final class S2CPvpBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CPvpBattleEnd.class);
		public IOBattleResult result;
		public List<IORewardItemSelect> reward;
		public int spoints;
		public int schange;
		public int epoints;
		public int echange;
		public long videoid;
		@Override
		public String toString() {
			return "S2CPvpBattleEnd [result="+result+",reward="+reward+",spoints="+spoints+",schange="+schange+",epoints="+epoints+",echange="+echange+",videoid="+videoid+",]";
		}
		public static final int msgCode = 516;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 516);
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
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(IORewardItemSelect reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
					retMsg.writeBool(reward1.real);
				}
			}
			retMsg.writeInt(spoints);
			retMsg.writeInt(schange);
			retMsg.writeInt(epoints);
			retMsg.writeInt(echange);
			retMsg.writeLong(videoid);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STowerBattleInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2STowerBattleInfo.class);
		public static final int id = 517;
	}
	public static final class S2CTowerBattleInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CTowerBattleInfo.class);
		public List<IOGeneralSimple> items;
		@Override
		public String toString() {
			return "S2CTowerBattleInfo [items="+items+",]";
		}
		public static final int msgCode = 518;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 518);
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
	public static final class C2STowerBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2STowerBattleStart.class);
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2STowerBattleStart [mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 519;

		public static C2STowerBattleStart parse(MyRequestMessage request){
			C2STowerBattleStart retObj = new C2STowerBattleStart();
			try{
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
	public static final class S2CTowerBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CTowerBattleStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CTowerBattleStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CTowerBattleStart(){}
		@Override
		public String toString() {
			return "S2CTowerBattleStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 520;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 520);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STowerBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2STowerBattleEnd.class);
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2STowerBattleEnd [battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 521;

		public static C2STowerBattleEnd parse(MyRequestMessage request){
			C2STowerBattleEnd retObj = new C2STowerBattleEnd();
			try{
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
	public static final class S2CTowerBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CTowerBattleEnd.class);
		public IOBattleResult result;
		public List<IORewardItem> items;
		public long videoid;
		@Override
		public String toString() {
			return "S2CTowerBattleEnd [result="+result+",items="+items+",videoid="+videoid+",]";
		}
		public static final int msgCode = 522;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 522);
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
			retMsg.writeLong(videoid);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STowerAchieveReward{
		private static final Logger logger = LoggerFactory.getLogger(C2STowerAchieveReward.class);
		public static final int id = 523;
	}
	public static final class S2CTowerAchieveReward{
		private static final Logger logger = LoggerFactory.getLogger(S2CTowerAchieveReward.class);
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2CTowerAchieveReward [rewards="+rewards+",]";
		}
		public static final int msgCode = 524;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 524);
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
	public static final class C2SExpedBattleInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SExpedBattleInfo.class);
		public static final int id = 525;
	}
	public static final class S2CExpedBattleInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CExpedBattleInfo.class);
		public long mapkey;
		public Map<Long,Integer> hp;
		public List<Integer> wish;
		public IOExpedPlayer map;
		@Override
		public String toString() {
			return "S2CExpedBattleInfo [mapkey="+mapkey+",hp="+hp+",wish="+wish+",map="+map+",]";
		}
		public static final int msgCode = 526;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 526);
			retMsg.writeLong(mapkey);
			if(hp == null || hp.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(hp.size());
				for(Map.Entry<Long,Integer> hp1 : hp.entrySet()){
			retMsg.writeLong(hp1.getKey());
			retMsg.writeInt(hp1.getValue());
				}
			}
			if(wish == null || wish.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(wish.size());
				for(Integer wish1 : wish){
			retMsg.writeInt(wish1);
				}
			}
			if(map == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(map.rname);
					retMsg.writeInt(map.level);
					retMsg.writeInt(map.iconid);
					retMsg.writeInt(map.headid);
					retMsg.writeInt(map.frameid);
					retMsg.writeInt(map.power);
					if(map.battleset == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(map.battleset.mythic == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(map.battleset.mythic.tid);
					retMsg.writeInt(map.battleset.mythic.pclass);
					retMsg.writeInt(map.battleset.mythic.level);
					if(map.battleset.mythic.pskill == null || map.battleset.mythic.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(map.battleset.mythic.pskill.size());
				for(Integer map_battleset_mythic_pskill1 : map.battleset.mythic.pskill){
			retMsg.writeInt(map_battleset_mythic_pskill1);
				}
			}
			}
					if(map.battleset.team == null || map.battleset.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(map.battleset.team.size());
				for(Map.Entry<Integer,IOGeneralBean> map_battleset_team1 : map.battleset.team.entrySet()){
			retMsg.writeInt(map_battleset_team1.getKey());
					retMsg.writeLong(map_battleset_team1.getValue().guid);
					retMsg.writeInt(map_battleset_team1.getValue().gsid);
					retMsg.writeInt(map_battleset_team1.getValue().level);
					retMsg.writeInt(map_battleset_team1.getValue().star);
					retMsg.writeInt(map_battleset_team1.getValue().camp);
					retMsg.writeInt(map_battleset_team1.getValue().occu);
					retMsg.writeInt(map_battleset_team1.getValue().pclass);
					retMsg.writeInt(map_battleset_team1.getValue().power);
					if(map_battleset_team1.getValue().talent == null || map_battleset_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(map_battleset_team1.getValue().talent.size());
				for(Integer map_battleset_team1_getValue_talent1 : map_battleset_team1.getValue().talent){
			retMsg.writeInt(map_battleset_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(map_battleset_team1.getValue().affairs);
					retMsg.writeInt(map_battleset_team1.getValue().treasure);
					if(map_battleset_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(map_battleset_team1.getValue().property.hp);
					retMsg.writeInt(map_battleset_team1.getValue().property.atk);
					retMsg.writeInt(map_battleset_team1.getValue().property.def);
					retMsg.writeInt(map_battleset_team1.getValue().property.mdef);
					retMsg.writeFloat(map_battleset_team1.getValue().property.atktime);
					retMsg.writeInt(map_battleset_team1.getValue().property.range);
					retMsg.writeInt(map_battleset_team1.getValue().property.msp);
					retMsg.writeInt(map_battleset_team1.getValue().property.pasp);
					retMsg.writeInt(map_battleset_team1.getValue().property.pcri);
					retMsg.writeInt(map_battleset_team1.getValue().property.pcrid);
					retMsg.writeInt(map_battleset_team1.getValue().property.pdam);
					retMsg.writeInt(map_battleset_team1.getValue().property.php);
					retMsg.writeInt(map_battleset_team1.getValue().property.patk);
					retMsg.writeInt(map_battleset_team1.getValue().property.pdef);
					retMsg.writeInt(map_battleset_team1.getValue().property.pmdef);
					retMsg.writeInt(map_battleset_team1.getValue().property.ppbs);
					retMsg.writeInt(map_battleset_team1.getValue().property.pmbs);
					retMsg.writeInt(map_battleset_team1.getValue().property.pefc);
					retMsg.writeInt(map_battleset_team1.getValue().property.ppthr);
					retMsg.writeInt(map_battleset_team1.getValue().property.patkdam);
					retMsg.writeInt(map_battleset_team1.getValue().property.pskidam);
					retMsg.writeInt(map_battleset_team1.getValue().property.pckatk);
					retMsg.writeInt(map_battleset_team1.getValue().property.pmthr);
					retMsg.writeInt(map_battleset_team1.getValue().property.pdex);
					retMsg.writeInt(map_battleset_team1.getValue().property.pmdex);
					retMsg.writeInt(map_battleset_team1.getValue().property.pmsatk);
					retMsg.writeInt(map_battleset_team1.getValue().property.pmps);
					retMsg.writeInt(map_battleset_team1.getValue().property.pcd);
			}
					if(map_battleset_team1.getValue().equip == null || map_battleset_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(map_battleset_team1.getValue().equip.size());
				for(Integer map_battleset_team1_getValue_equip1 : map_battleset_team1.getValue().equip){
			retMsg.writeInt(map_battleset_team1_getValue_equip1);
				}
			}
					if(map_battleset_team1.getValue().skill == null || map_battleset_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(map_battleset_team1.getValue().skill.size());
				for(Integer map_battleset_team1_getValue_skill1 : map_battleset_team1.getValue().skill){
			retMsg.writeInt(map_battleset_team1_getValue_skill1);
				}
			}
					if(map_battleset_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(map_battleset_team1.getValue().exclusive.level);
					if(map_battleset_team1.getValue().exclusive.skill == null || map_battleset_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(map_battleset_team1.getValue().exclusive.skill.size());
				for(Integer map_battleset_team1_getValue_exclusive_skill1 : map_battleset_team1.getValue().exclusive.skill){
			retMsg.writeInt(map_battleset_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(map_battleset_team1.getValue().exclusive.gsid);
					if(map_battleset_team1.getValue().exclusive.property == null || map_battleset_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(map_battleset_team1.getValue().exclusive.property.size());
				for(KvStringPair map_battleset_team1_getValue_exclusive_property1 : map_battleset_team1.getValue().exclusive.property){
					retMsg.writeString(map_battleset_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(map_battleset_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(map_battleset_team1.getValue().hppercent);
				}
			}
			}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SExpedBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SExpedBattleStart.class);
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SExpedBattleStart [mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 527;

		public static C2SExpedBattleStart parse(MyRequestMessage request){
			C2SExpedBattleStart retObj = new C2SExpedBattleStart();
			try{
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
	public static final class S2CExpedBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CExpedBattleStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CExpedBattleStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CExpedBattleStart(){}
		@Override
		public String toString() {
			return "S2CExpedBattleStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 528;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 528);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SExpedBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SExpedBattleEnd.class);
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SExpedBattleEnd [battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 529;

		public static C2SExpedBattleEnd parse(MyRequestMessage request){
			C2SExpedBattleEnd retObj = new C2SExpedBattleEnd();
			try{
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
	public static final class S2CExpedBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CExpedBattleEnd.class);
		public IOBattleResult result;
		public List<IORewardItem> drop;
		public List<IORewardItemSelect> reward;
		@Override
		public String toString() {
			return "S2CExpedBattleEnd [result="+result+",drop="+drop+",reward="+reward+",]";
		}
		public static final int msgCode = 530;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 530);
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
			if(drop == null || drop.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(drop.size());
				for(IORewardItem drop1 : drop){
					retMsg.writeInt(drop1.GSID);
					retMsg.writeInt(drop1.COUNT);
				}
			}
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(IORewardItemSelect reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
					retMsg.writeBool(reward1.real);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SExpedStatue{
		private static final Logger logger = LoggerFactory.getLogger(C2SExpedStatue.class);
		public int type;
		public long general_uuid;
		@Override
		public String toString() {
			return "C2SExpedStatue [type="+type+",general_uuid="+general_uuid+",]";
		}
		public static final int id = 531;

		public static C2SExpedStatue parse(MyRequestMessage request){
			C2SExpedStatue retObj = new C2SExpedStatue();
			try{
			retObj.type=request.readInt();
			retObj.general_uuid=request.readLong();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CExpedStatue{
		private static final Logger logger = LoggerFactory.getLogger(S2CExpedStatue.class);
		public Map<Long,Integer> hp;
		public List<Integer> wish;
		@Override
		public String toString() {
			return "S2CExpedStatue [hp="+hp+",wish="+wish+",]";
		}
		public static final int msgCode = 532;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 532);
			if(hp == null || hp.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(hp.size());
				for(Map.Entry<Long,Integer> hp1 : hp.entrySet()){
			retMsg.writeLong(hp1.getKey());
			retMsg.writeInt(hp1.getValue());
				}
			}
			if(wish == null || wish.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(wish.size());
				for(Integer wish1 : wish){
			retMsg.writeInt(wish1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SManorEnemyInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SManorEnemyInfo.class);
		public int index;
		@Override
		public String toString() {
			return "C2SManorEnemyInfo [index="+index+",]";
		}
		public static final int id = 533;

		public static C2SManorEnemyInfo parse(MyRequestMessage request){
			C2SManorEnemyInfo retObj = new C2SManorEnemyInfo();
			try{
			retObj.index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CManorEnemyInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CManorEnemyInfo.class);
		public IOManorBoss boss;
		public Map<Integer,IOGeneralSimple> enemy;
		@Override
		public String toString() {
			return "S2CManorEnemyInfo [boss="+boss+",enemy="+enemy+",]";
		}
		public static final int msgCode = 534;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 534);
			if(boss == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(boss.bossid);
					retMsg.writeInt(boss.maxhp);
					retMsg.writeInt(boss.lastdamage);
					retMsg.writeInt(boss.nowhp);
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
			if(enemy == null || enemy.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(enemy.size());
				for(Map.Entry<Integer,IOGeneralSimple> enemy1 : enemy.entrySet()){
			retMsg.writeInt(enemy1.getKey());
					retMsg.writeInt(enemy1.getValue().pos);
					retMsg.writeInt(enemy1.getValue().gsid);
					retMsg.writeInt(enemy1.getValue().level);
					retMsg.writeInt(enemy1.getValue().hpcover);
					retMsg.writeInt(enemy1.getValue().pclass);
					retMsg.writeLong(enemy1.getValue().nowhp);
					retMsg.writeFloat(enemy1.getValue().exhp);
					retMsg.writeFloat(enemy1.getValue().exatk);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SManorBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SManorBattleStart.class);
		public int index;
		public int friendid;
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SManorBattleStart [index="+index+",friendid="+friendid+",mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 535;

		public static C2SManorBattleStart parse(MyRequestMessage request){
			C2SManorBattleStart retObj = new C2SManorBattleStart();
			try{
			retObj.index=request.readInt();
			retObj.friendid=request.readInt();
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
	public static final class S2CManorBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CManorBattleStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CManorBattleStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CManorBattleStart(){}
		@Override
		public String toString() {
			return "S2CManorBattleStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 536;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 536);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SManorBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SManorBattleEnd.class);
		public int index;
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SManorBattleEnd [index="+index+",battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 537;

		public static C2SManorBattleEnd parse(MyRequestMessage request){
			C2SManorBattleEnd retObj = new C2SManorBattleEnd();
			try{
			retObj.index=request.readInt();
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
	public static final class S2CManorBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CManorBattleEnd.class);
		public IOBattleResult result;
		public List<IORewardItem> reward;
		public int kill;
		@Override
		public String toString() {
			return "S2CManorBattleEnd [result="+result+",reward="+reward+",kill="+kill+",]";
		}
		public static final int msgCode = 538;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 538);
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
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(IORewardItem reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
				}
			}
			retMsg.writeInt(kill);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SManorYijianFarm{
		private static final Logger logger = LoggerFactory.getLogger(C2SManorYijianFarm.class);
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SManorYijianFarm [mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 539;

		public static C2SManorYijianFarm parse(MyRequestMessage request){
			C2SManorYijianFarm retObj = new C2SManorYijianFarm();
			try{
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
	public static final class S2CManorYijianFarm{
		private static final Logger logger = LoggerFactory.getLogger(S2CManorYijianFarm.class);
		public static final int msgCode = 540;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 540);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMapEventList{
		private static final Logger logger = LoggerFactory.getLogger(C2SMapEventList.class);
		public static final int id = 541;
	}
	public static final class S2CMapEventList{
		private static final Logger logger = LoggerFactory.getLogger(S2CMapEventList.class);
		public Map<Integer,IOMapEvent> list;
		@Override
		public String toString() {
			return "S2CMapEventList [list="+list+",]";
		}
		public static final int msgCode = 542;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 542);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(Map.Entry<Integer,IOMapEvent> list1 : list.entrySet()){
			retMsg.writeInt(list1.getKey());
					retMsg.writeString(list1.getValue().hash);
					retMsg.writeInt(list1.getValue().type);
					retMsg.writeInt(list1.getValue().eid);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMapEventFinish{
		private static final Logger logger = LoggerFactory.getLogger(C2SMapEventFinish.class);
		public int cityId;
		public int option;
		public int giveup;
		@Override
		public String toString() {
			return "C2SMapEventFinish [cityId="+cityId+",option="+option+",giveup="+giveup+",]";
		}
		public static final int id = 543;

		public static C2SMapEventFinish parse(MyRequestMessage request){
			C2SMapEventFinish retObj = new C2SMapEventFinish();
			try{
			retObj.cityId=request.readInt();
			retObj.option=request.readInt();
			retObj.giveup=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMapEventFinish{
		private static final Logger logger = LoggerFactory.getLogger(S2CMapEventFinish.class);
		public int ret;
		public List<IORewardItem> reward;
		@Override
		public String toString() {
			return "S2CMapEventFinish [ret="+ret+",reward="+reward+",]";
		}
		public static final int msgCode = 544;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 544);
			retMsg.writeInt(ret);
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(IORewardItem reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetPvpAgainst{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetPvpAgainst.class);
		public static final int id = 545;
	}
	public static final class S2CGetPvpAgainst{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetPvpAgainst.class);
		public List<Integer> rids;
		@Override
		public String toString() {
			return "S2CGetPvpAgainst [rids="+rids+",]";
		}
		public static final int msgCode = 546;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 546);
			if(rids == null || rids.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rids.size());
				for(Integer rids1 : rids){
			retMsg.writeInt(rids1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SPvpRecords{
		private static final Logger logger = LoggerFactory.getLogger(C2SPvpRecords.class);
		public static final int id = 547;
	}
	public static final class S2CPvpRecords{
		private static final Logger logger = LoggerFactory.getLogger(S2CPvpRecords.class);
		public List<IOPvpRecord> records;
		@Override
		public String toString() {
			return "S2CPvpRecords [records="+records+",]";
		}
		public static final int msgCode = 548;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 548);
			if(records == null || records.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records.size());
				for(IOPvpRecord records1 : records){
					retMsg.writeLong(records1.videoid);
					retMsg.writeLong(records1.time);
					retMsg.writeLong(records1.version);
					retMsg.writeLong(records1.seed);
					retMsg.writeString(records1.result);
					if(records1.season == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1.season.season);
					retMsg.writeInt(records1.season.left);
					retMsg.writeInt(records1.season.right);
					if(records1.season.pos == null || records1.season.pos.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.season.pos.size());
				for(Integer records1_season_pos1 : records1.season.pos){
			retMsg.writeInt(records1_season_pos1);
				}
			}
			}
					if(records1.lper == null || records1.lper.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.lper.size());
				for(Map.Entry<Integer,Integer> records1_lper1 : records1.lper.entrySet()){
			retMsg.writeInt(records1_lper1.getKey());
			retMsg.writeInt(records1_lper1.getValue());
				}
			}
					if(records1.rper == null || records1.rper.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.rper.size());
				for(Map.Entry<Integer,Integer> records1_rper1 : records1.rper.entrySet()){
			retMsg.writeInt(records1_rper1.getKey());
			retMsg.writeInt(records1_rper1.getValue());
				}
			}
					retMsg.writeInt(records1.ltper);
					retMsg.writeInt(records1.rtper);
					if(records1.report == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(records1.report.left == null || records1.report.left.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.report.left.size());
				for(IOBattleReportItem records1_report_left1 : records1.report.left){
					retMsg.writeInt(records1_report_left1.gsid);
					retMsg.writeLong(records1_report_left1.hurm);
					retMsg.writeLong(records1_report_left1.heal);
					retMsg.writeInt(records1_report_left1.level);
					retMsg.writeInt(records1_report_left1.pclass);
				}
			}
					if(records1.report.right == null || records1.report.right.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.report.right.size());
				for(IOBattleReportItem records1_report_right1 : records1.report.right){
					retMsg.writeInt(records1_report_right1.gsid);
					retMsg.writeLong(records1_report_right1.hurm);
					retMsg.writeLong(records1_report_right1.heal);
					retMsg.writeInt(records1_report_right1.level);
					retMsg.writeInt(records1_report_right1.pclass);
				}
			}
			}
					if(records1.left == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(records1.left.info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(records1.left.info.rname);
					retMsg.writeInt(records1.left.info.level);
					retMsg.writeInt(records1.left.info.iconid);
					retMsg.writeInt(records1.left.info.headid);
					retMsg.writeInt(records1.left.info.frameid);
			}
					if(records1.left.set == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(records1.left.set.mythic == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1.left.set.mythic.tid);
					retMsg.writeInt(records1.left.set.mythic.pclass);
					retMsg.writeInt(records1.left.set.mythic.level);
					if(records1.left.set.mythic.pskill == null || records1.left.set.mythic.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.left.set.mythic.pskill.size());
				for(Integer records1_left_set_mythic_pskill1 : records1.left.set.mythic.pskill){
			retMsg.writeInt(records1_left_set_mythic_pskill1);
				}
			}
			}
					if(records1.left.set.team == null || records1.left.set.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.left.set.team.size());
				for(Map.Entry<Integer,IOGeneralBean> records1_left_set_team1 : records1.left.set.team.entrySet()){
			retMsg.writeInt(records1_left_set_team1.getKey());
					retMsg.writeLong(records1_left_set_team1.getValue().guid);
					retMsg.writeInt(records1_left_set_team1.getValue().gsid);
					retMsg.writeInt(records1_left_set_team1.getValue().level);
					retMsg.writeInt(records1_left_set_team1.getValue().star);
					retMsg.writeInt(records1_left_set_team1.getValue().camp);
					retMsg.writeInt(records1_left_set_team1.getValue().occu);
					retMsg.writeInt(records1_left_set_team1.getValue().pclass);
					retMsg.writeInt(records1_left_set_team1.getValue().power);
					if(records1_left_set_team1.getValue().talent == null || records1_left_set_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_left_set_team1.getValue().talent.size());
				for(Integer records1_left_set_team1_getValue_talent1 : records1_left_set_team1.getValue().talent){
			retMsg.writeInt(records1_left_set_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(records1_left_set_team1.getValue().affairs);
					retMsg.writeInt(records1_left_set_team1.getValue().treasure);
					if(records1_left_set_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1_left_set_team1.getValue().property.hp);
					retMsg.writeInt(records1_left_set_team1.getValue().property.atk);
					retMsg.writeInt(records1_left_set_team1.getValue().property.def);
					retMsg.writeInt(records1_left_set_team1.getValue().property.mdef);
					retMsg.writeFloat(records1_left_set_team1.getValue().property.atktime);
					retMsg.writeInt(records1_left_set_team1.getValue().property.range);
					retMsg.writeInt(records1_left_set_team1.getValue().property.msp);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pasp);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pcri);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pcrid);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pdam);
					retMsg.writeInt(records1_left_set_team1.getValue().property.php);
					retMsg.writeInt(records1_left_set_team1.getValue().property.patk);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pdef);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pmdef);
					retMsg.writeInt(records1_left_set_team1.getValue().property.ppbs);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pmbs);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pefc);
					retMsg.writeInt(records1_left_set_team1.getValue().property.ppthr);
					retMsg.writeInt(records1_left_set_team1.getValue().property.patkdam);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pskidam);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pckatk);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pmthr);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pdex);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pmdex);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pmsatk);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pmps);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pcd);
			}
					if(records1_left_set_team1.getValue().equip == null || records1_left_set_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_left_set_team1.getValue().equip.size());
				for(Integer records1_left_set_team1_getValue_equip1 : records1_left_set_team1.getValue().equip){
			retMsg.writeInt(records1_left_set_team1_getValue_equip1);
				}
			}
					if(records1_left_set_team1.getValue().skill == null || records1_left_set_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_left_set_team1.getValue().skill.size());
				for(Integer records1_left_set_team1_getValue_skill1 : records1_left_set_team1.getValue().skill){
			retMsg.writeInt(records1_left_set_team1_getValue_skill1);
				}
			}
					if(records1_left_set_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1_left_set_team1.getValue().exclusive.level);
					if(records1_left_set_team1.getValue().exclusive.skill == null || records1_left_set_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_left_set_team1.getValue().exclusive.skill.size());
				for(Integer records1_left_set_team1_getValue_exclusive_skill1 : records1_left_set_team1.getValue().exclusive.skill){
			retMsg.writeInt(records1_left_set_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(records1_left_set_team1.getValue().exclusive.gsid);
					if(records1_left_set_team1.getValue().exclusive.property == null || records1_left_set_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_left_set_team1.getValue().exclusive.property.size());
				for(KvStringPair records1_left_set_team1_getValue_exclusive_property1 : records1_left_set_team1.getValue().exclusive.property){
					retMsg.writeString(records1_left_set_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(records1_left_set_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(records1_left_set_team1.getValue().hppercent);
				}
			}
			}
			}
					if(records1.right == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(records1.right.info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(records1.right.info.rname);
					retMsg.writeInt(records1.right.info.level);
					retMsg.writeInt(records1.right.info.iconid);
					retMsg.writeInt(records1.right.info.headid);
					retMsg.writeInt(records1.right.info.frameid);
			}
					if(records1.right.set == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(records1.right.set.mythic == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1.right.set.mythic.tid);
					retMsg.writeInt(records1.right.set.mythic.pclass);
					retMsg.writeInt(records1.right.set.mythic.level);
					if(records1.right.set.mythic.pskill == null || records1.right.set.mythic.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.right.set.mythic.pskill.size());
				for(Integer records1_right_set_mythic_pskill1 : records1.right.set.mythic.pskill){
			retMsg.writeInt(records1_right_set_mythic_pskill1);
				}
			}
			}
					if(records1.right.set.team == null || records1.right.set.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.right.set.team.size());
				for(Map.Entry<Integer,IOGeneralBean> records1_right_set_team1 : records1.right.set.team.entrySet()){
			retMsg.writeInt(records1_right_set_team1.getKey());
					retMsg.writeLong(records1_right_set_team1.getValue().guid);
					retMsg.writeInt(records1_right_set_team1.getValue().gsid);
					retMsg.writeInt(records1_right_set_team1.getValue().level);
					retMsg.writeInt(records1_right_set_team1.getValue().star);
					retMsg.writeInt(records1_right_set_team1.getValue().camp);
					retMsg.writeInt(records1_right_set_team1.getValue().occu);
					retMsg.writeInt(records1_right_set_team1.getValue().pclass);
					retMsg.writeInt(records1_right_set_team1.getValue().power);
					if(records1_right_set_team1.getValue().talent == null || records1_right_set_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_right_set_team1.getValue().talent.size());
				for(Integer records1_right_set_team1_getValue_talent1 : records1_right_set_team1.getValue().talent){
			retMsg.writeInt(records1_right_set_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(records1_right_set_team1.getValue().affairs);
					retMsg.writeInt(records1_right_set_team1.getValue().treasure);
					if(records1_right_set_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1_right_set_team1.getValue().property.hp);
					retMsg.writeInt(records1_right_set_team1.getValue().property.atk);
					retMsg.writeInt(records1_right_set_team1.getValue().property.def);
					retMsg.writeInt(records1_right_set_team1.getValue().property.mdef);
					retMsg.writeFloat(records1_right_set_team1.getValue().property.atktime);
					retMsg.writeInt(records1_right_set_team1.getValue().property.range);
					retMsg.writeInt(records1_right_set_team1.getValue().property.msp);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pasp);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pcri);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pcrid);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pdam);
					retMsg.writeInt(records1_right_set_team1.getValue().property.php);
					retMsg.writeInt(records1_right_set_team1.getValue().property.patk);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pdef);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pmdef);
					retMsg.writeInt(records1_right_set_team1.getValue().property.ppbs);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pmbs);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pefc);
					retMsg.writeInt(records1_right_set_team1.getValue().property.ppthr);
					retMsg.writeInt(records1_right_set_team1.getValue().property.patkdam);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pskidam);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pckatk);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pmthr);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pdex);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pmdex);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pmsatk);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pmps);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pcd);
			}
					if(records1_right_set_team1.getValue().equip == null || records1_right_set_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_right_set_team1.getValue().equip.size());
				for(Integer records1_right_set_team1_getValue_equip1 : records1_right_set_team1.getValue().equip){
			retMsg.writeInt(records1_right_set_team1_getValue_equip1);
				}
			}
					if(records1_right_set_team1.getValue().skill == null || records1_right_set_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_right_set_team1.getValue().skill.size());
				for(Integer records1_right_set_team1_getValue_skill1 : records1_right_set_team1.getValue().skill){
			retMsg.writeInt(records1_right_set_team1_getValue_skill1);
				}
			}
					if(records1_right_set_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1_right_set_team1.getValue().exclusive.level);
					if(records1_right_set_team1.getValue().exclusive.skill == null || records1_right_set_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_right_set_team1.getValue().exclusive.skill.size());
				for(Integer records1_right_set_team1_getValue_exclusive_skill1 : records1_right_set_team1.getValue().exclusive.skill){
			retMsg.writeInt(records1_right_set_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(records1_right_set_team1.getValue().exclusive.gsid);
					if(records1_right_set_team1.getValue().exclusive.property == null || records1_right_set_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_right_set_team1.getValue().exclusive.property.size());
				for(KvStringPair records1_right_set_team1_getValue_exclusive_property1 : records1_right_set_team1.getValue().exclusive.property){
					retMsg.writeString(records1_right_set_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(records1_right_set_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(records1_right_set_team1.getValue().hppercent);
				}
			}
			}
			}
					retMsg.writeString(records1.mark);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STowerReplay{
		private static final Logger logger = LoggerFactory.getLogger(C2STowerReplay.class);
		public int tower_level;
		@Override
		public String toString() {
			return "C2STowerReplay [tower_level="+tower_level+",]";
		}
		public static final int id = 549;

		public static C2STowerReplay parse(MyRequestMessage request){
			C2STowerReplay retObj = new C2STowerReplay();
			try{
			retObj.tower_level=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CTowerReplay{
		private static final Logger logger = LoggerFactory.getLogger(S2CTowerReplay.class);
		public List<IOPvpRecord> records;
		@Override
		public String toString() {
			return "S2CTowerReplay [records="+records+",]";
		}
		public static final int msgCode = 550;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 550);
			if(records == null || records.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records.size());
				for(IOPvpRecord records1 : records){
					retMsg.writeLong(records1.videoid);
					retMsg.writeLong(records1.time);
					retMsg.writeLong(records1.version);
					retMsg.writeLong(records1.seed);
					retMsg.writeString(records1.result);
					if(records1.season == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1.season.season);
					retMsg.writeInt(records1.season.left);
					retMsg.writeInt(records1.season.right);
					if(records1.season.pos == null || records1.season.pos.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.season.pos.size());
				for(Integer records1_season_pos1 : records1.season.pos){
			retMsg.writeInt(records1_season_pos1);
				}
			}
			}
					if(records1.lper == null || records1.lper.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.lper.size());
				for(Map.Entry<Integer,Integer> records1_lper1 : records1.lper.entrySet()){
			retMsg.writeInt(records1_lper1.getKey());
			retMsg.writeInt(records1_lper1.getValue());
				}
			}
					if(records1.rper == null || records1.rper.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.rper.size());
				for(Map.Entry<Integer,Integer> records1_rper1 : records1.rper.entrySet()){
			retMsg.writeInt(records1_rper1.getKey());
			retMsg.writeInt(records1_rper1.getValue());
				}
			}
					retMsg.writeInt(records1.ltper);
					retMsg.writeInt(records1.rtper);
					if(records1.report == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(records1.report.left == null || records1.report.left.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.report.left.size());
				for(IOBattleReportItem records1_report_left1 : records1.report.left){
					retMsg.writeInt(records1_report_left1.gsid);
					retMsg.writeLong(records1_report_left1.hurm);
					retMsg.writeLong(records1_report_left1.heal);
					retMsg.writeInt(records1_report_left1.level);
					retMsg.writeInt(records1_report_left1.pclass);
				}
			}
					if(records1.report.right == null || records1.report.right.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.report.right.size());
				for(IOBattleReportItem records1_report_right1 : records1.report.right){
					retMsg.writeInt(records1_report_right1.gsid);
					retMsg.writeLong(records1_report_right1.hurm);
					retMsg.writeLong(records1_report_right1.heal);
					retMsg.writeInt(records1_report_right1.level);
					retMsg.writeInt(records1_report_right1.pclass);
				}
			}
			}
					if(records1.left == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(records1.left.info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(records1.left.info.rname);
					retMsg.writeInt(records1.left.info.level);
					retMsg.writeInt(records1.left.info.iconid);
					retMsg.writeInt(records1.left.info.headid);
					retMsg.writeInt(records1.left.info.frameid);
			}
					if(records1.left.set == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(records1.left.set.mythic == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1.left.set.mythic.tid);
					retMsg.writeInt(records1.left.set.mythic.pclass);
					retMsg.writeInt(records1.left.set.mythic.level);
					if(records1.left.set.mythic.pskill == null || records1.left.set.mythic.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.left.set.mythic.pskill.size());
				for(Integer records1_left_set_mythic_pskill1 : records1.left.set.mythic.pskill){
			retMsg.writeInt(records1_left_set_mythic_pskill1);
				}
			}
			}
					if(records1.left.set.team == null || records1.left.set.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.left.set.team.size());
				for(Map.Entry<Integer,IOGeneralBean> records1_left_set_team1 : records1.left.set.team.entrySet()){
			retMsg.writeInt(records1_left_set_team1.getKey());
					retMsg.writeLong(records1_left_set_team1.getValue().guid);
					retMsg.writeInt(records1_left_set_team1.getValue().gsid);
					retMsg.writeInt(records1_left_set_team1.getValue().level);
					retMsg.writeInt(records1_left_set_team1.getValue().star);
					retMsg.writeInt(records1_left_set_team1.getValue().camp);
					retMsg.writeInt(records1_left_set_team1.getValue().occu);
					retMsg.writeInt(records1_left_set_team1.getValue().pclass);
					retMsg.writeInt(records1_left_set_team1.getValue().power);
					if(records1_left_set_team1.getValue().talent == null || records1_left_set_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_left_set_team1.getValue().talent.size());
				for(Integer records1_left_set_team1_getValue_talent1 : records1_left_set_team1.getValue().talent){
			retMsg.writeInt(records1_left_set_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(records1_left_set_team1.getValue().affairs);
					retMsg.writeInt(records1_left_set_team1.getValue().treasure);
					if(records1_left_set_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1_left_set_team1.getValue().property.hp);
					retMsg.writeInt(records1_left_set_team1.getValue().property.atk);
					retMsg.writeInt(records1_left_set_team1.getValue().property.def);
					retMsg.writeInt(records1_left_set_team1.getValue().property.mdef);
					retMsg.writeFloat(records1_left_set_team1.getValue().property.atktime);
					retMsg.writeInt(records1_left_set_team1.getValue().property.range);
					retMsg.writeInt(records1_left_set_team1.getValue().property.msp);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pasp);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pcri);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pcrid);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pdam);
					retMsg.writeInt(records1_left_set_team1.getValue().property.php);
					retMsg.writeInt(records1_left_set_team1.getValue().property.patk);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pdef);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pmdef);
					retMsg.writeInt(records1_left_set_team1.getValue().property.ppbs);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pmbs);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pefc);
					retMsg.writeInt(records1_left_set_team1.getValue().property.ppthr);
					retMsg.writeInt(records1_left_set_team1.getValue().property.patkdam);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pskidam);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pckatk);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pmthr);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pdex);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pmdex);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pmsatk);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pmps);
					retMsg.writeInt(records1_left_set_team1.getValue().property.pcd);
			}
					if(records1_left_set_team1.getValue().equip == null || records1_left_set_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_left_set_team1.getValue().equip.size());
				for(Integer records1_left_set_team1_getValue_equip1 : records1_left_set_team1.getValue().equip){
			retMsg.writeInt(records1_left_set_team1_getValue_equip1);
				}
			}
					if(records1_left_set_team1.getValue().skill == null || records1_left_set_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_left_set_team1.getValue().skill.size());
				for(Integer records1_left_set_team1_getValue_skill1 : records1_left_set_team1.getValue().skill){
			retMsg.writeInt(records1_left_set_team1_getValue_skill1);
				}
			}
					if(records1_left_set_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1_left_set_team1.getValue().exclusive.level);
					if(records1_left_set_team1.getValue().exclusive.skill == null || records1_left_set_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_left_set_team1.getValue().exclusive.skill.size());
				for(Integer records1_left_set_team1_getValue_exclusive_skill1 : records1_left_set_team1.getValue().exclusive.skill){
			retMsg.writeInt(records1_left_set_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(records1_left_set_team1.getValue().exclusive.gsid);
					if(records1_left_set_team1.getValue().exclusive.property == null || records1_left_set_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_left_set_team1.getValue().exclusive.property.size());
				for(KvStringPair records1_left_set_team1_getValue_exclusive_property1 : records1_left_set_team1.getValue().exclusive.property){
					retMsg.writeString(records1_left_set_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(records1_left_set_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(records1_left_set_team1.getValue().hppercent);
				}
			}
			}
			}
					if(records1.right == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(records1.right.info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(records1.right.info.rname);
					retMsg.writeInt(records1.right.info.level);
					retMsg.writeInt(records1.right.info.iconid);
					retMsg.writeInt(records1.right.info.headid);
					retMsg.writeInt(records1.right.info.frameid);
			}
					if(records1.right.set == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(records1.right.set.mythic == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1.right.set.mythic.tid);
					retMsg.writeInt(records1.right.set.mythic.pclass);
					retMsg.writeInt(records1.right.set.mythic.level);
					if(records1.right.set.mythic.pskill == null || records1.right.set.mythic.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.right.set.mythic.pskill.size());
				for(Integer records1_right_set_mythic_pskill1 : records1.right.set.mythic.pskill){
			retMsg.writeInt(records1_right_set_mythic_pskill1);
				}
			}
			}
					if(records1.right.set.team == null || records1.right.set.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.right.set.team.size());
				for(Map.Entry<Integer,IOGeneralBean> records1_right_set_team1 : records1.right.set.team.entrySet()){
			retMsg.writeInt(records1_right_set_team1.getKey());
					retMsg.writeLong(records1_right_set_team1.getValue().guid);
					retMsg.writeInt(records1_right_set_team1.getValue().gsid);
					retMsg.writeInt(records1_right_set_team1.getValue().level);
					retMsg.writeInt(records1_right_set_team1.getValue().star);
					retMsg.writeInt(records1_right_set_team1.getValue().camp);
					retMsg.writeInt(records1_right_set_team1.getValue().occu);
					retMsg.writeInt(records1_right_set_team1.getValue().pclass);
					retMsg.writeInt(records1_right_set_team1.getValue().power);
					if(records1_right_set_team1.getValue().talent == null || records1_right_set_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_right_set_team1.getValue().talent.size());
				for(Integer records1_right_set_team1_getValue_talent1 : records1_right_set_team1.getValue().talent){
			retMsg.writeInt(records1_right_set_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(records1_right_set_team1.getValue().affairs);
					retMsg.writeInt(records1_right_set_team1.getValue().treasure);
					if(records1_right_set_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1_right_set_team1.getValue().property.hp);
					retMsg.writeInt(records1_right_set_team1.getValue().property.atk);
					retMsg.writeInt(records1_right_set_team1.getValue().property.def);
					retMsg.writeInt(records1_right_set_team1.getValue().property.mdef);
					retMsg.writeFloat(records1_right_set_team1.getValue().property.atktime);
					retMsg.writeInt(records1_right_set_team1.getValue().property.range);
					retMsg.writeInt(records1_right_set_team1.getValue().property.msp);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pasp);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pcri);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pcrid);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pdam);
					retMsg.writeInt(records1_right_set_team1.getValue().property.php);
					retMsg.writeInt(records1_right_set_team1.getValue().property.patk);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pdef);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pmdef);
					retMsg.writeInt(records1_right_set_team1.getValue().property.ppbs);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pmbs);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pefc);
					retMsg.writeInt(records1_right_set_team1.getValue().property.ppthr);
					retMsg.writeInt(records1_right_set_team1.getValue().property.patkdam);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pskidam);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pckatk);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pmthr);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pdex);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pmdex);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pmsatk);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pmps);
					retMsg.writeInt(records1_right_set_team1.getValue().property.pcd);
			}
					if(records1_right_set_team1.getValue().equip == null || records1_right_set_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_right_set_team1.getValue().equip.size());
				for(Integer records1_right_set_team1_getValue_equip1 : records1_right_set_team1.getValue().equip){
			retMsg.writeInt(records1_right_set_team1_getValue_equip1);
				}
			}
					if(records1_right_set_team1.getValue().skill == null || records1_right_set_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_right_set_team1.getValue().skill.size());
				for(Integer records1_right_set_team1_getValue_skill1 : records1_right_set_team1.getValue().skill){
			retMsg.writeInt(records1_right_set_team1_getValue_skill1);
				}
			}
					if(records1_right_set_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(records1_right_set_team1.getValue().exclusive.level);
					if(records1_right_set_team1.getValue().exclusive.skill == null || records1_right_set_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_right_set_team1.getValue().exclusive.skill.size());
				for(Integer records1_right_set_team1_getValue_exclusive_skill1 : records1_right_set_team1.getValue().exclusive.skill){
			retMsg.writeInt(records1_right_set_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(records1_right_set_team1.getValue().exclusive.gsid);
					if(records1_right_set_team1.getValue().exclusive.property == null || records1_right_set_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1_right_set_team1.getValue().exclusive.property.size());
				for(KvStringPair records1_right_set_team1_getValue_exclusive_property1 : records1_right_set_team1.getValue().exclusive.property){
					retMsg.writeString(records1_right_set_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(records1_right_set_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(records1_right_set_team1.getValue().hppercent);
				}
			}
			}
			}
					retMsg.writeString(records1.mark);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionBossBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionBossBattleStart.class);
		public int chapter_id;
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SLegionBossBattleStart [chapter_id="+chapter_id+",mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 1051;

		public static C2SLegionBossBattleStart parse(MyRequestMessage request){
			C2SLegionBossBattleStart retObj = new C2SLegionBossBattleStart();
			try{
			retObj.chapter_id=request.readInt();
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
	public static final class S2CLegionBossBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionBossBattleStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CLegionBossBattleStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CLegionBossBattleStart(){}
		@Override
		public String toString() {
			return "S2CLegionBossBattleStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 1052;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1052);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SLegionBossBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SLegionBossBattleEnd.class);
		public int bossId;
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SLegionBossBattleEnd [bossId="+bossId+",battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 1053;

		public static C2SLegionBossBattleEnd parse(MyRequestMessage request){
			C2SLegionBossBattleEnd retObj = new C2SLegionBossBattleEnd();
			try{
			retObj.bossId=request.readInt();
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
	public static final class S2CLegionBossBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CLegionBossBattleEnd.class);
		public IOBattleResult result;
		public List<IORewardItem> rewards;
		public long last;
		public long damge;
		public int kill;
		@Override
		public String toString() {
			return "S2CLegionBossBattleEnd [result="+result+",rewards="+rewards+",last="+last+",damge="+damge+",kill="+kill+",]";
		}
		public static final int msgCode = 1054;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1054);
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
			retMsg.writeLong(last);
			retMsg.writeLong(damge);
			retMsg.writeInt(kill);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDungeonBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SDungeonBattleStart.class);
		public int chapter;
		public int node;
		public int pos;
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SDungeonBattleStart [chapter="+chapter+",node="+node+",pos="+pos+",mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 1055;

		public static C2SDungeonBattleStart parse(MyRequestMessage request){
			C2SDungeonBattleStart retObj = new C2SDungeonBattleStart();
			try{
			retObj.chapter=request.readInt();
			retObj.node=request.readInt();
			retObj.pos=request.readInt();
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
	public static final class S2CDungeonBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CDungeonBattleStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CDungeonBattleStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CDungeonBattleStart(){}
		@Override
		public String toString() {
			return "S2CDungeonBattleStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 1056;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1056);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDungeonBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SDungeonBattleEnd.class);
		public int chapter;
		public int node;
		public int pos;
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SDungeonBattleEnd [chapter="+chapter+",node="+node+",pos="+pos+",battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 1057;

		public static C2SDungeonBattleEnd parse(MyRequestMessage request){
			C2SDungeonBattleEnd retObj = new C2SDungeonBattleEnd();
			try{
			retObj.chapter=request.readInt();
			retObj.node=request.readInt();
			retObj.pos=request.readInt();
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
	public static final class S2CDungeonBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CDungeonBattleEnd.class);
		public IOBattleResult result;
		public long videoid;
		@Override
		public String toString() {
			return "S2CDungeonBattleEnd [result="+result+",videoid="+videoid+",]";
		}
		public static final int msgCode = 1058;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1058);
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
			retMsg.writeLong(videoid);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SOpenExploreBoss{
		private static final Logger logger = LoggerFactory.getLogger(C2SOpenExploreBoss.class);
		public int boss_owner_id;
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SOpenExploreBoss [boss_owner_id="+boss_owner_id+",mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 1059;

		public static C2SOpenExploreBoss parse(MyRequestMessage request){
			C2SOpenExploreBoss retObj = new C2SOpenExploreBoss();
			try{
			retObj.boss_owner_id=request.readInt();
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
	public static final class S2COpenExploreBoss{
		private static final Logger logger = LoggerFactory.getLogger(S2COpenExploreBoss.class);
		public long seed;
		public long battleid;
		public int season;
		public S2COpenExploreBoss(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2COpenExploreBoss(){}
		@Override
		public String toString() {
			return "S2COpenExploreBoss [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 1060;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1060);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetFbossResult{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetFbossResult.class);
		public int boss_owner_id;
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SGetFbossResult [boss_owner_id="+boss_owner_id+",battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 1061;

		public static C2SGetFbossResult parse(MyRequestMessage request){
			C2SGetFbossResult retObj = new C2SGetFbossResult();
			try{
			retObj.boss_owner_id=request.readInt();
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
	public static final class S2CGetFbossResult{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetFbossResult.class);
		public IOBattleResult result;
		public long bosshurm;
		public int kill;
		@Override
		public String toString() {
			return "S2CGetFbossResult [result="+result+",bosshurm="+bosshurm+",kill="+kill+",]";
		}
		public static final int msgCode = 1062;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1062);
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
			retMsg.writeLong(bosshurm);
			retMsg.writeInt(kill);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SFriendBattleInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendBattleInfo.class);
		public int friendid;
		@Override
		public String toString() {
			return "C2SFriendBattleInfo [friendid="+friendid+",]";
		}
		public static final int id = 1063;

		public static C2SFriendBattleInfo parse(MyRequestMessage request){
			C2SFriendBattleInfo retObj = new C2SFriendBattleInfo();
			try{
			retObj.friendid=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CFriendBattleInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendBattleInfo.class);
		public IOMythicalAnimal mythic;
		public Map<Integer,IOGeneralBean> team;
		@Override
		public String toString() {
			return "S2CFriendBattleInfo [mythic="+mythic+",team="+team+",]";
		}
		public static final int msgCode = 1064;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1064);
			if(mythic == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(mythic.tid);
					retMsg.writeInt(mythic.pclass);
					retMsg.writeInt(mythic.level);
					if(mythic.pskill == null || mythic.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mythic.pskill.size());
				for(Integer mythic_pskill1 : mythic.pskill){
			retMsg.writeInt(mythic_pskill1);
				}
			}
			}
			if(team == null || team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(team.size());
				for(Map.Entry<Integer,IOGeneralBean> team1 : team.entrySet()){
			retMsg.writeInt(team1.getKey());
					retMsg.writeLong(team1.getValue().guid);
					retMsg.writeInt(team1.getValue().gsid);
					retMsg.writeInt(team1.getValue().level);
					retMsg.writeInt(team1.getValue().star);
					retMsg.writeInt(team1.getValue().camp);
					retMsg.writeInt(team1.getValue().occu);
					retMsg.writeInt(team1.getValue().pclass);
					retMsg.writeInt(team1.getValue().power);
					if(team1.getValue().talent == null || team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(team1.getValue().talent.size());
				for(Integer team1_getValue_talent1 : team1.getValue().talent){
			retMsg.writeInt(team1_getValue_talent1);
				}
			}
					retMsg.writeInt(team1.getValue().affairs);
					retMsg.writeInt(team1.getValue().treasure);
					if(team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(team1.getValue().property.hp);
					retMsg.writeInt(team1.getValue().property.atk);
					retMsg.writeInt(team1.getValue().property.def);
					retMsg.writeInt(team1.getValue().property.mdef);
					retMsg.writeFloat(team1.getValue().property.atktime);
					retMsg.writeInt(team1.getValue().property.range);
					retMsg.writeInt(team1.getValue().property.msp);
					retMsg.writeInt(team1.getValue().property.pasp);
					retMsg.writeInt(team1.getValue().property.pcri);
					retMsg.writeInt(team1.getValue().property.pcrid);
					retMsg.writeInt(team1.getValue().property.pdam);
					retMsg.writeInt(team1.getValue().property.php);
					retMsg.writeInt(team1.getValue().property.patk);
					retMsg.writeInt(team1.getValue().property.pdef);
					retMsg.writeInt(team1.getValue().property.pmdef);
					retMsg.writeInt(team1.getValue().property.ppbs);
					retMsg.writeInt(team1.getValue().property.pmbs);
					retMsg.writeInt(team1.getValue().property.pefc);
					retMsg.writeInt(team1.getValue().property.ppthr);
					retMsg.writeInt(team1.getValue().property.patkdam);
					retMsg.writeInt(team1.getValue().property.pskidam);
					retMsg.writeInt(team1.getValue().property.pckatk);
					retMsg.writeInt(team1.getValue().property.pmthr);
					retMsg.writeInt(team1.getValue().property.pdex);
					retMsg.writeInt(team1.getValue().property.pmdex);
					retMsg.writeInt(team1.getValue().property.pmsatk);
					retMsg.writeInt(team1.getValue().property.pmps);
					retMsg.writeInt(team1.getValue().property.pcd);
			}
					if(team1.getValue().equip == null || team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(team1.getValue().equip.size());
				for(Integer team1_getValue_equip1 : team1.getValue().equip){
			retMsg.writeInt(team1_getValue_equip1);
				}
			}
					if(team1.getValue().skill == null || team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(team1.getValue().skill.size());
				for(Integer team1_getValue_skill1 : team1.getValue().skill){
			retMsg.writeInt(team1_getValue_skill1);
				}
			}
					if(team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(team1.getValue().exclusive.level);
					if(team1.getValue().exclusive.skill == null || team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(team1.getValue().exclusive.skill.size());
				for(Integer team1_getValue_exclusive_skill1 : team1.getValue().exclusive.skill){
			retMsg.writeInt(team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(team1.getValue().exclusive.gsid);
					if(team1.getValue().exclusive.property == null || team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(team1.getValue().exclusive.property.size());
				for(KvStringPair team1_getValue_exclusive_property1 : team1.getValue().exclusive.property){
					retMsg.writeString(team1_getValue_exclusive_property1.key);
					retMsg.writeInt(team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(team1.getValue().hppercent);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SFriendBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendBattleStart.class);
		public int friendid;
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SFriendBattleStart [friendid="+friendid+",mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 1065;

		public static C2SFriendBattleStart parse(MyRequestMessage request){
			C2SFriendBattleStart retObj = new C2SFriendBattleStart();
			try{
			retObj.friendid=request.readInt();
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
	public static final class S2CFriendBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendBattleStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CFriendBattleStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CFriendBattleStart(){}
		@Override
		public String toString() {
			return "S2CFriendBattleStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 1066;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1066);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SFriendBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SFriendBattleEnd.class);
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SFriendBattleEnd [battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 1067;

		public static C2SFriendBattleEnd parse(MyRequestMessage request){
			C2SFriendBattleEnd retObj = new C2SFriendBattleEnd();
			try{
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
	public static final class S2CFriendBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CFriendBattleEnd.class);
		public IOBattleResult result;
		public long videoid;
		@Override
		public String toString() {
			return "S2CFriendBattleEnd [result="+result+",videoid="+videoid+",]";
		}
		public static final int msgCode = 1068;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1068);
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
			retMsg.writeLong(videoid);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SWorldBossBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SWorldBossBattleStart.class);
		public int bossid;
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SWorldBossBattleStart [bossid="+bossid+",mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 1069;

		public static C2SWorldBossBattleStart parse(MyRequestMessage request){
			C2SWorldBossBattleStart retObj = new C2SWorldBossBattleStart();
			try{
			retObj.bossid=request.readInt();
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
	public static final class S2CWorldBossBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CWorldBossBattleStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CWorldBossBattleStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CWorldBossBattleStart(){}
		@Override
		public String toString() {
			return "S2CWorldBossBattleStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 1070;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1070);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SWorldBossBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SWorldBossBattleEnd.class);
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SWorldBossBattleEnd [battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 1071;

		public static C2SWorldBossBattleEnd parse(MyRequestMessage request){
			C2SWorldBossBattleEnd retObj = new C2SWorldBossBattleEnd();
			try{
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
	public static final class S2CWorldBossBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CWorldBossBattleEnd.class);
		public IOBattleResult result;
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2CWorldBossBattleEnd [result="+result+",rewards="+rewards+",]";
		}
		public static final int msgCode = 1072;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1072);
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
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STnqwBossStart{
		private static final Logger logger = LoggerFactory.getLogger(C2STnqwBossStart.class);
		public int boss_index;
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2STnqwBossStart [boss_index="+boss_index+",mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 1073;

		public static C2STnqwBossStart parse(MyRequestMessage request){
			C2STnqwBossStart retObj = new C2STnqwBossStart();
			try{
			retObj.boss_index=request.readInt();
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
	public static final class S2CTnqwBossStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CTnqwBossStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CTnqwBossStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CTnqwBossStart(){}
		@Override
		public String toString() {
			return "S2CTnqwBossStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 1074;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1074);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STnqwBossEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2STnqwBossEnd.class);
		public int boss_index;
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2STnqwBossEnd [boss_index="+boss_index+",battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 1075;

		public static C2STnqwBossEnd parse(MyRequestMessage request){
			C2STnqwBossEnd retObj = new C2STnqwBossEnd();
			try{
			retObj.boss_index=request.readInt();
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
	public static final class S2CTnqwBossEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CTnqwBossEnd.class);
		public IOBattleResult result;
		public List<IORewardItem> reward;
		@Override
		public String toString() {
			return "S2CTnqwBossEnd [result="+result+",reward="+reward+",]";
		}
		public static final int msgCode = 1076;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1076);
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
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(IORewardItem reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STgslBossStart{
		private static final Logger logger = LoggerFactory.getLogger(C2STgslBossStart.class);
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2STgslBossStart [mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 1077;

		public static C2STgslBossStart parse(MyRequestMessage request){
			C2STgslBossStart retObj = new C2STgslBossStart();
			try{
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
	public static final class S2CTgslBossStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CTgslBossStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CTgslBossStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CTgslBossStart(){}
		@Override
		public String toString() {
			return "S2CTgslBossStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 1078;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1078);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STgslBossEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2STgslBossEnd.class);
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2STgslBossEnd [battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 1079;

		public static C2STgslBossEnd parse(MyRequestMessage request){
			C2STgslBossEnd retObj = new C2STgslBossEnd();
			try{
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
	public static final class S2CTgslBossEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CTgslBossEnd.class);
		public IOBattleResult result;
		public List<IORewardItem> reward;
		public long damge;
		public int kill;
		@Override
		public String toString() {
			return "S2CTgslBossEnd [result="+result+",reward="+reward+",damge="+damge+",kill="+kill+",]";
		}
		public static final int msgCode = 1080;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1080);
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
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(IORewardItem reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
				}
			}
			retMsg.writeLong(damge);
			retMsg.writeInt(kill);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SKpStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SKpStart.class);
		public int target_rid;
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SKpStart [target_rid="+target_rid+",mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 1081;

		public static C2SKpStart parse(MyRequestMessage request){
			C2SKpStart retObj = new C2SKpStart();
			try{
			retObj.target_rid=request.readInt();
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
	public static final class S2CKpStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CKpStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CKpStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CKpStart(){}
		@Override
		public String toString() {
			return "S2CKpStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 1082;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1082);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMineBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SMineBattleStart.class);
		public int mythic;
		public IOFormationGeneralPos[] items;
		public int level_index;
		public int point_index;
		@Override
		public String toString() {
			return "C2SMineBattleStart [mythic="+mythic+",items="+java.util.Arrays.toString(items)+",level_index="+level_index+",point_index="+point_index+",]";
		}
		public static final int id = 1083;

		public static C2SMineBattleStart parse(MyRequestMessage request){
			C2SMineBattleStart retObj = new C2SMineBattleStart();
			try{
			retObj.mythic=request.readInt();
			int items_size = request.readInt();
				retObj.items = new IOFormationGeneralPos[items_size];
				for(int i=0;i<items_size;i++){
						retObj.items[i] = new IOFormationGeneralPos();
					retObj.items[i].pos=request.readInt();
					retObj.items[i].general_uuid=request.readLong();
				}
			retObj.level_index=request.readInt();
			retObj.point_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMineBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CMineBattleStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CMineBattleStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CMineBattleStart(){}
		@Override
		public String toString() {
			return "S2CMineBattleStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 1084;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1084);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMineBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SMineBattleEnd.class);
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SMineBattleEnd [battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 1085;

		public static C2SMineBattleEnd parse(MyRequestMessage request){
			C2SMineBattleEnd retObj = new C2SMineBattleEnd();
			try{
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
	public static final class S2CMineBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CMineBattleEnd.class);
		public int level_index;
		public int point_index;
		public IOBattleResult result;
		public List<IORewardItem> reward;
		@Override
		public String toString() {
			return "S2CMineBattleEnd [level_index="+level_index+",point_index="+point_index+",result="+result+",reward="+reward+",]";
		}
		public static final int msgCode = 1086;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1086);
			retMsg.writeInt(level_index);
			retMsg.writeInt(point_index);
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
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(IORewardItem reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuozhanBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanBattleStart.class);
		public int mythic;
		public IOFormationGeneralPos[] items;
		public int city_index;
		@Override
		public String toString() {
			return "C2SGuozhanBattleStart [mythic="+mythic+",items="+java.util.Arrays.toString(items)+",city_index="+city_index+",]";
		}
		public static final int id = 1087;

		public static C2SGuozhanBattleStart parse(MyRequestMessage request){
			C2SGuozhanBattleStart retObj = new C2SGuozhanBattleStart();
			try{
			retObj.mythic=request.readInt();
			int items_size = request.readInt();
				retObj.items = new IOFormationGeneralPos[items_size];
				for(int i=0;i<items_size;i++){
						retObj.items[i] = new IOFormationGeneralPos();
					retObj.items[i].pos=request.readInt();
					retObj.items[i].general_uuid=request.readLong();
				}
			retObj.city_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuozhanBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanBattleStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CGuozhanBattleStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CGuozhanBattleStart(){}
		@Override
		public String toString() {
			return "S2CGuozhanBattleStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 1088;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1088);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuozhanBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanBattleEnd.class);
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SGuozhanBattleEnd [battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 1089;

		public static C2SGuozhanBattleEnd parse(MyRequestMessage request){
			C2SGuozhanBattleEnd retObj = new C2SGuozhanBattleEnd();
			try{
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
	public static final class S2CGuozhanBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanBattleEnd.class);
		public int city_index;
		public IOBattleResult result;
		public List<IORewardItem> reward;
		@Override
		public String toString() {
			return "S2CGuozhanBattleEnd [city_index="+city_index+",result="+result+",reward="+reward+",]";
		}
		public static final int msgCode = 1090;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1090);
			retMsg.writeInt(city_index);
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
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(IORewardItem reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuozhanOfficeStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanOfficeStart.class);
		public int mythic;
		public IOFormationGeneralPos[] items;
		public int office_index;
		@Override
		public String toString() {
			return "C2SGuozhanOfficeStart [mythic="+mythic+",items="+java.util.Arrays.toString(items)+",office_index="+office_index+",]";
		}
		public static final int id = 1091;

		public static C2SGuozhanOfficeStart parse(MyRequestMessage request){
			C2SGuozhanOfficeStart retObj = new C2SGuozhanOfficeStart();
			try{
			retObj.mythic=request.readInt();
			int items_size = request.readInt();
				retObj.items = new IOFormationGeneralPos[items_size];
				for(int i=0;i<items_size;i++){
						retObj.items[i] = new IOFormationGeneralPos();
					retObj.items[i].pos=request.readInt();
					retObj.items[i].general_uuid=request.readLong();
				}
			retObj.office_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuozhanOfficeStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanOfficeStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CGuozhanOfficeStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CGuozhanOfficeStart(){}
		@Override
		public String toString() {
			return "S2CGuozhanOfficeStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 1092;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1092);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuozhanOfficCalculate{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanOfficCalculate.class);
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SGuozhanOfficCalculate [battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 1093;

		public static C2SGuozhanOfficCalculate parse(MyRequestMessage request){
			C2SGuozhanOfficCalculate retObj = new C2SGuozhanOfficCalculate();
			try{
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
	public static final class S2CGuozhanOfficCalculate{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanOfficCalculate.class);
		public int office_index;
		public IOBattleResult result;
		public List<IORewardItem> reward;
		@Override
		public String toString() {
			return "S2CGuozhanOfficCalculate [office_index="+office_index+",result="+result+",reward="+reward+",]";
		}
		public static final int msgCode = 1094;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1094);
			retMsg.writeInt(office_index);
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
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(IORewardItem reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuozhanCityStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanCityStart.class);
		public int mythic;
		public IOFormationGeneralPos[] items;
		public int city_index;
		@Override
		public String toString() {
			return "C2SGuozhanCityStart [mythic="+mythic+",items="+java.util.Arrays.toString(items)+",city_index="+city_index+",]";
		}
		public static final int id = 1095;

		public static C2SGuozhanCityStart parse(MyRequestMessage request){
			C2SGuozhanCityStart retObj = new C2SGuozhanCityStart();
			try{
			retObj.mythic=request.readInt();
			int items_size = request.readInt();
				retObj.items = new IOFormationGeneralPos[items_size];
				for(int i=0;i<items_size;i++){
						retObj.items[i] = new IOFormationGeneralPos();
					retObj.items[i].pos=request.readInt();
					retObj.items[i].general_uuid=request.readLong();
				}
			retObj.city_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuozhanCityStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanCityStart.class);
		public long seed;
		public long battleid;
		public int season;
		public S2CGuozhanCityStart(long pseed,long pbattleid,int pseason){
			seed=pseed;
			battleid=pbattleid;
			season=pseason;
		}
		public S2CGuozhanCityStart(){}
		@Override
		public String toString() {
			return "S2CGuozhanCityStart [seed="+seed+",battleid="+battleid+",season="+season+",]";
		}
		public static final int msgCode = 1096;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1096);
			retMsg.writeLong(seed);
			retMsg.writeLong(battleid);
			retMsg.writeInt(season);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuozhanCityCalculate{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanCityCalculate.class);
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SGuozhanCityCalculate [battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 1097;

		public static C2SGuozhanCityCalculate parse(MyRequestMessage request){
			C2SGuozhanCityCalculate retObj = new C2SGuozhanCityCalculate();
			try{
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
	public static final class S2CGuozhanCityCalculate{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanCityCalculate.class);
		public int city_index;
		public IOBattleResult result;
		public List<IORewardItem> reward;
		public int move_step;
		@Override
		public String toString() {
			return "S2CGuozhanCityCalculate [city_index="+city_index+",result="+result+",reward="+reward+",move_step="+move_step+",]";
		}
		public static final int msgCode = 1098;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1098);
			retMsg.writeInt(city_index);
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
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(IORewardItem reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
				}
			}
			retMsg.writeInt(move_step);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SKpBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SKpBattleEnd.class);
		public long battleid;
		public IOBHurt[] as;
		public IOBHurt[] df;
		@Override
		public String toString() {
			return "C2SKpBattleEnd [battleid="+battleid+",as="+java.util.Arrays.toString(as)+",df="+java.util.Arrays.toString(df)+",]";
		}
		public static final int id = 1099;

		public static C2SKpBattleEnd parse(MyRequestMessage request){
			C2SKpBattleEnd retObj = new C2SKpBattleEnd();
			try{
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
	public static final class S2CKpBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CKpBattleEnd.class);
		public IOStageInfo stageinfo;
		public List<IOBattleResult> resultlist;
		public List<IORewardItemSelect> reward;
		@Override
		public String toString() {
			return "S2CKpBattleEnd [stageinfo="+stageinfo+",resultlist="+resultlist+",reward="+reward+",]";
		}
		public static final int msgCode = 1100;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1100);
			if(stageinfo == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(stageinfo.schange);
					retMsg.writeInt(stageinfo.stage);
					retMsg.writeInt(stageinfo.star);
			}
			if(resultlist == null || resultlist.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist.size());
				for(IOBattleResult resultlist1 : resultlist){
					retMsg.writeString(resultlist1.ret);
					retMsg.writeInt(resultlist1.round);
					if(resultlist1.lhp == null || resultlist1.lhp.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1.lhp.size());
				for(Map.Entry<Integer,Long> resultlist1_lhp1 : resultlist1.lhp.entrySet()){
			retMsg.writeInt(resultlist1_lhp1.getKey());
			retMsg.writeLong(resultlist1_lhp1.getValue());
				}
			}
					if(resultlist1.rhp == null || resultlist1.rhp.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1.rhp.size());
				for(Map.Entry<Integer,Long> resultlist1_rhp1 : resultlist1.rhp.entrySet()){
			retMsg.writeInt(resultlist1_rhp1.getKey());
			retMsg.writeLong(resultlist1_rhp1.getValue());
				}
			}
					if(resultlist1.lper == null || resultlist1.lper.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1.lper.size());
				for(Map.Entry<Integer,Integer> resultlist1_lper1 : resultlist1.lper.entrySet()){
			retMsg.writeInt(resultlist1_lper1.getKey());
			retMsg.writeInt(resultlist1_lper1.getValue());
				}
			}
					if(resultlist1.rper == null || resultlist1.rper.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1.rper.size());
				for(Map.Entry<Integer,Integer> resultlist1_rper1 : resultlist1.rper.entrySet()){
			retMsg.writeInt(resultlist1_rper1.getKey());
			retMsg.writeInt(resultlist1_rper1.getValue());
				}
			}
					retMsg.writeInt(resultlist1.ltper);
					retMsg.writeInt(resultlist1.rtper);
					retMsg.writeInt(resultlist1.rlosthp);
					if(resultlist1.report == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(resultlist1.report.left == null || resultlist1.report.left.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1.report.left.size());
				for(IOBattleReportItem resultlist1_report_left1 : resultlist1.report.left){
					retMsg.writeInt(resultlist1_report_left1.gsid);
					retMsg.writeLong(resultlist1_report_left1.hurm);
					retMsg.writeLong(resultlist1_report_left1.heal);
					retMsg.writeInt(resultlist1_report_left1.level);
					retMsg.writeInt(resultlist1_report_left1.pclass);
				}
			}
					if(resultlist1.report.right == null || resultlist1.report.right.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1.report.right.size());
				for(IOBattleReportItem resultlist1_report_right1 : resultlist1.report.right){
					retMsg.writeInt(resultlist1_report_right1.gsid);
					retMsg.writeLong(resultlist1_report_right1.hurm);
					retMsg.writeLong(resultlist1_report_right1.heal);
					retMsg.writeInt(resultlist1_report_right1.level);
					retMsg.writeInt(resultlist1_report_right1.pclass);
				}
			}
			}
					retMsg.writeLong(resultlist1.version);
					if(resultlist1.left == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(resultlist1.left.info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(resultlist1.left.info.rname);
					retMsg.writeInt(resultlist1.left.info.level);
					retMsg.writeInt(resultlist1.left.info.iconid);
					retMsg.writeInt(resultlist1.left.info.headid);
					retMsg.writeInt(resultlist1.left.info.frameid);
			}
					if(resultlist1.left.set == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(resultlist1.left.set.index);
					if(resultlist1.left.set.team == null || resultlist1.left.set.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1.left.set.team.size());
				for(Map.Entry<Integer,IOGeneralBean> resultlist1_left_set_team1 : resultlist1.left.set.team.entrySet()){
			retMsg.writeInt(resultlist1_left_set_team1.getKey());
					retMsg.writeLong(resultlist1_left_set_team1.getValue().guid);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().gsid);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().level);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().star);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().camp);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().occu);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().pclass);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().power);
					if(resultlist1_left_set_team1.getValue().talent == null || resultlist1_left_set_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1_left_set_team1.getValue().talent.size());
				for(Integer resultlist1_left_set_team1_getValue_talent1 : resultlist1_left_set_team1.getValue().talent){
			retMsg.writeInt(resultlist1_left_set_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(resultlist1_left_set_team1.getValue().affairs);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().treasure);
					if(resultlist1_left_set_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.hp);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.atk);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.def);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.mdef);
					retMsg.writeFloat(resultlist1_left_set_team1.getValue().property.atktime);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.range);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.msp);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pasp);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pcri);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pcrid);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pdam);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.php);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.patk);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pdef);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pmdef);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.ppbs);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pmbs);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pefc);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.ppthr);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.patkdam);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pskidam);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pckatk);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pmthr);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pdex);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pmdex);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pmsatk);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pmps);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().property.pcd);
			}
					if(resultlist1_left_set_team1.getValue().equip == null || resultlist1_left_set_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1_left_set_team1.getValue().equip.size());
				for(Integer resultlist1_left_set_team1_getValue_equip1 : resultlist1_left_set_team1.getValue().equip){
			retMsg.writeInt(resultlist1_left_set_team1_getValue_equip1);
				}
			}
					if(resultlist1_left_set_team1.getValue().skill == null || resultlist1_left_set_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1_left_set_team1.getValue().skill.size());
				for(Integer resultlist1_left_set_team1_getValue_skill1 : resultlist1_left_set_team1.getValue().skill){
			retMsg.writeInt(resultlist1_left_set_team1_getValue_skill1);
				}
			}
					if(resultlist1_left_set_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(resultlist1_left_set_team1.getValue().exclusive.level);
					if(resultlist1_left_set_team1.getValue().exclusive.skill == null || resultlist1_left_set_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1_left_set_team1.getValue().exclusive.skill.size());
				for(Integer resultlist1_left_set_team1_getValue_exclusive_skill1 : resultlist1_left_set_team1.getValue().exclusive.skill){
			retMsg.writeInt(resultlist1_left_set_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(resultlist1_left_set_team1.getValue().exclusive.gsid);
					if(resultlist1_left_set_team1.getValue().exclusive.property == null || resultlist1_left_set_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1_left_set_team1.getValue().exclusive.property.size());
				for(KvStringPair resultlist1_left_set_team1_getValue_exclusive_property1 : resultlist1_left_set_team1.getValue().exclusive.property){
					retMsg.writeString(resultlist1_left_set_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(resultlist1_left_set_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(resultlist1_left_set_team1.getValue().hppercent);
				}
			}
			}
			}
					if(resultlist1.right == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(resultlist1.right.info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(resultlist1.right.info.rname);
					retMsg.writeInt(resultlist1.right.info.level);
					retMsg.writeInt(resultlist1.right.info.iconid);
					retMsg.writeInt(resultlist1.right.info.headid);
					retMsg.writeInt(resultlist1.right.info.frameid);
			}
					if(resultlist1.right.set == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(resultlist1.right.set.index);
					if(resultlist1.right.set.team == null || resultlist1.right.set.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1.right.set.team.size());
				for(Map.Entry<Integer,IOGeneralBean> resultlist1_right_set_team1 : resultlist1.right.set.team.entrySet()){
			retMsg.writeInt(resultlist1_right_set_team1.getKey());
					retMsg.writeLong(resultlist1_right_set_team1.getValue().guid);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().gsid);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().level);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().star);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().camp);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().occu);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().pclass);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().power);
					if(resultlist1_right_set_team1.getValue().talent == null || resultlist1_right_set_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1_right_set_team1.getValue().talent.size());
				for(Integer resultlist1_right_set_team1_getValue_talent1 : resultlist1_right_set_team1.getValue().talent){
			retMsg.writeInt(resultlist1_right_set_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(resultlist1_right_set_team1.getValue().affairs);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().treasure);
					if(resultlist1_right_set_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.hp);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.atk);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.def);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.mdef);
					retMsg.writeFloat(resultlist1_right_set_team1.getValue().property.atktime);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.range);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.msp);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pasp);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pcri);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pcrid);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pdam);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.php);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.patk);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pdef);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pmdef);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.ppbs);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pmbs);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pefc);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.ppthr);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.patkdam);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pskidam);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pckatk);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pmthr);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pdex);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pmdex);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pmsatk);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pmps);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().property.pcd);
			}
					if(resultlist1_right_set_team1.getValue().equip == null || resultlist1_right_set_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1_right_set_team1.getValue().equip.size());
				for(Integer resultlist1_right_set_team1_getValue_equip1 : resultlist1_right_set_team1.getValue().equip){
			retMsg.writeInt(resultlist1_right_set_team1_getValue_equip1);
				}
			}
					if(resultlist1_right_set_team1.getValue().skill == null || resultlist1_right_set_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1_right_set_team1.getValue().skill.size());
				for(Integer resultlist1_right_set_team1_getValue_skill1 : resultlist1_right_set_team1.getValue().skill){
			retMsg.writeInt(resultlist1_right_set_team1_getValue_skill1);
				}
			}
					if(resultlist1_right_set_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(resultlist1_right_set_team1.getValue().exclusive.level);
					if(resultlist1_right_set_team1.getValue().exclusive.skill == null || resultlist1_right_set_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1_right_set_team1.getValue().exclusive.skill.size());
				for(Integer resultlist1_right_set_team1_getValue_exclusive_skill1 : resultlist1_right_set_team1.getValue().exclusive.skill){
			retMsg.writeInt(resultlist1_right_set_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(resultlist1_right_set_team1.getValue().exclusive.gsid);
					if(resultlist1_right_set_team1.getValue().exclusive.property == null || resultlist1_right_set_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(resultlist1_right_set_team1.getValue().exclusive.property.size());
				for(KvStringPair resultlist1_right_set_team1_getValue_exclusive_property1 : resultlist1_right_set_team1.getValue().exclusive.property){
					retMsg.writeString(resultlist1_right_set_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(resultlist1_right_set_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(resultlist1_right_set_team1.getValue().hppercent);
				}
			}
			}
			}
				}
			}
			if(reward == null || reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward.size());
				for(IORewardItemSelect reward1 : reward){
					retMsg.writeInt(reward1.GSID);
					retMsg.writeInt(reward1.COUNT);
					retMsg.writeBool(reward1.real);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
