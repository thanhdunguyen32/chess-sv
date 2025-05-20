package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOGeneralBean;
import ws.WsMessageBase.IOProperty;
import ws.WsMessageBase.IOExclusive;
import ws.WsMessageBase.KvStringPair;
import ws.WsMessageBase.RewardInfo;
import ws.WsMessageBase.IOAwardRandomGeneral;

public final class WsMessageHero{
	public static final class C2SHeroList{
		private static final Logger logger = LoggerFactory.getLogger(C2SHeroList.class);
		public static final int id = 351;
	}
	public static final class S2CHeroList{
		private static final Logger logger = LoggerFactory.getLogger(S2CHeroList.class);
		public List<IOGeneralBean> generals;
		@Override
		public String toString() {
			return "S2CHeroList [generals="+generals+",]";
		}
		public static final int msgCode = 352;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 352);
			if(generals == null || generals.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(generals.size());
				for(IOGeneralBean generals1 : generals){
					retMsg.writeLong(generals1.guid);
					retMsg.writeInt(generals1.gsid);
					retMsg.writeInt(generals1.level);
					retMsg.writeInt(generals1.star);
					retMsg.writeInt(generals1.camp);
					retMsg.writeInt(generals1.occu);
					retMsg.writeInt(generals1.pclass);
					retMsg.writeInt(generals1.power);
					if(generals1.talent == null || generals1.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(generals1.talent.size());
				for(Integer generals1_talent1 : generals1.talent){
			retMsg.writeInt(generals1_talent1);
				}
			}
					retMsg.writeInt(generals1.affairs);
					retMsg.writeInt(generals1.treasure);
					if(generals1.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(generals1.property.hp);
					retMsg.writeInt(generals1.property.atk);
					retMsg.writeInt(generals1.property.def);
					retMsg.writeInt(generals1.property.mdef);
					retMsg.writeFloat(generals1.property.atktime);
					retMsg.writeInt(generals1.property.range);
					retMsg.writeInt(generals1.property.msp);
					retMsg.writeInt(generals1.property.pasp);
					retMsg.writeInt(generals1.property.pcri);
					retMsg.writeInt(generals1.property.pcrid);
					retMsg.writeInt(generals1.property.pdam);
					retMsg.writeInt(generals1.property.php);
					retMsg.writeInt(generals1.property.patk);
					retMsg.writeInt(generals1.property.pdef);
					retMsg.writeInt(generals1.property.pmdef);
					retMsg.writeInt(generals1.property.ppbs);
					retMsg.writeInt(generals1.property.pmbs);
					retMsg.writeInt(generals1.property.pefc);
					retMsg.writeInt(generals1.property.ppthr);
					retMsg.writeInt(generals1.property.patkdam);
					retMsg.writeInt(generals1.property.pskidam);
					retMsg.writeInt(generals1.property.pckatk);
					retMsg.writeInt(generals1.property.pmthr);
					retMsg.writeInt(generals1.property.pdex);
					retMsg.writeInt(generals1.property.pmdex);
					retMsg.writeInt(generals1.property.pmsatk);
					retMsg.writeInt(generals1.property.pmps);
					retMsg.writeInt(generals1.property.pcd);
			}
					if(generals1.equip == null || generals1.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(generals1.equip.size());
				for(Integer generals1_equip1 : generals1.equip){
			retMsg.writeInt(generals1_equip1);
				}
			}
					if(generals1.skill == null || generals1.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(generals1.skill.size());
				for(Integer generals1_skill1 : generals1.skill){
			retMsg.writeInt(generals1_skill1);
				}
			}
					if(generals1.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(generals1.exclusive.level);
					if(generals1.exclusive.skill == null || generals1.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(generals1.exclusive.skill.size());
				for(Integer generals1_exclusive_skill1 : generals1.exclusive.skill){
			retMsg.writeInt(generals1_exclusive_skill1);
				}
			}
					retMsg.writeInt(generals1.exclusive.gsid);
					if(generals1.exclusive.property == null || generals1.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(generals1.exclusive.property.size());
				for(KvStringPair generals1_exclusive_property1 : generals1.exclusive.property){
					retMsg.writeString(generals1_exclusive_property1.key);
					retMsg.writeInt(generals1_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(generals1.hppercent);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class PushAddGeneral{
		private static final Logger logger = LoggerFactory.getLogger(PushAddGeneral.class);
		public String action;
		public IOGeneralBean general_info;
		@Override
		public String toString() {
			return "PushAddGeneral [action="+action+",general_info="+general_info+",]";
		}
		public static final int msgCode = 354;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 354);
			retMsg.writeString(action);
			if(general_info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(general_info.guid);
					retMsg.writeInt(general_info.gsid);
					retMsg.writeInt(general_info.level);
					retMsg.writeInt(general_info.star);
					retMsg.writeInt(general_info.camp);
					retMsg.writeInt(general_info.occu);
					retMsg.writeInt(general_info.pclass);
					retMsg.writeInt(general_info.power);
					if(general_info.talent == null || general_info.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_info.talent.size());
				for(Integer general_info_talent1 : general_info.talent){
			retMsg.writeInt(general_info_talent1);
				}
			}
					retMsg.writeInt(general_info.affairs);
					retMsg.writeInt(general_info.treasure);
					if(general_info.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_info.property.hp);
					retMsg.writeInt(general_info.property.atk);
					retMsg.writeInt(general_info.property.def);
					retMsg.writeInt(general_info.property.mdef);
					retMsg.writeFloat(general_info.property.atktime);
					retMsg.writeInt(general_info.property.range);
					retMsg.writeInt(general_info.property.msp);
					retMsg.writeInt(general_info.property.pasp);
					retMsg.writeInt(general_info.property.pcri);
					retMsg.writeInt(general_info.property.pcrid);
					retMsg.writeInt(general_info.property.pdam);
					retMsg.writeInt(general_info.property.php);
					retMsg.writeInt(general_info.property.patk);
					retMsg.writeInt(general_info.property.pdef);
					retMsg.writeInt(general_info.property.pmdef);
					retMsg.writeInt(general_info.property.ppbs);
					retMsg.writeInt(general_info.property.pmbs);
					retMsg.writeInt(general_info.property.pefc);
					retMsg.writeInt(general_info.property.ppthr);
					retMsg.writeInt(general_info.property.patkdam);
					retMsg.writeInt(general_info.property.pskidam);
					retMsg.writeInt(general_info.property.pckatk);
					retMsg.writeInt(general_info.property.pmthr);
					retMsg.writeInt(general_info.property.pdex);
					retMsg.writeInt(general_info.property.pmdex);
					retMsg.writeInt(general_info.property.pmsatk);
					retMsg.writeInt(general_info.property.pmps);
					retMsg.writeInt(general_info.property.pcd);
			}
					if(general_info.equip == null || general_info.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_info.equip.size());
				for(Integer general_info_equip1 : general_info.equip){
			retMsg.writeInt(general_info_equip1);
				}
			}
					if(general_info.skill == null || general_info.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_info.skill.size());
				for(Integer general_info_skill1 : general_info.skill){
			retMsg.writeInt(general_info_skill1);
				}
			}
					if(general_info.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_info.exclusive.level);
					if(general_info.exclusive.skill == null || general_info.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_info.exclusive.skill.size());
				for(Integer general_info_exclusive_skill1 : general_info.exclusive.skill){
			retMsg.writeInt(general_info_exclusive_skill1);
				}
			}
					retMsg.writeInt(general_info.exclusive.gsid);
					if(general_info.exclusive.property == null || general_info.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_info.exclusive.property.size());
				for(KvStringPair general_info_exclusive_property1 : general_info.exclusive.property){
					retMsg.writeString(general_info_exclusive_property1.key);
					retMsg.writeInt(general_info_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(general_info.hppercent);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGeneralTakeonEquip{
		private static final Logger logger = LoggerFactory.getLogger(C2SGeneralTakeonEquip.class);
		public int action_type;
		public long general_uuid;
		public int equip_id;
		public int pos_index;
		@Override
		public String toString() {
			return "C2SGeneralTakeonEquip [action_type="+action_type+",general_uuid="+general_uuid+",equip_id="+equip_id+",pos_index="+pos_index+",]";
		}
		public static final int id = 355;

		public static C2SGeneralTakeonEquip parse(MyRequestMessage request){
			C2SGeneralTakeonEquip retObj = new C2SGeneralTakeonEquip();
			try{
			retObj.action_type=request.readInt();
			retObj.general_uuid=request.readLong();
			retObj.equip_id=request.readInt();
			retObj.pos_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGeneralTakeonEquip{
		private static final Logger logger = LoggerFactory.getLogger(S2CGeneralTakeonEquip.class);
		public int action_type;
		public long general_uuid;
		public int equip_id;
		public int pos_index;
		public IOGeneralBean general_bean;
		@Override
		public String toString() {
			return "S2CGeneralTakeonEquip [action_type="+action_type+",general_uuid="+general_uuid+",equip_id="+equip_id+",pos_index="+pos_index+",general_bean="+general_bean+",]";
		}
		public static final int msgCode = 356;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 356);
			retMsg.writeInt(action_type);
			retMsg.writeLong(general_uuid);
			retMsg.writeInt(equip_id);
			retMsg.writeInt(pos_index);
			if(general_bean == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(general_bean.guid);
					retMsg.writeInt(general_bean.gsid);
					retMsg.writeInt(general_bean.level);
					retMsg.writeInt(general_bean.star);
					retMsg.writeInt(general_bean.camp);
					retMsg.writeInt(general_bean.occu);
					retMsg.writeInt(general_bean.pclass);
					retMsg.writeInt(general_bean.power);
					if(general_bean.talent == null || general_bean.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.talent.size());
				for(Integer general_bean_talent1 : general_bean.talent){
			retMsg.writeInt(general_bean_talent1);
				}
			}
					retMsg.writeInt(general_bean.affairs);
					retMsg.writeInt(general_bean.treasure);
					if(general_bean.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.property.hp);
					retMsg.writeInt(general_bean.property.atk);
					retMsg.writeInt(general_bean.property.def);
					retMsg.writeInt(general_bean.property.mdef);
					retMsg.writeFloat(general_bean.property.atktime);
					retMsg.writeInt(general_bean.property.range);
					retMsg.writeInt(general_bean.property.msp);
					retMsg.writeInt(general_bean.property.pasp);
					retMsg.writeInt(general_bean.property.pcri);
					retMsg.writeInt(general_bean.property.pcrid);
					retMsg.writeInt(general_bean.property.pdam);
					retMsg.writeInt(general_bean.property.php);
					retMsg.writeInt(general_bean.property.patk);
					retMsg.writeInt(general_bean.property.pdef);
					retMsg.writeInt(general_bean.property.pmdef);
					retMsg.writeInt(general_bean.property.ppbs);
					retMsg.writeInt(general_bean.property.pmbs);
					retMsg.writeInt(general_bean.property.pefc);
					retMsg.writeInt(general_bean.property.ppthr);
					retMsg.writeInt(general_bean.property.patkdam);
					retMsg.writeInt(general_bean.property.pskidam);
					retMsg.writeInt(general_bean.property.pckatk);
					retMsg.writeInt(general_bean.property.pmthr);
					retMsg.writeInt(general_bean.property.pdex);
					retMsg.writeInt(general_bean.property.pmdex);
					retMsg.writeInt(general_bean.property.pmsatk);
					retMsg.writeInt(general_bean.property.pmps);
					retMsg.writeInt(general_bean.property.pcd);
			}
					if(general_bean.equip == null || general_bean.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.equip.size());
				for(Integer general_bean_equip1 : general_bean.equip){
			retMsg.writeInt(general_bean_equip1);
				}
			}
					if(general_bean.skill == null || general_bean.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.skill.size());
				for(Integer general_bean_skill1 : general_bean.skill){
			retMsg.writeInt(general_bean_skill1);
				}
			}
					if(general_bean.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.exclusive.level);
					if(general_bean.exclusive.skill == null || general_bean.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.skill.size());
				for(Integer general_bean_exclusive_skill1 : general_bean.exclusive.skill){
			retMsg.writeInt(general_bean_exclusive_skill1);
				}
			}
					retMsg.writeInt(general_bean.exclusive.gsid);
					if(general_bean.exclusive.property == null || general_bean.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.property.size());
				for(KvStringPair general_bean_exclusive_property1 : general_bean.exclusive.property){
					retMsg.writeString(general_bean_exclusive_property1.key);
					retMsg.writeInt(general_bean_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(general_bean.hppercent);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGeneralAddLevel{
		private static final Logger logger = LoggerFactory.getLogger(C2SGeneralAddLevel.class);
		public long general_uuid;
		@Override
		public String toString() {
			return "C2SGeneralAddLevel [general_uuid="+general_uuid+",]";
		}
		public static final int id = 357;

		public static C2SGeneralAddLevel parse(MyRequestMessage request){
			C2SGeneralAddLevel retObj = new C2SGeneralAddLevel();
			try{
			retObj.general_uuid=request.readLong();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGeneralAddLevel{
		private static final Logger logger = LoggerFactory.getLogger(S2CGeneralAddLevel.class);
		public long general_uuid;
		public IOGeneralBean general_bean;
		@Override
		public String toString() {
			return "S2CGeneralAddLevel [general_uuid="+general_uuid+",general_bean="+general_bean+",]";
		}
		public static final int msgCode = 358;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 358);
			retMsg.writeLong(general_uuid);
			if(general_bean == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(general_bean.guid);
					retMsg.writeInt(general_bean.gsid);
					retMsg.writeInt(general_bean.level);
					retMsg.writeInt(general_bean.star);
					retMsg.writeInt(general_bean.camp);
					retMsg.writeInt(general_bean.occu);
					retMsg.writeInt(general_bean.pclass);
					retMsg.writeInt(general_bean.power);
					if(general_bean.talent == null || general_bean.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.talent.size());
				for(Integer general_bean_talent1 : general_bean.talent){
			retMsg.writeInt(general_bean_talent1);
				}
			}
					retMsg.writeInt(general_bean.affairs);
					retMsg.writeInt(general_bean.treasure);
					if(general_bean.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.property.hp);
					retMsg.writeInt(general_bean.property.atk);
					retMsg.writeInt(general_bean.property.def);
					retMsg.writeInt(general_bean.property.mdef);
					retMsg.writeFloat(general_bean.property.atktime);
					retMsg.writeInt(general_bean.property.range);
					retMsg.writeInt(general_bean.property.msp);
					retMsg.writeInt(general_bean.property.pasp);
					retMsg.writeInt(general_bean.property.pcri);
					retMsg.writeInt(general_bean.property.pcrid);
					retMsg.writeInt(general_bean.property.pdam);
					retMsg.writeInt(general_bean.property.php);
					retMsg.writeInt(general_bean.property.patk);
					retMsg.writeInt(general_bean.property.pdef);
					retMsg.writeInt(general_bean.property.pmdef);
					retMsg.writeInt(general_bean.property.ppbs);
					retMsg.writeInt(general_bean.property.pmbs);
					retMsg.writeInt(general_bean.property.pefc);
					retMsg.writeInt(general_bean.property.ppthr);
					retMsg.writeInt(general_bean.property.patkdam);
					retMsg.writeInt(general_bean.property.pskidam);
					retMsg.writeInt(general_bean.property.pckatk);
					retMsg.writeInt(general_bean.property.pmthr);
					retMsg.writeInt(general_bean.property.pdex);
					retMsg.writeInt(general_bean.property.pmdex);
					retMsg.writeInt(general_bean.property.pmsatk);
					retMsg.writeInt(general_bean.property.pmps);
					retMsg.writeInt(general_bean.property.pcd);
			}
					if(general_bean.equip == null || general_bean.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.equip.size());
				for(Integer general_bean_equip1 : general_bean.equip){
			retMsg.writeInt(general_bean_equip1);
				}
			}
					if(general_bean.skill == null || general_bean.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.skill.size());
				for(Integer general_bean_skill1 : general_bean.skill){
			retMsg.writeInt(general_bean_skill1);
				}
			}
					if(general_bean.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.exclusive.level);
					if(general_bean.exclusive.skill == null || general_bean.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.skill.size());
				for(Integer general_bean_exclusive_skill1 : general_bean.exclusive.skill){
			retMsg.writeInt(general_bean_exclusive_skill1);
				}
			}
					retMsg.writeInt(general_bean.exclusive.gsid);
					if(general_bean.exclusive.property == null || general_bean.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.property.size());
				for(KvStringPair general_bean_exclusive_property1 : general_bean.exclusive.property){
					retMsg.writeString(general_bean_exclusive_property1.key);
					retMsg.writeInt(general_bean_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(general_bean.hppercent);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGeneralAddClass{
		private static final Logger logger = LoggerFactory.getLogger(C2SGeneralAddClass.class);
		public long general_uuid;
		@Override
		public String toString() {
			return "C2SGeneralAddClass [general_uuid="+general_uuid+",]";
		}
		public static final int id = 359;

		public static C2SGeneralAddClass parse(MyRequestMessage request){
			C2SGeneralAddClass retObj = new C2SGeneralAddClass();
			try{
			retObj.general_uuid=request.readLong();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGeneralAddClass{
		private static final Logger logger = LoggerFactory.getLogger(S2CGeneralAddClass.class);
		public long general_uuid;
		public IOGeneralBean general_bean;
		@Override
		public String toString() {
			return "S2CGeneralAddClass [general_uuid="+general_uuid+",general_bean="+general_bean+",]";
		}
		public static final int msgCode = 360;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 360);
			retMsg.writeLong(general_uuid);
			if(general_bean == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(general_bean.guid);
					retMsg.writeInt(general_bean.gsid);
					retMsg.writeInt(general_bean.level);
					retMsg.writeInt(general_bean.star);
					retMsg.writeInt(general_bean.camp);
					retMsg.writeInt(general_bean.occu);
					retMsg.writeInt(general_bean.pclass);
					retMsg.writeInt(general_bean.power);
					if(general_bean.talent == null || general_bean.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.talent.size());
				for(Integer general_bean_talent1 : general_bean.talent){
			retMsg.writeInt(general_bean_talent1);
				}
			}
					retMsg.writeInt(general_bean.affairs);
					retMsg.writeInt(general_bean.treasure);
					if(general_bean.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.property.hp);
					retMsg.writeInt(general_bean.property.atk);
					retMsg.writeInt(general_bean.property.def);
					retMsg.writeInt(general_bean.property.mdef);
					retMsg.writeFloat(general_bean.property.atktime);
					retMsg.writeInt(general_bean.property.range);
					retMsg.writeInt(general_bean.property.msp);
					retMsg.writeInt(general_bean.property.pasp);
					retMsg.writeInt(general_bean.property.pcri);
					retMsg.writeInt(general_bean.property.pcrid);
					retMsg.writeInt(general_bean.property.pdam);
					retMsg.writeInt(general_bean.property.php);
					retMsg.writeInt(general_bean.property.patk);
					retMsg.writeInt(general_bean.property.pdef);
					retMsg.writeInt(general_bean.property.pmdef);
					retMsg.writeInt(general_bean.property.ppbs);
					retMsg.writeInt(general_bean.property.pmbs);
					retMsg.writeInt(general_bean.property.pefc);
					retMsg.writeInt(general_bean.property.ppthr);
					retMsg.writeInt(general_bean.property.patkdam);
					retMsg.writeInt(general_bean.property.pskidam);
					retMsg.writeInt(general_bean.property.pckatk);
					retMsg.writeInt(general_bean.property.pmthr);
					retMsg.writeInt(general_bean.property.pdex);
					retMsg.writeInt(general_bean.property.pmdex);
					retMsg.writeInt(general_bean.property.pmsatk);
					retMsg.writeInt(general_bean.property.pmps);
					retMsg.writeInt(general_bean.property.pcd);
			}
					if(general_bean.equip == null || general_bean.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.equip.size());
				for(Integer general_bean_equip1 : general_bean.equip){
			retMsg.writeInt(general_bean_equip1);
				}
			}
					if(general_bean.skill == null || general_bean.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.skill.size());
				for(Integer general_bean_skill1 : general_bean.skill){
			retMsg.writeInt(general_bean_skill1);
				}
			}
					if(general_bean.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.exclusive.level);
					if(general_bean.exclusive.skill == null || general_bean.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.skill.size());
				for(Integer general_bean_exclusive_skill1 : general_bean.exclusive.skill){
			retMsg.writeInt(general_bean_exclusive_skill1);
				}
			}
					retMsg.writeInt(general_bean.exclusive.gsid);
					if(general_bean.exclusive.property == null || general_bean.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.property.size());
				for(KvStringPair general_bean_exclusive_property1 : general_bean.exclusive.property){
					retMsg.writeString(general_bean_exclusive_property1.key);
					retMsg.writeInt(general_bean_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(general_bean.hppercent);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGeneralAddStar{
		private static final Logger logger = LoggerFactory.getLogger(C2SGeneralAddStar.class);
		public int target_gsid;
		public long general_uuid;
		public long[] cost_generals;
		@Override
		public String toString() {
			return "C2SGeneralAddStar [target_gsid="+target_gsid+",general_uuid="+general_uuid+",cost_generals="+java.util.Arrays.toString(cost_generals)+",]";
		}
		public static final int id = 361;

		public static C2SGeneralAddStar parse(MyRequestMessage request){
			C2SGeneralAddStar retObj = new C2SGeneralAddStar();
			try{
			retObj.target_gsid=request.readInt();
			retObj.general_uuid=request.readLong();
			int cost_generals_size = request.readInt();
				retObj.cost_generals = new long[cost_generals_size];
				for(int i=0;i<cost_generals_size;i++){
					retObj.cost_generals[i]=request.readLong();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGeneralAddStar{
		private static final Logger logger = LoggerFactory.getLogger(S2CGeneralAddStar.class);
		public int target_gsid;
		public long general_uuid;
		public List<Long> cost_generals;
		public List<RewardInfo> items;
		public IOGeneralBean general_bean;
		@Override
		public String toString() {
			return "S2CGeneralAddStar [target_gsid="+target_gsid+",general_uuid="+general_uuid+",cost_generals="+cost_generals+",items="+items+",general_bean="+general_bean+",]";
		}
		public static final int msgCode = 362;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 362);
			retMsg.writeInt(target_gsid);
			retMsg.writeLong(general_uuid);
			if(cost_generals == null || cost_generals.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(cost_generals.size());
				for(Long cost_generals1 : cost_generals){
			retMsg.writeLong(cost_generals1);
				}
			}
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(RewardInfo items1 : items){
					retMsg.writeInt(items1.GSID);
					retMsg.writeInt(items1.COUNT);
				}
			}
			if(general_bean == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(general_bean.guid);
					retMsg.writeInt(general_bean.gsid);
					retMsg.writeInt(general_bean.level);
					retMsg.writeInt(general_bean.star);
					retMsg.writeInt(general_bean.camp);
					retMsg.writeInt(general_bean.occu);
					retMsg.writeInt(general_bean.pclass);
					retMsg.writeInt(general_bean.power);
					if(general_bean.talent == null || general_bean.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.talent.size());
				for(Integer general_bean_talent1 : general_bean.talent){
			retMsg.writeInt(general_bean_talent1);
				}
			}
					retMsg.writeInt(general_bean.affairs);
					retMsg.writeInt(general_bean.treasure);
					if(general_bean.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.property.hp);
					retMsg.writeInt(general_bean.property.atk);
					retMsg.writeInt(general_bean.property.def);
					retMsg.writeInt(general_bean.property.mdef);
					retMsg.writeFloat(general_bean.property.atktime);
					retMsg.writeInt(general_bean.property.range);
					retMsg.writeInt(general_bean.property.msp);
					retMsg.writeInt(general_bean.property.pasp);
					retMsg.writeInt(general_bean.property.pcri);
					retMsg.writeInt(general_bean.property.pcrid);
					retMsg.writeInt(general_bean.property.pdam);
					retMsg.writeInt(general_bean.property.php);
					retMsg.writeInt(general_bean.property.patk);
					retMsg.writeInt(general_bean.property.pdef);
					retMsg.writeInt(general_bean.property.pmdef);
					retMsg.writeInt(general_bean.property.ppbs);
					retMsg.writeInt(general_bean.property.pmbs);
					retMsg.writeInt(general_bean.property.pefc);
					retMsg.writeInt(general_bean.property.ppthr);
					retMsg.writeInt(general_bean.property.patkdam);
					retMsg.writeInt(general_bean.property.pskidam);
					retMsg.writeInt(general_bean.property.pckatk);
					retMsg.writeInt(general_bean.property.pmthr);
					retMsg.writeInt(general_bean.property.pdex);
					retMsg.writeInt(general_bean.property.pmdex);
					retMsg.writeInt(general_bean.property.pmsatk);
					retMsg.writeInt(general_bean.property.pmps);
					retMsg.writeInt(general_bean.property.pcd);
			}
					if(general_bean.equip == null || general_bean.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.equip.size());
				for(Integer general_bean_equip1 : general_bean.equip){
			retMsg.writeInt(general_bean_equip1);
				}
			}
					if(general_bean.skill == null || general_bean.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.skill.size());
				for(Integer general_bean_skill1 : general_bean.skill){
			retMsg.writeInt(general_bean_skill1);
				}
			}
					if(general_bean.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.exclusive.level);
					if(general_bean.exclusive.skill == null || general_bean.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.skill.size());
				for(Integer general_bean_exclusive_skill1 : general_bean.exclusive.skill){
			retMsg.writeInt(general_bean_exclusive_skill1);
				}
			}
					retMsg.writeInt(general_bean.exclusive.gsid);
					if(general_bean.exclusive.property == null || general_bean.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.property.size());
				for(KvStringPair general_bean_exclusive_property1 : general_bean.exclusive.property){
					retMsg.writeString(general_bean_exclusive_property1.key);
					retMsg.writeInt(general_bean_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(general_bean.hppercent);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class PushGeneralAddLevel{
		private static final Logger logger = LoggerFactory.getLogger(PushGeneralAddLevel.class);
		public long general_uuid;
		public int level;
		public int new_power;
		public PushGeneralAddLevel(long pgeneral_uuid,int plevel,int pnew_power){
			general_uuid=pgeneral_uuid;
			level=plevel;
			new_power=pnew_power;
		}
		public PushGeneralAddLevel(){}
		@Override
		public String toString() {
			return "PushGeneralAddLevel [general_uuid="+general_uuid+",level="+level+",new_power="+new_power+",]";
		}
		public static final int msgCode = 364;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 364);
			retMsg.writeLong(general_uuid);
			retMsg.writeInt(level);
			retMsg.writeInt(new_power);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGeneralDecomp{
		private static final Logger logger = LoggerFactory.getLogger(C2SGeneralDecomp.class);
		public long[] general_uuid;
		public int action_type;
		@Override
		public String toString() {
			return "C2SGeneralDecomp [general_uuid="+java.util.Arrays.toString(general_uuid)+",action_type="+action_type+",]";
		}
		public static final int id = 365;

		public static C2SGeneralDecomp parse(MyRequestMessage request){
			C2SGeneralDecomp retObj = new C2SGeneralDecomp();
			try{
			int general_uuid_size = request.readInt();
				retObj.general_uuid = new long[general_uuid_size];
				for(int i=0;i<general_uuid_size;i++){
					retObj.general_uuid[i]=request.readLong();
				}
			retObj.action_type=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGeneralDecomp{
		private static final Logger logger = LoggerFactory.getLogger(S2CGeneralDecomp.class);
		public List<Long> general_uuid;
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2CGeneralDecomp [general_uuid="+general_uuid+",rewards="+rewards+",]";
		}
		public static final int msgCode = 366;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 366);
			if(general_uuid == null || general_uuid.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_uuid.size());
				for(Long general_uuid1 : general_uuid){
			retMsg.writeLong(general_uuid1);
				}
			}
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
	public static final class C2SGeneralReset{
		private static final Logger logger = LoggerFactory.getLogger(C2SGeneralReset.class);
		public long general_uuid;
		public int action_type;
		@Override
		public String toString() {
			return "C2SGeneralReset [general_uuid="+general_uuid+",action_type="+action_type+",]";
		}
		public static final int id = 367;

		public static C2SGeneralReset parse(MyRequestMessage request){
			C2SGeneralReset retObj = new C2SGeneralReset();
			try{
			retObj.general_uuid=request.readLong();
			retObj.action_type=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGeneralReset{
		private static final Logger logger = LoggerFactory.getLogger(S2CGeneralReset.class);
		public long general_uuid;
		public List<RewardInfo> items;
		public IOGeneralBean general_bean;
		@Override
		public String toString() {
			return "S2CGeneralReset [general_uuid="+general_uuid+",items="+items+",general_bean="+general_bean+",]";
		}
		public static final int msgCode = 368;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 368);
			retMsg.writeLong(general_uuid);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(RewardInfo items1 : items){
					retMsg.writeInt(items1.GSID);
					retMsg.writeInt(items1.COUNT);
				}
			}
			if(general_bean == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(general_bean.guid);
					retMsg.writeInt(general_bean.gsid);
					retMsg.writeInt(general_bean.level);
					retMsg.writeInt(general_bean.star);
					retMsg.writeInt(general_bean.camp);
					retMsg.writeInt(general_bean.occu);
					retMsg.writeInt(general_bean.pclass);
					retMsg.writeInt(general_bean.power);
					if(general_bean.talent == null || general_bean.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.talent.size());
				for(Integer general_bean_talent1 : general_bean.talent){
			retMsg.writeInt(general_bean_talent1);
				}
			}
					retMsg.writeInt(general_bean.affairs);
					retMsg.writeInt(general_bean.treasure);
					if(general_bean.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.property.hp);
					retMsg.writeInt(general_bean.property.atk);
					retMsg.writeInt(general_bean.property.def);
					retMsg.writeInt(general_bean.property.mdef);
					retMsg.writeFloat(general_bean.property.atktime);
					retMsg.writeInt(general_bean.property.range);
					retMsg.writeInt(general_bean.property.msp);
					retMsg.writeInt(general_bean.property.pasp);
					retMsg.writeInt(general_bean.property.pcri);
					retMsg.writeInt(general_bean.property.pcrid);
					retMsg.writeInt(general_bean.property.pdam);
					retMsg.writeInt(general_bean.property.php);
					retMsg.writeInt(general_bean.property.patk);
					retMsg.writeInt(general_bean.property.pdef);
					retMsg.writeInt(general_bean.property.pmdef);
					retMsg.writeInt(general_bean.property.ppbs);
					retMsg.writeInt(general_bean.property.pmbs);
					retMsg.writeInt(general_bean.property.pefc);
					retMsg.writeInt(general_bean.property.ppthr);
					retMsg.writeInt(general_bean.property.patkdam);
					retMsg.writeInt(general_bean.property.pskidam);
					retMsg.writeInt(general_bean.property.pckatk);
					retMsg.writeInt(general_bean.property.pmthr);
					retMsg.writeInt(general_bean.property.pdex);
					retMsg.writeInt(general_bean.property.pmdex);
					retMsg.writeInt(general_bean.property.pmsatk);
					retMsg.writeInt(general_bean.property.pmps);
					retMsg.writeInt(general_bean.property.pcd);
			}
					if(general_bean.equip == null || general_bean.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.equip.size());
				for(Integer general_bean_equip1 : general_bean.equip){
			retMsg.writeInt(general_bean_equip1);
				}
			}
					if(general_bean.skill == null || general_bean.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.skill.size());
				for(Integer general_bean_skill1 : general_bean.skill){
			retMsg.writeInt(general_bean_skill1);
				}
			}
					if(general_bean.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.exclusive.level);
					if(general_bean.exclusive.skill == null || general_bean.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.skill.size());
				for(Integer general_bean_exclusive_skill1 : general_bean.exclusive.skill){
			retMsg.writeInt(general_bean_exclusive_skill1);
				}
			}
					retMsg.writeInt(general_bean.exclusive.gsid);
					if(general_bean.exclusive.property == null || general_bean.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.property.size());
				for(KvStringPair general_bean_exclusive_property1 : general_bean.exclusive.property){
					retMsg.writeString(general_bean_exclusive_property1.key);
					retMsg.writeInt(general_bean_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(general_bean.hppercent);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SExclusiveLevelUp{
		private static final Logger logger = LoggerFactory.getLogger(C2SExclusiveLevelUp.class);
		public long general_uuid;
		public boolean is_lock;
		@Override
		public String toString() {
			return "C2SExclusiveLevelUp [general_uuid="+general_uuid+",is_lock="+is_lock+",]";
		}
		public static final int id = 369;

		public static C2SExclusiveLevelUp parse(MyRequestMessage request){
			C2SExclusiveLevelUp retObj = new C2SExclusiveLevelUp();
			try{
			retObj.general_uuid=request.readLong();
			retObj.is_lock=request.readBool();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CExclusiveLevelUp{
		private static final Logger logger = LoggerFactory.getLogger(S2CExclusiveLevelUp.class);
		public long general_uuid;
		public IOGeneralBean general_bean;
		@Override
		public String toString() {
			return "S2CExclusiveLevelUp [general_uuid="+general_uuid+",general_bean="+general_bean+",]";
		}
		public static final int msgCode = 370;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 370);
			retMsg.writeLong(general_uuid);
			if(general_bean == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(general_bean.guid);
					retMsg.writeInt(general_bean.gsid);
					retMsg.writeInt(general_bean.level);
					retMsg.writeInt(general_bean.star);
					retMsg.writeInt(general_bean.camp);
					retMsg.writeInt(general_bean.occu);
					retMsg.writeInt(general_bean.pclass);
					retMsg.writeInt(general_bean.power);
					if(general_bean.talent == null || general_bean.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.talent.size());
				for(Integer general_bean_talent1 : general_bean.talent){
			retMsg.writeInt(general_bean_talent1);
				}
			}
					retMsg.writeInt(general_bean.affairs);
					retMsg.writeInt(general_bean.treasure);
					if(general_bean.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.property.hp);
					retMsg.writeInt(general_bean.property.atk);
					retMsg.writeInt(general_bean.property.def);
					retMsg.writeInt(general_bean.property.mdef);
					retMsg.writeFloat(general_bean.property.atktime);
					retMsg.writeInt(general_bean.property.range);
					retMsg.writeInt(general_bean.property.msp);
					retMsg.writeInt(general_bean.property.pasp);
					retMsg.writeInt(general_bean.property.pcri);
					retMsg.writeInt(general_bean.property.pcrid);
					retMsg.writeInt(general_bean.property.pdam);
					retMsg.writeInt(general_bean.property.php);
					retMsg.writeInt(general_bean.property.patk);
					retMsg.writeInt(general_bean.property.pdef);
					retMsg.writeInt(general_bean.property.pmdef);
					retMsg.writeInt(general_bean.property.ppbs);
					retMsg.writeInt(general_bean.property.pmbs);
					retMsg.writeInt(general_bean.property.pefc);
					retMsg.writeInt(general_bean.property.ppthr);
					retMsg.writeInt(general_bean.property.patkdam);
					retMsg.writeInt(general_bean.property.pskidam);
					retMsg.writeInt(general_bean.property.pckatk);
					retMsg.writeInt(general_bean.property.pmthr);
					retMsg.writeInt(general_bean.property.pdex);
					retMsg.writeInt(general_bean.property.pmdex);
					retMsg.writeInt(general_bean.property.pmsatk);
					retMsg.writeInt(general_bean.property.pmps);
					retMsg.writeInt(general_bean.property.pcd);
			}
					if(general_bean.equip == null || general_bean.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.equip.size());
				for(Integer general_bean_equip1 : general_bean.equip){
			retMsg.writeInt(general_bean_equip1);
				}
			}
					if(general_bean.skill == null || general_bean.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.skill.size());
				for(Integer general_bean_skill1 : general_bean.skill){
			retMsg.writeInt(general_bean_skill1);
				}
			}
					if(general_bean.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.exclusive.level);
					if(general_bean.exclusive.skill == null || general_bean.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.skill.size());
				for(Integer general_bean_exclusive_skill1 : general_bean.exclusive.skill){
			retMsg.writeInt(general_bean_exclusive_skill1);
				}
			}
					retMsg.writeInt(general_bean.exclusive.gsid);
					if(general_bean.exclusive.property == null || general_bean.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.property.size());
				for(KvStringPair general_bean_exclusive_property1 : general_bean.exclusive.property){
					retMsg.writeString(general_bean_exclusive_property1.key);
					retMsg.writeInt(general_bean_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(general_bean.hppercent);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SExclusiveRefreshGet{
		private static final Logger logger = LoggerFactory.getLogger(C2SExclusiveRefreshGet.class);
		public long general_uuid;
		@Override
		public String toString() {
			return "C2SExclusiveRefreshGet [general_uuid="+general_uuid+",]";
		}
		public static final int id = 371;

		public static C2SExclusiveRefreshGet parse(MyRequestMessage request){
			C2SExclusiveRefreshGet retObj = new C2SExclusiveRefreshGet();
			try{
			retObj.general_uuid=request.readLong();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CExclusiveRefreshGet{
		private static final Logger logger = LoggerFactory.getLogger(S2CExclusiveRefreshGet.class);
		public long general_uuid;
		public boolean has_pending_property;
		public List<Integer> skill;
		public List<KvStringPair> property;
		@Override
		public String toString() {
			return "S2CExclusiveRefreshGet [general_uuid="+general_uuid+",has_pending_property="+has_pending_property+",skill="+skill+",property="+property+",]";
		}
		public static final int msgCode = 372;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 372);
			retMsg.writeLong(general_uuid);
			retMsg.writeBool(has_pending_property);
			if(skill == null || skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(skill.size());
				for(Integer skill1 : skill){
			retMsg.writeInt(skill1);
				}
			}
			if(property == null || property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(property.size());
				for(KvStringPair property1 : property){
					retMsg.writeString(property1.key);
					retMsg.writeInt(property1.val);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SExclusiveRefreshBegin{
		private static final Logger logger = LoggerFactory.getLogger(C2SExclusiveRefreshBegin.class);
		public long general_uuid;
		public int lock_type;
		@Override
		public String toString() {
			return "C2SExclusiveRefreshBegin [general_uuid="+general_uuid+",lock_type="+lock_type+",]";
		}
		public static final int id = 373;

		public static C2SExclusiveRefreshBegin parse(MyRequestMessage request){
			C2SExclusiveRefreshBegin retObj = new C2SExclusiveRefreshBegin();
			try{
			retObj.general_uuid=request.readLong();
			retObj.lock_type=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CExclusiveRefreshBegin{
		private static final Logger logger = LoggerFactory.getLogger(S2CExclusiveRefreshBegin.class);
		public long general_uuid;
		public int lock_type;
		public List<Integer> skill;
		public List<KvStringPair> property;
		@Override
		public String toString() {
			return "S2CExclusiveRefreshBegin [general_uuid="+general_uuid+",lock_type="+lock_type+",skill="+skill+",property="+property+",]";
		}
		public static final int msgCode = 374;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 374);
			retMsg.writeLong(general_uuid);
			retMsg.writeInt(lock_type);
			if(skill == null || skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(skill.size());
				for(Integer skill1 : skill){
			retMsg.writeInt(skill1);
				}
			}
			if(property == null || property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(property.size());
				for(KvStringPair property1 : property){
					retMsg.writeString(property1.key);
					retMsg.writeInt(property1.val);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SExclusiveRefreshConfirm{
		private static final Logger logger = LoggerFactory.getLogger(C2SExclusiveRefreshConfirm.class);
		public long general_uuid;
		public boolean is_confirm;
		@Override
		public String toString() {
			return "C2SExclusiveRefreshConfirm [general_uuid="+general_uuid+",is_confirm="+is_confirm+",]";
		}
		public static final int id = 375;

		public static C2SExclusiveRefreshConfirm parse(MyRequestMessage request){
			C2SExclusiveRefreshConfirm retObj = new C2SExclusiveRefreshConfirm();
			try{
			retObj.general_uuid=request.readLong();
			retObj.is_confirm=request.readBool();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CExclusiveRefreshConfirm{
		private static final Logger logger = LoggerFactory.getLogger(S2CExclusiveRefreshConfirm.class);
		public long general_uuid;
		public boolean is_confirm;
		public IOGeneralBean general_bean;
		@Override
		public String toString() {
			return "S2CExclusiveRefreshConfirm [general_uuid="+general_uuid+",is_confirm="+is_confirm+",general_bean="+general_bean+",]";
		}
		public static final int msgCode = 376;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 376);
			retMsg.writeLong(general_uuid);
			retMsg.writeBool(is_confirm);
			if(general_bean == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(general_bean.guid);
					retMsg.writeInt(general_bean.gsid);
					retMsg.writeInt(general_bean.level);
					retMsg.writeInt(general_bean.star);
					retMsg.writeInt(general_bean.camp);
					retMsg.writeInt(general_bean.occu);
					retMsg.writeInt(general_bean.pclass);
					retMsg.writeInt(general_bean.power);
					if(general_bean.talent == null || general_bean.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.talent.size());
				for(Integer general_bean_talent1 : general_bean.talent){
			retMsg.writeInt(general_bean_talent1);
				}
			}
					retMsg.writeInt(general_bean.affairs);
					retMsg.writeInt(general_bean.treasure);
					if(general_bean.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.property.hp);
					retMsg.writeInt(general_bean.property.atk);
					retMsg.writeInt(general_bean.property.def);
					retMsg.writeInt(general_bean.property.mdef);
					retMsg.writeFloat(general_bean.property.atktime);
					retMsg.writeInt(general_bean.property.range);
					retMsg.writeInt(general_bean.property.msp);
					retMsg.writeInt(general_bean.property.pasp);
					retMsg.writeInt(general_bean.property.pcri);
					retMsg.writeInt(general_bean.property.pcrid);
					retMsg.writeInt(general_bean.property.pdam);
					retMsg.writeInt(general_bean.property.php);
					retMsg.writeInt(general_bean.property.patk);
					retMsg.writeInt(general_bean.property.pdef);
					retMsg.writeInt(general_bean.property.pmdef);
					retMsg.writeInt(general_bean.property.ppbs);
					retMsg.writeInt(general_bean.property.pmbs);
					retMsg.writeInt(general_bean.property.pefc);
					retMsg.writeInt(general_bean.property.ppthr);
					retMsg.writeInt(general_bean.property.patkdam);
					retMsg.writeInt(general_bean.property.pskidam);
					retMsg.writeInt(general_bean.property.pckatk);
					retMsg.writeInt(general_bean.property.pmthr);
					retMsg.writeInt(general_bean.property.pdex);
					retMsg.writeInt(general_bean.property.pmdex);
					retMsg.writeInt(general_bean.property.pmsatk);
					retMsg.writeInt(general_bean.property.pmps);
					retMsg.writeInt(general_bean.property.pcd);
			}
					if(general_bean.equip == null || general_bean.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.equip.size());
				for(Integer general_bean_equip1 : general_bean.equip){
			retMsg.writeInt(general_bean_equip1);
				}
			}
					if(general_bean.skill == null || general_bean.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.skill.size());
				for(Integer general_bean_skill1 : general_bean.skill){
			retMsg.writeInt(general_bean_skill1);
				}
			}
					if(general_bean.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.exclusive.level);
					if(general_bean.exclusive.skill == null || general_bean.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.skill.size());
				for(Integer general_bean_exclusive_skill1 : general_bean.exclusive.skill){
			retMsg.writeInt(general_bean_exclusive_skill1);
				}
			}
					retMsg.writeInt(general_bean.exclusive.gsid);
					if(general_bean.exclusive.property == null || general_bean.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.property.size());
				for(KvStringPair general_bean_exclusive_property1 : general_bean.exclusive.property){
					retMsg.writeString(general_bean_exclusive_property1.key);
					retMsg.writeInt(general_bean_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(general_bean.hppercent);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGeneralSummon{
		private static final Logger logger = LoggerFactory.getLogger(C2SGeneralSummon.class);
		public int type;
		public int times;
		@Override
		public String toString() {
			return "C2SGeneralSummon [type="+type+",times="+times+",]";
		}
		public static final int id = 377;

		public static C2SGeneralSummon parse(MyRequestMessage request){
			C2SGeneralSummon retObj = new C2SGeneralSummon();
			try{
			retObj.type=request.readInt();
			retObj.times=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGeneralSummon{
		private static final Logger logger = LoggerFactory.getLogger(S2CGeneralSummon.class);
		public int type;
		public int times;
		public List<RewardInfo> rewards;
		@Override
		public String toString() {
			return "S2CGeneralSummon [type="+type+",times="+times+",rewards="+rewards+",]";
		}
		public static final int msgCode = 378;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 378);
			retMsg.writeInt(type);
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
	public static final class C2SGeneralExchangeGet{
		private static final Logger logger = LoggerFactory.getLogger(C2SGeneralExchangeGet.class);
		public static final int id = 379;
	}
	public static final class S2CGeneralExchangeGet{
		private static final Logger logger = LoggerFactory.getLogger(S2CGeneralExchangeGet.class);
		public long guid;
		public int gsid;
		public S2CGeneralExchangeGet(long pguid,int pgsid){
			guid=pguid;
			gsid=pgsid;
		}
		public S2CGeneralExchangeGet(){}
		@Override
		public String toString() {
			return "S2CGeneralExchangeGet [guid="+guid+",gsid="+gsid+",]";
		}
		public static final int msgCode = 380;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 380);
			retMsg.writeLong(guid);
			retMsg.writeInt(gsid);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGeneralExchangeRefresh{
		private static final Logger logger = LoggerFactory.getLogger(C2SGeneralExchangeRefresh.class);
		public long general_uuid;
		@Override
		public String toString() {
			return "C2SGeneralExchangeRefresh [general_uuid="+general_uuid+",]";
		}
		public static final int id = 381;

		public static C2SGeneralExchangeRefresh parse(MyRequestMessage request){
			C2SGeneralExchangeRefresh retObj = new C2SGeneralExchangeRefresh();
			try{
			retObj.general_uuid=request.readLong();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGeneralExchangeRefresh{
		private static final Logger logger = LoggerFactory.getLogger(S2CGeneralExchangeRefresh.class);
		public long guid;
		public int gsid;
		public S2CGeneralExchangeRefresh(long pguid,int pgsid){
			guid=pguid;
			gsid=pgsid;
		}
		public S2CGeneralExchangeRefresh(){}
		@Override
		public String toString() {
			return "S2CGeneralExchangeRefresh [guid="+guid+",gsid="+gsid+",]";
		}
		public static final int msgCode = 382;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 382);
			retMsg.writeLong(guid);
			retMsg.writeInt(gsid);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGeneralExchangeConfirm{
		private static final Logger logger = LoggerFactory.getLogger(C2SGeneralExchangeConfirm.class);
		public int action_type;
		@Override
		public String toString() {
			return "C2SGeneralExchangeConfirm [action_type="+action_type+",]";
		}
		public static final int id = 383;

		public static C2SGeneralExchangeConfirm parse(MyRequestMessage request){
			C2SGeneralExchangeConfirm retObj = new C2SGeneralExchangeConfirm();
			try{
			retObj.action_type=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGeneralExchangeConfirm{
		private static final Logger logger = LoggerFactory.getLogger(S2CGeneralExchangeConfirm.class);
		public int action_type;
		public IOGeneralBean general_bean;
		@Override
		public String toString() {
			return "S2CGeneralExchangeConfirm [action_type="+action_type+",general_bean="+general_bean+",]";
		}
		public static final int msgCode = 384;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 384);
			retMsg.writeInt(action_type);
			if(general_bean == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(general_bean.guid);
					retMsg.writeInt(general_bean.gsid);
					retMsg.writeInt(general_bean.level);
					retMsg.writeInt(general_bean.star);
					retMsg.writeInt(general_bean.camp);
					retMsg.writeInt(general_bean.occu);
					retMsg.writeInt(general_bean.pclass);
					retMsg.writeInt(general_bean.power);
					if(general_bean.talent == null || general_bean.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.talent.size());
				for(Integer general_bean_talent1 : general_bean.talent){
			retMsg.writeInt(general_bean_talent1);
				}
			}
					retMsg.writeInt(general_bean.affairs);
					retMsg.writeInt(general_bean.treasure);
					if(general_bean.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.property.hp);
					retMsg.writeInt(general_bean.property.atk);
					retMsg.writeInt(general_bean.property.def);
					retMsg.writeInt(general_bean.property.mdef);
					retMsg.writeFloat(general_bean.property.atktime);
					retMsg.writeInt(general_bean.property.range);
					retMsg.writeInt(general_bean.property.msp);
					retMsg.writeInt(general_bean.property.pasp);
					retMsg.writeInt(general_bean.property.pcri);
					retMsg.writeInt(general_bean.property.pcrid);
					retMsg.writeInt(general_bean.property.pdam);
					retMsg.writeInt(general_bean.property.php);
					retMsg.writeInt(general_bean.property.patk);
					retMsg.writeInt(general_bean.property.pdef);
					retMsg.writeInt(general_bean.property.pmdef);
					retMsg.writeInt(general_bean.property.ppbs);
					retMsg.writeInt(general_bean.property.pmbs);
					retMsg.writeInt(general_bean.property.pefc);
					retMsg.writeInt(general_bean.property.ppthr);
					retMsg.writeInt(general_bean.property.patkdam);
					retMsg.writeInt(general_bean.property.pskidam);
					retMsg.writeInt(general_bean.property.pckatk);
					retMsg.writeInt(general_bean.property.pmthr);
					retMsg.writeInt(general_bean.property.pdex);
					retMsg.writeInt(general_bean.property.pmdex);
					retMsg.writeInt(general_bean.property.pmsatk);
					retMsg.writeInt(general_bean.property.pmps);
					retMsg.writeInt(general_bean.property.pcd);
			}
					if(general_bean.equip == null || general_bean.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.equip.size());
				for(Integer general_bean_equip1 : general_bean.equip){
			retMsg.writeInt(general_bean_equip1);
				}
			}
					if(general_bean.skill == null || general_bean.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.skill.size());
				for(Integer general_bean_skill1 : general_bean.skill){
			retMsg.writeInt(general_bean_skill1);
				}
			}
					if(general_bean.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.exclusive.level);
					if(general_bean.exclusive.skill == null || general_bean.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.skill.size());
				for(Integer general_bean_exclusive_skill1 : general_bean.exclusive.skill){
			retMsg.writeInt(general_bean_exclusive_skill1);
				}
			}
					retMsg.writeInt(general_bean.exclusive.gsid);
					if(general_bean.exclusive.property == null || general_bean.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.property.size());
				for(KvStringPair general_bean_exclusive_property1 : general_bean.exclusive.property){
					retMsg.writeString(general_bean_exclusive_property1.key);
					retMsg.writeInt(general_bean_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(general_bean.hppercent);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STreasureTakeon{
		private static final Logger logger = LoggerFactory.getLogger(C2STreasureTakeon.class);
		public long guid;
		public int treasure_id;
		@Override
		public String toString() {
			return "C2STreasureTakeon [guid="+guid+",treasure_id="+treasure_id+",]";
		}
		public static final int id = 385;

		public static C2STreasureTakeon parse(MyRequestMessage request){
			C2STreasureTakeon retObj = new C2STreasureTakeon();
			try{
			retObj.guid=request.readLong();
			retObj.treasure_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CTreasureTakeon{
		private static final Logger logger = LoggerFactory.getLogger(S2CTreasureTakeon.class);
		public long guid;
		public int treasure_id;
		public IOGeneralBean general_bean;
		@Override
		public String toString() {
			return "S2CTreasureTakeon [guid="+guid+",treasure_id="+treasure_id+",general_bean="+general_bean+",]";
		}
		public static final int msgCode = 386;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 386);
			retMsg.writeLong(guid);
			retMsg.writeInt(treasure_id);
			if(general_bean == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(general_bean.guid);
					retMsg.writeInt(general_bean.gsid);
					retMsg.writeInt(general_bean.level);
					retMsg.writeInt(general_bean.star);
					retMsg.writeInt(general_bean.camp);
					retMsg.writeInt(general_bean.occu);
					retMsg.writeInt(general_bean.pclass);
					retMsg.writeInt(general_bean.power);
					if(general_bean.talent == null || general_bean.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.talent.size());
				for(Integer general_bean_talent1 : general_bean.talent){
			retMsg.writeInt(general_bean_talent1);
				}
			}
					retMsg.writeInt(general_bean.affairs);
					retMsg.writeInt(general_bean.treasure);
					if(general_bean.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.property.hp);
					retMsg.writeInt(general_bean.property.atk);
					retMsg.writeInt(general_bean.property.def);
					retMsg.writeInt(general_bean.property.mdef);
					retMsg.writeFloat(general_bean.property.atktime);
					retMsg.writeInt(general_bean.property.range);
					retMsg.writeInt(general_bean.property.msp);
					retMsg.writeInt(general_bean.property.pasp);
					retMsg.writeInt(general_bean.property.pcri);
					retMsg.writeInt(general_bean.property.pcrid);
					retMsg.writeInt(general_bean.property.pdam);
					retMsg.writeInt(general_bean.property.php);
					retMsg.writeInt(general_bean.property.patk);
					retMsg.writeInt(general_bean.property.pdef);
					retMsg.writeInt(general_bean.property.pmdef);
					retMsg.writeInt(general_bean.property.ppbs);
					retMsg.writeInt(general_bean.property.pmbs);
					retMsg.writeInt(general_bean.property.pefc);
					retMsg.writeInt(general_bean.property.ppthr);
					retMsg.writeInt(general_bean.property.patkdam);
					retMsg.writeInt(general_bean.property.pskidam);
					retMsg.writeInt(general_bean.property.pckatk);
					retMsg.writeInt(general_bean.property.pmthr);
					retMsg.writeInt(general_bean.property.pdex);
					retMsg.writeInt(general_bean.property.pmdex);
					retMsg.writeInt(general_bean.property.pmsatk);
					retMsg.writeInt(general_bean.property.pmps);
					retMsg.writeInt(general_bean.property.pcd);
			}
					if(general_bean.equip == null || general_bean.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.equip.size());
				for(Integer general_bean_equip1 : general_bean.equip){
			retMsg.writeInt(general_bean_equip1);
				}
			}
					if(general_bean.skill == null || general_bean.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.skill.size());
				for(Integer general_bean_skill1 : general_bean.skill){
			retMsg.writeInt(general_bean_skill1);
				}
			}
					if(general_bean.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.exclusive.level);
					if(general_bean.exclusive.skill == null || general_bean.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.skill.size());
				for(Integer general_bean_exclusive_skill1 : general_bean.exclusive.skill){
			retMsg.writeInt(general_bean_exclusive_skill1);
				}
			}
					retMsg.writeInt(general_bean.exclusive.gsid);
					if(general_bean.exclusive.property == null || general_bean.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.property.size());
				for(KvStringPair general_bean_exclusive_property1 : general_bean.exclusive.property){
					retMsg.writeString(general_bean_exclusive_property1.key);
					retMsg.writeInt(general_bean_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(general_bean.hppercent);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STreasureSell{
		private static final Logger logger = LoggerFactory.getLogger(C2STreasureSell.class);
		public int treasure_id;
		public int count;
		@Override
		public String toString() {
			return "C2STreasureSell [treasure_id="+treasure_id+",count="+count+",]";
		}
		public static final int id = 387;

		public static C2STreasureSell parse(MyRequestMessage request){
			C2STreasureSell retObj = new C2STreasureSell();
			try{
			retObj.treasure_id=request.readInt();
			retObj.count=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CTreasureSell{
		private static final Logger logger = LoggerFactory.getLogger(S2CTreasureSell.class);
		public int treasure_id;
		public int count;
		public S2CTreasureSell(int ptreasure_id,int pcount){
			treasure_id=ptreasure_id;
			count=pcount;
		}
		public S2CTreasureSell(){}
		@Override
		public String toString() {
			return "S2CTreasureSell [treasure_id="+treasure_id+",count="+count+",]";
		}
		public static final int msgCode = 388;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 388);
			retMsg.writeInt(treasure_id);
			retMsg.writeInt(count);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2STreasureLevel{
		private static final Logger logger = LoggerFactory.getLogger(C2STreasureLevel.class);
		public long guid;
		public int treasure_id;
		public RewardInfo[] consumes;
		@Override
		public String toString() {
			return "C2STreasureLevel [guid="+guid+",treasure_id="+treasure_id+",consumes="+java.util.Arrays.toString(consumes)+",]";
		}
		public static final int id = 389;

		public static C2STreasureLevel parse(MyRequestMessage request){
			C2STreasureLevel retObj = new C2STreasureLevel();
			try{
			retObj.guid=request.readLong();
			retObj.treasure_id=request.readInt();
			int consumes_size = request.readInt();
				retObj.consumes = new RewardInfo[consumes_size];
				for(int i=0;i<consumes_size;i++){
						retObj.consumes[i] = new RewardInfo();
					retObj.consumes[i].GSID=request.readInt();
					retObj.consumes[i].COUNT=request.readInt();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CTreasureLevel{
		private static final Logger logger = LoggerFactory.getLogger(S2CTreasureLevel.class);
		public long guid;
		public int treasure_id;
		public IOGeneralBean general_bean;
		@Override
		public String toString() {
			return "S2CTreasureLevel [guid="+guid+",treasure_id="+treasure_id+",general_bean="+general_bean+",]";
		}
		public static final int msgCode = 390;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 390);
			retMsg.writeLong(guid);
			retMsg.writeInt(treasure_id);
			if(general_bean == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeLong(general_bean.guid);
					retMsg.writeInt(general_bean.gsid);
					retMsg.writeInt(general_bean.level);
					retMsg.writeInt(general_bean.star);
					retMsg.writeInt(general_bean.camp);
					retMsg.writeInt(general_bean.occu);
					retMsg.writeInt(general_bean.pclass);
					retMsg.writeInt(general_bean.power);
					if(general_bean.talent == null || general_bean.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.talent.size());
				for(Integer general_bean_talent1 : general_bean.talent){
			retMsg.writeInt(general_bean_talent1);
				}
			}
					retMsg.writeInt(general_bean.affairs);
					retMsg.writeInt(general_bean.treasure);
					if(general_bean.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.property.hp);
					retMsg.writeInt(general_bean.property.atk);
					retMsg.writeInt(general_bean.property.def);
					retMsg.writeInt(general_bean.property.mdef);
					retMsg.writeFloat(general_bean.property.atktime);
					retMsg.writeInt(general_bean.property.range);
					retMsg.writeInt(general_bean.property.msp);
					retMsg.writeInt(general_bean.property.pasp);
					retMsg.writeInt(general_bean.property.pcri);
					retMsg.writeInt(general_bean.property.pcrid);
					retMsg.writeInt(general_bean.property.pdam);
					retMsg.writeInt(general_bean.property.php);
					retMsg.writeInt(general_bean.property.patk);
					retMsg.writeInt(general_bean.property.pdef);
					retMsg.writeInt(general_bean.property.pmdef);
					retMsg.writeInt(general_bean.property.ppbs);
					retMsg.writeInt(general_bean.property.pmbs);
					retMsg.writeInt(general_bean.property.pefc);
					retMsg.writeInt(general_bean.property.ppthr);
					retMsg.writeInt(general_bean.property.patkdam);
					retMsg.writeInt(general_bean.property.pskidam);
					retMsg.writeInt(general_bean.property.pckatk);
					retMsg.writeInt(general_bean.property.pmthr);
					retMsg.writeInt(general_bean.property.pdex);
					retMsg.writeInt(general_bean.property.pmdex);
					retMsg.writeInt(general_bean.property.pmsatk);
					retMsg.writeInt(general_bean.property.pmps);
					retMsg.writeInt(general_bean.property.pcd);
			}
					if(general_bean.equip == null || general_bean.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.equip.size());
				for(Integer general_bean_equip1 : general_bean.equip){
			retMsg.writeInt(general_bean_equip1);
				}
			}
					if(general_bean.skill == null || general_bean.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.skill.size());
				for(Integer general_bean_skill1 : general_bean.skill){
			retMsg.writeInt(general_bean_skill1);
				}
			}
					if(general_bean.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(general_bean.exclusive.level);
					if(general_bean.exclusive.skill == null || general_bean.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.skill.size());
				for(Integer general_bean_exclusive_skill1 : general_bean.exclusive.skill){
			retMsg.writeInt(general_bean_exclusive_skill1);
				}
			}
					retMsg.writeInt(general_bean.exclusive.gsid);
					if(general_bean.exclusive.property == null || general_bean.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general_bean.exclusive.property.size());
				for(KvStringPair general_bean_exclusive_property1 : general_bean.exclusive.property){
					retMsg.writeString(general_bean_exclusive_property1.key);
					retMsg.writeInt(general_bean_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(general_bean.hppercent);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGeneralReborn{
		private static final Logger logger = LoggerFactory.getLogger(C2SGeneralReborn.class);
		public long general_uuid;
		public int action_type;
		@Override
		public String toString() {
			return "C2SGeneralReborn [general_uuid="+general_uuid+",action_type="+action_type+",]";
		}
		public static final int id = 391;

		public static C2SGeneralReborn parse(MyRequestMessage request){
			C2SGeneralReborn retObj = new C2SGeneralReborn();
			try{
			retObj.general_uuid=request.readLong();
			retObj.action_type=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGeneralReborn{
		private static final Logger logger = LoggerFactory.getLogger(S2CGeneralReborn.class);
		public List<RewardInfo> items;
		public List<IOAwardRandomGeneral> general;
		@Override
		public String toString() {
			return "S2CGeneralReborn [items="+items+",general="+general+",]";
		}
		public static final int msgCode = 392;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 392);
			if(items == null || items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(items.size());
				for(RewardInfo items1 : items){
					retMsg.writeInt(items1.GSID);
					retMsg.writeInt(items1.COUNT);
				}
			}
			if(general == null || general.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(general.size());
				for(IOAwardRandomGeneral general1 : general){
					retMsg.writeInt(general1.COUNT);
					retMsg.writeInt(general1.STAR);
					retMsg.writeInt(general1.CAMP);
					retMsg.writeInt(general1.OCCU);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGeneralWuhun{
		private static final Logger logger = LoggerFactory.getLogger(C2SGeneralWuhun.class);
		public long general_uuid;
		public int pos_index;
		public int wuhun_id;
		@Override
		public String toString() {
			return "C2SGeneralWuhun [general_uuid="+general_uuid+",pos_index="+pos_index+",wuhun_id="+wuhun_id+",]";
		}
		public static final int id = 393;

		public static C2SGeneralWuhun parse(MyRequestMessage request){
			C2SGeneralWuhun retObj = new C2SGeneralWuhun();
			try{
			retObj.general_uuid=request.readLong();
			retObj.pos_index=request.readInt();
			retObj.wuhun_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGeneralWuhun{
		private static final Logger logger = LoggerFactory.getLogger(S2CGeneralWuhun.class);
		public static final int msgCode = 394;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 394);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
