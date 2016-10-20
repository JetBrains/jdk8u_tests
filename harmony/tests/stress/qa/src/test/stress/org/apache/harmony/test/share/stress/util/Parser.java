/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    

package org.apache.harmony.test.share.stress.util;

import java.util.List;
import java.util.LinkedList;
import java.util.StringTokenizer;
import org.apache.harmony.test.share.stress.ReliabilityRunner;

/**
 * Parser for string of activities.
 * 
 * @author Vladimir Nenashev, Alexander Shipilov
 * @version $Revision: 1.5 $
 * 
 */
public abstract class Parser {

    /**
     * Generates list of packages from String of activities to put near class
     * names.
     * 
     * @param params
     *            String of parameters
     * @return String of parameters without imports
     */
    public static String generatePackageList(String params) {
        String paramsWithoutImports = "";
        int importCount = 2;
        StringTokenizer st = new StringTokenizer(params);

        while (st.hasMoreTokens()) {
            String current = st.nextToken();
            if ("import".equals(current)) {
                importCount++;
                st.nextToken();
            }
        }

        st = new StringTokenizer(params);
        ReliabilityRunner.packageList = new String[importCount];
        // default packages
        ReliabilityRunner.packageList[0] = "org.apache.harmony.test.share.stress";
        ReliabilityRunner.packageList[1] = "";
        importCount = 2;
        while (st.hasMoreTokens()) {
            String current = st.nextToken();
            if ("import".equals(current)) {
                ReliabilityRunner.packageList[importCount++] = st.nextToken();
            } else {
                paramsWithoutImports = paramsWithoutImports.concat(current
                        + " ");
            }
        }

        return paramsWithoutImports;
    }

    /**
     * Parses a paramter string according to the folloiwng grammar.<br />
     * 
     * &lt;LoopDecorator&gt; ::= * | * &lt;Number&gt;
     * <br />
     * 
     * &lt;ThreadDecorator&gt; ::= & | & &lt;Number&gt;
     * <br />
     * 
     * &lt;ClassToRun&gt; ::= &lt;ClassName&gt; { &lt;ParameterString&gt; } |
     * &lt;ClassToRun&gt; &lt;LoopDecorator&gt; |
     * &lt;ClassToRun&gt; &lt;ThreadDecorator&gt;
     * <br />
     * 
     * &lt;Parameter&gt; ::= &lt;ClassToRun&gt; |
     * import &lt;PackageName&gt; | logLevel &lt;LogLevelValue&gt;  
     * <br />
     * 
     * &lt;ParameterString&gt; ::=  &lt;Parameter&gt; | &lt;ParameterString&gt; &lt;Parameter&gt;    
     * <br />
     * 
     * &lt;LogLevelValue&gt; ::= SEVERE | INFO
     * <br />
     * 
     * @param arg
     *            String of classes and parameters.
     * @return Array of classes to run with parameters
     * @throws ParseErrorException
     */
    public static String[][] generateToRunList(String arg)
            throws ParseErrorException {
        List Args = new LinkedList();
        char[] chars = new char[arg.length()];
        arg.getChars(0, arg.length(), chars, 0);
        int currIndex = 0;
        int startIndex = -1;
        int endIndex = -1;
        boolean ret = false;

        // Skip whitespaces
        while (Character.isWhitespace(chars[currIndex])) {
            currIndex++;
        }

        do {
            startIndex = currIndex;
            // Read class name
            boolean nameEnd = false;

            do {
                if (!Character.isJavaIdentifierStart(chars[currIndex])) {
                    throw new ParseErrorException("Class name expected");
                }
                currIndex++;
                while (Character.isJavaIdentifierPart(chars[currIndex])) {
                    currIndex++;
                }
                if (chars[currIndex] != '.') {
                    nameEnd = true;
                } else {
                    currIndex++;
                }
            } while (!nameEnd);
            endIndex = currIndex - 1;

            Args.add(new String(chars, startIndex, endIndex - startIndex + 1));

            // Skip whitespaces
            while (Character.isWhitespace(chars[currIndex])) {
                currIndex++;
            }

            // get class parameters
            if (chars[currIndex++] != '{') {
                throw new ParseErrorException(
                        "'{' expected after class name ('"
                                + chars[currIndex - 1] + "' found)");
            }
            startIndex = currIndex;
            int braces = 1;
            boolean quotas = false;

            while (currIndex < chars.length && braces > 0) {
                if (!quotas) {
                    if (chars[currIndex] == '{') {
                        braces++;
                    } else if (chars[currIndex] == '}') {
                        if (braces-- <= 0) {
                            throw new ParseErrorException("mismatched braces");
                        }
                    } else if (chars[currIndex] == '"') {
                        quotas = true;
                    }
                } else if (chars[currIndex] == '"') {
                    quotas = false;
                }
                currIndex++;
            }
            if (braces > 0) {
                throw new ParseErrorException("mismatched braces");
            }
            endIndex = currIndex - 2;

            while (currIndex < chars.length
                    && Character.isWhitespace(chars[currIndex])) {
                currIndex++;
            }

            String[] classParams = splitString(new String(chars, startIndex,
                    endIndex - startIndex + 1));
            Args.add(classParams);

            while (currIndex < chars.length
                    && Character.isWhitespace(chars[currIndex])) {
                currIndex++;
            }

            if (currIndex >= chars.length) {
                ret = true;
            }
        } while (!ret);

        int classCount = Args.size() / 2;
        String[][] res = new String[classCount][];
        for (int i = 0; i < classCount; i++) {
            String[] params = (String[]) Args.get(i * 2 + 1);
            res[i] = new String[params.length + 1];
            res[i][0] = (String) Args.get(i * 2);
            for (int j = 0; j < params.length; j++) {
                res[i][j + 1] = params[j];
            }
        }
        return res;
    }

    /**
     * Divide string into array.
     * 
     * @param s
     *            String to split.
     * @return Array of strings.
     * @throws ParseErrorException
     */
    private static String[] splitString(String s) throws ParseErrorException {
        char[] chars = new char[s.length()];
        boolean quotas = false;
        List res = new LinkedList();
        int currIndex = 0;
        int start, end;
        s.getChars(0, s.length(), chars, 0);

        do {
            while (currIndex < chars.length
                    && Character.isWhitespace(chars[currIndex])) {
                currIndex++;
            }
            if (currIndex == chars.length) {
                break;
            }

            if (chars[currIndex] == '"') {
                quotas = true;
                start = ++currIndex;
            } else {
                quotas = false;
                start = currIndex++;
            }
            while (currIndex < chars.length
                    && ((quotas && chars[currIndex] != '"') || (!quotas && !Character
                            .isWhitespace(chars[currIndex])))) {
                currIndex++;
            }
            if (currIndex == chars.length) {
                if (quotas) {
                    throw new ParseErrorException("Mismatched \"");
                } else {
                    end = chars.length - 1;
                    res.add(new String(chars, start, end - start + 1));
                    break;
                }
            } else {
                end = currIndex - 1;
            }
            String str = new String(chars, start, end - start + 1);
            res.add(str);
            currIndex++;
        } while (true);

        String[] str = new String[res.size()];

        for (int i = 0; i < res.size(); i++) {
            str[i] = (String) res.get(i);
        }
        return str;
    }

    /**
     * Exception which is thrown by <code>Generator</code>'s parsing method.
     * 
     * @author Vladimir Nenashev
     * @version $Revision: 1.5 $
     */
    private static class ParseErrorException extends Exception {
        private static final long serialVersionUID = 3461091033735733320L;

        ParseErrorException(String msg) {
            super(msg);
        }
    }
}
