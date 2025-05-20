package game.module.hero.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.hero.bean.GeneralExchange;
import game.module.hero.dao.GeneralExchangeCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHero;

@MsgCodeAnn(msgcode = WsMessageHero.C2SGeneralExchangeGet.id, accessLimit = 200)
public class GeneralExchangeGetProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GeneralExchangeGetProcessor.class);

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
        logger.info("general exchange get,player={}", playerId);
        GeneralExchange generalExchange = GeneralExchangeCache.getInstance().getGeneralExchange(playerId);
        WsMessageHero.S2CGeneralExchangeGet respmsg = new WsMessageHero.S2CGeneralExchangeGet();
        if (generalExchange != null) {
            respmsg.guid = generalExchange.getOldGeneralUuid();
            respmsg.gsid = generalExchange.getNewGeneralTemplateId();
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
