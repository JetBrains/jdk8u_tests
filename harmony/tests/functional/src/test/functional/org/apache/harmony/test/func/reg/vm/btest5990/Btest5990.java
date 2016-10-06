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
package org.apache.harmony.test.func.reg.vm.btest5990;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 * Reflection does not validate invokation parameters
 *
 */
public class Btest5990 extends MultiCase {

    static Logger log = Logger.global;
    
    public Result testMethod() {    
        try {
            GoodCitizen jim = new GoodCitizen("Jim");
            Method m = GoodCitizen.class.getMethod("sayHello",
                    new Class[] {GoodCitizen.class});
            m.invoke(jim, new Object[] {new GoodCitizen("Alice")});
            log.info("valid invokation passed");
            
            try {
                m.invoke(jim, new Object[] {this});
                return failed("Method accepted invalid argument");
            } catch (IllegalArgumentException ok) {
                ok.printStackTrace();
            }
            
            return passed();
        } catch (Throwable t) {
            t.printStackTrace();
            return new Result(Result.ERROR, "Unexpected excepton: " + t);
        }    
    }
    
    public Result testCtor() {    
        try {
            Constructor c = GoodCitizen.class.getConstructor(
                    new Class[] {String.class});
            c.newInstance(new Object[] {"Alice"});
            log.info("valid invokation passed");
            
            try {
                c.newInstance(new Object[] {this});
                return failed("Constructor accepted invalid argument");
            } catch (IllegalArgumentException ok) {
                ok.printStackTrace();
            }
            
            return passed();
        } catch (Throwable t) {
            t.printStackTrace();
            return new Result(Result.ERROR, "Unexpected excepton: " + t);
        }        
    }
    
    public static void main(String[] args) {
        System.exit(new Btest5990().test(args));
    }
}

class GoodCitizen {
    public GoodCitizen(String s) {}
    public String name() {return "Bob";}
    public void sayHello(GoodCitizen visavis) { 
        System.out.println("Hello, " + visavis.name()); 
    }
}