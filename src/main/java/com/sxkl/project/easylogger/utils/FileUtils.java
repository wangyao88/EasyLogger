package com.sxkl.project.easylogger.utils;

import java.io.File;

public class FileUtils {

    public static double getFileSize(File file) {
        if(!file.exists()) {
            return 0;
        }
        return file.length();
    }

    public static double getFileSize(String path) {
        File file = new File(path);
        if(!file.exists()) {
            return 0;
        }
        return file.length();
    }
}
