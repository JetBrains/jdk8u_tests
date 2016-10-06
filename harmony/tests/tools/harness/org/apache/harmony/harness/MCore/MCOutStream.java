/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Vladimir A. Ivanov
 * @version $Revision: 1.7 $
 */

/**
 * This class provides the cyclic buffer to write and read data
 */
package org.apache.harmony.harness.MCore;

import java.io.IOException;
import java.io.OutputStream;

public class MCOutStream extends OutputStream {

    protected byte[] dataBuf          = new byte[Short.MAX_VALUE];
    private int      curWritePosition = 0;
    private int      curReadPosition  = 0;

    /*
     * (non-Javadoc)
     * 
     * @see java.io.OutputStream#write(int)
     */
    public synchronized void write(int dataByte) throws IOException {
        if (curWritePosition + 1 > dataBuf.length) {
            curWritePosition = 0;
        }
        dataBuf[curWritePosition++] = (byte)dataByte;
    }

    /*
     * non-blocking read the next byte from the output. Return 0 if no data
     * available.
     */
    public synchronized byte read() {
        if (isAvailible()) {
            if (curReadPosition + 1 > dataBuf.length) {
                curReadPosition = 0;
            }
            return dataBuf[curReadPosition++];
        }
        return 0;
    }

    /*
     * return true if next byte is available to read
     */
    public synchronized boolean isAvailible() {
        if (curReadPosition != curWritePosition) {
            return true;
        }
        return false;
    }

    /*
     * return the numbers of byte that are available to read
     */
    public synchronized int available() {
        if (curReadPosition == curWritePosition) {
            return 0;
        } else if (curReadPosition < curWritePosition) {
            return curWritePosition - curReadPosition;
        }
        return curReadPosition - curWritePosition;
    }
}