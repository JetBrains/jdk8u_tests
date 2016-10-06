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
 * @version $Revision: 1.13 $
 */
package org.apache.harmony.harness.plugins;

import java.io.File;
import java.util.logging.Level;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.apache.harmony.harness.ConfigurationException;
import org.apache.harmony.harness.Logging;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.ParserException;
import org.apache.harmony.harness.TResIR;
import org.apache.harmony.harness.XMLParser;

class ResParser {

    private final String classID = "ResParser";

    private XMLParser    parser;
    private TResIR       resIR;
    private Logging      log     = Main.getCurCore().getInternalLogger();

    public ResParser() {
        try {
            parser = new XMLParser(false);
            parser.setErrorHandler(new ThrErrorHandler());
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the internal representation of test result
     * 
     * @param in the stream to parse
     * @return test result IR
     * @throws exception if can not parse input
     */
    protected TResIR parser(File in) throws ParserException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tparser(): ";
        resIR = new TResIR("Unspecified", "Unspecified", "Unspecified", null);
        ResHandler handler = new ResHandler(resIR);
        try {
            parser.parsefile(handler, in);
        } catch (ConfigurationException e) {
            throw new ParserException(methodLogPrefix + "Cann't parse a data:"
                + e);
        }
        return resIR;
    }

    class ThrErrorHandler implements ErrorHandler {

        private final String classID = "ThrErrorHandler";

        public void warning(SAXParseException exception) throws SAXException {
            log.add(Level.FINE, MessageInfo.MSG_PREFIX + classID
                + "\twarning(): " + exception);
        }

        public void error(SAXParseException exception) throws SAXException {
            log.add(Level.CONFIG, MessageInfo.MSG_PREFIX + classID
                + "\terror(): " + exception);
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            log.add(Level.CONFIG, MessageInfo.MSG_PREFIX + classID
                + "\tfatalError(): " + exception);
            throw exception;
        }
    }
}