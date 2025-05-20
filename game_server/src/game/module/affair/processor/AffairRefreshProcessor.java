package game.module.affair.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.affair.bean.AffairBean;
import game.module.affair.bean.DbAffairs;
import game.module.affair.bean.PlayerAffairs;
import game.module.affair.dao.AffairCache;
import game.module.affair.dao.AffairDaoHelper;
import game.module.affair.dao.AffairTemplateCache;
import game.module.affair.logic.AffairConstants;
import game.module.affair.logic.AffairManager;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.template.AffairsTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageAffair;
import ws.WsMessageHall;

import java.util.*;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageAffair.C2SAffairRefresh.id, accessLimit = 200)
public class AffairRefreshProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(AffairRefreshProcessor.class);

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
        logger.info("affair refresh!");
        int playerId = playingRole.getId();
        Date now = new Date();
        PlayerAffairs playerAffairs = AffairCache.getInstance().getAffairs(playerId);
        if (playerAffairs == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairRefresh.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //get affair new num
        int newAffairNum = 0;
        if (playerAffairs.getDbAffairs() != null && playerAffairs.getDbAffairs().getAffairList() != null) {
            List<AffairBean> affairMap = playerAffairs.getDbAffairs().getAffairList();
            Iterator<AffairBean> it = affairMap.iterator();
            while (it.hasNext()) {
                AffairBean affairBean = it.next();
                if (affairBean.getStatus() == AffairConstants.AFFAIR_STATUS_NEW && affairBean.getLock() == 0) {
                    newAffairNum++;
                }
            }
        }
        //money cost
        if (newAffairNum == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairRefresh.msgCode, 1421);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        if (playingRole.getPlayerBean().getMoney() < newAffairNum * AffairConstants.AFFAIR_REFRESH_PER_COST) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairRefresh.msgCode, 1422);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //cost
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -newAffairNum * AffairConstants.AFFAIR_REFRESH_PER_COST, LogConstants.MODULE_AFFAIR);
        //add affairs
        List<AffairBean> affairBeans = AffairManager.getInstance().generateAffairs(newAffairNum, 0);
        if (playerAffairs.getDbAffairs() == null || playerAffairs.getDbAffairs().getAffairList() == null) {
            List<AffairBean> affairBeanMap = new ArrayList<>();
            DbAffairs dbAffairs = new DbAffairs();
            dbAffairs.setAffairList(affairBeanMap);
            playerAffairs.setDbAffairs(dbAffairs);
        }
        //remove new affairs
        List<AffairBean> affairMap = playerAffairs.getDbAffairs().getAffairList();
        affairMap.removeIf(affairBean -> affairBean.getStatus() == AffairConstants.AFFAIR_STATUS_NEW && affairBean.getLock() == 0);
        //add refresh affairs
        affairMap.addAll(affairBeans);
        AffairDaoHelper.asyncUpdateAffair(playerAffairs);
        //ret
        WsMessageAffair.S2CAffairRefresh respmsg = new WsMessageAffair.S2CAffairRefresh();
        respmsg.item_list = AffairListProcessor.buildAffairList(playerId);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
