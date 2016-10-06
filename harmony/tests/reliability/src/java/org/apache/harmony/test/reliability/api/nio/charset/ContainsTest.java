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
 * @author Olessia Salmina
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.nio.charset;

import org.apache.harmony.test.reliability.share.Test;
import java.util.SortedMap;
import java.nio.charset.Charset;


/**
 * Goal: find memory leaks, connected with use of abstract method java.nio.charset.Charset.contains(Charset cs). 
 *
 * The test does:
 *     1. Reads parameters, which are:
 *          param[0] - number of iterations. During single iteration each available charset is chekced whether it contains
 *        all other available charsets. 
 *
 *     2. All available charsets are obtained. Each charset is checked by method java.nio.charset.Charset.contains(...) to
 *        be contained in all other charsets. Each charset must contains itself. 
 *
 */

public class ContainsTest extends Test {


    public int callSystemGC = 1;


    public int NUMBER_OF_ITERATIONS = 100;

    public static void main(String[] args) {
        System.exit(new ContainsTest().test(args));
    }


    public int test(String[] params) {
    
        boolean failed = false;

        parseParams(params);
                                
        SortedMap allCharsets = Charset.availableCharsets();

        Object[] names = allCharsets.keySet().toArray();

        for (int i = 0; i < names.length; i++){

            //log.add("Charset " + names[i]);
   
            for (int j = 0; j < names.length; j++){

                for (int k = 0; k < NUMBER_OF_ITERATIONS; k++) {

                    Charset chset1 = (Charset) allCharsets.get(names[i]);
                    Charset chset2 = (Charset) allCharsets.get(names[j]);
                    boolean contains = chset1.contains(chset2);

                    if (i == j && !contains) {
                        log.add("Charset(" + chset1.name() + ").contains(Charset(" + chset2.name() + ")) returns false");
                        failed = true;
                    }

                    // log.add("Charset " + names[i] + " contains " + names[j] + "? - " + contains);
                }
            }

            if (callSystemGC != 0){
                System.gc();
            }
    
        }

        return failed == true ? fail("failed") : pass("OK");
    }


    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[0]);
        }        

    }

}

