package game.module.user.processor;

import game.entity.PlayingRole;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageHall.S2CDownlineReconnect;

import java.util.Date;

@MsgCodeAnn(msgcode = WsMessageHall.C2SDownlineReconnect.id, accessLimit = 200)
public class DownlineReconnectProcessor extends MsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(DownlineReconnectProcessor.class);

    @Override
    public void process(GamePlayer session, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void process(GamePlayer player, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void process(GamePlayer player, MyRequestMessage request) throws Exception {
        WsMessageHall.C2SDownlineReconnect downlineReconnectMsg = WsMessageHall.C2SDownlineReconnect.parse(request);
        long inGameSessionId = downlineReconnectMsg.in_game_session_id;
        logger.info("{}-DownlineReconnect,inGameSessionId={},addr={}", WsMessageHall.C2SDownlineReconnect.id, inGameSessionId, player.getAddress());
        PlayingRole playingRole = SessionManager.getInstance().getPlayer(inGameSessionId);
        // session id 无效，超时，玩家缓存已经被移除
        if (playingRole == null) {
            logger.warn("player not exist!addr={}", player.getAddress());
            S2CDownlineReconnect retMsg = new S2CDownlineReconnect(1);
            player.writeAndFlush(retMsg.build(player.alloc()));
            return;
        }
        GamePlayer oldGamePlayer = playingRole.getGamePlayer();
        if (oldGamePlayer != null) {
            if (oldGamePlayer == player) {
                logger.warn("socket GamePlayer is equal!");
                S2CDownlineReconnect retMsg = new S2CDownlineReconnect(2);
                player.writeAndFlush(retMsg.build(player.alloc()));
                return;
            } else {
                oldGamePlayer.saveSessionId(0L);
                oldGamePlayer.close();
            }
        }
        // online
        SessionManager.getInstance().online(playingRole);
        playingRole.setGamePlayer(player);
        player.saveSessionId(inGameSessionId);
        SessionManager.getInstance().visit(inGameSessionId);
        // 自动加体力
//		PlayerLogic.getInstance().online(playingRole.getId());
        playingRole.getPlayerCacheStatus().setEnterGameTime(new Date());
        // chat cache
//		int guildId = GuildCache.getInstance().getGuildId(playingRole.getId());
//		GuildChatCache.getInstance().addPlayer(guildId, playingRole);
        // ret
        S2CDownlineReconnect retMsg = new S2CDownlineReconnect(0);
        player.writeAndFlush(retMsg.build(player.alloc()));
    }

}
