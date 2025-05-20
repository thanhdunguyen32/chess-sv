package game.module.dungeon.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.dao.DungeonCache;
import game.module.dungeon.dao.DungeonDaoHelper;
import game.module.dungeon.dao.DungeonTemplateCache;
import game.module.log.constants.LogConstants;
import game.module.template.MyDungeonTemplate;
import game.module.template.RewardTemplateSimple;
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
import java.util.List;

@MsgCodeAnn(msgcode = WsMessageDungeon.C2SDungeonChapterReward.id, accessLimit = 200)
public class DungeonChapterRewardProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(DungeonChapterRewardProcessor.class);

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
        WsMessageDungeon.C2SDungeonChapterReward reqmsg = WsMessageDungeon.C2SDungeonChapterReward.parse(request);
        logger.info("dungeon chapter reward,player={},req={}", playerId, reqmsg);
        //do
        int chapter = reqmsg.chapter;
        DungeonBean dungeonBean = DungeonCache.getInstance().getDungeon(playerId);
        if (dungeonBean == null || dungeonBean.getChapterAwardGet() == null || chapter >= dungeonBean.getChapterAwardGet().size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageDungeon.S2CDungeonChapterReward.msgCode, 1442);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Integer chapterAwardGet = dungeonBean.getChapterAwardGet().get(chapter);
        if (chapterAwardGet > 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageDungeon.S2CDungeonChapterReward.msgCode, 1448);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        dungeonBean.getChapterAwardGet().set(chapter, 1);
        //save bean
        DungeonDaoHelper.asyncUpdateDungeon(dungeonBean);
        //award
        List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
        MyDungeonTemplate.MyDungeonTemplateStep myDungeonTemplate = DungeonTemplateCache.getInstance().getDungeonTemplateStep(chapter);
        List<RewardTemplateSimple> reward = myDungeonTemplate.getReward();
        for (RewardTemplateSimple rewardTemplateSimple : reward) {
            Integer gsid = rewardTemplateSimple.getGSID();
            Integer count = rewardTemplateSimple.getCOUNT();
            AwardUtils.changeRes(playingRole, gsid, count, LogConstants.MODULE_DUNGEON);
            rewardItems.add(new WsMessageBase.IORewardItem(gsid, count));
        }
        //ret
        WsMessageDungeon.S2CDungeonChapterReward respmsg = new WsMessageDungeon.S2CDungeonChapterReward();
        respmsg.reward = rewardItems;
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
