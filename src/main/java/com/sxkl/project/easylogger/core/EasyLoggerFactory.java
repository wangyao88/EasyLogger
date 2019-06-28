package com.sxkl.project.easylogger.core;

public class EasyLoggerFactory {

    private EasyLoggerFactory() {}

    public static EasyLogger getEasyLogger() {
        return new EasyLogger();
    }
}
