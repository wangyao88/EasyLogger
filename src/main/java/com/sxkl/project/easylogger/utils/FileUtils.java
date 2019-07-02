package com.sxkl.project.easylogger.utils;

import com.sxkl.project.easylogger.common.LoggerConstant;
import com.sxkl.project.easylogger.common.LoggerLevelEnum;
import com.sxkl.project.easylogger.config.Configer;
import com.sxkl.project.easylogger.message.MessageManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

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

    public static Properties getConfigProperties() {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = Configer.class.getClassLoader().getResourceAsStream(LoggerConstant.PROPERTIES_FILE_NAME);
            properties.load(inputStream);
        } catch (Exception e) {
            System.out.println(MessageManager.buildMsg(e, LoggerLevelEnum.ERROR, "未找到配置文件，使用easy-logger默认配置", null));
        } finally {
            if(!Objects.isNull(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }
}
