package com.mmorpg.mbdl.framework.resource.core;

import com.mmorpg.mbdl.framework.resource.annotation.ResDef;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Optional;

/**
 * 静态资源工具类
 *
 * @author Sando Geek
 * @since v1.0
 **/
public class IStaticResUtil {
    /**
     * 获取运行路径所在的父目录
     * @return
     */
    public static String getRunParentPath() {
        URL url = IStaticResUtil.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {
            // 转化为utf-8编码，支持中文
            filePath = URLDecoder.decode(url.getPath(), "utf-8");
            File file = new File(filePath);
            filePath = file.getParent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtils.cleanPath(filePath + "/");
    }
    /**
     * 根据{@link ResDef}标注的类获取全文件名
     * @param clazz {@link ResDef}标注的类
     * @return 类对应的文件名
     */
    public static String getFullFileName(Class clazz){
        ResDef resDef = (ResDef)clazz.getAnnotation(ResDef.class);
        String suffix = StringUtils.trimWhitespace(resDef.suffix());
        suffix = !StringUtils.hasText(suffix)?".xlsx":suffix;
        String fileNameWithoutSuffix = !StringUtils.hasText(resDef.value())?clazz.getSimpleName():StringUtils.trimWhitespace(resDef.value());
        String relativePath = !StringUtils.hasText(resDef.relativePath())?null:StringUtils.trimWhitespace(resDef.relativePath());
        String fullFileName;
        if (relativePath != null) {
            fullFileName =relativePath;
        }else {
            fullFileName = fileNameWithoutSuffix + suffix;
        }
        String pathToUse = StringUtils.cleanPath(fullFileName);
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        return pathToUse;
    }

    /**
     * 获取以指定字符串结尾的ClassPath
     * @param s 指定字符串
     * @return 指定字符串结尾的ClassPath,不存在时返回null
     */
    public static String getClassPathSuffixWith(String s){
        String cleanString = StringUtils.cleanPath(s);
        String[] classPaths = System.getProperty("java.class.path").split(";");
        Optional<String> classPath = Arrays.stream(classPaths).filter(string -> {
            string = StringUtils.cleanPath(string);
            return string.endsWith(cleanString);
        }).findFirst();
        return classPath.map(StringUtils::cleanPath).orElse(null);

    }

    /**
     * 获取{@link FileSystemResource#getPath()}路径相对于以/classes结尾的ClassPath的路径
     * @param fileSystemResource FileSystemResource
     * @return {@link FileSystemResource#getPath()}路径相对于以/classes结尾的ClassPath的路径
     */
    public static String getResPathRelative2ClassPath(FileSystemResource fileSystemResource){
        String classPath = IStaticResUtil.getClassPathSuffixWith("/classes");
        String path = fileSystemResource.getPath();
        return path.substring(classPath.length()+1);
    }
}
