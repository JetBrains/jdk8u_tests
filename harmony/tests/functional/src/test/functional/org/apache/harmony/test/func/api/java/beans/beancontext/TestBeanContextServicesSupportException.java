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

import java.beans.beancontext.BeanContextServiceProvider;
import java.beans.beancontext.BeanContextServicesSupport;
import java.beans.beancontext.BeanContextChild;
import java.beans.beancontext.BeanContextServiceRevokedListener;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class TestBeanContextServicesSupportException extends MultiCase {

    // The BeanContextServicesSupport object
    BeanContextServicesSupport context;
    
    // The ServiceBean object
    ServiceBean serviceBean;

    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#addBeanContextServicesListener()
     */
    public Result testAddBeanContextServicesListenerException()
            throws Exception {

        context = new BeanContextServicesSupport();
        
        serviceBean = null;

        try {
            // Listen for new service
            context.addBeanContextServicesListener(serviceBean);
        } catch (NullPointerException e) {
            
                    return passed();
        }
        return failed("testAddBeanContextServicesListenerException");
    }
    
    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#addService()
     */
    public Result testAddServiceException()    throws Exception {

        Class serviceClass = null;
        BeanContextServiceProvider bcsp = null;
        IntermediateBeanContextServicesSupport context = new IntermediateBeanContextServicesSupport();

        try {
            context.addService(serviceClass,bcsp,true);
        } catch (NullPointerException e) {
            
            return passed();
        }
        return failed("testAddServiceException");
    }

    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#revokeService()
     */
    public Result testRevokeServiceException()    throws Exception {

        Class serviceClass = null;
        BeanContextServiceProvider bcsp = null;
        BeanContextServicesSupport context = new BeanContextServicesSupport();
        serviceBean = null;

        try {
            context.revokeService(serviceClass,bcsp,true);
        } catch (NullPointerException e) {
            return passed();
        }
        return failed("testRevokeServiceException");
    }
    
    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#getService()
     */
    public Result testGetServiceException()    throws Exception {

        BeanContextChild child = null;
        Object requestor = null;
        Class serviceClass = null;
        Object serviceSelector = null;
        BeanContextServiceRevokedListener bcsrl = null;
        BeanContextServicesSupport context = new BeanContextServicesSupport();
        serviceBean = null;

        try {
            context.getService(child, requestor ,serviceClass,serviceSelector,bcsrl);
        } catch (NullPointerException e) {

            return passed();
        }
        return failed("testGetServiceException");
    }
    
    /**
     * @see java.beans.beancontext.BeanContextServicesSupport#releaseService()
     */
    public Result testReleaseServiceException()    throws Exception {

        BeanContextChild child = null;
        Object requestor = null;
        Object service = null;
        BeanContextServicesSupport context = new BeanContextServicesSupport();
        serviceBean = null;

        try {
            context.releaseService(child, requestor ,service);
        } catch (NullPointerException e) {
        
            return passed();
        }
        return failed("testGetServiceException");
    }
    public static void main(String[] args) {
        try {
            System.exit(new TestBeanContextServicesSupportException()
                    .test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}