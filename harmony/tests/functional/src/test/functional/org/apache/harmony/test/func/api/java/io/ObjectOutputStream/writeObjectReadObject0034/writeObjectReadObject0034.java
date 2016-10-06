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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0034;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

class E implements Externalizable {
    public int i;

    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        i = in.readInt();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(i);
    }
}

public class writeObjectReadObject0034 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0034().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        Object o;
        try {
            waitAtBarrier();
            o = ois.readObject();
        } catch (java.io.InvalidClassException e) {
            return pass();
        }

        return fail("expected exception");

    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        waitAtBarrier();
        oos.writeObject(new E());
        return pass();
    }
}

