package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.manor.bean.SurrenderPersuade;
import game.module.manor.dao.SurrenderPersuadeCache;
import game.module.manor.dao.SurrenderTemplateCache;
import game.module.template.SurrenderTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageManor;

import java.util.HashMap;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageManor.C2SSrenderList.id, accessLimit = 200)
public class SurrenderListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(SurrenderListProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("surrender list,player={}", playerId);
        //ret
        WsMessageManor.S2CSrenderList respmsg = new WsMessageManor.S2CSrenderList();
        SurrenderPersuade surrenderPersuade = SurrenderPersuadeCache.getInstance().getSurrenderPersuade(playerId);
        if (surrenderPersuade != null && surrenderPersuade.getSurrendermap() != null) {
            respmsg.list = new HashMap<>();
            for (Map.Entry<Integer, Integer> aEntry : surrenderPersuade.getSurrendermap().entrySet()) {
                Integer chapterId = aEntry.getKey();
                Integer loyal = aEntry.getValue();
                SurrenderTemplate surrenderTemplate = SurrenderTemplateCache.getInstance().getSurrenderTemplate(chapterId);
                WsMessageBase.IOSrenderState ioSrenderState = new WsMessageBase.IOSrenderState(surrenderTemplate.getGSID(), loyal, loyal > 0 ? 0 : 1);
                respmsg.list.put(chapterId, ioSrenderState);
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
