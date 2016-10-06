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
package org.apache.harmony.test.func.api.java.beans.introspector.location.beans.thesamepackage;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import org.apache.harmony.test.func.api.java.beans.introspector.location.BeanInfoStatus;

/**
 */
public class Bean1BeanInfo extends SimpleBeanInfo implements BeanInfoStatus {
    private static boolean                invoked = false;
    private static IntrospectionException exception;

    /**
     * @see java.beans.BeanInfo#getPropertyDescriptors()
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor _property1 = new PropertyDescriptor("property1",
                Bean1.class, "ggetI", "ssetI");
            PropertyDescriptor[] pds = new PropertyDescriptor[] { _property1 };
            invoked = true;
            return pds;
        } catch (IntrospectionException exception) {
            this.exception = exception;
            return null;
        }
    }

    /**
     * Throw exception if there is a exception in invocating
     * getPropertyDescriptors() method.
     */
    private void getException() throws IntrospectionException {
        if (exception != null) {
            IntrospectionException tmp = exception;
            exception = null;
            throw tmp;
        }
    }

    public void setNotInvoked() {
        invoked = false;
    }

    /**
     * @see BeanInfoStatus#getIsInvokedAndChangeToNotInvoked()
     */
    public boolean getIsInvokedAndChangeToNotInvoked()
        throws IntrospectionException {
        boolean tmp = invoked;
        invoked = false;
        getException();
        return tmp;
    }
}