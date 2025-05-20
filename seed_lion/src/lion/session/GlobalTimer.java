package lion.session;

import java.sql.Time;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

public class GlobalTimer {

    private Timer mTimer;

    static class SingletonHolder {
        static GlobalTimer instance = new GlobalTimer();
    }

    public static GlobalTimer getInstance() {
        return SingletonHolder.instance;
    }

    private GlobalTimer() {
        mTimer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS, 1024);
        // mTimer = new HierarchicalWheelTimer(10, 60, 3);
    }

    /**
     * 以秒为单位，加入定时运算
     *
     * @param task
     * @param delay
     * @return
     */
    public Timeout newTimeout(TimerTask task, long delay) {
        return mTimer.newTimeout(task, delay, TimeUnit.SECONDS);
    }

    public Timeout newBuildingTask(TimerTask task, long delayTimeMili) {
        return mTimer.newTimeout(task, delayTimeMili, TimeUnit.MILLISECONDS);
    }

    public Set<Timeout> stop() {
        return mTimer.stop();
    }

    public void shutdown() {
        if (mTimer != null) {
            mTimer.stop();
        }
    }

}
