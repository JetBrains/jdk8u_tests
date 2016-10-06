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
package org.apache.harmony.test.func.api.java.io.share.FilterOutputStream;


import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

import org.apache.harmony.test.func.api.java.io.share.OutputStream.OutputStreamTestShared;
import org.apache.harmony.test.func.api.java.io.share.MockOutputStream;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class FilterOutputStreamTestShared extends OutputStreamTestShared {
    
    protected OutputStream getTestedOutputStream(int maxSize)
            throws IOException {
        return getOutputStream(super.getTestedOutputStream(maxSize));
    }

    protected String getWrittenAsString(OutputStream os) {
        try {
            final Field c = FilterOutputStream.class.getDeclaredField("out");
            
	    c.setAccessible(true);
            return super.getWrittenAsString((OutputStream) c.get(os));
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    protected OutputStream getOutputStream(OutputStream os) {
        return new FilterOutputStream(os);
    }

    public Result testClose() throws IOException {
        MockOutputStream os = new MockOutputStream(5);
        OutputStream fos = getOutputStream(os);
        
        MultiThreadRunner.waitAtBarrier();

        fos.write("xyz".getBytes());
        fos.close();

        if (os.isClosed() && os.isFlushed()
                && "xyz".equals(os.getWrittenAsString())) {
            return passed();
        }

        return failed("expected underlying stream to be flushed and closed");
    }

    public Result testFlush() throws IOException {
        MockOutputStream os = new MockOutputStream(5);
        OutputStream fos = getOutputStream(os);

        MultiThreadRunner.waitAtBarrier();

        fos.write("abc".getBytes());
        fos.flush();
        if (os.isFlushed() && "abc".equals(os.getWrittenAsString())) {
            return passed();
        }

        return failed("expected underlying stream to be flushed and all pending data written to it");
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

    public Result testOutField() throws IOException {
        MultiThreadRunner.waitAtBarrier();

        OutputStream os = new MockOutputStream(0);
        TestOut tos = new TestOut(os);
        
        if (os == tos.getOut()) {
            return passed();
        }
        return failed("wrong value in 'out' field");
    }
}

class TestOut extends FilterOutputStream {
    public TestOut(OutputStream arg0) {
        super(arg0);
    }

    OutputStream getOut() {
        return out;
    }
}
