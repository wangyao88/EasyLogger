package com.sxkl.project.easylogger.core;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.sxkl.project.easylogger.common.LoggerConstant;
import com.sxkl.project.easylogger.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

public class FileManager {

    private static final StampedLock LOCK = new StampedLock();

    private FileManager() {}

    private static final class Singleton {
        private static final FileManager FILE_MANAGER = new FileManager();
    }

    public static FileManager getInstance() {
        return Singleton.FILE_MANAGER;
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                FileManager.getInstance().flush();
            }
        }));
    }

    public void addMsg(LogMessage msg) {
        WorkQueueManager.add(msg);
        refresh();
    }

    private void refresh() {
        int currentSize = WorkQueueManager.currentSize();
        if(currentSize >= LoggerConstant.QUEUE_SIZE_THRESHOLD) {
            writeMsgToFile();
        }
    }

    private void writeMsgToFile() {
        ConcurrentLinkedQueue<LogMessage> allMsg = WorkQueueManager.getAllMsg();
        Map<String, List<LogMessage>> map = allMsg.stream().collect(Collectors.groupingBy(LogMessage::getLevel));
        map.forEach((level, logMessages) -> {
            try {
                File file = getFileByLevel(level);
                synchronized (file) {
                    List<String> msgs = logMessages.stream().map(LogMessage::getMsg).collect(Collectors.toList());
                    Files.append(Joiner.on("\n").join(msgs), file, Charsets.UTF_8);
                    double fileSize = FileUtils.getFileSize(file);
                    if(fileSize >= LoggerConstant.FILE_MAX_SIZE) {
                        File to = getCopyFileByLevel(level);
                        Files.copy(file, to);
                        Files.write("", file, Charsets.UTF_8);
                        file.deleteOnExit();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private File getFileByLevel(String level) throws IOException {
        String path = getPath(level);
        File file = new File(path);
        if(!file.exists()) {
            boolean newFile = file.createNewFile();
            if(!newFile) {
                throw new IOException("创建日志文件失败");
            }
        }
        return file;
    }

    private File getCopyFileByLevel(String level) throws IOException {
        String path = getPath(level);
        String suffix = "-" + LocalDate.now().toString() + "-" + System.currentTimeMillis() + LoggerConstant.LOG_SUFFIX;
        path = path.replaceAll(LoggerConstant.LOG_SUFFIX, suffix);
        File file = new File(path);
        if(!file.exists()) {
            boolean newFile = file.createNewFile();
            if(!newFile) {
//                throw new IOException("创建日志文件失败");
            }
        }
        return file;
    }

    private String getPath(String level) {
        String path = LoggerConstant.LOG_FILE_PATH;
        if(LoggerConstant.SPLITE_LEVEL) {
            path = LoggerConstant.getLogPathByLevel(level);
        }
        return path;
    }

    private void flush() {
        writeMsgToFile();
        System.out.println(System.currentTimeMillis());
    }
}
