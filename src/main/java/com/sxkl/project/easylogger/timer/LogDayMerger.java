package com.sxkl.project.easylogger.timer;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.sxkl.project.easylogger.config.Configer;
import com.sxkl.project.easylogger.core.EasyLogger;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogDayMerger implements Runnable {

    private static final Pattern pattern = Pattern.compile("\\d{13}");

    @Override
    public void run() {
        Map<String, List<File>> groupedFiles = getGroupedFiles();
        mergeFiles(groupedFiles);
    }

    private Map<String, List<File>> getGroupedFiles() {
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
                    boolean match = allLogNames.stream().anyMatch(logName -> nameWithoutExtension.startsWith(logName) && !name.equals(logName + logSuffix));
                    return match && logSuffix.equals("." + fileExtension);
                }
            };
            File[] files = source.listFiles(fileFilter);
            Map<String, List<File>> fileMap = Arrays.stream(files).filter(file->{
                String nameWithoutExtension = Files.getNameWithoutExtension(file.getName());
                int startIndex = nameWithoutExtension.lastIndexOf("-") + 1;
                int endIndex = nameWithoutExtension.length();
                nameWithoutExtension = nameWithoutExtension.substring(startIndex, endIndex);
                return pattern.matcher(nameWithoutExtension).matches();
            }).collect(Collectors.groupingBy(file -> {
                String name = file.getName();
                int index = name.lastIndexOf("-");
                name = name.substring(0, index);
                return name;
            }));
            return fileMap;
        }
        return Maps.newHashMap();
    }

    private void mergeFiles(Map<String, List<File>> fileMap) {
        String logPreffix = Configer.getInstance().getLogPreffix();
        String logSuffix = Configer.getInstance().getLogSuffix();
        fileMap.forEach((subFileName, fileList) -> {
            String mergeFileName = logPreffix+subFileName+logSuffix;
            File mergeFile = new File(mergeFileName);
            if(!mergeFile.exists()) {
                try {
                    boolean newFile = mergeFile.createNewFile();
                } catch (IOException e) {
                    EasyLogger.error(e, "创建按天合并日志文件的文件失败！");
                }
            }
            if(fileList.size() == 1) {
                File temp = fileList.get(0);
                if(!temp.getName().equals(mergeFile.getName())) {
                    temp.renameTo(mergeFile);
                    return;
                }
            }
            if(fileList.size() > 1) {
                try(FileChannel resultFileChannel = new FileOutputStream(mergeFile, true).getChannel()) {
                    for (File file : fileList) {
                        if (file.getName().equals(mergeFile.getName())) {
                            continue;
                        }
                        try (FileChannel blk = new FileInputStream(file).getChannel()) {
                            resultFileChannel.transferFrom(blk, resultFileChannel.size(), blk.size());
                            file.delete();
                        } catch (IOException e) {
                            EasyLogger.error(e, "按天合并日志文件失败！");
                        }
                    }
                } catch (Exception e) {
                    EasyLogger.error(e, "按天合并日志文件失败！");
                }
            }
        });
    }
}
