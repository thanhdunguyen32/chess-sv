package game.module.dungeon.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.bean.DungeonNode;
import game.module.dungeon.dao.DungeonCache;
import game.module.dungeon.dao.DungeonDaoHelper;
import game.module.dungeon.logic.DungeonManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageDungeon;
import ws.WsMessageHall;

@MsgCodeAnn(msgcode = WsMessageDungeon.C2SDungeonChooseBuf.id, accessLimit = 200)
public class DungeonChooseBuffProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DungeonChooseBuffProcessor.class);

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
        WsMessageDungeon.C2SDungeonChooseBuf reqmsg = WsMessageDungeon.C2SDungeonChooseBuf.parse(request);
        logger.info("dungeon choose buff,player={},req={}", playerId, reqmsg);
        DungeonBean dungeonBean = DungeonCache.getInstance().getDungeon(playerId);
        if (dungeonBean == null || dungeonBean.getDbDungeonNode() == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageDungeon.S2CDungeonChooseBuf.msgCode, 1442);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        DungeonNode dungeonNode = dungeonBean.getDbDungeonNode().getNodelist().get(1);
        DungeonNode.DungeonNodePos dungeonNodePos = dungeonNode.getPoslist().get(reqmsg.pos);
        DungeonNode.DungeonNodeDetail dungeonNodeDetail = dungeonNodePos.getDungeonNodeDetail();
        int buffIndex = reqmsg.buff;
        int buffid = 0;
        if (dungeonNodeDetail != null && dungeonNodeDetail.getBuffs() != null && buffIndex < dungeonNodeDetail.getBuffs().size()) {
            //add buff
            buffid = dungeonNodeDetail.getBuffs().get(buffIndex);
            if (dungeonBean.getNode() < 14) {
                DungeonManager.getInstance().addBuff(dungeonBean, buffid);
            } else {
                DungeonManager.getInstance().addSpBuff(dungeonBean, buffid);
            }
        }
        //
        DungeonManager.getInstance().move1Step(dungeonBean);
        //save bean
        DungeonDaoHelper.asyncUpdateDungeon(dungeonBean);
        //ret
        WsMessageDungeon.S2CDungeonChooseBuf respmsg = new WsMessageDungeon.S2CDungeonChooseBuf(buffid);
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
