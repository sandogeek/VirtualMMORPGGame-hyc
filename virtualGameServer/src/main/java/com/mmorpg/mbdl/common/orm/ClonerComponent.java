package com.mmorpg.mbdl.common.orm;

import com.rits.cloning.Cloner;
import com.rits.cloning.ICloningStrategy;
import com.rits.cloning.IDumpCloned;
import com.rits.cloning.IFastCloner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 拷贝器
 *
 * @author Sando Geek
 * @since v1.0 2019/2/20
 **/
@Component
public class ClonerComponent {
    private static ClonerComponent self;

    @Autowired
    private Cloner cloner;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static ClonerComponent getInstance() {
        return self;
    }

    public IDumpCloned getDumpCloned() {
        return cloner.getDumpCloned();
    }

    public void setDumpCloned(IDumpCloned dumpCloned) {
        cloner.setDumpCloned(dumpCloned);
    }

    public boolean isNullTransient() {
        return cloner.isNullTransient();
    }

    public void setNullTransient(boolean nullTransient) {
        cloner.setNullTransient(nullTransient);
    }

    public void setCloneSynthetics(boolean cloneSynthetics) {
        cloner.setCloneSynthetics(cloneSynthetics);
    }

    public void registerConstant(Object o) {
        cloner.registerConstant(o);
    }

    public void registerConstant(Class<?> c, String privateFieldName) {
        cloner.registerConstant(c, privateFieldName);
    }

    public void registerCloningStrategy(ICloningStrategy strategy) {
        cloner.registerCloningStrategy(strategy);
    }

    public void registerStaticFields(Class<?>... classes) {
        cloner.registerStaticFields(classes);
    }

    public void setExtraStaticFields(Set<Class<?>> set) {
        cloner.setExtraStaticFields(set);
    }

    public void dontClone(Class<?>... c) {
        cloner.dontClone(c);
    }

    public void dontCloneInstanceOf(Class<?>... c) {
        cloner.dontCloneInstanceOf(c);
    }

    public void setDontCloneInstanceOf(Class<?>... c) {
        cloner.setDontCloneInstanceOf(c);
    }

    public void nullInsteadOfClone(Class<?>... c) {
        cloner.nullInsteadOfClone(c);
    }

    public void setExtraNullInsteadOfClone(Set<Class<?>> set) {
        cloner.setExtraNullInsteadOfClone(set);
    }

    public void registerImmutable(Class<?>... c) {
        cloner.registerImmutable(c);
    }

    public void setExtraImmutables(Set<Class<?>> set) {
        cloner.setExtraImmutables(set);
    }

    public void registerFastCloner(Class<?> c, IFastCloner fastCloner) {
        cloner.registerFastCloner(c, fastCloner);
    }

    public void unregisterFastCloner(Class<?> c) {
        cloner.unregisterFastCloner(c);
    }

    public <T> T fastCloneOrNewInstance(Class<T> c) {
        return cloner.fastCloneOrNewInstance(c);
    }

    public <T> T deepClone(T o) {
        return cloner.deepClone(o);
    }

    public <T> T deepCloneDontCloneInstances(T o, Object... dontCloneThese) {
        return cloner.deepCloneDontCloneInstances(o, dontCloneThese);
    }

    public <T> T shallowClone(T o) {
        return cloner.shallowClone(o);
    }

    public <T, E extends T> void copyPropertiesOfInheritedClass(T src, E dest) {
        cloner.copyPropertiesOfInheritedClass(src, dest);
    }

    public boolean isDumpClonedClasses() {
        return cloner.isDumpClonedClasses();
    }

    public void setDumpClonedClasses(boolean dumpClonedClasses) {
        cloner.setDumpClonedClasses(dumpClonedClasses);
    }

    public boolean isCloningEnabled() {
        return cloner.isCloningEnabled();
    }

    public void setCloningEnabled(boolean cloningEnabled) {
        cloner.setCloningEnabled(cloningEnabled);
    }

    public void setCloneAnonymousParent(boolean cloneAnonymousParent) {
        cloner.setCloneAnonymousParent(cloneAnonymousParent);
    }

    public boolean isCloneAnonymousParent() {
        return cloner.isCloneAnonymousParent();
    }

    public static Cloner standard() {
        return Cloner.standard();
    }

    public static Cloner shared() {
        return Cloner.shared();
    }
}
