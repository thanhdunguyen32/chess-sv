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
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionApply.id, accessLimit = 200)
public class LegionApplyProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionApplyProcessor.class);

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
        WsMessageLegion.C2SLegionApply reqmsg = WsMessageLegion.C2SLegionApply.parse(request);
        logger.info("legion apply!player={},req={}", playerId, reqmsg);
        //is in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId > 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionApply.msgCode, 1521);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //legion full
        long legion_id = reqmsg.legion_id;
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legion_id);
        Integer maxPNum = MyLegionTemplateCache.getInstance().getLegionLevelConfig(legionBean.getLevel()).getPnum();
        if (legionBean.getDbLegionPlayers() != null && legionBean.getDbLegionPlayers().getMembers() != null && legionBean.getDbLegionPlayers().getMembers().size() >= maxPNum) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionApply.msgCode, 1522);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //level
        if (playingRole.getPlayerBean().getLevel() < legionBean.getMinLevel()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionApply.msgCode, 1523);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //already apply
        if (legionBean.getApplyPlayers() != null && legionBean.getApplyPlayers().containsKey(playerId)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionApply.msgCode, 1524);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        synchronized (legionBean) {
            if (legionBean.getPass()) {
                LegionPlayer legionMember = LegionManager.getInstance().getLegionMember(playerId, 0);
                legionBean.getDbLegionPlayers().getMembers().put(playerId, legionMember);
            } else {
                if (legionBean.getApplyPlayers() == null) {
                    Map<Integer, Long> applyPlayers = new HashMap<>();
                    legionBean.setApplyPlayers(applyPlayers);
                }
                legionBean.getApplyPlayers().put(playerId, System.currentTimeMillis());
            }
            LegionDaoHelper.asyncUpdateLegionBean(legionBean);
        }
        //ret
        WsMessageLegion.S2CLegionApply respmsg = new WsMessageLegion.S2CLegionApply();
        if (legionBean.getPass()) {
            respmsg.id = legion_id;
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
