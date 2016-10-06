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
/*
 *
 */  
package org.apache.harmony.test.func.api.javax.naming.event;

import java.util.EventObject;

import javax.naming.event.NamespaceChangeListener;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.ObjectChangeListener;

/**
 * Simple JNDI event listener.
 * 
 */
public class NamingEventListenerSample implements NamespaceChangeListener,
    ObjectChangeListener {

    /**
     *  Event object.
     */
    private EventObject e;

    /**
     * Returns EventObject e.
     * 
     * @return EventObject e.
     */
    public EventObject getEventObject() {
        return e;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.event.NamespaceChangeListener#objectAdded(javax.naming.event.NamingEvent)
     */
    public void objectAdded(NamingEvent e) {
        this.e = e;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.event.NamespaceChangeListener#objectRemoved(javax.naming.event.NamingEvent)
     */
    public void objectRemoved(NamingEvent e) {
        this.e = e;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.event.NamespaceChangeListener#objectRenamed(javax.naming.event.NamingEvent)
     */
    public void objectRenamed(NamingEvent e) {
        this.e = e;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.event.ObjectChangeListener#objectChanged(javax.naming.event.NamingEvent)
     */
    public void objectChanged(NamingEvent e) {
        this.e = e;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.event.NamingListener#namingExceptionThrown(javax.naming.event.NamingExceptionEvent)
     */
    public void namingExceptionThrown(NamingExceptionEvent e) {
        this.e = e;
    }
}
