package test.timer;

import java.util.Calendar;
import java.util.Date;

public class DateTest {

	public static void main(String[] args) {
		new DateTest().test2();
	}

	public void test1() {
		Calendar nowCal = Calendar.getInstance();
		nowCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		System.out.println(nowCal.getTime());
	}

	public void test2() {
		Date expireDate = new Date(1426160824L * 1000);
		//1426160824
		System.out.println(expireDate);
	}

}
