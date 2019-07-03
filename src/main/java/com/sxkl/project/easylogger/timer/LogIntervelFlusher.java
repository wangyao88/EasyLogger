package com.sxkl.project.easylogger.timer;

import com.sxkl.project.easylogger.core.FileManager;

import java.util.concurrent.locks.StampedLock;

public class LogIntervelFlusher implements Runnable {

    private static final StampedLock LOCK = new StampedLock();

    @Override
    public void run() {
        long stamp = LOCK.writeLock();
        try {
            FileManager.getInstance().writeMsgToFile();
        } finally {
            LOCK.unlockWrite(stamp);
        }

    }
}
