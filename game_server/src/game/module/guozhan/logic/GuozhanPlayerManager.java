package game.module.guozhan.logic;

import db.proto.ProtoMessageGuozhan.DBGuozhanPlayer;
import game.module.battle.dao.BattlePlayerBase;
import game.module.exped.logic.FormationRobotManager;
import game.module.guozhan.bean.GuozhanPlayer;
import game.module.guozhan.dao.CityJoinTemplateCache;
import game.module.guozhan.dao.GuozhanCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.mine.bean.DBMinePoint;
import game.module.template.GeneralTemplate;
import game.module.user.logic.PlayerHeadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GuozhanPlayerManager {

	private static Logger logger = LoggerFactory.getLogger(GuozhanPlayerManager.class);

	static class SingletonHolder {
		static GuozhanPlayerManager instance = new GuozhanPlayerManager();
	}

	public static GuozhanPlayerManager getInstance() {
		return SingletonHolder.instance;
	}

	public GuozhanPlayer createGuozhanPlayer(int playerId) {
		GuozhanPlayer guozhanPlayer = new GuozhanPlayer();
		guozhanPlayer.setPlayerId(playerId);
		guozhanPlayer.setStay_city_index(0);
		guozhanPlayer.setDbGuozhanPlayer(DBGuozhanPlayer.newBuilder().build());
		return guozhanPlayer;
	}

	public DBMinePoint createCityPointRobots(DBGuozhanPlayer dbBuilder) {
		int existCount = 0;
		if(dbBuilder != null){
			existCount = dbBuilder.getPassCityIndexCount();
		}
		int robotLevel = (int) (existCount / 2.5f) + GuozhanConstants.GUOZHAN_CITY_ENEMY_LEVEL;
		Map<Integer, BattlePlayerBase> battlePlayerMap = FormationRobotManager.getInstance().generateRobot(robotLevel);
		DBMinePoint guozhanRobot = new DBMinePoint();
		BattlePlayerBase next = battlePlayerMap.values().iterator().next();
		int gsid = next.getGsid();
		GeneralTemplate heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
		String rname = heroTemplate.getNAME() + heroTemplate.getSTAR() + "星" + GeneralTemplateCache.getInstance().getOccuCn(heroTemplate.getOCCU());
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

	public int getEnemyLevel(DBGuozhanPlayer dbBuilder) {
		int existCount = 0;
		if(dbBuilder != null){
			existCount = dbBuilder.getPassCityIndexCount();
		}
		int robotLevel = (int) (existCount / 2.5f) + GuozhanConstants.GUOZHAN_CITY_ENEMY_LEVEL;
		return robotLevel;
	}

	public boolean checkRedPoints(int playerId) {
		boolean ret = true;
		GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
		int citySize = CityJoinTemplateCache.getInstance().getSize();
		if (guozhanPlayer != null && guozhanPlayer.getDbGuozhanPlayer() != null
				&& guozhanPlayer.getDbGuozhanPlayer().getPassCityIndexCount() >= citySize) {
			ret = false;
		}
		return ret;
	}

	public void gmPassGuoZhanPve(int playerId) {
		GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
		if (guozhanPlayer == null) {
			return;
		}
		DBGuozhanPlayer dbGuozhanPlayer = guozhanPlayer.getDbGuozhanPlayer();
		DBGuozhanPlayer.Builder guozhanBuilder;
		if (dbGuozhanPlayer == null) {
			guozhanBuilder = DBGuozhanPlayer.newBuilder();
		} else {
			guozhanBuilder = dbGuozhanPlayer.toBuilder();
		}
		//
		int citySize = CityJoinTemplateCache.getInstance().getSize();
		for (int i = 0; i < citySize; i++) {
			guozhanBuilder.putPassCityIndex(i, true);
		}
		dbGuozhanPlayer = guozhanBuilder.build();
		guozhanPlayer.setDbGuozhanPlayer(dbGuozhanPlayer);
		guozhanPlayer.setStay_city_index(0);
		GuozhanDaoHelper.asyncUpdateGuozhanPlayer(guozhanPlayer);
	}

	public boolean isGuozhanPvp(int playerId){
		boolean ret = false;
		GuozhanPlayer guozhanPlayer = GuozhanCache.getInstance().getGuozhanPlayer(playerId);
		int citySize = CityJoinTemplateCache.getInstance().getSize();
		if (guozhanPlayer != null && guozhanPlayer.getDbGuozhanPlayer() != null
				&& guozhanPlayer.getDbGuozhanPlayer().getPassCityIndexCount() >= citySize && guozhanPlayer.getNation() > 0) {
			ret = true;
		}
		return ret;
	}

}
