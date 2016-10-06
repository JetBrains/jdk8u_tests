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
 * Goal: find resource leaks or intermittent failures caused by Object.clone() operation.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of iterations to call/initialize in a cycle and number of clones
 *       2. Excutes a cycle of param[0] iterationss, on each iteration:
 *            creates param[0] number of clones for the different objects
 */

public class ObjectCloneTest extends Test implements Cloneable {

    public static int NUMBER_OF_ITERATIONS = 1000;

    static {
        for(int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            try {
                new Cloner().clone();
            } catch(CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            _clone();
        }
    }


    public static void main(String[] args) {
        System.exit(new ObjectCloneTest().test(args));
    }

    public int test(String[] params) {

        parseParams(params);

        for(int i = 0; i < NUMBER_OF_ITERATIONS; i++) {                                                      
            ObjectCloneTest t = new ObjectCloneTest();
            t.test_clone();
            static_test_clone(t);
            static_clone();
        }

        return pass("OK");
    }

    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[0]);
        }

    }

    Object test_clone() {
        Object obj = null;    

        for(int i = 0; i < ObjectCloneTest.NUMBER_OF_ITERATIONS; i++) {
            try {
                obj = new Cloner().clone();
            } catch(CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < ObjectCloneTest.NUMBER_OF_ITERATIONS; i++) {
            obj = _clone();
        }
        return obj;
    }


    static Object _clone() {
        Object obj = null;    
        try {
            obj = new Cloner().clone();
        } catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }

    static Object static_test_clone(ObjectCloneTest t) {

        Object obj = null;    
        for(int i = 0; i < ObjectCloneTest.NUMBER_OF_ITERATIONS; i++) {
            try {
                obj = t.clone();
            } catch(CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

 

    static Object  static_clone(){
        Object obj = null;    
        for(int i = 0; i < ObjectCloneTest.NUMBER_OF_ITERATIONS; i++) {
            try {
                obj = new Cloner().clone();
            } catch(CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

}

class Cloner implements Cloneable{
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
