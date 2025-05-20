package game.module.chapter.logic;

import game.module.chapter.bean.ChapterBean;
import game.module.chapter.dao.ChapterCache;
import game.module.chapter.dao.ChapterTemplateCache;
import game.module.chapter.dao.CityTemplateCache;
import game.module.template.ChapterTemplate;
import game.module.template.CityTemplate;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class ChapterManager {

    private static Logger logger = LoggerFactory.getLogger(ChapterManager.class);

    static class SingletonHolder {
        static ChapterManager instance = new ChapterManager();
    }

    public static ChapterManager getInstance() {
        return SingletonHolder.instance;
    }

    public static final int MAX_HANG_SECONDS = 24*3600;

    public int getInitMapId() {
        int initCityId = CityTemplateCache.getInstance().getInitCityId();
        CityTemplate cityTemplate = CityTemplateCache.getInstance().getCityTemplateById(initCityId);
        return cityTemplate.getMAP().get(0);
    }

    public int getNextMapId(int mapId) {
        ChapterTemplate chapterTemplate = ChapterTemplateCache.getInstance().getChapterTemplateById(mapId);
        Integer cityId = chapterTemplate.getCITY();
        CityTemplate cityTemplate = CityTemplateCache.getInstance().getCityTemplateById(cityId);
        List<Integer> maplist = cityTemplate.getMAP();
        int mapIndex = maplist.indexOf(mapId);
        if (mapIndex == maplist.size() - 1) {
            Integer nextCityId = CityTemplateCache.getInstance().getNextCityId(cityId);
            cityTemplate = CityTemplateCache.getInstance().getCityTemplateById(nextCityId);
            return cityTemplate.getMAP().get(0);
        } else {
            return maplist.get(mapIndex + 1);
        }
    }

    public ChapterBean createChapterBean(int playerId, int maxMapId,Date lastGainTime) {
        ChapterBean chapterBean = new ChapterBean();
        chapterBean.setPlayerId(playerId);
        chapterBean.setLastGainTime(DateUtils.addMinutes(lastGainTime,-30));
        chapterBean.setMaxMapId(maxMapId);
        return chapterBean;
    }

    public long getLastGainTime(int playerId) {
        ChapterBean chapterBean = ChapterCache.getInstance().getPlayerChapter(playerId);
        if (chapterBean != null && chapterBean.getLastGainTime() != null) {
            return chapterBean.getLastGainTime().getTime();
        }
        return System.currentTimeMillis();
    }

}
