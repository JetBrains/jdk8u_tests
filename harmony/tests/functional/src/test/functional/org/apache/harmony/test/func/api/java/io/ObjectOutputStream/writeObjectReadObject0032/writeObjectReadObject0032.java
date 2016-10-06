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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0032;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

class C implements Serializable {
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        ObjectInputStream.GetField fields = in.readFields();

        if ("jjj".equals(fields.get("s", "")) && fields.get("i", 3) == 2
                && fields.get("b", false) == true
                && fields.get("by", (byte) 0) == 123
                && fields.get("c", 'l') == 'h' && fields.get("d", 0.0) == 5.5
                && fields.get("f", (float) 1.1) == (float) 6.7
                && fields.get("l", 0L) == 124L
                && fields.get("sh", (short) 0) == 222) {
            return;
        }
        throw new IOException("wrong values restored");

    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        ObjectOutputStream.PutField fields = out.putFields();

        fields.put("d", 5.5);
        fields.put("f", (float) 6.7);
        fields.put("l", 124L);
        fields.put("sh", (short) 222);
        fields.put("s", "jjj");
        fields.put("i", 2);
        fields.put("b", true);
        fields.put("by", (byte) 123);
        fields.put("c", 'h');

        out.writeFields();
    }

    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("s", String.class),
            new ObjectStreamField("i", Integer.TYPE),
            new ObjectStreamField("b", Boolean.TYPE),
            new ObjectStreamField("by", Byte.TYPE),
            new ObjectStreamField("c", Character.TYPE),
            new ObjectStreamField("d", Double.TYPE),
            new ObjectStreamField("f", Float.TYPE),
            new ObjectStreamField("l", Long.TYPE),
            new ObjectStreamField("sh", Short.TYPE) };
}

public class writeObjectReadObject0032 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0032().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();

        Object o = ois.readObject();

        return pass();

    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        waitAtBarrier();
        oos.writeObject(new C());

        return pass();
    }
}

