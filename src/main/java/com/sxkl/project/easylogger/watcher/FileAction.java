package com.sxkl.project.easylogger.watcher;

/**
 * 文件变动行为枚举
 * @author mohan
 * @Date 2019-07-09
 */
public enum FileAction {

    DELETE("ENTRY_DELETE"), CREATE("ENTRY_CREATE"), MODIFY("ENTRY_MODIFY");
    private String value;

    FileAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
