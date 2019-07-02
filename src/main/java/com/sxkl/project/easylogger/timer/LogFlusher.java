package com.sxkl.project.easylogger.timer;

import com.sxkl.project.easylogger.core.FileManager;

import java.time.LocalDateTime;
import java.util.concurrent.locks.StampedLock;

public class LogFlusher implements Runnable {

    private static final StampedLock LOCK = new StampedLock();

    @Override
    public void run() {
        long stamp = LOCK.writeLock();
        try {
            FileManager.getInstance().writeMsgToFile();
            System.out.println(LocalDateTime.now().toString());
        } finally {
            LOCK.unlockWrite(stamp);
        }

    }
}
