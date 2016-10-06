/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 */
package org.apache.harmony.test.func.api.java.io.share.PipedInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.harmony.test.func.api.java.io.share.InputStream.InputStreamTestShared;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

public class PipedInputStreamTestShared extends InputStreamTestShared {
    static final int BUF_LEN = 1024;

    Thread pipeThread;

    public InputStream getTestedInputStream() {
        try {
            PipedOutputStream pos = new PipedOutputStream();
            PipedInputStream pis = new PipedInputStream(pos);

            pipeThread = new PipeThread(super.getTestedInputStream(), pos);
            pipeThread.start();

            return pis;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Result testAvailable() throws IOException {
        try {
            InputStream is = getTestedInputStream();
            try {
                MultiThreadRunner.waitAtBarrier();
                int available = is.available();

                if (available == 0 || available == BUF_LEN) { //depends on
                                                              // version - both
                                                              // are correct
                    return passed();
                }

                return failed("InputStream 'available' method must return 0, or "
                        + BUF_LEN + ", got " + available);
            } finally {
                is.close();
            }
        } finally {
            try {
                pipeThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Result testClose() throws IOException {
        try {
            return super.testClose();
        } finally {
            try {
                pipeThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Result testMark() throws IOException {
        try {
            return super.testMark();
        } finally {
            try {
                pipeThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Result testMarkSupported() throws IOException {
        try {
            return super.testMarkSupported();
        } finally {
            try {
                pipeThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Result testReadByte() throws IOException {
        try {
            return super.testReadByte();
        } finally {
            try {
                pipeThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Result testReadBytes() throws IOException {
        try {
            return super.testReadBytes();
        } finally {
            try {
                pipeThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Result testReadBytes0001() throws IOException {
        try {
            return super.testReadBytes0001();
        } finally {
            try {
                pipeThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Result testReadBytes0002() throws IOException {
        try {
            return super.testReadBytes0002();
        } finally {
            try {
                pipeThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Result testReadBytes0003() throws IOException {
        try {
            return super.testReadBytes0003();
        } finally {
            try {
                pipeThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Result testReadBytesNull() throws IOException {
        try {
            return super.testReadBytesNull();
        } finally {
            try {
                pipeThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Result testReadBytesSlice() throws IOException {
        try {
            return super.testReadBytesSlice();
        } finally {
            try {
                pipeThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Result testReset() throws IOException {
        try {
            return super.testReset();
        } finally {
            try {
                pipeThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Result testSkip() throws IOException {
        try {
            return super.testSkip();
        } finally {
            try {
                pipeThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Result testReceive() throws IOException, InterruptedException {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStreamPublicReceive pis = new PipedInputStreamPublicReceive(
                pos);

        pipeThread = new PipeThread(super.getTestedInputStream(), pos);
        pipeThread.start();

        pis.read(new byte[20]);

        pis.close();

        pipeThread.join();

        if (pis.getReceived().size() != BUF_LEN + 2) {
            System.err.println("expected to receive " + (BUF_LEN + 2)
                    + " bytes, got " + pis.getReceived().size());
        }

        return passed();
    }

}

class PipeThread extends Thread {
    InputStream is;

    OutputStream os;

    PipeThread(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    public void run() {
        byte[] buf = new byte[PipedInputStreamTestShared.BUF_LEN];

        int read;
        try {
            while (true) {
                read = is.read(buf);
                if (read == -1) {
                    break;
                }
                os.write(buf, 0, read);
            }
        } catch (Throwable e) {
        }
    }
}

