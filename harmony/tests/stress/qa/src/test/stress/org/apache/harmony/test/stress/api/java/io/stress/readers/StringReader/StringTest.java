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

package org.apache.harmony.test.stress.api.java.io.stress.readers.StringReader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.harmony.test.stress.api.java.io.stress.readers.share.ReaderPair;
import org.apache.harmony.test.stress.api.java.io.stress.readers.share.ReaderProvider;
import org.apache.harmony.test.stress.api.java.io.stress.readers.share.ReaderWriterTest;

import org.apache.harmony.share.Result;

/*
 * Jan 16, 2006
 */
public class StringTest extends ReaderWriterTest {
	class Provider extends ReaderProvider {

		public ReaderPair getNext() throws IOException {
			return new ReaderPair() {
				StringWriter output;
				public Writer getOutput() {
					return output = new StringWriter();
				}
				
				public Reader getInput() {
					return new StringReader(((StringWriter)output).getBuffer().toString()); 
				}

				public void teardown() throws IOException {
				}
			};
		}
		
		public boolean isAsynchronous() {
			return false;
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
		System.exit(new StringTest().test(args));
	}	
}
