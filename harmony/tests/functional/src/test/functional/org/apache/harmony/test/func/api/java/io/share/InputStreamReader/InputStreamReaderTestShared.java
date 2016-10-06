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
 * Created on 30.11.2004
 *
 */
package org.apache.harmony.test.func.api.java.io.share.InputStreamReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.harmony.test.func.api.java.io.share.Reader.ReaderTestShared;
import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanup;
import org.apache.harmony.test.func.api.java.io.share.Utils;
import org.apache.harmony.share.Result;


public class InputStreamReaderTestShared extends ReaderTestShared implements
        PrepareTestCleanup {
    static File testDir;

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public int prepare(File dir) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(dir, "gold.txt"));
        fos.write("abcdefghijklmnop".getBytes());
        fos.close();

        for (int i = 0; i < Utils.MINIMAL_CHARSETS.length; ++i) {
            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(new File(dir, Utils.MINIMAL_CHARSETS[i]
                            + "gold.txt")), Utils.MINIMAL_CHARSETS[i]);
            int c = 0;

            try {
                for (c = 0; c < 0xD801 ; c += 1) {
                    osw.write(c);
                }
                try {
                    osw.write(c++);
                    return fail("expected error");
                } catch(Error e) {
                }
            } finally {
//                System.err.println("last written character for charset " + MINIMAL_CHARSETS[i] + " was " + c);
                osw.close();
            }
        }

        return pass();
    }

    public int cleanup(File dir) throws IOException {
        if (!new File(dir, "gold.txt").delete()) {
            return fail("can't delete " + dir.getAbsolutePath() + "/gold.txt");
        }

        for (int i = 0; i < Utils.MINIMAL_CHARSETS.length; ++i) {
            new File(dir, Utils.MINIMAL_CHARSETS[i] + "gold.txt").delete();
        }

        return pass();
    }

    protected Reader getTestedReader() throws IOException {
        return new InputStreamReader(new FileInputStream(new File(testDir,
                "gold.txt")));
    }

    public Result testExceptionAfterClose() throws IOException {
        return super._testExceptionAfterClose();
    }

    protected Reader getTestedReader(Object lock) throws IOException {
        throw new UnsupportedOperationException(
                "constructor of this kind is not supported");
    }

    public Result testClose() throws IOException {
        return super.testClose();
    }

    public Result testMark() throws IOException {
        return super.testMark();
    }

    public Result testMarkSupported() throws IOException {
        return super.testMarkSupported();
    }

    public Result testReadChar() throws IOException {
        return super.testReadChar();
    }

    public Result testReadChars0001() throws IOException {
        return super.testReadChars0001();
    }

    public Result testReadChars0002() throws IOException {
        return super.testReadChars0002();
    }

    public Result testReady() throws IOException {
        Reader r = getTestedReader();
        try {
            if (!r.ready()) {
                return failed("expected reader to be ready");
            }
        } finally {
            r.close();
        }
        return passed();
    }

    public Result testSkip() throws IOException {
        return super.testSkip();
    }

    public Result testSkipNegative() throws IOException {
        return super.testSkipNegative();
    }

    public Result testConstructorCharset() throws IOException {
        try {
        for (Iterator iter = Charset.availableCharsets().entrySet().iterator(); iter
                .hasNext();) {
            Map.Entry entry = (Map.Entry) iter.next();
            new InputStreamReader(new ByteArrayInputStream(new byte[] { 0 }),
                    (String) entry.getKey());
            new InputStreamReader(new ByteArrayInputStream(new byte[] { 0 }),
                    (Charset) entry.getValue());
        }
        for (int i = 0; i < Utils.MINIMAL_CHARSETS.length; ++i) {
            new InputStreamReader(new ByteArrayInputStream(new byte[] { 0 }),
                    Utils.MINIMAL_CHARSETS[i]);
        }
        try {
            new InputStreamReader(new ByteArrayInputStream(new byte[] { 0 }),
                    "this\nis\na\nname\r\tof\0anonexistentcharset");
        } catch (UnsupportedEncodingException e) {
        }
        } catch(Throwable e) {
            e.printStackTrace();
            return failed("bad luck");
        }

        return passed();
    }
    
    public Result testGetEncoding() throws IOException {
        String[] canonicalEncNames = new String[] {
            "ASCII",
            "ISO8859_1", "UTF8", "UnicodeBigUnmarked", "UnicodeLittleUnmarked", "UTF-16" }; 
        for (int i = 0; i < Utils.MINIMAL_CHARSETS.length; ++i) {
            String encName = new InputStreamReader(new ByteArrayInputStream(new byte[] { 0 }),
                    Utils.MINIMAL_CHARSETS[i]).getEncoding();
            if(!canonicalEncNames[i].equals(encName)) {
                return failed("getEncoding failed for the stream created with encoding " + Utils.MINIMAL_CHARSETS[i] + " : " + encName + ", expected " + canonicalEncNames[i]);
             }
        }
        
        return passed();
    }

    //beware - this test may hang in some JVMs
    public Result testReadCharset() throws IOException {
        int cfile;
        for (int i = 0; i < Utils.MINIMAL_CHARSETS.length; ++i) {
            InputStreamReader isr = new InputStreamReader(
                    new FileInputStream(new File(testDir, Utils.MINIMAL_CHARSETS[i]
                            + "gold.txt")), Utils.MINIMAL_CHARSETS[i]);
            try {
                int upper = 0xd800;
                switch(i) {
                   case 0: 
                       upper = 128;
                       break;
                   case 1:
                        upper = 256;
                        break;
                }
                for (int c = 0; c < upper; ++c) {
                    if((cfile = isr.read()) != c) {
                        return failed("failing read characters in charset " + Utils.MINIMAL_CHARSETS[i] + " expected " + c + ", got " + cfile);
                    }
                }
            } finally {
                isr.close();
            }
        }
        return passed();
    }
}
