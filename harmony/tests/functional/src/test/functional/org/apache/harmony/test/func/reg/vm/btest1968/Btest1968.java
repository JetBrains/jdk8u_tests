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
/**
 */

package org.apache.harmony.test.func.reg.vm.btest1968;

import org.apache.harmony.share.Test;

/**
 */

public class Btest1968 extends Test {
    public static void main(String[] args) {
        System.exit(new Btest1968().test());
    }

    public int test() {
            Error e = new Error();
            StackTraceElement[] ste = e.getStackTrace();

            if (!ste[0].getClassName().equals("org.apache.harmony.test.func.reg.vm.btest1968.Btest1968"))
                return fail("incorrect ClassName - " + ste[ste.length -1].getClassName());

            if (!ste[0].getFileName().equals("Btest1968.java"))
                return fail("incorrect FileName - " + ste[ste.length -1].getFileName());

            if (ste[0].getLineNumber() != 20) 
                return fail("incorrect LineNumber - " + ste[ste.length -1].getLineNumber());

            if (!ste[0].getMethodName().equals("test"))
                return fail("incorrect MethodName - " + ste[ste.length -1].getMethodName());

            return pass();
    }
}

