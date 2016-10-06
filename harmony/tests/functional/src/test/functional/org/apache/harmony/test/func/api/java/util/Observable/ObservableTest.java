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

package org.apache.harmony.test.func.api.java.util.Observable;

import java.util.Observable;
import java.util.Observer;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * Mar 16, 2006
 */
public class ObservableTest extends MultiCase {

    private class MyObserver implements Observer {
        public int id;

        public int notifiers = 0;

        public boolean validObj = false;

        public Object obj;

        public Observable observable;

        public boolean validObservable = false;

        public MyObserver(int id, Object obj, Observable observable) {
            this.id = id;
            this.obj = obj;
            this.observable = observable;
        }

        public void update(Observable data, Object obj) {
            notifiers++;
            validObj = obj == this.obj;
            validObservable = data == observable;
            ((MyObservable) data).update();
        }

        public boolean equals(Object obj) {
            if (obj instanceof MyObserver) {
                return ((MyObserver) obj).id == this.id;
            }
            return false;
        }

    }

    private class MyObservable extends Observable {
        public void setChanged() {
            super.setChanged();
        }

        public void clearChanged() {
            super.clearChanged();
        }

        int count = 0;

        int max;

        public MyObservable(int max) {
            this.max = max;
        }

        synchronized public void update() {
            if (++count >= max) {
                this.notifyAll();
            }
        }
    }

    public Result testNotify_Obj() {
        final MyObservable observable = new MyObservable(3);
        MyObserver[] observers = new MyObserver[3];
        final Object obj = new Object();
        for (int i = 0; i < observers.length; i++) {
            observers[i] = new MyObserver(i, obj, observable);
            observable.addObserver(observers[i]);
        }

        observable.clearChanged();
        observable.notifyObservers(obj);

        for (int i = 0; i < observers.length; i++) {
            assertFalse(observers[i].validObj);
            assertFalse(observers[i].validObservable);
            assertEquals(observers[i].notifiers, 0);
        }
        
        observable.setChanged();
        observable.notifyObservers(obj);

        for (int i = 0; i < observers.length; i++) {
            assertTrue(observers[i].validObj);
            assertTrue(observers[i].validObservable);
            assertEquals(observers[i].notifiers, 1);
        }

        return result();
    }

    public Result testNotify() {
        final MyObservable observable = new MyObservable(3);
        MyObserver[] observers = new MyObserver[3];

        for (int i = 0; i < observers.length; i++) {
            observers[i] = new MyObserver(i, null, observable);
            observable.addObserver(observers[i]);
        }

        observable.clearChanged();
        observable.notifyObservers();
        
        for (int i = 0; i < observers.length; i++) {
            assertFalse(observers[i].validObj);
            assertFalse(observers[i].validObservable);
            assertEquals(observers[i].notifiers, 0);
        }

        observable.setChanged();
        observable.notifyObservers();
        
        for (int i = 0; i < observers.length; i++) {
            assertTrue(observers[i].validObj);
            assertTrue(observers[i].validObservable);
            assertEquals(observers[i].notifiers, 1);
        }

        return result();
    }

    public Result testChangedState() {
        MyObservable observable = new MyObservable(3);

        assertFalse(observable.hasChanged());
        assertFalse(observable.hasChanged());

        observable.setChanged();

        assertTrue(observable.hasChanged());
        assertTrue(observable.hasChanged());

        observable.clearChanged();

        assertFalse(observable.hasChanged());
        assertFalse(observable.hasChanged());

        return result();
    }

    public static void main(String[] args) {
        System.exit(new ObservableTest().test(args));
    }

}
