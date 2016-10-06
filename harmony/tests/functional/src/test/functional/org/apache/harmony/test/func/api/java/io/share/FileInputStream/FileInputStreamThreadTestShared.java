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
package org.apache.harmony.test.func.api.java.io.share.FileInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.harmony.test.func.api.java.io.share.InputStream.InputStreamThreadTestShared;
import org.apache.harmony.share.Result;

public class FileInputStreamThreadTestShared extends InputStreamThreadTestShared {
    static private File gold = null;
    
    public InputStream getTestedInputStream() throws IOException {
        if (gold == null) {
            gold = File.createTempFile("abcd", "xyz");
            gold.deleteOnExit();

            FileOutputStream fos = new FileOutputStream(gold);
            for(int i =0; i < THREADS * READS * THREADS; ++i) {
                fos.write((byte) ('a' + (i % THREADS)));
            }
            fos.close();
        }

        return new FileInputStream(gold);
    }
    
    
    public Result testReadByte() throws IOException {
        return super.testReadByte();
    }

    public Result testReadBytes() throws IOException {
        return super.testReadBytes();
    }

    public Result testReadBytesSlice() throws IOException {
        return super.testReadBytesSlice();
    }
}
