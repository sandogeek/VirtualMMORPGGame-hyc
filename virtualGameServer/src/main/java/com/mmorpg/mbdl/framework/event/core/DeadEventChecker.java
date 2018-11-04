package com.mmorpg.mbdl.framework.event.core;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * DeadEvent检查器
 * <p>当post的事件没有找到对应的@Subscribe方法时，会把事件包装成DeadEvent post，这里用来警告发布的事件没有对应的观察者</p>
 */
@Component
public class DeadEventChecker {
    private static final Logger logger = LoggerFactory.getLogger(DeadEventChecker.class);
    @Subscribe
    @AllowConcurrentEvents
    public void check(DeadEvent deadEvent){
        logger.warn("{}.post({})没有对应的@Subscribe方法",deadEvent.getSource().toString(),deadEvent.getEvent().getClass().getSimpleName());
    }
}
