package com.mohan.project.easylogger.message;

import com.mohan.project.easylogger.common.LoggerLevelEnum;

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
