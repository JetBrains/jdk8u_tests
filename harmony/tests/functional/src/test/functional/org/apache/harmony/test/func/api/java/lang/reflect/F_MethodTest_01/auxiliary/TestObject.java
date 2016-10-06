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
 * Created on 01.03.2005
 * Last modification G.Seryakova
 * Last modified on 01.03.2005
 * 
 * Test Object for Method.
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_MethodTest_01.auxiliary;

/**
 * Test Object for Method.
 * 
 */
public class TestObject extends AbstractTestObject {
    
    public static final synchronized Object m1() {
        return new Object();
    }
    
    protected static Class m2(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }    
    
    public synchronized long m3(){
        return System.currentTimeMillis();
    }
    
    public int a1() {
        return 1;
    }
    
    protected final Object[] m4(Object[] objs){
        return objs;
    }
    
    synchronized strictfp void m5() {}
    
    private void m6(boolean b) {
        int i = 0;
    }
    
    private native int a3();
}
