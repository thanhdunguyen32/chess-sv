package game.module.guozhan.logic;

import db.proto.ProtoMessageGuozhan.DBGuoZhanFight;
import db.proto.ProtoMessageGuozhan.DBGuoZhanNation;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOffice;
import db.proto.ProtoMessageGuozhan.DBGuoZhanOfficePoint;
import game.GameServer;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.logic.ActivityManager;
import game.module.award.bean.GameConfig;
import game.module.battle.dao.BattlePlayerBase;
import game.module.exped.logic.FormationRobotManager;
import game.module.gm.logic.GmManager;
import game.module.guozhan.bean.GuozhanOfficeTemplate;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.bean.GuozhanRewardTemplate;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.dao.GuozhanOfficeTemplateCache;
import game.module.guozhan.dao.GuozhanRewardTemplateCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.mail.logic.MailManager;
import game.module.mine.bean.DBMinePoint;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.template.GeneralTemplate;
import game.module.user.logic.PlayerHeadManager;
import game.module.user.logic.ScrollAnnoManager;
import io.netty.util.Timeout;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GuoZhanManager implements Job {

	private static Logger logger = LoggerFactory.getLogger(GuoZhanManager.class);

	static class SingletonHolder {
		static GuoZhanManager instance = new GuoZhanManager();
	}

	public static GuoZhanManager getInstance() {
		return SingletonHolder.instance;
	}

	public Map<Integer, Timeout> scheduleMineFinishMap = new ConcurrentHashMap<>();


	public void addScheduleTimeout(int pointVal, Timeout aTimeout) {
		scheduleMineFinishMap.put(pointVal, aTimeout);
	}

	public Timeout getScheduleTimeout(int pointVal) {
		return scheduleMineFinishMap.get(pointVal);
	}

	public void removeScheduleTimeout(int pointVal) {
		scheduleMineFinishMap.remove(pointVal);
	}

	public void clearScheduleTimeout() {
		for(Timeout timeout : scheduleMineFinishMap.values()) {
			timeout.cancel();
		}
		scheduleMineFinishMap.clear();
	}

	public DBMinePoint generateRobot(int officePointId) {
		int officeLevelId = officePointId / 10;
		int robotLevel = (10 - officeLevelId) * 12 + 100;
        Map<Integer, BattlePlayerBase> battlePlayerMap = FormationRobotManager.getInstance().generateRobot(robotLevel);
        DBMinePoint guozhanRobot = new DBMinePoint();
        BattlePlayerBase next = battlePlayerMap.values().iterator().next();
        int gsid = next.getGsid();
        GeneralTemplate heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
        String rname = heroTemplate.getNAME() + heroTemplate.getSTAR() + " sao " + GeneralTemplateCache.getInstance().getOccuCn(heroTemplate.getOCCU());
        guozhanRobot.setRname(rname);
        guozhanRobot.setLevel(robotLevel);
        int headid = PlayerHeadManager.getInstance().getHeadid(gsid);
        int iconId = PlayerHeadManager.getInstance().headId2IconId(headid);
        guozhanRobot.setIconid(iconId);
        guozhanRobot.setHeadid(headid);
        guozhanRobot.setFrameid(51001);
        guozhanRobot.setPower(FormationRobotManager.getInstance().getPower(battlePlayerMap));
        guozhanRobot.setBattlePlayerMap(battlePlayerMap);
		return guozhanRobot;
	}

	public DBGuoZhanOfficePoint getGuozhanPoint(DBGuoZhanNation aNation, int officeIdx) {
		DBGuoZhanOfficePoint guoZhanOfficePoint = aNation.getPlayerOffices(officeIdx);
		return guoZhanOfficePoint;
	}

	public DBGuoZhanOffice.Builder createGuoZhanOffice() {
		DBGuoZhanOffice.Builder builder = DBGuoZhanOffice.newBuilder();
		for(int i=0;i<3;i++) {
			builder.addNationsBuilder();
		}
		return builder;
	}

	public void createGuozhanPoints(DBGuoZhanOffice.Builder builder,int nationId){
		DBGuoZhanNation.Builder nationBuilder = builder.getNationsBuilder(nationId-1);
		for(int i=0;i<100;i++) {
			nationBuilder.addPlayerOfficesBuilder().setPlayerId(0).setIsFighting(false);
		}
	}

	public int getPointIndexById(int officeId) {
		int levelIndex = officeId / 10 - 1;
		int pointIndex = officeId % 10 - 1;
		return levelIndex * 10 + pointIndex;
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("guozhan reward start!");
				GuoZhanManager.getInstance().guozhanSendReward();
				logger.info("guozhan reward end!");
				logger.info("guozhan city fight reward start!");
				GuoZhanFightManager.getInstance().scheduleSendOccupyReward();
				logger.info("guozhan city fight reward end!");
			}
		});
	}

	/**
	 * 官职每日奖励
	 */
	public void guozhanSendReward() {
		DBGuoZhanOffice guoZhanOffice = GuozhanCache.getInstance().getDBGuoZhanOffice();
		if(guoZhanOffice == null) {
			return;
		}
		List<Integer> nationOwnCitys = new ArrayList<>();
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		if(guoZhanFight != null) {
			nationOwnCitys = guoZhanFight.getNationOwnCityList();
		}
		List<DBGuoZhanNation> nations = guoZhanOffice.getNationsList();
		int nationIndex = 0;
		List<GuozhanOfficeTemplate> officeTemplates = GuozhanOfficeTemplateCache.getInstance().getTemplates();
		List<GuozhanRewardTemplate> guozhanRewardTemplates = GuozhanRewardTemplateCache.getInstance().getTemplates();
		for (DBGuoZhanNation dbGuoZhanNation : nations) {
			List<DBGuoZhanOfficePoint> playerList = dbGuoZhanNation.getPlayerOfficesList();
			if(playerList.size() == 0) {
				nationIndex++;
				continue;
			}
			//占领城池越少，奖励递减
			int ownCitys = 22;
			if(nationIndex < nationOwnCitys.size()) {
				ownCitys = nationOwnCitys.get(nationIndex);
			}
			float rewardPercent = 1/3f+ownCitys/33f;
			rewardPercent = Math.min(rewardPercent, 1.6f);
			int levelIndex = 0;
			for (GuozhanOfficeTemplate guozhanOfficeTemplate : officeTemplates) {
				int levelSize = guozhanOfficeTemplate.getCount();
				for (int i = 0; i < levelSize; i++) {
					int bigIndex = levelIndex * 10 + i;
					DBGuoZhanOfficePoint guoZhanOfficePoint = playerList.get(bigIndex);
					int targetPlayerId = guoZhanOfficePoint.getPlayerId();
					if (targetPlayerId > 0) {
                        //奖励
                        GuozhanRewardTemplate rewardTemplate = guozhanRewardTemplates.get(levelIndex);
                        //发送邮件
                        Map<Integer, Integer> dbMailAtt = getMailAtt(rewardTemplate, rewardPercent);
                        // 邮件奖励
                        String posStr = guozhanOfficeTemplate.getPositions().get(i);
                        if (levelIndex == 0) {
                            String nationName = GuozhanConstants.NATION_LIST[nationIndex];
                            posStr = nationName + posStr;
                        }
                        // %s: quốc gia
                        // %d: số lượng thành trì
						String mailTitle = "Phần thưởng hàng ngày từ đấu trường diễn võ";
						String mailContent = "Chúc mừng bạn đã đạt được vị trí thứ %1$d trong đấu trường diễn võ, bạn đã nhận được các phần thưởng sau: Hãy tiếp tục cố gắng!";
                        mailContent = String.format(mailContent,guozhanOfficeTemplate.getTitle(),posStr);
						MailManager.getInstance().sendSysMailToSingle(targetPlayerId, mailTitle,mailContent, dbMailAtt);
					}
				}
				levelIndex++;
			}
			nationIndex++;
		}

	}

	private Map<Integer,Integer> getMailAtt(GuozhanRewardTemplate awardConfig, float rewardPercent) {
		int activityDouble = ActivityManager.getInstance().getMoudelMultiple(ActivityConstants.AWARD_DOUBLE_GUOZHAN);
		Map<Integer,Integer> rewardsMap = new HashMap<>();
		if (awardConfig.getGamemoney() > 0) {
			int itemId = GameConfig.PLAYER.GOLD;
            rewardsMap.put(itemId, (int) (awardConfig.getGamemoney() * rewardPercent * activityDouble));
		}
		if (awardConfig.getMoney() > 0) {
			int itemId = GameConfig.PLAYER.YB;
            rewardsMap.put(itemId, (int) (awardConfig.getMoney() * rewardPercent * activityDouble));
		}
		if (awardConfig.getItems() != null && awardConfig.getItems().size() > 0) {
			for (List<Integer> aItemPair : awardConfig.getItems()) {
				int itemId = aItemPair.get(0);
				int count = aItemPair.get(1);
                rewardsMap.put(itemId, count * activityDouble);
			}
		}
		return rewardsMap;
	}

	public void kingSendMarquee(int playerId,String playerName) {
		DBGuoZhanOffice guoZhanOffice = GuozhanCache.getInstance().getDBGuoZhanOffice();
		if(guoZhanOffice == null) {
			return;
		}
		int officeIndex = guoZhanOffice.getPlayerPointOrDefault(playerId, -1);
		if (officeIndex == 0) {
			GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
			int nationId = guozhanPlayer.getNation();
			String nationName = GuozhanConstants.NATION_LIST[nationId - 1];
			ScrollAnnoManager.getInstance().kingOnline(nationName + "King·" + playerName);
		}
	}

	public int getOfficeIndex(int playerId) {
		int ret = -1;
		DBGuoZhanOffice guoZhanOffice = GuozhanCache.getInstance().getDBGuoZhanOffice();
		if (guoZhanOffice == null) {
			return ret;
		}
		ret = guoZhanOffice.getPlayerPointOrDefault(playerId, -1);
		// 国家id
		GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
		if(guozhanPlayer != null) {
			ret += guozhanPlayer.getNation() * 100;
		}else {
			PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(playerId);
			if (poc != null && poc.getNationId() != null) {
				ret += poc.getNationId() * 100;
			}
		}
		return ret;
	}

}
