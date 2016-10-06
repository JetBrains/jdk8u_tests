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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0007;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

public class writeObjectReadObject0007 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0007().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        if (!(o instanceof LinkedHashSet)) {
            return fail("read object belongs to another class");
        }
        Set s = (Set) o;

        if (!s.contains("abcd")) {
            return fail("lost set entry");
        }

        if (!s.contains("\u1234")) {
            return fail("lost set entry");
        }

        if (!s.contains(new Long(4321))) {
            return fail("lost set entry");
        }

        if (!s.contains(new Boolean(false))) {
            return fail("lost set entry");
        }

        return pass();
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        Set s = new LinkedHashSet();
        s.add("abcd");
        s.add("\u1234");
        s.add(new Long(4321));
        s.add(new Boolean(false));
        waitAtBarrier();
        oos.writeObject(s);
        return pass();
    }
}