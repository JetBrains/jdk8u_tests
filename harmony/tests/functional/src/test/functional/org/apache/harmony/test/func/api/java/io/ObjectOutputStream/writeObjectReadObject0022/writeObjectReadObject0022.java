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
package org.apache.harmony.test.func.api.java.io.ObjectOutputStream.writeObjectReadObject0022;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.harmony.test.func.api.java.io.ObjectOutputStream.share.SerializationTestFramework;

public class writeObjectReadObject0022 extends SerializationTestFramework {
    public static void main(String[] args) {
        System.exit(new writeObjectReadObject0022().test(args));
    }

    protected int testIn(ObjectInputStream ois) throws IOException,
            ClassNotFoundException {
        waitAtBarrier();
        Object o = ois.readObject();

        int[][] ia = (int[][]) o;
        if (ia.length == 3 && ia[0].length == 1 && ia[1].length == 2
                && ia[2].length == 4 && ia[0][0] == 123 && ia[1][0] == 23
                && ia[1][1] == 34 && ia[2][0] == 2 && ia[2][1] == 4
                && ia[2][2] == 6 && ia[2][3] == 8) {
            return pass();
        }

        return fail("wrong values restored");
    }

    protected int testOut(ObjectOutputStream oos) throws IOException {
        int[][] ia = { { 123 }, { 23, 34 }, { 2, 4, 6, 8 } };

        oos.writeObject(ia);

        return pass();
    }
}

