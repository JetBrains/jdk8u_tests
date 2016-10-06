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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0027;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

class C implements Serializable {
    public String s1 = "s1", s2 = "s2";

    public int i1 = 0, i2 = 0;

    boolean b;

    byte by;

    char c;

    double d;

    float f;

    long l;

    short sh;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(s1 + "out1");
        out.writeUTF(s2 + "out2");
        out.writeInt(1);
        out.writeInt(1);
        out.writeBoolean(true);
        out.writeByte(123);
        out.writeChar('f');
        out.writeDouble(3.3);
        out.writeFloat((float) 4.4);
        out.writeLong(888L);
        out.writeShort(222);
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        s1 = in.readUTF();
        s2 = in.readUTF();
        i1 = in.readInt();
        i2 = in.readInt();
        b = in.readBoolean();
        by = in.readByte();
        c = in.readChar();
        d = in.readDouble();
        f = in.readFloat();
        l = in.readLong();
        sh = in.readShort();

        s2 = "in2";
        i2 = -1;
    }

}

public class writeObjectReadObject0027 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0027().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        if (!(o instanceof C)) {
            return fail("expected another value");
        }

        C c = (C) o;

        if ("s1out1".equals(c.s1) && "in2".equals(c.s2) && c.i1 == 1
                && c.i2 == -1 && c.b == true && c.by == 123 && c.c == 'f'
                && c.d == 3.3 && c.f == (float) 4.4 && c.l == 888
                && c.sh == 222) {
            return pass();
        }

        return fail("wrong values restored " + c.s1 + c.s2 + c.i1 + c.i2 + c.b
                + c.by + c.c + c.d + c.f + c.l + c.sh);

    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        waitAtBarrier();
        oos.writeObject(new C());

        return pass();
    }
}

