/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Nikolay Bannikov
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.kernel.object;

import org.apache.harmony.test.reliability.share.Test;


/**
 * Goal: find resource leaks or intermittent failures caused by Object.getClass() / hashCode() / equals() operations.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of iterations to call/initialize in a cycle
 *       2. Excutes a cycle of param[0] iterations, on each iteration:
 *            get the runtime class of an object
 *          check result using hashCode() / equals()
 */

public class ObjectGetclassHashcodeTest extends Test {

    public String packageName = "org.apache.harmony.test.reliability.api.kernel.object";
    public String classPath = "";
    
    public int NUMBER_OF_ITERATIONS = 100000;


    public static void main(String[] args) {
        System.exit(new ObjectGetclassHashcodeTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);

        classPath = packageName + "." +"ObjectGetclassHashcodeTest";

        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            try {
                if (!testObj(classPath)) {
                    return fail("Failed : Iteration = " + i);
                }
            } catch(ClassNotFoundException e) {
                log.add(e.toString());
                return fail("Failed");
            }
        }
        return pass("OK");
    }

    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[0]);
        }
    }

    public boolean testObj(String classPath)  throws ClassNotFoundException {

        boolean result = true;

        Class   t1 = Class.forName(classPath);

        Class   t2 = (new ObjectGetclassHashcodeTest()).getClass();

        Class   t3 = ObjectGetclassHashcodeTest.class;

        Class   s1 = (new String()).getClass();
        
        Class   s2 = Class.forName("java.lang.String");
        
        Class   s3 = (new java.lang.String()).getClass();

        Class   s4 = java.lang.String.class;


        if(!checkObj(s1, s2)) {
            result = false;
        }
        if(!checkObj(s2, s3)) {
            result = false;
        }
        if(!checkObj(s3, s4)) {
            result = false;
        }
        if(!checkObj(t1, t2)) {
            result = false;
        }
        if(!checkObj(t2, t3)) {
            result = false;
        }
        return result;
    }

    public boolean checkObj( Object obj1, Object obj2 ) {
        boolean result = true;
        if( obj1.hashCode() != obj2.hashCode() ) {
            log.add("Check hashCode: Classes are different!");
            result = false;

        }

        if(!obj1.equals(obj2)) {
            log.add("Check equals: Classes are different!");
            result = false;
        }
        return result;
    }

}

