package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.template.ZhdCxryTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SGetCxryGeneralList.id, accessLimit = 200)
public class GetCxryGeneralListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetCxryGeneralListProcessor.class);

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
        logger.info("get cxry general list,playerId={}", playerId);
        //
        WsMessageActivity.S2CGetCxryGeneralList respmsg = new WsMessageActivity.S2CGetCxryGeneralList();
        ZhdCxryTemplate cxryTemplate = ActivityWeekTemplateCache.getInstance().getCxryTemplate();
        respmsg.generallist = cxryTemplate.getWish_generals();
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
