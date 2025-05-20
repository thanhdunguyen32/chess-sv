package game.module.guide.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.List;

@MsgCodeAnn(msgcode = WsMessageHall.C2SGuideChooseReward.id, accessLimit = 200)
public class GuideChooseRewardProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GuideChooseRewardProcessor.class);

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
        WsMessageHall.C2SGuideChooseReward reqmsg = WsMessageHall.C2SGuideChooseReward.parse(request);
        logger.info("guide choose reward,player={},req={}", playerId, reqmsg);
        List<WsMessageBase.SimpleItemInfo> rewardItems = new ArrayList<>();
        switch (reqmsg.choose_str) {
            case "newhand_choose_0":
                int markGsid = 90304;
                int markCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), markGsid);
                if (markCount > 0) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CGuideChooseReward.msgCode, 1505);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                int rewardGsid = 20241;//4星邓艾碎片
                //reward
                AwardUtils.changeRes(playingRole, rewardGsid, 30, LogConstants.MODULE_NEW_GUIDE);
                rewardItems.add(new WsMessageBase.SimpleItemInfo(rewardGsid, 1));
                //save mark
                AwardUtils.changeRes(playingRole, markGsid, 1, LogConstants.MODULE_NEW_GUIDE);
                break;
            case "newhand_choose_1":
                markGsid = 90305;
                markCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), markGsid);
                if (markCount > 0) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CGuideChooseReward.msgCode, 1505);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                rewardGsid = 24241;//4星陆抗碎片
                AwardUtils.changeRes(playingRole, rewardGsid, 30, LogConstants.MODULE_NEW_GUIDE);
                rewardItems.add(new WsMessageBase.SimpleItemInfo(rewardGsid, 1));
                //save mark
                AwardUtils.changeRes(playingRole, markGsid, 1, LogConstants.MODULE_NEW_GUIDE);
                break;
            case "newhand_choose_2":
                markGsid = 90306;
                markCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), markGsid);
                switch (markCount) {
                    case 0:
                        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, 50, LogConstants.MODULE_NEW_GUIDE);
                        rewardItems.add(new WsMessageBase.SimpleItemInfo(GameConfig.PLAYER.YB, 50));
                        break;
                    case 1:
                        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, 100, LogConstants.MODULE_NEW_GUIDE);
                        rewardItems.add(new WsMessageBase.SimpleItemInfo(GameConfig.PLAYER.YB, 100));
                        break;
                    case 2:
                        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, 200, LogConstants.MODULE_NEW_GUIDE);
                        rewardItems.add(new WsMessageBase.SimpleItemInfo(GameConfig.PLAYER.YB, 200));
                        break;
                    case 3:
                        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, 300, LogConstants.MODULE_NEW_GUIDE);
                        rewardItems.add(new WsMessageBase.SimpleItemInfo(GameConfig.PLAYER.YB, 300));
                        break;
                    case 4:
                        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, 400, LogConstants.MODULE_NEW_GUIDE);
                        rewardItems.add(new WsMessageBase.SimpleItemInfo(GameConfig.PLAYER.YB, 400));
                        break;
                }
                //save mark
                AwardUtils.changeRes(playingRole, markGsid, 1, LogConstants.MODULE_NEW_GUIDE);
                break;
        }
        //ret
        WsMessageHall.S2CGuideChooseReward respmsg = new WsMessageHall.S2CGuideChooseReward();
        respmsg.rewards = rewardItems;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
