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
 * Created on 26.10.2004
 * Last modification G.Seryakova
 * Last modified on 25.01.2005
 * 
 * This test use Extended Euclid algorithm for finding x and y 
 * for equation ax + by = 1 
 * 
 * scenario
 * 
 */


package org.apache.harmony.test.func.api.java.math.F_BigIntegerExtEuclidTest_01;

import java.math.BigInteger;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 *
 * This test use Extended Euclid algorithm for finding x and y 
 * for equation ax + by = 1 
 */
public class F_BigIntegerExtEuclidTest_01 extends ScenarioTest {
    
    public int test(){
        BigInteger a = new BigInteger(testArgs[0]);
        BigInteger b = new BigInteger(testArgs[1]);
        BigInteger  e[][] = new BigInteger[2][2];
        BigInteger  e1[][] = new BigInteger[2][2];
        BigInteger divideAndRemainder[] = new BigInteger[2];    
        
        e[0][0] = BigInteger.ONE;
        e[0][1] = new BigInteger(0, new byte[] {});
        e[1][0] = BigInteger.valueOf(0);
        e[1][1] = new BigInteger(1, new byte[] {1});        
        divideAndRemainder = a.divideAndRemainder(b); 
        
        if (divideAndRemainder[1].compareTo(a.remainder(b))!=0){
            return fail("Failed. divideAndRemainder() and remainder() return not equal remainder.");            
        }
        
        while(divideAndRemainder[1].compareTo(BigInteger.valueOf(0))!=0){
            e1[0][0] = e[1][0];
            e1[0][1] = e[0][0].subtract(e[0][1].multiply(divideAndRemainder[0]));
            e1[1][0] = e[1][1];
            e1[1][1] = e[1][0].subtract(e[1][1].multiply(divideAndRemainder[0]));
            e[0][0] = e1[0][0];
            e[0][1] = e1[0][1];
            e[1][0] = e1[1][0];
            e[1][1] = e1[1][1];
            a = b;
            b = divideAndRemainder[1];
            divideAndRemainder = a.divideAndRemainder(b); 
        }
        
        if (e[0][1].multiply(new BigInteger(testArgs[0])).add(e[1][1].multiply(new BigInteger(testArgs[1]))).compareTo(BigInteger.ONE)!=0) {
            return fail("Failed.");
        }
        return pass();
    }

    public static void main(String[] args) {
        System.exit(new F_BigIntegerExtEuclidTest_01().test(args));
    }
}
