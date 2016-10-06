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
//$Id$
package org.apache.harmony.test.func.api.javax.management.modelmbean;

import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * RequiredModelMBeanTest uses this class. This class is NotificationListener
 * and manage resource simultaneously.
 * 
 */
public class ManageResourceAndNotification implements NotificationListener {

    private String       g;
    private String       h;
    private boolean      isGetGWasInvoked = false;
    private Notification notification     = null;
    final static Object  handback         = "handback object";

    public String getG() {
        isGetGWasInvoked = true;
        // System.out.println("getG was invoked");
        return g;
    }

    public String getH() {
        return h;
    }

    public boolean isWasHandleNotificationInvoked() {
        return getNotification() != null;
    }

    public void setG(String g) {
        // System.out.println("setG was invoked");
        this.g = g;
    }

    public void setH(String h) {
        this.h = h;
    }

    public void handleNotification(Notification notification1, Object arg1) {
        if (!handback.equals(arg1)) {
            throw new RuntimeException(
                "handback is incorrect for handleNotification method");
        }
        notification = notification1;
    }

    public boolean isGetGWasInvoked() {
        boolean tmp = isGetGWasInvoked;
        isGetGWasInvoked = false;
        return tmp;
    }

    public Notification getNotification() {
        Notification tmp = notification;
        notification = null;
        return tmp;
    }
}