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
package org.apache.harmony.test.func.api.javax.naming.event;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.event.EventDirContext;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.NamingListener;

/**
 * Simple implementation of javax.naming.event.EventDirCtx
 * 
 */
public class EventDirCtx implements EventDirContext {

    /**
     * Event listeners.
     */
    private Map       listeners;

    /**
     * Bindings, associated with this context.
     */
    private Map       bindings;

    /**
     * Binding attributes.
     */
    private Map       attributes;

    /**
     * Context environment.
     */
    private Hashtable env;

    /**
     * Construct new context with specified environment.
     */
    public EventDirCtx(Hashtable env) {
        this.env = (env != null) ? (Hashtable) (env.clone()) : new Hashtable();
        listeners = new Hashtable();
        bindings = new TreeMap();
        attributes = new TreeMap();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.event.EventContext#addNamingListener(java.lang.String,
     *      int, javax.naming.event.NamingListener)
     */
    public void addNamingListener(String name, int scope, NamingListener l)
        throws NamingException {
        List list = getListeners(name);
        list.add(l);
        listeners.put(name, list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.event.EventContext#removeNamingListener(javax.naming.event.NamingListener)
     */
    public void removeNamingListener(NamingListener l) throws NamingException {
        Iterator it = listeners.keySet().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            getListeners(name).remove(l);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#bind(java.lang.String,
     *      java.lang.Object, javax.naming.directory.Attributes)
     */
    public void bind(String name, Object obj, Attributes attrs)
        throws NamingException {
        Binding bind = new Binding(name, obj);
        attributes.put(name, attrs);
        fire(name, NamingEvent.OBJECT_ADDED, bind, (Binding) bindings.put(name,
            bind), null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
     */
    public void bind(String name, Object obj) throws NamingException {
        bind(name, obj, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#rebind(java.lang.String,
     *      java.lang.Object, javax.naming.directory.Attributes)
     */
    public void rebind(String name, Object obj, Attributes attrs)
        throws NamingException {
        Binding bind = new Binding(name, obj);
        attributes.put(name, attrs);
        fire(name, NamingEvent.OBJECT_CHANGED, bind, (Binding) bindings.put(
            name, bind), null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
     */
    public void rebind(String name, Object obj) throws NamingException {
        rebind(name, obj, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#unbind(java.lang.String)
     */
    public void unbind(String name) throws NamingException {
        attributes.remove(name);
        fire(name, NamingEvent.OBJECT_REMOVED, null, (Binding) bindings
            .remove(name), null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
     */
    public void rename(String name1, String name2) throws NamingException {
        Binding old = (Binding) bindings.remove(name1);
        if (old == null) {
            throw new NameNotFoundException(name1);
        }

        Binding newBd = new Binding(name2, old.getObject());
        bindings.put(name2, newBd);
        fire(name1, NamingEvent.OBJECT_RENAMED, newBd, old, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookup(java.lang.String)
     */
    public Object lookup(String name) throws NamingException {
        if (name.trim().equals("")) {
            return this;
        }

        Binding b = (Binding) bindings.get(name);
        return b != null ? b.getObject() : null;
    }

    /**
     * @param name
     * @param type
     * @param newBd
     * @param oldBd
     * @param changeInfo
     */
    private void fire(String name, int type, Binding newBd, Binding oldBd,
        Object changeInfo) {

        NamingEvent e = new NamingEvent(this, type, newBd, oldBd, changeInfo);
        List l = getListeners(name);
        for (int i = 0; i < l.size(); i++) {
            e.dispatch((NamingListener) l.get(i));
        }
    }

    /**
     * @param name
     * @return
     */
    private List getListeners(String name) {
        List l = (List) listeners.get(name);
        return l != null ? l : new Vector();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#createSubcontext(java.lang.String,
     *      javax.naming.directory.Attributes)
     */
    public DirContext createSubcontext(String name, Attributes attrs)
        throws NamingException {
        DirContext ctx = new EventDirCtx(env);
        bind(name, ctx, attrs);
        return ctx;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#createSubcontext(javax.naming.Name,
     *      javax.naming.directory.Attributes)
     */
    public DirContext createSubcontext(Name name, Attributes attrs)
        throws NamingException {
        return createSubcontext(name.toString(), attrs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#destroySubcontext(java.lang.String)
     */
    public void destroySubcontext(String name) throws NamingException {
        unbind(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
     */
    public void destroySubcontext(Name arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#rebind(javax.naming.Name,
     *      java.lang.Object, javax.naming.directory.Attributes)
     */
    public void rebind(Name name, Object obj, Attributes attrs)
        throws NamingException {
        rebind(name.toString(), obj, attrs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
     */
    public void rebind(Name arg0, Object arg1) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.event.EventDirContext#addNamingListener(java.lang.String,
     *      java.lang.String, javax.naming.directory.SearchControls,
     *      javax.naming.event.NamingListener)
     */
    public void addNamingListener(String arg0, String arg1,
        SearchControls arg2, NamingListener arg3) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.event.EventDirContext#addNamingListener(javax.naming.Name,
     *      java.lang.String, javax.naming.directory.SearchControls,
     *      javax.naming.event.NamingListener)
     */
    public void addNamingListener(Name arg0, String arg1, SearchControls arg2,
        NamingListener arg3) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.event.EventDirContext#addNamingListener(java.lang.String,
     *      java.lang.String, java.lang.Object[],
     *      javax.naming.directory.SearchControls,
     *      javax.naming.event.NamingListener)
     */
    public void addNamingListener(String arg0, String arg1, Object[] arg2,
        SearchControls arg3, NamingListener arg4) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.event.EventDirContext#addNamingListener(javax.naming.Name,
     *      java.lang.String, java.lang.Object[],
     *      javax.naming.directory.SearchControls,
     *      javax.naming.event.NamingListener)
     */
    public void addNamingListener(Name arg0, String arg1, Object[] arg2,
        SearchControls arg3, NamingListener arg4) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.event.EventContext#targetMustExist()
     */
    public boolean targetMustExist() throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.event.EventContext#addNamingListener(javax.naming.Name,
     *      int, javax.naming.event.NamingListener)
     */
    public void addNamingListener(Name arg0, int arg1, NamingListener arg2)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getAttributes(java.lang.String)
     */
    public Attributes getAttributes(String arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#modifyAttributes(java.lang.String,
     *      int, javax.naming.directory.Attributes)
     */
    public void modifyAttributes(String arg0, int arg1, Attributes arg2)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getAttributes(javax.naming.Name)
     */
    public Attributes getAttributes(Name arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#modifyAttributes(javax.naming.Name,
     *      int, javax.naming.directory.Attributes)
     */
    public void modifyAttributes(Name arg0, int arg1, Attributes arg2)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getSchema(java.lang.String)
     */
    public DirContext getSchema(String name) throws NamingException {
        List l = getListeners(name);
        NamingExceptionEvent e = new NamingExceptionEvent(this,
            new NamingException("Ok"));
        for (int i = 0; i < l.size(); i++) {
            e.dispatch((NamingListener) l.get(i));
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getSchemaClassDefinition(java.lang.String)
     */
    public DirContext getSchemaClassDefinition(String arg0)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getSchema(javax.naming.Name)
     */
    public DirContext getSchema(Name arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getSchemaClassDefinition(javax.naming.Name)
     */
    public DirContext getSchemaClassDefinition(Name arg0)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#modifyAttributes(java.lang.String,
     *      javax.naming.directory.ModificationItem[])
     */
    public void modifyAttributes(String arg0, ModificationItem[] arg1)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#modifyAttributes(javax.naming.Name,
     *      javax.naming.directory.ModificationItem[])
     */
    public void modifyAttributes(Name arg0, ModificationItem[] arg1)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(java.lang.String,
     *      javax.naming.directory.Attributes)
     */
    public NamingEnumeration search(String arg0, Attributes arg1)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(javax.naming.Name,
     *      javax.naming.directory.Attributes)
     */
    public NamingEnumeration search(Name arg0, Attributes arg1)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#bind(javax.naming.Name,
     *      java.lang.Object, javax.naming.directory.Attributes)
     */
    public void bind(Name name, Object obj, Attributes arg2)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getAttributes(java.lang.String,
     *      java.lang.String[])
     */
    public Attributes getAttributes(String arg0, String[] arg1)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#getAttributes(javax.naming.Name,
     *      java.lang.String[])
     */
    public Attributes getAttributes(Name arg0, String[] arg1)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(java.lang.String,
     *      javax.naming.directory.Attributes, java.lang.String[])
     */
    public NamingEnumeration search(String arg0, Attributes arg1, String[] arg2)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(javax.naming.Name,
     *      javax.naming.directory.Attributes, java.lang.String[])
     */
    public NamingEnumeration search(Name arg0, Attributes arg1, String[] arg2)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(java.lang.String,
     *      java.lang.String, javax.naming.directory.SearchControls)
     */
    public NamingEnumeration search(String arg0, String arg1,
        SearchControls arg2) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(javax.naming.Name,
     *      java.lang.String, javax.naming.directory.SearchControls)
     */
    public NamingEnumeration search(Name arg0, String arg1, SearchControls arg2)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.directory.DirContext#search(java.lang.String,
     *      java.lang.String, java.lang.Object[],
     *      javax.naming.directory.SearchControls)
     */
    public NamingEnumeration search(String arg0, String arg1, Object[] arg2,
        SearchControls arg3) throws NamingException {
        throw new OperationNotSupportedException();
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
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#close()
     */
    public void close() throws NamingException {
        listeners.clear();
        attributes.clear();
        bindings.clear();
        env.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getNameInNamespace()
     */
    public String getNameInNamespace() throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getEnvironment()
     */
    public Hashtable getEnvironment() throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#unbind(javax.naming.Name)
     */
    public void unbind(Name arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookupLink(java.lang.String)
     */
    public Object lookupLink(String arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
     */
    public Object removeFromEnvironment(String arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookup(javax.naming.Name)
     */
    public Object lookup(Name arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookupLink(javax.naming.Name)
     */
    public Object lookupLink(Name arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
     */
    public void bind(Name arg0, Object arg1) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#createSubcontext(java.lang.String)
     */
    public Context createSubcontext(String arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#createSubcontext(javax.naming.Name)
     */
    public Context createSubcontext(Name arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
     */
    public void rename(Name arg0, Name arg1) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getNameParser(java.lang.String)
     */
    public NameParser getNameParser(String arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getNameParser(javax.naming.Name)
     */
    public NameParser getNameParser(Name arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#list(java.lang.String)
     */
    public NamingEnumeration list(String arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#listBindings(java.lang.String)
     */
    public NamingEnumeration listBindings(String arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#list(javax.naming.Name)
     */
    public NamingEnumeration list(Name arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#listBindings(javax.naming.Name)
     */
    public NamingEnumeration listBindings(Name arg0) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#addToEnvironment(java.lang.String,
     *      java.lang.Object)
     */
    public Object addToEnvironment(String arg0, Object arg1)
        throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
     */
    public String composeName(String arg0, String arg1) throws NamingException {
        throw new OperationNotSupportedException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#composeName(javax.naming.Name,
     *      javax.naming.Name)
     */
    public Name composeName(Name arg0, Name arg1) throws NamingException {
        throw new OperationNotSupportedException();
    }
}