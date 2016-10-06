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
package org.apache.harmony.test.func.api.java.io.DataInputStream;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanup;
import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanupRunner;
import org.apache.harmony.test.func.api.java.io.share.DataInputStream.DataInputStreamTestShared;
import org.apache.harmony.share.Result;

public final class DataInputStreamTest extends DataInputStreamTestShared implements
        PrepareTestCleanup {
    public static void main(String[] args) {
        try {
            System.exit(PrepareTestCleanupRunner.run(args,
                    new DataInputStreamTest()));
        } catch (IOException e) {
            System.err.println("Got exception - " + e.getMessage());
        }
    }
    
    
    public int cleanup(File dir) throws IOException {
        return super.cleanup(dir);
    }
    public DataInputStream getDataInputStream() throws IOException {
        return super.getDataInputStream();
    }
    public InputStream getTestedInputStream() throws IOException {
        return super.getTestedInputStream();
    }
    public InputStream getTestedInputStream(InputStream is) {
        return super.getTestedInputStream(is);
    }
    public int prepare(File dir) throws IOException {
        return super.prepare(dir);
    }
    public void setTestDir(File dir) {
        super.setTestDir(dir);
    }
    public Result testAvailable() throws IOException {
        return super.testAvailable();
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
    public Result testReadBoolean() throws IOException {
        return super.testReadBoolean();
    }
    public Result testReadByte() throws IOException {
        return super.testReadByte();
    }
    public Result testReadBytes() throws IOException {
        return super.testReadBytes();
    }
    public Result testReadBytes0001() throws IOException {
        return super.testReadBytes0001();
    }
    public Result testReadBytes0002() throws IOException {
        return super.testReadBytes0002();
    }
    public Result testReadBytes0003() throws IOException {
        return super.testReadBytes0003();
    }
    public Result testReadBytesNull() throws IOException {
        return super.testReadBytesNull();
    }
    public Result testReadBytesSlice() throws IOException {
        return super.testReadBytesSlice();
    }
    public Result testReadChar() throws IOException {
        return super.testReadChar();
    }
    public Result testReadDouble() throws IOException {
        return super.testReadDouble();
    }
    public Result testReadFloat() throws IOException {
        return super.testReadFloat();
    }
    public Result testReadFully0001() throws IOException {
        return super.testReadFully0001();
    }
    public Result testReadFully0002() throws IOException {
        return super.testReadFully0002();
    }
    public Result testReadFullyEmptyBuffer() throws IOException {
        return super.testReadFullyEmptyBuffer();
    }
    public Result testReadFullyNullBuffer() throws IOException {
        return super.testReadFullyNullBuffer();
    }
    public Result testReadInt() throws IOException {
        return super.testReadInt();
    }
    public Result testReadLong() throws IOException {
        return super.testReadLong();
    }
    public Result testReadSignedByte() throws IOException {
        return super.testReadSignedByte();
    }
    public Result testReadSignedShort() throws IOException {
        return super.testReadSignedShort();
    }
    public Result testReadUnsignedByte() throws IOException {
        return super.testReadUnsignedByte();
    }
    public Result testReadUnsignedShort() throws IOException {
        return super.testReadUnsignedShort();
    }
    public Result testReadUTF() throws IOException {
        return super.testReadUTF();
    }
    public Result testReset() throws IOException {
        return super.testReset();
    }
    public Result testSkip() throws IOException {
        return super.testSkip();
    }
    public Result testSkipBytes() throws IOException {
        return super.testSkipBytes();
    }
    public Result testSkipBytesEOF() throws IOException {
        return super.testSkipBytesEOF();
    }
    public Result testStaticReadUTF() throws IOException {
        return super.testStaticReadUTF();
    }
}
