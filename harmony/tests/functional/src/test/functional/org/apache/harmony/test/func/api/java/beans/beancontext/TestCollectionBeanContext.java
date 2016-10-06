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
public class TestCollectionBeanContext extends MultiCase {

    // The BeanContext object to be checked by the test
    BeanContextSupport context = new BeanContextSupport();

    // Components that should be added to context
    BeanContextChildSupport bean;
    BeanWithBeanContext beanWithContext;
    BeanContextSupport subContext;

    // Vector for bean context's objects 
    Vector vector;

    /**
     * @see java.beans.beancontext.BeanContextSupport#retainAll(Collection)
     * @see java.beans.beancontext.BeanContextSupport#iterator()
     */
    public Result testContainsAllBeanContext()throws Exception {

        bean = new BeanContextChildSupport();
        beanWithContext = new BeanWithBeanContext();
        subContext = new BeanContextSupport();

        vector = new Vector();

        // Adding the components
        context.add(bean);
        context.add(beanWithContext);
        context.add(subContext);

        // Adding the components from bean context to Vector
        for (int i = 0; i < context.size(); i++) {
            vector.add(context.iterator().next());
        }

        // It's verified that objects in the specified Vector are children of this bean context 
        if (context.containsAll(vector)) {
            return passed();
        } else {
            return failed("testCollectionContainsAll() - failed ");
        }
    }

    public static void main(String[] args) {
        try {
            System.exit(new TestCollectionBeanContext().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}