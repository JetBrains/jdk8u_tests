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
package org.apache.harmony.test.func.api.javax.xml.F_AttributesImplTest_01;

import java.io.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.sax.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import org.apache.harmony.test.func.share.ScenarioTest;

/**
 */
public class F_AttributesImplTest_01 extends ScenarioTest {
    private BufferedReader     input_stream;
    private StreamResult       output_steam;
    private TransformerHandler transformer_handler;
    private AttributesImpl     attribute;
    private FileReader         file_reader;

    public static void main(String[] args) {
        System.exit(new F_AttributesImplTest_01().test(args));
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
            XML_close();
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

    public void XML_configure() throws ParserConfigurationException,
        TransformerConfigurationException, SAXException {
        SAXTransformerFactory sax_transformer_factory = (SAXTransformerFactory)SAXTransformerFactory
            .newInstance();
        transformer_handler = sax_transformer_factory.newTransformerHandler();
        Transformer serializer = transformer_handler.getTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer_handler.setResult(output_steam);
        transformer_handler.startDocument();
        attribute = new AttributesImpl();
        transformer_handler.startElement("", "", "TABLE", attribute);
    }

    public void XML_build(String input_str) throws SAXException {
        String[] elements = input_str.split(":");
        attribute.clear();
        transformer_handler.startElement("", "", "TEAM", attribute);
        transformer_handler.startElement("", "", "NAME", attribute);
        transformer_handler.characters(elements[0].toCharArray(), 0,
            elements[0].length());
        transformer_handler.endElement("", "", "NAME");

        transformer_handler.startElement("", "", "GAMES", attribute);
        transformer_handler.characters(elements[1].toCharArray(), 0,
            elements[1].length());
        transformer_handler.endElement("", "", "GAMES");

        transformer_handler.startElement("", "", "WIN", attribute);
        transformer_handler.characters(elements[2].toCharArray(), 0,
            elements[2].length());
        transformer_handler.endElement("", "", "WIN");

        transformer_handler.startElement("", "", "WINOVER", attribute);
        transformer_handler.characters(elements[3].toCharArray(), 0,
            elements[3].length());
        transformer_handler.endElement("", "", "WINOVER");

        transformer_handler.startElement("", "", "DRAW", attribute);
        transformer_handler.characters(elements[4].toCharArray(), 0,
            elements[4].length());
        transformer_handler.endElement("", "", "DRAW");

        transformer_handler.startElement("", "", "DEFEATOVER", attribute);
        transformer_handler.characters(elements[5].toCharArray(), 0,
            elements[5].length());
        transformer_handler.endElement("", "", "DEFEATOVER");

        transformer_handler.startElement("", "", "DEFEAT", attribute);
        transformer_handler.characters(elements[6].toCharArray(), 0,
            elements[6].length());
        transformer_handler.endElement("", "", "DEFEAT");

        transformer_handler.startElement("", "", "DIFFERENCE", attribute);
        transformer_handler.characters(elements[7].toCharArray(), 0,
            elements[7].length());
        transformer_handler.endElement("", "", "DIFFERENCE");

        transformer_handler.startElement("", "", "POINT", attribute);
        transformer_handler.characters(elements[8].toCharArray(), 0,
            elements[8].length());
        transformer_handler.endElement("", "", "POINT");
        transformer_handler.endElement("", "", "TEAM");
    }

    public void XML_close() throws SAXException {
        transformer_handler.endElement("", "", "TABLE");
        transformer_handler.endDocument();
    }
}