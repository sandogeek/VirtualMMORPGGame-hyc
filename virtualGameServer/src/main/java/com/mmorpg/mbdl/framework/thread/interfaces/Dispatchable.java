package com.mmorpg.mbdl.framework.thread.interfaces;

import java.io.Serializable;

/**
 * 实现改接口的对象可用于分派任务
 *
 * @author Sando Geek
 * @since v1.0 2019/11/11
 **/
public interface Dispatchable<T extends Serializable> {
    /**
     * 分发id，每个id对于一个任务队列
    * @return
     */
    T dispatchId();
}
