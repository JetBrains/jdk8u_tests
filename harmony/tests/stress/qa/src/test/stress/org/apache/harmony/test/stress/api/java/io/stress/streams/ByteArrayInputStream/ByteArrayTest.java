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

package org.apache.harmony.test.stress.api.java.io.stress.streams.ByteArrayInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.harmony.test.stress.api.java.io.stress.streams.share.SimpleStreamProvider;
import org.apache.harmony.test.stress.api.java.io.stress.streams.share.StreamTest;

import org.apache.harmony.share.Result;

/*
 * Jan 12, 2006
 */
public class ByteArrayTest extends StreamTest {

	class Provider extends SimpleStreamProvider {
		// ByteArray Stream
		public void next() throws IOException {
			in = null;
			out = new ByteArrayOutputStream();
		}

		public InputStream getInput() throws IOException {
			if (in != null) {
				return in;
			} else {
				return in = new ByteArrayInputStream(
						((ByteArrayOutputStream) out).toByteArray());
			}
		}

	}
	
	public Result testManyStreams()
	{
		setManyStreamsScenario();
		return testImpl(new Provider());
	}
	
	public Result testBigObj()
	{
		setBigdataScenario();
		return testImpl(new Provider());
	}
	
	public static void main(String[] args) {
		System.exit(new ByteArrayTest().test(args));
	}
}
