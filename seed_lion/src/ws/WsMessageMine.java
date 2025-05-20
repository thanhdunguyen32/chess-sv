package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.Map;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOMintPoint;
import ws.WsMessageBase.IOMineHolder;
import ws.WsMessageBase.IOBattlesetEnemy;
import ws.WsMessageBase.IOMythicalAnimal;
import ws.WsMessageBase.IOGeneralBean;
import ws.WsMessageBase.IOProperty;
import ws.WsMessageBase.IOExclusive;
import ws.WsMessageBase.KvStringPair;
import ws.WsMessageBase.IORewardItem;
import ws.WsMessageBase.IOMineHistory;
import ws.WsMessageBase.IOFormationGeneralPos;

public final class WsMessageMine{
	public static final class C2SMineList{
		private static final Logger logger = LoggerFactory.getLogger(C2SMineList.class);
		public int level_index;
		public int page_index;
		@Override
		public String toString() {
			return "C2SMineList [level_index="+level_index+",page_index="+page_index+",]";
		}
		public static final int id = 3101;

		public static C2SMineList parse(MyRequestMessage request){
			C2SMineList retObj = new C2SMineList();
			try{
			retObj.level_index=request.readInt();
			retObj.page_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMineList{
		private static final Logger logger = LoggerFactory.getLogger(S2CMineList.class);
		public int level_index;
		public int page_index;
		public List<Integer> my_income;
		public List<Integer> my_hold;
		public List<Integer> my_cd_time;
		public List<IOMintPoint> mine_points;
		@Override
		public String toString() {
			return "S2CMineList [level_index="+level_index+",page_index="+page_index+",my_income="+my_income+",my_hold="+my_hold+",my_cd_time="+my_cd_time+",mine_points="+mine_points+",]";
		}
		public static final int msgCode = 3102;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3102);
			retMsg.writeInt(level_index);
			retMsg.writeInt(page_index);
			if(my_income == null || my_income.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(my_income.size());
				for(Integer my_income1 : my_income){
			retMsg.writeInt(my_income1);
				}
			}
			if(my_hold == null || my_hold.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(my_hold.size());
				for(Integer my_hold1 : my_hold){
			retMsg.writeInt(my_hold1);
				}
			}
			if(my_cd_time == null || my_cd_time.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(my_cd_time.size());
				for(Integer my_cd_time1 : my_cd_time){
			retMsg.writeInt(my_cd_time1);
				}
			}
			if(mine_points == null || mine_points.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mine_points.size());
				for(IOMintPoint mine_points1 : mine_points){
					if(mine_points1.hold_player == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(mine_points1.hold_player.rid);
					retMsg.writeString(mine_points1.hold_player.rname);
					retMsg.writeInt(mine_points1.hold_player.level);
					retMsg.writeInt(mine_points1.hold_player.iconid);
					retMsg.writeInt(mine_points1.hold_player.frameid);
					retMsg.writeInt(mine_points1.hold_player.fight);
					retMsg.writeInt(mine_points1.hold_player.cd_time);
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMineEnemyDetail{
		private static final Logger logger = LoggerFactory.getLogger(C2SMineEnemyDetail.class);
		public int level_index;
		public int point_index;
		@Override
		public String toString() {
			return "C2SMineEnemyDetail [level_index="+level_index+",point_index="+point_index+",]";
		}
		public static final int id = 3103;

		public static C2SMineEnemyDetail parse(MyRequestMessage request){
			C2SMineEnemyDetail retObj = new C2SMineEnemyDetail();
			try{
			retObj.level_index=request.readInt();
			retObj.point_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CMineEnemyDetail{
		private static final Logger logger = LoggerFactory.getLogger(S2CMineEnemyDetail.class);
		public int level_index;
		public int point_index;
		public IOMineHolder base_info;
		public IOBattlesetEnemy battleset;
		public List<Long> exclude_cards;
		public long rand_key;
		@Override
		public String toString() {
			return "S2CMineEnemyDetail [level_index="+level_index+",point_index="+point_index+",base_info="+base_info+",battleset="+battleset+",exclude_cards="+exclude_cards+",rand_key="+rand_key+",]";
		}
		public static final int msgCode = 3104;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3104);
			retMsg.writeInt(level_index);
			retMsg.writeInt(point_index);
			if(base_info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(base_info.rid);
					retMsg.writeString(base_info.rname);
					retMsg.writeInt(base_info.level);
					retMsg.writeInt(base_info.iconid);
					retMsg.writeInt(base_info.frameid);
					retMsg.writeInt(base_info.fight);
					retMsg.writeInt(base_info.cd_time);
			}
			if(battleset == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(battleset.mythic == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(battleset.mythic.tid);
					retMsg.writeInt(battleset.mythic.pclass);
					retMsg.writeInt(battleset.mythic.level);
					if(battleset.mythic.pskill == null || battleset.mythic.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset.mythic.pskill.size());
				for(Integer battleset_mythic_pskill1 : battleset.mythic.pskill){
			retMsg.writeInt(battleset_mythic_pskill1);
				}
			}
			}
					if(battleset.team == null || battleset.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset.team.size());
				for(Map.Entry<Integer,IOGeneralBean> battleset_team1 : battleset.team.entrySet()){
			retMsg.writeInt(battleset_team1.getKey());
					retMsg.writeLong(battleset_team1.getValue().guid);
					retMsg.writeInt(battleset_team1.getValue().gsid);
					retMsg.writeInt(battleset_team1.getValue().level);
					retMsg.writeInt(battleset_team1.getValue().star);
					retMsg.writeInt(battleset_team1.getValue().camp);
					retMsg.writeInt(battleset_team1.getValue().occu);
					retMsg.writeInt(battleset_team1.getValue().pclass);
					retMsg.writeInt(battleset_team1.getValue().power);
					if(battleset_team1.getValue().talent == null || battleset_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset_team1.getValue().talent.size());
				for(Integer battleset_team1_getValue_talent1 : battleset_team1.getValue().talent){
			retMsg.writeInt(battleset_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(battleset_team1.getValue().affairs);
					retMsg.writeInt(battleset_team1.getValue().treasure);
					if(battleset_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(battleset_team1.getValue().property.hp);
					retMsg.writeInt(battleset_team1.getValue().property.atk);
					retMsg.writeInt(battleset_team1.getValue().property.def);
					retMsg.writeInt(battleset_team1.getValue().property.mdef);
					retMsg.writeFloat(battleset_team1.getValue().property.atktime);
					retMsg.writeInt(battleset_team1.getValue().property.range);
					retMsg.writeInt(battleset_team1.getValue().property.msp);
					retMsg.writeInt(battleset_team1.getValue().property.pasp);
					retMsg.writeInt(battleset_team1.getValue().property.pcri);
					retMsg.writeInt(battleset_team1.getValue().property.pcrid);
					retMsg.writeInt(battleset_team1.getValue().property.pdam);
					retMsg.writeInt(battleset_team1.getValue().property.php);
					retMsg.writeInt(battleset_team1.getValue().property.patk);
					retMsg.writeInt(battleset_team1.getValue().property.pdef);
					retMsg.writeInt(battleset_team1.getValue().property.pmdef);
					retMsg.writeInt(battleset_team1.getValue().property.ppbs);
					retMsg.writeInt(battleset_team1.getValue().property.pmbs);
					retMsg.writeInt(battleset_team1.getValue().property.pefc);
					retMsg.writeInt(battleset_team1.getValue().property.ppthr);
					retMsg.writeInt(battleset_team1.getValue().property.patkdam);
					retMsg.writeInt(battleset_team1.getValue().property.pskidam);
					retMsg.writeInt(battleset_team1.getValue().property.pckatk);
					retMsg.writeInt(battleset_team1.getValue().property.pmthr);
					retMsg.writeInt(battleset_team1.getValue().property.pdex);
					retMsg.writeInt(battleset_team1.getValue().property.pmdex);
					retMsg.writeInt(battleset_team1.getValue().property.pmsatk);
					retMsg.writeInt(battleset_team1.getValue().property.pmps);
					retMsg.writeInt(battleset_team1.getValue().property.pcd);
			}
					if(battleset_team1.getValue().equip == null || battleset_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset_team1.getValue().equip.size());
				for(Integer battleset_team1_getValue_equip1 : battleset_team1.getValue().equip){
			retMsg.writeInt(battleset_team1_getValue_equip1);
				}
			}
					if(battleset_team1.getValue().skill == null || battleset_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset_team1.getValue().skill.size());
				for(Integer battleset_team1_getValue_skill1 : battleset_team1.getValue().skill){
			retMsg.writeInt(battleset_team1_getValue_skill1);
				}
			}
					if(battleset_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(battleset_team1.getValue().exclusive.level);
					if(battleset_team1.getValue().exclusive.skill == null || battleset_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset_team1.getValue().exclusive.skill.size());
				for(Integer battleset_team1_getValue_exclusive_skill1 : battleset_team1.getValue().exclusive.skill){
			retMsg.writeInt(battleset_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(battleset_team1.getValue().exclusive.gsid);
					if(battleset_team1.getValue().exclusive.property == null || battleset_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(battleset_team1.getValue().exclusive.property.size());
				for(KvStringPair battleset_team1_getValue_exclusive_property1 : battleset_team1.getValue().exclusive.property){
					retMsg.writeString(battleset_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(battleset_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(battleset_team1.getValue().hppercent);
				}
			}
			}
			if(exclude_cards == null || exclude_cards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(exclude_cards.size());
				for(Long exclude_cards1 : exclude_cards){
			retMsg.writeLong(exclude_cards1);
				}
			}
			retMsg.writeLong(rand_key);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class PushMineRob{
		private static final Logger logger = LoggerFactory.getLogger(PushMineRob.class);
		public int level_index;
		public int point_index;
		public int target_player_id;
		public String target_player_name;
		public List<IORewardItem> loose_items;
		@Override
		public String toString() {
			return "PushMineRob [level_index="+level_index+",point_index="+point_index+",target_player_id="+target_player_id+",target_player_name="+target_player_name+",loose_items="+loose_items+",]";
		}
		public static final int msgCode = 3106;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3106);
			retMsg.writeInt(level_index);
			retMsg.writeInt(point_index);
			retMsg.writeInt(target_player_id);
			retMsg.writeString(target_player_name);
			if(loose_items == null || loose_items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(loose_items.size());
				for(IORewardItem loose_items1 : loose_items){
					retMsg.writeInt(loose_items1.GSID);
					retMsg.writeInt(loose_items1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMineGetAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SMineGetAward.class);
		public static final int id = 3107;
	}
	public static final class S2CMineGetAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CMineGetAward.class);
		public List<IORewardItem> gain;
		@Override
		public String toString() {
			return "S2CMineGetAward [gain="+gain+",]";
		}
		public static final int msgCode = 3108;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3108);
			if(gain == null || gain.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(gain.size());
				for(IORewardItem gain1 : gain){
					retMsg.writeInt(gain1.GSID);
					retMsg.writeInt(gain1.COUNT);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMineHistory{
		private static final Logger logger = LoggerFactory.getLogger(C2SMineHistory.class);
		public static final int id = 3109;
	}
	public static final class S2CMineHistory{
		private static final Logger logger = LoggerFactory.getLogger(S2CMineHistory.class);
		public List<IOMineHistory> records;
		@Override
		public String toString() {
			return "S2CMineHistory [records="+records+",]";
		}
		public static final int msgCode = 3110;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3110);
			if(records == null || records.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records.size());
				for(IOMineHistory records1 : records){
					retMsg.writeInt(records1.target_player_id);
					retMsg.writeString(records1.target_player_name);
					retMsg.writeBool(records1.is_positive);
					retMsg.writeInt(records1.is_success);
					retMsg.writeInt(records1.mine_point);
					retMsg.writeInt(records1.type);
					retMsg.writeInt(records1.add_time);
					if(records1.gain == null || records1.gain.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.gain.size());
				for(Integer records1_gain1 : records1.gain){
			retMsg.writeInt(records1_gain1);
				}
			}
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMineDefFormationSave{
		private static final Logger logger = LoggerFactory.getLogger(C2SMineDefFormationSave.class);
		public int level_index;
		public int point_index;
		public int mythic;
		public IOFormationGeneralPos[] items;
		@Override
		public String toString() {
			return "C2SMineDefFormationSave [level_index="+level_index+",point_index="+point_index+",mythic="+mythic+",items="+java.util.Arrays.toString(items)+",]";
		}
		public static final int id = 3111;

		public static C2SMineDefFormationSave parse(MyRequestMessage request){
			C2SMineDefFormationSave retObj = new C2SMineDefFormationSave();
			try{
			retObj.level_index=request.readInt();
			retObj.point_index=request.readInt();
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
	public static final class S2CMineDefFormationSave{
		private static final Logger logger = LoggerFactory.getLogger(S2CMineDefFormationSave.class);
		public S2CMineEnemyDetail mine_point_detail;
		@Override
		public String toString() {
			return "S2CMineDefFormationSave [mine_point_detail="+mine_point_detail+",]";
		}
		public static final int msgCode = 3112;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3112);
			if(mine_point_detail == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(mine_point_detail.level_index);
					retMsg.writeInt(mine_point_detail.point_index);
					if(mine_point_detail.base_info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(mine_point_detail.base_info.rid);
					retMsg.writeString(mine_point_detail.base_info.rname);
					retMsg.writeInt(mine_point_detail.base_info.level);
					retMsg.writeInt(mine_point_detail.base_info.iconid);
					retMsg.writeInt(mine_point_detail.base_info.frameid);
					retMsg.writeInt(mine_point_detail.base_info.fight);
					retMsg.writeInt(mine_point_detail.base_info.cd_time);
			}
					if(mine_point_detail.battleset == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(mine_point_detail.battleset.mythic == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(mine_point_detail.battleset.mythic.tid);
					retMsg.writeInt(mine_point_detail.battleset.mythic.pclass);
					retMsg.writeInt(mine_point_detail.battleset.mythic.level);
					if(mine_point_detail.battleset.mythic.pskill == null || mine_point_detail.battleset.mythic.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mine_point_detail.battleset.mythic.pskill.size());
				for(Integer mine_point_detail_battleset_mythic_pskill1 : mine_point_detail.battleset.mythic.pskill){
			retMsg.writeInt(mine_point_detail_battleset_mythic_pskill1);
				}
			}
			}
					if(mine_point_detail.battleset.team == null || mine_point_detail.battleset.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mine_point_detail.battleset.team.size());
				for(Map.Entry<Integer,IOGeneralBean> mine_point_detail_battleset_team1 : mine_point_detail.battleset.team.entrySet()){
			retMsg.writeInt(mine_point_detail_battleset_team1.getKey());
					retMsg.writeLong(mine_point_detail_battleset_team1.getValue().guid);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().gsid);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().level);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().star);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().camp);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().occu);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().pclass);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().power);
					if(mine_point_detail_battleset_team1.getValue().talent == null || mine_point_detail_battleset_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mine_point_detail_battleset_team1.getValue().talent.size());
				for(Integer mine_point_detail_battleset_team1_getValue_talent1 : mine_point_detail_battleset_team1.getValue().talent){
			retMsg.writeInt(mine_point_detail_battleset_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().affairs);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().treasure);
					if(mine_point_detail_battleset_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.hp);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.atk);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.def);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.mdef);
					retMsg.writeFloat(mine_point_detail_battleset_team1.getValue().property.atktime);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.range);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.msp);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pasp);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pcri);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pcrid);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pdam);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.php);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.patk);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pdef);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pmdef);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.ppbs);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pmbs);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pefc);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.ppthr);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.patkdam);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pskidam);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pckatk);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pmthr);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pdex);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pmdex);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pmsatk);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pmps);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().property.pcd);
			}
					if(mine_point_detail_battleset_team1.getValue().equip == null || mine_point_detail_battleset_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mine_point_detail_battleset_team1.getValue().equip.size());
				for(Integer mine_point_detail_battleset_team1_getValue_equip1 : mine_point_detail_battleset_team1.getValue().equip){
			retMsg.writeInt(mine_point_detail_battleset_team1_getValue_equip1);
				}
			}
					if(mine_point_detail_battleset_team1.getValue().skill == null || mine_point_detail_battleset_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mine_point_detail_battleset_team1.getValue().skill.size());
				for(Integer mine_point_detail_battleset_team1_getValue_skill1 : mine_point_detail_battleset_team1.getValue().skill){
			retMsg.writeInt(mine_point_detail_battleset_team1_getValue_skill1);
				}
			}
					if(mine_point_detail_battleset_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().exclusive.level);
					if(mine_point_detail_battleset_team1.getValue().exclusive.skill == null || mine_point_detail_battleset_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mine_point_detail_battleset_team1.getValue().exclusive.skill.size());
				for(Integer mine_point_detail_battleset_team1_getValue_exclusive_skill1 : mine_point_detail_battleset_team1.getValue().exclusive.skill){
			retMsg.writeInt(mine_point_detail_battleset_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().exclusive.gsid);
					if(mine_point_detail_battleset_team1.getValue().exclusive.property == null || mine_point_detail_battleset_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mine_point_detail_battleset_team1.getValue().exclusive.property.size());
				for(KvStringPair mine_point_detail_battleset_team1_getValue_exclusive_property1 : mine_point_detail_battleset_team1.getValue().exclusive.property){
					retMsg.writeString(mine_point_detail_battleset_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(mine_point_detail_battleset_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(mine_point_detail_battleset_team1.getValue().hppercent);
				}
			}
			}
					if(mine_point_detail.exclude_cards == null || mine_point_detail.exclude_cards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mine_point_detail.exclude_cards.size());
				for(Long mine_point_detail_exclude_cards1 : mine_point_detail.exclude_cards){
			retMsg.writeLong(mine_point_detail_exclude_cards1);
				}
			}
					retMsg.writeLong(mine_point_detail.rand_key);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMineRedNotice{
		private static final Logger logger = LoggerFactory.getLogger(C2SMineRedNotice.class);
		public static final int id = 3113;
	}
	public static final class S2CMineRedNotice{
		private static final Logger logger = LoggerFactory.getLogger(S2CMineRedNotice.class);
		public boolean ret;
		public S2CMineRedNotice(boolean pret){
			ret=pret;
		}
		public S2CMineRedNotice(){}
		@Override
		public String toString() {
			return "S2CMineRedNotice [ret="+ret+",]";
		}
		public static final int msgCode = 3114;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3114);
			retMsg.writeBool(ret);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
