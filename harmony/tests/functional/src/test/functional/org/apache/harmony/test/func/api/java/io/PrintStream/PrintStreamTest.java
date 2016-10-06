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
package org.apache.harmony.test.func.api.java.io.PrintStream;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.PrintStream.PrintStreamTestShared;
import org.apache.harmony.share.Result;

//TODO: add tests for uncovered methods
public final class PrintStreamTest extends PrintStreamTestShared {
    public static void main(String[] args) {
        System.exit(MultiThreadRunner.run(new PrintStreamTest(), args));
    }
    
    protected OutputStream getOutputStream(OutputStream os) {
        return super.getOutputStream(os);
    }
    public Result testAutoFlush() throws IOException {
        return super.testAutoFlush();
    }
    public Result testClose() throws IOException {
        return super.testClose();
    }
    public Result testFlush() throws IOException {
        return super.testFlush();
    }
    public Result testNoAutoFlush() throws IOException {
        return super.testNoAutoFlush();
    }
    public Result testOutField() throws IOException {
        return super.testOutField();
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

