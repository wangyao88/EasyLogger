package com.sxkl.project.easylogger.watcher;

import com.sxkl.project.easylogger.config.Configer;

import java.io.File;

/**
 * 文件操作的回调方法
 * @author mohan
 * @Date 2019-07-09
 */
public class EasyLoggerPropertiesFileActionCallback implements FileActionCallback{

    @Override
    public void delete(File file) {
        Configer.getInstance().refreshProperties();
    }

    @Override
    public void modify(File file) {
        Configer.getInstance().refreshProperties();
    }

    @Override
    public void create(File file) {
        Configer.getInstance().refreshProperties();
    }
}
