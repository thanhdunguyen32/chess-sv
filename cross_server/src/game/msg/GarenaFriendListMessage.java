package game.msg;

import game.bean.BusPlayerBean;
import io.netty.buffer.ByteBufAllocator;

import java.util.List;

import lion.netty4.message.SendToMessage;

public class GarenaFriendListMessage extends SendToMessage {

	public GarenaFriendListMessage(ByteBufAllocator alloc) {
		super(alloc, 20006);
	}

	public void setData(Integer playerId, List<String> busPlayerBeans) {
		writeInt(playerId);
		writeInt(busPlayerBeans.size());

		for (String garenaOpenid : busPlayerBeans) {
			writeString(garenaOpenid);
			// writeInt(busPlayerBean.getZoneId());
			// writeInt(busPlayerBean.getPlayerId());
			// writeString(busPlayerBean.getName());
			// writeInt(busPlayerBean.getIcon());
			// writeInt(busPlayerBean.getLevel());

		}
	}

}
