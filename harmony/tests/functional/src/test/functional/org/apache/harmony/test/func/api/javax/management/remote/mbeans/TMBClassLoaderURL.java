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
package org.apache.harmony.test.func.api.javax.management.remote.mbeans;

import java.net.URL;
import java.net.URLClassLoader;

import org.apache.harmony.share.Test;

public class TMBClassLoaderURL extends URLClassLoader implements
        TMBClassLoaderURLMBean {

    private String number = "11";
    
    private ClassLoader loader;

    public TMBClassLoaderURL(URL source) {
        super(new URL[] { source });
    }

    public TMBClassLoaderURL(URL source, ClassLoader loader) {
        super(new URL[] { source }, loader);
        this.loader = loader;
    }

    public TMBClassLoaderURL() {
        super(new URL[0]);
    }

    public Class findClass(String name) throws ClassNotFoundException {
        Test.log.info("TMBClassLoader find class - " + name);
        return loader.loadClass(name);
        
    }

    public TMBClassLoaderURL(URL[] urls, ClassLoader cl) {
        super(urls, cl);
    }

    public void setSomething(String str) {
        this.number = str;
    }

    public String getMBeanNumber() {
        return number;
    }

    public void doOperation(String arg) {

    }
}
