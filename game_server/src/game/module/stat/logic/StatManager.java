package game.module.stat.logic;

import java.util.List;

import game.module.pay.bean.TopPayPlayerBean;
import game.module.pay.dao.PaymentLogDao;
import game.module.stat.dao.LogLoginDao;
import game.module.user.dao.PlayerDao;
import lion.netty4.core.MyIoExecutor;

public class StatManager {

	public static int[] getMainStatParams() {
		// 同时在线
		int onlineNum = MyIoExecutor.getOnlineCount();
		// 新增
		int newPlayer = PlayerDao.getInstance().getNewPlayerCount();
		// 活跃
		int activePlayer = LogLoginDao.getInstance().getActivePlayerCount();
		// 新增付费
		int newPayUsers = PaymentLogDao.getInstance().getTodayNewPayPlayers();
		// 付费人数
		List<Integer> payPlayers = PaymentLogDao.getInstance().getTodayPayPlayers();
		// 昨日留存
		List<Integer> yesterdayRetain = PlayerDao.getInstance().getYesterdayRetain();
		int today_login_count = 0;
		int yesterday_create_count = 0;
		if (yesterdayRetain != null && yesterdayRetain.size() == 2) {
			today_login_count = yesterdayRetain.get(0);
			yesterday_create_count = yesterdayRetain.get(1);
		}
		//
		int payPlayerCount = 0;
		int paySumVal = 0;
		if (payPlayers != null && payPlayers.size() == 2) {
			payPlayerCount = payPlayers.get(0);
			paySumVal = payPlayers.get(1);
		}
		return new int[] { onlineNum, newPlayer, activePlayer, newPayUsers, payPlayerCount, paySumVal,
				today_login_count, yesterday_create_count };
	}
	
	public static List<TopPayPlayerBean> getTopPayPlayerBeans(){
		return PaymentLogDao.getInstance().getTopPayPlayers();
	}
	
}
