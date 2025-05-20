package game.module.secret.processor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import lion.netty4.message.RequestByteMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.proto.ProtoMessageSecret.DBSecretUsedHero;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import game.module.secret.bean.Secret;
import game.module.secret.bean.SecretTemplate;
import game.module.secret.constants.SecretConstants;
import game.module.secret.dao.SecretCache;
import game.module.secret.dao.SecretTemplateCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestProtoMessage;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageSecret.C2SSecretBattleStart;
import ws.WsMessageBase.IOSecretHero;
import ws.WsMessageSecret.S2CSecretBattleStart;

/**
 * 选择副本关卡, 开始战斗
 * 
 * @author zhangning
 * 
 * @Date 2015年1月27日 下午3:46:15
 */
@MsgCodeAnn(msgcode = C2SSecretBattleStart.id, accessLimit = 500)
public class SecretWarStartProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(SecretWarStartProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

	}

	/**
	 * 选择副本关卡, 开始战斗
	 */
	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		C2SSecretBattleStart secretWarMsg = C2SSecretBattleStart.parse(request);
		int playerId = playingRole.getId();
		int mapId = secretWarMsg.map_id;
		logger.info("Secret War start battle, msg={}, playerId={}", secretWarMsg, playerId);
		// 正在战斗中
//		if (playingRole.getPlayerCacheStatus().getPosition() == PlayerPosition.PLAYER_POSITION_SECRET) {
//			playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_WAR, RetCode.SECRET_IN_BATTLE);
//			return;
//		}
		// secret bean是否存在
		SecretCache secretCache = SecretCache.getInstance();
		Secret secret = secretCache.getSecret(playingRole.getId());
		if (secret == null) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 601);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 是否第一关选难度
		int stageProgress = secret.getProgress();
		if (stageProgress == 0) {
			SecretTemplate st = SecretTemplateCache.getInstance().getSecretTemp(mapId);
			if (st == null) {
				S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 623);
				playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				return;
			}
			// 玩家等级不足
			if (playingRole.getPlayerBean().getLevel() < st.getLv()) {
				S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 624);
				playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				return;
			}
		}
		// 获得要挑战的关卡
		SecretTemplate currentStageTpl = SecretTemplateCache.getInstance().getSecretTemp(secret.getMapId());
		if (stageProgress == 0) {
			currentStageTpl = SecretTemplateCache.getInstance().getSecretTemp(mapId);
		}
		int[] win_strength = currentStageTpl.getWin_strength();
		// 是否已经通关
		int currentProgress = secret.getProgress();
		if (currentProgress >= win_strength.length) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 625);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		int winCostStrength = win_strength[currentProgress];
		// 体力值不足
		if (playingRole.getPlayerBean().getMoney() < winCostStrength) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 607);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 上阵英雄和士兵是否正确
		IOSecretHero[] onlineFormationList = secretWarMsg.online_formation;
		if (onlineFormationList == null || onlineFormationList.length > 8) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 610);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 上阵英雄参数检测
		int heroCount = 0;
		Set<Integer> heroIdSet = new HashSet<>();
		Set<Integer> solderPosSet = new HashSet<>();
		Map<Long, GeneralBean> heroAll = GeneralCache.getInstance().getHeros(playerId);
		for (IOSecretHero secretHero : onlineFormationList) {
			int heroType = secretHero.hero_type;
			if (heroType != SecretConstants.HERO && heroType != SecretConstants.NPC) {
				S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 601);
				playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				return;
			}
			if (heroType == SecretConstants.HERO) {
				int heroId = secretHero.hero_id;
				if (heroIdSet.contains(heroId)) {
					S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 601);
					playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
					return;
				}
				if (!heroAll.containsKey(heroId)) {
					S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 601);
					playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
					return;
				}
				heroIdSet.add(heroId);
				heroCount++;
			} else if (heroType == SecretConstants.NPC) {
				int soldierPosition = secretHero.hero_id;
				if (solderPosSet.contains(soldierPosition)) {
					S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 601);
					playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
					return;
				}
				if (soldierPosition < 0 || soldierPosition > 7) {
					S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 601);
					playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
					return;
				}
				solderPosSet.add(soldierPosition);
			}
		}
		// 英雄数量最多3个
		if (heroCount > 3) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 614);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
		// 是否有血量为0
		DBSecretUsedHero dbSecretUsedHero = secret.getMyCost();
		for (IOSecretHero secretHero : onlineFormationList) {
			int heroType = secretHero.hero_type;
			if (heroType == SecretConstants.HERO) {
				int heroTplId = secretHero.hero_id;
				int hpVal = dbSecretUsedHero.getHeroUsedOrDefault(heroTplId, 1000);
				if(hpVal == 0) {
					S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 611);
					playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
					return;
				}
			}else if(heroType == SecretConstants.NPC) {
				int soldierPosIndex = secretHero.hero_id;
				int soldierCount = dbSecretUsedHero.getSoldierUsedCount();
				if(soldierPosIndex < soldierCount) {
					int soldierHp = dbSecretUsedHero.getSoldierUsed(soldierPosIndex);
					if(soldierHp ==0) {
						S2CErrorCode respMsg = new S2CErrorCode(S2CSecretBattleStart.msgCode, 611);
						playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
						return;
					}
				}
			}
		}
		// 设置临时值
		if (stageProgress == 0) {
			secret.setTmpMapId(mapId);
		}
		secret.setTmpOnlineFormationList(onlineFormationList);
		// 战斗中
		playingRole.getPlayerCacheStatus().setPosition(PlayerPosition.PLAYER_POSITION_SECRET);
		// 开始战斗
		// send msg
		S2CSecretBattleStart respMsg = new S2CSecretBattleStart();
		respMsg.is_reset = 1;
		respMsg.map_id = mapId;
		respMsg.online_formation = null;
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));

	}

}
