package com.sxkl.project.easylogger.timer;

import com.sxkl.project.easylogger.config.Configer;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        executorService.scheduleAtFixedRate(new LogFlusher(), 0, Configer.getInstance().getFlushInterval(), TimeUnit.SECONDS);
    }
}
