package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActivityXiangou;
import game.module.activity.dao.ActivityXiangouCache;
import game.module.activity.dao.XiangouTemplateCache;
import game.module.template.MyXiangouTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SLevelGiftList.id, accessLimit = 200)
public class LevelGiftListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LevelGiftListProcessor.class);

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
        logger.info("level gift list,playerId={}", playerId);
        //
        WsMessageActivity.S2CLevelGiftList respmsg = new WsMessageActivity.S2CLevelGiftList();
        List<ActivityXiangou> activityXiangouAll = ActivityXiangouCache.getInstance().getActivityXiangouAll(playerId);
        respmsg.ret_list = new ArrayList<>();
        for (ActivityXiangou activityXiangou : activityXiangouAll) {
            Integer level = activityXiangou.getLevel();
            if (level != null && level > 0) {
                WsMessageBase.IOLevelGift ioLevelGift = new WsMessageBase.IOLevelGift();
                ioLevelGift.level = level;
                MyXiangouTemplate levelXiangouTemplate = XiangouTemplateCache.getInstance().getLevelXiangouTemplate(level);
                ioLevelGift.price = levelXiangouTemplate.getPrice();
                ioLevelGift.end = activityXiangou.getEndTime().getTime();
                if (activityXiangou.getLastBuyTime() != null && DateUtils.isSameDay(activityXiangou.getLastBuyTime(), new Date())) {
                    ioLevelGift.buytime = 0;//可购买次数
                } else {
                    ioLevelGift.buytime = 1;
                }
                ioLevelGift.items = new ArrayList<>();
                for (RewardTemplateSimple rewardTemplateSimple : levelXiangouTemplate.getItems()) {
                    ioLevelGift.items.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
                respmsg.ret_list.add(ioLevelGift);
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
