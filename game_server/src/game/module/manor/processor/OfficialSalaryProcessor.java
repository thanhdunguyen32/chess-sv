package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.manor.dao.OfficialItemsTemplateCache;
import game.module.manor.logic.OfficialConstants;
import game.module.template.OfficialItemsTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerHideCache;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageHero;
import ws.WsMessageManor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageManor.C2SOfficialSalary.id, accessLimit = 100)
public class OfficialSalaryProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OfficialSalaryProcessor.class);

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
        logger.info("official salary,player={}", playerId);
        //is get
        int getCount = PlayerManager.getInstance().getOtherCount(playerId, OfficialConstants.OFFICIAL_SALARY_GET_MARK);
        if (getCount > 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2COfficialSalary.msgCode, 1346);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //has official
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), GameConfig.PLAYER.OFFICIAL, 1)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2COfficialSalary.msgCode, 1347);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        Map<Integer, PlayerProp> playerHidden = PlayerHideCache.getInstance().getPlayerHidden(playerId);
        int myOfficial = playerHidden.get(GameConfig.PLAYER.OFFICIAL).getCount();
        OfficialItemsTemplate officialItemsTemplate = OfficialItemsTemplateCache.getInstance().getOfficialItemsTemplate(myOfficial - 1);
        List<WsMessageBase.IORewardItem> rewardItemList = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : officialItemsTemplate.getSALARY()) {
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_MANOR);
            rewardItemList.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //cost
        AwardUtils.changeRes(playingRole, OfficialConstants.OFFICIAL_SALARY_GET_MARK, 1, LogConstants.MODULE_MANOR);
        //ret
        WsMessageManor.S2COfficialSalary respmsg = new WsMessageManor.S2COfficialSalary();
        respmsg.rewards = rewardItemList;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
