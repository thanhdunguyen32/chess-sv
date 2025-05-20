package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.legion.bean.DbLegionBoss;
import game.module.legion.bean.LegionBean;
import game.module.legion.dao.LegionBossTemplateCache;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.MyLegionBossTemplateCache;
import game.module.legion.logic.LegionManager;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.template.ChapterBattleTemplate;
import game.module.template.LegionBossTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBase.IOGeneralLegion;
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionGetBossInfo.id, accessLimit = 200)
public class LegionBossInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionBossInfoProcessor.class);

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
        WsMessageLegion.C2SLegionGetBossInfo reqmsg = WsMessageLegion.C2SLegionGetBossInfo.parse(request);
        logger.info("legion boss info!player={},req={}", playerId, reqmsg);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionGetBossInfo.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        int chapter_index = reqmsg.chapter_index;
        if (chapter_index == -1) {
            chapter_index = 0;
            if (legionBean.getLegionBoss() != null) {
                chapter_index = legionBean.getLegionBoss().getChapterIndex();
            }
        }
        //can view
        if (chapter_index > 0 && legionBean.getLegionBoss() == null || legionBean.getLegionBoss() != null && chapter_index > legionBean.getLegionBoss().getChapterIndex()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionGetBossInfo.msgCode, 1542);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is finish
        if (chapter_index >= LegionBossTemplateCache.getInstance().getSize()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionGetBossInfo.msgCode, 1556);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        WsMessageLegion.S2CLegionGetBossInfo respmsg = new WsMessageLegion.S2CLegionGetBossInfo();
        respmsg.boss = new WsMessageBase.IOLegionBoss();
        LegionBossTemplate.LegionBoss1 bossTemplate = LegionBossTemplateCache.getInstance().getBossTemplate(chapter_index);
        respmsg.boss.name = bossTemplate.getNAME();
        respmsg.boss.maxhp = bossTemplate.getBOSSHP();
        respmsg.boss.chapter = bossTemplate.getID();
        respmsg.boss.rewards = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : bossTemplate.getREWARD()) {
            respmsg.boss.rewards.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        Map<Integer, ChapterBattleTemplate> legionBossTemplate = MyLegionBossTemplateCache.getInstance().getLegionBossTemplate(chapter_index).getBset();
        respmsg.boss.bset = new HashMap<>(legionBossTemplate.size());
        for (Map.Entry<Integer, ChapterBattleTemplate> aEntry : legionBossTemplate.entrySet()) {
            respmsg.boss.bset.put(aEntry.getKey(), buildBsetGeneral(aEntry.getValue()));
        }
        if (legionBean.getLegionBoss() == null) {
            respmsg.boss.nowhp = bossTemplate.getBOSSHP();
        } else if (chapter_index < legionBean.getLegionBoss().getChapterIndex()) {
            respmsg.boss.nowhp = 0;
        } else {
            respmsg.boss.nowhp = legionBean.getLegionBoss().getNowhp();
        }
        //rank
        respmsg.rank = new ArrayList<>();
        if (legionBean.getLegionBoss() != null && chapter_index < legionBean.getLegionBoss().getRecords().size()) {
            DbLegionBoss.LegionBossDamage legionBossDamage = legionBean.getLegionBoss().getRecords().get(chapter_index);
            Map<Integer, Long> damageList = legionBossDamage.getDamageList();
            if (damageList != null) {
                for (Map.Entry<Integer, Long> aEntry : damageList.entrySet()) {
                    int otherPlayerId = aEntry.getKey();
                    PlayerBaseBean playerBaseBean = PlayerOfflineManager.getInstance().getPlayerOfflineCache(otherPlayerId);
                    respmsg.rank.add(new WsMessageBase.IOLegionRank(playerBaseBean.getName(), aEntry.getValue(), playerBaseBean.getPower()));
                }
                respmsg.rank.sort((r1, r2) -> (int) (r2.damge - r1.damge));
            }
            //last damage
            Map<Integer, Long> lastDamageMap = legionBossDamage.getLastDamageMap();
            if(lastDamageMap != null && lastDamageMap.containsKey(playerId)){
                respmsg.self = new WsMessageBase.IOLegionBossSelf(lastDamageMap.get(playerId));
            }
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private IOGeneralLegion buildBsetGeneral(ChapterBattleTemplate chapterBattleTemplate) {
        return new IOGeneralLegion(chapterBattleTemplate.getGsid(), chapterBattleTemplate.getLevel(),
                chapterBattleTemplate.getHpcover(), chapterBattleTemplate.getPclass(), chapterBattleTemplate.getExhp().longValue(),
                chapterBattleTemplate.getExatk().intValue());
    }

}
