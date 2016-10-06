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
 * Created on 15.02.2005
 * Last modification G.Seryakova
 * Last modified on 15.02.2005
 *  
 * 
 */
package org.apache.harmony.test.func.api.java.lang.F_NumberTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 */
public class F_NumberTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_NumberTest_01().test(args));
    }

    public int test() {
        Number ns[] = new Number[4];
        ns[0] = new Float("12.5");
        ns[1] = new Integer(2);
        ns[2] = new Double("5.75");
        ns[3] = new MyNumber(Byte.parseByte("4"));
        
        double res = ns[0].doubleValue() + ns[1].doubleValue() + ns[2].doubleValue() + ns[3].doubleValue();
        
        log.info("" + res);
        
        if (res != 24.25D) {
            return fail("result must be 24.25");
        }
        return pass();
    }
}
class MyNumber extends Number {
    private byte value;
    
    public MyNumber (byte n) {
        super();
        value = n;
    }
    
    public int intValue() {
        return value;
    }
    
    public long longValue() {
        return value;
    }
    
    public byte byteValue() {
        return value;
    }
    
    public short shortValue() {
        return value;
    }
    
    public double doubleValue() {
        return value;
    }
    
    public float floatValue() {
        return value;
    }
}
