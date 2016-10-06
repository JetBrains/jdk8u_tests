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
 * @version $Revision: 1.14 $
 */
package org.apache.harmony.harness.plugins;

import java.io.File;
import java.util.logging.Level;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.apache.harmony.harness.ConfigIR;
import org.apache.harmony.harness.ConfigurationException;
import org.apache.harmony.harness.Logging;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.ParserException;
import org.apache.harmony.harness.TestIR;
import org.apache.harmony.harness.XMLParser;

public class TestParser {

    private String    classID = "TestParser";

    private XMLParser parser;
    private TestIR    testIR;
    private ConfigIR  cfg;
    private Logging   log     = Main.getCurCore().getInternalLogger();

    public TestParser() {
        cfg = Main.getCurCore().getConfigIR();
        try {
            String where = Main.getCurCore().getConfigIR()
                .getTestSuiteTestRoot()
                + File.separator + "test.dtd";
            parser = new XMLParser("test.dtd", where);
            parser.setErrorHandler(new TstErrorHandler());
            return;
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the internal representation for the test
     * 
     * @param in the input stream
     * @return TestIR
     * @throws exception if can not parse input
     */
    public TestIR parser(File in) throws ParserException {
        testIR = new TestIR();
        TestHandler handler = new TestHandler(testIR);
        try {
            parser.parsefile(handler, in);
        } catch (ConfigurationException e) {
            throw new ParserException(e.getMessage());
        }
        return testIR;
    }

    class TstErrorHandler implements ErrorHandler {
        private final String classID = "TstErrorHandler";

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