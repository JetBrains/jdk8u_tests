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
package org.apache.harmony.test.func.api.java.beans.beancontext;

import java.beans.beancontext.BeanContextChildSupport;
import java.beans.beancontext.BeanContextSupport;
import java.util.Vector;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class TestCollectionExceptionBeanContext extends MultiCase {

    // The BeanContext object to be checked by the test
    BeanContextSupport context;

    // Components that should be added to context
    BeanContextSupport subContext;
    BeanWithBeanContext beanWithContext;
    BeanContextChildSupport bean;

    // Vector for bean context's objects
    Vector vector;

    /**
     * Test verified UnsupportedOperationException  
     * 
     * @see java.beans.beancontext.BeanContextSupport#removeAll()
     */
    public Result testRemoveCollectionBeanContext() {

        try {

            context = new BeanContextSupport();
            
            Vector vector = new Vector();
            
            beanWithContext = new BeanWithBeanContext();
            bean = new BeanContextChildSupport();
            BeanContextChildSupport beanClone = new BeanContextChildSupport();
            
            // Adding the components
            context.add(bean);
            context.add(beanWithContext);

            // Adding the components from bean context to Vector
            for (int i = 0; i < context.size(); i++) {
                vector.add(context.iterator().next());
            }
            context.add(beanClone);

            boolean res = context.removeAll(vector);
            
            return failed("Exception is absence");
            
        } catch (java.lang.UnsupportedOperationException e) {
            //e.printStackTrace();
            return passed();
        }catch(Exception e){
            return failed("Wrong type of exception");
        
        }
    }

    /**
     * Test verified UnsupportedOperationException
     * 
     * @see java.beans.beancontext.BeanContextSupport#retainAll(Vector)
     */
    public Result testRetainCollectionBeanContext() {
        try {

            context = new BeanContextSupport();
            
            Vector vector = new Vector();
            
            beanWithContext = new BeanWithBeanContext();
            bean = new BeanContextChildSupport();
            BeanContextChildSupport beanClone = new BeanContextChildSupport();

            // Adding the components
            context.add(bean);
            context.add(beanWithContext);

            // Adding the components from bean context to Vector
            for (int i = 0; i < context.size(); i++) {
                vector.add(context.iterator().next());
            }
            context.add(beanClone);

            boolean res = context.retainAll(vector);
            
            return failed("Exception is absence");
            
        } catch (java.lang.UnsupportedOperationException e) {
            //e.printStackTrace();
            return passed();
        }catch(Exception e){
            return failed("Wrong type of exception");
        
        }
    }

    /**
     * Test verified UnsupportedOperationException
     * 
     * @see java.beans.beancontext.BeanContextSupport#clear()
     */
    public Result testClearCollectionBeanContext() {
        try {

            context = new BeanContextSupport();

            beanWithContext = new BeanWithBeanContext();
            bean = new BeanContextChildSupport();
            BeanContextChildSupport beanClone = new BeanContextChildSupport();
            
            // Adding the components
            context.add(bean);
            context.add(beanWithContext);
            context.add(beanClone);

            //Clear the children 
            context.clear();

            return failed("Exception is absence");
            
        } catch (java.lang.UnsupportedOperationException e) {
            //e.printStackTrace();
            return passed();
        }catch(Exception e){
            return failed("Wrong type of exception");
        
        }
    }

    public static void main(String[] args) {
        System.exit(new TestCollectionExceptionBeanContext().test(args));
    }
}