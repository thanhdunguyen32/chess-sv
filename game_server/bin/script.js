http://localhost:8080/script?type=input

服务器列表状态重载
http://my.lejigames.com:8081/script?type=input

http://ml1.lejigames.com:8081/script?type=input

http://s1.lejigames.com.cn:8081/script?type=input

http://yunbee.lejigames.com:8081/script?type=input

http://douwan.lejigames.com:9081/script?type=input

http://185sy.lejigames.com:9081/script?type=input
//名将传说
http://changwei.lejigames.com:8502/script?type=input

var ServerListManager = Java.type("login.logic.ServerListManager");
ServerListManager.getInstance().reload();

Cross服务器列表重载
http://my.lejigames.com:8072/script?type=input
var CrossGsListManager = Java.type("game.logic.CrossGsListManager");
CrossGsListManager.getInstance().reload();

// 重载登录服务器配置
var LoginServer = Java.type("login.LoginServer");
LoginServer.getInstance().reloadServerConfig();

// 道具表重载
var ItemTemplateCache = Java.type("game.module.item.dao.ItemTemplateCache");
ItemTemplateCache.getInstance().reload();

// award掉落表重载
var AwardTemplateCache = Java.type("game.module.award.dao.AwardTemplateCache");
AwardTemplateCache.getInstance().reload();

//签到重载
var SignTemplateCache = Java.type("game.module.sign.dao.SignTemplateCache");
SignTemplateCache.getInstance().reload();

//周活动
var ActivityWeekTemplateCache = Java.type("game.module.activity.dao.ActivityWeekTemplateCache");
ActivityWeekTemplateCache.getInstance().reload();

// 重载游戏服务器配置
var GameServer = Java.type("game.GameServer");
var serverConfig = GameServer.getInstance().loadConfig();
GameServer.getInstance().getServerConfig().setLanAllowIps(serverConfig.getLanAllowIps());

//修复铭文融合引导
var GuideManager = Java.type("game.module.newguide.logic.GuideManager");
GuideManager.getInstance().makeRuneCombineGuideFinish();

// 关服保存玩家数据
var ArenaManager = Java.type("game.module.arena.logic.ArenaManager");
ArenaManager.getInstance().saveToDb();
var CharacterDownlineManager = Java.type("game.session.CharacterDownlineManager");
CharacterDownlineManager.getInstance().serverShutdown();

//竞技场排行
var RankCache = Java.type("game.module.rank.dao.RankCache");
var arenaRanks = RankCache.getInstance().getArenaPositionRanks();
output = arenaRanks.size()+"";

var ArenaManager = Java.type("game.module.arena.logic.ArenaManager");
var arenaRanks = ArenaManager.getInstance().getArenaPlayerList().subList(0, 100);
output = arenaRanks.size()+"\n";
for each(var aAreanItem in arenaRanks) {
	var arenaPlayer = aAreanItem.getArenaPlayer();
	if(arenaPlayer != null){
		output += "id:"+arenaPlayer.getPlayerId()+"    ";
	}
}

var ArenaManager = Java.type("game.module.arena.logic.ArenaManager");
var arenaRanks = ArenaManager.getInstance().getArenaPlayerList().subList(0, 100);
output = arenaRanks.size()+"\n";
for each(var aAreanItem in arenaRanks) {
	output += "id:"+aAreanItem.getMemberType()+"    ";
}

//VIP每日奖励
var VipManager = Java.type("game.module.vip.logic.VipManager");
VipManager.getInstance().execute(null);

//执行排行：	http://127.0.0.1:11001/script?type=input
var RankManager = Java.type("game.module.rank.logic.RankManager");
RankManager.getInstance().execute(null);

var PvpRankManager = Java.type("game.module.pvp.logic.PvpRankManager");
PvpRankManager.getInstance().execute(null);

var PvpRewardJob = Java.type("game.module.pvp.logic.PvpRewardJob");
PvpRewardJob.getInstance().execute(null);

var CraftRewardJob = Java.type("game.module.craft.logic.CraftRewardJob");
CraftRewardJob.getInstance().execute(null);

策划数据重载
var GameServer = Java.type("game.GameServer");
GameServer.getInstance().loadTemplates();



// 性能测试：
var RandomUtils = Java.type("org.apache.commons.lang3.RandomUtils");
var startTime = new Date().getTime();
for (var i = 0; i <= 10000; i++) {
	var randBytes = RandomUtils.nextBytes(RandomUtils.nextInt(1, 4000));
}
var endTime = new Date().getTime();
output=endTime-startTime;


发送推送:
var PushManager = Java.type("game.module.other.PushManager");
PushManager.getInstance().sendNotification("c0ee3b8def23aff19ba921f49fe2f49d","弹弹弹！");


移除玩家的错误道具
var SessionManager = Java.type("game.session.SessionManager");
var aPlayer = SessionManager.getInstance().getPlayer(745);
var ItemManager = Java.type("game.module.item.logic.ItemManager");
ItemManager.getInstance().subItem(aPlayer, 1066, 60);

var HeroBufferManager = Java.type("game.module.card.logic.HeroBufferManager");
HeroBufferManager.getInstance().getHeroFightVal(10,148);

//开启每日任务
var SessionManager = Java.type("game.session.SessionManager");
var aPlayer = SessionManager.getInstance().getPlayer(362);
var DailyMissionManager = Java.type("game.module.mission.daily.logic.DailyMissionManager");
DailyMissionManager.getInstance().checkUnlockGz(aPlayer, 40);

//var oldSessionId = aPlayer.getGamePlayer().getSessionId();
//if (oldSessionId != null) {
//	SessionManager.getInstance().removeLogicHero(oldSessionId);
//}
aPlayer.getGamePlayer().close();



通天塔重置
var towerBeanCache = Java.type("game.module.tower.logic.TowerBeanCache");
var towerBean = towerBeanCache.getInstance().getTowerBean(1764);
var TowerBean = Java.type("game.module.tower.bean.TowerBean");
towerBean.setAttackCount(0);





重置Boss挑战次数
var bossLogCache = Java.type("game.module.boss.dao.BossLogCache");
var bossLogCache = Java.type("game.module.boss.dao.BossLogCache");
var bossBean = bossLogCache.getInstance().getBossLog(1031);
var dbBossLog = bossBean.getDbBossLog();
if(dbBossLog != null){
	var bossLogInfos = dbBossLog.getBossLogInfoList();
	if(bossLogInfos != null){
		for(var i = 0; i < bossLogInfos.size(); i++ ){
			var bossLogInfo = bossLogInfos.get(i);
			bossLogInfo.setChannelCnt(0);
		}
	}
}







重置秘密基地
var secretCache = Java.type("game.module.secret.dao.SecretCache");
var secret = secretCache.getInstance().getSecret(39);
secret.setPointId(0);
secret.setCopyId(0);
var aPlayer = SessionManager.getInstance().getPlayer(39);
secret.setNpcLevel(aPlayer.getPlayerBean().getLevel());
var dbRandomAwardType = Java.type("game.module.secret.bean.DBRandomAward");
var dbRandomAward = new dbRandomAwardType();
secret.setRandomAward(dbRandomAward);
var dbrandomHerosBean = Java.type("game.module.secret.bean.DbOnFormaHeros");
var dbrandomHeros = new dbrandomHerosBean();
secret.setSecretHeroList(dbrandomHeros);
var dbUsedHeroBean = Java.type("game.module.secret.bean.DBUsedHero");
var dbUsedHero = new dbUsedHeroBean();
secret.setUsedHero(dbUsedHero);







跳过新手引导
var SessionManager = Java.type("game.session.SessionManager");
var aPlayer = SessionManager.getInstance().getPlayer(42);
aPlayer.getPlayerBean().setNewGuideStep(9);
aPlayer.getPlayerBean().setGuideSubStep(0);



查看英雄配置
var HeroTemplateCache = Java.type("game.module.hero.dao.HeroTemplateCache");
var heroTemplate = HeroTemplateCache.getInstance().getHeroTemplateById(2009);


var ItemCache = Java.type("game.module.item.dao.ItemCache");
var itemMap = ItemCache.getInstance().getItemTemplateKey(1310);
var output=itemMap;




打印玩家信息
var ReflectionToStringBuilder = Java.type("org.apache.commons.lang3.builder.ReflectionToStringBuilder");
var SessionManager = Java.type("game.session.SessionManager");
var aPlayer = SessionManager.getInstance().getPlayer(1263);
ReflectionToStringBuilder.toString(aPlayer.getPlayerBean());



保存所有在线玩家的数据
var CharacterDownlineManager = Java.type("game.session.CharacterDownlineManager");
CharacterDownlineManager.getInstance().serverShutdown();
var output = "success!";



发送消息
var SessionManager = Java.type("game.session.SessionManager");
var PushNoviceOpen = Java.type("game.module.question.ProtoMessageQuestion.PushNoviceOpen");
var aPlayer = SessionManager.getInstance().getPlayer(42);
aPlayer.getGamePlayer().writeAndFlush(10136, PushNoviceOpen.newBuilder().setNoviceId(102).build());

var SessionManager = Java.type("game.session.SessionManager");
var PushServerNotification = Java.type("game.module.question.ProtoMessageQuestion.PushServerNotification");
var aPlayer = SessionManager.getInstance().getPlayer(1950);
aPlayer.getGamePlayer().writeAndFlush(10704, PushServerNotification.newBuilder().setContent("【全服】走马灯公告！").setCount(5));



var ProcessorStatManager = Java.type("lion.common.ProcessorStatManager");
var output =ProcessorStatManager.getInstance().getStartTimeMap();


修改玩家等级
var SessionManager = Java.type("game.session.SessionManager");
var aPlayer = SessionManager.getInstance().getPlayer(42);
aPlyer.getPlayerBean().setLevel(10);




var TowerBeanCache = Java.type("game.module.tower.logic.TowerBeanCache");
var towerBean = TowerBeanCache.getInstance().getTowerBean(3);
towerBean.setIsGetBox(false);



var TowerBeanCache = Java.type("game.module.tower.logic.TowerBeanCache");
var String = Java.type("java.lang.String");
var towerBean = TowerBeanCache.getInstance().getTowerBean(3);
//towerBean.setIsGetBox(false);
towerBean.setFloor(14);
var output = String.valueOf(towerBean.getIsGetBox());




 
var ArenaManager = Java.type("game.module.arena.logic.ArenaManager");
ArenaManager.getInstance().saveToDb();




var ActivityManager = Java.type("game.module.activity.logic.ActivityManager");
ActivityManager.getInstance().getActivityAllBase();



var ActivityCache = Java.type("game.module.activity.dao.ActivityCache");
ActivityCache.getInstance().loadFromDb();


var ArenaManager = Java.type("game.module.arena.logic.ArenaManager");
ArenaManager.getInstance().arenaSendReward();


var TowerTemplateCache = Java.type("game.module.tower.logic.TowerTemplateCache");
var String = Java.type("java.lang.String");
var output = String.valueOf(TowerTemplateCache.getInstance().getTowerTemplateById(1));


重置怪物入侵次数
var enemyInvadeCache = Java.type("game.module.invade.dao.EnemyInvadeCache");
var enemyInvadeBean = enemyInvadeCache.getInstance().getEnemyInvade(1764);
enemyInvadeBean.setAttackCount(0);


// 新手引导处理
var SessionManager = Java.type("game.session.SessionManager");
var playerAll = SessionManager.getInstance().getAllPlayers();
var count = 0;
for each(var playingRole in playerAll) {
    if(playingRole.getPlayerBean().getNewGuideStep()==5 && playingRole.getPlayerBean().getGuideSubStep() == 6){
    count++;
		playingRole.getPlayerBean().setNewGuideStep(6);
		playingRole.getPlayerBean().setGuideSubStep(0);
	}
}
output = "parse:"+count;

//修复
var CardCache = Java.type("game.module.card.dao.CardCache");
var CardEntityDao = Java.type("game.module.card.dao.CardEntityDao");
var cardAll = CardCache.getInstance().getCardEntityAll(3);
var count = 0;
for each(var aCard in cardAll) {
    if(aCard.getId() == null){
		count++;
		CardEntityDao.getInstance().addCardEntity(aCard);
	}
}
output = "parse:"+count;

// update player set new_guide_step = 6,guide_sub_step = 0 where new_guide_step = 5 and guide_sub_step = 6;

//发放月卡
var SessionManager = Java.type("game.session.SessionManager");
var ChargeInfoManager = Java.type("game.module.pay.logic.ChargeInfoManager");
var pr = SessionManager.getInstance().getPlayer(54);
ChargeInfoManager.getInstance().addYueKa(pr);
pr.flush();

var SessionManager = Java.type("game.session.SessionManager");
var ChargeInfoManager = Java.type("game.module.pay.logic.ChargeInfoManager");
var pr = SessionManager.getInstance().getPlayer(12);
ChargeInfoManager.getInstance().addLongYueKa(pr);
pr.flush();

//首冲奖励
var SessionManager = Java.type("game.session.SessionManager");
var pr = SessionManager.getInstance().getPlayer(4763);
var PaymentManager = Java.type("game.module.pay.logic.PaymentManager");
PaymentManager.getInstance().firstRechargeSave(pr);

var SessionManager = Java.type("game.session.SessionManager");
var pr = SessionManager.getInstance().getPlayer(1948);
var PaymentManager = Java.type("game.module.pay.logic.PaymentManager");
PaymentManager.firstRechargeAward(pr);
pr.getGamePlayer().flush();

首冲
var PaymentManager = Java.type("game.module.pay.logic.PaymentManager");
PaymentManager.getInstance().payCallback(null, "4053", "20171225155612AT", 6, 1514188572,7, 1);

首冲3日
var SessionManager = Java.type("game.session.SessionManager");
var pr = SessionManager.getInstance().getPlayer(1948);
var ActivityManager = Java.type("game.module.activity.logic.ActivityManager");
ActivityManager.getInstance().add3DayFirstRecharge(pr);
pr.getGamePlayer().flush();

//每日充值
var SessionManager = Java.type("game.session.SessionManager");
var pr = SessionManager.getInstance().getPlayer(745);
var ActivityManager = Java.type("game.module.activity.logic.ActivityManager");
ActivityManager.getInstance().dailyRecharge(pr,60);
pr.getGamePlayer().flush();


//英雄礼包
var SessionManager = Java.type("game.session.SessionManager");
var ActivityManager = Java.type("game.module.activity.logic.ActivityManager");
var pr = SessionManager.getInstance().getPlayer(10);
ActivityManager.getInstance().heroLibao(pr,100,500);
pr.getGamePlayer().flush();
英雄礼包离线奖励
var SessionManager = Java.type("game.session.SessionManager");
var ActivityManager = Java.type("game.module.activity.logic.ActivityManager");
ActivityManager.getInstance().heroLibaoOffline(1952,101,"1");

首冲3倍
var SessionManager = Java.type("game.session.SessionManager");
var ActivityManager = Java.type("game.module.activity.logic.ActivityManager");
var pr = SessionManager.getInstance().getPlayer(2376);
ActivityManager.getInstance().doFirstRechargeTriple(2106,4);
pr.getGamePlayer().flush();

var SessionManager = Java.type("game.session.SessionManager");
var ActivityManager = Java.type("game.module.activity.logic.ActivityManager");
var pr = SessionManager.getInstance().getPlayer(2376);
ActivityManager.getInstance().isFirstRechargeTriple(2106,4);
pr.getGamePlayer().flush();

每日首冲
var SessionManager = Java.type("game.session.SessionManager");
var ActivityRecordManager = Java.type("game.module.activity.logic.ActivityRecordManager");
var pr = SessionManager.getInstance().getPlayer(2376);
ActivityRecordManager.getInstance().meiRiShouChong(pr);
pr.getGamePlayer().flush();


单笔充值
var SessionManager = Java.type("game.session.SessionManager");
var ActivityRecordManager = Java.type("game.module.activity.logic.ActivityRecordManager");
var pr = SessionManager.getInstance().getPlayer(2376);
ActivityRecordManager.getInstance().danBiChongZhi(pr,30000);
pr.getGamePlayer().flush();

连续充值
var SessionManager = Java.type("game.session.SessionManager");
var ActivityRecordManager = Java.type("game.module.activity.logic.ActivityRecordManager");
var pr = SessionManager.getInstance().getPlayer(2376);
ActivityRecordManager.getInstance().lianXuChongZhi(pr);
pr.getGamePlayer().flush();

var SessionManager = Java.type("game.session.SessionManager");
var pr = SessionManager.getInstance().getPlayer(1948);
output = pr+"";

var PlayerOnlineCacheMng = Java.type("game.session.PlayerOnlineCacheMng");
var pr = PlayerOnlineCacheMng.getInstance().getCache(0, "691970");
output = pr+"";

//跳过新手引导
var SessionManager = Java.type("game.session.SessionManager");
var pr = SessionManager.getInstance().getPlayer(1734);
if(pr != null){
	pr.getPlayerBean().setNewGuideStep(pr.getPlayerBean().getNewGuideStep()+1);
}
var PlayerOnlineCacheMng = Java.type("game.session.PlayerOnlineCacheMng");
var pb = PlayerOnlineCacheMng.getInstance().getCache(0, "4782");
if(pb != null){
	pb.setNewGuideStep(pb.getNewGuideStep()+1);
}

//夺宝引导修复
var TreasureRobCache = Java.type("game.module.tresure_rob.dao.TreasureRobCache");
var treasureRobEntity = TreasureRobCache.getInstance().getTreasureRobEntity(4);
output=treasureRobEntity.toString();
treasureRobEntity.setIsDoGuide(0);

//提升VIP等级
var SessionManager = Java.type("game.session.SessionManager");
var pr = SessionManager.getInstance().getPlayer(10);
var VipManager = Java.type("game.module.vip.logic.VipManager");
VipManager.getInstance().rechargeVipLev(pr, 40000);


//发道具
var SessionManager = Java.type("game.session.SessionManager");
var ItemManager = Java.type("game.module.item.logic.ItemManager");
var pr = SessionManager.getInstance().getPlayer(12);
ItemManager.getInstance().addItem(pr, 11170001, 15, pr.getId());


//发金币
var SessionManager = Java.type("game.session.SessionManager");
var CharacterInfoManager = Java.type("game.module.user.logic.CharacterInfoManager");
var pr = SessionManager.getInstance().getPlayer(1509);
if(pr != null){
	CharacterInfoManager.getInstance().changeCoins(pr, 100000, 0);
}
var PlayerOnlineCacheMng = Java.type("game.session.PlayerOnlineCacheMng");
var pb = PlayerOnlineCacheMng.getInstance().getCache(0, "4467");
if(pb != null){
	pb.setCoins(pb.getCoins()+100000);
}

//模拟充值
var SessionManager = Java.type("game.session.SessionManager");
var CharacterInfoManager = Java.type("game.module.user.logic.CharacterInfoManager");
var PushPaymentResult = Java.type("ws.WsMessageHall.PushPaymentResult");
var pr = SessionManager.getInstance().getPlayer(32);
var addDiamond = 3000;
if(pr != null){
	CharacterInfoManager.getInstance().changeDiamond(pr, addDiamond, 13);
	var respMsg = new PushPaymentResult(addDiamond,4,0);
	pr.getGamePlayer().write(respMsg.build(pr.alloc()));
}
var VipManager = Java.type("game.module.vip.logic.VipManager");
VipManager.getInstance().rechargeVipLev(pr, addDiamond);
//充值活动
var ActivityManager = Java.type("game.module.activity.logic.ActivityManager");
var pr = SessionManager.getInstance().getPlayer(1);
ActivityManager.getInstance().recharge(pr, addDiamond);
//每日充值30元活动
ActivityManager.getInstance().dailyRecharge(pr, addDiamond);
// 每日首充活动
var ActivityRecordManager = Java.type("game.module.activity.logic.ActivityRecordManager");
ActivityRecordManager.getInstance().meiRiShouChong(pr);
// 连续充值活动
ActivityRecordManager.getInstance().lianXuChongZhi(pr);
// 单笔充值活动
ActivityRecordManager.getInstance().danBiChongZhi(pr, addDiamond/10);
pr.flush();

//增加充值人数

var PaymentManager = Java.type("game.module.pay.logic.PaymentManager");
PaymentManager.getInstance().addPayPlayer(365);

var FriendCache = Java.type("game.module.friend.dao.FriendCache");
output=FriendCache.getInstance().getFriendCnt(30);
FriendCache.getInstance().removeFriendCnt(30);

//成长基金
var SessionManager = Java.type("game.session.SessionManager");
var ActivityManager = Java.type("game.module.activity.logic.ActivityManager");
var pr = SessionManager.getInstance().getPlayer(1950);
ActivityManager.getInstance().chengZhangJiJin(pr);

//开服竞赛奖励
var ActivityManager = Java.type("game.module.activity.logic.ActivityManager");
ActivityManager.getInstance().kaiFuJinSaiSendAward();

//冲级榜
var ActivityManager = Java.type("game.module.activity.logic.ActivityManager");
ActivityManager.getInstance().chongJiBangSendAward();

//敏感词过滤重载
var BadWordTemplateCache = Java.type("game.module.badword.dao.BadWordTemplateCache");
var BadWordFilter = Java.type("game.module.badword.logic.BadWordFilter");
var badWordTemplates = BadWordTemplateCache.getInstance().loadFromDb();
BadWordFilter.getInstance().reload(badWordTemplates);

//国战通关
var GuozhanPlayerManager = Java.type("game.module.guozhan.logic.GuozhanPlayerManager");
GuozhanPlayerManager.getInstance().gmPassGuoZhanPve(365);

//国战发奖励
var GuoZhanManager = Java.type("game.module.guozhan.logic.GuoZhanManager");
GuoZhanManager.getInstance().guozhanSendReward();

//国战城池争夺初始化
var GuoZhanFightManager = Java.type("game.module.guozhan.logic.GuoZhanFightManager");
GuoZhanFightManager.getInstance().playerInit(365,2);

//国战城池争夺奖励
var GuoZhanFightManager = Java.type("game.module.guozhan.logic.GuoZhanFightManager");
GuoZhanFightManager.getInstance().scheduleSendOccupyReward();

//更新步数
var GuozhanCache = Java.type("game.module.guozhan.dao.GuozhanCache");
var guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
var builder = guoZhanFight.toBuilder();
var guoZhanPlayer = builder.getPlayersOrDefault(2, null);
var playerBuilder = guoZhanPlayer.toBuilder();
playerBuilder.setMoveStep(30);
builder.putPlayers(2, playerBuilder.build());
guoZhanFight = builder.build();
GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);

//国战统一时间
var GuozhanCache = Java.type("game.module.guozhan.dao.GuozhanCache");
output += GuozhanCache.getInstance().getLastUnionRewardTime().toString();

//国战通关
var GuoZhanFightManager = Java.type("game.module.guozhan.logic.GuoZhanFightManager");
GuoZhanFightManager.getInstance().scheduleResetGuoZhanFight();

//月卡
var DailyMissionManager = Java.type("game.module.mission.daily.logic.DailyMissionManager");
DailyMissionManager.getInstance().addMonthCardDailyMission(365);

//年卡
var DailyMissionManager = Java.type("game.module.mission.daily.logic.DailyMissionManager");
DailyMissionManager.getInstance().addForeverMonthCardDailyMission(365);

//VIP每日任务
var DailyMissionManager = Java.type("game.module.mission.daily.logic.DailyMissionManager");
DailyMissionManager.getInstance().addVipDailyMission(365);

//月卡，年卡，VIP修复
var DailyMissionManager = Java.type("game.module.mission.daily.logic.DailyMissionManager");
DailyMissionManager.getInstance().gmFixYuekaNiankaVip();

//测试服充值反馈
insert into log_topup_feedback(`transaction_code`,`add_point`,`reward_diamond`,`player_id`,`open_id`,`server_id`,`product_id`,`add_time`,`status`) values('dk3d9slgsddlteisd',30,NULL,0,'4694',1,0,'2019-01-04 16:10:14',0);
insert into log_topup_feedback(`transaction_code`,`add_point`,`reward_diamond`,`player_id`,`open_id`,`server_id`,`product_id`,`add_time`,`status`) values('sddlgsldgsdl3sdf9sdldg',98,NULL,0,'4694',1,1,'2019-01-04 16:10:14',0);
insert into log_topup_feedback(`transaction_code`,`add_point`,`reward_diamond`,`player_id`,`open_id`,`server_id`,`product_id`,`add_time`,`status`) values('sdflgl43s9sdidflsdgalro',500,NULL,0,'4694',1,6,'2019-01-04 16:10:14',0);
insert into log_topup_feedback(`transaction_code`,`add_point`,`reward_diamond`,`player_id`,`open_id`,`server_id`,`product_id`,`add_time`,`status`) values('fk489w830sklfglh',1000,NULL,0,'4694',1,7,'2019-01-04 16:10:14',0);
insert into log_topup_feedback(`transaction_code`,`add_point`,`reward_diamond`,`player_id`,`open_id`,`server_id`,`product_id`,`add_time`,`status`) values('ffsl3d9sdlgnslgf',1500,NULL,0,'4694',1,8,'2019-01-04 16:10:14',0);
insert into log_topup_feedback(`transaction_code`,`add_point`,`reward_diamond`,`player_id`,`open_id`,`server_id`,`product_id`,`add_time`,`status`) values('ddsl30293489dsflgsls',98,NULL,0,'4694',1,1000,'2019-01-04 16:10:14',0);
insert into log_topup_feedback(`transaction_code`,`add_point`,`reward_diamond`,`player_id`,`open_id`,`server_id`,`product_id`,`add_time`,`status`) values('dkgl39sselsdgglseslcfl',50,NULL,0,'4694',1,101,'2019-01-04 16:10:14',0);
insert into log_topup_feedback(`transaction_code`,`add_point`,`reward_diamond`,`player_id`,`open_id`,`server_id`,`product_id`,`add_time`,`status`) values('dd939f9dsxfsdldfglhkazs',50,NULL,0,'4694',1,101,'2019-01-04 16:10:14',0);

//本地测试地址
http://127.0.0.1:11001/script?type=input
//保存世界地图数据
var WorldMapCache = Java.type("game.module.map.dao.WorldMapCache");
WorldMapCache.getInstance().save2WorldMapDb();

//每日重置 http://127.0.0.1:11001/script?type=input		 http://db.lejigames.com:11001/script?type=input
var DayResetManager = Java.type("game.module.bigbattle.logic.DayResetManager");
DayResetManager.getInstance().execute(null);

//领地野外重置 http://127.0.0.1:11001/script?type=input
var SessionManager = Java.type("game.session.SessionManager");
var pr = SessionManager.getInstance().getPlayer(2);
var ManorManager = Java.type("game.module.manor.logic.ManorManager");
ManorManager.getInstance().fieldReset(pr, true);

//武将招降说服 http://127.0.0.1:11001/script?type=input
var SurrenderPersuadeManager = Java.type("game.module.manor.logic.SurrenderPersuadeManager");
SurrenderPersuadeManager.getInstance().passChapter(2, 10210);

//武将招降说服 http://127.0.0.1:11001/script?type=input
var FriendManager = Java.type("game.module.friend.logic.FriendManager");
FriendManager.getInstance().generateFriendExploreBoss(2);

//竞技场每日奖励 http://127.0.0.1:11001/script?type=input
var PvpManager = Java.type("game.module.pvp.logic.PvpManager");
PvpManager.getInstance().execute(null);

//竞技场周奖励 http://127.0.0.1:11001/script?type=input
var PvpWeekRewardManager = Java.type("game.module.pvp.logic.PvpWeekRewardManager");
PvpWeekRewardManager.getInstance().execute(null);

//删除过期邮件 http://127.0.0.1:11001/script?type=input
var MailManager = Java.type("game.module.mail.logic.MailManager");
MailManager.getInstance().checkPastDueMail();

//远征重置 http://127.0.0.1:11001/script?type=input		http://changwei.lejigames.com:25101/script?type=input
var SessionManager = Java.type("game.session.SessionManager");
var pr = SessionManager.getInstance().getPlayer(1);
var ExpedManager = Java.type("game.module.exped.logic.ExpedManager");
ExpedManager.getInstance().expedReset(pr);

//PVP信息
//http://changwei.lejigames.com:25101/script?type=input
var PvpCache = Java.type("game.module.pvp.dao.PvpCache");
var pvpBean = PvpCache.getInstance().getPvpBean();
output = pvpBean.toString();

//邮件信息		http://changwei.lejigames.com:25101/script?type=input
var MailCache = Java.type("game.module.mail.dao.MailCache");
var mailAll = MailCache.getInstance().getMailCache(3);
mailAll.iterator().next().remove();
output = mailAll.toString();

output="";
var MailCache = Java.type("game.module.mail.dao.MailCache");
for (var i = 0; i <= 100; i++) {
	var mailAll = MailCache.getInstance().getMailCache(i);
	if(mailAll != null){
		for each(var aMail in mailAll) {
			if(aMail == null){
				output += "p:"+i+"\n";
			}
		}
	}
}

var MailDao = Java.type("game.module.mail.dao.MailDao");
var mailBeanList = MailDao.getInstance().getPlayerMailAll(3);
output = mailBeanList.toString();

var SessionManager = Java.type("game.session.SessionManager");
var pr = SessionManager.getInstance().getPlayer(67);
var pb = pr.getPlayerBean();
output = pb.toString();

//远征重置
truncate exped;
update player_other set count = 0 where gsid = 90153;
update player_other set count = 0 where gsid = 90154;
update player_other set count = 0 where gsid = 90155;
update player_other set count = 0 where gsid = 90156;

//王者演武赛季结束	http://127.0.0.1:11001/script?type=input
var KingPvpManager = Java.type("game.module.kingpvp.logic.KingPvpManager");
var pr = KingPvpManager.getInstance().kpSeasonEnd(3);

var ActivityMonthManager = Java.type("game.module.activity_month.logic.ActivityMonthManager");
var SessionManager = Java.type("game.session.SessionManager");
var pr = SessionManager.getInstance().getPlayer(2);
// ActivityMonthManager.getInstance().expedFinish(pr,30);
ActivityMonthManager.getInstance().pvpBattleEnd(pr,true,true);
pr.flush();

//月度活动重置 http://changwei.lejigames.com:25101/script?type=input
var ActivityMonthManager = Java.type("game.module.activity_month.logic.ActivityMonthManager");
ActivityMonthManager.getInstance().resetMonthActivity();