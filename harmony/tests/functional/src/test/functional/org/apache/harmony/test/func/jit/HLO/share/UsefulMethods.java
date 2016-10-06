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
package org.apache.harmony.test.func.jit.HLO.share;

/**
 */

/*
 * Created on 27.10.2005
 * 
 * Auxilary class for Jitrino HLO tests
 * 
 */

public class UsefulMethods {
    
    public static boolean checkStackTraceElement(StackTraceElement st, 
            String className, String methodName) {
        if (st.getClassName().equals(className) && 
                st.getMethodName().equals(methodName)) return true;
        else return false;
        
    }
    
    public static boolean checkStackTrace(StackTraceElement[] st, 
            String pattern) {
        for(int i=0; i<st.length; i++) {
            if (st[i].toString().indexOf(pattern)>=0) return true;
        }
        return false;
    }

}
