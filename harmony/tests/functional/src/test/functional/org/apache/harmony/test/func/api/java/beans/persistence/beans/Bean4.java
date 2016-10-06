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
package org.apache.harmony.test.func.api.java.beans.persistence.beans;

import java.io.Serializable;

/**
 * Bean4 and Bean5 are used only in object_properties_cross_reference method of
 * GeneratingBeans class.
 * 
 */
public class Bean4 implements Serializable {
    private Bean5 bean5;

    public Bean5 getBean5() {
        return bean5;
    }

    public void setBean5(Bean5 bean5) {
        this.bean5 = bean5;
    }

    public boolean equals(Object arg0) {
        try {
            Bean4 bean4 = (Bean4)arg0;
            if(bean4==null) {
                //System.err.println("Bean4 is null");
                //this !=null, but arg0=null, so result is null.
                return false;
            }
            if (this == bean5.getBean4()) {
                if (bean4 == bean4.bean5.getBean4())
                    return true;
                else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}