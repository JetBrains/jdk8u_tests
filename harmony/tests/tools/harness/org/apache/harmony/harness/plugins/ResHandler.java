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
 * @author Y.Tokpanov
 * @version $Revision: 1.12 $
 */
package org.apache.harmony.harness.plugins;

import java.io.CharArrayWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.harmony.harness.Logging;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.TResIR;

public class ResHandler extends DefaultHandler {

    private TResIR            theResIR;
    private String            keyName       = "";
    private String            keyValue      = "";
    private HashMap           modifications = new HashMap();
    private ArrayList         runner        = new ArrayList();
    private ArrayList         executor      = new ArrayList();
    private ArrayList         parameters    = new ArrayList();
    private String            toRunName     = "";
    private boolean           inToRun       = false;
    private boolean           cmdAsPlain    = true;
    private boolean           logAsPlain    = true;
    private Logging           log           = Main.getCurCore()
                                                .getInternalLogger();

    protected CharArrayWriter context       = new CharArrayWriter();

    public ResHandler(TResIR curResIR) {
        theResIR = curResIR;
    }

    public void characters(char[] character, int start, int length)
        throws SAXException {
        context.write(character, start, length);
    }

    public void startDocument() throws SAXException {
        log.add(Level.FINEST, "<" + this.getClass().getName() + ">");

    }

    public void endDocument() throws SAXException {
        log.add(Level.FINEST, "</" + this.getClass().getName() + ">");

    }

    public void startElement(String uri, String localName, String qName,
        Attributes attrs) throws SAXException {
        context.reset();
        if (localName.equals("property-item")) {
            keyName = attrs.getValue("name");
        }
    }

    public void endElement(String uri, String localName, String qName)
        throws SAXException {
        if (localName.equals("property-item")) {
            keyValue = context.toString().trim();
            if (keyName.equals("OS")) {
                theResIR.setTestedOS(keyValue);
                log.add(Level.FINEST, "\tsetTestedOS(" + keyValue + ") -> "
                    + theResIR.getTestedOS());
            } else if (keyName.equals("Platform")) {
                theResIR.setTestedPlatform(keyValue);
                log.add(Level.FINEST, "\tsetTestedPlatform(" + keyValue
                    + ") -> " + theResIR.getTestedPlatform());
            } else if (keyName.equals("Run VM")) {
                theResIR.setTestedVM(keyValue);
                log.add(Level.FINEST, "\tsetTestedVM(" + keyValue + ") -> "
                    + theResIR.getTestedVM());
            } else if (keyName.equals("DATE")) {
                theResIR.setDate(keyValue);
                log.add(Level.FINEST, "\tsetDate(" + keyValue + ") -> "
                    + theResIR.getDate());
            } else if (keyName.equals("TestID")) {
                theResIR.setTestID(keyValue);
                log.add(Level.FINEST, "\tsetTestID(" + keyValue + ") -> "
                    + theResIR.getTestID());
            } else if (keyName.equals("cmd")) {
                if (cmdAsPlain) {
                    String[] cmd = keyValue.split(StoreRes.CMD_MSG_DELIMITER);
                    theResIR.setExecCmd(cmd);
                    log.add(Level.FINEST, "\tsetExecCmd(" + keyValue + ") -> "
                        + theResIR.getTestedOS());
                }
            } else if (keyName.equals("Status")) {
                int status;
                int index;
                String tmp;
                try {
                    index = keyValue.indexOf(" ");
                    if (index != -1) {
                        tmp = keyValue.substring(0, index);
                    } else {
                        tmp = keyValue;
                    }
                    status = Integer.parseInt(tmp);
                } catch (NumberFormatException e) {
                    throw new SAXException("NumberFormatException");
                }
                theResIR.setExecStat(status);
                log.add(Level.FINEST, "\tsetExecStat(" + status + ") -> "
                    + theResIR.getExecStat());
            } else if (keyName.equals("outMsg")) {
                if (logAsPlain) {
                    theResIR.setOutMsg(keyValue);
                    log.add(Level.FINEST, "\tsetOutMsg(" + keyValue + ") -> "
                        + theResIR.getOutMsg());
                }
            } else if (keyName.equals("testSpecificInfo")) {
                theResIR.setTestSpecificInfo(keyValue);
                log.add(Level.FINEST, "\tsetTestSpecificInfo(" + keyValue
                    + ") -> " + theResIR.getTestSpecificInfo());
            } else {
                theResIR.setProperty(keyName, keyValue);
                log.add(Level.FINEST, "\tsetProperty(" + keyName + ", "
                    + keyValue + ") -> " + theResIR.getProperty(keyName));
            }
        } else if (localName.equals("cmdmessage")) {
            cmdAsPlain = false;
            theResIR.setExecCmd(context.toString().trim());
        } else if (localName.equals("logmessage")) {
            logAsPlain = false;
            theResIR.setOutMsg(context.toString().trim());
        }
    }
}