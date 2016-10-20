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
 * @version $Revision: 1.5 $
 */

package org.apache.harmony.test.stress.api.java.io.stress.streams.share;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;
import org.apache.harmony.test.stress.api.java.io.stress.share.ThreadPool;

/*
 * Dec 20, 2005
 */
public abstract class StreamTest extends MultiCase {
	protected boolean finishWriteBeforeRead() {
		return true;
	}
	
	protected int chunkSize = 4096;

	protected int streamsCount = 100;

	protected void setBigdataScenario() {
		chunkSize = 1024 * 1024; 
		streamsCount = 32; // Should be enough
	}

	protected void setManyStreamsScenario() {
		chunkSize = 8192; // small pieces
		streamsCount = 1024; 
	}

	protected Result testImpl(StreamProvider provider) {
		boolean passed = true;
		ThreadPool pool = new ThreadPool(10);

		for (int i = 0; i < streamsCount; i++) {
			pool.invoke(new Executor(provider, i));
		}
		pool.finish();
		Iterator it = pool.runners();
		while (it.hasNext()) {
			passed &= ((Executor) it.next()).ok;
		}

		return passed ? passed() : failed("FAILED");

		// return 0;
	}

	protected boolean read(InputStream input, int id) throws IOException {
		RandomInputStream cg = new RandomInputStream();
		cg.setPos(id);
		int size = chunkSize;

		while (size-- > 0) {
			int i1 = input.read();
			int i2 = cg.read();
			if (i1 != i2) {
				return false;
			}
		}

		if (input.read() != -1) {
			return false;
		}
		return true;
	}

	protected void write(OutputStream output, int id) {
		RandomInputStream cg = new RandomInputStream();
		cg.setPos(id);
		byte[] data = new byte[chunkSize];
		try {
			cg.read(data);
//			System.err.println("output is " + output);
			output.write(data);
		} catch (IOException e) {
//			synchronized(IOException.class) {
//			System.err.println("error. output is " + output);
//			System.err.println("data is");
//			for(int i = 0; i < data.length; ++i) {
//				System.err.println("" + i + "\t: " + data[i]);
//			}
			e.printStackTrace();
//			}
		}
	}

	protected class Executor implements Runnable {
		int id;

		boolean ok = false;

		StreamProvider provider;

		public Executor(StreamProvider provider, int id) {
			this.provider = provider;
			this.id = id;
		}

		public void run() {
			boolean checked = false;
			try {
				final StreamProvider sp = provider.nextProvider();

				Thread writer = new Thread(new Runnable() {
					public void run() {
						try {
							OutputStream os = sp.getOutput();
							write(os, id);
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				
				writer.start();
				
				if(finishWriteBeforeRead()) {
					try {
						writer.join();
					} catch (InterruptedException e) {
					}
				}

				InputStream is = sp.getInput();
				if (read(is, id)) {
					checked = true;
				}
				
				if(!finishWriteBeforeRead()) {
					try {
						writer.join();
					} catch (InterruptedException e) {
					}
				}

				is.close();
				provider.teardown();
				sp.teardown();
				if (checked) {
					ok = true;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}

interface StreamProvider {
	void next() throws IOException;

	StreamProvider nextProvider() throws IOException;

	void teardown() throws IOException;

	InputStream getInput() throws IOException;

	OutputStream getOutput() throws IOException;
	
}
