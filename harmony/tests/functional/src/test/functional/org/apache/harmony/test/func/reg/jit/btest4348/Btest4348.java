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

package org.apache.harmony.test.func.reg.jit.btest4348;

/*
 * (There are incorrect properties of Double and Float NaN object)
*/

import org.apache.harmony.test.share.reg.RegressionTest;

public class Btest4348 extends RegressionTest {
   
    public static void main(String[] args) {
         System.exit(new Btest4348().test());
    }
    
    public int test() {
        
        Double nd = new Double(Double.NaN);       
        boolean doubleIsNan = nd.isNaN();
        boolean doubleIsInfinite = nd.isInfinite();
        System.err.println("nd.isNan() = " + doubleIsNan + " (true expected)");
        System.err.println("nd.isInfinite() = " + doubleIsInfinite 
                + " (false expected)");
        System.err.println();
        
        Float fd = new Float(Float.NaN);       
        boolean floatIsNan = fd.isNaN();
        boolean floatIsInfinite = fd.isInfinite();
        System.err.println("fd.isNan() = " + floatIsNan + " (true expected)");
        System.err.println("fd.isInfinite() = " + floatIsInfinite 
                + " (false expected)");
        System.err.println();

        boolean doubleIsNan1 = Double.isNaN(Double.NaN);
        System.err.println("Double.isNaN(Double.NaN) = " + doubleIsNan1 
                + " (true expected)");
        System.err.println();

        boolean floatIsNan1 = Float.isNaN(Float.NaN);
        System.err.println("Float.isNaN(Float.NaN) = " + floatIsNan1 
                + " (true expected)");
        System.err.println();
        
        return (doubleIsNan && floatIsNan && doubleIsNan1 && floatIsNan1 
            && !doubleIsInfinite && !floatIsInfinite) 
                ? pass() : fail();
    }
}
