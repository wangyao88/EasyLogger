package com.mohan.project.easylogger.timer;

import com.mohan.project.easylogger.config.Configer;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * EasyLogger定时任务类
 * @author mohan
 * @date 2019-08-29 16:00:23
 */
public class LogScheduler implements Observer {

    private LogScheduler() {}

    private static final class Singleton {
        private static final LogScheduler LOG_SCHEDULER = new LogScheduler();
    }

    public static LogScheduler getInstance() {
        return Singleton.LOG_SCHEDULER;
    }

    @Override
    public void update(Observable observable, Object arg) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new LogIntervelFlusher(), 10, Configer.getInstance().getFlushInterval(), TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new LogDayMerger(), 10, Configer.getInstance().getMergeInterval(), TimeUnit.HOURS);
        if(Configer.getInstance().dayRolling()) {
            executorService.scheduleAtFixedRate(new LogDayRoller(), 10, 1, TimeUnit.SECONDS);
        }
    }

}
