package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActMjbg;
import game.module.activity.dao.ActMjbgCache;
import game.module.activity.dao.ActMjbgDaoHelper;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.template.ZhdMjbgTemplate;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageHall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SMjbgReward.id, accessLimit = 200)
public class MjbgRewardProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MjbgRewardProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageActivity.C2SMjbgReward reqmsg = WsMessageActivity.C2SMjbgReward.parse(request);
        logger.info("mjbg dig reward,player={},req={}", playerId, reqmsg);
        //
        int index = reqmsg.index;
        ActMjbg actMjbg = ActMjbgCache.getInstance().getActMjbg(playerId);
        if (actMjbg == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CMjbgReward.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        ZhdMjbgTemplate mjbgTemplate = ActivityWeekTemplateCache.getInstance().getMjbgTemplate();
        List<ZhdMjbgTemplate.RewardTemplateMjbg> items = mjbgTemplate.getItems();
        if (index >= items.size() + 1) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CMjbgReward.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), mjbgTemplate.getDjgsid(), mjbgTemplate.getDjcount())) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CMjbgReward.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        Map<Integer, Integer> boxOpen = actMjbg.getBoxOpen();
        if (boxOpen == null) {
            boxOpen = new HashMap<>();
            actMjbg.setBoxOpen(boxOpen);
        }
        boxOpen.put(index, 1);
        //
        Map<Integer, Integer> rewardOpen = actMjbg.getRewardOpen();
        RandomDispatcher<Integer> rd = new RandomDispatcher<>();
        for (int i = 0; i < mjbgTemplate.getItems().size(); i++) {
            if (rewardOpen != null && rewardOpen.containsKey(i)) {
                continue;
            }
            ZhdMjbgTemplate.RewardTemplateMjbg rewardTemplateMjbg = mjbgTemplate.getItems().get(i);
            rd.put(rewardTemplateMjbg.getRate(), i);
        }
        int bigRewardIndex = 34;
        if (rewardOpen == null || !rewardOpen.containsKey(bigRewardIndex)) {
            rd.put(1, bigRewardIndex);
        }
        Integer randIndex = rd.random();
        ZhdMjbgTemplate.RewardTemplateMjbg rewardTemplateMjbg;
        if (randIndex < bigRewardIndex) {
            rewardTemplateMjbg = mjbgTemplate.getItems().get(randIndex);
        } else {
            rewardTemplateMjbg = mjbgTemplate.getFinallist().get(actMjbg.getIndex()).get(actMjbg.getBigRewardIndex());
            actMjbg.setBigRewardGet(true);
        }
        AwardUtils.changeRes(playingRole, rewardTemplateMjbg.getGsid(), rewardTemplateMjbg.getCount(), LogConstants.MODULE_ACTIVITY);
        //save info
        if (rewardOpen == null) {
            rewardOpen = new HashMap<>();
            actMjbg.setRewardOpen(rewardOpen);
        }
        rewardOpen.put(randIndex, 1);
        ActMjbgDaoHelper.asyncUpdateActMjbg(actMjbg);
        //cost
        AwardUtils.changeRes(playingRole, mjbgTemplate.getDjgsid(), -mjbgTemplate.getDjcount(), LogConstants.MODULE_ACTIVITY);
        //ret
        WsMessageActivity.S2CMjbgReward respmsg = new WsMessageActivity.S2CMjbgReward();
        respmsg.gsid = rewardTemplateMjbg.getGsid();
        respmsg.count = rewardTemplateMjbg.getCount();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
