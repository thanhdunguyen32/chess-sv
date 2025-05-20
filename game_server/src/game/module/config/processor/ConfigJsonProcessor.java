package game.module.config.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.module.config.logic.ConfigJsonManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import ws.WsMessageHall;

@MsgCodeAnn(msgcode = WsMessageHall.C2SConfigJson.id, accessLimit = 200)
public class ConfigJsonProcessor extends MsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ConfigJsonProcessor.class);


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
        WsMessageHall.C2SConfigJson reqmsg = WsMessageHall.C2SConfigJson.parse(request);
        String cmd = reqmsg.filename;
        int part = reqmsg.part;
        WsMessageHall.S2CConfigJson s2CConfigJson = new WsMessageHall.S2CConfigJson();
        String dataJson = ConfigJsonManager.getInstance().loadJsonFilePart(cmd + ".json", part);
        s2CConfigJson.json = dataJson;
        session.writeAndFlush(s2CConfigJson.build(session.alloc()));
    }
}


