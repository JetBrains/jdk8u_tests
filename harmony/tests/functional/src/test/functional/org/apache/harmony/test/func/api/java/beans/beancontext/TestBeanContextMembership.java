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
import java.beans.beancontext.BeanContextMembershipListener;
import java.beans.beancontext.BeanContextSupport;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class TestBeanContextMembership extends MultiCase {

    // The BeanContext object to be checked by the test
    BeanContextSupport context = new BeanContextSupport();

    BeanContextChildSupport bean;

    // Number of added and removed children to the context
    int childrenAdd;

    int childrenRm;

    // Create MembershipLisstener
    BeanContextMembershipListener bml = new BeanContextMembershipListener() {
        public void childrenAdded(BeanContextMembershipEvent bcme) {
            childrenAdd+=bcme.size();
        }

        public void childrenRemoved(BeanContextMembershipEvent bcme) {
            childrenRm+=bcme.size();
        }
    };

    private boolean isNumberOfChildrenCanObserve() {
        int childrenAddOld = childrenAdd;
        int childrenRmOld = childrenRm;

        context.add(bean);
        context.remove(bean);

        return !((childrenAddOld == childrenAdd) & (childrenRmOld == childrenRm));
    }

    protected void setUp() throws Exception {
        bean = new BeanContextChildSupport();
    }

    protected void tearDown() throws Exception {
        bean = null;
    }
    
    /**
     * @see java.beans.beancontext.BeanContextSupport#removeBeanContextMembershipListener(BeanContextMembershipListener)
     */
    public Result testRemoveBeanContextMembershipListener() throws Exception {
    
        context.addBeanContextMembershipListener(bml);
        
        assertTrue("Listener works", isNumberOfChildrenCanObserve());
        
        context.removeBeanContextMembershipListener(bml);
    
        assertFalse("Listener doesn't work", isNumberOfChildrenCanObserve());
        return result();
        
    }

    /**
     * @see java.beans.beancontext.BeanContextSupport#addBeanContextMembershipListener(BeanContextMembershipListener)
     */
    public Result testaAddBeanContextMembershipListener() throws Exception {
    
        context.addBeanContextMembershipListener(bml);
    
        assertTrue("Listener works", isNumberOfChildrenCanObserve());
        return result();
    }

    public static void main(String[] args) {
        try {
            System.exit(new TestBeanContextMembership().test(args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}