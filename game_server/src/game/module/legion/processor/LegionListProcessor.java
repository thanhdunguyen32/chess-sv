package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.legion.bean.LegionBean;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.MyLegionTemplateCache;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageLegion;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionList.id, accessLimit = 200)
public class LegionListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionListProcessor.class);

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
        logger.info("legion list!player={}", playerId);
        //ret
        Collection<LegionBean> legionAll = LegionCache.getInstance().getLegionAll();
        WsMessageLegion.S2CLegionList respmsg = new WsMessageLegion.S2CLegionList();
        respmsg.legionlist = new ArrayList<>(legionAll.size());
        for (LegionBean legionBean : legionAll) {
            Integer ceoId = legionBean.getCeoId();
            PlayerBaseBean playerOfflineBean = PlayerOfflineManager.getInstance().getPlayerOfflineCache(ceoId);
            int mpnum = MyLegionTemplateCache.getInstance().getLegionLevelConfig(legionBean.getLevel()).getPnum();
            WsMessageBase.IOLegionInfo ioLegionInfo = new WsMessageBase.IOLegionInfo(legionBean.getUuid(), legionBean.getLevel(), legionBean.getName(),
                    legionBean.getDbLegionPlayers().getMembers().size(), mpnum, legionBean.getMinLevel(), legionBean.getPass(),
                    playerOfflineBean.getName());
            respmsg.legionlist.add(ioLegionInfo);
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
