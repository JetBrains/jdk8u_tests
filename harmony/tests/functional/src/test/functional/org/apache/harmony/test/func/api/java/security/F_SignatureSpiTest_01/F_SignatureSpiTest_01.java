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

package org.apache.harmony.test.func.api.java.security.F_SignatureSpiTest_01;

import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignatureSpi;

import org.apache.harmony.test.func.share.ScenarioTest;

public class F_SignatureSpiTest_01 extends ScenarioTest {

    public class MySignatureSpi extends SignatureSpi {

        public AlgorithmParameters engineGetParameters() {
            return super.engineGetParameters();
        }

        public MySignatureSpi() {
            super();
        }

        protected byte[] engineSign() throws SignatureException {
            return null;
        }

        protected void engineUpdate(byte arg0) throws SignatureException {

        }

        protected boolean engineVerify(byte[] arg0) throws SignatureException {
            return false;
        }

        protected void engineUpdate(byte[] arg0, int arg1, int arg2)
                throws SignatureException {
        }

        protected void engineInitSign(PrivateKey arg0)
                throws InvalidKeyException {
        }

        protected void engineInitVerify(PublicKey arg0)
                throws InvalidKeyException {
        }

        protected Object engineGetParameter(String arg0)
                throws InvalidParameterException {
            return null;
        }

        protected void engineSetParameter(String arg0, Object arg1)
                throws InvalidParameterException {
        }
    }

    public int test() {

        MySignatureSpi mysSPI = new MySignatureSpi();
        boolean exceptionThrown = false;
        try {
            mysSPI.engineGetParameters();
        } catch (UnsupportedOperationException e) {
            exceptionThrown = true;
        }
        if (exceptionThrown == false) {
            return fail("\nCall to SignatureSpi#engineGetParameters() did not"
                    + "throw UnsupportedOperationException");
        }

        exceptionThrown = false;
        try {
            SignatureSpi anotherSSPI = (SignatureSpi) mysSPI.clone();
        } catch (CloneNotSupportedException e) {
            exceptionThrown = true;
        }
        if (exceptionThrown == false) {
            return fail("\nCall to SignatureSpi#clone() did not"
                    + "throw CloneNotSupportedException");
        }

        return pass();
    }

    public static void main(String[] args) {
        System.exit(new F_SignatureSpiTest_01().test(args));
    }
}