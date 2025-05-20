package game.module.spin.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.spin.bean.SpinBean;
import game.module.spin.dao.SpinTemplateCache;
import game.module.spin.dao.SpinCache;
import game.module.spin.dao.SpinDaoHelper;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageSpin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageSpin.C2SSpinBoxOpen.id, accessLimit = 200)
public class SpinBoxOpenProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SpinBoxOpenProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageSpin.C2SSpinBoxOpen reqMsg = WsMessageSpin.C2SSpinBoxOpen.parse(request);
        final int buy_type = reqMsg.type;
        int box_index = reqMsg.box_index;
        int playerId = playingRole.getId();
        logger.info("spin box open,player={},req={}", playerId, reqMsg);
        //params check
        SpinBean spinBean = SpinCache.getInstance().getSpinBean(playerId);
        if (spinBean == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinBoxOpen.msgCode, 1362);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        if (buy_type != 1 && buy_type != 2) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinBoxOpen.msgCode, 1360);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is score enough
        Map<String, Object> spinTemplate = SpinTemplateCache.getInstance().getSpinTemplate(buy_type);
        Map<String, Object> spin1Config = (Map) spinTemplate.get("1");
        List<Integer> drops = (List<Integer>) spin1Config.get("DROP");
        int scoreTemplateId = drops.get(0);
        //
        List<Map<String, Object>> scoreShopList = (List<Map<String, Object>>) spinTemplate.get("SCORE");
        if (box_index < 0 || box_index >= scoreShopList.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinBoxOpen.msgCode, 1363);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Map<String, Object> scoreLevel1Config = scoreShopList.get(box_index);
        int needCount = (Integer) scoreLevel1Config.get("COUNT");
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), scoreTemplateId, needCount)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinBoxOpen.msgCode, 1364);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //already buy
        int signId = (Integer) scoreLevel1Config.get("SIGN");
        Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
        if (playerOthers.containsKey(signId) && playerOthers.get(signId).getCount()>0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageSpin.S2CSpinBoxOpen.msgCode, 1365);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //save buy info
        AwardUtils.changeRes(playingRole,signId,1,LogConstants.SPIN);
        //award
        WsMessageSpin.S2CSpinBoxOpen respmsg = new WsMessageSpin.S2CSpinBoxOpen();
        respmsg.box_index = box_index;
        respmsg.type = buy_type;
        respmsg.rewards = new ArrayList<>();
        List<Map<String, Integer>> rewardMap = (List<Map<String, Integer>>) scoreLevel1Config.get("REWARD");
        for (Map<String, Integer> apair : rewardMap) {
            Integer gsid = apair.get("GSID");
            Integer gsCount = apair.get("COUNT");
            AwardUtils.changeRes(playingRole, gsid, gsCount, LogConstants.SPIN);
            respmsg.rewards.add(new WsMessageBase.IOSpinItem(gsid, gsCount, 0));
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }
}
