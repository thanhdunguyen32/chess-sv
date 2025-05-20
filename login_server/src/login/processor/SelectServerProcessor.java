package login.processor;

import lion.common.MsgCodeAnn;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import login.LoginServer;
import login.bean.ServerList4Db;
import login.cache.LoginSessionCache;
import login.logic.ServerHasRoleManager;
import login.logic.ServerListManager;
import login.stat.LoginStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageLogin.C2SSelectServer;
import ws.WsMessageLogin.S2CSelectServer;

import java.util.Set;

@MsgCodeAnn(msgcode = C2SSelectServer.id, accessLimit = 500)
public class SelectServerProcessor extends MsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(SelectServerProcessor.class);

    @Override
    public void process(GamePlayer session, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void process(final GamePlayer player, RequestProtoMessage request) throws Exception {

    }

    public static String getIpAddr(String ipAddrRaw) {
        String retStr = ipAddrRaw;
        int fenIndex = ipAddrRaw.indexOf(":");
        if (fenIndex >= 0) {
            retStr = retStr.substring(0, fenIndex);
        }
        if (retStr.startsWith("/")) {
            retStr = retStr.substring(1);
        }
        return retStr;
    }

    public static void main(String[] args) {
        String ipAddrRaw = "/101.81.106.190";
        String retStr = ipAddrRaw;
        int fenIndex = ipAddrRaw.indexOf(":");
        if (fenIndex >= 0) {
            retStr = retStr.substring(0, fenIndex);
        }
        if (retStr.startsWith("/")) {
            retStr = retStr.substring(1);
        }
        logger.info("ip={}", retStr);
    }

    @Override
    public void process(final GamePlayer player, MyRequestMessage request) throws Exception {
        C2SSelectServer reqMsg = C2SSelectServer.parse(request);
        int serverId = reqMsg.server_id;
        logger.info("select server,reqMsg={}", reqMsg);
        ServerList4Db targetServer = ServerListManager.getInstance().getServer(serverId);
        if (targetServer == null) {
            S2CSelectServer retMsg = new S2CSelectServer(2,serverId, false);
            player.writeAndFlush(retMsg.build(player.alloc()));
            return;
        }
        // get session
        long loginSessionId = reqMsg.session_id;
        String openId = LoginSessionCache.getInstance().getOpenId(loginSessionId);
        if (openId == null) {
            S2CSelectServer retMsg = new S2CSelectServer(3,serverId, false);
            player.writeAndFlush(retMsg.build(player.alloc()));
            return;
        }
        // 判断服务器是否开启
        if (targetServer.getStatus() == 0) {
            boolean isWhiteList = false;
            // 获取ip地址
            String ipAddrRaw = player.getAddress();
            String ipAddr = getIpAddr(ipAddrRaw);
            logger.info("联运 select server,ip=" + ipAddr);
            Set<String> whiteOpenIds = LoginServer.getInstance().getLoginServerConfig().getLoginWhiteList();
            if (whiteOpenIds.contains(ipAddr)) {
                isWhiteList = true;
            }
            if (!isWhiteList) {
                S2CSelectServer retMsg = new S2CSelectServer(4,serverId, false);
                player.writeAndFlush(retMsg.build(player.alloc()));
                return;
            }
        }
        LoginStat.getInstance().saveSelectServerTime(loginSessionId);
        boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(targetServer.getIp(),
                targetServer.getLanPort());
        if (reconnectRet) {
            LoginServer.getInstance().getLanClientManager().sendLogin2Gs(targetServer.getIp(),
                    targetServer.getLanPort(), loginSessionId, openId, serverId, msg -> {
                        // parse ret
                        boolean hasRole = msg.readBool();
                        S2CSelectServer respMsg = new S2CSelectServer(0,serverId,hasRole);
                        logger.info("game server ret:openId={},serverId={},hasRole={}", openId, serverId, hasRole);
                        player.writeAndFlush(respMsg.build(player.alloc()));
                        LoginStat.getInstance().statSelectServer(loginSessionId);
                        //remove
                        LoginSessionCache.getInstance().removeSession(loginSessionId);
                        //add log
                        ServerHasRoleManager.getInstance().addLastLoginServer(openId, serverId);
                    });
        } else {
            S2CSelectServer retMsg = new S2CSelectServer(1,serverId, false);
            player.writeAndFlush(retMsg.build(player.alloc()));
        }
    }

}
