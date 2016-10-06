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

package org.apache.harmony.test.func.api.javax.net.ssl.HandshakeCompletedEvent;

import java.security.cert.Certificate;

import javax.net.ssl.*;
import javax.security.cert.X509Certificate;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;
import org.apache.harmony.test.func.api.javax.net.ssl.share.*;

public class HandshakeCompletedEventTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new HandshakeCompletedEventTest().test(args));
    }

    public Result testConstructor() {
        SSLSession sess = new SSLSessionImpl();
        SSLSocket sock = new SSLSocketImpl();

        HandshakeCompletedEvent hsce = new HandshakeCompletedEvent(sock, sess);

        if (hsce.getSocket() != sock) {
            return failed("expected getSocket() to return constructor argument");
        }

        if (hsce.getSession() != sess) {
            return failed("expected getSession() to return constructor argument");
        }

        return passed();
    }

    public Result testSessionProperties() {
        SSLSessionImpl sess = new SSLSessionImpl();

        HandshakeCompletedEvent hsce = new HandshakeCompletedEvent(
                new SSLSocketImpl(), sess);
        hsce.getCipherSuite();
        if (!sess.isGetCiperSuiteCalled()) {
            return failed("expected getCipherSuite() to call session getCipherSuite()");
        }

        sess = new SSLSessionImpl();
        hsce = new HandshakeCompletedEvent(new SSLSocketImpl(), sess);
        hsce.getLocalCertificates();
        if (!sess.isGetLocalCertificatesCalled()) {
            return failed("expected getLocalCertificates() to call session getLocalCertificates()");
        }

        sess = new SSLSessionImpl();
        hsce = new HandshakeCompletedEvent(new SSLSocketImpl(), sess);
        try {
            hsce.getPeerCertificateChain();
        } catch (Throwable e) {
            e.printStackTrace();
            return failed("expected getPeerCertificateChain() not to throw any exceptions");
        }
        if (!sess.isGetPeerCertificateChainCalled()) {
            return failed("expected getPeerCertificateChain() to call session getPeerCertificateChain()");
        }

        sess = new SSLSessionImpl();
        hsce = new HandshakeCompletedEvent(new SSLSocketImpl(), sess);
        try {
            hsce.getPeerCertificates();
        } catch (Throwable e) {
            e.printStackTrace();
            return failed("expected getPeerCertificates() not to throw any exceptions");
        }
        if (!sess.isGetPeerCertificatesCalled()) {
            return failed("expected getPeerCertificates() to call session getPeerCertificates()");
        }

        return passed();
    }

    public Result testNullArguments() {
        new HandshakeCompletedEvent(new SSLSocketImpl(), null);

        try {
            new HandshakeCompletedEvent(null, new SSLSessionImpl());
            return failed("expected HandshakeCompletedEvent(null, SSLSession) to throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        return passed();
    }

    public Result testSessionExceptionsWrapping() {
        SSLSession sess = new SSLSessionImpl() {
            public String getCipherSuite() {
                throw new NullPointerException();
            }

            public Certificate[] getLocalCertificates() {
                throw new NullPointerException();
            }

            public Certificate[] getPeerCertificates()
                    throws SSLPeerUnverifiedException {
                throw new SSLPeerUnverifiedException("");
            }

            public X509Certificate[] getPeerCertificateChain()
                    throws SSLPeerUnverifiedException {
                throw new SSLPeerUnverifiedException("");
            }
        };

        HandshakeCompletedEvent hsce = new HandshakeCompletedEvent(
                new SSLSocketImpl(), sess);
        try {
            hsce.getCipherSuite();
            return failed("expected NPE in getCipherSuite()");
        } catch(NullPointerException e) {
        }    
        try {
            hsce.getLocalCertificates();
            return failed("expected NPE in getLocalCertificates()");
        } catch(NullPointerException e) {
        }    
        try {
            hsce.getPeerCertificateChain();
            return failed("expected SSLPeerUnverifiedException in getPeerCertificateChain()");
        } catch(SSLPeerUnverifiedException e) {
        }    

        try {
            hsce.getPeerCertificates();
            return failed("expected SSLPeerUnverifiedException in getPeerCertificates()");
        } catch(SSLPeerUnverifiedException e) {
        }    
        return passed();

    }
}