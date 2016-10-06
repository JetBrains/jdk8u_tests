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

/**
 * This class adds listeners, fires events and deletes listeners for all
 * properties. Also one can verify that number of invocations is equal to number
 * of registered listeners.
 * 
 */
public class AllProperties {
    private PropertyChangeSupport             vcs;
    private AuxiliaryPropertyChangeListener[] pcl;

    /**
     * @param variables are variables for this class.
     */
    public AllProperties(Variables variables) {
        this.vcs = variables.getVcs();
        pcl = new AuxiliaryPropertyChangeListener[3];
        pcl[0] = new AuxiliaryPropertyChangeListener0();
        pcl[1] = new AuxiliaryPropertyChangeListener1();
        pcl[2] = new AuxiliaryPropertyChangeListener2();
    }

    /**
     * Add listener to pcs. number points to listener which adds to vcs, for
     * example, 1 points to VetoableChangeListener1 class. Invoke
     * vcs.addVetoableChangeListener(vetoableChangeListener1).
     * 
     * @param number points to listener which adds to PropertyChangeSupport
     */
    public void addListener(int number) {
        vcs.addPropertyChangeListener(pcl[number]);
        pcl[number].listenerWasAdded();
    }

    /**
     * Remove listener from vcs for all properties. number points to listener
     * which removes from vcs, for example, 1 points to VetoableChangeListener1
     * class.
     * 
     * @param number points to listener which removes from vcs
     * @throws Exception
     */
    public void removeListener(int number) throws Exception {
        vcs.removePropertyChangeListener(pcl[number]);
        pcl[number].listenerWasRemoved();
    }

    /**
     * Verify that number of invocations of addListener minus invocations of
     * removedListener are equal number of invocations of
     * VetoableChangeListener.vetoableChange for all listener. Besides verify
     * that old value and new value in PropertyChangeEvent is equal to old value
     * and new value in parameter in method
     * PropertyChangeSupport.firePropertyChange.
     * 
     * @param oldValue is old value of changed property.
     * @param newValue is new value of changed property.
     */
    public void verify(Object oldValue, Object newValue) throws Exception {
        for (int number = 0; number < pcl.length; number++) {
            pcl[number].verify(oldValue, newValue);
        }
    }
}