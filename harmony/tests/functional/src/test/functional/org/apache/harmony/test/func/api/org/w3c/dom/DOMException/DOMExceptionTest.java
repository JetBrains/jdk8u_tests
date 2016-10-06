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

package org.apache.harmony.test.func.api.org.w3c.dom.DOMException;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

import org.w3c.dom.DOMException;

public class DOMExceptionTest extends MultiCase {
    public static void main(String[] args) {
        System.exit(new DOMExceptionTest().test(args));
    }

    /*
    * org.w3c.dom.DOMException(short, String)
    */
    public Result testDOMException() {
        short[] codes = {-10, -1, 0, 1, 10};
        String[] msgs = {null, "", "test message"};
        DOMException e = null;
        for (int i = 0; i < codes.length; i++) {
            for (int j = 0; j < msgs.length; j++) {
                e = new DOMException(codes[i], msgs[j]);
                if (e.code != codes[i]) {
                    return failed("Wrong code assigned");
                }
                if ((e.getMessage() != null && !e.getMessage().equals(msgs[j]))
                        || (e.getMessage() == null && msgs[j] != null)) {
                    return failed("Wrong message assigned");
                }
            }
        }
        return passed("OK");
    }
}
