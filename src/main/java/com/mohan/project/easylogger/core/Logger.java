package com.mohan.project.easylogger.core;

import com.google.common.base.Throwables;
import com.mohan.project.easylogger.common.LoggerConstant;
import com.mohan.project.easylogger.common.LoggerLevelEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
@Builder
public class Logger {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");

    private LocalDateTime dateTime;
    private LoggerLevelEnum level;
    private String threadName;
    private String userName;
    private String ip;
    private String className;
    private String methodName;
    private String message;
    private int costTime;
    private Throwable throwable;

    public String prettyShow() {
        StringBuilder stringBuilder = new StringBuilder(500);
        stringBuilder.append(dateTimeFormatter.format(dateTime))
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(level.name().toLowerCase())
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(threadName)
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(userName)
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(ip)
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(costTime)
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(className)
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(methodName)
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(message);
        if(!Objects.isNull(throwable)) {
            stringBuilder.append("\n完整错误栈信息：\n").append(Throwables.getStackTraceAsString(throwable));
        }
        return stringBuilder.toString();
    }
}
