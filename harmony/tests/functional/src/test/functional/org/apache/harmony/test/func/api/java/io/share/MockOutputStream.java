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
package org.apache.harmony.test.func.api.java.io.share;

import java.io.IOException;
import java.io.OutputStream;

public class MockOutputStream extends OutputStream {
    private int[] written;

    private int count;

    private boolean flushed;

    private boolean closed;

    public MockOutputStream(int capacity) {
        super();
        written = new int[capacity];
        count = 0;
        flushed = closed = false;
    }

    public void write(int arg0) throws IOException {
        written[count++] = arg0;
    }

    public int getCount() {
        return count;
    }

    public int[] getWritten() {
        return written;
    }

    public void close() throws IOException {
        super.close();
        closed = true;
    }

    public void flush() throws IOException {
        super.flush();
        flushed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean isFlushed() {
        return flushed;
    }

    public String getWrittenAsString() {
        return new String(getWrittenBytes());
    }

    private byte[] getWrittenBytes() {
        byte[] writtenBytes = new byte[count];
        
        for(int i = 0; i < count; ++i) {
            writtenBytes[i] = (byte) written[i];
        }
        return writtenBytes;
    }
}
