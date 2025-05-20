package game.module.mail.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.mail.bean.MailBean;
import game.module.mail.dao.MailCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageMail;

import java.util.List;

/**
 * 加载邮件
 *
 * @author zhangning
 * @Date 2014年12月30日 上午11:32:42
 */
@MsgCodeAnn(msgcode = WsMessageMail.C2SMailRedNotice.id, accessLimit = 200)
public class MailRedNoticeProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MailRedNoticeProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 加载所有邮件
     */
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        logger.info("mail red notice!");
        //ret
        WsMessageMail.S2CMailRedNotice respmsg = new WsMessageMail.S2CMailRedNotice();
        respmsg.ret = false;
        List<MailBean> mailAll = MailCache.getInstance().getMailCache(playingRole.getId());
        if (mailAll.size() > 0) {
            respmsg.ret = mailAll.get(mailAll.size() - 1).getState() == 0;
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
