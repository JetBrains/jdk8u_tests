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

import java.beans.VetoableChangeSupport;

/**
 * This class adds listeners, fires events and deletes listeners for all
 * properties.Also one can know how many invocations of method
 * vetoableChange(PropertyChangeEvent pce) have to occur.
 * 
 */
public class AllProperties {
    private VetoableChangeSupport             vcs;

    private AuxiliaryVetoableChangeListener[] vcl;

    /**
     * @param variables are variables for this class.
     */
    public AllProperties(Variables variables) {
        this.vcs = variables.getVcs();
        vcl = new AuxiliaryVetoableChangeListener[3];
        vcl[0] = new AuxiliaryVetoableChangeListener0();
        vcl[1] = new AuxiliaryVetoableChangeListener1();
        vcl[2] = new AuxiliaryVetoableChangeListener2();
    }

    /**
     * Add listener to vcs. number points to listener which adds to vcs, for
     * example, 1 points to VetoableChangeListener1 class. Invoke
     * vcs.addVetoableChangeListener(vetoableChangeListener1).
     * 
     * @param number points to listener which adds to VetoableChangeSupport
     */
    public void addListener(int number) {
        vcs.addVetoableChangeListener(vcl[number]);
        vcl[number].listenerWasAdded();
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
        vcs.removeVetoableChangeListener(vcl[number]);
        vcl[number].listenerWasRemoved();
    }

    /**
     * Verify that number of invocations of addListener minus invocations of
     * removedListener are equal number of invocations of
     * VetoableChangeListener.vetoableChange for all listener. Besides verify
     * that old value and new value in PropertyChangeEvent is equal to old value
     * and new value in parameter in method
     * VetoableChangeSupport.fireVetoableChange.
     * 
     * @param oldValue is old value of changed property.
     * @param newValue is new value of changed property.
     */
    public void verify(Object oldValue, Object newValue) throws Exception {
        for (int number = 0; number < vcl.length; number++) {
            vcl[number].verify(oldValue, newValue);
        }
    }
}