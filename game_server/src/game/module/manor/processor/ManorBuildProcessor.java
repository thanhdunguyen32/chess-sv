package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.manor.bean.ManorBean;
import game.module.manor.dao.ManorCache;
import game.module.manor.dao.ManorDaoHelper;
import game.module.manor.dao.ManorTemplateCache;
import game.module.template.ManorTemplate;
import game.module.template.RewardTemplateSimple;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageManor;

import java.util.Date;
import java.util.HashMap;

@MsgCodeAnn(msgcode = WsMessageManor.C2SManorBuild.id, accessLimit = 100)
public class ManorBuildProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ManorBuildProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageManor.C2SManorBuild reqmsg = WsMessageManor.C2SManorBuild.parse(request);
        logger.info("manor build,player={},req={}", playerId, reqmsg);
        int pos = reqmsg.pos;
        int bid = reqmsg.bid;
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        ManorBean.DbManorBuilding manorBuilding = manorBean.getManorBuilding();
        Integer manorLevel = manorBean.getLevel();
        if (pos >= 0) {
            ManorTemplate.ManorHomeTemplate manorHomeTemplate = ManorTemplateCache.getInstance().getManorHomeTemplate(manorLevel);
            //pos是否正确
            if (!manorHomeTemplate.getPOS().containsKey(pos)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CManorBuild.msgCode, 1352);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            //is max level
            if (manorBuilding != null && manorBuilding.getBuildings() != null && manorBuilding.getBuildings().containsKey(pos) &&
                    manorBuilding.getBuildings().get(pos).getLevel() >= manorHomeTemplate.getUPMAX()) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CManorBuild.msgCode, 1354);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            if (manorBuilding != null && manorBuilding.getBuildings() != null && manorBuilding.getBuildings().containsKey(pos)) {
                bid = manorBuilding.getBuildings().get(pos).getId();
            }
        } else {
            bid = 1;
        }
        //cost
        ManorTemplate.ManorUpTemplate manorUpTemplate = ManorTemplateCache.getInstance().getUpTemplate(bid);
        int currentLevel = 0;
        if (bid == 1) {
            currentLevel = manorLevel;
        } else if (manorBuilding != null && manorBuilding.getBuildings() != null && manorBuilding.getBuildings().containsKey(pos)) {
            currentLevel = manorBuilding.getBuildings().get(pos).getLevel();
        }
        ManorTemplate.ManorUpCostTemplate levelUpCostTemplate = manorUpTemplate.getINFO().get(currentLevel);
        for (RewardTemplateSimple rewardTemplateSimple : levelUpCostTemplate.getCOST()) {
            if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT())) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CManorBuild.msgCode, 1355);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        if (pos == -1) {//府邸升级
            manorBean.setLevel(manorBean.getLevel() + 1);
        } else {//建筑升级
            if (manorBuilding == null || manorBuilding.getBuildings() == null) {
                manorBuilding = new ManorBean.DbManorBuilding();
                manorBuilding.setBuildings(new HashMap<>());
                manorBean.setManorBuilding(manorBuilding);
            }
            ManorBean.DbManorBuilding1 manorBuilding1 = manorBuilding.getBuildings().get(pos);
            Date now = new Date();
            if (manorBuilding1 == null) {
                manorBuilding1 = new ManorBean.DbManorBuilding1();
                manorBuilding1.setId(bid);
                manorBuilding1.setLevel(1);
                manorBuilding1.setLastGain(now.getTime());
                manorBuilding1.setPos(pos);
                ManorTemplate.ManorMaxTemplate maxTemplate = ManorTemplateCache.getInstance().getMaxTemplate(bid);
                manorBuilding1.setType(maxTemplate.getTYPE());
                manorBuilding1.setRid(SessionManager.getInstance().generateSessionId());
                manorBuilding.getBuildings().put(pos, manorBuilding1);
            } else {
                manorBuilding1.setLevel(manorBuilding1.getLevel() + 1);
            }
        }
        ManorDaoHelper.asyncUpdateManor(manorBean);
        //cost
        for (RewardTemplateSimple rewardTemplateSimple : levelUpCostTemplate.getCOST()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), -rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_MANOR);
        }
        //ret
        WsMessageManor.S2CManorBuild respmsg = new WsMessageManor.S2CManorBuild();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
