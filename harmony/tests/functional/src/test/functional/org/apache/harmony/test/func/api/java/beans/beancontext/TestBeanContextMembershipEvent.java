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
import java.beans.beancontext.BeanContextMembershipEvent;
import java.beans.beancontext.BeanContextSupport;

import java.util.Collection;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class TestBeanContextMembershipEvent extends MultiCase {

    // The BeanContextMembershipEvent object to be checked by the test
    BeanContextMembershipEvent event;

    // The BeanContext object
    BeanContextSupport context;

    // Components that should be added to context
    BeanContextChildSupport bean;

    // The ServiceBean object
    ServiceBean sBean;

    /**
     * @see java.beans.beancontext.BeanContextMembershipEvent
     */
    public Result testNullPointerException1() throws Exception {

        context = new BeanContextSupport();
        
        bean = new BeanContextChildSupport();

        // Adding the component
        context.add(bean);

        Object[] array = null;
        try {
            event = new BeanContextMembershipEvent(bean.getBeanContext(), array);
        } catch (java.lang.NullPointerException e) {
            return passed();
        }
        return failed("testNullPointerException1");
    }

    /**
     * @see java.beans.beancontext.BeanContextMembershipEvent
     */
    public Result testNullPointerException2() throws Exception {

        context = new BeanContextSupport();
        bean = new BeanContextChildSupport();

        // Adding the component
        context.add(bean);

        Collection[] array = null;
        try {
            event = new BeanContextMembershipEvent(bean.getBeanContext(), array);
        } catch (java.lang.NullPointerException e) {

            return passed();
        }
        return failed("testNullPointerException2");
    }
    /**
     * @see java.beans.beancontext.BeanContextMembershipEvent#contains()
     */
    public Result testContainsBeanContextMembershipEvent() throws Exception {

        context = new BeanContextSupport();
        bean = new BeanContextChildSupport();
        sBean = new ServiceBean();

        // Adding the component
        context.add(bean);
        context.add(sBean);

        event = new BeanContextMembershipEvent(bean.getBeanContext(), context
                .toArray());

        if ((event.contains(sBean)) || !(event.contains(context)))
            return passed();
        else
            return failed("testContainsBeanContextMembershipEvent");
    }
    
    /**
     * @see java.beans.beancontext.BeanContextSupport#iterator()
     */
    public Result testIteratorBeanContextMembershipEvent() throws Exception {

        context = new BeanContextSupport();
        bean = new BeanContextChildSupport();
        sBean = new ServiceBean();

        // Adding the component
        context.add(bean);
        context.add(sBean);

        event = new BeanContextMembershipEvent(bean.getBeanContext(), context
                .toArray());

        if ((event.iterator() instanceof java.util.Iterator))
            return passed();
        else
            return failed("testIteratorBeanContextMembershipEvent");
    }
    
    /**
     * @see java.beans.beancontext.BeanContextSupport#size()
     */
    public Result testSizeBeanContextMembershipEvent() throws Exception {

        context = new BeanContextSupport();
        bean = new BeanContextChildSupport();
        sBean = new ServiceBean();

        // Adding the component
        context.add(bean);
        context.add(sBean);

        event = new BeanContextMembershipEvent(bean.getBeanContext(), context
                .toArray());
        // System.out.println(event.size());
        if (event.size() == 2)
            return passed();
        else
            return failed("testSizeBeanContextMembershipEvent");
    }
    
    /**
     * @see java.beans.beancontext.BeanContextSupport#toArray()
     */
    public Result testtToArrayBeanContextMembershipEvent() throws Exception {

        context = new BeanContextSupport();
        bean = new BeanContextChildSupport();
        sBean = new ServiceBean();

        // Adding the component
        context.add(bean);
        context.add(sBean);

        event = new BeanContextMembershipEvent(bean.getBeanContext(), context
                .toArray());

        Object[] array;

        array = new Object[context.size()];
        array = event.toArray();

        if ((array[0] instanceof BeanContextChildSupport)
                && (array[1] instanceof BeanContextChildSupport)) {
            return passed();
        } else {
            return failed("testtToArrayBeanContextMembershipEvent()");
        }

    }

    public static void main(String[] args) {
        try {
            System.exit(new TestBeanContextMembershipEvent().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}