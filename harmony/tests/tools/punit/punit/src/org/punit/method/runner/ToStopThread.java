/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.method.runner;

public class ToStopThread extends Thread {

	private long _timeout;

	private Object _lock = new Object();

	private boolean _closed;

	private Runnable _runnable;

	public ToStopThread(Runnable runnable, long timeout) {
		_runnable = runnable;
		_timeout = timeout;
	}

	public void run() {
		synchronized (_lock) {
			try {
				_lock.wait(_timeout);
			} catch (InterruptedException e) {

			}
			if(_closed) {
				return;
			}
			_runnable.run();
		}
	}

	public void close() {
		_closed = true;
		synchronized (_lock) {
			_lock.notifyAll();
		}
	}

}
