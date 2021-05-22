package com.xing.gfox.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 反射工具类
 */
public class U_reflector {
    
    public static final String TAG = "Reflector";
    
    protected Class<?> mType;
    protected Object mCaller;
    protected Constructor mConstructor;
    protected Field mField;
    protected Method mMethod;
    
    public static class ReflectedException extends Exception {
    
        public ReflectedException(String message) {
            super(message);
        }
        public ReflectedException(String message, Throwable cause) {
            super(message, cause);
        }
    
    }
    public static U_reflector on(@NonNull String name) throws ReflectedException {
        return on(name, true, U_reflector.class.getClassLoader());
    }
    
    public static U_reflector on(@NonNull String name, boolean initialize) throws ReflectedException {
        return on(name, initialize, U_reflector.class.getClassLoader());
    }
    
    public static U_reflector on(@NonNull String name, boolean initialize, @Nullable ClassLoader loader) throws ReflectedException {
        try {
            return on(Class.forName(name, initialize, loader));
        } catch (Throwable e) {
            throw new ReflectedException("Oops!", e);
        }
    }
    
    public static U_reflector on(@NonNull Class<?> type) {
        U_reflector hlreflector = new U_reflector();
        hlreflector.mType = type;
        return hlreflector;
    }
    
    public static U_reflector with(@NonNull Object caller) throws ReflectedException {
        return on(caller.getClass()).bind(caller);
    }
    
    protected U_reflector() {
    
    }
    
    public U_reflector constructor(@Nullable Class<?>... parameterTypes) throws ReflectedException {
        try {
            mConstructor = mType.getDeclaredConstructor(parameterTypes);
            mConstructor.setAccessible(true);
            mField = null;
            mMethod = null;
            return this;
        } catch (Throwable e) {
            throw new ReflectedException("Oops!", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public <R> R newInstance(@Nullable Object ... initargs) throws ReflectedException {
        if (mConstructor == null) {
            throw new ReflectedException("Constructor was null!");
        }
        try {
            return (R) mConstructor.newInstance(initargs);
        } catch (InvocationTargetException e) {
            throw new ReflectedException("Oops!", e.getTargetException());
        } catch (Throwable e) {
            throw new ReflectedException("Oops!", e);
        }
    }
    
    protected Object checked(@Nullable Object caller) throws ReflectedException {
        if (caller == null || mType.isInstance(caller)) {
            return caller;
        }
        throw new ReflectedException("Caller [" + caller + "] is not a instance of type [" + mType + "]!");
    }
    
    protected void check(@Nullable Object caller, @Nullable Member member, @NonNull String name) throws ReflectedException {
        if (member == null) {
            throw new ReflectedException(name + " was null!");
        }
        if (caller == null && !Modifier.isStatic(member.getModifiers())) {
            throw new ReflectedException("Need a caller!");
        }
        checked(caller);
    }
    
    public U_reflector bind(@Nullable Object caller) throws ReflectedException {
        mCaller = checked(caller);
        return this;
    }
    
    public U_reflector unbind() {
        mCaller = null;
        return this;
    }
    
    public U_reflector field(@NonNull String name) throws ReflectedException {
        try {
            mField = findField(name);
            mField.setAccessible(true);
            mConstructor = null;
            mMethod = null;
            return this;
        } catch (Throwable e) {
            throw new ReflectedException("Oops!", e);
        }
    }
    
    protected Field findField(@NonNull String name) throws NoSuchFieldException {
        try {
            return mType.getField(name);
        } catch (NoSuchFieldException e) {
            for (Class<?> cls = mType; cls != null; cls = cls.getSuperclass()) {
                try {
                    return cls.getDeclaredField(name);
                } catch (NoSuchFieldException ex) {
                    // Ignored
                }
            }
            throw e;
        }
    }
    
    @SuppressWarnings("unchecked")
    public <R> R get() throws ReflectedException {
        return get(mCaller);
    }
    
    @SuppressWarnings("unchecked")
    public <R> R get(@Nullable Object caller) throws ReflectedException {
        check(caller, mField, "Field");
        try {
            return (R) mField.get(caller);
        } catch (Throwable e) {
            throw new ReflectedException("Oops!", e);
        }
    }
    
    public U_reflector set(@Nullable Object value) throws ReflectedException {
        return set(mCaller, value);
    }
    
    public U_reflector set(@Nullable Object caller, @Nullable Object value) throws ReflectedException {
        check(caller, mField, "Field");
        try {
            mField.set(caller, value);
            return this;
        } catch (Throwable e) {
            throw new ReflectedException("Oops!", e);
        }
    }
    
    public U_reflector method(@NonNull String name, @Nullable Class<?>... parameterTypes) throws ReflectedException {
        try {
            mMethod = findMethod(name, parameterTypes);
            mMethod.setAccessible(true);
            mConstructor = null;
            mField = null;
            return this;
        } catch (NoSuchMethodException e) {
            throw new ReflectedException("Oops!", e);
        }
    }
    
    protected Method findMethod(@NonNull String name, @Nullable Class<?>... parameterTypes) throws NoSuchMethodException {
        try {
            return mType.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            for (Class<?> cls = mType; cls != null; cls = cls.getSuperclass()) {
                try {
                    return cls.getDeclaredMethod(name, parameterTypes);
                } catch (NoSuchMethodException ex) {
                    // Ignored
                }
            }
            throw e;
        }
    }
    
    public <R> R call(@Nullable Object... args) throws ReflectedException {
        return callByCaller(mCaller, args);
    }
    
    @SuppressWarnings("unchecked")
    public <R> R callByCaller(@Nullable Object caller, @Nullable Object... args) throws ReflectedException {
        check(caller, mMethod, "Method");
        try {
            return (R) mMethod.invoke(caller, args);
        } catch (InvocationTargetException e) {
            throw new ReflectedException("Oops!", e.getTargetException());
        } catch (Throwable e) {
            throw new ReflectedException("Oops!", e);
        }
    }
    
    public static class QuietHlreflector extends U_reflector {
        
        protected Throwable mIgnored;
    
        public static QuietHlreflector on(@NonNull String name) {
            return on(name, true, QuietHlreflector.class.getClassLoader());
        }
    
        public static QuietHlreflector on(@NonNull String name, boolean initialize) {
            return on(name, initialize, QuietHlreflector.class.getClassLoader());
        }
    
        public static QuietHlreflector on(@NonNull String name, boolean initialize, @Nullable ClassLoader loader) {
            Class<?> cls = null;
            try {
                cls = Class.forName(name, initialize, loader);
                return on(cls, null);
            } catch (Throwable e) {
//                Log.w(LOG_TAG, "Oops!", e);
                return on(cls, e);
            }
        }
    
        public static QuietHlreflector on(@Nullable Class<?> type) {
            return on(type, (type == null) ? new ReflectedException("Type was null!") : null);
        }
    
        private static QuietHlreflector on(@Nullable Class<?> type, @Nullable Throwable ignored) {
            QuietHlreflector reflector = new QuietHlreflector();
            reflector.mType = type;
            reflector.mIgnored = ignored;
            return reflector;
        }
    
        public static QuietHlreflector with(@Nullable Object caller) {
            if (caller == null) {
                return on((Class<?>) null);
            }
            return on(caller.getClass()).bind(caller);
        }
        
        protected QuietHlreflector() {
            
        }
    
        public Throwable getIgnored() {
            return mIgnored;
        }
    
        protected boolean skip() {
            return skipAlways() || mIgnored != null;
        }
        
        protected boolean skipAlways() {
            return mType == null;
        }
    
        @Override
        public QuietHlreflector constructor(@Nullable Class<?>... parameterTypes) {
            if (skipAlways()) {
                return this;
            }
            try {
                mIgnored = null;
                super.constructor(parameterTypes);
            } catch (Throwable e) {
                mIgnored = e;
//                Log.w(LOG_TAG, "Oops!", e);
            }
            return this;
        }
    
        @Override
        public <R> R newInstance(@Nullable Object... initargs) {
            if (skip()) {
                return null;
            }
            try {
                mIgnored = null;
                return super.newInstance(initargs);
            } catch (Throwable e) {
                mIgnored = e;
//                Log.w(LOG_TAG, "Oops!", e);
            }
            return null;
        }
    
        @Override
        public QuietHlreflector bind(@Nullable Object obj) {
            if (skipAlways()) {
                return this;
            }
            try {
                mIgnored = null;
                super.bind(obj);
            } catch (Throwable e) {
                mIgnored = e;
//                Log.w(LOG_TAG, "Oops!", e);
            }
            return this;
        }
    
        @Override
        public QuietHlreflector unbind() {
            super.unbind();
            return this;
        }
    
        @Override
        public QuietHlreflector field(@NonNull String name) {
            if (skipAlways()) {
                return this;
            }
            try {
                mIgnored = null;
                super.field(name);
            } catch (Throwable e) {
                mIgnored = e;
//                Log.w(LOG_TAG, "Oops!", e);
            }
            return this;
        }
    
        @Override
        public <R> R get() {
            if (skip()) {
                return null;
            }
            try {
                mIgnored = null;
                return super.get();
            } catch (Throwable e) {
                mIgnored = e;
//                Log.w(LOG_TAG, "Oops!", e);
            }
            return null;
        }
    
        @Override
        public <R> R get(@Nullable Object caller) {
            if (skip()) {
                return null;
            }
            try {
                mIgnored = null;
                return super.get(caller);
            } catch (Throwable e) {
                mIgnored = e;
//                Log.w(LOG_TAG, "Oops!", e);
            }
            return null;
        }
    
        @Override
        public QuietHlreflector set(@Nullable Object value) {
            if (skip()) {
                return this;
            }
            try {
                mIgnored = null;
                super.set(value);
            } catch (Throwable e) {
                mIgnored = e;
//                Log.w(LOG_TAG, "Oops!", e);
            }
            return this;
        }
    
        @Override
        public QuietHlreflector set(@Nullable Object caller, @Nullable Object value) {
            if (skip()) {
                return this;
            }
            try {
                mIgnored = null;
                super.set(caller, value);
            } catch (Throwable e) {
                mIgnored = e;
//                Log.w(LOG_TAG, "Oops!", e);
            }
            return this;
        }
    
        @Override
        public QuietHlreflector method(@NonNull String name, @Nullable Class<?>... parameterTypes) {
            if (skipAlways()) {
                return this;
            }
            try {
                mIgnored = null;
                super.method(name, parameterTypes);
            } catch (Throwable e) {
                mIgnored = e;
//                Log.w(LOG_TAG, "Oops!", e);
            }
            return this;
        }
    
        @Override
        public <R> R call(@Nullable Object... args)  {
            if (skip()) {
                return null;
            }
            try {
                mIgnored = null;
                return super.call(args);
            } catch (Throwable e) {
                mIgnored = e;
//                Log.w(LOG_TAG, "Oops!", e);
            }
            return null;
        }
    
        @Override
        public <R> R callByCaller(@Nullable Object caller, @Nullable Object... args) {
            if (skip()) {
                return null;
            }
            try {
                mIgnored = null;
                return super.callByCaller(caller, args);
            } catch (Throwable e) {
                mIgnored = e;
//                Log.w(LOG_TAG, "Oops!", e);
            }
            return null;
        }
    }
}