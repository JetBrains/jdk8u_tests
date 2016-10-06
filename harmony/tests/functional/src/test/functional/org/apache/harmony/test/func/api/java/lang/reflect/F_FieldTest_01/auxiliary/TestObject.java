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
 * Created on 25.02.2005
 * Last modification G.Seryakova
 * Last modified on 25.02.2005
 * 
 * Test Object for Field.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_FieldTest_01.auxiliary;

/**
 * Test Object for Field.
 * 
 */
public class TestObject {
    public static final int F = 1;
    protected static volatile int f1;
    protected static boolean f2;
    
    private final transient char c1;
    private final int c2;
    volatile byte f3;
    private char f4;
    private transient double f5;
    float f6;
    public long f7;
    private transient volatile short f8;
    protected Object f9;
    

    public TestObject() {
        c1 = 'a';
        c2 = 2;
        f2 = false;
        f4 = 'c';
        f5 = f6 = f7 = f1 = f8 = f3 = 0;
        f9 = new Object();
    }
}
