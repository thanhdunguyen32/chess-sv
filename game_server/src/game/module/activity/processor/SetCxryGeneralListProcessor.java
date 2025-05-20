package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActCxry;
import game.module.activity.dao.ActCxryCache;
import game.module.activity.dao.ActCxryDaoHelper;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.template.ZhdCxryTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageHall;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SSetCxryGeneralList.id, accessLimit = 200)
public class SetCxryGeneralListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(SetCxryGeneralListProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageActivity.C2SSetCxryGeneralList reqmsg = WsMessageActivity.C2SSetCxryGeneralList.parse(request);
        logger.info("set cxry general list,player={},req={}", playerId, reqmsg);
        int[] gsids = reqmsg.gsids;
        //是否已经设置
        ActCxry actCxry = ActCxryCache.getInstance().getActCxry(playerId);
        if (actCxry != null && actCxry.getWishGenerals() != null && actCxry.getWishGenerals().size() > 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CSetCxryGeneralList.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //数量不对
        ZhdCxryTemplate cxryTemplate = ActivityWeekTemplateCache.getInstance().getCxryTemplate();
        if (gsids == null || gsids.length != cxryTemplate.getGnummax()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CSetCxryGeneralList.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //英雄不在列表
        for (int gsid : gsids) {
            if (!cxryTemplate.getWish_generals().contains(gsid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CSetCxryGeneralList.msgCode, 30);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        if (actCxry == null) {
            actCxry = new ActCxry();
            actCxry.setPlayerId(playerId);
            actCxry.setNum(0);
            Map<Integer,Integer> toSaveGeneralMap = new HashMap<>();
            for (int gsid : gsids) {
                toSaveGeneralMap.put(gsid,0);
            }
            actCxry.setWishGenerals(toSaveGeneralMap);
            ActCxryCache.getInstance().addActCxry(actCxry);
            ActCxryDaoHelper.asyncInsertActCxry(actCxry);
        }
        WsMessageActivity.S2CSetCxryGeneralList respmsg = new WsMessageActivity.S2CSetCxryGeneralList();
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
