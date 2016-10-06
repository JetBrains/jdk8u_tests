/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.harmony.test.func.api.javax.management.share;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.harmony.test.func.api.javax.management.share.framework.Test;
import org.apache.harmony.test.func.api.javax.management.share.framework.TestRunner;

/**
 */
public class AdminToolTest extends Test {

    /**
     * HTTP session id.
     */
    private String sessionID = "";

    public void init() throws MalformedURLException, IOException {
        // Obtain session Id.
        HttpURLConnection con = (HttpURLConnection) new URL(
            "http://localhost:8080/admin/").openConnection();
        sessionID = con.getHeaderField("Set-Cookie");
        log.debug(sessionID);
        if (sessionID == null) {
            fail("null 'Set-Cookie' header retrieved.");
            finish();
            return;
        }

        int sind = sessionID.indexOf("JSESSIONID=");
        if (sind == -1) {
            fail("Invalid 'JSESSIONID' cookie retrieved.");
            finish();
            return;
        }

        sind += 11;

        int eind = sessionID.indexOf(";", sind);
        if (eind == -1) {
            eind = sessionID.length();
        }

        sessionID = sessionID.substring(sind, eind);
        log.debug(sessionID);

        // Login.
        con = (HttpURLConnection) new URL(
            "http://localhost:8080/admin/j_security_check;jsessionid="
                + sessionID + "?j_username=admin&j_password=123")
            .openConnection();
        con.setRequestMethod("POST");
        String resp = con.getResponseMessage();
        log.debug("Response: " + resp);
        if (!"Ok".equalsIgnoreCase(resp)) {
            fail("Login failed. Server said: " + resp);
            finish();
            return;
        }

        con = (HttpURLConnection) new URL(
            "http://localhost:8080/admin/setUpTree.do;jsessionid=" + sessionID)
            .openConnection();

        log.debug(getHTMLText(con.getInputStream()));
    }

    /**
     * Reads HTML document from the input stream and retrieves the text,
     * contained in the document.
     * 
     * @param is input stream.
     * @return text, contained in the document.
     * @throws IOException
     */
    private String getHTMLText(InputStream is) throws IOException {
        final byte lt = "<".getBytes()[0];
        final byte gt = ">".getBytes()[0];
        String text = "";

        byte[] buf = new byte[1];
        boolean isText = true;
        String str = "";
        while ((is.read(buf)) != -1) {
            if (buf[0] == lt) {
                isText = false;
                str = str.trim();
                if (!str.equals("")) {
                    text += str + "\n";
                }
                str = "";
                continue;
            } else if (buf[0] == gt) {
                isText = true;
                continue;
            }

            if (isText) {
                str += new String(buf);
            }
        }

        return text;
    }

    /**
     * Run the test.
     * 
     * @param args command line arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.exit(TestRunner.run(AdminToolTest.class, args));
    }
}
