package com.mohan.project.easylogger.utils;

import com.mohan.project.easylogger.common.LoggerConstant;
import com.mohan.project.easylogger.config.Configer;
import com.mohan.project.easylogger.common.LoggerLevelEnum;
import com.mohan.project.easylogger.exception.MakeLogPreffixDirectoryException;
import com.mohan.project.easylogger.message.MessageManager;
import com.mohan.project.easytools.common.StringTools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * EasyLogger文件操作相关工具类
 * @author mohan
 * @date 2019-08-29 16:00:23
 */
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
            System.out.println(MessageManager.buildMsg(null, LoggerLevelEnum.WARN, "EasyLogger 未找到配置文件，使用easy-logger默认配置", null));
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

    public static void mkdirForLogPreffix() {
        String logPreffix = Configer.getInstance().getLogPreffix();
        File rootDir = new File(logPreffix);
        if(!rootDir.exists() || !rootDir.isDirectory()) {
            boolean mkdirs = rootDir.mkdirs();
            if(!mkdirs) {
                String msg = StringTools.append(StringTools.SPACE, MakeLogPreffixDirectoryException.DEFAULT_MESSAGE, rootDir.getAbsolutePath());
                throw new MakeLogPreffixDirectoryException(msg);
            }
        }
    }
}
