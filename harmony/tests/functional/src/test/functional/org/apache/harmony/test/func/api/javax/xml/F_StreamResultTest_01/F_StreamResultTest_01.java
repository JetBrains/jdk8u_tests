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
package org.apache.harmony.test.func.api.javax.xml.F_StreamResultTest_01;

import java.io.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 */
public class F_StreamResultTest_01 extends ScenarioTest {
    private Document       xml_document;
    private Element        root_element;
    private BufferedReader input_stream;
    private StreamResult   output_steam;
    private FileReader     file_reader;

    public static void main(String[] args) {
        System.exit(new F_StreamResultTest_01().test(args));
    }

    public int test() {
        try {
            file_reader = new FileReader(testArgs[0] + testArgs[1]);
            input_stream = new BufferedReader(file_reader);
            output_steam = new StreamResult(testArgs[0] + testArgs[2]);
            XML_configure();
            String str;
            while ((str = input_stream.readLine()) != null) {
                XML_build(str);
            }
            input_stream.close();
            XML_write();
        } catch (TransformerException e) {
            return error("Error in test: " + e.getMessage());
        } catch (ParserConfigurationException e) {
            return error("Error in test: " + e.getMessage());
        } catch (IOException e) {
            return error("Error in test: " + e.getMessage());
        } catch (SAXException e) {
            return error("Error in test: " + e.getMessage());
        }
        return pass("Passed");
    }

    public void XML_configure() throws ParserConfigurationException {
        DocumentBuilderFactory document_builder_factory = DocumentBuilderFactory
            .newInstance();
        DocumentBuilder builder = document_builder_factory.newDocumentBuilder();
        DOMImplementation dom_impl = builder.getDOMImplementation();
        xml_document = dom_impl.createDocument(null, "TABLE", null);
        root_element = xml_document.getDocumentElement();
    }

    public void XML_build(String input_str) throws SAXException {
        String[] elements = input_str.split(":");
        Element element_0 = xml_document.createElement("TEAM");

        Element element_1 = xml_document.createElement("NAME");
        Node node_1 = xml_document.createTextNode(elements[0]);
        element_1.appendChild(node_1);

        Element element_2 = xml_document.createElement("GAMES");
        Node node_2 = xml_document.createTextNode(elements[1]);
        element_2.appendChild(node_2);

        Element element_3 = xml_document.createElement("WIN");
        Node node_3 = xml_document.createTextNode(elements[2]);
        element_3.appendChild(node_3);

        Element element_4 = xml_document.createElement("WINOVER");
        Node node_4 = xml_document.createTextNode(elements[3]);
        element_4.appendChild(node_4);

        Element element_5 = xml_document.createElement("DRAW");
        Node node_5 = xml_document.createTextNode(elements[4]);
        element_5.appendChild(node_5);

        Element element_6 = xml_document.createElement("DEFEATOVER");
        Node node_6 = xml_document.createTextNode(elements[5]);
        element_6.appendChild(node_6);

        Element element_7 = xml_document.createElement("DEFEAT");
        Node node_7 = xml_document.createTextNode(elements[6]);
        element_7.appendChild(node_7);

        Element element_8 = xml_document.createElement("DIFFERENCE");
        Node node_8 = xml_document.createTextNode(elements[7]);
        element_8.appendChild(node_8);

        Element element_9 = xml_document.createElement("POINT");
        Node node_9 = xml_document.createTextNode(elements[8]);
        element_9.appendChild(node_9);

        element_0.appendChild(element_1);
        element_0.appendChild(element_2);
        element_0.appendChild(element_3);
        element_0.appendChild(element_4);
        element_0.appendChild(element_5);
        element_0.appendChild(element_6);
        element_0.appendChild(element_7);
        element_0.appendChild(element_8);
        element_0.appendChild(element_9);
        root_element.appendChild(element_0);
    }

    public void XML_write() throws TransformerException,
        TransformerConfigurationException {
        DOMSource dom_source = new DOMSource(xml_document);
        TransformerFactory transformer_factory = TransformerFactory
            .newInstance();
        Transformer transformer = transformer_factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(dom_source, output_steam);
    }
}