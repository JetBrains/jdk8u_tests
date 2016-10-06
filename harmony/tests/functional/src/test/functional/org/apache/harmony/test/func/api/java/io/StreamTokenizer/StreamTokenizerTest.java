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
package org.apache.harmony.test.func.api.java.io.StreamTokenizer;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public final class StreamTokenizerTest extends IOMultiCase {
    public static void main(String[] args) {
        System.exit(MultiThreadRunner.run(new StreamTokenizerTest(), args));
    }

    public Result testConstructor() {
        MultiThreadRunner.waitAtBarrier();
        try {
            new StreamTokenizer((Reader) null);
        } catch (NullPointerException e) {
        }
        
        new StreamTokenizer(new InputStreamReader(new ByteArrayInputStream(
                new byte[] {}))); //no exception
        return passed();
    }
}