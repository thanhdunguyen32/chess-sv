package game.module.user.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.chapter.bean.ChapterBean;
import game.module.chapter.dao.ChapterCache;
import game.module.chapter.logic.ChapterManager;
import game.module.item.logic.BagManager;
import game.module.pay.logic.ChargeInfoManager;
import game.module.pvp.logic.PvpManager;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerHideCache;
import game.module.user.logic.PlayerServerPropManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageHall.C2SPlayerBaseData.id, accessLimit = 200)
public class PlayerBaseInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(PlayerBaseInfoProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("get player base info!playerId={}", playerId);
        WsMessageHall.S2CPlayerBaseData respMsg = new WsMessageHall.S2CPlayerBaseData();
        ChapterBean chapterBean = ChapterCache.getInstance().getPlayerChapter(playingRole.getPlayerBean().getId());
        if (chapterBean != null) {
            respMsg.maxmapid = chapterBean.getMaxMapId();
        } else {
            respMsg.maxmapid = ChapterManager.getInstance().getInitMapId();
        }
        respMsg.tower = PlayerServerPropManager.getInstance().getTower(playingRole.getPlayerBean().getId());
        respMsg.pvpscore = PvpManager.getInstance().getPvpScore(playingRole.getPlayerBean().getId());
        respMsg.gnum = BagManager.getInstance().getGNum(playerId);
        respMsg.bagspace = BagManager.getInstance().getGeneralBagSize(playerId, playingRole.getPlayerBean().getVipLevel());
        //hiddens
        Map<Integer, PlayerProp> playerHiddens = PlayerHideCache.getInstance().getPlayerHidden(playerId);
        respMsg.hiddens = new ArrayList<>(playerHiddens.size());
        for (PlayerProp playerProp : playerHiddens.values()) {
            respMsg.hiddens.add(new WsMessageBase.SimpleItemInfo(playerProp.getGsid(), playerProp.getCount()));
        }
        //special
        respMsg.special = ChargeInfoManager.getInstance().getChargeInfo(playerId);
        //ret
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

}
