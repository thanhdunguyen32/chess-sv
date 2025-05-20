package game.module.legion.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.legion.bean.DbLegionBoss;
import game.module.legion.bean.LegionBean;
import game.module.legion.dao.LegionBossTemplateCache;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.LegionDaoHelper;
import game.module.legion.dao.MyLegionBossTemplateCache;
import game.module.legion.logic.LegionManager;
import game.module.log.constants.LogConstants;
import game.module.manor.processor.ManorBattleEndProcessor;
import game.module.template.ChapterBattleTemplate;
import game.module.template.LegionBossTemplate;
import game.module.template.MyLegionBossConfig;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SLegionBossBattleEnd.id, accessLimit = 200)
public class LegionBossBattleEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionBossBattleEndProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageBattle.C2SLegionBossBattleEnd reqmsg = WsMessageBattle.C2SLegionBossBattleEnd.parse(request);
        logger.info("legion boss battle end!player={},req={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if(!battleIdValid){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CLegionBossBattleEnd.msgCode,1468);
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
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CLegionBossBattleEnd.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
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
            int chapter_index = 0;
            LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
            if (legionBean.getLegionBoss() != null) {
                chapter_index = legionBean.getLegionBoss().getChapterIndex();
            }
            MyLegionBossConfig legionBossTemplate = MyLegionBossTemplateCache.getInstance().getLegionBossTemplate(chapter_index);
            Map<Integer, ChapterBattleTemplate> legionBossTemplateBset = legionBossTemplate.getBset();
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateTemplate(playerId, battleFormation.getNormal(), legionBossTemplateBset);
            logger.info("legion boss battle result:{}", battleRet);
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
            //do
            List<WsMessageBase.IORewardItem> dropItems = new ArrayList<>();
            //reward
            LegionBossTemplate.LegionBoss1 bossTemplate = LegionBossTemplateCache.getInstance().getBossTemplate(chapter_index);
            for (RewardTemplateSimple rewardTemplateSimple : bossTemplate.getREWARD()) {
                AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_LEGION);
                dropItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            //伤害统计
            long hurmSum = 0;
            if (reqmsg.as == null || reqmsg.as.length == 0) {
                hurmSum = getMyHurm(battleRet);
            }
            long nowhp = 0;
            synchronized (legionBean) {
                DbLegionBoss legionBoss = legionBean.getLegionBoss();
                if (legionBean.getLegionBoss() == null) {
                    legionBoss = LegionManager.getInstance().createLegionBoss();
                    legionBean.setLegionBoss(legionBoss);
                }
                //save record
                List<DbLegionBoss.LegionBossDamage> records = legionBoss.getRecords();
                if (records == null) {
                    records = new ArrayList<>();
                    legionBoss.setRecords(records);
                }
                if (chapter_index >= records.size()) {
                    DbLegionBoss.LegionBossDamage legionBossDamage = new DbLegionBoss.LegionBossDamage();
                    legionBossDamage.setDamageList(new HashMap<>());
                    legionBossDamage.setLastDamageMap(new HashMap<>());
                    records.add(legionBossDamage);
                }
                //总伤害量
                if (reqmsg.as != null && reqmsg.as.length > 0) {
                    long nowEnemyHp = ManorBattleEndProcessor.getEnemyNowHp(reqmsg.as);
                    hurmSum = Math.max(legionBossTemplate.getMaxhp() - nowEnemyHp, 0);
                }
                Map<Integer, Long> damageList = records.get(chapter_index).getDamageList();
                if (damageList.containsKey(playerId)) {
                    damageList.put(playerId, damageList.get(playerId) + hurmSum);
                } else {
                    damageList.put(playerId, hurmSum);
                }
                //save last damage
                records.get(chapter_index).getLastDamageMap().put(playerId, hurmSum);
                //save hp
                nowhp = legionBoss.getNowhp() - hurmSum;
                if (nowhp <= 0) {
                    //mail send reward
                    LegionManager.getInstance().killBossRewards(legionId, chapter_index, damageList);
                    legionBoss.setChapterIndex(++chapter_index);
                    bossTemplate = LegionBossTemplateCache.getInstance().getBossTemplate(chapter_index);
                    legionBoss.setNowhp(bossTemplate.getBOSSHP());
                } else {
                    legionBoss.setNowhp(nowhp);
                }
                //save legion
                LegionDaoHelper.asyncUpdateLegionBean(legionBean);
            }
            //ret
            WsMessageBattle.S2CLegionBossBattleEnd respmsg = new WsMessageBattle.S2CLegionBossBattleEnd();
            respmsg.kill = nowhp <= 0 ? 1 : 0;
            respmsg.damge = hurmSum;
            respmsg.last = System.currentTimeMillis();
            respmsg.result = battleRet;
            respmsg.rewards = dropItems;
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });
    }

    private long getMyHurm(WsMessageBase.IOBattleResult battleRet) {
        long ret = 0;
        for (WsMessageBase.IOBattleReportItem ioBattleReportItem : battleRet.report.left) {
            ret += ioBattleReportItem.hurm;
        }
        return ret;
    }

}
