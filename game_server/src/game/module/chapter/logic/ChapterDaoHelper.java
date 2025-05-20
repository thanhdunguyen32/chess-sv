package game.module.chapter.logic;

import game.GameServer;
import game.module.chapter.bean.ChapterBean;
import game.module.chapter.dao.ChapterDao;

public class ChapterDaoHelper {

	public static void asyncInsertChapterBean(final ChapterBean onlineGiftBean) {
		GameServer.executorService.execute(() -> ChapterDao.getInstance().addChapterBean(onlineGiftBean));
	}

	public static void asyncUpdateChapterBean(final ChapterBean onlineGiftBean) {
		GameServer.executorService.execute(() -> ChapterDao.getInstance().updateChapterBean(onlineGiftBean));
	}

	public static void asyncRemoveChapterBean(int onlineGiftId) {
		GameServer.executorService.execute(() -> ChapterDao.getInstance().removeChapterBean(onlineGiftId));
	}

}
