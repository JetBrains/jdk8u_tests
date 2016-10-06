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
 * Created on 11.03.2005
 * Last modification Galina Seryakova
 * Last modified on 11.03.2005
 * 
 * Test for java.lang.reflect.Proxy class
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_ProxyTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.lang.reflect.*;

/**
 * Test for java.lang.reflect.Proxy class
 * 
 */
public class F_ProxyTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_ProxyTest_01().test(args));
    }

    class MyHandler implements InvocationHandler {
        Object obj;

        public MyHandler(Object obj) {
            this.obj = obj;
        }

        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            Object result = null;
            result = method.invoke(obj, args);
            return result;
        }
    }

    public int test() {
        if (!task1()) {
            return fail("fail in task1.");
        }
        if (!task2()) {
            return fail("fail in task2.");
        }
        if (!task3()) {
            return fail("fail in task3.");
        }
        return pass();
    }

    public boolean task1() {
        Field fld = null;
        try {
            fld = Integer.class.getField("MIN_VALUE");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        }
        InvocationHandler handler = new MyHandler(fld);
        Member mmb = (Member) Proxy.newProxyInstance(this.getClass()
                .getClassLoader(), new Class[] { Member.class }, handler);
        if (!Proxy.getInvocationHandler(mmb).equals(handler)) {
            System.out.println("Invocation handlers aren't equal.");
            return false;
        }
        StringBuffer res = new StringBuffer(mmb.getDeclaringClass().getName()
                + " ");
        res.append(Modifier.toString(mmb.getModifiers()) + " ");
        res.append(mmb.getName() + " ");
        res.append(mmb.equals(new Object()));
        res.append(' ');
        res.append(Proxy.isProxyClass(mmb.getClass()));
        if (!res.toString().trim().equals(
                "java.lang.Integer public static final MIN_VALUE false true")) {
            System.out
                    .println("Must be: java.lang.Integer public static final MIN_VALUE false");
            System.out.println("But is: " + res);
            return false;
        }
        System.out.println("Is: " + res);
        return true;
    }

    public boolean task2() {
        String str = "This is test string.";
        CharSequence csec = null;
        InvocationHandler handler = new MyHandler(str);
        Class proxyClass = Proxy.getProxyClass(
                this.getClass().getClassLoader(),
                new Class[] { CharSequence.class });
        try {
            csec = (CharSequence) proxyClass.getConstructor(
                    new Class[] { InvocationHandler.class }).newInstance(
                    new Object[] { handler });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (!Proxy.getInvocationHandler(csec).equals(handler)) {
            System.out.println("Invocation handlers aren't equal.");
            return false;
        }
        StringBuffer res = new StringBuffer(csec.subSequence(0, 12).toString()
                + " ");
        res.append(csec.charAt(15) + " ");
        res.append(csec.length() + " ");
        res.append(csec.equals(new String("This is test string.")));
        res.append(' ');
        res.append(Proxy.isProxyClass(csec.getClass()));
        if (!res.toString().trim().equals("This is test r 20 true true")) {
            System.out.println("Must be: This is test r 20 true true");
            System.out.println("But is: " + res);
            return false;
        }
        System.out.println("Is: " + res);
        return true;
    }

    public boolean task3() {
        String str = "This is test string.";
        InvocationHandler handler = new MyHandler(str);
        CharSequence csec = (CharSequence) Proxy.newProxyInstance(this
                .getClass().getClassLoader(),
                new Class[] { CharSequence.class }, handler);
        try {
            charAt(csec, 20);
        } catch (UndeclaredThrowableException e) {
//            e.printStackTrace();
            if (!StringIndexOutOfBoundsException.class.isInstance(e
                    .getUndeclaredThrowable())) {
                fail("Target exception must be StringIndexOutOfBoundsException.");
                return false;
            }
        }
        try {
            charAt(csec, 50);
        } catch (UndeclaredThrowableException e) {
//            e.printStackTrace();
            if (!StringIndexOutOfBoundsException.class.isInstance(e.getCause())) {
                fail("Target exception must be StringIndexOutOfBoundsException.");
                return false;
            }
            if (!e.getMessage().equals("n > 40")) {
                fail("Message must be n > 40.");
                return false;
            }
        }
        return true;
    }

    private char charAt(CharSequence csec, int n) {
        try {
            return csec.charAt(n);
        } catch (UndeclaredThrowableException e) {
            if (n < 40) {
                throw new UndeclaredThrowableException(e
                        .getUndeclaredThrowable().getCause());
            } else {
                throw new UndeclaredThrowableException(e.getCause().getCause(),
                        "n > 40");
            }
        }
    }
}