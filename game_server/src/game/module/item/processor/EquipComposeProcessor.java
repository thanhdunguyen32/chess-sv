package game.module.item.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.dao.EquipCompTemplateCache;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.mission.logic.MissionManager;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBag;
import ws.WsMessageHall;

import java.util.List;

@MsgCodeAnn(msgcode = WsMessageBag.C2SEquipComp.id, accessLimit = 250)
public class EquipComposeProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EquipComposeProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageBag.C2SEquipComp reqmsg = WsMessageBag.C2SEquipComp.parse(request);
        int playerId = playingRole.getId();
        logger.info("use item,playerId={},req={}", playerId, reqmsg);
        int equip_id = reqmsg.equip_id;
        int num = reqmsg.count;
        //数量核对
        if(num <= 0){
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBag.S2CEquipComp.msgCode, 1301);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        List<RewardTemplateSimple> equipCompTemplate = EquipCompTemplateCache.getInstance().getEquipCompTemplate(equip_id);
        if (equipCompTemplate == null) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBag.S2CEquipComp.msgCode, 1301);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        // 材料是否够
        for (RewardTemplateSimple rewardTemplateSimple : equipCompTemplate) {
            Integer gsid = rewardTemplateSimple.getGSID();
            Integer count = rewardTemplateSimple.getCOUNT();
            if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), gsid, count * num)) {
                WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageBag.S2CEquipComp.msgCode, 1301);
                playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        //award
        AwardUtils.changeRes(playingRole, equip_id, num, LogConstants.MODULE_BAG);
        //change mission progress
        MissionManager.getInstance().equipChangeProgress(playingRole, equip_id, num);
        //cost
        for (RewardTemplateSimple rewardTemplateSimple : equipCompTemplate) {
            Integer gsid = rewardTemplateSimple.getGSID();
            Integer count = rewardTemplateSimple.getCOUNT();
            AwardUtils.changeRes(playingRole, gsid, -num * count, LogConstants.MODULE_BAG);
        }
        //update mission progress
        AwardUtils.changeRes(playingRole, MissionConstants.EQUIP_COMPOSE_PMARK, num, LogConstants.MODULE_BAG);
        //ret
        WsMessageBag.S2CEquipComp respmsg = new WsMessageBag.S2CEquipComp(equip_id, num);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
