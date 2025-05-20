package game.common;

import java.util.Date;

public class TimeUtils {

    public static final int currentTimeSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static int getMinuteForTime(int seconds) {
        int minutes = seconds / 60;
        if (seconds % 60 > 0) {
            minutes++;
        }
        return minutes;
    }

    public static int date2Seconds(Date dateTime){
        return (int)(dateTime.getTime()/1000);
    }

}
