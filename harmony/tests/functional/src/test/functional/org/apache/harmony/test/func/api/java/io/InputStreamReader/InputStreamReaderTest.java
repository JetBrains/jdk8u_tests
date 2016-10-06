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
package org.apache.harmony.test.func.api.java.io.InputStreamReader;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanupRunner;
import org.apache.harmony.test.func.api.java.io.share.InputStreamReader.InputStreamReaderTestShared;
import org.apache.harmony.share.Result;

public final class InputStreamReaderTest extends InputStreamReaderTestShared {
    public static void main(String[] args) {
        try {
            System.exit(PrepareTestCleanupRunner.run(args,
                    new InputStreamReaderTest()));
        } catch (IOException e) {
            System.err.println("Got exception - " + e.getMessage());
        }
    }
    
    public int cleanup(File dir) throws IOException {
        return super.cleanup(dir);
    }
    protected Reader getTestedReader() throws IOException {
        return super.getTestedReader();
    }
    protected Reader getTestedReader(Object lock) throws IOException {
        return super.getTestedReader(lock);
    }
    public int prepare(File dir) throws IOException {
        return super.prepare(dir);
    }
    public void setTestDir(File dir) {
        super.setTestDir(dir);
    }
    public Result testClose() throws IOException {
        return super.testClose();
    }
    public Result testConstructorCharset() throws IOException {
        return super.testConstructorCharset();
    }
    public Result testExceptionAfterClose() throws IOException {
        return super.testExceptionAfterClose();
    }
    public Result testGetEncoding() throws IOException {
        return super.testGetEncoding();
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
    public Result testReadCharset() throws IOException {
        return super.testReadCharset();
    }
    public Result testReady() throws IOException {
        return super.testReady();
    }
    public Result testSkip() throws IOException {
        return super.testSkip();
    }
    public Result testSkipNegative() throws IOException {
        return super.testSkipNegative();
    }
}
