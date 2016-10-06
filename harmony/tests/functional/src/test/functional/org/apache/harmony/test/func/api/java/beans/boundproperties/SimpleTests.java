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

import org.apache.harmony.test.func.api.java.beans.boundproperties.auxiliary.Variables;
import org.apache.harmony.share.Result;

/**
 * Under test: PropertyChangeSupport, PropertyChangeEvent.
 * <p>
 * Purpose: verify that firePropertyChange(selectedProperyName, oldValue,
 * newValue) method of PropertyChangeSupport reports update for selected
 * property to any registered listeners. Simple tests were developed in this
 * class.
 * 
 * @see java.beans.PropertyChangeSupport
 * @see java.beans.PropertyChangeEvent
 */
public class SimpleTests extends Variables {
    /**
     * Verify that listener registered for selected property and for all
     * properties is notified, when selected property is changed.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Add a listener for all properties.
     * <li>Add 2 times the listener for selected property.</li>
     * <li>Call firePropertyChange method for selected property.</li>
     * <li>Verify that listener were notified 3 times.</li>
     * </ul>
     */
    public Result testAddListenerForSelectedPropertyAndForAllProperties() {
        try {
            certainProperties[0].addListener(1);
            certainProperties[0].addListener(1);
            allProperties.addListener(1);
            certainProperties[0].fireAndVerify("old value", "new value");
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * Add listener for N different selected properties and verify that listener
     * is notified when each selected property is changed.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Add listener for a selected property.</li>
     * <li>Call firePropertyChange method for the selected property.</li>
     * <li>Verify, that only one listener registered for the selected property
     * is notified.</li>
     * <li>Repeat N times items 1,2,3 for different selected property.</li>
     * </ul>
     */
    public Result testAddListenerForNSelectedProperties() {
        try {
            for (int i = 0; i < certainProperties.length; i++) {
                certainProperties[i].addListener(0);
                certainProperties[i].fireAndVerify("old value", "new value");
            }
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>1. This test adds two listeners for selected property.</li>
     * <li>2. Send signal about property change</li>
     * <li>3. Verify, that only listeners registered for particular property
     * are notified.</li>
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result testForSelectedProperty() {
        try {
            certainProperties[0].addListener(1);
            certainProperties[0].addListener(1);
            certainProperties[0].fireAndVerify("old value", "new value");
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>1. This test adds listener for all properties.</li>
     * <li>2. Send signal about property change.</li>
     * <li>3. Verify, that only listener registered for all properties is
     * notified.</li>
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result testForAllProperties() {
        try {
            allProperties.addListener(1);
            certainProperties[0].fireAndVerify("old value", "new value");
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>1. This test adds the second listener for all selected properties.
     * </li>
     * <li>2. Send signal about property change.</li>
     * <li>3. Verify, that only listener registered for all selected properties
     * is notified.</li>
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result testAddListener1ToAllSelectedProperties() {
        try {
            for (int i = 0; i < certainProperties.length; i++) {
                certainProperties[i].addListener(1);
                certainProperties[i].fireAndVerify("old value", "new value");
            }
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * Verify, that listeners registered for all properties are notified, when
     * selected property is changed.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Add 5 times listener for all properties.</li>
     * <li>Call firePropertyChange method for selected property.</li>
     * <li>Verify that listener were notified 5 times.</li>
     * </ul>
     */
    public Result testAddListenersForAllProperties() {
        try {
            for (int i = 0; i < 5; i++) {
                allProperties.addListener(0);
            }
            certainProperties[2].fireAndVerify("old value", "new value");
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>1. This test adds the third listener for all selected properties.
     * </li>
     * <li>2. Send signal about property change.</li>
     * <li>3. Verify, that only listener registered for all selected properties
     * is notified.</li>
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result testAddListener2ToAllSelectedProperties() {
        try {
            for (int i = 0; i < certainProperties.length; i++) {
                certainProperties[i].addListener(2);
                certainProperties[i].fireAndVerify("old value", "new value");
            }
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>1. This test removes the first listener for all selected properties.
     * </li>
     * <li>2. Send signal about property change.</li>
     * <li>3. Verify, that only listener registered for all selected properties
     * is notified.</li>
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result testRemoveListener0FromAllSelectedProperties() {
        try {
            for (int i = 0; i < certainProperties.length; i++) {
                certainProperties[i].removeListener(0);
                certainProperties[i].fireAndVerify("old value", "new value");
            }
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>1. This test removes the second listener for all selected
     * properties.</li>
     * <li>2. Send signal about property change.</li>
     * <li>3. Verify, that only listener registered for all selected properties
     * is notified.</li>
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result testRemoveListener1FromAllSelectedProperties() {
        try {
            for (int i = 0; i < certainProperties.length; i++) {
                certainProperties[i].removeListener(1);
                certainProperties[i].fireAndVerify("old value", "new value");
            }
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>1. This test adds 6 listeners for selected property.</li>
     * <li>2. Send signal about property change.</li>
     * <li>3. Verify, that only listeners registered for selected property is
     * notified.</li>
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result testAdd6Listeners0ToProperty0() {
        try {
            for (int i = 0; i < 6; i++) {
                certainProperties[0].addListener(0);
            }
            certainProperties[0].fireAndVerify("old value", "new value");
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>1. This test removes 6 listeners for selected property.</li>
     * <li>2. Send signal about property change.</li>
     * <li>3. Verify, that only listeners registered for selected property is
     * notified.</li>
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result testRemove6Listeners0fromProperty0() {
        try {
            for (int i = 0; i < 6; i++) {
                certainProperties[0].removeListener(0);
            }
            certainProperties[0].fireAndVerify("old value", "new value");
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>1. This test adds 5 listeners for selected property.</li>
     * <li>2. Send signal about property change.</li>
     * <li>3. Verify, that only listeners registered for selected property is
     * notified.</li>
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result testAdd5Listeners0ToProperty1() {
        try {
            for (int i = 0; i < 5; i++) {
                certainProperties[1].addListener(0);
            }
            certainProperties[1].fireAndVerify("old value", "new value");
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>1. This test removes 5 listeners for selected property.</li>
     * <li>2. Send signal about property change.</li>
     * <li>3. Verify, that only listeners registered for selected property is
     * notified.</li>
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result testRemove5Listeners0fromProperty1() {
        try {
            for (int i = 0; i < 5; i++) {
                certainProperties[1].removeListener(0);
            }
            certainProperties[1].fireAndVerify("old value", "new value");
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * Verify, that listeners registered for selected property are notified,
     * when selected property is changed.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Add 5 times listener for selected property.</li>
     * <li>Call firePropertyChange method for selected property.</li>
     * <li>Verify that listener were notified 5 times.</li>
     * </ul>
     */
    public Result testAddListenersForSelectedProperty() {
        try {
            for (int i = 0; i < 5; i++) {
                certainProperties[1].addListener(1);
            }
            certainProperties[1].fireAndVerify("old value", "new value");
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * Verify, that no exception is thrown when unregistered listener is removed
     * for all properties.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Remove 5 times listener for selected property.</li>
     * <li>Call firePropertyChange method for selected property.</li>
     * <li>Verify, that no exception (NullPointerException,
     * ArrayOfBoundsException, etc) wasn't thrown.</li>
     * </ul>
     */
    public Result testRemoveListenersForSelectedProperty() {
        try {
            for (int i = 0; i < 5; i++) {
                certainProperties[1].removeListener(1);
            }
            certainProperties[1].fireAndVerify("old value", "new value");
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>1. This test adds 4 listeners for all properties.</li>
     * <li>2. Call firePropertyChange method</li>
     * <li>3. Verify that only listeners registered for all property is
     * notified.</li>
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result testAdd4Listeners1ToAllProperties() {
        try {
            for (int i = 0; i < 4; i++) {
                allProperties.addListener(1);
            }
            certainProperties[2].fireAndVerify("old value", "new value");
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>1. This test removes 5 listeners for all properties.</li>
     * <li>2. Call firePropertyChange method..</li>
     * <li>3. Verify, that only listeners registered for all property is
     * notified.</li>
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result testRemove5Listeners0FromAllProperties() {
        try {
            for (int i = 0; i < 5; i++) {
                allProperties.removeListener(0);
            }
            certainProperties[2].fireAndVerify("old value", "new value");
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    /**
     * Verify, that no exception is thrown when unregistered listener is removed
     * for selected properties.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Remove 4 times listener for all properties.</li>
     * <li>Call firePropertyChange method for selected property.</li>
     * <li>Verify, that no exception (NullPointerException,
     * ArrayOfBoundsException, etc) wasn't thrown.</li>
     * </ul>
     */
    public Result testRemoveListenersForAllProperties() {
        try {
            for (int i = 0; i < 4; i++) {
                allProperties.removeListener(1);
            }
            certainProperties[2].fireAndVerify("old value", "new value");
            return passed();
        } catch (Exception e) {

            e.printStackTrace();
            return failed(e.toString());
        }
    }

    public static void main(String[] args) {
        System.exit(new SimpleTests().test(args));
    }

    public SimpleTests() {
        startup(null);
    }

    public SimpleTests(Variables params) {
        startup(params);
    }

}