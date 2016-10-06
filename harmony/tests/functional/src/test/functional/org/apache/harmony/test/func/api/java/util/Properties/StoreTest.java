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
/**
 */

package org.apache.harmony.test.func.api.java.util.Properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * Mar 21, 2006
 */
public class StoreTest extends MultiCase {

    File sample = null;

    private class StreamMatcher extends OutputStream {
        private InputStream input;

        public boolean ok = true;

        private boolean datePart = false;

        private int poundChars = 0;

        private String date;

        public StreamMatcher(InputStream input) {
            this.input = input;
        }

        public String read = "";

        public String written = "";

        public void write(int b) throws IOException {
            poundChars += b == '#' ? 1 : 0;

            if (!datePart) {
                if (b == '#' && poundChars == 2) {
                    datePart = true;
                    date = "";
                }
                int read = input.read();
                this.read += (char) read;
                this.written += (char) b;
                if (read != b) {
                    // Special Linux case
                    if (read != 0x0D || b != 0x0A || input.read() != 0x0A) {
                        ok = false;
                    }
                }
            } else {
                if (b == '\n') {
                    try {
                        // format is from javadoc Date.toString()
                        long delta = System.currentTimeMillis()
                                - new SimpleDateFormat(
                                        "EEE MMM dd HH:mm:ss zzz yyyy").parse(
                                        date).getTime();
                        if (delta > 2000) {
                            throw new IOException("Stale date");
                        }
                        datePart = false;

                        // skip date part in input
                        while (input.read() != '\n')
                            ;

                    } catch (ParseException e) {
                        ok = false;
                        throw (IOException) new IOException("Wrong date")
                                .initCause(e);
                    }

                } else {
                    date += (char) b;
                }
            }
        }
    }

    public Result testStore() {
        Properties p = new Properties();

        p.setProperty("key", "value");
        p.setProperty("KEY", "VALUE");
        p.setProperty("\u30ad\u30fc",
                "\u79c1\u306e\u5b8c\u5168\u306a\u65e5\u672c\u306e\u4fa1\u5024");
        p.setProperty("multiline\nkey", "\t\tTabbed\r\n\t\t\\Value");
        p.setProperty("#Special:Key=Characters!", "" + (char) 1 + (char) 2);

        try {

            OutputStream output = false ? (OutputStream) new FileOutputStream(
                    sample) : new StreamMatcher(new FileInputStream(sample));

            p.store(output, "This is the heading comment");

            if (output instanceof StreamMatcher) {
                StreamMatcher matcher = (StreamMatcher) output;
                if (!matcher.ok) {
                    log.add("Read: " + matcher.read);
                    log.add("Written: " + matcher.written);
                    return failed("Data do not match");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return failed(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return failed(e.getMessage());
        }

        return passed();
    }

    public static void main(String[] args) {

        StoreTest test = new StoreTest();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-propertiesPath")) {
                test.sample = new File(args[i + 1]);
                log.add("Properties path: " + test.sample);
            }
        }
        System.exit(test.test(args));
    }

}
