package com.mmorpg.mbdl.framework.common.utils;

import com.google.common.collect.Lists;
import edu.emory.mathcs.backport.java.util.Collections;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class FileUtils {
    /**
     * 检索出以ext结尾的文件
     * @param ext 后缀名
     * @return FileFilter
     */
    public static final FileFilter withSuffix(String ext){
        FileFilter extFileFilter = (pathName) -> {
            if (pathName.getName().endsWith(ext)){
                return true;
            }
            return false;
        };
        return extFileFilter;
    }

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    /**
     * 清空指定目录下满足FileFilter的所有File
     * @param targetDirName 目标目录全路径
     * @param recursive 是否递归子目录
     * @param fileFilters File过滤器
     */
    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_BAD_PRACTICE")
    public static void clearByFileFilter(String targetDirName, boolean recursive, FileFilter...fileFilters){
        File targetDir = new File(targetDirName);
        if (!targetDir.isDirectory()) {
            return;
        }
        if (recursive){
            final FileFilter directoryFilter = (pathname)->{
                if (pathname.isDirectory()){
                    return true;
                }
                return false;
            };
            final List<File> filesToDelete = Lists.newArrayList();
            for (FileFilter fileFilter:
                    fileFilters) {
                File[] listFiles= targetDir.listFiles(fileFilter);
                if (listFiles!=null) {
                    Collections.addAll(filesToDelete,listFiles);
                }
            }
            for (File file:filesToDelete){
                if (file.isDirectory()){
                    deleteFolder(file.getAbsolutePath(),true);
                }
                file.delete();
            }
            // 待扫描的目录
            Stack<File> directories = new Stack<>();
            File[] listFiles= targetDir.listFiles(directoryFilter);
            if (listFiles != null) {
                Collections.addAll(directories,listFiles);
            }
            while (!directories.isEmpty()){
                final File next = directories.pop();
                clearByFileFilter(next.getAbsolutePath(),true,fileFilters);
            }
        }else {
            final List<File> filesToDelete = Lists.newArrayList();
            for (FileFilter fileFilter:
                    fileFilters) {
                File[] listFiles= targetDir.listFiles(fileFilter);
                if (listFiles!=null) {
                    Collections.addAll(filesToDelete,listFiles);
                }
            }
            // 不递归，直接删除
            for (File file:filesToDelete){
                file.delete();
            }
        }
    }

    /**
     * 清空文件夹
     * @param dir 目标文件夹名称
     * @param includeSelf 是否包含自身
     */
    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_BAD_PRACTICE")
    public static void deleteFolder(String dir, boolean includeSelf) {
        File delfolder = new File(dir);
        if (!delfolder.isDirectory()){
            return;
        }
        File oldFile[] = delfolder.listFiles();
        if (oldFile!=null){
            for (int i = 0; i < oldFile.length; i++) {
                if (oldFile[i].isDirectory()) {
                    // 递归清空子文件夹
                    if (!dir.endsWith("//")) {
                        dir+="//";
                    }
                    deleteFolder(dir + oldFile[i].getName(),includeSelf);
                }
                oldFile[i].delete();
            }
        }else if (includeSelf){
            delfolder.delete();
        }
    }
    /**
     * 创建文件
     * @param file File对象
     * @return 成功返回true，失败返回false
     */
    public static boolean createFile(File file) throws IOException {
        if (!file.getParentFile().exists()){
            makeDir(file.getParentFile());
        }
        return file.createNewFile();
    }
    /**
     * 如果目录不存在，就递归地创建目录
     * @param file
     */
    public static boolean makeDir(File file){
        if (!file.getParentFile().exists()){
            makeDir(file.getParentFile());
        }
        return file.mkdir();
    }

}
