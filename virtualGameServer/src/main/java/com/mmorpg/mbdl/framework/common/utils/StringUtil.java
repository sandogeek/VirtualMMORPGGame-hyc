package com.mmorpg.mbdl.framework.common.utils;

import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.StringUtils;

/**
 * 字符串工具
 *
 * @author Sando Geek
 * @since v1.0 2019/7/26
 **/
public abstract class StringUtil extends StringUtils {
    public static String fommat(String template, Object ...objects) {
        return MessageFormatter.arrayFormat(template, objects).getMessage();
    }
}
