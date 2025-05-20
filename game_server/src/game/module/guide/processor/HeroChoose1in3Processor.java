package game.module.guide.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.chapter.bean.ChapterBean;
import game.module.chapter.dao.ChapterCache;
import game.module.guide.logic.CampaignHelpTemplateCache;
import game.module.log.constants.LogConstants;
import game.module.template.CampaignHelpTemplate;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;

import java.util.Collections;

@MsgCodeAnn(msgcode = WsMessageHall.C2SHeroChoose1in3.id, accessLimit = 200)
public class HeroChoose1in3Processor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(HeroChoose1in3Processor.class);

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
        WsMessageHall.C2SHeroChoose1in3 reqmsg = WsMessageHall.C2SHeroChoose1in3.parse(request);
        logger.info("choose hero 3 in 1,player={},req={}", playerId, reqmsg);
        int getMark = 90309;
        int choose_index = reqmsg.index;
        //is get
        int getCount = PlayerManager.getInstance().getOtherCount(playerId, getMark);
        if (getCount > 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CHeroChoose1in3.msgCode, 1506);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //param check
        if (choose_index < 0 || choose_index >= CampaignHelpTemplateCache.getInstance().getSize()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CHeroChoose1in3.msgCode, 1507);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //map
        CampaignHelpTemplate.Three1Template friendExploreConfig = CampaignHelpTemplateCache.getInstance().getFriendExploreConfig(choose_index);
        ChapterBean chapterBean = ChapterCache.getInstance().getPlayerChapter(playerId);
        if (chapterBean == null || chapterBean.getMaxMapId() <= friendExploreConfig.getMINMAP()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CHeroChoose1in3.msgCode, 1508);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        AwardUtils.changeRes(playingRole, friendExploreConfig.getREWARD().getGSID(), friendExploreConfig.getREWARD().getCOUNT(), LogConstants.MODULE_NEW_GUIDE);
        //mark
        AwardUtils.changeRes(playingRole, getMark, 1, LogConstants.MODULE_NEW_GUIDE);
        //ret
        WsMessageHall.S2CHeroChoose1in3 respmsg = new WsMessageHall.S2CHeroChoose1in3();
        respmsg.rewards = Collections.singletonList(new WsMessageBase.SimpleItemInfo(friendExploreConfig.getREWARD().getGSID(),
                friendExploreConfig.getREWARD().getCOUNT()));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
