package com.mohan.project.easylogger.core;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.mohan.project.easylogger.config.Configer;
import com.mohan.project.easylogger.common.LoggerConstant;
import com.mohan.project.easylogger.common.LoggerLevelEnum;
import com.mohan.project.easylogger.message.LogMessage;
import com.mohan.project.easylogger.message.MessageManager;
import com.mohan.project.easylogger.utils.FileUtils;
import com.mohan.project.easytools.file.BannerTools;
import com.mohan.project.easytools.file.FileTools;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;

/**
 * EasyLogger日志记录核心类
 * @author mohan
 * @date 2019-08-29 16:00:23
 */
public class WorkQueueManager {

    private static final StampedLock LOCK = new StampedLock();
    private static volatile String currentQueue = Configer.getInstance().getMasterQueue();
    private static volatile AtomicInteger currentSize = new AtomicInteger(0);
    private static Map<String, ConcurrentLinkedQueue<LogMessage>> pool = Maps.newHashMap();

    static {
        ConcurrentLinkedQueue<LogMessage> queueA = Queues.newConcurrentLinkedQueue();
        String msg = MessageManager.buildMsg(null, LoggerLevelEnum.INFO, LoggerConstant.EASY_LOGGER_START_SUCCESS, null);
        System.out.println(msg);
        queueA.add(new LogMessage(LoggerLevelEnum.INFO, msg));
        String banner = BannerTools.getBanner(LoggerConstant.DEFAULT_BANNER_FILE_NAME, LoggerConstant.PROJECT_NAME);
        System.out.println(banner);
        queueA.add(new LogMessage(LoggerLevelEnum.INFO, banner));
        ConcurrentLinkedQueue<LogMessage> queueB = Queues.newConcurrentLinkedQueue();
        pool.put(Configer.getInstance().getMasterQueue(), queueA);
        pool.put(Configer.getInstance().getReplicaQueue(), queueB);
    }

    private static ConcurrentLinkedQueue<LogMessage> getReplicaQueueMsgs() {
        long stamp = LOCK.writeLock();
        ConcurrentLinkedQueue<LogMessage> result;
        String localQueue = currentQueue;
        try {
            currentQueue = currentQueue.equals(Configer.getInstance().getMasterQueue()) ? Configer.getInstance().getReplicaQueue() : Configer.getInstance().getMasterQueue();
            currentSize.updateAndGet(num -> 0);
            ConcurrentLinkedQueue<LogMessage> queue = pool.get(localQueue);
            result = Queues.newConcurrentLinkedQueue(queue);
            queue.clear();
        } finally {
            LOCK.unlockWrite(stamp);
        }
        return result;
    }

    public static void add(LogMessage msg) {
        long writeStamp = LOCK.writeLock();
        try {
            pool.get(currentQueue).add(msg);
            currentSize.incrementAndGet();
        } finally {
            LOCK.unlockWrite(writeStamp);
        }
    }

    public static int currentSize() {
        long stamp = LOCK.readLock();
        try {
            return currentSize.get();
        } finally {
            LOCK.unlockRead(stamp);
        }
    }

    public static ConcurrentLinkedQueue<LogMessage> getAllMsg() {
        return getReplicaQueueMsgs();
    }
}
