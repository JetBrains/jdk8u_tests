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
 * Created on 24.02.2005
 * Last modification G.Seryakova
 * Last modified on 24.02.2005
 * 
 * Test Object for Constructor.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_ConstructorTest_01.auxiliary;

/**
 * Test Object for Constructor.
 * 
 */
public class TestObject {
    int n;
    
    public TestObject() {
        n = 0;
    }
    
    protected TestObject(int m) {
        n = m;
    }
    
    private TestObject(short m, short k) {
        n = m + k;
    }
    
    TestObject(byte m, long k) throws IllegalArgumentException {
        try {
            n = m + Integer.parseInt("" + k);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Second argument is illegal.");
        }
    }
}
