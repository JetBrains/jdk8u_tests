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
package org.apache.harmony.test.func.api.java.security.cert.F_CertificateFactoryTest_03.auxiliary;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathBuilderSpi;
import java.security.cert.CertPathParameters;

/**
 * Created on Oct 17, 2005 
 */
public class F_CertPathBuilderSPImplementation extends CertPathBuilderSpi {

    private CertPathBuilder certPathBuilder;
    /**
     * 
     */
    public F_CertPathBuilderSPImplementation() throws NoSuchAlgorithmException {
        super();
        certPathBuilder = CertPathBuilder.getInstance("PKIX");
        System.out.println("F_CertPathBuilderSPImplementation ctor has been called");
    }

    /* (non-Javadoc)
     * @see java.security.cert.CertPathBuilderSpi#engineBuild(java.security.cert.CertPathParameters)
     */
    public CertPathBuilderResult engineBuild(CertPathParameters params) throws CertPathBuilderException, InvalidAlgorithmParameterException {
        CertPathBuilderResult result = certPathBuilder.build(params);
        System.out.println("F_CertPathBuilderSPImplementation::build() has been called");
        return result;
    }

}
