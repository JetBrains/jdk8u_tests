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
 * Last modification G.Seryakova
 * Last modified on 25.01.2005
 * 
 * This test use RSA algorithm for encrypting and decode big integer string.
 * 
 */
package org.apache.harmony.test.func.api.java.math.F_BigIntegerRSATest_01;

import java.math.BigInteger;
import java.util.Random;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 *
 * This test use RSA algorithm for encrypting and decode big integer string.
 */
public class F_BigIntegerRSATest_01 extends ScenarioTest {
    
    private BigInteger e;
    private BigInteger d;
    private BigInteger m;
    
    public int test(){
        String input = "This is test string.";        
        BigInteger inStr = new BigInteger (input.getBytes());
        BigInteger inStr1;
        BigInteger outStr;
        
        setKeys();
        
        outStr = encript(inStr);
        inStr1 = unencript(outStr);
                
        if (!inStr1.equals(inStr)){
            return fail("Numbers not equal.");
        }
        if (inStr1.bitLength()!=inStr.bitLength()){
            return fail("Numbers equal but bitLength not equal.");
        }
        for (int i=0;i<inStr1.bitLength();i++){
            if ((inStr1.testBit(i)?1:0)!=(inStr.testBit(i)?1:0)){
                return fail("Numbers equal but bits representation not equal for bit number " + i);
            }
        }
        String output = new String(inStr1.toByteArray());
        if (!input.equals(output)){
            return fail("Strings not equal. Input - " + input + "; output - " + output);
        }
        return pass();
    }
    
    private void setKeys(){
//        Unsupported constructor
//        BigInteger p = new BigInteger(256, 32, new Random());
//        BigInteger q;
//        do {
//            q = new BigInteger(256, 32, new Random());
//        } while (p.gcd(q).compareTo(BigInteger.ONE) != 0);
//        
        BigInteger p = new BigInteger("103033300784852097463390352510794843128360350681133368062754830239157047410033");
        BigInteger q = new BigInteger("67642836507566743365732342115121338583053785622768926609173416187576091523423");
        BigInteger fi;
        fi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
//        do {
//            e = new BigInteger(16, 32, new Random());
//        } while (fi.gcd(e).compareTo(BigInteger.ONE) != 0);
        e = new BigInteger("58013");
        d = e.modInverse(fi);
        m = p.multiply(q);        
    }
    
    private BigInteger encript(BigInteger instr){
        return instr.modPow(e, m);
    }
    
    private BigInteger unencript(BigInteger instr){
        return instr.modPow(d, m);
    }

    public static void main(String[] args) {
        System.exit(new F_BigIntegerRSATest_01().test(args));
    }
}
