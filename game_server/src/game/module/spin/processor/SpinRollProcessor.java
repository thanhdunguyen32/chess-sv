package game.module.spin.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.logic.ActivityWeekManager;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.occtask.logic.OccTaskManager;
import game.module.spin.bean.SpinBean;
import game.module.spin.dao.MySpinTemplateCache;
import game.module.spin.dao.SpinTemplateCache;
import game.module.spin.dao.SpinCache;
import game.module.template.MySpineTemplate;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageSpin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 观星
 */
@MsgCodeAnn(msgcode = WsMessageSpin.C2SSpinRoll.id, accessLimit = 200)
public class SpinRollProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SpinRollProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageSpin.C2SSpinRoll reqMsg = WsMessageSpin.C2SSpinRoll.parse(request);
        final int buy_type = reqMsg.type;
        int times = reqMsg.times;
        int playerId = playingRole.getId();
        logger.info("spin roll,player={},req={}", playerId, reqMsg);
        SpinBean spinBean = SpinCache.getInstance().getSpinBean(playerId);
        if (spinBean == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinRoll.msgCode, 1362);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        if (buy_type != 1 && buy_type != 2) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinRoll.msgCode, 1360);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        if (times != 1 && times != 10) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinRoll.msgCode, 1362);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is item enough
        Map<String, Object> spinTemplate = SpinTemplateCache.getInstance().getSpinTemplate(buy_type);
        Map<String, Object> spin1Config = (Map) spinTemplate.get(String.valueOf(times));
        int gsid = (Integer) spin1Config.get("GSID");
        int count = (Integer) spin1Config.get("COUNT");
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), gsid, count)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinRoll.msgCode, 1363);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        List<List<Integer>> rewards = null;
        if (buy_type == 1) {
            rewards = spinBean.getRewardsNormal();
        } else {
            rewards = spinBean.getRewardsAdvance();
        }
        //rand award
        WsMessageSpin.S2CSpinRoll respmsg = new WsMessageSpin.S2CSpinRoll();
        respmsg.times = times;
        respmsg.type = buy_type;
        respmsg.items = new ArrayList<>(times);
        RandomDispatcher<Integer> rd = new RandomDispatcher<>();
        MySpineTemplate mySpineTemplate = MySpinTemplateCache.getInstance().getSpinByType(buy_type);
        List<Integer> spineTemplateRates = mySpineTemplate.getRates();
        int i = 0;
        for (int aRate : spineTemplateRates) {
            rd.put(aRate, i);
            i++;
        }
        Map<Integer, Integer> rewardMap = new HashMap<>();
        for (int j = 0; j < times; j++) {
            Integer randIndex = rd.random();
            if(j == 0) {
                respmsg.pos = randIndex;
            }
            List<Integer> rewardlist = rewards.get(randIndex);
            int rewardGsid = rewardlist.get(0);
            int rewardCount = rewardlist.get(1);
            respmsg.items.add(new WsMessageBase.IOSpinItem(rewardGsid, rewardCount, 1));
            if (!rewardMap.containsKey(rewardGsid)) {
                rewardMap.put(rewardGsid, rewardCount);
            } else {
                rewardMap.put(rewardGsid, rewardMap.get(rewardGsid) + rewardCount);
            }
        }
        //award
        for (Map.Entry<Integer, Integer> apair : rewardMap.entrySet()) {
            AwardUtils.changeRes(playingRole, apair.getKey(), apair.getValue(), LogConstants.SPIN);
        }
        //score and lucky star
        List<Integer> drops = (List<Integer>) spin1Config.get("DROP");
        int scoreTemplateId = drops.get(0);
        int luckyTemplteId = drops.get(1);
        AwardUtils.changeRes(playingRole, scoreTemplateId, mySpineTemplate.getAddScore()*times, LogConstants.SPIN);
        respmsg.items.add(new WsMessageBase.IOSpinItem(scoreTemplateId, mySpineTemplate.getAddScore()*times, 0));
        AwardUtils.changeRes(playingRole, luckyTemplteId, mySpineTemplate.getAddLuckyStar()*times, LogConstants.SPIN);
        respmsg.items.add(new WsMessageBase.IOSpinItem(luckyTemplteId, mySpineTemplate.getAddLuckyStar()*times, 0));
        //cost
        AwardUtils.changeRes(playingRole, gsid, -count, LogConstants.SPIN);
        //update mission progress
        AwardUtils.changeRes(playingRole, MissionConstants.LOOK_STAR_PMARK, times, LogConstants.SPIN);
        if(buy_type == 1) {
            AwardUtils.changeRes(playingRole, MissionConstants.MARK_SPIN_NORMAL, times, LogConstants.SPIN);
            ActivityWeekManager.getInstance().gxdbActivity(playingRole,times);
            OccTaskManager.getInstance().lookStarAddOccTaskMark(playingRole, times);
        }else {
            AwardUtils.changeRes(playingRole, MissionConstants.MARK_SPIN_ADVANCE, times, LogConstants.SPIN);
        }
        ActivityWeekManager.getInstance().spinTnqw(playingRole,buy_type,times);
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }
}
