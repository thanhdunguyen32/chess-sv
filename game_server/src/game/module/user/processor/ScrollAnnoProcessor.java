package game.module.user.processor;

import game.GameServer;
import lion.common.MsgCodeAnn;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@MsgCodeAnn(msgcode = WsMessageHall.C2SScrollAnno.id, accessLimit = 200)
public class ScrollAnnoProcessor extends MsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ScrollAnnoProcessor.class);

    @Override
    public void process(GamePlayer session, RequestByteMessage requestMessage) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void process(GamePlayer session, RequestProtoMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void process(GamePlayer session, MyRequestMessage request) throws Exception {
        logger.info("scroll anno!");
        WsMessageHall.S2CScrollAnno respmsg = new WsMessageHall.S2CScrollAnno();
        respmsg.annos = new ArrayList<>(1);
//        respmsg.annos.add("欢迎加入玩家QQ交流群684910294获取游戏更多资讯，入群还可获得加群礼包一份哦！");
        List<String> scrollAnno = GameServer.getInstance().getServerConfig().getScrollAnno();
        respmsg.annos.addAll(scrollAnno);
        session.writeAndFlush(respmsg.build(session.alloc()));
    }

}
