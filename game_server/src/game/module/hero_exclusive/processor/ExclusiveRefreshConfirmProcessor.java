package game.module.hero_exclusive.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.hero.bean.GeneralBean;
import game.module.hero.bean.GeneralExclusive;
import game.module.hero.dao.GeneralCache;
import game.module.hero.logic.GeneralDaoHelper;
import game.module.hero.logic.GeneralManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.ArrayList;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageHero.C2SExclusiveRefreshConfirm.id, accessLimit = 200)
public class ExclusiveRefreshConfirmProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ExclusiveRefreshConfirmProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SExclusiveRefreshConfirm reqmsg = WsMessageHero.C2SExclusiveRefreshConfirm.parse(request);
        int playerId = playingRole.getId();
        logger.info("general exclusive refresh confirm,player={},req={}", playerId, reqmsg);
        long general_uuid = reqmsg.general_uuid;
        boolean is_confirm = reqmsg.is_confirm;
        //is exist
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        if (!heroCache.containsKey(general_uuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CExclusiveRefreshConfirm.msgCode, 1301);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        GeneralBean generalBean = heroCache.get(general_uuid);
        GeneralExclusive generalExclusive = generalBean.getExclusive();
        if (is_confirm) {
            generalExclusive.setProperty(generalExclusive.getPropertyPending());
            generalExclusive.setSkill(generalExclusive.getSkillPending());
        }
        //clear pending
        generalExclusive.setSkillPending(null);
        generalExclusive.setPropertyPending(null);
        //save bean
        GeneralManager.getInstance().updatePropertyAndPower(generalBean);
        GeneralDaoHelper.asyncUpdateHero(generalBean);
        //ret
        WsMessageHero.S2CExclusiveRefreshConfirm respmsg = new WsMessageHero.S2CExclusiveRefreshConfirm();
        respmsg.general_uuid = general_uuid;
        respmsg.is_confirm = is_confirm;
        respmsg.general_bean = GeneralManager.getInstance().buildIoGeneral(generalBean);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
