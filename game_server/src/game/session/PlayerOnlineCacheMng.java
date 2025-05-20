package game.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.entity.PlayingRole;
import game.module.user.bean.PlayerBean;
import lion.common.StringConstants;

public class PlayerOnlineCacheMng {

	private static Logger logger = LoggerFactory.getLogger(PlayerOnlineCacheMng.class);

	public static final int MAX_PLAYER_CACHE_SIZE = 5000;

    static class SingletonHolder {
		static PlayerOnlineCacheMng instance = new PlayerOnlineCacheMng();
	}

	public static PlayerOnlineCacheMng getInstance() {
		return SingletonHolder.instance;
	}

	/*
	 * 游戏中
	 */
	private volatile Map<Integer, PlayingRole> onlineCharacterIdKey = new ConcurrentHashMap<Integer, PlayingRole>();

	private volatile Map<String, PlayerBean> characterOpenIdKey = new ConcurrentHashMap<String, PlayerBean>();

	private PlayerOnlineCacheMng() {
		// BaseIoExecutor.handlerSet.add(this);
		// BaseProtoIoExecutor.handlerSet.add(this);
	}

	public void online(PlayingRole playingRole) {
		PlayerBean pb = playingRole.getPlayerBean();
		onlineCharacterIdKey.put(pb.getId(), playingRole);
		if (StringUtils.isNotBlank(pb.getAccountId())) {
			characterOpenIdKey.put(pb.getAccountId() + StringConstants.SEPARATOR_HENG + pb.getServerId(),
					playingRole.getPlayerBean());
		}
	}

	public void offline(PlayerBean pb) {
		onlineCharacterIdKey.remove(pb.getId());
		if (StringUtils.isNotBlank(pb.getAccountId())) {
			characterOpenIdKey.remove(pb.getAccountId() + StringConstants.SEPARATOR_HENG + pb.getServerId());
		}
	}

	public PlayerBean getCache(String accountId, int serverId) {
		if(StringUtils.isNotBlank(accountId)) {
			return characterOpenIdKey.get(accountId + StringConstants.SEPARATOR_HENG + serverId);
		}
		return null;
	}

	public PlayingRole getOnlinePlayerById(int playerId) {
		return onlineCharacterIdKey.get(playerId);
	}

	public Map<Integer, PlayingRole> getOnlinePlayers() {
		return onlineCharacterIdKey;
	}
}
