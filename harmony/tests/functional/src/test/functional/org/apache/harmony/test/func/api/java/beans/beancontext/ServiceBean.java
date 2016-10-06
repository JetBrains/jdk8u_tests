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

import java.beans.beancontext.BeanContextChildSupport;
import java.beans.beancontext.BeanContextServices;
import java.beans.beancontext.BeanContextServiceAvailableEvent;
import java.beans.beancontext.BeanContextServiceRevokedEvent;

/**
 */
public final class ServiceBean extends BeanContextChildSupport {

    private String str;
    private BeanContextServices context;
    public BeanContextServiceAvailableEvent bcsae;
    public BeanContextServiceRevokedEvent bcsre;
    public TestCounter service;
    public int counterRevokedEvent = 0;
    
    /**
     * @param str - any String, the  (for example use as name of the tested class)
     */
    public ServiceBean(String str) {
        this.str = str;
    }

    public ServiceBean() {
        this.str = "Sservice is available";
    }
    
    /**
     * @param bcsae the BeanContextServiceAvailableEvent
     */
    public void serviceAvailable(BeanContextServiceAvailableEvent bcsae) {
        // Get a reference to the context
        BeanContextServices context = bcsae.getSourceAsBeanContextServices();
        // Use the service, if it's available
        if (context.hasService(TestCounter.class)) {
            // System.out.println("Attempting to use the service...");
            try {
                // Got the service
                service = (TestCounter) context.getService(this,
                        this, TestCounter.class, str, this);
                service.counter();
            } catch (Exception e) {
            }

            this.bcsae = bcsae;
        }
    }

    /**
     * @param bcsre the BeanContextServiceRevokedEvent
     */
    public void serviceRevoked(BeanContextServiceRevokedEvent bcsre) {
        
        System.out.println("Sservice is revoked");
        counterRevokedEvent++;
        this.bcsre = bcsre;
    }
}

