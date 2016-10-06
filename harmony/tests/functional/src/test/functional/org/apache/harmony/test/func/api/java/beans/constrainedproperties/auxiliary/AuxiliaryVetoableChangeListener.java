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
// Exp $
package org.apache.harmony.test.func.api.java.beans.constrainedproperties.auxiliary;

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;

/**
 * Abstract class AuxiliaryVetoableChangeListener implements
 * VetoableChangeListener. Subclass can verify that number of invocations of
 * method listenerWasAdded minus invocations of method listenerWasRemoved are
 * equal number of invocations of method vetoableChange(PropertyChangeEvent
 * arg0). Besides can verify that old value and new value in PropertyChangeEvent
 * is equal to old and new value in parameter of method
 * VetoableChangeSupport.fireVetoableChange.
 * 
 */
public abstract class AuxiliaryVetoableChangeListener implements
    VetoableChangeListener {
    private BoundException      boundException;

    /**
     * <code>count</code> show: how many times this listener was notified from
     * last invocation of verify method of this class.
     */
    private int                 count   = 0;

    /**
     * <code>assumed</code> show: how many times this listener will have to be
     * notified from last invocation of verify method of this class.
     */
    private int                 assumed = 0;

    /**
     * <code>pce</code> instance of PropertyChangeEvent.
     */
    private PropertyChangeEvent pce;

    /**
     * <code>isFailed</code> show: did the test have errors.
     */

    /**
     * Notify that instance of listener was added to registered listeners.
     * 
     * @see java.beans.VetoableChangeSupport#addVetoableChangeListener(java.lang.String,
     *      java.beans.VetoableChangeListener)
     * @see java.beans.VetoableChangeSupport#addVetoableChangeListener(java.beans.VetoableChangeListener)
     */
    public void listenerWasAdded() {
        assumed++;
    }

    /**
     * Notify that instance of listener was removed from registered listeners.
     * 
     * @see java.beans.VetoableChangeSupport#removeVetoableChangeListener(java.lang.String,
     *      java.beans.VetoableChangeListener)
     * @see java.beans.VetoableChangeSupport#removeVetoableChangeListener(java.beans.VetoableChangeListener)
     */
    public void listenerWasRemoved() throws BoundException {
        if (assumed != 0) {
            assumed--;
        }
    }

    /**
     * This method implements
     * {@link VetoableChangeListener#vetoableChange(PropertyChangeEvent)}
     * method.
     */
    public void vetoableChange(PropertyChangeEvent pce) {
        if (boundException != null)
            return;
        if (this.pce == null) {
            this.pce = pce;
        } else if (this.pce != pce) {
            if (!this.pce.getNewValue().equals(pce.getNewValue())) {
                boundException = new BoundException("new Value");
            }
            if (!this.pce.getOldValue().equals(pce.getOldValue())) {
                boundException = new BoundException("old Value");
            }
            if (!this.pce.getPropertyName().equals(pce.getPropertyName())) {
                boundException = new BoundException("PropertyName");
            }
            if (!this.pce.getSource().equals(pce.getSource())) {
                boundException = new BoundException("Source");
            }
        }
        count++;
    }

    /**
     * This method verify that number of invocations of method listenerWasAdded
     * minus invocations of method listenerWasRemoved are equal number of
     * invocations of method vetoableChange(PropertyChangeEvent arg0). Besides
     * can verify that old value and new value in PropertyChangeEvent is equal
     * to old and new value in parameter of method
     * VetoableChangeSupport.fireVetoableChange.
     * 
     * @param oldValue is old value of changed property.
     * @param newValue is new value of changed property.
     * @throws BoundException
     */
    public void verify(Object oldValue, Object newValue) throws BoundException {
        if (boundException != null)
            throw boundException;
        if (count != assumed) {
            System.out.println("count :" + count);
            System.out.println("assumed :" + assumed);
            throw new BoundException("count=" + count + "!= assumed=" + assumed);
        }
        if (count == 0)
            return;
        if (!pce.getNewValue().equals(newValue)) {
            throw new BoundException(
                "new values are different in PropertyChangeEvent");
        }
        if (!pce.getOldValue().equals(oldValue)) {
            throw new BoundException(
                "old values are different in PropertyChangeEvent");
        }
        count = 0;
        pce = null;
    }

    /**
     * Verify that since last invocation of method verify of this class,listener
     * did not invoked.
     * 
     * @throws BoundException
     */
    public void verifyThatCountIs0() throws BoundException {
        if (count != 0) {
            throw new BoundException("");
        }
    }
}