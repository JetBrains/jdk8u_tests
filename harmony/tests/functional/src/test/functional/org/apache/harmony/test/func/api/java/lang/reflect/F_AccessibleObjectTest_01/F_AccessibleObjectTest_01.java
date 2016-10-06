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
 * Created on 25.08.2005
 * 
 * scenario
 */
package org.apache.harmony.test.func.api.java.lang.reflect.F_AccessibleObjectTest_01;

import org.apache.harmony.test.func.share.ScenarioTest;
import java.lang.reflect.AccessibleObject;

/**
 * Test for java.lang.reflect.AccessibleObject. 
 * 
 */
public class F_AccessibleObjectTest_01 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_AccessibleObjectTest_01().test(args));
    }

    private class myAccessibleObject extends AccessibleObject {
        public myAccessibleObject() {
            super();
        }
    }
    public int test() {
        AccessibleObject ao = new myAccessibleObject();
//        if(!ao.isAccessible()) {
//            log.info("AccessibleObject is " + ao.isAccessible());
//            return fail("New AccessibleObject has 'true' accessible flag");
//        }
        ao.setAccessible(true);
        if(ao.isAccessible()) {
            log.info("AccessibleObject is " + ao.isAccessible());
            return pass("OK");
        }
        return fail("Unknown reason");
    }
}