package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.manor.bean.ManorBean;
import game.module.manor.dao.ManorCache;
import game.module.manor.dao.ManorDaoHelper;
import game.module.manor.dao.ManorTemplateCache;
import game.module.template.ManorTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageManor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageManor.C2SManorReward.id, accessLimit = 100)
public class ManorRewardProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ManorRewardProcessor.class);

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
        WsMessageManor.C2SManorReward reqmsg = WsMessageManor.C2SManorReward.parse(request);
        logger.info("manor reward,player={},req={}", playerId, reqmsg);
        int pos = reqmsg.pos;
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        if (pos >= 0) {
            //building不存在
            ManorBean.DbManorBuilding manorBuilding = manorBean.getManorBuilding();
            if (manorBuilding == null || manorBuilding.getBuildings() == null || !manorBuilding.getBuildings().containsKey(pos)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CManorBuild.msgCode, 1356);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            //不是资源建筑
            int bid = manorBuilding.getBuildings().get(pos).getId();
            int currentLevel = manorBuilding.getBuildings().get(pos).getLevel();
            ManorTemplate.ManorUpTemplate manorUpTemplate = ManorTemplateCache.getInstance().getUpTemplate(bid);
            List<ManorTemplate.ManorResTemplate> manorResTemplates = manorUpTemplate.getINFO().get(currentLevel).getRES();
            if (manorResTemplates == null) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CManorBuild.msgCode, 1357);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        //钱庄加成
        int resAddon = 0;
        for (ManorBean.DbManorBuilding1 dbManorBuilding1 : manorBean.getManorBuilding().getBuildings().values()) {
            Integer buildingId = dbManorBuilding1.getId();
            Integer buildingLevel = dbManorBuilding1.getLevel();
            ManorTemplate.ManorUpTemplate upTemplate = ManorTemplateCache.getInstance().getUpTemplate(buildingId);
            List<ManorTemplate.IdNumTemplate> att = upTemplate.getINFO().get(buildingLevel).getATT();
            if (att != null) {
                for (ManorTemplate.IdNumTemplate idNumTemplate : att) {
                    if (idNumTemplate.getID().equals("mout")) {
                        resAddon += idNumTemplate.getNUM();
                    }
                }
            }
        }
        List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
        if (pos >= 0) {
            ManorBean.DbManorBuilding1 manorBuilding1 = manorBean.getManorBuilding().getBuildings().get(pos);
            int bid = manorBuilding1.getId();
            int currentLevel = manorBuilding1.getLevel();
            ManorTemplate.ManorUpTemplate manorUpTemplate = ManorTemplateCache.getInstance().getUpTemplate(bid);
            List<ManorTemplate.ManorResTemplate> manorResTemplates = manorUpTemplate.getINFO().get(currentLevel).getRES();
            //reward
            for (ManorTemplate.ManorResTemplate manorResTemplate : manorResTemplates) {
                int rewardCount =
                        (int) ((System.currentTimeMillis() - manorBuilding1.getLastGain()) / 1000 * manorResTemplate.getCOUNT() * (1 + resAddon / 1000f) / manorResTemplate.getTIME());
                if (rewardCount >= manorResTemplate.getMAX()) {
                    rewardCount = manorResTemplate.getMAX();
                }
                if (rewardCount > 0) {
                    AwardUtils.changeRes(playingRole, manorResTemplate.getID(), rewardCount, LogConstants.MODULE_MANOR);
                    rewardItems.add(new WsMessageBase.IORewardItem(manorResTemplate.getID(), rewardCount));
                    manorBuilding1.setLastGain(System.currentTimeMillis());
                }
            }
        } else if (manorBean.getManorBuilding() != null && manorBean.getManorBuilding().getBuildings() != null) {
            Map<Integer, Integer> rewardMap = new HashMap<>();
            for (ManorBean.DbManorBuilding1 manorBuilding1 : manorBean.getManorBuilding().getBuildings().values()) {
                int bid = manorBuilding1.getId();
                int currentLevel = manorBuilding1.getLevel();
                ManorTemplate.ManorUpTemplate manorUpTemplate = ManorTemplateCache.getInstance().getUpTemplate(bid);
                List<ManorTemplate.ManorResTemplate> manorResTemplates = manorUpTemplate.getINFO().get(currentLevel).getRES();
                if (manorResTemplates != null) {
                    //reward
                    for (ManorTemplate.ManorResTemplate manorResTemplate : manorResTemplates) {
                        int rewardCount =
                                (int) ((System.currentTimeMillis() - manorBuilding1.getLastGain()) / 1000 * manorResTemplate.getCOUNT() * (1 + resAddon / 1000f) / manorResTemplate.getTIME());
                        if (rewardCount >= manorResTemplate.getMAX()) {
                            rewardCount = manorResTemplate.getMAX();
                        }
                        if (rewardCount > 0) {
                            if (rewardMap.containsKey(manorResTemplate.getID())) {
                                rewardMap.put(manorResTemplate.getID(), rewardMap.get(manorResTemplate.getID()) + rewardCount);
                            } else {
                                rewardMap.put(manorResTemplate.getID(), rewardCount);
                            }
                            manorBuilding1.setLastGain(System.currentTimeMillis());
                        }
                    }
                }
            }
            for (Map.Entry<Integer, Integer> aEntry : rewardMap.entrySet()) {
                AwardUtils.changeRes(playingRole, aEntry.getKey(), aEntry.getValue(), LogConstants.MODULE_MANOR);
                rewardItems.add(new WsMessageBase.IORewardItem(aEntry.getKey(), aEntry.getValue()));
            }
        }
        //save bean
        ManorDaoHelper.asyncUpdateManor(manorBean);
        //ret
        WsMessageManor.S2CManorReward respmsg = new WsMessageManor.S2CManorReward();
        respmsg.items = rewardItems;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
