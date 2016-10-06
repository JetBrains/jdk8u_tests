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

/**
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - size of byte buffer to initialize for Delfation/Inflation
 *
 *    2. For each combination of <strategy, compression level, wrap | nowrap>:
 *          a. Byte array of param[0] length with random values from 0..255 is initialized (for
 *             Deflator to use not only one dictionary).
 *          b. Deflates (with the given value of strategy, compression level, nowrap) 
 *             and then inflates the byte array.
 *          c. Compares that deflated/inflated byte array is byte-to-byte equal to the the original 
 *             one, i.e. orginal initialized buffer.
 *          d. Calls System.gc().
 */

public class ZlibTestStrategyLevel extends Test {

    // In-memory buffer size
    public int bufferSize = 1024;
        
    public int callSystemGC = 1;

    public int[] strategies = new int [] {
                                             Deflater.DEFAULT_STRATEGY,
                                             Deflater.HUFFMAN_ONLY,
                                             Deflater.FILTERED
                                         };
        
    public boolean[] nowrap = new boolean[] {false}; // should be also 'true' 

    public int[] level = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    public static void main(String[] args) {
        System.exit(new ZlibTestStrategyLevel().test(args));
    }


    public int test(String[] params) {
    
        parseParams(params);
                
        for (int i = 0; i < strategies.length; ++i) {

            for (int j = 0; j < level.length; ++j) {

                for (int k = 0; k < nowrap.length; ++k) {

                    // log.add("strategy: " + strategies[i] + ", level: " + level[j] + ", nowrap: " + nowrap[k]);

                    // Re-initialize byte array for Deflator to use not only one dictionary

                    BufferStorage bs = new BufferStorage();
                    bs.initialize(bufferSize);

                    // log.add("Byte array buffer of length " + bufferSize + " is initialized");

                    // get a copy of the initialized buffer, deflate it with the given 
                    // strategy, compression level, nowrap flag

                    byte[] subbuffer = bs.getSubBuffer(bs.buffer.length);
                    byte[] compressed = InflatorDeflator.zlibDef(subbuffer, 0, subbuffer.length, level[j], strategies[i], nowrap[k]);
                    byte[] decompressed;

                    try {

                        decompressed = InflatorDeflator.zlibInf(compressed, 0, compressed.length);

                    } catch (DataFormatException e) {
                        return fail("DataFormatException while Inflating: strategy: " + strategies[i] + ", level: " + level[j] + ", nowrap: " + nowrap[k]);
                    }

                    if (!InflatorDeflator.compareArrays(decompressed, subbuffer)){
                        return fail("strategy: " + strategies[i] + ", level: " + level[j] + ", nowrap: " + nowrap[k]+ " : decompressed bytes are not equal to original");
                    }
                        
                    if (callSystemGC != 0){
                        System.gc();
                    }

                }
            }               

        }

        return pass("OK");
    }


    public void parseParams(String[] params) {

        if (params.length >= 1) {
            bufferSize = Integer.parseInt(params[0]);
        }

    }

}





















