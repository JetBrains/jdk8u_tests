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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0003;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

public class writeObjectReadObject0003 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0003().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        if (!(o instanceof HashMap)) {
            return fail("read object belongs to another class");
        }
        Map m = (Map) o;

        if (!"abcd".equals(m.get("a"))) {
            return fail("expected another value");
        }

        if (!(new Long(4321)).equals(m.get("\u1234"))) {
            return fail("expected another value");
        }

        if (!(new Boolean(false)).equals(m.get(new Integer(5)))) {
            return fail("expected another value");
        }

        if (m.get("key1") != m.get("key2")) {
            return fail("reference graph destroyed after serialization");
        }

        return pass();
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        Map m = new HashMap();
        Object o = new Float(1.0);

        m.put("a", "abcd");
        m.put("\u1234", new Long(4321));
        m.put(new Integer(5), new Boolean(false));
        m.put("key1", o);
        m.put("key2", o);

        waitAtBarrier();
        oos.writeObject(m);
        return pass();
    }
}