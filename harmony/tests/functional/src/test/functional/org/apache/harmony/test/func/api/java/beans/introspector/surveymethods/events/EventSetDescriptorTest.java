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
package org.apache.harmony.test.func.api.java.beans.introspector.surveymethods.events;

import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: Introspector, EventSetDescriptor, MethodDescriptor,
 * FeatureDescriptor.
 * <p>
 * Purpose: verify that Introspector correctly introspects events of bean.
 * <p>
 * Signature of "get listener method" is not describe in specification. It is
 * supposed that signature of "get listener method" is getEventListenerTypes.
 * 
 * @see BeanInfo#getEventSetDescriptors()
 */
public class EventSetDescriptorTest extends MultiCase {
    EventSetDescriptor[] eventSetDescriptor;

    public static void main(String[] args) {
        try {
            System.exit(new EventSetDescriptorTest().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setUp() throws IntrospectionException {
        eventSetDescriptor = Introspector.getBeanInfo(Bean1.class)
            .getEventSetDescriptors();
    }

    /**
     * Verify that getListenerType() returns type of listener.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has standard multicast methods for adding, getting
     * and removing listener.
     * <li>Create listener.
     * <li>Create event for listener.
     * <li>Introspect bean.
     * <li>Verify type of listener using getListenerType() of
     * EventSetDescriptor.
     */
    public Result testTypeOfListener() {
        try {
            if (!eventSetDescriptor[0].getListenerType().equals(
                FredListener.class)) {
                throw new Exception("mistake in getListenerType");
            }
            return passed();
        } catch (Exception e) {
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that getName() returns a name of event.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has standard multicast methods for adding, getting
     * and removing listener.
     * <li>Create listener.
     * <li>Create event for listener.
     * <li>Introspect bean.
     * <li>Verify that getName() returns a name of event.
     */
    public Result testGetName() {
        assertEquals(eventSetDescriptor[0].getName(), "fred");
        return passed();
    }

    /**
     * Verify that getDisplayName() returns a name of event.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has standard multicast methods for adding, getting
     * and removing listener.
     * <li>Create listener.
     * <li>Create event for listener.
     * <li>Introspect bean.
     * <li>Verify that getDisplayName() returns a name of event.
     */
    public Result testGetDisplayName() {
        assertEquals(eventSetDescriptor[0].getDisplayName(), "fred");
        return passed();
    }

    /**
     * Verify that getListenerMethods() returns correct listener methods.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has standard multicast methods for adding, getting
     * and removing listener.
     * <li>Create listener which has two fire methods.
     * <li>Create event for listener.
     * <li>Introspect bean.
     * <li>Verify that getListenerMethods returns two fire methods of listener.
     */
    public Result testListenerMethods() {
        try {
            Method[] methods = eventSetDescriptor[0].getListenerMethods();
            assertEquals(methods.length, 2);
            if (methods[0].getName().equals("fireFredEvent")) {
                assertEquals(methods[1].getName(), "fireFred2Event");
            } else {
                assertEquals(methods[0].getName(), "fireFred2Event");
                assertEquals(methods[1].getName(), "fireFredEvent");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that getListenerMethodDescriptors() returns correct listener
     * method descriptors.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has standard multicast methods for adding, getting
     * and removing listener.
     * <li>Create listener which has two fire methods.
     * <li>Create event for listener.
     * <li>Introspect bean.
     * <li>Verify that getListenerMethodDescriptors returns two fire methods of
     * listener.
     */
    public Result testGetListenerMethodDescriptors() {
        try {
            MethodDescriptor[] methodDescriptors = eventSetDescriptor[0]
                .getListenerMethodDescriptors();
            assertEquals(methodDescriptors.length, 2);
            if (methodDescriptors[0].getName().equals("fireFredEvent")) {
                assertEquals(methodDescriptors[1].getName(), "fireFred2Event");
            } else {
                assertEquals(methodDescriptors[0].getName(), "fireFred2Event");
                assertEquals(methodDescriptors[1].getName(), "fireFredEvent");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that getAddListenerMethod() of EventSetDescriptor returns correct
     * "add listener method".
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has standard multicast methods for adding, getting
     * and removing listener.
     * <li>Create listener.
     * <li>Create event for listener.
     * <li>Introspect bean.
     * <li>Verify that getAddListenerMethod method returns correct "add
     * listener method".
     */
    public Result testAddListenerMethod() {
        try {
            if (!eventSetDescriptor[0].getAddListenerMethod().getName().equals(
                "addFredListener")) {
                throw new Exception("mistake in getAddListenerMethod");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that getGetListenerMethod() of EventSetDescriptor returns correct
     * "get listener method". Signature of "get listener method" is not describe
     * in specification. It is supposed that signature of "get listener method"
     * is get <code>EventListenerType</code>s.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has standard multicast methods for adding, getting
     * and removing listener.
     * <li>Create listener.
     * <li>Create event for listener.
     * <li>Introspect bean.
     * <li>Verify that getAddListenerMethod method returns correct "add
     * listener method".
     */
    public Result testGetListenerMethod() {
        try {
            if (!eventSetDescriptor[0].getGetListenerMethod().getName().equals(
                "getFredListeners")) {
                throw new Exception("mistake in getAddListenerMethod");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that getRemoveListenerMethod() returns correct "remove listener
     * method".
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has standard multicast methods for adding, getting
     * and removing listener.
     * <li>Create listener.
     * <li>Create event for listener.
     * <li>Introspect bean.
     * <li>Verify that getRemoveListenerMethod() method returns correct "remove
     * listener method".
     */
    public Result testRemoveListenerMethod() {
        try {
            if (!eventSetDescriptor[0].getRemoveListenerMethod().getName()
                .equals("removeFredListener")) {
                throw new Exception("mistake in getRemoveListenerMethod");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * If bean is multicast event source, verify it using isUnicast() method.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has standard multicast methods for adding, getting
     * and removing listener.
     * <li>Create listener.
     * <li>Create event for listener.
     * <li>Introspect bean.
     * <li>Verify that isUnicast method returns false.
     */
    public Result testMulticastEventSource() {
        try {
            if (eventSetDescriptor[0].isUnicast()) {
                throw new Exception("mistake in isUnicast");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify that isInDefaultEventSet() method of EventSetDescriptor returns
     * true.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has standard multicast methods for adding, getting
     * and removing listener.
     * <li>Create listener.
     * <li>Create event for listener.
     * <li>Introspect bean.
     * <li>Verify that isInDefaultEventSet() method returns true.
     */
    public Result testIsInDefaultEventSet() {
        try {
            if (!eventSetDescriptor[0].isInDefaultEventSet()) {
                throw new Exception("mistake in isInDefaultEventSet");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * If bean is unicast event source, verify it using isUnicast() method.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has standard unicast methods for adding, getting
     * and removing listener.
     * <li>Create listener.
     * <li>Create event for listener.
     * <li>Introspect bean.
     * <li>Verify that isUnicast method returns true.
     */
    public Result testUnicastEventSource() {
        try {
            if (!Introspector.getBeanInfo(UnicastBean.class)
                .getEventSetDescriptors()[0].isUnicast()) {
                throw new Exception("mistake in isUnicast");
            }
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify quantity of found events.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has standard unicast methods for adding, getting
     * and removing listener.
     * <li>Create listener.
     * <li>Create event for listener.
     * <li>Introspect bean.
     * <li>Verify that quantity of found EventSetDescriptor's is 1.
     */
    public Result testQuantityOfEvents() {
        try {
            assertEquals(eventSetDescriptor.length, 1);
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Verify, that no exception is thrown when bean has only methods for adding
     * and removing listener.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has standard multicast methods for adding and
     * removing listener.
     * <li>Create listener.
     * <li>Create event for listener.
     * <li>Introspect bean.
     * <li>Verify that no exception is thrown .
     */
    public Result testOnlyAddAndRemoveMethod() throws IntrospectionException {

        assertEquals(Introspector.getBeanInfo(UnicastBean.class)
            .getEventSetDescriptors()[0].getName(), "fred");
        return passed();
    }

    /**
     * Verify that Introspector doesn't find an event, which has wrong name of
     * addListenerMethod.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean which has wrong name of addListenerMethod:
     * addFeredListener, Fered instead of Fred.
     * <li>Create listener: FredListener.
     * <li>Create FredEvent for listener.
     * <li>Introspect bean.
     * <li>Verify that Introspector doesn't find event.
     */
    public Result testWrongNameOfAddListenerMethod() throws Exception {
        assertEquals(Introspector.getBeanInfo(WrongBean1.class)
            .getEventSetDescriptors().length, 0);
        return passed();
    }

    /**
     * Verify that Introspector doesn't find an event, which has wrong parameter
     * of addListenerMethod.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create bean, which has wrong parameter of addListenerMethod:
     * FredListenerr.
     * <li>Create listener: FredListener.
     * <li>Create FredEvent for listener.
     * <li>Introspect bean.
     * <li>Verify that Introspector doesn't find event.
     */
    public Result testWrongParameterOfAddListenerMethod() throws Exception {
        assertEquals(Introspector.getBeanInfo(WrongBean2.class)
            .getEventSetDescriptors().length, 0);
        return passed();
    }
}