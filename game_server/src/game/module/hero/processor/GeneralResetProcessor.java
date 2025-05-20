package game.module.hero.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.logic.GeneralConstants;
import game.module.hero.logic.GeneralDaoHelper;
import game.module.hero.logic.GeneralManager;
import game.module.log.constants.LogConstants;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageHero.C2SGeneralReset.id, accessLimit = 100)
public class GeneralResetProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GeneralResetProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SGeneralReset reqMsg = WsMessageHero.C2SGeneralReset.parse(request);
        int playerId = playingRole.getId();
        logger.info("武将reset,player={},req={}", playerId, reqMsg);
        long general_uuid = reqMsg.general_uuid;
        int action_type = reqMsg.action_type;
        //是否存在
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        if (!heroCache.containsKey(general_uuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralReset.msgCode, 1301);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        boolean isDo = action_type>0;
        GeneralBean generalBean = heroCache.get(general_uuid);
        Map<Integer, Integer> rewardMap = new HashMap<>();
        GeneralManager.getInstance().generalResetAwards(playingRole,generalBean,rewardMap, isDo);
        if(isDo) {
            //award
            for (Map.Entry<Integer, Integer> aEntry : rewardMap.entrySet()) {
                AwardUtils.changeRes(playingRole, aEntry.getKey(), aEntry.getValue(), LogConstants.MODULE_GENERAL_DECOMP);
            }
            //bean init
            generalBean.setLevel(1);
            generalBean.setPclass(0);
            generalBean.setEquip(new ArrayList<>(Arrays.asList(0, 0, 0, 0)));
            generalBean.setTreasure(0);
            GeneralManager.getInstance().updatePropertyAndPower(generalBean);
            GeneralDaoHelper.asyncUpdateHero(generalBean);
        }
        //cost
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -GeneralConstants.GENERAL_RESET_YB, LogConstants.MODULE_GENERAL_DECOMP);
        //ret
        WsMessageHero.S2CGeneralReset respmsg = new WsMessageHero.S2CGeneralReset();
        respmsg.general_uuid = general_uuid;
        respmsg.items = new ArrayList<>(rewardMap.size());
        for (Map.Entry<Integer, Integer> aEntry : rewardMap.entrySet()) {
            respmsg.items.add(new WsMessageBase.RewardInfo(aEntry.getKey(), aEntry.getValue()));
        }
        if(isDo) {
            respmsg.general_bean = GeneralManager.getInstance().buildIoGeneral(generalBean);
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
