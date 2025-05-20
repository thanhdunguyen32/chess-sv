import { net } from "./LejiSocket";
import { WsEncoder, WsDecoder } from "./WebsocketCodec";

export module protocol{

	export class MyProtocols {

		static send_C2SGetYuekaAward(senderSocket:net.LejiSocket,p_type:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(135);
	wsEncoder.writeInt(p_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOMailFrom(myDecoder:WsDecoder):IOMailFrom{
				var retObj = new IOMailFrom();
	retObj.type=myDecoder.readString();
	retObj.fid=myDecoder.readInt();
	retObj.legion=myDecoder.readInt();
			return retObj;
		}

		static get_IOGeneralSimple(myDecoder:WsDecoder):IOGeneralSimple{
				var retObj = new IOGeneralSimple();
	retObj.pos=myDecoder.readInt();
	retObj.gsid=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.hpcover=myDecoder.readInt();
	retObj.class=myDecoder.readInt();
	retObj.nowhp=myDecoder.readLong();
	retObj.exhp=myDecoder.readFloat();
	retObj.exatk=myDecoder.readFloat();
			return retObj;
		}

		static get_336(myDecoder:WsDecoder):S2CAffairReel{
				var retObj = new S2CAffairReel();
	retObj.scroll_id=myDecoder.readInt();
			return retObj;
		}

		static send_C2SFriendBossFarm(senderSocket:net.LejiSocket,p_boss_owner_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(477);
	wsEncoder.writeInt(p_boss_owner_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1066(myDecoder:WsDecoder):S2CFriendBattleStart{
				var retObj = new S2CFriendBattleStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static get_1204(myDecoder:WsDecoder):S2CActivitiesGetAward{
				var retObj = new S2CActivitiesGetAward();
	retObj.activeIdType=myDecoder.readInt();
	retObj.item_index=myDecoder.readInt();
	retObj.gain = new Array<ActivitiesItem>();
	let gain_size = myDecoder.readInt();
	if(gain_size >0){
		for(var i=0; i<gain_size;i++){
				retObj.gain[i] = MyProtocols.get_ActivitiesItem(myDecoder);
		}
	}
	retObj.lottery_is_pay=myDecoder.readBool();
			return retObj;
		}

		static get_1052(myDecoder:WsDecoder):S2CLegionBossBattleStart{
				var retObj = new S2CLegionBossBattleStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static send_C2STgslBossStart(senderSocket:net.LejiSocket,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1077);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOTnqwEvent(myDecoder:WsDecoder):IOTnqwEvent{
				var retObj = new IOTnqwEvent();
	retObj.mark=myDecoder.readInt();
	retObj.limit=myDecoder.readInt();
	retObj.intro=myDecoder.readString();
			return retObj;
		}

		static send_C2SBagExpand(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(309);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_386(myDecoder:WsDecoder):S2CTreasureTakeon{
				var retObj = new S2CTreasureTakeon();
	retObj.guid=myDecoder.readLong();
	retObj.treasure_id=myDecoder.readInt();
	let general_bean_exist = myDecoder.readBool();
	if(general_bean_exist == true){
		retObj.general_bean = MyProtocols.get_IOGeneralBean(myDecoder);
	}
			return retObj;
		}

		static get_542(myDecoder:WsDecoder):S2CMapEventList{
				var retObj = new S2CMapEventList();
	retObj.list = {};
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
			let list_key =myDecoder.readInt();
				retObj.list[list_key] = MyProtocols.get_IOMapEvent(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SMylbList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1219);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1112(myDecoder:WsDecoder):S2CDungeonChooseNode{
				var retObj = new S2CDungeonChooseNode();
	retObj.secret=myDecoder.readInt();
	retObj.type=myDecoder.readInt();
	let detail_exist = myDecoder.readBool();
	if(detail_exist == true){
		retObj.detail = MyProtocols.get_IODungeonChooseDetail(myDecoder);
	}
			return retObj;
		}

		static get_1108(myDecoder:WsDecoder):S2CDungeonChapterReward{
				var retObj = new S2CDungeonChapterReward();
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_572(myDecoder:WsDecoder):S2CBigBattleInfo{
				var retObj = new S2CBigBattleInfo();
	retObj.items = new Array<IOGeneralSimple>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOGeneralSimple(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOLegionFactoryDonation(myDecoder:WsDecoder):IOLegionFactoryDonation{
				var retObj = new IOLegionFactoryDonation();
	retObj.name=myDecoder.readString();
	retObj.icon=myDecoder.readInt();
	retObj.headid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.score=myDecoder.readInt();
	retObj.pos=myDecoder.readInt();
	retObj.last=myDecoder.readLong();
			return retObj;
		}

		static send_C2STowerReplay(senderSocket:net.LejiSocket,p_tower_level:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(549);
	wsEncoder.writeInt(p_tower_level);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SDungeonBattleEnd(senderSocket:net.LejiSocket,p_chapter:number,p_node:number,p_pos:number,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1057);
	wsEncoder.writeInt(p_chapter);
	wsEncoder.writeInt(p_node);
	wsEncoder.writeInt(p_pos);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_302(myDecoder:WsDecoder):S2CItemList{
				var retObj = new S2CItemList();
	retObj.item_list = new Array<SimpleItemInfo>();
	let item_list_size = myDecoder.readInt();
	if(item_list_size >0){
		for(var i=0; i<item_list_size;i++){
				retObj.item_list[i] = MyProtocols.get_SimpleItemInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOBattleSet(myDecoder:WsDecoder):IOBattleSet{
				var retObj = new IOBattleSet();
	retObj.mythic=myDecoder.readInt();
	retObj.power=myDecoder.readInt();
	retObj.team = {};
	let team_size = myDecoder.readInt();
	if(team_size >0){
		for(var i=0; i<team_size;i++){
			let team_key =myDecoder.readInt();
				retObj.team[team_key] = MyProtocols.get_IOGeneralBean(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SOccTaskGiftOne(senderSocket:net.LejiSocket,p_level_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(563);
	wsEncoder.writeInt(p_level_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SDjjfInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1233);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1006(myDecoder:WsDecoder):S2CGuideOne{
				var retObj = new S2CGuideOne();
	retObj.isHas=myDecoder.readBool();
			return retObj;
		}

		static send_C2SOccTaskPackRefresh(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(567);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_806(myDecoder:WsDecoder):S2CListOnlineAward{
				var retObj = new S2CListOnlineAward();
	retObj.need_online_time=myDecoder.readLong();
	retObj.reward_index=myDecoder.readInt();
			return retObj;
		}

		static send_C2SSecretBattleStart(senderSocket:net.LejiSocket,p_map_id:number,p_online_formation:Array<IOSecretHero>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3303);
	wsEncoder.writeInt(p_map_id);
	if(p_online_formation == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_online_formation.length);
		p_online_formation.forEach(function(p_online_formation_v){
			wsEncoder.writeInt(p_online_formation_v.hero_type);
			wsEncoder.writeInt(p_online_formation_v.hero_id);
			wsEncoder.writeInt(p_online_formation_v.hp_percent);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2S333PayPre(senderSocket:net.LejiSocket,p_gameId:number,p_goodsName:string,p_goodsId:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(151);
	wsEncoder.writeInt(p_gameId);
	wsEncoder.writeString(p_goodsName);
	wsEncoder.writeInt(p_goodsId);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1240(myDecoder:WsDecoder):S2CGetSzhc{
				var retObj = new S2CGetSzhc();
	retObj.list = new Array<IOSzhc>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOSzhc(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SGmCmd(senderSocket:net.LejiSocket,p_cmd:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(101);
	wsEncoder.writeString(p_cmd);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLegionDestroy(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(643);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1242(myDecoder:WsDecoder):S2CGetTnqwInfo{
				var retObj = new S2CGetTnqwInfo();
	retObj.event = new Array<IOTnqwEvent>();
	let event_size = myDecoder.readInt();
	if(event_size >0){
		for(var i=0; i<event_size;i++){
				retObj.event[i] = MyProtocols.get_IOTnqwEvent(myDecoder);
		}
	}
	retObj.bosslist = new Array<IOTnqwBosslist>();
	let bosslist_size = myDecoder.readInt();
	if(bosslist_size >0){
		for(var i=0; i<bosslist_size;i++){
				retObj.bosslist[i] = MyProtocols.get_IOTnqwBosslist(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SBigBattleEnd(senderSocket:net.LejiSocket,p_mapid:number,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(575);
	wsEncoder.writeInt(p_mapid);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SFriendList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(451);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1254(myDecoder:WsDecoder):S2CDjrwInfo{
				var retObj = new S2CDjrwInfo();
	retObj.list = new Array<IODjrw1>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IODjrw1(myDecoder);
		}
	}
			return retObj;
		}

		static get_IODungeonPotion(myDecoder:WsDecoder):IODungeonPotion{
				var retObj = new IODungeonPotion();
	retObj.id=myDecoder.readInt();
	retObj.count=myDecoder.readInt();
			return retObj;
		}

		static get_1152(myDecoder:WsDecoder):S2CGetKpMissionAward{
				var retObj = new S2CGetKpMissionAward();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_3308(myDecoder:WsDecoder):S2CSecretGetAward{
				var retObj = new S2CSecretGetAward();
	retObj.stage_index=myDecoder.readInt();
	retObj.reward_items = new Array<SecretItemInfo>();
	let reward_items_size = myDecoder.readInt();
	if(reward_items_size >0){
		for(var i=0; i<reward_items_size;i++){
				retObj.reward_items[i] = MyProtocols.get_SecretItemInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SManorEnemyInfo(senderSocket:net.LejiSocket,p_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(533);
	wsEncoder.writeInt(p_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGoldBuyList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(187);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOMjbgFinal1(myDecoder:WsDecoder):IOMjbgFinal1{
				var retObj = new IOMjbgFinal1();
	retObj.gsid=myDecoder.readInt();
	retObj.count=myDecoder.readInt();
	retObj.maxnum=myDecoder.readInt();
			return retObj;
		}

		static send_C2STnqwBossEnd(senderSocket:net.LejiSocket,p_boss_index:number,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1075);
	wsEncoder.writeInt(p_boss_index);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IORewardItemSelect(myDecoder:WsDecoder):IORewardItemSelect{
				var retObj = new IORewardItemSelect();
	retObj.GSID=myDecoder.readInt();
	retObj.COUNT=myDecoder.readInt();
	retObj.real=myDecoder.readBool();
			return retObj;
		}

		static get_156(myDecoder:WsDecoder):S2C4399PayPre{
				var retObj = new S2C4399PayPre();
	retObj.goodsId=myDecoder.readInt();
	retObj.rmb=myDecoder.readInt();
	retObj.cpOrderId=myDecoder.readString();
	retObj.extra=myDecoder.readString();
	retObj.goodsName=myDecoder.readString();
			return retObj;
		}

		static send_C2SChuangFuLogin(senderSocket:net.LejiSocket,p_app_id:string,p_uin:string,p_login_token:string,p_app_key:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10059);
	wsEncoder.writeString(p_app_id);
	wsEncoder.writeString(p_uin);
	wsEncoder.writeString(p_login_token);
	wsEncoder.writeString(p_app_key);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2STestLogin(senderSocket:net.LejiSocket,p_uname:string,p_pwd:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(51);
	wsEncoder.writeString(p_uname);
	wsEncoder.writeString(p_pwd);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOMineHolder(myDecoder:WsDecoder):IOMineHolder{
				var retObj = new IOMineHolder();
	retObj.rid=myDecoder.readInt();
	retObj.rname=myDecoder.readString();
	retObj.level=myDecoder.readInt();
	retObj.iconid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.fight=myDecoder.readInt();
	retObj.cd_time=myDecoder.readInt();
			return retObj;
		}

		static send_C2SLegionBossBattleEnd(senderSocket:net.LejiSocket,p_bossId:number,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1053);
	wsEncoder.writeInt(p_bossId);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_58(myDecoder:WsDecoder):S2CServerList{
				var retObj = new S2CServerList();
	retObj.recommands = new Array<number>();
	let recommands_size = myDecoder.readInt();
	if(recommands_size >0){
		for(var i=0; i<recommands_size;i++){
			retObj.recommands[i]=myDecoder.readInt();
		}
	}
	retObj.has_roles = new Array<IOServerHasRole>();
	let has_roles_size = myDecoder.readInt();
	if(has_roles_size >0){
		for(var i=0; i<has_roles_size;i++){
				retObj.has_roles[i] = MyProtocols.get_IOServerHasRole(myDecoder);
		}
	}
	retObj.alls = new Array<ServerListItem>();
	let alls_size = myDecoder.readInt();
	if(alls_size >0){
		for(var i=0; i<alls_size;i++){
				retObj.alls[i] = MyProtocols.get_ServerListItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_AwardItem(myDecoder:WsDecoder):AwardItem{
				var retObj = new AwardItem();
	retObj.gsid=myDecoder.readInt();
	retObj.count=myDecoder.readInt();
			return retObj;
		}

		static send_C2SLegionSign(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(645);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_662(myDecoder:WsDecoder):S2CLegionFactoryMissionFinish{
				var retObj = new S2CLegionFactoryMissionFinish();
	retObj.items = new Array<IORewardItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SGxCzlbList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1225);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SFriendshipGive(senderSocket:net.LejiSocket,p_role_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(465);
	wsEncoder.writeInt(p_role_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SRankLike(senderSocket:net.LejiSocket,p_targetPlayerId:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(157);
	wsEncoder.writeInt(p_targetPlayerId);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SChangeHeadIcon(senderSocket:net.LejiSocket,p_head_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(171);
	wsEncoder.writeInt(p_head_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGetAnnounce(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(59);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOBattleReport(myDecoder:WsDecoder):IOBattleReport{
				var retObj = new IOBattleReport();
	retObj.left = new Array<IOBattleReportItem>();
	let left_size = myDecoder.readInt();
	if(left_size >0){
		for(var i=0; i<left_size;i++){
				retObj.left[i] = MyProtocols.get_IOBattleReportItem(myDecoder);
		}
	}
	retObj.right = new Array<IOBattleReportItem>();
	let right_size = myDecoder.readInt();
	if(right_size >0){
		for(var i=0; i<right_size;i++){
				retObj.right[i] = MyProtocols.get_IOBattleReportItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SChapterBattleEnd(senderSocket:net.LejiSocket,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(509);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOGuozhanHistory(myDecoder:WsDecoder):IOGuozhanHistory{
				var retObj = new IOGuozhanHistory();
	retObj.action_type=myDecoder.readInt();
	retObj.target_player_name=myDecoder.readString();
	retObj.params = new Array<number>();
	let params_size = myDecoder.readInt();
	if(params_size >0){
		for(var i=0; i<params_size;i++){
			retObj.params[i]=myDecoder.readInt();
		}
	}
	retObj.add_time=myDecoder.readInt();
			return retObj;
		}

		static send_C2SGuozhanOfficeView(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3459);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGetOnlineAward(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(807);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_130(myDecoder:WsDecoder):S2CQunheiPayPre{
				var retObj = new S2CQunheiPayPre();
	retObj.charge_index=myDecoder.readInt();
	retObj.ext=myDecoder.readString();
	retObj.sign=myDecoder.readString();
	retObj.goodsName=myDecoder.readString();
	retObj.rmb=myDecoder.readInt();
			return retObj;
		}

		static get_IORankPlayer(myDecoder:WsDecoder):IORankPlayer{
				var retObj = new IORankPlayer();
	retObj.rid=myDecoder.readInt();
	retObj.rname=myDecoder.readString();
	retObj.iconid=myDecoder.readInt();
	retObj.headid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.power=myDecoder.readInt();
	retObj.vip=myDecoder.readInt();
	retObj.rank_change=myDecoder.readInt();
	retObj.hero_stars=myDecoder.readInt();
	retObj.win_count=myDecoder.readInt();
	retObj.score=myDecoder.readInt();
	retObj.damage=myDecoder.readInt();
	retObj.tower=myDecoder.readInt();
	retObj.like_count=myDecoder.readInt();
	retObj.chapter=myDecoder.readInt();
	retObj.node=myDecoder.readInt();
	retObj.stage=myDecoder.readInt();
	retObj.star=myDecoder.readInt();
			return retObj;
		}

		static get_628(myDecoder:WsDecoder):S2CLegionSearch{
				var retObj = new S2CLegionSearch();
	let legion1_exist = myDecoder.readBool();
	if(legion1_exist == true){
		retObj.legion1 = MyProtocols.get_IOLegionInfo(myDecoder);
	}
			return retObj;
		}

		static send_C2SLegionApplyList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(633);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_394(myDecoder:WsDecoder):S2CGeneralWuhun{
				var retObj = new S2CGeneralWuhun();
			return retObj;
		}

		static get_562(myDecoder:WsDecoder):S2COccTaskJobSelect{
				var retObj = new S2COccTaskJobSelect();
			return retObj;
		}

		static get_186(myDecoder:WsDecoder):S2CTeamsInfoSet{
				var retObj = new S2CTeamsInfoSet();
			return retObj;
		}

		static send_C2SWorldBossFarm(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(671);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IORecruitFree(myDecoder:WsDecoder):IORecruitFree{
				var retObj = new IORecruitFree();
	retObj.normal=myDecoder.readLong();
	retObj.premium=myDecoder.readLong();
			return retObj;
		}

		static send_C2SFriendApplyHandle(senderSocket:net.LejiSocket,p_is_agree:boolean,p_role_ids:Array<number>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(463);
	wsEncoder.writeBool(p_is_agree);
	if(p_role_ids == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_role_ids.length);
		p_role_ids.forEach(function(p_role_ids_v){
	wsEncoder.writeInt(p_role_ids_v);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1078(myDecoder:WsDecoder):S2CTgslBossStart{
				var retObj = new S2CTgslBossStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static send_C2SMineDefFormationSave(senderSocket:net.LejiSocket,p_level_index:number,p_point_index:number,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3111);
	wsEncoder.writeInt(p_level_index);
	wsEncoder.writeInt(p_point_index);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGetVipGift(senderSocket:net.LejiSocket,p_vip_level:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(123);
	wsEncoder.writeInt(p_vip_level);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOPvpRecord(myDecoder:WsDecoder):IOPvpRecord{
				var retObj = new IOPvpRecord();
	retObj.videoid=myDecoder.readLong();
	retObj.time=myDecoder.readLong();
	retObj.version=myDecoder.readLong();
	retObj.seed=myDecoder.readLong();
	retObj.result=myDecoder.readString();
	let season_exist = myDecoder.readBool();
	if(season_exist == true){
		retObj.season = MyProtocols.get_IOBattleRecordSeason(myDecoder);
	}
	retObj.lper = {};
	let lper_size = myDecoder.readInt();
	if(lper_size >0){
		for(var i=0; i<lper_size;i++){
			let lper_key =myDecoder.readInt();
			retObj.lper[lper_key]=myDecoder.readInt();
		}
	}
	retObj.rper = {};
	let rper_size = myDecoder.readInt();
	if(rper_size >0){
		for(var i=0; i<rper_size;i++){
			let rper_key =myDecoder.readInt();
			retObj.rper[rper_key]=myDecoder.readInt();
		}
	}
	retObj.ltper=myDecoder.readInt();
	retObj.rtper=myDecoder.readInt();
	let report_exist = myDecoder.readBool();
	if(report_exist == true){
		retObj.report = MyProtocols.get_IOBattleReport(myDecoder);
	}
	let left_exist = myDecoder.readBool();
	if(left_exist == true){
		retObj.left = MyProtocols.get_IOBattleRecordSide(myDecoder);
	}
	let right_exist = myDecoder.readBool();
	if(right_exist == true){
		retObj.right = MyProtocols.get_IOBattleRecordSide(myDecoder);
	}
	retObj.mark=myDecoder.readString();
			return retObj;
		}

		static send_C2SGetCxryInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1275);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SManorBattleStart(senderSocket:net.LejiSocket,p_index:number,p_friendid:number,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(535);
	wsEncoder.writeInt(p_index);
	wsEncoder.writeInt(p_friendid);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SExclusiveLevelUp(senderSocket:net.LejiSocket,p_general_uuid:number,p_is_lock:boolean){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(369);
	wsEncoder.writeLong(p_general_uuid);
	wsEncoder.writeBool(p_is_lock);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SBigBattleInfo(senderSocket:net.LejiSocket,p_mapid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(571);
	wsEncoder.writeInt(p_mapid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGeneralExchangeGet(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(379);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLiuyeLogin2(senderSocket:net.LejiSocket,p_mem_id:string,p_user_token:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10051);
	wsEncoder.writeString(p_mem_id);
	wsEncoder.writeString(p_user_token);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLiuyeLogin1(senderSocket:net.LejiSocket,p_mem_id:string,p_user_token:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10049);
	wsEncoder.writeString(p_mem_id);
	wsEncoder.writeString(p_user_token);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_556(myDecoder:WsDecoder):S2CGetNoviceTrainAward{
				var retObj = new S2CGetNoviceTrainAward();
	retObj.rewards = new Array<AwardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_AwardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_478(myDecoder:WsDecoder):S2CFriendBossFarm{
				var retObj = new S2CFriendBossFarm();
	let ret_exist = myDecoder.readBool();
	if(ret_exist == true){
		retObj.ret = MyProtocols.get_472(myDecoder);
	}
			return retObj;
		}

		static send_C2SDrawList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(191);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1232(myDecoder:WsDecoder):S2CGzCzlbList{
				var retObj = new S2CGzCzlbList();
	retObj.list = new Array<IOCzlb>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOCzlb(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SLegionFactoryMissionStart(senderSocket:net.LejiSocket,p_key:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(659);
	wsEncoder.writeLong(p_key);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGuozhanFightView(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3463);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOSecretBoxAward(myDecoder:WsDecoder):IOSecretBoxAward{
				var retObj = new IOSecretBoxAward();
	retObj.stage_index=myDecoder.readInt();
	retObj.award_list = new Array<SecretItemInfo>();
	let award_list_size = myDecoder.readInt();
	if(award_list_size >0){
		for(var i=0; i<award_list_size;i++){
				retObj.award_list[i] = MyProtocols.get_SecretItemInfo(myDecoder);
		}
	}
	retObj.is_get=myDecoder.readInt();
			return retObj;
		}

		static send_C2SSrenderList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(873);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1262(myDecoder:WsDecoder):S2CMjbgInfo{
				var retObj = new S2CMjbgInfo();
	retObj.djgsid=myDecoder.readInt();
	retObj.djcount=myDecoder.readInt();
	retObj.index=myDecoder.readInt();
	retObj.items = new Array<IOMjbgItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOMjbgItem(myDecoder);
		}
	}
	retObj.strbox=myDecoder.readString();
	retObj.finallist = new Array<IOMjbgFinal>();
	let finallist_size = myDecoder.readInt();
	if(finallist_size >0){
		for(var i=0; i<finallist_size;i++){
				retObj.finallist[i] = MyProtocols.get_IOMjbgFinal(myDecoder);
		}
	}
	retObj.list = new Array<IOMjbgSource>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOMjbgSource(myDecoder);
		}
	}
	let _final_exist = myDecoder.readBool();
	if(_final_exist == true){
		retObj._final = MyProtocols.get_IOMjbgItem(myDecoder);
	}
			return retObj;
		}

		static send_C2STreasureTakeon(senderSocket:net.LejiSocket,p_guid:number,p_treasure_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(385);
	wsEncoder.writeLong(p_guid);
	wsEncoder.writeInt(p_treasure_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_148(myDecoder:WsDecoder):S2CZMPayCheck{
				var retObj = new S2CZMPayCheck();
	retObj.fee_id=myDecoder.readString();
	retObj.check=myDecoder.readString();
	retObj.extradata=myDecoder.readString();
	retObj.goodsName=myDecoder.readString();
	retObj.rmb=myDecoder.readInt();
			return retObj;
		}

		static send_C2SChongChongLogin(senderSocket:net.LejiSocket,p_userId:string,p_time:string,p_sign:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10039);
	wsEncoder.writeString(p_userId);
	wsEncoder.writeString(p_time);
	wsEncoder.writeString(p_sign);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOSrenderState(myDecoder:WsDecoder):IOSrenderState{
				var retObj = new IOSrenderState();
	retObj.gsid=myDecoder.readInt();
	retObj.loyal=myDecoder.readInt();
	retObj.state=myDecoder.readInt();
			return retObj;
		}

		static send_C2SMineGetAward(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3107);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGuestLogin(senderSocket:net.LejiSocket,p_uid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(53);
	wsEncoder.writeInt(p_uid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_644(myDecoder:WsDecoder):S2CLegionDestroy{
				var retObj = new S2CLegionDestroy();
			return retObj;
		}

		static send_C2SFriendReceiveExplore(senderSocket:net.LejiSocket,p_chapter_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(475);
	wsEncoder.writeInt(p_chapter_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SShopList(senderSocket:net.LejiSocket,p_shop_type:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(321);
	wsEncoder.writeString(p_shop_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_3454(myDecoder:WsDecoder):S2CGuozhanBattleView{
				var retObj = new S2CGuozhanBattleView();
	retObj.city_index=myDecoder.readInt();
	retObj.enemy_level=myDecoder.readInt();
	let base_info_exist = myDecoder.readBool();
	if(base_info_exist == true){
		retObj.base_info = MyProtocols.get_IOGuozhanPointPlayer(myDecoder);
	}
	let battleset_exist = myDecoder.readBool();
	if(battleset_exist == true){
		retObj.battleset = MyProtocols.get_IOBattlesetEnemy(myDecoder);
	}
			return retObj;
		}

		static send_C2STnqwBossStart(senderSocket:net.LejiSocket,p_boss_index:number,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1073);
	wsEncoder.writeInt(p_boss_index);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SPingPingLogin(senderSocket:net.LejiSocket,p_game_id:string,p_user_code:string,p_login_token:string,p_game_key:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10053);
	wsEncoder.writeString(p_game_id);
	wsEncoder.writeString(p_user_code);
	wsEncoder.writeString(p_login_token);
	wsEncoder.writeString(p_game_key);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SEnterGame(senderSocket:net.LejiSocket,p_session_id:number,p_wanba_gift:WanbaLoginGift){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(107);
	wsEncoder.writeLong(p_session_id);
	if(p_wanba_gift == null){
		wsEncoder.writeBool(false);
	}else { 
		wsEncoder.writeBool(true);
			wsEncoder.writeInt(p_wanba_gift.wanba_gift_id);
			wsEncoder.writeString(p_wanba_gift.appid);
			wsEncoder.writeString(p_wanba_gift.openid);
			wsEncoder.writeString(p_wanba_gift.openkey);
			wsEncoder.writeString(p_wanba_gift.pf);
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_564(myDecoder:WsDecoder):S2COccTaskGiftOne{
				var retObj = new S2COccTaskGiftOne();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOLegionBoss(myDecoder:WsDecoder):IOLegionBoss{
				var retObj = new IOLegionBoss();
	retObj.chapter=myDecoder.readInt();
	retObj.name=myDecoder.readString();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.bset = {};
	let bset_size = myDecoder.readInt();
	if(bset_size >0){
		for(var i=0; i<bset_size;i++){
			let bset_key =myDecoder.readInt();
				retObj.bset[bset_key] = MyProtocols.get_IOGeneralLegion(myDecoder);
		}
	}
	retObj.maxhp=myDecoder.readLong();
	retObj.nowhp=myDecoder.readLong();
			return retObj;
		}

		static send_C2SExpedBattleInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(525);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOManorFieldEnemy(myDecoder:WsDecoder):IOManorFieldEnemy{
				var retObj = new IOManorFieldEnemy();
	retObj.boxItem = new Array<IORewardItem>();
	let boxItem_size = myDecoder.readInt();
	if(boxItem_size >0){
		for(var i=0; i<boxItem_size;i++){
				retObj.boxItem[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.id=myDecoder.readInt();
	retObj.state=myDecoder.readInt();
	retObj.hasOpen=myDecoder.readInt();
	retObj.cachehp = new Array<number>();
	let cachehp_size = myDecoder.readInt();
	if(cachehp_size >0){
		for(var i=0; i<cachehp_size;i++){
			retObj.cachehp[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static get_518(myDecoder:WsDecoder):S2CTowerBattleInfo{
				var retObj = new S2CTowerBattleInfo();
	retObj.items = new Array<IOGeneralSimple>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOGeneralSimple(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOSpecial(myDecoder:WsDecoder):IOSpecial{
				var retObj = new IOSpecial();
	retObj.bw=myDecoder.readLong();
	retObj.qz=myDecoder.readLong();
	retObj.zx=myDecoder.readLong();
	retObj.lm=myDecoder.readLong();
	retObj.yd=myDecoder.readLong();
	retObj.cz=myDecoder.readLong();
	retObj.gz=myDecoder.readLong();
	retObj.zz=myDecoder.readLong();
			return retObj;
		}

		static send_C2SOccTaskFree(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(559);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SPlayerByParam(senderSocket:net.LejiSocket,p_arr:Array<string>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1015);
	if(p_arr == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_arr.length);
		p_arr.forEach(function(p_arr_v){
	wsEncoder.writeString(p_arr_v);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_364(myDecoder:WsDecoder):PushGeneralAddLevel{
				var retObj = new PushGeneralAddLevel();
	retObj.general_uuid=myDecoder.readLong();
	retObj.level=myDecoder.readInt();
	retObj.new_power=myDecoder.readInt();
			return retObj;
		}

		static send_C2SZmjfInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1247);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOMjbgItem(myDecoder:WsDecoder):IOMjbgItem{
				var retObj = new IOMjbgItem();
	retObj.gsid=myDecoder.readInt();
	retObj.count=myDecoder.readInt();
	retObj.num=myDecoder.readInt();
			return retObj;
		}

		static get_62(myDecoder:WsDecoder):S2CSelectServer{
				var retObj = new S2CSelectServer();
	retObj.ret=myDecoder.readInt();
	retObj.server_id=myDecoder.readInt();
	retObj.has_role=myDecoder.readBool();
			return retObj;
		}

		static get_878(myDecoder:WsDecoder):S2CSrenderBehead{
				var retObj = new S2CSrenderBehead();
	retObj.awards = new Array<IORewardItem>();
	let awards_size = myDecoder.readInt();
	if(awards_size >0){
		for(var i=0; i<awards_size;i++){
				retObj.awards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SActivityList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1201);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_580(myDecoder:WsDecoder):S2CMonthBossInfo{
				var retObj = new S2CMonthBossInfo();
	retObj.index=myDecoder.readInt();
	retObj.general=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.bset = new Array<IOGeneralSimple>();
	let bset_size = myDecoder.readInt();
	if(bset_size >0){
		for(var i=0; i<bset_size;i++){
				retObj.bset[i] = MyProtocols.get_IOGeneralSimple(myDecoder);
		}
	}
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.lastdamge=myDecoder.readLong();
	retObj.maxhp=myDecoder.readLong();
	retObj.nowhp=myDecoder.readLong();
			return retObj;
		}

		static get_1252(myDecoder:WsDecoder):S2CGetGjdl{
				var retObj = new S2CGetGjdl();
	retObj.list = new Array<IOCjxg1>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOCjxg1(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SQedjList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(181);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_360(myDecoder:WsDecoder):S2CGeneralAddClass{
				var retObj = new S2CGeneralAddClass();
	retObj.general_uuid=myDecoder.readLong();
	let general_bean_exist = myDecoder.readBool();
	if(general_bean_exist == true){
		retObj.general_bean = MyProtocols.get_IOGeneralBean(myDecoder);
	}
			return retObj;
		}

		static send_C2SGeneralWuhun(senderSocket:net.LejiSocket,p_general_uuid:number,p_pos_index:number,p_wuhun_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(393);
	wsEncoder.writeLong(p_general_uuid);
	wsEncoder.writeInt(p_pos_index);
	wsEncoder.writeInt(p_wuhun_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGetMailAttach(senderSocket:net.LejiSocket,p_mail_id:Array<number>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(703);
	if(p_mail_id == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_mail_id.length);
		p_mail_id.forEach(function(p_mail_id_v){
	wsEncoder.writeInt(p_mail_id_v);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_104(myDecoder:WsDecoder):S2CQueryHasRole{
				var retObj = new S2CQueryHasRole();
	retObj.has_role=myDecoder.readBool();
	retObj.qq_nickname=myDecoder.readString();
			return retObj;
		}

		static send_C2SPvpBattleEnd(senderSocket:net.LejiSocket,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(515);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_702(myDecoder:WsDecoder):S2CMailList{
				var retObj = new S2CMailList();
	retObj.list = new Array<IOMailInfo>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOMailInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_112(myDecoder:WsDecoder):S2CPlayerKickOff{
				var retObj = new S2CPlayerKickOff();
			return retObj;
		}

		static get_IOWorldBossLegion(myDecoder:WsDecoder):IOWorldBossLegion{
				var retObj = new IOWorldBossLegion();
	retObj.rank=myDecoder.readInt();
	retObj.maxrank=myDecoder.readInt();
	retObj.damage=myDecoder.readLong();
			return retObj;
		}

		static send_C2SGuoZhanCityDetail(senderSocket:net.LejiSocket,p_city_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3465);
	wsEncoder.writeInt(p_city_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SSpinRoll(senderSocket:net.LejiSocket,p_type:number,p_times:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(403);
	wsEncoder.writeInt(p_type);
	wsEncoder.writeInt(p_times);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1116(myDecoder:WsDecoder):S2CDungeonChooseBuf{
				var retObj = new S2CDungeonChooseBuf();
	retObj.buffid=myDecoder.readInt();
			return retObj;
		}

		static send_C2SSecretReset(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3311);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SSpinBoxReset(senderSocket:net.LejiSocket,p_type:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(409);
	wsEncoder.writeInt(p_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SMjbgReward(senderSocket:net.LejiSocket,p_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1265);
	wsEncoder.writeInt(p_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_708(myDecoder:WsDecoder):S2CDelMail{
				var retObj = new S2CDelMail();
			return retObj;
		}

		static get_3102(myDecoder:WsDecoder):S2CMineList{
				var retObj = new S2CMineList();
	retObj.level_index=myDecoder.readInt();
	retObj.page_index=myDecoder.readInt();
	retObj.my_income = new Array<number>();
	let my_income_size = myDecoder.readInt();
	if(my_income_size >0){
		for(var i=0; i<my_income_size;i++){
			retObj.my_income[i]=myDecoder.readInt();
		}
	}
	retObj.my_hold = new Array<number>();
	let my_hold_size = myDecoder.readInt();
	if(my_hold_size >0){
		for(var i=0; i<my_hold_size;i++){
			retObj.my_hold[i]=myDecoder.readInt();
		}
	}
	retObj.my_cd_time = new Array<number>();
	let my_cd_time_size = myDecoder.readInt();
	if(my_cd_time_size >0){
		for(var i=0; i<my_cd_time_size;i++){
			retObj.my_cd_time[i]=myDecoder.readInt();
		}
	}
	retObj.mine_points = new Array<IOMintPoint>();
	let mine_points_size = myDecoder.readInt();
	if(mine_points_size >0){
		for(var i=0; i<mine_points_size;i++){
				retObj.mine_points[i] = MyProtocols.get_IOMintPoint(myDecoder);
		}
	}
			return retObj;
		}

		static get_1264(myDecoder:WsDecoder):S2CMjbgChange{
				var retObj = new S2CMjbgChange();
	retObj.gsid=myDecoder.readInt();
	retObj.count=myDecoder.readInt();
			return retObj;
		}

		static get_IOBattleRecordSeason(myDecoder:WsDecoder):IOBattleRecordSeason{
				var retObj = new IOBattleRecordSeason();
	retObj.season=myDecoder.readInt();
	retObj.left=myDecoder.readInt();
	retObj.right=myDecoder.readInt();
	retObj.pos = new Array<number>();
	let pos_size = myDecoder.readInt();
	if(pos_size >0){
		for(var i=0; i<pos_size;i++){
			retObj.pos[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static get_550(myDecoder:WsDecoder):S2CTowerReplay{
				var retObj = new S2CTowerReplay();
	retObj.records = new Array<IOPvpRecord>();
	let records_size = myDecoder.readInt();
	if(records_size >0){
		for(var i=0; i<records_size;i++){
				retObj.records[i] = MyProtocols.get_IOPvpRecord(myDecoder);
		}
	}
			return retObj;
		}

		static get_332(myDecoder:WsDecoder):S2CAffairList{
				var retObj = new S2CAffairList();
	retObj.item_list = new Array<IOAffairItem>();
	let item_list_size = myDecoder.readInt();
	if(item_list_size >0){
		for(var i=0; i<item_list_size;i++){
				retObj.item_list[i] = MyProtocols.get_IOAffairItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SChapterReap(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(503);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SChangeName(senderSocket:net.LejiSocket,p_name:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(167);
	wsEncoder.writeString(p_name);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SSrenderBehead(senderSocket:net.LejiSocket,p_gsid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(877);
	wsEncoder.writeInt(p_gsid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SQiZhenYiBaoJoin(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1209);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOTeamInfo(myDecoder:WsDecoder):IOTeamInfo{
				var retObj = new IOTeamInfo();
	retObj.type=myDecoder.readInt();
	retObj.pos_card_uuid = new Array<number>();
	let pos_card_uuid_size = myDecoder.readInt();
	if(pos_card_uuid_size >0){
		for(var i=0; i<pos_card_uuid_size;i++){
			retObj.pos_card_uuid[i]=myDecoder.readLong();
		}
	}
	retObj.pet_id=myDecoder.readInt();
			return retObj;
		}

		static send_C2SWorldBossBattleEnd(senderSocket:net.LejiSocket,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1071);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_880(myDecoder:WsDecoder):S2CManorFriendList{
				var retObj = new S2CManorFriendList();
	retObj.list = new Array<IOManorFriend>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOManorFriend(myDecoder);
		}
	}
			return retObj;
		}

		static get_1218(myDecoder:WsDecoder):S2CMzlbList{
				var retObj = new S2CMzlbList();
	retObj.end=myDecoder.readLong();
	retObj.items = new Array<IOLiBao1>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOLiBao1(myDecoder);
		}
	}
			return retObj;
		}

		static get_1230(myDecoder:WsDecoder):S2CCzlbList{
				var retObj = new S2CCzlbList();
	retObj.list = new Array<IOCzlb>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOCzlb(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SGuozhanOfficeDetail(senderSocket:net.LejiSocket,p_office_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3461);
	wsEncoder.writeInt(p_office_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_150(myDecoder:WsDecoder):S2COfflineAwardDouble{
				var retObj = new S2COfflineAwardDouble();
	retObj.add_level=myDecoder.readInt();
			return retObj;
		}

		static get_672(myDecoder:WsDecoder):S2CWorldBossFarm{
				var retObj = new S2CWorldBossFarm();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOLegionFactoryMission(myDecoder:WsDecoder):IOLegionFactoryMission{
				var retObj = new IOLegionFactoryMission();
	retObj.key=myDecoder.readLong();
	retObj.id=myDecoder.readInt();
	retObj.stime=myDecoder.readLong();
	retObj.etime=myDecoder.readLong();
			return retObj;
		}

		static get_524(myDecoder:WsDecoder):S2CTowerAchieveReward{
				var retObj = new S2CTowerAchieveReward();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SLegionInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(629);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_346(myDecoder:WsDecoder):S2CAffairRedNotice{
				var retObj = new S2CAffairRedNotice();
	retObj.ret=myDecoder.readBool();
			return retObj;
		}

		static get_1084(myDecoder:WsDecoder):S2CMineBattleStart{
				var retObj = new S2CMineBattleStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static send_C2SGetOtherPlayerInfo(senderSocket:net.LejiSocket,p_ids:Array<number>,p_pvpdef:boolean,p_battleset:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(199);
	if(p_ids == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_ids.length);
		p_ids.forEach(function(p_ids_v){
	wsEncoder.writeInt(p_ids_v);
		});
	}
	wsEncoder.writeBool(p_pvpdef);
	wsEncoder.writeString(p_battleset);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGuozhanBattleStart(senderSocket:net.LejiSocket,p_mythic:number,p_items:Array<IOFormationGeneralPos>,p_city_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1087);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
	wsEncoder.writeInt(p_city_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1276(myDecoder:WsDecoder):S2CGetCxryInfo{
				var retObj = new S2CGetCxryInfo();
	let zf_exist = myDecoder.readBool();
	if(zf_exist == true){
		retObj.zf = MyProtocols.get_IOCxryZf(myDecoder);
	}
	retObj.savegenerals = new Array<IOCxryGenerals>();
	let savegenerals_size = myDecoder.readInt();
	if(savegenerals_size >0){
		for(var i=0; i<savegenerals_size;i++){
				retObj.savegenerals[i] = MyProtocols.get_IOCxryGenerals(myDecoder);
		}
	}
	retObj.gnummax=myDecoder.readInt();
			return retObj;
		}

		static get_162(myDecoder:WsDecoder):S2CWanBaGetBalance{
				var retObj = new S2CWanBaGetBalance();
	retObj.is_balance_enough=myDecoder.readBool();
	retObj.defaultScore=myDecoder.readInt();
	retObj.itemid=myDecoder.readInt();
	retObj.cpOrderId=myDecoder.readString();
	retObj.goodsName=myDecoder.readString();
			return retObj;
		}

		static get_650(myDecoder:WsDecoder):S2CLegionTechReset{
				var retObj = new S2CLegionTechReset();
	retObj.items = new Array<IORewardItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_558(myDecoder:WsDecoder):S2COccTaskInfo{
				var retObj = new S2COccTaskInfo();
	retObj.index=myDecoder.readInt();
	retObj.occtype=myDecoder.readInt();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.list = new Array<IOOccTask1>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOOccTask1(myDecoder);
		}
	}
	retObj.reward = new Array<RewardInfo>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	let refcost_exist = myDecoder.readBool();
	if(refcost_exist == true){
		retObj.refcost = MyProtocols.get_RewardInfo(myDecoder);
	}
	let packinfo_exist = myDecoder.readBool();
	if(packinfo_exist == true){
		retObj.packinfo = MyProtocols.get_IOOcctaskPackinfo(myDecoder);
	}
	retObj.prewards = new Array<number>();
	let prewards_size = myDecoder.readInt();
	if(prewards_size >0){
		for(var i=0; i<prewards_size;i++){
			retObj.prewards[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static get_810(myDecoder:WsDecoder):S2CKeyConvert{
				var retObj = new S2CKeyConvert();
	retObj.result_id=myDecoder.readInt();
	retObj.reward_item = new Array<AwardItem>();
	let reward_item_size = myDecoder.readInt();
	if(reward_item_size >0){
		for(var i=0; i<reward_item_size;i++){
				retObj.reward_item[i] = MyProtocols.get_AwardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SOccTaskPackRPay(senderSocket:net.LejiSocket,p_task_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(569);
	wsEncoder.writeInt(p_task_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SXsdhList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1249);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SChapterBattleStart(senderSocket:net.LejiSocket,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(507);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SExclusiveRefreshConfirm(senderSocket:net.LejiSocket,p_general_uuid:number,p_is_confirm:boolean){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(375);
	wsEncoder.writeLong(p_general_uuid);
	wsEncoder.writeBool(p_is_confirm);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGuideStep(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1003);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_544(myDecoder:WsDecoder):S2CMapEventFinish{
				var retObj = new S2CMapEventFinish();
	retObj.ret=myDecoder.readInt();
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SChatView(senderSocket:net.LejiSocket,p_chat_type:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(605);
	wsEncoder.writeInt(p_chat_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_304(myDecoder:WsDecoder):S2CUseItem{
				var retObj = new S2CUseItem();
	retObj.status=myDecoder.readInt();
	retObj.reward = new Array<RewardInfo>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_IODjrwChk(myDecoder:WsDecoder):IODjrwChk{
				var retObj = new IODjrwChk();
	retObj.MARK=myDecoder.readInt();
	retObj.NUM=myDecoder.readInt();
			return retObj;
		}

		static get_60(myDecoder:WsDecoder):S2CGetAnnounce{
				var retObj = new S2CGetAnnounce();
	retObj.content=myDecoder.readString();
			return retObj;
		}

		static send_C2SGetJfbx(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1255);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SBattleFormationSave(senderSocket:net.LejiSocket,p_formation_name:string,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(511);
	wsEncoder.writeString(p_formation_name);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_110(myDecoder:WsDecoder):S2CUserInfoStruct{
				var retObj = new S2CUserInfoStruct();
	retObj.id=myDecoder.readInt();
	retObj.uid=myDecoder.readString();
	retObj.servid=myDecoder.readInt();
	retObj.rname=myDecoder.readString();
	retObj.sex=myDecoder.readInt();
	retObj.iconid=myDecoder.readInt();
	retObj.headid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.imageid=myDecoder.readInt();
	retObj.gold=myDecoder.readInt();
	retObj.yb=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.exp=myDecoder.readInt();
	retObj.vip=myDecoder.readInt();
	retObj.vipexp=myDecoder.readInt();
	retObj.power=myDecoder.readInt();
	retObj.firstcharge=myDecoder.readLong();
	retObj.create=myDecoder.readLong();
	retObj.time=myDecoder.readLong();
	retObj.guideProgress=myDecoder.readInt();
	retObj.maxmapid=myDecoder.readInt();
	retObj.nowmapid=myDecoder.readInt();
	retObj.tower=myDecoder.readInt();
	retObj.bagspace=myDecoder.readInt();
	retObj.online=myDecoder.readLong();
	retObj.hides = new Array<SimpleItemInfo>();
	let hides_size = myDecoder.readInt();
	if(hides_size >0){
		for(var i=0; i<hides_size;i++){
				retObj.hides[i] = MyProtocols.get_SimpleItemInfo(myDecoder);
		}
	}
	retObj.others = new Array<SimpleItemInfo>();
	let others_size = myDecoder.readInt();
	if(others_size >0){
		for(var i=0; i<others_size;i++){
				retObj.others[i] = MyProtocols.get_SimpleItemInfo(myDecoder);
		}
	}
	let recruitfree_exist = myDecoder.readBool();
	if(recruitfree_exist == true){
		retObj.recruitfree = MyProtocols.get_IORecruitFree(myDecoder);
	}
	retObj.lastgain=myDecoder.readLong();
	retObj.battlearr = new Array<IOBattleFormation>();
	let battlearr_size = myDecoder.readInt();
	if(battlearr_size >0){
		for(var i=0; i<battlearr_size;i++){
				retObj.battlearr[i] = MyProtocols.get_IOBattleFormation(myDecoder);
		}
	}
	retObj.pvpscore=myDecoder.readInt();
	retObj.legion=myDecoder.readLong();
	retObj.gnum=myDecoder.readInt();
	retObj.fbossphys=myDecoder.readInt();
	retObj.tech = {};
	let tech_size = myDecoder.readInt();
	if(tech_size >0){
		for(var i=0; i<tech_size;i++){
			let tech_key =myDecoder.readInt();
			retObj.tech[tech_key]=myDecoder.readInt();
		}
	}
	let dgtop_exist = myDecoder.readBool();
	if(dgtop_exist == true){
		retObj.dgtop = MyProtocols.get_IODungeonTop(myDecoder);
	}
	retObj.occtaskend=myDecoder.readLong();
	let occtask_exist = myDecoder.readBool();
	if(occtask_exist == true){
		retObj.occtask = MyProtocols.get_IOOcctask(myDecoder);
	}
	let special_exist = myDecoder.readBool();
	if(special_exist == true){
		retObj.special = MyProtocols.get_IOSpecial(myDecoder);
	}
	retObj.guozhan_pvp=myDecoder.readBool();
	retObj.ydend=myDecoder.readLong();
			return retObj;
		}

		static send_C2STowerBattleStart(senderSocket:net.LejiSocket,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(519);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SBuyRight(senderSocket:net.LejiSocket,p_pid:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1019);
	wsEncoder.writeString(p_pid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SMonthBossBattleStart(senderSocket:net.LejiSocket,p_index:number,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(581);
	wsEncoder.writeInt(p_index);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1100(myDecoder:WsDecoder):S2CKpBattleEnd{
				var retObj = new S2CKpBattleEnd();
	let stageinfo_exist = myDecoder.readBool();
	if(stageinfo_exist == true){
		retObj.stageinfo = MyProtocols.get_IOStageInfo(myDecoder);
	}
	retObj.resultlist = new Array<IOBattleResult>();
	let resultlist_size = myDecoder.readInt();
	if(resultlist_size >0){
		for(var i=0; i<resultlist_size;i++){
				retObj.resultlist[i] = MyProtocols.get_IOBattleResult(myDecoder);
		}
	}
	retObj.reward = new Array<IORewardItemSelect>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItemSelect(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2STgslInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1271);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGetQunheiWxShareAward(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(137);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOExpedPlayer(myDecoder:WsDecoder):IOExpedPlayer{
				var retObj = new IOExpedPlayer();
	retObj.rname=myDecoder.readString();
	retObj.level=myDecoder.readInt();
	retObj.iconid=myDecoder.readInt();
	retObj.headid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.power=myDecoder.readInt();
	let battleset_exist = myDecoder.readBool();
	if(battleset_exist == true){
		retObj.battleset = MyProtocols.get_IOBattlesetEnemy(myDecoder);
	}
			return retObj;
		}

		static send_C2SMythicalList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(751);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLegionCreate(senderSocket:net.LejiSocket,p_name:string,p_notice:string,p_minlv:number,p_ispass:boolean){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(621);
	wsEncoder.writeString(p_name);
	wsEncoder.writeString(p_notice);
	wsEncoder.writeInt(p_minlv);
	wsEncoder.writeBool(p_ispass);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_KvStringPair(myDecoder:WsDecoder):KvStringPair{
				var retObj = new KvStringPair();
	retObj.key=myDecoder.readString();
	retObj.val=myDecoder.readInt();
			return retObj;
		}

		static get_IOpstatus(myDecoder:WsDecoder):IOpstatus{
				var retObj = new IOpstatus();
	retObj.send=myDecoder.readInt();
	retObj.receive=myDecoder.readInt();
			return retObj;
		}

		static get_1016(myDecoder:WsDecoder):S2CPlayerByParam{
				var retObj = new S2CPlayerByParam();
	retObj.legion=myDecoder.readLong();
	retObj.gnum=myDecoder.readInt();
	retObj.bagspace=myDecoder.readInt();
	retObj.tech = {};
	let tech_size = myDecoder.readInt();
	if(tech_size >0){
		for(var i=0; i<tech_size;i++){
			let tech_key =myDecoder.readInt();
			retObj.tech[tech_key]=myDecoder.readInt();
		}
	}
	let dgtop_exist = myDecoder.readBool();
	if(dgtop_exist == true){
		retObj.dgtop = MyProtocols.get_IODungeonTop(myDecoder);
	}
	retObj.frameid=myDecoder.readInt();
			return retObj;
		}

		static get_540(myDecoder:WsDecoder):S2CManorYijianFarm{
				var retObj = new S2CManorYijianFarm();
			return retObj;
		}

		static get_532(myDecoder:WsDecoder):S2CExpedStatue{
				var retObj = new S2CExpedStatue();
	retObj.hp = {};
	let hp_size = myDecoder.readInt();
	if(hp_size >0){
		for(var i=0; i<hp_size;i++){
			let hp_key =myDecoder.readLong();
			retObj.hp[hp_key]=myDecoder.readInt();
		}
	}
	retObj.wish = new Array<number>();
	let wish_size = myDecoder.readInt();
	if(wish_size >0){
		for(var i=0; i<wish_size;i++){
			retObj.wish[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static get_366(myDecoder:WsDecoder):S2CGeneralDecomp{
				var retObj = new S2CGeneralDecomp();
	retObj.general_uuid = new Array<number>();
	let general_uuid_size = myDecoder.readInt();
	if(general_uuid_size >0){
		for(var i=0; i<general_uuid_size;i++){
			retObj.general_uuid[i]=myDecoder.readLong();
		}
	}
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_342(myDecoder:WsDecoder):S2CAffairAcce{
				var retObj = new S2CAffairAcce();
	retObj.affair_index=myDecoder.readInt();
	retObj.item_list = new Array<IOAffairItem>();
	let item_list_size = myDecoder.readInt();
	if(item_list_size >0){
		for(var i=0; i<item_list_size;i++){
				retObj.item_list[i] = MyProtocols.get_IOAffairItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_704(myDecoder:WsDecoder):S2CGetMailAttach{
				var retObj = new S2CGetMailAttach();
	retObj.rewards = new Array<IOMailAttach>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IOMailAttach(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOTnqwBosslist(myDecoder:WsDecoder):IOTnqwBosslist{
				var retObj = new IOTnqwBosslist();
	retObj.status=myDecoder.readInt();
	retObj.actscore=myDecoder.readInt();
	retObj.rewardgsids = new Array<string>();
	let rewardgsids_size = myDecoder.readInt();
	if(rewardgsids_size >0){
		for(var i=0; i<rewardgsids_size;i++){
			retObj.rewardgsids[i]=myDecoder.readString();
		}
	}
			return retObj;
		}

		static get_654(myDecoder:WsDecoder):S2CLegionFactoryDonateGet{
				var retObj = new S2CLegionFactoryDonateGet();
	retObj.items = new Array<IOLegionFactoryDonation>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOLegionFactoryDonation(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SLegionLog(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(663);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGzCzlbList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1231);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1010(myDecoder:WsDecoder):S2CGuideChooseReward{
				var retObj = new S2CGuideChooseReward();
	retObj.rewards = new Array<SimpleItemInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_SimpleItemInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SKpBattleEnd(senderSocket:net.LejiSocket,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1099);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IODungeonNode(myDecoder:WsDecoder):IODungeonNode{
				var retObj = new IODungeonNode();
	retObj.poslist = new Array<IODungeonNodePos>();
	let poslist_size = myDecoder.readInt();
	if(poslist_size >0){
		for(var i=0; i<poslist_size;i++){
				retObj.poslist[i] = MyProtocols.get_IODungeonNodePos(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SQunheiPayPre(senderSocket:net.LejiSocket,p_charge_index:number,p_goodsName:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(129);
	wsEncoder.writeInt(p_charge_index);
	wsEncoder.writeString(p_goodsName);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SManorInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(857);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SDungeonChooseShop(senderSocket:net.LejiSocket,p_chapter:number,p_node:number,p_pos:number,p_buy:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1117);
	wsEncoder.writeInt(p_chapter);
	wsEncoder.writeInt(p_node);
	wsEncoder.writeInt(p_pos);
	wsEncoder.writeInt(p_buy);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SKpSwitchOrder(senderSocket:net.LejiSocket,p_teamorder:Array<number>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1155);
	if(p_teamorder == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_teamorder.length);
		p_teamorder.forEach(function(p_teamorder_v){
	wsEncoder.writeInt(p_teamorder_v);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_504(myDecoder:WsDecoder):S2CChapterReap{
				var retObj = new S2CChapterReap();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_3106(myDecoder:WsDecoder):PushMineRob{
				var retObj = new PushMineRob();
	retObj.level_index=myDecoder.readInt();
	retObj.point_index=myDecoder.readInt();
	retObj.target_player_id=myDecoder.readInt();
	retObj.target_player_name=myDecoder.readString();
	retObj.loose_items = new Array<IORewardItem>();
	let loose_items_size = myDecoder.readInt();
	if(loose_items_size >0){
		for(var i=0; i<loose_items_size;i++){
				retObj.loose_items[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SSpinBuy(senderSocket:net.LejiSocket,p_count:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(407);
	wsEncoder.writeInt(p_count);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1234(myDecoder:WsDecoder):S2CDjjfInfo{
				var retObj = new S2CDjjfInfo();
	retObj.looplimit=myDecoder.readInt();
	retObj.currentloop=myDecoder.readInt();
	retObj.missions = new Array<IODjjfMission>();
	let missions_size = myDecoder.readInt();
	if(missions_size >0){
		for(var i=0; i<missions_size;i++){
				retObj.missions[i] = MyProtocols.get_IODjjfMission(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SFriendSearch(senderSocket:net.LejiSocket,p_rname:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(455);
	wsEncoder.writeString(p_rname);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IODungeonPosition(myDecoder:WsDecoder):IODungeonPosition{
				var retObj = new IODungeonPosition();
	retObj.node=myDecoder.readInt();
	retObj.pos=myDecoder.readInt();
			return retObj;
		}

		static send_C2SItemList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(301);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOBattleReportItem(myDecoder:WsDecoder):IOBattleReportItem{
				var retObj = new IOBattleReportItem();
	retObj.gsid=myDecoder.readInt();
	retObj.hurm=myDecoder.readLong();
	retObj.heal=myDecoder.readLong();
	retObj.level=myDecoder.readInt();
	retObj.class=myDecoder.readInt();
			return retObj;
		}

		static send_C2SOfficialPromo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(855);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SSecretBattleEnd(senderSocket:net.LejiSocket,p_is_win:boolean,p_my_cost:Array<IOSecretHero>,p_enemy_cost:Array<IOSecretHero>,p_is_interrupt:boolean){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3305);
	wsEncoder.writeBool(p_is_win);
	if(p_my_cost == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_my_cost.length);
		p_my_cost.forEach(function(p_my_cost_v){
			wsEncoder.writeInt(p_my_cost_v.hero_type);
			wsEncoder.writeInt(p_my_cost_v.hero_id);
			wsEncoder.writeInt(p_my_cost_v.hp_percent);
		});
	}
	if(p_enemy_cost == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_enemy_cost.length);
		p_enemy_cost.forEach(function(p_enemy_cost_v){
			wsEncoder.writeInt(p_enemy_cost_v.hero_type);
			wsEncoder.writeInt(p_enemy_cost_v.hero_id);
			wsEncoder.writeInt(p_enemy_cost_v.hp_percent);
		});
	}
	wsEncoder.writeBool(p_is_interrupt);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SFriendApplyList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(461);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SUseItem(senderSocket:net.LejiSocket,p_gsid:number,p_count:number,p_param:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(303);
	wsEncoder.writeInt(p_gsid);
	wsEncoder.writeInt(p_count);
	wsEncoder.writeInt(p_param);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SSpinBoxOpen(senderSocket:net.LejiSocket,p_type:number,p_box_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(405);
	wsEncoder.writeInt(p_type);
	wsEncoder.writeInt(p_box_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_3468(myDecoder:WsDecoder):S2CGuoZhanCityMove{
				var retObj = new S2CGuoZhanCityMove();
	retObj.city_index=myDecoder.readInt();
	retObj.move_step=myDecoder.readInt();
	retObj.occupy_enemy=myDecoder.readBool();
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2S4399PayPre(senderSocket:net.LejiSocket,p_goodsId:number,p_goodsName:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(155);
	wsEncoder.writeInt(p_goodsId);
	wsEncoder.writeString(p_goodsName);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_200(myDecoder:WsDecoder):S2CGetOtherPlayerInfo{
				var retObj = new S2CGetOtherPlayerInfo();
	retObj.items = new Array<IOOtherPlayer>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOOtherPlayer(myDecoder);
		}
	}
			return retObj;
		}

		static get_1074(myDecoder:WsDecoder):S2CTnqwBossStart{
				var retObj = new S2CTnqwBossStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static get_158(myDecoder:WsDecoder):S2CRankLike{
				var retObj = new S2CRankLike();
	retObj.targetPlayerId=myDecoder.readInt();
	retObj.likeCount=myDecoder.readInt();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SLegionList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(625);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SQunheiLogin(senderSocket:net.LejiSocket,p_username:string,p_serverid:number,p_isadult:number,p_time:number,p_flag:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10031);
	wsEncoder.writeString(p_username);
	wsEncoder.writeInt(p_serverid);
	wsEncoder.writeInt(p_isadult);
	wsEncoder.writeInt(p_time);
	wsEncoder.writeString(p_flag);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_622(myDecoder:WsDecoder):S2CLegionCreate{
				var retObj = new S2CLegionCreate();
	retObj.legion_id=myDecoder.readLong();
			return retObj;
		}

		static send_C2SAffairAward(senderSocket:net.LejiSocket,p_affair_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(343);
	wsEncoder.writeInt(p_affair_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SManorGetBox(senderSocket:net.LejiSocket,p_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(867);
	wsEncoder.writeInt(p_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_132(myDecoder:WsDecoder):PushPaymentResult{
				var retObj = new PushPaymentResult();
	retObj.yb=myDecoder.readInt();
	retObj.pid=myDecoder.readString();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SSpinList(senderSocket:net.LejiSocket,p_type:number,p_is_force:boolean){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(401);
	wsEncoder.writeInt(p_type);
	wsEncoder.writeBool(p_is_force);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOGuoZhanPvpPlayer(myDecoder:WsDecoder):IOGuoZhanPvpPlayer{
				var retObj = new IOGuoZhanPvpPlayer();
	let base_info_exist = myDecoder.readBool();
	if(base_info_exist == true){
		retObj.base_info = MyProtocols.get_GuozhanOfficePointPlayer(myDecoder);
	}
	let battleset_exist = myDecoder.readBool();
	if(battleset_exist == true){
		retObj.battleset = MyProtocols.get_IOBattlesetEnemy(myDecoder);
	}
			return retObj;
		}

		static get_IOLegionBossSelf(myDecoder:WsDecoder):IOLegionBossSelf{
				var retObj = new IOLegionBossSelf();
	retObj.lastdamge=myDecoder.readLong();
			return retObj;
		}

		static send_C2SGeneralReset(senderSocket:net.LejiSocket,p_general_uuid:number,p_action_type:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(367);
	wsEncoder.writeLong(p_general_uuid);
	wsEncoder.writeInt(p_action_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_124(myDecoder:WsDecoder):S2CGetVipGift{
				var retObj = new S2CGetVipGift();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_1110(myDecoder:WsDecoder):S2CDungeonChapterInfo{
				var retObj = new S2CDungeonChapterInfo();
	let position_exist = myDecoder.readBool();
	if(position_exist == true){
		retObj.position = MyProtocols.get_IODungeonPosition(myDecoder);
	}
	retObj.nodelist = new Array<IODungeonNode>();
	let nodelist_size = myDecoder.readInt();
	if(nodelist_size >0){
		for(var i=0; i<nodelist_size;i++){
				retObj.nodelist[i] = MyProtocols.get_IODungeonNode(myDecoder);
		}
	}
	retObj.potion = new Array<IODungeonPotion>();
	let potion_size = myDecoder.readInt();
	if(potion_size >0){
		for(var i=0; i<potion_size;i++){
				retObj.potion[i] = MyProtocols.get_IODungeonPotion(myDecoder);
		}
	}
	let bufflist_exist = myDecoder.readBool();
	if(bufflist_exist == true){
		retObj.bufflist = MyProtocols.get_IODungeonBuffList(myDecoder);
	}
	let spbufflist_exist = myDecoder.readBool();
	if(spbufflist_exist == true){
		retObj.spbufflist = MyProtocols.get_IODungeonBuffList(myDecoder);
	}
			return retObj;
		}

		static send_C2SGuozhanOfficCalculate(senderSocket:net.LejiSocket,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1093);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOStageInfo(myDecoder:WsDecoder):IOStageInfo{
				var retObj = new IOStageInfo();
	retObj.schange=myDecoder.readInt();
	retObj.stage=myDecoder.readInt();
	retObj.star=myDecoder.readInt();
			return retObj;
		}

		static get_1088(myDecoder:WsDecoder):S2CGuozhanBattleStart{
				var retObj = new S2CGuozhanBattleStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static get_IOOcctaskPackinfo(myDecoder:WsDecoder):IOOcctaskPackinfo{
				var retObj = new IOOcctaskPackinfo();
	retObj.ID=myDecoder.readInt();
	retObj.ITEMS = new Array<RewardInfo>();
	let ITEMS_size = myDecoder.readInt();
	if(ITEMS_size >0){
		for(var i=0; i<ITEMS_size;i++){
				retObj.ITEMS[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.TYPE=myDecoder.readInt();
	retObj.VALUE=myDecoder.readInt();
	retObj.WEIGHT=myDecoder.readInt();
			return retObj;
		}

		static get_760(myDecoder:WsDecoder):S2CMythicalReset{
				var retObj = new S2CMythicalReset();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.mythicals = new Array<IOMythicalAnimal>();
	let mythicals_size = myDecoder.readInt();
	if(mythicals_size >0){
		for(var i=0; i<mythicals_size;i++){
				retObj.mythicals[i] = MyProtocols.get_IOMythicalAnimal(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SMineEnemyDetail(senderSocket:net.LejiSocket,p_level_index:number,p_point_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3103);
	wsEncoder.writeInt(p_level_index);
	wsEncoder.writeInt(p_point_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_326(myDecoder:WsDecoder):S2CShopRefresh{
				var retObj = new S2CShopRefresh();
	retObj.shop_type=myDecoder.readString();
	retObj.item_list = new Array<IOShopItem>();
	let item_list_size = myDecoder.readInt();
	if(item_list_size >0){
		for(var i=0; i<item_list_size;i++){
				retObj.item_list[i] = MyProtocols.get_IOShopItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SMonthBossInfo(senderSocket:net.LejiSocket,p_monthIndex:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(579);
	wsEncoder.writeInt(p_monthIndex);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOAwardRandomGeneral(myDecoder:WsDecoder):IOAwardRandomGeneral{
				var retObj = new IOAwardRandomGeneral();
	retObj.COUNT=myDecoder.readInt();
	retObj.STAR=myDecoder.readInt();
	retObj.CAMP=myDecoder.readInt();
	retObj.OCCU=myDecoder.readInt();
			return retObj;
		}

		static send_C2SPvpBattleStart(senderSocket:net.LejiSocket,p_pvp_index:number,p_role_id:number,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(513);
	wsEncoder.writeInt(p_pvp_index);
	wsEncoder.writeInt(p_role_id);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_356(myDecoder:WsDecoder):S2CGeneralTakeonEquip{
				var retObj = new S2CGeneralTakeonEquip();
	retObj.action_type=myDecoder.readInt();
	retObj.general_uuid=myDecoder.readLong();
	retObj.equip_id=myDecoder.readInt();
	retObj.pos_index=myDecoder.readInt();
	let general_bean_exist = myDecoder.readBool();
	if(general_bean_exist == true){
		retObj.general_bean = MyProtocols.get_IOGeneralBean(myDecoder);
	}
			return retObj;
		}

		static get_3110(myDecoder:WsDecoder):S2CMineHistory{
				var retObj = new S2CMineHistory();
	retObj.records = new Array<IOMineHistory>();
	let records_size = myDecoder.readInt();
	if(records_size >0){
		for(var i=0; i<records_size;i++){
				retObj.records[i] = MyProtocols.get_IOMineHistory(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOFriendBoss(myDecoder:WsDecoder):IOFriendBoss{
				var retObj = new IOFriendBoss();
	retObj.ownPlayerId=myDecoder.readInt();
	retObj.id=myDecoder.readInt();
	retObj.gsid=myDecoder.readInt();
	retObj.name=myDecoder.readString();
	retObj.level=myDecoder.readInt();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.etime=myDecoder.readLong();
	retObj.last=myDecoder.readLong();
	retObj.maxhp=myDecoder.readLong();
	retObj.nowhp=myDecoder.readLong();
	retObj.lastdamges=myDecoder.readInt();
	retObj.bset = {};
	let bset_size = myDecoder.readInt();
	if(bset_size >0){
		for(var i=0; i<bset_size;i++){
			let bset_key =myDecoder.readInt();
				retObj.bset[bset_key] = MyProtocols.get_IOGeneralSimple(myDecoder);
		}
	}
			return retObj;
		}

		static get_508(myDecoder:WsDecoder):S2CChapterBattleStart{
				var retObj = new S2CChapterBattleStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static get_160(myDecoder:WsDecoder):PushSmallTips{
				var retObj = new PushSmallTips();
	retObj.colorType=myDecoder.readInt();
	retObj.content=myDecoder.readString();
			return retObj;
		}

		static send_C2SEquipComp(senderSocket:net.LejiSocket,p_equip_id:number,p_count:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(307);
	wsEncoder.writeInt(p_equip_id);
	wsEncoder.writeInt(p_count);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SMissionDailyAward(senderSocket:net.LejiSocket,p_mission_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(551);
	wsEncoder.writeInt(p_mission_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SDungeonChapterReward(senderSocket:net.LejiSocket,p_chapter:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1107);
	wsEncoder.writeInt(p_chapter);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1014(myDecoder:WsDecoder):S2CHeroChoose1in3{
				var retObj = new S2CHeroChoose1in3();
	retObj.rewards = new Array<SimpleItemInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_SimpleItemInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_188(myDecoder:WsDecoder):S2CGoldBuyList{
				var retObj = new S2CGoldBuyList();
	retObj.buy_seconds = new Array<number>();
	let buy_seconds_size = myDecoder.readInt();
	if(buy_seconds_size >0){
		for(var i=0; i<buy_seconds_size;i++){
			retObj.buy_seconds[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static get_362(myDecoder:WsDecoder):S2CGeneralAddStar{
				var retObj = new S2CGeneralAddStar();
	retObj.target_gsid=myDecoder.readInt();
	retObj.general_uuid=myDecoder.readLong();
	retObj.cost_generals = new Array<number>();
	let cost_generals_size = myDecoder.readInt();
	if(cost_generals_size >0){
		for(var i=0; i<cost_generals_size;i++){
			retObj.cost_generals[i]=myDecoder.readLong();
		}
	}
	retObj.items = new Array<RewardInfo>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	let general_bean_exist = myDecoder.readBool();
	if(general_bean_exist == true){
		retObj.general_bean = MyProtocols.get_IOGeneralBean(myDecoder);
	}
			return retObj;
		}

		static get_1076(myDecoder:WsDecoder):S2CTnqwBossEnd{
				var retObj = new S2CTnqwBossEnd();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SDownlineReconnect(senderSocket:net.LejiSocket,p_in_game_session_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(125);
	wsEncoder.writeLong(p_in_game_session_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_3312(myDecoder:WsDecoder):S2CSecretReset{
				var retObj = new S2CSecretReset();
			return retObj;
		}

		static send_C2SGuozhanOfficeStart(senderSocket:net.LejiSocket,p_mythic:number,p_items:Array<IOFormationGeneralPos>,p_office_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1091);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
	wsEncoder.writeInt(p_office_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_608(myDecoder:WsDecoder):S2CChatVisit{
				var retObj = new S2CChatVisit();
			return retObj;
		}

		static get_860(myDecoder:WsDecoder):S2CManorBuild{
				var retObj = new S2CManorBuild();
			return retObj;
		}

		static send_C2SMythicalPskillUp(senderSocket:net.LejiSocket,p_mythical_id:number,p_pskill_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(757);
	wsEncoder.writeInt(p_mythical_id);
	wsEncoder.writeInt(p_pskill_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_636(myDecoder:WsDecoder):S2CLegionApplyReview{
				var retObj = new S2CLegionApplyReview();
	retObj.ret = new Array<IOLegionApplyReview>();
	let ret_size = myDecoder.readInt();
	if(ret_size >0){
		for(var i=0; i<ret_size;i++){
				retObj.ret[i] = MyProtocols.get_IOLegionApplyReview(myDecoder);
		}
	}
			return retObj;
		}

		static get_470(myDecoder:WsDecoder):S2CFriendshipOnekey{
				var retObj = new S2CFriendshipOnekey();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SGetSzhc(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1239);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGuozhanCityStart(senderSocket:net.LejiSocket,p_mythic:number,p_items:Array<IOFormationGeneralPos>,p_city_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1095);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
	wsEncoder.writeInt(p_city_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGuozhanCityCalculate(senderSocket:net.LejiSocket,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1097);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SDungeonShopList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1119);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_118(myDecoder:WsDecoder):S2CGuideStepSet{
				var retObj = new S2CGuideStepSet();
			return retObj;
		}

		static send_C2S185syLogin(senderSocket:net.LejiSocket,p_token:string,p_userID:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10067);
	wsEncoder.writeString(p_token);
	wsEncoder.writeString(p_userID);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOMapEvent(myDecoder:WsDecoder):IOMapEvent{
				var retObj = new IOMapEvent();
	retObj.hash=myDecoder.readString();
	retObj.type=myDecoder.readInt();
	retObj.eid=myDecoder.readInt();
			return retObj;
		}

		static get_1202(myDecoder:WsDecoder):S2CActivityList{
				var retObj = new S2CActivityList();
	retObj.fixed_activities = new Array<FixedActivityInfo>();
	let fixed_activities_size = myDecoder.readInt();
	if(fixed_activities_size >0){
		for(var i=0; i<fixed_activities_size;i++){
				retObj.fixed_activities[i] = MyProtocols.get_FixedActivityInfo(myDecoder);
		}
	}
	retObj.dynamic_activities = new Array<DynamicActivityInfo>();
	let dynamic_activities_size = myDecoder.readInt();
	if(dynamic_activities_size >0){
		for(var i=0; i<dynamic_activities_size;i++){
				retObj.dynamic_activities[i] = MyProtocols.get_DynamicActivityInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_874(myDecoder:WsDecoder):S2CSrenderList{
				var retObj = new S2CSrenderList();
	retObj.list = {};
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
			let list_key =myDecoder.readInt();
				retObj.list[list_key] = MyProtocols.get_IOSrenderState(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOGuozhanPointPlayer(myDecoder:WsDecoder):IOGuozhanPointPlayer{
				var retObj = new IOGuozhanPointPlayer();
	retObj.rid=myDecoder.readInt();
	retObj.rname=myDecoder.readString();
	retObj.level=myDecoder.readInt();
	retObj.iconid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.fight=myDecoder.readInt();
			return retObj;
		}

		static get_134(myDecoder:WsDecoder):PushFirstPay{
				var retObj = new PushFirstPay();
			return retObj;
		}

		static get_638(myDecoder:WsDecoder):S2CLegionQuit{
				var retObj = new S2CLegionQuit();
			return retObj;
		}

		static send_C2SWanbaLogin(senderSocket:net.LejiSocket,p_appid:string,p_openid:string,p_openkey:string,p_platform:number,p_pf:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10033);
	wsEncoder.writeString(p_appid);
	wsEncoder.writeString(p_openid);
	wsEncoder.writeString(p_openkey);
	wsEncoder.writeInt(p_platform);
	wsEncoder.writeString(p_pf);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1102(myDecoder:WsDecoder):S2CDungeonOpenTime{
				var retObj = new S2CDungeonOpenTime();
	retObj.open = new Array<number>();
	let open_size = myDecoder.readInt();
	if(open_size >0){
		for(var i=0; i<open_size;i++){
			retObj.open[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static get_566(myDecoder:WsDecoder):S2COccTaskGiftAll{
				var retObj = new S2COccTaskGiftAll();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_KvIntPair(myDecoder:WsDecoder):KvIntPair{
				var retObj = new KvIntPair();
	retObj.key=myDecoder.readInt();
	retObj.value=myDecoder.readInt();
			return retObj;
		}

		static send_C2SGeneralAddLevel(senderSocket:net.LejiSocket,p_general_uuid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(357);
	wsEncoder.writeLong(p_general_uuid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGuozhanChangeNation(senderSocket:net.LejiSocket,p_target_nation:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3457);
	wsEncoder.writeInt(p_target_nation);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_116(myDecoder:WsDecoder):PushGuideInfo{
				var retObj = new PushGuideInfo();
	retObj.guideInfo = new Array<GuideStepInfo>();
	let guideInfo_size = myDecoder.readInt();
	if(guideInfo_size >0){
		for(var i=0; i<guideInfo_size;i++){
				retObj.guideInfo[i] = MyProtocols.get_GuideStepInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_192(myDecoder:WsDecoder):S2CDrawList{
				var retObj = new S2CDrawList();
			return retObj;
		}

		static get_IODungeonChooseDetail(myDecoder:WsDecoder):IODungeonChooseDetail{
				var retObj = new IODungeonChooseDetail();
	retObj.id=myDecoder.readInt();
	retObj.name=myDecoder.readString();
	retObj.gsid=myDecoder.readInt();
	retObj.set = {};
	let set_size = myDecoder.readInt();
	if(set_size >0){
		for(var i=0; i<set_size;i++){
			let set_key =myDecoder.readInt();
				retObj.set[set_key] = MyProtocols.get_IODungeonBset(myDecoder);
		}
	}
	retObj.hppercent = {};
	let hppercent_size = myDecoder.readInt();
	if(hppercent_size >0){
		for(var i=0; i<hppercent_size;i++){
			let hppercent_key =myDecoder.readInt();
			retObj.hppercent[hppercent_key]=myDecoder.readInt();
		}
	}
	retObj.buffs = new Array<number>();
	let buffs_size = myDecoder.readInt();
	if(buffs_size >0){
		for(var i=0; i<buffs_size;i++){
			retObj.buffs[i]=myDecoder.readInt();
		}
	}
	retObj.disc=myDecoder.readInt();
	retObj.item = new Array<IORewardItem>();
	let item_size = myDecoder.readInt();
	if(item_size >0){
		for(var i=0; i<item_size;i++){
				retObj.item[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.consume = new Array<IORewardItem>();
	let consume_size = myDecoder.readInt();
	if(consume_size >0){
		for(var i=0; i<consume_size;i++){
				retObj.consume[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.quality=myDecoder.readInt();
	retObj.refnum=myDecoder.readInt();
	retObj.goods = new Array<IORewardItem>();
	let goods_size = myDecoder.readInt();
	if(goods_size >0){
		for(var i=0; i<goods_size;i++){
				retObj.goods[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SZhdTime(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1221);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_52(myDecoder:WsDecoder):S2CTestLogin{
				var retObj = new S2CTestLogin();
	retObj.uid=myDecoder.readInt();
	retObj.session_id=myDecoder.readLong();
			return retObj;
		}

		static send_C2SZhangMengLogin(senderSocket:net.LejiSocket,p_uid:string,p_t:string,p_sign:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10035);
	wsEncoder.writeString(p_uid);
	wsEncoder.writeString(p_t);
	wsEncoder.writeString(p_sign);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_154(myDecoder:WsDecoder):S2CChongChongPayPre{
				var retObj = new S2CChongChongPayPre();
	retObj.goodsId=myDecoder.readInt();
	retObj.egretOrderId=myDecoder.readString();
	retObj.money=myDecoder.readInt();
	retObj.ext=myDecoder.readString();
	retObj.goodsName=myDecoder.readString();
			return retObj;
		}

		static send_C2SMapEventFinish(senderSocket:net.LejiSocket,p_cityId:number,p_option:number,p_giveup:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(543);
	wsEncoder.writeInt(p_cityId);
	wsEncoder.writeInt(p_option);
	wsEncoder.writeInt(p_giveup);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SBigBattleStart(senderSocket:net.LejiSocket,p_mapid:number,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(573);
	wsEncoder.writeInt(p_mapid);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SAffairLock(senderSocket:net.LejiSocket,p_affair_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(337);
	wsEncoder.writeInt(p_affair_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_190(myDecoder:WsDecoder):S2CGoldBuy{
				var retObj = new S2CGoldBuy();
	retObj.buy_type=myDecoder.readInt();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_1060(myDecoder:WsDecoder):S2COpenExploreBoss{
				var retObj = new S2COpenExploreBoss();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static send_C2SQiZhenYiBaoView(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1207);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SSetCxryGeneralList(senderSocket:net.LejiSocket,p_gsids:Array<number>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1279);
	if(p_gsids == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_gsids.length);
		p_gsids.forEach(function(p_gsids_v){
	wsEncoder.writeInt(p_gsids_v);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_SimpleItemInfo(myDecoder:WsDecoder):SimpleItemInfo{
				var retObj = new SimpleItemInfo();
	retObj.gsid=myDecoder.readInt();
	retObj.count=myDecoder.readInt();
			return retObj;
		}

		static send_C2SDungeonSaveGeneralList(senderSocket:net.LejiSocket,p_guids:Array<number>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1105);
	if(p_guids == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_guids.length);
		p_guids.forEach(function(p_guids_v){
	wsEncoder.writeLong(p_guids_v);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1068(myDecoder:WsDecoder):S2CFriendBattleEnd{
				var retObj = new S2CFriendBattleEnd();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.videoid=myDecoder.readLong();
			return retObj;
		}

		static get_180(myDecoder:WsDecoder):PushIosIAPVerify{
				var retObj = new PushIosIAPVerify();
	retObj.ret=myDecoder.readInt();
			return retObj;
		}

		static send_C2SLegionFactoryMissionList(senderSocket:net.LejiSocket,p_isupdate:boolean){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(655);
	wsEncoder.writeBool(p_isupdate);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SMjbgInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1261);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1082(myDecoder:WsDecoder):S2CKpStart{
				var retObj = new S2CKpStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static get_IODungeonTop(myDecoder:WsDecoder):IODungeonTop{
				var retObj = new IODungeonTop();
	retObj.chapter=myDecoder.readInt();
	retObj.node=myDecoder.readInt();
			return retObj;
		}

		static get_322(myDecoder:WsDecoder):S2CShopList{
				var retObj = new S2CShopList();
	retObj.item_list = new Array<IOShopItem>();
	let item_list_size = myDecoder.readInt();
	if(item_list_size >0){
		for(var i=0; i<item_list_size;i++){
				retObj.item_list[i] = MyProtocols.get_IOShopItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOFriendChapter(myDecoder:WsDecoder):IOFriendChapter{
				var retObj = new IOFriendChapter();
	retObj.status=myDecoder.readInt();
	retObj.chapterid=myDecoder.readInt();
	retObj.power=myDecoder.readInt();
	retObj.exploremin=myDecoder.readInt();
	retObj.friends = new Array<IOFriendEntity>();
	let friends_size = myDecoder.readInt();
	if(friends_size >0){
		for(var i=0; i<friends_size;i++){
				retObj.friends[i] = MyProtocols.get_IOFriendEntity(myDecoder);
		}
	}
	retObj.etime=myDecoder.readLong();
			return retObj;
		}

		static get_406(myDecoder:WsDecoder):S2CSpinBoxOpen{
				var retObj = new S2CSpinBoxOpen();
	retObj.type=myDecoder.readInt();
	retObj.box_index=myDecoder.readInt();
	retObj.rewards = new Array<IOSpinItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IOSpinItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_IODungeonGlobalBuf(myDecoder:WsDecoder):IODungeonGlobalBuf{
				var retObj = new IODungeonGlobalBuf();
	retObj.hp=myDecoder.readInt();
	retObj.atk=myDecoder.readInt();
	retObj.def=myDecoder.readInt();
	retObj.mdef=myDecoder.readInt();
			return retObj;
		}

		static get_574(myDecoder:WsDecoder):S2CBigBattleStart{
				var retObj = new S2CBigBattleStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static send_C2SLegionFactoryMissionUp(senderSocket:net.LejiSocket,p_key:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(657);
	wsEncoder.writeLong(p_key);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_854(myDecoder:WsDecoder):S2COfficialItemReward{
				var retObj = new S2COfficialItemReward();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_106(myDecoder:WsDecoder):S2CCreateCharacter{
				var retObj = new S2CCreateCharacter();
	retObj.ret=myDecoder.readInt();
	retObj.character_id=myDecoder.readInt();
			return retObj;
		}

		static get_1238(myDecoder:WsDecoder):S2CGetCjxg1{
				var retObj = new S2CGetCjxg1();
	retObj.list = new Array<IOCjxg1>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOCjxg1(myDecoder);
		}
	}
			return retObj;
		}

		static get_1244(myDecoder:WsDecoder):S2CGetTnqwBossInfo{
				var retObj = new S2CGetTnqwBossInfo();
	retObj.name=myDecoder.readString();
	retObj.gsid=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.challrewards = new Array<RewardInfo>();
	let challrewards_size = myDecoder.readInt();
	if(challrewards_size >0){
		for(var i=0; i<challrewards_size;i++){
				retObj.challrewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.killrewards = new Array<RewardInfo>();
	let killrewards_size = myDecoder.readInt();
	if(killrewards_size >0){
		for(var i=0; i<killrewards_size;i++){
				retObj.killrewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.nowhp=myDecoder.readLong();
	retObj.maxhp=myDecoder.readLong();
	retObj.lastdamge=myDecoder.readLong();
	retObj.last=myDecoder.readLong();
	retObj.bset = {};
	let bset_size = myDecoder.readInt();
	if(bset_size >0){
		for(var i=0; i<bset_size;i++){
			let bset_key =myDecoder.readInt();
				retObj.bset[bset_key] = MyProtocols.get_IOGeneralLegion(myDecoder);
		}
	}
			return retObj;
		}

		static get_1224(myDecoder:WsDecoder):S2CGetCjxg2{
				var retObj = new S2CGetCjxg2();
	retObj.list = new Array<IOCjxg2>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOCjxg2(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SOpenLogin(senderSocket:net.LejiSocket,p_access_token:string,p_open_id:string,p_platform_type:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(55);
	wsEncoder.writeString(p_access_token);
	wsEncoder.writeString(p_open_id);
	wsEncoder.writeInt(p_platform_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SMythicalClassUp(senderSocket:net.LejiSocket,p_mythical_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(755);
	wsEncoder.writeInt(p_mythical_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_174(myDecoder:WsDecoder):S2CChangeHeadFrame{
				var retObj = new S2CChangeHeadFrame();
	retObj.head_frame_id=myDecoder.readInt();
			return retObj;
		}

		static get_868(myDecoder:WsDecoder):S2CManorGetBox{
				var retObj = new S2CManorGetBox();
	retObj.items = new Array<IORewardItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.boxItem = new Array<IORewardItem>();
	let boxItem_size = myDecoder.readInt();
	if(boxItem_size >0){
		for(var i=0; i<boxItem_size;i++){
				retObj.boxItem[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_1248(myDecoder:WsDecoder):S2CZmjfInfo{
				var retObj = new S2CZmjfInfo();
	retObj.looplimit=myDecoder.readInt();
	retObj.currentloop=myDecoder.readInt();
	retObj.missions = new Array<IODjjfMission>();
	let missions_size = myDecoder.readInt();
	if(missions_size >0){
		for(var i=0; i<missions_size;i++){
				retObj.missions[i] = MyProtocols.get_IODjjfMission(myDecoder);
		}
	}
			return retObj;
		}

		static get_1008(myDecoder:WsDecoder):S2CGuideSave{
				var retObj = new S2CGuideSave();
			return retObj;
		}

		static get_IOWorldBossSelf(myDecoder:WsDecoder):IOWorldBossSelf{
				var retObj = new IOWorldBossSelf();
	retObj.rank=myDecoder.readInt();
	retObj.damage=myDecoder.readLong();
			return retObj;
		}

		static get_3472(myDecoder:WsDecoder):S2CGuozhanHistory{
				var retObj = new S2CGuozhanHistory();
	retObj.records = new Array<IOGuozhanHistory>();
	let records_size = myDecoder.readInt();
	if(records_size >0){
		for(var i=0; i<records_size;i++){
				retObj.records[i] = MyProtocols.get_IOGuozhanHistory(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOLegionMember(myDecoder:WsDecoder):IOLegionMember{
				var retObj = new IOLegionMember();
	retObj.id=myDecoder.readInt();
	retObj.name=myDecoder.readString();
	retObj.icon=myDecoder.readInt();
	retObj.headid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.lv=myDecoder.readInt();
	retObj.lastest=myDecoder.readLong();
	retObj.pos=myDecoder.readInt();
	retObj.score=myDecoder.readInt();
	retObj.power=myDecoder.readInt();
	retObj.time=myDecoder.readLong();
			return retObj;
		}

		static get_136(myDecoder:WsDecoder):S2CGetYuekaAward{
				var retObj = new S2CGetYuekaAward();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOWorldBossWorldRank(myDecoder:WsDecoder):IOWorldBossWorldRank{
				var retObj = new IOWorldBossWorldRank();
	retObj.sid=myDecoder.readInt();
	retObj.legion=myDecoder.readLong();
	retObj.lname=myDecoder.readString();
	retObj.total=myDecoder.readLong();
	retObj.level=myDecoder.readInt();
	retObj.flag=myDecoder.readInt();
	retObj.index=myDecoder.readInt();
			return retObj;
		}

		static get_182(myDecoder:WsDecoder):S2CQedjList{
				var retObj = new S2CQedjList();
	retObj.is_award_get=myDecoder.readBool();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_382(myDecoder:WsDecoder):S2CGeneralExchangeRefresh{
				var retObj = new S2CGeneralExchangeRefresh();
	retObj.guid=myDecoder.readLong();
	retObj.gsid=myDecoder.readInt();
			return retObj;
		}

		static send_C2SSecretGetAward(senderSocket:net.LejiSocket,p_stage_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3307);
	wsEncoder.writeInt(p_stage_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_472(myDecoder:WsDecoder):S2CFriendExploreData{
				var retObj = new S2CFriendExploreData();
	retObj.maxbphys=myDecoder.readInt();
	retObj.maxqphys=myDecoder.readInt();
	let boss_exist = myDecoder.readBool();
	if(boss_exist == true){
		retObj.boss = MyProtocols.get_IOFriendBoss(myDecoder);
	}
	retObj.chapters = new Array<IOFriendChapter>();
	let chapters_size = myDecoder.readInt();
	if(chapters_size >0){
		for(var i=0; i<chapters_size;i++){
				retObj.chapters[i] = MyProtocols.get_IOFriendChapter(myDecoder);
		}
	}
	retObj.kill=myDecoder.readInt();
			return retObj;
		}

		static send_C2STeamsInfoSet(senderSocket:net.LejiSocket,p_type:number,p_pos_card_uuid:Array<number>,p_pet_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(185);
	wsEncoder.writeInt(p_type);
	if(p_pos_card_uuid == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_pos_card_uuid.length);
		p_pos_card_uuid.forEach(function(p_pos_card_uuid_v){
	wsEncoder.writeLong(p_pos_card_uuid_v);
		});
	}
	wsEncoder.writeInt(p_pet_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGuideStepSet(senderSocket:net.LejiSocket,p_module:number,p_step:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(117);
	wsEncoder.writeInt(p_module);
	wsEncoder.writeInt(p_step);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_354(myDecoder:WsDecoder):PushAddGeneral{
				var retObj = new PushAddGeneral();
	retObj.action=myDecoder.readString();
	let general_info_exist = myDecoder.readBool();
	if(general_info_exist == true){
		retObj.general_info = MyProtocols.get_IOGeneralBean(myDecoder);
	}
			return retObj;
		}

		static get_1256(myDecoder:WsDecoder):S2CGetJfbx{
				var retObj = new S2CGetJfbx();
	retObj.box = new Array<IOJfbxBox>();
	let box_size = myDecoder.readInt();
	if(box_size >0){
		for(var i=0; i<box_size;i++){
				retObj.box[i] = MyProtocols.get_IOJfbxBox(myDecoder);
		}
	}
	retObj.event = new Array<IOJfbxEvent>();
	let event_size = myDecoder.readInt();
	if(event_size >0){
		for(var i=0; i<event_size;i++){
				retObj.event[i] = MyProtocols.get_IOJfbxEvent(myDecoder);
		}
	}
			return retObj;
		}

		static get_176(myDecoder:WsDecoder):S2CChangeHeadImage{
				var retObj = new S2CChangeHeadImage();
	retObj.image_id=myDecoder.readInt();
			return retObj;
		}

		static send_C2SMjbgChange(senderSocket:net.LejiSocket,p_index:number,p_itemindex:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1263);
	wsEncoder.writeInt(p_index);
	wsEncoder.writeInt(p_itemindex);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLegionGetBossInfo(senderSocket:net.LejiSocket,p_chapter_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(665);
	wsEncoder.writeInt(p_chapter_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SMineList(senderSocket:net.LejiSocket,p_level_index:number,p_page_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3101);
	wsEncoder.writeInt(p_level_index);
	wsEncoder.writeInt(p_page_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SMailList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(701);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_378(myDecoder:WsDecoder):S2CGeneralSummon{
				var retObj = new S2CGeneralSummon();
	retObj.type=myDecoder.readInt();
	retObj.times=myDecoder.readInt();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SDungeonChapterList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1103);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_582(myDecoder:WsDecoder):S2CMonthBossBattleStart{
				var retObj = new S2CMonthBossBattleStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static send_C2SManorReset(senderSocket:net.LejiSocket,p_pos:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(861);
	wsEncoder.writeInt(p_pos);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SOccTaskInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(557);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_460(myDecoder:WsDecoder):S2CFriendDel{
				var retObj = new S2CFriendDel();
	retObj.role_id=myDecoder.readInt();
			return retObj;
		}

		static send_C2SManorMop(senderSocket:net.LejiSocket,p_times:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(871);
	wsEncoder.writeInt(p_times);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1226(myDecoder:WsDecoder):S2CGxCzlbList{
				var retObj = new S2CGxCzlbList();
	retObj.list = new Array<IOCzlb>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOCzlb(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOCzlb(myDecoder:WsDecoder):IOCzlb{
				var retObj = new IOCzlb();
	retObj.value=myDecoder.readInt();
	retObj.price=myDecoder.readInt();
	retObj.items = new Array<RewardInfo>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.buytime=myDecoder.readInt();
	retObj.limit=myDecoder.readInt();
	retObj.special = new Array<RewardInfo>();
	let special_size = myDecoder.readInt();
	if(special_size >0){
		for(var i=0; i<special_size;i++){
				retObj.special[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.exp=myDecoder.readInt();
	retObj.path=myDecoder.readString();
			return retObj;
		}

		static get_604(myDecoder:WsDecoder):S2CChatPush{
				var retObj = new S2CChatPush();
	retObj.msgtype=myDecoder.readInt();
	retObj.senderid=myDecoder.readString();
	retObj.rid=myDecoder.readInt();
	retObj.rname=myDecoder.readString();
	retObj.iconid=myDecoder.readInt();
	retObj.headid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.vip=myDecoder.readInt();
	retObj.office_index=myDecoder.readInt();
	retObj.serverid=myDecoder.readInt();
	retObj.content=myDecoder.readString();
	retObj.videoId=myDecoder.readLong();
	retObj.send_time=myDecoder.readLong();
	retObj.id=myDecoder.readInt();
			return retObj;
		}

		static send_C2SLegionSet(senderSocket:net.LejiSocket,p_notice:string,p_minlv:number,p_ispass:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(623);
	wsEncoder.writeString(p_notice);
	wsEncoder.writeInt(p_minlv);
	wsEncoder.writeInt(p_ispass);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLegionFactoryLv(senderSocket:net.LejiSocket,p_type:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(651);
	wsEncoder.writeInt(p_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IORewardItem(myDecoder:WsDecoder):IORewardItem{
				var retObj = new IORewardItem();
	retObj.GSID=myDecoder.readInt();
	retObj.COUNT=myDecoder.readInt();
			return retObj;
		}

		static get_IOGuoZhanCity(myDecoder:WsDecoder):IOGuoZhanCity{
				var retObj = new IOGuoZhanCity();
	retObj.player_id=myDecoder.readInt();
	retObj.player_name=myDecoder.readString();
	retObj.player_size=myDecoder.readInt();
	retObj.nation_id=myDecoder.readInt();
	retObj.in_battle=myDecoder.readBool();
			return retObj;
		}

		static get_DynamicActivityInfo(myDecoder:WsDecoder):DynamicActivityInfo{
				var retObj = new DynamicActivityInfo();
	retObj.activeBigID=myDecoder.readInt();
	retObj.priority=myDecoder.readInt();
	retObj.des=myDecoder.readString();
	retObj.StartTime=myDecoder.readInt();
	retObj.EndTime=myDecoder.readInt();
	retObj.nComplete=myDecoder.readInt();
	retObj.sub_activities = new Array<ActivityInfoOne>();
	let sub_activities_size = myDecoder.readInt();
	if(sub_activities_size >0){
		for(var i=0; i<sub_activities_size;i++){
				retObj.sub_activities[i] = MyProtocols.get_ActivityInfoOne(myDecoder);
		}
	}
	retObj.hero_libao_config = new Array<IoActivityHeroLiBao>();
	let hero_libao_config_size = myDecoder.readInt();
	if(hero_libao_config_size >0){
		for(var i=0; i<hero_libao_config_size;i++){
				retObj.hero_libao_config[i] = MyProtocols.get_IoActivityHeroLiBao(myDecoder);
		}
	}
	let lottery_wheel_config_exist = myDecoder.readBool();
	if(lottery_wheel_config_exist == true){
		retObj.lottery_wheel_config = MyProtocols.get_IoLotteryWheelConfig(myDecoder);
	}
			return retObj;
		}

		static send_C2SMiaoRenLogin(senderSocket:net.LejiSocket,p_app_id:string,p_account_id:string,p_token_key:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10063);
	wsEncoder.writeString(p_app_id);
	wsEncoder.writeString(p_account_id);
	wsEncoder.writeString(p_token_key);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOCjxg2(myDecoder:WsDecoder):IOCjxg2{
				var retObj = new IOCjxg2();
	retObj.value=myDecoder.readInt();
	retObj.price=myDecoder.readInt();
	retObj.items = new Array<RewardInfo>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.buytime=myDecoder.readInt();
	retObj.icon=myDecoder.readString();
	retObj.special = new Array<RewardInfo>();
	let special_size = myDecoder.readInt();
	if(special_size >0){
		for(var i=0; i<special_size;i++){
				retObj.special[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.bg1=myDecoder.readString();
	retObj.bg2=myDecoder.readString();
	retObj.hero=myDecoder.readString();
	retObj.heroname=myDecoder.readString();
	retObj.txbig=myDecoder.readString();
	retObj.normal=myDecoder.readString();
	retObj.check=myDecoder.readString();
			return retObj;
		}

		static send_C2SDungeonBattleStart(senderSocket:net.LejiSocket,p_chapter:number,p_node:number,p_pos:number,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1055);
	wsEncoder.writeInt(p_chapter);
	wsEncoder.writeInt(p_node);
	wsEncoder.writeInt(p_pos);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOCjxg1(myDecoder:WsDecoder):IOCjxg1{
				var retObj = new IOCjxg1();
	retObj.viple=myDecoder.readInt();
	retObj.consume = new Array<RewardInfo>();
	let consume_size = myDecoder.readInt();
	if(consume_size >0){
		for(var i=0; i<consume_size;i++){
				retObj.consume[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.items = new Array<RewardInfo>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.buytime=myDecoder.readInt();
			return retObj;
		}

		static send_C2SFriendBattleInfo(senderSocket:net.LejiSocket,p_friendid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1063);
	wsEncoder.writeInt(p_friendid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_514(myDecoder:WsDecoder):S2CPvpBattleStart{
				var retObj = new S2CPvpBattleStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static send_C2SQuickLogin(senderSocket:net.LejiSocket,p_token:string,p_product_code:string,p_uid:string,p_channel_code:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10065);
	wsEncoder.writeString(p_token);
	wsEncoder.writeString(p_product_code);
	wsEncoder.writeString(p_uid);
	wsEncoder.writeString(p_channel_code);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_808(myDecoder:WsDecoder):S2CGetOnlineAward{
				var retObj = new S2CGetOnlineAward();
	retObj.reward = new Array<AwardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_AwardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_1090(myDecoder:WsDecoder):S2CGuozhanBattleEnd{
				var retObj = new S2CGuozhanBattleEnd();
	retObj.city_index=myDecoder.readInt();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SMzlbList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1217);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SXsdhDh(senderSocket:net.LejiSocket,p_item_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1273);
	wsEncoder.writeInt(p_item_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SMythicalLevelUp(senderSocket:net.LejiSocket,p_mythical_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(753);
	wsEncoder.writeInt(p_mythical_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOSpinItem(myDecoder:WsDecoder):IOSpinItem{
				var retObj = new IOSpinItem();
	retObj.GSID=myDecoder.readInt();
	retObj.COUNT=myDecoder.readInt();
	retObj.REPEAT=myDecoder.readInt();
			return retObj;
		}

		static get_102(myDecoder:WsDecoder):S2CGmCmd{
				var retObj = new S2CGmCmd();
	retObj.ret_code=myDecoder.readInt();
			return retObj;
		}

		static get_168(myDecoder:WsDecoder):S2CChangeName{
				var retObj = new S2CChangeName();
	retObj.name=myDecoder.readString();
			return retObj;
		}

		static send_C2SGeneralExchangeConfirm(senderSocket:net.LejiSocket,p_action_type:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(383);
	wsEncoder.writeInt(p_action_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLegionApply(senderSocket:net.LejiSocket,p_legion_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(631);
	wsEncoder.writeLong(p_legion_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_108(myDecoder:WsDecoder):S2CEnterGame{
				var retObj = new S2CEnterGame();
	retObj.current_time_seconds=myDecoder.readInt();
	retObj.game_session_id=myDecoder.readLong();
			return retObj;
		}

		static send_C2SGetFirstPayAward(senderSocket:net.LejiSocket,p_extype:number,p_markid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(121);
	wsEncoder.writeInt(p_extype);
	wsEncoder.writeInt(p_markid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGetCxryGeneralList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1277);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_170(myDecoder:WsDecoder):S2CHeadsFramesImagesList{
				var retObj = new S2CHeadsFramesImagesList();
	retObj._icons=myDecoder.readString();
	retObj.heads = new Array<number>();
	let heads_size = myDecoder.readInt();
	if(heads_size >0){
		for(var i=0; i<heads_size;i++){
			retObj.heads[i]=myDecoder.readInt();
		}
	}
	retObj.frames = new Array<number>();
	let frames_size = myDecoder.readInt();
	if(frames_size >0){
		for(var i=0; i<frames_size;i++){
			retObj.frames[i]=myDecoder.readInt();
		}
	}
	retObj.images = new Array<number>();
	let images_size = myDecoder.readInt();
	if(images_size >0){
		for(var i=0; i<images_size;i++){
			retObj.images[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static get_IOServerHasRole(myDecoder:WsDecoder):IOServerHasRole{
				var retObj = new IOServerHasRole();
	retObj.server_id=myDecoder.readInt();
	retObj.player_level=myDecoder.readInt();
			return retObj;
		}

		static send_C2SGeneralSummon(senderSocket:net.LejiSocket,p_type:number,p_times:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(377);
	wsEncoder.writeInt(p_type);
	wsEncoder.writeInt(p_times);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_3460(myDecoder:WsDecoder):S2CGuozhanOfficeView{
				var retObj = new S2CGuozhanOfficeView();
	retObj.player_list = new Array<GuozhanOfficePointPlayer>();
	let player_list_size = myDecoder.readInt();
	if(player_list_size >0){
		for(var i=0; i<player_list_size;i++){
				retObj.player_list[i] = MyProtocols.get_GuozhanOfficePointPlayer(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SGuideEnd(senderSocket:net.LejiSocket,p_add_step:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1011);
	wsEncoder.writeInt(p_add_step);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1270(myDecoder:WsDecoder):S2CPayGjdl{
				var retObj = new S2CPayGjdl();
			return retObj;
		}

		static send_C2SThree33Login(senderSocket:net.LejiSocket,p_gameId:number,p_time:number,p_uid:number,p_userName:string,p_sign:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10037);
	wsEncoder.writeInt(p_gameId);
	wsEncoder.writeInt(p_time);
	wsEncoder.writeInt(p_uid);
	wsEncoder.writeString(p_userName);
	wsEncoder.writeString(p_sign);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SCzlbList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1229);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGeneralAddStar(senderSocket:net.LejiSocket,p_target_gsid:number,p_general_uuid:number,p_cost_generals:Array<number>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(361);
	wsEncoder.writeInt(p_target_gsid);
	wsEncoder.writeLong(p_general_uuid);
	if(p_cost_generals == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_cost_generals.length);
		p_cost_generals.forEach(function(p_cost_generals_v){
	wsEncoder.writeLong(p_cost_generals_v);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SSelectServer(senderSocket:net.LejiSocket,p_server_id:number,p_session_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(61);
	wsEncoder.writeInt(p_server_id);
	wsEncoder.writeLong(p_session_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOManorFieldBoss(myDecoder:WsDecoder):IOManorFieldBoss{
				var retObj = new IOManorFieldBoss();
	retObj.state=myDecoder.readInt();
	retObj.lastdamage=myDecoder.readInt();
	retObj.maxhp=myDecoder.readInt();
	retObj.nowhp=myDecoder.readInt();
			return retObj;
		}

		static send_C2SMailRedNotice(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(711);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOExclusive(myDecoder:WsDecoder):IOExclusive{
				var retObj = new IOExclusive();
	retObj.level=myDecoder.readInt();
	retObj.skill = new Array<number>();
	let skill_size = myDecoder.readInt();
	if(skill_size >0){
		for(var i=0; i<skill_size;i++){
			retObj.skill[i]=myDecoder.readInt();
		}
	}
	retObj.gsid=myDecoder.readInt();
	retObj.property = new Array<KvStringPair>();
	let property_size = myDecoder.readInt();
	if(property_size >0){
		for(var i=0; i<property_size;i++){
				retObj.property[i] = MyProtocols.get_KvStringPair(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOBattleResult(myDecoder:WsDecoder):IOBattleResult{
				var retObj = new IOBattleResult();
	retObj.ret=myDecoder.readString();
	retObj.round=myDecoder.readInt();
	retObj.lhp = {};
	let lhp_size = myDecoder.readInt();
	if(lhp_size >0){
		for(var i=0; i<lhp_size;i++){
			let lhp_key =myDecoder.readInt();
			retObj.lhp[lhp_key]=myDecoder.readLong();
		}
	}
	retObj.rhp = {};
	let rhp_size = myDecoder.readInt();
	if(rhp_size >0){
		for(var i=0; i<rhp_size;i++){
			let rhp_key =myDecoder.readInt();
			retObj.rhp[rhp_key]=myDecoder.readLong();
		}
	}
	retObj.lper = {};
	let lper_size = myDecoder.readInt();
	if(lper_size >0){
		for(var i=0; i<lper_size;i++){
			let lper_key =myDecoder.readInt();
			retObj.lper[lper_key]=myDecoder.readInt();
		}
	}
	retObj.rper = {};
	let rper_size = myDecoder.readInt();
	if(rper_size >0){
		for(var i=0; i<rper_size;i++){
			let rper_key =myDecoder.readInt();
			retObj.rper[rper_key]=myDecoder.readInt();
		}
	}
	retObj.ltper=myDecoder.readInt();
	retObj.rtper=myDecoder.readInt();
	retObj.rlosthp=myDecoder.readInt();
	let report_exist = myDecoder.readBool();
	if(report_exist == true){
		retObj.report = MyProtocols.get_IOBattleReport(myDecoder);
	}
	retObj.version=myDecoder.readLong();
	let left_exist = myDecoder.readBool();
	if(left_exist == true){
		retObj.left = MyProtocols.get_IOBattleRetSide(myDecoder);
	}
	let right_exist = myDecoder.readBool();
	if(right_exist == true){
		retObj.right = MyProtocols.get_IOBattleRetSide(myDecoder);
	}
			return retObj;
		}

		static get_ServerListItem(myDecoder:WsDecoder):ServerListItem{
				var retObj = new ServerListItem();
	retObj.id=myDecoder.readInt();
	retObj.name=myDecoder.readString();
	retObj.ip_addr=myDecoder.readString();
	retObj.port=myDecoder.readInt();
	retObj.status=myDecoder.readInt();
	retObj.port_ssl=myDecoder.readInt();
			return retObj;
		}

		static send_C2SFriendOpenExplore(senderSocket:net.LejiSocket,p_chapter_id:number,p_friends:Array<number>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(473);
	wsEncoder.writeInt(p_chapter_id);
	if(p_friends == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_friends.length);
		p_friends.forEach(function(p_friends_v){
	wsEncoder.writeInt(p_friends_v);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGetGjdl(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1251);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_166(myDecoder:WsDecoder):S2CAddDesktopShortcutAward{
				var retObj = new S2CAddDesktopShortcutAward();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SPayCjxg1(senderSocket:net.LejiSocket,p_num_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1259);
	wsEncoder.writeInt(p_num_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_610(myDecoder:WsDecoder):S2CChatNewmsg{
				var retObj = new S2CChatNewmsg();
	retObj.has_newmsg=myDecoder.readBool();
			return retObj;
		}

		static send_C2SMythicalReset(senderSocket:net.LejiSocket,p_mythical_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(759);
	wsEncoder.writeInt(p_mythical_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SShopRefresh(senderSocket:net.LejiSocket,p_shop_type:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(325);
	wsEncoder.writeString(p_shop_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_516(myDecoder:WsDecoder):S2CPvpBattleEnd{
				var retObj = new S2CPvpBattleEnd();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.reward = new Array<IORewardItemSelect>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItemSelect(myDecoder);
		}
	}
	retObj.spoints=myDecoder.readInt();
	retObj.schange=myDecoder.readInt();
	retObj.epoints=myDecoder.readInt();
	retObj.echange=myDecoder.readInt();
	retObj.videoid=myDecoder.readLong();
			return retObj;
		}

		static send_C2SXingTengLogin(senderSocket:net.LejiSocket,p_app_id:string,p_uin:string,p_login_token:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10055);
	wsEncoder.writeString(p_app_id);
	wsEncoder.writeString(p_uin);
	wsEncoder.writeString(p_login_token);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_GuozhanOfficePointPlayer(myDecoder:WsDecoder):GuozhanOfficePointPlayer{
				var retObj = new GuozhanOfficePointPlayer();
	retObj.office_index=myDecoder.readInt();
	retObj.rid=myDecoder.readInt();
	retObj.rname=myDecoder.readString();
	retObj.level=myDecoder.readInt();
	retObj.iconid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.fight=myDecoder.readInt();
	retObj.hp_perc=myDecoder.readInt();
			return retObj;
		}

		static get_196(myDecoder:WsDecoder):S2CScrollAnno{
				var retObj = new S2CScrollAnno();
	retObj.annos = new Array<string>();
	let annos_size = myDecoder.readInt();
	if(annos_size >0){
		for(var i=0; i<annos_size;i++){
			retObj.annos[i]=myDecoder.readString();
		}
	}
			return retObj;
		}

		static send_C2SGeneralDecomp(senderSocket:net.LejiSocket,p_general_uuid:Array<number>,p_action_type:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(365);
	if(p_general_uuid == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_general_uuid.length);
		p_general_uuid.forEach(function(p_general_uuid_v){
	wsEncoder.writeLong(p_general_uuid_v);
		});
	}
	wsEncoder.writeInt(p_action_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOLiBao1(myDecoder:WsDecoder):IOLiBao1{
				var retObj = new IOLiBao1();
	retObj.price=myDecoder.readInt();
	retObj.buytime=myDecoder.readInt();
	retObj.items = new Array<RewardInfo>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SHeroChoose1in3(senderSocket:net.LejiSocket,p_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1013);
	wsEncoder.writeInt(p_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOBattleLine(myDecoder:WsDecoder):IOBattleLine{
				var retObj = new IOBattleLine();
	retObj.power=myDecoder.readInt();
			return retObj;
		}

		static get_3114(myDecoder:WsDecoder):S2CMineRedNotice{
				var retObj = new S2CMineRedNotice();
	retObj.ret=myDecoder.readBool();
			return retObj;
		}

		static get_528(myDecoder:WsDecoder):S2CExpedBattleStart{
				var retObj = new S2CExpedBattleStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static send_C2SMineHistory(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3109);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_560(myDecoder:WsDecoder):S2COccTaskFree{
				var retObj = new S2COccTaskFree();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_410(myDecoder:WsDecoder):S2CSpinBoxReset{
				var retObj = new S2CSpinBoxReset();
	retObj.type=myDecoder.readInt();
			return retObj;
		}

		static get_1114(myDecoder:WsDecoder):S2CDungeonUsePotion{
				var retObj = new S2CDungeonUsePotion();
	retObj.guid=myDecoder.readLong();
	retObj.hppercent=myDecoder.readInt();
			return retObj;
		}

		static get_172(myDecoder:WsDecoder):S2CChangeHeadIcon{
				var retObj = new S2CChangeHeadIcon();
	retObj.head_id=myDecoder.readInt();
			return retObj;
		}

		static send_C2SMonthBossBattleEnd(senderSocket:net.LejiSocket,p_monthIndex:number,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(583);
	wsEncoder.writeInt(p_monthIndex);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SOccTaskGiftAll(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(565);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGetFbossResult(senderSocket:net.LejiSocket,p_boss_owner_id:number,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1061);
	wsEncoder.writeInt(p_boss_owner_id);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_856(myDecoder:WsDecoder):S2COfficialPromo{
				var retObj = new S2COfficialPromo();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_374(myDecoder:WsDecoder):S2CExclusiveRefreshBegin{
				var retObj = new S2CExclusiveRefreshBegin();
	retObj.general_uuid=myDecoder.readLong();
	retObj.lock_type=myDecoder.readInt();
	retObj.skill = new Array<number>();
	let skill_size = myDecoder.readInt();
	if(skill_size >0){
		for(var i=0; i<skill_size;i++){
			retObj.skill[i]=myDecoder.readInt();
		}
	}
	retObj.property = new Array<KvStringPair>();
	let property_size = myDecoder.readInt();
	if(property_size >0){
		for(var i=0; i<property_size;i++){
				retObj.property[i] = MyProtocols.get_KvStringPair(myDecoder);
		}
	}
			return retObj;
		}

		static get_368(myDecoder:WsDecoder):S2CGeneralReset{
				var retObj = new S2CGeneralReset();
	retObj.general_uuid=myDecoder.readLong();
	retObj.items = new Array<RewardInfo>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	let general_bean_exist = myDecoder.readBool();
	if(general_bean_exist == true){
		retObj.general_bean = MyProtocols.get_IOGeneralBean(myDecoder);
	}
			return retObj;
		}

		static get_IOBattleRecordSide(myDecoder:WsDecoder):IOBattleRecordSide{
				var retObj = new IOBattleRecordSide();
	let info_exist = myDecoder.readBool();
	if(info_exist == true){
		retObj.info = MyProtocols.get_IOBattleRecordInfo(myDecoder);
	}
	let set_exist = myDecoder.readBool();
	if(set_exist == true){
		retObj.set = MyProtocols.get_IOBattleRecordSet(myDecoder);
	}
			return retObj;
		}

		static get_1058(myDecoder:WsDecoder):S2CDungeonBattleEnd{
				var retObj = new S2CDungeonBattleEnd();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.videoid=myDecoder.readLong();
			return retObj;
		}

		static get_1212(myDecoder:WsDecoder):PushQiZhenYiBaoKaiJiang{
				var retObj = new PushQiZhenYiBaoKaiJiang();
	let award_player_exist = myDecoder.readBool();
	if(award_player_exist == true){
		retObj.award_player = MyProtocols.get_QiZhenYiBaoPlayer(myDecoder);
	}
	retObj.hero_tpl_id=myDecoder.readInt();
			return retObj;
		}

		static send_C2SListSignIn(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(801);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_536(myDecoder:WsDecoder):S2CManorBattleStart{
				var retObj = new S2CManorBattleStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static get_640(myDecoder:WsDecoder):S2CLegionAppoint{
				var retObj = new S2CLegionAppoint();
			return retObj;
		}

		static get_IOSecretHero(myDecoder:WsDecoder):IOSecretHero{
				var retObj = new IOSecretHero();
	retObj.hero_type=myDecoder.readInt();
	retObj.hero_id=myDecoder.readInt();
	retObj.hp_percent=myDecoder.readInt();
			return retObj;
		}

		static get_IODungeonBuffList(myDecoder:WsDecoder):IODungeonBuffList{
				var retObj = new IODungeonBuffList();
	retObj.ppthr=myDecoder.readFloat();
	retObj.pskidam=myDecoder.readFloat();
	retObj.atk=myDecoder.readFloat();
	retObj.pcrid=myDecoder.readFloat();
	retObj.pmthr=myDecoder.readFloat();
	retObj.pcri=myDecoder.readFloat();
			return retObj;
		}

		static get_194(myDecoder:WsDecoder):S2CDrawRecruit{
				var retObj = new S2CDrawRecruit();
	retObj.buy_type=myDecoder.readInt();
	retObj.times=myDecoder.readInt();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOBattleFormation(myDecoder:WsDecoder):IOBattleFormation{
				var retObj = new IOBattleFormation();
	retObj.f_type=myDecoder.readString();
	retObj.mythic=myDecoder.readInt();
	retObj.items = new Array<IOFormationGeneralPos>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOFormationGeneralPos(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SGuozhanHistory(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3471);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SShopBuy(senderSocket:net.LejiSocket,p_shop_type:string,p_item_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(323);
	wsEncoder.writeString(p_shop_type);
	wsEncoder.writeInt(p_item_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1258(myDecoder:WsDecoder):S2CBuySzhc{
				var retObj = new S2CBuySzhc();
			return retObj;
		}

		static send_C2SOccTaskJobSelect(senderSocket:net.LejiSocket,p_occtype:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(561);
	wsEncoder.writeInt(p_occtype);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_340(myDecoder:WsDecoder):S2CAffairStart{
				var retObj = new S2CAffairStart();
	retObj.affair_index=myDecoder.readInt();
	retObj.item_list = new Array<IOAffairItem>();
	let item_list_size = myDecoder.readInt();
	if(item_list_size >0){
		for(var i=0; i<item_list_size;i++){
				retObj.item_list[i] = MyProtocols.get_IOAffairItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SPayGjdl(senderSocket:net.LejiSocket,p_item_index:number,p_buynum:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1269);
	wsEncoder.writeInt(p_item_index);
	wsEncoder.writeInt(p_buynum);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLegionDismiss(senderSocket:net.LejiSocket,p_rid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(641);
	wsEncoder.writeInt(p_rid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SChatVisit(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(607);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLegionQuit(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(637);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2STeamsInfoGet(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(141);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOCxryGenerals(myDecoder:WsDecoder):IOCxryGenerals{
				var retObj = new IOCxryGenerals();
	retObj.gsid=myDecoder.readInt();
	retObj.isget=myDecoder.readInt();
			return retObj;
		}

		static send_C2SListOnlineAward(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(805);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_3462(myDecoder:WsDecoder):S2CGuozhanOfficeDetail{
				var retObj = new S2CGuozhanOfficeDetail();
	retObj.office_index=myDecoder.readInt();
	let base_info_exist = myDecoder.readBool();
	if(base_info_exist == true){
		retObj.base_info = MyProtocols.get_GuozhanOfficePointPlayer(myDecoder);
	}
	let battleset_exist = myDecoder.readBool();
	if(battleset_exist == true){
		retObj.battleset = MyProtocols.get_IOBattlesetEnemy(myDecoder);
	}
			return retObj;
		}

		static send_C2SWorldBossBattleStart(senderSocket:net.LejiSocket,p_bossid:number,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1069);
	wsEncoder.writeInt(p_bossid);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SFriendExploreData(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(471);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SKpSearchEnemy(senderSocket:net.LejiSocket,p_force:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1153);
	wsEncoder.writeInt(p_force);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1228(myDecoder:WsDecoder):S2CGjCzlbList{
				var retObj = new S2CGjCzlbList();
	retObj.list = new Array<IOCzlb>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOCzlb(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SWorldBossInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(669);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1004(myDecoder:WsDecoder):S2CGuideStep{
				var retObj = new S2CGuideStep();
			return retObj;
		}

		static get_IOXsdh1(myDecoder:WsDecoder):IOXsdh1{
				var retObj = new IOXsdh1();
	retObj.grid=myDecoder.readInt();
	retObj.grch = new Array<RewardInfo>();
	let grch_size = myDecoder.readInt();
	if(grch_size >0){
		for(var i=0; i<grch_size;i++){
				retObj.grch[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.consume = new Array<RewardInfo>();
	let consume_size = myDecoder.readInt();
	if(consume_size >0){
		for(var i=0; i<consume_size;i++){
				retObj.consume[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.buytime=myDecoder.readInt();
			return retObj;
		}

		static get_IOMjbgSource(myDecoder:WsDecoder):IOMjbgSource{
				var retObj = new IOMjbgSource();
	retObj.intro=myDecoder.readString();
	retObj.page=myDecoder.readString();
			return retObj;
		}

		static get_1098(myDecoder:WsDecoder):S2CGuozhanCityCalculate{
				var retObj = new S2CGuozhanCityCalculate();
	retObj.city_index=myDecoder.readInt();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.move_step=myDecoder.readInt();
			return retObj;
		}

		static send_C2SBuyItem(senderSocket:net.LejiSocket,p_gsid:number,p_count:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(311);
	wsEncoder.writeInt(p_gsid);
	wsEncoder.writeInt(p_count);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLegionBossBattleStart(senderSocket:net.LejiSocket,p_chapter_id:number,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1051);
	wsEncoder.writeInt(p_chapter_id);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_666(myDecoder:WsDecoder):S2CLegionGetBossInfo{
				var retObj = new S2CLegionGetBossInfo();
	let boss_exist = myDecoder.readBool();
	if(boss_exist == true){
		retObj.boss = MyProtocols.get_IOLegionBoss(myDecoder);
	}
	retObj.rank = new Array<IOLegionRank>();
	let rank_size = myDecoder.readInt();
	if(rank_size >0){
		for(var i=0; i<rank_size;i++){
				retObj.rank[i] = MyProtocols.get_IOLegionRank(myDecoder);
		}
	}
	let self_exist = myDecoder.readBool();
	if(self_exist == true){
		retObj.self = MyProtocols.get_IOLegionBossSelf(myDecoder);
	}
			return retObj;
		}

		static send_C2SXingTengLogin2(senderSocket:net.LejiSocket,p_app_id:string,p_uin:string,p_login_token:string,p_app_key:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10057);
	wsEncoder.writeString(p_app_id);
	wsEncoder.writeString(p_uin);
	wsEncoder.writeString(p_login_token);
	wsEncoder.writeString(p_app_key);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SChapterInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(501);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_3466(myDecoder:WsDecoder):S2CGuoZhanCityDetail{
				var retObj = new S2CGuoZhanCityDetail();
	retObj.city_index=myDecoder.readInt();
	retObj.players = new Array<IOGuoZhanPvpPlayer>();
	let players_size = myDecoder.readInt();
	if(players_size >0){
		for(var i=0; i<players_size;i++){
				retObj.players[i] = MyProtocols.get_IOGuoZhanPvpPlayer(myDecoder);
		}
	}
	retObj.my_hp_perc=myDecoder.readInt();
			return retObj;
		}

		static get_GuideStepInfo(myDecoder:WsDecoder):GuideStepInfo{
				var retObj = new GuideStepInfo();
	retObj.module=myDecoder.readInt();
	retObj.step=myDecoder.readInt();
			return retObj;
		}

		static get_1158(myDecoder:WsDecoder):S2CKpStageInfo{
				var retObj = new S2CKpStageInfo();
	retObj.schange=myDecoder.readInt();
	retObj.stage=myDecoder.readInt();
	retObj.star=myDecoder.readInt();
	retObj.hstage=myDecoder.readInt();
	retObj.locate = new Array<string>();
	let locate_size = myDecoder.readInt();
	if(locate_size >0){
		for(var i=0; i<locate_size;i++){
			retObj.locate[i]=myDecoder.readString();
		}
	}
	retObj.promotion = new Array<string>();
	let promotion_size = myDecoder.readInt();
	if(promotion_size >0){
		for(var i=0; i<promotion_size;i++){
			retObj.promotion[i]=myDecoder.readString();
		}
	}
			return retObj;
		}

		static send_C2SListRedPoints(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(127);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2STreasureLevel(senderSocket:net.LejiSocket,p_guid:number,p_treasure_id:number,p_consumes:Array<RewardInfo>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(389);
	wsEncoder.writeLong(p_guid);
	wsEncoder.writeInt(p_treasure_id);
	if(p_consumes == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_consumes.length);
		p_consumes.forEach(function(p_consumes_v){
			wsEncoder.writeInt(p_consumes_v.GSID);
			wsEncoder.writeInt(p_consumes_v.COUNT);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_660(myDecoder:WsDecoder):S2CLegionFactoryMissionStart{
				var retObj = new S2CLegionFactoryMissionStart();
	retObj.stime=myDecoder.readLong();
	retObj.etime=myDecoder.readLong();
			return retObj;
		}

		static send_C2SAffairRefresh(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(333);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGuozhanMove(senderSocket:net.LejiSocket,p_city_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3455);
	wsEncoder.writeInt(p_city_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1278(myDecoder:WsDecoder):S2CGetCxryGeneralList{
				var retObj = new S2CGetCxryGeneralList();
	retObj.generallist = new Array<number>();
	let generallist_size = myDecoder.readInt();
	if(generallist_size >0){
		for(var i=0; i<generallist_size;i++){
			retObj.generallist[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static get_626(myDecoder:WsDecoder):S2CLegionList{
				var retObj = new S2CLegionList();
	retObj.legionlist = new Array<IOLegionInfo>();
	let legionlist_size = myDecoder.readInt();
	if(legionlist_size >0){
		for(var i=0; i<legionlist_size;i++){
				retObj.legionlist[i] = MyProtocols.get_IOLegionInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_402(myDecoder:WsDecoder):S2CSpinList{
				var retObj = new S2CSpinList();
	retObj.type=myDecoder.readInt();
	retObj.is_force=myDecoder.readBool();
	retObj.items = new Array<IOSpinItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOSpinItem(myDecoder);
		}
	}
	retObj.free=myDecoder.readLong();
			return retObj;
		}

		static send_C2STowerAchieveReward(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(523);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IODungeonNodePos(myDecoder:WsDecoder):IODungeonNodePos{
				var retObj = new IODungeonNodePos();
	retObj.type=myDecoder.readInt();
	retObj.choose=myDecoder.readInt();
	retObj.finish=myDecoder.readLong();
			return retObj;
		}

		static send_C2SSecretSoldierRevive(senderSocket:net.LejiSocket,p_hero_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3309);
	wsEncoder.writeInt(p_hero_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOMythicalAnimal(myDecoder:WsDecoder):IOMythicalAnimal{
				var retObj = new IOMythicalAnimal();
	retObj.tid=myDecoder.readInt();
	retObj.class=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.pskill = new Array<number>();
	let pskill_size = myDecoder.readInt();
	if(pskill_size >0){
		for(var i=0; i<pskill_size;i++){
			retObj.pskill[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static get_1266(myDecoder:WsDecoder):S2CMjbgReward{
				var retObj = new S2CMjbgReward();
	retObj.gsid=myDecoder.readInt();
	retObj.count=myDecoder.readInt();
			return retObj;
		}

		static get_522(myDecoder:WsDecoder):S2CTowerBattleEnd{
				var retObj = new S2CTowerBattleEnd();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.items = new Array<IORewardItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.videoid=myDecoder.readLong();
			return retObj;
		}

		static get_IOFriendEntity(myDecoder:WsDecoder):IOFriendEntity{
				var retObj = new IOFriendEntity();
	retObj.id=myDecoder.readInt();
	retObj.rname=myDecoder.readString();
	retObj.iconid=myDecoder.readInt();
	retObj.headid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.vipLevel=myDecoder.readInt();
	retObj.power=myDecoder.readInt();
	retObj.lasttime=myDecoder.readLong();
	let pstatus_exist = myDecoder.readBool();
	if(pstatus_exist == true){
		retObj.pstatus = MyProtocols.get_IOpstatus(myDecoder);
	}
			return retObj;
		}

		static send_C2SLegionTeckLv(senderSocket:net.LejiSocket,p_tech_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(647);
	wsEncoder.writeInt(p_tech_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_602(myDecoder:WsDecoder):S2CChat{
				var retObj = new S2CChat();
			return retObj;
		}

		static get_510(myDecoder:WsDecoder):S2CChapterBattleEnd{
				var retObj = new S2CChapterBattleEnd();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.items = new Array<IORewardItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_712(myDecoder:WsDecoder):S2CMailRedNotice{
				var retObj = new S2CMailRedNotice();
	retObj.ret=myDecoder.readBool();
			return retObj;
		}

		static send_C2SMineRedNotice(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3113);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLevelGiftList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1213);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOOcctask(myDecoder:WsDecoder):IOOcctask{
				var retObj = new IOOcctask();
	retObj.index=myDecoder.readInt();
	retObj.occtype=myDecoder.readInt();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.list = new Array<IOOccTask1>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOOccTask1(myDecoder);
		}
	}
	retObj.reward = new Array<RewardInfo>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	let refcost_exist = myDecoder.readBool();
	if(refcost_exist == true){
		retObj.refcost = MyProtocols.get_RewardInfo(myDecoder);
	}
	let packinfo_exist = myDecoder.readBool();
	if(packinfo_exist == true){
		retObj.packinfo = MyProtocols.get_IOOcctaskPackinfo(myDecoder);
	}
	retObj.prewards = new Array<number>();
	let prewards_size = myDecoder.readInt();
	if(prewards_size >0){
		for(var i=0; i<prewards_size;i++){
			retObj.prewards[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static send_C2SManorBuild(senderSocket:net.LejiSocket,p_pos:number,p_bid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(859);
	wsEncoder.writeInt(p_pos);
	wsEncoder.writeInt(p_bid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_390(myDecoder:WsDecoder):S2CTreasureLevel{
				var retObj = new S2CTreasureLevel();
	retObj.guid=myDecoder.readLong();
	retObj.treasure_id=myDecoder.readInt();
	let general_bean_exist = myDecoder.readBool();
	if(general_bean_exist == true){
		retObj.general_bean = MyProtocols.get_IOGeneralBean(myDecoder);
	}
			return retObj;
		}

		static get_3458(myDecoder:WsDecoder):S2CGuozhanChangeNation{
				var retObj = new S2CGuozhanChangeNation();
	retObj.old_nation=myDecoder.readInt();
	retObj.target_nation=myDecoder.readInt();
	retObj.change_nation_cd=myDecoder.readInt();
			return retObj;
		}

		static get_56(myDecoder:WsDecoder):S2COpenLogin{
				var retObj = new S2COpenLogin();
	retObj.open_id=myDecoder.readString();
	retObj.session_id=myDecoder.readLong();
	retObj.platform_type=myDecoder.readInt();
			return retObj;
		}

		static get_456(myDecoder:WsDecoder):S2CFriendSearch{
				var retObj = new S2CFriendSearch();
	retObj.rname=myDecoder.readString();
	retObj.items = new Array<IOFriendEntity>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOFriendEntity(myDecoder);
		}
	}
			return retObj;
		}

		static get_1154(myDecoder:WsDecoder):S2CKpSearchEnemy{
				var retObj = new S2CKpSearchEnemy();
	retObj.hide=myDecoder.readInt();
	retObj.rid=myDecoder.readInt();
	retObj.star=myDecoder.readInt();
	retObj.stage=myDecoder.readInt();
	retObj.uid=myDecoder.readString();
	retObj.servid=myDecoder.readInt();
	retObj.time=myDecoder.readLong();
	retObj.rname=myDecoder.readString();
	retObj.power=myDecoder.readInt();
	retObj.iconid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.vip=myDecoder.readInt();
	retObj.battleset = new Array<IOBattlesetEnemy>();
	let battleset_size = myDecoder.readInt();
	if(battleset_size >0){
		for(var i=0; i<battleset_size;i++){
				retObj.battleset[i] = MyProtocols.get_IOBattlesetEnemy(myDecoder);
		}
	}
			return retObj;
		}

		static get_706(myDecoder:WsDecoder):S2CSetMailRead{
				var retObj = new S2CSetMailRead();
			return retObj;
		}

		static get_IODjjfMission(myDecoder:WsDecoder):IODjjfMission{
				var retObj = new IODjjfMission();
	retObj.NUM=myDecoder.readInt();
	retObj.cur=myDecoder.readInt();
	retObj.NAME=myDecoder.readString();
	retObj.ITEMS = new Array<RewardInfo>();
	let ITEMS_size = myDecoder.readInt();
	if(ITEMS_size >0){
		for(var i=0; i<ITEMS_size;i++){
				retObj.ITEMS[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.status=myDecoder.readInt();
			return retObj;
		}

		static get_408(myDecoder:WsDecoder):S2CSpinBuy{
				var retObj = new S2CSpinBuy();
			return retObj;
		}

		static get_1072(myDecoder:WsDecoder):S2CWorldBossBattleEnd{
				var retObj = new S2CWorldBossBattleEnd();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOManorBoss(myDecoder:WsDecoder):IOManorBoss{
				var retObj = new IOManorBoss();
	retObj.bossid=myDecoder.readInt();
	retObj.maxhp=myDecoder.readInt();
	retObj.lastdamage=myDecoder.readInt();
	retObj.nowhp=myDecoder.readInt();
	retObj.bset = {};
	let bset_size = myDecoder.readInt();
	if(bset_size >0){
		for(var i=0; i<bset_size;i++){
			let bset_key =myDecoder.readInt();
				retObj.bset[bset_key] = MyProtocols.get_IOGeneralSimple(myDecoder);
		}
	}
			return retObj;
		}

		static get_632(myDecoder:WsDecoder):S2CLegionApply{
				var retObj = new S2CLegionApply();
	retObj.id=myDecoder.readLong();
			return retObj;
		}

		static get_634(myDecoder:WsDecoder):S2CLegionApplyList{
				var retObj = new S2CLegionApplyList();
	retObj.applylist = new Array<IOLegionMember>();
	let applylist_size = myDecoder.readInt();
	if(applylist_size >0){
		for(var i=0; i<applylist_size;i++){
				retObj.applylist[i] = MyProtocols.get_IOLegionMember(myDecoder);
		}
	}
			return retObj;
		}

		static get_530(myDecoder:WsDecoder):S2CExpedBattleEnd{
				var retObj = new S2CExpedBattleEnd();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.drop = new Array<IORewardItem>();
	let drop_size = myDecoder.readInt();
	if(drop_size >0){
		for(var i=0; i<drop_size;i++){
				retObj.drop[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.reward = new Array<IORewardItemSelect>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItemSelect(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SExpedStatue(senderSocket:net.LejiSocket,p_type:number,p_general_uuid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(531);
	wsEncoder.writeInt(p_type);
	wsEncoder.writeLong(p_general_uuid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_454(myDecoder:WsDecoder):S2CFriendRecommandList{
				var retObj = new S2CFriendRecommandList();
	retObj.items = new Array<IOFriendEntity>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOFriendEntity(myDecoder);
		}
	}
			return retObj;
		}

		static get_388(myDecoder:WsDecoder):S2CTreasureSell{
				var retObj = new S2CTreasureSell();
	retObj.treasure_id=myDecoder.readInt();
	retObj.count=myDecoder.readInt();
			return retObj;
		}

		static get_1062(myDecoder:WsDecoder):S2CGetFbossResult{
				var retObj = new S2CGetFbossResult();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.bosshurm=myDecoder.readLong();
	retObj.kill=myDecoder.readInt();
			return retObj;
		}

		static get_1216(myDecoder:WsDecoder):S2CStarGiftList{
				var retObj = new S2CStarGiftList();
	retObj.ret_list = new Array<IOStarGift>();
	let ret_list_size = myDecoder.readInt();
	if(ret_list_size >0){
		for(var i=0; i<ret_list_size;i++){
				retObj.ret_list[i] = MyProtocols.get_IOStarGift(myDecoder);
		}
	}
			return retObj;
		}

		static get_804(myDecoder:WsDecoder):S2CGetSignAward{
				var retObj = new S2CGetSignAward();
	retObj.awards = new Array<AwardItem>();
	let awards_size = myDecoder.readInt();
	if(awards_size >0){
		for(var i=0; i<awards_size;i++){
				retObj.awards[i] = MyProtocols.get_AwardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOLevelGift(myDecoder:WsDecoder):IOLevelGift{
				var retObj = new IOLevelGift();
	retObj.level=myDecoder.readInt();
	retObj.price=myDecoder.readInt();
	retObj.end=myDecoder.readLong();
	retObj.buytime=myDecoder.readInt();
	retObj.items = new Array<RewardInfo>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SAffairAcce(senderSocket:net.LejiSocket,p_affair_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(341);
	wsEncoder.writeInt(p_affair_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLegionFactoryMissionFinish(senderSocket:net.LejiSocket,p_key:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(661);
	wsEncoder.writeLong(p_key);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_352(myDecoder:WsDecoder):S2CHeroList{
				var retObj = new S2CHeroList();
	retObj.generals = new Array<IOGeneralBean>();
	let generals_size = myDecoder.readInt();
	if(generals_size >0){
		for(var i=0; i<generals_size;i++){
				retObj.generals[i] = MyProtocols.get_IOGeneralBean(myDecoder);
		}
	}
			return retObj;
		}

		static get_664(myDecoder:WsDecoder):S2CLegionLog{
				var retObj = new S2CLegionLog();
	retObj.list = new Array<IOLegionLog>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOLegionLog(myDecoder);
		}
	}
			return retObj;
		}

		static get_1246(myDecoder:WsDecoder):S2CTnqwBossSweep{
				var retObj = new S2CTnqwBossSweep();
	retObj.nowhp=myDecoder.readLong();
	retObj.reward = new Array<RewardInfo>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOJfbxEvent(myDecoder:WsDecoder):IOJfbxEvent{
				var retObj = new IOJfbxEvent();
	retObj.MARK=myDecoder.readInt();
	retObj.LIMIT=myDecoder.readInt();
	retObj.intro=myDecoder.readString();
			return retObj;
		}

		static get_370(myDecoder:WsDecoder):S2CExclusiveLevelUp{
				var retObj = new S2CExclusiveLevelUp();
	retObj.general_uuid=myDecoder.readLong();
	let general_bean_exist = myDecoder.readBool();
	if(general_bean_exist == true){
		retObj.general_bean = MyProtocols.get_IOGeneralBean(myDecoder);
	}
			return retObj;
		}

		static send_C2SOfficialItemReward(senderSocket:net.LejiSocket,p_official_id:number,p_item_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(853);
	wsEncoder.writeInt(p_official_id);
	wsEncoder.writeInt(p_item_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SOfflineAwardDouble(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(149);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOManorFriend(myDecoder:WsDecoder):IOManorFriend{
				var retObj = new IOManorFriend();
	retObj.icon=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.headid=myDecoder.readInt();
	retObj.name=myDecoder.readString();
	retObj.id=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.power=myDecoder.readInt();
			return retObj;
		}

		static get_656(myDecoder:WsDecoder):S2CLegionFactoryMissionList{
				var retObj = new S2CLegionFactoryMissionList();
	retObj.time=myDecoder.readLong();
	retObj.list = new Array<IOLegionFactoryMission>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOLegionFactoryMission(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOLegionLog(myDecoder:WsDecoder):IOLegionLog{
				var retObj = new IOLegionLog();
	retObj.params = new Array<string>();
	let params_size = myDecoder.readInt();
	if(params_size >0){
		for(var i=0; i<params_size;i++){
			retObj.params[i]=myDecoder.readString();
		}
	}
	retObj.event=myDecoder.readString();
	retObj.create=myDecoder.readLong();
			return retObj;
		}

		static send_C2SSecretView(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3301);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_670(myDecoder:WsDecoder):S2CWorldBossInfo{
				var retObj = new S2CWorldBossInfo();
	let legion_exist = myDecoder.readBool();
	if(legion_exist == true){
		retObj.legion = MyProtocols.get_IOWorldBossLegion(myDecoder);
	}
	let boss_exist = myDecoder.readBool();
	if(boss_exist == true){
		retObj.boss = MyProtocols.get_IOWorldBossInfo(myDecoder);
	}
	let self_exist = myDecoder.readBool();
	if(self_exist == true){
		retObj.self = MyProtocols.get_IOWorldBossSelf(myDecoder);
	}
	retObj.rank = new Array<IOWorldBossRank>();
	let rank_size = myDecoder.readInt();
	if(rank_size >0){
		for(var i=0; i<rank_size;i++){
				retObj.rank[i] = MyProtocols.get_IOWorldBossRank(myDecoder);
		}
	}
	retObj.worldrank = new Array<IOWorldBossWorldRank>();
	let worldrank_size = myDecoder.readInt();
	if(worldrank_size >0){
		for(var i=0; i<worldrank_size;i++){
				retObj.worldrank[i] = MyProtocols.get_IOWorldBossWorldRank(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SActivitiesGetAward(senderSocket:net.LejiSocket,p_activeIdType:number,p_item_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1203);
	wsEncoder.writeInt(p_activeIdType);
	wsEncoder.writeInt(p_item_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SFriendBattleStart(senderSocket:net.LejiSocket,p_friendid:number,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1065);
	wsEncoder.writeInt(p_friendid);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SChat(senderSocket:net.LejiSocket,p_msgtype:number,p_content:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(601);
	wsEncoder.writeInt(p_msgtype);
	wsEncoder.writeString(p_content);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2STgslBossEnd(senderSocket:net.LejiSocket,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1079);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SKpStageInfo(senderSocket:net.LejiSocket,p_uid:number,p_servid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1157);
	wsEncoder.writeInt(p_uid);
	wsEncoder.writeInt(p_servid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IoLotteryWheelConfig(myDecoder:WsDecoder):IoLotteryWheelConfig{
				var retObj = new IoLotteryWheelConfig();
	retObj.my_free_count=myDecoder.readInt();
	retObj.my_pay_count=myDecoder.readInt();
	retObj.daily_free_count=myDecoder.readInt();
	retObj.pay_count=myDecoder.readInt();
	retObj.pay_price=myDecoder.readInt();
	retObj.reward = new Array<ActivitiesItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_ActivitiesItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_864(myDecoder:WsDecoder):S2CManorReward{
				var retObj = new S2CManorReward();
	retObj.items = new Array<IORewardItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SDungeonChooseNode(senderSocket:net.LejiSocket,p_chapter:number,p_node:number,p_pos:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1111);
	wsEncoder.writeInt(p_chapter);
	wsEncoder.writeInt(p_node);
	wsEncoder.writeInt(p_pos);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_526(myDecoder:WsDecoder):S2CExpedBattleInfo{
				var retObj = new S2CExpedBattleInfo();
	retObj.mapkey=myDecoder.readLong();
	retObj.hp = {};
	let hp_size = myDecoder.readInt();
	if(hp_size >0){
		for(var i=0; i<hp_size;i++){
			let hp_key =myDecoder.readLong();
			retObj.hp[hp_key]=myDecoder.readInt();
		}
	}
	retObj.wish = new Array<number>();
	let wish_size = myDecoder.readInt();
	if(wish_size >0){
		for(var i=0; i<wish_size;i++){
			retObj.wish[i]=myDecoder.readInt();
		}
	}
	let map_exist = myDecoder.readBool();
	if(map_exist == true){
		retObj.map = MyProtocols.get_IOExpedPlayer(myDecoder);
	}
			return retObj;
		}

		static send_C2SYunbeeLogin(senderSocket:net.LejiSocket,p_user_id:string,p_token:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10047);
	wsEncoder.writeString(p_user_id);
	wsEncoder.writeString(p_token);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2STowerBattleInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(517);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_344(myDecoder:WsDecoder):S2CAffairAward{
				var retObj = new S2CAffairAward();
	retObj.affair_index=myDecoder.readInt();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.item_list = new Array<IOAffairItem>();
	let item_list_size = myDecoder.readInt();
	if(item_list_size >0){
		for(var i=0; i<item_list_size;i++){
				retObj.item_list[i] = MyProtocols.get_IOAffairItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_3464(myDecoder:WsDecoder):S2CGuozhanFightView{
				var retObj = new S2CGuozhanFightView();
	retObj.city_list = new Array<IOGuoZhanCity>();
	let city_list_size = myDecoder.readInt();
	if(city_list_size >0){
		for(var i=0; i<city_list_size;i++){
				retObj.city_list[i] = MyProtocols.get_IOGuoZhanCity(myDecoder);
		}
	}
	retObj.my_city_index=myDecoder.readInt();
	retObj.move_step=myDecoder.readInt();
	retObj.nation_city_count = new Array<number>();
	let nation_city_count_size = myDecoder.readInt();
	if(nation_city_count_size >0){
		for(var i=0; i<nation_city_count_size;i++){
			retObj.nation_city_count[i]=myDecoder.readInt();
		}
	}
	retObj.hp_perc=myDecoder.readInt();
	retObj.my_nation=myDecoder.readInt();
	retObj.change_nation_cd=myDecoder.readInt();
	retObj.my_office=myDecoder.readInt();
			return retObj;
		}

		static get_IOGeneralBean(myDecoder:WsDecoder):IOGeneralBean{
				var retObj = new IOGeneralBean();
	retObj.guid=myDecoder.readLong();
	retObj.gsid=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.star=myDecoder.readInt();
	retObj.camp=myDecoder.readInt();
	retObj.occu=myDecoder.readInt();
	retObj.class=myDecoder.readInt();
	retObj.power=myDecoder.readInt();
	retObj.talent = new Array<number>();
	let talent_size = myDecoder.readInt();
	if(talent_size >0){
		for(var i=0; i<talent_size;i++){
			retObj.talent[i]=myDecoder.readInt();
		}
	}
	retObj.affairs=myDecoder.readInt();
	retObj.treasure=myDecoder.readInt();
	let property_exist = myDecoder.readBool();
	if(property_exist == true){
		retObj.property = MyProtocols.get_IOProperty(myDecoder);
	}
	retObj.equip = new Array<number>();
	let equip_size = myDecoder.readInt();
	if(equip_size >0){
		for(var i=0; i<equip_size;i++){
			retObj.equip[i]=myDecoder.readInt();
		}
	}
	retObj.skill = new Array<number>();
	let skill_size = myDecoder.readInt();
	if(skill_size >0){
		for(var i=0; i<skill_size;i++){
			retObj.skill[i]=myDecoder.readInt();
		}
	}
	let exclusive_exist = myDecoder.readBool();
	if(exclusive_exist == true){
		retObj.exclusive = MyProtocols.get_IOExclusive(myDecoder);
	}
	retObj.hppercent=myDecoder.readInt();
			return retObj;
		}

		static send_C2SAddDesktopShortcutAward(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(165);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_3304(myDecoder:WsDecoder):S2CSecretBattleStart{
				var retObj = new S2CSecretBattleStart();
	retObj.map_id=myDecoder.readInt();
	retObj.online_formation = new Array<IOSecretHero>();
	let online_formation_size = myDecoder.readInt();
	if(online_formation_size >0){
		for(var i=0; i<online_formation_size;i++){
				retObj.online_formation[i] = MyProtocols.get_IOSecretHero(myDecoder);
		}
	}
	retObj.is_reset=myDecoder.readInt();
			return retObj;
		}

		static send_C2SGeneralAddClass(senderSocket:net.LejiSocket,p_general_uuid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(359);
	wsEncoder.writeLong(p_general_uuid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_862(myDecoder:WsDecoder):S2CManorReset{
				var retObj = new S2CManorReset();
	retObj.items = new Array<IORewardItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SFriendshipReceive(senderSocket:net.LejiSocket,p_role_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(467);
	wsEncoder.writeInt(p_role_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGetKpMissionAward(senderSocket:net.LejiSocket,p_mission_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1151);
	wsEncoder.writeInt(p_mission_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SgNetTopLogin(senderSocket:net.LejiSocket,p_userId:string,p_token:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10045);
	wsEncoder.writeString(p_userId);
	wsEncoder.writeString(p_token);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SDungeonChooseBuf(senderSocket:net.LejiSocket,p_chapter:number,p_node:number,p_pos:number,p_buff:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1115);
	wsEncoder.writeInt(p_chapter);
	wsEncoder.writeInt(p_node);
	wsEncoder.writeInt(p_pos);
	wsEncoder.writeInt(p_buff);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SItemSell(senderSocket:net.LejiSocket,p_gsid:number,p_count:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(305);
	wsEncoder.writeInt(p_gsid);
	wsEncoder.writeInt(p_count);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SBigBattleFarm(senderSocket:net.LejiSocket,p_mapid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(577);
	wsEncoder.writeInt(p_mapid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_568(myDecoder:WsDecoder):S2COccTaskPackRefresh{
				var retObj = new S2COccTaskPackRefresh();
	let refcost_exist = myDecoder.readBool();
	if(refcost_exist == true){
		retObj.refcost = MyProtocols.get_RewardInfo(myDecoder);
	}
	let packinfo_exist = myDecoder.readBool();
	if(packinfo_exist == true){
		retObj.packinfo = MyProtocols.get_IOOcctaskPackinfo(myDecoder);
	}
			return retObj;
		}

		static send_C2SAffairStart(senderSocket:net.LejiSocket,p_affair_index:number,p_arr:Array<number>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(339);
	wsEncoder.writeInt(p_affair_index);
	if(p_arr == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_arr.length);
		p_arr.forEach(function(p_arr_v){
	wsEncoder.writeLong(p_arr_v);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_872(myDecoder:WsDecoder):S2CManorMop{
				var retObj = new S2CManorMop();
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.nowhp=myDecoder.readInt();
			return retObj;
		}

		static get_1222(myDecoder:WsDecoder):S2CZhdTime{
				var retObj = new S2CZhdTime();
	retObj.ret = {};
	let ret_size = myDecoder.readInt();
	if(ret_size >0){
		for(var i=0; i<ret_size;i++){
			let ret_key =myDecoder.readString();
			retObj.ret[ret_key] = new Array<number>();
	let retret_key_size = myDecoder.readInt();
	if(retret_key_size >0){
		for(var ret_idx=0; ret_idx<retret_key_size;ret_idx++){
			retObj.ret[ret_key][ret_idx]=myDecoder.readLong();
		}
	}
		}
	}
			return retObj;
		}

		static get_1236(myDecoder:WsDecoder):S2CGxdbInfo{
				var retObj = new S2CGxdbInfo();
	retObj.looplimit=myDecoder.readInt();
	retObj.currentloop=myDecoder.readInt();
	retObj.missions = new Array<IODjjfMission>();
	let missions_size = myDecoder.readInt();
	if(missions_size >0){
		for(var i=0; i<missions_size;i++){
				retObj.missions[i] = MyProtocols.get_IODjjfMission(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SRankView(senderSocket:net.LejiSocket,p_rtype:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(143);
	wsEncoder.writeString(p_rtype);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_548(myDecoder:WsDecoder):S2CPvpRecords{
				var retObj = new S2CPvpRecords();
	retObj.records = new Array<IOPvpRecord>();
	let records_size = myDecoder.readInt();
	if(records_size >0){
		for(var i=0; i<records_size;i++){
				retObj.records[i] = MyProtocols.get_IOPvpRecord(myDecoder);
		}
	}
			return retObj;
		}

		static get_IODungeonBset(myDecoder:WsDecoder):IODungeonBset{
				var retObj = new IODungeonBset();
	retObj.gsid=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.class=myDecoder.readInt();
	retObj.exhp=myDecoder.readFloat();
	retObj.exatk=myDecoder.readFloat();
			return retObj;
		}

		static get_FixedActivityInfo(myDecoder:WsDecoder):FixedActivityInfo{
				var retObj = new FixedActivityInfo();
	retObj.id=myDecoder.readInt();
	retObj.level_index=myDecoder.readInt();
	retObj.progress=myDecoder.readInt();
	retObj.can_get_award=myDecoder.readBool();
			return retObj;
		}

		static get_1086(myDecoder:WsDecoder):S2CMineBattleEnd{
				var retObj = new S2CMineBattleEnd();
	retObj.level_index=myDecoder.readInt();
	retObj.point_index=myDecoder.readInt();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_1210(myDecoder:WsDecoder):S2CQiZhenYiBaoJoin{
				var retObj = new S2CQiZhenYiBaoJoin();
			return retObj;
		}

		static get_IODungeonList(myDecoder:WsDecoder):IODungeonList{
				var retObj = new IODungeonList();
	retObj.isget=myDecoder.readInt();
			return retObj;
		}

		static get_380(myDecoder:WsDecoder):S2CGeneralExchangeGet{
				var retObj = new S2CGeneralExchangeGet();
	retObj.guid=myDecoder.readLong();
	retObj.gsid=myDecoder.readInt();
			return retObj;
		}

		static get_624(myDecoder:WsDecoder):S2CLegionSet{
				var retObj = new S2CLegionSet();
			return retObj;
		}

		static send_C2SManorFriendList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(879);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOOccTask1(myDecoder:WsDecoder):IOOccTask1{
				var retObj = new IOOccTask1();
	retObj.status=myDecoder.readInt();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.intro=myDecoder.readString();
	retObj.mark=myDecoder.readInt();
	retObj.limit=myDecoder.readInt();
	retObj.page=myDecoder.readString();
	retObj.num=myDecoder.readInt();
			return retObj;
		}

		static send_C2SAffairRedNotice(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(345);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_138(myDecoder:WsDecoder):S2CGetQunheiWxShareAward{
				var retObj = new S2CGetQunheiWxShareAward();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SMineBattleStart(senderSocket:net.LejiSocket,p_mythic:number,p_items:Array<IOFormationGeneralPos>,p_level_index:number,p_point_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1083);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
	wsEncoder.writeInt(p_level_index);
	wsEncoder.writeInt(p_point_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_578(myDecoder:WsDecoder):S2CBigBattleFarm{
				var retObj = new S2CBigBattleFarm();
	retObj.times=myDecoder.readInt();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SExpedBattleEnd(senderSocket:net.LejiSocket,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(529);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SWanBaGetBalance(senderSocket:net.LejiSocket,p_appid:string,p_openid:string,p_openkey:string,p_pf:string,p_os_platfrom:number,p_goodsId:number,p_goodsName:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(161);
	wsEncoder.writeString(p_appid);
	wsEncoder.writeString(p_openid);
	wsEncoder.writeString(p_openkey);
	wsEncoder.writeString(p_pf);
	wsEncoder.writeInt(p_os_platfrom);
	wsEncoder.writeInt(p_goodsId);
	wsEncoder.writeString(p_goodsName);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLegionTechReset(senderSocket:net.LejiSocket,p_occu:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(649);
	wsEncoder.writeInt(p_occu);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_372(myDecoder:WsDecoder):S2CExclusiveRefreshGet{
				var retObj = new S2CExclusiveRefreshGet();
	retObj.general_uuid=myDecoder.readLong();
	retObj.has_pending_property=myDecoder.readBool();
	retObj.skill = new Array<number>();
	let skill_size = myDecoder.readInt();
	if(skill_size >0){
		for(var i=0; i<skill_size;i++){
			retObj.skill[i]=myDecoder.readInt();
		}
	}
	retObj.property = new Array<KvStringPair>();
	let property_size = myDecoder.readInt();
	if(property_size >0){
		for(var i=0; i<property_size;i++){
				retObj.property[i] = MyProtocols.get_KvStringPair(myDecoder);
		}
	}
			return retObj;
		}

		static get_1012(myDecoder:WsDecoder):S2CGuideEnd{
				var retObj = new S2CGuideEnd();
			return retObj;
		}

		static get_570(myDecoder:WsDecoder):S2COccTaskPackRPay{
				var retObj = new S2COccTaskPackRPay();
			return retObj;
		}

		static get_IOStarGift(myDecoder:WsDecoder):IOStarGift{
				var retObj = new IOStarGift();
	retObj.gstar=myDecoder.readInt();
	retObj.price=myDecoder.readInt();
	retObj.end=myDecoder.readLong();
	retObj.buytime=myDecoder.readInt();
	retObj.items = new Array<RewardInfo>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_ActivitiesItem(myDecoder:WsDecoder):ActivitiesItem{
				var retObj = new ActivitiesItem();
	retObj.nID=myDecoder.readInt();
	retObj.nNum=myDecoder.readInt();
			return retObj;
		}

		static send_C2SMineBattleEnd(senderSocket:net.LejiSocket,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1085);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1274(myDecoder:WsDecoder):S2CXsdhDh{
				var retObj = new S2CXsdhDh();
			return retObj;
		}

		static send_C2SScrollAnno(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(195);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SWanBaPay(senderSocket:net.LejiSocket,p_appid:string,p_openid:string,p_openkey:string,p_pf:string,p_os_platfrom:number,p_itemid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(163);
	wsEncoder.writeString(p_appid);
	wsEncoder.writeString(p_openid);
	wsEncoder.writeString(p_openkey);
	wsEncoder.writeString(p_pf);
	wsEncoder.writeInt(p_os_platfrom);
	wsEncoder.writeInt(p_itemid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SMjbgNext(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1267);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1260(myDecoder:WsDecoder):S2CPayCjxg1{
				var retObj = new S2CPayCjxg1();
			return retObj;
		}

		static get_184(myDecoder:WsDecoder):S2CQedjAward{
				var retObj = new S2CQedjAward();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SLegionAppoint(senderSocket:net.LejiSocket,p_rid:number,p_type:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(639);
	wsEncoder.writeInt(p_rid);
	wsEncoder.writeInt(p_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_586(myDecoder:WsDecoder):S2CMonthBossFarm{
				var retObj = new S2CMonthBossFarm();
	retObj.items = new Array<IORewardItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SGetCjxg2(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1223);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOBattleRecordInfo(myDecoder:WsDecoder):IOBattleRecordInfo{
				var retObj = new IOBattleRecordInfo();
	retObj.rname=myDecoder.readString();
	retObj.level=myDecoder.readInt();
	retObj.iconid=myDecoder.readInt();
	retObj.headid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
			return retObj;
		}

		static get_464(myDecoder:WsDecoder):S2CFriendApplyHandle{
				var retObj = new S2CFriendApplyHandle();
	retObj.is_agree=myDecoder.readBool();
	retObj.role_ids = new Array<number>();
	let role_ids_size = myDecoder.readInt();
	if(role_ids_size >0){
		for(var i=0; i<role_ids_size;i++){
			retObj.role_ids[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static send_C2SGetCjxg1(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1237);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_870(myDecoder:WsDecoder):S2CManorPatrol{
				var retObj = new S2CManorPatrol();
			return retObj;
		}

		static get_WanbaLoginGift(myDecoder:WsDecoder):WanbaLoginGift{
				var retObj = new WanbaLoginGift();
	retObj.wanba_gift_id=myDecoder.readInt();
	retObj.appid=myDecoder.readString();
	retObj.openid=myDecoder.readString();
	retObj.openkey=myDecoder.readString();
	retObj.pf=myDecoder.readString();
			return retObj;
		}

		static get_3452(myDecoder:WsDecoder):S2CGuozhanView{
				var retObj = new S2CGuozhanView();
	retObj.pass_city_index = new Array<number>();
	let pass_city_index_size = myDecoder.readInt();
	if(pass_city_index_size >0){
		for(var i=0; i<pass_city_index_size;i++){
			retObj.pass_city_index[i]=myDecoder.readInt();
		}
	}
	retObj.player_city_index=myDecoder.readInt();
	retObj.my_nation=myDecoder.readInt();
	retObj.change_nation_cd=myDecoder.readInt();
	retObj.my_office=myDecoder.readInt();
			return retObj;
		}

		static get_338(myDecoder:WsDecoder):S2CAffairLock{
				var retObj = new S2CAffairLock();
	retObj.affair_index=myDecoder.readInt();
	retObj.item_list = new Array<IOAffairItem>();
	let item_list_size = myDecoder.readInt();
	if(item_list_size >0){
		for(var i=0; i<item_list_size;i++){
				retObj.item_list[i] = MyProtocols.get_IOAffairItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOLegionApplyReview(myDecoder:WsDecoder):IOLegionApplyReview{
				var retObj = new IOLegionApplyReview();
	retObj.error=myDecoder.readString();
			return retObj;
		}

		static get_520(myDecoder:WsDecoder):S2CTowerBattleStart{
				var retObj = new S2CTowerBattleStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static get_IOCxryZf(myDecoder:WsDecoder):IOCxryZf{
				var retObj = new IOCxryZf();
	retObj.cur=myDecoder.readInt();
	retObj.max=myDecoder.readInt();
	retObj.prob=myDecoder.readInt();
			return retObj;
		}

		static get_IOJfbxBox(myDecoder:WsDecoder):IOJfbxBox{
				var retObj = new IOJfbxBox();
	retObj.SCORE=myDecoder.readInt();
	retObj.state=myDecoder.readInt();
	retObj.REWARD = new Array<RewardInfo>();
	let REWARD_size = myDecoder.readInt();
	if(REWARD_size >0){
		for(var i=0; i<REWARD_size;i++){
				retObj.REWARD[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_866(myDecoder:WsDecoder):S2CManorFieldInfo{
				var retObj = new S2CManorFieldInfo();
	retObj.etime=myDecoder.readLong();
	retObj.btime=myDecoder.readLong();
	retObj.items = new Array<IORewardItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.mop=myDecoder.readInt();
	retObj.blv=myDecoder.readInt();
	retObj.elv=myDecoder.readInt();
	let boss_exist = myDecoder.readBool();
	if(boss_exist == true){
		retObj.boss = MyProtocols.get_IOManorFieldBoss(myDecoder);
	}
	retObj.enemy = new Array<IOManorFieldEnemy>();
	let enemy_size = myDecoder.readInt();
	if(enemy_size >0){
		for(var i=0; i<enemy_size;i++){
				retObj.enemy[i] = MyProtocols.get_IOManorFieldEnemy(myDecoder);
		}
	}
			return retObj;
		}

		static get_1156(myDecoder:WsDecoder):S2CKpSwitchOrder{
				var retObj = new S2CKpSwitchOrder();
			return retObj;
		}

		static send_C2SChatNewmsg(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(609);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SMissionAchieveAward(senderSocket:net.LejiSocket,p_mission_type:number,p_mission_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(553);
	wsEncoder.writeInt(p_mission_type);
	wsEncoder.writeInt(p_mission_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SManorYijianFarm(senderSocket:net.LejiSocket,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(539);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_668(myDecoder:WsDecoder):S2CLegionBossFarm{
				var retObj = new S2CLegionBossFarm();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOSeason(myDecoder:WsDecoder):IOSeason{
				var retObj = new IOSeason();
	retObj.etime=myDecoder.readLong();
	retObj.year=myDecoder.readInt();
	retObj.season=myDecoder.readInt();
	retObj.pos = new Array<number>();
	let pos_size = myDecoder.readInt();
	if(pos_size >0){
		for(var i=0; i<pos_size;i++){
			retObj.pos[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static get_IOFormationGeneralPos(myDecoder:WsDecoder):IOFormationGeneralPos{
				var retObj = new IOFormationGeneralPos();
	retObj.pos=myDecoder.readInt();
	retObj.general_uuid=myDecoder.readLong();
			return retObj;
		}

		static get_198(myDecoder:WsDecoder):S2CBattleSeason{
				var retObj = new S2CBattleSeason();
	retObj.vals = new Array<number>();
	let vals_size = myDecoder.readInt();
	if(vals_size >0){
		for(var i=0; i<vals_size;i++){
			retObj.vals[i]=myDecoder.readInt();
		}
	}
	let info_exist = myDecoder.readBool();
	if(info_exist == true){
		retObj.info = MyProtocols.get_IOSeason(myDecoder);
	}
			return retObj;
		}

		static get_584(myDecoder:WsDecoder):S2CMonthBossBattleEnd{
				var retObj = new S2CMonthBossBattleEnd();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.items = new Array<IORewardItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.damge=myDecoder.readLong();
	retObj.kill=myDecoder.readInt();
			return retObj;
		}

		static send_C2SMonthBossFarm(senderSocket:net.LejiSocket,p_monthIndex:number,p_times:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(585);
	wsEncoder.writeInt(p_monthIndex);
	wsEncoder.writeInt(p_times);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SSrenderPersua(senderSocket:net.LejiSocket,p_gsid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(875);
	wsEncoder.writeInt(p_gsid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SAffairReel(senderSocket:net.LejiSocket,p_scroll_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(335);
	wsEncoder.writeInt(p_scroll_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGjCzlbList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1227);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGuozhanBattleView(senderSocket:net.LejiSocket,p_city_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3453);
	wsEncoder.writeInt(p_city_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_114(myDecoder:WsDecoder):PushPropChange{
				var retObj = new PushPropChange();
	retObj.gsid=myDecoder.readInt();
	retObj.count=myDecoder.readLong();
			return retObj;
		}

		static send_C2SDjrwInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1253);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGuideOne(senderSocket:net.LejiSocket,p_akey:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1005);
	wsEncoder.writeInt(p_akey);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1220(myDecoder:WsDecoder):S2CMylbList{
				var retObj = new S2CMylbList();
	retObj.end=myDecoder.readLong();
	retObj.items = new Array<IOLiBao1>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOLiBao1(myDecoder);
		}
	}
			return retObj;
		}

		static get_538(myDecoder:WsDecoder):S2CManorBattleEnd{
				var retObj = new S2CManorBattleEnd();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.kill=myDecoder.readInt();
			return retObj;
		}

		static get_IOBHurt(myDecoder:WsDecoder):IOBHurt{
				var retObj = new IOBHurt();
	retObj.gsid=myDecoder.readInt();
	retObj.hurm=myDecoder.readLong();
	retObj.heal=myDecoder.readLong();
	retObj.hp=myDecoder.readLong();
	retObj.born=myDecoder.readInt();
	retObj.hpperc=myDecoder.readInt();
	retObj.hpmax=myDecoder.readLong();
			return retObj;
		}

		static send_C2SGetNoviceTrainAward(senderSocket:net.LejiSocket,p_exid:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(555);
	wsEncoder.writeString(p_exid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLegionSearch(senderSocket:net.LejiSocket,p_name:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(627);
	wsEncoder.writeString(p_name);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLegionFactoryDonateGet(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(653);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IoActivityHeroLiBao(myDecoder:WsDecoder):IoActivityHeroLiBao{
				var retObj = new IoActivityHeroLiBao();
	retObj.buy_count=myDecoder.readInt();
	retObj.buy_count_total=myDecoder.readInt();
	retObj.favor_rate=myDecoder.readInt();
	retObj.price=myDecoder.readInt();
	retObj.recharge_id=myDecoder.readInt();
	retObj.extra_diamond=myDecoder.readInt();
	retObj.reward = new Array<ActivitiesItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_ActivitiesItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_452(myDecoder:WsDecoder):S2CFriendList{
				var retObj = new S2CFriendList();
	retObj.maxcount=myDecoder.readInt();
	retObj.surplusgiftmax=myDecoder.readInt();
	retObj.arrfriend = new Array<IOFriendEntity>();
	let arrfriend_size = myDecoder.readInt();
	if(arrfriend_size >0){
		for(var i=0; i<arrfriend_size;i++){
				retObj.arrfriend[i] = MyProtocols.get_IOFriendEntity(myDecoder);
		}
	}
			return retObj;
		}

		static get_462(myDecoder:WsDecoder):S2CFriendApplyList{
				var retObj = new S2CFriendApplyList();
	retObj.items = new Array<IOFriendEntity>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOFriendEntity(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SGetSignAward(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(803);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2STreasureSell(senderSocket:net.LejiSocket,p_treasure_id:number,p_count:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(387);
	wsEncoder.writeInt(p_treasure_id);
	wsEncoder.writeInt(p_count);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SItemExchange(senderSocket:net.LejiSocket,p_EXID:string,p_count:number,p_other:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(313);
	wsEncoder.writeString(p_EXID);
	wsEncoder.writeInt(p_count);
	wsEncoder.writeInt(p_other);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_468(myDecoder:WsDecoder):S2CFriendshipReceive{
				var retObj = new S2CFriendshipReceive();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOWorldBossInfo(myDecoder:WsDecoder):IOWorldBossInfo{
				var retObj = new IOWorldBossInfo();
	retObj.id=myDecoder.readInt();
	retObj.endtime=myDecoder.readLong();
	retObj.lastdamage=myDecoder.readLong();
	retObj.bset = {};
	let bset_size = myDecoder.readInt();
	if(bset_size >0){
		for(var i=0; i<bset_size;i++){
			let bset_key =myDecoder.readInt();
				retObj.bset[bset_key] = MyProtocols.get_IOGeneralLegion(myDecoder);
		}
	}
	retObj.hasgift=myDecoder.readInt();
			return retObj;
		}

		static get_QiZhenYiBaoPlayer(myDecoder:WsDecoder):QiZhenYiBaoPlayer{
				var retObj = new QiZhenYiBaoPlayer();
	retObj.player_id=myDecoder.readInt();
	retObj.player_name=myDecoder.readString();
	retObj.count=myDecoder.readInt();
			return retObj;
		}

		static get_1018(myDecoder:WsDecoder):PushScrollAnno{
				var retObj = new PushScrollAnno();
	retObj.msg_id=myDecoder.readInt();
	retObj.params = new Array<string>();
	let params_size = myDecoder.readInt();
	if(params_size >0){
		for(var i=0; i<params_size;i++){
			retObj.params[i]=myDecoder.readString();
		}
	}
			return retObj;
		}

		static send_C2SGuoZhanCityMove(senderSocket:net.LejiSocket,p_city_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3467);
	wsEncoder.writeInt(p_city_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_3470(myDecoder:WsDecoder):PushGuoZhanPass{
				var retObj = new PushGuoZhanPass();
	retObj.nation_id=myDecoder.readInt();
			return retObj;
		}

		static send_C2SKeyConvert(senderSocket:net.LejiSocket,p_key:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(809);
	wsEncoder.writeString(p_key);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1080(myDecoder:WsDecoder):S2CTgslBossEnd{
				var retObj = new S2CTgslBossEnd();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.damge=myDecoder.readLong();
	retObj.kill=myDecoder.readInt();
			return retObj;
		}

		static get_1206(myDecoder:WsDecoder):PushActivityProgressUpdate{
				var retObj = new PushActivityProgressUpdate();
	retObj.activeIdType=myDecoder.readInt();
	retObj.progress=myDecoder.readInt();
			return retObj;
		}

		static get_3306(myDecoder:WsDecoder):S2CSecretBattleEnd{
				var retObj = new S2CSecretBattleEnd();
	retObj.is_win=myDecoder.readBool();
	retObj.reward_items = new Array<SecretItemInfo>();
	let reward_items_size = myDecoder.readInt();
	if(reward_items_size >0){
		for(var i=0; i<reward_items_size;i++){
				retObj.reward_items[i] = MyProtocols.get_SecretItemInfo(myDecoder);
		}
	}
	retObj.is_interrupt=myDecoder.readBool();
	retObj.map_id=myDecoder.readInt();
	retObj.progress=myDecoder.readInt();
	retObj.boxAward = new Array<IOSecretBoxAward>();
	let boxAward_size = myDecoder.readInt();
	if(boxAward_size >0){
		for(var i=0; i<boxAward_size;i++){
				retObj.boxAward[i] = MyProtocols.get_IOSecretBoxAward(myDecoder);
		}
	}
	retObj.my_cost = new Array<IOSecretHero>();
	let my_cost_size = myDecoder.readInt();
	if(my_cost_size >0){
		for(var i=0; i<my_cost_size;i++){
				retObj.my_cost[i] = MyProtocols.get_IOSecretHero(myDecoder);
		}
	}
	retObj.enemy_cost = new Array<IOSecretHero>();
	let enemy_cost_size = myDecoder.readInt();
	if(enemy_cost_size >0){
		for(var i=0; i<enemy_cost_size;i++){
				retObj.enemy_cost[i] = MyProtocols.get_IOSecretHero(myDecoder);
		}
	}
	retObj.online_formation = new Array<IOSecretHero>();
	let online_formation_size = myDecoder.readInt();
	if(online_formation_size >0){
		for(var i=0; i<online_formation_size;i++){
				retObj.online_formation[i] = MyProtocols.get_IOSecretHero(myDecoder);
		}
	}
			return retObj;
		}

		static get_1280(myDecoder:WsDecoder):S2CSetCxryGeneralList{
				var retObj = new S2CSetCxryGeneralList();
			return retObj;
		}

		static get_IOLegionInfo(myDecoder:WsDecoder):IOLegionInfo{
				var retObj = new IOLegionInfo();
	retObj.id=myDecoder.readLong();
	retObj.lv=myDecoder.readInt();
	retObj.name=myDecoder.readString();
	retObj.npnum=myDecoder.readInt();
	retObj.mpnum=myDecoder.readInt();
	retObj.minlv=myDecoder.readInt();
	retObj.ispass=myDecoder.readBool();
	retObj.ceo=myDecoder.readString();
			return retObj;
		}

		static get_646(myDecoder:WsDecoder):S2CLegionSign{
				var retObj = new S2CLegionSign();
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.exp=myDecoder.readInt();
			return retObj;
		}

		static get_3112(myDecoder:WsDecoder):S2CMineDefFormationSave{
				var retObj = new S2CMineDefFormationSave();
	let mine_point_detail_exist = myDecoder.readBool();
	if(mine_point_detail_exist == true){
		retObj.mine_point_detail = MyProtocols.get_3104(myDecoder);
	}
			return retObj;
		}

		static get_1122(myDecoder:WsDecoder):S2CDungeonShopBuy{
				var retObj = new S2CDungeonShopBuy();
			return retObj;
		}

		static get_1104(myDecoder:WsDecoder):S2CDungeonChapterList{
				var retObj = new S2CDungeonChapterList();
	retObj.dungeonlist = new Array<IODungeonList>();
	let dungeonlist_size = myDecoder.readInt();
	if(dungeonlist_size >0){
		for(var i=0; i<dungeonlist_size;i++){
				retObj.dungeonlist[i] = MyProtocols.get_IODungeonList(myDecoder);
		}
	}
	retObj.dungeonset = new Array<IOGeneralBean>();
	let dungeonset_size = myDecoder.readInt();
	if(dungeonset_size >0){
		for(var i=0; i<dungeonset_size;i++){
				retObj.dungeonset[i] = MyProtocols.get_IOGeneralBean(myDecoder);
		}
	}
	retObj.globalbuf = new Array<IODungeonGlobalBuf>();
	let globalbuf_size = myDecoder.readInt();
	if(globalbuf_size >0){
		for(var i=0; i<globalbuf_size;i++){
				retObj.globalbuf[i] = MyProtocols.get_IODungeonGlobalBuf(myDecoder);
		}
	}
	retObj.formation_exist=myDecoder.readBool();
			return retObj;
		}

		static get_IOMineHistory(myDecoder:WsDecoder):IOMineHistory{
				var retObj = new IOMineHistory();
	retObj.target_player_id=myDecoder.readInt();
	retObj.target_player_name=myDecoder.readString();
	retObj.is_positive=myDecoder.readBool();
	retObj.is_success=myDecoder.readInt();
	retObj.mine_point=myDecoder.readInt();
	retObj.type=myDecoder.readInt();
	retObj.add_time=myDecoder.readInt();
	retObj.gain = new Array<number>();
	let gain_size = myDecoder.readInt();
	if(gain_size >0){
		for(var i=0; i<gain_size;i++){
			retObj.gain[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static get_IOBattleRecordSet(myDecoder:WsDecoder):IOBattleRecordSet{
				var retObj = new IOBattleRecordSet();
	let mythic_exist = myDecoder.readBool();
	if(mythic_exist == true){
		retObj.mythic = MyProtocols.get_IOMythicalAnimal(myDecoder);
	}
	retObj.team = {};
	let team_size = myDecoder.readInt();
	if(team_size >0){
		for(var i=0; i<team_size;i++){
			let team_key =myDecoder.readInt();
				retObj.team[team_key] = MyProtocols.get_IOGeneralBean(myDecoder);
		}
	}
			return retObj;
		}

		static get_10032(myDecoder:WsDecoder):S2CH5OpenLogin{
				var retObj = new S2CH5OpenLogin();
	retObj.session_id=myDecoder.readLong();
			return retObj;
		}

		static send_C2SGoldBuy(senderSocket:net.LejiSocket,p_buy_type:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(189);
	wsEncoder.writeInt(p_buy_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SChongChongPayPre(senderSocket:net.LejiSocket,p_goodsId:number,p_goodsName:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(153);
			wsEncoder.writeInt(p_goodsId);
			wsEncoder.writeString(p_goodsName);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOMintPoint(myDecoder:WsDecoder):IOMintPoint{
				var retObj = new IOMintPoint();
	let hold_player_exist = myDecoder.readBool();
	if(hold_player_exist == true){
		retObj.hold_player = MyProtocols.get_IOMineHolder(myDecoder);
	}
			return retObj;
		}

		static get_1002(myDecoder:WsDecoder):S2CPlayerBaseData{
				var retObj = new S2CPlayerBaseData();
	retObj.maxmapid=myDecoder.readInt();
	retObj.tower=myDecoder.readInt();
	retObj.pvpscore=myDecoder.readInt();
	retObj.gnum=myDecoder.readInt();
	retObj.bagspace=myDecoder.readInt();
	retObj.hiddens = new Array<SimpleItemInfo>();
	let hiddens_size = myDecoder.readInt();
	if(hiddens_size >0){
		for(var i=0; i<hiddens_size;i++){
				retObj.hiddens[i] = MyProtocols.get_SimpleItemInfo(myDecoder);
		}
	}
	let special_exist = myDecoder.readBool();
	if(special_exist == true){
		retObj.special = MyProtocols.get_IOSpecial(myDecoder);
	}
			return retObj;
		}

		static get_476(myDecoder:WsDecoder):S2CFriendReceiveExplore{
				var retObj = new S2CFriendReceiveExplore();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.bossid=myDecoder.readString();
			return retObj;
		}

		static send_C2SKpStart(senderSocket:net.LejiSocket,p_target_rid:number,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1081);
	wsEncoder.writeInt(p_target_rid);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_3108(myDecoder:WsDecoder):S2CMineGetAward{
				var retObj = new S2CMineGetAward();
	retObj.gain = new Array<IORewardItem>();
	let gain_size = myDecoder.readInt();
	if(gain_size >0){
		for(var i=0; i<gain_size;i++){
				retObj.gain[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_458(myDecoder:WsDecoder):S2CFriendApply{
				var retObj = new S2CFriendApply();
	retObj.role_ids = new Array<number>();
	let role_ids_size = myDecoder.readInt();
	if(role_ids_size >0){
		for(var i=0; i<role_ids_size;i++){
			retObj.role_ids[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static send_C2SDungeonShopBuy(senderSocket:net.LejiSocket,p_chapter:number,p_node:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1121);
	wsEncoder.writeInt(p_chapter);
	wsEncoder.writeInt(p_node);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_3104(myDecoder:WsDecoder):S2CMineEnemyDetail{
				var retObj = new S2CMineEnemyDetail();
	retObj.level_index=myDecoder.readInt();
	retObj.point_index=myDecoder.readInt();
	let base_info_exist = myDecoder.readBool();
	if(base_info_exist == true){
		retObj.base_info = MyProtocols.get_IOMineHolder(myDecoder);
	}
	let battleset_exist = myDecoder.readBool();
	if(battleset_exist == true){
		retObj.battleset = MyProtocols.get_IOBattlesetEnemy(myDecoder);
	}
	retObj.exclude_cards = new Array<number>();
	let exclude_cards_size = myDecoder.readInt();
	if(exclude_cards_size >0){
		for(var i=0; i<exclude_cards_size;i++){
			retObj.exclude_cards[i]=myDecoder.readLong();
		}
	}
	retObj.rand_key=myDecoder.readLong();
			return retObj;
		}

		static get_310(myDecoder:WsDecoder):S2CBagExpand{
				var retObj = new S2CBagExpand();
			return retObj;
		}

		static send_C2SServerList(senderSocket:net.LejiSocket,p_session_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(57);
	wsEncoder.writeLong(p_session_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SManorPatrol(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(869);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IODjrw1(myDecoder:WsDecoder):IODjrw1{
				var retObj = new IODjrw1();
	retObj.knark=myDecoder.readInt();
	retObj.chk = new Array<IODjrwChk>();
	let chk_size = myDecoder.readInt();
	if(chk_size >0){
		for(var i=0; i<chk_size;i++){
				retObj.chk[i] = MyProtocols.get_IODjrwChk(myDecoder);
		}
	}
	retObj.cnum=myDecoder.readInt();
	retObj.name=myDecoder.readString();
	retObj.items = new Array<RewardInfo>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.status=myDecoder.readInt();
			return retObj;
		}

		static get_576(myDecoder:WsDecoder):S2CBigBattleEnd{
				var retObj = new S2CBigBattleEnd();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.times=myDecoder.readInt();
			return retObj;
		}

		static get_324(myDecoder:WsDecoder):S2CShopBuy{
				var retObj = new S2CShopBuy();
	retObj.awards = new Array<SimpleItemInfo>();
	let awards_size = myDecoder.readInt();
	if(awards_size >0){
		for(var i=0; i<awards_size;i++){
				retObj.awards[i] = MyProtocols.get_SimpleItemInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SGuozhanView(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(3451);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1056(myDecoder:WsDecoder):S2CDungeonBattleStart{
				var retObj = new S2CDungeonBattleStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static get_710(myDecoder:WsDecoder):PushNewMail{
				var retObj = new PushNewMail();
	let mail_info_exist = myDecoder.readBool();
	if(mail_info_exist == true){
		retObj.mail_info = MyProtocols.get_IOMailInfo(myDecoder);
	}
			return retObj;
		}

		static send_C2SBuySzhc(senderSocket:net.LejiSocket,p_num_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1257);
	wsEncoder.writeInt(p_num_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGeneralReborn(senderSocket:net.LejiSocket,p_general_uuid:number,p_action_type:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(391);
	wsEncoder.writeLong(p_general_uuid);
	wsEncoder.writeInt(p_action_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGuideSave(senderSocket:net.LejiSocket,p_step:number,p_time:number,p_mark:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1007);
	wsEncoder.writeInt(p_step);
	wsEncoder.writeLong(p_time);
	wsEncoder.writeString(p_mark);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SFriendshipOnekey(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(469);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_758(myDecoder:WsDecoder):S2CMythicalPskillUp{
				var retObj = new S2CMythicalPskillUp();
	retObj.mythicals = new Array<IOMythicalAnimal>();
	let mythicals_size = myDecoder.readInt();
	if(mythicals_size >0){
		for(var i=0; i<mythicals_size;i++){
				retObj.mythicals[i] = MyProtocols.get_IOMythicalAnimal(myDecoder);
		}
	}
			return retObj;
		}

		static get_1096(myDecoder:WsDecoder):S2CGuozhanCityStart{
				var retObj = new S2CGuozhanCityStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static get_152(myDecoder:WsDecoder):S2C333PayPre{
				var retObj = new S2C333PayPre();
	retObj.time=myDecoder.readInt();
	retObj.server=myDecoder.readString();
	retObj.role=myDecoder.readString();
	retObj.goodsId=myDecoder.readString();
	retObj.goodsName=myDecoder.readString();
	retObj.money=myDecoder.readInt();
	retObj.cpOrderId=myDecoder.readString();
	retObj.sign=myDecoder.readString();
			return retObj;
		}

		static get_1120(myDecoder:WsDecoder):S2CDungeonShopList{
				var retObj = new S2CDungeonShopList();
	retObj.list = new Array<IODungeonShop>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IODungeonShop(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SYiJieLogin(senderSocket:net.LejiSocket,p_appId:string,p_channelId:string,p_userId:string,p_token:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10043);
	wsEncoder.writeString(p_appId);
	wsEncoder.writeString(p_channelId);
	wsEncoder.writeString(p_userId);
	wsEncoder.writeString(p_token);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOMjbgFinal(myDecoder:WsDecoder):IOMjbgFinal{
				var retObj = new IOMjbgFinal();
	retObj.list = new Array<IOMjbgFinal1>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOMjbgFinal1(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SManorBattleEnd(senderSocket:net.LejiSocket,p_index:number,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(537);
	wsEncoder.writeInt(p_index);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_474(myDecoder:WsDecoder):S2CFriendOpenExplore{
				var retObj = new S2CFriendOpenExplore();
	retObj.etime=myDecoder.readLong();
			return retObj;
		}

		static get_3310(myDecoder:WsDecoder):S2CSecretSoldierRevive{
				var retObj = new S2CSecretSoldierRevive();
	retObj.hero_id=myDecoder.readInt();
			return retObj;
		}

		static get_1094(myDecoder:WsDecoder):S2CGuozhanOfficCalculate{
				var retObj = new S2CGuozhanOfficCalculate();
	retObj.office_index=myDecoder.readInt();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_852(myDecoder:WsDecoder):S2COfficialSalary{
				var retObj = new S2COfficialSalary();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SDrawRecruit(senderSocket:net.LejiSocket,p_buy_type:number,p_times:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(193);
	wsEncoder.writeInt(p_buy_type);
	wsEncoder.writeInt(p_times);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOOtherPlayer(myDecoder:WsDecoder):IOOtherPlayer{
				var retObj = new IOOtherPlayer();
	retObj.rid=myDecoder.readInt();
	retObj.rno=myDecoder.readInt();
	retObj.rname=myDecoder.readString();
	retObj.sex=myDecoder.readInt();
	retObj.power=myDecoder.readInt();
	retObj.iconid=myDecoder.readInt();
	retObj.headid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.imageid=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.vip=myDecoder.readInt();
	retObj.office_index=myDecoder.readInt();
	retObj.bestgeneral = new Array<IOGeneralBean>();
	let bestgeneral_size = myDecoder.readInt();
	if(bestgeneral_size >0){
		for(var i=0; i<bestgeneral_size;i++){
				retObj.bestgeneral[i] = MyProtocols.get_IOGeneralBean(myDecoder);
		}
	}
	let battleset_exist = myDecoder.readBool();
	if(battleset_exist == true){
		retObj.battleset = MyProtocols.get_IOBattleSet(myDecoder);
	}
	retObj.points=myDecoder.readInt();
	retObj.battlelines = new Array<IOBattleLine>();
	let battlelines_size = myDecoder.readInt();
	if(battlelines_size >0){
		for(var i=0; i<battlelines_size;i++){
				retObj.battlelines[i] = MyProtocols.get_IOBattleLine(myDecoder);
		}
	}
	retObj.kpBattleset = new Array<IOBattleSet>();
	let kpBattleset_size = myDecoder.readInt();
	if(kpBattleset_size >0){
		for(var i=0; i<kpBattleset_size;i++){
				retObj.kpBattleset[i] = MyProtocols.get_IOBattleSet(myDecoder);
		}
	}
			return retObj;
		}

		static get_1070(myDecoder:WsDecoder):S2CWorldBossBattleStart{
				var retObj = new S2CWorldBossBattleStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static get_120(myDecoder:WsDecoder):S2CChargeInfo{
				var retObj = new S2CChargeInfo();
	retObj.is_first_pay=myDecoder.readBool();
	retObj.first_award_get=myDecoder.readBool();
	retObj.payment_level = new Array<number>();
	let payment_level_size = myDecoder.readInt();
	if(payment_level_size >0){
		for(var i=0; i<payment_level_size;i++){
			retObj.payment_level[i]=myDecoder.readInt();
		}
	}
	retObj.vip_gift_get = new Array<number>();
	let vip_gift_get_size = myDecoder.readInt();
	if(vip_gift_get_size >0){
		for(var i=0; i<vip_gift_get_size;i++){
			retObj.vip_gift_get[i]=myDecoder.readInt();
		}
	}
	retObj.is_long_yueka=myDecoder.readBool();
	retObj.long_yueka_get=myDecoder.readBool();
	retObj.yueka_left_day=myDecoder.readInt();
	retObj.yueka_get=myDecoder.readBool();
			return retObj;
		}

		static get_IOWorldBossRank(myDecoder:WsDecoder):IOWorldBossRank{
				var retObj = new IOWorldBossRank();
	retObj.headid=myDecoder.readInt();
	retObj.frameid=myDecoder.readInt();
	retObj.icon=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.power=myDecoder.readInt();
	retObj.damge=myDecoder.readLong();
	retObj.name=myDecoder.readString();
	retObj.rid=myDecoder.readInt();
			return retObj;
		}

		static get_312(myDecoder:WsDecoder):S2CBuyItem{
				var retObj = new S2CBuyItem();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SMapEventList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(541);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SDungeonOpenTime(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1101);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_178(myDecoder:WsDecoder):S2CIosIAPVerify{
				var retObj = new S2CIosIAPVerify();
			return retObj;
		}

		static get_IOLegionRank(myDecoder:WsDecoder):IOLegionRank{
				var retObj = new IOLegionRank();
	retObj.name=myDecoder.readString();
	retObj.damge=myDecoder.readLong();
	retObj.power=myDecoder.readInt();
			return retObj;
		}

		static get_IOMailAttach(myDecoder:WsDecoder):IOMailAttach{
				var retObj = new IOMailAttach();
	retObj.gsid=myDecoder.readInt();
	retObj.count=myDecoder.readInt();
			return retObj;
		}

		static send_C2SHeroList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(351);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_358(myDecoder:WsDecoder):S2CGeneralAddLevel{
				var retObj = new S2CGeneralAddLevel();
	retObj.general_uuid=myDecoder.readLong();
	let general_bean_exist = myDecoder.readBool();
	if(general_bean_exist == true){
		retObj.general_bean = MyProtocols.get_IOGeneralBean(myDecoder);
	}
			return retObj;
		}

		static get_IOManorBuilding(myDecoder:WsDecoder):IOManorBuilding{
				var retObj = new IOManorBuilding();
	retObj.lv=myDecoder.readInt();
	retObj.rid=myDecoder.readLong();
	retObj.id=myDecoder.readInt();
	retObj.type=myDecoder.readInt();
	retObj.pos=myDecoder.readInt();
	retObj.lastgain=myDecoder.readLong();
	retObj.items = new Array<IORewardItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOBattlesetEnemy(myDecoder:WsDecoder):IOBattlesetEnemy{
				var retObj = new IOBattlesetEnemy();
	let mythic_exist = myDecoder.readBool();
	if(mythic_exist == true){
		retObj.mythic = MyProtocols.get_IOMythicalAnimal(myDecoder);
	}
	retObj.team = {};
	let team_size = myDecoder.readInt();
	if(team_size >0){
		for(var i=0; i<team_size;i++){
			let team_key =myDecoder.readInt();
				retObj.team[team_key] = MyProtocols.get_IOGeneralBean(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SFriendDel(senderSocket:net.LejiSocket,p_role_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(459);
	wsEncoder.writeInt(p_role_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOLegionDonation(myDecoder:WsDecoder):IOLegionDonation{
				var retObj = new IOLegionDonation();
	retObj.count=myDecoder.readInt();
	retObj.last=myDecoder.readLong();
			return retObj;
		}

		static send_C2SManorFieldInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(865);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOSzhc(myDecoder:WsDecoder):IOSzhc{
				var retObj = new IOSzhc();
	retObj.consume = new Array<RewardInfo>();
	let consume_size = myDecoder.readInt();
	if(consume_size >0){
		for(var i=0; i<consume_size;i++){
				retObj.consume[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.demand = new Array<RewardInfo>();
	let demand_size = myDecoder.readInt();
	if(demand_size >0){
		for(var i=0; i<demand_size;i++){
				retObj.demand[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.buytime=myDecoder.readInt();
			return retObj;
		}

		static get_376(myDecoder:WsDecoder):S2CExclusiveRefreshConfirm{
				var retObj = new S2CExclusiveRefreshConfirm();
	retObj.general_uuid=myDecoder.readLong();
	retObj.is_confirm=myDecoder.readBool();
	let general_bean_exist = myDecoder.readBool();
	if(general_bean_exist == true){
		retObj.general_bean = MyProtocols.get_IOGeneralBean(myDecoder);
	}
			return retObj;
		}

		static get_IOMailInfo(myDecoder:WsDecoder):IOMailInfo{
				var retObj = new IOMailInfo();
	retObj.id=myDecoder.readInt();
	let from_exist = myDecoder.readBool();
	if(from_exist == true){
		retObj.from = MyProtocols.get_IOMailFrom(myDecoder);
	}
	retObj.type=myDecoder.readInt();
	retObj.state=myDecoder.readInt();
	retObj.title=myDecoder.readString();
	retObj.content=myDecoder.readString();
	retObj.reward = new Array<IOMailAttach>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IOMailAttach(myDecoder);
		}
	}
	retObj.stime=myDecoder.readLong();
	retObj.etime=myDecoder.readLong();
			return retObj;
		}

		static send_C2SOpenExploreBoss(senderSocket:net.LejiSocket,p_boss_owner_id:number,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1059);
	wsEncoder.writeInt(p_boss_owner_id);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_RewardInfo(myDecoder:WsDecoder):RewardInfo{
				var retObj = new RewardInfo();
	retObj.GSID=myDecoder.readInt();
	retObj.COUNT=myDecoder.readInt();
			return retObj;
		}

		static send_C2SZMPayCheck(senderSocket:net.LejiSocket,p_fee_id:number,p_goodsName:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(147);
	wsEncoder.writeInt(p_fee_id);
	wsEncoder.writeString(p_goodsName);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1208(myDecoder:WsDecoder):S2CQiZhenYiBaoView{
				var retObj = new S2CQiZhenYiBaoView();
	retObj.fixed_award = new Array<ActivitiesItem>();
	let fixed_award_size = myDecoder.readInt();
	if(fixed_award_size >0){
		for(var i=0; i<fixed_award_size;i++){
				retObj.fixed_award[i] = MyProtocols.get_ActivitiesItem(myDecoder);
		}
	}
	let big_award_exist = myDecoder.readBool();
	if(big_award_exist == true){
		retObj.big_award = MyProtocols.get_ActivitiesItem(myDecoder);
	}
	retObj.my_count=myDecoder.readInt();
	retObj.end_cd_time=myDecoder.readInt();
	retObj.player_count_kaijiang=myDecoder.readInt();
	retObj.cost_diamond=myDecoder.readInt();
	retObj.big_award_value=myDecoder.readInt();
	retObj.players = new Array<QiZhenYiBaoPlayer>();
	let players_size = myDecoder.readInt();
	if(players_size >0){
		for(var i=0; i<players_size;i++){
				retObj.players[i] = MyProtocols.get_QiZhenYiBaoPlayer(myDecoder);
		}
	}
	retObj.history_players = new Array<QiZhenYiBaoPlayer>();
	let history_players_size = myDecoder.readInt();
	if(history_players_size >0){
		for(var i=0; i<history_players_size;i++){
				retObj.history_players[i] = MyProtocols.get_QiZhenYiBaoPlayer(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SQuLeLeLogin(senderSocket:net.LejiSocket,p_app_id:string,p_user_id:string,p_session_id:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10061);
	wsEncoder.writeString(p_app_id);
	wsEncoder.writeString(p_user_id);
	wsEncoder.writeString(p_session_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_140(myDecoder:WsDecoder):PushMarquee{
				var retObj = new PushMarquee();
	retObj.template_id=myDecoder.readInt();
	retObj.order=myDecoder.readInt();
	retObj.parameters = new Array<string>();
	let parameters_size = myDecoder.readInt();
	if(parameters_size >0){
		for(var i=0; i<parameters_size;i++){
			retObj.parameters[i]=myDecoder.readString();
		}
	}
	retObj.content=myDecoder.readString();
	retObj.count=myDecoder.readInt();
			return retObj;
		}

		static send_C2SFriendRecommandList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(453);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SFriendBattleEnd(senderSocket:net.LejiSocket,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1067);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SChapterChange(senderSocket:net.LejiSocket,p_mapid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(505);
	wsEncoder.writeInt(p_mapid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGetTnqwInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1241);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1106(myDecoder:WsDecoder):S2CDungeonSaveGeneralList{
				var retObj = new S2CDungeonSaveGeneralList();
	retObj.generals = new Array<IOGeneralBean>();
	let generals_size = myDecoder.readInt();
	if(generals_size >0){
		for(var i=0; i<generals_size;i++){
				retObj.generals[i] = MyProtocols.get_IOGeneralBean(myDecoder);
		}
	}
	retObj.globalbuf = new Array<IODungeonGlobalBuf>();
	let globalbuf_size = myDecoder.readInt();
	if(globalbuf_size >0){
		for(var i=0; i<globalbuf_size;i++){
				retObj.globalbuf[i] = MyProtocols.get_IODungeonGlobalBuf(myDecoder);
		}
	}
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.potion = new Array<IODungeonPotion>();
	let potion_size = myDecoder.readInt();
	if(potion_size >0){
		for(var i=0; i<potion_size;i++){
				retObj.potion[i] = MyProtocols.get_IODungeonPotion(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SGuideChooseReward(senderSocket:net.LejiSocket,p_choose_str:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1009);
	wsEncoder.writeString(p_choose_str);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_100(myDecoder:WsDecoder):S2CErrorCode{
				var retObj = new S2CErrorCode();
	retObj.msg_num=myDecoder.readInt();
	retObj.ret_code=myDecoder.readInt();
			return retObj;
		}

		static get_648(myDecoder:WsDecoder):S2CLegionTeckLv{
				var retObj = new S2CLegionTeckLv();
			return retObj;
		}

		static send_C2SLegionBossFarm(senderSocket:net.LejiSocket,p_chapter_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(667);
	wsEncoder.writeInt(p_chapter_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_502(myDecoder:WsDecoder):S2CChapterInfo{
				var retObj = new S2CChapterInfo();
	retObj.items = new Array<IOGeneralSimple>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOGeneralSimple(myDecoder);
		}
	}
			return retObj;
		}

		static get_1118(myDecoder:WsDecoder):S2CDungeonChooseShop{
				var retObj = new S2CDungeonChooseShop();
			return retObj;
		}

		static get_128(myDecoder:WsDecoder):S2CListRedPoints{
				var retObj = new S2CListRedPoints();
	retObj.has_redpoint = new Array<number>();
	let has_redpoint_size = myDecoder.readInt();
	if(has_redpoint_size >0){
		for(var i=0; i<has_redpoint_size;i++){
			retObj.has_redpoint[i]=myDecoder.readInt();
		}
	}
	retObj.activity_redpoints = new Array<number>();
	let activity_redpoints_size = myDecoder.readInt();
	if(activity_redpoints_size >0){
		for(var i=0; i<activity_redpoints_size;i++){
			retObj.activity_redpoints[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static send_C2SCreateCharacter(senderSocket:net.LejiSocket,p_session_id:number,p_name:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(105);
	wsEncoder.writeLong(p_session_id);
	wsEncoder.writeString(p_name);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_658(myDecoder:WsDecoder):S2CLegionFactoryMissionUp{
				var retObj = new S2CLegionFactoryMissionUp();
	retObj.id=myDecoder.readInt();
			return retObj;
		}

		static send_C2SGetTnqwBossInfo(senderSocket:net.LejiSocket,p_boss_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1243);
	wsEncoder.writeInt(p_boss_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_3302(myDecoder:WsDecoder):S2CSecretView{
				var retObj = new S2CSecretView();
	retObj.map_id=myDecoder.readInt();
	retObj.progress=myDecoder.readInt();
	retObj.could_reset=myDecoder.readBool();
	retObj.boxAward = new Array<IOSecretBoxAward>();
	let boxAward_size = myDecoder.readInt();
	if(boxAward_size >0){
		for(var i=0; i<boxAward_size;i++){
				retObj.boxAward[i] = MyProtocols.get_IOSecretBoxAward(myDecoder);
		}
	}
	retObj.my_cost = new Array<IOSecretHero>();
	let my_cost_size = myDecoder.readInt();
	if(my_cost_size >0){
		for(var i=0; i<my_cost_size;i++){
				retObj.my_cost[i] = MyProtocols.get_IOSecretHero(myDecoder);
		}
	}
	retObj.enemy_cost = new Array<IOSecretHero>();
	let enemy_cost_size = myDecoder.readInt();
	if(enemy_cost_size >0){
		for(var i=0; i<enemy_cost_size;i++){
				retObj.enemy_cost[i] = MyProtocols.get_IOSecretHero(myDecoder);
		}
	}
	retObj.online_formation = new Array<IOSecretHero>();
	let online_formation_size = myDecoder.readInt();
	if(online_formation_size >0){
		for(var i=0; i<online_formation_size;i++){
				retObj.online_formation[i] = MyProtocols.get_IOSecretHero(myDecoder);
		}
	}
	retObj.revive_count=myDecoder.readInt();
			return retObj;
		}

		static get_54(myDecoder:WsDecoder):S2CGuestLogin{
				var retObj = new S2CGuestLogin();
	retObj.uid=myDecoder.readInt();
	retObj.session_id=myDecoder.readLong();
			return retObj;
		}

		static get_3456(myDecoder:WsDecoder):S2CGuozhanMove{
				var retObj = new S2CGuozhanMove();
	retObj.city_index=myDecoder.readInt();
			return retObj;
		}

		static get_554(myDecoder:WsDecoder):S2CMissionAchieveAward{
				var retObj = new S2CMissionAchieveAward();
	retObj.rewards = new Array<AwardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_AwardItem(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SChangeHeadFrame(senderSocket:net.LejiSocket,p_head_frame_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(173);
	wsEncoder.writeInt(p_head_frame_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_756(myDecoder:WsDecoder):S2CMythicalClassUp{
				var retObj = new S2CMythicalClassUp();
	retObj.mythicals = new Array<IOMythicalAnimal>();
	let mythicals_size = myDecoder.readInt();
	if(mythicals_size >0){
		for(var i=0; i<mythicals_size;i++){
				retObj.mythicals[i] = MyProtocols.get_IOMythicalAnimal(myDecoder);
		}
	}
			return retObj;
		}

		static get_1268(myDecoder:WsDecoder):S2CMjbgNext{
				var retObj = new S2CMjbgNext();
			return retObj;
		}

		static get_334(myDecoder:WsDecoder):S2CAffairRefresh{
				var retObj = new S2CAffairRefresh();
	retObj.item_list = new Array<IOAffairItem>();
	let item_list_size = myDecoder.readInt();
	if(item_list_size >0){
		for(var i=0; i<item_list_size;i++){
				retObj.item_list[i] = MyProtocols.get_IOAffairItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_IODungeonShop(myDecoder:WsDecoder):IODungeonShop{
				var retObj = new IODungeonShop();
	retObj.chapter=myDecoder.readInt();
	retObj.node=myDecoder.readInt();
	retObj.disc=myDecoder.readInt();
	retObj.item = new Array<IORewardItem>();
	let item_size = myDecoder.readInt();
	if(item_size >0){
		for(var i=0; i<item_size;i++){
				retObj.item[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.consume = new Array<IORewardItem>();
	let consume_size = myDecoder.readInt();
	if(consume_size >0){
		for(var i=0; i<consume_size;i++){
				retObj.consume[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.quality=myDecoder.readInt();
			return retObj;
		}

		static get_IOShopItem(myDecoder:WsDecoder):IOShopItem{
				var retObj = new IOShopItem();
	retObj.GSID=myDecoder.readInt();
	retObj.COUNT=myDecoder.readInt();
	retObj.BUYTIME=myDecoder.readInt();
	retObj.COIN=myDecoder.readInt();
	retObj.PRICE=myDecoder.readInt();
	retObj.DISCOUNT=myDecoder.readInt();
			return retObj;
		}

		static get_534(myDecoder:WsDecoder):S2CManorEnemyInfo{
				var retObj = new S2CManorEnemyInfo();
	let boss_exist = myDecoder.readBool();
	if(boss_exist == true){
		retObj.boss = MyProtocols.get_IOManorBoss(myDecoder);
	}
	retObj.enemy = {};
	let enemy_size = myDecoder.readInt();
	if(enemy_size >0){
		for(var i=0; i<enemy_size;i++){
			let enemy_key =myDecoder.readInt();
				retObj.enemy[enemy_key] = MyProtocols.get_IOGeneralSimple(myDecoder);
		}
	}
			return retObj;
		}

		static get_1054(myDecoder:WsDecoder):S2CLegionBossBattleEnd{
				var retObj = new S2CLegionBossBattleEnd();
	let result_exist = myDecoder.readBool();
	if(result_exist == true){
		retObj.result = MyProtocols.get_IOBattleResult(myDecoder);
	}
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.last=myDecoder.readLong();
	retObj.damge=myDecoder.readLong();
	retObj.kill=myDecoder.readInt();
			return retObj;
		}

		static send_C2SQedjAward(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(183);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SStarGiftList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1215);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SChangeHeadImage(senderSocket:net.LejiSocket,p_image_id:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(175);
	wsEncoder.writeInt(p_image_id);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_876(myDecoder:WsDecoder):S2CSrenderPersua{
				var retObj = new S2CSrenderPersua();
	retObj.loyal=myDecoder.readInt();
	let reward_exist = myDecoder.readBool();
	if(reward_exist == true){
		retObj.reward = MyProtocols.get_IORewardItem(myDecoder);
	}
			return retObj;
		}

		static send_C2SFriendApply(senderSocket:net.LejiSocket,p_role_ids:Array<number>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(457);
	if(p_role_ids == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_role_ids.length);
		p_role_ids.forEach(function(p_role_ids_v){
	wsEncoder.writeInt(p_role_ids_v);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SPvpRecords(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(547);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SIosIAPVerify(senderSocket:net.LejiSocket,p_receipt:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(177);
	wsEncoder.writeString(p_receipt);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOBattleRetSide(myDecoder:WsDecoder):IOBattleRetSide{
				var retObj = new IOBattleRetSide();
	let info_exist = myDecoder.readBool();
	if(info_exist == true){
		retObj.info = MyProtocols.get_IOBattleRecordInfo(myDecoder);
	}
	let set_exist = myDecoder.readBool();
	if(set_exist == true){
		retObj.set = MyProtocols.get_IOBattleRetSet(myDecoder);
	}
			return retObj;
		}

		static get_404(myDecoder:WsDecoder):S2CSpinRoll{
				var retObj = new S2CSpinRoll();
	retObj.type=myDecoder.readInt();
	retObj.times=myDecoder.readInt();
	retObj.pos=myDecoder.readInt();
	retObj.items = new Array<IOSpinItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IOSpinItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_384(myDecoder:WsDecoder):S2CGeneralExchangeConfirm{
				var retObj = new S2CGeneralExchangeConfirm();
	retObj.action_type=myDecoder.readInt();
	let general_bean_exist = myDecoder.readBool();
	if(general_bean_exist == true){
		retObj.general_bean = MyProtocols.get_IOGeneralBean(myDecoder);
	}
			return retObj;
		}

		static send_C2SGetPvpAgainst(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(545);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGxdbInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1235);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_144(myDecoder:WsDecoder):S2CRankView{
				var retObj = new S2CRankView();
	retObj.rtype=myDecoder.readString();
	retObj.selfrank=myDecoder.readInt();
	retObj.my_rank_change=myDecoder.readInt();
	retObj.list = new Array<IORankPlayer>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IORankPlayer(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SSetMailRead(senderSocket:net.LejiSocket,p_mail_id:Array<number>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(705);
	if(p_mail_id == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_mail_id.length);
		p_mail_id.forEach(function(p_mail_id_v){
	wsEncoder.writeInt(p_mail_id_v);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SExclusiveRefreshGet(senderSocket:net.LejiSocket,p_general_uuid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(371);
	wsEncoder.writeLong(p_general_uuid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_SecretItemInfo(myDecoder:WsDecoder):SecretItemInfo{
				var retObj = new SecretItemInfo();
	retObj.itemId=myDecoder.readInt();
	retObj.cnt=myDecoder.readInt();
			return retObj;
		}

		static send_C2STowerBattleEnd(senderSocket:net.LejiSocket,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(521);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_ActivityInfoOne(myDecoder:WsDecoder):ActivityInfoOne{
				var retObj = new ActivityInfoOne();
	retObj.nSubCount=myDecoder.readInt();
	retObj.nNeedLevel=myDecoder.readInt();
	retObj.nNeedVipLevel=myDecoder.readInt();
	retObj.nNeedPassNoChapID=myDecoder.readInt();
	retObj.nState=myDecoder.readInt();
	retObj.nComplete=myDecoder.readInt();
	retObj.szSubDes=myDecoder.readString();
	retObj.reward = new Array<ActivitiesItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_ActivitiesItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_306(myDecoder:WsDecoder):S2CItemSell{
				var retObj = new S2CItemSell();
	retObj.status=myDecoder.readInt();
	retObj.reward = new Array<RewardInfo>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_652(myDecoder:WsDecoder):S2CLegionFactoryLv{
				var retObj = new S2CLegionFactoryLv();
	retObj.lv=myDecoder.readInt();
	retObj.exp=myDecoder.readInt();
	retObj.newman=myDecoder.readInt();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_630(myDecoder:WsDecoder):S2CLegionInfo{
				var retObj = new S2CLegionInfo();
	retObj.notice=myDecoder.readString();
	retObj.power=myDecoder.readInt();
	retObj.minlv=myDecoder.readInt();
	retObj.lastmail=myDecoder.readLong();
	retObj.ispass=myDecoder.readBool();
	retObj.secretplace=myDecoder.readInt();
	retObj.wbosshistroyrank=myDecoder.readInt();
	retObj.servid=myDecoder.readInt();
	retObj.lastactive=myDecoder.readLong();
	retObj.name=myDecoder.readString();
	retObj.donation = {};
	let donation_size = myDecoder.readInt();
	if(donation_size >0){
		for(var i=0; i<donation_size;i++){
			let donation_key =myDecoder.readInt();
				retObj.donation[donation_key] = MyProtocols.get_IOLegionDonation(myDecoder);
		}
	}
	retObj.exp=myDecoder.readInt();
	retObj.fexp=myDecoder.readInt();
	retObj.flevel=myDecoder.readInt();
	retObj.kceo=myDecoder.readBool();
	retObj.level=myDecoder.readInt();
	retObj.maxexp=myDecoder.readInt();
	retObj.mpnum=myDecoder.readInt();
	retObj.id=myDecoder.readLong();
	retObj.members = new Array<IOLegionMember>();
	let members_size = myDecoder.readInt();
	if(members_size >0){
		for(var i=0; i<members_size;i++){
				retObj.members[i] = MyProtocols.get_IOLegionMember(myDecoder);
		}
	}
	retObj.ceo=myDecoder.readString();
	retObj.nextmail=myDecoder.readLong();
	retObj.pos=myDecoder.readInt();
			return retObj;
		}

		static get_752(myDecoder:WsDecoder):S2CMythicalList{
				var retObj = new S2CMythicalList();
	retObj.mythical_list = new Array<IOMythicalAnimal>();
	let mythical_list_size = myDecoder.readInt();
	if(mythical_list_size >0){
		for(var i=0; i<mythical_list_size;i++){
				retObj.mythical_list[i] = MyProtocols.get_IOMythicalAnimal(myDecoder);
		}
	}
			return retObj;
		}

		static get_466(myDecoder:WsDecoder):S2CFriendshipGive{
				var retObj = new S2CFriendshipGive();
	retObj.rewards = new Array<IORewardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_754(myDecoder:WsDecoder):S2CMythicalLevelUp{
				var retObj = new S2CMythicalLevelUp();
	retObj.mythicals = new Array<IOMythicalAnimal>();
	let mythicals_size = myDecoder.readInt();
	if(mythicals_size >0){
		for(var i=0; i<mythicals_size;i++){
				retObj.mythicals[i] = MyProtocols.get_IOMythicalAnimal(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SAffairList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(331);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SDungeonChapterInfo(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1109);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SBattleSeason(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(197);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_512(myDecoder:WsDecoder):S2CBattleFormationSave{
				var retObj = new S2CBattleFormationSave();
			return retObj;
		}

		static get_1064(myDecoder:WsDecoder):S2CFriendBattleInfo{
				var retObj = new S2CFriendBattleInfo();
	let mythic_exist = myDecoder.readBool();
	if(mythic_exist == true){
		retObj.mythic = MyProtocols.get_IOMythicalAnimal(myDecoder);
	}
	retObj.team = {};
	let team_size = myDecoder.readInt();
	if(team_size >0){
		for(var i=0; i<team_size;i++){
			let team_key =myDecoder.readInt();
				retObj.team[team_key] = MyProtocols.get_IOGeneralBean(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SPlayerBaseData(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1001);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_858(myDecoder:WsDecoder):S2CManorInfo{
				var retObj = new S2CManorInfo();
	retObj.manorlv=myDecoder.readInt();
	retObj.buildings = new Array<IOManorBuilding>();
	let buildings_size = myDecoder.readInt();
	if(buildings_size >0){
		for(var i=0; i<buildings_size;i++){
				retObj.buildings[i] = MyProtocols.get_IOManorBuilding(myDecoder);
		}
	}
	retObj.items = new Array<IORewardItem>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_1092(myDecoder:WsDecoder):S2CGuozhanOfficeStart{
				var retObj = new S2CGuozhanOfficeStart();
	retObj.seed=myDecoder.readLong();
	retObj.battleid=myDecoder.readLong();
	retObj.season=myDecoder.readInt();
			return retObj;
		}

		static get_546(myDecoder:WsDecoder):S2CGetPvpAgainst{
				var retObj = new S2CGetPvpAgainst();
	retObj.rids = new Array<number>();
	let rids_size = myDecoder.readInt();
	if(rids_size >0){
		for(var i=0; i<rids_size;i++){
			retObj.rids[i]=myDecoder.readInt();
		}
	}
			return retObj;
		}

		static get_552(myDecoder:WsDecoder):S2CMissionDailyAward{
				var retObj = new S2CMissionDailyAward();
	retObj.rewards = new Array<AwardItem>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_AwardItem(myDecoder);
		}
	}
			return retObj;
		}

		static get_IOProperty(myDecoder:WsDecoder):IOProperty{
				var retObj = new IOProperty();
	retObj.hp=myDecoder.readInt();
	retObj.atk=myDecoder.readInt();
	retObj.def=myDecoder.readInt();
	retObj.mdef=myDecoder.readInt();
	retObj.atktime=myDecoder.readFloat();
	retObj.range=myDecoder.readInt();
	retObj.msp=myDecoder.readInt();
	retObj.pasp=myDecoder.readInt();
	retObj.pcri=myDecoder.readInt();
	retObj.pcrid=myDecoder.readInt();
	retObj.pdam=myDecoder.readInt();
	retObj.php=myDecoder.readInt();
	retObj.patk=myDecoder.readInt();
	retObj.pdef=myDecoder.readInt();
	retObj.pmdef=myDecoder.readInt();
	retObj.ppbs=myDecoder.readInt();
	retObj.pmbs=myDecoder.readInt();
	retObj.pefc=myDecoder.readInt();
	retObj.ppthr=myDecoder.readInt();
	retObj.patkdam=myDecoder.readInt();
	retObj.pskidam=myDecoder.readInt();
	retObj.pckatk=myDecoder.readInt();
	retObj.pmthr=myDecoder.readInt();
	retObj.pdex=myDecoder.readInt();
	retObj.pmdex=myDecoder.readInt();
	retObj.pmsatk=myDecoder.readInt();
	retObj.pmps=myDecoder.readInt();
	retObj.pcd=myDecoder.readInt();
			return retObj;
		}

		static get_122(myDecoder:WsDecoder):S2CGetFirstPayAward{
				var retObj = new S2CGetFirstPayAward();
	retObj.rewards = new Array<RewardInfo>();
	let rewards_size = myDecoder.readInt();
	if(rewards_size >0){
		for(var i=0; i<rewards_size;i++){
				retObj.rewards[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2STnqwBossSweep(senderSocket:net.LejiSocket,p_boss_index:number,p_times:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1245);
	wsEncoder.writeInt(p_boss_index);
	wsEncoder.writeInt(p_times);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SExclusiveRefreshBegin(senderSocket:net.LejiSocket,p_general_uuid:number,p_lock_type:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(373);
	wsEncoder.writeLong(p_general_uuid);
	wsEncoder.writeInt(p_lock_type);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_126(myDecoder:WsDecoder):S2CDownlineReconnect{
				var retObj = new S2CDownlineReconnect();
	retObj.ret=myDecoder.readInt();
			return retObj;
		}

		static get_IOGeneralLegion(myDecoder:WsDecoder):IOGeneralLegion{
				var retObj = new IOGeneralLegion();
	retObj.gsid=myDecoder.readInt();
	retObj.level=myDecoder.readInt();
	retObj.hpcover=myDecoder.readInt();
	retObj.class=myDecoder.readInt();
	retObj.exhp=myDecoder.readLong();
	retObj.exatk=myDecoder.readInt();
			return retObj;
		}

		static get_142(myDecoder:WsDecoder):S2CTeamsInfoGet{
				var retObj = new S2CTeamsInfoGet();
	retObj.teams = new Array<IOTeamInfo>();
	let teams_size = myDecoder.readInt();
	if(teams_size >0){
		for(var i=0; i<teams_size;i++){
				retObj.teams[i] = MyProtocols.get_IOTeamInfo(myDecoder);
		}
	}
			return retObj;
		}

		static get_1250(myDecoder:WsDecoder):S2CXsdhList{
				var retObj = new S2CXsdhList();
	retObj.list = new Array<IOXsdh1>();
	let list_size = myDecoder.readInt();
	if(list_size >0){
		for(var i=0; i<list_size;i++){
				retObj.list[i] = MyProtocols.get_IOXsdh1(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SGeneralExchangeRefresh(senderSocket:net.LejiSocket,p_general_uuid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(381);
	wsEncoder.writeLong(p_general_uuid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SLegionApplyReview(senderSocket:net.LejiSocket,p_player_ids:Array<number>,p_is_accept:boolean){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(635);
	if(p_player_ids == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_player_ids.length);
		p_player_ids.forEach(function(p_player_ids_v){
	wsEncoder.writeInt(p_player_ids_v);
		});
	}
	wsEncoder.writeBool(p_is_accept);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_314(myDecoder:WsDecoder):S2CItemExchange{
				var retObj = new S2CItemExchange();
	retObj.reward = new Array<RewardInfo>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2S4399Login(senderSocket:net.LejiSocket,p_gameId:number,p_time:number,p_userId:string,p_userName:string,p_sign:string){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(10041);
	wsEncoder.writeInt(p_gameId);
	wsEncoder.writeInt(p_time);
	wsEncoder.writeString(p_userId);
	wsEncoder.writeString(p_userName);
	wsEncoder.writeString(p_sign);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_606(myDecoder:WsDecoder):S2CChatView{
				var retObj = new S2CChatView();
	retObj.chat_type=myDecoder.readInt();
	retObj.chat_content = new Array<S2CChatPush>();
	let chat_content_size = myDecoder.readInt();
	if(chat_content_size >0){
		for(var i=0; i<chat_content_size;i++){
				retObj.chat_content[i] = MyProtocols.get_604(myDecoder);
		}
	}
	retObj.new_msg_list = new Array<boolean>();
	let new_msg_list_size = myDecoder.readInt();
	if(new_msg_list_size >0){
		for(var i=0; i<new_msg_list_size;i++){
			retObj.new_msg_list[i]=myDecoder.readBool();
		}
	}
			return retObj;
		}

		static get_506(myDecoder:WsDecoder):S2CChapterChange{
				var retObj = new S2CChapterChange();
	retObj.mapid=myDecoder.readInt();
			return retObj;
		}

		static send_C2SManorReward(senderSocket:net.LejiSocket,p_pos:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(863);
	wsEncoder.writeInt(p_pos);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_308(myDecoder:WsDecoder):S2CEquipComp{
				var retObj = new S2CEquipComp();
	retObj.equip_id=myDecoder.readInt();
	retObj.count=myDecoder.readInt();
			return retObj;
		}

		static send_C2SQueryHasRole(senderSocket:net.LejiSocket,p_session_id:number,p_qq_open_data:WanbaLoginGift){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(103);
	wsEncoder.writeLong(p_session_id);
	if(p_qq_open_data == null){
		wsEncoder.writeBool(false);
	}else { 
		wsEncoder.writeBool(true);
			wsEncoder.writeInt(p_qq_open_data.wanba_gift_id);
			wsEncoder.writeString(p_qq_open_data.appid);
			wsEncoder.writeString(p_qq_open_data.openid);
			wsEncoder.writeString(p_qq_open_data.openkey);
			wsEncoder.writeString(p_qq_open_data.pf);
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_1272(myDecoder:WsDecoder):S2CTgslInfo{
				var retObj = new S2CTgslInfo();
	retObj.gsid=myDecoder.readInt();
	retObj.name=myDecoder.readString();
	retObj.level=myDecoder.readInt();
	retObj.mark=myDecoder.readInt();
	retObj.skill = new Array<number>();
	let skill_size = myDecoder.readInt();
	if(skill_size >0){
		for(var i=0; i<skill_size;i++){
			retObj.skill[i]=myDecoder.readInt();
		}
	}
	retObj.challrewards = new Array<number>();
	let challrewards_size = myDecoder.readInt();
	if(challrewards_size >0){
		for(var i=0; i<challrewards_size;i++){
			retObj.challrewards[i]=myDecoder.readInt();
		}
	}
	retObj.hp=myDecoder.readLong();
	retObj.bset = {};
	let bset_size = myDecoder.readInt();
	if(bset_size >0){
		for(var i=0; i<bset_size;i++){
			let bset_key =myDecoder.readInt();
				retObj.bset[bset_key] = MyProtocols.get_IOGeneralLegion(myDecoder);
		}
	}
			return retObj;
		}

		static get_1020(myDecoder:WsDecoder):S2CBuyRight{
				var retObj = new S2CBuyRight();
			return retObj;
		}

		static get_1214(myDecoder:WsDecoder):S2CLevelGiftList{
				var retObj = new S2CLevelGiftList();
	retObj.ret_list = new Array<IOLevelGift>();
	let ret_list_size = myDecoder.readInt();
	if(ret_list_size >0){
		for(var i=0; i<ret_list_size;i++){
				retObj.ret_list[i] = MyProtocols.get_IOLevelGift(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SDungeonUsePotion(senderSocket:net.LejiSocket,p_potion:number,p_guid:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1113);
	wsEncoder.writeInt(p_potion);
	wsEncoder.writeLong(p_guid);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SGeneralTakeonEquip(senderSocket:net.LejiSocket,p_action_type:number,p_general_uuid:number,p_equip_id:number,p_pos_index:number){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(355);
	wsEncoder.writeInt(p_action_type);
	wsEncoder.writeLong(p_general_uuid);
	wsEncoder.writeInt(p_equip_id);
	wsEncoder.writeInt(p_pos_index);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_IOAffairItem(myDecoder:WsDecoder):IOAffairItem{
				var retObj = new IOAffairItem();
	retObj.id=myDecoder.readInt();
	retObj.gnum=myDecoder.readInt();
	retObj.gstar=myDecoder.readInt();
	retObj.hour=myDecoder.readInt();
	retObj.cond = new Array<number>();
	let cond_size = myDecoder.readInt();
	if(cond_size >0){
		for(var i=0; i<cond_size;i++){
			retObj.cond[i]=myDecoder.readInt();
		}
	}
	retObj.reward = new Array<IORewardItem>();
	let reward_size = myDecoder.readInt();
	if(reward_size >0){
		for(var i=0; i<reward_size;i++){
				retObj.reward[i] = MyProtocols.get_IORewardItem(myDecoder);
		}
	}
	retObj.lock=myDecoder.readInt();
	retObj.create=myDecoder.readLong();
	retObj.etime=myDecoder.readLong();
	retObj.arr = new Array<number>();
	let arr_size = myDecoder.readInt();
	if(arr_size >0){
		for(var i=0; i<arr_size;i++){
			retObj.arr[i]=myDecoder.readLong();
		}
	}
			return retObj;
		}

		static get_IOBattleRetSet(myDecoder:WsDecoder):IOBattleRetSet{
				var retObj = new IOBattleRetSet();
	retObj.index=myDecoder.readInt();
	retObj.team = {};
	let team_size = myDecoder.readInt();
	if(team_size >0){
		for(var i=0; i<team_size;i++){
			let team_key =myDecoder.readInt();
				retObj.team[team_key] = MyProtocols.get_IOGeneralBean(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SHeadsFramesImagesList(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(169);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SDelMail(senderSocket:net.LejiSocket,p_mail_id:Array<number>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(707);
	if(p_mail_id == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_mail_id.length);
		p_mail_id.forEach(function(p_mail_id_v){
	wsEncoder.writeInt(p_mail_id_v);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_392(myDecoder:WsDecoder):S2CGeneralReborn{
				var retObj = new S2CGeneralReborn();
	retObj.items = new Array<RewardInfo>();
	let items_size = myDecoder.readInt();
	if(items_size >0){
		for(var i=0; i<items_size;i++){
				retObj.items[i] = MyProtocols.get_RewardInfo(myDecoder);
		}
	}
	retObj.general = new Array<IOAwardRandomGeneral>();
	let general_size = myDecoder.readInt();
	if(general_size >0){
		for(var i=0; i<general_size;i++){
				retObj.general[i] = MyProtocols.get_IOAwardRandomGeneral(myDecoder);
		}
	}
			return retObj;
		}

		static send_C2SGuozhanBattleEnd(senderSocket:net.LejiSocket,p_battleid:number,p_as:Array<IOBHurt>,p_df:Array<IOBHurt>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(1089);
	wsEncoder.writeLong(p_battleid);
	if(p_as == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_as.length);
		p_as.forEach(function(p_as_v){
			wsEncoder.writeInt(p_as_v.gsid);
			wsEncoder.writeLong(p_as_v.hurm);
			wsEncoder.writeLong(p_as_v.heal);
			wsEncoder.writeLong(p_as_v.hp);
			wsEncoder.writeInt(p_as_v.born);
			wsEncoder.writeInt(p_as_v.hpperc);
			wsEncoder.writeLong(p_as_v.hpmax);
		});
	}
	if(p_df == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_df.length);
		p_df.forEach(function(p_df_v){
			wsEncoder.writeInt(p_df_v.gsid);
			wsEncoder.writeLong(p_df_v.hurm);
			wsEncoder.writeLong(p_df_v.heal);
			wsEncoder.writeLong(p_df_v.hp);
			wsEncoder.writeInt(p_df_v.born);
			wsEncoder.writeInt(p_df_v.hpperc);
			wsEncoder.writeLong(p_df_v.hpmax);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_642(myDecoder:WsDecoder):S2CLegionDismiss{
				var retObj = new S2CLegionDismiss();
			return retObj;
		}

		static send_C2SExpedBattleStart(senderSocket:net.LejiSocket,p_mythic:number,p_items:Array<IOFormationGeneralPos>){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(527);
	wsEncoder.writeInt(p_mythic);
	if(p_items == null) {
		wsEncoder.writeInt(0);
	}else{
		wsEncoder.writeInt(p_items.length);
		p_items.forEach(function(p_items_v){
			wsEncoder.writeInt(p_items_v.pos);
			wsEncoder.writeLong(p_items_v.general_uuid);
		});
	}
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static send_C2SOfficialSalary(senderSocket:net.LejiSocket){
			let wsEncoder = WsEncoder.getInstance().init();
			wsEncoder.writeInt(851);
			var rawContent = wsEncoder.bytes();
			senderSocket.sendMessage(rawContent);
			wsEncoder.clear();
		}

		static get_802(myDecoder:WsDecoder):S2CListSignIn{
				var retObj = new S2CListSignIn();
	retObj.sign_in_count=myDecoder.readInt();
	retObj.is_get=myDecoder.readBool();
			return retObj;
		}


	}
	export class IOMailFrom{
		type:string;
		fid:number;
		legion:number;
	}

	export class IOGeneralSimple{
		pos:number;
		gsid:number;
		level:number;
		hpcover:number;
		class:number;
		nowhp:number;
		exhp:number;
		exatk:number;
	}

	export class S2CAffairReel{
		scroll_id:number;
		static id=336;
	}

	export class S2CFriendBattleStart{
		seed:number;
		battleid:number;
		season:number;
		static id=1066;
	}

	export class S2CActivitiesGetAward{
		activeIdType:number;
		item_index:number;
		gain:Array<ActivitiesItem>;
		lottery_is_pay:boolean;
		static id=1204;
	}

	export class S2CLegionBossBattleStart{
		seed:number;
		battleid:number;
		season:number;
		static id=1052;
	}

	export class IOTnqwEvent{
		mark:number;
		limit:number;
		intro:string;
	}

	export class S2CTreasureTakeon{
		guid:number;
		treasure_id:number;
		general_bean:IOGeneralBean;
		static id=386;
	}

	export class S2CMapEventList{
		list:{[key:number]:IOMapEvent};
		static id=542;
	}

	export class S2CDungeonChooseNode{
		secret:number;
		type:number;
		detail:IODungeonChooseDetail;
		static id=1112;
	}

	export class S2CDungeonChapterReward{
		reward:Array<IORewardItem>;
		static id=1108;
	}

	export class S2CBigBattleInfo{
		items:Array<IOGeneralSimple>;
		static id=572;
	}

	export class IOLegionFactoryDonation{
		name:string;
		icon:number;
		headid:number;
		frameid:number;
		score:number;
		pos:number;
		last:number;
	}

	export class S2CItemList{
		item_list:Array<SimpleItemInfo>;
		static id=302;
	}

	export class IOBattleSet{
		mythic:number;
		power:number;
		team:{[key:number]:IOGeneralBean};
	}

	export class S2CGuideOne{
		isHas:boolean;
		static id=1006;
	}

	export class S2CListOnlineAward{
		need_online_time:number;
		reward_index:number;
		static id=806;
	}

	export class S2CGetSzhc{
		list:Array<IOSzhc>;
		static id=1240;
	}

	export class S2CGetTnqwInfo{
		event:Array<IOTnqwEvent>;
		bosslist:Array<IOTnqwBosslist>;
		static id=1242;
	}

	export class S2CDjrwInfo{
		list:Array<IODjrw1>;
		static id=1254;
	}

	export class IODungeonPotion{
		id:number;
		count:number;
	}

	export class S2CGetKpMissionAward{
		rewards:Array<IORewardItem>;
		static id=1152;
	}

	export class S2CSecretGetAward{
		stage_index:number;
		reward_items:Array<SecretItemInfo>;
		static id=3308;
	}

	export class IOMjbgFinal1{
		gsid:number;
		count:number;
		maxnum:number;
	}

	export class IORewardItemSelect{
		GSID:number;
		COUNT:number;
		real:boolean;
	}

	export class S2C4399PayPre{
		goodsId:number;
		rmb:number;
		cpOrderId:string;
		extra:string;
		goodsName:string;
		static id=156;
	}

	export class IOMineHolder{
		rid:number;
		rname:string;
		level:number;
		iconid:number;
		frameid:number;
		fight:number;
		cd_time:number;
	}

	export class S2CServerList{
		recommands:Array<number>;
		has_roles:Array<IOServerHasRole>;
		alls:Array<ServerListItem>;
		static id=58;
	}

	export class AwardItem{
		gsid:number;
		count:number;
	}

	export class S2CLegionFactoryMissionFinish{
		items:Array<IORewardItem>;
		static id=662;
	}

	export class IOBattleReport{
		left:Array<IOBattleReportItem>;
		right:Array<IOBattleReportItem>;
	}

	export class IOGuozhanHistory{
		action_type:number;
		target_player_name:string;
		params:Array<number>;
		add_time:number;
	}

	export class S2CQunheiPayPre{
		charge_index:number;
		ext:string;
		sign:string;
		goodsName:string;
		rmb:number;
		static id=130;
	}

	export class IORankPlayer{
		rid:number;
		rname:string;
		iconid:number;
		headid:number;
		frameid:number;
		level:number;
		power:number;
		vip:number;
		rank_change:number;
		hero_stars:number;
		win_count:number;
		score:number;
		damage:number;
		tower:number;
		like_count:number;
		chapter:number;
		node:number;
		stage:number;
		star:number;
	}

	export class S2CLegionSearch{
		legion1:IOLegionInfo;
		static id=628;
	}

	export class S2CGeneralWuhun{
		static id=394;
	}

	export class S2COccTaskJobSelect{
		static id=562;
	}

	export class S2CTeamsInfoSet{
		static id=186;
	}

	export class IORecruitFree{
		normal:number;
		premium:number;
	}

	export class S2CTgslBossStart{
		seed:number;
		battleid:number;
		season:number;
		static id=1078;
	}

	export class IOPvpRecord{
		videoid:number;
		time:number;
		version:number;
		seed:number;
		result:string;
		season:IOBattleRecordSeason;
		lper:{[key:number]:number};
		rper:{[key:number]:number};
		ltper:number;
		rtper:number;
		report:IOBattleReport;
		left:IOBattleRecordSide;
		right:IOBattleRecordSide;
		mark:string;
	}

	export class S2CGetNoviceTrainAward{
		rewards:Array<AwardItem>;
		static id=556;
	}

	export class S2CFriendBossFarm{
		ret:S2CFriendExploreData;
		static id=478;
	}

	export class S2CGzCzlbList{
		list:Array<IOCzlb>;
		static id=1232;
	}

	export class IOSecretBoxAward{
		stage_index:number;
		award_list:Array<SecretItemInfo>;
		is_get:number;
	}

	export class S2CMjbgInfo{
		djgsid:number;
		djcount:number;
		index:number;
		items:Array<IOMjbgItem>;
		strbox:string;
		finallist:Array<IOMjbgFinal>;
		list:Array<IOMjbgSource>;
		_final:IOMjbgItem;
		static id=1262;
	}

	export class S2CZMPayCheck{
		fee_id:string;
		check:string;
		extradata:string;
		goodsName:string;
		rmb:number;
		static id=148;
	}

	export class IOSrenderState{
		gsid:number;
		loyal:number;
		state:number;
	}

	export class S2CLegionDestroy{
		static id=644;
	}

	export class S2CGuozhanBattleView{
		city_index:number;
		enemy_level:number;
		base_info:IOGuozhanPointPlayer;
		battleset:IOBattlesetEnemy;
		static id=3454;
	}

	export class S2COccTaskGiftOne{
		rewards:Array<RewardInfo>;
		static id=564;
	}

	export class IOLegionBoss{
		chapter:number;
		name:string;
		rewards:Array<IORewardItem>;
		bset:{[key:number]:IOGeneralLegion};
		maxhp:number;
		nowhp:number;
	}

	export class IOManorFieldEnemy{
		boxItem:Array<IORewardItem>;
		reward:Array<IORewardItem>;
		id:number;
		state:number;
		hasOpen:number;
		cachehp:Array<number>;
	}

	export class S2CTowerBattleInfo{
		items:Array<IOGeneralSimple>;
		static id=518;
	}

	export class IOSpecial{
		bw:number;
		qz:number;
		zx:number;
		lm:number;
		yd:number;
		cz:number;
		gz:number;
		zz:number;
	}

	export class PushGeneralAddLevel{
		general_uuid:number;
		level:number;
		new_power:number;
		static id=364;
	}

	export class IOMjbgItem{
		gsid:number;
		count:number;
		num:number;
	}

	export class S2CSelectServer{
		ret:number;
		server_id:number;
		has_role:boolean;
		static id=62;
	}

	export class S2CSrenderBehead{
		awards:Array<IORewardItem>;
		static id=878;
	}

	export class S2CMonthBossInfo{
		index:number;
		general:number;
		level:number;
		bset:Array<IOGeneralSimple>;
		rewards:Array<IORewardItem>;
		lastdamge:number;
		maxhp:number;
		nowhp:number;
		static id=580;
	}

	export class S2CGetGjdl{
		list:Array<IOCjxg1>;
		static id=1252;
	}

	export class S2CGeneralAddClass{
		general_uuid:number;
		general_bean:IOGeneralBean;
		static id=360;
	}

	export class S2CQueryHasRole{
		has_role:boolean;
		qq_nickname:string;
		static id=104;
	}

	export class S2CMailList{
		list:Array<IOMailInfo>;
		static id=702;
	}

	export class S2CPlayerKickOff{
		static id=112;
	}

	export class IOWorldBossLegion{
		rank:number;
		maxrank:number;
		damage:number;
	}

	export class S2CDungeonChooseBuf{
		buffid:number;
		static id=1116;
	}

	export class S2CDelMail{
		static id=708;
	}

	export class S2CMineList{
		level_index:number;
		page_index:number;
		my_income:Array<number>;
		my_hold:Array<number>;
		my_cd_time:Array<number>;
		mine_points:Array<IOMintPoint>;
		static id=3102;
	}

	export class S2CMjbgChange{
		gsid:number;
		count:number;
		static id=1264;
	}

	export class IOBattleRecordSeason{
		season:number;
		left:number;
		right:number;
		pos:Array<number>;
	}

	export class S2CTowerReplay{
		records:Array<IOPvpRecord>;
		static id=550;
	}

	export class S2CAffairList{
		item_list:Array<IOAffairItem>;
		static id=332;
	}

	export class IOTeamInfo{
		type:number;
		pos_card_uuid:Array<number>;
		pet_id:number;
	}

	export class S2CManorFriendList{
		list:Array<IOManorFriend>;
		static id=880;
	}

	export class S2CMzlbList{
		end:number;
		items:Array<IOLiBao1>;
		static id=1218;
	}

	export class S2CCzlbList{
		list:Array<IOCzlb>;
		static id=1230;
	}

	export class S2COfflineAwardDouble{
		add_level:number;
		static id=150;
	}

	export class S2CWorldBossFarm{
		rewards:Array<IORewardItem>;
		static id=672;
	}

	export class IOLegionFactoryMission{
		key:number;
		id:number;
		stime:number;
		etime:number;
	}

	export class S2CTowerAchieveReward{
		rewards:Array<IORewardItem>;
		static id=524;
	}

	export class S2CAffairRedNotice{
		ret:boolean;
		static id=346;
	}

	export class S2CMineBattleStart{
		seed:number;
		battleid:number;
		season:number;
		static id=1084;
	}

	export class S2CGetCxryInfo{
		zf:IOCxryZf;
		savegenerals:Array<IOCxryGenerals>;
		gnummax:number;
		static id=1276;
	}

	export class S2CWanBaGetBalance{
		is_balance_enough:boolean;
		defaultScore:number;
		itemid:number;
		cpOrderId:string;
		goodsName:string;
		static id=162;
	}

	export class S2CLegionTechReset{
		items:Array<IORewardItem>;
		static id=650;
	}

	export class S2COccTaskInfo{
		index:number;
		occtype:number;
		rewards:Array<RewardInfo>;
		list:Array<IOOccTask1>;
		reward:Array<RewardInfo>;
		refcost:RewardInfo;
		packinfo:IOOcctaskPackinfo;
		prewards:Array<number>;
		static id=558;
	}

	export class S2CKeyConvert{
		result_id:number;
		reward_item:Array<AwardItem>;
		static id=810;
	}

	export class S2CMapEventFinish{
		ret:number;
		reward:Array<IORewardItem>;
		static id=544;
	}

	export class S2CUseItem{
		status:number;
		reward:Array<RewardInfo>;
		static id=304;
	}

	export class IODjrwChk{
		MARK:number;
		NUM:number;
	}

	export class S2CGetAnnounce{
		content:string;
		static id=60;
	}

	export class S2CUserInfoStruct{
		id:number;
		uid:string;
		servid:number;
		rname:string;
		sex:number;
		iconid:number;
		headid:number;
		frameid:number;
		imageid:number;
		gold:number;
		yb:number;
		level:number;
		exp:number;
		vip:number;
		vipexp:number;
		power:number;
		firstcharge:number;
		create:number;
		time:number;
		guideProgress:number;
		maxmapid:number;
		nowmapid:number;
		tower:number;
		bagspace:number;
		online:number;
		hides:Array<SimpleItemInfo>;
		others:Array<SimpleItemInfo>;
		recruitfree:IORecruitFree;
		lastgain:number;
		battlearr:Array<IOBattleFormation>;
		pvpscore:number;
		legion:number;
		gnum:number;
		fbossphys:number;
		tech:{[key:number]:number};
		dgtop:IODungeonTop;
		occtaskend:number;
		occtask:IOOcctask;
		special:IOSpecial;
		guozhan_pvp:boolean;
		ydend:number;
		static id=110;
	}

	export class S2CKpBattleEnd{
		stageinfo:IOStageInfo;
		resultlist:Array<IOBattleResult>;
		reward:Array<IORewardItemSelect>;
		static id=1100;
	}

	export class IOExpedPlayer{
		rname:string;
		level:number;
		iconid:number;
		headid:number;
		frameid:number;
		power:number;
		battleset:IOBattlesetEnemy;
	}

	export class KvStringPair{
		key:string;
		val:number;
	}

	export class IOpstatus{
		send:number;
		receive:number;
	}

	export class S2CPlayerByParam{
		legion:number;
		gnum:number;
		bagspace:number;
		tech:{[key:number]:number};
		dgtop:IODungeonTop;
		frameid:number;
		static id=1016;
	}

	export class S2CManorYijianFarm{
		static id=540;
	}

	export class S2CExpedStatue{
		hp:{[key:number]:number};
		wish:Array<number>;
		static id=532;
	}

	export class S2CGeneralDecomp{
		general_uuid:Array<number>;
		rewards:Array<RewardInfo>;
		static id=366;
	}

	export class S2CAffairAcce{
		affair_index:number;
		item_list:Array<IOAffairItem>;
		static id=342;
	}

	export class S2CGetMailAttach{
		rewards:Array<IOMailAttach>;
		static id=704;
	}

	export class IOTnqwBosslist{
		status:number;
		actscore:number;
		rewardgsids:Array<string>;
	}

	export class S2CLegionFactoryDonateGet{
		items:Array<IOLegionFactoryDonation>;
		static id=654;
	}

	export class S2CGuideChooseReward{
		rewards:Array<SimpleItemInfo>;
		static id=1010;
	}

	export class IODungeonNode{
		poslist:Array<IODungeonNodePos>;
	}

	export class S2CChapterReap{
		rewards:Array<IORewardItem>;
		static id=504;
	}

	export class PushMineRob{
		level_index:number;
		point_index:number;
		target_player_id:number;
		target_player_name:string;
		loose_items:Array<IORewardItem>;
		static id=3106;
	}

	export class S2CDjjfInfo{
		looplimit:number;
		currentloop:number;
		missions:Array<IODjjfMission>;
		static id=1234;
	}

	export class IODungeonPosition{
		node:number;
		pos:number;
	}

	export class IOBattleReportItem{
		gsid:number;
		hurm:number;
		heal:number;
		level:number;
		class:number;
	}

	export class S2CGuoZhanCityMove{
		city_index:number;
		move_step:number;
		occupy_enemy:boolean;
		reward:Array<IORewardItem>;
		static id=3468;
	}

	export class S2CGetOtherPlayerInfo{
		items:Array<IOOtherPlayer>;
		static id=200;
	}

	export class S2CTnqwBossStart{
		seed:number;
		battleid:number;
		season:number;
		static id=1074;
	}

	export class S2CRankLike{
		targetPlayerId:number;
		likeCount:number;
		rewards:Array<RewardInfo>;
		static id=158;
	}

	export class S2CLegionCreate{
		legion_id:number;
		static id=622;
	}

	export class PushPaymentResult{
		yb:number;
		pid:string;
		rewards:Array<RewardInfo>;
		static id=132;
	}

	export class IOGuoZhanPvpPlayer{
		base_info:GuozhanOfficePointPlayer;
		battleset:IOBattlesetEnemy;
	}

	export class IOLegionBossSelf{
		lastdamge:number;
	}

	export class S2CGetVipGift{
		rewards:Array<RewardInfo>;
		static id=124;
	}

	export class S2CDungeonChapterInfo{
		position:IODungeonPosition;
		nodelist:Array<IODungeonNode>;
		potion:Array<IODungeonPotion>;
		bufflist:IODungeonBuffList;
		spbufflist:IODungeonBuffList;
		static id=1110;
	}

	export class IOStageInfo{
		schange:number;
		stage:number;
		star:number;
	}

	export class S2CGuozhanBattleStart{
		seed:number;
		battleid:number;
		season:number;
		static id=1088;
	}

	export class IOOcctaskPackinfo{
		ID:number;
		ITEMS:Array<RewardInfo>;
		TYPE:number;
		VALUE:number;
		WEIGHT:number;
	}

	export class S2CMythicalReset{
		rewards:Array<IORewardItem>;
		mythicals:Array<IOMythicalAnimal>;
		static id=760;
	}

	export class S2CShopRefresh{
		shop_type:string;
		item_list:Array<IOShopItem>;
		static id=326;
	}

	export class IOAwardRandomGeneral{
		COUNT:number;
		STAR:number;
		CAMP:number;
		OCCU:number;
	}

	export class S2CGeneralTakeonEquip{
		action_type:number;
		general_uuid:number;
		equip_id:number;
		pos_index:number;
		general_bean:IOGeneralBean;
		static id=356;
	}

	export class S2CMineHistory{
		records:Array<IOMineHistory>;
		static id=3110;
	}

	export class IOFriendBoss{
		ownPlayerId:number;
		id:number;
		gsid:number;
		name:string;
		level:number;
		rewards:Array<IORewardItem>;
		etime:number;
		last:number;
		maxhp:number;
		nowhp:number;
		lastdamges:number;
		bset:{[key:number]:IOGeneralSimple};
	}

	export class S2CChapterBattleStart{
		seed:number;
		battleid:number;
		season:number;
		static id=508;
	}

	export class PushSmallTips{
		colorType:number;
		content:string;
		static id=160;
	}

	export class S2CHeroChoose1in3{
		rewards:Array<SimpleItemInfo>;
		static id=1014;
	}

	export class S2CGoldBuyList{
		buy_seconds:Array<number>;
		static id=188;
	}

	export class S2CGeneralAddStar{
		target_gsid:number;
		general_uuid:number;
		cost_generals:Array<number>;
		items:Array<RewardInfo>;
		general_bean:IOGeneralBean;
		static id=362;
	}

	export class S2CTnqwBossEnd{
		result:IOBattleResult;
		reward:Array<IORewardItem>;
		static id=1076;
	}

	export class S2CSecretReset{
		static id=3312;
	}

	export class S2CChatVisit{
		static id=608;
	}

	export class S2CManorBuild{
		static id=860;
	}

	export class S2CLegionApplyReview{
		ret:Array<IOLegionApplyReview>;
		static id=636;
	}

	export class S2CFriendshipOnekey{
		rewards:Array<IORewardItem>;
		static id=470;
	}

	export class S2CGuideStepSet{
		static id=118;
	}

	export class IOMapEvent{
		hash:string;
		type:number;
		eid:number;
	}

	export class S2CActivityList{
		fixed_activities:Array<FixedActivityInfo>;
		dynamic_activities:Array<DynamicActivityInfo>;
		static id=1202;
	}

	export class S2CSrenderList{
		list:{[key:number]:IOSrenderState};
		static id=874;
	}

	export class IOGuozhanPointPlayer{
		rid:number;
		rname:string;
		level:number;
		iconid:number;
		frameid:number;
		fight:number;
	}

	export class PushFirstPay{
		static id=134;
	}

	export class S2CLegionQuit{
		static id=638;
	}

	export class S2CDungeonOpenTime{
		open:Array<number>;
		static id=1102;
	}

	export class S2COccTaskGiftAll{
		rewards:Array<RewardInfo>;
		static id=566;
	}

	export class KvIntPair{
		key:number;
		value:number;
	}

	export class PushGuideInfo{
		guideInfo:Array<GuideStepInfo>;
		static id=116;
	}

	export class S2CDrawList{
		static id=192;
	}

	export class IODungeonChooseDetail{
		id:number;
		name:string;
		gsid:number;
		set:{[key:number]:IODungeonBset};
		hppercent:{[key:number]:number};
		buffs:Array<number>;
		disc:number;
		item:Array<IORewardItem>;
		consume:Array<IORewardItem>;
		quality:number;
		refnum:number;
		goods:Array<IORewardItem>;
	}

	export class S2CTestLogin{
		uid:number;
		session_id:number;
		static id=52;
	}

	export class S2CChongChongPayPre{
		goodsId:number;
		egretOrderId:string;
		money:number;
		ext:string;
		goodsName:string;
		static id=154;
	}

	export class S2CGoldBuy{
		buy_type:number;
		rewards:Array<RewardInfo>;
		static id=190;
	}

	export class S2COpenExploreBoss{
		seed:number;
		battleid:number;
		season:number;
		static id=1060;
	}

	export class SimpleItemInfo{
		gsid:number;
		count:number;
	}

	export class S2CFriendBattleEnd{
		result:IOBattleResult;
		videoid:number;
		static id=1068;
	}

	export class PushIosIAPVerify{
		ret:number;
		static id=180;
	}

	export class S2CKpStart{
		seed:number;
		battleid:number;
		season:number;
		static id=1082;
	}

	export class IODungeonTop{
		chapter:number;
		node:number;
	}

	export class S2CShopList{
		item_list:Array<IOShopItem>;
		static id=322;
	}

	export class IOFriendChapter{
		status:number;
		chapterid:number;
		power:number;
		exploremin:number;
		friends:Array<IOFriendEntity>;
		etime:number;
	}

	export class S2CSpinBoxOpen{
		type:number;
		box_index:number;
		rewards:Array<IOSpinItem>;
		static id=406;
	}

	export class IODungeonGlobalBuf{
		hp:number;
		atk:number;
		def:number;
		mdef:number;
	}

	export class S2CBigBattleStart{
		seed:number;
		battleid:number;
		season:number;
		static id=574;
	}

	export class S2COfficialItemReward{
		rewards:Array<IORewardItem>;
		static id=854;
	}

	export class S2CCreateCharacter{
		ret:number;
		character_id:number;
		static id=106;
	}

	export class S2CGetCjxg1{
		list:Array<IOCjxg1>;
		static id=1238;
	}

	export class S2CGetTnqwBossInfo{
		name:string;
		gsid:number;
		level:number;
		challrewards:Array<RewardInfo>;
		killrewards:Array<RewardInfo>;
		nowhp:number;
		maxhp:number;
		lastdamge:number;
		last:number;
		bset:{[key:number]:IOGeneralLegion};
		static id=1244;
	}

	export class S2CGetCjxg2{
		list:Array<IOCjxg2>;
		static id=1224;
	}

	export class S2CChangeHeadFrame{
		head_frame_id:number;
		static id=174;
	}

	export class S2CManorGetBox{
		items:Array<IORewardItem>;
		boxItem:Array<IORewardItem>;
		static id=868;
	}

	export class S2CZmjfInfo{
		looplimit:number;
		currentloop:number;
		missions:Array<IODjjfMission>;
		static id=1248;
	}

	export class S2CGuideSave{
		static id=1008;
	}

	export class IOWorldBossSelf{
		rank:number;
		damage:number;
	}

	export class S2CGuozhanHistory{
		records:Array<IOGuozhanHistory>;
		static id=3472;
	}

	export class IOLegionMember{
		id:number;
		name:string;
		icon:number;
		headid:number;
		frameid:number;
		lv:number;
		lastest:number;
		pos:number;
		score:number;
		power:number;
		time:number;
	}

	export class S2CGetYuekaAward{
		rewards:Array<RewardInfo>;
		static id=136;
	}

	export class IOWorldBossWorldRank{
		sid:number;
		legion:number;
		lname:string;
		total:number;
		level:number;
		flag:number;
		index:number;
	}

	export class S2CQedjList{
		is_award_get:boolean;
		rewards:Array<RewardInfo>;
		static id=182;
	}

	export class S2CGeneralExchangeRefresh{
		guid:number;
		gsid:number;
		static id=382;
	}

	export class S2CFriendExploreData{
		maxbphys:number;
		maxqphys:number;
		boss:IOFriendBoss;
		chapters:Array<IOFriendChapter>;
		kill:number;
		static id=472;
	}

	export class PushAddGeneral{
		action:string;
		general_info:IOGeneralBean;
		static id=354;
	}

	export class S2CGetJfbx{
		box:Array<IOJfbxBox>;
		event:Array<IOJfbxEvent>;
		static id=1256;
	}

	export class S2CChangeHeadImage{
		image_id:number;
		static id=176;
	}

	export class S2CGeneralSummon{
		type:number;
		times:number;
		rewards:Array<RewardInfo>;
		static id=378;
	}

	export class S2CMonthBossBattleStart{
		seed:number;
		battleid:number;
		season:number;
		static id=582;
	}

	export class S2CFriendDel{
		role_id:number;
		static id=460;
	}

	export class S2CGxCzlbList{
		list:Array<IOCzlb>;
		static id=1226;
	}

	export class IOCzlb{
		value:number;
		price:number;
		items:Array<RewardInfo>;
		buytime:number;
		limit:number;
		special:Array<RewardInfo>;
		exp:number;
		path:string;
	}

	export class S2CChatPush{
		msgtype:number;
		senderid:string;
		rid:number;
		rname:string;
		iconid:number;
		headid:number;
		frameid:number;
		level:number;
		vip:number;
		office_index:number;
		serverid:number;
		content:string;
		videoId:number;
		send_time:number;
		id:number;
		static id=604;
	}

	export class IORewardItem{
		GSID:number;
		COUNT:number;
	}

	export class IOGuoZhanCity{
		player_id:number;
		player_name:string;
		player_size:number;
		nation_id:number;
		in_battle:boolean;
	}

	export class DynamicActivityInfo{
		activeBigID:number;
		priority:number;
		des:string;
		StartTime:number;
		EndTime:number;
		nComplete:number;
		sub_activities:Array<ActivityInfoOne>;
		hero_libao_config:Array<IoActivityHeroLiBao>;
		lottery_wheel_config:IoLotteryWheelConfig;
	}

	export class IOCjxg2{
		value:number;
		price:number;
		items:Array<RewardInfo>;
		buytime:number;
		icon:string;
		special:Array<RewardInfo>;
		bg1:string;
		bg2:string;
		hero:string;
		heroname:string;
		txbig:string;
		normal:string;
		check:string;
	}

	export class IOCjxg1{
		viple:number;
		consume:Array<RewardInfo>;
		items:Array<RewardInfo>;
		buytime:number;
	}

	export class S2CPvpBattleStart{
		seed:number;
		battleid:number;
		season:number;
		static id=514;
	}

	export class S2CGetOnlineAward{
		reward:Array<AwardItem>;
		static id=808;
	}

	export class S2CGuozhanBattleEnd{
		city_index:number;
		result:IOBattleResult;
		reward:Array<IORewardItem>;
		static id=1090;
	}

	export class IOSpinItem{
		GSID:number;
		COUNT:number;
		REPEAT:number;
	}

	export class S2CGmCmd{
		ret_code:number;
		static id=102;
	}

	export class S2CChangeName{
		name:string;
		static id=168;
	}

	export class S2CEnterGame{
		current_time_seconds:number;
		game_session_id:number;
		static id=108;
	}

	export class S2CHeadsFramesImagesList{
		_icons:string;
		heads:Array<number>;
		frames:Array<number>;
		images:Array<number>;
		static id=170;
	}

	export class IOServerHasRole{
		server_id:number;
		player_level:number;
	}

	export class S2CGuozhanOfficeView{
		player_list:Array<GuozhanOfficePointPlayer>;
		static id=3460;
	}

	export class S2CPayGjdl{
		static id=1270;
	}

	export class IOManorFieldBoss{
		state:number;
		lastdamage:number;
		maxhp:number;
		nowhp:number;
	}

	export class IOExclusive{
		level:number;
		skill:Array<number>;
		gsid:number;
		property:Array<KvStringPair>;
	}

	export class IOBattleResult{
		ret:string;
		round:number;
		lhp:{[key:number]:number};
		rhp:{[key:number]:number};
		lper:{[key:number]:number};
		rper:{[key:number]:number};
		ltper:number;
		rtper:number;
		rlosthp:number;
		report:IOBattleReport;
		version:number;
		left:IOBattleRetSide;
		right:IOBattleRetSide;
	}

	export class ServerListItem{
		id:number;
		name:string;
		ip_addr:string;
		port:number;
		status:number;
		port_ssl:number;
	}

	export class S2CAddDesktopShortcutAward{
		rewards:Array<RewardInfo>;
		static id=166;
	}

	export class S2CChatNewmsg{
		has_newmsg:boolean;
		static id=610;
	}

	export class S2CPvpBattleEnd{
		result:IOBattleResult;
		reward:Array<IORewardItemSelect>;
		spoints:number;
		schange:number;
		epoints:number;
		echange:number;
		videoid:number;
		static id=516;
	}

	export class GuozhanOfficePointPlayer{
		office_index:number;
		rid:number;
		rname:string;
		level:number;
		iconid:number;
		frameid:number;
		fight:number;
		hp_perc:number;
	}

	export class S2CScrollAnno{
		annos:Array<string>;
		static id=196;
	}

	export class IOLiBao1{
		price:number;
		buytime:number;
		items:Array<RewardInfo>;
	}

	export class IOBattleLine{
		power:number;
	}

	export class S2CMineRedNotice{
		ret:boolean;
		static id=3114;
	}

	export class S2CExpedBattleStart{
		seed:number;
		battleid:number;
		season:number;
		static id=528;
	}

	export class S2COccTaskFree{
		rewards:Array<RewardInfo>;
		static id=560;
	}

	export class S2CSpinBoxReset{
		type:number;
		static id=410;
	}

	export class S2CDungeonUsePotion{
		guid:number;
		hppercent:number;
		static id=1114;
	}

	export class S2CChangeHeadIcon{
		head_id:number;
		static id=172;
	}

	export class S2COfficialPromo{
		rewards:Array<IORewardItem>;
		static id=856;
	}

	export class S2CExclusiveRefreshBegin{
		general_uuid:number;
		lock_type:number;
		skill:Array<number>;
		property:Array<KvStringPair>;
		static id=374;
	}

	export class S2CGeneralReset{
		general_uuid:number;
		items:Array<RewardInfo>;
		general_bean:IOGeneralBean;
		static id=368;
	}

	export class IOBattleRecordSide{
		info:IOBattleRecordInfo;
		set:IOBattleRecordSet;
	}

	export class S2CDungeonBattleEnd{
		result:IOBattleResult;
		videoid:number;
		static id=1058;
	}

	export class PushQiZhenYiBaoKaiJiang{
		award_player:QiZhenYiBaoPlayer;
		hero_tpl_id:number;
		static id=1212;
	}

	export class S2CManorBattleStart{
		seed:number;
		battleid:number;
		season:number;
		static id=536;
	}

	export class S2CLegionAppoint{
		static id=640;
	}

	export class IOSecretHero{
		hero_type:number;
		hero_id:number;
		hp_percent:number;
	}

	export class IODungeonBuffList{
		ppthr:number;
		pskidam:number;
		atk:number;
		pcrid:number;
		pmthr:number;
		pcri:number;
	}

	export class S2CDrawRecruit{
		buy_type:number;
		times:number;
		rewards:Array<RewardInfo>;
		static id=194;
	}

	export class IOBattleFormation{
		f_type:string;
		mythic:number;
		items:Array<IOFormationGeneralPos>;
	}

	export class S2CBuySzhc{
		static id=1258;
	}

	export class S2CAffairStart{
		affair_index:number;
		item_list:Array<IOAffairItem>;
		static id=340;
	}

	export class IOCxryGenerals{
		gsid:number;
		isget:number;
	}

	export class S2CGuozhanOfficeDetail{
		office_index:number;
		base_info:GuozhanOfficePointPlayer;
		battleset:IOBattlesetEnemy;
		static id=3462;
	}

	export class S2CGjCzlbList{
		list:Array<IOCzlb>;
		static id=1228;
	}

	export class S2CGuideStep{
		static id=1004;
	}

	export class IOXsdh1{
		grid:number;
		grch:Array<RewardInfo>;
		consume:Array<RewardInfo>;
		buytime:number;
	}

	export class IOMjbgSource{
		intro:string;
		page:string;
	}

	export class S2CGuozhanCityCalculate{
		city_index:number;
		result:IOBattleResult;
		reward:Array<IORewardItem>;
		move_step:number;
		static id=1098;
	}

	export class S2CLegionGetBossInfo{
		boss:IOLegionBoss;
		rank:Array<IOLegionRank>;
		self:IOLegionBossSelf;
		static id=666;
	}

	export class S2CGuoZhanCityDetail{
		city_index:number;
		players:Array<IOGuoZhanPvpPlayer>;
		my_hp_perc:number;
		static id=3466;
	}

	export class GuideStepInfo{
		module:number;
		step:number;
	}

	export class S2CKpStageInfo{
		schange:number;
		stage:number;
		star:number;
		hstage:number;
		locate:Array<string>;
		promotion:Array<string>;
		static id=1158;
	}

	export class S2CLegionFactoryMissionStart{
		stime:number;
		etime:number;
		static id=660;
	}

	export class S2CGetCxryGeneralList{
		generallist:Array<number>;
		static id=1278;
	}

	export class S2CLegionList{
		legionlist:Array<IOLegionInfo>;
		static id=626;
	}

	export class S2CSpinList{
		type:number;
		is_force:boolean;
		items:Array<IOSpinItem>;
		free:number;
		static id=402;
	}

	export class IODungeonNodePos{
		type:number;
		choose:number;
		finish:number;
	}

	export class IOMythicalAnimal{
		tid:number;
		class:number;
		level:number;
		pskill:Array<number>;
	}

	export class S2CMjbgReward{
		gsid:number;
		count:number;
		static id=1266;
	}

	export class S2CTowerBattleEnd{
		result:IOBattleResult;
		items:Array<IORewardItem>;
		videoid:number;
		static id=522;
	}

	export class IOFriendEntity{
		id:number;
		rname:string;
		iconid:number;
		headid:number;
		frameid:number;
		level:number;
		vipLevel:number;
		power:number;
		lasttime:number;
		pstatus:IOpstatus;
	}

	export class S2CChat{
		static id=602;
	}

	export class S2CChapterBattleEnd{
		result:IOBattleResult;
		items:Array<IORewardItem>;
		static id=510;
	}

	export class S2CMailRedNotice{
		ret:boolean;
		static id=712;
	}

	export class IOOcctask{
		index:number;
		occtype:number;
		rewards:Array<RewardInfo>;
		list:Array<IOOccTask1>;
		reward:Array<RewardInfo>;
		refcost:RewardInfo;
		packinfo:IOOcctaskPackinfo;
		prewards:Array<number>;
	}

	export class S2CTreasureLevel{
		guid:number;
		treasure_id:number;
		general_bean:IOGeneralBean;
		static id=390;
	}

	export class S2CGuozhanChangeNation{
		old_nation:number;
		target_nation:number;
		change_nation_cd:number;
		static id=3458;
	}

	export class S2COpenLogin{
		open_id:string;
		session_id:number;
		platform_type:number;
		static id=56;
	}

	export class S2CFriendSearch{
		rname:string;
		items:Array<IOFriendEntity>;
		static id=456;
	}

	export class S2CKpSearchEnemy{
		hide:number;
		rid:number;
		star:number;
		stage:number;
		uid:string;
		servid:number;
		time:number;
		rname:string;
		power:number;
		iconid:number;
		frameid:number;
		level:number;
		vip:number;
		battleset:Array<IOBattlesetEnemy>;
		static id=1154;
	}

	export class S2CSetMailRead{
		static id=706;
	}

	export class IODjjfMission{
		NUM:number;
		cur:number;
		NAME:string;
		ITEMS:Array<RewardInfo>;
		status:number;
	}

	export class S2CSpinBuy{
		static id=408;
	}

	export class S2CWorldBossBattleEnd{
		result:IOBattleResult;
		rewards:Array<IORewardItem>;
		static id=1072;
	}

	export class IOManorBoss{
		bossid:number;
		maxhp:number;
		lastdamage:number;
		nowhp:number;
		bset:{[key:number]:IOGeneralSimple};
	}

	export class S2CLegionApply{
		id:number;
		static id=632;
	}

	export class S2CLegionApplyList{
		applylist:Array<IOLegionMember>;
		static id=634;
	}

	export class S2CExpedBattleEnd{
		result:IOBattleResult;
		drop:Array<IORewardItem>;
		reward:Array<IORewardItemSelect>;
		static id=530;
	}

	export class S2CFriendRecommandList{
		items:Array<IOFriendEntity>;
		static id=454;
	}

	export class S2CTreasureSell{
		treasure_id:number;
		count:number;
		static id=388;
	}

	export class S2CGetFbossResult{
		result:IOBattleResult;
		bosshurm:number;
		kill:number;
		static id=1062;
	}

	export class S2CStarGiftList{
		ret_list:Array<IOStarGift>;
		static id=1216;
	}

	export class S2CGetSignAward{
		awards:Array<AwardItem>;
		static id=804;
	}

	export class IOLevelGift{
		level:number;
		price:number;
		end:number;
		buytime:number;
		items:Array<RewardInfo>;
	}

	export class S2CHeroList{
		generals:Array<IOGeneralBean>;
		static id=352;
	}

	export class S2CLegionLog{
		list:Array<IOLegionLog>;
		static id=664;
	}

	export class S2CTnqwBossSweep{
		nowhp:number;
		reward:Array<RewardInfo>;
		static id=1246;
	}

	export class IOJfbxEvent{
		MARK:number;
		LIMIT:number;
		intro:string;
	}

	export class S2CExclusiveLevelUp{
		general_uuid:number;
		general_bean:IOGeneralBean;
		static id=370;
	}

	export class IOManorFriend{
		icon:number;
		frameid:number;
		headid:number;
		name:string;
		id:number;
		level:number;
		power:number;
	}

	export class S2CLegionFactoryMissionList{
		time:number;
		list:Array<IOLegionFactoryMission>;
		static id=656;
	}

	export class IOLegionLog{
		params:Array<string>;
		event:string;
		create:number;
	}

	export class S2CWorldBossInfo{
		legion:IOWorldBossLegion;
		boss:IOWorldBossInfo;
		self:IOWorldBossSelf;
		rank:Array<IOWorldBossRank>;
		worldrank:Array<IOWorldBossWorldRank>;
		static id=670;
	}

	export class IoLotteryWheelConfig{
		my_free_count:number;
		my_pay_count:number;
		daily_free_count:number;
		pay_count:number;
		pay_price:number;
		reward:Array<ActivitiesItem>;
	}

	export class S2CManorReward{
		items:Array<IORewardItem>;
		static id=864;
	}

	export class S2CExpedBattleInfo{
		mapkey:number;
		hp:{[key:number]:number};
		wish:Array<number>;
		map:IOExpedPlayer;
		static id=526;
	}

	export class S2CAffairAward{
		affair_index:number;
		rewards:Array<IORewardItem>;
		item_list:Array<IOAffairItem>;
		static id=344;
	}

	export class S2CGuozhanFightView{
		city_list:Array<IOGuoZhanCity>;
		my_city_index:number;
		move_step:number;
		nation_city_count:Array<number>;
		hp_perc:number;
		my_nation:number;
		change_nation_cd:number;
		my_office:number;
		static id=3464;
	}

	export class IOGeneralBean{
		guid:number;
		gsid:number;
		level:number;
		star:number;
		camp:number;
		occu:number;
		class:number;
		power:number;
		talent:Array<number>;
		affairs:number;
		treasure:number;
		property:IOProperty;
		equip:Array<number>;
		skill:Array<number>;
		exclusive:IOExclusive;
		hppercent:number;
	}

	export class S2CSecretBattleStart{
		map_id:number;
		online_formation:Array<IOSecretHero>;
		is_reset:number;
		static id=3304;
	}

	export class S2CManorReset{
		items:Array<IORewardItem>;
		static id=862;
	}

	export class S2COccTaskPackRefresh{
		refcost:RewardInfo;
		packinfo:IOOcctaskPackinfo;
		static id=568;
	}

	export class S2CManorMop{
		reward:Array<IORewardItem>;
		nowhp:number;
		static id=872;
	}

	export class S2CZhdTime{
		ret:{[key:string]:Array<number>};
		static id=1222;
	}

	export class S2CGxdbInfo{
		looplimit:number;
		currentloop:number;
		missions:Array<IODjjfMission>;
		static id=1236;
	}

	export class S2CPvpRecords{
		records:Array<IOPvpRecord>;
		static id=548;
	}

	export class IODungeonBset{
		gsid:number;
		level:number;
		class:number;
		exhp:number;
		exatk:number;
	}

	export class FixedActivityInfo{
		id:number;
		level_index:number;
		progress:number;
		can_get_award:boolean;
	}

	export class S2CMineBattleEnd{
		level_index:number;
		point_index:number;
		result:IOBattleResult;
		reward:Array<IORewardItem>;
		static id=1086;
	}

	export class S2CQiZhenYiBaoJoin{
		static id=1210;
	}

	export class IODungeonList{
		isget:number;
	}

	export class S2CGeneralExchangeGet{
		guid:number;
		gsid:number;
		static id=380;
	}

	export class S2CLegionSet{
		static id=624;
	}

	export class IOOccTask1{
		status:number;
		rewards:Array<RewardInfo>;
		intro:string;
		mark:number;
		limit:number;
		page:string;
		num:number;
	}

	export class S2CGetQunheiWxShareAward{
		rewards:Array<RewardInfo>;
		static id=138;
	}

	export class S2CBigBattleFarm{
		times:number;
		rewards:Array<IORewardItem>;
		static id=578;
	}

	export class S2CExclusiveRefreshGet{
		general_uuid:number;
		has_pending_property:boolean;
		skill:Array<number>;
		property:Array<KvStringPair>;
		static id=372;
	}

	export class S2CGuideEnd{
		static id=1012;
	}

	export class S2COccTaskPackRPay{
		static id=570;
	}

	export class IOStarGift{
		gstar:number;
		price:number;
		end:number;
		buytime:number;
		items:Array<RewardInfo>;
	}

	export class ActivitiesItem{
		nID:number;
		nNum:number;
	}

	export class S2CXsdhDh{
		static id=1274;
	}

	export class S2CPayCjxg1{
		static id=1260;
	}

	export class S2CQedjAward{
		rewards:Array<RewardInfo>;
		static id=184;
	}

	export class S2CMonthBossFarm{
		items:Array<IORewardItem>;
		static id=586;
	}

	export class IOBattleRecordInfo{
		rname:string;
		level:number;
		iconid:number;
		headid:number;
		frameid:number;
	}

	export class S2CFriendApplyHandle{
		is_agree:boolean;
		role_ids:Array<number>;
		static id=464;
	}

	export class S2CManorPatrol{
		static id=870;
	}

	export class WanbaLoginGift{
		wanba_gift_id:number;
		appid:string;
		openid:string;
		openkey:string;
		pf:string;
	}

	export class S2CGuozhanView{
		pass_city_index:Array<number>;
		player_city_index:number;
		my_nation:number;
		change_nation_cd:number;
		my_office:number;
		static id=3452;
	}

	export class S2CAffairLock{
		affair_index:number;
		item_list:Array<IOAffairItem>;
		static id=338;
	}

	export class IOLegionApplyReview{
		error:string;
	}

	export class S2CTowerBattleStart{
		seed:number;
		battleid:number;
		season:number;
		static id=520;
	}

	export class IOCxryZf{
		cur:number;
		max:number;
		prob:number;
	}

	export class IOJfbxBox{
		SCORE:number;
		state:number;
		REWARD:Array<RewardInfo>;
	}

	export class S2CManorFieldInfo{
		etime:number;
		btime:number;
		items:Array<IORewardItem>;
		mop:number;
		blv:number;
		elv:number;
		boss:IOManorFieldBoss;
		enemy:Array<IOManorFieldEnemy>;
		static id=866;
	}

	export class S2CKpSwitchOrder{
		static id=1156;
	}

	export class S2CLegionBossFarm{
		rewards:Array<IORewardItem>;
		static id=668;
	}

	export class IOSeason{
		etime:number;
		year:number;
		season:number;
		pos:Array<number>;
	}

	export class IOFormationGeneralPos{
		pos:number;
		general_uuid:number;
	}

	export class S2CBattleSeason{
		vals:Array<number>;
		info:IOSeason;
		static id=198;
	}

	export class S2CMonthBossBattleEnd{
		result:IOBattleResult;
		items:Array<IORewardItem>;
		damge:number;
		kill:number;
		static id=584;
	}

	export class PushPropChange{
		gsid:number;
		count:number;
		static id=114;
	}

	export class S2CMylbList{
		end:number;
		items:Array<IOLiBao1>;
		static id=1220;
	}

	export class S2CManorBattleEnd{
		result:IOBattleResult;
		reward:Array<IORewardItem>;
		kill:number;
		static id=538;
	}

	export class IOBHurt{
		gsid:number;
		hurm:number;
		heal:number;
		hp:number;
		born:number;
		hpperc:number;
		hpmax:number;
	}

	export class IoActivityHeroLiBao{
		buy_count:number;
		buy_count_total:number;
		favor_rate:number;
		price:number;
		recharge_id:number;
		extra_diamond:number;
		reward:Array<ActivitiesItem>;
	}

	export class S2CFriendList{
		maxcount:number;
		surplusgiftmax:number;
		arrfriend:Array<IOFriendEntity>;
		static id=452;
	}

	export class S2CFriendApplyList{
		items:Array<IOFriendEntity>;
		static id=462;
	}

	export class S2CFriendshipReceive{
		rewards:Array<IORewardItem>;
		static id=468;
	}

	export class IOWorldBossInfo{
		id:number;
		endtime:number;
		lastdamage:number;
		bset:{[key:number]:IOGeneralLegion};
		hasgift:number;
	}

	export class QiZhenYiBaoPlayer{
		player_id:number;
		player_name:string;
		count:number;
	}

	export class PushScrollAnno{
		msg_id:number;
		params:Array<string>;
		static id=1018;
	}

	export class PushGuoZhanPass{
		nation_id:number;
		static id=3470;
	}

	export class S2CTgslBossEnd{
		result:IOBattleResult;
		reward:Array<IORewardItem>;
		damge:number;
		kill:number;
		static id=1080;
	}

	export class PushActivityProgressUpdate{
		activeIdType:number;
		progress:number;
		static id=1206;
	}

	export class S2CSecretBattleEnd{
		is_win:boolean;
		reward_items:Array<SecretItemInfo>;
		is_interrupt:boolean;
		map_id:number;
		progress:number;
		boxAward:Array<IOSecretBoxAward>;
		my_cost:Array<IOSecretHero>;
		enemy_cost:Array<IOSecretHero>;
		online_formation:Array<IOSecretHero>;
		static id=3306;
	}

	export class S2CSetCxryGeneralList{
		static id=1280;
	}

	export class IOLegionInfo{
		id:number;
		lv:number;
		name:string;
		npnum:number;
		mpnum:number;
		minlv:number;
		ispass:boolean;
		ceo:string;
	}

	export class S2CLegionSign{
		reward:Array<IORewardItem>;
		exp:number;
		static id=646;
	}

	export class S2CMineDefFormationSave{
		mine_point_detail:S2CMineEnemyDetail;
		static id=3112;
	}

	export class S2CDungeonShopBuy{
		static id=1122;
	}

	export class S2CDungeonChapterList{
		dungeonlist:Array<IODungeonList>;
		dungeonset:Array<IOGeneralBean>;
		globalbuf:Array<IODungeonGlobalBuf>;
		formation_exist:boolean;
		static id=1104;
	}

	export class IOMineHistory{
		target_player_id:number;
		target_player_name:string;
		is_positive:boolean;
		is_success:number;
		mine_point:number;
		type:number;
		add_time:number;
		gain:Array<number>;
	}

	export class IOBattleRecordSet{
		mythic:IOMythicalAnimal;
		team:{[key:number]:IOGeneralBean};
	}

	export class S2CH5OpenLogin{
		session_id:number;
		static id=10032;
	}

	export class IOMintPoint{
		hold_player:IOMineHolder;
	}

	export class S2CPlayerBaseData{
		maxmapid:number;
		tower:number;
		pvpscore:number;
		gnum:number;
		bagspace:number;
		hiddens:Array<SimpleItemInfo>;
		special:IOSpecial;
		static id=1002;
	}

	export class S2CFriendReceiveExplore{
		rewards:Array<IORewardItem>;
		bossid:string;
		static id=476;
	}

	export class S2CMineGetAward{
		gain:Array<IORewardItem>;
		static id=3108;
	}

	export class S2CFriendApply{
		role_ids:Array<number>;
		static id=458;
	}

	export class S2CMineEnemyDetail{
		level_index:number;
		point_index:number;
		base_info:IOMineHolder;
		battleset:IOBattlesetEnemy;
		exclude_cards:Array<number>;
		rand_key:number;
		static id=3104;
	}

	export class S2CBagExpand{
		static id=310;
	}

	export class IODjrw1{
		knark:number;
		chk:Array<IODjrwChk>;
		cnum:number;
		name:string;
		items:Array<RewardInfo>;
		status:number;
	}

	export class S2CBigBattleEnd{
		result:IOBattleResult;
		rewards:Array<IORewardItem>;
		times:number;
		static id=576;
	}

	export class S2CShopBuy{
		awards:Array<SimpleItemInfo>;
		static id=324;
	}

	export class S2CDungeonBattleStart{
		seed:number;
		battleid:number;
		season:number;
		static id=1056;
	}

	export class PushNewMail{
		mail_info:IOMailInfo;
		static id=710;
	}

	export class S2CMythicalPskillUp{
		mythicals:Array<IOMythicalAnimal>;
		static id=758;
	}

	export class S2CGuozhanCityStart{
		seed:number;
		battleid:number;
		season:number;
		static id=1096;
	}

	export class S2C333PayPre{
		time:number;
		server:string;
		role:string;
		goodsId:string;
		goodsName:string;
		money:number;
		cpOrderId:string;
		sign:string;
		static id=152;
	}

	export class S2CDungeonShopList{
		list:Array<IODungeonShop>;
		static id=1120;
	}

	export class IOMjbgFinal{
		list:Array<IOMjbgFinal1>;
	}

	export class S2CFriendOpenExplore{
		etime:number;
		static id=474;
	}

	export class S2CSecretSoldierRevive{
		hero_id:number;
		static id=3310;
	}

	export class S2CGuozhanOfficCalculate{
		office_index:number;
		result:IOBattleResult;
		reward:Array<IORewardItem>;
		static id=1094;
	}

	export class S2COfficialSalary{
		rewards:Array<IORewardItem>;
		static id=852;
	}

	export class IOOtherPlayer{
		rid:number;
		rno:number;
		rname:string;
		sex:number;
		power:number;
		iconid:number;
		headid:number;
		frameid:number;
		imageid:number;
		level:number;
		vip:number;
		office_index:number;
		bestgeneral:Array<IOGeneralBean>;
		battleset:IOBattleSet;
		points:number;
		battlelines:Array<IOBattleLine>;
		kpBattleset:Array<IOBattleSet>;
	}

	export class S2CWorldBossBattleStart{
		seed:number;
		battleid:number;
		season:number;
		static id=1070;
	}

	export class S2CChargeInfo{
		is_first_pay:boolean;
		first_award_get:boolean;
		payment_level:Array<number>;
		vip_gift_get:Array<number>;
		is_long_yueka:boolean;
		long_yueka_get:boolean;
		yueka_left_day:number;
		yueka_get:boolean;
		static id=120;
	}

	export class IOWorldBossRank{
		headid:number;
		frameid:number;
		icon:number;
		level:number;
		power:number;
		damge:number;
		name:string;
		rid:number;
	}

	export class S2CBuyItem{
		rewards:Array<RewardInfo>;
		static id=312;
	}

	export class S2CIosIAPVerify{
		static id=178;
	}

	export class IOLegionRank{
		name:string;
		damge:number;
		power:number;
	}

	export class IOMailAttach{
		gsid:number;
		count:number;
	}

	export class S2CGeneralAddLevel{
		general_uuid:number;
		general_bean:IOGeneralBean;
		static id=358;
	}

	export class IOManorBuilding{
		lv:number;
		rid:number;
		id:number;
		type:number;
		pos:number;
		lastgain:number;
		items:Array<IORewardItem>;
	}

	export class IOBattlesetEnemy{
		mythic:IOMythicalAnimal;
		team:{[key:number]:IOGeneralBean};
	}

	export class IOLegionDonation{
		count:number;
		last:number;
	}

	export class IOSzhc{
		consume:Array<RewardInfo>;
		demand:Array<RewardInfo>;
		buytime:number;
	}

	export class S2CExclusiveRefreshConfirm{
		general_uuid:number;
		is_confirm:boolean;
		general_bean:IOGeneralBean;
		static id=376;
	}

	export class IOMailInfo{
		id:number;
		from:IOMailFrom;
		type:number;
		state:number;
		title:string;
		content:string;
		reward:Array<IOMailAttach>;
		stime:number;
		etime:number;
	}

	export class RewardInfo{
		GSID:number;
		COUNT:number;
	}

	export class S2CQiZhenYiBaoView{
		fixed_award:Array<ActivitiesItem>;
		big_award:ActivitiesItem;
		my_count:number;
		end_cd_time:number;
		player_count_kaijiang:number;
		cost_diamond:number;
		big_award_value:number;
		players:Array<QiZhenYiBaoPlayer>;
		history_players:Array<QiZhenYiBaoPlayer>;
		static id=1208;
	}

	export class PushMarquee{
		template_id:number;
		order:number;
		parameters:Array<string>;
		content:string;
		count:number;
		static id=140;
	}

	export class S2CDungeonSaveGeneralList{
		generals:Array<IOGeneralBean>;
		globalbuf:Array<IODungeonGlobalBuf>;
		reward:Array<IORewardItem>;
		potion:Array<IODungeonPotion>;
		static id=1106;
	}

	export class S2CErrorCode{
		msg_num:number;
		ret_code:number;
		static id=100;
	}

	export class S2CLegionTeckLv{
		static id=648;
	}

	export class S2CChapterInfo{
		items:Array<IOGeneralSimple>;
		static id=502;
	}

	export class S2CDungeonChooseShop{
		static id=1118;
	}

	export class S2CListRedPoints{
		has_redpoint:Array<number>;
		activity_redpoints:Array<number>;
		static id=128;
	}

	export class S2CLegionFactoryMissionUp{
		id:number;
		static id=658;
	}

	export class S2CSecretView{
		map_id:number;
		progress:number;
		could_reset:boolean;
		boxAward:Array<IOSecretBoxAward>;
		my_cost:Array<IOSecretHero>;
		enemy_cost:Array<IOSecretHero>;
		online_formation:Array<IOSecretHero>;
		revive_count:number;
		static id=3302;
	}

	export class S2CGuestLogin{
		uid:number;
		session_id:number;
		static id=54;
	}

	export class S2CGuozhanMove{
		city_index:number;
		static id=3456;
	}

	export class S2CMissionAchieveAward{
		rewards:Array<AwardItem>;
		static id=554;
	}

	export class S2CMythicalClassUp{
		mythicals:Array<IOMythicalAnimal>;
		static id=756;
	}

	export class S2CMjbgNext{
		static id=1268;
	}

	export class S2CAffairRefresh{
		item_list:Array<IOAffairItem>;
		static id=334;
	}

	export class IODungeonShop{
		chapter:number;
		node:number;
		disc:number;
		item:Array<IORewardItem>;
		consume:Array<IORewardItem>;
		quality:number;
	}

	export class IOShopItem{
		GSID:number;
		COUNT:number;
		BUYTIME:number;
		COIN:number;
		PRICE:number;
		DISCOUNT:number;
	}

	export class S2CManorEnemyInfo{
		boss:IOManorBoss;
		enemy:{[key:number]:IOGeneralSimple};
		static id=534;
	}

	export class S2CLegionBossBattleEnd{
		result:IOBattleResult;
		rewards:Array<IORewardItem>;
		last:number;
		damge:number;
		kill:number;
		static id=1054;
	}

	export class S2CSrenderPersua{
		loyal:number;
		reward:IORewardItem;
		static id=876;
	}

	export class IOBattleRetSide{
		info:IOBattleRecordInfo;
		set:IOBattleRetSet;
	}

	export class S2CSpinRoll{
		type:number;
		times:number;
		pos:number;
		items:Array<IOSpinItem>;
		static id=404;
	}

	export class S2CGeneralExchangeConfirm{
		action_type:number;
		general_bean:IOGeneralBean;
		static id=384;
	}

	export class S2CRankView{
		rtype:string;
		selfrank:number;
		my_rank_change:number;
		list:Array<IORankPlayer>;
		static id=144;
	}

	export class SecretItemInfo{
		itemId:number;
		cnt:number;
	}

	export class ActivityInfoOne{
		nSubCount:number;
		nNeedLevel:number;
		nNeedVipLevel:number;
		nNeedPassNoChapID:number;
		nState:number;
		nComplete:number;
		szSubDes:string;
		reward:Array<ActivitiesItem>;
	}

	export class S2CItemSell{
		status:number;
		reward:Array<RewardInfo>;
		static id=306;
	}

	export class S2CLegionFactoryLv{
		lv:number;
		exp:number;
		newman:number;
		rewards:Array<IORewardItem>;
		static id=652;
	}

	export class S2CLegionInfo{
		notice:string;
		power:number;
		minlv:number;
		lastmail:number;
		ispass:boolean;
		secretplace:number;
		wbosshistroyrank:number;
		servid:number;
		lastactive:number;
		name:string;
		donation:{[key:number]:IOLegionDonation};
		exp:number;
		fexp:number;
		flevel:number;
		kceo:boolean;
		level:number;
		maxexp:number;
		mpnum:number;
		id:number;
		members:Array<IOLegionMember>;
		ceo:string;
		nextmail:number;
		pos:number;
		static id=630;
	}

	export class S2CMythicalList{
		mythical_list:Array<IOMythicalAnimal>;
		static id=752;
	}

	export class S2CFriendshipGive{
		rewards:Array<IORewardItem>;
		static id=466;
	}

	export class S2CMythicalLevelUp{
		mythicals:Array<IOMythicalAnimal>;
		static id=754;
	}

	export class S2CBattleFormationSave{
		static id=512;
	}

	export class S2CFriendBattleInfo{
		mythic:IOMythicalAnimal;
		team:{[key:number]:IOGeneralBean};
		static id=1064;
	}

	export class S2CManorInfo{
		manorlv:number;
		buildings:Array<IOManorBuilding>;
		items:Array<IORewardItem>;
		static id=858;
	}

	export class S2CGuozhanOfficeStart{
		seed:number;
		battleid:number;
		season:number;
		static id=1092;
	}

	export class S2CGetPvpAgainst{
		rids:Array<number>;
		static id=546;
	}

	export class S2CMissionDailyAward{
		rewards:Array<AwardItem>;
		static id=552;
	}

	export class IOProperty{
		hp:number;
		atk:number;
		def:number;
		mdef:number;
		atktime:number;
		range:number;
		msp:number;
		pasp:number;
		pcri:number;
		pcrid:number;
		pdam:number;
		php:number;
		patk:number;
		pdef:number;
		pmdef:number;
		ppbs:number;
		pmbs:number;
		pefc:number;
		ppthr:number;
		patkdam:number;
		pskidam:number;
		pckatk:number;
		pmthr:number;
		pdex:number;
		pmdex:number;
		pmsatk:number;
		pmps:number;
		pcd:number;
	}

	export class S2CGetFirstPayAward{
		rewards:Array<RewardInfo>;
		static id=122;
	}

	export class S2CDownlineReconnect{
		ret:number;
		static id=126;
	}

	export class IOGeneralLegion{
		gsid:number;
		level:number;
		hpcover:number;
		class:number;
		exhp:number;
		exatk:number;
	}

	export class S2CTeamsInfoGet{
		teams:Array<IOTeamInfo>;
		static id=142;
	}

	export class S2CXsdhList{
		list:Array<IOXsdh1>;
		static id=1250;
	}

	export class S2CItemExchange{
		reward:Array<RewardInfo>;
		static id=314;
	}

	export class S2CChatView{
		chat_type:number;
		chat_content:Array<S2CChatPush>;
		new_msg_list:Array<boolean>;
		static id=606;
	}

	export class S2CChapterChange{
		mapid:number;
		static id=506;
	}

	export class S2CEquipComp{
		equip_id:number;
		count:number;
		static id=308;
	}

	export class S2CTgslInfo{
		gsid:number;
		name:string;
		level:number;
		mark:number;
		skill:Array<number>;
		challrewards:Array<number>;
		hp:number;
		bset:{[key:number]:IOGeneralLegion};
		static id=1272;
	}

	export class S2CBuyRight{
		static id=1020;
	}

	export class S2CLevelGiftList{
		ret_list:Array<IOLevelGift>;
		static id=1214;
	}

	export class IOAffairItem{
		id:number;
		gnum:number;
		gstar:number;
		hour:number;
		cond:Array<number>;
		reward:Array<IORewardItem>;
		lock:number;
		create:number;
		etime:number;
		arr:Array<number>;
	}

	export class IOBattleRetSet{
		index:number;
		team:{[key:number]:IOGeneralBean};
	}

	export class S2CGeneralReborn{
		items:Array<RewardInfo>;
		general:Array<IOAwardRandomGeneral>;
		static id=392;
	}

	export class S2CLegionDismiss{
		static id=642;
	}

	export class S2CListSignIn{
		sign_in_count:number;
		is_get:boolean;
		static id=802;
	}


}