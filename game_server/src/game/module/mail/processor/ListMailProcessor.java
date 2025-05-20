package game.module.mail.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.mail.bean.MailBean;
import game.module.mail.constants.MailConstants;
import game.module.mail.dao.MailCache;
import game.module.mail.dao.MailTemplateCache;
import game.module.mail.logic.MailManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageMail;

import java.util.*;

/**
 * 加载邮件
 *
 * @author zhangning
 * @Date 2014年12月30日 上午11:32:42
 */
@MsgCodeAnn(msgcode = WsMessageMail.C2SMailList.id, accessLimit = 200)
public class ListMailProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ListMailProcessor.class);

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
        int playerId = playingRole.getId();
        logger.info("list mails!player={}", playerId);
        sendMailList(playingRole);
    }

    public static void sendMailList(PlayingRole playingRole) {
        MailCache mailCache = MailCache.getInstance();
        // 检查玩家邮件是否过期
        List<MailBean> mailAll = mailCache.getMailCache(playingRole.getId());
        Iterator<MailBean> mailIte = mailAll.iterator();
        Date now = new Date();
        while (mailIte.hasNext()){
            MailBean mailBean = mailIte.next();
            if(mailBean == null){
                mailIte.remove();
            }else if (mailBean.getEndTime().before(now)) {// 邮件过期,删除
                mailIte.remove();
                MailManager.getInstance().asyncRemoveMail(mailBean.getId());
            }
        }
        // 所有邮件
        WsMessageMail.S2CMailList retMsg = new WsMessageMail.S2CMailList();
        retMsg.list = new ArrayList<>();
        // ret
        for (int i = mailAll.size()-1; i >= 0; i--) {
            retMsg.list.add(MailManager.getInstance().build1MailMsg(mailAll.get(i),i+1));
        }
        playingRole.getGamePlayer().writeAndFlush(retMsg.build(playingRole.getGamePlayer().alloc()));
    }

}
