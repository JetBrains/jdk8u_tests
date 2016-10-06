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
package org.apache.harmony.test.func.reg.vm.btest5991;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;


/**
 * Reflection never reports ExceptionInInitializerError
 * (throws InvocationTargetException instead)
 * 
 */
public class InitializerErrorInReflection extends MultiCase {
    
    public Result testConstructor() {
        try {
            Constructor c = FailInit1.class.getDeclaredConstructor(null);
            c.newInstance(null);
            return new Result(Result.ERROR, "Constructor: no exception thrown");
        } catch (ExceptionInInitializerError eiie) {
            return passed();
        } catch (InvocationTargetException ite) {
            return failed("InvocationTargetException should not wrap"
                    + " ExceptionInInitializerError");
        } catch (Throwable t) {
            return new Result(Result.ERROR, "Unexpected exception: " + t);
        }
    }

    public Result testMethod() {
        try {
            Method m = FailInit2.class.getDeclaredMethod("foo", null);
            m.invoke(null, null);
            return new Result(Result.ERROR, "Method: no exception thrown");
        } catch (ExceptionInInitializerError eiie) {
            return passed();
        } catch (InvocationTargetException ite) {
            return failed("InvocationTargetException should not wrap"
                    + " ExceptionInInitializerError");
        } catch (Throwable t) {
            return new Result(Result.ERROR, "Unexpected exception: " + t);
        }
    }
    
    public Result testFieldGet() {
        try {
            Field f = FailInit3.class.getDeclaredField("bar");
            f.get(null);
            return new Result(Result.ERROR, "Field.get(): no exception thrown");
        } catch (ExceptionInInitializerError eiie) {
            return passed();
        } catch (Throwable t) {
            return new Result(Result.ERROR, "Unexpected exception: " + t);
        }
    }
    
    public Result testFieldSet() {
        try {
            Field f = FailInit4.class.getDeclaredField("buz");
            f.set(null, null);
            return new Result(Result.ERROR, "Field.set(): no exception thrown");
        } catch (ExceptionInInitializerError eiie) {
            return passed();
        } catch (Throwable t) {
            return new Result(Result.ERROR, "Unexpected exception: " + t);
        }
    }

    public static void main(String[] args) {
        System.exit(new InitializerErrorInReflection().test(args));
    }
}

class FailInit1{
    static {
        try {
            throw new RuntimeException("FailInit1 failed in static init");
        } catch(ArithmeticException e){}
    }
    
    FailInit1() {}
}

class FailInit2{
    static {
        try {
            throw new RuntimeException("FailInit2 failed in static init");
        } catch(ArithmeticException e){}
    }
    public static void foo(){}
}

class FailInit3{
    static {
        try {
            throw new RuntimeException("FailInit3 failed in static init");
        } catch(ArithmeticException e){}
    }
    public static Object bar;
}

class FailInit4{
    static {
        try {
            throw new RuntimeException("FailInit4 failed in static init");
        } catch(ArithmeticException e){}
    }
    public static Object buz;
}
