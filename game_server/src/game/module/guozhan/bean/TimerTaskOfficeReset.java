package game.module.guozhan.bean;

import db.proto.ProtoMessageGuozhan.DBGuoZhanOffice;
import game.module.guozhan.dao.GuozhanCache;
import game.module.guozhan.logic.GuoZhanManager;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

public class TimerTaskOfficeReset implements TimerTask {

	private int officePointVal;

	public TimerTaskOfficeReset(int minePointVal) {
		this.officePointVal = minePointVal;
	}

	@Override
	public void run(Timeout timeout) throws Exception {
		TimerTaskOfficeReset myTask = (TimerTaskOfficeReset)timeout.task();
		int nationIdx = myTask.officePointVal/100;
		int officeIndex = myTask.officePointVal%100;
		DBGuoZhanOffice guoZhanOffice = GuozhanCache.getInstance().getDBGuoZhanOffice();
		if(guoZhanOffice != null) {
			DBGuoZhanOffice.Builder builder = guoZhanOffice.toBuilder();
			builder.getNationsBuilder(nationIdx).getPlayerOfficesBuilder(officeIndex).setIsFighting(false);
			guoZhanOffice = builder.build();
			GuozhanCache.getInstance().setGuozhanOffice(guoZhanOffice);
			GuoZhanManager.getInstance().removeScheduleTimeout(myTask.officePointVal);
		}
	}

	public int getMinePointVal() {
		return officePointVal;
	}

	public void setMinePointVal(int minePointVal) {
		this.officePointVal = minePointVal;
	}

}
