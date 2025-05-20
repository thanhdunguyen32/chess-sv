package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.Map;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IODungeonList;
import ws.WsMessageBase.IOGeneralBean;
import ws.WsMessageBase.IOProperty;
import ws.WsMessageBase.IOExclusive;
import ws.WsMessageBase.KvStringPair;
import ws.WsMessageBase.IODungeonGlobalBuf;
import ws.WsMessageBase.IORewardItem;
import ws.WsMessageBase.IODungeonPotion;
import ws.WsMessageBase.IODungeonPosition;
import ws.WsMessageBase.IODungeonNode;
import ws.WsMessageBase.IODungeonNodePos;
import ws.WsMessageBase.IODungeonBuffList;
import ws.WsMessageBase.IODungeonChooseDetail;
import ws.WsMessageBase.IODungeonBset;
import ws.WsMessageBase.IODungeonShop;

public final class WsMessageDungeon{
	public static final class C2SDungeonOpenTime{
		private static final Logger logger = LoggerFactory.getLogger(C2SDungeonOpenTime.class);
		public static final int id = 1101;
	}
	public static final class S2CDungeonOpenTime{
		private static final Logger logger = LoggerFactory.getLogger(S2CDungeonOpenTime.class);
		public List<Integer> open;
		@Override
		public String toString() {
			return "S2CDungeonOpenTime [open="+open+",]";
		}
		public static final int msgCode = 1102;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1102);
			if(open == null || open.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(open.size());
				for(Integer open1 : open){
			retMsg.writeInt(open1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDungeonChapterList{
		private static final Logger logger = LoggerFactory.getLogger(C2SDungeonChapterList.class);
		public static final int id = 1103;
	}
	public static final class S2CDungeonChapterList{
		private static final Logger logger = LoggerFactory.getLogger(S2CDungeonChapterList.class);
		public List<IODungeonList> dungeonlist;
		public List<IOGeneralBean> dungeonset;
		public List<IODungeonGlobalBuf> globalbuf;
		public boolean formation_exist;
		@Override
		public String toString() {
			return "S2CDungeonChapterList [dungeonlist="+dungeonlist+",dungeonset="+dungeonset+",globalbuf="+globalbuf+",formation_exist="+formation_exist+",]";
		}
		public static final int msgCode = 1104;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1104);
			if(dungeonlist == null || dungeonlist.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(dungeonlist.size());
				for(IODungeonList dungeonlist1 : dungeonlist){
					retMsg.writeInt(dungeonlist1.isget);
				}
			}
			if(dungeonset == null || dungeonset.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(dungeonset.size());
				for(IOGeneralBean dungeonset1 : dungeonset){
					retMsg.writeLong(dungeonset1.guid);
					retMsg.writeInt(dungeonset1.gsid);
					retMsg.writeInt(dungeonset1.level);
					retMsg.writeInt(dungeonset1.star);
					retMsg.writeInt(dungeonset1.camp);
					retMsg.writeInt(dungeonset1.occu);
					retMsg.writeInt(dungeonset1.pclass);
					retMsg.writeInt(dungeonset1.power);
					if(dungeonset1.talent == null || dungeonset1.talent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(dungeonset1.talent.size());
				for(Integer dungeonset1_talent1 : dungeonset1.talent){
			retMsg.writeInt(dungeonset1_talent1);
				}
			}
					retMsg.writeInt(dungeonset1.affairs);
					retMsg.writeInt(dungeonset1.treasure);
					if(dungeonset1.property == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(dungeonset1.property.hp);
					retMsg.writeInt(dungeonset1.property.atk);
					retMsg.writeInt(dungeonset1.property.def);
					retMsg.writeInt(dungeonset1.property.mdef);
					retMsg.writeFloat(dungeonset1.property.atktime);
					retMsg.writeInt(dungeonset1.property.range);
					retMsg.writeInt(dungeonset1.property.msp);
					retMsg.writeInt(dungeonset1.property.pasp);
					retMsg.writeInt(dungeonset1.property.pcri);
					retMsg.writeInt(dungeonset1.property.pcrid);
					retMsg.writeInt(dungeonset1.property.pdam);
					retMsg.writeInt(dungeonset1.property.php);
					retMsg.writeInt(dungeonset1.property.patk);
					retMsg.writeInt(dungeonset1.property.pdef);
					retMsg.writeInt(dungeonset1.property.pmdef);
					retMsg.writeInt(dungeonset1.property.ppbs);
					retMsg.writeInt(dungeonset1.property.pmbs);
					retMsg.writeInt(dungeonset1.property.pefc);
					retMsg.writeInt(dungeonset1.property.ppthr);
					retMsg.writeInt(dungeonset1.property.patkdam);
					retMsg.writeInt(dungeonset1.property.pskidam);
					retMsg.writeInt(dungeonset1.property.pckatk);
					retMsg.writeInt(dungeonset1.property.pmthr);
					retMsg.writeInt(dungeonset1.property.pdex);
					retMsg.writeInt(dungeonset1.property.pmdex);
					retMsg.writeInt(dungeonset1.property.pmsatk);
					retMsg.writeInt(dungeonset1.property.pmps);
					retMsg.writeInt(dungeonset1.property.pcd);
			}
					if(dungeonset1.equip == null || dungeonset1.equip.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(dungeonset1.equip.size());
				for(Integer dungeonset1_equip1 : dungeonset1.equip){
			retMsg.writeInt(dungeonset1_equip1);
				}
			}
					if(dungeonset1.skill == null || dungeonset1.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(dungeonset1.skill.size());
				for(Integer dungeonset1_skill1 : dungeonset1.skill){
			retMsg.writeInt(dungeonset1_skill1);
				}
			}
					if(dungeonset1.exclusive == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(dungeonset1.exclusive.level);
					if(dungeonset1.exclusive.skill == null || dungeonset1.exclusive.skill.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(dungeonset1.exclusive.skill.size());
				for(Integer dungeonset1_exclusive_skill1 : dungeonset1.exclusive.skill){
			retMsg.writeInt(dungeonset1_exclusive_skill1);
				}
			}
					retMsg.writeInt(dungeonset1.exclusive.gsid);
					if(dungeonset1.exclusive.property == null || dungeonset1.exclusive.property.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(dungeonset1.exclusive.property.size());
				for(KvStringPair dungeonset1_exclusive_property1 : dungeonset1.exclusive.property){
					retMsg.writeString(dungeonset1_exclusive_property1.key);
					retMsg.writeInt(dungeonset1_exclusive_property1.val);
				}
			}
			}
					retMsg.writeInt(dungeonset1.hppercent);
				}
			}
			if(globalbuf == null || globalbuf.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(globalbuf.size());
				for(IODungeonGlobalBuf globalbuf1 : globalbuf){
					retMsg.writeInt(globalbuf1.hp);
					retMsg.writeInt(globalbuf1.atk);
					retMsg.writeInt(globalbuf1.def);
					retMsg.writeInt(globalbuf1.mdef);
				}
			}
			retMsg.writeBool(formation_exist);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDungeonSaveGeneralList{
		private static final Logger logger = LoggerFactory.getLogger(C2SDungeonSaveGeneralList.class);
		public long[] guids;
		@Override
		public String toString() {
			return "C2SDungeonSaveGeneralList [guids="+java.util.Arrays.toString(guids)+",]";
		}
		public static final int id = 1105;

		public static C2SDungeonSaveGeneralList parse(MyRequestMessage request){
			C2SDungeonSaveGeneralList retObj = new C2SDungeonSaveGeneralList();
			try{
			int guids_size = request.readInt();
				retObj.guids = new long[guids_size];
				for(int i=0;i<guids_size;i++){
					retObj.guids[i]=request.readLong();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CDungeonSaveGeneralList{
		private static final Logger logger = LoggerFactory.getLogger(S2CDungeonSaveGeneralList.class);
		public List<IOGeneralBean> generals;
		public List<IODungeonGlobalBuf> globalbuf;
		public List<IORewardItem> reward;
		public List<IODungeonPotion> potion;
		@Override
		public String toString() {
			return "S2CDungeonSaveGeneralList [generals="+generals+",globalbuf="+globalbuf+",reward="+reward+",potion="+potion+",]";
		}
		public static final int msgCode = 1106;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1106);
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
			if(globalbuf == null || globalbuf.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(globalbuf.size());
				for(IODungeonGlobalBuf globalbuf1 : globalbuf){
					retMsg.writeInt(globalbuf1.hp);
					retMsg.writeInt(globalbuf1.atk);
					retMsg.writeInt(globalbuf1.def);
					retMsg.writeInt(globalbuf1.mdef);
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
			if(potion == null || potion.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(potion.size());
				for(IODungeonPotion potion1 : potion){
					retMsg.writeInt(potion1.id);
					retMsg.writeInt(potion1.count);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDungeonChapterReward{
		private static final Logger logger = LoggerFactory.getLogger(C2SDungeonChapterReward.class);
		public int chapter;
		@Override
		public String toString() {
			return "C2SDungeonChapterReward [chapter="+chapter+",]";
		}
		public static final int id = 1107;

		public static C2SDungeonChapterReward parse(MyRequestMessage request){
			C2SDungeonChapterReward retObj = new C2SDungeonChapterReward();
			try{
			retObj.chapter=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CDungeonChapterReward{
		private static final Logger logger = LoggerFactory.getLogger(S2CDungeonChapterReward.class);
		public List<IORewardItem> reward;
		@Override
		public String toString() {
			return "S2CDungeonChapterReward [reward="+reward+",]";
		}
		public static final int msgCode = 1108;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1108);
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
	public static final class C2SDungeonChapterInfo{
		private static final Logger logger = LoggerFactory.getLogger(C2SDungeonChapterInfo.class);
		public static final int id = 1109;
	}
	public static final class S2CDungeonChapterInfo{
		private static final Logger logger = LoggerFactory.getLogger(S2CDungeonChapterInfo.class);
		public IODungeonPosition position;
		public List<IODungeonNode> nodelist;
		public List<IODungeonPotion> potion;
		public IODungeonBuffList bufflist;
		public IODungeonBuffList spbufflist;
		@Override
		public String toString() {
			return "S2CDungeonChapterInfo [position="+position+",nodelist="+nodelist+",potion="+potion+",bufflist="+bufflist+",spbufflist="+spbufflist+",]";
		}
		public static final int msgCode = 1110;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1110);
			if(position == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(position.node);
					retMsg.writeInt(position.pos);
			}
			if(nodelist == null || nodelist.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(nodelist.size());
				for(IODungeonNode nodelist1 : nodelist){
					if(nodelist1.poslist == null || nodelist1.poslist.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(nodelist1.poslist.size());
				for(IODungeonNodePos nodelist1_poslist1 : nodelist1.poslist){
					retMsg.writeInt(nodelist1_poslist1.type);
					retMsg.writeInt(nodelist1_poslist1.choose);
					retMsg.writeLong(nodelist1_poslist1.finish);
				}
			}
				}
			}
			if(potion == null || potion.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(potion.size());
				for(IODungeonPotion potion1 : potion){
					retMsg.writeInt(potion1.id);
					retMsg.writeInt(potion1.count);
				}
			}
			if(bufflist == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeFloat(bufflist.ppthr);
					retMsg.writeFloat(bufflist.pskidam);
					retMsg.writeFloat(bufflist.atk);
					retMsg.writeFloat(bufflist.pcrid);
					retMsg.writeFloat(bufflist.pmthr);
					retMsg.writeFloat(bufflist.pcri);
			}
			if(spbufflist == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeFloat(spbufflist.ppthr);
					retMsg.writeFloat(spbufflist.pskidam);
					retMsg.writeFloat(spbufflist.atk);
					retMsg.writeFloat(spbufflist.pcrid);
					retMsg.writeFloat(spbufflist.pmthr);
					retMsg.writeFloat(spbufflist.pcri);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDungeonChooseNode{
		private static final Logger logger = LoggerFactory.getLogger(C2SDungeonChooseNode.class);
		public int chapter;
		public int node;
		public int pos;
		@Override
		public String toString() {
			return "C2SDungeonChooseNode [chapter="+chapter+",node="+node+",pos="+pos+",]";
		}
		public static final int id = 1111;

		public static C2SDungeonChooseNode parse(MyRequestMessage request){
			C2SDungeonChooseNode retObj = new C2SDungeonChooseNode();
			try{
			retObj.chapter=request.readInt();
			retObj.node=request.readInt();
			retObj.pos=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CDungeonChooseNode{
		private static final Logger logger = LoggerFactory.getLogger(S2CDungeonChooseNode.class);
		public int secret;
		public int type;
		public IODungeonChooseDetail detail;
		@Override
		public String toString() {
			return "S2CDungeonChooseNode [secret="+secret+",type="+type+",detail="+detail+",]";
		}
		public static final int msgCode = 1112;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1112);
			retMsg.writeInt(secret);
			retMsg.writeInt(type);
			if(detail == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(detail.id);
					retMsg.writeString(detail.name);
					retMsg.writeInt(detail.gsid);
					if(detail.set == null || detail.set.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(detail.set.size());
				for(Map.Entry<Integer,IODungeonBset> detail_set1 : detail.set.entrySet()){
			retMsg.writeInt(detail_set1.getKey());
					retMsg.writeInt(detail_set1.getValue().gsid);
					retMsg.writeInt(detail_set1.getValue().level);
					retMsg.writeInt(detail_set1.getValue().pclass);
					retMsg.writeFloat(detail_set1.getValue().exhp);
					retMsg.writeFloat(detail_set1.getValue().exatk);
				}
			}
					if(detail.hppercent == null || detail.hppercent.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(detail.hppercent.size());
				for(Map.Entry<Integer,Integer> detail_hppercent1 : detail.hppercent.entrySet()){
			retMsg.writeInt(detail_hppercent1.getKey());
			retMsg.writeInt(detail_hppercent1.getValue());
				}
			}
					if(detail.buffs == null || detail.buffs.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(detail.buffs.size());
				for(Integer detail_buffs1 : detail.buffs){
			retMsg.writeInt(detail_buffs1);
				}
			}
					retMsg.writeInt(detail.disc);
					if(detail.item == null || detail.item.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(detail.item.size());
				for(IORewardItem detail_item1 : detail.item){
					retMsg.writeInt(detail_item1.GSID);
					retMsg.writeInt(detail_item1.COUNT);
				}
			}
					if(detail.consume == null || detail.consume.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(detail.consume.size());
				for(IORewardItem detail_consume1 : detail.consume){
					retMsg.writeInt(detail_consume1.GSID);
					retMsg.writeInt(detail_consume1.COUNT);
				}
			}
					retMsg.writeInt(detail.quality);
					retMsg.writeInt(detail.refnum);
					if(detail.goods == null || detail.goods.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(detail.goods.size());
				for(IORewardItem detail_goods1 : detail.goods){
					retMsg.writeInt(detail_goods1.GSID);
					retMsg.writeInt(detail_goods1.COUNT);
				}
			}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDungeonUsePotion{
		private static final Logger logger = LoggerFactory.getLogger(C2SDungeonUsePotion.class);
		public int potion;
		public long guid;
		@Override
		public String toString() {
			return "C2SDungeonUsePotion [potion="+potion+",guid="+guid+",]";
		}
		public static final int id = 1113;

		public static C2SDungeonUsePotion parse(MyRequestMessage request){
			C2SDungeonUsePotion retObj = new C2SDungeonUsePotion();
			try{
			retObj.potion=request.readInt();
			retObj.guid=request.readLong();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CDungeonUsePotion{
		private static final Logger logger = LoggerFactory.getLogger(S2CDungeonUsePotion.class);
		public long guid;
		public int hppercent;
		public S2CDungeonUsePotion(long pguid,int phppercent){
			guid=pguid;
			hppercent=phppercent;
		}
		public S2CDungeonUsePotion(){}
		@Override
		public String toString() {
			return "S2CDungeonUsePotion [guid="+guid+",hppercent="+hppercent+",]";
		}
		public static final int msgCode = 1114;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1114);
			retMsg.writeLong(guid);
			retMsg.writeInt(hppercent);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDungeonChooseBuf{
		private static final Logger logger = LoggerFactory.getLogger(C2SDungeonChooseBuf.class);
		public int chapter;
		public int node;
		public int pos;
		public int buff;
		@Override
		public String toString() {
			return "C2SDungeonChooseBuf [chapter="+chapter+",node="+node+",pos="+pos+",buff="+buff+",]";
		}
		public static final int id = 1115;

		public static C2SDungeonChooseBuf parse(MyRequestMessage request){
			C2SDungeonChooseBuf retObj = new C2SDungeonChooseBuf();
			try{
			retObj.chapter=request.readInt();
			retObj.node=request.readInt();
			retObj.pos=request.readInt();
			retObj.buff=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CDungeonChooseBuf{
		private static final Logger logger = LoggerFactory.getLogger(S2CDungeonChooseBuf.class);
		public int buffid;
		public S2CDungeonChooseBuf(int pbuffid){
			buffid=pbuffid;
		}
		public S2CDungeonChooseBuf(){}
		@Override
		public String toString() {
			return "S2CDungeonChooseBuf [buffid="+buffid+",]";
		}
		public static final int msgCode = 1116;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1116);
			retMsg.writeInt(buffid);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDungeonChooseShop{
		private static final Logger logger = LoggerFactory.getLogger(C2SDungeonChooseShop.class);
		public int chapter;
		public int node;
		public int pos;
		public int buy;
		@Override
		public String toString() {
			return "C2SDungeonChooseShop [chapter="+chapter+",node="+node+",pos="+pos+",buy="+buy+",]";
		}
		public static final int id = 1117;

		public static C2SDungeonChooseShop parse(MyRequestMessage request){
			C2SDungeonChooseShop retObj = new C2SDungeonChooseShop();
			try{
			retObj.chapter=request.readInt();
			retObj.node=request.readInt();
			retObj.pos=request.readInt();
			retObj.buy=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CDungeonChooseShop{
		private static final Logger logger = LoggerFactory.getLogger(S2CDungeonChooseShop.class);
		public static final int msgCode = 1118;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1118);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDungeonShopList{
		private static final Logger logger = LoggerFactory.getLogger(C2SDungeonShopList.class);
		public static final int id = 1119;
	}
	public static final class S2CDungeonShopList{
		private static final Logger logger = LoggerFactory.getLogger(S2CDungeonShopList.class);
		public List<IODungeonShop> list;
		@Override
		public String toString() {
			return "S2CDungeonShopList [list="+list+",]";
		}
		public static final int msgCode = 1120;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1120);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IODungeonShop list1 : list){
					retMsg.writeInt(list1.chapter);
					retMsg.writeInt(list1.node);
					retMsg.writeInt(list1.disc);
					if(list1.item == null || list1.item.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.item.size());
				for(IORewardItem list1_item1 : list1.item){
					retMsg.writeInt(list1_item1.GSID);
					retMsg.writeInt(list1_item1.COUNT);
				}
			}
					if(list1.consume == null || list1.consume.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.consume.size());
				for(IORewardItem list1_consume1 : list1.consume){
					retMsg.writeInt(list1_consume1.GSID);
					retMsg.writeInt(list1_consume1.COUNT);
				}
			}
					retMsg.writeInt(list1.quality);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDungeonShopBuy{
		private static final Logger logger = LoggerFactory.getLogger(C2SDungeonShopBuy.class);
		public int chapter;
		public int node;
		@Override
		public String toString() {
			return "C2SDungeonShopBuy [chapter="+chapter+",node="+node+",]";
		}
		public static final int id = 1121;

		public static C2SDungeonShopBuy parse(MyRequestMessage request){
			C2SDungeonShopBuy retObj = new C2SDungeonShopBuy();
			try{
			retObj.chapter=request.readInt();
			retObj.node=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CDungeonShopBuy{
		private static final Logger logger = LoggerFactory.getLogger(S2CDungeonShopBuy.class);
		public static final int msgCode = 1122;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 1122);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
