package game.module.mine.bean;

import game.module.mine.logic.MineManager;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

public class TimerTaskMineReward implements TimerTask {

	private int minePointVal;
	
	public TimerTaskMineReward(int minePointVal) {
		this.minePointVal = minePointVal;
	}

	@Override
	public void run(Timeout timeout) throws Exception {
		TimerTaskMineReward myTask = (TimerTaskMineReward)timeout.task();
		MineManager.getInstance().mineSendReward(myTask.minePointVal);
	}

	public int getMinePointVal() {
		return minePointVal;
	}

	public void setMinePointVal(int minePointVal) {
		this.minePointVal = minePointVal;
	}

}
