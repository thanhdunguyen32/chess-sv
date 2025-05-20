package game.module.guide.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.guide.logic.GuideConstants;
import game.module.log.constants.LogConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

@MsgCodeAnn(msgcode = WsMessageHall.C2SGuideEnd.id, accessLimit = 200)
public class GuideEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GuideEndProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageHall.C2SGuideEnd reqmsg = WsMessageHall.C2SGuideEnd.parse(request);
        logger.info("guide end,player={},req={}", playerId, reqmsg);
        //do
        AwardUtils.changeRes(playingRole, GuideConstants.GUIDE_PROGRESS_MARK, reqmsg.add_step, LogConstants.MODULE_GUILD);
        //ret
        WsMessageHall.S2CGuideEnd respmsg = new WsMessageHall.S2CGuideEnd();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
