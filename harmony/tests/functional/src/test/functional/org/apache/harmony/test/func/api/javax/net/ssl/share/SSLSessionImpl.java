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

package org.apache.harmony.test.func.api.javax.net.ssl.share;

import java.security.Principal;
import java.security.cert.Certificate;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.security.cert.X509Certificate;

public class SSLSessionImpl implements SSLSession {
    private int methodsCalled = 0;
    private static final int GET_CIPHER_SUITE_CALLED = 1;
    private static final int GET_LOCAL_CERTIFICATES_CALLED = 2;
    private static final int GET_PEER_CERTIFICATE_CHAIN_CALLED = 4;
    private static final int GET_PEER_CERTIFICATES_CALLED = 8;
    
    public boolean isGetCiperSuiteCalled() {
        return (methodsCalled & GET_CIPHER_SUITE_CALLED) != 0;
    }

    public boolean isGetLocalCertificatesCalled() {
        return (methodsCalled & GET_LOCAL_CERTIFICATES_CALLED) != 0;
    }

    public boolean isGetPeerCertificateChainCalled() {
        return (methodsCalled & GET_PEER_CERTIFICATE_CHAIN_CALLED) != 0;
    }

    public boolean isGetPeerCertificatesCalled() {
        return (methodsCalled & GET_PEER_CERTIFICATES_CALLED) != 0;
    }
    
    
    public long getCreationTime() {
        return 0;
    }

    public long getLastAccessedTime() {
        return 0;
    }

    public void invalidate() {
    }

    public byte[] getId() {
        return null;
    }

    public String getCipherSuite() {
        methodsCalled |= GET_CIPHER_SUITE_CALLED;
        return null;
    }

    public String getPeerHost() {
        return null;
    }

    public String getProtocol() {
        return null;
    }

    public String[] getValueNames() {
        return null;
    }

    public void removeValue(String arg0) {
    }

    public Certificate[] getLocalCertificates() {
        methodsCalled |= GET_LOCAL_CERTIFICATES_CALLED;
        return null;
    }

    public Certificate[] getPeerCertificates()
            throws SSLPeerUnverifiedException {
        methodsCalled |= GET_PEER_CERTIFICATES_CALLED;
        return null;
    }

    public SSLSessionContext getSessionContext() {
        return null;
    }

    public X509Certificate[] getPeerCertificateChain()
            throws SSLPeerUnverifiedException {
        methodsCalled |= GET_PEER_CERTIFICATE_CHAIN_CALLED;
        return null;
    }

    public Object getValue(String arg0) {
        return null;
    }

    public void putValue(String arg0, Object arg1) {
    }

    public boolean isValid() {
        return false;
    }

    public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        return null;
    }

    public Principal getLocalPrincipal() {
        return null;
    }

    public int getPeerPort() {
        return 7777;
    }

    public int getPacketBufferSize() {
        return 1024;
    }

    public int getApplicationBufferSize() {
        return 4096;
    }
    

}