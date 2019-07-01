package com.sxkl.project.easylogger.core;

import com.sxkl.project.easylogger.common.LoggerLevelEnum;

import java.util.Objects;

public class LogMessage {

    private LoggerLevelEnum level;
    private String msg;

    public LogMessage(LoggerLevelEnum level, String msg) {
        this.level = level;
        this.msg = msg;
    }

    public String getLevel() {
        return Objects.isNull(level) ? "" : level.name().toLowerCase();
    }

    public String getMsg() {
        return msg;
    }
}
