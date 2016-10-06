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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0012;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

class C implements Serializable {
    Integer i;

    public int fake(int j) {
        return j;
    }
}

public class writeObjectReadObject0012 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0012().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        Object o1, o2;
        waitAtBarrier();
        o1 = ois.readObject();
        o2 = ois.readObject();

        if (!(o1 instanceof C && o2 instanceof C)) {
            return fail("wrong type of objects");
        }

        if (((C) o1).i == ((C) o2).i) {
            return pass();
        }

        return fail("objects graph destroyed in (de)serialization");
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        C c1 = new C();
        C c2 = new C();

        c1.i = new Integer(3);
        c2.i = c1.i;

        waitAtBarrier();
        oos.writeObject(c1);
        oos.writeObject(c2);

        return pass();
    }
}