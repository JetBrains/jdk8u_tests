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
/** 
 */  

package org.apache.harmony.test.func.api.java.text.share.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.harmony.share.Test;

/**
 *  
 */
public abstract class TextTestFramework extends Test {

    protected final void setFieldValue(Object target, String fieldName,
            char value) throws Throwable, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        setValueImpl(target, fieldName, new Character(value), char.class);
    }

    protected final void setFieldValue(Object target, String fieldName,
            int value) throws Throwable, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        setValueImpl(target, fieldName, new Integer(value), int.class);
    }

    protected final void setFieldValue(Object target, String fieldName,
            double value) throws Throwable, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        setValueImpl(target, fieldName, new Double(value), double.class);
    }

    protected final void setFieldValue(Object target, String fieldName,
            byte value) throws Throwable, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        setValueImpl(target, fieldName, new Byte(value), byte.class);
    }

    protected final void setFieldValue(Object target, String fieldName,
            boolean value) throws Throwable, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        setValueImpl(target, fieldName, new Boolean(value), boolean.class);
    }

    protected final void setFieldValue(Object target, String fieldName,
            short value) throws Throwable, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        setValueImpl(target, fieldName, new Short(value), short.class);
    }

    protected final void setFieldValue(Object target, String fieldName,
            float value) throws Throwable, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        setValueImpl(target, fieldName, new Float(value), float.class);
    }

    protected final void setFieldValue(Object target, String fieldName,
            long value) throws Throwable, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        setValueImpl(target, fieldName, new Long(value), long.class);
    }

    protected final void setFieldValue(Object target, String fieldName,
            Object value) throws Throwable, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        setValueImpl(target, fieldName, value, value.getClass());
    }

    protected final void setValueImpl(Object target, String fieldName,
            Object value, Class valueClass) throws Throwable,
            NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Method setter = null;
        Method getter = null;
        Object[] values = { value };
        Object[] empty = {};
        Class[] params = { valueClass };
        Class[] nothing = {};
        while (true) {
            try {
                setter = target.getClass().getMethod("set" + fieldName, params);
                break;
            } catch (NoSuchMethodException e) {
                Class base = params[0].getSuperclass();
                if (params[0].equals(base)) {
                    throw e;
                } else {
                    params[0] = base;
                }
            }
        }
        ;

        try {
            getter = target.getClass().getMethod("get" + fieldName, nothing);
        } catch (NoSuchMethodException e) {
        }

        setter.invoke(target, values);
        if (getter != null) {
            Object result = getter.invoke(target, empty);
            if (!result.equals(value)) {
                throw new Throwable(target.getClass().getName() + ".get"
                        + fieldName + " != " + target.getClass().getName()
                        + ".set" + fieldName);
            }
        }

    }
}