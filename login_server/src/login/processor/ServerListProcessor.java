package login.processor;

import lion.common.MsgCodeAnn;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import login.bean.ServerHasRole;
import login.bean.ServerList4Db;
import login.cache.LoginSessionCache;
import login.logic.ServerHasRoleManager;
import login.logic.ServerListManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ws.WsMessageBase;
import ws.WsMessageLogin.C2SServerList;
import ws.WsMessageLogin.S2CServerList;

import java.util.*;

@MsgCodeAnn(msgcode = C2SServerList.id, accessLimit = 500)
public class ServerListProcessor extends MsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ServerListProcessor.class);

    @Override
    public void process(GamePlayer session, RequestByteMessage request) throws Exception {

    }

    @Override
    public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void process(GamePlayer gamePlayer, MyRequestMessage request) throws Exception {
        C2SServerList reqmsg = C2SServerList.parse(request);
        List<ServerList4Db> retList = ServerListManager.getInstance().getServerList();
        
        S2CServerList respMsg = new S2CServerList();
        respMsg.alls = new ArrayList<>(retList.size());
        for (ServerList4Db serverList4Db : retList) {
            respMsg.alls.add(new WsMessageBase.ServerListItem(serverList4Db.getId(), serverList4Db.getName(),
                    serverList4Db.getIp(), serverList4Db.getPort(), serverList4Db.getStatus(),
                    serverList4Db.getPortSsl(), serverList4Db.getUrlSsl(), serverList4Db.getIsSsl()));
        }
        //recommand
        int recommandSize = 1;
        if (retList.size() > 1) {
            recommandSize = 2;
        }
        respMsg.recommands = new ArrayList<>(recommandSize);
        for (int i = 0; i < recommandSize; i++) {
            ServerList4Db serverList4Db = retList.get(retList.size() - 1 - i);
            respMsg.recommands.add(serverList4Db.getId());
        }
        //has role
        long loginSessionId = reqmsg.session_id;
        String openId = LoginSessionCache.getInstance().getOpenId(loginSessionId);
        if (StringUtils.isNotBlank(openId)) {
            ServerHasRole serverHasRole = ServerHasRoleManager.getInstance().getServerHasRole(openId);
            if (serverHasRole != null) {
                //有角色为空
                if (serverHasRole.getServerLevels() == null || serverHasRole.getServerLevels().size() == 0) {
                    respMsg.has_roles = Collections.singletonList(new WsMessageBase.IOServerHasRole(serverHasRole.getLastLoginServerId(), 0));
                } else {
                    respMsg.has_roles = new ArrayList<>(serverHasRole.getServerLevels().size());
                    for (Map.Entry<Integer, Integer> aEntry : serverHasRole.getServerLevels().entrySet()) {
                        respMsg.has_roles.add(new WsMessageBase.IOServerHasRole(aEntry.getKey(), aEntry.getValue()));
                    }
                    //服务器越新，拍越前面
                    respMsg.has_roles.sort(Comparator.comparingInt(f -> -f.server_id));
                }
            }
        }
        gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
    }

    public static void main(String[] args) {
        WsMessageBase.IOServerHasRole ioServerHasRole = new WsMessageBase.IOServerHasRole(1, 15);
        List<WsMessageBase.IOServerHasRole> list = new ArrayList<>();
        list.add(new WsMessageBase.IOServerHasRole(1, 15));
        list.add(new WsMessageBase.IOServerHasRole(2, 10));
        list.sort(Comparator.comparingInt(f -> -f.server_id));
        System.out.println(list.get(0).player_level);
    }

}
