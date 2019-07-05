package com.sxkl.project.easylogger.message;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sxkl.project.easylogger.common.LoggerConstant;
import com.sxkl.project.easylogger.common.LoggerLevelEnum;

import java.util.concurrent.ExecutionException;

public class MessageCache {

    private static Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000).build();

    public static String get(StackTraceElement element, LoggerLevelEnum level) {
        String key = new StringBuilder().append(element.getClassName()).append(element.getLineNumber()).toString();
        try {
            return cache.get(key, () -> buildValue(level, element));
        } catch (ExecutionException e) {
            String value = buildValue(level, element);
            cache.put(key, value);
            return value;
        }
    }

    private static String buildValue(LoggerLevelEnum level, StackTraceElement element) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LoggerConstant.WHITE_SPACE)
                     .append("[")
                     .append(level.name().toLowerCase())
                     .append("]")
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(element.getClassName())
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(element.getMethodName())
                     .append(LoggerConstant.WHITE_SPACE)
                     .append(element.getLineNumber())
                     .append(LoggerConstant.WHITE_SPACE)
                     .append("信息：");
        return stringBuilder.toString();
    }
}
