package game.module.affair.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.affair.bean.AffairBean;
import game.module.affair.bean.PlayerAffairs;
import game.module.affair.dao.AffairCache;
import game.module.affair.dao.AffairTemplateCache;
import game.module.affair.logic.AffairConstants;
import game.module.template.AffairsTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageAffair;

import java.util.Date;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageAffair.C2SAffairRedNotice.id, accessLimit = 200)
public class AffairRedNoticeProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(AffairRedNoticeProcessor.class);

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
        logger.info("affair get RedNotice!player={}", playerId);
        //ret
        WsMessageAffair.S2CAffairRedNotice respmsg = new WsMessageAffair.S2CAffairRedNotice();
        respmsg.ret = false;
        Date now = new Date();
        PlayerAffairs playerAffairs = AffairCache.getInstance().getAffairs(playerId);
        if (playerAffairs == null) {
            respmsg.ret = true;
        } else if (!DateUtils.isSameDay(playerAffairs.getLastVisitTime(), now)) {
            respmsg.ret = true;
        }
        //list
        if (playerAffairs != null && playerAffairs.getDbAffairs() != null) {
            for (AffairBean affairBean : playerAffairs.getDbAffairs().getAffairList()) {
                //
                AffairsTemplate affairsTemplate = AffairTemplateCache.getInstance().getAffairsTemplate(affairBean.getTemplateId());
                Integer affairStar = affairsTemplate.getSTAR();
                if (affairBean.getStatus() == AffairConstants.AFFAIR_STATUS_NEW) {
                    respmsg.ret = true;
                    break;
                } else if (affairBean.getStatus() == AffairConstants.AFFAIR_STATUS_PROGRESS) {
                    int costHour = AffairConstants.AFFAIR_COST_HOUR[affairStar - 1];
                    if (DateUtils.addHours(affairBean.getStartTime(), costHour).before(now)) {
                        respmsg.ret = true;
                        break;
                    }
                } else if (affairBean.getStatus() == AffairConstants.AFFAIR_STATUS_FINISH) {
                    respmsg.ret = true;
                    break;
                }
            }
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
