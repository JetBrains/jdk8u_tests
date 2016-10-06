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

import java.beans.beancontext.BeanContextMembershipEvent;
import java.beans.beancontext.BeanContextSupport;
import java.beans.beancontext.BeanContextChild;

import java.util.Iterator;
import java.util.Collection;

import java.io.ObjectInputStream;
import java.io.IOException;

/**
 */

public class IntermediateBeanContextSupport extends BeanContextSupport {

    /**
     * Override method 
     * @see java.beans.beancontext.BeanContextSupport#getChildBeanContextChild()
     */
    public BeanContextChild testGetChildBeanContextChild(Object child){
            return super.getChildBeanContextChild(child);
    }
    
    /**
     * Override method 
     * @see java.beans.beancontext.BeanContextSupport#bcsChildren()
     */
    public Iterator bcsChildren() {
        return super.bcsChildren();
    }

    /**
     * Override method 
     * @see java.beans.beancontext.BeanContextSupport#childDeserializedHook()
     */    
    public void childDeserializedHook(Object child,    BeanContextSupport.BCSChild bcsc) {
        super.childDeserializedHook(child, bcsc);
    }

    /**
     * Override method 
     * @see java.beans.beancontext.BeanContextSupport#classEquals()
     */
    public boolean testClassEquals(Class first, Class second) {
        return super.classEquals(first, second);
    }

    /**
     * Override method 
     * @see java.beans.beancontext.BeanContextSupport#copyChildren()
     */
    public Object[] testCopyChildren() {
        return super.copyChildren();
    }

    /**
     * Override method 
     * @see java.beans.beancontext.BeanContextSupport#deserialize()
     */
    public void testDeserialize(ObjectInputStream ois, Collection coll)
            throws IOException, ClassNotFoundException {
        super.deserialize(ois, coll);
    }

    /**
     * Override method 
     * @see java.beans.beancontext.BeanContextSupport#fireChildrenAdded()
     */
    public void testFireChildrenAdded(BeanContextMembershipEvent bcme) {
        super.fireChildrenAdded(bcme);
    }

    /**
     * Override method 
     * @see java.beans.beancontext.BeanContextSupport#fireChildrenRemoved()
     */
    public void testFireChildrenRemoved(BeanContextMembershipEvent bcme) {
        super.fireChildrenRemoved(bcme);
    }
}