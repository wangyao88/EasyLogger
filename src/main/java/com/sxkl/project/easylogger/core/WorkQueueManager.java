package com.sxkl.project.easylogger.core;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.sxkl.project.easylogger.common.LoggerLevelEnum;
import com.sxkl.project.easylogger.config.Configer;
import com.sxkl.project.easylogger.message.LogMessage;
import com.sxkl.project.easylogger.message.MessageManager;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;

public class WorkQueueManager {

    private static final StampedLock LOCK = new StampedLock();
    private static volatile String currentQueue = Configer.getInstance().getMasterQueue();
    private static volatile AtomicInteger currentSize = new AtomicInteger(0);
    private static Map<String, ConcurrentLinkedQueue<LogMessage>> pool = Maps.newHashMap();

    static {
        ConcurrentLinkedQueue<LogMessage> queueA = Queues.newConcurrentLinkedQueue();
        String msg = MessageManager.buildMsg(null, LoggerLevelEnum.INFO, "easy-logger成功启动服务！", null);
        queueA.add(new LogMessage(LoggerLevelEnum.INFO, msg));
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
