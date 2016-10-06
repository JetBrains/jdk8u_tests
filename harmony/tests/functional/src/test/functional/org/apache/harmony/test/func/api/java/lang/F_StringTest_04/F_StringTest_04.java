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
package org.apache.harmony.test.func.api.java.lang.F_StringTest_04;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * scenario test 
 * Parses xml file, counts tags
 */
public class F_StringTest_04 extends ScenarioTest {
    private String  response;
    private HashMap tags     = new HashMap();
    private int     startTag = 0;
    private int     emptyTag = 0;
    private int     endTag   = 0;

    public F_StringTest_04() {
        super();
    }

    /**
     * counts matches in the file
     * 
     * @param source - java.lang.String name of the text file
     * @return true if count, false - otherwise
     */
    protected boolean parse(String source) {
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(
                source));
            String line = new String();
            while (reader.ready()) {
                line = reader.readLine();
                if (line.startsWith("<?") || line.indexOf("<?") != -1) {
                    if (line.endsWith("?>") && line.indexOf("xml", 2) != -1) {
                        continue;
                    } else {
                        response += "Incorrect start (<?xml) of xml-file.\n";
                    }
                }
                if (line.startsWith("<!") || line.indexOf("<!") != -1) {
                    if (line.endsWith(String.valueOf('>'))
                        && line.indexOf("DOCTYPE", 2) != -1) {
                        continue;
                    } else if (line.indexOf("--", 2) != -1) {
                        if (line.indexOf("-->") == -1) {
                            while (reader.ready()) {
                                line = reader.readLine();
                                if (!line.endsWith("-->")
                                    || line.indexOf("-->") == -1) {
                                    continue;
                                } else {
                                    line = line.substring(line
                                        .lastIndexOf("-->"));
                                    break;
                                }
                            }
                        }
                        if (line.endsWith("-->") || line.indexOf("-->") == -1) {
                            continue;
                        } else {
                            line = line.substring(line.lastIndexOf("-->"));
                        }
                    } else {
                        response += "Incorrect start (<!DOCTYPE) of xml-file.\n";
                    }
                }
                char[] letters = line.toCharArray();
                for (int i = 0; i < letters.length; i++) {
                    if ("<".equalsIgnoreCase(String.valueOf(letters[i]))) {
                        StringBuffer tagName = new StringBuffer("");
                        char[] couple = { letters[i], letters[i + 1] };
                        if (line.trim().charAt(1) == '/') {
                            endTag++;
                            while (i < letters.length
                                && !Character.isWhitespace(letters[i])
                                && new Character('>').charValue() != letters[i]) {
                                tagName.append(letters[i++]);
                            }
                            if (tags.containsKey("end|"
                                + new String(tagName).substring(2))) {
                                Object key = "end|"
                                    + new String(tagName).substring(2);
                                int k = (new Integer((String)tags.get(key)))
                                    .intValue() + 1;
                                tags.put("end|"
                                    + new String(tagName).substring(2), "" + k);
                            } else {
                                tags.put("end|"
                                    + new String(tagName).substring(2), "" + 1);
                            }
                        } else if (line.trim().indexOf("/>") != -1) {
                            emptyTag++;
                            while (i < letters.length
                                && !Character.isSpaceChar(letters[i])
                                && '>' != letters[i]
                                && !"/>".equalsIgnoreCase(new String(couple))) {
                                couple[0] = letters[i];
                                couple[1] = letters[i + 1];
                                tagName.append(letters[i++]);
                                if ("/>".equals(new String(couple)))
                                    tagName.deleteCharAt(tagName.length() - 1);
                            }
                            if (tags.containsKey("empty|"
                                + new String(tagName).substring(1))) {
                                Object key = "empty|"
                                    + new String(tagName).substring(1);
                                int k = (new Integer((String)tags.get(key)))
                                    .intValue() + 1;
                                tags.put("empty|"
                                    + new String(tagName).substring(1), "" + k);
                            } else {
                                tags.put("empty|"
                                    + new String(tagName).substring(1), "" + 1);
                            }
                        } else {
                            if (line.indexOf("</") == -1) {
                                startTag++;
                                while (i < letters.length - 1
                                    && !Character.isSpaceChar(letters[i])
                                    && '>' != letters[i]) {
                                    couple[0] = letters[i];
                                    couple[1] = letters[i + 1];
                                    tagName.append(letters[i++]);
                                }
                                if (tags.containsKey("start|"
                                    + new String(tagName).substring(1))) {
                                    Object key = "start|"
                                        + new String(tagName).substring(1);
                                    int k = (new Integer((String)tags.get(key)))
                                        .intValue() + 1;
                                    tags.put("start|"
                                        + new String(tagName).substring(1), ""
                                        + k);
                                } else {
                                    tags.put("start|"
                                        + new String(tagName).substring(1),
                                        "" + 1);
                                }
                            } else {
                                startTag++;
                                while (i < letters.length - 1
                                    && !Character.isSpaceChar(letters[i])
                                    && '>' != letters[i]) {
                                    couple[0] = letters[i];
                                    couple[1] = letters[i + 1];
                                    tagName.append(letters[i++]);
                                }
                                if (tags.containsKey("start|"
                                    + new String(tagName).substring(1))) {
                                    Object key = "start|"
                                        + new String(tagName).substring(1);
                                    int k = (new Integer((String)tags.get(key)))
                                        .intValue() + 1;
                                    tags.put("start|"
                                        + new String(tagName).substring(1), ""
                                        + k);
                                } else {
                                    tags.put("start|"
                                        + new String(tagName).substring(1),
                                        "" + 1);
                                }
                                while (i < letters.length && letters[i] != '<') {
                                    i++;
                                }
                                tagName = new StringBuffer();
                                endTag++;
                                while (i < letters.length
                                    && !Character.isWhitespace(letters[i])
                                    && new Character('>').charValue() != letters[i]) {
                                    tagName.append(letters[i++]);
                                }
                                if (tags.containsKey("end|"
                                    + new String(tagName).substring(2))) {
                                    Object key = "end|"
                                        + new String(tagName).substring(2);
                                    int k = (new Integer((String)tags.get(key)))
                                        .intValue() + 1;
                                    tags.put("end|"
                                        + new String(tagName).substring(2), ""
                                        + k);
                                } else {
                                    tags.put("end|"
                                        + new String(tagName).substring(2),
                                        "" + 1);
                                }
                            }
                        }
                    }
                }
            }
            reader.close();

            if (startTag != endTag) {
                response += "Incorrect xml-file: number of start tags must be equal number of end tags.\n";
            }
            int quantity = 0;
            StringBuffer tagName = new StringBuffer();
            StringBuffer tagStart = new StringBuffer("Start tags = " + startTag
                + ":\n");
            StringBuffer tagEnd = new StringBuffer("End tags = " + endTag
                + ":\n");
            StringBuffer tagEmpty = new StringBuffer("Empty tags = " + emptyTag
                + ":\n");
            for (Iterator it = tags.keySet().iterator(); it.hasNext();) {
                Object key = it.next();
                if ((key.toString().subSequence(0, 5)).toString()
                    .equalsIgnoreCase("start")) {
                    tagStart.append(((String)key).replaceAll("(start\\|)", "")
                        + " = " + (String)tags.get(key) + "\n");
                    quantity++;
                }
                if ((key.toString().subSequence(0, 3)).toString()
                    .equalsIgnoreCase("end"))
                    tagEnd.append(((String)key).replaceFirst("(end\\|)", "")
                        + " = " + (String)tags.get(key) + "\n");
                if ((key.toString().subSequence(0, 5)).toString()
                    .equalsIgnoreCase("empty")) {
                    tagEmpty.append(key.toString().subSequence(6,
                        key.toString().length())
                        + " = " + (String)tags.get(key) + "\n");
                    quantity++;
                }
            }
            tagName.append(tagStart + "\n" + tagEnd + "\n" + tagEmpty + "\n");
            response += "\nIn the file " + (new File(source)).getName() + " "
                + quantity + " different tags.\n\n" + new String(tagName);
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
            return false;
        } catch (IOException fe) {
            fe.printStackTrace();
            return false;
        }
        return true;
    }

    public int test() {
        try {
            if (parse(testArgs[0])) {
                return pass(response);
            } else {
                return fail("Some problems has occured during parsing of source file or not String argument");
            }
        } catch (NullPointerException e) {
            return error("Exception: some of args is empty");
        }
    }

    public static void main(String[] args) {
        System.exit(new F_StringTest_04().test(args));
    }
}