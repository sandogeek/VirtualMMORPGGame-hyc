package com.mmorpg.mbdl.framework.resource.resolver;

import com.mmorpg.mbdl.framework.resource.core.StaticResDefinitionFactory;
import com.mmorpg.mbdl.framework.resource.facade.AbstractBeanFactoryAwareResResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Excel静态资源解析器
 *
 * @author Sando Geek
 * @since v1.0
 **/
@Component
public class ExcelResResolver extends AbstractBeanFactoryAwareResResolver {
    @Autowired
    private StaticResDefinitionFactory staticResDefinitionFactory;
    @Override
    public String getSuffix() {
        return ".xlsx";
    }

    @Override
    public void resolve() {

    }
}
