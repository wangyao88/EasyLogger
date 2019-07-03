package com.sxkl.project.easylogger.timer;

import com.google.common.io.Files;
import com.sxkl.project.easylogger.config.Configer;
import com.sxkl.project.easylogger.message.LogMessage;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LogDaylyRoller implements Runnable {

    private static LocalDateTime preRunTime;

    @Override
    public void run() {
        String logPreffix = Configer.getInstance().getLogPreffix();
        String logSuffix = Configer.getInstance().getLogSuffix();
        File dir = new File(logPreffix);


        preRunTime = LocalDateTime.now();
    }

    public static void main(String[] args) {
        String logPreffix = Configer.getInstance().getLogPreffix();
        String logSuffix = Configer.getInstance().getLogSuffix();
        Set<String> allLogNames = Configer.getInstance().getAllLogNames();
        File source = new File(logPreffix);
        if(source.isDirectory()) {
            FileFilter fileFilter = new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isFile() && isLogFile(file);
                }

                private boolean isLogFile(File file) {
                    String name = file.getName();
                    String nameWithoutExtension = Files.getNameWithoutExtension(name);
                    String fileExtension = Files.getFileExtension(name);
                    boolean match = allLogNames.stream().anyMatch(logName-> nameWithoutExtension.startsWith(logName) && !name.equals(logName+logSuffix));
                    return match && logSuffix.equals("."+fileExtension);
                }
            };
            File[] files = source.listFiles(fileFilter);
            Map<String, List<File>> fileMap = Arrays.stream(files).collect(Collectors.groupingBy(file -> {
                String name = file.getName();
                int index = name.lastIndexOf("-");
                name = name.substring(0, index);
                return name;
            }));
            System.out.println(fileMap);
            fileMap.forEach((subFileName, fileList) -> {
                String mergeFileName = logPreffix+subFileName+logSuffix;
                File mergeFile = new File(mergeFileName);
                if(!mergeFile.exists()) {
                    try {
                        boolean newFile = mergeFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(fileList.size() == 1) {

                }
            });
        }
    }
}
