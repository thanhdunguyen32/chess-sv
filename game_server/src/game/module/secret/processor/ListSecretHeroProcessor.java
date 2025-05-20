package game.module.secret.processor;//package game.module.secret.processor;
//
//import game.common.PlayingRoleMsgProcessor;
//import game.entity.PlayingRole;
//import game.module.secret.ProtoMessageSecret.S2CSecretHero;
//import game.module.secret.ProtoMessageSecret.SecretHero;
//import game.module.secret.bean.DbOnFormaHeros;
//import game.module.secret.bean.Secret;
//import game.module.secret.bean.SecretHeroDb;
//import game.module.secret.constants.SecretProtoConstants;
//import game.module.secret.dao.SecretCache;
//
//import java.util.List;
//
//import lion.common.MsgCodeAnn;
//import lion.netty4.message.RequestMessage;
//import lion.netty4.message.RequestProtoMessage;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * 加载秘密基地上阵英雄+士兵
// * 
// * @author zhangning
// * 
// * @Date 2015年1月27日 下午2:26:11
// */
//@MsgCodeAnn(msgcode = SecretProtoConstants.C2S_SECRET_HERO, accessLimit = 500)
//public class ListSecretHeroProcessor extends PlayingRoleMsgProcessor {
//
//	private static Logger logger = LoggerFactory.getLogger(ListSecretHeroProcessor.class);
//
//	@Override
//	public void process(PlayingRole playingRole, RequestMessage request) throws Exception {
//
//	}
//
//	/**
//	 * 加载秘密基地上阵英雄+士兵
//	 */
//	@Override
//	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
//		logger.info("list Secret Hero And Npc");
//
//		// 没有秘密基地记录
//		SecretCache secretCache = SecretCache.getInstance();
//		Secret secret = secretCache.getSecret(playingRole.getId());
//		if (secret == null) {
//			return;
//		}
//
//		// send msg
//		S2CSecretHero.Builder builder = S2CSecretHero.newBuilder();
//		DbOnFormaHeros secretHeroBds = secret.getSecretHeroList();
//		List<SecretHeroDb> secretHeroDbs = secretHeroBds.getOnFormaHeroList();
//		if (secretHeroDbs != null && !secretHeroDbs.isEmpty()) {
//			for (SecretHeroDb secretHeroDb : secretHeroDbs) {
//				SecretHero.Builder hero = SecretHero.newBuilder();
//				hero.setHeroType(secretHeroDb.getHeroType());
//				hero.setHeroId(secretHeroDb.getHeroId());
//				hero.setHP(secretHeroDb.getHp());
//				hero.setAnger(secretHeroDb.getAnger());
//
//				builder.addOnFormaHeroNpc(hero);
//			}
//		}
//
//		playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_HERO, builder.build());
//		// logger.info("秘密基地的上阵英雄有: {}", builder.getOnFormaHeroNpcList());
//	}
//
//}
