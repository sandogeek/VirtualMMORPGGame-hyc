package com.mmorpg.mbdl.framework.resource.core;

import com.mmorpg.mbdl.framework.resource.annotation.ResDef;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;

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
     * 根据{@link ResDef}标注的类获取全文件名
     * @param clazz {@link ResDef}标注的类
     * @return 类对应的文件名
     */
    public static String getFullFileName(Class clazz){
        ResDef resDef = (ResDef)clazz.getAnnotation(ResDef.class);
        String suffix = StringUtils.trim(resDef.suffix());
        suffix = StringUtils.isEmpty(suffix)?".xlsx":suffix;
        String fileNameWithoutSuffix = StringUtils.isEmpty(resDef.value())?clazz.getSimpleName():StringUtils.trim(resDef.value());
        String relativePath = StringUtils.isEmpty(resDef.relativePath())?null:StringUtils.trim(resDef.relativePath());
        String fullFileName;
        if (relativePath != null) {
            fullFileName =relativePath;
        }else {
            fullFileName = fileNameWithoutSuffix + suffix;
        }
        String pathToUse = org.springframework.util.StringUtils.cleanPath(fullFileName);
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        return fullFileName;
    }

    /**
     * 获取以指定字符串结尾的ClassPath
     * @param s 指定字符串
     * @return 指定字符串结尾的ClassPath,不存在时返回null
     */
    public static String getClassPathSuffixWith(String s){
        String cleanString = org.springframework.util.StringUtils.cleanPath(s);
        String[] classPaths = System.getProperty("java.class.path").split(";");
        Optional<String> classPath = Arrays.stream(classPaths).filter(string -> {
            string = org.springframework.util.StringUtils.cleanPath(string);
            return string.endsWith(cleanString);
        }).findFirst();
        return classPath.map(org.springframework.util.StringUtils::cleanPath).orElse(null);

    }

    /**
     * 获取{@link FileSystemResource#getPath()}路径相对于以/classes结尾的ClassPath的路径
     * @param fileSystemResource FileSystemResource
     * @return {@link FileSystemResource#getPath()}路径相对于以/classes结尾的ClassPath的路径
     */
    public static String getResPathRelative2ClassPath(FileSystemResource fileSystemResource){
        String classPath = IStaticResUtil.getClassPathSuffixWith("/classes");
        String path = fileSystemResource.getPath();
        String result = path.substring(classPath.length()+1);
        return result;
    }
}
