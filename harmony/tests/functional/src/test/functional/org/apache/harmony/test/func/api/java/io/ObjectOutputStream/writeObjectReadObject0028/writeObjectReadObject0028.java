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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0028;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

class C implements Serializable {
    public String s1 = "s1", s2 = "s2";

    public int i1 = 0, i2 = 0;

    private void writeObject(ObjectOutputStream out) throws IOException {
        s1 = s1 + "out1";
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        i2 = -1;
    }

}

public class writeObjectReadObject0028 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0028().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        if (!(o instanceof C)) {
            return fail("expected another value");
        }

        C c = (C) o;

        if ("set1out1".equals(c.s1) && "set2".equals(c.s2) && c.i1 == 2
                && c.i2 == -1) {
            return pass();
        }

        return fail("wrong values restored");

    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        C c = new C();

        c.s1 = "set1";
        c.s2 = "set2";
        c.i1 = 2;
        c.i2 = 3;

        waitAtBarrier();
        oos.writeObject(c);

        oos.close();
        return pass();
    }
}

