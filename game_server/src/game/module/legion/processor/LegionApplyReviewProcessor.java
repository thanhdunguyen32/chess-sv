package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.LegionDaoHelper;
import game.module.legion.dao.MyLegionTemplateCache;
import game.module.legion.logic.LegionManager;
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
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionApplyReview.id, accessLimit = 200)
public class LegionApplyReviewProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionApplyReviewProcessor.class);

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
        WsMessageLegion.C2SLegionApplyReview reqmsg = WsMessageLegion.C2SLegionApplyReview.parse(request);
        logger.info("legion apply review!player={},req={}", playerId, reqmsg);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionApplyReview.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //not ceo
        int myLegionPos = LegionManager.getInstance().getMyLegionPos(playerId, legionId);
        if (myLegionPos < 1) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionApplyReview.msgCode, 1518);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //not in apply list
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        if (legionBean.getApplyPlayers() == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionApplyReview.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        int[] player_ids = reqmsg.player_ids;
        //do
        List<WsMessageBase.IOLegionApplyReview> retlist = new ArrayList<>();
        synchronized (legionBean) {
            for (int aPlayerId : player_ids) {
                WsMessageBase.IOLegionApplyReview ioLegionApplyReview = new WsMessageBase.IOLegionApplyReview("");
                retlist.add(ioLegionApplyReview);
                //already join
                long otherLegionId = LegionManager.getInstance().getLegionId(aPlayerId);
                if (otherLegionId > 0) {
                    ioLegionApplyReview.error = "already join";
                    continue;
                }
                //"cd nyet" 玩家退出军团不满足12小时
                //member full
                Integer maxPNum = MyLegionTemplateCache.getInstance().getLegionLevelConfig(legionBean.getLevel()).getPnum();
                if (legionBean.getDbLegionPlayers() != null && legionBean.getDbLegionPlayers().getMembers() != null && legionBean.getDbLegionPlayers().getMembers().size() >= maxPNum) {
                    ioLegionApplyReview.error = "member full";
                    continue;
                }
                //apply nexist
                if (!legionBean.getApplyPlayers().containsKey(aPlayerId)) {
                    ioLegionApplyReview.error = "apply nexist";
                    continue;
                }
                //do
                if (reqmsg.is_accept) {
                    LegionPlayer legionMember = LegionManager.getInstance().getLegionMember(aPlayerId, 0);
                    legionBean.getDbLegionPlayers().getMembers().put(aPlayerId, legionMember);
                }
                //remove
                legionBean.getApplyPlayers().remove(aPlayerId);
            }
            //update legion
            LegionDaoHelper.asyncUpdateLegionBean(legionBean);
        }
        //ret
        WsMessageLegion.S2CLegionApplyReview respmsg = new WsMessageLegion.S2CLegionApplyReview();
        respmsg.ret = retlist;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
