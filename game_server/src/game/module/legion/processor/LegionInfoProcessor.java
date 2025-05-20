package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.friend.logic.FriendManager;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.MyLegionTemplateCache;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionInfo.id, accessLimit = 200)
public class LegionInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionInfoProcessor.class);

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
        logger.info("legion info!player={}", playerId);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionInfo.msgCode, 1516);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //ret
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        WsMessageLegion.S2CLegionInfo respmsg = new WsMessageLegion.S2CLegionInfo();
        respmsg.notice = legionBean.getNotice();
        respmsg.power = legionBean.getPower();
        respmsg.minlv = legionBean.getMinLevel();
        respmsg.lastmail = 0;
        respmsg.ispass = legionBean.getPass();
        respmsg.secretplace = LegionManager.getInstance().isLegionBossFinish(legionBean) ? 1 : 0;//神将试炼是否开放
//        respmsg.secretplace = 1;
        respmsg.wbosshistroyrank = 0;
        respmsg.servid = 1;
        respmsg.lastactive = 0;
        respmsg.name = legionBean.getName();
        respmsg.donation = getDonation(legionBean.getDbLegionPlayers());
        respmsg.exp = legionBean.getExp();
        respmsg.fexp = legionBean.getFexp();
        respmsg.flevel = legionBean.getFlevel();
        respmsg.kceo = legionBean.getKceo();
        respmsg.level = legionBean.getLevel();
        respmsg.maxexp = MyLegionTemplateCache.getInstance().getLegionLevelConfig(legionBean.getLevel()).getExp();
        respmsg.mpnum = MyLegionTemplateCache.getInstance().getLegionLevelConfig(legionBean.getLevel()).getPnum();
        respmsg.id = legionBean.getUuid();
        respmsg.members = getLegionMembers(legionBean.getDbLegionPlayers());
        respmsg.ceo = getCeoName(legionBean.getCeoId());
        respmsg.nextmail = 0L;
        respmsg.pos = legionBean.getDbLegionPlayers().getMembers().get(playerId).getPos();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private List<WsMessageBase.IOLegionMember> getLegionMembers(LegionPlayer.DbLegionPlayers dbLegionPlayers) {
        List<WsMessageBase.IOLegionMember> retlist = new ArrayList<>();
        for (Map.Entry<Integer, LegionPlayer> aEntry : dbLegionPlayers.getMembers().entrySet()) {
            int legionPlayerId = aEntry.getKey();
            LegionPlayer legionPlayer = aEntry.getValue();
            long lastestTime = FriendManager.getInstance().getPlayerLastestTime(legionPlayerId);
            PlayerBaseBean playerOfflineBean = PlayerOfflineManager.getInstance().getPlayerOfflineCache(legionPlayerId);
            WsMessageBase.IOLegionMember ioLegionMember = new WsMessageBase.IOLegionMember(legionPlayerId, playerOfflineBean.getName(),
                    playerOfflineBean.getIconid(), playerOfflineBean.getHeadid(), playerOfflineBean.getFrameid(), playerOfflineBean.getLevel(),
                    lastestTime, legionPlayer.getPos(), legionPlayer.getScore(), playerOfflineBean.getPower(), 0);
            retlist.add(ioLegionMember);
        }
        return retlist;
    }

    private Map<Integer, WsMessageBase.IOLegionDonation> getDonation(LegionPlayer.DbLegionPlayers dbLegionPlayers) {
        Map<Integer, WsMessageBase.IOLegionDonation> retmap = new HashMap<>();
        for (Map.Entry<Integer, LegionPlayer> aEntry : dbLegionPlayers.getMembers().entrySet()) {
            LegionPlayer legionPlayer = aEntry.getValue();
            Integer playerId = aEntry.getKey();
            WsMessageBase.IOLegionDonation ioLegionDonation = new WsMessageBase.IOLegionDonation(legionPlayer.getDonateSum(),
                    legionPlayer.getLastDonateTime().getTime());
            retmap.put(playerId, ioLegionDonation);
        }
        return retmap;
    }

    private String getCeoName(Integer ceoId) {
        PlayerBaseBean playerOfflineBean = PlayerOfflineManager.getInstance().getPlayerOfflineCache(ceoId);
        return playerOfflineBean.getName();
    }

}
