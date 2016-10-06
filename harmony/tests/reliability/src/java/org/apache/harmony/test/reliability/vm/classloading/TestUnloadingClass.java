/*
 * Copyright 2007 The Apache Software Foundation or its licensors, as applicable
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
 * @author Aleksey Ignatenko
 * @version $Revision: 1.0 $
 */

/**
 * The class is loaded by User Defined Classloader
 * test function emulates some work
**/

package org.apache.harmony.test.reliability.vm.classloading;

import java.math.BigInteger;
import java.util.Random;

public class TestUnloadingClass {
    public void test(){
        res = sum(v1,v2);
    }
    int v1 = 5,v2 = 7;
    int res;
    int sum(int p1, int p2) {
        Random rm = new Random();
        int res = 0;
        for (int i = 0; i < 10; i++ ){
            BigInteger bi = new BigInteger(10, rm); 
            res =  p1+p2 + bi.intValue();
        }
        return res;
    }
}
