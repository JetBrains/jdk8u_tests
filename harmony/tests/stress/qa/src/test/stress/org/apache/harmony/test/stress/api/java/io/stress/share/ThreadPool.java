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

package org.apache.harmony.test.stress.api.java.io.stress.share;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Allows to wait for multiple threads to finish in one call
 * 
 */
public class ThreadPool {
	private ArrayList threads = new ArrayList();

	private ArrayList runners = new ArrayList();

	private int maxThreads = -1;

	public ThreadPool()
	{
		this(Integer.MAX_VALUE);
	}
	
	public ThreadPool(int max)
	{
		maxThreads = max;
	}

	
	public void invoke(Runnable r) {
		Thread t = new Thread(r);
		if (threads.size() > maxThreads) {
			try {
				FindThread: while (true) {
					for (int i = 0; i < threads.size(); i++) {
						((Thread) threads.get(i)).join(10);
						if (!((Thread)threads.get(i)).isAlive()) {
							threads.remove(i);
							break FindThread;
						}
					}
				}
			} catch (InterruptedException e) {
				throw new Error(e);
			}
			
		}
		threads.add(t);
		t.start();
		runners.add(r);
	}

	public void finish() {
		Iterator i = threads.iterator();
		while (i.hasNext()) {
			try {
				((Thread) i.next()).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Iterator runners() {
		return runners.iterator();
	}
}