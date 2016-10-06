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
package org.apache.harmony.test.func.api.javax.naming.provider.rmi.share;

import java.lang.reflect.Method;
import java.util.Hashtable;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;

/**
 */
public class SimpleCtx implements Context, Referenceable {

    /**
     * Environment properties.
     */
    private Hashtable      env;

    /**
     * Last invoked method.
     */
    public static Method   lastMethodInvoked;

    /**
     * Parameters, that have been passed to the last invoked method.
     */
    public static Object[] lastMethodParams;

    public SimpleCtx(Hashtable env) {
        this.env = env == null ? new Hashtable() : env;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookup(javax.naming.Name)
     */
    public Object lookup(Name name) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("lookup",
                new Class[] { Name.class });
            lastMethodParams = new Object[] { name };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Ok";
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookup(java.lang.String)
     */
    public Object lookup(String name) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("lookup",
                new Class[] { String.class });
            lastMethodParams = new Object[] { name };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Ok";
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
     */
    public void bind(Name name, Object obj) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("bind", new Class[] {
                Name.class, Object.class });
            lastMethodParams = new Object[] { name, obj };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
     */
    public void bind(String name, Object obj) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("bind", new Class[] {
                String.class, Object.class });
            lastMethodParams = new Object[] { name, obj };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
     */
    public void rebind(Name name, Object obj) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("rebind",
                new Class[] { Name.class, Object.class });
            lastMethodParams = new Object[] { name, obj };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
     */
    public void rebind(String name, Object obj) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("rebind",
                new Class[] { String.class, Object.class });
            lastMethodParams = new Object[] { name, obj };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#unbind(javax.naming.Name)
     */
    public void unbind(Name name) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("unbind",
                new Class[] { Name.class });
            lastMethodParams = new Object[] { name };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#unbind(java.lang.String)
     */
    public void unbind(String name) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("unbind",
                new Class[] { String.class });
            lastMethodParams = new Object[] { name };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
     */
    public void rename(Name arg0, Name arg1) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("rename",
                new Class[] { Name.class, Name.class });
            lastMethodParams = new Object[] { arg0, arg1 };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
     */
    public void rename(String arg0, String arg1) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("rename",
                new Class[] { String.class, String.class });
            lastMethodParams = new Object[] { arg0, arg1 };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#list(javax.naming.Name)
     */
    public NamingEnumeration list(Name arg0) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("list",
                new Class[] { Name.class });
            lastMethodParams = new Object[] { arg0 };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#list(java.lang.String)
     */
    public NamingEnumeration list(String arg0) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("list",
                new Class[] { String.class });
            lastMethodParams = new Object[] { arg0 };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#listBindings(javax.naming.Name)
     */
    public NamingEnumeration listBindings(Name arg0) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("listBindings",
                new Class[] { Name.class });
            lastMethodParams = new Object[] { arg0 };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#listBindings(java.lang.String)
     */
    public NamingEnumeration listBindings(String arg0) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("listBindings",
                new Class[] { String.class });
            lastMethodParams = new Object[] { arg0 };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
     */
    public void destroySubcontext(Name arg0) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("destroySubcontext",
                new Class[] { Name.class });
            lastMethodParams = new Object[] { arg0 };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#destroySubcontext(java.lang.String)
     */
    public void destroySubcontext(String arg0) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("destroySubcontext",
                new Class[] { String.class });
            lastMethodParams = new Object[] { arg0 };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#createSubcontext(javax.naming.Name)
     */
    public Context createSubcontext(Name arg0) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("createSubcontext",
                new Class[] { Name.class });
            lastMethodParams = new Object[] { arg0 };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#createSubcontext(java.lang.String)
     */
    public Context createSubcontext(String arg0) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("createSubcontext",
                new Class[] { String.class });
            lastMethodParams = new Object[] { arg0 };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookupLink(javax.naming.Name)
     */
    public Object lookupLink(Name arg0) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("lookupLink",
                new Class[] { Name.class });
            lastMethodParams = new Object[] { arg0 };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Ok";
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookupLink(java.lang.String)
     */
    public Object lookupLink(String arg0) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("lookupLink",
                new Class[] { String.class });
            lastMethodParams = new Object[] { arg0 };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Ok";
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getNameParser(javax.naming.Name)
     */
    public NameParser getNameParser(Name arg0) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("getNameParser",
                new Class[] { Name.class });
            lastMethodParams = new Object[] { arg0 };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getNameParser(java.lang.String)
     */
    public NameParser getNameParser(String arg0) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("getNameParser",
                new Class[] { String.class });
            lastMethodParams = new Object[] { arg0 };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#composeName(javax.naming.Name,
     *      javax.naming.Name)
     */
    public Name composeName(Name arg0, Name arg1) throws NamingException {
        try {
            System.out.println(arg0);
            lastMethodInvoked = SimpleCtx.class.getMethod("composeName",
                new Class[] { Name.class, Name.class });
            lastMethodParams = new Object[] { arg0, arg1 };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CompositeName("Ok");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
     */
    public String composeName(String arg0, String arg1) throws NamingException {
        try {
            lastMethodInvoked = SimpleCtx.class.getMethod("composeName",
                new Class[] { String.class, String.class });
            lastMethodParams = new Object[] { arg0, arg1 };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Ok";
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#addToEnvironment(java.lang.String,
     *      java.lang.Object)
     */
    public Object addToEnvironment(String arg0, Object arg1)
        throws NamingException {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
     */
    public Object removeFromEnvironment(String arg0) throws NamingException {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getEnvironment()
     */
    public Hashtable getEnvironment() throws NamingException {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#close()
     */
    public void close() throws NamingException {
        // TODO Implement this method

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getNameInNamespace()
     */
    public String getNameInNamespace() throws NamingException {
        // TODO Implement this method
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Referenceable#getReference()
     */
    public Reference getReference() throws NamingException {
        final String f = SimpleCtxFactory.class.getName();
        return new Reference(f);
    }

}
