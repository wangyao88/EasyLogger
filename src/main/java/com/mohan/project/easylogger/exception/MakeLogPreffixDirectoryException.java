package com.mohan.project.easylogger.exception;

import com.mohan.project.easytools.common.StringTools;

/**
 * 创建指定日志文件夹失败
 * @author mohan
 * @date 2019-08-30 17:20:00
 */
public class MakeLogPreffixDirectoryException extends RuntimeException{

    public static final String DEFAULT_MESSAGE = "EasyLogger创建指定日志文件夹失败";

    public MakeLogPreffixDirectoryException() {
        this(DEFAULT_MESSAGE);
    }

    public MakeLogPreffixDirectoryException(String msg) {
        super(msg);
    }
}
