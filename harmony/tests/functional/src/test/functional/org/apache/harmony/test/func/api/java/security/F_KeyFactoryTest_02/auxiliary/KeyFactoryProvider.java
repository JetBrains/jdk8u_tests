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
package org.apache.harmony.test.func.api.java.security.F_KeyFactoryTest_02.auxiliary;

import java.security.Provider;

/**
 * Created on 14.01.2005 
 */
public final class KeyFactoryProvider extends Provider {

    public KeyFactoryProvider(String name, double version, String info) {
        super(name, version, info);
        put("KeyFactory.DSA", "org.apache.harmony.test.func.api.java.security.F_KeyFactoryTest_02.auxiliary.KeyFactorySPImplementation");
        put("KeyFactory.DSA ImplementedIn", "Software");
        put("KeyFactory.RSA", "org.apache.harmony.test.func.api.java.security.F_KeyFactoryTest_02.auxiliary.KeyFactorySPImplementation");
        put("KeyFactory.RSA ImplementedIn", "Software");
        put("Alg.Alias.KeyFactory.1.2.840.10040.4.1", "DSA");
        System.err.println("KeyFactoryProvider ctor has been called.");
    }    
}
