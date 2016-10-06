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
package org.apache.harmony.test.func.jit.HLO.simplify.typeConv.NarrowingConv;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 21.07.2006
 */

public class NarrowingConv extends MultiCase {

    public static void main(String[] args) {
        log.info("Start NarrowingConv test ...");
        System.exit((new NarrowingConv()).test(args));
    }
    
  //------------------ Narrowing conversion d2* --------------------------//
    
    public Result testD2F() {
        log.info("Test d2f ...");
        double d = Float.MAX_VALUE+0.999999999999999999999999;
        float result = (float)(d);
        log.info("result=" + result);
        if (result>Float.MAX_VALUE || result<Float.MIN_VALUE) 
            return failed("TEST FAILED: result doesn't lie in float range");
        else return passed();
    }
    
    public Result testD2L() {
        log.info("Test d2l ...");
        double d  = -1.3E15;
        long result = (long)(d);
        log.info("result=" + result);
        if (result != -1300000000000000L) 
            return failed("TEST FAILED: result doesn't lie in long range");
        else return passed();
    }
    
    public Result testD2I() {
        log.info("Test d2i ...");
        double d = -1.7777777E9;
        int result = (int)(d);
        log.info("result=" + result);
        if (result != -1777777700) 
            return failed("TEST FAILED: result doesn't lie in int range");
        else return passed();
    }
    
    public Result testD2S() {
        log.info("Test d2s ...");
        double d  = -70000;
        short result = (short)(d);
        log.info("result=" + result);
        if (result>Short.MAX_VALUE || result<Short.MIN_VALUE) 
            return failed("TEST FAILED: result doesn't lie in short range");
        else return passed();
    }
    
    public Result testD2B() {
        log.info("Test d2b ...");
        double d  = 500.666666;
        byte result = (byte)(d);
        log.info("result=" + result);
        if (result>Byte.MAX_VALUE || result<Byte.MIN_VALUE) 
            return failed("TEST FAILED: result doesn't lie in byte range");
        else return passed();
    }
    
    public Result testD2C() {
        log.info("Test d2c ...");
        double d  = 6000.22;
        char result = (char)(d);
        log.info("result=" + (int)result);
        if (result != 6000)  
            return failed("TEST FAILED: result doesn't lie in char range");
        else return passed();
    }
    
  //------------------ Narrowing conversion f2* --------------------------//
    
    public Result testF2L() {
        log.info("Test f2l ...");
        float f = 2.22E33f;
        int result = (int)(f);
        log.info("result=" + result);
        if (result>Integer.MAX_VALUE || result<Integer.MIN_VALUE) 
            return failed("TEST FAILED: result doesn't lie in long range");
        else return passed();
    }
    
    public Result testF2I() {
        log.info("Test f2i ...");
        float f = -2147483648.333f;
        int result = (int)(f);
        log.info("result=" + result);
        if (result != Integer.MIN_VALUE) 
            return failed("TEST FAILED: result doesn't lie in int range");
        else return passed();
    }
    
    public Result testF2S() {
        log.info("Test f2s ...");
        float f = -100000.1f;
        short result = (short)(f);
        log.info("result=" + result);
        if (result>Short.MAX_VALUE || result<Short.MIN_VALUE) 
            return failed("TEST FAILED: result doesn't lie in short range");
        else return passed();
    }
    
    public Result testF2B() {
        log.info("Test f2b ...");
        float f = -127.7777777f;
        byte result = (byte)(f);
        log.info("result=" + result);
        if (result != -127) 
            return failed("TEST FAILED: result doesn't lie in byte range");
        else return passed();
    }
    
    public Result testF2C() {
        log.info("Test f2c ...");
        float f = 66000.666666f;
        char result = (char)(f);
        log.info("result=" + (int)result);
        if (result>Character.MAX_VALUE || result<Character.MIN_VALUE)  
            return failed("TEST FAILED: result doesn't lie in char range");
        else return passed();
    }

  //------------------ Narrowing conversion l2* --------------------------//
    
    public Result testL2I() {
        log.info("Test l2i ...");
        long l = Long.MIN_VALUE;
        int result = (int)(l);
        log.info("result=" + result);
        if (result>Integer.MAX_VALUE || result<Integer.MIN_VALUE) 
            return failed("TEST FAILED: result doesn't lie in int range");
        else return passed();
    }
    
    public Result testL2S() {
        log.info("Test l2s ...");
        long l = -70000;
        short result = (short)(l);
        log.info("result=" + result);
        if (result>Short.MAX_VALUE || result<Short.MIN_VALUE) 
            return failed("TEST FAILED: result doesn't lie in short range");
        else return passed();
    }
    
    public Result testL2B() {
        log.info("Test l2b ...");
        long l = 500;
        byte result = (byte)(l);
        log.info("result=" + result);
        if (result>Byte.MAX_VALUE || result<Byte.MIN_VALUE) 
            return failed("TEST FAILED: result doesn't lie in byte range");
        else return passed();
    }
    
    public Result testL2C() {
        log.info("Test l2c ...");
        long l = 66000;
        char result = (char)(l);
        log.info("result=" + (int)result);
        if (result>Character.MAX_VALUE || result<Character.MIN_VALUE)  
            return failed("TEST FAILED: result doesn't lie in char range");
        else return passed();
    }

  //------------------ Narrowing conversion i2* --------------------------//

    public Result testI2S() {
        log.info("Test i2s ...");
        int integer = 700000;
        short result = (short)(integer);
        log.info("result=" + result);
        if (result>Short.MAX_VALUE || result<Short.MIN_VALUE) 
            return failed("TEST FAILED: result doesn't lie in short range");
        else return passed();
    }
    
    public Result testI2B() {
        log.info("Test i2b ...");
        int integer = -400;
        byte result = (byte)(integer);
        log.info("result=" + result);
        if (result>Byte.MAX_VALUE || result<Byte.MIN_VALUE) 
            return failed("TEST FAILED: result doesn't lie in byte range");
        else return passed();
    }
    
    public Result testI2C() {
        log.info("Test i2c ...");
        int integer = -2;
        char result = (char)(integer);
        log.info("result=" + (int)result);
        if (result>Character.MAX_VALUE || result<Character.MIN_VALUE)  
            return failed("TEST FAILED: result doesn't lie in char range");
        else return passed();
    }
    
  //------------------ Narrowing conversion s2* --------------------------//
    
    public Result testS2C() {
        log.info("Test s2c ...");
        short varShort = -10;
        char result = (char)(varShort);
        log.info("result=" + (int)result);
        if (result>Character.MAX_VALUE || result<Character.MIN_VALUE)  
            return failed("TEST FAILED: result doesn't lie in char range");
        else return passed();
    }
    
    public Result testS2B() {
        log.info("Test s2b ...");
        short varShort = 200;
        byte result = (byte)(varShort);
        log.info("result=" + result);
        if (result>Byte.MAX_VALUE || result<Byte.MIN_VALUE) 
            return failed("TEST FAILED: result doesn't lie in byte range");
        else return passed();
    }
    
  //------------------ Narrowing conversion c2* --------------------------//
    
    public Result testC2S() {
        log.info("Test c2s ...");
        char varChar = 65000;
        short result = (short)(varChar);
        log.info("result=" + (int)result);
        if (result>Short.MAX_VALUE || result<Short.MIN_VALUE)  
            return failed("TEST FAILED: result doesn't lie in short range");
        else return passed();
    }
    
    public Result testC2B() {
        log.info("Test c2b ...");
        char varChar = 1000;
        byte result = (byte)(varChar);
        log.info("result=" + result);
        if (result>Byte.MAX_VALUE || result<Byte.MIN_VALUE) 
            return failed("TEST FAILED: result doesn't lie in byte range");
        else return passed();
    }
    
  //------------------ Narrowing conversion b2* --------------------------//
    
    public Result testB2C() {
        log.info("Test s2c ...");
        byte varByte = -7;
        char result = (char)(varByte);
        log.info("result=" + (int)result);
        if (result>Character.MAX_VALUE || result<Character.MIN_VALUE)  
            return failed("TEST FAILED: result doesn't lie in char range");
        else return passed();
    }
    
    
}


