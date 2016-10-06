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
package org.apache.harmony.test.func.api.java.io.share.FilterInputStream;

import java.io.IOException;
import java.io.InputStream;

import org.apache.harmony.test.func.api.java.io.share.InputStream.InputStreamTestShared;
import org.apache.harmony.test.func.api.java.io.share.MockInputStream;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class FilterInputStreamTestShared extends InputStreamTestShared {
    public InputStream getTestedInputStream(InputStream is) {
        return new FilterInputStreamPublicConstructor(is);
    }

    public InputStream getTestedInputStream() throws IOException {
        return getTestedInputStream(super.getTestedInputStream());
    }

    public Result testAvailable() throws IOException {
        return super.testAvailable();
    }

    public Result testClose() throws IOException {
        MockInputStream mis = new MockInputStream();
        InputStream tis = getTestedInputStream(mis);
        MultiThreadRunner.waitAtBarrier();
        
        if(mis.isClosed()) {
            return failed("underlying stream should not be closed here");
        }
        tis.close();
        if(mis.isClosed()) {
            return passed();
        }
        return failed("underlying stream be closed when wrapping stream is closed");
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
    
    public Result testReset0001() throws IOException {
        return super.testReset0001();
    }

    public Result testReset0002() throws IOException {
        return super.testReset0002();
    }
}
