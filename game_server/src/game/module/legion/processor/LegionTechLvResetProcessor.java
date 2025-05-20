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
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionTechReset.id, accessLimit = 200)
public class LegionTechLvResetProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionTechLvResetProcessor.class);

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
        WsMessageLegion.C2SLegionTechReset reqmsg = WsMessageLegion.C2SLegionTechReset.parse(request);
        logger.info("legion sign!player={},req={}", playerId, reqmsg);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionTechReset.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //
        int occu = reqmsg.occu;
        if (occu < 1 || occu > 5) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionTechReset.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //has tech
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        LegionPlayer legionPlayer = legionBean.getDbLegionPlayers().getMembers().get(playerId);
        if (legionPlayer.getTech() == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionTechReset.msgCode, 1530);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //tech exist
        boolean hasTech = false;
        for (int techId : legionPlayer.getTech().keySet()) {
            if (techId / 100 == occu) {
                hasTech = true;
                break;
            }
        }
        if (!hasTech) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionTechReset.msgCode, 1530);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        int doCountMarkGsid = 90349;
        int doCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), doCountMarkGsid);
        if (doCount > 0 && !ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), GameConfig.PLAYER.YB, 1000)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionTechReset.msgCode, 1531);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //mark
        AwardUtils.changeRes(playingRole, doCountMarkGsid, 1, LogConstants.MODULE_LEGION);
        //cost
        if (doCount > 0) {
            AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -1000, LogConstants.MODULE_LEGION);
        }
        //update legion
        Map<Integer, Integer> backItemMap = new HashMap<>();
        Iterator<Map.Entry<Integer, Integer>> ite = legionPlayer.getTech().entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<Integer, Integer> techPair = ite.next();
            int techId = techPair.getKey();
            int techLevel = techPair.getValue();
            if (techId / 100 != occu) {
                continue;
            }
            LegionTechTemplate legionTechTemplate = LegionTechTemplateCache.getInstance().getLegionTechTemplate(techId);
            for (int i = 0; i < techLevel; i++) {
                Integer costGold = legionTechTemplate.getCOST().getGoldCoin().get(i);
                Integer costLegionCoin = legionTechTemplate.getCOST().getLegionCoin().get(i);
                putBackItems(backItemMap, GameConfig.PLAYER.GOLD, costGold);
                putBackItems(backItemMap, LegionConstants.LEGION_COIN, costLegionCoin);
            }
            ite.remove();
        }
        LegionDaoHelper.asyncUpdateLegionBean(legionBean);
        //ret
        WsMessageLegion.S2CLegionTechReset respmsg = new WsMessageLegion.S2CLegionTechReset();
        respmsg.items = new ArrayList<>(backItemMap.size());
        for (Map.Entry<Integer, Integer> aEntry : backItemMap.entrySet()) {
            respmsg.items.add(new WsMessageBase.IORewardItem(aEntry.getKey(), aEntry.getValue()));
            AwardUtils.changeRes(playingRole, aEntry.getKey(), aEntry.getValue(), LogConstants.MODULE_LEGION);
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void putBackItems(Map<Integer, Integer> backItemMap, int gsid, Integer count) {
        if (backItemMap.containsKey(gsid)) {
            backItemMap.put(gsid, backItemMap.get(gsid) + count);
        } else {
            backItemMap.put(gsid, count);
        }
    }

}
