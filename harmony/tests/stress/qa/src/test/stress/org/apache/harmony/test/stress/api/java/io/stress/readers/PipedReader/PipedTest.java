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

package org.apache.harmony.test.stress.api.java.io.stress.readers.PipedReader;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.harmony.test.stress.api.java.io.stress.readers.CharArrayReader.CharArrayTest;
import org.apache.harmony.test.stress.api.java.io.stress.readers.share.ReaderPair;
import org.apache.harmony.test.stress.api.java.io.stress.readers.share.ReaderProvider;
import org.apache.harmony.test.stress.api.java.io.stress.readers.share.ReaderWriterTest;
import org.apache.harmony.test.stress.api.java.io.stress.streams.share.RandomInputStream;

import org.apache.harmony.share.Result;

/*
 * Jan 16, 2006
 */
public class PipedTest extends ReaderWriterTest {
	
	class Provider extends ReaderProvider {

		public ReaderPair getNext() throws IOException {
			return new ReaderPair() {
				PipedReader input;
				PipedWriter output;

				{
					output = new PipedWriter();
					input = new PipedReader(output);
				}
				public void teardown() throws IOException {
				}

				public Reader getInput() throws IOException {
					return input;
				}

				public Writer getOutput() throws IOException {
					return output;
				}				
			};
		}
		
		public boolean isAsynchronous() {
			return true;			
		}
	}

	public Result testManyStreams() {
		setManyStreamsScenario();
		return testImpl(new Provider());
	}

	public Result testBigObj() {
		setBigdataScenario();
		return testImpl(new Provider());
	}
	
	public static void main(String[] args) {
		System.exit(new PipedTest().test(args));
	}
}
