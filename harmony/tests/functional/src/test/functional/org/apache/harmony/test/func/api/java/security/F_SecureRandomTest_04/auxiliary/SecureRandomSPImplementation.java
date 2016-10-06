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
package org.apache.harmony.test.func.api.java.security.F_SecureRandomTest_04.auxiliary;

import java.security.SecureRandomSpi;
import java.util.Random;

/**
 * Created on 27.12.2004 
 * 
 * For the sake of simplicity and for testing purpose ONLY!!!
 * 
 */
public final class SecureRandomSPImplementation extends SecureRandomSpi {

    Random random = null;
    public SecureRandomSPImplementation() {
        super();
        random = new Random();
        System.err.println("SecureRandomSPImplementation ctor has been called.");
    }
    
    protected byte[] engineGenerateSeed(int numBytes) {
        System.err.println("Seeds generation method has been called.");
        byte[] bytes = new byte[numBytes];        
        random.nextBytes(bytes);        
        return bytes;        
    }

    protected void engineNextBytes(byte[] bytes) {
        System.err.println("Random bytes generation method has been called.");
        random.nextBytes(bytes);                
    }

    protected void engineSetSeed(byte[] seed) {
        //random.setSeed(1000);
        System.err.println("Reseeding method has been called.");
    }
}