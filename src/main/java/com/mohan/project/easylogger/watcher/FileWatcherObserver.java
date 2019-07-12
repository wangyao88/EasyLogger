package com.mohan.project.easylogger.watcher;

import com.mohan.project.easylogger.config.Configer;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FileWatcherObserver implements Observer {

    @Override
    public void update(Observable observable, Object arg) {
        try {
            URL resource = Configer.class.getClassLoader().getResource("");
            String path = resource.getPath();
            final File file = new File(path);
            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.submit(() -> new FileWatcher(file, false, new EasyLoggerPropertiesFileActionCallback()));
            System.out.println("正在监视文件夹:" + file.getAbsolutePath() + "的变化");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}