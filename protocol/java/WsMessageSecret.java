package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOSecretBoxAward;
import ws.WsMessageBase.SecretItemInfo;
import ws.WsMessageBase.IOSecretHero;

public final class WsMessageSecret{
	public static final class C2SSecretView{
		private static final Logger logger = LoggerFactory.getLogger(C2SSecretView.class);
		public static final int id = 3301;
	}
	public static final class S2CSecretView{
		private static final Logger logger = LoggerFactory.getLogger(S2CSecretView.class);
		public int map_id;
		public int progress;
		public boolean could_reset;
		public List<IOSecretBoxAward> boxAward;
		public List<IOSecretHero> my_cost;
		public List<IOSecretHero> enemy_cost;
		public List<IOSecretHero> online_formation;
		public int revive_count;
		@Override
		public String toString() {
			return "S2CSecretView [map_id="+map_id+",progress="+progress+",could_reset="+could_reset+",boxAward="+boxAward+",my_cost="+my_cost+",enemy_cost="+enemy_cost+",online_formation="+online_formation+",revive_count="+revive_count+",]";
		}
		public static final int msgCode = 3302;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3302);
			retMsg.writeInt(map_id);
			retMsg.writeInt(progress);
			retMsg.writeBool(could_reset);
			if(boxAward == null || boxAward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(boxAward.size());
				for(IOSecretBoxAward boxAward1 : boxAward){
					retMsg.writeInt(boxAward1.stage_index);
					if(boxAward1.award_list == null || boxAward1.award_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(boxAward1.award_list.size());
				for(SecretItemInfo boxAward1_award_list1 : boxAward1.award_list){
					retMsg.writeInt(boxAward1_award_list1.itemId);
					retMsg.writeInt(boxAward1_award_list1.cnt);
				}
			}
					retMsg.writeInt(boxAward1.is_get);
				}
			}
			if(my_cost == null || my_cost.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(my_cost.size());
				for(IOSecretHero my_cost1 : my_cost){
					retMsg.writeInt(my_cost1.hero_type);
					retMsg.writeInt(my_cost1.hero_id);
					retMsg.writeInt(my_cost1.hp_percent);
				}
			}
			if(enemy_cost == null || enemy_cost.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(enemy_cost.size());
				for(IOSecretHero enemy_cost1 : enemy_cost){
					retMsg.writeInt(enemy_cost1.hero_type);
					retMsg.writeInt(enemy_cost1.hero_id);
					retMsg.writeInt(enemy_cost1.hp_percent);
				}
			}
			if(online_formation == null || online_formation.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(online_formation.size());
				for(IOSecretHero online_formation1 : online_formation){
					retMsg.writeInt(online_formation1.hero_type);
					retMsg.writeInt(online_formation1.hero_id);
					retMsg.writeInt(online_formation1.hp_percent);
				}
			}
			retMsg.writeInt(revive_count);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSecretBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(C2SSecretBattleStart.class);
		public int map_id;
		public IOSecretHero[] online_formation;
		@Override
		public String toString() {
			return "C2SSecretBattleStart [map_id="+map_id+",online_formation="+java.util.Arrays.toString(online_formation)+",]";
		}
		public static final int id = 3303;

		public static C2SSecretBattleStart parse(MyRequestMessage request){
			C2SSecretBattleStart retObj = new C2SSecretBattleStart();
			try{
			retObj.map_id=request.readInt();
			int online_formation_size = request.readInt();
				retObj.online_formation = new IOSecretHero[online_formation_size];
				for(int i=0;i<online_formation_size;i++){
						retObj.online_formation[i] = new IOSecretHero();
					retObj.online_formation[i].hero_type=request.readInt();
					retObj.online_formation[i].hero_id=request.readInt();
					retObj.online_formation[i].hp_percent=request.readInt();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSecretBattleStart{
		private static final Logger logger = LoggerFactory.getLogger(S2CSecretBattleStart.class);
		public int map_id;
		public List<IOSecretHero> online_formation;
		public int is_reset;
		@Override
		public String toString() {
			return "S2CSecretBattleStart [map_id="+map_id+",online_formation="+online_formation+",is_reset="+is_reset+",]";
		}
		public static final int msgCode = 3304;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3304);
			retMsg.writeInt(map_id);
			if(online_formation == null || online_formation.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(online_formation.size());
				for(IOSecretHero online_formation1 : online_formation){
					retMsg.writeInt(online_formation1.hero_type);
					retMsg.writeInt(online_formation1.hero_id);
					retMsg.writeInt(online_formation1.hp_percent);
				}
			}
			retMsg.writeInt(is_reset);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSecretBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(C2SSecretBattleEnd.class);
		public boolean is_win;
		public IOSecretHero[] my_cost;
		public IOSecretHero[] enemy_cost;
		public boolean is_interrupt;
		@Override
		public String toString() {
			return "C2SSecretBattleEnd [is_win="+is_win+",my_cost="+java.util.Arrays.toString(my_cost)+",enemy_cost="+java.util.Arrays.toString(enemy_cost)+",is_interrupt="+is_interrupt+",]";
		}
		public static final int id = 3305;

		public static C2SSecretBattleEnd parse(MyRequestMessage request){
			C2SSecretBattleEnd retObj = new C2SSecretBattleEnd();
			try{
			retObj.is_win=request.readBool();
			int my_cost_size = request.readInt();
				retObj.my_cost = new IOSecretHero[my_cost_size];
				for(int i=0;i<my_cost_size;i++){
						retObj.my_cost[i] = new IOSecretHero();
					retObj.my_cost[i].hero_type=request.readInt();
					retObj.my_cost[i].hero_id=request.readInt();
					retObj.my_cost[i].hp_percent=request.readInt();
				}
			int enemy_cost_size = request.readInt();
				retObj.enemy_cost = new IOSecretHero[enemy_cost_size];
				for(int i=0;i<enemy_cost_size;i++){
						retObj.enemy_cost[i] = new IOSecretHero();
					retObj.enemy_cost[i].hero_type=request.readInt();
					retObj.enemy_cost[i].hero_id=request.readInt();
					retObj.enemy_cost[i].hp_percent=request.readInt();
				}
			retObj.is_interrupt=request.readBool();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSecretBattleEnd{
		private static final Logger logger = LoggerFactory.getLogger(S2CSecretBattleEnd.class);
		public boolean is_win;
		public List<SecretItemInfo> reward_items;
		public boolean is_interrupt;
		public int map_id;
		public int progress;
		public List<IOSecretBoxAward> boxAward;
		public List<IOSecretHero> my_cost;
		public List<IOSecretHero> enemy_cost;
		public List<IOSecretHero> online_formation;
		@Override
		public String toString() {
			return "S2CSecretBattleEnd [is_win="+is_win+",reward_items="+reward_items+",is_interrupt="+is_interrupt+",map_id="+map_id+",progress="+progress+",boxAward="+boxAward+",my_cost="+my_cost+",enemy_cost="+enemy_cost+",online_formation="+online_formation+",]";
		}
		public static final int msgCode = 3306;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3306);
			retMsg.writeBool(is_win);
			if(reward_items == null || reward_items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward_items.size());
				for(SecretItemInfo reward_items1 : reward_items){
					retMsg.writeInt(reward_items1.itemId);
					retMsg.writeInt(reward_items1.cnt);
				}
			}
			retMsg.writeBool(is_interrupt);
			retMsg.writeInt(map_id);
			retMsg.writeInt(progress);
			if(boxAward == null || boxAward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(boxAward.size());
				for(IOSecretBoxAward boxAward1 : boxAward){
					retMsg.writeInt(boxAward1.stage_index);
					if(boxAward1.award_list == null || boxAward1.award_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(boxAward1.award_list.size());
				for(SecretItemInfo boxAward1_award_list1 : boxAward1.award_list){
					retMsg.writeInt(boxAward1_award_list1.itemId);
					retMsg.writeInt(boxAward1_award_list1.cnt);
				}
			}
					retMsg.writeInt(boxAward1.is_get);
				}
			}
			if(my_cost == null || my_cost.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(my_cost.size());
				for(IOSecretHero my_cost1 : my_cost){
					retMsg.writeInt(my_cost1.hero_type);
					retMsg.writeInt(my_cost1.hero_id);
					retMsg.writeInt(my_cost1.hp_percent);
				}
			}
			if(enemy_cost == null || enemy_cost.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(enemy_cost.size());
				for(IOSecretHero enemy_cost1 : enemy_cost){
					retMsg.writeInt(enemy_cost1.hero_type);
					retMsg.writeInt(enemy_cost1.hero_id);
					retMsg.writeInt(enemy_cost1.hp_percent);
				}
			}
			if(online_formation == null || online_formation.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(online_formation.size());
				for(IOSecretHero online_formation1 : online_formation){
					retMsg.writeInt(online_formation1.hero_type);
					retMsg.writeInt(online_formation1.hero_id);
					retMsg.writeInt(online_formation1.hp_percent);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSecretGetAward{
		private static final Logger logger = LoggerFactory.getLogger(C2SSecretGetAward.class);
		public int stage_index;
		@Override
		public String toString() {
			return "C2SSecretGetAward [stage_index="+stage_index+",]";
		}
		public static final int id = 3307;

		public static C2SSecretGetAward parse(MyRequestMessage request){
			C2SSecretGetAward retObj = new C2SSecretGetAward();
			try{
			retObj.stage_index=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSecretGetAward{
		private static final Logger logger = LoggerFactory.getLogger(S2CSecretGetAward.class);
		public int stage_index;
		public List<SecretItemInfo> reward_items;
		@Override
		public String toString() {
			return "S2CSecretGetAward [stage_index="+stage_index+",reward_items="+reward_items+",]";
		}
		public static final int msgCode = 3308;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3308);
			retMsg.writeInt(stage_index);
			if(reward_items == null || reward_items.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(reward_items.size());
				for(SecretItemInfo reward_items1 : reward_items){
					retMsg.writeInt(reward_items1.itemId);
					retMsg.writeInt(reward_items1.cnt);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSecretSoldierRevive{
		private static final Logger logger = LoggerFactory.getLogger(C2SSecretSoldierRevive.class);
		public int hero_id;
		@Override
		public String toString() {
			return "C2SSecretSoldierRevive [hero_id="+hero_id+",]";
		}
		public static final int id = 3309;

		public static C2SSecretSoldierRevive parse(MyRequestMessage request){
			C2SSecretSoldierRevive retObj = new C2SSecretSoldierRevive();
			try{
			retObj.hero_id=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSecretSoldierRevive{
		private static final Logger logger = LoggerFactory.getLogger(S2CSecretSoldierRevive.class);
		public int hero_id;
		public S2CSecretSoldierRevive(int phero_id){
			hero_id=phero_id;
		}
		public S2CSecretSoldierRevive(){}
		@Override
		public String toString() {
			return "S2CSecretSoldierRevive [hero_id="+hero_id+",]";
		}
		public static final int msgCode = 3310;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3310);
			retMsg.writeInt(hero_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSecretReset{
		private static final Logger logger = LoggerFactory.getLogger(C2SSecretReset.class);
		public static final int id = 3311;
	}
	public static final class S2CSecretReset{
		private static final Logger logger = LoggerFactory.getLogger(S2CSecretReset.class);
		public static final int msgCode = 3312;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 3312);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
