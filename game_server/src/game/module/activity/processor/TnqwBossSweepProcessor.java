package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActTnqw;
import game.module.activity.dao.ActTnqwCache;
import game.module.activity.dao.ActTnqwDaoHelper;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemConstants;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.template.RewardTemplateSimple;
import game.module.template.ZhdTnqwBossTemplate;
import game.module.template.ZhdTnqwTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageBase;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageActivity.C2STnqwBossSweep.id, accessLimit = 200)
public class TnqwBossSweepProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(TnqwBossSweepProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 加载所有邮件
     */
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageActivity.C2STnqwBossSweep reqmsg = WsMessageActivity.C2STnqwBossSweep.parse(request);
        logger.info("tnqw boss sweep!player={},req={}", playerId, reqmsg);
        int boss_index = reqmsg.boss_index;
        int times = reqmsg.times;
        ZhdTnqwTemplate tnqwTemplate = ActivityWeekTemplateCache.getInstance().getTnqwTemplate();
        if (boss_index < 0 || boss_index >= tnqwTemplate.getBosslist().size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CTnqwBossSweep.msgCode, 130);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        int scoreSum = 0;
        for (ZhdTnqwTemplate.ZhdTnqwEvent zhdTnqwEvent : tnqwTemplate.getEvent()) {
            int scoreMark = zhdTnqwEvent.getMark();
            scoreSum += ItemManager.getInstance().getCount(playingRole.getPlayerBean(), scoreMark);
        }
        ActTnqw actTnqw = ActTnqwCache.getInstance().getActTnqw(playerId);
        ZhdTnqwTemplate.ZhdTnqwBosslist zhdTnqwBosslist = tnqwTemplate.getBosslist().get(boss_index);
        if (zhdTnqwBosslist.getActscore() != null) {
            //积分未到
            if (scoreSum < zhdTnqwBosslist.getActscore()) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CTnqwBossSweep.msgCode, 1551);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        } else {//大 boss
            if (actTnqw == null || actTnqw.getNowHp() == null) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CTnqwBossSweep.msgCode, 1553);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            //其余boss没有全部死亡
            for (int i = 0; i < tnqwTemplate.getBosslist().size() - 1; i++) {
                if (!actTnqw.getNowHp().containsKey(i) || actTnqw.getNowHp().get(i) > 0) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CTnqwBossSweep.msgCode, 1553);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
            }
        }
        //已经死亡
        if (actTnqw != null && actTnqw.getNowHp() != null && actTnqw.getNowHp().containsKey(boss_index) && actTnqw.getNowHp().get(boss_index)<=0) {//已经死亡
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CTnqwBossSweep.msgCode, 1552);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost兵符
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), ItemConstants.BING_FU, times)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CTnqwBossSweep.msgCode, 1554);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //上次伤害值
        if(actTnqw == null || actTnqw.getLastDamage() == null || !actTnqw.getLastDamage().containsKey(boss_index)){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CTnqwBossSweep.msgCode, 1555);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost兵符
        AwardUtils.changeRes(playingRole, ItemConstants.BING_FU, -times, LogConstants.MODULE_ACTIVITY);
        //伤害统计
        List<ZhdTnqwBossTemplate> tnqwBossTemplates = ActivityWeekTemplateCache.getInstance().getTnqwBossTemplates();
        ZhdTnqwBossTemplate zhdTnqwBossTemplate = tnqwBossTemplates.get(boss_index);
        long hurmSum = actTnqw.getLastDamage().get(boss_index)*times;
        Long nowHp = actTnqw.getNowHp().get(boss_index);
        if (nowHp == null) {
            nowHp = zhdTnqwBossTemplate.getMaxhp();
        }
        nowHp -= hurmSum;
        nowHp = Math.max(0, nowHp);
        actTnqw.getNowHp().put(boss_index, nowHp);
        ActTnqwDaoHelper.asyncUpdateActTnqw(actTnqw);
        //reward
        List<RewardTemplateSimple> challrewards = zhdTnqwBossTemplate.getChallrewards();
        List<WsMessageBase.RewardInfo> rewardItems = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : challrewards) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_ACTIVITY);
            rewardItems.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //check die
        if (actTnqw.getNowHp().get(boss_index) <= 0) {
            List<RewardTemplateSimple> killrewards = zhdTnqwBossTemplate.getKillrewards();
            for (RewardTemplateSimple rewardTemplateSimple : killrewards) {
                AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_ACTIVITY);
                rewardItems.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
        }
        //ret
        WsMessageActivity.S2CTnqwBossSweep respmsg = new WsMessageActivity.S2CTnqwBossSweep();
        respmsg.nowhp = nowHp;
        respmsg.reward = rewardItems;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
