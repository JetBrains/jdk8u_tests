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

package org.apache.harmony.test.func.api.java.io.StringWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.harmony.test.func.api.java.io.share.Writer.WriterThreadTestShared;
import org.apache.harmony.share.Result;

public final class StringWriterThreadTest extends WriterThreadTestShared {
    public static void main(String[] args) {
        System.exit(new StringWriterThreadTest().test(args));
    }

    protected Writer getTestedWriter() throws IOException {
        return new StringWriter();
    }

    protected int[] getWritten(Writer w) throws IOException {
        int[] iarr = new int['z'];
        byte[] barr = ((StringWriter) w).getBuffer().toString().getBytes();
        
        for(int i = 0; i < barr.length; ++i) {
               ++iarr[barr[i]];
        }

        return iarr;
    }
    public Result testWrite0001() throws IOException {
        return super.testWrite0001();
    }
    public Result testWriteArray() throws IOException {
        return super.testWriteArray();
    }
    public Result testWriteArraySlice() throws IOException {
        return super.testWriteArraySlice();
    }
    public Result testWriteString() throws IOException {
        return super.testWriteString();
    }
    public Result testWriteStringSlice() throws IOException {
        return super.testWriteStringSlice();
    }
}

