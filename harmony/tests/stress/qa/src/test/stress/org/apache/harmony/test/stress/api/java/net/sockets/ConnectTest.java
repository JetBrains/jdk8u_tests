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

package org.apache.harmony.test.stress.api.java.net.sockets;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.harmony.share.DRLLoggerL;
import org.apache.harmony.share.DRLLogging;
import org.apache.harmony.share.Test;

/*
 * Nov 16, 2005
 */
public class ConnectTest extends Test {

	private int clientsOK = 0;

	private class Client implements Runnable {
		int port;

		String host;

		public Client(String host, int port) {
			this.port = port;
			this.host = host;
		}

		public void run() {
			try {
				Socket s = new Socket(host, port);
				OutputStream os = s.getOutputStream();

				for (int i = 0; i < 10; i++) {
					byte[] data = new byte[1024 * 1025];
					os.write(data);
				}
				log.info("*");
				s.close();

				clientsOK++;
			} catch (OutOfMemoryError e) {
			} catch (ConnectException e) {
			} catch (SocketException e) {
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public int test() {
		try {
			ServerSocket ss = new ServerSocket();
			ss.bind(new InetSocketAddress("localhost", 0));

			for (int i = 0; i < 100; i++) {
				log.info(".");
				createClients(ss, 2);
			}
			waitClients();
		} catch (OutOfMemoryError e) {
		} catch (IOException e) {
			e.printStackTrace();
			return fail("FAILED");
		}

		try {
			clientsOK = 0;
			threads.clear();
			ServerSocket ss = new ServerSocket();
			ss.bind(new InetSocketAddress("localhost", 0));
			createClients(ss, 1);
			waitClients();
			return clientsOK == 1 ? pass("PASSED")
					: fail("FAILED " + clientsOK);
		} catch (Throwable e1) {
			e1.printStackTrace();
			return fail("FAILED");
		}
	}

	LinkedList threads = new LinkedList();

	private Thread addThread(Thread t) {
		synchronized (threads) {
			threads.add(t);
		}
		return t;
	}

	private void waitClients() {
		for (int i = 0; i < threads.size(); i++) {
			
			try {
				Thread t = (Thread) threads.get(i);
				if (t.isAlive()) {
					t.join(3000);
				}
				if (t.isAlive()) {
					log.add("Thread " + t + " was not finished normally");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void createClients(ServerSocket ss, int count) throws IOException {
		for (int j = 0; j < count; j++) {
			addThread(new Thread(new Client("localhost", ss.getLocalPort())))
					.start();
		}
		final Socket s = ss.accept();
		addThread(new Thread(new Runnable() {
			public void run() {

				byte[] buffer = new byte[777];
				InputStream is;
				try {
					is = s.getInputStream();
					while (is.read(buffer) != -1)
						;
					is.close();
				} catch (OutOfMemoryError e) {
				} catch (SocketException e) {
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		})).start();
	}

	public static void main(String[] args) {
		System.exit(new ConnectTest().test(args));
	}
}
