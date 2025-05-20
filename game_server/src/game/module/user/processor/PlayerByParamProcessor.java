package game.module.user.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.dungeon.logic.DungeonManager;
import game.module.item.logic.BagManager;
import game.module.legion.logic.LegionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

@MsgCodeAnn(msgcode = WsMessageHall.C2SPlayerByParam.id, accessLimit = 200)
public class PlayerByParamProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(PlayerByParamProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageHall.C2SPlayerByParam reqmsg = WsMessageHall.C2SPlayerByParam.parse(request);
        logger.info("get player by param!playerId={},req={}", playerId, reqmsg);
        String[] arr = reqmsg.arr;
        
        WsMessageHall.S2CPlayerByParam respMsg = new WsMessageHall.S2CPlayerByParam();
        //ret
        if (ArrayUtils.contains(arr, "legion")) {
            respMsg.legion = LegionManager.getInstance().getLegionId(playerId);
        }
        if (ArrayUtils.contains(arr, "gnum")) {
            respMsg.gnum = BagManager.getInstance().getGNum(playerId);
        }
        if (ArrayUtils.contains(arr, "tech")) {
            respMsg.tech = LegionManager.getInstance().getLegionTech(playerId);
        }
        if (ArrayUtils.contains(arr, "dgtop")) {
            respMsg.dgtop = DungeonManager.getInstance().getDungeonInfo(playerId);
        }
        if (ArrayUtils.contains(arr, "bagspace")) {
            respMsg.bagspace = BagManager.getInstance().getGeneralBagSize(playerId,playingRole.getPlayerBean().getVipLevel());
        }
        if (ArrayUtils.contains(arr, "frameid")) {
            respMsg.frameid = playingRole.getPlayerBean().getFrameid();
        }
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

}
