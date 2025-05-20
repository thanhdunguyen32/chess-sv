package game.module.hero.processor;

import game.common.IoBeanConvertor;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.hero.bean.GeneralBean;
import game.module.hero.bean.GeneralExclusive;
import game.module.hero.dao.GeneralCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.hero.logic.GeneralManager;
import game.module.template.GeneralTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHero;

import java.util.ArrayList;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageHero.C2SHeroList.id, accessLimit = 200)
public class GeneralListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GeneralListProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        sendResponse(playingRole);
    }

    public static void sendResponse(PlayingRole playingRole){
        int playerId = playingRole.getId();
        logger.info("general list,player={}", playerId);
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        WsMessageHero.S2CHeroList respmsg = new WsMessageHero.S2CHeroList();
        respmsg.generals = new ArrayList<>(heroCache.size());
        for (GeneralBean heroBean : heroCache.values()) {
            WsMessageBase.IOGeneralBean generalBean = GeneralManager.getInstance().buildIoGeneral(heroBean);
            respmsg.generals.add(generalBean);
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
