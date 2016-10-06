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
package org.apache.harmony.test.func.jit.HLO.simplify.simplifyAnd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 11.07.2006
 */

public class SimplifyAnd extends MultiCase {

    public static void main(String[] args) {
        log.info("Start SimplifyAnd test ...");
        log.info("Check1: s & s -> s");
        log.info("Check2: s & 0 -> 0, 0 & s -> 0");
        log.info("Check3: s & 0xf..f -> s,  0xf..f &s -> s");
        System.exit((new SimplifyAnd()).test(args));
    }

    public Result testLong() {
        log.info("Test long ...");
        return run("org.apache.harmony.test.func.jit.HLO.simplify.simplifyAnd.land");
    }
    
    public Result testInt() {
        log.info("Test int ...");
        return run("org.apache.harmony.test.func.jit.HLO.simplify.simplifyAnd.iand");
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

    short varShort = -1;
    
    public Result testShort() {
        log.info("Test short ...");
        
        short result = (short)(varShort & varShort);
        if (result != varShort) 
            return failed("Check1 failed: result != " + varShort);
        
        result = (short)(varShort & 0);
        if (result != 0) 
            return failed("Check2 failed: result != 0");
        
        result = (short)(varShort & 0xffff);
        if (result != varShort) 
            return failed("Check3 failed: result != " + varShort);
        
        return passed();
    }
    
    byte varByte = 5;
    
    public Result testByte() {
        log.info("Test byte ...");
        
        byte result = (byte)(varByte & varByte);
        if (result != varByte) 
            return failed("Check1 failed: result != " + varByte);
        
        result = (byte)(varByte & 0);
        if (result != 0) 
            return failed("Check2 failed: result != 0");
        
        result = (byte)(varByte & 0xff);
        if (result != varByte) 
            return failed("Check3 failed: result != " + varByte);
        
        return passed();
    }
}


