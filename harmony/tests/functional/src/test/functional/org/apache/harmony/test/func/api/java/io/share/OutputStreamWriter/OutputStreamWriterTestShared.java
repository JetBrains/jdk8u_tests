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
/*
 * Created on 19.11.2004
 *
 */
package org.apache.harmony.test.func.api.java.io.share.OutputStreamWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanup;
import org.apache.harmony.test.func.api.java.io.share.Utils;
import org.apache.harmony.test.func.api.java.io.share.Writer.WriterTestShared;
import org.apache.harmony.share.Result;

public class OutputStreamWriterTestShared extends WriterTestShared implements
        PrepareTestCleanup {
    static File testDir;

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public int prepare(File dir) throws IOException {
        return pass();
    }

    public int cleanup(File dir) throws IOException {
        for (int i = -1; i <= Utils.THREADS; ++i) {
            File f = new File(dir, (i == -1 ? "main" : "Thread-" + i) + ".txt");
            f.delete();
        }

        
        new File(dir,  "Main Thread.txt").delete(); 
        
        //test that what was written in testWriteOutput() is correct
        int cfile;
        for (int i = -1; i <= Utils.THREADS; ++i) {
            for (int j = 0; i < Utils.MINIMAL_CHARSETS.length; ++i) {
                File charsetFile = new File(dir, (i == -1 ? "main" : "Thread-"
                        + i)
                        + ".txt" + Utils.MINIMAL_CHARSETS[j]);

                charsetFile.delete();

                //the following section is commented out because tht reference
                // jvm hangs in
                //it
                //            InputStreamReader isr = new InputStreamReader(
                //                    new FileInputStream(charsetFile));
                //            try {
                //                int upper = 0xd800;
                //                switch(i) {
                //                   case 0:
                //                   case 1:
                //                       upper = 128;
                //                       break;
                //                }
                //                for (int c = 0; c < upper; ++c) {
                //                    if((cfile = isr.read()) != c) {
                //                        return fail("failing read characters in charset " +
                // Utils.MINIMAL_CHARSETS[i] + " expected " + c + ", got " +
                // cfile);
                //                    }
                //                }
                //            } finally {
                //                isr.close();
                //                isr.close();
                //                charsetFile.delete();
                //            }
            }
        }

        return pass();
    }

    private static String getDefaultOutputFileName() {
        return testDir.getAbsolutePath() + "/"
                + Thread.currentThread().getName() + ".txt";
    }

    protected Writer getTestedWriter() throws IOException {
        return new OutputStreamWriter(new FileOutputStream(
                getDefaultOutputFileName()));
    }

    protected Writer getTestedWriter(Object lock) {
        throw new UnsupportedOperationException(
                "This test is not applicable for FileWriter");
    }

    protected String getWriterAsString(Writer w) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(
                    getDefaultOutputFileName());
            byte[] arr = new byte[1000]; //must be sufficient for tests
            int len = fis.read(arr);
            fis.close();
            return new String(arr, 0, len);
        } catch (IOException e) {
            fail("error opening file " + getDefaultOutputFileName());
            return null;
        }
    }

    public Result testClose() throws IOException {
        return super.testClose();
    }

    public Result testFlush() throws IOException {
        return super.testFlush();
    }

    public Result testWriteArray() throws IOException {
        return super.testWriteArray();
    }

    public Result testWriteArrayBigLength() throws IOException {
        return super.testWriteArrayBigLength();
    }

    public Result testWriteArrayNegativeLength() throws IOException {
        return super.testWriteArrayNegativeLength();
    }

    public Result testWriteArrayNegativeStart() throws IOException {
        return super.testWriteArrayNegativeStart();
    }

    public Result testWriteArraySlice() throws IOException {
        return super.testWriteArraySlice();
    }

    public Result testWriteInt() throws IOException {
        return super.testWriteInt();
    }

    public Result testWriteNullArray() throws IOException {
        return super.testWriteNullArray();
    }

    public Result testWriteNullString() throws IOException {
        return super.testWriteNullString();
    }

    public Result testWriteString() throws IOException {
        return super.testWriteString();
    }

    public Result testWriteStringBigLength() throws IOException {
        return super.testWriteStringBigLength();
    }

    public Result testWriteStringNegativeLength() throws IOException {
        return super.testWriteStringNegativeLength();
    }

    public Result testWriteStringNegativeStart() throws IOException {
        return super.testWriteStringNegativeStart();
    }

    public Result testWriteStringSlice() throws IOException {
        return super.testWriteStringSlice();
    }

    public Result testConstructorCharset() throws IOException {
        try {
            MultiThreadRunner.waitAtBarrier();
            for (Iterator iter = Charset.availableCharsets().entrySet()
                    .iterator(); iter.hasNext();) {
                Map.Entry entry = (Map.Entry) iter.next();
                new OutputStreamWriter(new ByteArrayOutputStream(),
                        (String) entry.getKey());
                new OutputStreamWriter(new ByteArrayOutputStream(),
                        (Charset) entry.getValue());
            }
            for (int i = 0; i < Utils.MINIMAL_CHARSETS.length; ++i) {
                new OutputStreamWriter(new ByteArrayOutputStream(),
                        Utils.MINIMAL_CHARSETS[i]);
            }
            try {
                new OutputStreamWriter(new ByteArrayOutputStream(),
                        "this\nis\na\nname\r\tof\0anonexistentcharset");
            } catch (UnsupportedEncodingException e) {
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return failed("bad luck");
        }

        return passed();
    }

    public Result testGetEncoding() throws IOException {
        String[] canonicalEncNames = new String[] { "ASCII", "ISO8859_1",
                "UTF8", "UnicodeBigUnmarked", "UnicodeLittleUnmarked", "UTF-16" };
        MultiThreadRunner.waitAtBarrier();
        for (int i = 0; i < Utils.MINIMAL_CHARSETS.length; ++i) {
            String encName = new OutputStreamWriter(
                    new ByteArrayOutputStream(), Utils.MINIMAL_CHARSETS[i])
                    .getEncoding();
            if (!canonicalEncNames[i].equals(encName)) {
                return failed("getEncoding failed for the stream created with encoding "
                        + Utils.MINIMAL_CHARSETS[i]
                        + " : "
                        + encName
                        + ", expected " + canonicalEncNames[i]);
            }
        }

        return passed();
    }

    public Result testWriteCharset() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        for (int i = 0; i < Utils.MINIMAL_CHARSETS.length; ++i) {
            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(getDefaultOutputFileName()
                            + Utils.MINIMAL_CHARSETS[i]),
                    Utils.MINIMAL_CHARSETS[i]);
            int c = 0;

            try {
                for (c = 0; c < 0xD801; c += 1) {
                    osw.write(c);
                }
                try {
                    osw.write(c++);
                    return failed("expected error");
                } catch (Error e) {
                }
            } finally {
                osw.close();
            }
        }
        return passed();
    }
}

