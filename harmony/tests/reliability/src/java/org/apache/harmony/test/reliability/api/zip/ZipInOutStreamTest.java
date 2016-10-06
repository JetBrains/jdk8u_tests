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

import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.Deflater;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.Random;


/**
 * Goal: find memory leaks caused by using hyarchive library method.
 *
 * The test does:
 *    1. Reads parameters, which are:
 *          param[0] - number of nesting zip entry 
 *          param[1] - path to Zip file's directory  
 *    2. Creates a zip file with different zip strategy and nesting of zip entry.
 *    3. Writes the random bytes into the entries.
 *    4. Check writing bytes 
 */

public class ZipInOutStreamTest extends Test {

    static Random r = new Random(10);

    String fileName = "";

    String outputFileName = "ZipInOutStreamTest.out.zip";
     
    int NUM_ENTRIES = 10;

    int Multiplier = 1000;

    String outputDir = "";

    public int[] strategies = new int [] {
                                             Deflater.DEFAULT_STRATEGY,
                                             Deflater.HUFFMAN_ONLY,
                                             Deflater.FILTERED
                                         };
        
    public int[] level = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    int LEVEL = 0;
    int STRATEGY = Deflater.DEFAULT_STRATEGY;

    public static void main(String[] args) {
        System.exit(new ZipInOutStreamTest().test(args));
    }


    public int test(String[] params) {
    
        parseParams(params);

        try {
            createTmpDir(outputDir);
        } catch (Exception t){
            log.add(t.toString());
            return fail("Creates tmp directory: Failed");

        }

        fileName = outputDir + File.separator + outputFileName;

        FileOutputStream fos = null;
        FileInputStream fis = null;

        for (int x = 0; x < level.length; ++x) {

            for (int y = 0; y < strategies.length; ++y) {

                LEVEL = level[x];
                STRATEGY = strategies[y];

                try {

                    fos = new FileOutputStream(outputFile(fileName));
                    createZipFile(fos);

                    fis = new FileInputStream(inputFile(fileName));
                    readZipFile(fis);

                } catch (Throwable t){

                    log.add(t.toString());
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

            } // for strategies

        } // for level

        return pass("OK");
    }


    public void parseParams(String[] params) {

        if (params.length >= 1) {
            NUM_ENTRIES =  Integer.parseInt(params[0]);
        }        

        // NOTE: outputDir must exist prior to running this test

        if (params.length >= 2) {
            outputDir =  params[1];
        }        


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


    public void readZipFile(InputStream is) throws Exception {

        ZipInputStream zis = new ZipInputStream(is);

        int i = 0;
        ZipEntry z;
        byte[] bb = new byte[100];
  
        while ((z = zis.getNextEntry()) != null) {

            int read_bytes = 0;
            int r = 0;

            while ((r = zis.read(bb)) != -1){
                read_bytes += r;
            }

            if (read_bytes != getNextSize(i)) {
                throw new Exception("Unexpected: zip entry " + z.getName() + " read " + read_bytes + " instead of " + getNextSize(i));
            }

            // log.add(z.getName() + " read " + read_bytes + ", expected " + getNextSize(i));

            ++i;

            zis.closeEntry();
        }

        if (i != NUM_ENTRIES) {
            throw new Exception("Unexpected: " + i + " zip entries were read instead of " + NUM_ENTRIES);
        }

        zis.close();

    }

    public void createZipFile(OutputStream os) throws Exception {

        ZipOutputStream zos = new ZipOutputStream(os);

        zos.setComment("This is comment");

        for (int i = 0; i < NUM_ENTRIES; ++i){

            String s = "";

            for (int j = 0; j < i; ++j) {
                s += j + File.separator;
            }

            s += i + ".txt";

            zos.putNextEntry(new ZipEntry(s));

            byte[] b = GetBytes(getNextSize(i));

            zos.write(b);
        }

        zos.closeEntry();
        zos.close();

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

