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

public class Btest5789_child extends Btest5789_parent {
    private int privateField = 2;

    private int privateMethod() {
        System.out.println("Btest5789_child::privateMethod()");
        return 2;
    }

    protected int protectedMethod() {
        System.out.println("Btest5789_child::protectedMethod()");
        return 2;
    }

    public int publicMethod() {
        System.out.println("Btest5789_child::publicMethod()");
        return 2;
    }

    public int testMe() {
        if(super.testMe() == -1) return error();
        if (privateField != 2) return error();
        if (protectedField != 1) return error();
        if (publicField != 1) return error();

        if (privateMethod() != 2) return error();
        if (protectedMethod() != 2) return error();
        if (super.protectedMethod() != 0) return error();
        if (publicMethod() != 2) return error();
        return 1;
    }
};
