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
package org.apache.harmony.test.func.api.java.beans.boundproperties.auxiliary;

import java.beans.PropertyChangeSupport;

import org.apache.harmony.share.MultiCase;

/**
 * Variables for test are placed here.
 * 
 */

public abstract class Variables extends MultiCase {
    static final int                numberOfCertainProperties = 10;

    protected PropertyChangeSupport vcs;

    protected CertainProperty       certainProperties[];

    protected AllProperties         allProperties;

    /**
     * @return Returns all properties.
     */
    public AllProperties getAllProperties() {
        return allProperties;
    }

    /**
     * Common start up.
     */
    protected void startup(Variables params) {
        if (params == null) {
            vcs = new PropertyChangeSupport(new String("bean source"));
            certainProperties = new CertainProperty[numberOfCertainProperties];
            for (int i = 0; i < certainProperties.length; i++) {
                certainProperties[i] = new CertainProperty("property " + i,
                    this);
            }
            allProperties = new AllProperties(this);
        } else {
            vcs = params.getVcs();
            certainProperties = params.getCertainProperties();
            allProperties = params.getAllProperties();
        }
    }

    /**
     * @return Returns the certain properties.
     */
    public CertainProperty[] getCertainProperties() {
        return certainProperties;
    }

    /**
     * @return Returns the instance of PropertyChangeSupport.
     */
    public PropertyChangeSupport getVcs() {
        return vcs;
    }
}