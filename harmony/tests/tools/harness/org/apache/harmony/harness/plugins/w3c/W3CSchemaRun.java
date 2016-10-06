/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Vladimir A. Ivanov
 * @author Serguei I. Katkov
 * @version $Revision: 1.9 $
 */
package org.apache.harmony.harness.plugins.w3c;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.xerces.parsers.XMLGrammarPreparser;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;

import org.apache.harmony.share.Result;

public class W3CSchemaRun {

    private String xmlDoc;
    private String xmlSchema;
    private String valid;

    public int runSchema() {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setValidating(true);
        parserFactory.setNamespaceAware(true);
        try {
            SAXParser parser = parserFactory.newSAXParser();
            parser.setProperty(
                "http://java.sun.org/xml/jaxp/properties/schemaLanguage",
                "http://www.w3.org/2001/XMLSchema");
            parser.setProperty(
                "http://java.sun.org/xml/jaxp/properties/schemaSource",
                new File(xmlSchema));
            TerminateOnErrorHandler errorCollector = new TerminateOnErrorHandler();
            parser.parse(new File(xmlDoc), errorCollector);
            System.err.println("W3CSchemaRun: no errors reported");
            return Result.PASS;
        } catch (Exception e) {
            System.err.println("W3CSchemaRun: unexpected exception " + e);
            e.printStackTrace(System.err);
            return Result.FAIL;
        }
    }

    public int runSchemaAlone() {
        try {
            XMLGrammarPreparser parser = new XMLGrammarPreparser();
            TerminateOnErrorHandler errorCollector = new TerminateOnErrorHandler();
            parser.setErrorHandler(new XMLErrorHandler() {
                public void warning(String domain, String key,
                    XMLParseException exception) throws XMLParseException {
                    System.err.println(exception);
                }

                public void error(String domain, String key,
                    XMLParseException exception) throws XMLParseException {
                    throw exception;
                }

                public void fatalError(String domain, String key,
                    XMLParseException exception) throws XMLParseException {
                    throw exception;
                }
            });
            parser.registerPreparser("http://www.w3.org/2001/XMLSchema", null);
            parser.preparseGrammar("http://www.w3.org/2001/XMLSchema",
                new XMLInputSource(null, new File(xmlSchema).getAbsolutePath(),
                    null));
            System.err.println("no errors reported");
            return Result.PASS;
        } catch (XMLParseException e) {
            System.err.println(e.getLiteralSystemId() + "(" + e.getLineNumber()
                + ", " + e.getColumnNumber() + "): " + e.getMessage());
            return Result.FAIL;
        } catch (Exception e) {
            System.err.println(e.toString());
            return Result.ERROR;
        }
    }

    public int test(String[] args) {
        if (args == null) {
            System.err.println("W3CSchemaRun: invalid arguments");
            return Result.ERROR;
        }
        int tstArgs = args.length - 2; //LogLevel INFO should be ignored. It's
        // always last
        if (tstArgs < 2) {
            System.err.println("W3CSchemaRun: invalid arguments number "
                + args.length);
            return Result.ERROR;
        }
        valid = args[0];
        xmlSchema = args[1];
        int res;
        if (tstArgs > 2) {
            xmlDoc = args[2];
            res = runSchema();
        } else {
            xmlDoc = null;
            res = runSchemaAlone();
        }
        if (valid.equals("invalid")) {
            if (res == Result.PASS) {
                return Result.FAIL;
            } else {
                return Result.PASS;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        System.exit(new W3CSchemaRun().test(args));
    }
}

