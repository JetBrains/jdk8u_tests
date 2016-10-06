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
 * Created on 07.04.2005
 * Last modification G.Seryakova 
 * Last modified on 08.04.2005 
 * 
 * Class for F_ReflectionTest_05 test.
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_ReflectionTest_05.auxiliary;

/**
 * Class for F_ReflectionTest_05 test.
 * 
 */
public class ReflectionTestObject {    
    public static void method1(int n) {
        if (n == 0) {
            throw new NullPointerException();
        } else if (n < 0) {
            throw new IllegalArgumentException();
        } else {
            throw new NumberFormatException();
        }
    }
}
