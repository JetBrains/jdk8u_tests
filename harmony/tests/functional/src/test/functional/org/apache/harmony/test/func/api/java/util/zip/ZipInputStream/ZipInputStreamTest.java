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

package org.apache.harmony.test.func.api.java.util.zip.ZipInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.harmony.test.func.api.java.util.jar.share.IOMultiCase;
import org.apache.harmony.share.Result;

public class ZipInputStreamTest extends IOMultiCase {
    public static void main(String[] args) throws IOException {
        System.exit(new ZipInputStreamTest().test(args));
    }

    public Result testRead() throws IOException {
        byte[] output = new byte[2000];

        String inputString = "The Functional test suite is a "
                + "collection of "
                + "micro scenarios for "
                + "testing various functional parts of "
                + "an implementation.";

        Deflater compresser = new Deflater();
        compresser.setInput(inputString.getBytes("UTF-8"));
        compresser.finish();
        compresser.deflate(output);

        ZipInputStream zis = new ZipInputStream(
                new ByteArrayInputStream(output));
        byte[] barr = new byte[20000];
        int read = zis.read(barr, 3, barr.length - 3);
        if (read != -1) {
            return failed("case 1: expected read() to return -1");
        }

        zis.getNextEntry();

        if (read != -1) {
            return failed("case 2: expected read() to return -1");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        zos.putNextEntry(new ZipEntry("entry 1"));
        zos.write(inputString.getBytes("UTF-8"));
        zos.closeEntry();
        zos.close();

        zis = new ZipInputStream(new ByteArrayInputStream(baos.toByteArray()));

        read = zis.read(barr, 3, barr.length - 5);

        if (read != -1) {
            return failed("case 3: expected read() to return -1");
        }

        zis.getNextEntry();
        read = zis.read(barr, 3, barr.length - 5);

        if (read != inputString.getBytes("UTF-8").length) {
            return failed("case 4: expected read() to return inputString.getBytes('UTF-8').length");
        }

        if (!inputString.equals(new String(barr, 3, read))) {
            return failed("expected strings to be equal");
        }

        return passed();
    }

    public Result testCloseEntry() throws IOException {
        byte[] output = new byte[2000];

        String inputString = "The Functional test suite is a "
                + "collection of "
                + "micro scenarios for "
                + "testing various functional parts of "
                + "an implementation.";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        zos.putNextEntry(new ZipEntry("entry 1"));
        zos.write(inputString.toUpperCase().getBytes("UTF-8"));
        zos.closeEntry();

        zos.putNextEntry(new ZipEntry("entry 2"));
        zos.write(inputString.toLowerCase().getBytes("UTF-8"));
        zos.closeEntry();

        zos.close();

        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(baos
                .toByteArray()));
        zis.closeEntry();

        byte[] barr = new byte[500];

        int read = zis.read(barr);
        if (read != -1) {
            return failed("expected read to be -1, got  " + read);
        }
        zis.getNextEntry();
        read = zis.read(barr, 1, 2);
        if (read != 2) {
            return failed("expected read to be 2, got  " + read);
        }
        zis.closeEntry();
        read = zis.read(barr);

        if (read != -1) {
            return failed("expected read to be -1, got  " + read);
        }
        return passed();
    }

}