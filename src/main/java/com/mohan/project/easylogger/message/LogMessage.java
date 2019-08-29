package com.mohan.project.easylogger.message;

import com.mohan.project.easylogger.common.LoggerLevelEnum;

import java.util.Objects;

/**
 * EasyLogger内部消息封装类
 * @author mohan
 * @date 2019-08-29 16:00:23
 */
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
