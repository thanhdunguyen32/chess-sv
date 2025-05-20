package game.common;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间工具类
 * 
 * @author zhangning
 * @Date 2014年12月17日 下午5:46:17
 *
 */
public class DateCommonUtils {
	private static Logger logger = LoggerFactory.getLogger(DateCommonUtils.class);

	/**
	 * Check两个日期之间
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean betweenDate(Date startTime, Date endTime) {
		long nowTimeMillis = System.currentTimeMillis();
		return nowTimeMillis >= startTime.getTime() && nowTimeMillis <= endTime.getTime();
	}

	public static int dayDiff(Date time1, Date tim2) {
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(time1);
		int year1 = aCalendar.get(Calendar.YEAR);
		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(tim2);
		int year2 = aCalendar.get(Calendar.YEAR);
		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
		return year2 * 365 + day2 - (year1 * 365 + day1);
	}

	/**
	 * Check两个小时之间
	 * 
	 * @param hour1
	 * @param hour2
	 * @return
	 */
	public static boolean betweenHour(int hour1, int hour2) {
		Calendar calendar = Calendar.getInstance();
		int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
		int nowMillisSec = calendar.get(Calendar.MILLISECOND);
		if ((nowHour >= hour1 && nowHour < hour2) || (nowHour == hour2 && nowMillisSec == 0)) {
			return true;
		}

		return false;
	}

	/**
	 * 距离到达某时刻的秒值
	 * 
	 * @param monmentHour
	 * @return
	 */
	public static int getSecsReachMoment(int monmentHour) {
		int secs = 0;
		Calendar calendar = Calendar.getInstance();
		int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
		int millsSecs = calendar.get(Calendar.MILLISECOND);
		Date monmentDate = getCalTime(monmentHour);

		if ((nowHour < monmentHour) || (nowHour == monmentHour && millsSecs == 0)) {
			secs = (int) ((monmentDate.getTime() - System.currentTimeMillis()) / 1000);
		} else {
			long tomorrowMonment = monmentDate.getTime() + 24 * 60 * 60 * 1000;
			secs = (int) ((tomorrowMonment - System.currentTimeMillis()) / 1000);
		}

		return secs;
	}

	/**
	 * 距离到达某时刻的秒值
	 * 
	 * @param monmentDate
	 * @return
	 */
	public static int getSecsReachMonment(Date monmentDate) {
		return (int) ((monmentDate.getTime() - System.currentTimeMillis()) / 1000);
	}

	/**
	 * 小时
	 * 
	 * @return
	 */
	public static int getHour() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 判断是否为同一天
	 * 
	 * @param lastRestTime
	 *            ：上次更新的时间
	 * @param hour
	 *            ：要比较的小时数（6 表示每日6时更新）
	 * @return
	 */
	public static boolean isSameDay(Date lastRestTime, int hour) {
		if (lastRestTime != null) {
			Date lastOpenNewTime = DateUtils.addHours(lastRestTime, -hour);
			Date nowNewTime = DateUtils.addHours(new Date(), -hour);
			return DateUtils.isSameDay(lastOpenNewTime, nowNewTime);
		}
		return false;
	}

	/**
	 * 判断是否为同一天
	 * 
	 * @param lastRestTime
	 *            : 毫秒
	 * @param hour
	 * @return
	 */
	public static boolean isSameDay(long lastRestTime, int hour) {
		if (lastRestTime > 0) {
			Date lastResetDate = new Date(lastRestTime);

			return isSameDay(lastResetDate, hour);
		}
		return false;
	}

	/**
	 * 判断是否小于某个时间段
	 * 
	 * @param time
	 * @param week
	 * @param hour
	 * @return
	 * @throws ParseException
	 */
	public static boolean isLessTime(Date time, int month, int week, int hour) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_WEEK, week);
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			long resetTime = cal.getTimeInMillis();
			return time.getTime() - resetTime < 0;
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return false;
	}

	/**
	 * 判断今天周几
	 * 
	 * @param dayOfWeek
	 * @param hour
	 * @return
	 */
	public static boolean isDayOfWeek(int dayOfWeek) {
		try {
			Calendar cal = Calendar.getInstance();
			int nowWeek = cal.get(Calendar.DAY_OF_WEEK);
			return nowWeek == dayOfWeek;
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return false;
	}

	/**
	 * 根据小时, 规定时间
	 * 
	 * @param hour
	 * @return
	 */
	public static Date getCalTime(int hour) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	/**
	 * 根据小时, 规定时间
	 * 
	 * @param hour
	 * @return
	 */
	public static Date getLastCalTime(int hour) {
		Date time = getCalTime(hour);
		long lastDay = time.getTime() - 24 * 60 * 60 * 1000L;

		return new Date(lastDay);
	}

	/**
	 * 是否同一个月
	 * 
	 * @param signNewTime
	 * @return
	 */
	public static boolean isSameMonth(Date signNewTime) {
		if (signNewTime != null) {
			final Calendar cal1 = Calendar.getInstance();
			final Calendar cal2 = Calendar.getInstance();
			cal2.setTime(signNewTime);
			return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
					&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
					&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH));
		}
		return false;
	}

	/**
	 * 是否同一个小时点
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isSameHour(Date time) {
		if (time != null) {
			final Calendar cal1 = Calendar.getInstance();
			final Calendar cal2 = Calendar.getInstance();
			cal2.setTime(time);
			return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
					&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
					&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))
					&& cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
					&& cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY);
		}
		return false;
	}

	/**
	 * 是否同一个小时点
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isYesterdayHour(Date time, int hour) {
		if (time != null) {
			final Calendar cal1 = Calendar.getInstance();
			cal1.setTime(time);

			Date nowHour = getCalTime(hour);
			long yesterdayHour = nowHour.getTime() - 24 * 60 * 60 * 1000;
			Date yesterdayDate = new Date(yesterdayHour);
			final Calendar cal2 = Calendar.getInstance();
			cal2.setTime(yesterdayDate);

			return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
					&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
					&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))
					&& cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
					&& cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY);
		}
		return false;
	}

	/**
	 * 取得当月天数
	 * 
	 * @return
	 */
	public static int getCurrentMonthLastDay() {
		Calendar a = Calendar.getInstance();
		int maxDate = a.getActualMaximum(Calendar.DAY_OF_MONTH);
		return maxDate;
	}

	/**
	 * 月份
	 * 
	 * @return
	 */
	public static int getCurrentMonth() {
		Calendar a = Calendar.getInstance();
		return a.get(Calendar.MONTH) + 1;
	}

	public static void main(String[] args) {
//		 System.out.println(getCurrentMonthLastDay());
//		 long system = System.currentTimeMillis() - 2592000000L;
//		 System.out.println(isSameMonth(new Date(system)));
//		 System.out.println(getCurrentMonth());
	}

}
