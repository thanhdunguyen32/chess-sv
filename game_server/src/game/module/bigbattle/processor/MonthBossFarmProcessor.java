package game.module.bigbattle.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.bigbattle.bean.MonthBoss;
import game.module.bigbattle.dao.MonthBossCache;
import game.module.bigbattle.dao.MonthBossDaoHelper;
import game.module.bigbattle.dao.MyMonthBossTemplateCache;
import game.module.item.logic.ItemConstants;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.template.MyMonthBossTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageBigbattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBigbattle.C2SMonthBossFarm.id, accessLimit = 200)
public class MonthBossFarmProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MonthBossFarmProcessor.class);

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
        WsMessageBigbattle.C2SMonthBossFarm reqmsg = WsMessageBigbattle.C2SMonthBossFarm.parse(request);
        logger.info("month boss farm!player={},req={}", playerId, reqmsg);
        int monthIndex = reqmsg.monthIndex;
        int times = reqmsg.times;
        //already through
        MonthBoss monthBoss = MonthBossCache.getInstance().getMonthBoss(playerId);
        int levelIndex = monthBoss.getLevelIndex().get(monthIndex);
        if(levelIndex >= 2 && monthBoss.getNowHp().get(monthIndex) <= 0){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CMonthBossFarm.msgCode, 1421);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //item enough
        if(!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), ItemConstants.BING_FU, times)){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CMonthBossFarm.msgCode, 1422);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        if(times <= 0){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CMonthBossFarm.msgCode, 1422);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        MyMonthBossTemplate monthBossConfig = MyMonthBossTemplateCache.getInstance().getMonthBossConfig(monthIndex, levelIndex);
        //save damage and hp
        long myDamage = monthBoss.getLastDamage().get(monthIndex) * times;
        Long nowHp = monthBoss.getNowHp().get(monthIndex);
        if (nowHp - myDamage < 0) {
            myDamage = nowHp;
        }
        //fix award
        List<RewardTemplateSimple> rewards = monthBossConfig.getRewards();
        List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : rewards) {
            int gsCount = rewardTemplateSimple.getCOUNT() / 180;
            if (gsCount > 0) {
                gsCount *= times;
                AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), gsCount, LogConstants.BIG_BATTLE);
                rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), gsCount));
            }
        }
        //save bean
        nowHp -= myDamage;
        if (nowHp > 0) {
            monthBoss.getNowHp().set(monthIndex, nowHp);
        } else if (levelIndex <= 2) {
            //pass award
            for (RewardTemplateSimple rewardTemplateSimple : rewards) {
                AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.BIG_BATTLE);
                rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            levelIndex++;
            monthBoss.getLevelIndex().set(monthIndex, levelIndex);
            List<MyMonthBossTemplate> monthBossConfig1 = MyMonthBossTemplateCache.getInstance().getMonthBossConfig(monthIndex);
            if(levelIndex < monthBossConfig1.size()) {
                monthBossConfig = MyMonthBossTemplateCache.getInstance().getMonthBossConfig(monthIndex, levelIndex);
                monthBoss.getLastDamage().set(monthIndex, monthBossConfig.getMaxhp() / 500);
                monthBoss.getNowHp().set(monthIndex, monthBossConfig.getMaxhp());
            }else{
                monthBoss.getNowHp().set(monthIndex, 0L);
            }
        }else {
            monthBoss.getNowHp().set(monthIndex, nowHp);
        }
        MonthBossDaoHelper.asyncUpdateMonthBoss(monthBoss);
        //update mission progress
        AwardUtils.changeRes(playingRole, MissionConstants.BIG_BATTLE_PMARK, 1, LogConstants.BIG_BATTLE);
        //cost
        AwardUtils.changeRes(playingRole, ItemConstants.BING_FU, -times, LogConstants.BIG_BATTLE);
        //ret
        WsMessageBigbattle.S2CMonthBossFarm respmsg = new WsMessageBigbattle.S2CMonthBossFarm();
        respmsg.items = rewardItems;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
