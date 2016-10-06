/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Nikolay V. Bannikov
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.zip;

import org.apache.harmony.test.reliability.share.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.Random;

/**
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - size of byte buffer to initialize for Delfation/Inflation
 *          param[1] - number of Deflate/Inflate iterations
 *
 *    2. Initializes byte array of param[0] length with random values from 0..255.
 *
 *    3. Number of Deflate/Inflate iterations is equal to the size of byte buffer (param[0]).
 *       On each iteration:
 *          a. Receives subbuffer of length equal to iteration number. Each next Deflate/Inflate
 *             operation works with a byte buffer of larger size.
 *          b. Deflates and then inflates the subbuffer.
 *          c. Compares that deflated/inflated byte array is byte-to-byte equal to the the original 
 *             one, i.e. subbuffer.
 *          d. Calls System.gc().
 */

public class ZlibTestVariableLength extends Test {

    // In-memory buffer size
    public int bufferSize = 1024;
        
    public int NumberOfIterations = 100;

    public int callSystemGC = 1;


    public static void main(String[] args) {
        System.exit(new ZlibTestVariableLength().test(args));
    }


    public int test(String[] params) {
    
        parseParams(params);

        // initialize a byte buffer which will be compressed/decompressed

        BufferStorage bs = new BufferStorage();
        bs.initialize(bufferSize);

        //log.add("Byte array buffer of length " + bufferSize + " is initialized");
                  
        int step = (bufferSize / NumberOfIterations) < 1 ? 1 : (bufferSize / NumberOfIterations);

        for (int i = 1; i < bs.buffer.length; i = i + step) {

            byte[] subbuffer = bs.getSubBuffer(i);
            byte[] compressed = InflatorDeflator.zlibDef(subbuffer, 0, subbuffer.length);
            byte[] decompressed;

            try {

                decompressed = InflatorDeflator.zlibInf(compressed, 0, compressed.length);

            } catch (DataFormatException e) {
                return fail("DataFormatException while Inflating on iteration: " + i);
            }

            if (!InflatorDeflator.compareArrays(decompressed, subbuffer)){
                return fail("Iteration " + i + ": decompressed bytes are not equal to original");
            }
                        
            if (callSystemGC != 0){
                System.gc();
            }
        }

        return pass("OK");
    }


    public void parseParams(String[] params) {

        if (params.length >= 1) {
            bufferSize = Integer.parseInt(params[0]);
        }

        if (params.length >= 2) {
            NumberOfIterations = Integer.parseInt(params[1]);
        }


    }

}


class BufferStorage {

    public byte[] buffer = null;

    public void initialize(int bufferSize) {

        if (buffer == null){

            buffer = new byte[bufferSize];

            // why random? - to avoid small vocabulary
            Random rand = new Random(100);

            for (int i = 0; i < bufferSize; ++i) {
                buffer[i] = (byte) rand.nextInt(Byte.MAX_VALUE);
            }
        }
    }


    public byte[] getSubBuffer(int i){

        byte[] b = new byte[i];

        System.arraycopy(buffer, 0, b, 0, b.length);

        return b;
    }

}





















