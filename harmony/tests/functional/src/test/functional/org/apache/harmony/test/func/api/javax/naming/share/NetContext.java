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
package org.apache.harmony.test.func.api.javax.naming.share;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 */
public class NetContext implements Context {

    /**
     * Environment properties.
     */
    private Hashtable    env;

    /**
     * Default context.
     */
    private String       resAddress  = "http://127.0.0.1/";

    /**
     * Resource IP address.
     */
    public static String RESOURCE_IP = "RESOURCE_IP";

    /**
     * HTTP proxy.
     */
    public static String HTTP_PROXY  = "HTTP_PROXY";

    /**
     * Construct new context with specified environment.
     * 
     * @param env environment properties.
     */
    public NetContext(Hashtable env) {
        this.env = (env != null) ? (Hashtable) (env.clone()) : new Hashtable();
        String httpProxy = (String) env.get(HTTP_PROXY);
        if (httpProxy != null) {
            int i = httpProxy.lastIndexOf(':');
            if (i != -1) {
                System.getProperties().put("proxyPort",
                    httpProxy.substring(i + 1));
                httpProxy = httpProxy.substring(0, i);
            }
            System.getProperties().put("proxyHost", httpProxy);
            System.getProperties().put("proxySet", "true");
        }
        String ip = (String) env.get(RESOURCE_IP);
        if (ip != null) {
            resAddress = "http://" + ip + "/";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookup(javax.naming.Name)
     */
    public Object lookup(Name name) throws NamingException {
        return lookup(name.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookup(java.lang.String)
     */
    public Object lookup(String name) throws NamingException {
        try {
            URL url = new URL(resAddress + name);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            String str = "";
            byte[] b = new byte[512];
            int i = -1;
            while ((i = is.read(b)) >= 0) {
                str += new String(b, 0, i);
            }
            is.close();
            conn.disconnect();
            return str;
        } catch (Exception e) {
            throw new NamingException(e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
     */
    public void bind(Name arg0, Object arg1) throws NamingException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
     */
    public void bind(String arg0, Object arg1) throws NamingException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
     */
    public void rebind(Name arg0, Object arg1) throws NamingException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
     */
    public void rebind(String arg0, Object arg1) throws NamingException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#unbind(javax.naming.Name)
     */
    public void unbind(Name arg0) throws NamingException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#unbind(java.lang.String)
     */
    public void unbind(String arg0) throws NamingException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
     */
    public void rename(Name arg0, Name arg1) throws NamingException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
     */
    public void rename(String arg0, String arg1) throws NamingException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#list(javax.naming.Name)
     */
    public NamingEnumeration list(Name arg0) throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#list(java.lang.String)
     */
    public NamingEnumeration list(String arg0) throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#listBindings(javax.naming.Name)
     */
    public NamingEnumeration listBindings(Name arg0) throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#listBindings(java.lang.String)
     */
    public NamingEnumeration listBindings(String arg0) throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
     */
    public void destroySubcontext(Name arg0) throws NamingException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#destroySubcontext(java.lang.String)
     */
    public void destroySubcontext(String arg0) throws NamingException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#createSubcontext(javax.naming.Name)
     */
    public Context createSubcontext(Name arg0) throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#createSubcontext(java.lang.String)
     */
    public Context createSubcontext(String arg0) throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookupLink(javax.naming.Name)
     */
    public Object lookupLink(Name arg0) throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#lookupLink(java.lang.String)
     */
    public Object lookupLink(String arg0) throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getNameParser(javax.naming.Name)
     */
    public NameParser getNameParser(Name arg0) throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getNameParser(java.lang.String)
     */
    public NameParser getNameParser(String arg0) throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#composeName(javax.naming.Name,
     *      javax.naming.Name)
     */
    public Name composeName(Name arg0, Name arg1) throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
     */
    public String composeName(String arg0, String arg1) throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#addToEnvironment(java.lang.String,
     *      java.lang.Object)
     */
    public Object addToEnvironment(String arg0, Object arg1)
        throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
     */
    public Object removeFromEnvironment(String arg0) throws NamingException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Context#getEnvironment()
     */
    public Hashtable getEnvironment() throws NamingException {
        return null;
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
        return null;
    }
}