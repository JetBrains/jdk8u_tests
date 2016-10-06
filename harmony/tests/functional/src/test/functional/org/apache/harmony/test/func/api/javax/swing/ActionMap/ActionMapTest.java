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
 * Created on 05.04.2005
 */
package org.apache.harmony.test.func.api.javax.swing.ActionMap;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;



public class ActionMapTest extends MultiCase {
    final int KEY_NUM = 20;
    ActionMap instance;
    public static void main(String[] args) {
        System.exit(new ActionMapTest().test(args));
    }
    
    public Result testInit() {
        instance = new ActionMap();
        if (instance.getParent() != null) {
            return failed("Parent should be null after creating");
        }
        if (instance.allKeys() != null || instance.keys() != null) {
            if (instance.allKeys().length != 0 || instance.keys().length != 0) {
                return failed("It should be no keys after creating");
            }
            
        }
        if (instance.size() != 0) {
            return failed("It should be no KeyStrokes after creating");
        }
        return passed();
    }
    
    
    public Result testSize() {
        instance = new ActionMap();
        //TODO find out how size() works and write appropriate testcase 
//        KeyStroke ks = KeyStroke.getKeyStroke("a");
//        Action a = new AbstractAction(){
//            public void actionPerformed(ActionEvent arg0) {
//            }
//        };
//        instance.put( ks, a);
//        int i = instance.size();
//        if (instance.size() != 1) {
//            return failed("failed to put a binding");
//        }
        return passed();
    }
    
    public Result testParent01() {
        instance = new ActionMap();
        ActionMap map2 = new ActionMap();
        instance.setParent( map2 );
        if (instance.getParent() != map2) {
            return failed("couldn't set parent");
        }
        
        ActionMap map3 = new ActionMap();
        instance.setParent( map3 );
        instance.setParent( map3 );
        if (instance.getParent() != map3) {
            return failed("couldn't set other parent");
        }
        
        instance.setParent( null );
        if (instance.getParent() != null ) {
            return failed("couldn't clear parent");
        }
        
        return passed();
    }
    
    public Result testGetPut01() {
        instance = new ActionMap();
        try {
            instance.put(null, null);
            instance.get(null);
        } catch (Exception e) {
            return failed("Unxecpected: " + e.getMessage()); 
        }
        Object key1 = new Object();
        A action1 = new A();
        instance.put( key1, action1 );
        if (instance.get( key1) != action1) {
            return failed("could't set/get bindings");
        }
        A action2 = new A();
        instance.put( key1, action2);
        
        if (instance.get( key1 ) != action2 ) {
            return failed("could't set/get bindings");
        }
        Object key2 = new Object();
        instance.put( key2, action2);
        if (instance.get(key1) != action2) {
            return failed("could't set one action for several keys");
        }
        
        instance.put(key2, null);
        if (instance.get( key2) != null) {
            return failed("could't remove key");
        }
        if (instance.get( key1) != action2) {
            return failed("removing failed");
        }
        class ActionKey {
            ActionKey(Object key, Action action) {
                this.key = key;
                this.action = action;
            }
            Object key;
            Action action;
        }
        ActionKey[] array = new ActionKey[3];
        ActionMap ansestorMap = instance;
        for (int i = 0; i < array.length; i++) {
            ActionMap map = new ActionMap();
            ansestorMap.setParent(map);
            ansestorMap = map;
            array[i] = new ActionKey(new Object(), new A());
            map.put( array[i].key, array[i].action);
        }
        for (int i = 0; i < array.length; i++ ) {
            if (instance.get( array[i].key ) != array[i].action ) {
                return failed("couldn't get binding from parent of " + i + " descent");
            }
        }
        return passed();
    }
    
    public Result testRemove() {
        instance = new ActionMap();
        try {
            instance.remove( null );
        } catch (Exception e) {
            return failed("Unexcpected: " + e.getMessage());
        }
        Object key1 = new Object();
        A action1  = new A();
        instance.put( key1, action1 );
        Object key2 = new Object();
        instance.remove( key2 );
        if (instance.get(key1) != action1) {
            return failed("keys are depended");
        }
        instance.remove( key1 );
        if (instance.get( key1 ) != null) {
            return failed("couldn't remove key");
        }
        return passed();
    }
    
    public Result testClear() {
        instance = new ActionMap();
        boolean nullKeys = instance.keys() == null;
        if (!nullKeys) {
            if (instance.keys().length != 0) {
                return failed("the init keys array is not empty");
            }
        }
        
        for (int i = 0; i < 20; i++) {
            instance.put( new Object(), new A());
        }
        Object[] keys = instance.keys();
        if (instance.keys().length != 20) {
            return failed("couldn't put keys");
        }
        instance.clear();
        if (!nullKeys) {
            if (instance.keys() == null) {
                return failed("history depended behaviour of clear() detected");
            }
            if (instance.keys().length != 0) {
                return failed("couldn't clear keys" + instance.keys().length );
            } 
        } else {
            if (instance.keys() != null) {
                return failed("couldn't clear keys" + instance.keys().length );
            }
        }
        
        return passed();
    }
    
    public Result testKeys() {
        instance = new ActionMap();
        Object[] keys = new Object[KEY_NUM];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = new Object();
            instance.put(keys[i], new A());
        }
        Object[] retrKeys = instance.keys();
        if (retrKeys.length == keys.length ) {
            for (int i = 0; i < keys.length; i++) {
                if (!ActionMapTest.contains( keys, retrKeys[i])) {
                    return failed("key # " + i + " doesn't equal to approp one");
                }
            }
        } else {
            return failed("number of retrived keys doesn't equal to putted one");
        }
        return passed();
    }
    
    public Result testAllKeys() {
        instance = new ActionMap();
        Object[] keys = new Object[KEY_NUM];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = new Object();
            instance.put(keys[i], new A());
        }
        ActionMap instance2 = new ActionMap();
        keys = new Object[KEY_NUM / 2];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = new Object();
            instance2.put(keys[i], new A());
        }
        if (instance.allKeys().length != KEY_NUM) {
            return failed("unexpected number of keys");
        }
        instance.setParent( instance2 );
        if (instance.allKeys().length != KEY_NUM + KEY_NUM / 2) {
            return failed("unexpected number of common keys");
        }
        return passed();
    }
    
    private static boolean contains(Object[] array, Object key) {
        boolean find = false;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals( key )) {
                find = true;
            }
        }
        return find;
    }

    
    class A extends AbstractAction {
        public void actionPerformed(ActionEvent arg0) {
        }
    }
    
    
    
}
