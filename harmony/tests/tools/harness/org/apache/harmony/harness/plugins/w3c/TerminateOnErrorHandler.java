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

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class TerminateOnErrorHandler extends DefaultHandler {

    public void warning(SAXParseException exception) throws SAXException {
        System.err.println("Warning: " + exception);
    }

    public void error(SAXParseException exception) throws SAXException {
        System.err.println("Error: " + exception);
        throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        System.err.println("fatalError: " + exception);
        throw exception;
    }
}