/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */    
/**
 * @author Anton Luht
 * @version $Revision: 1.2 $
 */
package org.apache.harmony.test.stress.api.java.io.share;

import java.io.IOException;
import java.io.OutputStream;

public class MockOutputStreamThreaded extends OutputStream {
    private int[] written;

    private boolean flushed;

    private boolean closed;

    public MockOutputStreamThreaded() {
        super();
        written = new int['z'];
        flushed = closed = false;
    }

    public synchronized void write(int arg0) throws IOException {
        if(isClosed()) {
            throw new IOException("stream is closed");
        }
        ++written[arg0];
    }

    public int[] getWritten() {
        return written;
    }

    public synchronized void close() throws IOException {
        super.close();
        closed = true;
    }

    public void flush() throws IOException {
        super.flush();
        flushed = true;
    }

    public synchronized boolean isClosed() {
        return closed;
    }

    public boolean isFlushed() {
        return flushed;
    }
}
