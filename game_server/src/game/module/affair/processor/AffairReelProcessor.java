package game.module.affair.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.affair.bean.AffairBean;
import game.module.affair.bean.DbAffairs;
import game.module.affair.bean.PlayerAffairs;
import game.module.affair.dao.AffairCache;
import game.module.affair.dao.AffairDaoHelper;
import game.module.affair.logic.AffairConstants;
import game.module.affair.logic.AffairManager;
import game.module.award.logic.AwardUtils;
import game.module.item.dao.ItemTemplateCache;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.template.AffairsTemplate;
import game.module.template.ItemTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageAffair;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageAffair.C2SAffairReel.id, accessLimit = 200)
public class AffairReelProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(AffairReelProcessor.class);

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
        WsMessageAffair.C2SAffairReel reqmsg = WsMessageAffair.C2SAffairReel.parse(request);
        int playerId = playingRole.getId();
        logger.info("affair reel!player={},req={}", playerId, reqmsg);
        int scroll_id = reqmsg.scroll_id;
        //is scroll
        if (!ArrayUtils.contains(AffairConstants.AFFAIR_SCROLL, scroll_id)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairReel.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //exist
        boolean isItemEnough = ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), scroll_id, 1);
        if (!isItemEnough) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairReel.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //
        PlayerAffairs playerAffairs = AffairCache.getInstance().getAffairs(playerId);
        if (playerAffairs == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairReel.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        ItemTemplate itemTemplate = ItemTemplateCache.getInstance().getItemTemplateById(scroll_id);
        Integer affairQuality = itemTemplate.getQUALITY();
        AffairsTemplate affairsTemplate = AffairManager.getInstance().randAffairByStar(affairQuality);
        //create
        AffairBean affairBean = AffairManager.getInstance().createAffairBean(affairsTemplate.getID());
        DbAffairs dbAffairs = playerAffairs.getDbAffairs();
        if (dbAffairs == null || dbAffairs.getAffairList() == null) {
            List<AffairBean> affairBeanMap = new ArrayList<>();
            dbAffairs = new DbAffairs();
            dbAffairs.setAffairList(affairBeanMap);
            playerAffairs.setDbAffairs(dbAffairs);
        }
        dbAffairs.getAffairList().add(affairBean);
        //save bean
        AffairDaoHelper.asyncUpdateAffair(playerAffairs);
        //cost
        AwardUtils.changeRes(playingRole, scroll_id, -1, LogConstants.MODULE_AFFAIR);
        //ret
        WsMessageAffair.S2CAffairReel respmsg = new WsMessageAffair.S2CAffairReel();
        respmsg.scroll_id = scroll_id;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
