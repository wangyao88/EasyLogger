package com.sxkl.project.easylogger.core;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.sxkl.project.easylogger.common.LoggerConstant;
import com.sxkl.project.easylogger.config.Configer;
import com.sxkl.project.easylogger.message.LogMessage;
import com.sxkl.project.easylogger.timer.LogScheduler;
import com.sxkl.project.easylogger.utils.FileUtils;
import com.sxkl.project.easylogger.watcher.FileWatcherObserver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

public class FileManager extends Observable {

    private static final StampedLock LOCK = new StampedLock();
    private volatile AtomicBoolean first = new AtomicBoolean(true);

    private FileManager() {
        addObserver(LogScheduler.getInstance());
        addObserver(new FileWatcherObserver());
    }

    private static final class Singleton {
        private static final FileManager FILE_MANAGER = new FileManager();
    }

    public static FileManager getInstance() {
        return Singleton.FILE_MANAGER;
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> FileManager.getInstance().flush()));
    }

    public void addMsg(LogMessage msg) {
        long stamp = LOCK.writeLock();
        try {
            if(first.get()) {
                ConcurrentLinkedQueue<LogMessage> allMsg = WorkQueueManager.getAllMsg();
                LogMessage logMessage = allMsg.peek();
                if(!Objects.isNull(logMessage) && logMessage.getMsg().contains(LoggerConstant.EASY_LOGGER_START_SUCCESS)) {
                    setChanged();
                    notifyObservers();
                    first.set(false);
                }
            }
            WorkQueueManager.add(msg);
            refresh();
        } finally {
            LOCK.unlockWrite(stamp);
        }
    }

    private void refresh() {
        int currentSize = WorkQueueManager.currentSize();
        if(currentSize >= Configer.getInstance().getQueueSizeThreshold()) {
            writeMsgToFile();
        }
    }

    public void writeMsgToFile() {
        ConcurrentLinkedQueue<LogMessage> allMsg = WorkQueueManager.getAllMsg();
        if(allMsg.isEmpty()) {
            return;
        }
        Map<String, List<LogMessage>> map = allMsg.stream().collect(Collectors.groupingBy(LogMessage::getLevel));
        map.forEach((level, logMessages) -> {
            try {
                File file = getFileByLevel(level);
                List<String> messages = logMessages.stream().map(LogMessage::getMsg).collect(Collectors.toList());
                String msg = Joiner.on("\n").join(messages)+"\n";
                Files.append(msg, file, Charsets.UTF_8);
                double fileSize = FileUtils.getFileSize(file);
                if(fileSize >= Configer.getInstance().getFileMaxSize()) {
                    File to = getCopyFileByLevel(level);
                    boolean renameTo = file.renameTo(to);
                    if(!renameTo) {
                        String errorMsg = new StringBuilder().append("easy-logger内部错误。")
                                                             .append(file.getName())
                                                             .append("拆分为")
                                                             .append(to.getName())
                                                             .append("失败！")
                                                             .toString();
                        EasyLogger.error(errorMsg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                EasyLogger.error(e, "easy-logger内部错误。");
            }
        });
    }

    private File getFileByLevel(String level) throws IOException {
        String path = getPath(level);
        File file = new File(path);
        if(!file.exists()) {
            boolean newFile = file.createNewFile();
            if(!newFile) {
                EasyLogger.error("easy-logger内部错误。创建日志文件"+file.getName()+"失败");
            }
        }
        return file;
    }

    private File getCopyFileByLevel(String level) throws IOException {
        FileUtils.mkdirForLogPreffix();
        String logSuffix = Configer.getInstance().getLogSuffix();
        String path = new StringBuilder(Configer.getInstance().getLogPreffix()).append(level).append("-")
                                           .append(LocalDate.now().toString())
                                           .append("-")
                                           .append(System.currentTimeMillis())
                                           .append(logSuffix)
                                           .toString();
        File file = new File(path);
        if(!file.exists()) {
            boolean newFile = file.createNewFile();
            if(!newFile) {
                EasyLogger.error("easy-logger内部错误。创建日志文件"+file.getName()+"失败");
            }
        }
        return file;
    }

    private String getPath(String level) {
        FileUtils.mkdirForLogPreffix();
        String path = Configer.getInstance().getLogFilePath();
        if(Configer.getInstance().spliteLevel()) {
            path = Configer.getInstance().getLogPathByLevel(level);
        }
        return path;
    }

    private synchronized void flush() {
        EasyLogger.info(LoggerConstant.EASY_LOGGER_STOP_SUCCESS);
        writeMsgToFile();
    }
}
