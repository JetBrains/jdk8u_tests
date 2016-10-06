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
package org.apache.harmony.test.func.api.java.beans.constrainedproperties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListenerProxy;
import java.beans.VetoableChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.harmony.test.func.api.java.beans.constrainedproperties.auxiliary.AuxiliaryVetoableChangeListener0;
import org.apache.harmony.test.func.api.java.beans.constrainedproperties.auxiliary.AuxiliaryVetoableChangeListener1;
import org.apache.harmony.test.func.api.java.beans.constrainedproperties.auxiliary.AuxiliaryVetoableChangeListener2;
import org.apache.harmony.test.func.api.java.beans.constrainedproperties.auxiliary.SimpleListener;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Under test: VetoableChangeSupport,VetoableChangeListenerProxy,
 * PropertyChangeEvent.
 * <p>
 * Purpose: test fireVetoableChange(PropertyChangeEvent evt),
 * fireVetoableChange(String propertyName, boolean oldValue, boolean newValue),
 * fireVetoableChange(String propertyName, int oldValue, int newValue),
 * getVetoableChangeListeners(), getVetoableChangeListeners(String
 * propertyName), hasListeners(String propertyName) methods from
 * VetoableChangeSupport class.
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
     * Verify that getVetoableChangeListeners() method returns listener
     * registered for all properties.
     * <p>
     * Step-by-step encoding: register a listener for all properties. Verify
     * that getVetoableChangeListeners() method returns the listener.
     */
    public Result testGetVetoableChangeListeners01() {
        VetoableChangeSupport propertyChangeSupport = new VetoableChangeSupport(
            "bean1");
        propertyChangeSupport.addVetoableChangeListener(new SimpleListener());
        assertEquals(propertyChangeSupport.getVetoableChangeListeners()[0]
            .getClass(), SimpleListener.class);
        return passed();
    }

    /**
     * Verify that getVetoableChangeListeners(String selectedPropertyName)
     * method returns listener registered for selected property.
     * <p>
     * Step-by-step encoding: register a listener for selected properties.
     * Verify that getVetoableChangeListeners(String selectedPropertyName)
     * method returns the listener.
     */
    public Result testGetVetoableChangeListeners02() {
        VetoableChangeSupport propertyChangeSupport = new VetoableChangeSupport(
            "bean1");
        propertyChangeSupport.addVetoableChangeListener("property1",
            new SimpleListener());
        assertEquals(propertyChangeSupport
            .getVetoableChangeListeners("property1")[0].getClass(),
            SimpleListener.class);
        return passed();
    }

    /**
     * Verify that by default getVetoableChangeListeners() method returns empty
     * array.
     */
    public Result testGetVetoableChangeListeners03() {
        VetoableChangeSupport propertyChangeSupport = new VetoableChangeSupport(
            "bean1");
        assertEquals(propertyChangeSupport.getVetoableChangeListeners().length,
            0);
        return passed();
    }

    /**
     * Verify that by default getVetoableChangeListeners(String propertyName)
     * method returns empty array.
     */
    public Result testGetVetoableChangeListeners04() {
        VetoableChangeSupport propertyChangeSupport = new VetoableChangeSupport(
            "bean1");
        assertEquals(propertyChangeSupport
            .getVetoableChangeListeners("property1").length, 0);
        return passed();
    }

    /**
     * Verify that getVetoableChangeListeners() method returns
     * VetoableChangeListenerProxy object registered for all properties.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Create VetoableChangeListener object.
     * <li>Create VetoableChangeListenerProxy object which binds the
     * VetoableChangeListener to a specific property.
     * <li>Add VetoableChangeListenerProxy object for all properties.
     * <li>Verify that getVetoableChangeListeners() method returns
     * VetoableChangeListenerProxy object.
     */
    /*
     * public Result testGetVetoableChangeListeners05() { VetoableChangeSupport
     * propertyChangeSupport = new VetoableChangeSupport( "bean1");
     * propertyChangeSupport.addVetoableChangeListener(new SimpleProxy(
     * "property1", new SimpleListener()));
     * assertEquals(propertyChangeSupport.getVetoableChangeListeners()[0]
     * .getClass(), SimpleListener.class); return passed(); }
     */

    /**
     * Verify that if a listener have been registered for selected property,
     * then getVetoableChangeListeners() method returns
     * VetoableChangeListenerProxy, which is a cover for the registered
     * listener.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Register a listener for selected properties.
     * <li>Invoke getVetoableChangeListeners() method.
     * <li>Verify that returned value is VetoableChangeListenerProxy object.
     * <li>Verify that getListener() of VetoableChangeListenerProxy object
     * returns registered listener.
     * <li>Verify that getPropertyName() of VetoableChangeListenerProxy object
     * returns a name of selected property.
     */
    public Result testGetVetoableChangeListeners06() {
        VetoableChangeSupport propertyChangeSupport = new VetoableChangeSupport(
            "bean1");
        propertyChangeSupport.addVetoableChangeListener("property1",
            new SimpleListener());
        VetoableChangeListenerProxy propertyChangeListener2 = (VetoableChangeListenerProxy)propertyChangeSupport
            .getVetoableChangeListeners()[0];
        assertEquals(propertyChangeListener2.getListener().getClass(),
            SimpleListener.class);
        assertEquals(propertyChangeListener2.getPropertyName(), "property1");
        return passed();
    }

    /**
     * Verify that no event is fired if old and new values are equal.
     * <p>
     * Step-by-step encoding: register a listener for selected properties. Call
     * fireVetoableChange method for selected property with the same old and new
     * values. Verify that no event is fired.
     * 
     * @throws PropertyVetoException
     */
    public Result testFireVetoableChange01() throws PropertyVetoException {
        VetoableChangeSupport propertyChangeSupport = new VetoableChangeSupport(
            "bean1");
        SimpleListener simpleListener = new SimpleListener();
        propertyChangeSupport.addVetoableChangeListener("property1",
            simpleListener);
        propertyChangeSupport.fireVetoableChange("property1", "value", "value");
        assertFalse(simpleListener.isInvoked());
        return passed();
    }

    /**
     * Verify that listener is notified when
     * fireVetoableChange(PropertyChangeEvent evt) method is invoked.
     * <p>
     * Step-by-step encoding: create PropertyChangeEvent, register a listener
     * for selected properties, invoked fireVetoableChange method for selected
     * property. Verify that listener is notified.
     * 
     * @throws PropertyVetoException
     */
    public Result testFireVetoableChange02() throws PropertyVetoException {
        VetoableChangeSupport propertyChangeSupport = new VetoableChangeSupport(
            "bean1");
        SimpleListener simpleListener = new SimpleListener();
        propertyChangeSupport.addVetoableChangeListener(simpleListener);
        propertyChangeSupport.fireVetoableChange(new PropertyChangeEvent(
            "bean1", "property1", "old value", "new value"));
        assertTrue(simpleListener.isInvoked());
        return passed();
    }

    /**
     * Verify that listener is notified when fireVetoableChange(String
     * propertyName, boolean oldValue, boolean newValue) method is invoked.
     * <p>
     * Step-by-step encoding: register a listener for selected properties,
     * invoked fireVetoableChange method for selected property. Verify that
     * listener is notified.
     * 
     * @throws PropertyVetoException
     */
    public Result testFireVetoableChange03() throws PropertyVetoException {
        VetoableChangeSupport propertyChangeSupport = new VetoableChangeSupport(
            "bean1");
        SimpleListener simpleListener = new SimpleListener();
        propertyChangeSupport.addVetoableChangeListener(simpleListener);
        propertyChangeSupport.fireVetoableChange("property1", false, true);
        assertTrue(simpleListener.isInvoked());
        return passed();

    }

    /**
     * Verify that listener is notified when fireVetoableChange(String
     * propertyName, int oldValue, int newValue) method is invoked.
     * <p>
     * Step-by-step encoding: register a listener for selected properties,
     * invoked fireVetoableChange method for selected property. Verify that
     * listener is notified.
     * 
     * @throws PropertyVetoException
     */
    public Result testFireVetoableChange04() throws PropertyVetoException {
        VetoableChangeSupport propertyChangeSupport = new VetoableChangeSupport(
            "bean1");
        SimpleListener simpleListener = new SimpleListener();
        propertyChangeSupport.addVetoableChangeListener(simpleListener);
        propertyChangeSupport.fireVetoableChange("property1", 0, 1);
        assertTrue(simpleListener.isInvoked());
        return passed();

    }

    /*
     * Verify that listener, which uses in constructor of
     * VetoableChangeListenerProxy object is notified when fireVetoableChange
     * method is invoked. <p>
     * Step-by-step encoding: <ul><li> Create VetoableChangeListener object.
     * <li> Create VetoableChangeListenerProxy object which binds the
     * VetoableChangeListener to a specific property. <li> Add
     * VetoableChangeListenerProxy object for all properties. <li> Verify that
     * listener, which uses in constructor of VetoableChangeListenerProxy is
     * notified when fireVetoableChange method is invoked.
     */
    /*
     * public Result testFireVetoableChange05() { VetoableChangeSupport
     * propertyChangeSupport = new VetoableChangeSupport( "bean1");
     * SimpleListener simpleListener = new SimpleListener(); SimpleProxy
     * simpleProxy = new SimpleProxy("property1", simpleListener);
     * propertyChangeSupport.addVetoableChangeListener(simpleProxy);
     * propertyChangeSupport.fireVetoableChange("property2", 0, 1);
     * System.out.println("Is inner listener invoked=" +
     * simpleListener.isInvoked()); System.out.println("Is proxy listener
     * invoked=" + simpleProxy.isInvoked());
     * //assertTrue(simpleListener.isInvoked()); return passed(); }
     */
    /**
     * Verify that hasListeners method returns true if there are any listeners
     * for a specific property.
     * <p>
     * Step-by-step encoding: register a listener for selected property. Verify
     * that hasListeners(String selectedPropertyName) method returns the
     * listener.
     */
    public Result testHasListeners01() {
        VetoableChangeSupport propertyChangeSupport = new VetoableChangeSupport(
            "bean1");
        propertyChangeSupport.addVetoableChangeListener("property1",
            new SimpleListener());
        assertTrue(propertyChangeSupport.hasListeners("property1"));
        return passed();

    }

    /**
     * Verify that by default hasListeners(String propertyName) method returns
     * false.
     */
    public Result testHasListeners02() {
        VetoableChangeSupport propertyChangeSupport = new VetoableChangeSupport(
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
     * Verify that when one serializes VetoableChangeSupport class, all
     * serializable listeners are serialized and non-serializable listeners
     * aren't serialize.
     * <p>
     * Step-by-step encoding:
     * <ul>
     * <li>Add serializable listener#0 to all properties.
     * <li>Add non-serializable listener#1 to certain property#1.
     * <li>Add serializable listener#2 to certain property#2.
     * <li>Verify that getVetoableChangeListeners() method returns massive
     * consisting of two listeners. Verify that first listener is listener#0.
     * <li>Verify that getVetoableChangeListeners(property#1) returns empty
     * massive.
     * <li>Verify that getVetoableChangeListeners(property#2) returns
     * listener#2.
     */
    public Result testSerialisation() throws Exception {
        VetoableChangeSupport propertyChangeSupport = new VetoableChangeSupport(
            "bean1");
        propertyChangeSupport
            .addVetoableChangeListener(new AuxiliaryVetoableChangeListener0());
        propertyChangeSupport.addVetoableChangeListener("prop1",
            new AuxiliaryVetoableChangeListener1());
        propertyChangeSupport.addVetoableChangeListener("prop2",
            new AuxiliaryVetoableChangeListener2());
        VetoableChangeSupport propertyChangeSupport2 = serialisationDesirialisation(propertyChangeSupport);
        assertEquals(
            propertyChangeSupport2.getVetoableChangeListeners().length, 2);
        assertEquals(
            propertyChangeSupport2.getVetoableChangeListeners("prop1").length,
            0);
        assertEquals(
            propertyChangeSupport2.getVetoableChangeListeners("prop2").length,
            1);
        assertEquals(
            propertyChangeSupport2.getVetoableChangeListeners("prop2")[0]
                .getClass(), AuxiliaryVetoableChangeListener2.class);
        assertEquals(propertyChangeSupport2.getVetoableChangeListeners()[0]
            .getClass(), AuxiliaryVetoableChangeListener0.class);
        return passed();
    }

    /**
     * Verify that NullPointerException isn't threw, if add listener to "null"
     * property.
     */
    public Result testNullProperty() {
            new VetoableChangeSupport("bean1").addVetoableChangeListener(null,
                new AuxiliaryVetoableChangeListener0());
            return passed();
    }

    /**
     * Serialize and deserialize object.
     * 
     * @param object
     * @return
     * @throws Exception
     */
    private VetoableChangeSupport serialisationDesirialisation(
        VetoableChangeSupport object) throws Exception {
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(outBytes);
        objectOut.writeObject(object);
        objectOut.close();
        ObjectInputStream objectInputStream = new ObjectInputStream(
            new ByteArrayInputStream(outBytes.toByteArray()));
        return (VetoableChangeSupport)objectInputStream.readObject();
    }
}