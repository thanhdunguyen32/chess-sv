package lion.netty4.message;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessageV3;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lion.common.ProcessorStatManager;
import lion.netty4.core.SocketProtoServer;

import java.io.IOException;

public class GamePlayer {

    private static Logger logger = LoggerFactory.getLogger(GamePlayer.class);

    private Channel channel;

    private long lastVisitTime;

    public GamePlayer(Channel channel) {
        this.setChannel(channel);
    }

    public void sendMsg(Object obj) {
        if (obj == null || !channel.isActive()) {
            return;
        }
        channel.write(obj);
    }

    public String getAddress() {
        String addr = "";
        if (channel.remoteAddress() != null) {
            addr = channel.remoteAddress().toString();
        }
        return addr;
    }

    public ByteBufAllocator alloc() {
        return channel.alloc();
    }

    public long getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(long lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    public void write(SendToByteMessage respMsg) {
        if (!channel.isActive()) {
            return;
        }
        channel.write(respMsg);
    }

    public void write(MySendToMessage respMsg) {
        if (!channel.isActive()) {
            respMsg.release();
            return;
        }
        ByteBuf rawBytes;
        try {
            rawBytes = respMsg.entireMsg();
            BinaryWebSocketFrame retFrame = new BinaryWebSocketFrame(rawBytes);
            getChannel().write(retFrame);
        } catch (IOException e) {
            logger.error("",e);
        }
    }

    public boolean isChannelActive() {
        return channel.isActive();
    }

    /**
     * 在一次请求中，同一个channel写入了多个消息，写入的时候，channel做了缓存
     * 等到最后一个消息写入channel之后，执行flush方法，将所有消息内容发送给系统的信道，清空channel的缓存
     */
    public void flush() {
        if (channel.isActive()) {
            channel.flush();
        }
    }

    public void writeAndFlush(SendToByteMessage respMsg) {
        if (channel.isActive()) {
            channel.writeAndFlush(respMsg);
        }
    }

    public void writeAndFlush(MySendToMessage respMsg) {
        if (!channel.isActive()) {
            respMsg.release();
            return;
        }
        try {
            ByteBuf rawBytes = respMsg.entireMsg();
            BinaryWebSocketFrame retFrame = new BinaryWebSocketFrame(rawBytes);
            getChannel().writeAndFlush(retFrame);
        } catch (IOException e) {
            logger.error("",e);
        }
    }

    public void write(int msgCode, GeneratedMessageV3 respMsg) {
        if (channel.isActive()) {
            channel.write(new ResponseProtoCodeMessage(msgCode, respMsg, 0));
            // logger.info("write,msgCode={}", msgCode);
        }
    }

    public void write(int msgCode, int errorCode) {
        if (channel.isActive()) {
            channel.write(new ResponseProtoCodeMessage(msgCode, null, errorCode));
            // logger.info("write,msgCode={}", msgCode);
        }
    }

    public void writeAndFlush(int msgCode, GeneratedMessageV3 respMsg) {
        if (channel.isActive()) {
            channel.writeAndFlush(new ResponseProtoCodeMessage(msgCode, respMsg, 0));
            ProcessorStatManager.getInstance().logEnd(msgCode, channel);
            // logger.info("writeAndFlush,msgCode={}", msgCode);
        }
    }

    public void writeAndFlush(int msgCode, int errorCode) {
        if (channel.isActive()) {
            channel.writeAndFlush(new ResponseProtoCodeMessage(msgCode, null, errorCode));
            ProcessorStatManager.getInstance().logEnd(msgCode, channel);
            // logger.info("writeAndFlush,msgCode={}", msgCode);
        }
    }

    public ChannelFuture close() {
        return channel.close();
    }

    public Long getSessionId() {
        return channel.attr(SocketProtoServer.KEY_SESSION_ID).get();
    }

    public void saveSessionId(Long sessionId) {
        channel.attr(SocketProtoServer.KEY_SESSION_ID).set(sessionId);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
