package game.module.dungeon.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.dungeon.logic.DungeonConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageDungeon;

@MsgCodeAnn(msgcode = WsMessageDungeon.C2SDungeonOpenTime.id, accessLimit = 200)
public class DungeonOpenTimeProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(DungeonOpenTimeProcessor.class);

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
        logger.info("dungeon open time,playerId={}", playerId);
        //ret
        WsMessageDungeon.S2CDungeonOpenTime respmsg = new WsMessageDungeon.S2CDungeonOpenTime();
        respmsg.open = DungeonConstants.OPEN_TIME;
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
