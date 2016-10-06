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
package org.apache.harmony.test.func.jit.HLO.peel.TryCatch5;

import java.util.Vector;

import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 22.06.2006
 */

public class TryCatch5 extends Test {
    
    public static void main(String[] args) {
        System.exit((new TryCatch5()).test(args));
    }

    public int test() {
        log.info("Start TryCatch5 test...");
        int limit = 90000;
        Vector vec = new Vector(limit);
        for (int i=0; i<limit; i+=3) {
            vec.add(i, new Byte( (byte) 1));
            vec.add(i+1, null);
            vec.add(i+2, "str");
        }
        int k = 0;
        byte arr[] = new byte[limit];
        for (int i=0; i<limit; i++) {
            k = i;
            try {
                arr[i] = ((Byte) vec.get(i)).byteValue();
            } catch (NullPointerException npe) {
                do {
                    arr[i] = (byte) 2;
                } while (i<k);
            } catch (ClassCastException cce) {
                do {
                    arr[i] = (byte) 3;
                } while (i<k);
            }
        }
        for (int i=0; i<limit; i+=3) {
            if ((arr[i+1] != 2) || (arr[i+2] != 3)) 
                return fail("TEST FAILED: result array was corrupted.\nCheck " 
                    + "if loop optimization was performed incorrectly.");
        }
        return pass();
    }
}


