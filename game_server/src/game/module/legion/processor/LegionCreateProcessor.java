package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.item.logic.ItemManager;
import game.module.legion.bean.LegionBean;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.LegionDaoHelper;
import game.module.legion.logic.LegionConstants;
import game.module.legion.logic.LegionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageLegion;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionCreate.id, accessLimit = 200)
public class LegionCreateProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionCreateProcessor.class);

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
        WsMessageLegion.C2SLegionCreate reqmsg = WsMessageLegion.C2SLegionCreate.parse(request);
        logger.info("legion create!player={},req={}", playerId, reqmsg);
        String name = reqmsg.name;
        //is already in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId > 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionCreate.msgCode, 1513);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //yb
        if (playingRole.getPlayerBean().getMoney() < LegionConstants.LEGION_CREATE_YB) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionCreate.msgCode, 1514);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //官职-都尉以上
        int myOffical = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), GameConfig.PLAYER.OFFICIAL);
        if (myOffical < 2) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionCreate.msgCode, 1515);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        LegionBean legionBean = LegionManager.getInstance().createLegion(playerId, playingRole.getPlayerBean().getPower(), reqmsg.name, reqmsg.notice,
                reqmsg.minlv, reqmsg.ispass);
        LegionCache.getInstance().addLegionBean(legionBean);
        LegionDaoHelper.asyncInsertLegionBean(legionBean);
        //ret
        WsMessageLegion.S2CLegionCreate respmsg = new WsMessageLegion.S2CLegionCreate();
        respmsg.legion_id = legionBean.getUuid();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
