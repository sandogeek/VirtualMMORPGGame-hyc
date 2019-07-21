package com.mmorpg.mbdl.staticresource;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.mmorpg.mbdl.TestWithSpring;
import com.mmorpg.mbdl.business.container.res.ItemRes;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * 静态资源测试
 *
 * @author Sando Geek
 * @since v1.0 2019/7/21
 **/
public class StaticResTest extends TestWithSpring {
    @Autowired
    private IStaticRes<Integer, ItemRes> itemResIStaticRes;
    private transient Codec codec;

    @PostConstruct
    public void init() {
        codec = ProtobufProxy.create(itemResIStaticRes.getClass());
    }

    @Test
    void 序列化测试() throws IOException {
        byte[] bytes = codec.encode(itemResIStaticRes);
        Object decode = codec.decode(bytes);
        System.out.println("");
    }
}
