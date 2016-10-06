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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0014;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

class SelfReference implements Serializable {
    SelfReference selfReference;

    public long fake(int i) {
        return (i == 3) ? 1 : 2;
    }
}

public class writeObjectReadObject0014 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0014().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        if (!(o instanceof SelfReference)) {
            return fail("wrong type of objects");
        }

        SelfReference sr = (SelfReference) o;

        if (sr.selfReference == sr) {
            return pass();
        }

        return fail("objects graph destroyed in (de)serialization");
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {

        SelfReference sr = new SelfReference();
        sr.selfReference = sr;

        waitAtBarrier();
        oos.writeObject(sr);
        return pass();
    }
}