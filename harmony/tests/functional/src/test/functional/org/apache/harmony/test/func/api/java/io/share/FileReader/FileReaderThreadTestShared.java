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
package org.apache.harmony.test.func.api.java.io.share.FileReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import org.apache.harmony.test.func.api.java.io.share.Reader.ReaderThreadTestShared;
import org.apache.harmony.share.Result;

public class FileReaderThreadTestShared extends ReaderThreadTestShared {
    static private File gold = null;
    

    protected int getReads() {
        return super.getReads() / 10;
    }
    protected Reader getTestedReader() throws IOException {
        if (gold == null) {
            gold = File.createTempFile("abcd", "xyz");
            gold.deleteOnExit();
            FileWriter fw = new FileWriter(gold);
            for (int i = 0; i < THREADS * THREADS * getReads(); ++i) {
                fw.write('a' + (i % THREADS));
            }
            fw.close();
        }
        return new FileReader(gold);
    }

    public Result testReadChars0001() throws IOException {
        return super.testReadChars0001();
    }

    public Result testReadChars0002() throws IOException {
        return super.testReadChars0002();
    }

    public Result testReadInt() throws IOException {
        return super.testReadInt();
    }

    public Result testSkip() throws IOException {
        return super.testSkip();
    }
}
