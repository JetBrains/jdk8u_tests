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
package org.apache.harmony.test.func.reg.vm.btest5068;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;

import org.apache.harmony.test.share.reg.RegressionTest;

/**
 * java.lang.reflect.Constructor.newInstance() unwraps null 
 * to any primitive type without any exceptions.
 *
 */
public class UnwrapNull extends RegressionTest {

    public int test(Logger log, String[] args) {
        Constructor[] c = PrimitiveCtorParam.class.getConstructors();
        Object[] nullParam = new Object[]{null};
        for (int i = 0; i < c.length; i++) {
            try {
                c[i].newInstance(nullParam);
                log.info("Illegally unwrapped null to " 
                        + c[i].getParameterTypes()[0]);
                return FAILED;
            } catch (IllegalArgumentException ok) {}
            catch (Exception e) {
                log.info("Unexpected excepton: " + e);
                e.printStackTrace();
                return ERROR;
            }
        }
        
        return PASSED;
    }
    
    public static void main(String[] args) {
        System.exit(new UnwrapNull().test(Logger.global, args));
    }
}

class PrimitiveCtorParam {
    public PrimitiveCtorParam(int i){}
    public PrimitiveCtorParam(boolean b){}
    public PrimitiveCtorParam(char c){}
}