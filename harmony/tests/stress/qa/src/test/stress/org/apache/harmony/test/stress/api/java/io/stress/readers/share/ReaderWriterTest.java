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

package org.apache.harmony.test.stress.api.java.io.stress.readers.share;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;

import org.apache.harmony.test.stress.api.java.io.stress.share.ThreadPool;

import org.apache.harmony.share.MultiCase;
import org.apache.harmony.share.Result;

/*
 * Jan 12, 2006
 */
public abstract class ReaderWriterTest extends MultiCase {

	protected int chunkSize = 4096;

	protected int streamsCount = 1000;

	protected int count = 128;

	protected void setBigdataScenario() {
		count = 4;
		chunkSize = 1024 * 128; 
		streamsCount = 16;
	}

	protected void setManyStreamsScenario() {
		count = 128;
		chunkSize = 1024;
		streamsCount = 128;
	}

	protected Result testImpl(ReaderProvider provider) {
		boolean failed = false;
		ThreadPool pool = new ThreadPool(streamsCount / 4);

		for (int i = 0; i < streamsCount; i++) {
			pool.invoke(new Executor(provider, i));
		}
		pool.finish();
		Iterator it = pool.runners();
		while (it.hasNext()) {
			failed |= !((Executor) it.next()).ok;
		}

		return failed ? failed("FAILED") : passed();

		// return 0;
	}

	protected boolean read(Reader input, int count, int id)
			throws IOException {
		RandomReader rr = new RandomReader();
		rr.setPos(id);
		int size = chunkSize * count;

		boolean error = false;

		int read = 0;
		int need = 0;
		while (size-- > 0) {
			read = input.read();
			need = rr.read();
			if (read != need) {
				error = true;
			}
		}
		return !error;
	}

	protected void write(Writer output, int count, int id) {
		RandomReader rr = new RandomReader();
		rr.setPos(id);
		int size = count * chunkSize;
		int input;
		try {
			while (size-- > 0) {
				input = rr.read();
				output.write(input);
			}
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected class Executor implements Runnable {
		int id;

		boolean ok = false;

		boolean checked = false;

		ReaderProvider provider;

		public Executor(ReaderProvider provider, int id) {
			this.provider = provider;
			this.id = id;
		}

		public void run() {
			try {
				final Monitor written = new Monitor(2);

				final ReaderPair pair = provider.getNext();
				Thread writer = new Thread(new Runnable() {
					public void run() {
						Writer os = null;
						try {
							os = pair.getOutput();
							write(os, count, id);
						} catch (IOException e) {
							if (os != null) {
								try {
									os.close();
								} catch (IOException ex) {
									throw new RuntimeException(ex);
								}
							}							
						}
						try {
							written.enter();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});

				Thread reader = new Thread(new Runnable() {
					public void run() {
						try {
							if (!provider.isAsynchronous()) {
								try {
									written.enter();
								} catch (InterruptedException e) {
									throw new RuntimeException(e);
								}
							}
							Reader is = pair.getInput();

							if (read(is, count, id)) {
								checked = true;
							}
							
							if (provider.isAsynchronous()) {
								try {
									written.enter();
								} catch (InterruptedException e) {
									throw new RuntimeException(e);
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});

				writer.start();
				reader.start();

				writer.join();
				reader.join();

				pair.teardown();

				if (checked) {
					ok = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}

class Monitor {

	int visitors = 0;

	int size;

	public Monitor(int size) {
		this.size = size;
	}

	public void enter() throws InterruptedException {
		synchronized (this) {
			visitors++;
			
			if (visitors < size) {
				this.wait();
				return;
			} else if (visitors == size) {
				visitors = 0;
				this.notifyAll();
			} else {
				throw new RuntimeException("Monitor was broken");
			}
		}
	}
}

