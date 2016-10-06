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
package org.apache.harmony.test.func.api.java.io.FileOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanupRunner;
import org.apache.harmony.test.func.api.java.io.share.FileOutputStream.FileOutputStreamTestShared;
import org.apache.harmony.share.Result;

public final class FileOutputStreamTest extends FileOutputStreamTestShared {
    public static void main(String[] args) {
        try {
            System.exit(PrepareTestCleanupRunner.run(args,
                    new FileOutputStreamTest()));
        } catch (IOException e) {
            System.err.println("Got exception - " + e.getMessage());
        }
    }
    
    public int cleanup(File dir) throws IOException {
        return super.cleanup(dir);
    }
    protected OutputStream getTestedOutputStream(int maxSize)
            throws FileNotFoundException {
        return super.getTestedOutputStream(maxSize);
    }
    protected String getWrittenAsString(OutputStream os) {
        return super.getWrittenAsString(os);
    }
    public int prepare(File dir) throws IOException {
        return super.prepare(dir);
    }
    public void setTestDir(File dir) {
        super.setTestDir(dir);
    }
    public Result testAppend() throws IOException {
        return super.testAppend();
    }
    public Result testChannelNotNull() throws IOException {
        return super.testChannelNotNull();
    }
    public Result testClose() throws IOException {
        return super.testClose();
    }
    public Result testDoubleOpen() throws IOException {
        return super.testDoubleOpen();
    }
    public Result testFileDescriptor() throws IOException {
        return super.testFileDescriptor();
    }
    public Result testFinalize() throws IOException {
        return super.testFinalize();
    }
    public Result testFlush() throws IOException {
        return super.testFlush();
    }
    public Result testOpenDirectory() throws IOException {
        return super.testOpenDirectory();
    }
    public Result testReadonly() throws IOException {
        return super.testReadonly();
    }
    public Result testWrite0001() throws IOException {
        return super.testWrite0001();
    }
    public Result testWriteByte() throws IOException {
        return super.testWriteByte();
    }
    public Result testWriteIndexOutOfBounds0001() throws IOException {
        return super.testWriteIndexOutOfBounds0001();
    }
    public Result testWriteIndexOutOfBounds0002() throws IOException {
        return super.testWriteIndexOutOfBounds0002();
    }
    public Result testWriteIndexOutOfBounds0003() throws IOException {
        return super.testWriteIndexOutOfBounds0003();
    }
    public Result testWriteNull() throws IOException {
        return super.testWriteNull();
    }
    public Result testWriteSlice() throws IOException {
        return super.testWriteSlice();
    }
}
