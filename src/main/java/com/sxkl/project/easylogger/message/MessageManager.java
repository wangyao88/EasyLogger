package com.sxkl.project.easylogger.message;

import com.google.common.base.Throwables;
import com.sxkl.project.easylogger.common.LoggerConstant;
import com.sxkl.project.easylogger.common.LoggerLevelEnum;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MessageManager {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");

    public static String buildMsg(Exception e, LoggerLevelEnum level, String msg, Object[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime localDateTime = LocalDateTime.now();
        String time = dateTimeFormatter.format(localDateTime);
        stringBuilder.append(time)
                .append(LoggerConstant.WHITE_SPACE)
                .append("[")
                .append(level.name().toLowerCase())
                .append("]")
                .append(LoggerConstant.WHITE_SPACE)
                .append(getClassMethodLine())
                .append(LoggerConstant.WHITE_SPACE)
                .append("信息：")
                .append(buildMsg(e, msg, args));
        return stringBuilder.toString();
    }

    private static String getClassMethodLine() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int length = stackTrace.length;
        StackTraceElement lastElement = stackTrace[length - 1];
        if(lastElement.getClassName().equals(LoggerConstant.Thread_name)) {
            lastElement = stackTrace[length - 2];
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(lastElement.getClassName())
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(lastElement.getMethodName())
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(lastElement.getLineNumber());
        return stringBuilder.toString();
    }

    private static String buildMsg(Exception e, String msg, Object[] args) {
        String format = MessageFormat.format(msg, args);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(format);
        if(!Objects.isNull(e)) {
            stringBuilder.append("\n完成错误栈信息：\n").append(Throwables.getStackTraceAsString(e));
        }
        return stringBuilder.toString();
    }
}
