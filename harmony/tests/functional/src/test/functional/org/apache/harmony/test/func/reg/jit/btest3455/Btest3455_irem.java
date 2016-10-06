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
 
package org.apache.harmony.test.func.reg.jit.btest3455;

public class Btest3455_irem {   

    public static void main(String[] args) throws Exception {
        int iMin = Integer.MIN_VALUE;
        int iM1 = -1;
        int iRes;
        System.err.println(
                "Verify: Integer.MIN_VALUE % -1 == Integer.MIN_VALUE");
        try {
            iRes = iMin % iM1;
            if (iRes == 0) {
                System.err.println("PASSED!");
            } else {
                System.err.println("FAILED: " + iRes);
                System.exit(-99);
            }
        } catch (Throwable e) {
            System.err.println("Unexpected error was thrown:");
            e.printStackTrace();   
        }   
    }
}
