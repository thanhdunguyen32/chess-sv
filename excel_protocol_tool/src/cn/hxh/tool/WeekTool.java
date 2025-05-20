package cn.hxh.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeekTool {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		for (int i = 1; i < 60; i++) {
			cal.set(Calendar.WEEK_OF_YEAR, i);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			Date weekStartTime = cal.getTime();
			System.out.print(dateFormat.format(weekStartTime)+"\t");
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			Date weekEndTime = cal.getTime();
			System.out.println(dateFormat.format(weekEndTime));
		}
	}

}
