package com.sxkl.project.easylogger.common;

public class LoggerConstant {

    public static final String PROPERTIES_FILE_NAME = "easylogger.properties";

    public static final String MASTER_QUEUE = "master";
    public static final String REPLICA_QUEUE = "replica";
    public static final Integer QUEUE_SIZE_THRESHOLD = 10000;
    public static final Integer FLUSH_INTERVAL = 10;
    public static final Integer MERGE_INTERVAL = 1;
    public static final Double FILE_MAX_SIZE = 104857600.0; //100 * 1024 * 1024.0;
    public static final String INFO_FILE_PATH = "info";
    public static final String DEBUG_FILE_PATH = "debug";
    public static final String WARN_FILE_PATH = "warn";
    public static final String ERROR_FILE_PATH = "error";
    public static final String LOG_FILE_PATH = "log";
    public static final String LOG_PREFFIX = "./log/";
    public static final String LOG_SUFFIX = ".log";
    public static final Boolean DAY_ROLLING = true;
    public static final Boolean SPLITE_LEVEL = true;
    public static final Boolean NEED_WRITE_INFO_TO_CONSOLE = true;
    public static final Boolean NEED_WRITE_ERROR_TO_CONSOLE = true;
    public static final Boolean NEED_WRITE_WARN_TO_CONSOLE = true;
    public static final Boolean NEED_WRITE_DEBUG_TO_CONSOLE = true;

    public static final String BLANK = "";
    public static final String WHITE_SPACE = " ";
    public static final String Thread_name = "java.lang.Thread";

    public static final String EASY_LOGGER_START_SUCCESS = "easy-logger成功启动服务!";
    public static final String EASY_LOGGER_STOP_SUCCESS = "easy-logger成功停止服务!";
}
