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

package org.apache.harmony.test.func.api.java.net.Socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;

/*
 * Mar 22, 2006
 */
class Impl extends SocketImpl 
{

    public boolean connectCalled = false;
    
    protected void create(boolean arg0) throws IOException {
        
    }

    protected void connect(String arg0, int arg1) throws IOException {
    }

    protected void connect(InetAddress arg0, int arg1) throws IOException {
    }

    protected void connect(SocketAddress arg0, int arg1) throws IOException {
        connectCalled = true;
    }

    protected void bind(InetAddress arg0, int arg1) throws IOException {
    }

    protected void listen(int arg0) throws IOException {
    }

    protected void accept(SocketImpl arg0) throws IOException {
    }

    protected InputStream getInputStream() throws IOException {
        return null;
    }

    protected OutputStream getOutputStream() throws IOException {
        return null;
    }

    protected int available() throws IOException {
        return 0;
    }

    protected void close() throws IOException {
    }

    protected void sendUrgentData(int arg0) throws IOException {
    }

    public void setOption(int arg0, Object arg1) throws SocketException {
    }

    public Object getOption(int arg0) throws SocketException {
        return null;
    }
    
}
