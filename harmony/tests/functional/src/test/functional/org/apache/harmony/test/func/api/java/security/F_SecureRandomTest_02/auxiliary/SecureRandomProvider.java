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
package org.apache.harmony.test.func.api.java.security.F_SecureRandomTest_02.auxiliary;

import java.io.IOException;
import java.io.InputStream;
import java.security.Provider;
import java.util.Collection;
//import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created on 23.12.2004 
 */
public final class SecureRandomProvider extends Provider {

    public SecureRandomProvider(String name, double version, String description) {
        super(name, version, description);
        System.err.println("SecureRandomProvider ctor has been called.");
//        HashMap map = new HashMap();
//        map.put("SecureRandom.SHA1PRNG", "sun.security.provider.SecureRandom");
//        map.put("Alg.Alias.SecureRandom.SHA1PRNG", "SHARNG");
//        map.put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
//        putAll(map);        
    }

    public synchronized void clear() {
        System.err.println("Calling SRP.clear()...");
        super.clear();
    }
    
    public synchronized Set entrySet() {
        System.err.println("Calling SRP.entrySet()...");
        return super.entrySet();
    }

    public String getInfo() {
        System.err.println("Calling SRP.getInfo()...");
        return super.getInfo();
    }
    
    public String getName() {
        System.err.println("Calling SRP.getName()...");
        return super.getName();
    }
    
    public double getVersion() {
        System.err.println("Calling SRP.getVersion()...");
        return super.getVersion();
    }
    
    public Set keySet() {
        System.err.println("Calling SRP.keySet()...");
        return super.keySet();
    }
    
    public synchronized void load(InputStream inStream) throws IOException {
        System.err.println("Calling SRP.load()...");
        super.load(inStream);
    }
    
    public synchronized Object put(Object key, Object value) {
        System.err.println("Calling SRP.put()...");
        return super.put(key, value);
    }
    
    public synchronized void putAll(Map t) {
        System.err.println("Calling SRP.putAll()...");
        super.putAll(t);
    }
    
    public synchronized Object remove(Object key) {
        System.err.println("Calling SRP.remove()...");
        return super.remove(key);
    }
    
    public String toString() {
        System.err.println("Calling SRP.toString()...");
        return super.toString();
    }
    
    public Collection values() {
        System.err.println("Calling SRP.values()...");
        return super.values();
    }
}