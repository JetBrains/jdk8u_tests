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
 * Created on 23.08.2005
 */
package org.apache.harmony.test.func.api.java.lang.F_SystemTest_04;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Tests System.mapLibraryName(String) method.
 * 
 */
public class F_SystemTest_04 extends ScenarioTest {
    String libraryPrefix = "";
    String libraryExt;

    public static void main(String[] args) {
        System.exit(new F_SystemTest_04().test(args));
    }

    public int test() {
        if (System.getProperty("os.name").indexOf("Windows") == -1) {
            libraryPrefix = "lib";
            libraryExt = ".so";
        } else {
            libraryExt = ".dll";
        }
        String libraryName = "libc";
        if (System.mapLibraryName(libraryName).equals(
            libraryPrefix + libraryName + libraryExt)) {
            log.info("mapLibraryName(" + libraryName + ") = "
                + (libraryPrefix + libraryName + libraryExt));
            return pass();
        }
        return fail("Incorrect compared string in mapLibraryName method.");
    }
}