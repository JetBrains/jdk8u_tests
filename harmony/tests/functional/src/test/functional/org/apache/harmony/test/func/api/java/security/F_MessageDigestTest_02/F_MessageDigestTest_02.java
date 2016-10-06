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
/*
 * Created on 25.10.2004
 */
package org.apache.harmony.test.func.api.java.security.F_MessageDigestTest_02;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.apache.harmony.test.func.api.java.security.F_MessageDigestTest_02.auxiliary.MessageDigestSPIProviderImplementation;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * check order of methods calling in own Provider implementation.
 */
public class F_MessageDigestTest_02 extends ScenarioTest {

    public static final String providerName = "MDPI";
    public static final int ENGINE_ERROR                                = 000;
    public static final int ENGINE_void_engineReset                     = 001;
    public static final int ENGINE_bytearray_engineDigest                 = 002;
    public static final int ENGINE_void_engineUpdate_byte                 = 003;
    public static final int ENGINE_void_engineUpdate_bytearray_int_int     = 004;
    private static Vector v = new Vector();
    
    public static void addAction(int action) {
        v.add(new Integer(action));
    }
    
    private void printProvidersList() {
        try {
            log.info("Provider list:");
            Provider p[] = Security.getProviders();
            for (int i = 0; i < p.length; i++) {
                log.info("\t"+p[i]);
            }
        }
        catch (Exception e) {
            log.info(""+e);
        }
    }
    
    private void printProviderProperties(Provider p) {
        log.info("\t-----------------------------------------");
        for (Enumeration e = p.keys(); e.hasMoreElements();) {
            Object value = e.nextElement();
            log.info("\t" + value + "\t=\t" + p.get(value));
        }
        log.info("\t-----------------------------------------");
    }
    
    public int test() {
        //System.setSecurityManager(new SecurityManager());
        //security.provider.2=org.apache.harmony.test.func.api.java.security.F_MessageDigestTest_02.auxiliary.MessageDigestSPIProviderImplementation
        //above line can be placed into java.security file instead of following code
        printProvidersList();
// get instance of provider:
        MessageDigestSPIProviderImplementation p = new MessageDigestSPIProviderImplementation();
        //Provider p = Security.getProvider(providerName);
        if (p == null) { log.info(providerName+" provider is null"); return 2005; }
        log.info("\tprovider name\t= " + p.getName());
        log.info("\tprovider version#\t= " + p.getVersion());
        log.info("\tprovider info\t= " + p.getInfo());
        printProviderProperties(p);
// add it to Security providers collection
        Security.insertProviderAt(p, 2);        
        printProvidersList();
// get the right digest with reference algorithm implementation.        
        byte[] digest = new byte[16];
        try {
            //MessageDigest md = MessageDigest.getInstance(algorithmName, p);            
            MessageDigest md = MessageDigest.getInstance("MD5");
            log.info("\t1:Algorithm name = " + md.getAlgorithm());
            log.info("\t1:Provider name = " + md.getProvider().getName());
            md.reset();
            md.update(p.getInfo().getBytes());
            digest = md.digest();
            for (int i = 0; i < digest.length; i++) {
                log.info((byte)digest[i]+":");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
// get digest from own SPI and check above to equal with it        
        p.clear();
/*        if (!p.values().isEmpty()) {
            return error("Provider properties aren't empty.");
        }*/
        
        p.put("MessageDigest.MD5", "org.apache.harmony.test.func.api.java.security.F_MessageDigestTest_02.MessageDigestSPICorrectImplementation");
        printProviderProperties(p);
        try {
            MessageDigest md2 = MessageDigest.getInstance("MD5", providerName);
            log.info(""+md2);
            log.info("\t2:Algorithm name = " + md2.getAlgorithm());
            log.info("\t2:Provider name = " + md2.getProvider().getName());
            md2.reset();
            md2.update(p.getInfo().getBytes());
            if (MessageDigest.isEqual(digest, md2.digest()))
                log.info("\nDigests are equal!\n");
            else 
                fail("\nDigests aren't equal...\n");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return error(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
// check impossibility to get instance when no constructor without any parameters        
        p.clear();
/*        if (!p.values().isEmpty()) {
            return error("Provider properties aren't empty.");
        }*/
        
        p.put("MessageDigest.MD5", "org.apache.harmony.test.func.api.java.security.F_MessageDigestTest_02.auxiliary.MessageDigestSPIWrongImplementation");
        printProviderProperties(p);
        try {
            MessageDigest md3 = MessageDigest.getInstance("MD5", providerName);            
            return fail("MessageDigestSPI wrong implementation can't be used ");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return error(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            //probably, it's the right way
            //if (!e.getMessage().equals("class org.apache.harmony.test.func.api.java.security.F_MessageDigestTest_02.auxiliary.MessageDigestSPIWrongImplementation configured for MessageDigest(provider: "+providerName+") cannot be instantiated.\n")) {            
            e.printStackTrace();
            log.info("Algorithm implementation wasn't found for wrong provider.");
            /*if (e.getMessage().indexOf("class org.apache.harmony.test.func.api.java.security.F_MessageDigestTest_02.auxiliary.MessageDigestSPIWrongImplementation configured for MessageDigest(provider: "+providerName+") cannot be instantiated.\n") == -1 &&
                e.getMessage().indexOf("MessageDigest " + algorithmName + " implementation not found") == -1) {                
                e.printStackTrace();
                return error(e.getMessage());
            }*/
            //yeah, it's the right way
        }
// check removeProvider (Security class);        
        Security.removeProvider(providerName);
        printProvidersList();
        try {
            MessageDigest md3 = MessageDigest.getInstance("MD5", providerName);
            return fail("Message digest wasn't removed and is still in use.");
        } catch (NoSuchProviderException e) {
            // right way
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        
        String result = "";
        for (Iterator iter = v.iterator(); iter.hasNext();) {
            Integer element = (Integer) iter.next();
            result = result.concat(element+"::"); // must be 1::4::2::
        }
        if (!result.equals("1::4::2::"))
            return fail("order of methods calling is wrong.");
        
        return pass();
    }
    
    public static void main(String[] args) {
        System.exit(new F_MessageDigestTest_02().test(args));
    }

}
