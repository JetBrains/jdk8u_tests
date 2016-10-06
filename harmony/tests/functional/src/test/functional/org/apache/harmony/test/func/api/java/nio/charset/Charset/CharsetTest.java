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
package org.apache.harmony.test.func.api.java.nio.charset.Charset;

import java.io.IOException;
import java.nio.charset.IllegalCharsetNameException;

import org.apache.harmony.test.func.api.java.nio.share.CharsetImpl;
import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

public final class CharsetTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new CharsetTest().test(args));
    }

    public Result testConstructor() throws IOException {
        new CharsetImpl("foobar", null);
        try {
            new CharsetImpl(null, new String[] {});
            return failed("expected NPE with null charset name");
        } catch (NullPointerException e) {
        }

        try {
            new CharsetImpl("bad\ncharset", null);
            return failed("expected IllegalCharsetNameException with bad charset name");
        } catch (IllegalCharsetNameException e) {
        }
        
        if(!("foobar".equals(new CharsetImpl("foobar", null).name()))) {
            return failed("expected name() to return constructor argument");
        }
        
        if(new CharsetImpl("foobar", null).aliases().size() != 0) {
            return failed("expected aliases to be empty");
        }

        if(new CharsetImpl("foo", new String[] {"bar"}).aliases().size() != 1) {
            return failed("expected aliases to have one element");
        }
        if(!(new CharsetImpl("foo", new String[] {"bar"}).aliases().contains("bar"))) {
            return failed("expected aliases to contain 'bar'");
        }
        return passed();
    }
}

