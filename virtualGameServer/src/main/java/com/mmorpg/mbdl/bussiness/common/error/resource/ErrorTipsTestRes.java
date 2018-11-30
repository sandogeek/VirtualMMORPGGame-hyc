package com.mmorpg.mbdl.bussiness.common.error.resource;

import com.mmorpg.mbdl.framework.resource.annotation.Id;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

/**
 * 静态资源测试类
 *
 * @author Sando Geek
 * @since v1.0
 **/
@ResDef(relativePath = "excel/b/ErrorTipsRes.xlsx")
public class ErrorTipsTestRes {
    @Id
    private int code;
}
