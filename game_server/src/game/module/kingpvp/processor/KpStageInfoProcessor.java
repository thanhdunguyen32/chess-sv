package game.module.kingpvp.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.kingpvp.bean.KingPvp;
import game.module.kingpvp.dao.KingPvpCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessagePvp;

import java.util.ArrayList;
import java.util.List;

@MsgCodeAnn(msgcode = WsMessagePvp.C2SKpStageInfo.id, accessLimit = 250)
public class KpStageInfoProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(KpStageInfoProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("king pvp stage info,playerId={}", playerId);
        //ret
        WsMessagePvp.S2CKpStageInfo respmsg = new WsMessagePvp.S2CKpStageInfo();
        KingPvp kingPvp = KingPvpCache.getInstance().getKingPvp(playerId);
        if (kingPvp != null) {
            respmsg.stage = kingPvp.getStage();
            respmsg.hstage = kingPvp.getHstage();
            respmsg.star = kingPvp.getStar();
            respmsg.locate = buildStrList(kingPvp.getLocate());
            respmsg.promotion = buildStrList(kingPvp.getPromotion());
        }
        //send
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private List<String> buildStrList(List<Integer> intList) {
        List<String> ret = null;
        if (intList != null) {
            ret = new ArrayList<>();
            for (int aint : intList) {
                if (aint > 0) {
                    ret.add("win");
                } else {
                    ret.add("lose");
                }
            }
        }
        return ret;
    }

}
