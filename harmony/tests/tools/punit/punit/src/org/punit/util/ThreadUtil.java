package org.punit.util;

public class ThreadUtil {
	public static void sleepIgnoreInterruption(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {

		}
	}
}
