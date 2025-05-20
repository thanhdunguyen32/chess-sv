package game.module.user.processor;

import game.common.CommonUtils;
import game.module.badword.logic.BadWordFilter;
import game.module.lan.bean.LoginSessionBean;
import game.module.lan.dao.LoginSessionCache;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageHall.C2SCreateCharacter;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2SCreateCharacter.id, accessLimit = 200)
public class CreateCharacterProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(CreateCharacterProcessor.class);

	private PlayerManager playerManager = PlayerManager.getInstance();

	@Override
	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
		
	}

	@Override
	public void process(GamePlayer player, MyRequestMessage request) throws Exception {
		C2SCreateCharacter reqMsg = C2SCreateCharacter.parse(request);
		String name = reqMsg.name.trim();
		long loginSessionId = reqMsg.session_id;
		logger.info("创建角色，name={},loginSessionId={}", name, loginSessionId);
		// 判断session是否有效
		LoginSessionBean loginSessionBean = LoginSessionCache.getInstance().getLoginSessionBean(loginSessionId);
		if (loginSessionBean == null) {
			S2CErrorCode respMsg = new S2CErrorCode(WsMessageHall.S2CCreateCharacter.msgCode, 106);
			player.writeAndFlush(respMsg.build(player.alloc()));
			return;
		}
		//
		if (CommonUtils.getStrLength(name) > 14) {
			name = name.substring(0, 14);
		}
//		if (CommonUtils.getStrLength(name) > 14 || name.length() == 0) {
//			logger.info("name too long,size={}", name.length());
//			S2CErrorCode respMsg = new S2CErrorCode(WsMessageHall.S2CCreateCharacter.msgCode, 110);
//			player.writeAndFlush(respMsg.build(player.alloc()));
//			return;
//		}
		// 过滤 bad word filter
		String playerName = BadWordFilter.getInstance().filterBadWords(name);
		playerName = StringUtils.remove(playerName, '\n');
		// 创建角色
		playerManager.createCharacter(player, loginSessionBean, playerName);
		// update cache
		LoginSessionCache.getInstance().updateLoginSession(loginSessionBean);
	}

}
