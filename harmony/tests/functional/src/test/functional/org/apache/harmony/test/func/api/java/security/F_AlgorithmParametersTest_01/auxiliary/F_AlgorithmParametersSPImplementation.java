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
package org.apache.harmony.test.func.api.java.security.F_AlgorithmParametersTest_01.auxiliary;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.AlgorithmParametersSpi;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;

/**
 * Created on 09.08.2005 
 */
public class F_AlgorithmParametersSPImplementation extends AlgorithmParametersSpi {

    AlgorithmParameters ap = null;
    
    public F_AlgorithmParametersSPImplementation() throws NoSuchAlgorithmException {
        super();
        ap = AlgorithmParameters.getInstance("DSA");
        System.out.println("AlgorithmParametersSPImplementation ctor has been called.");
    }

    /* (non-Javadoc)
     * @see java.security.AlgorithmParametersSpi#engineInit(java.security.spec.AlgorithmParameterSpec)
     */
    protected void engineInit(AlgorithmParameterSpec paramSpec) throws InvalidParameterSpecException {
        ap.init(paramSpec);
        System.out.println("F_AlgorithmParametersSPImplementation::engineInit(AlgorithmParameterSpec paramSpec) has been called.");
    }

    /* (non-Javadoc)
     * @see java.security.AlgorithmParametersSpi#engineInit(byte[])
     */
    protected void engineInit(byte[] params) throws IOException {
        ap.init(params);
        System.out.println("F_AlgorithmParametersSPImplementation::engineInit(byte[] params) has been called.");
    }

    /* (non-Javadoc)
     * @see java.security.AlgorithmParametersSpi#engineInit(byte[], java.lang.String)
     */
    protected void engineInit(byte[] params, String format) throws IOException {
        ap.init(params, format);
        System.out.println("F_AlgorithmParametersSPImplementation::engineInit(byte[] params, String format) has been called.");
    }

    /* (non-Javadoc)
     * @see java.security.AlgorithmParametersSpi#engineGetParameterSpec(java.lang.Class)
     */
    protected AlgorithmParameterSpec engineGetParameterSpec(Class paramSpec) throws InvalidParameterSpecException {
        System.out.println("F_AlgorithmParametersSPImplementation::engineGetParameterSpec() has been called.");
        return ap.getParameterSpec(paramSpec);
    }

    /* (non-Javadoc)
     * @see java.security.AlgorithmParametersSpi#engineGetEncoded()
     */
    protected byte[] engineGetEncoded() throws IOException {
        System.out.println("F_AlgorithmParametersSPImplementation::engineGetEncoded() has been called.");
        return ap.getEncoded();
    }

    /* (non-Javadoc)
     * @see java.security.AlgorithmParametersSpi#engineGetEncoded(java.lang.String)
     */
    protected byte[] engineGetEncoded(String format) throws IOException {
        System.out.println("F_AlgorithmParametersSPImplementation::engineGetEncoded(String format) has been called.");
        return ap.getEncoded(format);
    }

    /* (non-Javadoc)
     * @see java.security.AlgorithmParametersSpi#engineToString()
     */
    protected String engineToString() {
        System.out.println("F_AlgorithmParametersSPImplementation::engineToString() has been called.");
        return ap.toString();
    }
}
