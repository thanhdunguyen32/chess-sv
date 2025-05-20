package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.legion.bean.DbLegionBoss;
import game.module.legion.bean.LegionBean;
import game.module.legion.dao.LegionBossTemplateCache;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.LegionDaoHelper;
import game.module.legion.logic.LegionConstants;
import game.module.legion.logic.LegionManager;
import game.module.log.constants.LogConstants;
import game.module.template.LegionBossTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionBossFarm.id, accessLimit = 200)
public class LegionBossFarmProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionBossFarmProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageLegion.C2SLegionBossFarm reqmsg = WsMessageLegion.C2SLegionBossFarm.parse(request);
        logger.info("legion boss battle end!player={},req={}", playerId, reqmsg);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionBossFarm.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //count max
        List<LegionBossTemplate.LegionBossCost> legionBossCosts = LegionBossTemplateCache.getInstance().getCosts();
        int attackCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), LegionConstants.LEGION_BOSS_ATTACK_MARK);
        if (attackCount >= legionBossCosts.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionBossFarm.msgCode, 1543);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        LegionBossTemplate.LegionBossCost legionBossCost = legionBossCosts.get(attackCount);
        if (legionBossCost.getCOST() > 0 && !ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), GameConfig.PLAYER.YB,
                legionBossCost.getCOST())) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionBossFarm.msgCode, 1544);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //record exist
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        DbLegionBoss legionBoss = legionBean.getLegionBoss();
        if (legionBoss == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionBossFarm.msgCode, 1545);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        int chapter_index = legionBoss.getChapterIndex();
        List<DbLegionBoss.LegionBossDamage> records = legionBoss.getRecords();
        Map<Integer, Long> lastDamageMap = records.get(chapter_index).getLastDamageMap();
        if (lastDamageMap == null || !lastDamageMap.containsKey(playerId)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionBossFarm.msgCode, 1545);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //reward
        List<WsMessageBase.IORewardItem> dropItems = new ArrayList<>();
        LegionBossTemplate.LegionBoss1 bossTemplate = LegionBossTemplateCache.getInstance().getBossTemplate(chapter_index);
        for (RewardTemplateSimple rewardTemplateSimple : bossTemplate.getREWARD()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_LEGION);
            dropItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //hurt
        long hurmSum = lastDamageMap.get(playerId);
        synchronized (legionBean) {
            Map<Integer, Long> damageList = records.get(chapter_index).getDamageList();
            if (damageList.containsKey(playerId)) {
                damageList.put(playerId, damageList.get(playerId) + hurmSum);
            } else {
                damageList.put(playerId, hurmSum);
            }
            //save hp
            long nowhp = legionBoss.getNowhp() - hurmSum;
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
        //mark
        AwardUtils.changeRes(playingRole, LegionConstants.LEGION_BOSS_ATTACK_MARK, 1, LogConstants.MODULE_LEGION);
        //cost
        if (legionBossCost.getCOST() > 0) {
            AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -legionBossCost.getCOST(), LogConstants.MODULE_LEGION);
        }
        //ret
        WsMessageLegion.S2CLegionBossFarm respmsg = new WsMessageLegion.S2CLegionBossFarm();
        respmsg.rewards = dropItems;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
