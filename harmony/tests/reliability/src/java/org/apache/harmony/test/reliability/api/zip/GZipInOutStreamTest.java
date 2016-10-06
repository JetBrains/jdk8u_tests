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
 * @version $Revision: 1.2 $
 */

package org.apache.harmony.test.reliability.api.zip;

import org.apache.harmony.test.reliability.share.Test;

import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.util.Random;


/**
 * Goal: find resource leaks or intermittent failures caused by writing into 
 * single FileOutputStream by multiple GZIPOutputStreams successively (without close-ing).
 * 
 * The test does:
 *
 * 1. Reads parameters, which are:
 *      1) args[0] - number of GZIPOutputStreams to write successively into one FileOutputStream
 *      2) args[1] - output directory where to write output file. Must exist prior to test invocation.
 *
 * 2. Creates FileOutputWriter into output file whose full name is "args[1]/GZipInOutStreamTest.out.gz",
 *    before creation checks the file for existance and if such file already exists, removes it.
 *
 * 3. In a cycle for args[0] iterations:
 *    
 *    1) Creates GZIPOutputStream over a single FileOutputWriter
 *    2) Writes an array of random bytes into it
 *    3) Calls finish(), but not close.
 *
 * 4. Calls GZIPOutputStream.close().
 *
 * 5. Reads from the file via GZIPInputStream.
 *
 */

public class GZipInOutStreamTest extends Test {

    static Random r = new Random(10);

    String fileName = "";

    String outputFileName = "GZipInOutStreamTest.out.gz";
     
    int NUM_ITERATIONS = 10;

    int Multiplier = 1000;

    String outputDir = "";

    static final int BUF_SIZE = 10000;

    public static void main(String[] args) {
        System.exit(new GZipInOutStreamTest().test(args));
    }

    public int test(String[] params) {
    
        parseParams(params);

        FileOutputStream fos = null;
        FileInputStream fis = null;

        try {
            createTmpDir(outputDir);
        } catch (Exception t){
            log.add(t.toString());
            return fail("Creates tmp directory: Failed");

        }

        fileName = outputDir + File.separator + outputFileName;

        try {

            // write into GZIPOutputStream/FileOutputStream

            fos = new FileOutputStream(outputFile(fileName));
            createGZipFile(fos);

            // read from GZIPInputStream/FileInputStream

            fis = new FileInputStream(inputFile(fileName));
            readGZipFile(fis);

        } catch (Throwable t){

            log.add(t.toString());
            t.printStackTrace();
            return fail("Failed");

        } finally {

            try {
                if (fos != null){
                    fos.close();
                }
                if (fis != null){
                    fis.close();
                }
            } catch (Throwable t){
            }
        }

        return pass("OK");
    }


    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUM_ITERATIONS =  Integer.parseInt(params[0]);
        }        

        // NOTE: outputDir must exist prior to running this test

        if (params.length >= 2) {
            outputDir =  params[1];
        }        

        fileName = outputDir + File.separator + outputFileName;

    }

    public File outputFile(String name) throws Exception {
  
        File f = new File(name);

        if (f.exists()){
            f.delete();
        }

        return f;
    }


    public File inputFile(String fileName) throws Exception {
        
        File f = new File(fileName);

        if (!f.exists()) {
            throw new Exception("Unexpected: file " + f.getName() + " does not exist...");
        }

        return f;
    }


    public void readGZipFile(InputStream is) throws Exception {

        // read from the InputStream, BUF_SIZE and read bytes 
        // actualy do not mean anything

        GZIPInputStream gzis = new GZIPInputStream(is, BUF_SIZE);

        byte[] bb = new byte[BUF_SIZE + 100];
 
        while (gzis.available() != 0) {
            gzis.read(bb, 0, bb.length);
        }
                
        gzis.close();
    }


    public void createGZipFile(OutputStream os) throws Exception {

        GZIPOutputStream gzos = null;

        for (int i = 1; i < NUM_ITERATIONS; ++i){

            gzos = new GZIPOutputStream(os, getNextSize(i));

            // get random byte array of getNextSize(i) length

            byte[] b = GetBytes(getNextSize(i));

            // log.add("Iteration " + i + ", writing " + b.length + " bytes");

            gzos.write(b, 1, b.length - 1);

            // finish, but, not close - for the next GZIPOutputStream to 
            // write into the same FileOutputStream
 
            gzos.finish();
        }

        if (gzos != null) {
            gzos.close();
        }
    }

    public void createTmpDir(String outputDir) throws Exception {
        File temp = new File(outputDir);

        if(temp.exists()) {
            File[] lstf = temp.listFiles();
            for( int i = 0; i < lstf.length; i++) {
                if( !lstf[i].delete()) {
                    log.add("Delete " + lstf[i].getName() + " file: Failed.");
                }
            }
        }
        temp.mkdir();
    }


    int getNextSize(int i){
        return i * Multiplier;
    }

    byte[] GetBytes(int size){

        byte[] b = new byte[size];

        for (int i = 0; i < b.length; ++i){
            b[i] = (byte) r.nextInt(Byte.MAX_VALUE);
        }
        return b;
    }

}

