package game.module.chapter.dao;

import game.module.chapter.bean.ChapterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务缓存数据
 *
 * @author zhangning
 */
public class ChapterCache {

    private static Logger logger = LoggerFactory.getLogger(ChapterCache.class);

    static class SingletonHolder {
        static ChapterCache instance = new ChapterCache();
    }

    public static ChapterCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 任务进度缓存<br/>
     * Key：玩家唯一ID<br/>
     * Value：每个玩家的任务缓存
     */
    private Map<Integer, ChapterBean> playerTaskCache = new ConcurrentHashMap<>();

    /**
     * 初始化数据到缓存中
     *
     * @param playerId
     */
    public void loadFromDb(int playerId) {
        ChapterBean myTaskAll = playerTaskCache.get(playerId);
        if (myTaskAll == null) {
            ChapterBean chapterBean = ChapterDao.getInstance().getPlayerChapterBean(playerId);
            if (chapterBean != null) {
                playerTaskCache.put(playerId, chapterBean);
            }
        }
        logger.info("Player ID: ChapterBean cache data of {} is loaded successfully", playerId);
    }

    public void addChapterBean(ChapterBean chapterBean) {
        playerTaskCache.put(chapterBean.getPlayerId(), chapterBean);
    }

    /**
     * 获取玩家自身的任务缓存
     *
     * @param playerId
     * @return
     */
    public ChapterBean getPlayerChapter(int playerId) {
        ChapterBean chapterBeans = playerTaskCache.get(playerId);
        return chapterBeans;
    }

    /**
     * 下线删除缓存
     *
     * @param playerId
     */
    public void remove(int playerId) {
        playerTaskCache.remove(playerId);
    }

}
