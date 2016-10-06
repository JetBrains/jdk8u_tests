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

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXParseException;

import org.apache.harmony.share.Result;

public class W3Cxml_testRun {

    private String  xmlDoc;
    private boolean valid;

    protected int runCore() {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setValidating(valid);
        try {
            SAXParser parser = parserFactory.newSAXParser();
            TerminateOnErrorHandler errorCollector = new TerminateOnErrorHandler();
            parser.parse(new File(xmlDoc), errorCollector);
            System.err.println("no errors reported");
            return Result.PASS;
        } catch (SAXParseException e) {
            // the xmlDoc is non-well-formed or invalid
            System.err.println(e.getSystemId() + "(" + e.getLineNumber() + ", "
                + e.getColumnNumber() + "): " + e.getMessage());
            return Result.FAIL;
        } catch (Exception e) {
            // the xmlDoc is non-well-formed or invalid
            System.err.println(e.toString());
            return Result.ERROR;
        }
    }

    public int test(String[] args) {
        if (args == null) {
            System.err.println("JAXPCoreRun: invalid arguments");
            return Result.ERROR;
        }
        if (args.length < 2) {
            System.err.println("JAXPCoreRun: invalid arguments number "
                + args.length);
            return Result.ERROR;
        }
        xmlDoc = args[0];
        valid = args[1].equals("valid");
        return runCore();
    }

    public static void main(String[] args) {
        System.exit(new W3Cxml_testRun().test(args));
    }
}