/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/*
 * Created on 3.12.2004
 * Last modification G.Seryakova
 * Last modified on 3.12.2004
 * 
 * Manipulating Objects.
 * 
 * methods for reflection scenario tests.
 */
package org.apache.harmony.test.func.api.java.lang.reflect.share;

import java.lang.reflect.*;

/**
 * methods for reflection scenario tests.
 * 
 */
public class ReflectionTestLib {

    public static Object createObject(String name, ClassLoader clsLoader)
        throws ClassNotFoundException, InstantiationException,
        IllegalAccessException {
        Object obj = null;

        try {
            Class cls = Class.forName(name, true, clsLoader);
            obj = cls.newInstance();
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("In Class.forName for name ("
                + name + "):" + e.getMessage());
        } catch (InstantiationException e) {
            throw new InstantiationException("In newInstance() for name ("
                + name + "):" + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalAccessException("In newInstance() for name ("
                + name + "):" + e.getMessage());
        }

        return obj;
    }

    public static Object createObject(String name, ClassLoader clsLoader,
        Object[] args, Class[] types) throws ClassNotFoundException,
        NoSuchMethodException, IllegalArgumentException,
        InstantiationException, IllegalAccessException,
        InvocationTargetException {
        Object obj = null;

        try {
            Class cls = Class.forName(name, true, clsLoader);
            Constructor constr = getConstructor(cls, types);
            obj = constr.newInstance(args);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("In forName() for name (" + name
                + "):" + e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodException("In getConstructor() for name ("
                + name + "):" + e.getMessage());
        } catch (InstantiationException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("In constructor (" + name + "):"
                + e.getMessage());
        } catch (InvocationTargetException e) {
            throw e;
        }

        return obj;
    }

    public static Constructor getConstructor(Class cls, Class[] types)
        throws NoSuchMethodException {
        Constructor constr = null;
        try {
            constr = cls.getConstructor(types);
        } catch (NoSuchMethodException e) {
            try {
                constr = cls.getDeclaredConstructor(types);
            } catch (NoSuchMethodException e1) {
                throw new NoSuchMethodException(
                    "In getConstructor() for class (" + cls.getName() + "):"
                        + e1.getMessage());
            }
            if (!constr.isAccessible()) {
                constr.setAccessible(true);
            }
        }
        return constr;
    }

    public static Object getFieldValue(Object obj, String name)
        throws NoSuchFieldException, IllegalArgumentException,
        IllegalAccessException {
        Object value = null;

        try {
            Field fld = getField(obj.getClass(), name);
            value = fld.get(obj);
        } catch (NoSuchFieldException e) {
            throw new NoSuchFieldException("In getField(" + name
                + ") for class " + obj.getClass().getName() + ":"
                + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("In getField(" + name
                + ") for class " + obj.getClass().getName() + ":"
                + e.getMessage());
        } catch (IllegalAccessException e) {
            throw e;
        }

        return value;
    }

    public static Object setFieldValue(Object obj, String name, Object value)
        throws NoSuchFieldException, IllegalArgumentException,
        IllegalAccessException {

        try {
            Field fld = getField(obj.getClass(), name);
            fld.set(obj, value);
        } catch (NoSuchFieldException e) {
            throw new NoSuchFieldException("In setField(" + name
                + ") for class " + obj.getClass().getName() + ":"
                + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("In setField(" + name
                + ") for class " + obj.getClass().getName() + ":"
                + e.getMessage());
        } catch (IllegalAccessException e) {
            throw e;
        }

        return value;
    }

    public static Field getField(Class cls, String name)
        throws NoSuchFieldException {
        Field fld;
        try {
            fld = cls.getField(name);
        } catch (NoSuchFieldException e) {
            try {
                fld = cls.getDeclaredField(name);
            } catch (NoSuchFieldException e1) {
                throw new NoSuchFieldException("In getField(" + name
                    + ") for class (" + cls.getName() + "):" + e1.getMessage());
            }
            if (!fld.isAccessible()) {
                fld.setAccessible(true);
            }            
        }
        return fld;
    }

    public static Object invokeMethod(Object obj, String name, Object[] args,
        Class[] types) throws NoSuchMethodException, IllegalAccessException,
        IllegalArgumentException, InvocationTargetException {
        Object result = null;

        try {
            Method mthd = getMethod(obj.getClass(), name, types);
            result = mthd.invoke(obj, args);
        } catch (NoSuchMethodException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("In invokeMethod (" + name
                + ") for class (" + obj.getClass().getName() + "):"
                + e.getMessage());
        } catch (InvocationTargetException e) {
            throw e;
        }
        return result;
    }

    public static Method getMethod(Class cls, String name, Class[] types)
        throws NoSuchMethodException {
        Method mth;
        try {
            mth = cls.getMethod(name, types);
        } catch (NoSuchMethodException e) {
            try {
                mth = cls.getDeclaredMethod(name, types);
            } catch (NoSuchMethodException e1) {
                throw new NoSuchMethodException("In getMethod(" + name
                    + ") for class (" + cls.getName() + "):" + e1.getMessage());
            }
            if (!mth.isAccessible()) {
                mth.setAccessible(true);                
            }
        }
        return mth;
    }
}