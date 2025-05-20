package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.Map;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOGuozhanPointPlayer;
import ws.WsMessageBase.IOBattlesetEnemy;
import ws.WsMessageBase.IOMythicalAnimal;
import ws.WsMessageBase.IOGeneralBean;
import ws.WsMessageBase.IOProperty;
import ws.WsMessageBase.IOExclusive;
import ws.WsMessageBase.KvStringPair;
import ws.WsMessageBase.IORewardItem;
import ws.WsMessageBase.GuozhanOfficePointPlayer;
import ws.WsMessageBase.IOGuoZhanCity;
import ws.WsMessageBase.IOGuoZhanPvpPlayer;
import ws.WsMessageBase.IOGuozhanHistory;

public final class WsMessageGuozhan{
	public static final class C2SGuozhanView{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanView.class);
		public static final int id = 3451;
	}
	public static final class S2CGuozhanView{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanView.class);
		public List<Integer> pass_city_index;
		public int player_city_index;
		public int my_nation;
		public int change_nation_cd;
		public int my_office;
		@Override
		public String toString() {
			return "S2CGuozhanView [pass_city_index="+pass_city_index+",player_city_index="+player_city_index+",my_nation="+my_nation+",change_nation_cd="+change_nation_cd+",my_office="+my_office+",]";
		}
		public static final int msgCode = 3452;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3452);
			if(pass_city_index == null || pass_city_index.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(pass_city_index.size());
				for(Integer pass_city_index1 : pass_city_index){
			retMsg.writeInt(pass_city_index1);
				}
			}
			retMsg.writeInt(player_city_index);
			retMsg.writeInt(my_nation);
			retMsg.writeInt(change_nation_cd);
			retMsg.writeInt(my_office);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuozhanBattleView{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanBattleView.class);
		public int city_index;
		@Override
		public String toString() {
			return "C2SGuozhanBattleView [city_index="+city_index+",]";
		}
		public static final int id = 3453;

		public static C2SGuozhanBattleView parse(MyRequestMessage request){
			C2SGuozhanBattleView retObj = new C2SGuozhanBattleView();
			try{
			retObj.city_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuozhanBattleView{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanBattleView.class);
		public int city_index;
		public int enemy_level;
		public IOGuozhanPointPlayer base_info;
		public IOBattlesetEnemy battleset;
		@Override
		public String toString() {
			return "S2CGuozhanBattleView [city_index="+city_index+",enemy_level="+enemy_level+",base_info="+base_info+",battleset="+battleset+",]";
		}
		public static final int msgCode = 3454;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3454);
			retMsg.writeInt(city_index);
			retMsg.writeInt(enemy_level);
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
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuozhanMove{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanMove.class);
		public int city_index;
		@Override
		public String toString() {
			return "C2SGuozhanMove [city_index="+city_index+",]";
		}
		public static final int id = 3455;

		public static C2SGuozhanMove parse(MyRequestMessage request){
			C2SGuozhanMove retObj = new C2SGuozhanMove();
			try{
			retObj.city_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuozhanMove{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanMove.class);
		public int city_index;
		public S2CGuozhanMove(int pcity_index){
			city_index=pcity_index;
		}
		public S2CGuozhanMove(){}
		@Override
		public String toString() {
			return "S2CGuozhanMove [city_index="+city_index+",]";
		}
		public static final int msgCode = 3456;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3456);
			retMsg.writeInt(city_index);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuozhanChangeNation{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanChangeNation.class);
		public int target_nation;
		@Override
		public String toString() {
			return "C2SGuozhanChangeNation [target_nation="+target_nation+",]";
		}
		public static final int id = 3457;

		public static C2SGuozhanChangeNation parse(MyRequestMessage request){
			C2SGuozhanChangeNation retObj = new C2SGuozhanChangeNation();
			try{
			retObj.target_nation=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuozhanChangeNation{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanChangeNation.class);
		public int old_nation;
		public int target_nation;
		public int change_nation_cd;
		public S2CGuozhanChangeNation(int pold_nation,int ptarget_nation,int pchange_nation_cd){
			old_nation=pold_nation;
			target_nation=ptarget_nation;
			change_nation_cd=pchange_nation_cd;
		}
		public S2CGuozhanChangeNation(){}
		@Override
		public String toString() {
			return "S2CGuozhanChangeNation [old_nation="+old_nation+",target_nation="+target_nation+",change_nation_cd="+change_nation_cd+",]";
		}
		public static final int msgCode = 3458;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3458);
			retMsg.writeInt(old_nation);
			retMsg.writeInt(target_nation);
			retMsg.writeInt(change_nation_cd);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuozhanOfficeView{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanOfficeView.class);
		public static final int id = 3459;
	}
	public static final class S2CGuozhanOfficeView{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanOfficeView.class);
		public List<GuozhanOfficePointPlayer> player_list;
		@Override
		public String toString() {
			return "S2CGuozhanOfficeView [player_list="+player_list+",]";
		}
		public static final int msgCode = 3460;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3460);
			if(player_list == null || player_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(player_list.size());
				for(GuozhanOfficePointPlayer player_list1 : player_list){
					retMsg.writeInt(player_list1.office_index);
					retMsg.writeInt(player_list1.rid);
					retMsg.writeString(player_list1.rname);
					retMsg.writeInt(player_list1.level);
					retMsg.writeInt(player_list1.iconid);
					retMsg.writeInt(player_list1.frameid);
					retMsg.writeInt(player_list1.fight);
					retMsg.writeInt(player_list1.hp_perc);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuozhanOfficeDetail{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanOfficeDetail.class);
		public int office_index;
		@Override
		public String toString() {
			return "C2SGuozhanOfficeDetail [office_index="+office_index+",]";
		}
		public static final int id = 3461;

		public static C2SGuozhanOfficeDetail parse(MyRequestMessage request){
			C2SGuozhanOfficeDetail retObj = new C2SGuozhanOfficeDetail();
			try{
			retObj.office_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuozhanOfficeDetail{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanOfficeDetail.class);
		public int office_index;
		public GuozhanOfficePointPlayer base_info;
		public IOBattlesetEnemy battleset;
		@Override
		public String toString() {
			return "S2CGuozhanOfficeDetail [office_index="+office_index+",base_info="+base_info+",battleset="+battleset+",]";
		}
		public static final int msgCode = 3462;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3462);
			retMsg.writeInt(office_index);
			if(base_info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(base_info.office_index);
					retMsg.writeInt(base_info.rid);
					retMsg.writeString(base_info.rname);
					retMsg.writeInt(base_info.level);
					retMsg.writeInt(base_info.iconid);
					retMsg.writeInt(base_info.frameid);
					retMsg.writeInt(base_info.fight);
					retMsg.writeInt(base_info.hp_perc);
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
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuozhanFightView{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanFightView.class);
		public static final int id = 3463;
	}
	public static final class S2CGuozhanFightView{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanFightView.class);
		public List<IOGuoZhanCity> city_list;
		public int my_city_index;
		public int move_step;
		public List<Integer> nation_city_count;
		public int hp_perc;
		public int my_nation;
		public int change_nation_cd;
		public int my_office;
		@Override
		public String toString() {
			return "S2CGuozhanFightView [city_list="+city_list+",my_city_index="+my_city_index+",move_step="+move_step+",nation_city_count="+nation_city_count+",hp_perc="+hp_perc+",my_nation="+my_nation+",change_nation_cd="+change_nation_cd+",my_office="+my_office+",]";
		}
		public static final int msgCode = 3464;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3464);
			if(city_list == null || city_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(city_list.size());
				for(IOGuoZhanCity city_list1 : city_list){
					retMsg.writeInt(city_list1.player_id);
					retMsg.writeString(city_list1.player_name);
					retMsg.writeInt(city_list1.player_size);
					retMsg.writeInt(city_list1.nation_id);
					retMsg.writeBool(city_list1.in_battle);
				}
			}
			retMsg.writeInt(my_city_index);
			retMsg.writeInt(move_step);
			if(nation_city_count == null || nation_city_count.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(nation_city_count.size());
				for(Integer nation_city_count1 : nation_city_count){
			retMsg.writeInt(nation_city_count1);
				}
			}
			retMsg.writeInt(hp_perc);
			retMsg.writeInt(my_nation);
			retMsg.writeInt(change_nation_cd);
			retMsg.writeInt(my_office);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuoZhanCityDetail{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuoZhanCityDetail.class);
		public int city_index;
		@Override
		public String toString() {
			return "C2SGuoZhanCityDetail [city_index="+city_index+",]";
		}
		public static final int id = 3465;

		public static C2SGuoZhanCityDetail parse(MyRequestMessage request){
			C2SGuoZhanCityDetail retObj = new C2SGuoZhanCityDetail();
			try{
			retObj.city_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuoZhanCityDetail{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuoZhanCityDetail.class);
		public int city_index;
		public List<IOGuoZhanPvpPlayer> players;
		public int my_hp_perc;
		@Override
		public String toString() {
			return "S2CGuoZhanCityDetail [city_index="+city_index+",players="+players+",my_hp_perc="+my_hp_perc+",]";
		}
		public static final int msgCode = 3466;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3466);
			retMsg.writeInt(city_index);
			if(players == null || players.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(players.size());
				for(IOGuoZhanPvpPlayer players1 : players){
					if(players1.base_info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(players1.base_info.office_index);
					retMsg.writeInt(players1.base_info.rid);
					retMsg.writeString(players1.base_info.rname);
					retMsg.writeInt(players1.base_info.level);
					retMsg.writeInt(players1.base_info.iconid);
					retMsg.writeInt(players1.base_info.frameid);
					retMsg.writeInt(players1.base_info.fight);
					retMsg.writeInt(players1.base_info.hp_perc);
			}
					if(players1.battleset == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					if(players1.battleset.mythic == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(players1.battleset.mythic.tid);
					retMsg.writeInt(players1.battleset.mythic.pclass);
					retMsg.writeInt(players1.battleset.mythic.level);
					if(players1.battleset.mythic.pskill == null || players1.battleset.mythic.pskill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(players1.battleset.mythic.pskill.size());
				for(Integer players1_battleset_mythic_pskill1 : players1.battleset.mythic.pskill){
			retMsg.writeInt(players1_battleset_mythic_pskill1);
				}
			}
			}
					if(players1.battleset.team == null || players1.battleset.team.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(players1.battleset.team.size());
				for(Map.Entry<Integer,IOGeneralBean> players1_battleset_team1 : players1.battleset.team.entrySet()){
			retMsg.writeInt(players1_battleset_team1.getKey());
					retMsg.writeLong(players1_battleset_team1.getValue().guid);
					retMsg.writeInt(players1_battleset_team1.getValue().gsid);
					retMsg.writeInt(players1_battleset_team1.getValue().level);
					retMsg.writeInt(players1_battleset_team1.getValue().star);
					retMsg.writeInt(players1_battleset_team1.getValue().camp);
					retMsg.writeInt(players1_battleset_team1.getValue().occu);
					retMsg.writeInt(players1_battleset_team1.getValue().pclass);
					retMsg.writeInt(players1_battleset_team1.getValue().power);
					if(players1_battleset_team1.getValue().talent == null || players1_battleset_team1.getValue().talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(players1_battleset_team1.getValue().talent.size());
				for(Integer players1_battleset_team1_getValue_talent1 : players1_battleset_team1.getValue().talent){
			retMsg.writeInt(players1_battleset_team1_getValue_talent1);
				}
			}
					retMsg.writeInt(players1_battleset_team1.getValue().affairs);
					retMsg.writeInt(players1_battleset_team1.getValue().treasure);
					if(players1_battleset_team1.getValue().property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(players1_battleset_team1.getValue().property.hp);
					retMsg.writeInt(players1_battleset_team1.getValue().property.atk);
					retMsg.writeInt(players1_battleset_team1.getValue().property.def);
					retMsg.writeInt(players1_battleset_team1.getValue().property.mdef);
					retMsg.writeFloat(players1_battleset_team1.getValue().property.atktime);
					retMsg.writeInt(players1_battleset_team1.getValue().property.range);
					retMsg.writeInt(players1_battleset_team1.getValue().property.msp);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pasp);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pcri);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pcrid);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pdam);
					retMsg.writeInt(players1_battleset_team1.getValue().property.php);
					retMsg.writeInt(players1_battleset_team1.getValue().property.patk);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pdef);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pmdef);
					retMsg.writeInt(players1_battleset_team1.getValue().property.ppbs);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pmbs);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pefc);
					retMsg.writeInt(players1_battleset_team1.getValue().property.ppthr);
					retMsg.writeInt(players1_battleset_team1.getValue().property.patkdam);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pskidam);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pckatk);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pmthr);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pdex);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pmdex);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pmsatk);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pmps);
					retMsg.writeInt(players1_battleset_team1.getValue().property.pcd);
			}
					if(players1_battleset_team1.getValue().equip == null || players1_battleset_team1.getValue().equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(players1_battleset_team1.getValue().equip.size());
				for(Integer players1_battleset_team1_getValue_equip1 : players1_battleset_team1.getValue().equip){
			retMsg.writeInt(players1_battleset_team1_getValue_equip1);
				}
			}
					if(players1_battleset_team1.getValue().skill == null || players1_battleset_team1.getValue().skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(players1_battleset_team1.getValue().skill.size());
				for(Integer players1_battleset_team1_getValue_skill1 : players1_battleset_team1.getValue().skill){
			retMsg.writeInt(players1_battleset_team1_getValue_skill1);
				}
			}
					if(players1_battleset_team1.getValue().exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(players1_battleset_team1.getValue().exclusive.level);
					if(players1_battleset_team1.getValue().exclusive.skill == null || players1_battleset_team1.getValue().exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(players1_battleset_team1.getValue().exclusive.skill.size());
				for(Integer players1_battleset_team1_getValue_exclusive_skill1 : players1_battleset_team1.getValue().exclusive.skill){
			retMsg.writeInt(players1_battleset_team1_getValue_exclusive_skill1);
				}
			}
					retMsg.writeInt(players1_battleset_team1.getValue().exclusive.gsid);
					if(players1_battleset_team1.getValue().exclusive.property == null || players1_battleset_team1.getValue().exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(players1_battleset_team1.getValue().exclusive.property.size());
				for(KvStringPair players1_battleset_team1_getValue_exclusive_property1 : players1_battleset_team1.getValue().exclusive.property){
					retMsg.writeString(players1_battleset_team1_getValue_exclusive_property1.key);
					retMsg.writeInt(players1_battleset_team1_getValue_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(players1_battleset_team1.getValue().hppercent);
				}
			}
			}
				}
			}
			retMsg.writeInt(my_hp_perc);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuoZhanCityMove{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuoZhanCityMove.class);
		public int city_index;
		@Override
		public String toString() {
			return "C2SGuoZhanCityMove [city_index="+city_index+",]";
		}
		public static final int id = 3467;

		public static C2SGuoZhanCityMove parse(MyRequestMessage request){
			C2SGuoZhanCityMove retObj = new C2SGuoZhanCityMove();
			try{
			retObj.city_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuoZhanCityMove{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuoZhanCityMove.class);
		public int city_index;
		public int move_step;
		public boolean occupy_enemy;
		public List<IORewardItem> reward;
		@Override
		public String toString() {
			return "S2CGuoZhanCityMove [city_index="+city_index+",move_step="+move_step+",occupy_enemy="+occupy_enemy+",reward="+reward+",]";
		}
		public static final int msgCode = 3468;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3468);
			retMsg.writeInt(city_index);
			retMsg.writeInt(move_step);
			retMsg.writeBool(occupy_enemy);
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
	public static final class PushGuoZhanPass{
		private static final Logger logger = LoggerFactory.getLogger(PushGuoZhanPass.class);
		public int nation_id;
		public PushGuoZhanPass(int pnation_id){
			nation_id=pnation_id;
		}
		public PushGuoZhanPass(){}
		@Override
		public String toString() {
			return "PushGuoZhanPass [nation_id="+nation_id+",]";
		}
		public static final int msgCode = 3470;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3470);
			retMsg.writeInt(nation_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuozhanHistory{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuozhanHistory.class);
		public static final int id = 3471;
	}
	public static final class S2CGuozhanHistory{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuozhanHistory.class);
		public List<IOGuozhanHistory> records;
		@Override
		public String toString() {
			return "S2CGuozhanHistory [records="+records+",]";
		}
		public static final int msgCode = 3472;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3472);
			if(records == null || records.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records.size());
				for(IOGuozhanHistory records1 : records){
					retMsg.writeInt(records1.action_type);
					retMsg.writeString(records1.target_player_name);
					if(records1.params == null || records1.params.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(records1.params.size());
				for(Integer records1_params1 : records1.params){
			retMsg.writeInt(records1_params1);
				}
			}
					retMsg.writeInt(records1.add_time);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
