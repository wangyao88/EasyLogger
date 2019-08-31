package com.mohan.project.easylogger.utils;

import com.mohan.project.easylogger.common.LoggerConstant;
import com.mohan.project.easylogger.common.LoggerLevelEnum;
import com.mohan.project.easylogger.config.Configer;
import com.mohan.project.easylogger.exception.MakeLogPreffixDirectoryException;
import com.mohan.project.easylogger.message.MessageManager;
import com.mohan.project.easytools.common.StringTools;
import com.mohan.project.easytools.file.FileTools;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
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

    public static String getRelativeBanner(String bannerFileName, String defaultBanner) {
        try {
            String path = FileUtils.class.getResource("/" + bannerFileName).getPath();
            Optional<String> content = FileTools.getContent(path);
            return content.orElse(defaultBanner);

//            BufferedReader in = new BufferedReader(new InputStreamReader(FileUtils.class.getClassLoader().getResourceAsStream(bannerFileName)));
//            StringBuilder buffer = new StringBuilder();
//            String line;
//            while ((line = in.readLine()) != null){
//                buffer.append(line).append(FileTools.LF);
//            }
//            return buffer.toString();
        }catch (Exception e) {
            return defaultBanner;
        }
    }
}