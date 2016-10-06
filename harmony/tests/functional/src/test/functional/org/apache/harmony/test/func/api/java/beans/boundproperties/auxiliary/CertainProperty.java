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
package org.apache.harmony.test.func.api.java.beans.boundproperties.auxiliary;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;

/**
 * This class adds listeners, fires events and deletes listeners for particular
 * properties. Also verify that method propertyChange is invoked required number
 * of times.
 * 
 */
public class CertainProperty {
    private PropertyChangeSupport             vcs;

    private String                            propertyName;

    /**
     * PropertyChangeListener are stored in this array.
     */
    private AuxiliaryPropertyChangeListener[] pcl;

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
        pcl = new AuxiliaryPropertyChangeListener[3];
        pcl[0] = new AuxiliaryPropertyChangeListener0();
        pcl[1] = new AuxiliaryPropertyChangeListener1();
        pcl[2] = new AuxiliaryPropertyChangeListener2();
    }

    /**
     * Add listener to vcs. number points to listener which adds to vcs, for
     * example, 1 points to AuxiliaryPropertyChangeListener1 class. Invoke
     * vcs.addVetoableChangeListener(propertyName,vetoableChangeListenerMy1).
     * 
     * @param number is points to listener which adds to vcs
     */
    public void addListener(int number) {// Need after line 1 line 2
                                            // executes. ?
        vcs.addPropertyChangeListener(propertyName, pcl[number]);
        pcl[number].listenerWasAdded();
    }

    /**
     * Remove listener from vcs for propertyName. Number points to listener
     * which removes from vcs, for example, 1 points to
     * AuxiliaryPropertyChangeListener1 class.
     * 
     * @param number points to listener which removes from vcs
     * @throws BoundException
     */
    public void removeListener(int number) throws BoundException {
        vcs.removePropertyChangeListener(propertyName, pcl[number]);
        pcl[number].listenerWasRemoved();
    }

    /**
     * Invoke method vcs.firePropertyChange(propetyName,oldValue,newValue) and
     * persist old and new value of changed property.
     * 
     * @param oldValue is old value of changed property
     * @param newValue is new value of changed property
     * @throws PropertyVetoException
     */
    private void firePropertyChange(Object oldValue, Object newValue)
        throws PropertyVetoException {
        vcs.firePropertyChange(propertyName, oldValue, newValue);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * Verify that number of invocations of method addListener minus invocations
     * of method removedListener are equal number of invocations of method
     * VetoableChangeListener.vetoableChange for given property for all
     * listeners. Besides verify that old and new value in PropertyChangeEvent
     * is equal to old and new value in parameter in method
     * PropertyChangeSupport.firePropertyChange.
     */
    private void verify() throws Exception {
        for (int number = 0; number < pcl.length; number++) {
            pcl[number].verify(oldValue, newValue);
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
        for (int number = 0; number < pcl.length; number++) {
            pcl[number].verifyThatCountIs0();
        }
    }

    /**
     * Invoke firePropertyChange of this class and then invoked verify.
     * 
     * @param oldValue is old value of changed property.
     * @param newValue is new value of changed property.
     */
    public void fireAndVerify(Object oldValue, Object newValue)
        throws Exception {
        /**
         * Verify that doesn't execute simultaneously both addListener and
         * firePropertyChange methods of PropertyChangeSupport class.
         */
        synchronized (pcl) {
            firePropertyChange(oldValue, newValue);
            verify();
        }
    }
}