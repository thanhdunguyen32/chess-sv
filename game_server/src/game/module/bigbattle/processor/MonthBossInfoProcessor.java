package game.module.bigbattle.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.bigbattle.bean.MonthBoss;
import game.module.bigbattle.dao.MonthBossCache;
import game.module.bigbattle.dao.MonthBossDaoHelper;
import game.module.bigbattle.dao.MyMonthBossTemplateCache;
import game.module.bigbattle.logic.MonthBossManager;
import game.module.template.ChapterBattleTemplate;
import game.module.template.MyMonthBossTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBigbattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBigbattle.C2SMonthBossInfo.id, accessLimit = 200)
public class MonthBossInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MonthBossInfoProcessor.class);

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
        WsMessageBigbattle.C2SMonthBossInfo reqmsg = WsMessageBigbattle.C2SMonthBossInfo.parse(request);
        logger.info("month boss info!player={},req={}", playerId, reqmsg);
        int monthIndex = reqmsg.monthIndex;
        MonthBoss monthBoss = MonthBossCache.getInstance().getMonthBoss(playerId);
        if (monthBoss == null) {
            monthBoss = MonthBossManager.getInstance().createMonthBoss(playerId);
            MonthBossDaoHelper.asyncInsertMonthBoss(monthBoss);
            MonthBossCache.getInstance().addMonthBoss(monthBoss);
        }
        //check need reset
        MonthBossManager.getInstance().resetCheck(playingRole, monthBoss);
        //is pass
        int levelIndex = monthBoss.getLevelIndex().get(monthIndex);
        List<MyMonthBossTemplate> monthBossConfig1 = MyMonthBossTemplateCache.getInstance().getMonthBossConfig(monthIndex);
        if (levelIndex >= monthBossConfig1.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CMonthBossInfo.msgCode, 1470);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //ret
        WsMessageBigbattle.S2CMonthBossInfo respmsg = new WsMessageBigbattle.S2CMonthBossInfo();
        MyMonthBossTemplate monthBossConfig = monthBossConfig1.get(levelIndex);
        respmsg.general = monthBossConfig.getGeneral();
        respmsg.lastdamge = monthBoss.getLastDamage().get(monthIndex);
        respmsg.index = levelIndex;
        respmsg.maxhp = monthBossConfig.getMaxhp();
        respmsg.nowhp = monthBoss.getNowHp().get(monthIndex);
        respmsg.level = monthBossConfig.getLevel();
        respmsg.rewards = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : monthBossConfig.getRewards()) {
            respmsg.rewards.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //bset
        respmsg.bset = new ArrayList<>();
        Map<Integer, ChapterBattleTemplate> bsetConfig = monthBossConfig.getBset();
        for (Map.Entry<Integer, ChapterBattleTemplate> aEntry : bsetConfig.entrySet()) {
            int pos = aEntry.getKey();
            ChapterBattleTemplate beastSet = aEntry.getValue();
            respmsg.bset.add(new WsMessageBase.IOGeneralSimple(pos, beastSet.getGsid(), beastSet.getLevel(), beastSet.getHpcover(), beastSet.getPclass(),
                    beastSet.getExhp().longValue(), beastSet.getExhp(), beastSet.getExatk()));
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
