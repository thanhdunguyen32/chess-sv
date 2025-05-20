package game.module.mapevent.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.mapevent.bean.MapEvent;
import game.module.mapevent.bean.PlayerMapEvent;
import game.module.mapevent.dao.MapEventCache;
import game.module.mapevent.logic.MapEventManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SMapEventList.id, accessLimit = 200)
public class MapEventListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MapEventListProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("map event list!player={}", playerId);
        //map event generate
        MapEventManager.getInstance().mapEventRefresh(playerId);
        //ret
        PlayerMapEvent playerMapEvent = MapEventCache.getInstance().getMapEvent(playerId);
        Map<Integer, WsMessageBase.IOMapEvent> retmap = new HashMap<>();
        if (playerMapEvent != null && playerMapEvent.getDbMapEvent() != null && playerMapEvent.getDbMapEvent().getEvents().size() > 0) {
            for (MapEvent mapEvent : playerMapEvent.getDbMapEvent().getEvents().values()) {
                WsMessageBase.IOMapEvent ioMapEvent = new WsMessageBase.IOMapEvent("", mapEvent.getType(), mapEvent.getEid());
                retmap.put(mapEvent.getCityId(), ioMapEvent);
            }
        }
        //ret
        WsMessageBattle.S2CMapEventList respmsg = new WsMessageBattle.S2CMapEventList();
        respmsg.list = retmap;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
