package com.sxkl.project.easylogger.core;

import com.sxkl.project.easylogger.common.LoggerLevelEnum;
import com.sxkl.project.easylogger.config.Configer;
import com.sxkl.project.easylogger.message.LogMessage;
import com.sxkl.project.easylogger.message.MessageManager;

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

    public static void error(Exception e, String msg, Object ...args) {
        writeToFileAndConsole(e, Configer.getInstance().needWriteErrorToConsole(), LoggerLevelEnum.ERROR, msg, args);
    }

    private static void writeToFileAndConsole(Exception e, boolean needWriteToConsole, LoggerLevelEnum level, String msg, Object[] args) {
        String msgRow = MessageManager.buildMsg(e, level, msg, args);
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
