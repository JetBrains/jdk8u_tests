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
package org.apache.harmony.test.func.reg.vm.btest4673;

public class Test4673 {

    public long call(long i) { return i;}
    public double call(double l) {return l;}
    public float call(float l) {return l;}

    public synchronized native float foo2(float l);
    public synchronized native long foo(long l);
    public synchronized native double foo1(double l);

    
    public static void main(String argv[]) {
            System.loadLibrary("Btest4673");
            Test4673 t = new Test4673();
            System.err.println(t.foo2(1.1f));
            System.err.println(t.foo(10L)); 
            System.err.println(t.foo1(0.1));
    }


}
