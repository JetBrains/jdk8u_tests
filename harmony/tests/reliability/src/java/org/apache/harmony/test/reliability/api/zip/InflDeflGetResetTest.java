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
 * @author Oleg Oleinik
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.zip;

import org.apache.harmony.test.reliability.share.Test;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.Random;

/**
 * Goal: cover getTotalIn(), getTotalOut(), reset(), end() methods and reveal 
 *       possible resource leaks when working with a single instance of Deflater/Inflater.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of internal iterations
 *          param[1] - maximum byte array size to deflate/inflate
 *
 *    2. On each iteration:
 *
 *    3. Creates NUM_BYTE_ARRAYS arrays of variable random length and random byte values.
 *       Creates single instance of Deflater and Inflater. Then, for each array:
 *
 *    4. Deflates it into a byte array of sufficient length, checks for correctness values
 *       returned by getTotalIn(), getTotalOut(), remembers adler checksum and resets Deflater.
 *
 *    5. Does one additional step - deflates a byte array into small output array, does not
 *       reset Deflater, instead, just closes the Deflater (hoping this will cause a leak).
 *
 *    6. Inflates each of deflated arrays into a byte array of sufficient length, checks for 
 *       correctness values returned by getTotalIn(), getTotalOut() and adler checksum, resets Inflater.
 *
 *    7. Does one additional step - inflates a byte array into small output array, does not
 *       reset Inflater, instead, just closes the Inflater (hoping this will cause a leak).
 *
 *    8. Calls System.gc();
 *
 */

public class InflDeflGetResetTest extends Test {

    // In-memory buffer size
    public int bufferSize = 1024;
        
    public static final int NUM_BYTE_ARRAYS = 10;

    public int NumberOfIterations = 100;

    public int callSystemGC = 1;


    public static void main(String[] args) {
        System.exit(new InflDeflGetResetTest().test(args));
    }


    public int test(String[] params) {
    
        parseParams(params);

        for (int j = 0; j < NumberOfIterations; ++j) {

            // array of NUM_BYTE_ARRAYS byte arrays to be filled in and compressed
            byte[][] b_to_compress = new byte[NUM_BYTE_ARRAYS][];

            // array of NUM_BYTE_ARRAYS byte arrays into which we will compress
            byte[][] b_compressed = new byte[NUM_BYTE_ARRAYS][];

            // array of NUM_BYTE_ARRAYS byte arrays into which we will decompress
            byte[][] b_decompress_to = new byte[NUM_BYTE_ARRAYS][];

            // number of bytes deflate() returned for each of NUM_BYTE_ARRAYS byte arrays
            int[] compressed_sizes = new int[NUM_BYTE_ARRAYS];

            // adler checksums for each of compressed NUM_BYTE_ARRAYS byte arrays
            int[] adler_before = new int[NUM_BYTE_ARRAYS];
   
            // use a single instance of Deflater for operations with NUM_BYTE_ARRAYS arrays

            Deflater defl = new Deflater();

            for (int i = 0; i < b_to_compress.length; ++i){

                // create byte array of random width with random byte values

                b_to_compress[i] = ByteArray.getRandomBytes(bufferSize);

                // create array of sufficient size into which we will store compressed bytes

                b_compressed[i] = new byte[b_to_compress[i].length * 2];

                defl.setInput(b_to_compress[i]);
                defl.finish();

                // store number of bytes returned by deflate()

                compressed_sizes[i] = defl.deflate(b_compressed[i]);

                if (defl.getTotalOut() != compressed_sizes[i]) {
                    log.add("Iteration: " + j + ", getTotalOut() returns " + defl.getTotalOut()  
                        + ", while deflate() returned " + compressed_sizes[i]);
                }

                if (defl.getTotalIn() != b_to_compress[i].length) {
                    log.add("Iteration: " + j + ", getTotalIn() returns " + defl.getTotalIn() + 
                        ", while to be compressed array size is " + b_to_compress[i].length);
                }

                // store adler checksum

                adler_before[i] = defl.getAdler();

                defl.reset();

            }

            // final operation - compress the 10-th part of some array, for example 
            // lets take b_to_compress[0]:

            defl.setInput(b_to_compress[0], 0, b_to_compress[0].length / 10);
            defl.finish();

            // compress this 10-th part into 10-th part of another newly created array
            // why? - just in case deflate looses memory is byte array into which we compress
            // is smaller than required for complete compression and we end() our deflater.

            int c_s = defl.deflate(new byte[compressed_sizes[0] / 10]);

            // intentionally commented reset() - to try to loose left unused part of array 
            // b_compressed[0] or the whole array.

            // defl.reset();

            defl.end();

            // use a single instance of Inflater for operations with NUM_BYTE_ARRAYS arrays

            Inflater infl = new Inflater();

            for (int i = 0; i < b_to_compress.length; ++i){

                b_decompress_to[i] = new byte[b_to_compress[i].length * 2];

                infl.setInput(b_compressed[i], 0, compressed_sizes[i]);

                int inflated_bytes = 0;

                try {

                    inflated_bytes = infl.inflate(b_decompress_to[i]);

                } catch (DataFormatException dfe){
                    log.add("Iteration: " + j + " DataFormatException is thrown");
                    continue;
                }

                if (adler_before[i] != infl.getAdler()) {
                    log.add("adler before compression is: " + adler_before[i] + ", adler after is: " 
                        + infl.getAdler());
                }

                if (inflated_bytes != b_to_compress[i].length){
                    log.add("inflate() returned " + inflated_bytes + " instead of " + b_to_compress[i].length);
                }

                if (infl.getTotalOut() != inflated_bytes) {
                    log.add("getTotalOut() returns " + infl.getTotalOut() + ", while inflate() returned " 
                        + inflated_bytes);
                }

                if (infl.getTotalIn() != compressed_sizes[i]) {
                    log.add("getTotalIn() returns " + infl.getTotalIn() + ", while to be decompressed " + 
                        " byte numebr is " + compressed_sizes[i]);
                }

                infl.reset();

                // log.add(" " + j + " " + i + " " + b_to_compress[i].length + " " + 
                //         compressed_sizes[i] + " " + inflated_bytes);
            }

            // lets try to check whether inflate() looses memory if we inflate into 
            // array of insufficient size and just forget about this array, end()-ing
            // the inflater:

            infl.setInput(b_compressed[0], 0, compressed_sizes[0]);

            try {

                infl.inflate(b_decompress_to[0], 0, b_decompress_to[0].length / 10);

            } catch (DataFormatException dfe){
                log.add("Last compression: Iteration: " + j + " DataFormatException is thrown");
            }

            // intentionally commented reset() - to try to loose left unused part of array 
            // b_decompress_to[0] or the whole array.

            // infl.reset();

            infl.end();

                        
            if (callSystemGC != 0){
                System.gc();
            }
        }

        return pass("OK");
    }


    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NumberOfIterations = Integer.parseInt(params[0]);
            //log.add("Number of iterations : " + NumberOfIterations);
        }

        if (params.length >= 2) {
            bufferSize = Integer.parseInt(params[1]);
        }

    }

}


class ByteArray {

    static Random rand = new Random();

    public static byte[] getRandomBytes(int size) {

        byte[] b = new byte[100 + rand.nextInt(size)];

        for (int i = 0; i < b.length; ++i) {
            b[i] = (byte) rand.nextInt(Byte.MAX_VALUE);
        }

        return b;

    }
}





















