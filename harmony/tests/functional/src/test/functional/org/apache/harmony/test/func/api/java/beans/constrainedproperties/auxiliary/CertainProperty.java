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
package org.apache.harmony.test.func.api.java.beans.constrainedproperties.auxiliary;

import java.beans.PropertyVetoException;
import java.beans.VetoableChangeSupport;

/**
 * This class adds listeners, fire events and delete listeners for particular
 * properties. Also verify that method propertyChange is invoked required number
 * of times.
 * 
 */
public class CertainProperty {
    private VetoableChangeSupport             vcs;

    private String                            propertyName;

    /**
     * VetoableChangeListener are stored in this array.
     */
    private AuxiliaryVetoableChangeListener[] vcl;

    /**
     * Old and new value of property.
     */
    private Object                            oldValue, newValue;

    private Variables                         variables;

    /**
     * @param propertyName is a name of property.
     * @param variables are variables for this class.
     */
    public CertainProperty(String propertyName, Variables variables) {
        this.propertyName = propertyName;
        this.vcs = variables.getVcs();
        this.variables = variables;
        vcl = new AuxiliaryVetoableChangeListener[3];
        vcl[0] = new AuxiliaryVetoableChangeListener0();
        vcl[1] = new AuxiliaryVetoableChangeListener1();
        vcl[2] = new AuxiliaryVetoableChangeListener2();
    }

    /**
     * Add listener to vcs. number points to listener which adds to vcs, for
     * example, 1 points to AuxiliaryVetoableChangeListener1 class. Invoke
     * vcs.addVetoableChangeListener(propertyName,vetoableChangeListenerMy1).
     * 
     * @param number is points to listener which adds to vcs
     */
    public void addListener(int number) {// Need after line 1 line 2
                                            // executes. ?
        vcs.addVetoableChangeListener(propertyName, vcl[number]);
        vcl[number].listenerWasAdded();
    }

    /**
     * Remove listener from vcs for propertyName. Number points to listener
     * which removes from vcs, for example, 1 points to
     * AuxiliaryVetoableChangeListener1 class.
     * 
     * @param number points to listener which removes from vcs
     * @throws BoundException
     */
    public void removeListener(int number) throws BoundException {
        vcs.removeVetoableChangeListener(propertyName, vcl[number]);
        vcl[number].listenerWasRemoved();
    }

    /**
     * Invoke method vcs.fireVetoableChange(propetyName,oldValue,newValue) and
     * persist old and new value of changed property.
     * 
     * @param oldValue is old value of changed property
     * @param newValue is new value of changed property
     * @throws PropertyVetoException
     */
    private void fireVetoableChange(Object oldValue, Object newValue)
        throws PropertyVetoException {
        vcs.fireVetoableChange(propertyName, oldValue, newValue);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Verify that number of invocations of method addListener minus invocations
     * of method removedListener are equal number of invocations of method
     * VetoableChangeListener.vetoableChange for given property for all
     * listeners. Besides verify that old and new value in PropertyChangeEvent
     * is equal to old and new value in parameter in method
     * VetoableChangeSupport.fireVetoableChange.
     */
    private void verify() throws Exception {
        for (int number = 0; number < vcl.length; number++) {
            vcl[number].verify(oldValue, newValue);
        }
        AllProperties allProperties = variables.getAllProperties();
        if (allProperties != null) {
            allProperties.verify(oldValue, newValue);
        }
        CertainProperty[] properties = variables.getCertainProperties();
        for (int i = 0; i < properties.length; i++) {
            properties[i].verifyThatNotInvoked();
        }
    }

    /**
     * Verify that since last invocation of method verify of this
     * class,listeners registered for this property did not invoked.
     * 
     * @throws BoundException
     */
    private void verifyThatNotInvoked() throws BoundException {
        for (int number = 0; number < vcl.length; number++) {
            vcl[number].verifyThatCountIs0();
        }
    }

    /**
     * Invoke fireVetoableChange of this class and then invoked verify.
     * 
     * @param oldValue is old value of changed property.
     * @param newValue is new value of changed property.
     */
    public void fireAndVerify(Object oldValue, Object newValue)
        throws Exception {
        /**
         * Verify that doesn't execute simultaneously both addListener and
         * fireVetoableChange methods of VetoableChangeSupport class.
         */
        synchronized (vcl) {
            fireVetoableChange(oldValue, newValue);
            verify();
        }
    }
}