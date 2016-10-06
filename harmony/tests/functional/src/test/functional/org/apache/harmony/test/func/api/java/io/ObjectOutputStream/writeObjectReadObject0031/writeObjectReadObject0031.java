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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0031;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

class C implements Serializable {
    public String s = "s";

    public static final C reference = new C("reference");

    public static final C notReference = new C("notReference");

    C(String s) {
        this.s = s;
    }

    private Object readResolve() {
        if (s.equals(reference.s)) {
            return reference;
        }
        return this;
    }
}

public class writeObjectReadObject0031 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0031().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        Object o1, o2;

        waitAtBarrier();
        o1 = ois.readObject();
        o2 = ois.readObject();

        if (!(o1 instanceof C) || !(o1 instanceof C)) {
            return fail("expected another value");
        }

        C c1 = (C) o1;
        C c2 = (C) o2;

        if (c1 == C.reference && c2.s.equals(C.notReference.s)
                && c2 != C.notReference) {
            return pass();
        }

        return fail("wrong values restored");

    }

    protected int testOut(ObjectOutputStream oos) throws IOException {

        waitAtBarrier();
        oos.writeObject(C.reference);
        oos.writeObject(C.notReference);

        return pass();
    }
}

