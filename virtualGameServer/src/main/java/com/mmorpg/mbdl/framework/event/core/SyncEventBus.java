package com.mmorpg.mbdl.framework.event.core;

import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 同步事件总线，调用post后观察者(@Subscribe注解的方法)会在当前线程按事件post发布顺序调用，
 * 适合@Subscribe方法执行时间很短的情况下使用，对于执行时间较长的{@link EventBus#post}（例如其中的某个@Subscribe方法包含阻塞I/O操作），
 * 应采用AsyncEventBus,以防止当前玩家后续的请求响应时间太长。<br>
 * 由于不同的用户请求（会在不同的线程处理）可能会post同一种事件，这就涉及到线程安全问题。<br/>
 * EventBus默认情况下会把@Subscribe方法封装成SynchronizedSubscriber保证线程安全，即同步锁的方式，
 * 如果能确保@Subscribe注解的方法是线程安全的，最好在其上添加@AllowConcurrentEvents防止锁竞争以提高性能
 * <p>目前暂时未出现需要消耗大量时间的{@link EventBus#post}，所以暂时只提供EventBus</p>
 * @author sando
 */
@Component
public class SyncEventBus extends EventBus {
    public SyncEventBus() {
        super("同步事件总线");
    }

    private static SyncEventBus self;
    @PostConstruct
    private void init(){
        self = this;
    }
    public static SyncEventBus getInstance(){
        return self;
    }
}
