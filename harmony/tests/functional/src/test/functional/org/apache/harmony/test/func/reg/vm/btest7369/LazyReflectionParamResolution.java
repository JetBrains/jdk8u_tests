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
package org.apache.harmony.test.func.reg.vm.btest7369;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Reflection is impossible if some parameter class is missing,
 * VM reports NoClassDefFoundError on every attempt to get any method
 *
 */
public class LazyReflectionParamResolution extends MultiCase {

    static Logger log = Logger.global;
    
    public Result testMethod() {    
        try {
            Object[] param = new Object[]{new PartiallyResolvable()};
            Method m = PartiallyResolvable.class.getMethod("foo", 
                    new Class[] {Object.class});
            m.invoke(param[0], param);
            log.info("first method passed");
            
            Method m2 = PartiallyResolvable.class.getMethod("bar", 
                    new Class[] {});
            m2.invoke(param[0], null);
            log.info("last method passed");
            
            try {
                Method m3 = PartiallyResolvable.class.getMethod("getMissing", 
                        null);
                m3.getReturnType();
                return new Result(Result.ERROR, 
                   "Misconfiguration: org.apache.harmony.test.func.reg.vm.btest7369.Missing should not be in classpath");
            } catch (NoClassDefFoundError e) {
                e.printStackTrace();
            }
            
            return passed();
        } catch (Throwable t) {
            t.printStackTrace();
            return failed("Unexpected excepton: " + t);
        }        
    }
    
    public Result testCtor() {    
        try {
            Constructor c = PartiallyResolvable.class.getConstructor(
                    new Class[] {Object.class});
            c.newInstance(new Object[] {null});
            log.info("first ctor passed");
            
            Constructor c2 = PartiallyResolvable.class.getConstructor(
                    new Class[] {});
            c2.newInstance(null);
            log.info("last ctor passed");
            
            try {
                Constructor[] all = PartiallyResolvable.class.getConstructors();
                for (int i = 0; i < all.length; i++) {
                    all[i].getParameterTypes();
                }
                return new Result(Result.ERROR, 
                   "Misconfiguration: org.apache.harmony.test.func.reg.vm.btest7369.Missing should not be in classpath");
            } catch (NoClassDefFoundError e) {
                e.printStackTrace();
            }
            
            return passed();
        } catch (Throwable t) {
            t.printStackTrace();
            return failed("Unexpected excepton: " + t);
        }        
    }
    
    public static void main(String[] args) {
        System.exit(new LazyReflectionParamResolution().test(args));
    }
}

class PartiallyResolvable {
    public PartiallyResolvable() {}
    public PartiallyResolvable(Missing m) {}
    public PartiallyResolvable(Object obj) {}
    public void foo(Object obj) {}
    public void foo(Missing obj) {}
    public Missing getMissing() {return null;}
    public void setMissing(Missing m){}
    public static int bar() { return 0;}
    public static int bar(Missing m) { return 0;}
}

class Missing {}