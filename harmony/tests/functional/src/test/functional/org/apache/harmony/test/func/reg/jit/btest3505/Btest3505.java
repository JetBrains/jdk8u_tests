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

package org.apache.harmony.test.func.reg.jit.btest3505;

import org.apache.harmony.test.share.reg.RegressionTest;

import java.lang.reflect.Field;

public class Btest3505 extends RegressionTest{

    public static void main(String[] args) {
        System.exit(new Btest3505().test(args));
    }
    
    public int test(String [] args) {        
        try {
            Field f = Fvalue.class.getField("f");
            try {
                Fvalue value = new Fvalue();
                float obtainF = f.getFloat(value);
                if (obtainF != value.f) {
                    System.err.println("obtain: " + obtainF + " (" + value.f
                            + " expected)");
                    return fail();
                } else {
                    return pass();
                }
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } 
        return error();
    }
}

class Fvalue {
    public float f = 46f;
}
