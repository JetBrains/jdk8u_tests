/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Valery Shakurov
 * @version $Revision: 1.6 $
 */
package org.apache.harmony.harness.plugins.variationtests;

import java.util.ArrayList;
import java.util.Set;

import org.apache.harmony.harness.TResIR;

/**
 * An utility class that is used to convert some data structures, perform
 * certain operations on strings, etc.
 */
public class Util {
    private final String classID      = "Util";

    static String[]      stringArr    = new String[0];
    static ArrayList[]   arrayListArr = new ArrayList[0];

    public static String[] toStringArr(ArrayList al) {
        return (String[])al.toArray(stringArr);
    }

    public static String[] toStringArr(Set set) {
        return (String[])set.toArray(stringArr);
    }

    public static ArrayList[] toArrayListArr(ArrayList al) {
        return (ArrayList[])al.toArray(arrayListArr);
    }

    public static String[] chop(String[] arr, int pos) {
        String[] res = new String[arr.length - pos];
        for (int i = 0; i < arr.length - pos; i++) {
            res[i] = arr[pos + i];
        }
        return res;
    }

    //  Escape &, < and > - use general entities
    public static String toXML(String s) {
        return s.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
            ">", "&gt;");
    }

    public static void dump(String t, String[] arr) {
        System.out.println("--------" + t + "-------------");
        for (int i = 0; i < arr.length; i++) {
            System.out.println("#" + i + "='" + arr[i] + "'");
        }
    }

    public static void printStack(TResIR rir, Throwable t) {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stack = t.getStackTrace();
        for (int i = 0; i < stack.length; i++) {
            sb.setLength(0);
            sb.append(i).append(": ");
            if (stack[i].getFileName() != null) {
                sb.append(stack[i].getFileName()).append("[").append(
                    stack[i].getLineNumber()).append("]");
            } else {
                sb.append("(unknown location)");
            }
            sb.append(",").append(stack[i].getClassName());
            sb.append(".").append(stack[i].getMethodName());
            rir.setOutMsg(sb.toString());
        }
    }
}