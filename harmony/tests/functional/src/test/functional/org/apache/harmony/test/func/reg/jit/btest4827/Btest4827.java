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

package org.apache.harmony.test.func.reg.jit.btest4827;

import org.apache.harmony.test.share.reg.RegressionTest;

import java.awt.AWTKeyStroke;
import java.awt.event.KeyEvent;
import java.io.PrintStream;

public class Btest4827 extends RegressionTest {

    public static void main(String[] args) {
        System.exit(new Btest4827().test());
    }
    
    public int test() {
        neg cl = new neg();
        boolean ret = true;
        
        double d1 = cl.testD1();      
        double d2 = cl.testD2();             
        if (Double.doubleToLongBits(d1) != Double.doubleToLongBits(d2)) {
             System.err.println("dneg test fails: " 
                     + Double.doubleToLongBits(d1) + " " + d1 + " " 
                    + Double.doubleToLongBits(d2) + " " + d2);
             ret = false;
        } else {
             System.err.println("dneg test passes");
        }

        float f1 = cl.testF1();
        float f2 = cl.testF2();
        if (Float.floatToIntBits(f1) != Float.floatToIntBits(f2)) {
             System.err.println("fneg test fails: " + Float.floatToIntBits(f1)
                     + " " + f1 + " " + Float.floatToIntBits(f2) + " " + f2);
             ret = false;
        } else {
             System.err.println("fneg test passes");
        }
        
        return ret ? pass() : fail();
    }    
}
