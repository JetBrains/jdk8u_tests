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

package org.apache.harmony.test.reliability.api.kernel.string;

import org.apache.harmony.test.reliability.share.Test;
import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;

/**
 * Goal: find resource leaks caused by String.CASE_INSENSITIVE_ORDER field.
 *
 * The test does:
 *  - sets String.CASE_INSENSITIVE_ORDER
 *  - check result using TreeSet.addAll/TreeSet.removeAll methods
 */

public class InsensitiveTest extends Test {

    public static void main(String[] args) {
        System.exit(new InsensitiveTest().test(args));
    }

    public int test(String[] params) {


        String[] b = {"a", "b", "c", "d", "e", "f", "g"};

        if(addRemString(b, false, b).size() != 0) {
            return fail("Failed. size should be 0");
        }

        for(int j = 0; j < b.length; j++) {
            if(!addRemString(b, true, b[j].toUpperCase()).equals(addRemString(b, false, b[j]))) {
                return fail("Failed. The Sets should be equals.");
            }
        }


        return pass("OK");
    }


    public Set addRemString(String[] a, boolean flag, String... str) {

    Set<String> s = new TreeSet<String>();

    if(flag) {
    s = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
}
    
    s.addAll(Arrays.asList(a));
    s.removeAll(Arrays.asList(str));
    return s;
}


}

