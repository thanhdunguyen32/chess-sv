package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActCxry;
import game.module.activity.dao.ActCxryCache;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.template.ZhdCxryTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SGetCxryInfo.id, accessLimit = 200)
public class GetCxryInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetCxryInfoProcessor.class);

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
        logger.info("get cxry info,playerId={}", playerId);
        //
        WsMessageActivity.S2CGetCxryInfo respmsg = new WsMessageActivity.S2CGetCxryInfo();
        ZhdCxryTemplate cxryTemplate = ActivityWeekTemplateCache.getInstance().getCxryTemplate();
        respmsg.zf = new WsMessageBase.IOCxryZf(0, cxryTemplate.getZfmax(), 0);
        ActCxry actCxry = ActCxryCache.getInstance().getActCxry(playerId);
        if (actCxry != null) {
            respmsg.zf.cur = actCxry.getNum();
            respmsg.zf.prob = actCxry.getNum() *80;
            respmsg.savegenerals = new ArrayList<>();
            for (Map.Entry<Integer, Integer> aEntry : actCxry.getWishGenerals().entrySet()) {
                WsMessageBase.IOCxryGenerals ioCxryGenerals = new WsMessageBase.IOCxryGenerals(aEntry.getKey(), aEntry.getValue());
                respmsg.savegenerals.add(ioCxryGenerals);
            }
        }
        respmsg.gnummax = cxryTemplate.getGnummax();
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
