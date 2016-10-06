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

import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.util.Random;


/**
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of Adler32/CRC32.update() iterations
 *          param[1] - byte arrays size to update Adler32 with
 *
 *    2. Creates two byte arrays - one with ordered sequential valies, another 
 *       with random byte values.
 *
 *    3. Starts param[0] iterations. At each iteration:
 *
 *       a. Creates Adler32 and CRC32 objects.
 *       b. For each object and byte array:
 *          c. in a cycle calls update(byte[] array, 0, length) with variable 
 *             length value - from 'array.length' to '0'.
 *          d. checks that getValue() returns the same result being called twice sequentially.
 *          e. calls update(int).
 *          f. calls reset().
 *
 *       g. calls System.gc();
 *
 */

public class AdlerCRC32Test extends Test {

    // Number of iterations
    public int NUMBER_OF_ITERATIONS = 10;

    // size of allocated byte arrays
    public int SIZE = 1000;

    public int callSystemGC = 1;


    public static void main(String[] args) {
        System.exit(new AdlerCRC32Test().test(args));
    }


    public int test(String[] params) {
    
        parseParams(params);

        byte[][] b = new byte[2][];

        // initialize ordered byte array (containing positive and negative values) once
        b[0] = ByteCreator.getOderedBytes(SIZE);

        long checksum;
            
        for (int k = 0; k < NUMBER_OF_ITERATIONS; ++k){

            // log.add("Strating " + k + " iteration");
 
            // initialize random byte array at each iteration
            b[1] = ByteCreator.getRandomBytes(SIZE);

            Checksum[] cs = {new Adler32(), new CRC32()};
                
            for (int c = 0; c < cs.length; ++c) {  // for each of Checksum algorithms...

                // log.add("" + cs[c].getClass().getName());

                for (int i = 0; i < b.length; ++i){  // for each of byte arrays...

                    // log.add("Byte array: " + i);
  
                    for (int j = 0; j <= b[i].length; j += 10){  // for each 'length'...

                        // just in case - GC does not free memory allocated for new Adler32, CRC32
                        Adler32 a = new Adler32();
                        CRC32 crc = new CRC32();

                        cs[c].update(b[i], 0, b[i].length - j);

                        checksum = cs[c].getValue();

                        // twice invoked getValue() must return the same result
                        if (cs[c].getValue() != checksum){

                            return fail("Checksum: " + cs[c].getClass().getName() + "byte array: " + i + 
                                ", iteration in byte array = " + j + ": getValue() != getValue()");
                        }
                        
                        // just in case, call update(int) as well
                        cs[c].update(j);
                    }

                    cs[c].reset();
                }

            }

            if (callSystemGC != 0){
                System.gc();
            }
                  
        }

        return pass("OK");
    }


    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUMBER_OF_ITERATIONS = Integer.parseInt(params[0]);
        }        

        if (params.length >= 2) {
            SIZE = Integer.parseInt(params[1]);
        }        

    }

}

class ByteCreator {

    public static byte[] getOderedBytes(int size) {

        byte[] b = new byte[size];

        int j = 0;

        // while() provides byte array is filled in with series of sequentially
        // increased byte values: MIN_VALUE..MAX_VALUE, MIN_VALUE..MAX_VALUE, etc.

        while (j < b.length){

            for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE && j < b.length; ++i){
                b[j++] = (byte) i;
            }

        }

        return b;
    }


    public static byte[] getRandomBytes(int size) {

        byte[] b = new byte[size];

        Random rand = new Random(10);

        for (int i = 0; i < b.length; ++i) {
            b[i] = (byte) rand.nextInt(Byte.MAX_VALUE);
        }

        return b;
    }

}

