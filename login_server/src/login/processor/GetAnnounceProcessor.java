package login.processor;

import lion.common.MsgCodeAnn;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import login.LoginServer;
import login.bean.AnnounceBean;
import login.logic.AnnounceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageLogin;
import ws.WsMessageLogin.C2SGetAnnounce;

import java.util.List;

@MsgCodeAnn(msgcode = C2SGetAnnounce.id, accessLimit = 500)
public class GetAnnounceProcessor extends MsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetAnnounceProcessor.class);

    @Override
    public void process(GamePlayer session, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void process(GamePlayer gamePlayer, MyRequestMessage request) throws Exception {
        logger.info("get announcement!");
        WsMessageLogin.S2CGetAnnounce retMsg = new WsMessageLogin.S2CGetAnnounce();
        List<AnnounceBean> list = AnnounceManager.getInstance().getAnnounceList();
        if (list.isEmpty()) {
            retMsg.content = LoginServer.getInstance().getLoginServerConfig().getAnnouncement();
        } else {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (AnnounceBean announceBean : list) {
                if (i > 0) {
                    sb.append("*/*");
                }
                sb.append(announceBean.getContent());
                i++;
            }
            retMsg.content = sb.toString();
        }
        gamePlayer.writeAndFlush(retMsg.build(gamePlayer.alloc()));
    }

}
