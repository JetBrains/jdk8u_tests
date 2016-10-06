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
package org.apache.harmony.test.func.api.java.io.PushBackInputStream;

import java.io.IOException;
import java.io.InputStream;

import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.PushBackInputStream.PushBackInputStreamTestShared;
import org.apache.harmony.share.Result;

//TODO: add tests for uncovered methods
public final class PushBackInputStreamTest extends PushBackInputStreamTestShared {
    public static void main(String[] args) {
        System.exit(MultiThreadRunner.run(new PushBackInputStreamTest(), args));
    }
    
    public InputStream getTestedInputStream() throws IOException {
        return super.getTestedInputStream();
    }
    public InputStream getTestedInputStream(InputStream is) {
        return super.getTestedInputStream(is);
    }
    public Result testAvailable() throws IOException {
        return super.testAvailable();
    }
    public Result testAvailable0001() throws IOException {
        return super.testAvailable0001();
    }
    public Result testClose() throws IOException {
        return super.testClose();
    }
    public Result testConstructorException() throws IOException {
        return super.testConstructorException();
    }
    public Result testFields() throws IOException {
        return super.testFields();
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
    public Result testReset() throws IOException {
        return super.testReset();
    }
    public Result testSkip() throws IOException {
        return super.testSkip();
    }
    public Result testSkip0001() throws IOException {
        return super.testSkip0001();
    }
    public Result testUnread() throws IOException {
        return super.testUnread();
    }
    public Result testUnread0001() throws IOException {
        return super.testUnread0001();
    }
    public Result testUnread0002() throws IOException {
        return super.testUnread0002();
    }
    public Result testUnreadBeforeRead() throws IOException {
        return super.testUnreadBeforeRead();
    }
    public Result testUnreadBeforeRead0001() throws IOException {
        return super.testUnreadBeforeRead0001();
    }
    public Result testUnreadBytes0001() throws IOException {
        return super.testUnreadBytes0001();
    }
    public Result testUnreadBytes0002() throws IOException {
        return super.testUnreadBytes0002();
    }
    public Result testUnreadBytes0003() throws IOException {
        return super.testUnreadBytes0003();
    }
    public Result testUnreadBytes0004() throws IOException {
        return super.testUnreadBytes0004();
    }
    public Result testUnreadBytesNull() throws IOException {
        return super.testUnreadBytesNull();
    }
}    
