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
package org.apache.harmony.test.func.api.java.io.FileReader;

import java.io.IOException;
import java.io.Reader;

import org.apache.harmony.test.func.api.java.io.share.FileReader.FileReaderThreadTestShared;
import org.apache.harmony.share.Result;

public final class FileReaderThreadTest extends FileReaderThreadTestShared {
    public static void main(String[] args) {
        System.exit(new FileReaderThreadTest().test(args));
    }

    
    protected int getReads() {
        return super.getReads();
    }
    protected Reader getTestedReader() throws IOException {
        return super.getTestedReader();
    }
    public Result testReadChars0001() throws IOException {
        return super.testReadChars0001();
    }
    public Result testReadChars0002() throws IOException {
        return super.testReadChars0002();
    }
    public Result testReadInt() throws IOException {
        return super.testReadInt();
    }
    public Result testSkip() throws IOException {
        return super.testSkip();
    }
}
