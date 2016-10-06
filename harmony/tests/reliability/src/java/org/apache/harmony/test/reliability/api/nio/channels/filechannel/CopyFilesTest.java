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
 * @author Oleg V. Oleinik
 * @version $Revision: 1.1 $
 */

package org.apache.harmony.test.reliability.api.nio.channels.filechannel;

import org.apache.harmony.test.reliability.share.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;


/**
 * Goal: check FileChannel transferFrom/To operations work correctly.
 *
 * Idea: Create a file with random content, then, many threads copy the file
 *       via transferFrom/To, each into its own file.
 *
 * The test does:
 *
 *     1. Parses parameters:
 *             param[0] - a path to an exiting directory where the test can store its files.
 *
 *     2. Creates a file with the name param[0]/fileName, fills it in with FILE_SIZE random bytes.
 *    
 *     3. Starts N_OF_THREADS threads, each thread:
 *
 *          a. Transfers from original file's channel FILE_SIZE/2 bytes into copy file.
 *
 *          b. Transfers to the copy file's channel the rest of FILE_SIZE/2 bytes from the
 *             original file's channel.
 *        
 *     4. Checks the size of each copy file is equal to the original file.
 *
 *     7. Closes channels and deletes all created files.
 * 
 */


public class CopyFilesTest extends Test {

    // Size of the original file and hence of each file's copy

    static final int FILE_SIZE = 100000;

    // Number of copying threads, i.e. copy files

    static final int N_OF_THREADS = 10;


    volatile boolean passed = true;

    String workFileName = "";
  
    String fileName = "CopyFilesTest.file";

    String outputDir = "";


    public static void main(String[] args) {
        System.exit(new CopyFilesTest().test(args));
    }


    public int test(String[] params) {

        File f_from = null;

        passed = true;

        try {

            parseParams(params);

            // First, create work file
            f_from = Utils.createWorkFile(workFileName);


            // Second, fill it in with random byte values
            fillInFile(f_from);


            // Third, run threads each copying the file into another file
            copy(f_from);


            // Finally, remove the created work file and return appropriate status
            if (!passed) {
                return fail("Failed");
            }


        } catch (Throwable t) {

            t.printStackTrace();

            return fail("Exception thrown: " + t);

        } finally {

            if (f_from != null) {
                f_from.delete();
            }
        }

        return pass("OK");
    }



    // The method just fills-in the file 'f' with random byte values

    void fillInFile(File f) throws Exception {

        FileChannel fc = null;

        try {

            fc = new FileOutputStream(f).getChannel();

            byte[] b = Utils.createRndBytes(FILE_SIZE);

            ByteBuffer bb = ByteBuffer.allocateDirect(b.length).wrap(b);

            fc.write(bb);

            fc.force(false);

        } finally {

            if (fc != null) { 
                fc.close();
            }
        }

        return;
    }


    // The key method - runs N_OF_THREADS threads, passing to each a channel
    // from and to which copy the content, waits for threads' finishing and checks
    // files' sizes.
 
    void copy(File f_from) throws Exception {
            
        Thread[] t = new Thread[N_OF_THREADS];

        File[] f_to = new File[N_OF_THREADS];

        FileChannel[] outChannels = new FileChannel[N_OF_THREADS];

        FileChannel inChannel = null;

        try {

            // First, open a read-only channel of original file

            inChannel = new FileInputStream(f_from).getChannel();

            for (int i = 0; i < t.length; ++i) {

                // Second, create files to copy the original file to

                f_to[i] = new File(f_from.getAbsolutePath() + "_copy_" + i);

                // Third, create write-only channels to copy bytes to

                outChannels[i] = new FileOutputStream(f_to[i]).getChannel();

                // Forth, create and run threads, each copying from inChannel 
                // into its outChannel[i]

                t[i] = new Copier("" + i, inChannel, outChannels[i], this);

                t[i].start();
            }
 
            // Fifth, wait threads for finishing

            for (int i = 0; i < t.length; ++i) {
                t[i].join();
            }

            // Finally, is there were no errors, check that content of the original
            // file is equal to the copies
                               
            if (passed) {
 
                for (int i = 0; i < f_to.length; ++i) {

                    if (!checkContent(f_from, f_to[i])) {

                        passed = false;

                    }
                }
            }

        } catch (Throwable tr) {

            tr.printStackTrace();

            log.add("Exception thrown: " + tr);

            passed = false;

        } finally {

            // Actually, this is final action - we close all channels 
            // and delete all created files-copies:

            if (inChannel != null){
                inChannel.close();
            }

            for (int i = 0; i < outChannels.length; ++i) {

                if (outChannels[i] != null) {
                    outChannels[i].close();
                }

                if (f_to[i] != null) {
                    f_to[i].delete();
                }
            }
        }

        return;
    }


    // Just checks that sizes of two files are the same

    boolean checkContent(File f1, File f2) {
            
        if (f1.length() != f2.length()) {

            log.add("Size of " + f1.getName() + " is " + f1.length() + ", while size of " + 
                f2.getName() + " is " + f2.length());

            return false;
        }

        return true;
    }


    public void parseParams(String[] params) throws Exception {

        // NOTE: outputDir must exist prior to running this test

        if (params.length >= 1) {
            outputDir =  params[0];
        } else {
            throw new Exception("output directory is not specified. Usage: Test <existing output dir name>");
        }

        workFileName = outputDir + File.separator + fileName;
    }

}




class Copier extends Thread {

    FileChannel from = null, to = null;

    CopyFilesTest base = null;


    Copier (String id, FileChannel from, FileChannel to, CopyFilesTest base) {
        super(id);
        this.from = from;
        this.to = to;
        this.base = base; // needed to log failures
    }


    public void run() {

        long size = 0l, transfered = 0l;

        try {

            // First, a lock upon input channel must be aquired - experimants show
            // operation transferFrom/To is not thread safe

            synchronized (from) {

                Thread.yield();

                // transferFrom/To is not completely thread-safe, we set-up position to 0 explicitely

                from.position(0); 

                // we will transfer by 2 equal portions: 0..size/2 and size/2..size

                size = from.size() / 2; 

                Thread.yield();

                // TESTED METHOD CALL - transferFrom

                transfered = to.transferFrom(from, 0, size);

                if (transfered != size) {

                    base.log.add("Thread " + this.getName() + ": transferFrom transfered " + 
                        transfered + " bytes, instead of expected " + size + " bytes");

                    base.passed = false;
   
                }

                to.force(false);

                // transferFrom/To is not completely thread-safe, we set-up position to the middle 
                // of file explicitely

                to.position(size);

                // TESTED METHOD CALL - transferTo

                transfered = from.transferTo(size, size, to);

                if (transfered != size) {

                    base.log.add("Thread " + this.getName() + ": transferTo transfered " + 
                        transfered + " bytes, instead of expected " + size + " bytes");

                    base.passed = false;
   
                }

                to.force(false);
            }


        } catch (Exception e) {

            e.printStackTrace();

            base.log.add("Thread " + this.getName() + ": " + e + " while transferFrom/To() operation");

            base.passed = false;
        }
    }

}
