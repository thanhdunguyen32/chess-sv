package login.logic;

import login.bean.AnnounceBean;
import login.dao.AnnounceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AnnounceManager {

    private static Logger logger = LoggerFactory.getLogger(AnnounceManager.class);

    private volatile List<AnnounceBean> serverListCache = new ArrayList<AnnounceBean>();

    static class SingletonHolder {
        static AnnounceManager instance = new AnnounceManager();
    }

    public static AnnounceManager getInstance() {
        return SingletonHolder.instance;
    }

    public void reload() {
        logger.info("reload announce list!");
        serverListCache = getAnnounceFromDb();
    }

    public List<AnnounceBean> getAnnounceList() {
        return serverListCache;
    }

    public List<AnnounceBean> getAnnounceFromDb() {
        List<AnnounceBean> retList = AnnounceDao.getInstance().getServerList();
        return retList;
    }

    public void updateAnnounce(int announceId, String announcementContent) {
        boolean findExist = false;
        for (AnnounceBean announceBean : serverListCache) {
            if (announceId == announceBean.getId()) {
                findExist = true;
                announceBean.setContent(announcementContent);
                AnnounceDao.getInstance().updateAnnounce(announceId, announcementContent);
                break;
            }
        }
        if (!findExist) {
            serverListCache.add(new AnnounceBean(announceId,announcementContent));
            AnnounceDao.getInstance().addAnnounce(announceId, announcementContent);
        }
    }

}
