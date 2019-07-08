package com.sxkl.project.easylogger.timer;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.sxkl.project.easylogger.common.LoggerConstant;
import com.sxkl.project.easylogger.common.LoggerLevelEnum;
import com.sxkl.project.easylogger.config.Configer;
import com.sxkl.project.easylogger.core.EasyLogger;
import com.sxkl.project.easylogger.utils.FileUtils;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class LogDayRoller implements Runnable {

    private static final Pattern pattern = Pattern.compile("\\d{13}");

    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        if(hour == 23 && minute == 59 && second == 59) {
            String date = Joiner.on(LoggerConstant.BLANK).join(Lists.newArrayList(LocalDate.now().toString(), "-", System.currentTimeMillis()));
            doRolling(date);
        }
    }

    private synchronized void doRolling(String date) {
        FileUtils.mkdirForLogPreffix();
        String logPreffix = Configer.getInstance().getLogPreffix();
        String logSuffix = Configer.getInstance().getLogSuffix();
        roolingByLevel(logPreffix, logSuffix, LoggerLevelEnum.INFO, date);
        roolingByLevel(logPreffix, logSuffix, LoggerLevelEnum.DEBUG, date);
        roolingByLevel(logPreffix, logSuffix, LoggerLevelEnum.WARN, date);
        roolingByLevel(logPreffix, logSuffix, LoggerLevelEnum.ERROR, date);
    }

    private void roolingByLevel(String logPreffix, String logSuffix, LoggerLevelEnum level, String date) {
        String info = level.name().toLowerCase();
        String path = Joiner.on(LoggerConstant.BLANK).join(Lists.newArrayList(logPreffix, info, logSuffix));
        File file = new File(path);
        if(file.exists() && file.isFile()) {
            String toPath = Joiner.on(LoggerConstant.BLANK).join(Lists.newArrayList(logPreffix, info, "-", date, logSuffix));
            File to = new File(toPath);
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
    }
}
