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
package org.apache.harmony.test.func.api.java.io.share.PipedInputStream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class PipedInputStreamPublicReceive extends PipedInputStream {
    List received = Collections.synchronizedList( new ArrayList());
    
    public PipedInputStreamPublicReceive(PipedOutputStream pos) throws IOException {
        super(pos);
    }
    
    public List getReceived() {
        return received;
    }

    protected synchronized void receive(int arg0) throws IOException {
        received.add(new Integer(arg0));
        
        super.receive(arg0);
    }
}
