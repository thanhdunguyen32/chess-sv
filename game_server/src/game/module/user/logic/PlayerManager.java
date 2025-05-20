package game.module.user.logic;

import game.GameServer;
import game.entity.PlayingRole;
import game.module.activity.dao.ActCxryCache;
import game.module.activity.dao.ActMjbgCache;
import game.module.activity.dao.ActTnqwCache;
import game.module.activity.dao.ActivityXiangouCache;
import game.module.activity.logic.ActivityManager;
import game.module.activity_month.logic.ActivityMonthManager;
import game.module.affair.dao.AffairCache;
import game.module.bigbattle.dao.MonthBossCache;
import game.module.chapter.bean.ChapterBean;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.ChapterCache;
import game.module.chapter.logic.BattleFormationManager;
import game.module.chapter.logic.ChapterManager;
import game.module.chat.logic.ChatManager;
import game.module.draw.dao.PubDrawCache;
import game.module.draw.logic.PubDrawManager;
import game.module.dungeon.dao.DungeonCache;
import game.module.dungeon.logic.DungeonManager;
import game.module.exped.dao.ExpedCache;
import game.module.friend.dao.FriendCache;
import game.module.friend.dao.FriendExploreCache;
import game.module.friend.logic.FriendConstants;
import game.module.guide.logic.GuideManager;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuoZhanManager;
import game.module.guozhan.logic.GuozhanPlayerManager;
import game.module.hero.dao.GeneralCache;
import game.module.hero.dao.GeneralExchangeCache;
import game.module.hero.processor.GeneralListProcessor;
import game.module.item.dao.ItemCache;
import game.module.item.logic.BagManager;
import game.module.item.logic.ItemConstants;
import game.module.item.processor.ItemListProcessor;
import game.module.lan.bean.LoginSessionBean;
import game.module.lan.bean.QqLoginSessionBean;
import game.module.lan.dao.LoginSessionCache;
import game.module.legion.logic.LegionManager;
import game.module.log.logic.LogItemGoManager;
import game.module.mail.dao.MailCache;
import game.module.mail.logic.MailManager;
import game.module.manor.dao.ManorCache;
import game.module.manor.dao.SurrenderPersuadeCache;
import game.module.mapevent.dao.MapEventCache;
import game.module.mission.dao.MissionDailyCache;
import game.module.mission.logic.MissionManager;
import game.module.mythical.dao.MythicalAnimalCache;
import game.module.mythical.processor.MythicalListProcessor;
import game.module.occtask.dao.OccTaskCache;
import game.module.occtask.logic.OccTaskManager;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.offline.logic.ServerCache;
import game.module.online.logic.OnlineGiftManager;
import game.module.pay.OpenConstants;
import game.module.pay.bean.ChargeEntity;
import game.module.pay.dao.ChargeCache;
import game.module.pay.dao.LibaoBuyCache;
import game.module.pay.logic.ChargeInfoManager;
import game.module.pvp.dao.PvpRecordCache;
import game.module.pvp.logic.PvpManager;
import game.module.shop.dao.ShopCache;
import game.module.sign.dao.SignInCache;
import game.module.sign.logic.SignManager;
import game.module.spin.dao.SpinCache;
import game.module.stat.dao.LogLoginDao;
import game.module.user.bean.PlayerBean;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.*;
import game.session.PlayerOnlineCacheMng;
import game.session.SessionManager;
import lion.common.SimpleTextConvert;
import lion.netty4.core.SocketProtoServer;
import lion.netty4.message.GamePlayer;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sdk.zy.WanbaOpenApi;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageHall.S2CPlayerKickOff;
import ws.WsMessageHall.S2CQueryHasRole;
import ws.WsMessageHall.S2CUserInfoStruct;
import ws.WsMessageBase.WanbaLoginGift;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class PlayerManager {

	private static Logger logger = LoggerFactory.getLogger(PlayerManager.class);

    static class SingletonHolder {
		static PlayerManager instance = new PlayerManager();
	}

	public static PlayerManager getInstance() {
		return SingletonHolder.instance;
	}

	private PlayerDao playerDao = PlayerDao.getInstance();

	protected int getRandIcon() {
		int MAX_ICON_COUNT = 20;
		int randIcon = new Random().nextInt(MAX_ICON_COUNT);
		return randIcon + 1;
	}

	/**
	 * 登录接口
	 * 
	 * @param gamePlayer
	 * @param deviceId
	 * @param userName
	 * @param loginIp
	 */
	public void login(final GamePlayer gamePlayer, final String deviceId, final String userName, final String loginIp) {
		// GameServer.executorService.execute(new Runnable() {
		// @Override
		// public void run() {
		// boolean existUser = playerDao.checkExistByUserName(userName);
		// if (!existUser) {
		// S2CLogin.Builder respBuilder = S2CLogin.newBuilder();
		// respBuilder.setUid(0);
		// gamePlayer.writeAndFlush(10014, respBuilder.build(),
		// RetCode.LOGIN_NAME_NOT_EXIST);
		// } else {
		// PlayerBean ub = cacheNewPlayer(gamePlayer, userName);
		// S2CLogin.Builder respBuilder = S2CLogin.newBuilder();
		// playerDao.updateLoginIp(loginIp, ub.getId());
		// respBuilder.setUid(ub.getId());
		// gamePlayer.writeAndFlush(10014, respBuilder.build());
		// }
		// }
		//
		// });
	}

	private boolean cacheNewPlayer(GamePlayer gamePlayer, PlayerBean playerBean) {
		// 保存玩家
		PlayingRole existPlayer = SessionManager.getInstance().getPlayer(playerBean.getId());
		if (existPlayer != null) {
			logger.info("还有登陆信息！,id={}", playerBean.getId());
			if (existPlayer.getGamePlayer() != null) {
				if (existPlayer.getGamePlayer() == gamePlayer) {
					logger.warn("重复登陆!addr={},playerId={}", gamePlayer.getAddress(),playerBean.getId());
					Long inGameSessionId = existPlayer.getGamePlayer().getSessionId();
					SessionManager.getInstance().visit(inGameSessionId);
					return false;
				}else {
					Date now = new Date();
					//玩家下线记录
					playerBean.setDownlineTime(now);
					//老的session处理
					Long oldSessionId = existPlayer.getGamePlayer().getSessionId();
					if (oldSessionId != null) {
						SessionManager.getInstance().removeLogicPlayer(oldSessionId, false);
					}
					if (existPlayer.isChannelActive()) {
						S2CPlayerKickOff pushMsg = new S2CPlayerKickOff();
						existPlayer.getGamePlayer().writeAndFlush(pushMsg.build(existPlayer.alloc()));
					}
					existPlayer.getGamePlayer().saveSessionId(null);
				}
			}
		} else {
			SessionManager.getInstance().removeSessionTimeout(playerBean.getId());
		}
		PlayingRole aHero = new PlayingRole(gamePlayer, playerBean);
		Long loginSessionId = gamePlayer.getChannel().attr(SocketProtoServer.KEY_SESSION_ID).get();
		SessionManager.getInstance().create(aHero, loginSessionId);
		//update cache
		PlayerOfflineManager.getInstance().updatePlayerOfflineCache(playerBean.getId(),playerBean);
		return true;
	}

	public void queryCharacterList(final GamePlayer gamePlayer, final LoginSessionBean loginSessionBean,
			final WanbaLoginGift qqOpenData) throws IOException {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				boolean isPlayerExist = playerDao.checkPlayerExist(loginSessionBean.getOpenId(), loginSessionBean.getServerId());
				if (!isPlayerExist) {
					if (loginSessionBean instanceof QqLoginSessionBean) {
						// 没有角色，获取昵称
						GameServer.executorService.execute(new Runnable() {
							@Override
							public void run() {
								try {
									String appKey = OpenConstants.WANBA_APP_KEY;
									String wanbaUrlBase = "https://api.urlshare.cn";
									Map<String, Object> retMap = WanbaOpenApi.getInstance().getLoginInfo(qqOpenData.openid,
											qqOpenData.openkey, qqOpenData.appid, qqOpenData.pf, wanbaUrlBase, appKey);
									if (retMap == null) {
										logger.warn("network error!");
										S2CQueryHasRole respMsg = new S2CQueryHasRole(false, "");
										gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
										return;
									}
									logger.info("wan ba get login user info result={}", retMap);
									if (!retMap.containsKey("ret") || (int) (retMap.get("ret")) != 0) {
										S2CQueryHasRole respMsg = new S2CQueryHasRole(false, "");
										gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
										return;
									}
									String nickname = (String) retMap.get("nickname");
									S2CQueryHasRole respMsg = new S2CQueryHasRole(false, nickname);
									gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
								} catch (Exception e) {
									logger.error("", e);
									S2CQueryHasRole respMsg = new S2CQueryHasRole(false, "");
									gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
								}
							}
						});
					} else {
						S2CQueryHasRole respMsg = new S2CQueryHasRole(false, "");
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					}
				} else {
					S2CQueryHasRole respMsg = new S2CQueryHasRole(true, "");
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
				}
			}
		});
	}

	/**
	 * 创建角色
	 * 
	 */
	public void createCharacter(final GamePlayer player, final LoginSessionBean loginSessionBean, final String name) {
		GameServer.executorService.execute(() -> {
			PlayerBean pb = playerDao.getPlayerByName(name);
//			if (pb != null) {
//				WsMessageHall.S2CCreateCharacter respMsg = new WsMessageHall.S2CCreateCharacter(1, pb.getId());
//				player.writeAndFlush(respMsg.build(player.alloc()));
//				return;
//			}
			List<PlayerBean> playerList = playerDao.getPlayersByPlatfrom(loginSessionBean);
			if (playerList != null && playerList.size() > 0) {
				WsMessageHall.S2CCreateCharacter respMsg = new WsMessageHall.S2CCreateCharacter(1, pb.getId());
				player.writeAndFlush(respMsg.build(player.alloc()));
				return;
			}
			//
			String openId = loginSessionBean.getOpenId();
			int serverId = loginSessionBean.getServerId();
			pb = new PlayerBean();
			pb.setAccountId(openId);
			pb.setName(name);
			pb.setGold(0);
			pb.setMoney(0);
			pb.setLevel(1);
			pb.setLevelExp(0);
			pb.setVipLevel(0);
			pb.setVipExp(0);
			pb.setSex(1);
			pb.setIconid(14161);
			pb.setHeadid(50051);
			pb.setFrameid(51001);
			pb.setImageid(52077);
			pb.setPower(0);
			pb.setServerId(serverId);
			pb.setGuideProgress(0);
			pb.setCreateTime(new Date());
			pb.setNationId(0);
			playerDao.addUser(pb);
			// 添加任务
//				TaskManager.getInstance().bornAddTask(pb.getId());
			// ret
			WsMessageHall.S2CCreateCharacter respMsg = new WsMessageHall.S2CCreateCharacter(0, pb.getId());
			player.writeAndFlush(respMsg.build(player.alloc()));
		});
	}

	/**
	 * 测试发邮件, 待删除
	 * 
	 * @param gamePlayer
	 * @param uid
	 * @param tempId
	 * @param itemId1
	 * @param cnt1
	 * @param itemId2
	 * @param cnt2
	 */
	// private void sendOneMail(GamePlayer gamePlayer, int uid, int tempId, int
	// itemId1, int cnt1, int itemId2, int
	// cnt2) {
	// DBMailAtt dbMailAtt = new DBMailAtt();
	// List<MailAtt> mailAtts = new ArrayList<DBMailAtt.MailAtt>();
	// MailAtt att = new MailAtt();
	// att.setItemId(itemId1);
	// att.setCnt(cnt1);
	// mailAtts.add(att);
	// MailAtt att2 = new MailAtt();
	// att2.setItemId(itemId2);
	// att2.setCnt(cnt2);
	// mailAtts.add(att2);
	// dbMailAtt.setMailAttList(mailAtts);
	//
	// MailManager.getInstance().sendMail(uid, tempId, null, dbMailAtt);
	// }

	/**
	 * 直接登录游戏
	 * 
	 * @param player
	 * @param wanbaLoginGift 
	 * @param uid
	 */
	public void enterGame(final GamePlayer player, final LoginSessionBean loginSessionBean,final WanbaLoginGift wanbaLoginGift) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				// find from cache
				String gaOpenId = loginSessionBean.getOpenId();
				int serverId = 0;
				serverId = loginSessionBean.getServerId();
				PlayerBean playerBean = PlayerOnlineCacheMng.getInstance().getCache(gaOpenId, serverId);
				if (playerBean == null) {
					// find from db
					List<PlayerBean> retList = playerDao.getPlayersByPlatfrom(loginSessionBean);
					if (retList == null || retList.size() <= 0) {
						logger.info("Player not found,accountId={}", loginSessionBean.getOpenId());
						WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CEnterGame.msgCode,105);
						player.writeAndFlush(respmsg.build(player.alloc()));
						return;
					} else {
						playerBean = retList.get(0);
					}
				}
				// 账号被禁
				if (ServerCache.getInstance().isFengHao(playerBean.getId())) {
					WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CEnterGame.msgCode,122);
					player.writeAndFlush(respmsg.build(player.alloc()));
					return;
				}
				boolean addRet = cacheNewPlayer(player, playerBean);
				if (!addRet) {
					WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CEnterGame.msgCode,151);
					player.writeAndFlush(respmsg.build(player.alloc()));
					return;
				} else {
					logger.info("player enter game,id={},ipAddr={}", playerBean.getId(), player.getAddress());
					// online time
					PlayingRole playingRole = SessionManager.getInstance().getPlayer(playerBean.getId());
					Date now = new Date();
					if (playingRole != null) {
						playingRole.getPlayerCacheStatus().setEnterGameTime(now);
					}
					// login log
					logLogin(playerBean.getId());
					// update cache
					LoginSessionCache.getInstance().removeLoginSession(loginSessionBean);
					int currentTime = (int) (System.currentTimeMillis() / 1000);
					//
					loadPlayerData(playerBean.getId());
					//
					MissionManager.getInstance().loginUpdateMissionDaily(playingRole);
					SignManager.getInstance().loginUpdateSign(playingRole);
					// Novice guide and free props
					GuideManager.getInstance().sendInitGoods(playingRole);
					// Test server monthly card growth fund feedback
					if (playingRole.getPlayerBean().getDownlineTime() == null) {
						PlayerLogic.getInstance().yueKaChengZhangFeedback(playingRole);
					}
					// Update gold purchase information
					GoldBuyManager.getInstance().updateGoldBuyProperty(playingRole, false);
					// Main interface information
					// ret msg
					S2CUserInfoStruct userInfoStruct = new S2CUserInfoStruct();
					userInfoStruct.id = playerBean.getId();
					userInfoStruct.uid = playerBean.getAccountId();
					userInfoStruct.servid = playerBean.getServerId();
					userInfoStruct.rname = playerBean.getName();
					userInfoStruct.level = playerBean.getLevel();
					int currentLevelSumExp = PlayerLevelTemplateCache.getInstance().getPlayerNextExpByLv(playerBean.getLevel() - 1);
					userInfoStruct.exp = currentLevelSumExp + playerBean.getLevelExp();
					userInfoStruct.vip = playerBean.getVipLevel();
					userInfoStruct.vipexp = playerBean.getVipExp();
					userInfoStruct.sex = playerBean.getSex();
					userInfoStruct.iconid = playerBean.getIconid();
					userInfoStruct.headid = playerBean.getHeadid();
					userInfoStruct.frameid = playerBean.getFrameid();
					userInfoStruct.imageid = playerBean.getImageid();
					userInfoStruct.yb = playerBean.getMoney();
					userInfoStruct.gold = playerBean.getGold();
					userInfoStruct.power = playerBean.getPower();
					//first charge
					ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerBean.getId());
					userInfoStruct.firstcharge = 0;
					if(chargeEntity != null && chargeEntity.getFirstPayTime() != null){
						userInfoStruct.firstcharge = chargeEntity.getFirstPayTime().getTime();
					}
					userInfoStruct.create = playerBean.getCreateTime().getTime();
					userInfoStruct.time = now.getTime();
					userInfoStruct.guideProgress = playerBean.getGuideProgress();
					//max map id
					ChapterBean chapterBean = ChapterCache.getInstance().getPlayerChapter(playerBean.getId());
					if(chapterBean != null){
						userInfoStruct.maxmapid = chapterBean.getMaxMapId();
					}else{
						userInfoStruct.maxmapid = ChapterManager.getInstance().getInitMapId();
					}
					userInfoStruct.nowmapid = userInfoStruct.maxmapid;
					userInfoStruct.tower = PlayerServerPropManager.getInstance().getTower(playerBean.getId());
					userInfoStruct.hides = buildHides(playerBean.getId());
					userInfoStruct.others = buildOthers(playerBean.getId());
					userInfoStruct.bagspace = BagManager.getInstance().getGeneralBagSize(playerBean.getId(), playerBean.getVipLevel());
					// Tavern free information
					userInfoStruct.recruitfree = PubDrawManager.getInstance().buildRecruitFree(playerBean.getId());
					// Online gift information
					userInfoStruct.online = OnlineGiftManager.getInstance().getNextOnlineTime(playingRole);
					// Dungeon hanging reward time
					userInfoStruct.lastgain = ChapterManager.getInstance().getLastGainTime(playerBean.getId());
					// Dungeon formation list
					userInfoStruct.battlearr = BattleFormationManager.getInstance().buildFormationList(playerBean.getId());
					userInfoStruct.pvpscore = PvpManager.getInstance().getPvpScore(playerBean.getId());
					// Friend boss physical value
					userInfoStruct.fbossphys =
							FriendConstants.FRIEND_BOSS_ATTACK_COUNT - PlayerServerPropManager.getInstance().getServerPropCount(playerBean.getId(),
							ItemConstants.FRIEND_BOSS_ATTACK_COUNT_MARK);
					// Legion information
					userInfoStruct.legion = LegionManager.getInstance().getLegionId(playerBean.getId());
					userInfoStruct.gnum = BagManager.getInstance().getGNum(playerBean.getId());
					userInfoStruct.tech = LegionManager.getInstance().getLegionTech(playerBean.getId());
					userInfoStruct.dgtop = DungeonManager.getInstance().getDungeonInfo(playerBean.getId());
					//occ task
					userInfoStruct.occtaskend = OccTaskManager.getInstance().getOccTaskEndTime(playerBean);
					userInfoStruct.occtask = OccTaskManager.getInstance().getOccTaskInfo(playerBean);
					userInfoStruct.special = ChargeInfoManager.getInstance().getChargeInfo(playerBean.getId());
					userInfoStruct.ydend = ActivityMonthManager.getInstance().getYdhdEndTime().getTime();
					// PVE clearance
					userInfoStruct.guozhan_pvp = GuozhanPlayerManager.getInstance().isGuozhanPvp(playerBean.getId());
					player.write(userInfoStruct.build(player.alloc()));
					// First login logic
					PlayerLogic.getInstance().firstEnterMail(playingRole);
					// Update online reward time
//					OnlineAwardManager.getInstance().updateOnlineAwardTime(playerBean.getId());
					ActivityManager.getInstance().loginDynamic(playingRole);
					// Send item information
					ItemListProcessor.sendResponse(playingRole);
					// Hero information
					GeneralListProcessor.sendResponse(playingRole);
					// Mythical information
					MythicalListProcessor.sendResponse(playingRole);
					// Payment information
					ChargeInfoManager.getInstance().sendPaymentInfo(playingRole);
					// Online duration reward
//					OnlineGiftListProcessor.sendResponse(playingRole);
					//flush message
					WsMessageHall.S2CEnterGame retMsg = new WsMessageHall.S2CEnterGame(currentTime, player.getSessionId());
					player.writeAndFlush(retMsg.build(player.alloc()));
					 // Log
					LogItemGoManager.getInstance().scheduleLogItemGo(playingRole.getId());
					//Guozhan
					GuoZhanManager.getInstance().kingSendMarquee(playerBean.getId(), playerBean.getName());
				}
			}
		});
	}

	private List<WsMessageBase.SimpleItemInfo> buildOthers(Integer playerId) {
		List<WsMessageBase.SimpleItemInfo> retlist = new ArrayList<>();
		Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
		if(playerOthers != null){
			for(PlayerProp playerProp : playerOthers.values()){
				retlist.add(new WsMessageBase.SimpleItemInfo(playerProp.getGsid(), playerProp.getCount()));
			}
		}
		return retlist;
	}

	private List<WsMessageBase.SimpleItemInfo> buildHides(Integer playerId) {
		List<WsMessageBase.SimpleItemInfo> retlist = new ArrayList<>();
		Map<Integer, PlayerProp> playerHiddens = PlayerHideCache.getInstance().getPlayerHidden(playerId);
		if(playerHiddens != null){
			for(PlayerProp playerProp : playerHiddens.values()){
				retlist.add(new WsMessageBase.SimpleItemInfo(playerProp.getGsid(), playerProp.getCount()));
			}
		}
		return retlist;
	}

	public int[] getRedPointInfo(PlayingRole playingRole) {
		int playerId = playingRole.getId();
		List<Integer> redPointModules = new ArrayList<>();
		// 未读邮件
		boolean hasNewMail = MailManager.getInstance().checkHasNewMail(playerId);
		if (hasNewMail) {
			redPointModules.add(1);
		}
		// 签到领取
		boolean signFree = false;
		if (signFree) {
			redPointModules.add(4);
		}
		// 精英副本攻打次数
//		boolean canEliteStageAttack = StageUtils.checkStageRedPoint(playerId);
//		if (canEliteStageAttack) {
//			redPointModules.add(7);
//		}
		// 聊天新消息
		boolean chatHasNewMsg = ChatManager.getInstance().checkHasNewMsg(playerId);
		if(chatHasNewMsg) {
			redPointModules.add(8);
		}
		//装备合成
		boolean equipCombineHasNewMsg = false;
		if(equipCombineHasNewMsg) {
			redPointModules.add(10);
		}
		//背包
		boolean bagHasNewMsg = false;
		if(bagHasNewMsg) {
			redPointModules.add(14);
		}
		Integer[] redPointsList = redPointModules.toArray(new Integer[] {});
		int[] retlist = SimpleTextConvert.intObj2Raw(redPointsList);
		return retlist;
	}

	protected void logLogin(int playerId) {
		LogLoginDao.getInstance().insertLogLogin(playerId);
	}

	/**
	 * 登陆加载数据
	 * 
	 * @param playerId
	 */
	protected void loadPlayerData(Integer playerId) {
//		PlayerExtraCache.getInstance().loadFromDb(playerId);
//		ActivityPlayerCache.getInstance().loadFromDb(playerId);
//		ActivityBattleCache.getInstance().loadFromDb(playerId);
//		ArenaPlayerCache.getInstance().loadFromDb(playerId);
//		OvercomeCache.getInstance().loadFromDb(playerId);
//		PubEntityCache.getInstance().loadFromDb(playerId);
		ItemCache.getInstance().loadFromDb(playerId);
//		BagEntityCache.getInstance().loadFromDb(playerId);
//		TeamskillCache.getInstance().loadFromDb(playerId);
		MissionDailyCache.getInstance().loadFromDb(playerId);
		GeneralCache.getInstance().loadFromDb(playerId);
		MailCache.getInstance().loadFromDb(playerId);
		SignInCache.getInstance().loadFromDb(playerId);
		FriendCache.getInstance().loadFromDb(playerId);
		AffairCache.getInstance().loadFromDb(playerId);
		MythicalAnimalCache.getInstance().loadFromDb(playerId);
		ShopCache.getInstance().loadFromDb(playerId);
		ChapterCache.getInstance().loadFromDb(playerId);
		GoldBuyCache.getInstance().loadFromDb(playerId);
		PubDrawCache.getInstance().loadFromDb(playerId);
		SpinCache.getInstance().loadFromDb(playerId);
		GeneralExchangeCache.getInstance().loadFromDb(playerId);
		PlayerHeadCache.getInstance().loadFromDb(playerId);
		PlayerHideCache.getInstance().loadFromDb(playerId);
		PlayerOtherCache.getInstance().loadFromDb(playerId);
		BattleFormationCache.getInstance().loadFromDb(playerId);
		MonthBossCache.getInstance().loadFromDb(playerId);
		PlayerServerPropCache.getInstance().loadFromDb(playerId);
		ExpedCache.getInstance().loadFromDb(playerId);
		ManorCache.getInstance().loadFromDb(playerId);
		FriendExploreCache.getInstance().loadFromDb(playerId);
		MapEventCache.getInstance().loadFromDb(playerId);
		PvpRecordCache.getInstance().loadFromDb(playerId);
		DungeonCache.getInstance().loadFromDb(playerId);
		SurrenderPersuadeCache.getInstance().loadFromDb(playerId);
//		OnlineAwardCache.getInstance().loadFromDb(playerId);
//		GuideCache.getInstance().loadFromDb(playerId);
		ChargeCache.getInstance().loadFromDb(playerId);
		OccTaskCache.getInstance().loadFromDb(playerId);
		ActivityXiangouCache.getInstance().loadFromDb(playerId);
//		StoreCache.getInstance().loadFromDb(playerId);
//		PlayerBuyCache.getInstance().loadFromDb(playerId);
//		PlayerPkCache.getInstance().loadFromDb(playerId);
//		WanbaGiftCache.getInstance().loadFromDb(playerId);
//		// // player offline cache
//		if (!PlayerOfflineManager.getInstance().checkExist(playerId)) {
//			PlayerOfflineManager.getInstance().loadFromDb(playerId);
//		}
//		SecretCache.getInstance().loadFromDb(playerId);
		GuozhanCache.getInstance().loadFromDb(playerId);
		LibaoBuyCache.getInstance().loadFromDb(playerId);
		ActMjbgCache.getInstance().loadFromDb(playerId);
		ActCxryCache.getInstance().loadFromDb(playerId);
		ActTnqwCache.getInstance().loadFromDb(playerId);
	}


	public void changeHide(PlayingRole playingRole, int gsid, int changeCount) {
		logger.info("hide prop change,playerId={},id={},count={}", playingRole.getId(), gsid, changeCount);
		int playerId = playingRole.getId();
		Map<Integer, PlayerProp> playerHiddens = PlayerHideCache.getInstance().getPlayerHidden(playerId);
		int existCount = 0;
		if (playerHiddens.containsKey(gsid)) {
			existCount = playerHiddens.get(gsid).getCount();
		}
		int nowCount = existCount + changeCount;
		nowCount = Math.max(nowCount, 0);
		if (nowCount > 0) {
			//change val
			PlayerProp playerProp = playerHiddens.get(gsid);
			if (playerProp == null) {
				playerProp = new PlayerProp();
				playerProp.setGsid(gsid);
				playerProp.setCount(nowCount);
				playerProp.setPlayerId(playerId);
				PlayerProp finalPlayerProp = playerProp;
				GameServer.executorService.execute(() -> PlayerHideDao.getInstance().addPlayerHidden(finalPlayerProp));
				playerHiddens.put(gsid, playerProp);
			} else {
				playerProp.setCount(nowCount);
				PlayerProp finalPlayerProp1 = playerProp;
				GameServer.executorService.execute(() -> PlayerHideDao.getInstance().updatePlayerHidden(finalPlayerProp1));
			}
		} else {
			PlayerProp playerProp = playerHiddens.get(gsid);
			if (playerProp != null) {
				playerHiddens.remove(gsid);
				GameServer.executorService.execute(() -> PlayerHideDao.getInstance().removePlayerHidden(playerProp.getId()));
			}
		}
		// push item change
		WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(gsid, nowCount);
		playingRole.write(pushMsg.build(playingRole.alloc()));
	}

	public void changeOther(PlayingRole playingRole, int gsid, int changeCount) {
		logger.info("other prop change,playerId={},id={},count={}", playingRole.getId(), gsid, changeCount);
		int playerId = playingRole.getId();
		Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
		int existCount = 0;
		if (playerOthers.containsKey(gsid)) {
			existCount = playerOthers.get(gsid).getCount();
		}
		int nowCount = existCount + changeCount;
		nowCount = Math.max(nowCount, 0);
		if (nowCount > 0) {
			//change val
			PlayerProp playerProp = playerOthers.get(gsid);
			if (playerProp == null) {
				playerProp = new PlayerProp();
				playerProp.setGsid(gsid);
				playerProp.setCount(nowCount);
				playerProp.setPlayerId(playerId);
				PlayerProp finalPlayerProp = playerProp;
				GameServer.executorService.execute(() -> PlayerOtherDao.getInstance().addPlayerOther(finalPlayerProp));
				playerOthers.put(gsid, playerProp);
			} else {
				playerProp.setCount(nowCount);
				PlayerProp finalPlayerProp1 = playerProp;
				GameServer.executorService.execute(() -> PlayerOtherDao.getInstance().updatePlayerOther(finalPlayerProp1));
			}
		} else {
			PlayerProp playerProp = playerOthers.get(gsid);
			if (playerProp != null) {
				playerOthers.remove(gsid);
				GameServer.executorService.execute(() -> PlayerOtherDao.getInstance().removePlayerOther(playerProp.getId()));
			}
		}
		// push item change
		WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(gsid, nowCount);
		playingRole.write(pushMsg.build(playingRole.alloc()));
	}


	public void setHide(PlayingRole playingRole, int gsid, int nowCount, boolean isPush) {
		int playerId = playingRole.getId();
		logger.info("set hide,player={},gsid={},count={}",playerId, gsid, nowCount);
		Map<Integer, PlayerProp> playerHides = PlayerHideCache.getInstance().getPlayerHidden(playerId);
		nowCount = Math.max(nowCount, 0);
		if (nowCount > 0) {
			//change val
			PlayerProp playerProp = playerHides.get(gsid);
			if (playerProp == null) {
				playerProp = new PlayerProp();
				playerProp.setGsid(gsid);
				playerProp.setCount(nowCount);
				playerProp.setPlayerId(playerId);
				PlayerProp finalPlayerProp = playerProp;
				GameServer.executorService.execute(() -> PlayerHideDao.getInstance().addPlayerHidden(finalPlayerProp));
				playerHides.put(gsid, playerProp);
			} else {
				playerProp.setCount(nowCount);
				PlayerProp finalPlayerProp1 = playerProp;
				GameServer.executorService.execute(() -> PlayerHideDao.getInstance().updatePlayerHidden(finalPlayerProp1));
			}
		} else {
			PlayerProp playerProp = playerHides.get(gsid);
			if (playerProp != null) {
				playerProp.setCount(0);
				playerHides.remove(gsid);
				GameServer.executorService.execute(() -> PlayerHideDao.getInstance().removePlayerHidden(playerProp.getId()));
			}
		}
		if(isPush){
			// push item change
			WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(gsid, nowCount);
			playingRole.write(pushMsg.build(playingRole.alloc()));
		}
	}

	public void setOther(PlayingRole playingRole, int gsid, int nowCount, boolean isPush) {
		int playerId = playingRole.getId();
		logger.info("set other,player={},gsid={},count={}",playerId, gsid, nowCount);
		Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
		nowCount = Math.max(nowCount, 0);
		if (nowCount > 0) {
			//change val
			PlayerProp playerProp = playerOthers.get(gsid);
			if (playerProp == null) {
				playerProp = new PlayerProp();
				playerProp.setGsid(gsid);
				playerProp.setCount(nowCount);
				playerProp.setPlayerId(playerId);
				PlayerProp finalPlayerProp = playerProp;
				GameServer.executorService.execute(() -> PlayerOtherDao.getInstance().addPlayerOther(finalPlayerProp));
				playerOthers.put(gsid, playerProp);
			} else {
				playerProp.setCount(nowCount);
				PlayerProp finalPlayerProp1 = playerProp;
				GameServer.executorService.execute(() -> PlayerOtherDao.getInstance().updatePlayerOther(finalPlayerProp1));
			}
		} else {
			PlayerProp playerProp = playerOthers.get(gsid);
			if (playerProp != null) {
				playerProp.setCount(0);
				playerOthers.remove(gsid);
				GameServer.executorService.execute(() -> PlayerOtherDao.getInstance().removePlayerOther(playerProp.getId()));
			}
		}
		if(isPush){
			// push item change
			WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(gsid, nowCount);
			playingRole.write(pushMsg.build(playingRole.alloc()));
		}
	}

	public int getOtherCount(int playerId, int gsid){
		Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
		PlayerProp playerProp = playerOthers.get(gsid);
		if(playerProp != null){
			return playerProp.getCount();
		}
		return 0;
	}

	public int getHideCount(int playerId, int gsid) {
		Map<Integer, PlayerProp> playerHides = PlayerHideCache.getInstance().getPlayerHidden(playerId);
		PlayerProp playerProp = playerHides.get(gsid);
		if(playerProp != null){
			return playerProp.getCount();
		}
		return 0;
	}

	public static void main(String[] args) {
		Method[] methods = PlayerBean.class.getDeclaredMethods();
		List<Method> setMethods = new ArrayList<Method>();
		for (Method aMethod : methods) {
			if (aMethod.getName().startsWith("get")) {
				// logger.info(aMethod.getName());
				setMethods.add(aMethod);
			}
		}
		Collections.sort(setMethods, new Comparator<Method>() {
			@Override
			public int compare(Method o1, Method o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		for (Method method : setMethods) {
			logger.info(method.getName());
		}
	}

	public int randRoleType() {
		return RandomUtils.nextInt(1, 3);
	}

}
