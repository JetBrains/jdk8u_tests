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
package org.apache.harmony.tests.java.awt;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeListenerProxy;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.TestCase;

public class KeyboardFocusManagerTest extends TestCase {

    boolean                listenerCalled, newListenerCalled, vlistenerCalled;
    KeyboardFocusManager kfm       = new MyKeyboardManager();

    SimpleComponent        comp      = new SimpleComponent();
    SimpleComponent        comp1     = new SimpleComponent();
    PropertyChangeEvent    event, newEvent, vetoedEvent;
    PropertyChangeListener listener  = new PropertyChangeListener() {

                                         public void propertyChange(
                                                 PropertyChangeEvent e) {
                                             listenerCalled = true;
                                             event = e;
                                         }

                                     };
    VetoableChangeListener vlistener = new VetoableChangeListener() {
                                         String vetoedPropName = "activeWindow";

                                         public void vetoableChange(
                                                 PropertyChangeEvent e)
                                                 throws PropertyVetoException {
                                             event = e;
                                             vlistenerCalled = true;
                                             String propName = e
                                                     .getPropertyName();
                                             if (propName
                                                     .equals(vetoedPropName)
                                                     && e.getNewValue() != null) {
                                                 vetoedEvent = e;
                                                 throw new PropertyVetoException(
                                                         propName
                                                                 + " change is vetoed!",
                                                         e);
                                             }
                                         }

                                     };

    public static void main(String[] args) {
        junit.textui.TestRunner.run(KeyboardFocusManagerTest.class);
    }

    @SuppressWarnings("serial")
    public class SimpleComponent extends Component {
    }

    class MyKeyboardManager extends DefaultKeyboardFocusManager {

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        listenerCalled = vlistenerCalled = false;
        clearVetoableChangeListeners();
        clearPropertyChangeListeners();
        kfm.clearGlobalFocusOwner();
        event = vetoedEvent = newEvent = null;
    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    void clearPropertyChangeListeners() {
        if (kfm != null) {
            PropertyChangeListener[] listeners = kfm
                    .getPropertyChangeListeners();
            if (listeners != null) {
                for (PropertyChangeListener element : listeners) {
                    kfm.removePropertyChangeListener(element);
                }
            }
        }
    }

    void clearVetoableChangeListeners() {
        if (kfm != null) {
            VetoableChangeListener[] listeners = kfm
                    .getVetoableChangeListeners();
            if (listeners != null) {
                for (VetoableChangeListener element : listeners) {
                    kfm.removeVetoableChangeListener(element);
                }
            }
        }
    }

    /*
     * Class under test for java.beans.PropertyChangeListener[]
     * getPropertyChangeListeners()
     */
    public final void testGetPropertyChangeListeners() {
        PropertyChangeListener[] listeners = kfm.getPropertyChangeListeners();
        assertNotNull(listeners);
        assertEquals(0, listeners.length);
        kfm.addPropertyChangeListener(listener);
        assertEquals(0, listeners.length);
        listeners = kfm.getPropertyChangeListeners();
        assertEquals(1, listeners.length);
        assertSame(listener, listeners[0]);
        String propName = "currentFocusCycleRoot";
        kfm.addPropertyChangeListener(propName, listener);
        listeners = kfm.getPropertyChangeListeners();
        assertEquals(2, listeners.length);
        assertNotSame(listener, listeners[1]);
        assertTrue(listeners[1] instanceof PropertyChangeListenerProxy);
        PropertyChangeListenerProxy proxy = (PropertyChangeListenerProxy) listeners[1];
        assertEquals(propName, proxy.getPropertyName());
        assertSame(listener, proxy.getListener());
    }

    /*
     * Class under test for java.beans.PropertyChangeListener[]
     * getPropertyChangeListeners(java.lang.String)
     */
    public final void testGetPropertyChangeListenersString() {
        String propName = "defaultFocusTraversalPolicy";
        PropertyChangeListener[] listeners = kfm
                .getPropertyChangeListeners(propName);
        assertNotNull(listeners);
        assertEquals(0, listeners.length);

        kfm.addPropertyChangeListener("focusedWindow", listener);
        int len = kfm.getPropertyChangeListeners(propName).length;
        assertEquals(0, len);
        kfm.addPropertyChangeListener(propName, listener);
        listeners = kfm.getPropertyChangeListeners(propName);
        len = listeners.length;
        assertEquals(1, len);
        assertSame(listener, listeners[0]);
        listeners = kfm.getPropertyChangeListeners("property");
        assertEquals(0, listeners.length);
    }

    /*
     * Class under test for java.beans.VetoableChangeListener[]
     * getVetoableChangeListeners(java.lang.String)
     */
    public final void testGetVetoableChangeListenersString() {
        String propName = "permanentFocusOwner";
        VetoableChangeListener[] listeners = kfm
                .getVetoableChangeListeners(propName);
        assertNotNull(listeners);
        assertEquals(0, listeners.length);

        kfm.addPropertyChangeListener("focusedWindow", listener);
        int len = kfm.getVetoableChangeListeners(propName).length;
        assertEquals(0, len);
        kfm.addVetoableChangeListener(propName, vlistener);
        listeners = kfm.getVetoableChangeListeners(propName);
        len = listeners.length;
        assertEquals(1, len);
        assertSame(vlistener, listeners[0]);
        listeners = kfm.getVetoableChangeListeners("q");
        assertEquals(0, listeners.length);
    }

    /*
     * Class under test for java.beans.VetoableChangeListener[]
     * getVetoableChangeListeners()
     */
    public final void testGetVetoableChangeListeners() {
        VetoableChangeListener[] listeners = kfm.getVetoableChangeListeners();
        assertNotNull(listeners);
        assertEquals(0, listeners.length);
        kfm.addVetoableChangeListener(vlistener);
        assertEquals(0, listeners.length);
        listeners = kfm.getVetoableChangeListeners();
        assertEquals(1, listeners.length);
        assertSame(vlistener, listeners[0]);
        String propName = "focusOwner";
        kfm.addVetoableChangeListener(propName, vlistener);
        listeners = kfm.getVetoableChangeListeners();
        assertEquals(2, listeners.length);
        assertNotSame(vlistener, listeners[1]);
        assertTrue(listeners[1] instanceof VetoableChangeListenerProxy);
        VetoableChangeListenerProxy proxy = (VetoableChangeListenerProxy) listeners[1];
        assertEquals(propName, proxy.getPropertyName());
        assertSame(vlistener, proxy.getListener());
    }

    /*
     * Class under test for void removePropertyChangeListener(java.lang.String,
     * java.beans.PropertyChangeListener)
     */
    public final void testRemovePropertyChangeListenerString() {
        String propName = "forwardDefaultFocusTraversalKeys";
        kfm.removePropertyChangeListener(propName, null);
        PropertyChangeListener[] listeners = kfm
                .getPropertyChangeListeners(propName);
        assertNotNull(listeners);
        assertEquals(0, listeners.length);
        kfm.addPropertyChangeListener(propName, listener);
        listeners = kfm.getPropertyChangeListeners(propName);
        assertEquals(1, listeners.length);
        Set<AWTKeyStroke> forSet = new HashSet<AWTKeyStroke>();
        forSet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
        kfm.setDefaultFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forSet);
        assertTrue(listenerCalled);
        listenerCalled = false;
        kfm.removePropertyChangeListener("property", listener);
        listeners = kfm.getPropertyChangeListeners(propName);
        assertEquals(1, listeners.length);
        kfm.removePropertyChangeListener(propName, listener);
        listeners = kfm.getPropertyChangeListeners(propName);
        assertEquals(0, listeners.length);
        forSet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER,
                InputEvent.CTRL_DOWN_MASK));
        kfm.setDefaultFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forSet);
        assertFalse(listenerCalled);
    }

    /*
     * Class under test for void
     * removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    // public final void testRemovePropertyChangeListener() {
    // String propName = "backwardDefaultFocusTraversalKeys";
    // kfm.removePropertyChangeListener(listener);
    // PropertyChangeListener[] listeners = kfm.getPropertyChangeListeners();
    // assertNotNull(listeners);
    // assertEquals(0, listeners.length);
    // kfm.addPropertyChangeListener(propName, listener);
    // listeners = kfm.getPropertyChangeListeners();
    // assertEquals(1, listeners.length);
    // Set backSet = new HashSet();
    // backSet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_SPACE,
    // KeyEvent.CTRL_DOWN_MASK));
    // kfm.setDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
    // backSet);
    // assertTrue(listenerCalled);
    // listenerCalled = false;
    // kfm.removePropertyChangeListener(listeners[0]);
    // listeners = kfm.getPropertyChangeListeners();
    // assertEquals(0, listeners.length);
    // backSet.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_SPACE, 0));
    // kfm.setDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
    // backSet);
    // assertFalse(listenerCalled);
    // kfm.addPropertyChangeListener(listener);
    // listeners = kfm.getPropertyChangeListeners();
    // assertEquals(1, listeners.length);
    // kfm.firePropertyChange("property", new Integer(0), new Integer(1));
    // assertTrue(listenerCalled);
    // listenerCalled = false;
    // kfm.removePropertyChangeListener(listener);
    // listeners = kfm.getPropertyChangeListeners();
    // assertEquals(0, listeners.length);
    // kfm.firePropertyChange("property", new Integer(1), new Integer(0));
    // assertFalse(listenerCalled);
    //
    // }
    public final void testSetCurrentKeyboardFocusManager() {
        String propName = "managingFocus";
        PropertyChangeListener newListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                newListenerCalled = true;
                newEvent = e;
            }

        };

        KeyboardFocusManager oldFocusManager = KeyboardFocusManager
                .getCurrentKeyboardFocusManager();
        assertNotSame(oldFocusManager, kfm);
        oldFocusManager.addPropertyChangeListener(propName, listener);
        kfm.addPropertyChangeListener(propName, newListener);
        KeyboardFocusManager.setCurrentKeyboardFocusManager(kfm);
        assertSame(KeyboardFocusManager.getCurrentKeyboardFocusManager(), kfm);
        assertTrue(listenerCalled);
        assertNotNull(event);
        assertSame(event.getSource(), oldFocusManager);
        assertEquals(propName, event.getPropertyName());
        assertEquals(event.getOldValue(), new Boolean(true));
        assertEquals(event.getNewValue(), new Boolean(false));
        assertTrue(newListenerCalled);
        assertNotNull(newEvent);
        assertSame(newEvent.getSource(), kfm);
        assertEquals(propName, newEvent.getPropertyName());
        assertEquals(newEvent.getOldValue(), new Boolean(false));
        assertEquals(newEvent.getNewValue(), new Boolean(true));
    }

    public void testSetDefaultFocusTraversalKeys() {
        final KeyboardFocusManager mgr = new DefaultKeyboardFocusManager();
        Set<AWTKeyStroke> s;
        Set<AWTKeyStroke> s1;

        try {
            mgr.setDefaultFocusTraversalKeys(-1, createSet());
        } catch (IllegalArgumentException ex) {
            // expected
        }

        try {
            mgr.setDefaultFocusTraversalKeys(
                    KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
        } catch (IllegalArgumentException ex) {
            // expected
        }

        try {
            mgr.setDefaultFocusTraversalKeys(
                    KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                    createSet((AWTKeyStroke) null));
        } catch (IllegalArgumentException ex) {
            // expected
        }

        try {
            mgr.setDefaultFocusTraversalKeys(
                    KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                    createSet(AWTKeyStroke.getAWTKeyStroke(KeyEvent.KEY_TYPED,
                            0)));
        } catch (IllegalArgumentException ex) {
            // expected
        }

        try {
            mgr
                    .setDefaultFocusTraversalKeys(
                            KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                            createSet(AWTKeyStroke.getAWTKeyStroke(
                                    KeyEvent.VK_TAB, 0)));
        } catch (IllegalArgumentException ex) {
            // expected
        }

        s = createSet(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
        s1 = mgr
                .getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        mgr.addPropertyChangeListener(listener);
        mgr.setDefaultFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, s);
        assertEquals(
                s,
                mgr
                        .getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        assertNotNull(event);
        assertSame(s, event.getNewValue());
        assertSame(s1, event.getOldValue());
    }

    public void testGetDefaultFocusTraversalKeys() {
        final KeyboardFocusManager mgr = new DefaultKeyboardFocusManager();

        assertNotNull(mgr
                .getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        assertNotNull(mgr
                .getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
        assertNotNull(mgr
                .getDefaultFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS));
        assertNotNull(mgr
                .getDefaultFocusTraversalKeys(KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS));

        assertEquals(
                createSet(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0),
                        AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB,
                                InputEvent.CTRL_DOWN_MASK)),
                mgr
                        .getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        assertEquals(
                createSet(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB,
                        InputEvent.SHIFT_DOWN_MASK), AWTKeyStroke
                        .getAWTKeyStroke(KeyEvent.VK_TAB,
                                InputEvent.SHIFT_DOWN_MASK
                                        | InputEvent.CTRL_DOWN_MASK)),
                mgr
                        .getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
        assertTrue(mgr.getDefaultFocusTraversalKeys(
                KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS).isEmpty());
        assertTrue(mgr.getDefaultFocusTraversalKeys(
                KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS).isEmpty());

        try {
            mgr.getDefaultFocusTraversalKeys(
                    KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS).add(null);
            fail("UnsupportedOperationException was not thrown"); //$NON-NLS-1$
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    private static Set<AWTKeyStroke> createSet(final AWTKeyStroke... keystrokes) {
        final Set<AWTKeyStroke> s = new LinkedHashSet<AWTKeyStroke>();

        for (AWTKeyStroke stroke : keystrokes) {
            s.add(stroke);
        }
        return s;
    }
}
