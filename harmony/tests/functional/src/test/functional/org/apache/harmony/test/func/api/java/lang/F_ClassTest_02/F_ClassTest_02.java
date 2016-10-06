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
 * Created on 08.04.2005
 * Last modification G.Seryakova
 * Last modified on 08.04.2005
 *  
 * 
 */
package org.apache.harmony.test.func.api.java.lang.F_ClassTest_02;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.lang.reflect.*;

/**
 */
public class F_ClassTest_02 extends ScenarioTest {
    
    public static void main(String[] args) {
        System.exit(new F_ClassTest_02().test(args));
    }

     public int test() {
         if (!task1()) {
            return fail("fail in task1.");
        }
         if (!task2()) {
            return fail("fail in task2.");
        }
         if (!task3()) {
            return fail("fail in task3.");
        }
        return pass();
    }
     
    private boolean task1() {
        Class[] clss = TestObject.class.getDeclaredClasses();
        for (int i = 0; i < clss.length; i++) {
            StringBuffer res = new StringBuffer();
            int mdf = clss[i].getModifiers();
            if (Modifier.isInterface(mdf)) {
                res.append("interface ");
            } else {
                res.append("class ");
            }
            res.append(clss[i].getDeclaringClass().getName());
            if (Modifier.isInterface(mdf)) {
                res.append("$Interface1");
            } else {
                res.append("$Class1");
            }
            if (!clss[i].toString().trim().equals(res.toString().trim())) {
                log.info("String representatin isn't expected. ");
                log.info("Must be " + res.toString() + " ");
                log.info("But is " + res.toString());
                return false;
            }            
        }
        return true;
    }
    
    private boolean task2() {
        boolean res = true;        
        if (!boolean.class.toString().equals("boolean")) {
            log.info("Fail for boolean.");
            res = false;
        }
        if (!byte.class.toString().equals("byte")) {
            log.info("Fail for byte.");
            res = false;
        }
        if (!char.class.toString().equals("char")) {
            log.info("Fail for char.");
            res = false;
        }
        if (!double.class.toString().equals("double")) {
            log.info("Fail for double.");
            res = false;
        }
        if (!float.class.toString().equals("float")) {
            log.info("Fail for float.");
            res = false;
        }
        if (!int.class.toString().equals("int")) {
            log.info("Fail for int.");
            res = false;
        }
        if (!long.class.toString().equals("long")) {
            log.info("Fail for long.");
            res = false;
        }
        if (!short.class.toString().equals("short")) {
            log.info("Fail for short.");
            res = false;
        }
        if (!void.class.toString().equals("void")) {
            log.info("Fail for void.");
            res = false;
        }
        return res;
    }
    
    private boolean task3() {
        boolean res = true;
        if (!int[].class.toString().equals("class [I")) {
            log.info("Fail for array int[]. Result is " + int[].class.toString());
            res = false;
        }
        if (!long[][][][][].class.toString().equals("class [[[[[J")) {
            log.info("Fail for array long[][][][][]. Result is " + long[][][][][].class.toString());
            res = false;
        }
        if (!String[].class.toString().equals("class [Ljava.lang.String;")) {
            log.info("Fail for array String[]. Result is " + String[].class.toString());
            res = false;
        }
        if (!Object[][][][][][][].class.toString().equals("class [[[[[[[Ljava.lang.Object;")) {
            log.info("Fail for array Object[][][][][][][]. Result is " + Object[][][][][][][].class.toString());
            res = false;
        }
        return res;
    }
}

class TestObject {
    class Class1 {}
    interface Interface1 {}
}