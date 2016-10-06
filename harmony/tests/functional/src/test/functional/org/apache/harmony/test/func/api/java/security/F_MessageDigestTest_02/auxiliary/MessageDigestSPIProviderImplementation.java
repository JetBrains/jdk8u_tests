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
package org.apache.harmony.test.func.api.java.security.F_MessageDigestTest_02.auxiliary;
import java.io.IOException;
import java.io.InputStream;
import java.security.Provider;
import java.security.Security;
import java.util.Collection;
import java.util.Map;
import java.util.Set;


    public final class MessageDigestSPIProviderImplementation extends Provider {

        public MessageDigestSPIProviderImplementation() {
            super("MDPI", 1.0, "MDPI provider v1.0, implementing for test reason only.");
            Provider p[] = Security.getProviders("MessageDigest.MD5");
            for (int i = 0; i < p.length; i++) {
                String name = p[i].get("MessageDigest.MD5").toString();
                if (name != null) {
                    put("MessageDigest.MD5", name);
                    put("MessageDigest.MD5" + " ImplementedIn", "Software");
                    break;
                }
            }           
            
            System.err.println("MDPI:constructor() finished");
        }
        
        public String getName() {
            System.err.println("MDPI:getName()");
            return super.getName();
        }

        public double getVersion() {
            System.err.println("MDPI:getVersion()");
            return super.getVersion();
        }

        public String getInfo() {
            System.err.println("MDPI:getInfo()");
            return super.getInfo();
        }
        
        public String toString() {
            System.err.println("MDPI:toString()");
            return super.toString();
        }
        
        public void clear() {
            System.err.println("MDPI:clear()");
            super.clear();
        }
        
        public void load(InputStream inStream) throws IOException {
            System.err.println("MDPI:load()");
            super.load(inStream);
        }
        
        public void putAll(Map t) {
            System.err.println("MDPI:putAll()");
            super.putAll(t);
        }
        
        public Set entrySet() {
            System.err.println("MDPI:entrySet()");
            return super.entrySet();
        }
        
        public Set keySet() {
            System.err.println("MDPI:keySet()");
            return super.keySet();
        }

        public Collection values() {
            System.err.println("MDPI:values()");
            return super.values();
        }
        
        public Object put(Object key, Object value) {
            System.err.println("MDPI:put("+key+", "+value+")");
            return super.put(key, value);
        }
        
        public Object remove(Object key) {
            System.err.println("MDPI:remove("+key+")");
            return super.remove(key);
        }
        
    }

