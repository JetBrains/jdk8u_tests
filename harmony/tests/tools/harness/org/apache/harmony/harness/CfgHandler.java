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
 * @version $Revision: 1.9 $
 */
package org.apache.harmony.harness;

import java.io.CharArrayWriter;
import java.util.logging.Level;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * It is a handler that used as a super class to parse all harness configuration
 * files
 */
public class CfgHandler extends DefaultHandler {

    protected ConfigIR        theConfig;
    protected String          keyName  = "";
    protected String          keyValue = "";
    protected CharArrayWriter context  = new CharArrayWriter();
    Logging                   log      = Main.getCurCore().getInternalLogger();

    private final String      classID  = "CfgHandler";

    public CfgHandler(ConfigIR curConfig) {
        theConfig = curConfig;
    }

    public void characters(char[] character, int start, int length)
        throws SAXException {
        context.write(character, start, length);
    }

    public void startDocument() throws SAXException {
        log.add(Level.CONFIG, MessageInfo.MSG_PREFIX + classID
            + "\tstartDocument");
    }

    public void endDocument() throws SAXException {
        log.add(Level.CONFIG, MessageInfo.MSG_PREFIX + classID
            + "\tendDocument");
    }
}