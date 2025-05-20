package game.module.spin.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.spin.bean.SpinBean;
import game.module.spin.dao.MySpinTemplateCache;
import game.module.spin.dao.SpinTemplateCache;
import game.module.spin.dao.SpinCache;
import game.module.spin.dao.SpinDaoHelper;
import game.module.spin.logic.SpinManager;
import game.module.template.MySpineTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageSpin;

import java.util.*;

@MsgCodeAnn(msgcode = WsMessageSpin.C2SSpinList.id, accessLimit = 200)
public class SpinListProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SpinListProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageSpin.C2SSpinList reqmsg = WsMessageSpin.C2SSpinList.parse(request);
        int playerId = playingRole.getId();
        logger.info("spin list,player={},req={}", playerId, reqmsg);
        int type = reqmsg.type;
        boolean is_force = reqmsg.is_force;
        if (type != 1 && type != 2) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinList.msgCode, 1360);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        SpinBean spinBean = SpinCache.getInstance().getSpinBean(playerId);
        if (!is_force) {
            if (spinBean == null) {
                spinBean = SpinManager.getInstance().createSpin(playerId, type);
                SpinDaoHelper.asyncInsertSpinBean(spinBean);
                SpinCache.getInstance().addSpinBean(spinBean);
            } else if (type == 1 && spinBean.getRewardsNormal() == null) {
                List<List<Integer>> rewardslist = SpinManager.getInstance().generateSpinRewards(type);
                spinBean.setRewardsNormal(rewardslist);
                SpinDaoHelper.asyncUpdateSpinBean(spinBean);
            } else if (type == 2 && spinBean.getRewardsAdvance() == null) {
                List<List<Integer>> rewardslist = SpinManager.getInstance().generateSpinRewards(type);
                spinBean.setRewardsAdvance(rewardslist);
                SpinDaoHelper.asyncUpdateSpinBean(spinBean);
            }
            //ret
            sendResponse(playingRole, spinBean, is_force, type);
        } else {//刷新的话，spinBean肯定不为空
            //check free
            int refreshCostMoney = 0;
            Date lastRefreshTime = null;
            if (type == 1) {
                lastRefreshTime = spinBean.getLastRefreshTimeNormal();
            } else {
                lastRefreshTime = spinBean.getLastRefreshTimeAdvance();
            }
            Date now = new Date();
            MySpineTemplate mySpineTemplate = MySpinTemplateCache.getInstance().getSpinByType(type);
            if (lastRefreshTime != null && DateUtils.addHours(lastRefreshTime, mySpineTemplate.getFreeRefreshHour()).after(now)) {
                Map<String, Object> spinTemplate = SpinTemplateCache.getInstance().getSpinTemplate(type);
                Map<String, Integer> refreshConfig = (Map<String, Integer>) spinTemplate.get("REFRESH");
                if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), refreshConfig.get("GSID"), refreshConfig.get("COUNT"))) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinList.msgCode, 1361);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                refreshCostMoney = refreshConfig.get("COUNT");
            }
            //do
            List<List<Integer>> rewards = null;
            if (type == 1) {
                rewards = spinBean.getRewardsNormal();
            } else {
                rewards = spinBean.getRewardsAdvance();
            }
            rewards.clear();
            List<List<Integer>> rewardslist = SpinManager.getInstance().generateSpinRewards(type);
            rewards.addAll(rewardslist);
            if (refreshCostMoney == 0) {
                if (type == 1) {
                    spinBean.setLastRefreshTimeNormal(now);
                } else {
                    spinBean.setLastRefreshTimeAdvance(now);
                }
            } else {
                AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -refreshCostMoney, LogConstants.SPIN);
            }
            SpinDaoHelper.asyncUpdateSpinBean(spinBean);
            sendResponse(playingRole, spinBean, is_force, type);
        }
    }

    private void sendResponse(PlayingRole playingRole, SpinBean spinBean, boolean is_force, int type) {
        WsMessageSpin.S2CSpinList respmsg = new WsMessageSpin.S2CSpinList();
        respmsg.is_force = is_force;
        respmsg.type = type;
        //
        Date lastRefreshTime = null;
        if (type == 1) {
            lastRefreshTime = spinBean.getLastRefreshTimeNormal();
        } else {
            lastRefreshTime = spinBean.getLastRefreshTimeAdvance();
        }
        if (lastRefreshTime != null) {
            respmsg.free = lastRefreshTime.getTime();
        } else {
            MySpineTemplate mySpineTemplate = MySpinTemplateCache.getInstance().getSpinByType(type);
            respmsg.free = DateUtils.addHours(new Date(), -mySpineTemplate.getFreeRefreshHour()).getTime();
        }
        //
        List<List<Integer>> rewards;
        if (type == 1) {
            rewards = spinBean.getRewardsNormal();
        } else {
            rewards = spinBean.getRewardsAdvance();
        }
        respmsg.items = new ArrayList<>(rewards.size());
        for (List<Integer> apair : rewards) {
            respmsg.items.add(new WsMessageBase.IOSpinItem(apair.get(0), apair.get(1), 1));
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
