package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ZhdBean;
import game.module.activity.dao.ActivityWeekCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SZhdTime.id, accessLimit = 200)
public class ZhdTimeProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ZhdTimeProcessor.class);

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
        logger.info("zhd list,playerId={}", playerId);
        //
        WsMessageActivity.S2CZhdTime respmsg = new WsMessageActivity.S2CZhdTime();
        respmsg.ret = new HashMap<>();
        Map<String, ZhdBean> zhdAll = ActivityWeekCache.getInstance().getZhdAll();
        for (ZhdBean zhdBean : zhdAll.values()) {
            respmsg.ret.put(zhdBean.getZhdName(), Arrays.asList(zhdBean.getStartTime().getTime(), zhdBean.getEndTime().getTime()));
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
