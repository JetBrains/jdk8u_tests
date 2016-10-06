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
package org.apache.harmony.test.func.reg.vm.btest7919;

import java.io.File;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.RegressionTest;

/**
 * VM segfaults in type_info_get_loading_error()
 *
 */

public class ReflectMissingParamArray extends RegressionTest {

    public int test(Logger log, String[] args) throws Throwable {
        Method[] ms = this.getClass().getMethods();
        for(int i = 0; i<ms.length; i++) {
            Method m = ms[i];
            if (m.getName().equals("m1")) {
                try {
                    m.getParameterTypes();
                    log.severe("misconfigured test");
                    return ERROR;
                } catch (NoClassDefFoundError ok) {}
            }
        }

        return PASSED;
    }

    public void m1(Missing[] param){}


    public static void main(String[] args)  throws Throwable {
        System.exit(new ReflectMissingParamArray().test(Logger.global, args));
    }

}


class Missing{}
