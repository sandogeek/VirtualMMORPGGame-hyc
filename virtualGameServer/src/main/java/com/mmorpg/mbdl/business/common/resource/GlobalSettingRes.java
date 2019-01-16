package com.mmorpg.mbdl.business.common.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mmorpg.mbdl.framework.resource.annotation.Id;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

/**
 * 全局配置
 *
 * @author Sando Geek
 * @since v1.0 2018/12/5
 **/
@ResDef
public class GlobalSettingRes {
    @Id
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Value")
    private int value;

    public String getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }
}
