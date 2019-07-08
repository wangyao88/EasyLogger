package com.sxkl.project.easylogger.timer;

import com.sxkl.project.easylogger.core.FileManager;

public class LogIntervelFlusher implements Runnable {

    @Override
    public synchronized void run() {
        FileManager.getInstance().writeMsgToFile();
    }
}
