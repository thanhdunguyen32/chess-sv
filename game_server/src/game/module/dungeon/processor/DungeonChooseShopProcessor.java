package game.module.dungeon.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.bean.DungeonNode;
import game.module.dungeon.dao.DungeonCache;
import game.module.dungeon.dao.DungeonDaoHelper;
import game.module.dungeon.logic.DungeonManager;
import game.module.log.constants.LogConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageDungeon;
import ws.WsMessageHall;

import java.util.List;

@MsgCodeAnn(msgcode = WsMessageDungeon.C2SDungeonChooseShop.id, accessLimit = 200)
public class DungeonChooseShopProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DungeonChooseShopProcessor.class);

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
        WsMessageDungeon.C2SDungeonChooseShop reqmsg = WsMessageDungeon.C2SDungeonChooseShop.parse(request);
        logger.info("dungeon choose shop,player={},req={}", playerId, reqmsg);
        DungeonBean dungeonBean = DungeonCache.getInstance().getDungeon(playerId);
        if (dungeonBean == null || dungeonBean.getDbDungeonNode() == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageDungeon.S2CDungeonChooseShop.msgCode, 1442);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        DungeonNode dungeonNode = dungeonBean.getDbDungeonNode().getNodelist().get(1);
        DungeonNode.DungeonNodePos dungeonNodePos = dungeonNode.getPoslist().get(reqmsg.pos);
        DungeonNode.DungeonNodeDetail dungeonNodeDetail = dungeonNodePos.getDungeonNodeDetail();
        int isBuy = reqmsg.buy;
        if (isBuy == 1) {
            for (WsMessageBase.IORewardItem aitem : dungeonNodeDetail.getItem()) {
                AwardUtils.changeRes(playingRole, aitem.GSID, aitem.COUNT, LogConstants.MODULE_DUNGEON);
            }
            for (WsMessageBase.IORewardItem aitem : dungeonNodeDetail.getConsume()) {
                AwardUtils.changeRes(playingRole, aitem.GSID, -aitem.COUNT, LogConstants.MODULE_DUNGEON);
            }
        }
        //step forward
        DungeonManager.getInstance().move1Step(dungeonBean);
        //save bean
        DungeonDaoHelper.asyncUpdateDungeon(dungeonBean);
        //ret
        WsMessageDungeon.S2CDungeonChooseShop respmsg = new WsMessageDungeon.S2CDungeonChooseShop();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
