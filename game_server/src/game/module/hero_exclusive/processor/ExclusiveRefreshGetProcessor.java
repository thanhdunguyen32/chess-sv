package game.module.hero_exclusive.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.hero.bean.GeneralBean;
import game.module.hero.bean.GeneralExclusive;
import game.module.hero.dao.GeneralCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.ArrayList;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageHero.C2SExclusiveRefreshGet.id, accessLimit = 200)
public class ExclusiveRefreshGetProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ExclusiveRefreshGetProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SExclusiveRefreshGet reqmsg = WsMessageHero.C2SExclusiveRefreshGet.parse(request);
        int playerId = playingRole.getId();
        logger.info("general exclusive refresh get,player={},req={}", playerId, reqmsg);
        long general_uuid = reqmsg.general_uuid;
        //is exist
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        if (!heroCache.containsKey(general_uuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CExclusiveRefreshGet.msgCode, 1301);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        GeneralBean generalBean = heroCache.get(general_uuid);
        GeneralExclusive generalExclusive = generalBean.getExclusive();
        WsMessageHero.S2CExclusiveRefreshGet respmsg = new WsMessageHero.S2CExclusiveRefreshGet();
        respmsg.general_uuid = general_uuid;
        if (generalExclusive.getPropertyPending() != null) {
            respmsg.has_pending_property = true;
            respmsg.property = new ArrayList<>(generalExclusive.getPropertyPending().size());
            for (Map.Entry<String, Integer> aEntry : generalExclusive.getPropertyPending().entrySet()) {
                respmsg.property.add(new WsMessageBase.KvStringPair(aEntry.getKey(), aEntry.getValue()));
            }
            respmsg.skill = generalExclusive.getSkillPending();
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
