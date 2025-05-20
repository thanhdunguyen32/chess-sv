package game.module.secret.processor;

import game.common.DateCommonUtils;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.secret.bean.Secret;
import game.module.secret.dao.SecretCache;
import game.module.secret.logic.SecretManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageSecret.C2SSecretReset;
import ws.WsMessageSecret.S2CSecretReset;

import java.util.Date;

/**
 * 复活士兵
 * 
 * @author zhangning
 * 
 * @Date 2015年1月27日 下午6:42:14
 */
@MsgCodeAnn(msgcode = C2SSecretReset.id, accessLimit = 500)
public class SecretResetProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(SecretResetProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

	}

	/**
	 * 复活士兵
	 */
	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		int playerId = playingRole.getId();
		logger.info("Secret reset：playerId={}", playerId);

		// 没有秘密基地记录
		SecretCache secretCache = SecretCache.getInstance();
		Secret secret = secretCache.getSecret(playingRole.getId());
		if (secret == null) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretReset.msgCode, 601);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}

		// 不需要重置
		int currentProgress = secret.getProgress();
		if (currentProgress <= 0) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretReset.msgCode,621);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}

		// 重置次数上限
		Date lastResetTime = secret.getResetTime();
		if (lastResetTime != null && DateCommonUtils.isSameDay(lastResetTime, 5)) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretReset.msgCode, 622);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}

		// do
		SecretManager.getInstance().reset(playingRole);
		//ret
		S2CSecretReset respMsg = new S2CSecretReset();
		playingRole.getGamePlayer().writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
