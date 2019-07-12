package com.mohan.project.easylogger.config;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mohan.project.easylogger.common.LoggerConstant;
import com.mohan.project.easylogger.common.LoggerLevelEnum;
import com.mohan.project.easylogger.utils.FileUtils;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Configer {

//    private String masterQueue;
//    private String replicaQueue;
//    private int queueSizeThreshold;
//    private int flushInterval;
//    private int mergeInterval;
//    private double fileMaxSize;
//    private String infoFilePath;
//    private String debugFilePath;
//    private String warnFilePath;
//    private String errorFilePath;
//    private String logFilePath;
//    private String logPreffix;
//    private String logSuffix;
//    private boolean dayRolling;
//    private boolean spliteLevel;
//    private boolean needWriteInfoToConsole;
//    private boolean needWriteWarnToConsole;
//    private boolean needWriteDebugToConsole;
//    private boolean needWriteErrorToConsole;


    private final Map<String, String> LOG_PATH = Maps.newHashMap();
    private Map<String, Object> propertiesMap;

    private Configer() {
        refreshProperties();
        LOG_PATH.put(LoggerLevelEnum.INFO.name().toLowerCase(), getInfoFilePath());
        LOG_PATH.put(LoggerLevelEnum.DEBUG.name().toLowerCase(), getDebugFilePath());
        LOG_PATH.put(LoggerLevelEnum.WARN.name().toLowerCase(), getWarnFilePath());
        LOG_PATH.put(LoggerLevelEnum.ERROR.name().toLowerCase(), getErrorFilePath());
    }

    private static ImmutableMap<String, Object> fromProperties(Properties properties) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            builder.put(key, properties.get(key));
        }
        return builder.build();
    }

    public void refreshProperties() {
        Properties properties = FileUtils.getConfigProperties();
        propertiesMap = fromProperties(properties);
    }

    public Set<String> getAllLogNames() {
        return Sets.newHashSet(LOG_PATH.values());
    }

    private static final class Singleton {
        private static final Configer CONFIGER = new Configer();
    }

    public static Configer getInstance() {
        return Singleton.CONFIGER;
    }

    public String getMasterQueue() {
        return propertiesMap.getOrDefault("masterQueue", LoggerConstant.MASTER_QUEUE).toString();
    }

    public String getReplicaQueue() {
        return propertiesMap.getOrDefault("replicaQueue", LoggerConstant.REPLICA_QUEUE).toString();
    }

    public int getQueueSizeThreshold() {
        return getNumberValue("queueSizeThreshold", LoggerConstant.QUEUE_SIZE_THRESHOLD).intValue();
    }

    public int getFlushInterval() {
        return getNumberValue("flushInterval", LoggerConstant.FLUSH_INTERVAL).intValue();
    }

    public int getMergeInterval() {
        return getNumberValue("mergeInterval", LoggerConstant.MERGE_INTERVAL).intValue();
    }

    public double getFileMaxSize() {
        return getNumberValue("fileMaxSize", LoggerConstant.FILE_MAX_SIZE).doubleValue();
    }

    private Number getNumberValue(String key, Number defaultValue) {
        String queueSizeThreshold = propertiesMap.getOrDefault(key, defaultValue).toString();
        try {
            return Double.parseDouble(queueSizeThreshold);
        } catch (NumberFormatException e) {
            String msg = new StringBuilder().append(queueSizeThreshold).append("无法转换为数字，使用系统默认配置项： ").append(key).append("=").append(defaultValue).toString();
            System.out.println(msg);
            return defaultValue;
        }
    }

    public String getInfoFilePath() {
        return propertiesMap.getOrDefault("infoFilePath", LoggerConstant.INFO_FILE_PATH).toString();
    }

    public String getDebugFilePath() {
        return propertiesMap.getOrDefault("debugFilePath", LoggerConstant.DEBUG_FILE_PATH).toString();
    }

    public String getWarnFilePath() {
        return propertiesMap.getOrDefault("warnFilePath", LoggerConstant.WARN_FILE_PATH).toString();
    }

    public String getErrorFilePath() {
        return propertiesMap.getOrDefault("errorFilePath", LoggerConstant.ERROR_FILE_PATH).toString();
    }

    public String getLogFilePath() {
        return propertiesMap.getOrDefault("logFilePath", LoggerConstant.LOG_FILE_PATH).toString();
    }

    public String getLogPreffix() {
        return propertiesMap.getOrDefault("logPreffix", LoggerConstant.LOG_PREFFIX).toString();
    }

    public String getLogSuffix() {
        return propertiesMap.getOrDefault("logSuffix", LoggerConstant.LOG_SUFFIX).toString();
    }

    public boolean dayRolling() {
        String dayRollingStr = propertiesMap.getOrDefault("dayRolling", LoggerConstant.DAY_ROLLING).toString();
        return Boolean.parseBoolean(dayRollingStr);
    }

    public boolean spliteLevel() {
        String spliteLevelStr = propertiesMap.getOrDefault("spliteLevel", LoggerConstant.SPLITE_LEVEL).toString();
        return Boolean.parseBoolean(spliteLevelStr);
    }

    public boolean needWriteInfoToConsole() {
        String needWriteInfoToConsoleStr = propertiesMap.getOrDefault("needWriteInfoToConsole", LoggerConstant.NEED_WRITE_INFO_TO_CONSOLE).toString();
        return Boolean.parseBoolean(needWriteInfoToConsoleStr);
    }

    public boolean needWriteErrorToConsole() {
        String needWriteErrorToConsoleStr = propertiesMap.getOrDefault("needWriteErrorToConsole", LoggerConstant.NEED_WRITE_ERROR_TO_CONSOLE).toString();
        return Boolean.parseBoolean(needWriteErrorToConsoleStr);
    }

    public boolean needWriteWarnToConsole() {
        String needWriteWarnToConsoleStr = propertiesMap.getOrDefault("needWriteWarnToConsole", LoggerConstant.NEED_WRITE_WARN_TO_CONSOLE).toString();
        return Boolean.parseBoolean(needWriteWarnToConsoleStr);
    }

    public boolean needWriteDebugToConsole() {
        String needWriteDebugToConsoleStr = propertiesMap.getOrDefault("needWriteDebugToConsole", LoggerConstant.NEED_WRITE_DEBUG_TO_CONSOLE).toString();
        return Boolean.parseBoolean(needWriteDebugToConsoleStr);
    }

    public String getLogPathByLevel(String level) {
        return Joiner.on("").join(Lists.newArrayList(getLogPreffix(), LOG_PATH.get(level), getLogSuffix()));
    }
}
