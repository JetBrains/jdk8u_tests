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
package org.apache.harmony.test.func.api.java.nio.channels.spi.AbstractInterruptibleChannel;

import java.io.IOException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.spi.AbstractInterruptibleChannel;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public final class AbstractInterruptibleChannelTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new AbstractInterruptibleChannelTest().test(args));
    }

    public Result testConstructor() throws IOException {
        AbstractInterruptibleChannel aic = new AbstractInterruptibleChannelImpl();

        if (!aic.isOpen()) {
            return failed("newly created AbstractInterruptibleChannel should be open by default");
        }
        return passed();
    }

    public Result test1() throws IOException {
        AbstractInterruptibleChannelImpl aic = new AbstractInterruptibleChannelImpl();
        aic.test1();
        if(!aic.isOpen()) {
            return failed("expected channel to be open here");
        }
        aic.close();
        if(aic.isOpen()) {
            return failed("expected channel to be closed here");
        }

        return passed();
    }

    public Result test2() throws IOException {
        AbstractInterruptibleChannelImpl aic = new AbstractInterruptibleChannelImpl();
        aic.test2();
        if(!aic.isOpen()) {
            return failed("expected channel to be open here");
        }
        aic.close();
        if(aic.isOpen()) {
            return failed("expected channel to be closed here");
        }

        return passed();
    }

    public Result test3() throws IOException {
        AbstractInterruptibleChannelImpl aic = new AbstractInterruptibleChannelImpl();
        aic.test3();
        if(!aic.isOpen()) {
            return failed("expected channel to be open here");
        }
        aic.close();
        if(aic.isOpen()) {
            return failed("expected channel to be closed here");
        }

        return passed();
    }

    public Result test4() throws IOException {
        AbstractInterruptibleChannelImpl aic = new AbstractInterruptibleChannelImpl();
        aic.test4();
        if(!aic.isOpen()) {
            return failed("expected channel to be open here");
        }
        aic.close();
        if(aic.isOpen()) {
            return failed("expected channel to be closed here");
        }

        return passed();
    }

    public Result test5() throws IOException {
        AbstractInterruptibleChannelImpl aic = new AbstractInterruptibleChannelImpl();
        aic.test5();
        if(!aic.isOpen()) {
            return failed("expected channel to be open here");
        }
        aic.close();
        if(aic.isOpen()) {
            return failed("expected channel to be closed here");
        }

        return passed();
    }

    public Result test6() throws IOException {
        AbstractInterruptibleChannelImpl aic = new AbstractInterruptibleChannelImpl();
        aic.test6();
        if(!aic.isOpen()) {
            return failed("expected channel to be open here");
        }
        aic.close();
        aic.test6();
        aic.close();
        if(aic.isOpen()) {
            return failed("expected channel to be closed here");
        }

        return passed();
    }

}

class AbstractInterruptibleChannelImpl extends AbstractInterruptibleChannel {
    public void implCloseChannel() throws IOException {
    }

    public void test1() throws AsynchronousCloseException {
        begin();
        end(true);
    }

    public void test2() throws AsynchronousCloseException {
        begin();
        end(false);
    }

    public void test3() throws AsynchronousCloseException {
        begin();
    }

    public void test4() throws AsynchronousCloseException {
        end(false);
    }

    public void test5() throws AsynchronousCloseException {
        end(true);
    }
    
    public void test6() throws AsynchronousCloseException {
        end(true);
        end(true);
    }

}