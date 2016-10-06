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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyXor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 13.07.2006
 */

public class SimplifyXor extends MultiCase {

    public static void main(String[] args) {
        log.info("Start SimplifyXor test ...");
        log.info("Check1: 0 ^ s -> s");
        log.info("Check2: s1 ^ 0 -> s");
        log.info("Check3: 0xf..f ^ s -> not(s)");
        log.info("Check4: s ^ 0xf..f  -> not(s)");
        System.exit((new SimplifyXor()).test(args));
    }

    public Result testLong() {
        log.info("Test long ...");
        return run("org.apache.harmony.test.func.jit.HLO.simplify.simplifyXor.lxor");
    }
    
    public Result testInt() {
        log.info("Test int ...");
        return run("org.apache.harmony.test.func.jit.HLO.simplify.simplifyXor.ixor");
    }
    
    public Result run(String classname) {
        
        try {
            Class test = Class.forName(classname);
            Object obj = test.newInstance();
            Method m = test.getMethod("test", null);
            int i = ((Integer) m.invoke(obj, null)).intValue();
            if (i == 0) return passed();
            else return failed("Check" + i + " failed");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getTargetException();
            log.add(cause); 
            return failed("TEST FAILED: " + cause);
        } catch (Throwable e) {
            log.add(e);
            return failed("TEST FAILED: " + e);
        }
    }
    
  //------------------With narrowing conversion------------------------------//
    
    short varShort = 50;
    short negShort = -50;
    
    public Result testShort() {
        log.info("Test short ...");
        
        short result = (short)(negShort ^ ((short)0));
        if (result != negShort) 
            return failed("Check1 failed: result != " + negShort);
        
        result = (short)(((short)0) ^ varShort);
        if (result != varShort) 
            return failed("Check2 failed: result != " + varShort);
        
        result = (short) (negShort ^ 0xffff);
        if (result != ~negShort) 
            return failed("Check3 failed: result != " + ~negShort);
        
        result = (short)(0xffff ^ varShort);
        if (result != ~varShort) 
            return failed("Check4 failed: result != " + ~varShort);
        
        return passed();
    }
    
    byte varByte = 8;
    byte negByte = -8;
    
    public Result testByte() {
        log.info("Test byte ...");
        
        byte result = (byte)(varByte ^ ((byte)0));
        if (result != varByte) 
            return failed("Check1 failed: result != " + varByte);
        
        result = (byte)(((byte)0) ^ negByte);
        if (result != negByte) 
            return failed("Check2 failed: result != " + negByte);
        
        result = (byte)(varByte ^ 0xff);
        if (result != ~varByte) 
            return failed("Check3 failed: result != " + ~varByte);
        
        result = (byte)(0xff ^ negByte);
        if (result != ~negByte) 
            return failed("Check4 failed: result == " + ~negByte);
        return passed();
    }
}


