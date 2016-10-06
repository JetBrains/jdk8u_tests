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
/*
 *
 */  
package org.apache.harmony.test.func.api.javax.naming.provider.dns;

import java.util.Hashtable;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;

/**
 */
public class TestCtx implements DirContext {

    private Hashtable     env;

    private CompositeName reqName;

    public TestCtx(Hashtable env) throws NamingException {
        this.env = (env != null) ? (Hashtable) (env.clone()) : new Hashtable();
        reqName = new CompositeName("bla-bla-bla");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getAttributes(javax.naming.Name)
     */
    public Attributes getAttributes(Name arg0) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getAttributes(java.lang.String)
     */
    public Attributes getAttributes(String name) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getAttributes(javax.naming.Name,
     *      java.lang.String[])
     */
    public Attributes getAttributes(Name arg0, String[] arg1)
        throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getAttributes(java.lang.String,
     *      java.lang.String[])
     */
    public Attributes getAttributes(String name, String[] arg1)
        throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#modifyAttributes(javax.naming.Name,
     *      int, javax.naming.directory.Attributes)
     */
    public void modifyAttributes(Name arg0, int arg1, Attributes arg2)
        throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#modifyAttributes(java.lang.String,
     *      int, javax.naming.directory.Attributes)
     */
    public void modifyAttributes(String name, int arg1, Attributes arg2)
        throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#modifyAttributes(javax.naming.Name,
     *      javax.naming.directory.ModificationItem[])
     */
    public void modifyAttributes(Name arg0, ModificationItem[] arg1)
        throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#modifyAttributes(java.lang.String,
     *      javax.naming.directory.ModificationItem[])
     */
    public void modifyAttributes(String name, ModificationItem[] arg1)
        throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#bind(javax.naming.Name,
     *      java.lang.Object, javax.naming.directory.Attributes)
     */
    public void bind(Name arg0, Object arg1, Attributes arg2)
        throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#bind(java.lang.String,
     *      java.lang.Object, javax.naming.directory.Attributes)
     */
    public void bind(String name, Object arg1, Attributes arg2)
        throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#rebind(javax.naming.Name,
     *      java.lang.Object, javax.naming.directory.Attributes)
     */
    public void rebind(Name arg0, Object arg1, Attributes arg2)
        throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#rebind(java.lang.String,
     *      java.lang.Object, javax.naming.directory.Attributes)
     */
    public void rebind(String name, Object arg1, Attributes arg2)
        throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#createSubcontext(javax.naming.Name,
     *      javax.naming.directory.Attributes)
     */
    public DirContext createSubcontext(Name arg0, Attributes arg1)
        throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#createSubcontext(java.lang.String,
     *      javax.naming.directory.Attributes)
     */
    public DirContext createSubcontext(String name, Attributes arg1)
        throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getSchema(javax.naming.Name)
     */
    public DirContext getSchema(Name arg0) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getSchema(java.lang.String)
     */
    public DirContext getSchema(String name) throws NamingException {
        throw reqName.compareTo(new CompositeName(name)) == 0
            ? new NamingException("Ok") : new NamingException(
                "RemainingName=\"" + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getSchemaClassDefinition(javax.naming.Name)
     */
    public DirContext getSchemaClassDefinition(Name arg0)
        throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getSchemaClassDefinition(java.lang.String)
     */
    public DirContext getSchemaClassDefinition(String name)
        throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(javax.naming.Name,
     *      javax.naming.directory.Attributes, java.lang.String[])
     */
    public NamingEnumeration search(Name arg0, Attributes arg1, String[] arg2)
        throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(java.lang.String,
     *      javax.naming.directory.Attributes, java.lang.String[])
     */
    public NamingEnumeration search(String name, Attributes arg1, String[] arg2)
        throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(javax.naming.Name,
     *      javax.naming.directory.Attributes)
     */
    public NamingEnumeration search(Name arg0, Attributes arg1)
        throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(java.lang.String,
     *      javax.naming.directory.Attributes)
     */
    public NamingEnumeration search(String name, Attributes arg1)
        throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(javax.naming.Name,
     *      java.lang.String, javax.naming.directory.SearchControls)
     */
    public NamingEnumeration search(Name arg0, String arg1, SearchControls arg2)
        throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(java.lang.String,
     *      java.lang.String, javax.naming.directory.SearchControls)
     */
    public NamingEnumeration search(String name, String arg1,
        SearchControls arg2) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(javax.naming.Name,
     *      java.lang.String, java.lang.Object[],
     *      javax.naming.directory.SearchControls)
     */
    public NamingEnumeration search(Name arg0, String arg1, Object[] arg2,
        SearchControls arg3) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(java.lang.String,
     *      java.lang.String, java.lang.Object[],
     *      javax.naming.directory.SearchControls)
     */
    public NamingEnumeration search(String name, String arg1, Object[] arg2,
        SearchControls arg3) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookup(javax.naming.Name)
     */
    public Object lookup(Name name) throws NamingException {
        if (name.equals(new CompositeName("bla-bla-bla")))
            reqName = new CompositeName("");
        else if (name.equals(new CompositeName("")))
            reqName = new CompositeName("bla-bla-bla");
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookup(java.lang.String)
     */
    public Object lookup(String name) throws NamingException {
        if (new CompositeName(name).equals(new CompositeName("bla-bla-bla")))
            reqName = new CompositeName("");
        else if (new CompositeName(name).equals(new CompositeName("")))
            reqName = new CompositeName("bla-bla-bla");
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
     */
    public void bind(Name arg0, Object arg1) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
     */
    public void bind(String name, Object arg1) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
     */
    public void rebind(Name arg0, Object arg1) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
     */
    public void rebind(String name, Object arg1) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#unbind(javax.naming.Name)
     */
    public void unbind(Name arg0) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#unbind(java.lang.String)
     */
    public void unbind(String name) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
     */
    public void rename(Name arg0, Name arg1) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
     */
    public void rename(String name, String arg1) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#list(javax.naming.Name)
     */
    public NamingEnumeration list(Name arg0) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#list(java.lang.String)
     */
    public NamingEnumeration list(String name) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#listBindings(javax.naming.Name)
     */
    public NamingEnumeration listBindings(Name arg0) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#listBindings(java.lang.String)
     */
    public NamingEnumeration listBindings(String name) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
     */
    public void destroySubcontext(Name arg0) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#destroySubcontext(java.lang.String)
     */
    public void destroySubcontext(String name) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#createSubcontext(javax.naming.Name)
     */
    public Context createSubcontext(Name arg0) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#createSubcontext(java.lang.String)
     */
    public Context createSubcontext(String name) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookupLink(javax.naming.Name)
     */
    public Object lookupLink(Name arg0) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookupLink(java.lang.String)
     */
    public Object lookupLink(String name) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getNameParser(javax.naming.Name)
     */
    public NameParser getNameParser(Name arg0) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getNameParser(java.lang.String)
     */
    public NameParser getNameParser(String name) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#composeName(javax.naming.Name,
     *      javax.naming.Name)
     */
    public Name composeName(Name arg0, Name arg1) throws NamingException {
        throw reqName.equals(arg0) ? new NamingException("Ok")
            : new NamingException("RemainingName=\"" + arg0 + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
     */
    public String composeName(String name, String arg1) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#addToEnvironment(java.lang.String,
     *      java.lang.Object)
     */
    public Object addToEnvironment(String name, Object arg1)
        throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
     */
    public Object removeFromEnvironment(String name) throws NamingException {
        throw reqName.equals(new CompositeName(name)) ? new NamingException(
            "Ok") : new NamingException("RemainingName=\""
            + new CompositeName(name) + "\"");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getEnvironment()
     */
    public Hashtable getEnvironment() throws NamingException {
        return env;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#close()
     */
    public void close() throws NamingException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getNameInNamespace()
     */
    public String getNameInNamespace() throws NamingException {
        return ".";
    }

}