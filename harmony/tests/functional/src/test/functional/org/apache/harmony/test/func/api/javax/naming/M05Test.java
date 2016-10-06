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
package org.apache.harmony.test.func.api.javax.naming;

import java.util.Hashtable;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.spi.NamingManager;
import javax.naming.spi.StateFactory;

import org.apache.harmony.test.func.api.javax.naming.share.NetContext;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/**
 */
public class M05Test extends MultiCase implements StateFactory {

    /**
     * Indicates whether getStateToBind has been invoked.
     */
    private static boolean isGSTBInvoked;

    /**
     * Test for the constructor NameNotFoundException()
     * 
     * @see javax.naming.NameNotFoundException#NameNotFoundException()
     */
    public Result testNameNotFoundException() {
        NameNotFoundException ex = new NameNotFoundException();
        assertNull("resolvedObj field should be initialized to null", ex
            .getResolvedObj());
        assertNull("explanation field should be initialized to null", ex
            .getExplanation());
        assertNull("remainingName field should be initialized to null", ex
            .getRemainingName());
        assertNull("resolvedName field should be initialized to null", ex
            .getResolvedName());
        return result();
    }

    /**
     * Test for the constructor NamingException()
     * 
     * @see javax.naming.NamingException#NamingException()
     */
    public Result testNamingException() {
        NamingException ex = new NamingException();
        assertNull("resolvedObj field should be initialized to null", ex
            .getResolvedObj());
        assertNull("explanation field should be initialized to null", ex
            .getExplanation());
        assertNull("remainingName field should be initialized to null", ex
            .getRemainingName());
        assertNull("resolvedName field should be initialized to null", ex
            .getResolvedName());
        return result();
    }

    /**
     * Test for the constructor NamingException(java.lang.String)
     * 
     * @see javax.naming.NamingException#NamingException(java.lang.String)
     */
    public Result testNamingExceptionString() {
        NamingException ex = new NamingException("Explanation");
        assertNull("resolvedObj field should be initialized to null", ex
            .getResolvedObj());
        assertNotNull("explanation field should be initialized to Explanation",
            ex.getExplanation());
        assertNull("remainingName field should be initialized to null", ex
            .getRemainingName());
        assertNull("resolvedName field should be initialized to null", ex
            .getResolvedName());
        return result();
    }

    /**
     * Test for the constructor Reference(java.lang.String)
     * 
     * @see javax.naming.Reference#Reference(java.lang.String)
     */
    public Result testReferenceString() {
        Reference ref = new Reference("className");
        assertEquals("className field should be initialized to className",
            "className", ref.getClassName());
        assertNull("factoryClassName field should be initialized to null", ref
            .getFactoryClassName());
        assertNull("factoryClassLocation field should be initialized to null",
            ref.getFactoryClassLocation());
        return result();
    }

    /**
     * Test for the constructor Reference(java.lang.String, java.lang.String,
     * java.lang.String)
     * 
     * @see javax.naming.Reference#Reference(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public Result testReferenceStringStringString() {
        Reference ref = new Reference("className", "factory", "factoryLocation");
        assertEquals("className field should be initialized to className",
            "className", ref.getClassName());
        assertEquals("factory field should be initialized to factory",
            "factory", ref.getFactoryClassName());
        assertEquals(
            "factoryLocation field should be initialized to factoryLocation",
            "factoryLocation", ref.getFactoryClassLocation());
        return result();
    }

    /**
     * Test for the method getStateToBind(java.lang.Object, javax.naming.Name,
     * javax.naming.Context, java.util.Hashtable)
     * 
     * @throws NamingException
     * @throws InvalidNameException
     * @see javax.naming.spi.NamingManager#getStateToBind(java.lang.Object,
     *      javax.naming.Name, javax.naming.Context, java.util.Hashtable)
     */
    public Result testGetStateToBind() throws InvalidNameException,
        NamingException {
        isGSTBInvoked = false;
        Hashtable env = new Hashtable();
        env.put(Context.STATE_FACTORIES, this.getClass().getName());
        Object obj = NamingManager.getStateToBind("Obj", new CompositeName(
            "Name"), new NetContext(new Hashtable()), env);
        assertEquals("Hello", obj);
        assertTrue(isGSTBInvoked);
        return result();
    }

    public Object getStateToBind(Object arg0, Name arg1, Context arg2,
        Hashtable arg3) {
        isGSTBInvoked = true;
        return "Hello";
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.exit(new M05Test().test(args));
    }
}
