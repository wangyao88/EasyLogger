package com.sxkl.project.easylogger.common;

import com.google.common.collect.Maps;

import java.io.File;
import java.util.Map;

public class LoggerConstant {

    public static final String MASTER_QUEUE = "master";
    public static final String REPLICA_QUEUE = "replica";
    public static final int QUEUE_SIZE_THRESHOLD = 10000;
    public static final double FLUSH_INTERVAL = 10.0 * 1024 * 1024;
    public static final double FILE_MAX_SIZE = 10*1024*1024.0;
    public static final String INFO_FILE_PATH = "./info.log";
    public static final String DEBUG_FILE_PATH = "./debug.log";
    public static final String WARN_FILE_PATH = "./warn.log";
    public static final String ERROR_FILE_PATH = "./error.log";
    public static final String LOG_FILE_PATH = "./log.log";
    public static final String LOG_SUFFIX = ".log";
    public static final boolean SPLITE_LEVEL = true;
    public static final Map<String, String> LOG_PATH = Maps.newHashMap();

    static {
        LOG_PATH.put(LoggerLevelEnum.INFO.name().toLowerCase(), INFO_FILE_PATH);
        LOG_PATH.put(LoggerLevelEnum.DEBUG.name().toLowerCase(), DEBUG_FILE_PATH);
        LOG_PATH.put(LoggerLevelEnum.WARN.name().toLowerCase(), WARN_FILE_PATH);
        LOG_PATH.put(LoggerLevelEnum.ERROR.name().toLowerCase(), ERROR_FILE_PATH);
    }

    public static String getLogPathByLevel(String level) {
        return LOG_PATH.get(level);
    }

    public static void main(String[] args) {
        System.out.println(new File("/Users/localadmin/workspace/简约蓝色OA.zip").length()/1024/1024.0);
    }
}
