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

package org.apache.harmony.test.func.api.java.nio.share;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

public class CharsetEncoderImpl extends CharsetEncoder {
    public CharsetEncoderImpl(Charset arg0, float arg1, float arg2, byte[] arg3) {
        super(arg0, arg1, arg2, arg3);

    }
    public boolean isLegalReplacement(byte[] arg0) {
        return true;
    }
    public CharsetEncoderImpl(Charset arg0, float arg1, float arg2) {
        super(arg0, arg1, arg2);
    }

    protected CoderResult encodeLoop(CharBuffer arg0, ByteBuffer arg1) {
        return null;
    }

}