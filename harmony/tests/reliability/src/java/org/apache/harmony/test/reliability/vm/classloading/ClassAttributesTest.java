/*
 * Copyright 2007 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Aleksey Ignatenko
 * @version $Revision: 1.0 $
 */

/**
 *  GOAL: the test checks that for each loaded class (from "java.home") invocation of java.lang.Class methods
 *             in multiple threads running in parallel does not cause unexpected errors (crashes, hangs, exceptions).
 *
 *  NOTE: see additional description in ClassMultiBase class.
 *
 *  The test does:
 *    1. Reads parameter, which is:
 *            param[0] - number of threads to launch for parallel classes processing
 *
 *     2. Overrides method testContent(Class) in which almost all methods of java.lang.Class are invoked.
 *         Finally, newInstance() is invoked only for "java." classes.
 *
 *     3. The test fails if crash or hang or unexpected runtime exception occurred while invocations of 
 *         java.lang.Class methods in testContent(Class).
 *
 */

package org.apache.harmony.test.reliability.vm.classloading;

import java.lang.reflect.GenericSignatureFormatError;
import java.lang.reflect.MalformedParameterizedTypeException;
import org.apache.harmony.test.reliability.share.ClassMultiTestBase;

public class ClassAttributesTest extends ClassMultiTestBase{

    public static void main(String[] args){
        System.exit(new ClassAttributesTest().test(args));
    }

    public void initCycle() {
        // do nothing
    }
    public void releaseCycle() {
        // do nothing
    }
    
    public void testContent(Class cls) {
        cls.getName();        
        cls.getAnnotations();
        try{
            cls.getClassLoader();
        } catch (SecurityException e){ 
            // Expected
        }
        cls.getComponentType();
        cls.getDeclaredAnnotations();
        cls.getDeclaringClass();
        cls.getEnclosingConstructor();
        cls.getEnumConstants();
        cls.getGenericInterfaces();
        cls.getModifiers();
        
        // check package info
        Package pk = cls.getPackage();
        pk.getName();
        pk.getImplementationTitle();
        pk.getImplementationVendor();
        pk.getImplementationVersion();
        pk.getSpecificationTitle();
        pk.getSpecificationVendor();
        pk.getSpecificationVersion();
        pk.isSealed();
        
        try{
            cls.getProtectionDomain();
        } catch (SecurityException e){
            // Expected
        }

        try{
            cls.getGenericSuperclass();
        } catch(GenericSignatureFormatError e){
            // Expected
        } catch (TypeNotPresentException e){
            // Expected
        } catch (MalformedParameterizedTypeException e){
            // Expected
        }

        try{        
            cls.getProtectionDomain();
        } catch (SecurityException e){
            // Expected
        }
           
        cls.getSigners();
        cls.isAnonymousClass();
        cls.getCanonicalName();
        cls.getSimpleName();
        
        cls.getSuperclass();
        
        try{
            cls.getTypeParameters();
        } catch(GenericSignatureFormatError e){
            // Expected
        }
        cls.hashCode();
        cls.isAnnotation();
        cls.isArray();
        cls.isEnum();
        cls.isInterface();
        cls.isLocalClass();
        cls.isPrimitive();
        cls.isSynthetic();
        cls.toString();

        // make newInstance only for "java." classes to avoid hangs, avoid "awt" and "swing" to avoid XServer up requirement
        if (cls.getName().startsWith("java.") && !cls.getName().contains("awt") && !cls.getName().contains("swing")){
            try {
                cls.newInstance();
            } catch(IllegalAccessException e){
                // Expected
            } catch (InstantiationException e){
                // Expected
            } catch (ExceptionInInitializerError e){
                // Expected
            } catch (SecurityException e){
                // Expected
            }catch (Exception e){
                // Expected - propogated exception from class default constructor
            }
        }       

    }

}