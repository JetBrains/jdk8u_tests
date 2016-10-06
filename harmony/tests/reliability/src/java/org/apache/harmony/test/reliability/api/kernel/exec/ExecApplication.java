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
 * @version $Revision: 1.2 $
 */
package org.apache.harmony.test.reliability.api.kernel.exec;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

class ExecApplication extends Thread {

    private static int id = 10;

    private String firstarg = "StartaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaEND";

    private static String secondarg = "10";

    private static String[] appname = { "org.apache.harmony.test.reliability.api.kernel.exec.ShutdownHookApp" };

    public static String classrootdir = "";

    public static String testedruntime = "java";

    private final static String filein = "inputstreamfile";

    private final static String fileerr = "errorstreamfile";

    private final static String currthread = "Current Thread : Thread-";

    String separator = File.separator;

    public Process process = null;

    public ExecApplication(String appname) {
        Runtime runtime = Runtime.getRuntime();
        String twoarg = firstarg + " " + secondarg;
        String str = cmdArg(appname, twoarg);
        try {
            process = runtime.exec(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    public static int getCnt() {
        return id;
    }

    public static void setId(int newid) {
        id = newid;
    }

    public static void setSecondarg(String arg) {
        secondarg = arg;
    }

    public String getFirstArg() {
        return firstarg;
    }

    public String getSecondArg() {
        return secondarg;
    }

    public static String getAppName(int i) {
        return appname[i];
    }

    public static String fileNameforIn() {
        return filein;
    }

    public static String fileNameforErr() {
        return fileerr;
    }

    public static String currThreadStr() {
        return currthread;
    }

    public void processDestroy() {
        this.process.destroy();
    }

    public String cmdArg(String appname, String arg) {
        String str = testedruntime + " " + "-cp" + " " + classrootdir + " " + appname + " " + arg;

        return str;
    }

    public int processWaitFor(Process process) {
        int exitvalue = 0;
        try {
            exitvalue = process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return exitvalue;
    }

    public InputStream processInputStream(Process process) {
        InputStream inputstream = process.getInputStream();
        return inputstream;
    }

    public InputStream processErrorStream(Process process) {
        InputStream errorstream = process.getErrorStream();
        return errorstream;
    }
    
    public OutputStream processOutputStream(Process process) {
        OutputStream outputstream = process.getOutputStream();
        return outputstream;
    }

    public void readInputStream(InputStream in, String filename) {
        String line;
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(in));
        PrintWriter prnWriter = null;

        try {
            prnWriter = new PrintWriter(new BufferedWriter(new FileWriter(
                filename)));
        } catch (IOException e) {
            RunExec.log.add("Failed to create PrintWriter, file name: " + filename);
            return;
        }

        try {
            while ((line = bufReader.readLine()) != null) {
                prnWriter.println(line);
            }

        } catch (IOException e) {
        	RunExec.log.add("Failed print into file: " + filename);
        } finally {
            try {
                prnWriter.close();
                bufReader.close();
                in.close();
            } catch (IOException e) {
                //e.printStackTrace();
            	// Expected 
            }
        }
    }

    public void writeToFileInputStream(String filename, Process process) {
        InputStream in = processInputStream(process);
        readInputStream(in, filename);
    }

    public void writeToFileErrorStream(String filename, Process process) {
        InputStream error = processErrorStream(process);
        readInputStream(error, filename);
    }

    public String readLineFromFile(String filename, int linenumber) {
        String str = new String();
        BufferedReader bufReader = null;

        try {
            bufReader = new BufferedReader(new FileReader(filename));
            for (int i = 0; i < linenumber; i++) {
                str = bufReader.readLine();
                if (str == null) {
                    // System.out.println("end of the stream has been reached ");
                    return str;
                }
            }
        } catch (FileNotFoundException e) {
            RunExec.log.add("Read the content of the file" + filename + ". The file does not exist: " + e.getMessage());
            //e.printStackTrace();
        } catch (IOException ee) {
        	RunExec.log.add("Read a line of text: I/O error occurs: " + ee.getMessage());
            //ee.printStackTrace();
        } finally {
            try {
                bufReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;

    }

    public boolean checkLine(String line1, String line2) {
        return line1.equals(line2);
    }

    public boolean checkSomeLine(String filename, String chekedline, int cntline) {
        int strcounter = 0;
        String line;
        boolean result = false;
        while (readLineFromFile(filename, strcounter++) != null) {
            line = readLineFromFile(filename, strcounter);
            if (line != null) {
                if (checkLine(line, chekedline)) {
                    return result = true;
                }
            }
        }
        if (!result) {
        	RunExec.log.add("Line is not found in the file " + filename
                + ". Number of lines = " + strcounter);
        	return result;
        }
        strcounter = strcounter - 2;
        if (strcounter != cntline) {
        	RunExec.log.add("Invalid number of lines in the file "
                + filename + ". Number of lines = " + strcounter);
            result = false;
        }
        return result;
    }

    public boolean checkSomeLine2(String filename, String linestartswith, int cntline) {
        int strcounter = 0;
        String line;
        boolean result = false;
        while (readLineFromFile(filename, strcounter++) != null) {
            line = readLineFromFile(filename, strcounter);
            if (line != null) {
                for (int i = 0; i <= cntline; i++) {
                    if (checkLine(line, linestartswith, "")) {
                        result = true;
                        break;
                    }
                }

            }
        }
        if (!result) {
        	RunExec.log.add("Invalid number of lines in the file "
                + filename + ". Number of lines = " + strcounter);
        }
        return result;
    }

    public boolean checkLine(String line, String startwith, String endwith) {
        boolean result = false;
        if (line.startsWith(startwith) && line.endsWith(endwith)) {
            result = true;
        }
        return result;
    }

    public static boolean deleteFiles(String directory, String filename) {
        boolean result = true;
        File dir = new File(directory);
        File[] listFiles = dir.listFiles();
        String fln;
        for (int i = 0; i < listFiles.length; i++) {
            fln = listFiles[i].getName();
            if (fln.equals(filename)) {
                if (!listFiles[i].delete()) {
                	RunExec.log.add("delete " + fln + " : failed");
                    return false;
                }

            }
        }
        return result;
    }

    int exitvalue = 0;

    public void run() {
        if (process != null) {
            exitvalue = processWaitFor(process);
            if (exitvalue != 104) {
            	RunExec.log.add("the exit value = " + exitvalue
                    + " The exit value of the process should be 104.");
            }
        }
    }
}

