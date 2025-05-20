package game.module.hero.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.bean.GeneralExchange;
import game.module.hero.dao.GeneralCache;
import game.module.hero.dao.GeneralExchangeCache;
import game.module.hero.dao.GeneralExchangeDaoHelper;
import game.module.hero.logic.GeneralDaoHelper;
import game.module.hero.logic.GeneralManager;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageHero.C2SGeneralExchangeConfirm.id, accessLimit = 200)
public class GeneralExchangeConfirmProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GeneralExchangeConfirmProcessor.class);

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
        WsMessageHero.C2SGeneralExchangeConfirm reqmsg = WsMessageHero.C2SGeneralExchangeConfirm.parse(request);
        logger.info("general exchange confirm,player={},req={}", playerId, reqmsg);
        int action_type = reqmsg.action_type;
        //是否存在
        GeneralExchange generalExchange = GeneralExchangeCache.getInstance().getGeneralExchange(playerId);
        if (generalExchange == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralExchangeConfirm.msgCode, 1380);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        GeneralBean generalBean = null;
        if (action_type == 1) {//do
            Long oldGeneralUuid = generalExchange.getOldGeneralUuid();
            Integer newGeneralTemplateId = generalExchange.getNewGeneralTemplateId();
            Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
            if (!heroCache.containsKey(oldGeneralUuid)) {
                GeneralManager.getInstance().removeGeneral(playingRole, oldGeneralUuid);
                //remove
                GeneralExchangeCache.getInstance().removeGeneralExchange(playerId);
                GeneralExchangeDaoHelper.asyncRemoveGeneralExchange(generalExchange.getId());
                //ret
                WsMessageHero.S2CGeneralExchangeConfirm respmsg = new WsMessageHero.S2CGeneralExchangeConfirm();
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            //bean save
            generalBean = heroCache.get(oldGeneralUuid);
            generalBean.setTemplateId(newGeneralTemplateId);
            GeneralManager.getInstance().updatePropertyAndPower(generalBean);
            GeneralDaoHelper.asyncUpdateHero(generalBean);
            //update mission progress
            AwardUtils.changeRes(playingRole, MissionConstants.MARK_GENERAL_EXCHANGE, 1, LogConstants.MODULE_GENERAL_EXCHANGE);
        }
        //remove
        GeneralExchangeCache.getInstance().removeGeneralExchange(playerId);
        GeneralExchangeDaoHelper.asyncRemoveGeneralExchange(generalExchange.getId());
        //ret
        WsMessageHero.S2CGeneralExchangeConfirm respmsg = new WsMessageHero.S2CGeneralExchangeConfirm();
        respmsg.action_type = action_type;
        if (action_type == 1) {
            respmsg.general_bean = GeneralManager.getInstance().buildIoGeneral(generalBean);
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
