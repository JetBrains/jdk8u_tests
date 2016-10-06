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
 
package org.apache.harmony.test.func.reg.jit.btest4768;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;;

public class Btest4768 {   

    public static void main(String[] args) {
        new Btest4768().test();
    }

    public void test() {
        String str = "This is test string.";
        CharSequence csec = null;
        InvocationHandler handler = new MyHandler(str);

        System.err.println("Start test ... ");

        csec = (CharSequence)Proxy.newProxyInstance(
                this.getClass().getClassLoader(), new Class[] { 
            CharSequence.class }, handler);
            System.err.println(csec.charAt(15));        
            System.err.println("PASSED!");
        }

    class MyHandler implements InvocationHandler {
        Object obj;

        public MyHandler(Object obj) {
            this.obj = obj;
        }

        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            System.err.println("I'm in invoke method");
            return method.invoke(obj, args);
        }
    }

}
