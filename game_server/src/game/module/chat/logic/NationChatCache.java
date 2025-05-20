package game.module.chat.logic;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.entity.PlayingRole;

public class NationChatCache {

	private static Logger logger = LoggerFactory.getLogger(NationChatCache.class);

	private Map<Integer, Set<PlayingRole>> cache = new ConcurrentHashMap<Integer, Set<PlayingRole>>();

	static class SingletonHolder {
		static NationChatCache instance = new NationChatCache();
	}

	public static NationChatCache getInstance() {
		return SingletonHolder.instance;
	}

	public void addPlayer(int nationId, PlayingRole playingRole) {
		Set<PlayingRole> playerList = cache.get(nationId);
		if (playerList == null) {
			playerList = new HashSet<PlayingRole>();
			cache.put(nationId, playerList);
		}
		playerList.add(playingRole);
	}

	public void removePlayer(int nationId, PlayingRole playingRole) {
		Set<PlayingRole> playerList = cache.get(nationId);
		if (playerList == null) {
			return;
		}
		playerList.remove(playingRole);
	}

	public void clearNationPlayers() {
		for(Set<PlayingRole> playerSet : cache.values()) {
			playerSet.clear();
		}
	}

	public Set<PlayingRole> getNationPlayers(int guildId) {
		return cache.get(guildId);
	}

}
