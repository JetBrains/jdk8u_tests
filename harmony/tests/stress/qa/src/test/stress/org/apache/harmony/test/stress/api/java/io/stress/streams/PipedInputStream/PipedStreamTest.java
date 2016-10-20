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
 * @version $Revision: 1.4 $
 */

package org.apache.harmony.test.stress.api.java.io.stress.streams.PipedInputStream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.harmony.test.stress.api.java.io.stress.streams.share.SimpleStreamProvider;
import org.apache.harmony.test.stress.api.java.io.stress.streams.share.StreamTest;

import org.apache.harmony.share.Result;

/*
 * Dec 28, 2005
 */
public class PipedStreamTest extends StreamTest {
	protected boolean finishWriteBeforeRead() {
		return false;
	}

	class Provider extends SimpleStreamProvider {
		public void next() throws IOException {
			in = new PipedInputStream();
			out = new PipedOutputStream((PipedInputStream) in);
		}
	}

	public Result testManyStreams() {
		//setManyStreamsScenario();
		return testImpl(new Provider());
	}

	public Result testBigObj() {
		//setBigdataScenario();
		return testImpl(new Provider());
	}

	public static void main(String[] args) {
		System.exit(new PipedStreamTest().test(args));
	}

}
