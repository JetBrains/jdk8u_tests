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
package org.apache.harmony.test.func.reg.vm.btest5293;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


/**
 * VM crashes on classloading failure during reflection
 * (Eclipse 3.0.1 crashed when editing 
 * malformed build.xml: extra &lt; character inside tag.)
 * 
 */
public class NotFoundReflectionParam extends MultiCase {
    
    public Result testConstructor() {
        try {
            Constructor[] ctrs = GuineaPig.class.getDeclaredConstructors();
            for (int i = 0; i < ctrs.length; i++) {
                try {
                    ctrs[i].getParameterTypes();
                    ctrs[i].getExceptionTypes();
                    return new Result(Result.ERROR, "Misconfiguration: "
                            + "no Btest5293.Missing* classes should be in classpath");
                } catch (NoClassDefFoundError ok) {}
            }
            return passed();
        } catch (Throwable t) {
            t.printStackTrace();
            return new Result(Result.ERROR, "Unexpected exception: " + t);
        }
    }

    public Result testMethod() {
        try {
            Method[] mtds = GuineaPig.class.getDeclaredMethods();
            for (int i = 0; i < mtds.length; i++) {
                try {
                    mtds[i].getParameterTypes();
                    mtds[i].getExceptionTypes();
                    mtds[i].getReturnType();
                    return new Result(Result.ERROR, "Misconfiguration: "
                            + "no Btest5293.Missing* classes should be in classpath");
                } catch (NoClassDefFoundError ok) {}
            }
            return passed();
        } catch (Throwable t) {
            t.printStackTrace();
            return new Result(Result.ERROR, "Unexpected exception: " + t);
        }    
    }

    public static void main(String[] args) {
        System.exit(new NotFoundReflectionParam().test(args));
    }
}

class GuineaPig {
    
    GuineaPig(Missing1 param) {}
    
    GuineaPig() throws Missing2 {}
    
    void foo(Missing3 param) {}
    
    void bar() throws Missing4 {}
    
    Missing5 buz() {
        return null;
    }
}

class Missing1{}

class Missing2 extends RuntimeException{}

class Missing3{}

class Missing4 extends RuntimeException{}

class Missing5 {}