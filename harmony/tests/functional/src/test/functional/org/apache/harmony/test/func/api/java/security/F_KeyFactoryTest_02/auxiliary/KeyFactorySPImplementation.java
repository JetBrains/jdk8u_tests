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

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyFactorySpi;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Created on 31.12.2004 
 * 
 * FOR TESTING PURPOSE ONLY!!!
 */
public final class KeyFactorySPImplementation extends KeyFactorySpi {

    KeyFactory kf = null;
    
    public KeyFactorySPImplementation(String algorithm) throws NoSuchAlgorithmException {
        super();
        kf = KeyFactory.getInstance(algorithm);
        System.err.println("KeyFactorySPImplementation ctor has been called.");
    }

    protected Key engineTranslateKey(Key key) throws InvalidKeyException {
        Key translatedKey = kf.translateKey(key);
        System.err.println("KeyFactory.translateKey() has been called.");
        return translatedKey;
    }

    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        PrivateKey privKey = kf.generatePrivate(keySpec);
        System.err.println("KeyFactory.generatePrivate() has been called.");
        return privKey;
    }

    protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
        PublicKey pubKey = kf.generatePublic(keySpec);
        System.err.println("KeyFactory.generatePublic() has been called.");
        return pubKey;
    }

    protected KeySpec engineGetKeySpec(Key key, Class keySpec) throws InvalidKeySpecException {
        KeySpec localKeySpec = kf.getKeySpec(key, keySpec);
        System.err.println("KeyFactory.getKeySpec() has been called.");
        return localKeySpec;
    }
}