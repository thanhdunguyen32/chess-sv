package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.manor.dao.OfficialItemsTemplateCache;
import game.module.manor.dao.OfficialTemplateCache;
import game.module.mission.logic.MissionManager;
import game.module.template.OfficialItemsTemplate;
import game.module.template.OfficialTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerHideCache;
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
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageManor.C2SOfficialPromo.id, accessLimit = 100)
public class OfficialPromoProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OfficialPromoProcessor.class);

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
        logger.info("official promo,player={}", playerId);
        //is all finish
        Map<Integer, PlayerProp> playerHidden = PlayerHideCache.getInstance().getPlayerHidden(playerId);
        int myOfficial = 0;
        if(playerHidden.containsKey(GameConfig.PLAYER.OFFICIAL)){
            myOfficial = playerHidden.get(GameConfig.PLAYER.OFFICIAL).getCount();
        }
        List<OfficialTemplate> officialTemplateList = OfficialTemplateCache.getInstance().getOfficialTemplates(myOfficial);
        for (int item_index = 0; item_index < officialTemplateList.size(); item_index++) {
            OfficialTemplate officialTemplate = officialTemplateList.get(item_index);
            //check is finish
            boolean isFinish = MissionManager.getInstance().checkMissionFinish(playingRole.getPlayerBean(), officialTemplate.getPMARK(),
                    officialTemplate.getCNUM());
            if (!isFinish) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2COfficialPromo.msgCode, 1349);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        OfficialItemsTemplate officialItemsTemplate = OfficialItemsTemplateCache.getInstance().getOfficialItemsTemplate(myOfficial);
        List<WsMessageBase.IORewardItem> rewardItemList = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : officialItemsTemplate.getREWARD()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_MANOR);
            rewardItemList.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //add official
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.OFFICIAL, 1, LogConstants.MODULE_MANOR);
        //ret
        WsMessageManor.S2COfficialPromo respmsg = new WsMessageManor.S2COfficialPromo();
        respmsg.rewards = rewardItemList;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
