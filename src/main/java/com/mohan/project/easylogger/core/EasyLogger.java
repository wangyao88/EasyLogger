package com.mohan.project.easylogger.core;

import com.mohan.project.easylogger.config.Configer;
import com.mohan.project.easylogger.common.LoggerLevelEnum;
import com.mohan.project.easylogger.message.LogMessage;
import com.mohan.project.easylogger.message.MessageManager;

/**
 * EasyLogger对外API
 * @author mohan
 * @date 2019-08-29 16:00:23
 */
public class EasyLogger {

    private EasyLogger() {}

    public static void info(String msg, Object ...args) {
        writeToFileAndConsole(null, Configer.getInstance().needWriteInfoToConsole(), LoggerLevelEnum.INFO, msg, args);
    }

    public static void debug(String msg, Object ...args) {
        writeToFileAndConsole(null, Configer.getInstance().needWriteDebugToConsole(), LoggerLevelEnum.DEBUG, msg, args);
    }

    public static void warn(String msg, Object ...args) {
        writeToFileAndConsole(null, Configer.getInstance().needWriteWarnToConsole(), LoggerLevelEnum.WARN, msg, args);
    }

    public static void error(String msg, Object ...args) {
        writeToFileAndConsole(null, Configer.getInstance().needWriteErrorToConsole(), LoggerLevelEnum.ERROR, msg, args);
    }

    public static void error(Throwable throwable, String msg, Object ...args) {
        writeToFileAndConsole(throwable, Configer.getInstance().needWriteErrorToConsole(), LoggerLevelEnum.ERROR, msg, args);
    }

    public static void log(Logger logger) {
        LoggerLevelEnum level = logger.getLevel();
        String msgRow = MessageManager.buildMsg(logger);
        writeToFile(level, msgRow);
        writeToConsole(needWriteToConsole(level), msgRow);
    }

    private static boolean needWriteToConsole(LoggerLevelEnum level) {
        if(level.equals(LoggerLevelEnum.INFO)) {
            return Configer.getInstance().needWriteInfoToConsole();
        }
        if(level.equals(LoggerLevelEnum.DEBUG)) {
            return Configer.getInstance().needWriteDebugToConsole();
        }
        if(level.equals(LoggerLevelEnum.WARN)) {
            return Configer.getInstance().needWriteWarnToConsole();
        }
        if(level.equals(LoggerLevelEnum.ERROR)) {
            return Configer.getInstance().needWriteErrorToConsole();
        }
        return false;
    }

    private static void writeToFileAndConsole(Throwable throwable, boolean needWriteToConsole, LoggerLevelEnum level, String msg, Object[] args) {
        String msgRow = MessageManager.buildMsg(throwable, level, msg, args);
        writeToFile(level, msgRow);
        writeToConsole(needWriteToConsole, msgRow);
    }

    private static void writeToFile(LoggerLevelEnum level, String msg) {
        FileManager.getInstance().addMsg(new LogMessage(level, msg));
    }

    private static void writeToConsole(boolean needWriteToConsole, String msg) {
        if(needWriteToConsole) {
            System.out.println(msg);
        }
    }
}