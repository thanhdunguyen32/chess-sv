package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.LegionCache;
import game.module.legion.logic.LegionManager;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionFactoryDonateGet.id, accessLimit = 200)
public class LegionFactoryDonateGetProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionFactoryDonateGetProcessor.class);

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
        logger.info("legion factory donate get!player={}", playerId);
        //ret
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryDonateGet.msgCode, 1516);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        List<LegionPlayer> playerlist = new ArrayList<>(legionBean.getDbLegionPlayers().getMembers().values());
        playerlist.sort(Comparator.comparingInt(LegionPlayer::getDonateSum).reversed());
        //ret
        WsMessageLegion.S2CLegionFactoryDonateGet respmsg = new WsMessageLegion.S2CLegionFactoryDonateGet();
        respmsg.items = new ArrayList<>();
        for (LegionPlayer legionPlayer : playerlist) {
            int legionPlayerId = legionPlayer.getPlayerId();
            PlayerBaseBean playerOfflineBean = PlayerOfflineManager.getInstance().getPlayerOfflineCache(legionPlayerId);
            WsMessageBase.IOLegionFactoryDonation ioLegionFactoryDonation = new WsMessageBase.IOLegionFactoryDonation(playerOfflineBean.getName(),
                    playerOfflineBean.getIconid(), playerOfflineBean.getHeadid(), playerOfflineBean.getFrameid(), legionPlayer.getDonateSum(),
                    legionPlayer.getPos(), legionPlayer.getLastDonateTime().getTime());
            respmsg.items.add(ioLegionFactoryDonation);
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
