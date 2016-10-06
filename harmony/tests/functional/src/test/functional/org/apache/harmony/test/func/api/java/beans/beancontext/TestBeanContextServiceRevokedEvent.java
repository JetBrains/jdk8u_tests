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

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class TestBeanContextServiceRevokedEvent extends MultiCase {

    // The BeanContext object
    BeanContextServicesSupport context;

    // The ServiceBean object
    ServiceBean serviceBean;
    
    /**
     * @see java.beans.beancontext.BeanContextServiceRevokedEvent#isServiceClass()
     */
    public Result testIsServiceClass() throws Exception {
        
        context = new BeanContextServicesSupport();

        serviceBean = new ServiceBean();

        // Adding the component
        context.add(serviceBean);

        // Listen for new services
        context.addBeanContextServicesListener(serviceBean);

        TestServiceProvider provider = new TestServiceProvider();

        // Add the service to the context
        context.addService(TestCounter.class, provider);

        // Revoke the service from the context
        context.revokeService(TestCounter.class, provider, true);

        if ((context.hasService(TestCounter.class))
                || (serviceBean.bcsre.isServiceClass(TestCounter.class)))
            return passed();
        else
            return failed("method testIsServiceClasss");
    }

    /**
     * @see java.beans.beancontext.BeanContextServiceRevokedEvent#isCurrentServiceInvalidNow()
     */
    public Result testIsCurrentServiceInvalidNow() throws Exception {

        context = new BeanContextServicesSupport();

        serviceBean = new ServiceBean();

        // Adding the component
        context.add(serviceBean);

        // Listen for new services
        context.addBeanContextServicesListener(serviceBean);

        TestServiceProvider provider = new TestServiceProvider();

        // Add the service to the context
        context.addService(TestCounter.class, provider);

        // Revoke the service from the context
        context.revokeService(TestCounter.class, provider, true);

        // Verified that the method isCurrentServiceInvalidNow return true if current service is revoked
        if ((context.hasService(TestCounter.class))
                || (serviceBean.bcsre.isCurrentServiceInvalidNow()))
            return passed();
        else
            return failed("method testIsCurrentServiceInvalidNow");
    }

    /**
     * @see java.beans.beancontext.BeanContextServiceRevokedEvent#getSourceAsBeanContextServices()
     */
    public Result testGetSourceAsBeanContextServices() throws Exception {
        
        context = new BeanContextServicesSupport();

        serviceBean = new ServiceBean();

        // Adding the component
        context.add(serviceBean);

        // Listen for new services
        context.addBeanContextServicesListener(serviceBean);

        TestServiceProvider provider = new TestServiceProvider();
        
        // Add the service to the context
        context.addService(TestCounter.class, provider);

        // Revoke the service from the context
        context.revokeService(TestCounter.class, provider, true);

        // Verified that the method getSourceAsBeanContextServices return BeanContextServicesSupport object if current service is revoked
        if ((context.hasService(TestCounter.class))
                || serviceBean.bcsre.getSourceAsBeanContextServices() instanceof BeanContextServicesSupport)
            return passed();
        else
            return failed("method testGetSourceAsBeanContextServices");
    }

    /**
     * Test for revoke events numbers
     * @see java.beans.beancontext.BeanContextServiceRevokedEvent
     */
    public Result testNumberRevokedEvents() throws Exception {
        
        context = new BeanContextServicesSupport();

        serviceBean = new ServiceBean();

        // Adding the component        
        context.add(serviceBean);

        // Listen for new services
        context.addBeanContextServicesListener(serviceBean);

        TestServiceProvider provider = new TestServiceProvider();

        // Add the service to the context
        context.addService(TestCounter.class, provider);

        // Revoke the service from the context
        context.revokeService(TestCounter.class, provider, true);

        if ((context.hasService(TestCounter.class))
                || (serviceBean.counterRevokedEvent == 2))
            return passed();
        else
            return failed("method testNumberRevokedEvents");
    }

    public static void main(String[] args) {
        try {
            System.exit(new TestBeanContextServiceRevokedEvent().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}