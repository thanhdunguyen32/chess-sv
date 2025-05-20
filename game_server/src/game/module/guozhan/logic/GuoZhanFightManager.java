package game.module.guozhan.logic;

import db.proto.ProtoMessageGuozhan.*;
import db.proto.ProtoMessageGuozhan.DBGuoZhanFight.Builder;
import game.GameServer;
import game.entity.PlayingRole;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.logic.ActivityManager;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.chat.logic.NationChatCache;
import game.module.guozhan.bean.CityListTemplate;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.CityJoinTemplateCache;
import game.module.guozhan.dao.CityListTemplateCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.dao.GuozhanPlayerDao;
import game.module.guozhan.processor.GuozhanFightViewProcessor;
import game.module.log.constants.LogConstants;
import game.module.mail.logic.MailManager;
import game.module.mine.logic.MineConstants;
import game.module.mission.constants.MissionConstants;
import game.module.offline.logic.ServerCache;
import game.module.pvp.logic.PvpConstants;
import game.module.user.dao.CommonTemplateCache;
import game.module.user.logic.ScrollAnnoManager;
import game.session.SessionManager;
import io.netty.util.Timeout;
import lion.math.RandomDispatcher;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageGuozhan.PushGuoZhanPass;
import ws.WsMessageGuozhan.S2CGuoZhanCityMove;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class GuoZhanFightManager {

	private static Logger logger = LoggerFactory.getLogger(GuoZhanFightManager.class);

	static class SingletonHolder {
		static GuoZhanFightManager instance = new GuoZhanFightManager();
	}

	public static GuoZhanFightManager getInstance() {
		return SingletonHolder.instance;
	}

	public Map<Integer, Timeout> scheduleMineFinishMap = new ConcurrentHashMap<>();

	public void addScheduleTimeout(int pointVal, Timeout aTimeout) {
		scheduleMineFinishMap.put(pointVal, aTimeout);
	}

	public Timeout getScheduleTimeout(int pointVal) {
		return scheduleMineFinishMap.get(pointVal);
	}

	public void removeScheduleTimeout(int cityIndex) {
		scheduleMineFinishMap.remove(cityIndex);
	}

	public void clearScheduleTimeout() {
		for(Timeout timeout : scheduleMineFinishMap.values()) {
			timeout.cancel();
		}
		scheduleMineFinishMap.clear();
	}

	public int getMyMoveStep(DBGuoZhanRole guoZhanRole) {
		int ret = 0;
		if (guoZhanRole != null) {
			int oldMoveStep = guoZhanRole.getMoveStep();
			long lastAddStepTime = guoZhanRole.getLastAddStepTime();
			int addStep = (int) ((System.currentTimeMillis() - lastAddStepTime) / 600000);
			ret = oldMoveStep + addStep;
		}
		ret = ret > GuozhanConstants.PLAYER_MAX_MOVE_STEP ? GuozhanConstants.PLAYER_MAX_MOVE_STEP : ret;
		return ret;
	}

	public DBGuoZhanFight buildGuozhanFight() {
		DBGuoZhanFight.Builder builder = DBGuoZhanFight.newBuilder();
		builder.addNationOwnCity(GuozhanConstants.NATION_INIT_CITY).addNationOwnCity(GuozhanConstants.NATION_INIT_CITY)
				.addNationOwnCity(GuozhanConstants.NATION_INIT_CITY);
		List<CityListTemplate> cityListTemplates = CityListTemplateCache.getInstance().getCityListTemplates();
		int idx = 0;
		for (CityListTemplate cityListTemplate : cityListTemplates) {
			builder.addCitysBuilder().setInBattle(false).setOwnNationId(cityListTemplate.getCountryType()).setCityIndex(idx++);
		}
		DBGuoZhanFight guoZhanFight = builder.build();
		GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);
		return guoZhanFight;
	}

	public synchronized void playerInit(int playerId,int myNationId) {
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		boolean needInsert = false;
		if (guoZhanFight == null || guoZhanFight.getCitysCount() == 0) {
			guoZhanFight = buildGuozhanFight();
			needInsert = true;
		}
		DBGuoZhanFight.Builder builder = guoZhanFight.toBuilder();
		if(builder.getPlayersOrDefault(playerId, null) == null) {
			playerRandPos(builder, playerId, myNationId, -1);
			guoZhanFight = builder.build();
			GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);
		}
		if(needInsert){
			GuozhanDaoHelper.asyncInsertGuozhaFight(guoZhanFight);
		}
	}

	public synchronized DBGuoZhanFight viewPlayerRandPos(DBGuoZhanFight guoZhanFight,int playerId,int myNationId) {
		DBGuoZhanFight.Builder fightBuilder = guoZhanFight.toBuilder();
		boolean ret = GuoZhanFightManager.getInstance().playerRandPos(fightBuilder, playerId,
				myNationId, -1);
		if (ret) {
			guoZhanFight = fightBuilder.build();
			GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);
		}
		return fightBuilder.build();
	}

	public boolean playerRandPos(DBGuoZhanFight.Builder builder,int playerId,int myNationId, int excludeCityIndex) {
		boolean ret = false;
		DBGuoZhanRole guoZhanRole = builder.getPlayersOrDefault(playerId, null);
		DBGuoZhanRole.Builder playerBuilder;
		if(guoZhanRole != null) {
			playerBuilder = guoZhanRole.toBuilder();
		} else {
			playerBuilder = DBGuoZhanRole.newBuilder();
			playerBuilder.setHpPerc(100).setLastAttackTime(System.currentTimeMillis())
					.setMoveStep(GuozhanConstants.PLAYER_INIT_MOVE_STEP).setLastAddStepTime(System.currentTimeMillis());
		}
		//随机位置
		List<DBGuoZhanCity.Builder> cityBuilderList = builder.getCitysBuilderList();
		List<DBGuoZhanCity.Builder> randList = new ArrayList<>();
		int cityIdx = 0;
		for (DBGuoZhanCity.Builder builder2 : cityBuilderList) {
			if(cityIdx == excludeCityIndex){
				continue;
			}
			if(builder2.getOwnNationId() == myNationId && builder2.getOccupyPlayerCount() < GuozhanConstants.CITY_MAX_PLAYER_SIZE) {
				randList.add(builder2);
			}
			cityIdx++;
		}
		if(randList.size()>0) {
			ret = true;
			int randIndex = RandomUtils.nextInt(0, randList.size());
			DBGuoZhanCity.Builder cityBuilder = randList.get(randIndex);
			cityBuilder.addOccupyPlayer(playerId);
			playerBuilder.setCityIndex(cityBuilder.getCityIndex());
		}else{
			ret = false;
			playerBuilder.setCityIndex(-1);
		}
		builder.putPlayers(playerId, playerBuilder.build());
		return ret;
	}

	public int getPlayerHpPercent(int playerId) {
		int ret = 100;
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		if (guoZhanFight == null) {
			return ret;
		}
		DBGuoZhanRole guoZhanRole = guoZhanFight.getPlayersOrDefault(playerId, null);
		if (guoZhanRole == null) {
			return ret;
		}
		int hpPerc = guoZhanRole.getHpPerc();
		int passSeconds = (int) ((System.currentTimeMillis() - guoZhanRole.getLastAttackTime()) / 1000);
		// 每小时恢复20%
		hpPerc += (int) (passSeconds * 20f / 3600);
		hpPerc = hpPerc > 100 ? 100 : hpPerc;
		return hpPerc;
	}

	public int checkMove2City(int start_city_index, int target_city_index, int my_nation_id) {
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		//广度优先搜索
		boolean isFind = false;
	    Map<Integer,Integer> adjlist = new HashMap<>();//临接表
	    Set<Integer> checkedCitys = new HashSet<>();
	    checkedCitys.add(start_city_index);
	    Queue<Integer> queue = new LinkedList<>();
	    queue.add(start_city_index); // 添加到队尾
	    while(queue.size() > 0){
	        int search_start_index = queue.remove(); // 从队首移除
	        Map<String,Object> CityJoinConfig = CityJoinTemplateCache.getInstance().getSecretTemp(search_start_index+1);
	        for(int i=0;i<GuozhanConstants.CITY_JOIN_ID_NAMES.length;i++) {
				String aIdName = GuozhanConstants.CITY_JOIN_ID_NAMES[i];
				int joinCityId = (int)CityJoinConfig.get(aIdName);
				int joinCityIndex = joinCityId-1;
				//当前国家
	            if(joinCityId > 0 && !checkedCitys.contains(joinCityIndex)){
	            	int nationId = guoZhanFight.getCitys(joinCityIndex).getOwnNationId();
	                adjlist.put(joinCityIndex, search_start_index);
	                if(target_city_index == joinCityIndex){
	                	isFind = true;
	                	queue.clear();
	                    break;
	                }else if(nationId == my_nation_id){
	                    checkedCitys.add(joinCityIndex);
	                    queue.add(joinCityIndex);
	                }
	            }
	        }
	    }
	    //
	    if(isFind) {
	    	 int search_end_city = target_city_index;
	    	 int passLength = 0;
	         while(adjlist.containsKey(search_end_city)){
	         	passLength++;
	             search_end_city = adjlist.get(search_end_city);
	         }
	         return (int)Math.ceil(passLength/2f);
//			return 1;
	    }else {
	    	return 0;
	    }
	}

	public void updateNationOwnCitys(Builder guoZhanFightBuilder) {
		List<DBGuoZhanCity> cityList = guoZhanFightBuilder.getCitysList();
		int[] nation_own_citys = new int[3];
		for (DBGuoZhanCity dbGuoZhanCity : cityList) {
			if (dbGuoZhanCity.getOwnNationId() > 0 && dbGuoZhanCity.getOwnNationId() < 4) {
				nation_own_citys[dbGuoZhanCity.getOwnNationId() - 1]++;
			}
		}
		for (int i = 0; i < 3; i++) {
			guoZhanFightBuilder.setNationOwnCity(i, nation_own_citys[i]);
		}
	}

	public void scheduleSendOccupyReward() {
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		if(guoZhanFight == null) {
			return;
		}
		List<Integer> nationOwnCitys = guoZhanFight.getNationOwnCityList();
		for (int playerId : guoZhanFight.getPlayersMap().keySet()) {
			GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
			if (guozhanPlayer == null) {
				guozhanPlayer = GuozhanPlayerDao.getInstance().getPlayerGuozhanPlayer(playerId);
			}
			int myNationId = guozhanPlayer.getNation();
			int myCityCount = nationOwnCitys.get(myNationId - 1);
			if (myCityCount == 0) {
				continue;
			}
			int rewardJinKuai = (int) (myCityCount * 1.5);
			logger.info("guozhan city occupy reward,playerId={},gold={}", playerId, rewardJinKuai);
			// 发送邮件
			Map<Integer,Integer> dbMailAtt = getMailAtt(rewardJinKuai);
            // 邮件奖励
            // %s: quốc gia
            // %d: số lượng thành trì
			String mailTitle = "Phần thưởng hàng ngày từ việc tranh đoạt thành trì trong Quốc Chiến";
			String mailContent = "Chúc mừng quốc gia của bạn [%s] đã chiếm được [%d] thành trì trong cuộc tranh đoạt, dưới đây là phần thưởng:";
			mailContent = String.format(mailContent,GuozhanConstants.NATION_LIST[myNationId - 1],myCityCount);
			MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle,mailContent, dbMailAtt);
		}
	}

	private Map<Integer,Integer> getMailAttGuoZhanPass() {
		int activityDouble = ActivityManager.getInstance().getMoudelMultiple(ActivityConstants.AWARD_DOUBLE_GUOZHAN);
		Map<Integer,Integer> dbMailAtt = new HashMap<>();
		List<List<Integer>> guozhan_pass_rewards = (List<List<Integer>>)CommonTemplateCache.getInstance().getConfig("guozhan_pass_reward");
		for (List<Integer> list : guozhan_pass_rewards) {
			int itemId = list.get(0);
			int itemCount = list.get(1);
			dbMailAtt.put(itemId, itemCount * activityDouble);
		}
		return dbMailAtt;
	}

	private Map<Integer,Integer> getMailAtt(int rewardJinKuai) {
		int activityDouble = ActivityManager.getInstance().getMoudelMultiple(ActivityConstants.AWARD_DOUBLE_GUOZHAN);
		Map<Integer,Integer> dbMailAtt = new HashMap<>();
		int itemId = GameConfig.PLAYER.YB;
		dbMailAtt.put(itemId, rewardJinKuai * activityDouble);
		return dbMailAtt;
	}

	public synchronized void changeNation(int playerId, int oldNation, int target_nation) {
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		if (guoZhanFight == null) {
			return;
		}
		DBGuoZhanRole guoZhanRole = guoZhanFight.getPlayersOrDefault(playerId, null);
		if (guoZhanRole == null) {
			return;
		}
		int old_index = guoZhanRole.getCityIndex();
		// 老数据清理
		DBGuoZhanFight.Builder builder = guoZhanFight.toBuilder();
		DBGuoZhanCity.Builder oldCityBuilder = builder.getCitysBuilder(old_index);
		List<Integer> oldPlayerList = oldCityBuilder.getOccupyPlayerList();
		List<Integer> playerLists = new ArrayList<>(oldPlayerList);
		playerLists.remove(Integer.valueOf(playerId));
		oldCityBuilder.clearOccupyPlayer().addAllOccupyPlayer(playerLists);
		// 新位置
		playerRandPos(builder, playerId, target_nation, -1);
		guoZhanFight = builder.build();
		GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);
	}

	public boolean checkAllOccupy(DBGuoZhanFight guoZhanFight) {
		boolean ret = false;
		int zeroSize = 0;
		for(Integer aCityCount : guoZhanFight.getNationOwnCityList()) {
			if(aCityCount == 0) {
				zeroSize++;
			}
			if(zeroSize >=2) {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * 国战城池争夺重置
	 */
	public void scheduleResetGuoZhanFight() {
		logger.info("guozhan fight will reset in 20 seconds!");
		GameServer.executorService.schedule(new Runnable() {
			@Override
			public void run() {
				GuoZhanFightManager.getInstance().scheduleResetGuoZhanFightDo();
			}
		}, 20, TimeUnit.SECONDS);
	}

	public synchronized void scheduleResetGuoZhanFightDo() {
		logger.info("---------------guozhan fight reset!-----------------");
		//发奖励
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		int winNationIndex = 0;
		int i=0;
		for(Integer aCityCount : guoZhanFight.getNationOwnCityList()) {
			if(aCityCount > 0) {
				winNationIndex = i;
				break;
			}
			i++;
		}
		logger.info("guozhan fight winNationId={}",(winNationIndex+1));
		Date lastUnionRewardTime = GuozhanCache.getInstance().getLastUnionRewardTime();
		if (lastUnionRewardTime == null || System.currentTimeMillis()
				- lastUnionRewardTime.getTime() > GuozhanConstants.GUOZHAN_WIN_REWARD_INTERVAL) {
			for (DBGuoZhanCity guoZhanCity : guoZhanFight.getCitysList()) {
				if (guoZhanCity.getOwnNationId() == winNationIndex + 1) {
					for (Integer winPlayerId : guoZhanCity.getOccupyPlayerList()) {
						logger.info("reward player,id={}", winPlayerId);
						// 奖励
						// 发送邮件
						Map<Integer,Integer> dbMailAtt = getMailAttGuoZhanPass();
						// 邮件奖励
						String mailTitle = "Phần thưởng hoạt động tranh đoạt thành trì quốc chiến"; //"国战城池争夺通关奖励"
						String mailContent = "Chúc mừng quốc gia của bạn [%s] đã chiếm được [%d] thành trì trong cuộc tranh đoạt, dưới đây là phần thưởng:"; //"恭喜您所在的国家【%s国】将士们骁勇善战，歼灭敌国，完成了统一大业，可喜可贺，以下是您的报酬：";
						mailContent = String.format(mailContent,GuozhanConstants.NATION_LIST[winNationIndex]);
						MailManager.getInstance().sendSysMailToSingle(winPlayerId, mailTitle,mailContent, dbMailAtt);
					}
				}
			}
			GuozhanCache.getInstance().setLastUnionRewardTime(new Date());
		}
		//重置
		DBGuoZhanFight.Builder builder = guoZhanFight.toBuilder();
		builder.setNationOwnCity(0, GuozhanConstants.NATION_INIT_CITY)
				.setNationOwnCity(1, GuozhanConstants.NATION_INIT_CITY)
				.setNationOwnCity(2, GuozhanConstants.NATION_INIT_CITY);
		List<CityListTemplate> cityListTemplates = CityListTemplateCache.getInstance().getCityListTemplates();
		int idx = 0;
		for (CityListTemplate cityListTemplate : cityListTemplates) {
			builder.getCitysBuilder(idx).setInBattle(false).setOwnNationId(cityListTemplate.getCountryType())
					.clearOccupyPlayer();
			idx++;
		}
		builder.clearPlayers();
		guoZhanFight = builder.build();
		GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);
		//官职重置
		DBGuoZhanOffice guoZhanOffice = GuozhanCache.getInstance().getDBGuoZhanOffice();
		if (guoZhanOffice != null) {
			DBGuoZhanOffice.Builder officeBuilder = guoZhanOffice.toBuilder();
			officeBuilder.clearPlayerPoint();
			for (DBGuoZhanNation.Builder nationBuilder : officeBuilder.getNationsBuilderList()) {
				nationBuilder.clearPlayerOffices();
			}
			guoZhanOffice = officeBuilder.build();
			GuozhanCache.getInstance().setGuozhanOffice(guoZhanOffice);
		}
		GuoZhanManager.getInstance().clearScheduleTimeout();
		// 国家重置
		Collection<GuozhanPlayer> playerAll = GuozhanCache.getInstance().getGuozhanPlayerAll();
		for (GuozhanPlayer guozhanPlayer : playerAll) {
			guozhanPlayer.setNation(0);
			guozhanPlayer.setNationChangeTime(null);
		}
		GuozhanPlayerDao.getInstance().clearNationIdAndTime();
		clearScheduleTimeout();
		//聊天重置
		NationChatCache.getInstance().clearNationPlayers();
		ServerCache.getInstance().clearNationChat();
		// 广播消息
		ScrollAnnoManager.getInstance().guozhanPvpPass(GuozhanConstants.NATION_LIST[winNationIndex]);
		PushGuoZhanPass pushMsg = new PushGuoZhanPass(winNationIndex + 1);
		for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
			pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
		}
	}

	public void limitBattleRecord(DBGuoZhanRole.Builder playerBuilder) {
		while (playerBuilder.getMyRecordCount() > MineConstants.MAX_RECORD_SIZE) {
			playerBuilder.removeMyRecord(0);
		}
	}

	public synchronized void processorCityMove(PlayingRole playingRole, int playerId, int city_index,
			int targetNationId, int mySteps, int costSteps, int oldCityIndex,
			DBGuoZhanCity guoZhanCity, DBGuoZhanRole guoZhanRole, GuozhanPlayer guozhanPlayer) {
		S2CGuoZhanCityMove respMsg = new S2CGuoZhanCityMove();
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		respMsg.city_index = city_index;
		//do
		DBGuoZhanFight.Builder guozhanFightBuilder = guoZhanFight.toBuilder();
		DBGuoZhanRole.Builder guoZhanRoleBuilder = guoZhanRole.toBuilder();
		int curMoveStep = mySteps-costSteps;
		guoZhanRoleBuilder.setCityIndex(city_index).setMoveStep(curMoveStep).setLastAddStepTime(System.currentTimeMillis());
		guozhanFightBuilder.putPlayers(playerId, guoZhanRoleBuilder.build());
		//移除老的city
		DBGuoZhanCity.Builder oldCityBuilder = guozhanFightBuilder.getCitysBuilder(oldCityIndex);
		List<Integer> oldPlayerList = oldCityBuilder.getOccupyPlayerList();
		List<Integer> playerLists = new ArrayList<>(oldPlayerList);
		playerLists.remove(Integer.valueOf(playerId));
		oldCityBuilder.clearOccupyPlayer().addAllOccupyPlayer(playerLists);
		//新city添加本人
		guozhanFightBuilder.getCitysBuilder(city_index).addOccupyPlayer(playerId);
		//
		guoZhanFight = guozhanFightBuilder.build();
		GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);
		//移动到友方城池
		if (targetNationId != guozhanPlayer.getNation() && guoZhanCity.getOccupyPlayerCount() == 0) {
			//切换国家
			guozhanFightBuilder.getCitysBuilder(city_index).setOwnNationId(guozhanPlayer.getNation());
			guozhanPlayer.setOfficeId(city_index);
			//国家拥有城市数量更新
			GuoZhanFightManager.getInstance().updateNationOwnCitys(guozhanFightBuilder);
			//ret
			respMsg.occupy_enemy = true;
			//reward
			// 奖励铜币
			int rewardCoins = 300000;
			// 道具奖励
			List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
			RandomDispatcher<Integer> rd = new RandomDispatcher<>();
			for (int i = 0; i < PvpConstants.REWARD_RATE.length; i++) {
				rd.put(PvpConstants.REWARD_RATE[i], i);
			}
			Integer aIndex = rd.randomRemove();
			int[] rewardConfig = PvpConstants.PVP_REWARD[aIndex];
			int gsid = rewardConfig[0];
			int countMin = rewardConfig[1];
			int countMax = rewardConfig[2];
			int gsCount = RandomUtils.nextInt(countMin, countMax + 1);
			if (gsid == GameConfig.PLAYER.GOLD) {
				gsCount += rewardCoins;
			} else {
				AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, rewardCoins, LogConstants.MODULE_GUOZHAN);
				rewardItems.add(new WsMessageBase.IORewardItem(GameConfig.PLAYER.GOLD, rewardCoins));
			}
			AwardUtils.changeRes(playingRole, gsid, gsCount, LogConstants.MODULE_GUOZHAN);
			rewardItems.add(new WsMessageBase.IORewardItem(gsid, gsCount));
			respMsg.reward = rewardItems;
			//update mission progress
			AwardUtils.changeRes(playingRole, MissionConstants.GUOZHAN_BATTLE_PMARK, 1, LogConstants.MODULE_GUOZHAN);
		}
		guoZhanFight = guozhanFightBuilder.build();
		GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);
		//ret
		respMsg.move_step = curMoveStep;
		//发送更新的国战城池争夺信息
		GuozhanFightViewProcessor.sendResponse(playingRole, playerId);
		// ret
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
		//国战通关判断
		boolean isAllPass = GuoZhanFightManager.getInstance().checkAllOccupy(guoZhanFight);
		if(isAllPass) {
			GuoZhanFightManager.getInstance().scheduleResetGuoZhanFight();
		}
	}

	public synchronized void processorCityCalculate(PlayingRole playingRole, WsMessageBase.IOBattleResult btlRes, int myNationId, int playerId,
			int cityIndex, int[] hp_perc, boolean setNotInBattle) {
		WsMessageBattle.S2CGuozhanCityCalculate respMsg = new WsMessageBattle.S2CGuozhanCityCalculate();
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		if(btlRes.ret.equals("lose")) {//失败
			DBGuoZhanCity guoZhanCity = guoZhanFight.getCitys(cityIndex);
			 int cityNationId = guoZhanCity.getOwnNationId();
			if(myNationId == cityNationId && guoZhanCity.getOccupyPlayerCount()<GuozhanConstants.CITY_MAX_PLAYER_SIZE) {
				//移动到该城市
				DBGuoZhanFight.Builder guoZhanFightBuilder = move2City(guoZhanFight.toBuilder(), playerId, cityIndex);
				guoZhanFight = guoZhanFightBuilder.build();
				GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);
			}
			if (myNationId != cityNationId) {
				//防守方血量百分比
				DBGuoZhanFight.Builder guoZhanFightBuilder = guoZhanFight.toBuilder();
				DBGuoZhanCity.Builder oldCityBuilder = guoZhanFightBuilder.getCitysBuilder(cityIndex);
				//战斗中状态
				if(setNotInBattle) {
					oldCityBuilder.setInBattle(false);
				}
				int oldCityNationId = oldCityBuilder.getOwnNationId();
				List<Integer> occupyPlayers = oldCityBuilder.getOccupyPlayerList();
				List<Integer> tmp_occupyPlayers = new ArrayList<>(occupyPlayers);
				int i = 0;
				int targetPlayerId = 0;
				for (Integer defencePlayerId : tmp_occupyPlayers) {
					if(i == 0) {
						targetPlayerId = defencePlayerId;
					}
					DBGuoZhanRole guoZhanRole = guoZhanFightBuilder.getPlayersOrDefault(defencePlayerId, null);
					int hp_perc1 = 100;
					if (i < hp_perc.length) {
						hp_perc1 = hp_perc[i];
					}
					i++;
					if (guoZhanRole != null) {
						// 被打败
						if (hp_perc1 <= 0) {
							//系统恢复40%血量
							DBGuoZhanRole.Builder roleBuilder = guoZhanRole.toBuilder().setHpPerc(40)
									.setLastAttackTime(System.currentTimeMillis());
							//历史记录 防守win，移城
							DBGuoZhanHistory.Builder historyBuilder = roleBuilder.addMyRecordBuilder().setType(4)
									.addParams(myNationId).addParams(cityIndex).setAddTime(System.currentTimeMillis());
							historyBuilder.setTargetPlayerId(playerId);
							GuoZhanFightManager.getInstance().limitBattleRecord(roleBuilder);
							guoZhanFightBuilder.putPlayers(defencePlayerId, roleBuilder.build());
							//移除老的城池
							List<Integer> oldPlayerList = oldCityBuilder.getOccupyPlayerList();
							List<Integer> playerLists = new ArrayList<>(oldPlayerList);
							playerLists.remove(Integer.valueOf(defencePlayerId));
							oldCityBuilder.clearOccupyPlayer().addAllOccupyPlayer(playerLists);
							GuoZhanFightManager.getInstance().playerRandPos(guoZhanFightBuilder, defencePlayerId,
									cityNationId, -1);
						} else {
							DBGuoZhanRole.Builder roleBuilder = guoZhanRole.toBuilder().setHpPerc(hp_perc1)
									.setLastAttackTime(System.currentTimeMillis());
							//历史记录 防守win
							DBGuoZhanHistory.Builder historyBuilder = roleBuilder.addMyRecordBuilder().setType(3)
									.addParams(myNationId).addParams(cityIndex).setAddTime(System.currentTimeMillis());
							historyBuilder.setTargetPlayerId(playerId);
							GuoZhanFightManager.getInstance().limitBattleRecord(roleBuilder);
							guoZhanFightBuilder.putPlayers(defencePlayerId, roleBuilder.build());
						}
					}
				}
				// 我方血量
				DBGuoZhanRole myRole = guoZhanFightBuilder.getPlayersOrDefault(playerId, null);
				if (myRole != null && GuoZhanFightManager.getInstance().getPlayerHpPercent(playerId) > 40) {
					myRole = myRole.toBuilder().setHpPerc(40).setLastAttackTime(System.currentTimeMillis()).build();
					guoZhanFightBuilder.putPlayers(playerId, myRole);
				}
				if (myRole != null) {
					//历史记录 进攻loose
					DBGuoZhanRole.Builder guoZhanRoleBuilder = myRole.toBuilder();
					DBGuoZhanHistory.Builder historyBuilder = guoZhanRoleBuilder.addMyRecordBuilder().setType(2)
							.addParams(tmp_occupyPlayers.size()).addParams(oldCityNationId).addParams(cityIndex)
							.setAddTime(System.currentTimeMillis());
					if (tmp_occupyPlayers.size() > 0) {
						historyBuilder.setTargetPlayerId(targetPlayerId);
					}
					GuoZhanFightManager.getInstance().limitBattleRecord(guoZhanRoleBuilder);
					guoZhanFightBuilder.putPlayers(playerId, guoZhanRoleBuilder.build());
				}
				// save
				guoZhanFight = guoZhanFightBuilder.build();
				GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);
			}else {
				DBGuoZhanFight.Builder guoZhanFightBuilder = guoZhanFight.toBuilder();
				DBGuoZhanCity.Builder oldCityBuilder = guoZhanFightBuilder.getCitysBuilder(cityIndex);
				//战斗中状态
				if(setNotInBattle) {
					oldCityBuilder.setInBattle(false);
					guoZhanFight = guoZhanFightBuilder.build();
					GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);
				}
			}
		}else {//获胜
			DBGuoZhanFight.Builder guoZhanFightBuilder = guoZhanFight.toBuilder();
			DBGuoZhanCity.Builder guoZhanCityBuilder = guoZhanFightBuilder.getCitysBuilder(cityIndex);
			 int cityNationId = guoZhanCityBuilder.getOwnNationId();
			//老的踢掉
			int defencePlayerSize = 0;
			int targetPlayerId = 0;
			if(myNationId != cityNationId) {
				List<Integer> oldPlayerIds = guoZhanCityBuilder.getOccupyPlayerList();
				defencePlayerSize = oldPlayerIds.size();
				int i =0;
				for (Integer aPlayerId : oldPlayerIds) {
					if(i++ == 0) {
						targetPlayerId = aPlayerId;
					}
					GuoZhanFightManager.getInstance().playerRandPos(guoZhanFightBuilder, aPlayerId, cityNationId, cityIndex);
					// 恢复50%血量
					DBGuoZhanRole role1 = guoZhanFightBuilder.getPlayersOrDefault(aPlayerId, null);
					if (role1 != null) {
						DBGuoZhanRole.Builder roleBuilder = role1.toBuilder().setHpPerc(40)
								.setLastAttackTime(System.currentTimeMillis());
						//历史记录 防守loose
						DBGuoZhanHistory.Builder historyBuilder = roleBuilder.addMyRecordBuilder().setType(5)
								.addParams(myNationId).addParams(cityIndex).setAddTime(System.currentTimeMillis());
						historyBuilder.setTargetPlayerId(playerId);
						GuoZhanFightManager.getInstance().limitBattleRecord(roleBuilder);
						guoZhanFightBuilder.putPlayers(aPlayerId, roleBuilder.build());
					}
				}
			}
			//移动到该城市
			guoZhanFightBuilder = move2City(guoZhanFightBuilder, playerId, cityIndex);
			//状态
			guoZhanCityBuilder.setOwnNationId(myNationId);
			//战斗中状态
			if(setNotInBattle) {
				guoZhanCityBuilder.setInBattle(false);
			}
			if(myNationId != cityNationId) {
				guoZhanCityBuilder.clearOccupyPlayer().addOccupyPlayer(playerId);
			}else {
				guoZhanCityBuilder.addOccupyPlayer(playerId);
			}
			//国家拥有城市数量更新
			GuoZhanFightManager.getInstance().updateNationOwnCitys(guoZhanFightBuilder);
			//血量
			DBGuoZhanRole.Builder guoZhanRoleBuilder = guoZhanFightBuilder.getPlayersOrDefault(playerId, null).toBuilder();
			guoZhanRoleBuilder.setHpPerc(hp_perc[0]).setLastAttackTime(System.currentTimeMillis());
			//历史记录，进攻win
			DBGuoZhanHistory.Builder historyBuilder = guoZhanRoleBuilder.addMyRecordBuilder().setType(1)
					.addParams(defencePlayerSize).addParams(cityNationId).addParams(cityIndex).setAddTime(System.currentTimeMillis());
			if (defencePlayerSize > 0) {
				historyBuilder.setTargetPlayerId(targetPlayerId);
			}
			GuoZhanFightManager.getInstance().limitBattleRecord(guoZhanRoleBuilder);
			guoZhanFightBuilder.putPlayers(playerId, guoZhanRoleBuilder.build());
			//save
			guoZhanFight = guoZhanFightBuilder.build();
			GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);
			//奖励
			// 奖励铜币
			int rewardCoins = 500000 + 50000 * defencePlayerSize;
			// 道具奖励
			List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
			RandomDispatcher<Integer> rd = new RandomDispatcher<>();
			for (int i = 0; i < PvpConstants.REWARD_RATE.length; i++) {
				rd.put(PvpConstants.REWARD_RATE[i], i);
			}
			Integer aIndex = rd.randomRemove();
			int[] rewardConfig = PvpConstants.PVP_REWARD[aIndex];
			int gsid = rewardConfig[0];
			int countMin = rewardConfig[1];
			int countMax = rewardConfig[2];
			int gsCount = RandomUtils.nextInt(countMin, countMax + 1);
			if (gsid == GameConfig.PLAYER.GOLD) {
				gsCount += rewardCoins;
			} else {
				AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, rewardCoins, LogConstants.MODULE_GUOZHAN);
				rewardItems.add(new WsMessageBase.IORewardItem(GameConfig.PLAYER.GOLD, rewardCoins));
			}
			AwardUtils.changeRes(playingRole, gsid, gsCount, LogConstants.MODULE_GUOZHAN);
			rewardItems.add(new WsMessageBase.IORewardItem(gsid, gsCount));
			respMsg.reward = rewardItems;
		}
		respMsg.result = btlRes;
		respMsg.city_index = cityIndex;
		respMsg.move_step = GuoZhanFightManager.getInstance().getMyMoveStep(guoZhanFight.getPlayersOrDefault(playerId, null));
		//发送更新的国战城池争夺信息
//		GuozhanFightViewProcessor.sendResponse(playingRole, playerId);
		// send
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
		//国战通关判断
		boolean isAllPass = GuoZhanFightManager.getInstance().checkAllOccupy(guoZhanFight);
		if(isAllPass) {
			GuoZhanFightManager.getInstance().scheduleResetGuoZhanFight();
		}
	}

	private DBGuoZhanFight.Builder move2City(DBGuoZhanFight.Builder fightBuilder,int playerId,int cityIndex) {
		DBGuoZhanRole.Builder guoZhanRoleBuilder = fightBuilder.getPlayersOrDefault(playerId, null).toBuilder();
		int oldCityIndex = guoZhanRoleBuilder.getCityIndex();
		guoZhanRoleBuilder.setCityIndex(cityIndex);
		fightBuilder.putPlayers(playerId, guoZhanRoleBuilder.build());
		//移除老的city
		if(oldCityIndex >=0) {
			DBGuoZhanCity.Builder oldCityBuilder = fightBuilder.getCitysBuilder(oldCityIndex);
			List<Integer> oldPlayerList = oldCityBuilder.getOccupyPlayerList();
			List<Integer> playerLists = new ArrayList<>(oldPlayerList);
			playerLists.remove(Integer.valueOf(playerId));
			oldCityBuilder.clearOccupyPlayer().addAllOccupyPlayer(playerLists);
		}
		fightBuilder.getCitysBuilder(cityIndex).addOccupyPlayer(playerId);
		//
		return fightBuilder;
	}

}
