package game.module.bigbattle.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.bigbattle.bean.MonthBoss;
import game.module.bigbattle.dao.MonthBossCache;
import game.module.bigbattle.dao.MonthBossDaoHelper;
import game.module.bigbattle.dao.MyMonthBossTemplateCache;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.log.constants.LogConstants;
import game.module.manor.processor.ManorBattleEndProcessor;
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
import ws.WsMessageBattle;
import ws.WsMessageBigbattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBigbattle.C2SMonthBossBattleEnd.id, accessLimit = 200)
public class MonthBossEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MonthBossEndProcessor.class);

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
        WsMessageBigbattle.C2SMonthBossBattleEnd reqmsg = WsMessageBigbattle.C2SMonthBossBattleEnd.parse(request);
        logger.info("month boss end!player={},req={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if(!battleIdValid){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CMonthBossBattleEnd.msgCode,1468);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Map<Integer,WsMessageBase.IOBHurt> leftHpMap = new HashMap<>();
        Map<Integer,WsMessageBase.IOBHurt> rightHpMap = new HashMap<>();
        if(reqmsg.as != null && reqmsg.as.length>0){
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.as){
                rightHpMap.put(iobHurt.gsid,iobHurt);
            }
        }
        if(reqmsg.df != null && reqmsg.df.length>0){
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.df){
                leftHpMap.put(iobHurt.gsid,iobHurt);
            }
        }
        GameServer.executorService.execute(() -> {
            //胜负判断
            WsMessageBase.IOBHurt[] rightHp = reqmsg.as;
            String battleResult = null;
            if(rightHp != null && rightHp.length>0) {
                boolean isRightAllDie = true;
                for (WsMessageBase.IOBHurt iobHurt : rightHp){
                    if(iobHurt.hp>0){
                        isRightAllDie = false;
                        break;
                    }
                }
                if(isRightAllDie){
                    battleResult = "win";
                }else{
                    battleResult = "lose";
                }
            }
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            MonthBoss monthBoss = MonthBossCache.getInstance().getMonthBoss(playerId);
            int monthIndex = reqmsg.monthIndex;
            int levelIndex = monthBoss.getLevelIndex().get(monthIndex);
            MyMonthBossTemplate monthBossConfig = MyMonthBossTemplateCache.getInstance().getMonthBossConfig(monthIndex, levelIndex);
            Map<Integer, ChapterBattleTemplate> bsetConfig = monthBossConfig.getBset();
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateTemplate(playerId, battleFormation.getNormal(), bsetConfig);
            logger.info("month boss battle result:{}", battleRet);
            //战斗结果修正
            if(battleResult != null){
                battleRet.ret = battleResult;
            }
            for(WsMessageBase.IOBattleReportItem ioBattleReportItem : battleRet.report.left){
                if(leftHpMap.get(ioBattleReportItem.gsid) != null){
                    WsMessageBase.IOBHurt iobHurt = leftHpMap.get(ioBattleReportItem.gsid);
                    ioBattleReportItem.hurm = iobHurt.hurm;
                    ioBattleReportItem.heal = iobHurt.heal;
                }
            }
            for(WsMessageBase.IOBattleReportItem ioBattleReportItem : battleRet.report.right){
                if(rightHpMap.get(ioBattleReportItem.gsid) != null){
                    WsMessageBase.IOBHurt iobHurt = rightHpMap.get(ioBattleReportItem.gsid);
                    ioBattleReportItem.hurm = iobHurt.hurm;
                    ioBattleReportItem.heal = iobHurt.heal;
                }
            }
            //save damage and hp
            long myDamage = 0;
            if (reqmsg.df != null && reqmsg.df.length > 0) {
                myDamage = ManorBattleEndProcessor.getClientHurmSum(reqmsg.df);
            } else {
                myDamage = getMyDamage(battleRet);
            }
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
                    AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), gsCount, LogConstants.BIG_BATTLE);
                    rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), gsCount));
                }
            }
            //save bean
            nowHp -= myDamage;
            if (nowHp > 0) {
                monthBoss.getNowHp().set(monthIndex, nowHp);
                monthBoss.getLastDamage().set(monthIndex, myDamage);
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
            //ret
            WsMessageBigbattle.S2CMonthBossBattleEnd respmsg = new WsMessageBigbattle.S2CMonthBossBattleEnd();
            respmsg.result = battleRet;
            respmsg.items = rewardItems;
            respmsg.damge = myDamage;
            respmsg.kill = nowHp <= 0 ? 1 : 0;
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });

    }

    private long getMyDamage(WsMessageBase.IOBattleResult battleRet) {
        long ret = 0;
        for(WsMessageBase.IOBattleReportItem ioBattleReportItem : battleRet.report.left){
            ret += ioBattleReportItem.hurm;
        }
        return ret;
    }

}
