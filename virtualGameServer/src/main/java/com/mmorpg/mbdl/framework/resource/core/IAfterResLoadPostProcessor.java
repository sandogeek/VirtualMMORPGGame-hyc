package com.mmorpg.mbdl.framework.resource.core;

import com.mmorpg.mbdl.framework.resource.exposed.IAfterResLoad;
import com.mmorpg.mbdl.framework.resource.exposed.ResPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 资源加载完成后处理器
 *
 * @author Sando Geek
 * @since v1.0 2019/4/16
 **/
@Component
public class IAfterResLoadPostProcessor implements ResPostProcessor {
    @Override
    public void postProcess(Object obj) {
        if (obj instanceof IAfterResLoad) {
            IAfterResLoad afterLoad = (IAfterResLoad) obj;
            afterLoad.afterLoad();
        }
    }

    @Override
    public int order() {
        return 0;
    }
}
