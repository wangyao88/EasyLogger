package com.sxkl.project.easylogger.core;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.sxkl.project.easylogger.common.LoggerConstant;
import com.sxkl.project.easylogger.config.Configer;
import com.sxkl.project.easylogger.message.LogMessage;
import com.sxkl.project.easylogger.timer.LogScheduler;
import com.sxkl.project.easylogger.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
                List<String> msgs = logMessages.stream().map(LogMessage::getMsg).collect(Collectors.toList());
                if(first.get() && msgs.get(0).contains(LoggerConstant.EASY_LOGGER_START_SUCCESS)) {
                    setChanged();
                    notifyObservers();
                    first.set(false);
                }
                String msg = Joiner.on("\n").join(msgs)+"\n";
                Files.append(msg, file, Charsets.UTF_8);
                double fileSize = FileUtils.getFileSize(file);
                if(fileSize >= Configer.getInstance().getFileMaxSize()) {
                    File to = getCopyFileByLevel(level);
                    boolean renameTo = file.renameTo(to);
                    if(!renameTo) {
                        EasyLogger.error("easy-logger内部错误。"+file.getName()+"拆分为"+to.getName()+"失败！");
                    }
                }
            } catch (Exception e) {
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
        String logSuffix = Configer.getInstance().getLogSuffix();
        String path = getPath(level);
        String suffix = new StringBuilder().append("-").append(LocalDate.now().toString()).append("-").append(System.currentTimeMillis()).append(logSuffix).toString();
        path = path.replaceAll(logSuffix, suffix);
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
        String path = Configer.getInstance().getLogFilePath();
        if(Configer.getInstance().spliteLevel()) {
            path = Configer.getInstance().getLogPathByLevel(level);
        }
        return path;
    }

    private void flush() {
        long stamp = LOCK.writeLock();
        try {
            writeMsgToFile();
            EasyLogger.info(LoggerConstant.EASY_LOGGER_STOP_SUCCESS);
            writeMsgToFile();
            System.out.println(System.currentTimeMillis());
        } finally {
            LOCK.unlockWrite(stamp);
        }
    }
}
