package com.mmorpg.mbdl.common.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * json测试类
 *
 * @author Sando Geek
 * @since v1.0 2019/1/14
 **/
public class JsonTest {
    private boolean success;
    private String msg;
    private Object obj;
    private Map<String, Object> attributes;

    public JsonTest setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public JsonTest setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public JsonTest setObj(Object obj) {
        this.obj = obj;
        return this;
    }

    public JsonTest setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    //getter and setter

    public String getJsonStr() {
        JSONObject obj = new JSONObject();
        obj.put("success", true);
        obj.put("msg", "hello");
        obj.put("obj", this.obj);
        obj.put("attributes", this.attributes);
        return obj.toJSONString();
    }
}
