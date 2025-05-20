package game.module.draw.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.draw.bean.PubDraw;
import game.module.draw.dao.PubDrawCache;
import game.module.draw.logic.PubDrawConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

import java.util.Date;

@MsgCodeAnn(msgcode = WsMessageHall.C2SDrawList.id, accessLimit = 200)
public class DrawListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(DrawListProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        //
        WsMessageHall.S2CDrawList respmsg = new WsMessageHall.S2CDrawList();
        PubDraw pubDraw = PubDrawCache.getInstance().getPubDraw(playerId);
//        if (pubDraw == null) {
//            respmsg.recruit_free_normal = 0;
//            respmsg.recruit_free_advance = 0;
//            respmsg.lucky_points = 0;
//        } else {
//            Date lastNormalTime = pubDraw.getLastNormalTime();
//            if (lastNormalTime != null) {
//                respmsg.recruit_free_normal = (int) (DateUtils.addHours(lastNormalTime, PubDrawConstants.DRAW_NORMAL_HOURS).getTime() / 1000);
//            } else {
//                respmsg.recruit_free_normal = 0;
//            }
//            Date lastAdvanceTime = pubDraw.getLastAdvanceTime();
//            if (lastAdvanceTime != null) {
//                respmsg.recruit_free_advance = (int) (DateUtils.addHours(lastAdvanceTime, PubDrawConstants.DRAW_ADVANCE_HOURS).getTime() / 1000);
//            } else {
//                respmsg.recruit_free_advance = 0;
//            }
//        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
