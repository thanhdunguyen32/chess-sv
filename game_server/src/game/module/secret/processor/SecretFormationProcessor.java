package game.module.secret.processor;//package game.module.secret.processor;
//
//import game.common.PlayingRoleMsgProcessor;
//import game.entity.PlayingRole;
//import game.module.hero.dao.HeroCache;
//import game.module.other.OtherConfigCache;
//import game.module.secret.ProtoMessageSecret.C2SSecretFormation;
//import game.module.secret.ProtoMessageSecret.S2CSecretFormation;
//import game.module.secret.bean.DBUsedHero;
//import game.module.secret.bean.DbOnFormaHeros;
//import game.module.secret.bean.Secret;
//import game.module.secret.bean.SecretHeroDb;
//import game.module.secret.bean.UsedHero;
//import game.module.secret.constants.SecretConstants;
//import game.module.secret.constants.SecretProtoConstants;
//import game.module.secret.dao.SecretCache;
//import game.module.secret.logic.SecretManager;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
//import lion.common.MsgCodeAnn;
//import lion.netty4.codec.ProtoUtil;
//import lion.netty4.message.RequestMessage;
//import lion.netty4.message.RequestProtoMessage;
//import lion.netty4.proto.RpcBaseProto.RetCode;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * 上阵、下阵
// * 
// * @author zhangning
// * 
// * @Date 2015年1月27日 下午7:00:46
// */
//@MsgCodeAnn(msgcode = SecretProtoConstants.C2S_SECRET_FORMATION, accessLimit = 500)
//public class SecretFormationProcessor extends PlayingRoleMsgProcessor {
//
//	private static Logger logger = LoggerFactory.getLogger(SecretFormationProcessor.class);
//
//	@Override
//	public void process(PlayingRole playingRole, RequestMessage request) throws Exception {
//
//	}
//
//	/**
//	 * 上阵、下阵
//	 */
//	@Override
//	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
//		C2SSecretFormation requestParam = ProtoUtil.getProtoObj(C2SSecretFormation.PARSER, request);
//		int chooseType = requestParam.getChooseType();
//		int heroType = requestParam.getHeroType();
//		int heroId = requestParam.getHeroId();
//		int hp = requestParam.getHP();
//		int anger = requestParam.getAnger();
//		logger.info("Secret Formation：chooseType={}, heroType={}, heroId={}, hp={}, anger={}", chooseType, heroType,
//				heroId, hp, anger);
//
//		// 参数有误
//		if (heroType <= 0 || heroId <= 0) {
//			playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_FORMATION,
//					RetCode.SECRET_PARAM_ERROR);
//			return;
//		}
//
//		// 没有秘密基地记录
//		SecretCache secretCache = SecretCache.getInstance();
//		Secret secret = secretCache.getSecret(playingRole.getId());
//		if (secret == null) {
//			return;
//		}
//
//		// 英雄不存在
//		if (heroType == SecretConstants.HERO) {
//			if (!HeroCache.getInstance().isExistHero(playingRole.getId(), heroId)) {
//				playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_FORMATION,
//						RetCode.SECRET_HERO_NOT_EXIST);
//				return;
//			}
//		}
//
//		// 英雄或士兵已经阵亡
//		DBUsedHero dbUsedHero = secret.getUsedHero();
//		List<UsedHero> usedHeros = dbUsedHero.getUsedHeroList();
//		UsedHero usedHero = getUsedHero(usedHeros, heroType, heroId);
//		if (usedHero != null && usedHero.getHP() == 0) {
//			playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_FORMATION,
//					RetCode.SECRET_HERO_DIED);
//			return;
//		}
//
//		DbOnFormaHeros secretHeroDb = secret.getSecretHeroList();
//		LinkedList<SecretHeroDb> secretHeroDbs = secretHeroDb.getOnFormaHeroList();
//
//		if (chooseType == SecretConstants.UP_FORMATION) {
//			int heroSize = getHeroCnt(secretHeroDbs);
//			// 英雄最多2个
//			int maxHeroCnt = OtherConfigCache.getInstance().getIntConfig(SecretConstants.MAX_HERO_CNT_NUM);
//			if (heroType == SecretConstants.HERO && heroSize == maxHeroCnt) {
//				playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_FORMATION,
//						RetCode.SECRET_OVER_HERO_MAX_CNT);
//				return;
//			}
//
//			// 不能超过最大上阵个数
//			if (secretHeroDbs.size() >= SecretConstants.FORMATION_MAX_CNT) {
//				playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_FORMATION,
//						RetCode.SECRET_CAN_NOT_OVER_FORMAT_CNT);
//				return;
//			}
//
//			// 配置中士兵不存在
//			if (heroType == SecretConstants.NPC
//					&& (heroId < SecretConstants.NPC_START_SITE || heroId > SecretConstants.NPC_END_SITE)) {
//				playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_FORMATION,
//						RetCode.SECRET_NPC_NOT_EXIST);
//				return;
//			}
//
//			// 已在阵型中(上阵)
//			SecretHeroDb heroDb = getSecretHero(secretHeroDbs, heroType, heroId);
//			if (heroDb != null) {
//				playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_FORMATION,
//						RetCode.SECRET_HERO_IN_FORMATION);
//				return;
//			}
//
//			SecretHeroDb newHeroDb = new SecretHeroDb();
//			newHeroDb.setHeroType(heroType);
//			newHeroDb.setHeroId(heroId);
//			newHeroDb.setHp(hp);
//			newHeroDb.setAnger(anger);
//			if (usedHero != null) {
//				newHeroDb.setHp(usedHero.getHP());
//				newHeroDb.setAnger(usedHero.getAnger());
//
//				usedHeros.remove(usedHero);
//				secret.setUsedHero(dbUsedHero);
//			}
//
//			secretHeroDbs.addLast(newHeroDb);
//			secret.setSecretHeroList(secretHeroDb);
//			SecretManager.getInstance().asyncUpdateSecretBlob(secret);
//
//		} else if (chooseType == SecretConstants.DOWN_FORMATION) {
//			// 阵型中士兵不存在
//			if (heroType == SecretConstants.NPC && getSecretHero(secretHeroDbs, heroType, heroId) == null) {
//				playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_FORMATION,
//						RetCode.SECRET_NPC_NOT_EXIST);
//				return;
//			}
//
//			// 不在阵型中(下阵)
//			SecretHeroDb heroDb = getSecretHero(secretHeroDbs, heroType, heroId);
//			if (heroDb == null) {
//				playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_FORMATION,
//						RetCode.SECRET_HERO_NOT_IN_FORMATION);
//				return;
//			}
//			secretHeroDbs.remove(heroDb);
//			secret.setSecretHeroList(secretHeroDb);
//
//			if (usedHero == null) {
//				UsedHero newUsedHero = new UsedHero();
//				newUsedHero.setHeroType(heroType);
//				newUsedHero.setHeroId(heroId);
//				newUsedHero.setHP(hp);
//				newUsedHero.setAnger(anger);
//				if (usedHeros == null) {
//					usedHeros = new ArrayList<UsedHero>(1);
//					dbUsedHero.setUsedHeroList(usedHeros);
//				}
//				usedHeros.add(newUsedHero);
//				secret.setUsedHero(dbUsedHero);
//			}
//
//			SecretManager.getInstance().asyncUpdateSecretBlob(secret);
//		}
//
//		// send msg
//		S2CSecretFormation.Builder builder = S2CSecretFormation.newBuilder();
//		builder.setChooseType(chooseType);
//		builder.setHeroType(heroType);
//		builder.setHeroId(heroId);
//		builder.setHP(hp);
//		builder.setAnger(anger);
//		playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_FORMATION, builder.build());
//	}
//
//	/**
//	 * 阵型中英雄数量
//	 * 
//	 * @param secretHeroDbs
//	 * @return
//	 */
//	private int getHeroCnt(LinkedList<SecretHeroDb> secretHeroDbs) {
//		int heroSize = 0;
//		if (secretHeroDbs != null) {
//			for (SecretHeroDb heroDb : secretHeroDbs) {
//				if (heroDb.getHeroType() == SecretConstants.HERO) {
//					heroSize++;
//				}
//			}
//		}
//		return heroSize;
//	}
//
//	/**
//	 * 阵型中的士兵
//	 * 
//	 * @param secretHeroDbs
//	 * @param heroType
//	 * @param heroId
//	 * @param npcNum
//	 * @return
//	 */
//	private SecretHeroDb getSecretHero(List<SecretHeroDb> secretHeroDbs, int heroType, int heroId) {
//		if (secretHeroDbs != null && !secretHeroDbs.isEmpty()) {
//			for (SecretHeroDb heroDb : secretHeroDbs) {
//				if (heroDb.getHeroType() == heroType && heroDb.getHeroId() == heroId) {
//					return heroDb;
//				}
//			}
//		}
//
//		return null;
//	}
//
//	/**
//	 * 死亡/下阵士兵
//	 * 
//	 * @param usedHeros
//	 * @param heroId
//	 * @param npcNum
//	 * @return
//	 */
//	private UsedHero getUsedHero(List<UsedHero> usedHeros, int heroType, int heroId) {
//		if (usedHeros != null) {
//			for (UsedHero usedHero : usedHeros) {
//				if (usedHero.getHeroType().equals(heroType) && usedHero.getHeroId().equals(heroId)) {
//					return usedHero;
//				}
//			}
//		}
//
//		return null;
//	}
//}
