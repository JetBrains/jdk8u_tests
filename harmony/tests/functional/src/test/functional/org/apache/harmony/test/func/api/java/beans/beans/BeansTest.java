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
package org.apache.harmony.test.func.api.java.beans.beans;

import java.beans.Beans;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: Beans class.
 * <p>
 * Purpose: Verify that Beans#instantiate(..) methods correctly instantiate
 * bean. Also this test verifies that bean is loaded from file.
 * <p>
 * Create jar-file with help SerializeBeans class and include this file in class
 * path, before run this test.
 * 
 */
public class BeansTest extends MultiCase {
    public static void main(String[] args) {
        try {
            System.exit(new BeansTest().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verify that instantiate(..) method loads serialised bean.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create a bean class, which has a field. Instantiate the bean and set
     * the field of the bean to something value.
     * <li>Serialise bean to a directory which is in class path, but in which
     * there isn't the bean class.
     * <li>Deserialize the bean using instantiate(null,DotSeparatedNameOfBean)
     * method.
     * <li>Verify that field of the bean is set to the value of the first item.
     */
    public Result testLoadSerialisedBean() throws Exception {
        Bean1 bean1 = (Bean1)Beans.instantiate(null, Bean1.class.getName());
        assertEquals(bean1.i, 5);
        return result();
    }

    /**
     * Verify that instantiate(..) method loads bean as class.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create a bean class.
     * <li>Load the bean using instantiate(null,DotSeparatedNameOfBean) method.
     * <li>Verify that instantiate(..) method returns instance of the bean
     * class.
     */
    public Result testLoadBeanAsClass() throws Exception {
        Bean2 bean2 = (Bean2)Beans.instantiate(null, Bean2.class.getName());
        return result();
    }

    /**
     * Verify that bean is added to bean context.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create a bean class.
     * <li>Create beanContext which extends BeanContextSupport and redefines
     * add(Object) method.
     * <li>Load the bean using
     * instantiate(null,DotSeparatedNameOfBean,beanContext) method.
     * <li>Verify that add(Object) method of beanContext was invoked, with the
     * bean as parameter of this method.
     */
    public Result testAddBeanToBeanContext() throws Exception {
        SimpleBeanContext beanContext = new SimpleBeanContext();
        Bean1 bean1 = (Bean1)Beans.instantiate(null, Bean1.class.getName(),
            beanContext);
        assertEquals(beanContext.bean, bean1);
        return result();
    }

    /**
     * Verify that instantiate(..) method uses class loader pointed in
     * parameters of instantiate(..) method.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create a bean class.
     * <li>Create classLoader, which extends java.lang.ClassLoader class and
     * redefines loadClass(String) method.
     * <li>Load the bean using instantiate(classLoader,DotSeparatedNameOfBean)
     * method.
     * <li>Verify that load(String) method was invoked.
     */
    public Result testClassLoader() throws Exception {
        PrimitiveClassLoader classLoader = new PrimitiveClassLoader();
        Beans.instantiate(classLoader, Bean1.class.getName());
        assertTrue(classLoader.isInvoked);
        return result();
    }

    /**
     * Verify that instantiate(..) method invokes initialize(..) and
     * activate(..) methods of AppletInitializer, if appletInitializer parameter
     * isn't null.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create applet class, which extends java.applet.Applet.
     * <li>(May be miss)Create beanContext, which extends BeanContextSupport.
     * <li>Create appletInitializer class, which implements
     * java.beans.AppletInitializer.
     * <li>Load the applet using
     * instantiate(null,DotSeparatedNameOfBean,beanContext,appletInitializer)
     * method.
     * <li>Verify that initialize(..) method of AppletInitializer was invoked
     * with parameters: applet, beanContext.
     * <li>Verify that after initialize(..) method, activate(..) method of
     * AppletInitializer was invoked with parameter: applet.
     * <li>Verify that instantiate(..) method invoked init() method of applet.
     */
    public Result testAppletInitializer() throws Exception {
        Beans.instantiate(null, SimpleApplet.class.getName(),
            new SimpleBeanContext(), new SimpleAppletInitializer());
        SimpleAppletInitializer.verifyException();
        assertTrue(SimpleApplet.isInitInvoked());
        return result();
    }

    /**
     * Verify that when instantiate(..) method loads serialised applet, it
     * doesn't invoke init() method of applet.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create class, which extends java.applet.Applet and redefines init()
     * method.
     * <li>Serialise applet.
     * <li>Load the applet using instantiate(null,DotSeparatedNameOfBean)
     * method.
     * <li>Verify that instantiate(..) method doesn't invoke init() method of
     * applet.
     */
    public Result testNotInvokeInit() throws Exception {
        Beans.instantiate(null, SimpleApplet2.class.getName());
        assertFalse(SimpleApplet2.isInitInvoked());
        return result();

    }
}