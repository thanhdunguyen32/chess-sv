package game.common;

import game.entity.PlayingRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleUtils {

	private static Logger logger = LoggerFactory.getLogger(BattleUtils.class);

	static class SingletonHolder {
		static BattleUtils instance = new BattleUtils();
	}

	public static BattleUtils getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 防作弊
	 * 
	 * @param attackInfo
	 * @param playingRole
	 * @return
	 */
//	public boolean antiCheet(BeanAttack1 attackInfo, PlayingRole playingRole) {
//		if (attackInfo.getRandBattleId() != playingRole.getPlayerCacheStatus().getRandBattleId()) {
//			logger.warn("anti cheat,find battle id not match,dest={},pass={}",
//					playingRole.getPlayerCacheStatus().getRandBattleId(), attackInfo.getRandBattleId());
//			clickPlayer(playingRole);
//			return false;
//		}
//		float serverDamage = BattleFormula.attackCalc(attackInfo.getIsPhysical(), attackInfo.getAddedDamage(),
//				attackInfo.getAttackerAttack(), attackInfo.getAttackeeArmor(), attackInfo.getArmorThrough(),
//				attackInfo.getAttackerMagic(), attackInfo.getAttackeeMagicDefence());
//		float clientDamage = attackInfo.getEndValue();
//		if (Math.abs(serverDamage - clientDamage) > 0.2f) {
//			logger.info("anti cheat,attack damage not match,clientDamage={},serverDamage={}", clientDamage,
//					serverDamage);
//			clickPlayer(playingRole);
//			return false;
//		}
//		return true;
//	}

	public void clickPlayer(PlayingRole playingRole) {
//		playingRole.getGamePlayer().writeAndFlush(10114, S2CPlayerKickOff.newBuilder().build());
		//Long sessionId = playingRole.getGamePlayer().getSessionId();
		//SessionManager.getInstance().removeLogicHero(sessionId);
		//playingRole.getGamePlayer().saveSessionId(null);
		playingRole.getGamePlayer().close();
	}

}
