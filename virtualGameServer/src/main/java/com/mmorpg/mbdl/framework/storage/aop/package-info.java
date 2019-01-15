/**
 * 可能能用上 https://github.com/neoremind/dynamic-proxy
 * 利用ByteBuddy AOP，实现所有的实体在有变更后自动定时更新到缓存，
 * 缓存定期自动更新到数据库（目前更新到缓存会立即同步到数据库，所以可以考虑设置较长的delay），业务实现者大多数情况下不需要管理实体更新。
 * 建立EntityFactory，所有的实体创建都需要使用EntityFactory.getEntityByType(Class<T extends IEntity>)创建，
 * 此方法获得的实例实际上是原本类的子类，这个子类的所有setter方法都织入了增强，当调用时会生成一个DelayTask，在这个Task执行前，
 * 所有的setter都不会再创建新的DelayTask，这样就达到了定时更新的效果。
 */
package com.mmorpg.mbdl.framework.storage.aop;