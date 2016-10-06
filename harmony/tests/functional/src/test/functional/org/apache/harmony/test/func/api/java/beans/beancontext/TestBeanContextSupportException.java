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
import java.beans.beancontext.BeanContextMembershipListener;
import java.beans.beancontext.BeanContextChild;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class TestBeanContextSupportException extends MultiCase {

    // The BeanContext
    BeanContextSupport context = new BeanContextSupport();

    // The JavaBean
    BeanContextChildSupport bean;

    /**
     * @see java.beans.beancontext.BeanContextSupport#add()
     */
    public Result testAddException() throws Exception {

        bean = null;

        try {
            context.add(bean);
        } catch (java.lang.IllegalArgumentException e) {

            return passed();
        } catch (java.lang.IllegalStateException e) {

            return failed("testAddException");
        }
        return failed("testAddException");
    }

    /**
     * @see java.beans.beancontext.BeanContextSupport#remove()
     */
    public Result testRemoveException() throws Exception {

        bean = null;

        try {
            context.remove(bean);
        } catch (java.lang.IllegalArgumentException e) {

            return passed();
        } catch (java.lang.IllegalStateException e) {

            return failed("testRemoveException");
        }
        return failed("testRemoveException");
    }

    /**
     * @see java.beans.beancontext.BeanContextSupport#addBeanContextMembershipListener()
     */
    public Result testAddBeanContextMembershipListenerException()
            throws Exception {

        BeanContextMembershipListener bcml = null;
        try {
            context.addBeanContextMembershipListener(bcml);

        } catch (NullPointerException e) {

            return passed();
        }
        return failed("testAddBeanContextMembershipListenerException");
    }

    /**
     * @see java.beans.beancontext.BeanContextSupport#removeBeanContextMembershipListener()
     */
    public Result testRemoveBeanContextMembershipListenerException()
            throws Exception {

                BeanContextMembershipListener bcml = null;
        try {
            context.removeBeanContextMembershipListener(bcml);

        } catch (NullPointerException e) {

            return passed();
        }
        return failed("testRemoveBeanContextMembershipListenerException");
    }

    /**
     * @see java.beans.beancontext.BeanContextSupport#getResourceAsStream()
     */
    public Result testGetResourceAsStreamException() throws Exception {

        String name = null;
        BeanContextChild bcc = null;

        try {
            context.getResourceAsStream(name, bcc);

        } catch (NullPointerException e) {

            return passed();
        } catch (IllegalArgumentException e) {
            return failed("testGetResourceAsStreamException");
        }
        return failed("testGetResourceAsStreamException");
    }

    /**
     * @see java.beans.beancontext.BeanContextSupport#getChildBeanContextChild()
     */
    public Result testGetChildBeanContextChild() throws Exception {

        IntermediateBeanContextSupport context = new IntermediateBeanContextSupport();

        TestBeanException bean = new TestBeanException();

        try {
            context.testGetChildBeanContextChild(bean);
        } catch (IllegalArgumentException e) {

            //e.printStackTrace();
            return passed();
        }
        return failed("testGetChildBeanContextChild");
    }

    public static void main(String[] args) {
        try {
            System.exit(new TestBeanContextSupportException().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}