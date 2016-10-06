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
 *  GOAL: the test checks that for each loaded class (from "java.home") invocation of java.lang.reflect methods 
 *             in multiple threads running in parallel does not cause unexpected errors (crashes, hangs, exceptions).
 *
 *  NOTE: see additional description in ClassMultiBase class.
 *
 *  The test does:
 *    1. Reads parameter, which is:
 *            param[0] - number of threads to launch for parallel classes processing
 *
 *     2. Overrides method testContent(Class) in which functionality of java.lang.reflect package is used.
 *
 *     3. The test fails if crash or hang or unexpected runtime exception occurred while processing
 *        in testContent(Class).
 *
 */

package org.apache.harmony.test.reliability.vm.classloading;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericSignatureFormatError;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.Method;
import org.apache.harmony.test.reliability.share.ClassMultiTestBase;

public class ClassReflectionTest extends ClassMultiTestBase{

    public static void main(String[] args){
        System.exit(new ClassReflectionTest().test(args));
    }

    public void initCycle() {
        // do nothing
    }
    public void releaseCycle() {
        // do nothing
    }

    public void testContent(Class cls) {
        Constructor[] ctrs = null;
        try{
            ctrs = cls.getConstructors();
        } catch (SecurityException e){
            // Expected
        }
        if (ctrs != null){
            for (int i=0; i<ctrs.length; i++){
                Annotation[] antns = ctrs[i].getAnnotations();
                for (int j=0; j<antns.length; j++){
                    antns[j].toString();
                }
                ctrs[i].getDeclaringClass();
                ctrs[i].getExceptionTypes();
                try{
                    ctrs[i].getGenericParameterTypes();
                } catch(GenericSignatureFormatError e){
                    // Expected
                } catch(TypeNotPresentException e){
                    // Expected
                } catch(MalformedParameterizedTypeException e){
                    // Expected
                }
                
                ctrs[i].getName();
                try{
                    ctrs[i].getGenericExceptionTypes();
                } catch(GenericSignatureFormatError e){
                    // Expected
                } catch(TypeNotPresentException e){
                    // Expected
                } catch(MalformedParameterizedTypeException e){
                    // Expected
                }
                try{
                    ctrs[i].getGenericParameterTypes();
                } catch(GenericSignatureFormatError e){
                    // Expected
                } catch(TypeNotPresentException e){
                    // Expected
                } catch(MalformedParameterizedTypeException e){
                    // Expected
                }
                
                ctrs[i].getModifiers();
                ctrs[i].getParameterAnnotations();
                ctrs[i].getParameterTypes();
                ctrs[i].isAccessible();
                try{
                    ctrs[i].getTypeParameters();
                } catch (GenericSignatureFormatError e){
                    // Expected
                }
            }
        }
        Field[] flds = null; 
        try {
            flds = cls.getFields();
        } catch (SecurityException e){
            // Expected
        }

        if (flds != null){
            for (int i=0; i< flds.length; i++){
                flds[i].getClass();
                flds[i].getDeclaredAnnotations();
                flds[i].getDeclaringClass();
                try{
                    flds[i].getGenericType();
                } catch(GenericSignatureFormatError e){
                    // Expected
                } catch(TypeNotPresentException e){
                    // Expected
                } catch(MalformedParameterizedTypeException e){
                    // Expected
                }
                    
                flds[i].getModifiers();
                flds[i].getName();
                flds[i].getType();
                flds[i].isAccessible();
                flds[i].isEnumConstant();
                flds[i].isSynthetic();
                flds[i].toGenericString();
                flds[i].toString();
            }
        }

        Method[] mthds = null;
        try {
            mthds = cls.getMethods();
        } catch (SecurityException e){
            // Expected
        }
            
        if (mthds != null){
            for (int i=0; i<mthds.length;i++){
                mthds[i].getExceptionTypes();
                try{
                    mthds[i].getGenericExceptionTypes();
                } catch(GenericSignatureFormatError e){
                    // Expected
                } catch(TypeNotPresentException e){
                    // Expected
                } catch(MalformedParameterizedTypeException e){
                    // Expected
                }
                
                try{
                    mthds[i].getGenericParameterTypes();
                } catch(GenericSignatureFormatError e){
                    // Expected
                } catch(TypeNotPresentException e){
                    // Expected
                } catch(MalformedParameterizedTypeException e){
                    // Expected
                }
                
                try{
                    mthds[i].getGenericReturnType();
                } catch(GenericSignatureFormatError e){
                    // Expected
                } catch(TypeNotPresentException e){
                    // Expected
                } catch(MalformedParameterizedTypeException e){
                    // Expected
                }
                
                mthds[i].getModifiers();
                mthds[i].getName();
                mthds[i].getReturnType();
                mthds[i].getParameterTypes();
                try{
                    mthds[i].getTypeParameters();
                } catch (GenericSignatureFormatError e){
                    // Expected
                }
                mthds[i].isAccessible();
                mthds[i].isBridge();
                mthds[i].isSynthetic();
                mthds[i].isVarArgs();
                mthds[i].toGenericString();
                mthds[i].toString();
            }
        }
        
    }
}