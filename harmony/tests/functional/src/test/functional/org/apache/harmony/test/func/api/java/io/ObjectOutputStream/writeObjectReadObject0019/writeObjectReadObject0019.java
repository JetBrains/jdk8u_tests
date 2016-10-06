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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0019;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

class NoDefaultConstructor {
    private int i;

    private long l;

    private String s;

    NoDefaultConstructor(int i, long l, String s) {
        this.i = i;
        this.l = l;
        this.s = s;
    }
}

class SerializableNoDefaultConstructor extends NoDefaultConstructor implements
        Serializable {
    SerializableNoDefaultConstructor(int i, long l, String s) {
        super(i, l, s);
    }
}

public class writeObjectReadObject0019 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0019().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        try {
            waitAtBarrier();
            Object o = ois.readObject();
        } catch (InvalidClassException e) {
            return pass();
        }
        return fail("should be exception");
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        waitAtBarrier();
        oos.writeObject(new SerializableNoDefaultConstructor(1234, 23456,
                "qwertyj"));
        return pass();
    }
}

