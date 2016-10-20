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
 * @version $Revision: 1.3 $
 */

package org.apache.harmony.test.stress.api.java.io.stress.streams.FileInputStream;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.harmony.test.stress.api.java.io.stress.share.Tools;
import org.apache.harmony.test.stress.api.java.io.stress.streams.share.FileProvider;
import org.apache.harmony.test.stress.api.java.io.stress.streams.share.StreamTest;

import org.apache.harmony.share.Result;

/*
 * Dec 26, 2005
 */
public class FileStreamTest extends StreamTest {

	protected String tempDir = null;

	private boolean configure(String[] args) {
		Properties props = Tools.parseArgs(args);
		tempDir = props.getProperty("temp");
		if (tempDir == null) {
			return false;
		}
		
		tempDir += File.separator;

		return true;
	}

	public int test(String[] args) {
		if (!configure(args)) {
			return fail("Supplied command line arguments is not valid");
		}
		return super.test(args);
	}

	public Result testGetFile() {
		final String name = tempDir + this.getClass().getCanonicalName();

		return testImpl(new FileProvider() {
			private int c = 0;

			private String n = name;

			public File getFile() throws IOException {
				return new File(n + (++c) + ".tmp");
			}
		});

	}

	public Result testGetTemp() {

		return testImpl(new FileProvider() {
			public File getFile() throws IOException {
				return File.createTempFile("fios", ".tmp", new File(tempDir));
			}
		});
	}

	public static void main(String[] args) {
		System.exit(new FileStreamTest().test(args));

	}

}
