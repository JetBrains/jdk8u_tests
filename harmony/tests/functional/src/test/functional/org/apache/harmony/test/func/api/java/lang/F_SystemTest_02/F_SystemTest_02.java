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
/*
 * Created on 16.01.2005
 * Last modification G.Seryakova
 * Last modified on 16.01.2005
 * 
 * Test for system properties. For classes: System, Integer, Boolean.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.F_SystemTest_02;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.util.*;

/**
 * Test for system properties. For classes: System, Integer, Boolean.
 * 
 */
public class F_SystemTest_02 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_SystemTest_02().test(args));
    }

    public int test() {
        Properties prop = System.getProperties();
        prop.setProperty("1SystemTest_02", "true");
        System.setProperties(prop);
        for (Enumeration e = prop.propertyNames(); e.hasMoreElements();) {
            String elem = (String)e.nextElement();
            if (System.getProperty(elem) == null) {
                return fail("After resetting properties don't contain expected element - "
                    + elem);
            }
        }
        System.setProperty("2SystemTest_02", "false");
        System.setProperty("3SystemTest_02", "1");
        System.setProperty("4SystemTest_02", Integer
            .toString(Integer.MAX_VALUE));
        System.setProperty("5SystemTest_02", Integer
            .toString(Integer.MIN_VALUE));
        System.setProperty("6SystemTest_02", "2147483648");

        if (!System.getProperty("6SystemTest_02", "0").equals("2147483648")) {
            return fail("getProperty(6SystemTest_02) don't return expected element - 2147483648: return - "
                + System.getProperty("5SystemTest_02"));
        }
        if (!System.getProperty("7SystemTest_02", "0").equals("0")) {
            return fail("getProperty(6SystemTest_02) don't return expected element - 2147483648: return - "
                + System.getProperty("5SystemTest_02"));
        }

        if (Boolean.getBoolean("1SystemTest_02")
            && !(Boolean.getBoolean("2SystemTest_02"))
            && !(Boolean.getBoolean("3SystemTest_02"))) {
            int arrInt[] = new int[5];
//          expect 0
            arrInt[0] = Integer.getInteger("2SystemTest_02", 0).intValue(); 
//          expect 1
            arrInt[1] = Integer.getInteger("3SystemTest_02").intValue(); 
//          expect 2147483647
            arrInt[2] = Integer.getInteger("4SystemTest_02").intValue(); 
//          expect -2147483648
            arrInt[3] = Integer.getInteger("5SystemTest_02", new Integer(0)).intValue(); 
//          expect 0
            arrInt[4] = Integer.getInteger("6SystemTest_02", new Integer(0)).intValue(); // expect 0
            if (-(arrInt[3] + arrInt[1]) + arrInt[0] + arrInt[4] != arrInt[2]) {
                return fail("-(" +arrInt[3] + " + " + arrInt[1] + ") + " + arrInt[0] + " + " + arrInt[4] + " != " + arrInt[2]);
            }
        } else {
            return fail("getBoolean(1SystemTest_02) return - "
                + Boolean.getBoolean("1SystemTest_02")
                + " except true or getBoolean(2SystemTest_02) return "
                + Boolean.getBoolean("2SystemTest_02")
                + "except false or getBoolean(3SystemTest_02) return "
                + Boolean.getBoolean("3SystemTest_02") + "except false");
        }

        return pass();
    }
}