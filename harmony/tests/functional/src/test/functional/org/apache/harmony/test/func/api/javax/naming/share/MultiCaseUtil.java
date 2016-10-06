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

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.harmony.share.MultiCase;

/**
 */
public class MultiCaseUtil extends MultiCase {

    /**
     * @param o
     */
    public static void echo(Object message) {
        System.out.println(message);
    }

    /**
     * @param message
     */
    public static void echo(boolean message) {
        System.out.println(message);
    }

    /**
     * @param message
     */
    public static void echo(int message) {
        System.out.println(message);
    }

    /**
     * @param message
     */
    public static void echo(long message) {
        System.out.println(message);
    }

    /**
     * @param name
     * @return
     */
    protected String getArg(String name) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < testArgs.length - 1; i++) {
            try {
                if (name.equals(testArgs[i])) {
                    return testArgs[i + 1];
                }
            } catch (Exception e) {
                log.info(e.toString());
                break;
            }
        }
        return null;
    }

    /**
     * @param str
     * @return
     */
    public String[] getCommaSeparatedTokens(String str) {
        if (str == null) {
            return new String[0];
        }
        StringTokenizer st = new StringTokenizer(str, ",");
        ArrayList list = new ArrayList();
        while (st.hasMoreTokens()) {
            String t = st.nextToken().trim();
            if (t.length() > 0) {
                list.add(t);
            }
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
}