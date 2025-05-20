package game.module.hero.processor;

import com.google.common.primitives.Longs;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.logic.GeneralManager;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.*;

@MsgCodeAnn(msgcode = WsMessageHero.C2SGeneralDecomp.id, accessLimit = 200)
public class GeneralDeComposeProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GeneralDeComposeProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SGeneralDecomp reqMsg = WsMessageHero.C2SGeneralDecomp.parse(request);
        int playerId = playingRole.getId();
        logger.info("武将分解,player={},req={}", playerId, reqMsg);
        long[] general_uuid = reqMsg.general_uuid;
        int action_type = reqMsg.action_type;
        //是否存在
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        Set<Long> existUuids = new HashSet<>();
        for (long aGeneralUuid : general_uuid) {
            if (!heroCache.containsKey(aGeneralUuid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralDecomp.msgCode, 1301);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            if (existUuids.contains(aGeneralUuid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralDecomp.msgCode, 1330);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            existUuids.add(aGeneralUuid);
        }
        //do
        boolean isDo = action_type>0;
        Map<Integer, Integer> rewardMap = new HashMap<>();
        for (long aGeneralUuid : general_uuid) {
            decompGeneral1(playingRole, aGeneralUuid, rewardMap, isDo);
        }
        //award
        if(isDo) {
            for (Map.Entry<Integer, Integer> aEntry : rewardMap.entrySet()) {
                AwardUtils.changeRes(playingRole, aEntry.getKey(), aEntry.getValue(), LogConstants.MODULE_GENERAL_DECOMP);
            }
            //update mission progress
            AwardUtils.changeRes(playingRole, MissionConstants.MISSION_GENERAL_DECOMP, general_uuid.length, LogConstants.MODULE_GENERAL_DECOMP);
        }
        //ret
        WsMessageHero.S2CGeneralDecomp respmsg = new WsMessageHero.S2CGeneralDecomp();
        respmsg.general_uuid = Longs.asList(general_uuid);
        respmsg.rewards = new ArrayList<>(rewardMap.size());
        for (Map.Entry<Integer, Integer> aEntry : rewardMap.entrySet()) {
            respmsg.rewards.add(new WsMessageBase.RewardInfo(aEntry.getKey(), aEntry.getValue()));
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void decompGeneral1(PlayingRole playingRole, long aGeneralUuid, Map<Integer, Integer> rewardMap, boolean isDo) {
        int playerId = playingRole.getId();
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        GeneralBean generalBean = heroCache.get(aGeneralUuid);
        //star & level & pclass & takeoff equip and treasure
        GeneralManager.getInstance().generalDecompAwards(playingRole, generalBean, rewardMap, isDo);
        //remove
        if(isDo) {
            GeneralManager.getInstance().removeGeneral(playingRole, generalBean.getUuid());
        }
    }

}
