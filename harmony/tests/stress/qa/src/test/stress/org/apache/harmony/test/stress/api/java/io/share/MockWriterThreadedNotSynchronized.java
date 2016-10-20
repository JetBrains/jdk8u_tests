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

public class MockWriterThreadedNotSynchronized extends MockWriterThreaded {
    public MockWriterThreadedNotSynchronized(Object lock) {
        super(lock);

    }
    public void write(char[] arg0, int arg1, int arg2) throws IOException {
        if (isClosed()) {
            throw new IOException("closed");
        }
        if (arg1 < 0 || arg2 < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        for (int i = arg1; i < arg1 + arg2; ++i) {
            ++written[arg0[i]];
        }
    }
}
