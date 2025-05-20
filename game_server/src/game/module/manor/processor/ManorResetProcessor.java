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
import game.module.template.RewardTemplateSimple;
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

@MsgCodeAnn(msgcode = WsMessageManor.C2SManorReset.id, accessLimit = 100)
public class ManorResetProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ManorResetProcessor.class);

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
        WsMessageManor.C2SManorReset reqmsg = WsMessageManor.C2SManorReset.parse(request);
        logger.info("manor build,player={},req={}", playerId, reqmsg);
        int pos = reqmsg.pos;
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        Integer manorLevel = manorBean.getLevel();
        ManorTemplate.ManorHomeTemplate manorHomeTemplate = ManorTemplateCache.getInstance().getManorHomeTemplate(manorLevel);
        //pos是否正确
        if (!manorHomeTemplate.getPOS().containsKey(pos)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CManorReset.msgCode, 1352);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //building不存在
        ManorBean.DbManorBuilding manorBuilding = manorBean.getManorBuilding();
        if (manorBuilding == null || manorBuilding.getBuildings() == null || !manorBuilding.getBuildings().containsKey(pos)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CManorBuild.msgCode, 1356);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        int bid = manorBuilding.getBuildings().get(pos).getId();
        int currentLevel = manorBuilding.getBuildings().get(pos).getLevel();
        Map<Integer, Integer> costMap = new HashMap<>();
        ManorTemplate.ManorUpTemplate manorUpTemplate = ManorTemplateCache.getInstance().getUpTemplate(bid);
        for (int i = 0; i < currentLevel; i++) {
            ManorTemplate.ManorUpCostTemplate levelUpCostTemplate = manorUpTemplate.getINFO().get(i);
            for (RewardTemplateSimple rewardTemplateSimple : levelUpCostTemplate.getCOST()) {
                if (costMap.containsKey(rewardTemplateSimple.getGSID())) {
                    costMap.put(rewardTemplateSimple.getGSID(), costMap.get(rewardTemplateSimple.getGSID()) + rewardTemplateSimple.getCOUNT());
                } else {
                    costMap.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                }
            }
        }
        //do
        List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
        for (Map.Entry<Integer, Integer> aEntry : costMap.entrySet()) {
            AwardUtils.changeRes(playingRole, aEntry.getKey(), aEntry.getValue() / 2, LogConstants.MODULE_MANOR);
            rewardItems.add(new WsMessageBase.IORewardItem(aEntry.getKey(), aEntry.getValue() / 2));
        }
        //save bean
        manorBuilding.getBuildings().remove(pos);
        ManorDaoHelper.asyncUpdateManor(manorBean);
        //ret
        WsMessageManor.S2CManorReset respmsg = new WsMessageManor.S2CManorReset();
        respmsg.items = rewardItems;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
