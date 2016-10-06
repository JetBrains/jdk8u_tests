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

import java.rmi.RemoteException;

/**
 */
public class HelloImpl implements Hello, Comparable {

    private static final long serialVersionUID = 1L;

    /**
     * String which the method sayHello() should returns.
     */
    private String            str              = "hello";

    /**
     * Default constructor.
     */
    public HelloImpl() {
    }

    /**
     * Construct the remote object whose remote method sayHello() will returns
     * specified string.
     * 
     * @param str the string which the sayHello() method should returns.
     */
    public HelloImpl(String str) {
        this.str = str;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.test.func.api.javax.naming.Hello#sayHello()
     */
    public String sayHello() throws RemoteException {
        return str;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        try {
            return (o instanceof Hello) && str.equals(((Hello) o).sayHello())
                ? 0 : -1;
        } catch (RemoteException e) {
            return -1;
        }
    }
}
