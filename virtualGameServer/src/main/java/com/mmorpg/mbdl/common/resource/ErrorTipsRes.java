package com.mmorpg.mbdl.common.resource;

import com.mmorpg.mbdl.framework.resource.annotation.Key;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

/**
 * 定义静态资源
 *
 * @author Sando Geek
 * @since v1.0
 **/
@ResDef(relativePath = "excel/a/ErrorTipsRes.xlsx")
// @ResDef
public class ErrorTipsRes {
    @Key
    private int code;

    public int getCode() {
        return code;
    }
}
