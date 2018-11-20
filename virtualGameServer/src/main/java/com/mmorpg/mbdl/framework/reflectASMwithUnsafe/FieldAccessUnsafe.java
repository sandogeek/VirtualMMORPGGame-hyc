package com.mmorpg.mbdl.framework.reflectASMwithUnsafe;

/**
 * 利用Unsafe访问私有成员
 * TODO 利用ByteBuddy重写ReflectASM
 * ReflectASM使用场景：大量使用反射访问类成员
 * FieldAccessUnsafe come form https://github.com/EsotericSoftware/reflectasm/pull/39 has some revise.
 **/
import sun.misc.Unsafe;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;

@SuppressWarnings("restriction")
class FieldAccessUnsafe extends FieldAccess {
    private static final Unsafe unsafe;
    long[] addresses;
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
                throw new RuntimeException("Unsafe获取失败", e.getCause());
            }
        }
        unsafe = unsafeTry;
    }
    FieldAccessUnsafe(Class<?> clazz)
    {
        if(unsafe == null) {
            throw new UnsupportedOperationException();
        }
        Field[] fields = clazz.getDeclaredFields();

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
            this.addresses[i] = unsafe.objectFieldOffset(fields[i]);
        }
    }

    @Override
    public void setObject(Object instance, int fieldIndex, Object value)
    {
        unsafe.putObject(instance, addresses[fieldIndex], value);
    }

    @Override
    public void setBoolean(Object instance, int fieldIndex, boolean value)
    {
        unsafe.putBoolean(instance, addresses[fieldIndex], value);
    }

    @Override
    public void setByte(Object instance, int fieldIndex, byte value)
    {
        unsafe.putByte(instance, addresses[fieldIndex], value);
    }

    @Override
    public void setShort(Object instance, int fieldIndex, short value)
    {
        unsafe.putShort(instance, addresses[fieldIndex], value);
    }

    @Override
    public void setInt(Object instance, int fieldIndex, int value)
    {
        unsafe.putInt(instance, addresses[fieldIndex], value);
    }

    @Override
    public void setLong(Object instance, int fieldIndex, long value)
    {
        unsafe.putLong(instance, addresses[fieldIndex], value);
    }

    @Override
    public void setDouble(Object instance, int fieldIndex, double value)
    {
        unsafe.putDouble(instance, addresses[fieldIndex], value);
    }

    @Override
    public void setFloat(Object instance, int fieldIndex, float value)
    {
        unsafe.putFloat(instance, addresses[fieldIndex], value);
    }

    @Override
    public void setChar(Object instance, int fieldIndex, char value)
    {
        unsafe.putChar(instance, addresses[fieldIndex], value);
    }

    @Override
    public Object getObject(Object instance, int fieldIndex)
    {
        return unsafe.getObject(instance, addresses[fieldIndex]);
    }

    @Override
    public String getString(Object instance, int fieldIndex)
    {
        return (String)unsafe.getObject(instance, addresses[fieldIndex]);
    }

    @Override
    public char getChar(Object instance, int fieldIndex)
    {
        return unsafe.getChar(instance, addresses[fieldIndex]);
    }

    @Override
    public boolean getBoolean(Object instance, int fieldIndex)
    {
        return unsafe.getBoolean(instance, addresses[fieldIndex]);
    }

    @Override
    public byte getByte(Object instance, int fieldIndex)
    {
        return unsafe.getByte(instance, addresses[fieldIndex]);
    }

    @Override
    public short getShort(Object instance, int fieldIndex)
    {
        return unsafe.getShort(instance, addresses[fieldIndex]);
    }

    @Override
    public int getInt(Object instance, int fieldIndex)
    {
        return unsafe.getInt(instance, addresses[fieldIndex]);
    }

    @Override
    public long getLong(Object instance, int fieldIndex)
    {
        return unsafe.getLong(instance, addresses[fieldIndex]);
    }

    @Override
    public double getDouble(Object instance, int fieldIndex)
    {
        return unsafe.getDouble(instance, addresses[fieldIndex]);
    }

    @Override
    public float getFloat(Object instance, int fieldIndex)
    {
        return unsafe.getFloat(instance, addresses[fieldIndex]);
    }

}
