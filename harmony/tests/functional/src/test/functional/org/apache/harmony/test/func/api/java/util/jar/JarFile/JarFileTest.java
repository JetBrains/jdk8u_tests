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
/*
 * Created on 21.02.2005
 *  
 */
package org.apache.harmony.test.func.api.java.util.jar.JarFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Random;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.harmony.test.func.api.java.util.jar.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.util.jar.share.PrepareTestCleanup;
import org.apache.harmony.test.func.api.java.util.jar.share.PrepareTestCleanupRunner;
import org.apache.harmony.share.Result;

public class JarFileTest extends IOMultiCase implements PrepareTestCleanup {
    static File testDir;

    private static final String fileName = "JarFileTest";

    private static final String testedStringEntry = "Test_Entry0001";

    protected static final int testedEntrySize = 10;

    protected static final int checkNumber = 7;

    public int prepare(File dir) throws IOException {
        JarEntry testedEntry = new JarEntry(testedStringEntry);
        testedEntry.setSize(testedEntrySize * 3);

        /* Create tested files */
        File testedFile = new File(dir, fileName);

        String manifestContents = "Manifest-Version: 1.0\n"
                + "Created-By: Example\n" + "Class-Path: .;/root/\r"
                + "Main-Class: test/test/main.class\n" + "\n"
                + "Name: test/test/test.class\n" + "Java-Bean: TRUE\n" + "\n"
                + "Name: Example/corp/\n" + "Java-Bean: TRUE\n" + "\n"
                + "Name: test/test/main.class\n" + "Java-Bean: FALSE";

        Manifest testedManifest = new Manifest(new ByteArrayInputStream(
                manifestContents.getBytes()));

        /* Create and open stream */
        JarOutputStream jarOStr1 = new JarOutputStream(new FileOutputStream(
                testedFile), new Manifest());
        jarOStr1.putNextEntry(testedEntry);
        byte b[] = new byte[testedEntrySize];
        b[0] = checkNumber;
        jarOStr1.write(b);
        /* Close stream */
        jarOStr1.close();

        File testedFileWithManifest = new File(dir, fileName + "WithManifest");

        /* Create and open stream */
        JarOutputStream jarOStr2 = new JarOutputStream(new FileOutputStream(
                testedFileWithManifest), testedManifest);
        jarOStr2.putNextEntry((JarEntry) testedEntry.clone());
        jarOStr2.write(b);
        /* Close stream */
        jarOStr2.close();

        JarFile testedJarFile = new JarFile(testedFile);
        testedJarFile.close();

        File gold = new File(dir, "gold.txt");
        JarOutputStream jaos = new JarOutputStream(new FileOutputStream(gold),
                new Manifest());
        jaos.putNextEntry(new JarEntry("abcd"));
        jaos.write(new byte[] { 'a' });
        jaos.close();

        gold = new File(dir, "null_manifest.txt");
        jaos = new JarOutputStream(new FileOutputStream(gold));
        jaos.putNextEntry(new JarEntry("abcde"));
        jaos.close();

        gold = new File(dir, "jar_zip.txt");
        jaos = new JarOutputStream(new FileOutputStream(gold));
        jaos.putNextEntry(new JarEntry(new ZipEntry("abcdef")));
        jaos.putNextEntry(new ZipEntry("xyz"));
        jaos.close();

        gold = new File(dir, "manifest.txt");

        String s = "Manifest-Version: 1.0\n" + "Created-By: test\n"
                + "Class-Path: .;/root/\r"
                + "Main-Class: test/test/main.class\n" + "\n"
                + "Name: test/test/test.class\n" + "Java-Bean: TRUE\n" + "\n"
                + "Name: test/test/\n" + "\n" + "Name: test/test/main.class\n"
                + "Java-Bean: FALSE";

        Manifest mf = new Manifest(new ByteArrayInputStream(s.getBytes()));
        jaos = new JarOutputStream(new FileOutputStream(gold), mf);
        jaos.close();

        return pass();
    }

    public int cleanup(File dir) throws IOException {
        new File(dir, fileName).delete();
        new File(dir, fileName + "WithManifest").delete();
        new File(dir, "gold.txt").delete();
        new File(dir, "jar_zip.txt").delete();
        new File(dir, "null_manifest.txt").delete();
        new File(dir, "manifest.txt").delete();

        return pass();
    }

    public void setTestDir(File dir) {
        testDir = dir;
    }

    public static void main(String[] args) throws IOException {
        System.exit(PrepareTestCleanupRunner.run(args, new JarFileTest()));
    }

    public Result testJarFile_getEntry() throws IOException {
        JarFile testedJarFile = null;

        try {
            testedJarFile = new JarFile(new File(testDir, fileName));
            ZipEntry ze = testedJarFile.getEntry(testedStringEntry);

            if (ze == null) {
                return failed("\nCould not find existing entry" + ze.getName());
            }
            /* Check entry name it must be equal to testedStringEntry */
            if (!ze.getName().equals(testedStringEntry)) {
                return failed("\nExpected name of jarentry is: "
                        + testedStringEntry + "\nThe recieved name is: "
                        + ze.getName());
            }
            /* Check entry size it must be equal to testedEntrySize */
            if (ze.getSize() != testedEntrySize) {
                return failed("\nExpected size of jarentry is: "
                        + testedEntrySize + "\nThe recieved size is: "
                        + ze.getSize());
            }
            /* Extend current entry size by a factor of 3 */
            ze.setSize(testedEntrySize * 3);
            if (ze.getSize() != testedEntrySize * 3) {
                return failed("\nExpected size of jarentry is: "
                        + testedEntrySize * 3 + "\nThe recieved size is: "
                        + ze.getSize());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed("\nUnhandled exception in testJarFile_getEntry()");
        } finally {
            testedJarFile.close();
        }
        return passed();
    }

    public Result testJarFile_getJarEntry() throws IOException {
        JarFile testedJarFile = null;
        try {
            testedJarFile = new JarFile(new File(testDir, fileName));
            ZipEntry ze = testedJarFile.getJarEntry(testedStringEntry);

            if (ze == null) {
                return failed("\nCould not find existing entry" + ze.getName());
            }
            /* Check entry name it must be equal to testedStringEntry */
            if (!ze.getName().equals(testedStringEntry)) {
                return failed("\nExpected name of jarentry is: "
                        + testedStringEntry + "\nThe recieved name is: "
                        + ze.getName());
            }
            /* Check entry size it must be equal to testedEntrySize */
            if (ze.getSize() != testedEntrySize) {
                return failed("\nExpected size of jarentry is: "
                        + testedEntrySize + "\nThe recieved size is: "
                        + ze.getSize());
            }
            /* Extend current entry size by a factor of 3 */
            ze.setSize(testedEntrySize * 3);
            if (ze.getSize() != testedEntrySize * 3) {
                return failed("\nExpected size of jarentry is: "
                        + testedEntrySize * 3 + "\nThe recieved size is: "
                        + ze.getSize());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed("\nUnhandled exception in testJarFile_getJarEntry()");
        } finally {
            testedJarFile.close();
        }
        return passed();
    }

    public Result testJarFile_entries() throws IOException {
        File f = new File(testDir, "MultiEntries");
        JarOutputStream jarOStr;

        try {
            /* Create file with custom manifest */
            String manifestContents = "Manifest-Version: 1.0\n"
                    + "Created-By: Example Corp\n" + "Class-Path: .;/root/\r"
                    + "Main-Class: test/test/main.class\n" + "\n"
                    + "Name: test/test/test.class\n" + "Java-Bean: TRUE\n"
                    + "\n" + "Name: Example/corp/\n" + "Java-Bean: TRUE\n" + "\n"
                    + "Name: test/test/main.class\n" + "Java-Bean: FALSE";

            Manifest testedManifest = new Manifest(new ByteArrayInputStream(
                    manifestContents.getBytes()));

            jarOStr = new JarOutputStream(new FileOutputStream(f),
                    testedManifest);

        } catch (IOException e) {
            e.printStackTrace();
            return failed("\nCan't create JarOutputStream");
        }

        for (int i = 0; i < testedEntrySize; ++i) {
            String s = new String(testedStringEntry
                    + new Double(new Random(i).nextDouble()).toString());
            try {
                jarOStr.putNextEntry(new JarEntry(s));
            } catch (ZipException e) {
                e.printStackTrace();
                return failed("\nException in testJarFile_entries()");
            }
        }
        jarOStr.close();

        /* Create JarFile from file */
        JarFile jf = new JarFile(f);

        try {
            Enumeration e = jf.entries();
            int i = 0;
            while (e.hasMoreElements()) {
                Object o = e.nextElement();
                ++i;
            }
            /*
             * We compare testedEntrySize + 1 because entry with index 0 is
             * reserved for manifest
             */
            if (i != testedEntrySize + 1) {
                return failed("\nThe number of entries in JarFile must be: "
                        + testedEntrySize
                        + "\nThe number of elements retrieved from "
                        + "enumeration is: " + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed("\nUnhandled exception in testJarFile_entries()");
        } finally {
            jf.close();
            f.delete();
        }
        return passed();
    }

    public Result testJarFile_getInputStream_ZipEntry() throws IOException {
        /* Create JarFile from file */
        JarFile jf = new JarFile(new File(testDir, fileName));

        try {
            JarEntry testedEntry = new JarEntry(testedStringEntry);
            testedEntry.setSize(testedEntrySize * 3);

            InputStream stdIStr = jf.getInputStream(testedEntry);
            if (stdIStr == null) {
                return failed("\nJarEntry not found");
            }
            byte b[] = new byte[testedEntrySize * 3];
            int bytesRead = stdIStr.read(b);
            if (b[0] != checkNumber) {
                return failed("\nWrong data was retrieved from entry"
                        + "Expected data in b[0] is: " + checkNumber + "\n"
                        + "The retrived data is: " + b[0]);
            }

            if (bytesRead != testedEntrySize) {
                return failed("\nWrong number of bytes is read from entry"
                        + "Expected number of bytes is: " + testedEntrySize
                        + "\n" + "The actual number of bytes read is: "
                        + bytesRead);
            }
            /* Read non-existent byte */
            bytesRead = stdIStr.read();
            if (bytesRead != -1) {
                return failed("\n-1 is expected as a return type as the end"
                        + "of the stream has been reached.\n"
                        + "\nInsteasd the return result is: " + bytesRead);
            }
            stdIStr.close();
            stdIStr = jf.getInputStream(testedEntry);

            ZipEntry ze = jf.getEntry(testedStringEntry);
            ze.setSize(testedEntrySize * 3);
            bytesRead = stdIStr.read(b);
            if (bytesRead != testedEntrySize) {
                return failed("\nWrong number of bytes read"
                        + "\nExpected to number of bytes read is:"
                        + testedEntrySize
                        + "\nThe retrieved number of bytes read is:"
                        + bytesRead);
            }
            stdIStr.close();
        } catch (Exception e) {
            e.printStackTrace();
            return failed("\nUnhandled exception in"
                    + " testJarFile_getInputStream_ZipEntry()");
        } finally {
            jf.close();
        }
        return passed();
    }

    public Result testJarFile_getManifest() throws IOException {
        /* Create JarFile from file */
        JarFile jf = new JarFile(new File(testDir, fileName));

        try {

            Manifest mf = jf.getManifest();
            if (mf == null) {
                return failed("\nManifest not found");
            }

            if (mf.getMainAttributes().entrySet().size() != 0) {
                return failed("Current file is created without manifest");
            }
            jf.close();
            jf = new JarFile(new File(testDir, fileName + "WithManifest"));

            mf = jf.getManifest();
            Attributes attribs = (Attributes) mf.getAttributes("Example/corp/");
            if (attribs.size() != 1) {
                return failed("\nEntry \"Example/corp/\" contains one attribute "
                        + "whereas the number of found attributes is: "
                        + attribs.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return failed("\nUnhandled exception in"
                    + " testJarFile_getInputStream_ZipEntry()");
        } finally {
            jf.close();

        }
        return passed();
    }

    public Result testBasic() throws IOException {

        JarFile jf = new JarFile(new File(testDir, "gold.txt"));
        try {
            JarEntry je = jf.getJarEntry("abcd");

            if (!"abcd".equals(je.getName())) {
                return failed("wrong jar entry name - expected 'abcd', got "
                        + je.getName());
            }

            if (je.getSize() != 1) {
                return failed("expected jarentry to contain one byte, got "
                        + je.getSize());
            }
        } finally {

            jf.close();
        }

        return passed();
    }

    public Result testJarEntryZipEntry() throws IOException {
        JarFile jf = new JarFile(new File(testDir, "jar_zip.txt"));
        try {
            JarEntry je = jf.getJarEntry("abcde");

            if (je != null) {
                return failed("expected jar entry to be null");
            }

            ZipEntry ze = jf.getEntry("abcde");
            if (ze != null) {
                return failed("wrong zip entry name - expected null, got "
                        + ze.getName());
            }

            ze = jf.getEntry("xyz");
            if (!"xyz".equals(ze.getName())) {
                return failed("wrong zip entry name - expected 'xyz', got "
                        + ze.getName());
            }

        } finally {
            jf.close();
        }
        return passed();
    }

    public Result testEntries() throws IOException {
        JarFile jf = null;
        Enumeration e;
        JarEntry je;
        try {
            //first file
            jf = new JarFile(new File(testDir, "gold.txt"));
            e = jf.entries();
            je = (JarEntry) e.nextElement();
            if (!"META-INF/MANIFEST.MF".equals(je.getName())) {
                return failed("expected jar entry name to be 'META-INF/MANIFEST.MF', got "
                        + je.getName());
            }
            je = (JarEntry) e.nextElement();
            if (!"abcd".equals(je.getName())) {
                return failed("expected jar entry name to be 'abcd', got "
                        + je.getName());
            }
            if (e.hasMoreElements()) {
                return failed("expected only two entries in gold.txt");
            }

            jf.close();

            //second file
            jf = new JarFile(new File(testDir, "jar_zip.txt"));
            e = jf.entries();
            je = (JarEntry) e.nextElement();
            if (!"abcdef".equals(je.getName())) {
                return failed("expected jar entry name to be 'abcdef', got "
                        + je.getName());
            }

            ZipEntry ze = (ZipEntry) e.nextElement();

            if (!"xyz".equals(ze.getName())) {
                return failed("expected zip entry name to be 'xyz', got "
                        + ze.getName());
            }

            if (e.hasMoreElements()) {
                return failed("expected only one entry in jar_zip.txt but got "
                        + e.nextElement());
            }

            jf.close();

            //third file
            jf = new JarFile(new File(testDir, "null_manifest.txt"));
            e = jf.entries();
            je = (JarEntry) e.nextElement();
            if (!"abcde".equals(je.getName())) {
                return failed("expected jar entry name to be 'abcde', got "
                        + je.getName());
            }
            if (e.hasMoreElements()) {
                return failed("expected only one entry in null_manifest.txt");
            }

            jf.close();

        } finally {
            jf.close();
        }
        return passed();
    }

    public Result testNullManifest() throws IOException {
        JarFile jf = new JarFile(new File(testDir, "null_manifest.txt"));
        try {
            if (jf.getManifest() != null) {
                return failed("expected jar manifest to ne null");
            }
        } finally {
            jf.close();
        }

        return passed();
    }

    public Result testManifest() throws IOException {
        JarFile jf = new JarFile(new File(testDir, "manifest.txt"));
        try {
            Manifest mf = jf.getManifest();

            Map entries = mf.getEntries();
            if (entries.size() == 3 && entries.containsKey("test/test/")
                    && entries.containsKey("test/test/test.class")
                    && entries.containsKey("test/test/main.class")) {
                return passed();
            }
        } finally {
            jf.close();
        }

        return failed("manifest is wrong");
    }

    public Result testConstructorFileVerifyMode() throws IOException {
        new JarFile(new File(testDir, "gold.txt"), true, JarFile.OPEN_READ)
                .close();
        try {
            new JarFile(new File(testDir, "gold.txt"), true,
                    JarFile.OPEN_DELETE).close();
            return failed("expected IllegalArgumentException with illegal mode");
        } catch (IllegalArgumentException e) {
        }
        
        return passed();
    }
}