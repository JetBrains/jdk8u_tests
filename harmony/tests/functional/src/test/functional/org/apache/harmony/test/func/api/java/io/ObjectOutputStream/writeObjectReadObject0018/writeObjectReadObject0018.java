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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0018;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

class SVer implements Serializable {
    private static final long serialVersionUID = -5242478962404715464L;
    
    int i;
    long l;
    String s;
}

public class writeObjectReadObject0018 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0018().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        if (!(o instanceof SVer)) {
            return fail("wrong type of object");
        }

        SVer s = (SVer) o;

        if ("go".equals(s.s) && s.i == 1234 && s.l == 23456) {
            return pass();
        }

        return fail("serialVersionUID deserialization failed");
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
            SVer s = new SVer();
            s.s = "go";
            s.i = 1234;
            s.l = 23456;

            waitAtBarrier();
            oos.writeObject(s);

        return pass();
    }
}

