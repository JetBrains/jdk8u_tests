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
 * functional
 */
package org.apache.harmony.test.func.api.javax.xml.F_SAXParserTest_02;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.apache.harmony.test.func.share.ScenarioTest;

public class F_SAXParserTest_02 extends ScenarioTest {
    static HashMap sax_map;
    static HashMap args_map;
    static int     count = 0;

    public static void main(String[] args) {
        System.exit(new F_SAXParserTest_02().test(args));
    }

    public int test() {
        try {
            sax_map = new HashMap();
            args_map = new HashMap();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            if (factory.isNamespaceAware() == false) {
                factory.setNamespaceAware(true);
            }
            factory.setValidating(true);
            SAXParser saxparser = factory.newSAXParser();
            File input_file = new File(testArgs[0] + testArgs[1]);
            InputStream in = new FileInputStream(input_file);
            InputSource is = new InputSource(in);
            saxparser.parse(is, handler);
            Args_Map(testArgs);

            for (int i = 0; i < args_map.size(); i++) {
                if (!args_map.get(new Integer(i)).equals(
                    sax_map.get(new Integer(i)))) {
                    return error("SAXParser error  :(input data from xml) != (valid data)");
                }
            }

            return pass("Passed");

        } catch (Exception e) {
            return error("Error in test: " + e.getMessage());
        }
    }

    static DefaultHandler handler = new DefaultHandler() {
                                      public void characters(char[] ch,
                                          int start, int length) {
                                          String testString = new String(ch,
                                              start, length);
                                          if (!testString.trim()
                                              .equalsIgnoreCase("")) {
                                              sax_map.put(new Integer(count),
                                                  testString.trim());
                                              count++;
                                          }
                                      }

                                      public void warning(SAXParseException e)
                                          throws SAXException {
                                          e.printStackTrace();
                                      }

                                      public void error(SAXParseException e)
                                          throws SAXException {
                                          e.printStackTrace();
                                      }

                                  };


    public static void Args_Map(String[] testArgs) {
        for (int i = 0; i < testArgs.length - 2; i++) {
            args_map.put(new Integer(i), testArgs[i + 2]);
        }
    }
}

