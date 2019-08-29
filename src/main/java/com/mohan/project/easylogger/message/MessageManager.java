package com.mohan.project.easylogger.message;

import com.google.common.base.Throwables;
import com.mohan.project.easylogger.common.LoggerConstant;
import com.mohan.project.easylogger.common.LoggerLevelEnum;
import com.mohan.project.easylogger.core.Logger;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * EasyLogger日志消息解析类
 * @author mohan
 * @date 2019-08-29 16:00:23
 */
public class MessageManager {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");

    public static String buildMsg(Throwable throwable, LoggerLevelEnum level, String msg, Object[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String time = dateTimeFormatter.format(localDateTime);
        StringBuilder stringBuilder = new StringBuilder(time);
        stringBuilder.append(LoggerConstant.WHITE_SPACE)
                     .append(level.name().toLowerCase())
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(buildMsg(throwable, msg, args));
        return stringBuilder.toString();
    }

    private static String buildMsg(Throwable throwable, String msg, Object[] args) {
        String format = MessageFormat.format(msg, args);
        StringBuilder stringBuilder = new StringBuilder(format);
        if(!Objects.isNull(throwable)) {
            stringBuilder.append("\n完整错误栈信息：\n").append(Throwables.getStackTraceAsString(throwable));
        }
        return stringBuilder.toString();
    }

    public static String buildMsg(Logger logger) {
        return logger.prettyShow();
    }
}
