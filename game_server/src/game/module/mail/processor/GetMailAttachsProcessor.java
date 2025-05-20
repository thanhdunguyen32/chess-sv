package game.module.mail.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
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
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageMail;
import ws.WsMessageMail.C2SGetMailAttach;
import ws.WsMessageMail.S2CGetMailAttach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 领取附件
 *
 * @author zhangning
 * @Date 2014年12月30日 下午3:40:24
 */
@MsgCodeAnn(msgcode = C2SGetMailAttach.id, accessLimit = 200)
public class GetMailAttachsProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetMailAttachsProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 领取邮件奖励
     */
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    /**
     * 添加奖励
     *
     * @param playingRole
     */
    public void addReward(final PlayingRole playingRole, Map<Integer, Integer> mailAtts) {
        if (mailAtts == null || mailAtts.isEmpty()) {
            return;
        }
        for (Map.Entry<Integer, Integer> att : mailAtts.entrySet()) {
            AwardUtils.changeRes(playingRole, att.getKey(), att.getValue(), LogConstants.MODULE_MAIL);
        }
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        C2SGetMailAttach reqMsg = C2SGetMailAttach.parse(request);
        int[] mailIds = reqMsg.mail_id;
        int playerId = playingRole.getId();
        logger.info("mail get attach： player={},req={}", playerId, reqMsg);

        MailCache mailCache = MailCache.getInstance();
        for (int mailId : mailIds) {
            MailBean mailBean = mailCache.getOneMail(playingRole.getId(), mailId);
            if (mailBean == null) {
                logger.error("Player ID: {} When receiving the email attachment, the email does not exist! Template ID: {}", playerId, mailId);
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(S2CGetMailAttach.msgCode,1101);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            //已经领取
            if (mailBean.getState() == MailConstants.MAIL_STATUS_ATTACH_GET) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(S2CGetMailAttach.msgCode,1001);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            }
        }
        //do
        Map<Integer, Integer> retRewardMap = new HashMap<>();
        for (int mailId : mailIds) {
            MailBean mailBean = mailCache.getOneMail(playerId, mailId);
            Map<Integer, Integer> mailAtts = mailBean.getAttachs();
            if (mailAtts != null && !mailAtts.isEmpty()) {
                addReward(playingRole, mailAtts);
                for (Map.Entry<Integer, Integer> att : mailAtts.entrySet()) {
                    if (retRewardMap.containsKey(att.getKey())) {
                        retRewardMap.put(att.getKey(), retRewardMap.get(att.getKey()) + att.getValue());
                    } else {
                        retRewardMap.put(att.getKey(), att.getValue());
                    }
                }
                //update db
                mailBean.setState(MailConstants.MAIL_STATUS_ATTACH_GET);
                MailManager.getInstance().asyncUpdateMailStatus(mailBean);
            }
        }
        MailCache.getInstance().sortMails(playerId);
        // send msg
        S2CGetMailAttach respmsg = new S2CGetMailAttach();
        respmsg.rewards = new ArrayList<>(retRewardMap.size());
        for (Map.Entry<Integer, Integer> aEntry : retRewardMap.entrySet()) {
            respmsg.rewards.add(new WsMessageBase.IOMailAttach(aEntry.getKey(), aEntry.getValue()));
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
