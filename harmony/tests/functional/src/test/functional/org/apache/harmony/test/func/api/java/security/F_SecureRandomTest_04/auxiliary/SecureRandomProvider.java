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

import java.security.Provider;

/**
 * Created on 23.12.2004 
 */
public final class SecureRandomProvider extends Provider {

    public SecureRandomProvider(String name, double version, String description) {
        super(name, version, description);
        System.err.println("SecureRandomProvider ctor has been called.");        
        put("SecureRandom.SHA1PRNG", "org.apache.harmony.test.func.api.java.security.F_SecureRandomTest_04.auxiliary.SecureRandomSPImplementation");        
        put("SecureRandom.SHA1PRNG ImplementedIn", "Software");                
    }
}