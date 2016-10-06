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
 
package org.apache.harmony.test.func.reg.jit.btest4513;

/*
 * This is a testcase from "
 * Additional Comment #6 From Mikhail Fursov 2005-09-26 18:34" 
 */

import java.util.List;

public class Btest4513_case3 {   
    
    public static void main(String[] args) throws Exception  {    
        System.err.println("Start Btest4513_case3 ...");        
        try {
            foo((List) null);
            System.err.println("NullPointer was not thrown as expected!");
            System.err.println("FAILED!");
        } catch (NullPointerException e) {
            System.err.println("NullPointer was thrown as expected!");
            System.err.println("PASSED!");
        }

    }

    public static void foo(List list) throws Exception {
        int i = list.size();
    }

}
