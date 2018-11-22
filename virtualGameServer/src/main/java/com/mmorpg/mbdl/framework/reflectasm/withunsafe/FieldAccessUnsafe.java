package com.mmorpg.mbdl.framework.reflectasm.withunsafe;

/**
 * 利用Unsafe访问私有成员
 * TODO 利用ByteBuddy重写ReflectASM
 * ReflectASM使用场景：大量使用反射访问类成员
 * FieldAccessUnsafe come form https://github.com/EsotericSoftware/reflectasm/pull/39 has some revise.
 **/

import com.google.common.base.Preconditions;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;

@SuppressWarnings("restriction")
class FieldAccessUnsafe extends FieldAccess {
    private static final Unsafe unsafe;
    private long[] addresses;
    // 原来的类
    private Class<?> type;
    // from guava AbstractFuture
    static {
        sun.misc.Unsafe unsafeTry = null;
        try {
            unsafeTry = sun.misc.Unsafe.getUnsafe();
        } catch (SecurityException tryReflectionInstead) {
            try {
                unsafeTry =
                        AccessController.doPrivileged(
                                new PrivilegedExceptionAction<Unsafe>() {
                                    @Override
                                    public sun.misc.Unsafe run() throws Exception {
                                        Class<sun.misc.Unsafe> k = sun.misc.Unsafe.class;
                                        for (java.lang.reflect.Field f : k.getDeclaredFields()) {
                                            f.setAccessible(true);
                                            Object x = f.get(null);
                                            if (k.isInstance(x)) {
                                                return k.cast(x);
                                            }
                                        }
                                        throw new NoSuchFieldError("Unsafe字段已经不存在");
                                    }
                                });
            } catch (PrivilegedActionException e) {
                try {
                    Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
                    unsafeConstructor.setAccessible(true);
                    unsafeTry = unsafeConstructor.newInstance();
                }catch (Throwable throwable){
                    throwable.printStackTrace();
                    throw new RuntimeException("Unsafe获取失败", e.getCause());
                }
            }
        }
        unsafe = unsafeTry;
    }

    FieldAccessUnsafe(Class<?> clazz)
    {
        if(unsafe == null) {
            throw new UnsupportedOperationException();
        }
        this.type = clazz;
        super.fields = clazz.getDeclaredFields();

        super.fieldNames = new String[fields.length];
        super.fieldName2Index = new HashMap<>(64);
        super.fieldTypes = new Class<?>[fields.length];
        this.addresses = new long[fields.length];
        String fieldName;
        for(int i = 0; i < fields.length; i++)
        {
            fieldName = fields[i].getName();
            super.fieldNames[i] = fieldName;
            super.fieldName2Index.put(fieldName,i);
            super.fieldTypes[i] = fields[i].getType();
            if (isStaticField(fields[i])) {
                this.addresses[i] = unsafe.staticFieldOffset(fields[i]);
            }else {
                this.addresses[i] = unsafe.objectFieldOffset(fields[i]);
            }
        }
    }

    private boolean isStaticField(Field field){
        return java.lang.reflect.Modifier.isStatic(field.getModifiers());
    }

    private Object transform(Object instance,int fieldIndex){
        if (isStaticField(fields[fieldIndex])){
            return this.type;
        }else if (Preconditions.checkNotNull(instance,"访问非静态字段不能传入null").getClass()!=this.type) {
            throw new IllegalArgumentException(String.format("参数异常，传入的对象类型必须为[%s]",type.getSimpleName()));
        }
        return instance;
    }
    @Override
    public void setObject(Object instance, int fieldIndex, Object value)
    {
        if (!checked){
            if (primitiveTypes.contains(fieldTypes[fieldIndex])){
                throw new IllegalArgumentException("目标类型是原生类型，请使用相应的set方法");
            }
        }
        unsafe.putObject(transform(instance,fieldIndex), addresses[fieldIndex], value);
        checked = false;
    }

    @Override
    public void setBoolean(Object instance, int fieldIndex, boolean value)
    {
        unsafe.putBoolean(transform(instance,fieldIndex), addresses[fieldIndex], value);
    }

    @Override
    public void setByte(Object instance, int fieldIndex, byte value)
    {
        unsafe.putByte(transform(instance,fieldIndex), addresses[fieldIndex], value);
    }

    @Override
    public void setShort(Object instance, int fieldIndex, short value)
    {
        unsafe.putShort(transform(instance,fieldIndex), addresses[fieldIndex], value);
    }

    @Override
    public void setInt(Object instance, int fieldIndex, int value)
    {
        unsafe.putInt(transform(instance,fieldIndex), addresses[fieldIndex], value);
    }

    @Override
    public void setLong(Object instance, int fieldIndex, long value)
    {
        unsafe.putLong(transform(instance,fieldIndex), addresses[fieldIndex], value);
    }

    @Override
    public void setDouble(Object instance, int fieldIndex, double value)
    {
        unsafe.putDouble(transform(instance,fieldIndex), addresses[fieldIndex], value);
    }

    @Override
    public void setFloat(Object instance, int fieldIndex, float value)
    {
        unsafe.putFloat(transform(instance,fieldIndex), addresses[fieldIndex], value);
    }

    @Override
    public void setChar(Object instance, int fieldIndex, char value)
    {
        unsafe.putChar(transform(instance,fieldIndex), addresses[fieldIndex], value);
    }

    @Override
    public Object getObject(Object instance, int fieldIndex)
    {
        return unsafe.getObject(transform(instance,fieldIndex), addresses[fieldIndex]);
    }

    @Override
    public String getString(Object instance, int fieldIndex)
    {
        return (String)unsafe.getObject(transform(instance,fieldIndex), addresses[fieldIndex]);
    }

    @Override
    public char getChar(Object instance, int fieldIndex)
    {
        return unsafe.getChar(transform(instance,fieldIndex), addresses[fieldIndex]);
    }

    @Override
    public boolean getBoolean(Object instance, int fieldIndex)
    {
        return unsafe.getBoolean(transform(instance,fieldIndex), addresses[fieldIndex]);
    }

    @Override
    public byte getByte(Object instance, int fieldIndex)
    {
        return unsafe.getByte(transform(instance,fieldIndex), addresses[fieldIndex]);
    }

    @Override
    public short getShort(Object instance, int fieldIndex)
    {
        return unsafe.getShort(transform(instance,fieldIndex), addresses[fieldIndex]);
    }

    @Override
    public int getInt(Object instance, int fieldIndex)
    {
        return unsafe.getInt(transform(instance,fieldIndex), addresses[fieldIndex]);
    }

    @Override
    public long getLong(Object instance, int fieldIndex)
    {
        return unsafe.getLong(transform(instance,fieldIndex), addresses[fieldIndex]);
    }

    @Override
    public double getDouble(Object instance, int fieldIndex)
    {
        return unsafe.getDouble(transform(instance,fieldIndex), addresses[fieldIndex]);
    }

    @Override
    public float getFloat(Object instance, int fieldIndex)
    {
        return unsafe.getFloat(transform(instance,fieldIndex), addresses[fieldIndex]);
    }

}
