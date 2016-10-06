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
package org.apache.harmony.test.func.api.java.beans.eventhandler;

import java.beans.EventHandler;
import java.lang.reflect.Method;

/**
 */
class MakeInvocation {
    Target               target = new Target();
    private String       action;
    private String       eventPropertyName;
    private String       listenerMethodName;
    private FredListener fredListener;

    /**
     * @param action is action in EventHandler.create(..) method.
     * @param eventPropertyName is eventPropertyName in EventHandler.create(..)
     *        method.
     * @param listenerMethodName is name of method of listener which will be
     *        implemented. If this parameter is null then all methods of
     *        listener will be implemented.
     */
    public MakeInvocation(String action, String eventPropertyName,
        String listenerMethodName) {
        this.action = action;
        this.eventPropertyName = eventPropertyName;
        this.listenerMethodName = listenerMethodName;
    }

    /**
     * Invoke create(..) method of EventHandler.
     */
    public void create() {
        fredListener = (FredListener)EventHandler.create(FredListener.class,
            target, action, eventPropertyName, listenerMethodName);
    }

    /**
     * Invoke <code>methodName</code> method by type casting if
     * invokeByTypeCasting==true or thru invoke(..) of EventHandler call if
     * invokeByTypeCasting==false.
     * 
     * @param methodName is name of method of listener which will be invoked.
     * @param fredEvent instance of FredEvent.
     * @param invokeByTypeCasting if true do invocation fire method of listener
     *        thru Method.invoke(..), else thru EventHandler.invoke(..)
     * @throws Exception
     * @see EventHandler#invoke(java.lang.Object, java.lang.reflect.Method,
     *      java.lang.Object[])
     */
    public void invoke(String methodName, FredEvent fredEvent,
        boolean invokeByTypeCasting) throws Exception {
        Method method = FredListener.class.getMethod(methodName,
            new Class[] { FredEvent.class });
        if (invokeByTypeCasting) {
            method.invoke(fredListener, new FredEvent[] { fredEvent });
        } else {
            EventHandler eventHandler = new EventHandler(target, action,
                eventPropertyName, listenerMethodName);
            eventHandler.invoke(fredListener, method,
                new FredEvent[] { fredEvent });
        }
    }
}