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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0020;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

class Static implements Serializable {
    static int si = 1;

    int i = 11;

    public boolean fake() {
        return si == i;
    }
}

public class writeObjectReadObject0020 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0020().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        Static s = new Static();
        Static.si = 3;
        s.i = 3;

        waitAtBarrier();
        Object o = ois.readObject();

        if (!(o instanceof Static)) {
            return fail("wrong object type");
        }

        s = (Static) o;
        if (Static.si == 3 && s.i == 22) {
            return pass();
        }

        return fail("wrong values restored");
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        Static s = new Static();
        Static.si = 2;
        s.i = 22;

        waitAtBarrier();

        oos.writeObject(s);

        return pass();
    }
}

