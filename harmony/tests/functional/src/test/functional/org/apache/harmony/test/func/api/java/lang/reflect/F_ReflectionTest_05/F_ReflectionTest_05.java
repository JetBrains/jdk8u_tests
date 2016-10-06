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
 * Created on 07.04.2005
 * Last modification G.Seryakova 
 * Last modified on 08.04.2005 
 * 
 * Tests InvocationTargetException class. 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_ReflectionTest_05;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.test.func.api.java.lang.reflect.F_ReflectionTest_05.auxiliary.*;
import java.lang.reflect.*;

/**
 * Tests InvocationTargetException class.
 * 
 */
public class F_ReflectionTest_05 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_ReflectionTest_05().test(args));
    }

    class MyInvocationTargetException extends InvocationTargetException {
        MyInvocationTargetException() {
            super();
        }
    }

    public int test() {
        Class cls = ReflectionTestObject.class;
        Method mthd = cls.getDeclaredMethods()[0];
        try {
            try {
                invoke(mthd, new Object[] { new Integer(0) }, 0);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                if (e.getCause() != null) {
                    return fail("Target exception must be null.");
                }            
            }
            try {
                invoke(mthd, new Object[] { new Integer(1) }, 1);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                if (!NumberFormatException.class.isInstance(e.getTargetException())) {
                    return fail("Target exception must be NumberFormatException.");
                }
                if (!e.getMessage().equals("n > 0")) {
                    return fail("Message must be n > 0.");
                }
            }
            try {
                invoke(mthd, new Object[] { new Integer(-1) }, -1);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                if ((!IllegalArgumentException.class.isInstance(e.getCause()))) {
                    return fail("Target exception must be IllegalArgumentException.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return fail("Unexpected exception.");
        }
        
        try {
            mthd = Integer.class.getMethod("parseInt", new Class[] {String.class});
            try {
                invoke(mthd, new Object[] {"abc"}, -1);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                if (!NumberFormatException.class.isInstance(e.getTargetException())) {
                    return fail("Target exception must be NumberFormatException.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return fail("Unexpected exception.");
        }
        return pass();
    }

    private Object invoke(Method mthd, Object[] objs, int n) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Object result = null;
        try {
            mthd.invoke(null, objs);
        } catch (InvocationTargetException e) {
            if (n == 0) {
                throw new MyInvocationTargetException();
            } else if (n < 0) {
                throw new InvocationTargetException(e.getCause());
            } else {
                throw new InvocationTargetException(e.getTargetException(),
                        "n > 0");
            }
        }
        return result;
    }
}