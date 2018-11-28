package com.mmorpg.mbdl.bussiness.common.error.resource;

import com.mmorpg.mbdl.framework.resource.annotation.Id;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

/**
 * 定义静态资源
 *
 * @author Sando Geek
 * @since v1.0
 **/
@ResDef
public class ErrorTipsRes {
    @Id
    private int code;
}
