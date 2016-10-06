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
package org.apache.harmony.test.func.api.java.io.DataOutputStream;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.DataOutputStream.DataOutputStreamTestShared;
import org.apache.harmony.share.Result;

public final class DataOutputStreamTest extends DataOutputStreamTestShared {
    public static void main(String[] args) {
        System.exit(MultiThreadRunner.run(new DataOutputStreamTest(), args));
    }
    
    protected OutputStream getOutputStream(OutputStream os) {
        return super.getOutputStream(os);
    }
    public Result testClose() throws IOException {
        return super.testClose();
    }
    public Result testFlush() throws IOException {
        return super.testFlush();
    }
    public Result testWrite0001() throws IOException {
        return super.testWrite0001();
    }
    public Result testWriteBoolean() throws IOException {
        return super.testWriteBoolean();
    }
    public Result testWriteByte() throws IOException {
        return super.testWriteByte();
    }
    public Result testWriteByte0001() throws IOException {
        return super.testWriteByte0001();
    }
    public Result testWriteBytes() throws IOException {
        return super.testWriteBytes();
    }
    public Result testWriteChar() throws IOException {
        return super.testWriteChar();
    }
    public Result testWriteChars() throws IOException {
        return super.testWriteChars();
    }
    public Result testWriteDouble() throws IOException {
        return super.testWriteDouble();
    }
    public Result testWriteFloat() throws IOException {
        return super.testWriteFloat();
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
    public Result testWriteInt() throws IOException {
        return super.testWriteInt();
    }
    public Result testWriteLong() throws IOException {
        return super.testWriteLong();
    }
    public Result testWriteNull() throws IOException {
        return super.testWriteNull();
    }
    public Result testWriteShort() throws IOException {
        return super.testWriteShort();
    }
    public Result testWriteSlice() throws IOException {
        return super.testWriteSlice();
    }
    public Result testWriteUTF() throws IOException {
        return super.testWriteUTF();
    }
    public Result testWriteUTF0001() throws IOException {
        return super.testWriteUTF0001();
    }
}
