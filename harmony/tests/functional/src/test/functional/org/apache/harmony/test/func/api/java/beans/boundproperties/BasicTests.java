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
package org.apache.harmony.test.func.api.java.beans.boundproperties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListenerProxy;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.harmony.test.func.api.java.beans.boundproperties.auxiliary.AuxiliaryPropertyChangeListener0;
import org.apache.harmony.test.func.api.java.beans.boundproperties.auxiliary.AuxiliaryPropertyChangeListener1;
import org.apache.harmony.test.func.api.java.beans.boundproperties.auxiliary.AuxiliaryPropertyChangeListener2;
import org.apache.harmony.test.func.api.java.beans.boundproperties.auxiliary.SimpleListener;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: PropertyChangeSupport,PropertyChangeListenerProxy,
 * PropertyChangeEvent.
 * <p>
 * Purpose: test firePropertyChange(PropertyChangeEvent evt),
 * firePropertyChange(String propertyName, boolean oldValue, boolean newValue),
 * firePropertyChange(String propertyName, int oldValue, int newValue),
 * getPropertyChangeListeners(), getPropertyChangeListeners(String
 * propertyName), hasListeners(String propertyName) methods from
 * PropertyChangeSupport class.
 * 
 */
public class BasicTests extends MultiCase {
    public static void main(String[] args) {
        try {
            System.exit(new BasicTests().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verify that getPropertyChangeListeners() method returns listener
     * registered for all properties.
     * <p>
     * Step-by-step encoding: register a listener for all properties. Verify
     * that getPropertyChangeListeners() method returns the listener.
     */
    public Result testGetPropertyChangeListeners01() {
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            "bean1");
        propertyChangeSupport.addPropertyChangeListener(new SimpleListener());
        assertEquals(propertyChangeSupport.getPropertyChangeListeners()[0]
            .getClass(), SimpleListener.class);
        return passed();
    }

    /**
     * Verify that getPropertyChangeListeners(String selectedPropertyName)
     * method returns listener registered for selected property.
     * <p>
     * Step-by-step encoding: register a listener for selected properties.
     * Verify that getPropertyChangeListeners(String selectedPropertyName)
     * method returns the listener.
     */
    public Result testGetPropertyChangeListeners02() {
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            "bean1");
        propertyChangeSupport.addPropertyChangeListener("property1",
            new SimpleListener());
        assertEquals(propertyChangeSupport
            .getPropertyChangeListeners("property1")[0].getClass(),
            SimpleListener.class);
        return passed();
    }

    /**
     * Verify that by default getPropertyChangeListeners() method returns empty
     * array.
     */
    public Result testGetPropertyChangeListeners03() {
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            "bean1");
        assertEquals(propertyChangeSupport.getPropertyChangeListeners().length,
            0);
        return passed();
    }

    /**
     * Verify that by default getPropertyChangeListeners(String propertyName)
     * method returns empty array.
     */
    public Result testGetPropertyChangeListeners04() {
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            "bean1");
        assertEquals(propertyChangeSupport
            .getPropertyChangeListeners("property1").length, 0);
        return passed();
    }

    /**
     * Verify that if a listener have been registered for selected property,
     * then getPropertyChangeListeners() method returns
     * PropertyChangeListenerProxy, which is a wrapper for the registered
     * listener.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Register a listener for selected properties.
     * <li>Invoke getPropertyChangeListeners() method.
     * <li>Verify that returned value is PropertyChangeListenerProxy object.
     * <li>Verify that getListener() of PropertyChangeListenerProxy object
     * returns registered listener.
     * <li>Verify that getPropertyName() of PropertyChangeListenerProxy object
     * returns a name of selected property.
     */
    public Result testGetPropertyChangeListeners05() {
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            "bean1");
        propertyChangeSupport.addPropertyChangeListener("property1",
            new SimpleListener());
        PropertyChangeListenerProxy propertyChangeListener2 = (PropertyChangeListenerProxy)propertyChangeSupport
            .getPropertyChangeListeners()[0];
        assertEquals(propertyChangeListener2.getListener().getClass(),
            SimpleListener.class);
        assertEquals(propertyChangeListener2.getPropertyName(), "property1");
        return passed();
    }

    /**
     * Verify that no event is fired if old and new values are equal.
     * <p>
     * Step-by-step encoding: register a listener for selected properties. Call
     * firePropertyChange method for selected property with the same old and new
     * values. Verify that no event is fired.
     */
    public Result testFirePropertyChange01() {
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            "bean1");
        SimpleListener simpleListener = new SimpleListener();
        propertyChangeSupport.addPropertyChangeListener("property1",
            simpleListener);
        propertyChangeSupport.firePropertyChange("property1", "value", "value");
        assertFalse(simpleListener.isInvoked());
        return passed();
    }

    /**
     * Verify that listener is notified when
     * firePropertyChange(PropertyChangeEvent evt) method is invoked.
     * <p>
     * Step-by-step encoding: create PropertyChangeEvent, register a listener
     * for selected properties, invoked firePropertyChange method for selected
     * property. Verify that listener is notified.
     */
    public Result testFirePropertyChange02() {
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            "bean1");
        SimpleListener simpleListener = new SimpleListener();
        propertyChangeSupport.addPropertyChangeListener(simpleListener);
        propertyChangeSupport.firePropertyChange(new PropertyChangeEvent(
            "bean1", "property1", "old value", "new value"));
        assertTrue(simpleListener.isInvoked());
        return passed();
    }

    /**
     * Verify that listener is notified when firePropertyChange(String
     * propertyName, boolean oldValue, boolean newValue) method is invoked.
     * <p>
     * Step-by-step encoding: register a listener for selected properties,
     * invoked firePropertyChange method for selected property. Verify that
     * listener is notified.
     */
    public Result testFirePropertyChange03() {
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            "bean1");
        SimpleListener simpleListener = new SimpleListener();
        propertyChangeSupport.addPropertyChangeListener(simpleListener);
        propertyChangeSupport.firePropertyChange("property1", false, true);
        assertTrue(simpleListener.isInvoked());
        return passed();

    }

    /**
     * Verify that listener is notified when firePropertyChange(String
     * propertyName, int oldValue, int newValue) method is invoked.
     * <p>
     * Step-by-step encoding: register a listener for selected properties,
     * invoked firePropertyChange method for selected property. Verify that
     * listener is notified.
     */
    public Result testFirePropertyChange04() {
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            "bean1");
        SimpleListener simpleListener = new SimpleListener();
        propertyChangeSupport.addPropertyChangeListener(simpleListener);
        propertyChangeSupport.firePropertyChange("property1", 0, 1);
        assertTrue(simpleListener.isInvoked());
        return passed();

    }

    /**
     * Verify that hasListeners method returns true if there are any listeners
     * for a specific property.
     * <p>
     * Step-by-step encoding: register a listener for selected property. Verify
     * that hasListeners(String selectedPropertyName) method returns the
     * listener.
     */
    public Result testHasListeners01() {
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            "bean1");
        propertyChangeSupport.addPropertyChangeListener("property1",
            new SimpleListener());
        assertTrue(propertyChangeSupport.hasListeners("property1"));
        return passed();

    }

    /**
     * Verify that by default hasListeners(String propertyName) method returns
     * false.
     */
    public Result testHasListeners02() {
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            "bean1");
        assertFalse(propertyChangeSupport.hasListeners("property1"));
        return passed();
    }

    /**
     * Verify that getPropagationId() of PropertyChangeEvent class returns
     * propagationId.
     * <p>
     * Step-by-step encoding: set propagationId. Verify that getPropagationId()
     * method returns PropagationId.
     */
    public Result testPropagationId() {
        PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(
            "bean1", "property1", "old value", "new value");
        propertyChangeEvent.setPropagationId("PropagationId");
        assertEquals(propertyChangeEvent.getPropagationId(), "PropagationId");
        return passed();
    }

    /**
     * Verify that when one serializes PropertyChangeSupport class, all
     * serializable listeners are serialized and non-serializable listeners
     * aren't serialize.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Add serializable listener#0 to all properties.
     * <li>Add non-serializable listener#1 to certain property#1.
     * <li>Add serializable listener#2 to certain property#2.
     * <li>Verify that getPropertyChangeListeners() method returns massive
     * consisting of two listeners. Verify that first listener is listener#0.
     * <li>Verify that getPropertyChangeListeners(property#1) returns empty
     * massive.
     * <li>Verify that getPropertyChangeListeners(property#2) returns
     * listener#2.
     */
    public Result testSerialisation() throws Exception {
        PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            "bean1");
        propertyChangeSupport
            .addPropertyChangeListener(new AuxiliaryPropertyChangeListener0());
        propertyChangeSupport.addPropertyChangeListener("prop1",
            new AuxiliaryPropertyChangeListener1());
        propertyChangeSupport.addPropertyChangeListener("prop2",
            new AuxiliaryPropertyChangeListener2());
        PropertyChangeSupport propertyChangeSupport2 = serialisationDesirialisation(propertyChangeSupport);
        assertEquals(
            propertyChangeSupport2.getPropertyChangeListeners().length, 2);
        assertEquals(
            propertyChangeSupport2.getPropertyChangeListeners("prop1").length,
            0);
        assertEquals(
            propertyChangeSupport2.getPropertyChangeListeners("prop2").length,
            1);
        assertEquals(
            propertyChangeSupport2.getPropertyChangeListeners("prop2")[0]
                .getClass(), AuxiliaryPropertyChangeListener2.class);
        assertEquals(propertyChangeSupport2.getPropertyChangeListeners()[0]
            .getClass(), AuxiliaryPropertyChangeListener0.class);
        return passed();
    }

    /**
     * Verify that NullPointerException isn't threw, if add listener to "null"
     * property.
     */
    public Result testNullProperty() {
            new PropertyChangeSupport("bean1").addPropertyChangeListener(null,
                new AuxiliaryPropertyChangeListener0());
            return passed();
    }

    /**
     * Serialize and deserialize object.
     */
    private PropertyChangeSupport serialisationDesirialisation(
        PropertyChangeSupport object) throws Exception {
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(outBytes);
        objectOut.writeObject(object);
        objectOut.close();
        ObjectInputStream objectInputStream = new ObjectInputStream(
            new ByteArrayInputStream(outBytes.toByteArray()));
        return (PropertyChangeSupport)objectInputStream.readObject();
    }
}