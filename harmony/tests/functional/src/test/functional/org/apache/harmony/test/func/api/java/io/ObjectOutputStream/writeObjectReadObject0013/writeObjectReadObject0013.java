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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0013;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

class Peer implements Serializable {
    Peer peer;

    public int fake(boolean b) {
        return b ? 1 : 2;
    }
}

public class writeObjectReadObject0013 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0013().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        if (!(o instanceof Peer)) {
            return fail("wrong type of objects");
        }

        Peer p = (Peer) o;

        if (p.peer.peer == p) {
            return pass();
        }

        return fail("objects graph destroyed in (de)serialization");
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        Peer p = new Peer();
        p.peer = new Peer();
        p.peer.peer = p;

        waitAtBarrier();
        oos.writeObject(p);

        oos.close();
        return pass();
    }
}