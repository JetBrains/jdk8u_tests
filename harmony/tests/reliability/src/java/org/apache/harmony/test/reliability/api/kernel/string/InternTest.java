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
 * @version $Revision: 1.2 $
 */

package org.apache.harmony.test.reliability.api.kernel.string;

import org.apache.harmony.test.reliability.share.Test;
import java.util.Random;


/**
 * Goal: find resource leaks or other problems caused by invocation of String.intern() method.
 *
 * Idea: being run in a cycle in a single JVM the test will lead to creation big number of different string 
 *       objects, references to which are lost and they can be finalized and removed from the pull of
 *       of internal string representations. There can be an errors cuased by creation and invocation of 
 *       intern() for big number of String objects and working in parallel GC.
 *
 * The test does:
 *     1. Creates a random string.
 *     2. In static or instance contexts, in a cycle for N_OF_SAME_STRINGS times:
 *             a. Creates a new string with the given content via new String(random string).
 *             b. Calls intern().
 *             c. Looses references to the created String object.
 */

public class InternTest extends Test {

    static final int N_OF_SAME_STRINGS = 100;


    public static void main(String[] args) {
        System.exit(new InternTest().test(args));
    }


    public int test(String[] params) {
    
        instanceContextStrManipulation();

        staticContextStrManipulation();

        return pass("OK");
    }



    public void instanceContextStrManipulation() {

        String str = getRandomString();

        for (int i = 0; i < N_OF_SAME_STRINGS; i++) {

            str = new String(str);

            str.intern();
        }
            
    }

    public static void staticContextStrManipulation() {

        String str = getRandomString();

        for (int i = 0; i < N_OF_SAME_STRINGS; i++) {

            str = new String(str);

            str.intern();
        }
            
    }

    static Random rnd = new Random(10);

    static String getRandomString() {

        char[] c = new char[rnd.nextInt(Character.MAX_VALUE)];

        for (int j = 0; j < c.length; ++j){
            c[j] = (char)rnd.nextInt(Character.MAX_VALUE);
        }

        return new String(c);
    }

}



