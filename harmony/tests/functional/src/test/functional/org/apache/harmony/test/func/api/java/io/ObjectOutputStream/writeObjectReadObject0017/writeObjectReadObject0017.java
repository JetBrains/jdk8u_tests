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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0017;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

public class writeObjectReadObject0017 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0017().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        if (!(o instanceof S2)) {
            return fail("wrong type of objects");
        }

        S2 s = (S2) o;

        if ("c".equals(s.getS()) && s.getI() == 3) {
            return pass();
        }

        return fail("hierarchy deserialization failed, values : " + s.getS()
                + " " + s.getI());
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        S2 s = new S2();
        s.setS("c");

        waitAtBarrier();
        oos.writeObject(s);
        return pass();
    }
}

class Ns1 {
    protected int i;

    public int getI() {
        return i;
    };

    Ns1() {
        i = 1;
    }
}

class Ns2 extends Ns1 {
    Ns2() {
        i = 2;
    }
}

class Ns3 extends Ns2 {
    Ns3() {
        i = 3;
    }
}

class S1 extends Ns3 implements Serializable {
    protected String s;

    public String getS() {
        return s;
    };

    public void setS(String s) {
        this.s = s;
    };

    S1() {
        s = "a";
    }
}

class S2 extends S1 {
    S2() {
        s = "b";
    }
}