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
package org.apache.harmony.test.func.api.java.security.F_AlgorithmParametersTest_01;

import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

import org.apache.harmony.test.func.api.java.security.F_AlgorithmParametersTest_01.auxiliary.F_AlgorithmParametersProvider;
import org.apache.harmony.test.func.api.java.security.F_AlgorithmParametersTest_01.auxiliary.F_AlgorithmParametersSPImplementation;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 09.08.2005 
 */
public class F_AlgorithmParametersTest_01 extends ScenarioTest {

    public static final String providerName = "org.apache.harmony.test.func.api.java.security.AlgorithmParametersTestProvider";
    public static final double providerVersion = 1.0;
    public static final String providerInfo = "AlgorithmParametersTestProvider v1.0 for testing purpose only";
    F_AlgorithmParametersSPImplementation apspi = null;
    Provider p = null;

    /* (non-Javadoc)
     * @see org.apache.harmony.share.Test#test()
     */
    public int test() {
        
        if ("failed".equalsIgnoreCase(Security.getProperty("policy.allowSystemProperty"))) {
            return error("An extra policy feature is disabled by default. Test can't be run.");
        }
        
        System.setSecurityManager(new SecurityManager());
        
        try {
            apspi = new F_AlgorithmParametersSPImplementation();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return fail("Couldn't create an instance of AlgorithmParametersSPImplementation object.");
        }
        
        p = new F_AlgorithmParametersProvider(providerName, providerVersion, providerInfo);
        if (null == p) {
            return fail("Couldn't create a KeyFactoryProvider object.");
        }
 
        Security.addProvider(p);
        
        AlgorithmParameters ap = new MyAlgorithmParameters();
        
        if (null == ap) {
            return fail("Couldn't create an instance of AlgorithmParameters object.");
        }
        
        return pass();
    }
    
    class MyAlgorithmParameters extends AlgorithmParameters {
        public MyAlgorithmParameters() {
            super(apspi, p, "DSA");
            System.out.println("MyAlgorithmParameters ctor has been called.");
        }
    }

    public static void main(String[] args) {
        System.exit(new F_AlgorithmParametersTest_01().test(args));
    }
}
