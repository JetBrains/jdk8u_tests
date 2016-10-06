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
package org.apache.harmony.test.func.api.java.security.F_MessageDigestTest_01;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;

import org.apache.harmony.test.func.share.ScenarioTest;
import org.apache.harmony.share.Result;

/**
 * 
 * Usage: java F_MessageDigestTest_01 <path><algorithmname><filename1>
 * <filename2><filename3><path>- name of algorithm for instance.
 * <algorithmname>- name of algorithm for instance. <filename1>- name of file
 * with input text. <filename2>- name of file with output bytes for task1(),
 * task2(). <filename3>- name of file with output bytes for task4().
 *  
 */

public class F_MessageDigestTest_01 extends ScenarioTest {
    private MessageDigest md;

    private String path;

    private String algorithmname;

    private String inputname;

    private String outputname;

    private String outputname4;

    private void writeDigestToFile(String filename, byte[] digest) {
        try {
            FileOutputStream fos = new FileOutputStream(path + filename);
            fos.write(digest);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] loadFileBytes(String filename) throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(path + filename);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int ch;
            while ((ch = in.read()) != -1) {
                buf.write(ch);
            }
            in.close();
            return buf.toByteArray();
        } finally {
            if (in != null)
                in.close();
        }
    }

    private void updateFileByte(String filename) throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(path + filename);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int ch;
            while ((ch = in.read()) != -1) {
                md.update((byte) ch);
            }
            in.close();
        } finally {
            if (in != null)
                in.close();
        }
    }

    private void updateWithDigestInputStream(String filename)
            throws IOException {
        DigestInputStream di = null;
        try {
            di = new DigestInputStream(new FileInputStream(path + filename), md);
            di.on(true);
            int ch;
            while (di.read() != -1) {
            }
            di.close();
        } finally {
            if (di != null)
                di.close();
        }
    }

    public boolean isEqual(String filename, byte[] digest) {

        FileInputStream in = null;
        try {
            in = new FileInputStream(path + filename);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int ch;
            while ((ch = in.read()) != -1) {
                buf.write(ch);
            }
            in.close();
            return MessageDigest.isEqual(digest, buf.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public int task1() {
        byte[] digest;
        try {
            byte[] buf = loadFileBytes(inputname);

            md.reset();
            md.update(buf);
            digest = md.digest();
            if (!isEqual(outputname, digest))
                return fail("Digests aren't equal after call MessageDigest.digest()");

            md.reset();
            digest = md.digest(buf);
            if (!isEqual(outputname, digest))
                return fail("Digests aren't equal after call MessageDigest.digest(byte[] buf)");

            int digestLength = md.getDigestLength();
            if (algorithmname.equalsIgnoreCase("MD5") && digestLength != 16) {
                return error("error in digestLength (for MD5 it must be 16)");
            }
            if (algorithmname.equalsIgnoreCase("SHA-1") && digestLength != 20) {
                return error("error in digestLength (for SHA-1 it must be 20)");
            }

            md.reset();
            md.update(buf);
            byte[] outbuf = new byte[100 + digestLength + 5];
            // initialize by '11' in order to see absense '0' bytes after
            // inserted digest
            for (int i = 0; i < outbuf.length; i++)
                outbuf[i] = 11;
            try {
                int offset = 10;
                //                for(int i=0; i < digest.length; i++)
                // log.info((byte)digest[i]+","); log.info();
                int len = md.digest(outbuf, offset, digestLength + 5);
                if (len != digestLength)
                    return fail("Digest has a wrong length");
                byte[] newdigest = new byte[digestLength];
                for (int i = 0; i < digestLength; i++)
                    newdigest[i] = outbuf[i + offset];
                if (!isEqual(outputname, newdigest))
                    return fail("Digests aren't equal after call MessageDigest.digest(byte[] buf, int offset, int length)");
                //                for(int i=0; i < outbuf.length; i++)
                // log.info((byte)outbuf[i]+","); log.info();
            } catch (DigestException e) {
                return error("Test failed: " + e.getMessage());
            }
            /**/

        } catch (IOException e) {
            return error("Error in test: " + e.getMessage());
        }

        /*
         * log.info(pass("passed")); log.info(Result.PASS);
         */
        return pass("task1 passed");
    }

    public int task2() {
        byte[] digest;
        try {
            md.reset();
            updateFileByte(inputname);
            digest = md.digest();
            if (!isEqual(outputname, digest))
                return fail("Digests aren't equal after call MessageDigest.digest()");

        } catch (IOException e) {
            e.printStackTrace();
            return error("updateFileByte has failed");
        }

        return pass("task2 passed");
    }

    public int task3() {
        boolean eof = false;
        byte[] digest;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path + inputname);
            md.reset();

            while (!eof) {
                int ch = fis.read();
                if (ch == -1)
                    break;
                md.update((byte) ch);
                byte[] inBuf = new byte[(int) (Math.random() * 10)];
                int bytesRead = fis.read(inBuf);
                if (bytesRead == -1)
                    break;

                byte[] outBuf = inBuf;
                if (bytesRead != inBuf.length) {
                    eof = true;
                    outBuf = new byte[bytesRead];
                    System.arraycopy(inBuf, 0, outBuf, 0, bytesRead);
                }

                md.update(outBuf);
            }

            digest = md.digest();
            if (!isEqual(outputname, digest))
                return fail("Digests aren't equal after call MessageDigest.digest()");

        } catch (IOException e) {
            e.printStackTrace();
            return error("Can't open file " + path + inputname);
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
                return error("Couldn't close file");
            }
        }
        return pass("task3 passed");
    }

    public int task4() {
        byte[] digest;
        try {
            int offset = 1029;
            int len = 18562;
            byte[] buf = loadFileBytes(inputname);
            md.reset();
            md.update(buf, offset, len);
            digest = md.digest();

            if (!isEqual(outputname4, digest))
                return fail("Digests aren't equal after call MessageDigest.digest()");

        } catch (IOException e) {
            e.printStackTrace();
            return error("loadFileBytes failed");
        }
        return pass("task4 passed");
    }

    public int task5() {
        final int BLOCK_LENGTH = 1024;
        FileInputStream fis = null;
        boolean eof = false;
        byte[] digest;
        try {
            fis = new FileInputStream(path + inputname);
            md.reset();

            byte[] inBuf = new byte[BLOCK_LENGTH];
            while (!eof) {
                int bytesRead = fis.read(inBuf);
                if (bytesRead == -1)
                    break;

                byte[] outBuf = inBuf;
                if (bytesRead != inBuf.length) {
                    eof = true;
                    outBuf = new byte[bytesRead];
                    System.arraycopy(inBuf, 0, outBuf, 0, bytesRead);
                }

                md.update(outBuf);
                MessageDigest mdClone = (MessageDigest) md.clone();
                mdClone.digest();
            }

            digest = md.digest();
            if (!isEqual(outputname, digest))
                return fail("Digests aren't equal after call MessageDigest.digest()");
        } catch (IOException e) {
            return error("Error in test: " + e.getMessage());
        } catch (CloneNotSupportedException e) {
            return fail("test failed - " + e.getMessage());
        }
        return pass("task5 passed");
    }

    public int task6() {
        byte[] digest;
        try {
            md.reset();
            updateWithDigestInputStream(inputname);
            digest = md.digest();
            if (!isEqual(outputname, digest))
                return fail("Digests aren't equal after call MessageDigest.digest()");

        } catch (IOException e) {
            e.printStackTrace();
            return error("updateWithDigestInputStream has failed");
        }

        return pass("task6 passed");
    }

    public int task7() {
        byte[] digest;
        try {
            int offset = 1029;
            int len = 18562;
            byte[] buf = loadFileBytes(inputname);
            md.reset();
            DigestOutputStream di = null;
            try {
                di = new DigestOutputStream(new ByteArrayOutputStream(), md);
                di.on(true);
                di.write(buf, offset, len);
                di.close();
            } finally {
                if (di != null)
                    di.close();
            }
            digest = md.digest();
            if (!isEqual(outputname4, digest))
                return fail("Digests aren't equal after call MessageDigest.digest()");

        } catch (IOException e) {
            e.printStackTrace();
            return error("loadFileBytes failed");
        } catch (Exception e) {
            e.printStackTrace();
            return error("loadFileBytes failed");
        }
        return pass("task7 passed");
    }

    public int test() {
        path = testArgs[0] + File.separator;
        //      algorithmname = testArgs[1];
        inputname = testArgs[1];
        //      outputname = testArgs[3];
        //      outputname4 = testArgs[4];
        String algs[] = new String[] { "MD2", "MD5", "SHA-1", "SHA-256",
                "SHA-384", "SHA-512" };
        for (int i = 0; i < algs.length; i++) {
            algorithmname = algs[i];
            outputname = "output" + algs[i].toString() + ".dat";
            outputname4 = "output" + algs[i].toString() + ".dat4";
            log.info("Input parameters: " + algorithmname + " ('" + inputname
                    + "', '" + outputname + "', '" + outputname4 + "')");
            try {
                md = MessageDigest.getInstance(algorithmname);
                log.info("Provider: " + md.getProvider().getName());
                if (task1() != Result.PASS || task2() != Result.PASS
                        || task3() != Result.PASS || task4() != Result.PASS
                        || task5() != Result.PASS || task6() != Result.PASS
                        || task7() != Result.PASS) {
                    return fail("test NOT passed");
                }
            } catch (NoSuchAlgorithmException e) {
                log.info("Algorithm " + algorithmname + " isn't implemented.");
            }
        }
        return pass("Test passed");

    }

    public static void main(String[] args) {

        System.exit(new F_MessageDigestTest_01().test(args));

    }

}