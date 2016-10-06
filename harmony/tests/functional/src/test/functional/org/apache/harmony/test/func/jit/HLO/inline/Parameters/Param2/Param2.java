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
package org.apache.harmony.test.func.jit.HLO.inline.Parameters.Param2;

import java.util.HashMap;

import org.apache.harmony.test.func.jit.HLO.share.UsefulMethods;
import org.apache.harmony.share.Test;

/**
 */

/*
 * Created on 27.10.2005
 * 
 * Jitrino HLO test
 * 
 */

public class Param2 extends Test {
    
    public static void main(String[] args) {
        System.exit((new Param2()).test(args));
    }
    
    public int test() {
        log.info("Start Param2 test...");
        float[] array = new float[1000000];
        HashMap table = new HashMap(1000000);
        for (int k=0; k<array.length; k++)
            array[k] = k;
        try {
            for(int a=0; a<100; a++) 
                for(int b=0; b<100; b++) 
                    for(int c=0; c<100; c++) 
                        Called.inlineMethod(array, table);
            return fail("TEST FAILED: " +
                    "ArrayIndexOutOfBoundsException was expected");
        } catch (ArrayIndexOutOfBoundsException e) {
            float check = ((Float)table.get(new Integer(374))).floatValue();
            if (check != 374.0f) return fail("TEST FAILED: " + check);
            if (!UsefulMethods.checkStackTraceElement(e.getStackTrace()[0], 
                    Called.class.getName(), "inlineMethod")) {
                log.add("Stack Trace: " + e);
                log.add(e);
                return fail("TEST FAILED: incorrect stack trace");
            }
            return pass();
        } catch (Throwable e) {
            log.add(e);
            return fail("TEST FAILED: unexpected exception " + e);
        }
    }
}


class Called {
    
    static Bound b = new Bound();
    
    static void inlineMethod(float[] arr, HashMap map) {
        for (int j=0; j<b.bound; j++) {
            map.put(new Integer(j), new Float(arr[j]));
            if (j==999999) b.bound = 1000001;
        }          
    }
}


class Bound {
    public int bound = 1000000;
}

