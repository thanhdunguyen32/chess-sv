package game.module.guozhan.bean;

import db.proto.ProtoMessageGuozhan.DBGuoZhanFight;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuoZhanFightManager;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

public class TimerTaskGuozhanCityReset implements TimerTask {

	private int cityIndex;

	private int taskSize;

	public TimerTaskGuozhanCityReset(int cityIndex) {
		this.setCityIndex(cityIndex);
		this.taskSize = 1;
	}

	@Override
	public void run(Timeout timeout) throws Exception {
		TimerTaskGuozhanCityReset myTask = (TimerTaskGuozhanCityReset) timeout.task();
		DBGuoZhanFight guoZhanFight = GuozhanCache.getInstance().getDBGuoZhanFight();
		if (guoZhanFight != null) {
			DBGuoZhanFight.Builder fightBuilder = guoZhanFight.toBuilder();
			fightBuilder.getCitysBuilder(myTask.cityIndex).setInBattle(false);
			guoZhanFight = fightBuilder.build();
			GuozhanCache.getInstance().setGuozhanFight(guoZhanFight);
			GuoZhanFightManager.getInstance().removeScheduleTimeout(myTask.cityIndex);
		}
	}

	public int getCityIndex() {
		return cityIndex;
	}

	public void setCityIndex(int cityIndex) {
		this.cityIndex = cityIndex;
	}

	public int getTaskSize() {
		return taskSize;
	}

	public void setTaskSize(int taskSize) {
		this.taskSize = taskSize;
	}

	public void increaseTaskSize() {
		this.taskSize++;
	}

	public void decreaseTaskSize() {
		this.taskSize--;
	}

}
