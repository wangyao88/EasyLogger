package com.sxkl.project.easylogger.message;

import com.google.common.base.Throwables;
import com.sxkl.project.easylogger.common.LoggerConstant;
import com.sxkl.project.easylogger.common.LoggerLevelEnum;
import net.sf.cglib.proxy.MethodProxy;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MessageManager {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");

    public static String buildMsg(Throwable throwable, LoggerLevelEnum level, String msg, Object[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String time = dateTimeFormatter.format(localDateTime);
        StackTraceElement element = getStackTraceElement();
        StringBuilder stringBuilder = new StringBuilder(time);
        stringBuilder.append(MessageCache.get(element, level)).append(buildMsg(throwable, msg, args));
        return stringBuilder.toString();
    }

    private static StackTraceElement getStackTraceElement() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int length = stackTrace.length;
        StackTraceElement lastElement = stackTrace[length - 1];
        if(lastElement.getClassName().equals(LoggerConstant.Thread_name)) {
            lastElement = stackTrace[length - 2];
        }
        return lastElement;
    }

    private static String buildMsg(Throwable throwable, String msg, Object[] args) {
        String format = MessageFormat.format(msg, args);
        StringBuilder stringBuilder = new StringBuilder(format);
        if(!Objects.isNull(throwable)) {
            stringBuilder.append("\n完成错误栈信息：\n").append(Throwables.getStackTraceAsString(throwable));
        }
        return stringBuilder.toString();
    }

    public static String buildProxyMsg(Object target, MethodProxy methodProxy, boolean needWriteToConsole, LoggerLevelEnum level, Throwable throwable, String msg) {
        return null;
    }
}
