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

import java.applet.Applet;
import java.beans.AppletInitializer;
import java.beans.beancontext.BeanContext;

/**
 * Simple implementation for java.beans.AppletInitializer interface.
 * 
 */
public class SimpleAppletInitializer implements AppletInitializer {
    static Exception exception;
    private boolean  IsInitializeInvoked = false;

    public void activate(Applet applet1) {
        if (!(applet1 instanceof SimpleApplet)) {
            exception = new Exception("applet1 is not SimpleApplet");
        }
        if (IsInitializeInvoked == false) {
            exception = new Exception("initialize method wasn't invoked");
        }
    }

    public void initialize(Applet applet2, BeanContext beanContext) {
        IsInitializeInvoked = true;
        if (!(applet2 instanceof SimpleApplet)) {
            exception = new Exception("applet2 is not SimpleApplet");
        }
        if (!(beanContext instanceof SimpleBeanContext)) {
            exception = new Exception("beanContext is not SimpleBeanContext");
        }

    }

    public static void verifyException() throws Exception {
        if (exception != null)
            throw exception;
    }
}