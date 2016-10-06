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
 * @version $Revision: 1.6 $
 */

package org.apache.harmony.test.reliability.api.io;


import org.apache.harmony.test.reliability.share.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.zip.ZipEntry;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipOutputStream;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipInputStream;

import java.util.jar.JarOutputStream;
import java.util.jar.JarInputStream;

/**
 * Goal: check for intermittent errors operating with a file via java.io classes.
 *
 * Idea: The test uses java.io.* API methods working with files for runtime testing - 
 *       many files of various types (java, zip, jar, tmp) are created in the test and
 *       the input/output operations are performed on them.
 *       This test case is repeated until TestTimeout for reliability testing
 * 
 */

public class IOTest  extends Test {

    String separator = File.separator;
    String filedir = System.getProperty("java.io.tmpdir") + separator + "reliability_io";

    public int test(String[] params) {

        int cnt = Integer.parseInt(params[0]);
        if (cnt < 4 ) {
            cnt = 100;
        } else {
            cnt = cnt/4;
        }

        String path_to_files =  params[1];

        if (!path_to_files.endsWith("auxiliary")) {
            log.add("invalid path to the golden files");
            return fail("Failed.");
        }
        // log.add("tmpdir = " + filedir);

        File temp = new File(filedir);

        if(temp.exists()) {
            File[] lstf = temp.listFiles();
            for( int i = 0; i < lstf.length; i++) {
                if( !lstf[i].delete()) {
                    return fail("Delete " + lstf[i].getName() + " file: Failed.");

                }
            }

        }

        temp.mkdir();
        temp.deleteOnExit();

        String[] listFileNames = new String[cnt];

        String javaFileName = "java_file.java";

        if(System.getProperty("os.name").indexOf("Windows") == -1) {
            javaFileName = "java_file_lnx.java";
        }

        if(!createEmptyFiles("file", ".java", filedir, cnt)) {
            return fail("Creates a new empty file: Failed.");
        }
        
        if(!createTMP("file", "tmp", filedir, cnt)) {
            return fail("Creates a new tmp file: Failed.");
        }

        if(!copyZipContent(path_to_files + separator + "zip_file.zip", filedir + separator + "file", cnt)) {
            return fail("Copy a zip content: Failed.");
        }

        if(!copyJarContent(path_to_files + separator + "jar_file.jar", "file",  cnt)) {
            return fail("Copy a jar content: Failed.");
        }

        if(!copyJavaFileContent(path_to_files + separator + javaFileName, filedir + separator + "file", ".java",  cnt)) {
            return fail("Copy a java file content: Failed.");
        }

        listFileNames = listFileNamesInDirectory(filedir, "tmp");
        if(!copyContentToTmpFiles(path_to_files + separator + javaFileName, listFileNames, filedir, cnt)) {
            return fail("Copy a content to tmp file: Failed.");
        }

        String[] listOutFileNames = listFileNamesInDirectory(filedir, "file", ".java");
        if(!copyFilesContentUseString(filedir, "file", ".out", listOutFileNames, cnt)) {
            return fail("Copy a content to file.out: Failed.");
        }

        if(!copyFilesContentUseString(filedir, "tmp_file", ".out", listFileNames, cnt)) {
            return fail("Copy a content to tmp_file.out: Failed.");
        }

        String[] listAllFiles = temp.list();
        File[] listFiles = temp.listFiles();
        int fileCount = 0;
        for( int i = 0; i < listFiles.length; i++) {
            if( listFiles[i].isFile()) {
                listAllFiles[fileCount] = listFiles[i].getName();
                fileCount++;
            }
        }

        if(!compressZip(filedir, "allfileszip.zip", "jar", listAllFiles)) {
            return fail("Compress a zip file: Failed.");
        }

        String fileName;
        for( int i = 0; i < listFiles.length; i++) {
            fileName = listAllFiles[i];
            if(!fileName.endsWith("jar") && !fileName.startsWith("allfileszip")) {
                listFiles[i].delete();
            }
        }

        if(!unZipFile(filedir, "allfileszip.zip")) {
            return fail("Uncompress allfileszip.zip file: Failed.");
        }

        if(!unZipListFiles(filedir, "file", "zip", listAllFiles, "file_from_zip")) {
            return fail("Uncompress file.zip files: Failed.");
        }

        if(!compressZip(filedir, "allfileszip.jar", "jar", listAllFiles)) {
            return fail("Uncompress allfileszip.jar file: Failed.");
        }

        if(!unZipListFiles(filedir, "file", "jar", listAllFiles, "file_from_jar")) {
            return fail("Uncompress file.jar files: Failed.");
        }

        listFiles = temp.listFiles();
        if(!renameFiles("tmp", "renamed_tmp_file", listFiles)) {
            return fail("Rename tmp files: Failed.");
        }

        if(!renameFiles("file", "java", "renamed_out_file", listFiles)) {
            return fail("Rename file.java: Failed.");
        }

        if(!renameFiles("file", "out", "renamed_out_file", listFiles)) {
            return fail("Rename file.out files: Failed.");
        }

        if(!renameFiles("file_from_zip","", "renamed_file_from_zip", listFiles)) {
            return fail("Rename file_from_zip.out files: Failed.");
        }
 
        if(!compareFilesContent(temp, path_to_files + separator + javaFileName, "renamed_out_file")) {
            return fail("Check renamed_out_file files: Failed.");
        }

        if(!compareFilesContent(temp, path_to_files + separator + javaFileName, "renamed_tmp_file")) {
            return fail("Check renamed_tmpt_file: Failed.");
        }

        if(!compareFilesContent(temp, filedir + separator + "renamed_file_from_zip0", "renamed_file_from_zip")) {
            return fail("Check renamed_file_from_zip.");
        }


        listFiles = temp.listFiles();
        for(int i = 0; i < listFiles.length; i++) {
            if(!listFiles[i].delete()) {
                log.add("delete " + listFiles[i].getName() + "  failed");
                return fail("Failed.");
            }
        }

        File[] unexisted_file = new File[cnt];

        for (int i = 0; i < cnt; i++) {
            unexisted_file[i] = new File(filedir + separator + "file"+i+".java");
        }

        for (int i = 0; i < cnt; i++) {
            unexisted_file[i].delete();
        }

        return pass("OK");
    }


    /* Create empty files */
    protected boolean createEmptyFiles(String prefix, String suffix, String directory, int numfiles) {
        File file;
        for (int i = 0; i < numfiles; i++) {
            try {
                file = new File(directory + separator + prefix + i + suffix);
                file.createNewFile();
            } catch (IOException e) {

                log.add("Create empty files: IOException during test execution");
                log.add(e);
                return false;
            }
        }
        return true;
    }

    /* Create tmp files */
    protected boolean createTMP(String prefix, String suffix, String directory, int numfiles) {

        File dir = new File(directory);

        for (int i = 0; i < numfiles; i++) {
            try {
                File.createTempFile(prefix, suffix, dir).getName();
            } catch (IOException e) {

                log.add("Create TMP file "+ prefix + i +suffix +": IOException during test execution");
                log.add(e);
                return false;
            }
        }
        return true;
    }

    /* Copy the content of zip files */
    protected boolean copyZipContent(String pathtoparentfiles, String chaildname,  int numfiles) {

        BufferedWriter bufWriter = null;
        BufferedReader bufReader = null;
        File parentFile, childFile;
        int singlCharacter;
        String fileName; 

        for (int i = 0; i < numfiles; i++) {
            parentFile = new File(pathtoparentfiles);
            fileName = chaildname + i + ".zip";
            childFile = new File(fileName);

            try {
                childFile.createNewFile();
            } catch (IOException e) {
                log.add("creates a new, empty zip file : IOException during test execution");
                log.add(e);
                return false;
            }

            try {
                bufReader = new BufferedReader(new FileReader(parentFile));
                bufWriter = new BufferedWriter(new FileWriter(fileName));
                while((singlCharacter = bufReader.read()) != -1) {
                    bufWriter.write(singlCharacter);
                }

                bufReader.close();
                bufWriter.close();

            } catch (FileNotFoundException e) {
                log.add("Copy the content of zip: file" + parentFile +" does not exist");
                log.add(e);
                return false;
            } catch (IOException ee) {
                log.add("Copy the content of zip: file " + fileName + " cannot be opened");
                log.add(ee);
                return false;
            } finally {
                try {
                    bufReader.close();
                    bufWriter.close();
                }  catch (IOException e) {
                    log.add(e);
                }
            }
        }
        return true;
    }

    /* Copy the content of jar files */
    protected boolean copyJarContent(String pathtoparentfiles, String chaildname,  int numfiles) {

        RandomAccessFile wf = null;
        RandomAccessFile rf = null;
        File parentFile;
        String fileName; 
        byte[] cbuf = new byte[4128];

        for (int i = 0; i < numfiles; i++) {
            parentFile = new File(pathtoparentfiles);
            fileName = filedir + separator + chaildname + i + ".jar";
            try {
                rf =new RandomAccessFile(parentFile, "r");
            } catch (FileNotFoundException e) {
                log.add("Copy the content of jar: file " + parentFile +" does not exist");
                log.add(e);
                return false;
            }

            try {
                wf =new RandomAccessFile(fileName, "rw");
            } catch (FileNotFoundException e) {
                log.add("Copy the content of jar: file " + fileName +" does not exist");
                log.add(e);
                return false;
            }

            try {
                rf.read(cbuf);
            } catch (IOException e) {
                log.add("Read the content of " + parentFile + " jar: I/O error occurs");
                log.add(e);
                return false;
            } finally {
                try {
                    rf.close();
                }  catch (IOException e) {
                    log.add(e);
                }
            }

            try {
                wf.write(cbuf);
            } catch (IOException e) {
                log.add("Write the content to " + fileName + " jar: I/O error occurs");
                log.add(e);
                return false;
            } finally {
                try {
                    wf.close();
                }  catch (IOException e) {
                    log.add(e);
                }
            }


        }
        return true;
    }

    /* Copy the content of java files */
    protected boolean copyJavaFileContent(String pathtoparentfiles, String pathtochaild, String suffix, int numfiles) {

        BufferedInputStream in = null;
        PrintStream out = null;
        BufferedReader bufReader = null;
        File parentFile, childFile;
        String str;

        for (int i = 0; i < numfiles; i++) {

            try {
                in =new  BufferedInputStream(new FileInputStream(pathtoparentfiles));
            } catch (FileNotFoundException e) {
                log.add("Copy the content of golden to file.java:  file " + pathtoparentfiles + " does not exist");
                log.add(e);
                return false;
            }

            try {
                out = new PrintStream(new BufferedOutputStream(new FileOutputStream(pathtochaild + i + suffix)));
            } catch (FileNotFoundException e) {
                log.add("Copy the content of golden to file.java:  file " + pathtochaild + i + suffix + " does not exist");
                log.add(e);
                return false;
            }

            bufReader = new BufferedReader(new InputStreamReader(in));     

            try {
                while((str = bufReader.readLine()) != null) {
                    out.println(str);
                }
            } catch (IOException e) {
                log.add("Copy the content of golden to file.java: IOException during test execution");
                log.add(e);
                return false;
            } finally {
                try {
                    in.close();
                    out.close();
                    bufReader.close();
                }  catch (IOException e) {
                    log.add(e);
                }
            }

        }
        return true;
    }

    /* Returns an array of file names in the directory denoted by filename */
    protected String[] listFileNamesInDirectory(String filedir, String filesname) {

        File[] listFiles = new File(filedir).listFiles();
        int filescnt = listFiles.length;
        String[] listFileNames = new String[filescnt];
        String str;
        int index = 0;

        for( int i = 0; i < filescnt; i++) {
            str = listFiles[i].getName();
            if(str.indexOf(filesname) != -1 && index < filescnt ) {
                listFileNames[index] = str;
                index++;
            }
        }
        return listFileNames;
    }

    /* Returns an array of file names in the directory denoted by filename and extention */
    protected String[] listFileNamesInDirectory(String filedir, String filesname, String extension) {

        File[] listFiles = new File(filedir).listFiles();
        int filescnt = listFiles.length;
        String[] listFileNames = new String[filescnt];
        String str;
        int index = 0;

        for( int i = 0; i < filescnt; i++) {
            str = listFiles[i].getName();
            if(str.indexOf(filesname) != -1 && str.endsWith(extension) && index < filescnt ) {
                listFileNames[index] = str;
                index++;
            }
        }
        return listFileNames;
    }

    /* Copy the content of java file to  tmp files*/
    protected boolean copyContentToTmpFiles(String pathtoparentfiles, String[] chaildnames, String pathtolistfiles, int numfiles) {

        BufferedInputStream in = null;
        PrintWriter prnWriter = null;
        BufferedReader bufReader = null;
        String str;

        if(numfiles <= chaildnames.length ) {
            for (int i = 0; i < numfiles; i++) {
                try {
                    in =new  BufferedInputStream(new FileInputStream(pathtoparentfiles));
                } catch (FileNotFoundException e) {
                    log.add("Copy the content of golden to tmp files: the file" + pathtoparentfiles + " does not exist");
                    log.add(e);
                    return false;
                }

                bufReader = new BufferedReader(new InputStreamReader(in));     

                try {
                    prnWriter = new PrintWriter(new BufferedWriter(new FileWriter(pathtolistfiles + separator + chaildnames[i])));    
                } catch (IOException e) {
                    log.add("Copy the content of golden to tmp files: the file" + chaildnames[i] + " does not exist");
                    log.add(e);
                    return false;
                }

                try {
                    while((str = bufReader.readLine()) != null) {
                        prnWriter.println(str);
                    }
                } catch (IOException e) {
                    log.add("Copy the content of golden to tmp files: IOException during test execution");
                    log.add(e);
                    return false;
                } finally {
                    try {
                        prnWriter.close();
                        bufReader.close();
                        in.close();
                    }  catch (IOException e) {
                        log.add(e);
                    }
                }

            }
        } else {
            log.add("Incorrected number of files");
            return false;
        }
        return true;
    }

    /* Copy the content of files */
    protected boolean copyFilesContentUseString(String directory, String filename, String sufix,  String[] chaildnames, int numfiles) {

        PrintWriter prnWriter = null;
        BufferedReader bufReader = null;
        String str1 = new String();
        String str2 = new String();

        if(numfiles <= chaildnames.length ) {
            for (int i = 0; i < numfiles; i++) {
                str1 = new String();
                str2 = new String();

                try {
                    bufReader = new  BufferedReader(new FileReader(directory + separator + chaildnames[i]));
                    while((str1 = bufReader.readLine()) != null) {
                        str2 += str1 + "\n";
                    }
                } catch (FileNotFoundException e) {
                    log.add("Read the content of the file" + chaildnames[i] + ". The file does not exist");
                    log.add(e);
                    return false;
                } catch (IOException ee) {
                    log.add("Read a line of text: I/O error occurs");
                    log.add(ee);
                    return false;
                } finally {
                    try {
                        bufReader.close();
                    } catch (IOException e) {
                        log.add(e);
                    }

                }

                bufReader = new BufferedReader(new StringReader(str2));

                try {
                    prnWriter = new PrintWriter(new BufferedWriter(new FileWriter(directory + separator + filename + i + sufix)));
                } catch (IOException e) {
                    log.add("Write the content to the " + filename + i + sufix + " file");
                    log.add(e);
                    return false;
                }

                try {
                    while((str1 = bufReader.readLine()) != null) {
                        prnWriter.println(str1);
                    }
                } catch (Exception e) {
                    log.add("write the file content to " + filename + i + sufix + ": Exception during test execution");
                    log.add(e);
                    return false;
                } finally {

                    try {
                        bufReader.close();
                    } catch (IOException e) {
                        log.add(e);
                    }
                    prnWriter.close();
                }
            }
        } else {
            log.add("Incorrected number of files");
            return false;
        }
        return true;
    }

    /* compress files to zip */ 
    protected boolean compressZip(String directory, String filenamezip, String withoutfilesextension, String[] listfiles) {

        BufferedReader bufReader;
        FileOutputStream fos = null;
        CheckedOutputStream checkoutput = null;
        ZipOutputStream zos = null;
        String fileName = "";

        try {
            fos = new FileOutputStream(directory + separator + filenamezip);
        } catch (FileNotFoundException e) {
            log.add("Read the content of the file" + filenamezip + ". The file does not exist");
            log.add(e);
            return false;
        }

        checkoutput = new CheckedOutputStream(fos, new CRC32());
        zos = new ZipOutputStream(new BufferedOutputStream(checkoutput));

        try {
            for(int i = 0; i < listfiles.length; i++) {
                fileName = listfiles[i];
                if(!fileName.endsWith(withoutfilesextension)) {
                    bufReader = new BufferedReader(new FileReader(filedir + separator + listfiles[i]));
                    zos.putNextEntry(new ZipEntry(listfiles[i]));
                    int j;
                    while((j = bufReader.read()) != -1) {
                        zos.write(j);
                    }
                    bufReader.close();
                }
            }
            zos.close();
            fos.close();
            checkoutput.close();

        } catch (FileNotFoundException e) {
            log.add("Read the "+ fileName + " file: Exception during test execution");
            log.add(e);
            return false;

        } catch (IOException ee) {
            log.add("Compress the "+ filenamezip + ": Exception during test execution");
            log.add(ee);
            return false;
        } finally {
            try {
                fos.close();
                zos.close();
                checkoutput.close();
            } catch (Exception e) {
                log.add(e);
            }
        }

        return true;
    }

    /* uncompress files from zip file*/ 
    protected boolean unZipFile(String directory, String filename) {

        CheckedInputStream checkinput = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        ZipInputStream zis = null;
        ZipEntry zipentry;
        String zipentryfile = "";

        try {
            fis = new FileInputStream(directory + separator + filename);
        } catch (FileNotFoundException e) {
            log.add("Read the content of the file" + filename + ". The file does not exist");
            log.add(e);
            return false;
        }
        checkinput = new CheckedInputStream(fis, new CRC32());
        zis = new ZipInputStream(new BufferedInputStream(checkinput));


        try {
            while((zipentry = zis.getNextEntry()) != null) {
                int j;
                zipentryfile = zipentry.toString();
                fos = new FileOutputStream(directory + separator + zipentryfile);
                while((j = zis.read()) != -1) {
                    fos.write(j);
                }
                fos.close();
            }
            zis.close();    
            fis.close();
            checkinput.close();

        } catch (FileNotFoundException e) {
            log.add("The file " + zipentryfile + "does not exist " + " file: Exception during test execution");
            log.add(e);
            return false;
        } catch (IOException ee) {
            log.add("Unzip the " + filename + ": Exception during test execution");
            log.add(ee);
            return false;
        } finally {

            try {
                zis.close();
                fis.close();
                checkinput.close();
            } catch (Exception e) {
                log.add(e);
            }
        }

        return true;
    }

    /* uncompress files from list of zip, jar files  */ 
    protected boolean unZipListFiles(String directory, String filename, String filext, String[] listfiles, String outfilename) {

        CheckedInputStream checkinput = null;
        FileInputStream fis = null;
        FileOutputStream fos;
        ZipInputStream zis = null;
        JarInputStream jis = null;
        ZipEntry zipentry;
        String zipFileName = "";
        String zipentryfile;
        int zip_entry_file = 0;
        int j;


        for(int i = 0; i < listfiles.length; i++) {
            zipFileName = listfiles[i];
            if(zipFileName.startsWith(filename) && zipFileName.endsWith(filext)) {
                try {
                    fis = new FileInputStream(directory + separator + zipFileName);
                } catch (FileNotFoundException e) {
                    log.add("The file " + zipFileName + "does not exist " + " : Exception during test execution");
                    log.add(e);
                    return false;
                }

                checkinput = new CheckedInputStream(fis, new CRC32());

                if(filext.endsWith("zip")) {
                    zis = new ZipInputStream(new BufferedInputStream(checkinput));

                    try {
                        while((zipentry = zis.getNextEntry()) != null) {
                            j = 0;
                            zipentryfile = zipentry.toString();
                            fos = new FileOutputStream(directory + separator + outfilename + zip_entry_file);
                            while((j = zis.read()) != -1) {
                                fos.write(j);
                            }
                            zip_entry_file++;
                            fos.close();
                        }
                        zis.close();    
                        fis.close();
                        checkinput.close();

                    } catch (FileNotFoundException e) {
                        log.add("The file " + zip_entry_file + "does not exist " + " : Exception during test execution");
                        log.add(e);
                        return false;
                    } catch (IOException ee) {
                        log.add("Reads the next ZIP file entry : Exception during test execution");
                        log.add(ee);
                        return false;
                    } finally {

                        try {
                            zis.close();    
                            fis.close();
                            checkinput.close();
                        } catch (Exception e) {
                            log.add(e);
                        }
                    }
                } else if (filext.endsWith("jar")) {
                    try {
                        jis = new JarInputStream(new BufferedInputStream(checkinput));
                    } catch (IOException e) {
                        log.add("Creates a new JarInputStream: I/O error has occurred");
                        log.add(e);
                        return false;
                    }

                    try {

                        while((zipentry = jis.getNextEntry()) != null) {
                            j = 0;
                            zipentryfile = zipentry.toString();
                            fos = new FileOutputStream(new File(directory + separator + zipentryfile));
                            while((j = jis.read()) != -1) {
                                fos.write(j);
                            }
                            fos.close();
                        }
                        fis.close();
                        jis.close();    
                        checkinput.close();

                    } catch (FileNotFoundException e) {
                        log.add("The file " + zip_entry_file + "does not exist " + " : Exception during test execution");
                        log.add(e);
                        return false;
                    } catch (IOException ee) {
                        log.add("Reads the next ZIP file entry : Exception during test execution");
                        log.add(ee);
                        return false;
                    } finally {
                        try {
                            fis.close();
                            jis.close();    
                            checkinput.close();
                        } catch (Exception e) {
                            log.add(e);
                        }
                    }
                } else {
                    log.add("Incorrect extension of file");
                    return false;
                }
            }
        }
        return true;
    }

    /* rename files if this name starts and ends with the specified prefix */
    protected boolean renameFiles(String startsWithorendsWith, String newfilename, File[] listFiles) {
        String file_name;
        int idx = 0;
        for(int i = 0; i < listFiles.length; i++) {
            file_name = listFiles[i].getName();        
            if(file_name.startsWith(startsWithorendsWith) || file_name.endsWith(startsWithorendsWith)) {
                listFiles[i].renameTo(new File(filedir + separator + newfilename + idx));
                idx++;
            }
        }
        return true;
    }

    /* rename files if this name starts with the specified prefix and ends with the other specified prefix */
    protected boolean renameFiles(String startsWith, String endsWith, String newfilename, File[] listFiles) {
        String file_name;
        int idx = 0;
        for(int i = 0; i < listFiles.length; i++) {
            file_name = listFiles[i].getName();        
            if (file_name.startsWith(startsWith) && file_name.endsWith(endsWith)) {
                listFiles[i].renameTo(new File(filedir + separator + newfilename + idx));
                idx++;
            }
        }
        return true;
    }

    /* compare file contents */
    protected boolean compareFilesContent(File directory, String pathtogolden, String filename) {

        String file_name;
        RandomAccessFile rf = null;
        File[] listFiles = directory.listFiles();
        byte[] golden_file;
        byte[] checks_file;
        int file_length;
        try {
            rf = new RandomAccessFile(pathtogolden, "r");
            file_length = (int)rf.length();
            golden_file = new byte[file_length];
            checks_file = new byte[file_length];
            rf.readFully(golden_file);
            rf.close();

        } catch (FileNotFoundException e) {
            log.add("The " + pathtogolden + " file does not exist: Exception during test execution");
            log.add(e);
            return false;

        } catch (IOException ee) {
            log.add("Reads from file "  + pathtogolden +  " into the byte array: Exception during test execution ");
            log.add(ee);
            return false;
        } finally {

            try {
                rf.close();
            } catch (Exception e) {
                log.add(e);
            }
        }

        for(int i = 0; i < listFiles.length; i++) {
            file_name = listFiles[i].getName();        
            if(file_name.startsWith(filename)) {
                try {

                    rf =new RandomAccessFile(listFiles[i], "r");
                    rf.readFully(checks_file);
                    rf.close();
                } catch (FileNotFoundException e) {
                    log.add("The " + listFiles[i] + " file does not exist: Exception during test execution");
                    log.add(e);
                    return false;

                } catch (IOException ee) {
                    log.add("Reads from file"  + listFiles[i] +  "into the byte array: Exception during test execution");
                    log.add(ee);
                    return false;
                } finally {

                    try {
                        rf.close();
                    } catch (Exception e) {
                        log.add(e);
                    }
                }
 
                if(!java.util.Arrays.equals(golden_file, checks_file)) {
                    log.add("Checks the" + file_name + ":  failed");
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.exit(new IOTest().test(args));
    }
} 
