package game.module.dungeon.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.bean.DungeonBuff;
import game.module.dungeon.bean.DungeonNode;
import game.module.dungeon.dao.DungeonCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageDungeon;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageDungeon.C2SDungeonChapterInfo.id, accessLimit = 200)
public class DungeonChapterInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(DungeonChapterInfoProcessor.class);

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
        logger.info("dungeon chapter info,player={}", playerId);
        //ret
        DungeonBean dungeonBean = DungeonCache.getInstance().getDungeon(playerId);
        if (dungeonBean == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageDungeon.S2CDungeonChapterInfo.msgCode, 1442);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        WsMessageDungeon.S2CDungeonChapterInfo respmsg = new WsMessageDungeon.S2CDungeonChapterInfo();
        //position
        respmsg.position = new WsMessageBase.IODungeonPosition(dungeonBean.getNode(), dungeonBean.getPos());
        //node list
        respmsg.nodelist = new ArrayList<>();
        if (dungeonBean.getDbDungeonNode() != null) {
            for (DungeonNode dungeonNode : dungeonBean.getDbDungeonNode().getNodelist()) {
                WsMessageBase.IODungeonNode ioDungeonNode = new WsMessageBase.IODungeonNode();
                respmsg.nodelist.add(ioDungeonNode);
                ioDungeonNode.poslist = new ArrayList<>();
                for (DungeonNode.DungeonNodePos dungeonNodePos : dungeonNode.getPoslist()) {
                    WsMessageBase.IODungeonNodePos ioDungeonNodePos = new WsMessageBase.IODungeonNodePos(dungeonNodePos.getType(), dungeonNodePos.getChoose(),
                            dungeonNodePos.getFinish());
                    ioDungeonNode.poslist.add(ioDungeonNodePos);
                }
            }
        }
        //potion
        if (dungeonBean.getPotions() != null) {
            respmsg.potion = new ArrayList<>();
            for (Map.Entry<Integer, Integer> aEntry : dungeonBean.getPotions().entrySet()) {
                respmsg.potion.add(new WsMessageBase.IODungeonPotion(aEntry.getKey(), aEntry.getValue()));
            }
        }
        //buff
        DungeonBuff dungeonBuff = dungeonBean.getDungeonBuff();
        if (dungeonBuff != null) {
            respmsg.bufflist = new WsMessageBase.IODungeonBuffList(dungeonBuff.getPpthr(), dungeonBuff.getPskidam(), dungeonBuff.getAtk(),
                    dungeonBuff.getPcrid(), dungeonBuff.getPmthr(), dungeonBuff.getPcri());
        } else {
            respmsg.bufflist = new WsMessageBase.IODungeonBuffList(0f, 0f, 0f, 0f, 0f, 0f);
        }
        DungeonBuff spBuff = dungeonBean.getSpBuff();
        if (spBuff != null) {
            respmsg.spbufflist = new WsMessageBase.IODungeonBuffList(spBuff.getPpthr(), spBuff.getPskidam(), spBuff.getAtk(),
                    spBuff.getPcrid(), spBuff.getPmthr(), spBuff.getPcri());
        } else {
            respmsg.spbufflist = new WsMessageBase.IODungeonBuffList(0f, 0f, 0f, 0f, 0f, 0f);
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
