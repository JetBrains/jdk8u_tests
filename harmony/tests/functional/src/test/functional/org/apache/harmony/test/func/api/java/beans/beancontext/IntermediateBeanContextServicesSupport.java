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
import java.beans.beancontext.BeanContextServiceProvider;
import java.beans.beancontext.BeanContextServiceAvailableEvent;

/**
 */
public class IntermediateBeanContextServicesSupport extends
        BeanContextServicesSupport {

    /**
     * Override method 
     * @see java.beans.beancontext.BeanContextServicesSupport#addService()
     */
    public boolean addService(Class serviceClass,
            BeanContextServiceProvider bcsp, boolean fireEvent) {
        
        return super.addService(serviceClass, bcsp, fireEvent);
    }

    /**
     * Override method 
     * @see java.beans.beancontext.BeanContextServicesSupport#fireServiceAdded()
     */
    public void testFireServiceAdded(BeanContextServiceAvailableEvent bcssae) {

        super.fireServiceAdded(bcssae);

    }

}