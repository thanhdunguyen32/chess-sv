package game.module.worldboss.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.legion.bean.LegionBean;
import game.module.legion.dao.LegionCache;
import game.module.legion.logic.LegionManager;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.season.bean.BattleSeason;
import game.module.season.dao.SeasonCache;
import game.module.template.ChapterBattleTemplate;
import game.module.template.WorldBossTemplate;
import game.module.worldboss.bean.WorldBoss;
import game.module.worldboss.dao.WorldBossCache;
import game.module.worldboss.dao.WorldBossTemplateCache;
import game.module.worldboss.logic.WorldBossManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBase.IOGeneralLegion;
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SWorldBossInfo.id, accessLimit = 200)
public class WorldBossInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(WorldBossInfoProcessor.class);

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
        logger.info("world boss info!player={}", playerId);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CWorldBossInfo.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //check world boss battle finish
        WorldBossManager.getInstance().checkFinish();
        //
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        boolean legionBossFinish = LegionManager.getInstance().isLegionBossFinish(legionBean);
        if (!legionBossFinish) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CWorldBossInfo.msgCode, 1546);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        WsMessageLegion.S2CWorldBossInfo respmsg = new WsMessageLegion.S2CWorldBossInfo();
        //legion
        respmsg.legion = new WsMessageBase.IOWorldBossLegion();
        WorldBoss worldBoss = WorldBossCache.getInstance().getWorldBoss();
        if (worldBoss != null) {
            Map<Long, Long> legionDamageSum = worldBoss.getLegionDamageSum();
            if (legionDamageSum != null && legionDamageSum.containsKey(legionId)) {
                respmsg.legion.damage = legionDamageSum.get(legionId);
            }
            List<Long> legionRank = WorldBossCache.getInstance().getLegionRank();
            int i = 1;
            for (Long aLegionId : legionRank) {
                if (aLegionId.equals(legionId)) {
                    respmsg.legion.rank = i;
                    respmsg.legion.maxrank = i;
                    break;
                }
                i++;
            }
        }
        //boss
        respmsg.boss = new WsMessageBase.IOWorldBossInfo();
        BattleSeason battleSeason = SeasonCache.getInstance().getBattleSeason();
        Integer seasonId = battleSeason.getSeason();
        respmsg.boss.id = seasonId;
        //最后一天不开放
        respmsg.boss.endtime = DateUtils.addDays(battleSeason.getEtime(), -1).getTime();
        if (worldBoss != null && worldBoss.getPlayerLastDamage() != null) {
            Long myLastDamage = worldBoss.getPlayerLastDamage().get(playerId);
            if(myLastDamage != null){
                respmsg.boss.lastdamage = myLastDamage;
            }
        }
        respmsg.boss.bset = new HashMap<>();
        WorldBossTemplate worldBossTemplate = WorldBossTemplateCache.getInstance().getWorldBossTemplate(seasonId - 1);
        for (Map.Entry<Integer, ChapterBattleTemplate> aEntry : worldBossTemplate.getBset().entrySet()) {
            respmsg.boss.bset.put(aEntry.getKey(), buildBsetGeneral(aEntry.getValue()));
        }
        respmsg.boss.hasgift = 1;
        //self
        respmsg.self = new WsMessageBase.IOWorldBossSelf();
        if (worldBoss != null) {
            Map<Integer, Long> playerDamageSum = worldBoss.getPlayerDamageSum();
            if (playerDamageSum != null && playerDamageSum.containsKey(playerId)) {
                respmsg.self.damage = playerDamageSum.get(playerId);
            }
            List<Integer> playerRank = WorldBossCache.getInstance().getPlayerRank();
            int i = 1;
            for (Integer aPlayerId : playerRank) {
                if (aPlayerId.equals(playerId)) {
                    respmsg.self.rank = i;
                    break;
                }
                i++;
            }
        }
        //rank
        respmsg.rank = new ArrayList<>();
        List<Integer> playerRank = WorldBossCache.getInstance().getPlayerRank();
        for (int i = 0; i < playerRank.size() && i < 6; i++) {
            int aPlayerId = playerRank.get(i);
            Long damageSum = worldBoss.getPlayerDamageSum().get(aPlayerId);
            PlayerBaseBean playerBaseBean = PlayerOfflineManager.getInstance().getPlayerOfflineCache(aPlayerId);
            respmsg.rank.add(new WsMessageBase.IOWorldBossRank(playerBaseBean.getHeadid(), playerBaseBean.getFrameid(), playerBaseBean.getIconid(),
                    playerBaseBean.getLevel(), playerBaseBean.getPower(), damageSum, playerBaseBean.getName(), playerBaseBean.getId()));
        }
        //worldrank
        respmsg.worldrank = new ArrayList<>();
        List<Long> legionRank = WorldBossCache.getInstance().getLegionRank();
        for (int i = 0; i < legionRank.size() && i < 20; i++) {
            long aLegionId = legionRank.get(i);
            Long damageSum = worldBoss.getLegionDamageSum().get(aLegionId);
            LegionBean legionBean1 = LegionCache.getInstance().getLegionBean(aLegionId);
            respmsg.worldrank.add(new WsMessageBase.IOWorldBossWorldRank(101, aLegionId, legionBean1.getName(), damageSum, legionBean1.getLevel(), 0, i));
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private IOGeneralLegion buildBsetGeneral(ChapterBattleTemplate chapterBattleTemplate) {
        return new IOGeneralLegion(chapterBattleTemplate.getGsid(), chapterBattleTemplate.getLevel(),
                chapterBattleTemplate.getHpcover(), chapterBattleTemplate.getPclass(), chapterBattleTemplate.getExhp().longValue(),
                chapterBattleTemplate.getExatk().intValue());
    }

}
