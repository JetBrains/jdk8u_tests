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
package org.apache.harmony.test.func.api.java.util.jar.Manifest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.harmony.test.func.api.java.util.jar.share.IOMultiCase;
import org.apache.harmony.share.Result;

public class ManifestTest extends IOMultiCase {
    protected static String   manifestContents;

    protected static Manifest testedManifest;

    protected static Manifest testedManifestEmpty;

    public static void main(String[] args) throws IOException {
        manifestContents = "Manifest-Version: 1.0\n"
                + "Created-By: Example Corp\n" + "Class-Path: .;/root/\r"
                + "Main-Class: test/test/main.class\n" + "\n"
                + "Name: test/test/test.class\n" + "Java-Bean: TRUE\n" + "\n"
                + "Name: Example/corp/\n" + "Java-Bean: TRUE\n" + "\n"
                + "Name: test/test/main.class\n" + "Java-Bean: FALSE";

        testedManifest = new Manifest(new ByteArrayInputStream(manifestContents
                .getBytes()));
        testedManifestEmpty = new Manifest();

        System.exit(new ManifestTest().test(args));
    }

    public Result testManifest_clear_clone_equals() throws IOException {
        try {
            Manifest testedMfCarbonCopy = new Manifest(testedManifest);
            Manifest testedMfClonedCopy = (Manifest) testedManifest.clone();

            if (!testedMfCarbonCopy.equals(testedMfClonedCopy)) {
                return failed("\nThe cloned object does not equal to its "
                        + "carbon-copy");
            }

            if (!testedMfClonedCopy.equals(testedManifest)) {
                return failed("\nThe cloned object does not equal to its "
                        + "original");
            }

            if (!new Manifest().equals(testedManifestEmpty)) {
                return failed("\nOne empty object does not equal to "
                        + "another empty object");
            }

            testedMfClonedCopy.clear();

            if (testedMfCarbonCopy.equals(testedMfClonedCopy)) {
                return failed("\nThe cloned object has been cleared and still"
                        + "equals to its carbon-copy");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return failed("\nUnhandled exception in testManifest_clear_clone_equals()");
        }
        return passed();
    }

    public Result testManifest_getAttributes_getMainAttributes()
            throws IOException {
        try {
            try {

                Manifest mf = (Manifest) testedManifestEmpty.clone();
                if (mf == null) {
                    return failed("\nCould not be cloned");
                }

                if (mf.getMainAttributes().entrySet().size() != 0) {
                    return failed("Current file is created without manifest");
                }

                mf = new Manifest(testedManifest);
                if (mf == null) {
                    return failed("\nCould not be created");
                }

                Attributes mainAttribs = mf.getMainAttributes();

                if (mainAttribs.getValue("Created-By").length() == 0) {
                    return failed("\nMain attribute is not found");
                }
                if (mainAttribs.containsKey("Example/corp/")) {
                    return failed("\nThis is not main attribute.\n"
                            + "The main attributes of a JAR file are values"
                            + "that are associated with the JAR file itself,\n"
                            + "not with any particular entry");
                }

                Attributes attribs = (Attributes) mf
                        .getAttributes("Example/corp/");
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

            }

        } catch (Exception e) {
            e.printStackTrace();
            return failed("\nUnhandled exception in testManifest_clear_clone_equals()");
        }
        return passed();
    }

    public Result testDefaultConstructor() {
        Manifest mf = new Manifest();

        if (!mf.equals(new Manifest())) {
            return failed("expected two new manifests to be equal");
        }

        if (!mf.getEntries().isEmpty()) {
            return failed("expected new manifest entries to be empty");
        }

        if (!mf.getMainAttributes().isEmpty()) {
            return failed("expected new manifest attributes to be empty");
        }

        return passed();
    }

    public Result testConstructorBadInputStream() throws IOException {
        String s = "Manifest-Version: 1.0\rCreated-By: test\r\n"
                + "Class-Path: .;/root/\r"
                + "Main-Class: test/test/main.class\n\n"
                + "Name: test/test/test.class\nJava-Bean: TRUE\r" + "\r"
                + "Name: test/test/\n\rName: test/test/main.class\n"
                + "Java-Bean: FALSE";
        try {
            Manifest mf = new Manifest(new ByteArrayInputStream(s.getBytes()));
        } catch (IOException e) {
            return passed();
        }

        return failed("IOException is expected");
    }

    public Result testConstructorInputStream() throws IOException {

        String s = "Manifest-Version: 1.0\nCreated-By: test\n"
                + "Class-Path: .;/root/\n"
                + "Main-Class: test/test/main.class\n\n"
                + "Name: test/test/test.class\nJava-Bean: TRUE\n\n"
                + "Name: test/test/\n\nName: test/test/main.class\n"
                + "Java-Bean: FALSE";
        Manifest mf = new Manifest(new ByteArrayInputStream(s.getBytes()));
        Map entries = mf.getEntries();
        if (entries.size() != 3 || !entries.containsKey("test/test/")
                || !entries.containsKey("test/test/test.class")
                || !entries.containsKey("test/test/main.class")) {
            return failed("mf.getEntries() returns unexpected value");
        }
        Attributes attrs = mf.getMainAttributes();

        if (attrs.size() != 4) {
            return failed("expected main attributes size to be 4 , got "
                    + attrs.size());
        }
        if (!"1.0".equals(attrs.getValue("Manifest-Version"))
                || !"test".equals(attrs.getValue("Created-By"))
                || !".;/root/".equals(attrs.getValue("Class-Path"))
                || !"test/test/main.class".equals(attrs.getValue("Main-Class"))) {
            return failed("invalid attribute value");
        }

        if (attrs.get("Manifest-Version") != null) {
            return failed("expected get(<attribute name>) to return null");
        }

        return passed();
    }

    public Result testRead_InputStream() throws IOException {
        String s1 = "Manifest-Version: 1.0\nCreated-By: test\n"
                + "Class-Path: .;/root/\n"
                + "Main-Class: test/test/main.class\n\n"
                + "Name: test/test/\n\nName: test/test/main.class\n"
                + "Java-Bean: FALSE";
        String s2 = "Manifest-Version: 2.0\nCreated-By: test2\n"
                + "Class-Path: .;/root2/\n"
                + "Main-Class: test/test/main.class\n\n"
                + "Name: Example/corp/\n" + "Java-Bean: TRUE\n\n"
                + "Java-Bean: FALSE";

        /* Create new manifest */
        Manifest mf = new Manifest(new ByteArrayInputStream(s1.getBytes()));
        /* Compose additional entry */
        InputStream iStream = new ByteArrayInputStream(s2.getBytes());
        
        if (mf.getEntries().size() != 2) {
            return failed("expected number of attributes is 2, got "
                    + mf.getEntries().size());
        }
        
        /* Read additional data */
        mf.read(iStream);
        
        if (mf.getEntries().size() != 3) {
            return failed("expected number of attributes is 3, got "
                    + mf.getEntries().size());
        }
        
        Attributes attrs = mf.getMainAttributes();

        if (attrs.size() != 4) {
            return failed("expected main attributes size to be 4 , got "
                    + attrs.size());
        }
        
        if (!"2.0".equals(attrs.getValue("Manifest-Version"))
                || !"test2".equals(attrs.getValue("Created-By"))
                || !".;/root2/".equals(attrs.getValue("Class-Path"))
                || !"test/test/main.class".equals(attrs.getValue("Main-Class"))) {
            return failed("invalid attribute value");
        }
        
        Map entries = mf.getEntries();
        
        if(entries.containsKey("Example/corp/") == false){
            return failed("Manifest does not contain attribute");
        }
        
        if (attrs.get("Manifest-Version") != null) {
            return failed("expected get(<attribute name>) to return null");
        }

        return passed();
    }

}