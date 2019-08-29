package com.mohan.project.easylogger.watcher;

import com.mohan.project.easylogger.common.LoggerLevelEnum;
import com.mohan.project.easylogger.config.Configer;
import com.mohan.project.easylogger.message.MessageManager;

import java.io.File;
import java.net.URL;
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
            URL resource = Configer.class.getClassLoader().getResource("");
            String path = resource.getPath();
            final File file = new File(path);
            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.submit(() -> new FileWatcher(file, false, new EasyLoggerPropertiesFileActionCallback()));
            System.out.println(MessageManager.buildMsg(null, LoggerLevelEnum.INFO, "EasyLogger 正在监视文件夹:{0}的变化", new Object[]{file.getAbsolutePath()}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
