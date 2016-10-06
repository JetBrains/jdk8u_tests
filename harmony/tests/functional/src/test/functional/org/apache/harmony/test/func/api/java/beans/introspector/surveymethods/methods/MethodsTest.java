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
package org.apache.harmony.test.func.api.java.beans.introspector.surveymethods.methods;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: Introspector, MethodDescriptor, FeatureDescriptor.
 * <p>
 * Purpose: verify that Introspector correctly introspects methods in a bean and
 * returns only public methods of a bean.
 * 
 * @see java.beans.BeanInfo#getMethodDescriptors()
 */
public class MethodsTest extends MultiCase {
    MethodDescriptor[] methodDescriptors;

    public static void main(String[] args) {
        System.exit(new MethodsTest().test(args));
    }

    protected void setUp() throws IntrospectionException {
        methodDescriptors = Introspector.getBeanInfo(Bean1.class)
            .getMethodDescriptors();
    }

    /**
     * Verify that Introspector doesn't return protected method.
     */
    public Result testProtectedMethod() {
        if (findMethod("f1") != -1) {
            return failed("Introspector have not to return protected method f1()");
        }
        return passed();
    }

    /**
     * Verify that Introspector doesn't return package protected method.
     */
    public Result testMethodWithoutIndentificator() {
        if (findMethod("f2") != -1) {
            return failed("Introspector have not to return package protected method f2()");
        }
        return passed();
    }

    /**
     * Verify that Introspector returns public method.
     */
    public Result testPublicMethod() {
        if (findMethod("ggetI") == -1) {
            return failed("Introspecor have to return public method ggetI");
        }
        return passed();
    }

    /**
     * Verify that Introspector doesn't return private method.
     */
    public Result testPrivateMethod() {
        if (findMethod("setProperty9") != -1) {
            return failed("Introspector have not to return private method setProperty9()");
        }
        return passed();
    }

    private int findMethod(String name) {
        for (int i = 0; i < methodDescriptors.length; i++) {
            if (methodDescriptors[i].getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Verify that Introspector finds all public methods.
     */
    public Result testReturnedMethods() {
        assertTrue(findMethod("getProperty8") != -1);
        assertTrue(findMethod("setProperty8") != -1);
        assertTrue(findMethod("removeFredListener") != -1);
        assertTrue(findMethod("addFredListener") != -1);
        assertTrue(findMethod("ssetI") != -1);
        assertTrue(findMethod("ggetI") != -1);
        assertTrue(findMethod("getClass") != -1);
        assertTrue(findMethod("equals") != -1);
        assertTrue(findMethod("hashCode") != -1);
        assertTrue(findMethod("notify") != -1);
        assertTrue(findMethod("notifyAll") != -1);
        assertTrue(findMethod("toString") != -1);
        assertTrue(findMethod("wait") != -1);
        // There are 3 wait methods in Object class.
        assertEquals(methodDescriptors.length, 15);
        return passed();
    }
}