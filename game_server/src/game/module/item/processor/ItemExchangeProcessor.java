package game.module.item.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.dao.SevenLoginTemplateCache;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.template.RewardTemplateSimple;
import game.module.template.SevenLoginTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBag;
import ws.WsMessageBase;
import ws.WsMessageHall.S2CErrorCode;

import java.util.ArrayList;
import java.util.List;

@MsgCodeAnn(msgcode = WsMessageBag.C2SItemExchange.id, accessLimit = 200)
public class ItemExchangeProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ItemExchangeProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageBag.C2SItemExchange reqmsg = WsMessageBag.C2SItemExchange.parse(request);
        int playerId = playingRole.getId();
        logger.info("item exchange,playerId={},req={}", playerId, reqmsg);
        String EXID = reqmsg.EXID;
        int itemCount = reqmsg.count;
        if (itemCount < 0) {
            S2CErrorCode retMsg = new S2CErrorCode(WsMessageBag.S2CItemExchange.msgCode, 130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        if (EXID.startsWith("sevenlogin_")) {
            doSevenLogin(playingRole, EXID);
        }else if(EXID.endsWith("kingpvp_fight_cost")){
            AwardUtils.changeRes(playingRole, 90335, 1, LogConstants.MODULE_BAG);
            //ret
            WsMessageBag.S2CItemExchange respmsg = new WsMessageBag.S2CItemExchange();
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        }
    }

    private void doSevenLogin(PlayingRole playingRole, String EXID) {
        String timeMark_str = EXID.substring(11);
        String[] day_strs = StringUtils.split(timeMark_str, "_");
        int dayIndex = Integer.parseInt(day_strs[0]);
        //是否已经领取
        SevenLoginTemplate.SevenLoginRewardTemplate sevenLoginRewardTemplate = SevenLoginTemplateCache.getInstance().getSevenLoginRewardTemplates(EXID);
        if (sevenLoginRewardTemplate == null) {
            S2CErrorCode retMsg = new S2CErrorCode(WsMessageBag.S2CItemExchange.msgCode, 130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        if (ItemManager.getInstance().getCount(playingRole.getPlayerBean(), sevenLoginRewardTemplate.getGETSIGN()) > 0) {
            S2CErrorCode retMsg = new S2CErrorCode(WsMessageBag.S2CItemExchange.msgCode, 1510);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //天数是否到
        int loginSign = SevenLoginTemplateCache.getInstance().getLoginSign();
        int signInCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), loginSign);
        if (dayIndex > signInCount - 1) {
            S2CErrorCode retMsg = new S2CErrorCode(WsMessageBag.S2CItemExchange.msgCode, 1511);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //do
        AwardUtils.changeRes(playingRole, sevenLoginRewardTemplate.getGETSIGN(), 1, LogConstants.MODULE_SIGN);
        //reward
        List<WsMessageBase.RewardInfo> rewardItems = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : sevenLoginRewardTemplate.getREWARD()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_SIGN);
            rewardItems.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //ret
        WsMessageBag.S2CItemExchange respmsg = new WsMessageBag.S2CItemExchange();
        respmsg.reward = rewardItems;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
