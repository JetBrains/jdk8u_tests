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

import org.apache.harmony.test.func.api.java.beans.boundproperties.auxiliary.BoundException;
import org.apache.harmony.test.func.api.java.beans.boundproperties.auxiliary.Variables;
import org.apache.harmony.share.Result;

/**
 * Under test: PropertyChangeSupport, PropertyChangeEvent.
 * <p>
 * Purpose: verify that firePropertyChange(selectedProperyName, oldValue,
 * newValue) method of PropertyChangeSupport reports an update for selected
 * property to any registered listeners. Complex tests were developed in this
 * class.
 * 
 * @see java.beans.PropertyChangeSupport
 * @see java.beans.PropertyChangeEvent
 */

public class ComplexTests extends Variables {
    /**
     * Add many listeners for selected property and for all properties. Verify
     * that all registered listeners are notified, when selected property is
     * changed.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Add 4 times listener#1 for all properties.</li>
     * <li>Add listener#1 for N different selected properties.</li>
     * <li>Add listener#2 for N different selected properties.</li>
     * <li>Add 5 times listener#3 for selected property.</li>
     * <li>Add 10 times listener#1 for selected property.</li>
     * <li>After each addition firePropertyChange method is invoked and test
     * verifies that all registered listeners were notified.</li>
     * </ul>
     */
    public Result testAddListenersForSelectedPropertyAndForAllProperties() {
        try {
            // Set instance PropertyChangeSupport, AllProperties and
            // CertainProperty classes for SimpleTests the same as for this
            // class.
            SimpleTests simple = new SimpleTests(this);
            // Methods written in SimpleTests class are reused.
            checkResult(simple.testAdd4Listeners1ToAllProperties());
            checkResult(simple.testAddListener1ToAllSelectedProperties());
            checkResult(simple.testAddListener2ToAllSelectedProperties());
            checkResult(simple.testAdd5Listeners0ToProperty1());
            checkResult(simple.testAddListenersForSelectedProperty());
            checkResult(simple.testAddListenersForSelectedProperty());
            return passed();
        } catch (BoundException e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Add and remove many listeners for selected property, for all properties
     * and for N selected properties. Verify that all registered listeners are
     * notified, when selected property is changed.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Add 4 times listener#1 for all properties.</li>
     * <li>Add listener#1 for N different selected properties.</li>
     * <li>Add listener#2 for N different selected properties</li>
     * <li>Add 5 times listener#0 for selected property.</li>
     * <li>Add 10 times listener#1 for selected property.</li>
     * <li>remove 4 times listener#1 for all properties.</li>
     * <li>remove listener#0 for N different selected properties.</li>
     * <li>remove 4 times listener#1 for all properties.</li>
     * <li>remove 5 times listener#0 for selected property.</li>
     * <li>remove 5 times listener#1 for selected property.</li>
     * <li>remove listener#0 for N different selected properties.</li>
     * <li>remove listener#1 for N different selected properties.</li>
     * <li>After each adding or removing listener firePropertyChange method is
     * invoked and test verifies that all registered listeners was notified.
     * </li>
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result testForSelectedPropertyAndForAllProperties() {
        try {
            checkResult(testAddListenersForSelectedPropertyAndForAllProperties());
            SimpleTests simple = new SimpleTests(this);
            checkResult(simple.testRemoveListenersForAllProperties());
            checkResult(simple.testRemoveListener0FromAllSelectedProperties());
            checkResult(simple.testRemoveListenersForAllProperties());
            checkResult(simple.testRemove5Listeners0fromProperty1());
            checkResult(simple.testRemoveListenersForSelectedProperty());
            checkResult(simple.testRemoveListener0FromAllSelectedProperties());
            checkResult(simple.testRemoveListener1FromAllSelectedProperties());
            return passed();
        } catch (BoundException e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Test for two selected properties without testing remove methods.
     * <p>
     * <ul>
     * <li>Add 5 times listeners#0 for selected property#1.</li>
     * <li>Add 12 times listeners#0 for selected property#0.</li>
     * <li>Add 5 times listener#1 for selected property#1.</li>
     * <li>Add 6 times listener#0 for selected property#0.</li>
     * <li>After each adding or removing listener firePropertyChange method is
     * invoked and test verifies that all registered listeners was notified.
     * </ul>
     */
    /**
     * Disregard this test case.
     */
    public Result test02() {
        try {
            SimpleTests simple = new SimpleTests(this);
            checkResult(simple.testAdd5Listeners0ToProperty1());
            checkResult(simple.testAdd6Listeners0ToProperty0());
            checkResult(simple.testAdd6Listeners0ToProperty0());
            checkResult(simple.testAddListenersForSelectedProperty());
            checkResult(simple.testAdd6Listeners0ToProperty0());
            return passed();
        } catch (BoundException e) {

            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Add and remove many listeners for two different selected properties. Call
     * firePropertyChange method for one of selected property. Verify that all
     * listeners registered for this selected property are notified. Do the same
     * verification for other property.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Add 5 times listeners#0 for selected property#1.</li>
     * <li>Add 12 times listeners#0 for selected property#0.</li>
     * <li>Add 5 times listener#1 for selected property#1.</li>
     * <li>Add 6 times listener#0 for selected property#0.</li>
     * <li>Remove 5 times listeners#0 for selected property#1.</li>
     * <li>Remove 5 times listener#1 for selected property#1.</li>
     * <li>Remove 12 times listeners#0 for selected property#0.</li>
     * </ul>
     */
    public Result testForTwoDifferentProperties() {
        try {
            checkResult(test02());
            SimpleTests simple = new SimpleTests(this);
            checkResult(simple.testRemove5Listeners0fromProperty1());
            checkResult(simple.testRemoveListenersForSelectedProperty());
            checkResult(simple.testRemove6Listeners0fromProperty0());
            checkResult(simple.testRemove6Listeners0fromProperty0());
            return passed();
        } catch (BoundException e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * <li>Add 4 times listener#1 for all properties.</li>
     * <li>Add listener#1 for N different selected properties.</li>
     * <li>Add listener#2 for N different selected properties</li>
     * <li>Add 5 times listener#3 for selected property.</li>
     * <li>Add 10 times listener#1 for selected property.</li>
     * <li>Add 4 times listener#1 for all properties.</li>
     * <li>Add listener#1 for N different selected properties.</li>
     * <li>Add listener#2 for N different selected properties</li>
     * <li>Add 5 times listener#3 for selected property.</li>
     * <li>Add 10 times listener#1 for selected property.</li>
     */
    /**
     * Disregard this test case.
     */
    public Result test03() {
        try {
            checkResult(testAddListenersForSelectedPropertyAndForAllProperties());
            certainProperties[0].fireAndVerify("older value", "newer value");
            checkResult(testAddListenersForSelectedPropertyAndForAllProperties());
            return passed();
        } catch (Exception e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Use different combination of adding and removing different listeners for
     * different selected properties and for all properties. Verify that all
     * listeners registered for selected property and for all property are
     * notified when call firePropertyChange method for selected property.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Do the same actions as in
     * testForSelectedPropertyAndForAllProperties()</li>
     * <li>Do the same actions as in testForTwoDifferentProperties()</li>
     * <li>Add 4 times listener#1 for all properties.</li>
     * <li>Add listener#1 for N different selected properties.</li>
     * <li>Add listener#2 for N different selected properties</li>
     * <li>Add 5 times listener#3 for selected property.</li>
     * <li>Add 10 times listener#1 for selected property.</li>
     * <li>Do the same actions as in
     * testForSelectedPropertyAndForAllProperties()</li>
     * <li>After each adding or removing firePropertyChange method is invoked
     * and test verifies that all registered listeners were notified.</li>
     * <ul>
     */
    public Result testAddRemoveStress() {
        try {
            checkResult(testAddListenersForSelectedPropertyAndForAllProperties());
            checkResult(testForTwoDifferentProperties());
            checkResult(testAddListenersForSelectedPropertyAndForAllProperties());
            checkResult(testForSelectedPropertyAndForAllProperties());
            return passed();
        } catch (BoundException e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * Register 200 listeners for selected property. Verify that all registered
     * listeners are notified, when selected property is changed.
     * <p>
     * Step-by-step decoding:
     * <ul>
     * <li>Add 5 times listener for selected property.
     * <li>Add 5 times other listener for selected property.
     * <li>Repeat 20 times step 1 and 2.
     * <li>After each adding firePropertyChange method is invoked and test
     * verifies that all registered listeners were notified.
     */
    /**
     * Disregard this test case.
     */
    public Result testStress() {
        try {
            SimpleTests simple = new SimpleTests(this);
            for (int i = 0; i < 20; i++) {
                checkResult(simple.testAdd5Listeners0ToProperty1());
                checkResult(simple.testAddListenersForSelectedProperty());
            }
            return passed();
        } catch (BoundException e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    /**
     * This test runs test01 from this class. Then this test runs
     * testRemove5Listeners0fromProp1() and testRemove5Listeners1fromProp1() 10
     * times.
     */
    /**
     * Disregard this test case.
     */
    public Result test06() {
        try {
            checkResult(testAddListenersForSelectedPropertyAndForAllProperties());
            SimpleTests simple = new SimpleTests(this);
            for (int i = 0; i < 10; i++) {
                checkResult(simple.testRemove5Listeners0fromProperty1());
                checkResult(simple.testRemoveListenersForSelectedProperty());
            }
            return passed();
        } catch (BoundException e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }
    }

    public ComplexTests() {
        startup(null);
    }

    private void checkResult(Result result) throws BoundException {
        if (result.getResult() != Result.PASS) {
            throw new BoundException(result.getMessage());
        }
    }

    public static void main(String[] args) {
        System.exit(new ComplexTests().test(args));
    }
}