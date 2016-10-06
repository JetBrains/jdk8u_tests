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

import java.beans.beancontext.BeanContextServicesSupport;
import java.util.Iterator;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class TestBeanContextServicesSupport extends MultiCase {

    // The BeanContextServicesSupport object
    BeanContextServicesSupport context;

    // The ServiceBean object
    ServiceBean serviceBean;

    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#addBeanContextServicesListener()
     */
    public Result testAddBeanContextServicesListener() throws Exception {

        context = new BeanContextServicesSupport();
        
        serviceBean = new ServiceBean();
        
        // Adding the component
        context.add(serviceBean);

        // Listen for new services
        context.addBeanContextServicesListener(serviceBean);

        return result();
    }

    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#removeBeanContextServicesListener()
     */
    public Result testRemoveBeanContextServicesListener() throws Exception {

        context = new BeanContextServicesSupport();
        
        serviceBean = new ServiceBean();

        // Adding the component
        context.add(serviceBean);

        context.addBeanContextServicesListener(serviceBean);
        
        context.removeBeanContextServicesListener(serviceBean);

        return result();
    }

    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#addService()
     */
    public Result testAddService() throws Exception {

        context = new BeanContextServicesSupport();

        serviceBean = new ServiceBean();

        // Adding the component
        context.add(serviceBean);

        // Listen for new services
        context.addBeanContextServicesListener(serviceBean);
        
        TestServiceProvider provider = new TestServiceProvider();

        // Add service to the context
        context.addService(TestCounter.class, provider);

        return result();
    }

    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#getBeanContextServicesPeer()
     */
    public Result testGetBeanContextServicesPeer() throws Exception {

        context = new BeanContextServicesSupport();
        
        serviceBean = new ServiceBean();

        // Adding the component
        context.add(serviceBean);

        // Add service to the context
        if ((context.getBeanContextServicesPeer() instanceof BeanContextServicesSupport))

            return passed();
        else
            return failed("method testGetBeanContextServicesPeer");
    }

    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#getCurrentServiceClasses()
     */
    public Result testGetCurrentServiceClasses() throws Exception {
        
        context = new BeanContextServicesSupport();
        
        serviceBean = new ServiceBean();
        
        // Adding the component
        context.add(serviceBean);

        if ((context.getCurrentServiceClasses() instanceof Iterator))
            return passed();
        else
            return failed("method testGetCurrentServiceClasses");

    }

    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#getCurrentServiceSelectors()
     */
    public Result testGetCurrentServiceSelectors() throws Exception {

        context = new BeanContextServicesSupport();
        
        serviceBean = new ServiceBean();
        
        // Adding the component
        context.add(serviceBean);

        // Listen for new services
        context.addBeanContextServicesListener(serviceBean);
        
        TestServiceProvider provider = new TestServiceProvider();

        // Add service to the context
        context.addService(TestCounter.class, provider);
        
        if ((context.getCurrentServiceSelectors(TestCounter.class) instanceof Iterator))
            return passed();
        else
            return failed("method testGetCurrentServiceSelectors");
    }
    
    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#getService()
     */
    public Result testGetService() throws Exception {
        
        context = new BeanContextServicesSupport();
        
        serviceBean = new ServiceBean();
        
        // Adding the component
        context.add(serviceBean);

        // Listen for new services
        context.addBeanContextServicesListener(serviceBean);
        TestServiceProvider provider = new TestServiceProvider();

        // Add service to the context
        context.addService(TestCounter.class, provider);
        
        if ((serviceBean.service instanceof TestCounter))
            return passed();
        else
            return failed("method testGetService");
    }

    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#hasService()
     */
    public Result testHasService() throws Exception {

        context = new BeanContextServicesSupport();

        serviceBean = new ServiceBean();

        // Adding the component
        context.add(serviceBean);

        // Listen for new services
        context.addBeanContextServicesListener(serviceBean);

        TestServiceProvider provider = new TestServiceProvider();

        // Add service to the context
        context.addService(TestCounter.class, provider);

        if (context.hasService(TestCounter.class))

            return passed();
        else
            return failed("method testHasService");
    }

    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#initialize()
     */
    public Result testInitialize() throws Exception {

        context = new BeanContextServicesSupport();
        
        context.initialize();
        
        return result();
    }

    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#releaseService()
     */
    public Result testReleaseService() throws Exception {

        context = new BeanContextServicesSupport();

        serviceBean = new ServiceBean();

        // Adding the component
        context.add(serviceBean);

        // Listen for new services
        context.addBeanContextServicesListener(serviceBean);

        TestServiceProvider provider = new TestServiceProvider();

        // Release service
        context.releaseService(serviceBean, this, TestCounter.class);
        
        return result();
    }
    
    public static void main(String[] args) {
        try {
            System.exit(new TestBeanContextServicesSupport().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}