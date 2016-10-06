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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0029;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

class VCallback implements ObjectInputValidation {
    public static final int CALLBACKS_NUM = 10;

    private boolean[] called; //this field was static, but after implementing multi-threaded tests
    //it is passed explicitly for callbacks from one thread not to confuxe each other.
    private V v = null;

    private int priority;

    VCallback(V v, int p, boolean[] c) {
        this.v = v;
        priority = p;
        called = c;
    }

    public void validateObject() throws InvalidObjectException {
        //it is supposed that callbacks with lower priorities should be called
        // first
        synchronized (VCallback.class) {
            if (!v.restored()) {
                throw new InvalidObjectException(
                        "object not restored before callback");
            }
            for (int i = 0; i < CALLBACKS_NUM; ++i) {
                if (i <= priority && called[i]) {
                    throw new InvalidObjectException(
                            "callbacks priorities error");
                }
                if (i > priority && !called[i]) {
                    throw new InvalidObjectException(
                            "callbacks priorities error");
                }
            }
            called[priority] = true;
        }
    }
}

class V implements Serializable {
    public String s = "s";

    public int i = 0;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
    
        boolean[] called = new boolean[VCallback.CALLBACKS_NUM];  
        //initialized       with        'false'

        
        for (int i = 0; i < VCallback.CALLBACKS_NUM; ++i) {
            in.registerValidation(new VCallback(this, i, called), i);
        }
        in.defaultReadObject();
    }

    public boolean restored() {
        return i == 3 && "set".equals(s);
    }
}

public class writeObjectReadObject0029 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0029().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        if (!(o instanceof V)) {
            return fail("expected another value");
        }

        V v = (V) o;

        if (v.restored()) {
            return pass();
        }

        return fail("wrong values restored");

    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        V v = new V();

        v.s = "set";
        v.i = 3;

        waitAtBarrier();
        oos.writeObject(v);

        return pass();
    }
}

