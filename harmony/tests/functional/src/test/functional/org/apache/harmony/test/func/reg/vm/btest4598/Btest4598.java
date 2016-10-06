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
package org.apache.harmony.test.func.reg.vm.btest4598;

import java.util.logging.Logger;
import org.apache.harmony.test.share.reg.RegressionTest;

/**
 */
public class Btest4598 extends RegressionTest {

    public static void main(String[] args) {
        System.exit(new Btest4598().test(Logger.global, args));
    }

    public static void foo() {
        throw new MyException((byte) 1, (short) 2, 4, 8L);
    }

    public int test(Logger logger, String[] args) {

        MyException t = null;
        try {
            foo();
        } catch (MyException e) {
            t = e;
        }

        if (t != null) {
            t.printStackTrace();

            if (t.b == 1 && t.s == 2 && t.i == 4 && t.l == 8) {
                return pass();
            } else {
                return fail();
            }
        }
        return fail();

    }
}

class MyException extends RuntimeException {
    public byte b;

    public short s;

    public int i;

    public long l;

    public MyException(byte _b, short _s, int _i, long _l) {
        b = _b;
        s = _s;
        i = _i;
        l = _l;
    }

    public String toString() {
        return super.toString() + ", b: " + b + ", s: " + s + ", i: " + i
                + ", l: " + l;
    }
}
