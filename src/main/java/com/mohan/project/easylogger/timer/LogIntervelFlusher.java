package com.mohan.project.easylogger.timer;

import com.mohan.project.easylogger.core.FileManager;

public class LogIntervelFlusher implements Runnable {

    @Override
    public synchronized void run() {
        FileManager.getInstance().writeMsgToFile();
    }
}
