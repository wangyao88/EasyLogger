package com.sxkl.project.easylogger.watcher;

import java.io.File;

/**
 * 文件操作的回调方法
 * @author mohan
 * @Date 2019-07-09
 */
public interface FileActionCallback {

    void delete(File file);

    void modify(File file);

    void create(File file);
}
