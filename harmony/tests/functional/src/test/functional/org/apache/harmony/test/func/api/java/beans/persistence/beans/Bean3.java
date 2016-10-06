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
import java.util.Arrays;

/**
 */
public class Bean3 implements Serializable {
    private Bean3 bean3;
    private int[] indexedProperty = new int[10];

    public Bean3 getBean3() {
        return bean3;
    }

    public void setBean3(Bean3 bean3) {
        this.bean3 = bean3;
    }

    /*
     * public boolean equals(Object arg0) { if (arg0 instanceof Bean3) return
     * true; return false; }
     */
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        Bean3 bean3Local = (Bean3)obj;
        if (!Arrays.equals(indexedProperty, bean3Local.indexedProperty))
            return false;
        if (this == this.bean3) {
            if (bean3Local == bean3Local.bean3)
                return true;
            else
                return false;
        }
        if (this.bean3 == null) {
            if (bean3Local.bean3 == null)
                return true;
            else
                return false;
        }
        return false;
    }
}