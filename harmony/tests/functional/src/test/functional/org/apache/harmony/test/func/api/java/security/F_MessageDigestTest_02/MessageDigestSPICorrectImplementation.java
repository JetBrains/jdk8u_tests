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
package org.apache.harmony.test.func.api.java.security.F_MessageDigestTest_02;
import java.security.MessageDigestSpi;

import org.apache.harmony.test.func.api.java.security.F_MessageDigestTest_02.F_MessageDigestTest_02;

    public class MessageDigestSPICorrectImplementation extends MessageDigestSpi {

        public MessageDigestSPICorrectImplementation() {
            super();
            System.err.println("MDSI::constructor()");
        }

        protected void engineReset() {
            F_MessageDigestTest_02.addAction(F_MessageDigestTest_02.ENGINE_void_engineReset);
            System.err.println("MDSI::engineReset()");
        }

        protected byte[] engineDigest() {
            F_MessageDigestTest_02.addAction(F_MessageDigestTest_02.ENGINE_bytearray_engineDigest);
            System.err.println("MDSI::engineDigest()");
            byte[] buf = {-65,-32,22,-82,38,-21,17,116,11,-123,17,-73,-69,-27,82,5}; // provider info's digest
            return buf; 
        }

        protected void engineUpdate(byte arg0) {
            F_MessageDigestTest_02.addAction(F_MessageDigestTest_02.ENGINE_void_engineUpdate_byte);
            System.err.println("MDSI::engineUpdate(*)");
        }

        protected void engineUpdate(byte[] arg0, int arg1, int arg2) {
            F_MessageDigestTest_02.addAction(F_MessageDigestTest_02.ENGINE_void_engineUpdate_bytearray_int_int);
            System.err.println("MDSI::engineUpdate(*,*,*)");
        }

    }
