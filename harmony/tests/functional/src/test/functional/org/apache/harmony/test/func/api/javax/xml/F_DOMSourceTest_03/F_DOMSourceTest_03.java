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
package org.apache.harmony.test.func.api.javax.xml.F_DOMSourceTest_03;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

import org.xml.sax.SAXException;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 */
public class F_DOMSourceTest_03 extends ScenarioTest {

    public static void main(String[] args) {
        System.exit(new F_DOMSourceTest_03().test(args));
    }

    public int test() {
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document document;

        try {
            factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File(testArgs[0] + testArgs[1]));
            DOMSource source = new DOMSource(document.getDocumentElement());
        } catch (ParserConfigurationException pce) {
            // exception code
            pce.getStackTrace();
        } catch (SAXException se) { // DOM reuses some SAX classes
            // Exception code
            se.getStackTrace();
        } catch (IOException ioe) {
            // Exception code
            ioe.getStackTrace();
        }

        try {
            System.out.println("Hello world");
        } catch (Exception e) {
            return error("Error in test: " + e.getMessage());
        }
        return pass("Passed");
    }

}