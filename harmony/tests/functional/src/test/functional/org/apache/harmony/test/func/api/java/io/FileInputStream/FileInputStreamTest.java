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
package org.apache.harmony.test.func.api.java.io.FileInputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanupRunner;
import org.apache.harmony.test.func.api.java.io.share.FileInputStream.FileInputStreamTestShared;
import org.apache.harmony.share.Result;

public final class FileInputStreamTest extends FileInputStreamTestShared {
    public static void main(String[] args) {
        try {
            System.exit(PrepareTestCleanupRunner.run(args,
                    new FileInputStreamTest()));
        } catch (IOException e) {
            System.err.println("Got exception - " + e.getMessage());
        }
    }
    
    
    

    public int cleanup(File dir) throws IOException {
        return super.cleanup(dir);
    }
    public InputStream getTestedInputStream() throws IOException {
        return super.getTestedInputStream();
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
    public Result testExceptionAfterClose() throws IOException {
        return super.testExceptionAfterClose();
    }
    public Result testFileDescriptor() throws IOException {
        return super.testFileDescriptor();
    }
    public Result testFileNotFoundException() throws IOException {
        return super.testFileNotFoundException();
    }
    public Result testMark() throws IOException {
        return super.testMark();
    }
    public Result testMarkSupported() throws IOException {
        return super.testMarkSupported();
    }
    public Result testReadByte() throws IOException {
        return super.testReadByte();
    }
    public Result testReadByte0001() throws IOException {
        return super.testReadByte0001();
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
    public Result testReadBytes0004() throws IOException {
        return super.testReadBytes0004();
    }
    public Result testReadBytesNull() throws IOException {
        return super.testReadBytesNull();
    }
    public Result testReadBytesSlice() throws IOException {
        return super.testReadBytesSlice();
    }
    public Result testReset() throws IOException {
        return super.testReset();
    }
    public Result testReset0001() throws IOException {
        return super.testReset0001();
    }
    public Result testReset0002() throws IOException {
        return super.testReset0002();
    }
    public Result testSkip() throws IOException {
        return super.testSkip();
    }
    public Result testFinalize() throws IOException {
        return super.testFinalize();
    }
}