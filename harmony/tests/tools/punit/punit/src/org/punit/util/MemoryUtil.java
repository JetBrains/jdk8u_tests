package org.punit.util;

import org.punit.assertion.Assert;
import org.punit.exception.OutOfMemoryException;

public class MemoryUtil {

    private static final int GC_TIMES = 3;

	private static Runtime _runtime = Runtime.getRuntime();
    
    public static long totalMemory() {
        return _runtime.totalMemory();
    }

    public static long usedMemory() {
        return totalMemory() - _runtime.freeMemory();
    }

    public static void clear() {
		for (int i = 0; i < GC_TIMES; ++i) {
			System.gc();
			System.runFinalization();
		}
		ThreadUtil.sleepIgnoreInterruption(100);
	}
    
    private static final int WILDERNESS_SIZE = 4096;

    private static byte[] _wilderness;

    static {
        allocateWilderness();
    }

    public static void allocateWilderness() {
        Assert.assertNull(_wilderness);
        try {
            _wilderness = new byte[WILDERNESS_SIZE];
        } catch (OutOfMemoryError oome) {
            throw new OutOfMemoryException(oome);
        }
    }

    public static void releaseWilderness() {
        Assert.assertNotNull(_wilderness);
        _wilderness = null;
    }
}
