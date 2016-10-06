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
package org.apache.harmony.test.func.api.java.io.share.PrintStream;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.harmony.test.func.api.java.io.share.FilterOutputStream.FilterOutputStreamTestShared;
import org.apache.harmony.test.func.api.java.io.share.MockOutputStream;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.share.Result;

//TODO: add tests for uncovered methods
public class PrintStreamTestShared extends FilterOutputStreamTestShared {
   protected OutputStream getOutputStream(OutputStream os) {
        return new PrintStream(os);
    }

    public Result testClose() throws IOException {
        MockOutputStream os = new MockOutputStream(5);
        OutputStream fos = getOutputStream(os);
        fos.write("xyz".getBytes());
        fos.close();

        if (os.isClosed() && "xyz".equals(os.getWrittenAsString())) {
            return passed();
        }

        return failed("expected underlying stream to be closed");
    }

    public Result testFlush() throws IOException {
        return super.testFlush();
    }

    public Result testOutField() throws IOException {
        return super.testOutField();
    }

    public Result testWrite0001() throws IOException {
        return super.testWrite0001();
    }

    public Result testWriteByte() throws IOException {
        return super.testWriteByte();
    }

    public Result testWriteIndexOutOfBounds0001() throws IOException {
        return super.testWriteIndexOutOfBounds0001();
    }

    public Result testWriteIndexOutOfBounds0002() throws IOException {
        return super.testWriteIndexOutOfBounds0002();
    }

    public Result testWriteIndexOutOfBounds0003() throws IOException {
        return super.testWriteIndexOutOfBounds0003();
    }

    public Result testWriteNull() throws IOException {
        return super.testWriteNull();
    }

    public Result testWriteSlice() throws IOException {
        return super.testWriteSlice();
    }

    public Result testNoAutoFlush() throws IOException {
        MockOutputStream os = new MockOutputStream(5);
        OutputStream fos = getOutputStream(new BufferedOutputStream(os));

        MultiThreadRunner.waitAtBarrier();
        
        fos.write("xyz".getBytes());

        try {
            if (os.getWrittenAsString().length() == 0) {
                return passed();
            }
        } finally {
            fos.close();
        }

        return failed("expected data to be buffered");
    }

    public Result testAutoFlush() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream fos = new PrintStream(new BufferedOutputStream(baos), true);
        final int EOF_LENGTH = System.getProperty("line.separator").length();
       
        MultiThreadRunner.waitAtBarrier();
        
        fos.write("xyz".getBytes());
        if(baos.size() != 3) {
            return failed("expected flush after byte array write");
        }
        baos.reset();
        
        fos.println();
        if(baos.size() != EOF_LENGTH) {
            return failed("expected flush after println() " + baos.size() + " " + EOF_LENGTH);
        }
        baos.reset();
        
        fos.println(true);
        if(baos.size() != 4 + EOF_LENGTH) {
            return failed("expected flush after println(boolean) " + baos.size() + " " + EOF_LENGTH);
        }
        baos.reset();
        
        fos.println('c');
        if(baos.size() != 1 + EOF_LENGTH) {
            return failed("expected flush after println(char)" +  baos.size() + " " + EOF_LENGTH);
        }
        baos.reset();

        fos.println("abc".toCharArray());
        if(baos.size() != 3 + EOF_LENGTH) {
            return failed("expected flush after println(char[])" + baos.size() + " " + EOF_LENGTH);
        }
        baos.reset();

        fos.println((int) 1);
        if(baos.size() != 1 + EOF_LENGTH) {
            return failed("expected flush after println(int)" + baos.size() + " " + EOF_LENGTH);
        }
        baos.reset();

        fos.println(1L);
        if(baos.size() != 1 + EOF_LENGTH) {
            return failed("expected flush after println(long)" + baos.size() + " " + EOF_LENGTH);
        }
        baos.reset();

        fos.println((float) 1.0);
        if(baos.size() != 3 + EOF_LENGTH) {
            return failed("expected flush after println(float)" + baos.size() + " " + EOF_LENGTH);
        }
        baos.reset();

        fos.println((double) 1.0);
        if(baos.size() != 3 + EOF_LENGTH) {
            return failed("expected flush after println(double)" + baos.size() + " " + EOF_LENGTH);
        }
        baos.reset();
        
        fos.println("abc");
        if(baos.size() != 3 + EOF_LENGTH) {
            return failed("expected flush after println(String)" + baos.size() + " " + EOF_LENGTH);
        }
        baos.reset();

        fos.println((Object) null);
        if(baos.size() != 4 + EOF_LENGTH) {
            return failed("expected flush after println(Object)" + baos.size() + " " + EOF_LENGTH);
        }
        baos.reset();

        fos.print('a');
        if(baos.size() != 0) {
            return failed("expected bufferization after write(char) " + baos.size());
        }
        baos.reset();
        
        return passed();
    }
}

