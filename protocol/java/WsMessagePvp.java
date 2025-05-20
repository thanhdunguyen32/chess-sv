package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.Map;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IORewardItem;
import ws.WsMessageBase.IOBattlesetEnemy;
import ws.WsMessageBase.IOMythicalAnimal;
import ws.WsMessageBase.IOGeneralBean;
import ws.WsMessageBase.IOProperty;
import ws.WsMessageBase.IOExclusive;
import ws.WsMessageBase.KvStringPair;

public final class WsMessagePvp{
	public static final class C2SGetKpMissionAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetKpMissionAward.class);
		public int mission_index;
		@Override
		public String toString() {
			return "C2SGetKpMissionAward [mission_index="+mission_index+",]";
		}
		public static final int id = 1151;

		public static C2SGetKpMissionAward parse(MyRequestMessage request){
			C2SGetKpMissionAward retObj = new C2SGetKpMissionAward();
			try{
			retObj.mission_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGetKpMissionAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetKpMissionAward.class);
		public List<IORewardItem> rewards;
		@Override
		public String toString() {
			return "S2CGetKpMissionAward [rewards="+rewards+",]";
		}
		public static final int msgCode = 1152;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1152);
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
	public static final class C2SKpSearchEnemy{
		private static final Logger logger = LoggerFactory.getLogger(C2SKpSearchEnemy.class);
		public int force;
		@Override
		public String toString() {
			return "C2SKpSearchEnemy [force="+force+",]";
		}
		public static final int id = 1153;

		public static C2SKpSearchEnemy parse(MyRequestMessage request){
			C2SKpSearchEnemy retObj = new C2SKpSearchEnemy();
			try{
			retObj.force=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CKpSearchEnemy{
		private static final Logger logger = LoggerFactory.getLogger(S2CKpSearchEnemy.class);
		public int hide;
		public int rid;
		public int star;
		public int stage;
		public String uid;
		public int servid;
		public long time;
		public String rname;
		public int power;
		public int iconid;
		public int frameid;
		public int level;
		public int vip;
		public List<IOBattlesetEnemy> battleset;
		@Override
		public String toString() {
			return "S2CKpSearchEnemy [hide="+hide+",rid="+rid+",star="+star+",stage="+stage+",uid="+uid+",servid="+servid+",time="+time+",rname="+rname+",power="+power+",iconid="+iconid+",frameid="+frameid+",level="+level+",vip="+vip+",battleset="+battleset+",]";
		}
		public static final int msgCode = 1154;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1154);
			retMsg.writeInt(hide);
			retMsg.writeInt(rid);
			retMsg.writeInt(star);
			retMsg.writeInt(stage);
			retMsg.writeString(uid);
			retMsg.writeInt(servid);
			retMsg.writeLong(time);
			retMsg.writeString(rname);
			retMsg.writeInt(power);
			retMsg.writeInt(iconid);
			retMsg.writeInt(frameid);
			retMsg.writeInt(level);
			retMsg.writeInt(vip);
			if(battleset == null || battleset.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset.size());
				for(IOBattlesetEnemy battleset1 : battleset){
					if(battleset1.mythic == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(battleset1.mythic.tid);
					retMsg.writeInt(battleset1.mythic.pclass);
					retMsg.writeInt(battleset1.mythic.level);
					if(battleset1.mythic.pskill == null || battleset1.mythic.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset1.mythic.pskill.size());
				for(Integer battleset1_mythic_pskill1 : battleset1.mythic.pskill){
			retMsg.writeInt(battleset1_mythic_pskill1);
				}
			}
			}
					if(battleset1.team == null || battleset1.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset1.team.size());
				for(Map.Entry<Integer,IOGeneralBean> battleset1_team1 : battleset1.team.entrySet()){
			retMsg.writeInt(battleset1_team1.getKey());
					retMsg.writeLong(battleset1_team1.getValue().guid);
					retMsg.writeInt(battleset1_team1.getValue().gsid);
					retMsg.writeInt(battleset1_team1.getValue().level);
					retMsg.writeInt(battleset1_team1.getValue().star);
					retMsg.writeInt(battleset1_team1.getValue().camp);
					retMsg.writeInt(battleset1_team1.getValue().occu);
					retMsg.writeInt(battleset1_team1.getValue().pclass);
					retMsg.writeInt(battleset1_team1.getValue().power);
					if(battleset1_team1.getValue().talent == null || battleset1_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset1_team1.getValue().talent.size());
				for(Integer battleset1_team1_getValue_talent1 : battleset1_team1.getValue().talent){
			retMsg.writeInt(battleset1_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(battleset1_team1.getValue().affairs);
					retMsg.writeInt(battleset1_team1.getValue().treasure);
					if(battleset1_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(battleset1_team1.getValue().property.hp);
					retMsg.writeInt(battleset1_team1.getValue().property.atk);
					retMsg.writeInt(battleset1_team1.getValue().property.def);
					retMsg.writeInt(battleset1_team1.getValue().property.mdef);
					retMsg.writeFloat(battleset1_team1.getValue().property.atktime);
					retMsg.writeInt(battleset1_team1.getValue().property.range);
					retMsg.writeInt(battleset1_team1.getValue().property.msp);
					retMsg.writeInt(battleset1_team1.getValue().property.pasp);
					retMsg.writeInt(battleset1_team1.getValue().property.pcri);
					retMsg.writeInt(battleset1_team1.getValue().property.pcrid);
					retMsg.writeInt(battleset1_team1.getValue().property.pdam);
					retMsg.writeInt(battleset1_team1.getValue().property.php);
					retMsg.writeInt(battleset1_team1.getValue().property.patk);
					retMsg.writeInt(battleset1_team1.getValue().property.pdef);
					retMsg.writeInt(battleset1_team1.getValue().property.pmdef);
					retMsg.writeInt(battleset1_team1.getValue().property.ppbs);
					retMsg.writeInt(battleset1_team1.getValue().property.pmbs);
					retMsg.writeInt(battleset1_team1.getValue().property.pefc);
					retMsg.writeInt(battleset1_team1.getValue().property.ppthr);
					retMsg.writeInt(battleset1_team1.getValue().property.patkdam);
					retMsg.writeInt(battleset1_team1.getValue().property.pskidam);
					retMsg.writeInt(battleset1_team1.getValue().property.pckatk);
					retMsg.writeInt(battleset1_team1.getValue().property.pmthr);
					retMsg.writeInt(battleset1_team1.getValue().property.pdex);
					retMsg.writeInt(battleset1_team1.getValue().property.pmdex);
					retMsg.writeInt(battleset1_team1.getValue().property.pmsatk);
					retMsg.writeInt(battleset1_team1.getValue().property.pmps);
					retMsg.writeInt(battleset1_team1.getValue().property.pcd);
			}
					if(battleset1_team1.getValue().equip == null || battleset1_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset1_team1.getValue().equip.size());
				for(Integer battleset1_team1_getValue_equip1 : battleset1_team1.getValue().equip){
			retMsg.writeInt(battleset1_team1_getValue_equip1);
				}
			}
					if(battleset1_team1.getValue().skill == null || battleset1_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset1_team1.getValue().skill.size());
				for(Integer battleset1_team1_getValue_skill1 : battleset1_team1.getValue().skill){
			retMsg.writeInt(battleset1_team1_getValue_skill1);
				}
			}
					if(battleset1_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(battleset1_team1.getValue().exclusive.level);
					if(battleset1_team1.getValue().exclusive.skill == null || battleset1_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset1_team1.getValue().exclusive.skill.size());
				for(Integer battleset1_team1_getValue_exclusive_skill1 : battleset1_team1.getValue().exclusive.skill){
			retMsg.writeInt(battleset1_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(battleset1_team1.getValue().exclusive.gsid);
					if(battleset1_team1.getValue().exclusive.property == null || battleset1_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset1_team1.getValue().exclusive.property.size());
				for(KvStringPair battleset1_team1_getValue_exclusive_property1 : battleset1_team1.getValue().exclusive.property){
					retMsg.writeString(battleset1_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(battleset1_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(battleset1_team1.getValue().hppercent);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SKpSwitchOrder{
		private static final Logger logger = LoggerFactory.getLogger(C2SKpSwitchOrder.class);
		public int[] teamorder;
		@Override
		public String toString() {
			return "C2SKpSwitchOrder [teamorder="+java.util.Arrays.toString(teamorder)+",]";
		}
		public static final int id = 1155;

		public static C2SKpSwitchOrder parse(MyRequestMessage request){
			C2SKpSwitchOrder retObj = new C2SKpSwitchOrder();
			try{
			int teamorder_size = request.readInt();
				retObj.teamorder = new int[teamorder_size];
				for(int i=0;i<teamorder_size;i++){
					retObj.teamorder[i]=request.readInt();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CKpSwitchOrder{
		private static final Logger logger = LoggerFactory.getLogger(S2CKpSwitchOrder.class);
		public static final int msgCode = 1156;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1156);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SKpStageInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SKpStageInfo.class);
		public int uid;
		public int servid;
		@Override
		public String toString() {
			return "C2SKpStageInfo [uid="+uid+",servid="+servid+",]";
		}
		public static final int id = 1157;

		public static C2SKpStageInfo parse(MyRequestMessage request){
			C2SKpStageInfo retObj = new C2SKpStageInfo();
			try{
			retObj.uid=request.readInt();
			retObj.servid=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CKpStageInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CKpStageInfo.class);
		public int schange;
		public int stage;
		public int star;
		public int hstage;
		public List<String> locate;
		public List<String> promotion;
		@Override
		public String toString() {
			return "S2CKpStageInfo [schange="+schange+",stage="+stage+",star="+star+",hstage="+hstage+",locate="+locate+",promotion="+promotion+",]";
		}
		public static final int msgCode = 1158;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1158);
			retMsg.writeInt(schange);
			retMsg.writeInt(stage);
			retMsg.writeInt(star);
			retMsg.writeInt(hstage);
			if(locate == null || locate.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(locate.size());
				for(String locate1 : locate){
			retMsg.writeString(locate1);
				}
			}
			if(promotion == null || promotion.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(promotion.size());
				for(String promotion1 : promotion){
			retMsg.writeString(promotion1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
