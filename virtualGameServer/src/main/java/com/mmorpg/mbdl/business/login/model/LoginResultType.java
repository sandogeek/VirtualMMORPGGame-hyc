package com.mmorpg.mbdl.business.login.model;

import com.baidu.bjf.remoting.protobuf.EnumReadable;

public enum LoginResultType implements EnumReadable {
    /**
     * 登录结果为成功
     */
    SUCCESS(1),
    /**
     * 登录结果为失败
     */
    FAILURE(0);
    private int code;

    LoginResultType(int code) {
        this.code = code;
    }

    @Override
    public int value() {
        return this.code;
    }
}
