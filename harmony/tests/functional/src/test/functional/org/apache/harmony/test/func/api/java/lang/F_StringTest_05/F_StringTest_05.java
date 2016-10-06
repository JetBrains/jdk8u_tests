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
package org.apache.harmony.test.func.api.java.lang.F_StringTest_05;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * 
 * scenario test 
 * Parses file and counts statistic of symbols
 */
public class F_StringTest_05 extends ScenarioTest {
    private String  response;
    private HashMap symbols = new HashMap();

    public F_StringTest_05() {
        super();
    }

    /**
     * counts matches in the file
     * 
     * @param source - java.lang.String name of the text file
     * @return true if count, false - otherwise
     */
    protected boolean parse(String source) {
        StringBuffer symbolName = new StringBuffer();
        try {
            FileInputStream fin = new FileInputStream(source);
            while (fin.available() > 0) {
                byte[] b = new byte[fin.available()];
                fin.read(b);
                String content = new String(b);
                char[] letters = content.toCharArray();
                for (int i = 0; i < letters.length; i++) {
                    if (symbols.containsKey(new Character(letters[i])
                        .toString())) {
                        int k = (new Integer((String)symbols.get(Character
                            .toString(letters[i])))).intValue() + 1;
                        symbols.put(new Character(letters[i]).toString(), ""
                            + k);
                    } else {
                        symbols.put(Character.toString(letters[i]), "" + 1);
                    }
                }
            }
            fin.close();
            for (Iterator it = symbols.keySet().iterator(); it.hasNext();) {
                Object key = it.next();
                String number = (String)symbols.get(key);
                if (((String)key).equals(" "))
                    key = "SPACE";
                if (((String)key).equals("\r"))
                    key = "RETURN";
                if (((String)key).equals("\n"))
                    key = "ENTER";
                if (((String)key).equals("\t"))
                    key = "TAB";
                //                symbolName.append("'" + key.toString() + "' = " + number +
                // "\t\tcode='" +
                // (Integer.getInteger(key.toString())).intValue() + "'\n");
                symbolName
                    .append("'" + key.toString() + "' = " + number + "\n");
            }
            response = "In the file " + (new File(source)).getName() + " "
                + symbols.size() + " different symbols.\n"
                + new String(symbolName);
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
        System.exit(new F_StringTest_05().test(args));
    }
}