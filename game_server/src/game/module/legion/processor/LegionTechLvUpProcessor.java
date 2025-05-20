package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.LegionDaoHelper;
import game.module.legion.dao.LegionTechTemplateCache;
import game.module.legion.logic.LegionConstants;
import game.module.legion.logic.LegionManager;
import game.module.log.constants.LogConstants;
import game.module.template.LegionTechTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionTeckLv.id, accessLimit = 200)
public class LegionTechLvUpProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionTechLvUpProcessor.class);

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
        WsMessageLegion.C2SLegionTeckLv reqmsg = WsMessageLegion.C2SLegionTeckLv.parse(request);
        logger.info("legion tech up!player={},req={}", playerId, reqmsg);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionTeckLv.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //tech exist
        int tech_id = reqmsg.tech_id;
        if (!LegionTechTemplateCache.getInstance().containsTechId(tech_id)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionTeckLv.msgCode, 1526);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //条件是否满足
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        LegionPlayer legionPlayer = legionBean.getDbLegionPlayers().getMembers().get(playerId);
        LegionTechTemplate legionTechTemplate = LegionTechTemplateCache.getInstance().getLegionTechTemplate(tech_id);
        if (legionTechTemplate.getPRE() != null) {
            Integer condTechId = legionTechTemplate.getPRE().getID();
            Integer condLevel = legionTechTemplate.getPRE().getLV();
            if (legionPlayer.getTech() == null || !legionPlayer.getTech().containsKey(condTechId) || legionPlayer.getTech().get(condTechId) < condLevel) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionTeckLv.msgCode, 1527);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //材料
        int currentLevel = 0;
        if (legionPlayer.getTech() != null && legionPlayer.getTech().containsKey(tech_id)) {
            currentLevel = legionPlayer.getTech().get(tech_id);
        }
        //max level
        List<Integer> legionCoin = legionTechTemplate.getCOST().getLegionCoin();
        if (currentLevel >= legionCoin.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionTeckLv.msgCode, 1528);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Integer costCoin = legionCoin.get(currentLevel);
        Integer costGold = legionTechTemplate.getCOST().getGoldCoin().get(currentLevel);
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), LegionConstants.LEGION_COIN, costCoin)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionTeckLv.msgCode, 1529);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), GameConfig.PLAYER.GOLD, costGold)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionTeckLv.msgCode, 1529);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        if (legionPlayer.getTech() == null) {
            Map<Integer, Integer> techMap = new HashMap<>();
            legionPlayer.setTech(techMap);
        }
        legionPlayer.getTech().put(tech_id, currentLevel + 1);
        //update bean
        LegionDaoHelper.asyncUpdateLegionBean(legionBean);
        //cost
        AwardUtils.changeRes(playingRole, LegionConstants.LEGION_COIN, -costCoin, LogConstants.MODULE_LEGION);
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, -costGold, LogConstants.MODULE_LEGION);
        //ret
        WsMessageLegion.S2CLegionTeckLv respmsg = new WsMessageLegion.S2CLegionTeckLv();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
