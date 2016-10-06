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
 * @version $Revision: 1.8 $
 */
package org.apache.harmony.harness.plugins;

public class StoreResXMLOut extends StoreRes {

    /*
     * substitute symbols that can not appear in correct xml v 1.0
     */
    protected String substituteChars(String data) {
        String retVal = data.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
            .replaceAll("&", "&amp;");

        return retVal;
    }

    protected String convertOutToSpecificFormat(String[] data) {
        String tmpVal = "";
        for (int i = 0; i < data.length; i++) {
            if (data[i].indexOf(MSG_DELIMITER) == -1) {
                //no timestamp. Use unavailable value
                tmpVal = tmpVal + "<logmessage timestamp=\"99:99:99\">"
                    + substituteChars(data[i]) + "</logmessage>\n";
            } else {
                tmpVal = tmpVal
                    + "<logmessage timestamp=\""
                    + data[i].substring(0, data[i].indexOf(MSG_DELIMITER))
                    + "\">"
                    + substituteChars(data[i].substring(data[i]
                        .indexOf(MSG_DELIMITER) + 1, data[i].length()))
                    + "</logmessage>\n";
            }
        }
        return tmpVal;
    }

    protected String convertCmdToSpecificFormat(String[] data) {
        String tmpVal = "";
        for (int i = 0; i < data.length; i++) {
            tmpVal = tmpVal + "<cmdmessage>" + substituteChars(data[i])
                + "</cmdmessage>\n";
        }
        return tmpVal;
    }

}