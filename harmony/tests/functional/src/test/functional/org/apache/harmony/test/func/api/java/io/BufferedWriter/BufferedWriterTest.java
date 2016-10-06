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
 * Created on 23.11.2004
 *
 */
package org.apache.harmony.test.func.api.java.io.BufferedWriter;

import java.io.IOException;
import java.io.Writer;

import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.BufferedWriter.BufferedWriterTestShared;
import org.apache.harmony.share.Result;

public final class BufferedWriterTest extends BufferedWriterTestShared {
    public static void main(String[] args) {
        System.exit(MultiThreadRunner.run(new BufferedWriterTest(), args));
    }

    protected Writer getTestedWriter() throws IOException {
        return super.getTestedWriter();
    }
    protected Writer getTestedWriter(Object lock) {
        return super.getTestedWriter(lock);
    }
    protected String getWriterAsString(Writer w) throws IOException {
        return super.getWriterAsString(w);
    }
    public Result testBufferedOutput0001() throws IOException {
        return super.testBufferedOutput0001();
    }
    public Result testBufferedOutput0002() throws IOException {
        return super.testBufferedOutput0002();
    }
    public Result testBufferedOutput0003() throws IOException {
        return super.testBufferedOutput0003();
    }
    public Result testBufferedOutput0004() throws IOException {
        return super.testBufferedOutput0004();
    }
    public Result testClose() throws IOException {
        return super.testClose();
    }
    public Result testExceptionAfterClose() throws IOException {
        return super.testExceptionAfterClose();
    }
    public Result testFlush() throws IOException {
        return super.testFlush();
    }
    public Result testNewLine() throws IOException {
        return super.testNewLine();
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
}