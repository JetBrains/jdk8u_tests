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
package org.apache.harmony.test.func.api.javax.xml.F_DocumentBuilderFactoryTest_03;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 */
public class F_DocumentBuilderFactoryTest_03 extends ScenarioTest {
    static Document document;

    static HashMap  treeXML_map;

    static HashMap  hashXMLNS_map;

    static int      treeXML_count = 0;

    static int      flag          = 0;

    public static void main(String[] args) {
        System.exit(new F_DocumentBuilderFactoryTest_03().test(args));
    }

    public int test() {
        try {
            DocumentBuilderFactory document_builder_factory = DocumentBuilderFactory
                .newInstance();
            document_builder_factory.setValidating(true);
            if (document_builder_factory.isIgnoringElementContentWhitespace() == false) {
                document_builder_factory
                    .setIgnoringElementContentWhitespace(true);
            }
            DocumentBuilder builder = document_builder_factory
                .newDocumentBuilder();
            File input_file = new File(testArgs[0] + testArgs[1]);
            treeXML_map = new HashMap();
            builder.setErrorHandler(new MyErrorHandler());
            document = builder.parse(input_file);
            Element root = document.getDocumentElement();
            treeXML(document);
            for (int i = 0; i < treeXML_map.size(); i++) {
                if (treeXML_map.get(new Integer(i)).equals("")) {
                    return error("Error in test: method setIgnoringElementContentWhitespace error");
                }
            }
            if (flag == 1) {
                return error("ParseException");
            }
            return pass("Passed");

        } catch (Exception e) {
            return error("Error in test: " + e.getMessage());
        }
    }

    public static void treeXML(Node treeXML_node) {
        if (!(treeXML_node instanceof Node)) {
            return;
        }
        Element treeXML_element;
        Document treeXML_document;
        NamedNodeMap treeXML_attributes;
        Node treeXML_node_attribute;
        NodeList treeXML_node_children;
        int treeXML_node_type;

        treeXML_node_type = treeXML_node.getNodeType();

        switch (treeXML_node_type) {
        case Node.DOCUMENT_NODE: {
            treeXML_document = (Document)treeXML_node;
            treeXML_element = treeXML_document.getDocumentElement();
            treeXML(treeXML_element);
            break;
        }
        case Node.ELEMENT_NODE: {
            treeXML_map.put(new Integer(treeXML_count), treeXML_node
                .getNodeName().toString());
            treeXML_count++;
            treeXML_node_children = treeXML_node.getChildNodes();
            if (treeXML_node_children != null) {
                int len = treeXML_node_children.getLength();
                for (int i = 0; i < len; i++) {
                    treeXML(treeXML_node_children.item(i));
                }
            }
            break;
        }
        case Node.TEXT_NODE: {
            treeXML_map.put(new Integer(treeXML_count), treeXML_node
                .getNodeValue().trim());
            treeXML_count++;

            break;
        }

        }
    }
    class MyErrorHandler implements ErrorHandler {
        public void error(SAXParseException e) {
            e.printStackTrace();
            flag = 1;
        }

        public void fatalError(SAXParseException e) {
            e.printStackTrace();
            flag = 1;
        }

        public void warning(SAXParseException e) {
            e.printStackTrace();
            flag = 1;
        }
    }
}

