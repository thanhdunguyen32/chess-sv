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
import ws.WsMessageMail.C2SDelMail;
import ws.WsMessageMail.S2CDelMail;

import java.util.Iterator;
import java.util.List;

/**
 * 删邮件
 *
 * @author zhangning
 * @Date 2015年1月12日 下午2:16:25
 */
@MsgCodeAnn(msgcode = C2SDelMail.id, accessLimit = 200)
public class DelMailProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(DelMailProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 删邮件
     */
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        C2SDelMail readMsg = C2SDelMail.parse(request);
        int[] mailIds = readMsg.mail_id;
        int playerId = playingRole.getId();
        logger.info("del mail msg： player={},req={}", playerId, readMsg);

        if (mailIds == null || mailIds.length == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(S2CDelMail.msgCode, 1101);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        List<MailBean> mailCache = MailCache.getInstance().getMailCache(playerId);
        Iterator<MailBean> mailIte = mailCache.iterator();
        int mailId = 1;
        while (mailIte.hasNext()) {
            MailBean mailBean = mailIte.next();
            if (ArrayUtils.indexOf(mailIds, mailId) > -1) {
                if (mailBean == null) {
                    logger.error("Player ID: {} When deleting the email, the email does not exist! Unique ID of the email: {}", playerId, mailId);
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(S2CDelMail.msgCode, 1101);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    continue;// 邮件不存在
                }
                //附件未领取
                if (mailBean.getAttachs() != null && mailBean.getState() != MailConstants.MAIL_STATUS_ATTACH_GET) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(S2CDelMail.msgCode, 1106);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    continue;
                }
                mailIte.remove();
                // 更新内存、数据库
                MailManager.getInstance().asyncRemoveMail(mailBean.getId());
            }
            mailId++;
        }
        S2CDelMail builder = new S2CDelMail();
        playingRole.getGamePlayer().writeAndFlush(builder.build(playingRole.getGamePlayer().alloc()));

    }

}
