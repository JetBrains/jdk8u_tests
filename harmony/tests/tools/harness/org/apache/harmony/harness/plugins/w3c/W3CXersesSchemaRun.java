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
 * @version $Revision: 1.8 $
 */
package org.apache.harmony.harness.plugins.w3c;

import java.io.File;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.StandardParserConfiguration;

import org.xml.sax.SAXParseException;

import org.apache.harmony.share.Result;

public class W3CXersesSchemaRun {

    private String xmlDoc;
    private String xmlSchema;
    private String valid;

    public int runSchema() {
        StandardParserConfiguration config = new StandardParserConfiguration();
        config.setProperty(
            "http://java.sun.org/xml/jaxp/properties/schemaSource", new File(
                xmlSchema));
        config.setFeature(
            "http://apache.org/xml/features/validation/schema-full-checking",
            true);
        config.setFeature("http://apache.org/xml/features/validation/schema",
            true);
        config.setFeature("http://xml.org/sax/features/validation", true);
        TerminateOnErrorHandler errorCollector = new TerminateOnErrorHandler();
        try {
            SAXParser parser = new SAXParser(config);
            parser.setErrorHandler(errorCollector);
            parser.parse(new File(xmlDoc).getAbsolutePath());
            System.err.println("no errors reported");
            return Result.PASS;
        } catch (SAXParseException e) {
            System.err.println(e.getSystemId() + "(" + e.getLineNumber() + ", "
                + e.getColumnNumber() + "): " + e.getMessage());
            return Result.FAIL;
        } catch (Exception e) {
            System.err.println(e.toString());
            return Result.ERROR;
        }
    }

    public int test(String[] args) {
        if (args == null) {
            System.err.println("W3CJAXPSchemaRun: invalid arguments");
            return Result.ERROR;
        }
        if (args.length < 3) {
            System.err.println("W3CJAXPSchemaRun: invalid arguments number "
                + args.length);
            return Result.ERROR;
        }
        xmlSchema = args[0];
        xmlDoc = args[1];
        valid = args[2];
        return runSchema();
    }

    public static void main(String[] args) {
        System.exit(new W3CXersesSchemaRun().test(args));
    }
}

