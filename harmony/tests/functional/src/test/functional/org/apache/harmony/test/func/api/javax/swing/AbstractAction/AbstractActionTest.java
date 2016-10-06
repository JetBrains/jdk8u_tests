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
/** 
 */  
/*
 * Created on 22.02.2005
 */
package org.apache.harmony.test.func.api.javax.swing.AbstractAction;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Array;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


public class AbstractActionTest extends MultiCase {
    private static final int KEYS_TEST_QUANTITY = 100;
    private static final String PROPERTY_NAME = "propertyName";
    private AbstractActionImpl instance;
    
    private AbstractActionImpl createInstance() {
        return new AbstractActionImpl();
    }
    
    private void checkNewInstance(AbstractAction action) throws TestFailException {
        if (action == null) {
            throw new TestFailException("fail to create an instance");
        }
        if (!action.isEnabled()) {
            throw new TestFailException("enable is false by default");
        }
    }
    
    public Result testInit() {
        instance = new AbstractActionImpl();
        checkNewInstance( instance );
        return passed();
    }
    
    public Result testInit02() {
        instance = new AbstractActionImpl("Name");
        checkNewInstance( instance );
        instance = new AbstractActionImpl(null);
        checkNewInstance( instance );
        return passed();
        
    }
    
    public Result testInit03() {
        instance = new AbstractActionImpl(null, null);
        checkNewInstance( instance );
        instance = new AbstractActionImpl(null, new IconImpl());
        checkNewInstance( instance );
        instance = new AbstractActionImpl("", null);
        checkNewInstance( instance );
        instance = new AbstractActionImpl("Name", new IconImpl());
        checkNewInstance( instance );
        return passed();
    }
    
    public Result testAddPropertyChangeListener() {
        instance = createInstance();
        try {
            instance.addPropertyChangeListener( null );
            instance.removePropertyChangeListener( null );
        } catch (NullPointerException e) {
            return failed("Unexpected NPE");
        }
        
        PropertyChangeListener propListener = new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent arg0) {
            }
        };  
        
        instance.addPropertyChangeListener(propListener);
        PropertyChangeListener[] array = instance.getPropertyChangeListeners();
        int c = countOccurences( array, propListener );         
        if (c != 1) {
           return failed("Unexpected number of own PropertyChangeListeners after adding one: " + c + "(must be 1)"); 
        }
        instance.removePropertyChangeListener( propListener );
        array = instance.getPropertyChangeListeners();
        c = countOccurences( array, propListener );         
        if (c != 0) {
           return failed("Unexpected number of own PropertyChangeListeners after removing one: " + c + "(must be 0)"); 
        }
        return passed();
    }
    
    private int countOccurences(Object[] array, Object key) {
        int i, k = 0;
        for (i = 0; i <  array.length ; i++) {
            if (array[i] == key) {
                k++;
            }
        }
        return k;
    }
    
    private int countOccurencesEquals(Object[] array, Object key) {
        int i, k = 0;
        for (i = 0; i <  array.length ; i++) {
            if (array[i].equals(key)) {
                k++;
            }
        }
        return k;
    }
    
    public Result testClone() {
        instance = createInstance();
        Object value1 = new Object();
        instance.putValue("Value001", value1);
        AbstractActionImpl instance2;
        try {
            instance2 = (AbstractActionImpl) instance.clone();
        } catch (CloneNotSupportedException e) {
            return failed("Unexpected CNSE");
        } catch (NullPointerException e2) {
            return failed("Unxexepted NPE");
        }
        
        Object value2 = new Object();
        instance.putValue( "Value001", value2);
        if (instance2.getValue( "Value001") == value2) {
            return failed("cloned object has shared values list");
        }
        try {
            checkNewInstance(instance2);
        } catch (TestFailException e1) {
            return failed(e1.getMessage());
        }
        return passed();
    }
    
    boolean fireOk;
    
    private boolean checkPropertyChangeEventProcessing() {
        fireOk = false;
        final Object oldValue = new Object();
        final Object newValue = new Object();
        final Object lock = new Object();
        instance.addPropertyChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent e) {
                fireOk = false;
                if (oldValue == e.getOldValue()) {
                    if (newValue == e.getNewValue()) {
                        if (e.getPropertyName().equals( PROPERTY_NAME )) {
                            fireOk = true;
                        }
                    }
                }
//                synchronized(lock) {
//                    lock.notify();
//                }
            }
        });
        instance.firePropertyChange( PROPERTY_NAME, oldValue, newValue );
//        synchronized(lock) {
//            try {
//                lock.wait(200);
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }
//        }
        if (!fireOk ) {
            return false;
        }
        return true;
    }
    
    public Result testFirePropertyChange() {
        instance = createInstance();
        instance.setEnabled(true);
        if (!checkPropertyChangeEventProcessing()) {
            return failed("wrong event occurs on firePropertyChange() call");
        }
        
        return passed();
    }
    
    public Result testGetKeys() {
        instance = createInstance();
        Object value = new Object();
        instance.putValue( "KeyName", value);
        Object[] keys = instance.getKeys();
        int k = countOccurencesEquals(keys, "KeyName");
        if (k != 1) {
            return failed("Unexpected number of own keys : " + k + "(must be 1)"); 
        }
        int len = keys.length;
        for (int i = 0; i < KEYS_TEST_QUANTITY; i++) {
            if (instance.getValue( Integer.toString( i )) == null) {
                instance.putValue( Integer.toString( i ), new Object());
            } 
        }
        if (instance.getKeys().length != len + KEYS_TEST_QUANTITY) {
            return failed("Unexpected number of added keys");
        }
        return passed();
    }
    
    public Result testIsEnabled() {
        instance = createInstance();
        instance.setEnabledTest(false);
        instance.setEnabled(true);
        if (!instance.isEnabled() ) {
            return failed("Couldn't set to enabled");
        }
        instance.setEnabledTest(true);
        instance.setEnabled( false ); 
        if (instance.isEnabled() ){
            return failed("Couldn't set to disabled");
        }
        return passed();
    }
    
    public Result testSetEnabled() {
        return testIsEnabled() ;
    }
    
    private boolean putResult;
    
    public Result testPutValue() {
        putResult = false;
        instance = createInstance();
        final Object value001 = new Object();
        instance.addPropertyChangeListener( new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                putResult = false;
                if (e.getPropertyName().equals( "Value001")) {
                    if (e.getNewValue() == value001) {
                        putResult = true;
                    }
                }
            }
        });
        
        instance.putValue( "Value001", value001);
        if (instance.getValue( "Value001") != value001) {
            return failed("Couldn't give back the value");
        }
        
        if (!putResult) {
            return failed("Expected changed property event occurence, but it wasn't");
        }
        
        Object value002 = new Object();
        instance.putValue( "Value001", value002);
        if (instance.getValue( "Value001") != value002) {
            if (instance.getValue( "Value001") == value001) {
                return failed("Couldn't reset value");
            } else {
                return failed("Couldn't give back the value2");
            }
        }
        return passed();
    }
    
    public Result testGetValue() {
        instance = createInstance();
        instance.putValue( "Value001", null);
        if (instance.getValue( "Value001") != null) {
            return failed("Couldn't clear the key");
        }
        Object value = new Object();
        instance.putValue( "Value001", value); 
        if (instance.getValue( "Value001") != value) {
            return failed("Couldn't give back the value");
        }
        return passed();
    }

    public static void main(String[] args) {
        System.exit( new AbstractActionTest().test(args) );
    }
    
    private class AbstractActionImpl extends AbstractAction {
        public AbstractActionImpl() {
            super();
        }
        public AbstractActionImpl(String s) {
            super(s);
        }
        public AbstractActionImpl(String s, Icon i) {
            super(s, i);
        }
        public void actionPerformed(ActionEvent arg0) {
        }
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
        public void firePropertyChange(String arg0, Object arg1, Object arg2) {
            super.firePropertyChange(arg0, arg1, arg2);
        }
        public void setEnabledTest(boolean enabled) {
            this.enabled = enabled;
        }
        
    }
    
    private class TestFailException extends RuntimeException {
        public TestFailException(String message) {
            super(message);
        }
    }
    
    private class IconImpl implements Icon {
        public int getIconHeight() {
            return 2;
        }
        public int getIconWidth() {
            return 2;
        }
        public void paintIcon(Component arg0, Graphics arg1, int arg2, int arg3) {
        }
}
    }
