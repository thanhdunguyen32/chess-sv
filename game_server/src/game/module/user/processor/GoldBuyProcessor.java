package game.module.user.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.occtask.logic.OccTaskManager;
import game.module.season.logic.SeasonManager;
import game.module.template.GoldTemplate;
import game.module.template.VipTemplate;
import game.module.user.bean.GoldBuy;
import game.module.user.dao.GoldBuyCache;
import game.module.user.dao.GoldBuyDaoHelper;
import game.module.user.dao.GoldTemplateCache;
import game.module.user.logic.GoldBuyManager;
import game.module.vip.dao.VipTemplateCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@MsgCodeAnn(msgcode = WsMessageHall.C2SGoldBuy.id, accessLimit = 200)
public class GoldBuyProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GoldBuyProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHall.C2SGoldBuy reqMsg = WsMessageHall.C2SGoldBuy.parse(request);
        int buy_type = reqMsg.buy_type;
        int playerId = playingRole.getId();
        logger.info("buy gold,player={},buy_type={}", playerId, buy_type);
        buy_type--;
        //
        List<GoldTemplate.GoldTemplateInfo> goldTemplateInfos = GoldTemplateCache.getInstance().getInfo();
        if (buy_type < 0 || buy_type >= goldTemplateInfos.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CGoldBuy.msgCode, 901);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //时间是否到判断
        GoldBuy goldBuy = GoldBuyCache.getInstance().getGoldBuy(playerId);
        if (goldBuy != null && goldBuy.getBuyTime().get(buy_type) > 0) {//有购买记录
            Date now = new Date();
            long dayPassSeconds = DateUtils.getFragmentInSeconds(now, Calendar.YEAR);
            long oldBuyTime = goldBuy.getBuyTime().get(buy_type);
            Date oldBuyDate = new Date(oldBuyTime);
            long oldBuySeconds = DateUtils.getFragmentInSeconds(oldBuyDate, Calendar.YEAR);
            if (GoldBuyManager.getInstance().isSameTimeZone(oldBuySeconds, dayPassSeconds)) {
                WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CGoldBuy.msgCode, 904);
                playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
                return;
            }
        }
        GoldTemplate.GoldTemplateInfo goldTemplateInfo = goldTemplateInfos.get(buy_type);
        //money够
        Integer cost_money = goldTemplateInfo.getCOST();
        if (playingRole.getPlayerBean().getMoney() < cost_money) {
            WsMessageHall.S2CErrorCode respMsg = new WsMessageHall.S2CErrorCode(WsMessageHall.S2CGoldBuy.msgCode, 903);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        //
        int player_level = playingRole.getPlayerBean().getLevel();
        VipTemplate vipTemplate = VipTemplateCache.getInstance().getVipTemplate(playingRole.getPlayerBean().getVipLevel());
        float vipAddon = vipTemplate.getRIGHT().getGOLD() / 1e3f;
        int seasonAddon = SeasonManager.getInstance().getBuyCoinAddon();
        int sumGold = (int) (((player_level - 1) * goldTemplateInfo.getADD() + goldTemplateInfo.getBASE()) * (1 + vipAddon + seasonAddon / 100f));
        //do
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.GOLD, sumGold, LogConstants.MODULE_BUY_COINS);
        //save
        Date now = new Date();
        if (goldBuy == null) {
            goldBuy = GoldBuyManager.getInstance().createGoldBuy(playerId);
            goldBuy.getBuyTime().set(buy_type, now.getTime());
            GoldBuyDaoHelper.asyncInsertGoldBuy(goldBuy);
            GoldBuyCache.getInstance().addGoldBuy(goldBuy);
        } else {
            goldBuy.getBuyTime().set(buy_type, now.getTime());
            GoldBuyDaoHelper.asyncUpdateGoldBuy(goldBuy);
        }
        //cost
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -cost_money, LogConstants.MODULE_BUY_COINS);
        //change prop
        AwardUtils.changeRes(playingRole, goldTemplateInfo.getMARK(), 1, LogConstants.MODULE_BUY_COINS);
        //update mission progress
        AwardUtils.changeRes(playingRole, MissionConstants.GOLD_BUY_PMARK, 1, LogConstants.MODULE_BUY_COINS);
        AwardUtils.changeRes(playingRole, MissionConstants.MARK_BUY_GOLD, 1, LogConstants.MODULE_BUY_COINS);
        OccTaskManager.getInstance().buyCoinAddOccTaskMark(playingRole);
        //ret
        WsMessageHall.S2CGoldBuy respmsg = new WsMessageHall.S2CGoldBuy();
        respmsg.buy_type = buy_type + 1;
        respmsg.rewards = Collections.singletonList(new WsMessageBase.RewardInfo(GameConfig.PLAYER.GOLD, sumGold));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
