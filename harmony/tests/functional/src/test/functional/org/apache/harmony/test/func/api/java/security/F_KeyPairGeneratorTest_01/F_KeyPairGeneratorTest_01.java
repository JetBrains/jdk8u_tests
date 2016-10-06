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

package org.apache.harmony.test.func.api.java.security.F_KeyPairGeneratorTest_01;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.spec.RC5ParameterSpec;

import org.apache.harmony.test.func.share.ScenarioTest;

public class F_KeyPairGeneratorTest_01 extends ScenarioTest {

    public class MyKeyPairGenerator extends KeyPairGenerator {

        private boolean generateKeyPairCalled = false;

        public boolean getGenerateKeyPairCalled() {
            return generateKeyPairCalled;
        }

        public KeyPair generateKeyPair() {
            generateKeyPairCalled = true;
            return super.generateKeyPair();
        }

        public MyKeyPairGenerator(String arg0) {
            super(arg0);
        }

    }

    public int test() {

        MyKeyPairGenerator mykpg = new MyKeyPairGenerator("MD5");
        mykpg.genKeyPair();
        if (!mykpg.getGenerateKeyPairCalled()) {
            return fail("\nCall to KeyPairGenerator#genKeyPair() does not"
                    + "imply call to KeyPairGenerator#generateKeyPair()");
        }
        
        RC5ParameterSpec rc5 = new RC5ParameterSpec(1, 16, 32);

        try {
            mykpg.initialize(rc5);
        } catch (InvalidAlgorithmParameterException e) {
            return fail("\nCall to KeyPairGenerator#initialize() implied"
                    + "InvalidAlgorithmParameterException");
        }

        return pass();
    }

    public static void main(String[] args) {
        System.exit(new F_KeyPairGeneratorTest_01().test(args));
    }
}