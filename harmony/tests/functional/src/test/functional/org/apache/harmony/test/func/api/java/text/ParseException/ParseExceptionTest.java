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

package org.apache.harmony.test.func.api.java.text.ParseException;

import java.text.ParseException;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * 17.10.2005
 */
public class ParseExceptionTest extends MultiCase {

    public Result testConstructor() {
        String message = "ParseException with the specified detail "
                + "message and offset. A detail message is a String "
                + "that describes this particular exception.";

        int offset = 37;

        try {
            throw new ParseException(message, offset);
        } catch (ParseException e) {
            if (!e.getMessage().equals(message)) {
                return failed("Message invalid");
            }
            if (e.getErrorOffset() != offset) {
                return failed("Offset invalid");
            }
            return passed();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.exit(new ParseExceptionTest().test(args));
    }

}
