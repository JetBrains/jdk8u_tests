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
 * @version $Revision: 1.11 $
 */
package org.apache.harmony.harness;

import org.xml.sax.helpers.DefaultHandler;
import java.io.File;
import java.util.logging.Level;

public class CfgParser {

    private XMLParser    parser;
    private Logging      log     = Main.getCurCore().getInternalLogger();

    private final String classID = "CfgParser";

    public CfgParser() {
        try {
            parser = new XMLParser();
            log.add(Level.CONFIG, MessageInfo.MSG_PREFIX + classID + "\t"
                + classID + ": Create XMLParser. Use default values.");
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    public CfgParser(String file, String where) {
        try {
            parser = new XMLParser(file, where);
            log.add(Level.CONFIG, MessageInfo.MSG_PREFIX + classID + "\t"
                + classID + ": Create XMLParser. File is " + file
                + ", place is " + where);
            return;
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fill the current configuration
     * 
     * @param in file to parse
     * @throws ConfigurationException
     */
    void parser(DefaultHandler handler, File in) throws ConfigurationException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tparser(): ";
        try {
            parser.parsefile(handler, in);
            log.add(Level.CONFIG, methodLogPrefix
                + "parse file successfully. file is " + in);
        } catch (ParserException e) {
            if (handler instanceof TSCfgHandler) {
                log.add(Level.INFO, methodLogPrefix
                    + "Can't parse or find files for test suite configuration."
                    + " Use default values. Please check your settings.");
            }
        }
    }
}