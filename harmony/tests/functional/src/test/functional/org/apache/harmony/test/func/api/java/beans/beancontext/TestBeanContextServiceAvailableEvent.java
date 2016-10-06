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
public class TestBeanContextServiceAvailableEvent extends MultiCase {

    // The BeanContextServicesSupport object
    BeanContextServicesSupport context;

    // The ServiceBean object
    ServiceBean serviceBean ;

    /**
     * @see java.beans.beancontext.BeanContextServiceAvailableEvent#getSourceAsBeanContextServices()
     * @see java.beans.beancontext.BeanContextServicesSupport#hasService()
     * @see java.beans.beancontext.BeanContextServicesSupport#add()
     * @see java.beans.beancontext.BeanContextServicesSupport#addBeanContextServicesListener()
     * @see java.beans.beancontext.BeanContextServicesSupport#addService()
     */
    public Result testGetSourceAsBeanContextServices() throws Exception {

        context = new BeanContextServicesSupport();

        serviceBean = new ServiceBean();
        
        // Adding the component
        context.add(serviceBean);

        // Listen for new services
        context.addBeanContextServicesListener(serviceBean);

        TestServiceProvider provider = new TestServiceProvider();

        // Add service to the context
        context.addService(TestCounter.class, provider);
        
        if ((context.hasService(TestCounter.class))
                || (serviceBean.bcsae.getSourceAsBeanContextServices() instanceof BeanContextServicesSupport))

            return passed();
        else
            return failed("method getSourceAsBeanContextServices");

    }

     /**
     * @see java.beans.beancontext.BeanContextServiceAvailableEvent#getCurrentServiceSelectors()
     * @see java.beans.beancontext.BeanContextServicesSupport#hasService()
     */
    public Result testGetCurrentServiceSelectors() throws Exception {
        
        context = new BeanContextServicesSupport();

        serviceBean = new ServiceBean();

        // Add service to the context
        context.add(serviceBean);        
        
        // Listen for new services
        context.addBeanContextServicesListener(serviceBean);

        TestServiceProvider provider = new TestServiceProvider();

        // Add service to the context
        context.addService(TestCounter.class, provider);

        if ((context.hasService(TestCounter.class))
                || (serviceBean.bcsae.getCurrentServiceSelectors() instanceof java.util.Iterator))
            return passed();
        else
            return failed("method getCurrentServiceSelectors");
    }

    public static void main(String[] args) {
        try {
            System.exit(new TestBeanContextServiceAvailableEvent().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}