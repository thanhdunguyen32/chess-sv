package game.module.secret.processor;

import java.util.List;

import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import lion.netty4.message.RequestByteMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.proto.ProtoMessageSecret.DBSecretUsedHero;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.log.constants.LogConstants;
import game.module.secret.bean.Secret;
import game.module.secret.dao.SecretCache;
import game.module.secret.logic.SecretManager;
import game.module.user.dao.CommonTemplateCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestProtoMessage;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageSecret.C2SSecretSoldierRevive;
import ws.WsMessageSecret.S2CSecretSoldierRevive;

/**
 * 复活士兵
 * 
 * @author zhangning
 * 
 * @Date 2015年1月27日 下午6:42:14
 */
@MsgCodeAnn(msgcode = C2SSecretSoldierRevive.id, accessLimit = 500)
public class SecretSoldierReviveProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(SecretSoldierReviveProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

	}

	/**
	 * 复活士兵
	 */
	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		
	}

	/**
	 * 死亡士兵
	 * 
	 * @param usedHeros
	 * @param npcSite
	 * @param npcNum
	 * @return
	 */
	private boolean getUsedHero(List<Integer> usedHeros, int heroType, int npcSite) {
		if (usedHeros != null && npcSite < usedHeros.size()) {
			int soldierHp = usedHeros.get(npcSite);
			if(soldierHp != -1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		C2SSecretSoldierRevive reqMsg = C2SSecretSoldierRevive.parse(request);
		final int hero_id = reqMsg.hero_id;
		int playerId = playingRole.getId();
		logger.info("Secret soldier revive, target_hero_id={},playerId={}", hero_id, playerId);

		// 参数有误
		if (hero_id < -10) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretSoldierRevive.msgCode, 601);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}

		// 没有秘密基地记录
		SecretCache secretCache = SecretCache.getInstance();
		Secret secret = secretCache.getSecret(playerId);
		if (secret == null) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretSoldierRevive.msgCode,601);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}

		// 钻石不足
		List<Integer> reviveCostDiamonds = (List<Integer>)CommonTemplateCache.getInstance().getConfig("secret_soldier_revive");
		int currentReviveCount = secret.getReviveCount();
		int costDiamond = 0;
		if (currentReviveCount < reviveCostDiamonds.size()) {
			costDiamond = reviveCostDiamonds.get(currentReviveCount);
		} else {
			costDiamond = reviveCostDiamonds.get(reviveCostDiamonds.size()-1);
		}
		if (costDiamond > playingRole.getPlayerBean().getMoney()) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretSoldierRevive.msgCode,612);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}

		// 死亡士兵不存在
		DBSecretUsedHero usedHero = secret.getMyCost();
		if(hero_id > 0) {
			int heroHp = usedHero.getHeroUsedOrDefault(hero_id, -1);
			if(heroHp == -1) {
				S2CErrorCode respMsg = new S2CErrorCode(S2CSecretSoldierRevive.msgCode,605);
				playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				return;
			}
		}else {
			int soldierHp = usedHero.getSoldierUsed(-hero_id);
			if(soldierHp == -1) {
				S2CErrorCode respMsg = new S2CErrorCode(S2CSecretSoldierRevive.msgCode,605);
				playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				return;
			}
		}
		// do
		DBSecretUsedHero.Builder myCostBuilder = usedHero.toBuilder();
		if(hero_id > 0) {
			myCostBuilder.removeHeroUsed(hero_id);
		}else {
			myCostBuilder.setSoldierUsed(-hero_id, -1);
		}
		usedHero = myCostBuilder.build();
		secret.setMyCost(usedHero);
		secret.setReviveCount(secret.getReviveCount() + 1);
		SecretManager.getInstance().asyncUpdateUsedHero(secret);
		// 扣除钻石
		AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -costDiamond, LogConstants.MODULE_SECRET);
		// ret
		S2CSecretSoldierRevive respMsg = new S2CSecretSoldierRevive(hero_id);
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
