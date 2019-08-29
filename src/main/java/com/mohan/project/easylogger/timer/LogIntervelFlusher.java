package com.mohan.project.easylogger.timer;

import com.mohan.project.easylogger.core.FileManager;

/**
 * EasyLogger定时落盘
 * @author mohan
 * @date 2019-08-29 16:00:23
 */
public class LogIntervelFlusher implements Runnable {

    @Override
    public synchronized void run() {
        FileManager.getInstance().writeMsgToFile();
    }
}
