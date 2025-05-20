package game.module.secret.processor;//package game.module.secret.processor;
//
//import game.common.PlayingRoleMsgProcessor;
//import game.entity.PlayingRole;
//import game.module.secret.ProtoMessageSecret.S2CSecretUsedHero;
//import game.module.secret.ProtoMessageSecret.SecretHero;
//import game.module.secret.bean.DBUsedHero;
//import game.module.secret.bean.Secret;
//import game.module.secret.bean.UsedHero;
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
// * 加载阵亡的英雄+士兵
// * 
// * @author zhangning
// * 
// * @Date 2015年1月27日 下午3:39:15
// */
//@MsgCodeAnn(msgcode = SecretProtoConstants.C2S_SECRET_USED_HERO, accessLimit = 500)
//public class ListUsedHeroProcessor extends PlayingRoleMsgProcessor {
//
//	private static Logger logger = LoggerFactory.getLogger(ListUsedHeroProcessor.class);
//
//	@Override
//	public void process(PlayingRole playingRole, RequestMessage request) throws Exception {
//
//	}
//
//	/**
//	 * 加载阵亡的的英雄+士兵
//	 */
//	@Override
//	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
//		logger.info("list Secret Die Hero And Npc");
//
//		// 没有秘密基地记录
//		SecretCache secretCache = SecretCache.getInstance();
//		Secret secret = secretCache.getSecret(playingRole.getId());
//		if (secret == null) {
//			return;
//		}
//
//		// send msg
//		S2CSecretUsedHero.Builder builder = S2CSecretUsedHero.newBuilder();
//		DBUsedHero usedHero = secret.getUsedHero();
//		List<UsedHero> usedtHeroList = usedHero.getUsedHeroList();
//		if (usedtHeroList != null && !usedtHeroList.isEmpty()) {
//			for (UsedHero usedHerodb : usedtHeroList) {
//				SecretHero.Builder hero = SecretHero.newBuilder();
//				hero.setHeroType(usedHerodb.getHeroType());
//				hero.setHeroId(usedHerodb.getHeroId());
//				hero.setHP(usedHerodb.getHP());
//				hero.setAnger(usedHerodb.getAnger());
//
//				builder.addUsedHeroNpc(hero);
//			}
//		}
//
//		playingRole.getGamePlayer().writeAndFlush(SecretProtoConstants.S2C_SECRET_USED_HERO, builder.build());
//		// logger.info("秘密基地已经用过的英雄/士兵: {}", builder.getUsedHeroNpcList());
//	}
//}
