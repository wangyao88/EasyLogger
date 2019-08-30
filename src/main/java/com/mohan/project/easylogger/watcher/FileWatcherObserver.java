package com.mohan.project.easylogger.watcher;

import com.mohan.project.easylogger.common.LoggerLevelEnum;
import com.mohan.project.easylogger.message.MessageManager;
import com.mohan.project.easytools.file.PathTools;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * EasyLogger文件监控观察者， 启动时被触发
 * @author mohan
 * @date 2019-08-29 16:00:23
 */
public class FileWatcherObserver implements Observer {

    @Override
    public void update(Observable observable, Object arg) {
        try {
            String path = PathTools.getClassesPath();
            final File file = new File(path);
            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.submit(() -> new FileWatcher(file, false, new EasyLoggerPropertiesFileActionCallback()));
            System.out.println(MessageManager.buildMsg(null, LoggerLevelEnum.INFO, "EasyLogger 正在监视文件夹:{0}的变化", new Object[]{file.getAbsolutePath()}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
