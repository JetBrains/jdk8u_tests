/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * @author Dmitry Vozzhaev
 * @version $Revision: 1.2 $
 */

package org.apache.harmony.test.stress.api.java.io.stress.readers.FileReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.apache.harmony.test.stress.api.java.io.stress.readers.share.ReaderPair;
import org.apache.harmony.test.stress.api.java.io.stress.readers.share.ReaderProvider;

public abstract class FileReaderProvider extends ReaderProvider {

    public ReaderPair getNext() throws IOException {
        return new ReaderPair() {

            private Writer output;
            private Reader input;
            private File file;

            public void teardown() throws IOException {
                input.close();
                file.delete();
            }

            public Reader getInput() throws IOException {
                output.close();
                input = new FileReader(file);
                return input;
            }

            public Writer getOutput() throws IOException {
                file = getNextFile();
                output = new FileWriter(file);
                return output;
            }

        };
    }

    public abstract File getNextFile() throws IOException;
}
