package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActivityXiangou;
import game.module.activity.dao.ActivityXiangouCache;
import game.module.activity.dao.SpPackStarTemplateCache;
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

@MsgCodeAnn(msgcode = WsMessageActivity.C2SStarGiftList.id, accessLimit = 200)
public class StarGiftListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(StarGiftListProcessor.class);

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
        logger.info("star gift list,playerId={}", playerId);
        //
        WsMessageActivity.S2CStarGiftList respmsg = new WsMessageActivity.S2CStarGiftList();
        List<ActivityXiangou> activityXiangouAll = ActivityXiangouCache.getInstance().getActivityXiangouAll(playerId);
        respmsg.ret_list = new ArrayList<>();
        for (ActivityXiangou activityXiangou : activityXiangouAll) {
            Integer gstar = activityXiangou.getGstar();
            if (gstar != null && gstar > 0) {
                WsMessageBase.IOStarGift ioStarGift = new WsMessageBase.IOStarGift();
                ioStarGift.gstar = gstar;
                MyXiangouTemplate gstarXiangouTemplate = XiangouTemplateCache.getInstance().getGstarXiangouTemplate(gstar);
                ioStarGift.price = gstarXiangouTemplate.getPrice();
                ioStarGift.end = activityXiangou.getEndTime().getTime();
                Integer gstarXiangouLimit = SpPackStarTemplateCache.getInstance().getGstarXiangouLimit(gstar);
                if (activityXiangou.getLastBuyTime() != null && DateUtils.isSameDay(activityXiangou.getLastBuyTime(), new Date())) {
                    ioStarGift.buytime = gstarXiangouLimit - activityXiangou.getBuyCount();//可购买次数
                } else {
                    ioStarGift.buytime = gstarXiangouLimit;
                }
                ioStarGift.items = new ArrayList<>();
                for (RewardTemplateSimple rewardTemplateSimple : gstarXiangouTemplate.getItems()) {
                    ioStarGift.items.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
                respmsg.ret_list.add(ioStarGift);
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
