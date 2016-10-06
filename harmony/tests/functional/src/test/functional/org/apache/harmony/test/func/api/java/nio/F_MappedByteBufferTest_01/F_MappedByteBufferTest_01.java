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

package org.apache.harmony.test.func.api.java.nio.F_MappedByteBufferTest_01;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.GregorianCalendar;
import java.util.Random;

import org.apache.harmony.share.Test;

public class F_MappedByteBufferTest_01 extends Test {
    private File tempFile;

    private String testDir = "./";

    private String testFileName = "tempFile.txt";
    
    private int[] firstInts = new int[2];

    public void before() throws Exception {
        tempFile = new File(testDir, testFileName);
        tempFile.deleteOnExit();
        String testString = "\n `Just a c0uple of w0rd$ and a few 5pecial $"
                + ")( -= charact3rs. ~";
        FileOutputStream fos = new FileOutputStream(tempFile);

        Random r = new Random(new GregorianCalendar().getTimeInMillis());

        for (int i = 0; i < 4096; i++) {
            int randNum = r.nextInt(testString.length());
            if(i < 2) {
                firstInts[i] = testString.charAt(randNum);
            }
            fos.write(testString.charAt(randNum));
        }
        fos.close();
    }

    public void after() throws Exception {
        // try {
        // Thread.sleep(25000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // boolean deleted = tempFile.delete();
        // System.out.println("deleted" + deleted);
    }

    public int test() {

        FileInputStream fis = null;
        MappedByteBuffer mappedBuf = null;
        int fileLength = 0;
        try {
            fis = new FileInputStream(tempFile);
        } catch (FileNotFoundException e) {
            return fail("\nCan't open file: " + testFileName);
        }

        FileChannel channel = fis.getChannel();

        try {
            fileLength = (int) channel.size();
        } catch (IOException e) {
            return fail("\nCan't get channel size on file: " + testFileName);
        }

        try {

            mappedBuf = channel.map(FileChannel.MapMode.READ_ONLY, 0,
                    fileLength);
        } catch (IOException e) {
            return fail("\nCan't map file channel created from file: "
                    + testFileName);
        }

        if (mappedBuf.isLoaded()) {
            return fail("\nMapped buffer is already loaded");
        }
        MappedByteBuffer loadedBuf = mappedBuf.load();

        int loadedBytes1 = mappedBuf.get();
        int loadedBytes2 = loadedBuf.get();

        if (loadedBytes1 != firstInts[0] || loadedBytes2 != firstInts[1]) {
            return fail("\nLoaded unexpected bytes\n"
                    + "loadedBytes1 == " + loadedBytes1 + "\nloadedBytes2 == "
                    + loadedBytes2);
        }

        /*
         * We need this in order to remove all handles from buffer thus removing
         * handle from temporary file, so we can delete it later
         */
        mappedBuf = null;
        loadedBuf = null;
        System.gc();        
       
        try {
            channel.close();
            fis.close();
        } catch (IOException e) {
            return fail("\nCan't close file \"" + testFileName
                    + "\" for reading");
        }
        return pass();
    }

    public static void main(String[] args) {
        System.exit(new F_MappedByteBufferTest_01().test(args));
    }
}