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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0015;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

class Transient implements Serializable {
    transient String trs = "aaa";

    transient int tri = 1234;

    String s = "bbb";

    int i = 2345;

    void fake(int i) {
    };
}

public class writeObjectReadObject0015 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0015().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        if (!(o instanceof Transient)) {
            return fail("wrong type of objects");
        }

        Transient t = (Transient) o;

        if (t.trs == null && "ccc".equals(t.s) && (0 == t.tri) && (3456 == t.i)) {
            return pass();
        }

        return fail("transient deserialization failed " + t.trs + " " + t.s
                + " " + t.tri + " " + t.i);
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {

        Transient t = new Transient();
        t.s = "ccc";
        t.trs = "ddd";
        t.i = 3456;
        t.tri = 4567;

        waitAtBarrier();
        oos.writeObject(t);

        return pass();
    }
}