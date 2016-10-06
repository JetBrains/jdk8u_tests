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
/**
 */
/*
 * Created on 27.09.2005
 */
package org.apache.harmony.test.func.api.java.util.share;

import java.util.Comparator;

public class StringComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        if ((o1 == null) && (o2 != null)) {
            return 1;
        } else {
            if ((o1 != null) && (o2 == null)) {
                return -1;
            }
            if ((o1 == null) && (o2 == null)) {
                return 0;
            }
            if (o1 instanceof Integer) {
                return ((Integer) o1).compareTo((Integer) o2);
            }
            if (o1 instanceof String) {
                return ((String) o1).compareTo((String) o2);
            }
            return (((String[]) o1)[0]).compareTo(((String[]) o2)[0]);
        }
    }
}