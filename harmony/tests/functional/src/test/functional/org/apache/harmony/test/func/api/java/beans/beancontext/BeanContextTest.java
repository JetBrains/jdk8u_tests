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
package org.apache.harmony.test.func.api.java.beans.beancontext;

import java.beans.beancontext.BeanContextSupport;
import java.beans.beancontext.BeanContextChildSupport;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;
import java.util.*;

/**
 */

public class BeanContextTest extends MultiCase {

    // The bean context object to be checked by the test
    BeanContextSupport context = null;

    // Components that should be added to context
    BeanContextChildSupport bean = new BeanContextChildSupport();

    BeanContextChildSupport[] beans = new BeanContextChildSupport[100];

    BeanContextChildSupport child = null;

    /**
     * Adding the components and initialization
     * 
     * @see java.beans.beancontext.BeanContextSupport#add(Object)
     * @see java.beans.beancontext.BeanContextSupport#setDesignTime(boolean)
     * @see java.beans.beancontext.BeanContextSupport#setLocale(Locale)
     */
    protected void setUp() throws Exception {
        context = new BeanContextSupport();
        context.add(bean);
        context.setDesignTime(true);
        context.setLocale(new Locale("rus"));

        for (int i = 0; i < 100; i++) {
            beans[i] = new BeanContextSupport();
            context.add(beans[i]);
        }
    }

    protected void tearDown() throws Exception {
        context = null;
    }

    /**
     * It's verified number of children is changes
     * 
     * @see java.beans.beancontext.BeanContextSupport#size
     */
    public Result testSimpleSizeVary() throws Exception {
        child = (BeanContextChildSupport) context
                .instantiateChild("java.beans.beancontext.BeanContextChildSupport");
        assertEquals("number of children in the bean context is varied",
                context.size(), 102);
        return result();
    }

    /**
     * It's verified number of children in the bean context
     * 
     * @see java.beans.beancontext.BeanContextSupport#size
     */
    public Result testSimpleSize() throws Exception {
        assertEquals("number of children in the bean context", context.size(),
                101);
        return result();
    }

    /**
     * It's verified the serializing of bean context
     * 
     * @see java.beans.beancontext.BeanContextSupport#isSerializing()
     */
    public Result testIsSerializing() throws Exception {
        assertFalse("the serializing of bean context", context.isSerializing());
        return result();
    }

    /**
     * It's verified that the bean context isn't empty
     * 
     * @see java.beans.beancontext.BeanContextSupport#isEmpty()
     */
    public Result testIsEmpty() throws Exception {
        assertFalse("the bean context isn't empty", context.isEmpty());
        return result();
    }

    /**
     * It's verified "DesignTime" of bean context
     * 
     * @see java.beans.beancontext.BeanContextSupport#isDesignTime()
     */
    public Result testDesignTime() {
        assertTrue("\"DesignTime\" of bean context", context.isDesignTime());
        return result();
    }

    /**
     * It's verified that the bean context locale in "rus"
     * 
     * @see java.beans.beancontext.BeanContextSupport#getLocale()
     */
    public Result testLocateBeanContext() {
        assertEquals("the bean context locale is \"rus\"",
                (context.getLocale()).toString(), "rus");
        return result();
    }

    public static void main(String[] args) {
        try {
            System.exit(new BeanContextTest().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}