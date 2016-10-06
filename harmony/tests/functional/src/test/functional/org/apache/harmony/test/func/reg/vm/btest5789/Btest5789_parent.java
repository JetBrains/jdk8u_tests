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
package org.apache.harmony.test.func.reg.vm.btest5789;

public class Btest5789_parent extends Btest5789_grandParent {
    private int privateField = 1;
    protected int protectedField = 1;
    public int publicField = 1;
    
    private int privateMethod() {
        System.out.println("Btest5789_parent::privateMethod()");
        return 1;
    }

    public int publicMethod() {
        System.out.println("Btest5789_parent::publicMethod()");
        return 1;
    }

    public int testMe() {
        if(super.testMe() == -1) return error();
        if (privateField != 1) return error();
        if (protectedField != 1) return error();
        if (publicField != 1) return error();

        if (privateMethod() != 1) return error();
        if (protectedMethod() != 2) return error();
        if (publicMethod() != 2) return error();
        return 1;
    }

    protected int error() {
        System.out.println("Error!!!!\n");
        return -1;
    }
};
