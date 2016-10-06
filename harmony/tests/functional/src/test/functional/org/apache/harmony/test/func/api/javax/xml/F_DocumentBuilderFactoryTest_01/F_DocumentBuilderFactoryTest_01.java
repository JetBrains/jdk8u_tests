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
package org.apache.harmony.test.func.api.javax.xml.F_DocumentBuilderFactoryTest_01;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 */
public class F_DocumentBuilderFactoryTest_01 extends ScenarioTest {

    private Document               document;

    private boolean                is_validating;

    private DocumentBuilderFactory document_builder_factory;

    private DocumentBuilder        builder;

    private File                   input_file;

    private MyErrorHandler         my_error_handler;

    public static int              count;

    public static void main(String[] args) {
        System.exit(new F_DocumentBuilderFactoryTest_01().test(args));
    }

    public void is_validating(boolean b) throws ParserConfigurationException,
        SAXException, IOException {
        document_builder_factory = DocumentBuilderFactory.newInstance();
        is_validating = document_builder_factory.isValidating();
        if (!is_validating) {
            document_builder_factory.setValidating(true);
        }
        builder = document_builder_factory.newDocumentBuilder();
        my_error_handler = new MyErrorHandler();
        builder.setErrorHandler(my_error_handler);
        if (b) {
            count = 1;
            input_file = new File(testArgs[0] + testArgs[1]);
        } else {
            count = 2;
            input_file = new File(testArgs[0] + testArgs[2]);
        }
        document = builder.parse(input_file);
    }

    public int test() {
        try {
            is_validating(true);
            if (MyErrorHandler.flag == 0) {
                is_validating(false);
            }
        } catch (Exception e) {
            return error("Error in test: " + e.getMessage());
        }
        if (!builder.isValidating()) {
            return error("Error in DocumentBuilder.isValidating()");
        }
        if (F_DocumentBuilderFactoryTest_01.count == 1
            && MyErrorHandler.flag == 1) {
            return error("Error validate document (validate: XML + dtd files)");
        }
        if (F_DocumentBuilderFactoryTest_01.count == 2
            && MyErrorHandler.flag == 0) {
            return error("Warning!!!Parser does not use the dtd-file in check XML");
        }
        return pass("Passed");
    }
}


class MyErrorHandler implements ErrorHandler {
    static int flag = 0; 
    public void error(SAXParseException e) {
        if (F_DocumentBuilderFactoryTest_01.count != 2)
            e.printStackTrace();
        flag = 1;
    }

    public void fatalError(SAXParseException e) {
        if (F_DocumentBuilderFactoryTest_01.count != 2)
            e.printStackTrace();
        flag = 1;
    }

    public void warning(SAXParseException e) {
        if (F_DocumentBuilderFactoryTest_01.count != 2)
            e.printStackTrace();
        flag = 1;
    }
}
