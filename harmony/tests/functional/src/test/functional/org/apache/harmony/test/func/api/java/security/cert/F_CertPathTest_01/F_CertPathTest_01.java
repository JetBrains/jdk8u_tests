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

package org.apache.harmony.test.func.api.java.security.cert.F_CertPathTest_01;

import java.util.List;
import java.io.ObjectStreamException;
import java.security.cert.CertPath;
import java.security.cert.CertificateEncodingException;
import java.util.Iterator;

import org.apache.harmony.test.func.share.ScenarioTest;

public class F_CertPathTest_01 extends ScenarioTest {

    private String type = "";

    public class MyCertPath extends CertPath {

        public class MyCertPathRep extends CertPathRep {
            public MyCertPathRep(String arg0, byte[] arg1) {
                super(arg0, arg1);
            }
        }

        public MyCertPath(String arg0) {
            super(arg0);
            type = arg0;
        }

        public CertPath.CertPathRep getCertPathRepObj() {
            byte data[] = { 'h', 'i', '!' };
            return new MyCertPathRep(type, data);
        }

        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public byte[] getEncoded() throws CertificateEncodingException {
            return null;
        }

        public byte[] getEncoded(String arg0)
                throws CertificateEncodingException {
            return null;
        }

        public Iterator getEncodings() {
            return null;
        }

        public List getCertificates() {
            return null;
        }
    }

    public int test() {
        MyCertPath mycp = new MyCertPath("PGP");
        MyCertPath.MyCertPathRep o = null;

        try {
            String name = mycp.writeReplace().getClass().getName();
            if (name.indexOf("CertPath$CertPathRep") < 0) {
                return fail("\nCertPath#writeReplace() retrieved incorrect"
                        + "type of object");
            }
        } catch (Exception e) {
            return fail("\nCall to CertPath#writeReplace() implied"
                    + e.getClass().getName());
        }

        return pass();
    }

    public static void main(String[] args) {
        System.exit(new F_CertPathTest_01().test(args));
    }
}