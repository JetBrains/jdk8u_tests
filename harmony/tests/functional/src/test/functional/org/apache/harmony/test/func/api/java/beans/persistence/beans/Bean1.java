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
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 */
public class Bean1 implements Serializable {
    private String    title;
    private String    subTitle;
    private int[]     ints;
    private Integer[] integers;

    public int[] getInts() {
        return ints;
    }

    public void setInts(int[] ints) {
        this.ints = ints;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        Class class1 = obj.getClass();
        if (class1 != this.getClass())
            return false;
        Field[] fields = class1.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                Object field = fields[i].get(obj);
                if (field == null) {
                    if (fields[i].get(this) == null)
                        continue;
                    else
                        return false;
                }
                if (field.getClass().isArray()) {
                    if (int[].class.equals(field.getClass())) {
                        if (!Arrays.equals((int[])field, (int[])fields[i]
                            .get(this)))
                            return false;
                    } else if (!Arrays.equals((Object[])field,
                        (Object[])fields[i].get(this)))
                        return false;
                    continue;
                }
                if (!field.equals(fields[i].get(this)))
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Integer[] getIntegers() {
        return integers;
    }

    public void setIntegers(Integer[] integers) {
        this.integers = integers;
    }
}