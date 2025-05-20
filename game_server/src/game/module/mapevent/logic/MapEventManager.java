package game.module.mapevent.logic;

import game.GameServer;
import game.module.chapter.bean.ChapterBean;
import game.module.chapter.dao.ChapterCache;
import game.module.chapter.dao.ChapterTemplateCache;
import game.module.chapter.dao.CityTemplateCache;
import game.module.mapevent.bean.MapEvent;
import game.module.mapevent.bean.PlayerMapEvent;
import game.module.mapevent.dao.MapEventCache;
import game.module.mapevent.dao.MapEventDao;
import game.module.mapevent.dao.MapEventTemplateCache;
import game.module.template.ChapterTemplate;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author HeXuhui
 */
public class MapEventManager {

    private static Logger logger = LoggerFactory.getLogger(MapEventManager.class);

    static class SingletonHolder {

        static MapEventManager instance = new MapEventManager();


    }

    public static MapEventManager getInstance() {
        return SingletonHolder.instance;
    }

    public void mapEventRefresh(int playerId) {
        PlayerMapEvent playerMapEvent = MapEventCache.getInstance().getMapEvent(playerId);
        if (playerMapEvent == null) {
            playerMapEvent = createPlayerMapEvent(playerId);
            MapEventCache.getInstance().addMapEvent(playerMapEvent);
            PlayerMapEvent finalPlayerMapEvent = playerMapEvent;
            GameServer.executorService.execute(() -> {
                MapEventDao.getInstance().addMapEvent(finalPlayerMapEvent);
            });
        } else {
            Date lastGenerateTime = playerMapEvent.getLastGenerateTime();
            int diffHour = (int) ((System.currentTimeMillis() - lastGenerateTime.getTime()) / 1000 / 3600);
            int generateSize = diffHour / MapEventConstants.MAX_EVENT_INTERVAL_HOUR;
            int existSize = 0;
            if (playerMapEvent.getDbMapEvent() != null && playerMapEvent.getDbMapEvent().getEvents() != null) {
                existSize = playerMapEvent.getDbMapEvent().getEvents().size();
            }
            if (existSize + generateSize > MapEventConstants.MAX_EVENT_SIZE) {
                generateSize = MapEventConstants.MAX_EVENT_SIZE - existSize;
            }
            //
            if (generateSize > 0) {
                MapEvent.DBMapEvent dbMapEvent = playerMapEvent.getDbMapEvent();
                if (dbMapEvent == null) {
                    dbMapEvent = new MapEvent.DBMapEvent();
                    playerMapEvent.setDbMapEvent(dbMapEvent);
                }
                generateMapEvent(dbMapEvent, playerId, generateSize);
            }
            int lackSeconds = (int) ((System.currentTimeMillis() - lastGenerateTime.getTime()) / 1000) % (MapEventConstants.MAX_EVENT_INTERVAL_HOUR * 3600);
            playerMapEvent.setLastGenerateTime(DateUtils.addSeconds(new Date(), -lackSeconds));
            PlayerMapEvent finalPlayerMapEvent1 = playerMapEvent;
            GameServer.executorService.execute(() -> {
                MapEventDao.getInstance().updateMapEvent(finalPlayerMapEvent1);
            });
        }
    }

    private PlayerMapEvent createPlayerMapEvent(int playerId) {
        PlayerMapEvent playerMapEvent = new PlayerMapEvent();
        playerMapEvent.setPlayerId(playerId);
        playerMapEvent.setLastGenerateTime(new Date());
        MapEvent.DBMapEvent dbMapEvent = new MapEvent.DBMapEvent();
        generateMapEvent(dbMapEvent, playerId, MapEventConstants.INIT_MAP_EVENT_SIZE);
        playerMapEvent.setDbMapEvent(dbMapEvent);
        return playerMapEvent;
    }

    private MapEvent.DBMapEvent generateMapEvent(MapEvent.DBMapEvent dbMapEvent, int playerId, int generateSize) {
        if (dbMapEvent.getEvents() == null) {
            Map<Integer, MapEvent> eventMap = new HashMap<>();
            dbMapEvent.setEvents(eventMap);
        }
        ChapterBean playerChapter = ChapterCache.getInstance().getPlayerChapter(playerId);
        int maxMapId = playerChapter.getMaxMapId();
        ChapterTemplate chapterTemplate = ChapterTemplateCache.getInstance().getChapterTemplateById(maxMapId);
        Integer maxCityId = chapterTemplate.getCITY();
        List<Integer> cityIds = CityTemplateCache.getInstance().getCityIds();
        int aIndex = Collections.binarySearch(cityIds, maxCityId);
        //候选eid
        List<Integer> eidPending = new ArrayList<>();
        for (int i = 0; i <= aIndex; i++) {
            Integer aCityId = cityIds.get(i);
            List<Integer> eids = CityTemplateCache.getInstance().getCityTemplateById(aCityId).getEID();
            eidPending.addAll(eids);
        }
        //剔除已经生成
        eidPending.removeAll(dbMapEvent.getEvents().keySet());
        //随机选择
        while (generateSize > 0 && eidPending.size() > 0) {
            int randIndex = RandomUtils.nextInt(0, eidPending.size());
            Integer randCityId = eidPending.get(randIndex);
            boolean isEidCitySide = CityTemplateCache.getInstance().isEidCitySide(randCityId);
            int randType = 1;
            if (!isEidCitySide) {
                randType = MapEventConstants.RAND_EVENT_TYPES[RandomUtils.nextInt(0, MapEventConstants.RAND_EVENT_TYPES.length)];
            }
            List<Integer> eventIds = MapEventTemplateCache.getInstance().getEventIds(randType);
            int randEid = eventIds.get(RandomUtils.nextInt(0, eventIds.size()));
            dbMapEvent.getEvents().put(randCityId, new MapEvent(randCityId, randType, randEid, null));
            generateSize--;
            eidPending.remove(randIndex);
        }
        return dbMapEvent;
    }

}
