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
package org.apache.harmony.test.func.api.javax.xml.F_SAXParserTest_03;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 */
public class F_SAXParserTest_03 extends ScenarioTest {
    static HashMap          sax_map;
    static HashMap          args_map;
    static SAXParserFactory factory;
    static SAXParser        saxparser;
    static File             input_file;
    static InputStream      in;
    static InputSource      is;
    static int              count = 0;
    static int              flag  = 0;

    public static void main(String[] args) {
        System.exit(new F_SAXParserTest_03().test(args));
    }

    public void init() throws ParserConfigurationException, SAXException {
        sax_map = new HashMap();
        args_map = new HashMap();
        factory = SAXParserFactory.newInstance();
        if (factory.isNamespaceAware() == false) {
            factory.setNamespaceAware(true);
        }
        factory.setValidating(false);
        saxparser = factory.newSAXParser();

    }

    public void run() throws SAXException, IOException {
        input_file = new File(testArgs[0] + testArgs[1]);
        in = new FileInputStream(input_file);
        is = new InputSource(in);
        is.setSystemId(new File(System.getProperty("user.dir")).toURL()
            .toString());
        saxparser.parse(is, handler);
    }

    public int test() {
        try {
            init();
            run();
            System.out.println(factory.isValidating());
            System.out.println(flag);

            if (flag != 0) {
                return error("Method setValidating(false) works with a mistake");
            }
            return pass("Passed");

        } catch (Exception e) {
            return error("Error in test: " + e.getMessage());
        }
    }

    static DefaultHandler handler = new DefaultHandler() {

                                      public void warning(SAXParseException e)
                                          throws SAXException {
                                          flag++;
                                          //            e.printStackTrace();
                                      }

                                      public void error(SAXParseException e)
                                          throws SAXException {
                                          flag++;
                                          //               e.printStackTrace();
                                      }

                                      public void fatalError(SAXParseException e)
                                          throws SAXException {
                                          flag++;
                                          //               e.printStackTrace();
                                      }
                                  };
}

