package game.module.exped.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.exped.bean.ExpedBean;
import game.module.exped.dao.ExpedCache;
import game.module.exped.dao.ExpedDaoHelper;
import game.module.exped.dao.ExpedStatueTemplateCache;
import game.module.exped.dao.ExpeditionCostTemplateCache;
import game.module.exped.logic.ExpedConstants;
import game.module.exped.logic.ExpedManager;
import game.module.item.logic.ItemConstants;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.template.ExpedStatueTemplate;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.*;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SExpedStatue.id, accessLimit = 200)
public class ExpedStatueProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ExpedStatueProcessor.class);

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
        WsMessageBattle.C2SExpedStatue reqmsg = WsMessageBattle.C2SExpedStatue.parse(request);
        logger.info("exped statue!player={},req={}", playerId, reqmsg);
        int action_type = reqmsg.type;
        long general_uuid = reqmsg.general_uuid;
        if (action_type < 1 || action_type > 3) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CExpedStatue.msgCode, 1437);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //数量是否够
        ExpedBean expedBean = ExpedCache.getInstance().getExped(playerId);
        switch (action_type) {
            case 1:
                int wishCount = PlayerManager.getInstance().getOtherCount(playerId, ExpedConstants.EXPED_USE_COUNT_WISH_MARK);
                List<Integer> costList = ExpeditionCostTemplateCache.getInstance().getTowerBattleById(action_type);
                if(wishCount >= costList.size()){
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CExpedStatue.msgCode, 1441);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                int costNum = costList.get(wishCount);
                if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), ItemConstants.JI_TAN_COIN, costNum)) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CExpedStatue.msgCode, 1439);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                break;
            case 2:
                int treatCount = PlayerManager.getInstance().getOtherCount(playerId, ExpedConstants.EXPED_USE_COUNT_TREAT_MARK);
                costList = ExpeditionCostTemplateCache.getInstance().getTowerBattleById(action_type);
                if(treatCount >= costList.size()){
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CExpedStatue.msgCode, 1441);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                costNum = costList.get(treatCount);
                if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), ItemConstants.JI_TAN_COIN, costNum)) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CExpedStatue.msgCode, 1439);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                //check general exist
                if (expedBean == null || expedBean.getMyHp() == null || !expedBean.getMyHp().containsKey(general_uuid)) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CExpedStatue.msgCode, 1440);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                //check general die
                Integer generalHpPercent = expedBean.getMyHp().get(general_uuid);
                if (generalHpPercent <= 0) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CExpedStatue.msgCode, 1441);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                break;
            case 3:
                int reviveCount = PlayerManager.getInstance().getOtherCount(playerId, ExpedConstants.EXPED_USE_COUNT_REVIVE_MARK);
                costList = ExpeditionCostTemplateCache.getInstance().getTowerBattleById(action_type);
                if(reviveCount >= costList.size()){
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CExpedStatue.msgCode, 1441);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                costNum = costList.get(reviveCount);
                if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), ItemConstants.JI_TAN_COIN, costNum)) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CExpedStatue.msgCode, 1439);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                //check general exist
                if (expedBean == null || expedBean.getMyHp() == null || !expedBean.getMyHp().containsKey(general_uuid)) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CExpedStatue.msgCode, 1440);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
                break;
        }
        //do
        switch (action_type) {
            case 1:
                int wishCount = PlayerManager.getInstance().getOtherCount(playerId, ExpedConstants.EXPED_USE_COUNT_WISH_MARK);
                List<Integer> costList = ExpeditionCostTemplateCache.getInstance().getTowerBattleById(action_type);
                int costNum = costList.get(wishCount);
                //cost
                AwardUtils.changeRes(playingRole, ExpedConstants.EXPED_USE_COUNT_WISH_MARK, 1, LogConstants.MODULE_EXPED);
                AwardUtils.changeRes(playingRole, ItemConstants.JI_TAN_COIN, -costNum, LogConstants.MODULE_EXPED);
                //save bean
                if(expedBean == null){
                    expedBean = ExpedManager.getInstance().createExped(playerId);
                }
                //rand index
                int randIndex = RandomUtils.nextInt(0, 3);
                Integer existWishLevel = expedBean.getWishCount().get(randIndex);
                ExpedStatueTemplate expedStatueTemplate = ExpedStatueTemplateCache.getInstance().getExpedStatueTemplateById(randIndex + 1);
                if (existWishLevel < expedStatueTemplate.getMAX()) {
                    expedBean.getWishCount().set(randIndex, existWishLevel + 1);
                }
                if(expedBean.getId() == null){
                    ExpedDaoHelper.asyncInsertExped(expedBean);
                    ExpedCache.getInstance().addExped(expedBean);
                }else {
                    ExpedDaoHelper.asyncUpdateExped(expedBean);
                }
                break;
            case 2:
                int treatCount = PlayerManager.getInstance().getOtherCount(playerId, ExpedConstants.EXPED_USE_COUNT_TREAT_MARK);
                costList = ExpeditionCostTemplateCache.getInstance().getTowerBattleById(action_type);
                costNum = costList.get(treatCount);
                //cost
                AwardUtils.changeRes(playingRole, ExpedConstants.EXPED_USE_COUNT_TREAT_MARK, 1, LogConstants.MODULE_EXPED);
                AwardUtils.changeRes(playingRole, ItemConstants.JI_TAN_COIN, -costNum, LogConstants.MODULE_EXPED);
                //save bean
                expedBean.getMyHp().remove(general_uuid);
                ExpedDaoHelper.asyncUpdateExped(expedBean);
                break;
            case 3:
                int reviveCount = PlayerManager.getInstance().getOtherCount(playerId, ExpedConstants.EXPED_USE_COUNT_REVIVE_MARK);
                costList = ExpeditionCostTemplateCache.getInstance().getTowerBattleById(action_type);
                costNum = costList.get(reviveCount);
                //cost
                AwardUtils.changeRes(playingRole, ExpedConstants.EXPED_USE_COUNT_REVIVE_MARK, 1, LogConstants.MODULE_EXPED);
                AwardUtils.changeRes(playingRole, ItemConstants.JI_TAN_COIN, -costNum, LogConstants.MODULE_EXPED);
                //save bean
                expedBean.getMyHp().remove(general_uuid);
                ExpedDaoHelper.asyncUpdateExped(expedBean);
                break;
        }
        //ret
        WsMessageBattle.S2CExpedStatue respmsg = new WsMessageBattle.S2CExpedStatue();
        if (expedBean != null) {
            respmsg.wish = expedBean.getWishCount();
        } else {
            respmsg.wish = Arrays.asList(0, 0, 0);
        }
        if (expedBean != null && expedBean.getMyHp() != null) {
            respmsg.hp = expedBean.getMyHp();
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
