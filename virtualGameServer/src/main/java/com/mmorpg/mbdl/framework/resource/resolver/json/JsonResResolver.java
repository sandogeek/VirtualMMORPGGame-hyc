package com.mmorpg.mbdl.framework.resource.resolver.json;

import com.mmorpg.mbdl.framework.common.utils.JsonUtil;
import com.mmorpg.mbdl.framework.resource.core.StaticResDefinition;
import com.mmorpg.mbdl.framework.resource.exposed.BaseResResolver;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * 表格型json资源解析器
 *
 * @author Sando Geek
 * @since v1.0 2018/12/6
 **/
@Component
public class JsonResResolver extends BaseResResolver {

    @Override
    public String suffix() {
        return ".json";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void resolve(StaticResDefinition staticResDefinition) {
        try (InputStream inputStream = staticResDefinition.getResource().getInputStream()){
            List vClassObjects = JsonUtil.inputStream2Object(inputStream,
                    JsonUtil.constructCollectionType(List.class, staticResDefinition.getvClass()));
            vClassObjects.forEach(staticResDefinition::add);
        } catch (Exception e) {
            throw new RuntimeException(String.format("读取资源文件[%s]发生异常",staticResDefinition.getFullFileName()),e);
        }
    }
}
