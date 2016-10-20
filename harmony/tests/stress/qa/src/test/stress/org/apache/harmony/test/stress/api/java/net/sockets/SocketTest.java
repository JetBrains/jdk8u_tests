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

package org.apache.harmony.test.stress.api.java.net.sockets;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ListIterator;

import org.apache.harmony.share.Test;

/*
 * Nov 11, 2005
 */
public class SocketTest extends Test {

	private static final int MAX_DATA = 0xFFF;

	private static final int OK_RESPONSE = 0x1a1a1a1a;

	private static final int CLIENTS_COUNT = 15;

	private boolean hasErrors = false;

	private void err(String msg) {
		log.severe(msg);
		hasErrors = true;
		throw new RuntimeException();
	}

	/**
	 * Allows to receive from stream int's as one piece. Original stream crops
	 * int to two or even one byte
	 * 
	 * @see OutputProvider
	 */
	private class InputProvider {
		SocketChannel socketChannel;
		ByteBuffer bb = ByteBuffer.allocateDirect(4);

		public InputProvider(SocketChannel socketChannel) {
			this.socketChannel = socketChannel;
//			System.err.println("socketChannel: " + socketChannel);
//			try {
//				socketChannel.configureBlocking(true);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}

		public int read() throws IOException {
			int r = 0;
			int t = 0;
			int s = 0;
			

			bb.clear();
			socketChannel.read(bb);
			
			bb.flip();
			
			
			for (int i = 0; i < 4; i++) {
				t = bb.get();
				if (t == -1) {
					return -1;
				}
				t &= 0xFF;
				t <<= s;
				r += t;
				s += 8;
			}
			return r;
		}
	}

	/**
	 * Allows to send through stream int's as one piece. Original stream crops
	 * int to two or even one byte
	 * 
	 * @see InputProvider
	 */
	private class OutputProvider {
		private SocketChannel socketChannel;

		public OutputProvider(SocketChannel socketChannel) {
			this.socketChannel = socketChannel;
		}

		public void write(int v) throws IOException {
			byte[] out = new byte[4];

			for (int i = 0; i < 4; i++) {
				out[i] = (byte) (v & 0xFF);
				v >>= 8;
			}
			
			socketChannel.write(ByteBuffer.wrap(out));
		}
	}

	/**
	 * Allows to wait for multiple threads to finish in one call
	 * 
	 */
	private class ThreadPool {
		private ArrayList threads = new ArrayList();

		public void addThread(Thread t) {
			threads.add(t);
		}

		public void waitAll() {
			ListIterator e = threads.listIterator();
			while (e.hasNext()) {
				try {
					((Thread) e.next()).join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private class ServerThread implements Runnable {
		private SocketChannel sc;

		private ServerThread(SocketChannel server) {
			sc = server;
		}

		private int getClientID(int data) {
			return data >> 16;
		}

		private int getClientInfo(int data) {
			return data & 0xFFFF;
		}

		public void run() {
			Hashtable clients = new Hashtable();

			try {

				InputProvider is = new InputProvider(sc);
				OutputProvider os = new OutputProvider(sc);
				int data;

				while ((data = is.read()) != -1) {
					// log.info("Get :" + Integer.toHexString(data));

					Object key = new Integer(getClientID(data));
					Object value = clients.get(key);
					if (value != null) {
						((Client) value).check(data);
					} else {
						if (clients.size() < CLIENTS_COUNT) {
							clients.put(key, new Client(data));
						} else
							err("Maximum clients " + clients.size()
									+ " reached");
					}
					os.write(OK_RESPONSE);
				}

			} catch (IOException e) {
				err(e.getMessage());
				// e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				err(e.getMessage());
			}
		}

		private class Client {
			int seed;

			int id;

			int current;

			boolean isClosed;

			public Client(int data) throws Exception {
				seed = getClientInfo(data);
				id = getClientID(data);
				current = seed;
				if (seed != 0) {
					err("Invalid header :" + Integer.toHexString(data));
				}
			}

			public void check(int data) throws Exception {
				if (isClosed) {
					err("Invalid data");
				}

				if (getClientID(data) != id) {
					err("Invalid ClientID");
				}

				if (++current != getClientInfo(data)) {
					err("Invalid sequence");
				}
				isClosed |= (current - seed) == MAX_DATA;
			}

			public boolean isClosed() {
				return isClosed;
			}
		}
	}

	private class ClientThread implements Runnable {
		private SocketChannel socketChannel;

		private int id;

		public ClientThread(SocketChannel socketChannel, int id) {
			this.socketChannel = socketChannel;
			this.id = id << 16;
		}

		private boolean isOk = false;

		public boolean isOk() {
			return isOk;
		}

		public void run() {

			try {
				// log.info("Client " + (id >> 16) + " ready");
				int data = 0;
				OutputProvider os = new OutputProvider(socketChannel);
				InputProvider is = new InputProvider(socketChannel);

				while (data <= MAX_DATA) {
					// log.info("Sent :"
					// + Integer.toHexString(data + id));
					os.write(data + id);
					int read = is.read();
					if (read != OK_RESPONSE) {
						err("Invalid response: " + Integer.toHexString(read));
					}
					data++;
				}
			} catch (IOException e) {
				err(e.getMessage());
				// e.printStackTrace();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	private class Client {
		private SocketChannel s;

		private int id;

		public Client(SocketChannel client, int id) {
			s = client;
			this.id = id;
		}

		public void start() {

			final ClientThread[] clients = new ClientThread[CLIENTS_COUNT];
			final Thread[] threads = new Thread[clients.length];
			for (int i = 0; i < threads.length; i++) {
				clients[i] = new ClientThread(s, ++id);
				threads[i] = new Thread(clients[i]);
				threads[i].start();
			}
			new Thread(new Runnable() {
				public void run() {
					for (int i = 0; i < threads.length; i++) {
						try {
							threads[i].join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					try {
						s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	private class Connector {
		private SocketChannel clientSocketChannel;

		private SocketChannel serverSocketChannel;

		public SocketChannel getClientSocket() {
			return clientSocketChannel;
		}

		public SocketChannel getServerSocketChannel() {
			return serverSocketChannel;
		}

		public void connect(final ServerSocketChannel s) {
			try {
				clientSocketChannel = SocketChannel.open();
				Thread t = new Thread(new Runnable() {
					public void run() {
						try {
							serverSocketChannel = s.accept();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				t.start();

				clientSocketChannel.connect(new InetSocketAddress(s.socket().getInetAddress()
						.getHostName(), s.socket().getLocalPort()));

				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public int test() {
		try {
			ServerSocketChannel s = ServerSocketChannel.open();
			s.socket().bind(new InetSocketAddress("localhost", 0));
			int id = 0;
			ThreadPool pool = new ThreadPool();

			while (id < 1500) {
				Connector c = new Connector();
				c.connect(s);
				Thread server = new Thread(
						new ServerThread(c.getServerSocketChannel()));
				server.start();
				pool.addThread(server);

				Client client = new Client(c.getClientSocket(), id);
				id += CLIENTS_COUNT;
				client.start();
			}

			pool.waitAll();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return hasErrors ? fail("FAILED") : pass("PASSED");
	}

	public static void main(String[] args) {
		System.exit(new SocketTest().test(args));
	}
}
