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
package org.apache.harmony.test.func.api.javax.xml.F_InputStreamTest_01;

/**
 * functional 
 */
import java.io.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.*;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class F_InputStreamTest_01 extends ScenarioTest {
    private File               myxsl;
    private StreamSource       myxslsource;
    private StreamResult       output_steam;
    private Transformer        transformer;
    private TransformerFactory transformerfactory;
    private MyXmlReader        myreader;
    private FileInputStream    fileinputstream;
    private InputSource        inputsourse;
    private SAXSource          saxsource;

    public int test() {
        try {
            myxsl = new File(testArgs[0] + testArgs[1]);
            myxslsource = new StreamSource(myxsl);
            output_steam = new StreamResult(testArgs[0] + testArgs[2]);
            transformerfactory = TransformerFactory.newInstance();
            transformer = transformerfactory.newTransformer(myxslsource);
            myreader = new MyXmlReader();
            fileinputstream = new FileInputStream(testArgs[0] + testArgs[3]);
            inputsourse = new InputSource(fileinputstream);
            saxsource = new SAXSource(myreader, inputsourse);
            transformer.transform(saxsource, output_steam);
        } catch (TransformerException e) {
            return error("Error in test: " + e.getMessage());
        } catch (IOException e) {
            return error("Error in test: " + e.getMessage());
        } catch (TransformerFactoryConfigurationError e) {
            return error("Error in test: " + e.getMessage());
        }
        return pass("Passed");
    }

    public static void main(String[] args) {
        System.exit(new F_InputStreamTest_01().test(args));
    }
}

class MyXmlReader implements XMLReader {

    private ContentHandler     contenthandler;
    private InputStream        inputstream;
    private InputStreamReader  inputstreamreader;
    private BufferedReader     bufferreader;
    private String             root_element;
    private AttributesImpl     attribute;
    private StringTokenizer    stringtokenizer;
    private String             string;
    private BufferedReader     input_stream;
    private TransformerHandler transformer_handler;

    private FileReader         file_reader;

    public void parse(InputSource source) throws IOException, SAXException {
        inputstream = source.getByteStream();
        inputstreamreader = new InputStreamReader(inputstream);
        bufferreader = new BufferedReader(inputstreamreader);
        root_element = "table";
        attribute = new AttributesImpl();
        contenthandler.startDocument();
        contenthandler.startElement("", root_element, root_element, attribute);
        String line;
        while ((line = bufferreader.readLine()) != null) {
            contenthandler.startElement("", "team", "team", attribute);
            stringtokenizer = new StringTokenizer(line, "|");

            contenthandler.startElement("", "name", "name", attribute);
            string = stringtokenizer.nextToken();
            contenthandler.characters(string.toCharArray(), 0, string.length());
            contenthandler.endElement("", "name", "name");

            contenthandler.startElement("", "games", "games", attribute);
            string = stringtokenizer.nextToken();
            contenthandler.characters(string.toCharArray(), 0, string.length());
            contenthandler.endElement("", "games", "games");

            contenthandler.startElement("", "win", "win", attribute);
            string = stringtokenizer.nextToken();
            contenthandler.characters(string.toCharArray(), 0, string.length());
            contenthandler.endElement("", "win", "win");

            contenthandler.startElement("", "winover", "winover", attribute);
            string = stringtokenizer.nextToken();
            contenthandler.characters(string.toCharArray(), 0, string.length());
            contenthandler.endElement("", "winover", "winover");

            contenthandler.startElement("", "draw", "draw", attribute);
            string = stringtokenizer.nextToken();
            contenthandler.characters(string.toCharArray(), 0, string.length());
            contenthandler.endElement("", "draw", "draw");

            contenthandler.startElement("", "defeatover", "defeatover",
                attribute);
            string = stringtokenizer.nextToken();
            contenthandler.characters(string.toCharArray(), 0, string.length());
            contenthandler.endElement("", "defeatover", "defeatover");

            contenthandler.startElement("", "defeat", "defeat", attribute);
            string = stringtokenizer.nextToken();
            contenthandler.characters(string.toCharArray(), 0, string.length());
            contenthandler.endElement("", "defeat", "defeat");

            contenthandler.startElement("", "difference", "difference",
                attribute);
            string = stringtokenizer.nextToken();
            contenthandler.characters(string.toCharArray(), 0, string.length());
            contenthandler.endElement("", "difference", "difference");

            contenthandler.startElement("", "point", "point", attribute);
            string = stringtokenizer.nextToken();
            contenthandler.characters(string.toCharArray(), 0, string.length());
            contenthandler.endElement("", "point", "point");

            contenthandler.endElement("", "team", "team");
        }
        contenthandler.endElement("", root_element, root_element);
        contenthandler.endDocument();
    }

    public ContentHandler getContentHandler() {
        return contenthandler;
    }

    public void setContentHandler(ContentHandler aHandler) {
        contenthandler = aHandler;
    }

    public void parse(String systemId) throws IOException, SAXException {
    }

    public void setErrorHandler(ErrorHandler handler) {
    }

    public ErrorHandler getErrorHandler() {
        return null;
    }

    public void setDTDHandler(DTDHandler handler) {
    }

    public DTDHandler getDTDHandler() {
        return null;
    }

    public void setEntityResolver(EntityResolver resolver) {
    }

    public EntityResolver getEntityResolver() {
        return null;
    }

    public void setProperty(String name, Object value) {
    }

    public Object getProperty(String name) {
        return null;
    }

    public void setFeature(String name, boolean value) {
    }

    public boolean getFeature(String name) {
        return false;
    }
}