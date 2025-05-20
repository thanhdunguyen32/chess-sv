package game.module.online.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.online.dao.OnlineGiftTemplateCache;
import game.module.online.logic.OnlineGiftManager;
import game.module.template.OnlineGiftTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageAward;
import ws.WsMessageBase;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageAward.C2SGetOnlineAward.id, accessLimit = 100)
public class OnlineGiftGetProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OnlineGiftGetProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("get online gift,player={}", playerId);
        Date enterGameTime = playingRole.getPlayerCacheStatus().getEnterGameTime();
        int totalOnlineSeconds = (int) ((System.currentTimeMillis() - enterGameTime.getTime()) / 1000);
        //能否领取
        int giveSign = OnlineGiftTemplateCache.getInstance().getGiveSign();
        int signCount = 0;
        Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
        if (playerOthers.containsKey(giveSign)) {
            signCount = playerOthers.get(giveSign).getCount();
        }
        //领取满
        if (signCount >= OnlineGiftTemplateCache.getInstance().getRewardSize()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAward.S2CGetOnlineAward.msgCode, 1201);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //时间还没到
        OnlineGiftTemplate.OnlineGiftTemplateReward onlineRewardTemplate = OnlineGiftTemplateCache.getInstance().getOnlineReward(signCount);
        int needSeconds = onlineRewardTemplate.getTIME() * 60;
        if (totalOnlineSeconds < needSeconds) {
            logger.warn("online gift time notenough,now={},need={},template={}",totalOnlineSeconds,needSeconds, signCount);
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAward.S2CGetOnlineAward.msgCode, 1202);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //save bean
        AwardUtils.changeRes(playingRole, giveSign, 1, LogConstants.MODULE_ONLINE_AWARD);
        //send award
        List<WsMessageBase.AwardItem> rewards = new ArrayList<>();
        List<RewardTemplateSimple> onlineRewardTemplateREWARD = onlineRewardTemplate.getREWARD();
        for (RewardTemplateSimple rewardTemplateSimple : onlineRewardTemplateREWARD) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_ONLINE_AWARD);
            rewards.add(new WsMessageBase.AwardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //push prop change
        long newonlinetime = OnlineGiftManager.getInstance().getNextOnlineTime(playingRole);
        WsMessageHall.PushPropChange pushPropChange = new WsMessageHall.PushPropChange(100003, newonlinetime);
        playingRole.write(pushPropChange.build(playingRole.alloc()));
        //ret
        WsMessageAward.S2CGetOnlineAward respmsg = new WsMessageAward.S2CGetOnlineAward();
        respmsg.reward = rewards;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
