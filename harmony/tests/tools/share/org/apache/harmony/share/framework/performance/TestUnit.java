/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Aleksey V Golubitsky
 * @version $Revision: 1.3 $
 */
package org.apache.harmony.share.framework.performance;

import java.lang.reflect.Method;

public class TestUnit {
    private Class    clazz      = null;
    private Method   before     = null;
    private Object[] beforeArgs = null;
    private Method   test       = null;
    private Object[] testArgs   = null;
    private Method   after      = null;
    private Object[] afterArgs  = null;

    /**
     * @param clazz
     */
    public TestUnit(Class clazz) {
        setTestClass(clazz);
    }

    /**
     * @param className
     * @throws NullPointerException
     * @throws ClassNotFoundException
     */
    public TestUnit(String className) throws NullPointerException,
        ClassNotFoundException {
        if (className == null) {
            throw new NullPointerException();
        }
        setTestClass(Class.forName(className));
    }

    /**
     * @param clazz
     * @param beforeMethod
     * @param testMethod
     * @param afterMethod
     */
    public TestUnit(Class clazz, Method beforeMethod, Method testMethod,
        Method afterMethod) {
        this(clazz);
        setBefore(beforeMethod);
        setTest(testMethod);
        setAfter(afterMethod);
    }

    /**
     * @param clazz
     * @throws NullPointerException
     */
    public void setTestClass(Class clazz) throws NullPointerException {
        if (clazz == null) {
            throw new NullPointerException();
        }
        this.clazz = clazz;
    }

    /**
     * @return test class
     */
    public Class getTestClass() {
        return clazz;
    }

    /**
     * @param name
     * @throws NullPointerException
     * @throws NoSuchMethodException
     */
    public void setBefore(String name) throws NullPointerException,
        NoSuchMethodException {
        if (name == null) {
            throw new NullPointerException();
        }
        setBefore(name, null, null);
    }

    /**
     * @param name
     * @param args
     * @param params
     * @throws NullPointerException
     * @throws NoSuchMethodException
     */
    public void setBefore(String name, Class[] args, Object[] params)
        throws NullPointerException, NoSuchMethodException {
        if (name == null) {
            throw new NullPointerException();
        }
        setBefore(clazz.getMethod(name, args), params);
    }

    /**
     * @param method
     */
    public void setBefore(Method method) {
        setBefore(method, null);
    }

    /**
     * @param method
     * @param args
     */
    public void setBefore(Method method, Object[] args) {
        before = method;
        if (args != null) {
            beforeArgs = new Object[args.length];
            System.arraycopy(args, 0, beforeArgs, 0, args.length);
        } else {
            beforeArgs = null;
        }
    }

    /**
     * @param method
     * @throws NullPointerException
     */
    public void setTest(Method method) throws NullPointerException {
        setTest(method, null);
    }

    /**
     * @param method
     * @param args
     * @throws NullPointerException
     */
    public void setTest(Method method, Object[] args)
        throws NullPointerException {
        if (method == null) {
            throw new NullPointerException();
        }
        test = method;
        if (args != null) {
            testArgs = new Object[args.length];
            System.arraycopy(args, 0, testArgs, 0, args.length);
        } else {
            afterArgs = null;
        }
    }

    /**
     * @param name
     * @throws NullPointerException
     * @throws NoSuchMethodException
     */
    public void setTest(String name) throws NullPointerException,
        NoSuchMethodException {
        if (name == null) {
            throw new NullPointerException();
        }
        setTest(name, null, null);
    }

    /**
     * @param name
     * @param args
     * @param params
     * @throws NullPointerException
     * @throws NoSuchMethodException
     */
    public void setTest(String name, Class[] args, Object[] params)
        throws NullPointerException, NoSuchMethodException {
        if (name == null) {
            throw new NullPointerException();
        }
        setTest(clazz.getMethod(name, args), params);
    }

    /**
     * @param method
     */
    public void setAfter(Method method) {
        setAfter(method);
    }

    /**
     * @param method
     * @param args
     */
    public void setAfter(Method method, Object[] args) {
        after = method;
        if (args != null) {
            afterArgs = new Object[args.length];
            System.arraycopy(args, 0, afterArgs, 0, args.length);
        } else {
            afterArgs = null;
        }
    }

    /**
     * @param name
     * @throws NullPointerException
     * @throws NoSuchMethodException
     */
    public void setAfter(String name) throws NullPointerException,
        NoSuchMethodException {
        if (name == null) {
            throw new NullPointerException();
        }
        setAfter(name, null, null);
    }

    /**
     * @param name
     * @param args
     * @param params
     * @throws NullPointerException
     * @throws NoSuchMethodException
     */
    public void setAfter(String name, Class[] args, Object[] params)
        throws NullPointerException, NoSuchMethodException {
        if (name == null) {
            throw new NullPointerException();
        }
        setAfter(clazz.getMethod(name, args), params);
    }

    /**
     * @return before method
     */
    public Method getBefore() {
        return before;
    }

    /**
     * @return before method arguments
     */
    public Object[] getBeforeArgs() {
        return beforeArgs;
    }

    /**
     * @return after method
     */
    public Method getAfter() {
        return after;
    }

    /**
     * @return after method arguments
     */
    public Object[] getAfterArgs() {
        return afterArgs;
    }

    /**
     * @return method test
     */
    public Method getTest() {
        return test;
    }

    /**
     * @return method test arguments
     */
    public Object[] getTestArgs() {
        return testArgs;
    }
}