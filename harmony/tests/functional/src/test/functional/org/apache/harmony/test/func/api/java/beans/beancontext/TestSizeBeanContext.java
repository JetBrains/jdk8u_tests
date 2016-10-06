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
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class TestSizeBeanContext extends MultiCase {

    /**
     * @see java.beans.beancontext.BeanContextSupport#size()
     */
    public Result testSize() throws Exception {

        // The BeanContext object to be checked by the test
        BeanContextSupport contextForSize = new BeanContextSupport(); 

        // Components that should be added to context
        BeanContextSupport subContext = new BeanContextSupport();                                                                  
        BeanWithBeanContext beanWithContext = new BeanWithBeanContext();        
        BeanContextChildSupport bean = new BeanContextChildSupport();
        
        // Adding the components
        contextForSize.add(subContext);
        contextForSize.add(beanWithContext);
        contextForSize.add(bean);
        
        // It's verified that the BeanContext contains 4 components
        if (contextForSize.size() == 4) {
            return passed();
        } else {
            return failed("testSize() - failed ");
        }
    }
    
    /**
     * @see java.beans.beancontext.BeanContextSupport#isEmpty()
     */
    public Result testIsContextEmpty() throws Exception {

        // The BeanContext object to be checked by the test
        BeanContextSupport context = new BeanContextSupport(); 

        // Components that should be added to context
        BeanContextSupport subContext = new BeanContextSupport();                                                                  
        BeanWithBeanContext beanWithContext = new BeanWithBeanContext();        
        BeanContextChildSupport bean = new BeanContextChildSupport();
        
        // Adding the components
        context.add(subContext);
        context.add(beanWithContext);
        context.add(bean);
        
        // It's verified that the BeanContext isn't empty
        if (!context.isEmpty()) {
            return passed();
        } else {
            return failed("testIsContextEmpty() - failed ");
        }
    }

    public static void main(String[] args) {
        try {
            System.exit(new TestSizeBeanContext().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}