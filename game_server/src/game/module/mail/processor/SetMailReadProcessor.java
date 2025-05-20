package game.module.mail.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.mail.bean.MailBean;
import game.module.mail.constants.MailConstants;
import game.module.mail.dao.MailCache;
import game.module.mail.logic.MailManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageMail;

import java.util.Arrays;

/**
 * Read新邮件
 * 
 * @author zhangning
 * @Date 2014年12月30日 下午3:36:07
 *
 */
@MsgCodeAnn(msgcode = WsMessageMail.C2SSetMailRead.id, accessLimit = 200)
public class SetMailReadProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(SetMailReadProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

	}

	/**
	 * Read新邮件
	 */
	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		WsMessageMail.C2SSetMailRead readMsg = WsMessageMail.C2SSetMailRead.parse(request);
		int[] mailIds = readMsg.mail_id;
		logger.info("set mail read： req={}", readMsg);

		if (mailIds == null || mailIds.length == 0) {
			WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMail.S2CSetMailRead.msgCode, 1101);
			playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
			return;
		}

		MailCache mailCache = MailCache.getInstance();
		for (int mailId : mailIds) {
			MailBean mailBean = mailCache.getOneMail(playingRole.getId(), mailId);
			if (mailBean == null) {
				logger.error("Player ID: {} When reading a new email, the email does not exist! Email ID: {}", playingRole.getId(), mailId);
				WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMail.S2CSetMailRead.msgCode, 1101);
				playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
				return;// 邮件不存在
			}
		}
		//do
		for (int mailId : mailIds) {
			MailBean mailBean = mailCache.getOneMail(playingRole.getId(), mailId);
			mailBean.setState(MailConstants.MAIL_STATUS_IS_READ);
			// 更新内存、数据库
			MailManager.getInstance().asyncUpdateMailStatus(mailBean);
		}
		int playerId = playingRole.getId();
		MailCache.getInstance().sortMails(playerId);
		// send msg
		WsMessageMail.S2CSetMailRead builder = new WsMessageMail.S2CSetMailRead();
		playingRole.getGamePlayer().writeAndFlush(builder.build(playingRole.getGamePlayer().alloc()));
	}
}
