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

@MsgCodeAnn(msgcode = WsMessageHall.C2SGuideStep.id, accessLimit = 200)
public class GuideStepProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GuideStepProcessor.class);

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
        logger.info("guide step,player={}", playerId);
        //do
        AwardUtils.changeRes(playingRole, GuideConstants.GUIDE_PROGRESS_MARK, 1, LogConstants.MODULE_GUILD);
        //ret
        WsMessageHall.S2CGuideStep respmsg = new WsMessageHall.S2CGuideStep();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
