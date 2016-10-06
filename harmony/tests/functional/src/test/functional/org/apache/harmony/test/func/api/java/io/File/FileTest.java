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
 * Created on 23.11.2004
 *
 */
package org.apache.harmony.test.func.api.java.io.File;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.harmony.test.func.api.java.io.share.IOMultiCase;
import org.apache.harmony.test.func.api.java.io.share.MultiThreadRunner;
import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanup;
import org.apache.harmony.test.func.api.java.io.share.PrepareTestCleanupRunner;
import org.apache.harmony.test.func.api.java.io.share.Utils;
import org.apache.harmony.share.Result;

public final class FileTest extends IOMultiCase implements PrepareTestCleanup {
    static File TEMP_DIR;

    public void setTestDir(File dir) {
        TEMP_DIR = dir;
    }

    public int prepare(File dir) throws IOException {
        File f = new File(dir.getAbsolutePath() + "/readonly.txt");
        f.createNewFile();
        f.setReadOnly();

        FileWriter fw = new FileWriter(new File(dir.getAbsolutePath() + "/4chars.txt"));
        fw.write(new char[] { 0, 1, 2, 3 });
        fw.close();


        return pass();
    }

    public int cleanup(File dir) throws IOException {
        if (!new File(dir.getAbsolutePath() + "/readonly.txt").delete()) {
            return fail("can't delete " + dir.getAbsolutePath()
                    + "/readonly.txt");
        }

        if (!new File(dir.getAbsolutePath() + "/4chars.txt").delete()) {
            return fail("can't delete " + dir.getAbsolutePath()
                    + "/4chars.txt");
        }

        String[] shouldBeDeletedOnExit = new String[] { "deleteOnExit.txt",
                "deleteOnExit1", "deleteOnExit2/deleteOnExit2",
                "deleteOnExit2/deleteOnExit2.txt",
                "deleteOnExit3/deleteOnExit3", "deleteOnExit4/deleteOnExit4",
                "deleteOnExit4/deleteOnExit4.txt", "deleteOnExit4", };

        String[] shouldRemainOnExit = new String[] { "deleteOnExit2",
                "deleteOnExit3/deleteOnExit3.txt", "deleteOnExit3",
                };

        for (int i = 0; i < shouldBeDeletedOnExit.length; ++i) {
            if (new File(dir.getAbsolutePath() + "/" + shouldBeDeletedOnExit[i])
                    .exists()) {
                return fail("expected " + dir.getAbsolutePath() + "/"
                        + shouldBeDeletedOnExit[i] + " to be deleted on exit");
            }
        }

        for (int i = 0; i < shouldRemainOnExit.length; ++i) {
            File f = new File(dir.getAbsolutePath() + "/"
                    + shouldRemainOnExit[i]);
            if (!f.exists()) {
                return fail("expected " + f.getAbsolutePath()
                        + " to remain on exit");
            }

            f.delete();
        }

        return pass();
    }

    public static void main(String[] args) {
        try {
            System.exit(PrepareTestCleanupRunner.run(args, new FileTest()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Result testConstructor0001() {
        MultiThreadRunner.waitAtBarrier();
        new File("" + Math.random());
        new File("");
        new File("\u0000");
        try {
            new File((String) null);
            return failed("expected NPE");
        } catch (NullPointerException e) {
        }
        return passed();
    }

    public Result testConstructor0002() {
        MultiThreadRunner.waitAtBarrier();
        new File("" + Math.random(), "");
        new File("", "\u0000");
        new File("\u0000", "" + Math.random());
        new File((String) null, "" + Math.random());

        try {
            new File("\u0000", (String) null);
            return failed("expected NPE");
        } catch (NullPointerException e) {
        }
        return passed();
    }

    public Result testConstructor0003() {
        MultiThreadRunner.waitAtBarrier();
        new File(new File("" + Math.random()), "");
        new File(new File(""), "\u0000");
        new File(new File("\u0000"), "" + Math.random());
        new File((File) null, "" + Math.random());

        try {
            new File(new File("\u0000"), (String) null);
            return failed("expected NPE");
        } catch (NullPointerException e) {
        }
        return passed();
    }

    public Result testConstructor0004() throws Exception {
        MultiThreadRunner.waitAtBarrier();
        new File(new URI("file:///tmp/"));

        try {
            new File((URI) null);
            return failed("expected NPE");
        } catch (NullPointerException e) {
        }
        return passed();
    }

    public Result testCanReadNonexistent() {
        MultiThreadRunner.waitAtBarrier();
        File f = new File("\b\n\r" + Math.random());
        if (!f.canRead()) {
            return passed();
        }

        return failed("can't read nonexistent files");
    }

    public Result testCanWriteNonexistent() {
        MultiThreadRunner.waitAtBarrier();
        File f = new File("\b\n\r" + Math.random());
        if (!f.canWrite()) {
            return passed();
        }

        return failed("can't write nonexistent files");
    }

    public Result testCanRead() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        File f = File.createTempFile("testCanRead", "tmp", TEMP_DIR);
        f.deleteOnExit();

        if (f.canRead()) {
            return passed();
        }

        return failed("JVM should be able to read newly created file");
    }

    public Result testCanWrite() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        File f = File.createTempFile("testCanWrite", "tmp", TEMP_DIR);
        f.deleteOnExit();
        if (!f.canWrite()) {
            return failed("Newly created file must be writable");
        }
        if (!f.setReadOnly()) {
            return failed("Failed to make file " + f.getAbsolutePath()
                    + " read-only");
        }
        if (f.canWrite()) {
            return failed("cannot write to read-only file");
        }
        if (new File(TEMP_DIR, "readonly.txt").canWrite()) {
            return failed("cannot write to gold read-only file");
        }
        try {
            new FileWriter(f);
            return failed("expected IOException trying to write to non-writable file");
        } catch (IOException e) {
        }
        return passed();
    }

    public Result testCompareTo() {
        MultiThreadRunner.waitAtBarrier();
        if (new File("a").compareTo(new File("b")) >= 0) {
            return failed("'a' vs 'b' failed - expected < 0");
        }
        if (new File("a///").compareTo(new File("a")) != 0) {
            return failed("'a///' vs 'a' failed - expected 0");
        }
        if (new File("a/./").compareTo(new File("a/")) <= 0) {
            return failed("'a/./' vs 'a/' failed - expected > 0");
        }
        if (new File("a/b/../").compareTo(new File("a")) <= 0) {
            return failed("'a/b/../' vs 'a' failed  - expected > 0");
        }
        if (new File("A").compareTo(new File("a")) != 0
                && !Utils.isPathCaseSensitive()) {
            return failed("'A' vs 'a' failed  - expected 0");
        }
        if (new File("A").compareTo(new File("a")) >= 0
                && Utils.isPathCaseSensitive()) {
            return failed("'A' vs 'a' failed  - expected < 0");
        }
        try {
            new File("A").compareTo("abcd");
            return failed("File vs String failed  - expected ClassCastException");
        } catch (ClassCastException e) {
        }

        return passed();

    }

    public Result testCreateNewFile0001() throws IOException {
        File f = null;
        try {
            MultiThreadRunner.waitAtBarrier();
            do {
                f = new File(TEMP_DIR.getAbsolutePath() + "/"
                        + System.currentTimeMillis() + "XXX" + Math.random());
            } while (f.exists());
            if (!f.createNewFile()) {
                return failed("failed to create non-existent file");
            }
            if (!f.exists()) {
                return failed("file doesn't exist after createNewFile()");
            }
            if (f.createNewFile()) {
                return failed("createNewFile() on existing file returned 'true'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (f != null) {
                f.delete();
            }
        }
        return passed();
    }

    public Result testCreateNewFile0002() throws IOException {
        try {
            MultiThreadRunner.waitAtBarrier();
            new File(TEMP_DIR + "/" + System.currentTimeMillis() + "XXX"
                    + Math.random() + "/abcd").createNewFile();
            return failed("expected IOexception creating file in non-existent directory");
        } catch (IOException e) {
            return passed();
        }
    }

    public Result testCreateTempFileShortPrefix() throws IOException {
        try {
            MultiThreadRunner.waitAtBarrier();
            File.createTempFile("ab", "abcd", TEMP_DIR);
        } catch (IllegalArgumentException e) {
            return passed();
        }
        return failed("expected IllegalArgumentException");
    }

    public Result testCreateTempFileNonExistentDirectory() {
        try {
            MultiThreadRunner.waitAtBarrier();
            File.createTempFile("abcd", "abcd", new File(TEMP_DIR + "/"
                    + System.currentTimeMillis() + "XXX" + Math.random()));
        } catch (IOException e) {
            return passed();
        }
        return failed("expected IOException");
    }

    public Result testCreateTempFile() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        File f = File.createTempFile("abcd", ".xyz", TEMP_DIR);
        if (!f.exists()) {
            return failed("created file doesn't exist");
        }
        f.deleteOnExit();

        if (!(f.getName().startsWith("abcd") && f.getName().endsWith(".xyz"))) {
            return failed("wrong suffix or prefix");
        }

        return passed();
    }

    public Result testCreateTempFileNullSuffix() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        File f = File.createTempFile("abcd", null, TEMP_DIR);
        if (!f.exists()) {
            return failed("created file doesn't exist");
        }

        f.deleteOnExit();

        if (!(f.getName().startsWith("abcd") && f.getName().endsWith(".tmp"))) {
            return failed("wrong suffix or prefix");
        }

        return passed();
    }

    public Result testDelete() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        File f = File.createTempFile("abcd", null, TEMP_DIR);
        if (!f.exists()) {
            return failed("created file doesn't exist");
        }

        if (!f.delete()) {
            return failed("delete failed");
        }

        if (f.exists()) {
            return failed("deleted file exists");
        }

        if (!f.mkdir()) {
            return failed("deleted file exists");
        }
        if (!f.exists() || !f.isDirectory()) {
            return failed("created dir doesn't exist");
        }

        if (!f.delete()) {
            return failed("delete failed");
        }

        if (f.exists()) {
            return failed("deleted dir exists");
        }

        File ff = new File(f.getAbsolutePath() + "/xx/yy");
        ff.mkdirs();
        if (!f.exists() || !f.isDirectory()) {
            return failed("created dir doesn't exist");
        }

        if (f.delete()) {
            return failed("delete non-empty directory succeeded");
        }

        if (!ff.delete() || !new File(f.getAbsolutePath() + "/xx/").delete()
                || !f.delete()) {
            return failed("last delete failed");
        }

        return passed();
    }

    public Result testDeleteOnExit() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        File f = new File(TEMP_DIR.getAbsolutePath() + "/deleteOnExit.txt");
        f.createNewFile();
        f.deleteOnExit();

        f = new File(TEMP_DIR.getAbsolutePath() + "/deleteOnExit1");
        f.mkdir();
        f.deleteOnExit();

        f = new File(TEMP_DIR.getAbsolutePath()
                + "/deleteOnExit2/deleteOnExit2");
        f.mkdirs();
        f.deleteOnExit();

        f = new File(TEMP_DIR.getAbsolutePath()
                + "/deleteOnExit2/deleteOnExit2.txt");
        f.createNewFile();
        f.deleteOnExit();

        f = new File(TEMP_DIR.getAbsolutePath()
                + "/deleteOnExit3/deleteOnExit3");
        f.mkdirs();
        f.deleteOnExit();
        f.getParentFile().deleteOnExit();
        new File(TEMP_DIR.getAbsolutePath()
                + "/deleteOnExit3/deleteOnExit3.txt").createNewFile();

        f = new File(TEMP_DIR.getAbsolutePath()
                + "/deleteOnExit4/deleteOnExit4");
        f.mkdirs();
        f.deleteOnExit();
        f.getParentFile().deleteOnExit();
        f = new File(TEMP_DIR.getAbsolutePath()
                + "/deleteOnExit4/deleteOnExit4.txt");
        f.createNewFile();
        f.deleteOnExit();

        return passed();
    }

    public Result testEquals() {
        MultiThreadRunner.waitAtBarrier();
        if (!new File("a").equals(new File("a"))) {
            return failed("'a' is not equal to 'a'");
        }
        if (new File("a").equals((new File("b")))) {
            return failed("'a' is equal to 'b'");
        }
        if (!new File("a///").equals((new File("a")))) {
            return failed("'a///' is not equal to 'a'");
        }
        if (new File("a/./").equals((new File("a/")))) {
            return failed("'a/./' is equal to 'a/'");
        }
        if (new File("a/b/../").equals((new File("a")))) {
            return failed("'a/b/../' equals to 'a'");
        }
        if (new File("A").equals((new File("a")))) {
            if (Utils.isPathCaseSensitive()) {
                return failed("'A' is equal to 'a'");
            }
        }
        if (!(new File("A").equals((new File("a"))))) {
            if (!Utils.isPathCaseSensitive()) {
                return failed("'A' is not equal to 'a'");
            }
        }
        if (new File("a").equals("a")) {
            return failed("file is equal to string");
        }

        if (new File("a").equals(null)) {
            return failed("file is equal to null");
        }

        return passed();
    }

    public Result testExists() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        File f = File.createTempFile("testExists", "tmp", TEMP_DIR);
        if (!f.exists()) {
            return failed("file " + f.getAbsolutePath()
                    + " is said to be nonexistent");
        }

        f.delete();

        if (!TEMP_DIR.exists()) {
            return failed("file " + TEMP_DIR + " is said to be nonexistent");
        }

        if (f.exists()) {
            return failed("file " + f.getAbsolutePath() + " is said to exist");
        }

        f = new File("");
        if (f.exists()) {
            return failed("empty file is said to exist");
        }
        return passed();
    }

    public Result testGetAbsolutePath() {
        File f;

        String s = "babab\u0000" + Math.random();
        
        MultiThreadRunner.waitAtBarrier();
        f = new File(s);
        if (f.getAbsolutePath().equals(s)) {
            return failed("file " + s + " is considered absolute");
        }

        if (!f.getAbsolutePath().endsWith(s)) {
            return failed("absolute path doesn't end with its non-absolute part");
        }

        return passed();
    }

    public Result testGetCanonicalPath() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (!(new File("a").getCanonicalPath().equals(new File("a")
                .getCanonicalPath()))) {
            return failed("'a' is not equal to 'a'");
        }
        if (new File("a").getCanonicalPath().equals(
                (new File("b").getCanonicalPath()))) {
            return failed("'a' is equal to 'b'");
        }
        if (!new File("a///").getCanonicalPath().equals(
                (new File("a").getCanonicalPath()))) {
            return failed("'a///' is not equal to 'a'");
        }
        if (!new File("a/./").getCanonicalPath().equals(
                (new File("a/").getCanonicalPath()))) {
            return failed("'a/./' is not equal to 'a/'");
        }
        if (!new File("a/b/../").getCanonicalPath().equals(
                (new File("a").getCanonicalPath()))) {
            return failed("'a/b/../' is not equal to 'a'");
        }
        if (new File("A").getCanonicalPath().equals(
                (new File("a").getCanonicalPath()))) {
            return failed("'A' is equal to 'a'");
        }
        if (!Utils.isPathCaseSensitive()
                && !new File(TEMP_DIR.getAbsolutePath().toLowerCase())
                        .getCanonicalPath().equals(
                                (new File(TEMP_DIR.getAbsolutePath()
                                        .toUpperCase()).getCanonicalPath()))) {
            return failed("upper/lower case in existing directories matters");
        }

        return passed();
    }

    public Result testGetCanonicalFile() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (!(new File("a").getCanonicalFile().equals(new File("a")
                .getCanonicalFile()))) {
            return failed("'a' is not equal to 'a'");
        }
        if (new File("a").getCanonicalFile().equals(
                (new File("b").getCanonicalFile()))) {
            return failed("'a' is equal to 'b'");
        }
        if (!new File("a///").getCanonicalFile().equals(
                (new File("a").getCanonicalFile()))) {
            return failed("'a///' is not equal to 'a'");
        }
        if (!new File("a/./").getCanonicalFile().equals(
                (new File("a/").getCanonicalFile()))) {
            return failed("'a/./' is not equal to 'a/'");
        }
        if (!new File("a/b/../").getCanonicalFile().equals(
                (new File("a").getCanonicalFile()))) {
            return failed("'a/b/../' is not equal to 'a'");
        }
        //seems to be inconsistency (see corresponding place in
        // testGetCanonicalPath())
        if (!Utils.isPathCaseSensitive()
                && !new File("A").getCanonicalFile().equals(
                        (new File("a").getCanonicalFile()))) {
            return failed("'A' is not equal to 'a'");
        }
        if (!Utils.isPathCaseSensitive()
                && !new File(TEMP_DIR.getAbsolutePath().toLowerCase())
                        .getCanonicalFile().equals(
                                (new File(TEMP_DIR.getAbsolutePath()
                                        .toUpperCase()).getCanonicalFile()))) {
            return failed("upper/lower case in existing directories matters");
        }

        return passed();
    }

    public Result testGetName() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (!(new File("a").getName().equals("a"))) {
            return failed("'a' name failed");
        }

        if (!(new File("a/b").getName().equals("b"))) {
            return failed("'a/b' name failed");
        }

        if (!(new File("a/b/").getName().equals("b"))) {
            return failed("'a/b/' name failed");
        }

        if (!(new File("a/b/.").getName().equals("."))) {
            return failed("'a/b/.' name failed");
        }

        if (!(new File("a/b/\n").getName().equals("\n"))) {
            return failed("'a/b/\\n' name failed");
        }

        if (!(new File("").getName().equals(""))) {
            return failed("'' name failed");
        }
        return passed();
    }

    public Result testGetParent() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (!(new File("a").getParent() == null)) {
            return failed("'a' parent failed");
        }

        if (!(new File("a/b").getParent().equals("a"))) {
            return failed("'a/b' parent failed");
        }

        if (!(new File("a/b/").getParent().equals("a"))) {
            return failed("'a/b/' parent failed");
        }

        if (!(new File("/a/b/").getParent().equals(File.separator + "a"))) {
            return failed("'/a/b/' parent failed");
        }

        if (!(new File("/////A/b/").getParent().equals(File.separator
                + ( !Utils.isPathCaseSensitive() ? File.separator : "") + "A"))) {
            return failed("'/////A/b/' parent failed");
        }

        if (!(new File("").getParent() == null)) {
            return failed("'' parent failed");
        }
        
        if (!Utils.isPathCaseSensitive() && !new File("C:\\abc\\def\\.").getParent().equals("C:\\abc")
                && !new File("C:\\abc\\def\\.").getParent().equals("C:\\abc\\def")) {
            return failed("'C:\\abc\\def\\.' parent failed : " + new File("C:\\abc\\def\\.").getParent());
        }
        
        return passed();
    }

    public Result testGetParentFile() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (!(new File("a").getParentFile() == null)) {
            return failed("'a' parent file failed");
        }

        if (!(new File("a/b").getParentFile().equals(new File("a")))) {
            return failed("'a/b' parent file failed");
        }

        if (!(new File("a/b/").getParentFile().equals(new File("a")))) {
            return failed("'a/b/' parent failed");
        }

        if (!(new File("/a/b/").getParentFile().equals(new File(File.separator
                + "a")))) {
            return failed("'/a/b/' parent file failed");
        }

        if (!(new File("/////A/b/").getParentFile().equals(new File(
                File.separator + ( !Utils.isPathCaseSensitive() ? File.separator : "") + "A")))) {
            return failed("'/////A/b/' parent file failed");
        }

        if (!(new File("").getParentFile() == null)) {
            return failed("'' parent failed");
        }
        return passed();
    }

    public Result testGetPath() throws IOException {
        if (!(new File("a").getPath().equals("a"))) {
            return failed("File('a').getPath() is not equal to 'a'");
        }
        if (!new File("a///").getPath().equals("a")) {
            return failed("File('a///').getPath() is not equal to 'a'");
        }
        if (!new File("a/./").getPath().equals("a" + File.separator + ".")) {
            return failed("File('a/./').getPath() is not equal to 'a"
                    + File.separator + ".' : " + new File("a/./").getPath());
        }
        if (!new File("a/b/../").getPath().equals(
                "a" + File.separator + "b" + File.separator + "..")) {
            return failed("File('a/b/../').getPath() is not equal to 'a"
                    + File.separator + "b" + File.separator + "..': "
                    + new File("a/b/../").getPath());
        }
        if (!new File("/a/b/../").getPath().equals(
                File.separator + "a" + File.separator + "b" + File.separator
                        + "..")) {
            return failed("File('/a/b/../').getPath() is not equal to '"
                    + File.separator + "a" + File.separator + "b"
                    + File.separator + "..': " + new File("/a/b/../").getPath());
        }
        if (!new File("/////a/b/../").getPath().equals(
                File.separator + ( !Utils.isPathCaseSensitive() ? File.separator : "") + "a" + File.separator + "b"
                        + File.separator + "..")) {
            return failed("File('/////a/b/../').getPath() is not equal to '"
                    + File.separator + ( !Utils.isPathCaseSensitive() ? File.separator : "") + "a" + File.separator
                    + "b" + File.separator + "..': "
                    + new File("/////a/b/../").getPath());
        }

        if (new File("A").getPath().equals("a")) {
            return failed("File('A').getPath() is equal to 'a'");
        }

        return passed();
    }

    //same test as for testGetPath - just copy-paste
    public Result testToString() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (!(new File("a").toString().equals("a"))) {
            return failed("File('a').toString() is not equal to 'a'");
        }
        if (!new File("a///").toString().equals("a")) {
            return failed("File('a///').toString() is not equal to 'a'");
        }
        if (!new File("a/./").toString().equals("a" + File.separator + ".")) {
            return failed("File('a/./').toString() is not equal to 'a"
                    + File.separator + ".' : " + new File("a/./").toString());
        }
        if (!new File("a/b/../").toString().equals(
                "a" + File.separator + "b" + File.separator + "..")) {
            return failed("File('a/b/../').toString() is not equal to 'a"
                    + File.separator + "b" + File.separator + "..': "
                    + new File("a/b/../").toString());
        }
        if (!new File("/a/b/../").toString().equals(
                File.separator + "a" + File.separator + "b" + File.separator
                        + "..")) {
            return failed("File('/a/b/../').toString() is not equal to '"
                    + File.separator + "a" + File.separator + "b"
                    + File.separator + "..': "
                    + new File("/a/b/../").toString());
        }
        if (!new File("/////a/b/../").toString().equals(
                File.separator + ( !Utils.isPathCaseSensitive() ? File.separator : "") + "a" + File.separator + "b"
                        + File.separator + "..")) {
            return failed("File('/////a/b/../').toString() is not equal to '"
                    + File.separator + ( !Utils.isPathCaseSensitive() ? File.separator : "") + "a" + File.separator
                    + "b" + File.separator + "..': "
                    + new File("/////a/b/../").toString());
        }

        if (new File("A").toString().equals("a")) {
            return failed("File('A').toString() is equal to 'a'");
        }

        return passed();
    }

    public Result testHashCode() {
        MultiThreadRunner.waitAtBarrier();
        if (new File("a").hashCode() != new File("a").hashCode()) {
            return failed("'a' is not equal to 'a'");
        }

        //two files differ by one char - hashcode should be different
        if (new File("a").hashCode() == new File("b").hashCode()) {
            return failed("'a' is equal to 'b'");
        }
        if (new File("a///").hashCode() != new File("a").hashCode()) {
            return failed("'a///' is not equal to 'a'");
        }
        if (new File("a/./").hashCode() == new File("a/").hashCode()) {
            return failed("'a/./' is equal to 'a/'");
        }

        if (new File("A").hashCode() == new File("a").hashCode()
                && Utils.isPathCaseSensitive()) {
            return failed("'A' is equal to 'a'");
        }
        if (new File("A").hashCode() != new File("a").hashCode()
                && !Utils.isPathCaseSensitive()) {
            return failed("'A' is not equal to 'a'");
        }
        return passed();
    }

    public Result testIsAbsolute() {
        if (new File("a").isAbsolute()) {
            return failed("'a' is absolute");
        }
        if (new File("a/b").isAbsolute()) {
            return failed("'a/b' is absolute");
        }
        if (new File("/a/b").isAbsolute() && !Utils.isPathCaseSensitive()) {
            return failed("'/a/b' is absolute");
        }
        if (!new File("/a/b").isAbsolute() && Utils.isPathCaseSensitive()) {
            return failed("'/a/b' is not absolute");
        }
        if (!new File("//a/b").isAbsolute()) {
            return failed("'//a/b' is not absolute");
        }
        if (!new File("////a/b").isAbsolute()) {
            return failed("'////a/b' is not absolute");
        }
        if (!new File("\\\\\\a\b").isAbsolute() && !Utils.isPathCaseSensitive()) {
            return failed("'\\\\\\a/b' is not absolute");
        }
        if (new File("").isAbsolute()) {
            return failed("'' is  absolute");
        }
        return passed();
    }

    public Result testIsDirectory() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (new File("a").isDirectory()) {
            return failed("'a' is directory");
        }
        if (!TEMP_DIR.isDirectory()) {
            return failed("'" + TEMP_DIR + "' is not a directory");
        }
        if (new File("").isDirectory()) {
            return failed("'' is directory");
        }
        if (!Utils.isPathCaseSensitive() && !new File("//C:/").isDirectory()) {
            return failed("'//c:/' is not a directory");
        }
        if (Utils.isPathCaseSensitive() && !new File("/").isDirectory()) {
            return failed("'/' is not a directory");
        }

        File f = File.createTempFile("testIsDirectory", "tmp", TEMP_DIR);
        f.deleteOnExit();

        if (f.isDirectory()) {
            return failed(f.getAbsolutePath() + " is a directory");
        }
        return passed();
    }

    public Result testIsFile() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (new File("a").isFile()) {
            return failed("'a' is a file");
        }
        if (TEMP_DIR.isFile()) {
            return failed("'" + TEMP_DIR + "' is a file");
        }
        if (new File("").isFile()) {
            return failed("'' is a file");
        }
        if (!Utils.isPathCaseSensitive() && new File("//C:/").isFile()) {
            return failed("'//c:/' is a file");
        }
        if (Utils.isPathCaseSensitive() && new File("/").isFile()) {
            return failed("'/' is a file");
        }

        File f = File.createTempFile("testIsFile", "tmp", TEMP_DIR);
        f.deleteOnExit();

        if (!f.isFile()) {
            return failed(f.getAbsolutePath() + " is not a file");
        }
        return passed();
    }

    public Result testLength() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (new File("a").length() != 0) {
            return failed("'a' length is not 0");
        }
        TEMP_DIR.length(); //just to cause possible exception -
        // return value is not checked
        File f = File.createTempFile("testLength", "tmp", TEMP_DIR);
        f.deleteOnExit();

        if (f.length() != 0) {
            return failed("Expected size of file " + f.getAbsolutePath()
                    + " to be 0");
        }

        if (new File(TEMP_DIR, "4chars.txt").length() != 4) {
            return failed("Expected size of file " + new File(TEMP_DIR, "4chars.txt").getAbsolutePath()
                    + " to be 4");
        }

        return passed();
    }

    public Result testList() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (new File("a").list() != null) {
            return failed("'a' list is not null");
        }

        File f = File.createTempFile("testList", "tmp", TEMP_DIR);
        f.deleteOnExit();

        if (f.list() != null) {
            return failed("plain file list is not null");
        }

        f.delete();

        //no such file now - create a directory
        f.mkdir();

        if (f.list().length != 0) {
            return failed("list of newly created directory is not empty");
        }

        Collection tmpFiles = new ArrayList();

        for (int i = 0; i < 10; ++i) {
            File ff = File.createTempFile("testList", "tmp", f);
            ff.deleteOnExit();
            tmpFiles.add(ff);
            Collection c = Arrays.asList(f.list());
            if (c.size() != tmpFiles.size()) {
                return failed("wrong number of files read");
            }

            for (Iterator iter = tmpFiles.iterator(); iter.hasNext();) {
                File fff = (File) iter.next();
                if (!c.contains(fff.getName())) {
                    return failed("couldn't find " + fff.getName()
                            + " in directory listing");
                }
            }

        }

        return passed();
    }

    public Result testListFilter() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (new File("a").list(null) != null) {
            return failed("'a' list is not null");
        }

        File f = File.createTempFile("testListFilter", "tmp", TEMP_DIR);
        f.deleteOnExit();

        if (f.list(null) != null) {
            return failed("plain file filtered list is not null");
        }

        f.delete();

        //no such file now - create a directory
        f.mkdir();

        if (f.list(null).length != 0) {
            return failed("list of newly created directory is not empty");
        }

        Collection tmpFiles = new ArrayList();

        for (int i = 0; i < 10; ++i) {
            File ff = File.createTempFile("testList", "tmp" + (i % 2), f);
            ff.deleteOnExit();
            tmpFiles.add(ff);
            Collection c = Arrays.asList(f.list(new FilenameFilter() {
                public boolean accept(File arg0, String arg1) {
                    return arg1.endsWith("tmp0");
                }
            }));

            if (c.size() != (tmpFiles.size() + 1) / 2) {
                return failed("wrong number of files read");
            }

            for (Iterator iter = tmpFiles.iterator(); iter.hasNext();) {
                File fff = (File) iter.next();
                if (f.getName().endsWith("tmp0") && !c.contains(fff.getName())) {
                    return failed("couldn't find " + fff.getName()
                            + " in filtered directory listing");
                }
            }
        }
        return passed();
    }

    public Result testListFiles() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (new File("a").listFiles() != null) {
            return failed("'a' files list is not null");
        }

        File f = File.createTempFile("testListFiles", "tmp", TEMP_DIR);
        f.deleteOnExit();

        if (f.listFiles() != null) {
            return failed("plain file files list is not null");
        }

        f.delete();

        //no such file now - create a directory
        f.mkdir();

        if (f.listFiles().length != 0) {
            return failed("list of newly created directory is not empty");
        }

        Collection tmpFiles = new ArrayList();

        for (int i = 0; i < 10; ++i) {
            File ff = File.createTempFile("testListFiles", "tmp", f);
            ff.deleteOnExit();
            tmpFiles.add(ff);
            Collection c = Arrays.asList(f.listFiles());
            if (c.size() != tmpFiles.size()) {
                return failed("wrong number of files read");
            }

            for (Iterator iter = tmpFiles.iterator(); iter.hasNext();) {
                File fff = (File) iter.next();
                if (!c.contains(fff)) {
                    return failed("couldn't find " + fff.getName()
                            + " in directory listing");
                }
            }

        }

        return passed();
    }

    public Result testListFilesFilter() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        if (new File("a").listFiles((FilenameFilter) null) != null) {
            return failed("'a' list is not null");
        }

        File f = File.createTempFile("testListFilesFilter", "tmp", TEMP_DIR);
        f.deleteOnExit();

        if (f.listFiles((FilenameFilter) null) != null) {
            return failed("plain file filtered list is not null");
        }

        f.delete();

        //no such file now - create a directory
        f.mkdir();

        if (f.list(null).length != 0) {
            return failed("files list of newly created directory is not empty");
        }

        Collection tmpFiles = new ArrayList();

        for (int i = 0; i < 10; ++i) {
            File ff = File.createTempFile("testListFilesFilter", "tmp"
                    + (i % 2), f);
            ff.deleteOnExit();
            tmpFiles.add(ff);
            Collection c = Arrays.asList(f.listFiles(new FilenameFilter() {
                public boolean accept(File arg0, String arg1) {
                    return arg1.endsWith("tmp0");
                }
            }));

            if (c.size() != (tmpFiles.size() + 1) / 2) {
                return failed("wrong number of files read");
            }

            for (Iterator iter = tmpFiles.iterator(); iter.hasNext();) {
                File fff = (File) iter.next();
                if (f.getName().endsWith("tmp0") && !c.contains(fff)) {
                    return failed("couldn't find " + fff.getName()
                            + " in filtered directory listing");
                }
            }
        }
        return passed();
    }

    public Result testMkdir() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        File f = File.createTempFile("testMkdir", "tmp", TEMP_DIR);
        f.delete();

        if (f.isDirectory()) {
            return failed("deleted file is a directory:" + f.getAbsolutePath());
        }
        if (!f.mkdir()) {
            return failed("failed to make dir " + f.getAbsolutePath());
        }
        if (!f.isDirectory()) {
            return failed("created file is not a directory:"
                    + f.getAbsolutePath());
        }

        f.delete();

        if (new File(f.getAbsolutePath() + "/a/b/c/d").mkdir()) {
            return failed("managed to make dir at nonexistent location : "
                    + f.getAbsolutePath() + "/a/b/c/d");
        }

        if (new File(f.getAbsolutePath() + "/..").mkdir()) {
            return failed("managed to make existing dir: "
                    + f.getAbsolutePath() + "/..");
        }
        if (TEMP_DIR.mkdir()) {
            return failed("managed to make existing dir: " + TEMP_DIR);
        }

        if (new File("").mkdir()) {
            return failed("managed to make dir at empty location ");
        }

        return passed();
    }

    public Result testMkdirs() throws IOException {
        File f = File.createTempFile("testMkdirs", "tmp", TEMP_DIR);
        f.delete();

        if (f.isDirectory()) {
            return failed("deleted file is a directory:" + f.getAbsolutePath());
        }

        if (!new File(f.getAbsolutePath() + "/a/b/c/d").mkdirs()) {
            return failed("failed to make dirs : " + f.getAbsolutePath()
                    + "/a/b/c/d");
        }

        if (!(f.isDirectory()
                && new File(f.getAbsolutePath() + "/a").isDirectory()
                && new File(f.getAbsolutePath() + "/a/b/").isDirectory()
                && new File(f.getAbsolutePath() + "/a/b/c").isDirectory() && new File(
                f.getAbsolutePath() + "/a/b/c/d").isDirectory())) {
            return failed("directory chain wasn't created");
        }

        if (new File(f.getAbsolutePath() + "/a/b/c/d").mkdirs()) {
            return failed("managed to create existing directory : "
                    + f.getAbsolutePath() + "/a/b/c/d");
        }

        if (!new File(f.getAbsolutePath() + "/a/b/c/d").delete()
                || !new File(f.getAbsolutePath() + "/a/b/c").delete()
                || !new File(f.getAbsolutePath() + "/a/b").delete()
                || !new File(f.getAbsolutePath() + "/a").delete()
                || !f.delete()) {
            return failed("managed to create existing directory : "
                    + f.getAbsolutePath() + "/a/b/c/d");
        }

        if (new File("").mkdirs()) {
            return failed("managed to make dir at empty location ");
        }

        return passed();
    }

    public Result testRenameTo() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        File f = File.createTempFile("testRenameTo", "tmp", TEMP_DIR);
        f.deleteOnExit();

        try {
            f.renameTo(null);
            return failed("Expected NPE in renameTo(null)");
        } catch (NullPointerException e) {
        }

        if (!f.renameTo(f)) {
            return failed("self-rename failed");
        }
        if (f.renameTo(new File(f.getAbsolutePath() + "/fff"))) {
            return failed("renaming to nonexistent directory succeeded");
        }

        File ff = File.createTempFile("testRenameTo", "tmp", TEMP_DIR);
        ff.deleteOnExit();

        if(Utils.isPathCaseSensitive()) { 
          if (!f.renameTo(ff)) {
              return failed("renaming to existing file failed");
          }
        } else {
          if (f.renameTo(ff)) {
              return failed("renaming to existing file succeeded");
          }
        }

        ff.delete();

        if (!f.renameTo(ff)) {
            return failed("renaming failed");
        }

        if (f.exists()) {
            return failed("file exists after rename");
        }

        f = File.createTempFile("testRenameTo", "tmp", TEMP_DIR);
        f.deleteOnExit();
        f.setReadOnly();
        ff.delete();

        if (!f.renameTo(ff)) {
            return failed("renaming read-only file failed");
        }

        f.delete();
        f.mkdir();

        if (ff.renameTo(f)) {
            return failed("rename plain file to directory succeeded");
        }

        if (f.renameTo(ff)) {
            return failed("rename directory to plain file succeeded");
        }

        ff.delete();
        if (!f.renameTo(ff)) {
            return failed("renaming directory failed");
        }

        return passed();
    }

    public Result testLastModified() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        long start = System.currentTimeMillis();
        File f = File.createTempFile("testLastModified", "tmp", TEMP_DIR);
        f.deleteOnExit();

        if (!(System.currentTimeMillis() / 1000 >= f.lastModified() / 1000 && f
                .lastModified() / 1000 >= start / 1000)) {
            return failed("last modified time of newly created file is not 'now'");
        }

        if (!f.setLastModified(1000)) {
            return failed("setLastModified failed");
        }

        if (f.lastModified() != 1000) {
            return failed("lastModified returns value that is not the one which was set by setLastModified()");
        }

        if (!f.setLastModified(0)) {
            return failed("setLastModified 0 failed");
        }

        if (f.lastModified() != 0) {
            return failed("lastModified 0 returns value that is not the one which was set by setLastModified()");
        }

        try {
            f.setLastModified(-1000);
            return failed("expected IllegalArgumentException in setting setLastModified(-1000)");
        } catch (IllegalArgumentException e) {
        }

        FileWriter fw = new FileWriter(f);
        fw.write("a");
        fw.close();

        if (!(System.currentTimeMillis() / 1000 >= f.lastModified() / 1000 && f
                .lastModified() / 1000 >= start / 1000)) {
            return failed("last modified time of recently written file is not 'now'");
        }

        f.delete();
        f.mkdir();

        if (!f.isDirectory()) {
            return failed("expected directory here");
        }

        if (!(System.currentTimeMillis() / 1000 >= f.lastModified() / 1000 && f
                .lastModified() / 1000 >= start / 1000)) {
            return failed("last modified time of newly created dir is not 'now'");
        }

        if (!f.setLastModified(1000)) {
            return failed("setLastModified failed");
        }

        if (f.lastModified() != 1000) {
            return failed("lastModified returns value that is not the one which was set by setLastModified()");
        }

        if (!f.setLastModified(0)) {
            return failed("setLastModified 0 failed");
        }

        if (f.lastModified() != 0) {
            return failed("lastModified 0 returns value that is not the one which was set by setLastModified()");
        }

        try {
            f.setLastModified(-1000);
            return failed("expected IllegalArgumentException in setting setLastModified(-1000)");
        } catch (IllegalArgumentException e) {
        }

        File.createTempFile("testLastModified", "tmp", f).delete();

        if (!(System.currentTimeMillis() >= f.lastModified() && f
                .lastModified() >= start)) {
            return failed("last modified time of recently modified dir is not 'now'");
        }

        return passed();
    }

    public Result testToURL() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        new File("").toURL(); // no exception
        new File("a").toURL(); //no exception

        String s = TEMP_DIR.getAbsolutePath();

        //strip trailing slaches
        while (s.charAt(s.length() - 1) == '/'
                || s.charAt(s.length() - 1) == '\\') {
            s = s.substring(0, s.length() - 1);
        }

        if (!new File(s).toURL().toString().endsWith("/")) {
            return failed("expected directory URL to end with "
                    + File.separator + ", but it is not : "
                    + new File(s).toURL().toString());
        }

        if (!new File(" ").toURL().toString().endsWith(" ")) {
            return failed("expected illegal URL chars not to be escaped");
        }

        if (!new File("\u0000").toURL().toString().endsWith("\u0000")) {
            return failed("expected illegal URL chars not to be escaped");
        }

        return passed();
    }
    
    public Result testToURI() throws IOException {
        MultiThreadRunner.waitAtBarrier();
        new File("").toURI(); // no exception
        new File("a").toURI(); //no exception

        String s = TEMP_DIR.getAbsolutePath();

        //strip trailing slaches
        while (s.charAt(s.length() - 1) == '/'
                || s.charAt(s.length() - 1) == '\\') {
            s = s.substring(0, s.length() - 1);
        }

        if (!new File(s).toURI().toString().endsWith("/")) {
            return failed("expected directory URI to end with "
                    + File.separator + ", but it is not : "
                    + new File(s).toURI().toString());
        }

        if (!new File(" ").toURI().toString().endsWith("%20") && !new File(" ").toURI().toString().endsWith("/%20/")) {
            return failed("expected illegal URI chars to be escaped : " + new File(" ").toURI());
        }

        if (!new File("\u0000").toURI().toString().endsWith("/\u0000/")) {
            return failed("expected illegal URI chars not to be escaped : " + new File("\u0000").toURI());
        }

        return passed();
    }
}
