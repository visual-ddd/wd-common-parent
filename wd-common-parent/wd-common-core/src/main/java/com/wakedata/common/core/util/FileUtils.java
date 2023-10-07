package com.wakedata.common.core.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author EASON.TONG
 * @create 2021/06/15
 **/
@Slf4j
public class FileUtils {

    public static String parseFileExtInfo(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return null;
        } else {
            return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        }
    }
}
