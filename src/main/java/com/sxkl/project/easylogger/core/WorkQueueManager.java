package com.sxkl.project.easylogger.core;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.sxkl.project.easylogger.common.LoggerConstant;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;

public class WorkQueueManager {

    private static final StampedLock LOCK = new StampedLock();
    private static volatile String currentQueue = LoggerConstant.MASTER_QUEUE;
    private static volatile AtomicInteger currentSize = new AtomicInteger(0);
    private static Map<String, ConcurrentLinkedQueue<LogMessage>> pool = Maps.newHashMap();

    static {
        ConcurrentLinkedQueue<LogMessage> queueA = Queues.newConcurrentLinkedQueue();
        ConcurrentLinkedQueue<LogMessage> queueB = Queues.newConcurrentLinkedQueue();
        pool.put(LoggerConstant.MASTER_QUEUE, queueA);
        pool.put(LoggerConstant.REPLICA_QUEUE, queueB);
    }

    private static ConcurrentLinkedQueue<LogMessage> getReplicaQueueMsgs() {
        long stamp = LOCK.writeLock();
        ConcurrentLinkedQueue<LogMessage> result;
        String localQueue = currentQueue;
        try {
            currentQueue = currentQueue.equals(LoggerConstant.MASTER_QUEUE) ? LoggerConstant.REPLICA_QUEUE : LoggerConstant.MASTER_QUEUE;
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
