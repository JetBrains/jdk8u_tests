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
package org.apache.harmony.test.func.jit.HLO.peel.ExceptionLoop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */

/*
 * Created on 05.07.2006
 */

public class ExceptionLoop extends  MultiCase {

    public static void main(String[] args) {
        System.exit((new ExceptionLoop()).test(args));
    }

    public Result test1() {
        log.info("Start ExceptionLoop1...");
        return run("org.apache.harmony.test.func.jit.HLO.peel.ExceptionLoop.Loop1");
    }
    
    public Result test2() {
        log.info("Start ExceptionLoop2...");
        return run("org.apache.harmony.test.func.jit.HLO.peel.ExceptionLoop.Loop2");
    }
    
    public Result run(String className) {
        try {
            Class test = Class.forName(className);
            Object obj = test.newInstance();
            Method m = test.getMethod("test", null);
            int i = ((Integer) m.invoke(obj, null)).intValue();
            if (i == 500000) {
                return passed();
            } else {
                log.info("Loop execution was broken on interation " + i);
                return failed("");
            }
        } catch (InvocationTargetException e) {
            Throwable cause = e.getTargetException();
            log.info("Unexpected " + cause);
            log.add(cause); 
            return failed("");
        } catch (Throwable e) {
            log.info("Unexpected " + e);
            log.add(e);
            return failed("");
        }
    }
}


