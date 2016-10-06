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
 * Created on 18.02.2005
 * Last modification G.Seryakova
 * Last modified on 18.02.2005
 *  
 * 
 */
package org.apache.harmony.test.func.api.java.lang.F_ObjectTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 */
public class F_ObjectTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_ObjectTest_01().test(args));
    }

    class MyObject1 {
        int i;

        MyObject1(int n) {
            i = n;
        }

        public Object getClone() throws CloneNotSupportedException {
            return clone();
        }
    }

    class MyObject2 implements Cloneable {
        boolean b;

        int i;

        MyObject2(boolean p1, int p2) {
            b = p1;
            i = p2;
        }

        public MyObject2 getClone() throws CloneNotSupportedException {
            return (MyObject2) clone();
        }
    }

    class MyObject3 implements Cloneable {
        int i;

        MyObject1 obj;

        MyObject3(int p1, int p2) {
            i = p1;
            obj = new MyObject1(p2);
        }

        public MyObject3 getClone() throws CloneNotSupportedException {
            return (MyObject3) clone();
        }
    }

    public int test() {
        MyObject1 obj1 = new MyObject1(10);
        try {
            obj1.getClone();
            return fail("Object doesn't implement Cloneable, but ivocation of clone() doesn't throw exception.");
        } catch (CloneNotSupportedException e) {
            //            expected exception.
        }

        MyObject2 obj2 = new MyObject2(true, 20);
        MyObject2 obj2Clon = null;
        try {
            obj2Clon = obj2.getClone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return fail("Object implements Cloneable.");
        }
        if ((obj2.b != obj2Clon.b) || (obj2.i != obj2Clon.i)
                || (obj2.equals(obj2Clon))) {
            return fail("((" + obj2.b + " != " + obj2Clon.b + ") || (" + obj2.i
                    + " != " + obj2Clon.i + ") || (" + obj2.hashCode() + " == "
                    + obj2Clon.hashCode() + "))");
        }

        MyObject3 obj3 = new MyObject3(30, 31);
        MyObject3 obj3Clon = null;
        try {
            obj3Clon = obj3.getClone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return fail("Object implements Cloneable.");
        }
        if (((obj3.i != obj3Clon.i)) || (obj2.equals(obj2Clon))
                || (!obj3.obj.equals(obj3Clon.obj))) {
            return fail("((" + obj3.i + " != " + obj3Clon.i + ") || ("
                    + obj3.hashCode() + " == " + obj3Clon.hashCode() + ") || ("
                    + obj3.obj.hashCode() + " != " + obj3Clon.obj.hashCode()
                    + "))");
        }

        return pass();
    }
}