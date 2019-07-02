package com.sxkl.project.easylogger.core;

import com.sxkl.project.easylogger.common.LoggerLevelEnum;
import com.sxkl.project.easylogger.config.Configer;

import java.util.Random;

public class EasyLogger {

    private EasyLogger() {}

    public static void info(String msg, Object ...args) {
        writeToFileAndConsole(null, Configer.needWriteInfoToConsole(), LoggerLevelEnum.INFO, msg, args);
    }

    public static void debug(String msg, Object ...args) {
        writeToFileAndConsole(null, Configer.NEED_WRITE_TO_CONSOLE, LoggerLevelEnum.DEBUG, msg, args);
    }

    public static void warn(String msg, Object ...args) {
        writeToFileAndConsole(null, Configer.needWriteWarnToConsole(), LoggerLevelEnum.WARN, msg, args);
    }

    public static void error(String msg, Object ...args) {
        writeToFileAndConsole(null, Configer.NEED_WRITE_TO_CONSOLE, LoggerLevelEnum.ERROR, msg, args);
    }

    public static void error(Exception e, String msg, Object ...args) {
        writeToFileAndConsole(e, Configer.NEED_WRITE_TO_CONSOLE, LoggerLevelEnum.ERROR, msg, args);
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
//            System.out.println(msg);
        }
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        for (int i = 0; i < 1; i++) {
            new Thread(() -> {
                for (int j = 0; j < 200000; j++) {
                    EasyLogger.info(Thread.currentThread().getName()+"info"+j);
                }
            }).start();
        }
    }
}
