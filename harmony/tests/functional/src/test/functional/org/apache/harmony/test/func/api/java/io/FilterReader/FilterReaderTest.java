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
 * Created on 17.11.2004
 *  
 */
package org.apache.harmony.test.func.api.java.io.FilterReader;

import java.io.IOException;
import java.io.Reader;

import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.FilterReader.FilterReaderTestShared;
import org.apache.harmony.share.Result;

public final class FilterReaderTest extends FilterReaderTestShared {
    public static void main(String[] args) {
        System.exit(MultiThreadRunner.run(new FilterReaderTest(), args));
    }
    
    protected Reader getTestedReader() throws IOException {
        return super.getTestedReader();
    }
    protected Reader getTestedReader(Object lock) throws IOException {
        return super.getTestedReader(lock);
    }
    public Result testClose() throws IOException {
        return super.testClose();
    }
    public Result testConstructor() throws IOException {
        return super.testConstructor();
    }
    public Result testMark() throws IOException {
        return super.testMark();
    }
    public Result testMarkNegative() throws IOException {
        return super.testMarkNegative();
    }
    public Result testMarkSupported() throws IOException {
        return super.testMarkSupported();
    }
    public Result testNullObjectInConstructor() throws IOException {
        return super.testNullObjectInConstructor();
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
        return super.testReady();
    }
    public Result testReset() throws IOException {
        return super.testReset();
    }
    public Result testSkip() throws IOException {
        return super.testSkip();
    }
    public Result testSkipNegative() throws IOException {
        return super.testSkipNegative();
    }
}

