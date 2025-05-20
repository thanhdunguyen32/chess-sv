package game.module.secret.logic;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.proto.ProtoMessageSecret.DBSecretBoxAward;
import db.proto.ProtoMessageSecret.DBSecretUsedHero;
import game.GameServer;
import game.common.CommonUtils;
import game.common.DateCommonUtils;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import game.module.secret.bean.Secret;
import game.module.secret.constants.SecretConstants;
import game.module.secret.dao.SecretCache;
import game.module.secret.dao.SecretDao;

/**
 * 秘密基地逻辑处理
 * 
 * @author zhangning
 * 
 * @Date 2015年1月27日 下午1:42:38
 */
public class SecretManager {

	private static Logger logger = LoggerFactory.getLogger(SecretManager.class);

	SecretDao secretDao = SecretDao.getInstance();

	static class SingletonHolder {
		static SecretManager instance = new SecretManager();
	}

	public static SecretManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 下线--秘密基地战斗中
	 * 
	 * @param playingRole
	 */
	public void downlineSecret(PlayingRole playingRole) {
//		CharacterInfoManager.getInstance().changeStrength(playingRole, -SecretConstants.DEFAULT_LOSE_STRENGTH);
		// 更新状态
		playingRole.getPlayerCacheStatus().setPosition(PlayerPosition.PLAYER_POSITION_HALL);
	}

	public void reset(PlayingRole hero) {
		Secret secret = SecretCache.getInstance().getSecret(hero.getId());
		if (secret != null) {
			secret.setMapId(0);
			secret.setProgress(0);
			secret.setReviveCount(0);
			secret.setBoxAward(DBSecretBoxAward.newBuilder().build());
			secret.setFormationHeros(DBSecretUsedHero.newBuilder().build());
			secret.setMyCost(DBSecretUsedHero.newBuilder().build());
			secret.setEnemyCost(DBSecretUsedHero.newBuilder().build());
			secret.setResetTime(new Date());
			SecretManager.getInstance().asyncUpdateSecretAll(secret);
		}
	}

	/**
	 * 异步添加一条秘密基地记录
	 * 
	 * @param secret
	 */
	public void asyncInsertSecret(final Secret secret) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				secretDao.addSecret(secret);
			}
		});
	}

	/**
	 * 异步更新秘密基地信息
	 * 
	 * @param secret
	 */
	public void asyncUpdateSecret(final Secret secret) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				secretDao.updateSecretAll(secret);
			}
		});
	}

	/**
	 * 异步更新随机奖励
	 * 
	 * @param mail
	 */
	public void asyncUpdateRandomAward(final Secret secret) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				secretDao.updateRandomAward(secret);
			}
		});
	}

	/**
	 * 异步更新上阵英雄+士兵
	 * 
	 * @param secret
	 */
	public void asyncUpdateSecretHero(final Secret secret) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				secretDao.updateSecretHero(secret);
			}
		});
	}

	/**
	 * 异步更新已经用过的英雄+士兵
	 * 
	 * @param secret
	 */
	public void asyncUpdateUsedHero(final Secret secret) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				secretDao.updateUsedHero(secret);
			}
		});
	}

	/**
	 * 异步更新秘密基地所有信息
	 * 
	 * @param secret
	 */
	public void asyncUpdateSecretAll(final Secret secret) {
		GameServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				secretDao.updateSecretAll(secret);
			}
		});
	}

	public boolean checkRedPoints(int playerId) {
		Secret secret = SecretCache.getInstance().getSecret(playerId);
		if (secret == null) {
			return true;
		}
		// 重置次数
		if (secret.getResetTime() == null
				|| !DateCommonUtils.isSameDay(secret.getResetTime(), CommonUtils.RESET_HOUR)) {
			return true;
		}
		// 是否通关
		if (secret.getProgress() < 5) {
			return true;
		}
		return false;
	}

}
