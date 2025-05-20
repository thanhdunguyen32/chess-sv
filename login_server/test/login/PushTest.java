package login;

import game.module.user.ProtoMessageLogin.S2CPushTest;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import lion.netty4.message.GamePlayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushTest {

	private static Logger logger = LoggerFactory.getLogger(PushTest.class);

	static class SingletonHolder {
		static PushTest instance = new PushTest();
	}

	public static PushTest getInstance() {
		return SingletonHolder.instance;
	}

	private Set<GamePlayer> playerList = Collections.newSetFromMap(new ConcurrentHashMap<GamePlayer, Boolean>());

	public void addPushTest(GamePlayer gamePlayer) {
		if (!playerList.contains(gamePlayer)) {
			playerList.add(gamePlayer);
			scheduleSendTestMessage(gamePlayer);
		}
	}

	private void scheduleSendTestMessage(final GamePlayer gamePlayer) {
		LoginServer.executorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (gamePlayer.isChannelActive()) {
					String currentDateStr = new Date().toString();
					logger.info("send push test!date={}", currentDateStr);
					gamePlayer.writeAndFlush(10018, S2CPushTest.newBuilder().setIndex(0).setCurrentDate(currentDateStr).build());
				}
			}
		}, 1, 1, TimeUnit.SECONDS);
	}

}
