package com.mmorpg.mbdl.business.common.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.mmorpg.mbdl.framework.thread.ThreadUtils;
import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;

import java.io.Serializable;

/**
 * 当前线程的Dispatcherable对象显示器
 *
 * @author Sando Geek
 * @since v1.0 2019/11/11
 **/
public class DispatcherConverter extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent event) {
        Dispatchable<? extends Serializable> dispatchable = null;
        try {
            dispatchable = ThreadUtils.currentThreadDispatchable();
        } catch (Exception e) {
        }
        String result = dispatchable != null ? dispatchable.toString() : Thread.currentThread().getName();
        return result;
    }
}
