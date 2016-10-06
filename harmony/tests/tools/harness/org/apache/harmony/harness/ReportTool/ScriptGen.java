/*
    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and
    limitations under the License.
*/
/**
 * @author Vladimir A. Ivanov
 * @version $Revision: 1.14 $
 */
package org.apache.harmony.harness.ReportTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.apache.harmony.harness.DefaultConfigSetup;
import org.apache.harmony.harness.ExecUnit;
import org.apache.harmony.harness.Finder;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.TResIR;
import org.apache.harmony.harness.Util;
import org.apache.harmony.harness.InternalTHLogger;

public class ScriptGen extends Main {

    public static final String ALL_SCRIPTS      = "all";

    public static final String SH_ONLY          = "sh";

    public static final String BAT_ONLY         = "bat";

    public static final String PATH_ROOT_MARKER = "qwe#zxc";

    // when reportOnly == true, no test run. Generate report only.
    boolean                    reportOnly       = false;

    protected Level            logLevel         = Level.INFO;
    protected String           scriptMode       = ALL_SCRIPTS;
    protected char             pathSeparator;
    protected String           pathRoot;
    protected String           testRunOS;

    Finder                     resf;

    protected boolean parseParam(String[] params) {
        String helpMsg = "Generate the run scripts for failed test.\nUsage:\n"
            + "-cfp <path>\t\t\tpath to the configuration files\n"
            + "-format \"name\"\t\tname of the format (bat or sh formats, by default both)\n"
            + "Also options of harness are supported";
        try {
            for (int i = 0; i < params.length; i++) {
                if ("-format".equalsIgnoreCase(params[i])) {
                    scriptMode = params[++i];
                } else if ("-cfp".equalsIgnoreCase(params[i])) {
                    cfgStorePath = params[++i];
                } else if ("-help".equalsIgnoreCase(params[i])
                    || "-h".equalsIgnoreCase(params[i])) {
                    System.out.println(helpMsg);
                    System.exit(0);
                } else {
                    super.parseParam(Util.removeFirstElem(params, i));
                    break;
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            System.out.println("Incorrect parameters. Please check.\n"
                + helpMsg);
            System.exit(0);
        }
        return true;
    }

    void generateScripts() {
        try {
            createConfigurator();
            cfg.createConfiguration(cfgStorePath);
            setCfgFromCmdline();
            new DefaultConfigSetup().setDefaultValues();
            checkAndUpdateConfig();
            createResFinder();
            if (cfgM.getExecM() == SAME) { //to receive class path and so on
                createLog();
                createRunners();
            }
            if (subSuites != null && subSuites.length > 0) {
                for (int i = 0; i < subSuites.length; i++) {
                    mkScriptsByResults(rf.find(subSuites[i]));
                }
            } else {
                mkScriptsByResults(rf.find());
            }
            System.out
                .println("done. See the scripts in the directory tree\n\t"
                    + cfgM.getTestResultRoot());
        } catch (Throwable e) {
            System.out.println("Unexpected exception: " + e);
            e.printStackTrace();
        }
    }

    boolean mkScriptsByResults(int num) {
        boolean retVal = false;
        for (int cnt = 0; cnt < num; cnt++) {
            retVal = mkScritByResult((TResIR)rf.getNext());
        }
        return retVal;
    }

    /*
     * generate script to run the test
     */
    boolean mkScritByResult(TResIR tRes) {
        try {
            String testID = tRes.getTestID();
            int testRes = tRes.getExecStat();
            int[] values = cfgM.getRepPassed();
            for (int i = 0; i < values.length; i++) {
                if (testRes == values[i]) {
                    internalLog.add(Level.INFO, "ScriptGen.\tThe test "
                        + testID + " is ignored due to status (passed) "
                        + testRes);
                    return true; //no scripts required
                }
            }
            values = cfgM.getRepModErr();
            for (int i = 0; i < values.length; i++) {
                if (testRes == values[i]) {
                    internalLog.add(Level.INFO, "ScriptGen.\tThe test "
                        + testID + " is ignored due to status (skipped) "
                        + testRes);
                    return true; //no scripts required
                }
            }
            //remove the invalid entry or entry that have no the test ID
            if (testID != null) {
                internalLog.add(Level.INFO,
                    "ScriptGen.\tgenerate script(s) for the test " + testID);
                String comment = "";
                String runClass = "";
                testRunOS = tRes.getTestedOS();
                String[][] cmd = null;
                if (testRunOS.indexOf("Windows") != -1) {
                    pathSeparator = ';';
                } else {
                    pathSeparator = ':';
                }
                int cmdCnt = 1;
                // all parameters in the report file
                if (cfgM.getExecM() == OTHER) {
                    comment = "According to configuration, test was run in other VM mode";
                    String[] args = tRes.getExecCmd();
                    runClass = args[0];
                    for (int i = 1; i < args.length; i++) {
                        if (runClass.equals(args[i])) {
                            cmdCnt++;
                        }
                    }
                    cmd = new String[cmdCnt][];
                    int cnt = 1;
                    int curArgPtr = 0;
                    for (int i = 0; i < cmd.length; i++) {
                        int curSize = 0;
                        curArgPtr++; //miss the runClass name
                        for (int y = cnt; y < args.length; y++) {
                            if (!runClass.equals(args[y])) {
                                curSize++;
                            } else {
                                break;
                            }
                        }
                        cmd[i] = new String[curSize];
                        for (int y = cnt; y < cnt + curSize; y++) {
                            cmd[i][y - cnt] = args[curArgPtr++];
                        }
                        cnt = cnt + curSize + 1;
                        pathRoot = findAndReplacePathRoot(cmd[i]);
                        replacePathSeparator(cmd[i], pathSeparator);
                    }
                    writeFiles(testID, comment, runClass, pathRoot, cmd);
                    // prepare parameters according to configuration
                } else {
                    comment = "According to configuration, test was run in same VM mode. The script may be inaccurate";
                    String[] genVm = new String[0];
                    String[] secOpt = new String[0];
                    String[] bcpOpt = new String[0];
                    String[] cpOpt = new String[0];
                    String[] testOpt = tRes.getExecCmd();
                    String[] optionToVM;
                    String cp = null;
                    String bcp = null;
                    ExecUnit eunit = getRunner("Runtime");
                    int cnt;
                    if (eunit != null) {
                        cp = eunit.getCPOptions();
                        bcp = eunit.getBCPOptions();
                        genVm = eunit.getSecurityOptions();
                        secOpt = eunit.getGeneralVMOptions();
                    }
                    if (bcp != null) {
                        bcpOpt = new String[1];
                        bcpOpt[0] = bcp;
                    }
                    if (cp != null) {
                        cpOpt = new String[2];
                        cpOpt[0] = "-cp";
                        cpOpt[1] = cp;
                    }
                    for (int i = 0; i < genVm.length; i++) {
                        if (genVm[i].equalsIgnoreCase("-cp")
                            || genVm[i].equalsIgnoreCase("-classpath")) {
                            cpOpt = new String[0];
                        } else if (genVm[i]
                            .startsWith("-Djava.security.policy")) {
                            secOpt = new String[0];
                        }
                    }
                    optionToVM = new String[1 /* tested runtime */
                        + genVm.length + secOpt.length + bcpOpt.length
                        + cpOpt.length];// +
                    // testOpt.length
                    // - 1
                    // /*run
                    // class*/];
                    cnt = 0;
                    optionToVM[cnt++] = cfgM.getTestedRuntime();
                    for (int i = 0; i < genVm.length; i++) {
                        optionToVM[cnt++] = genVm[i];
                    }
                    for (int i = 0; i < secOpt.length; i++) {
                        optionToVM[cnt++] = secOpt[i];
                    }
                    for (int i = 0; i < bcpOpt.length; i++) {
                        optionToVM[cnt++] = bcpOpt[i];
                    }
                    for (int i = 0; i < cpOpt.length; i++) {
                        optionToVM[cnt++] = cpOpt[i];
                    }
                    runClass = testOpt[0];
                    for (int i = 1; i < testOpt.length; i++) {
                        if (runClass.equals(testOpt[i])) {
                            cmdCnt++;
                        }
                    }
                    cmd = new String[cmdCnt][];
                    if (cmdCnt == 1) { //one command
                        cmd[0] = new String[optionToVM.length + testOpt.length
                            - 1];
                        cnt = 0;
                        for (int i = 0; i < optionToVM.length; i++) {
                            cmd[0][cnt++] = optionToVM[i];
                        }
                        for (int i = 1; i < testOpt.length; i++) {
                            cmd[0][cnt++] = testOpt[i];
                        }
                        pathRoot = findAndReplacePathRoot(cmd[0]);
                        replacePathSeparator(cmd[0], pathSeparator);
                        writeFiles(testID, comment, runClass, pathRoot, cmd);
                    } else { //more than one command
                        int curCnt = 1;
                        int curArgPtr = 0;
                        for (int i = 0; i < cmd.length; i++) {
                            int curSize = 0;
                            curArgPtr++; //miss the runClass name
                            for (int y = curCnt; y < testOpt.length; y++) {
                                if (!runClass.equals(testOpt[y])) {
                                    curSize++;
                                } else {
                                    break;
                                }
                            }
                            cmd[i] = new String[optionToVM.length + curSize];
                            cnt = 0;
                            for (int y = 0; y < optionToVM.length; y++) {
                                cmd[i][cnt++] = optionToVM[y];
                            }
                            for (int y = curCnt; y < curCnt + curSize; y++) {
                                cmd[i][cnt++] = testOpt[curArgPtr++];
                            }
                            curCnt = curCnt + curSize + 1;
                            pathRoot = findAndReplacePathRoot(cmd[i]);
                            replacePathSeparator(cmd[i], pathSeparator);
                        }
                        writeFiles(testID, comment, runClass, pathRoot, cmd);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("mkScritByResult. Unexpected exception " + e);
        }
        return false;
    }

    protected String[] replacePathSeparator(String[] cmd, char newPathSeparator) {
        if (pathSeparator == newPathSeparator) {
            return cmd;
        }
        for (int i = 1; i < cmd.length; i++) {
            cmd[i] = cmd[i].replace(pathSeparator, newPathSeparator);
        }
        return cmd;
    }

    String myReplaceAll(String data, String from, String to) {
        String retVal = "";
        int index;
        while (data.indexOf(from) > -1) {
            index = data.indexOf(from);
            retVal = retVal + data.substring(0, index) + to;
            data = data.substring(index + from.length());
        }
        retVal = retVal + data;
        return retVal;
    }

    int minValue(int i1, int i2) {
        return (i1 < i2) ? i1 : i2;
    }

    String findAndReplacePathRoot(String[] cmd) {
        for (int i = 0; i < cmd.length; i++) {
            // it does not matter for windows but critical for unix
            cmd[i] = cmd[i].replace('\\', '/');
        }
        try {
            for (int i = 0; i < cmd.length; i++) {
                if (cmd[i].equalsIgnoreCase("-cp")
                    || cmd[i].equalsIgnoreCase("-classpath")) {
                    String tmp = cmd[i + 1]; //expect a class path here
                    StringTokenizer st;
                    String[] dirPath;
                    String[] etalonPath;
                    st = new StringTokenizer(tmp, pathSeparator + ""); //split
                    // path
                    // into
                    // directories
                    int iter = st.countTokens();
                    dirPath = new String[iter];
                    for (int cnt = 0; cnt < iter; cnt++) {
                        dirPath[cnt] = st.nextToken();
                    }
                    pathRoot = dirPath[0];
                    st = new StringTokenizer(dirPath[0], "/"); //split
                    // directories
                    // into tokens
                    int priter = st.countTokens();
                    etalonPath = new String[priter]; //fill in etalon array
                    for (int cnt = 0; cnt < etalonPath.length; cnt++) {
                        etalonPath[cnt] = st.nextToken();
                    }
                    for (int cnt = 1; cnt < dirPath.length; cnt++) {
                        st = new StringTokenizer(dirPath[cnt], "/"); //split
                        // directories
                        // into
                        // tokens
                        priter = st.countTokens();
                        priter = minValue(priter, etalonPath.length);
                        int y = 0;
                        for (; y < priter; y++) {
                            if (!etalonPath[y].equals(st.nextToken())) {
                                break;
                            }
                        }
                        pathRoot = etalonPath[0];
                        for (int z = 1; z < y; z++) {
                            pathRoot = pathRoot + "/" + etalonPath[z];
                        }
                        for (int z = y; z < etalonPath.length; z++) {
                            etalonPath[z] = "";
                        }
                    }
                    break;
                }
            }
            if (pathRoot.equals(".") || pathRoot.equals("..")) {
                pathRoot = "";
            }
        } catch (Exception e) {
            pathRoot = "";
        }
        //replace path root on the path marker
        if (pathRoot.length() > 0) {
            for (int i = 0; i < cmd.length; i++) {
                cmd[i] = myReplaceAll(cmd[i], pathRoot, PATH_ROOT_MARKER);
            }
        }
        return pathRoot;
    }

    protected void writeFiles(String testID, String comment, String runClass,
        String root, String[][] runcmd) {
        try {
            if (scriptMode.equalsIgnoreCase(ALL_SCRIPTS)) {
                String[][] cmd = new String[runcmd.length][];
                for (int i = 0; i < runcmd.length; i++) {
                    cmd[i] = (String[])runcmd[i].clone();
                }
                FileWriter out = initFile(testID + "." + SH_ONLY);
                writeShFileHead(out, comment, runClass, root);
                for (int i = 0; i < cmd.length - 1; i++) {
                    writeShFileCmd(out, " &", cmd[i]);
                }
                writeShFileCmd(out, "", cmd[cmd.length - 1]);
                writeShFileTail(out);
                closeFile(out);
                cmd = runcmd;
                out = initFile(testID + "." + BAT_ONLY);
                writeBatFileHead(out, comment, runClass, root);
                for (int i = 0; i < cmd.length - 1; i++) {
                    writeBatFileCmd(out, "start ", cmd[i]);
                }
                writeBatFileCmd(out, "", cmd[cmd.length - 1]);
                writeBatFileTail(out);
                closeFile(out);
            } else if (scriptMode.equalsIgnoreCase(SH_ONLY)) {
                FileWriter out = initFile(testID + "." + SH_ONLY);
                writeShFileHead(out, comment, runClass, root);
                for (int i = 1; i < runcmd.length - 1; i++) {
                    writeShFileCmd(out, " &", runcmd[i]);
                }
                writeShFileCmd(out, "", runcmd[runcmd.length - 1]);
                writeShFileTail(out);
                closeFile(out);
            } else {
                FileWriter out = initFile(testID + "." + BAT_ONLY);
                writeBatFileHead(out, comment, runClass, root);
                for (int i = 0; i < runcmd.length - 1; i++) {
                    writeBatFileCmd(out, "start ", runcmd[i]);
                }
                writeBatFileCmd(out, "", runcmd[runcmd.length - 1]);
                writeBatFileTail(out);
                closeFile(out);
            }
        } catch (Exception e) {
            internalLog.add(Level.WARNING, "writeFile. Unexpected exception "
                + e);
        }
    }

    protected void writeShFileHead(FileWriter out, String comments,
        String runClass, String root) {
        if (out == null) {
            return;
        }
        try {
            out.write("#!/bin/sh\n\n");
            out.write("#\t" + comments + "\n\n");
            out.write("#\tTest was run by runner class:\n#\t" + runClass); //write
            // runner
            // class
            out.write("\n\n" + "rootPath=" + root); //write env variable
        } catch (IOException e) {
            internalLog.add(Level.WARNING,
                "writeShFileHead. Unexpected exception " + e);
        }
    }

    protected void writeShFileCmd(FileWriter out, String cmdAdd, String[] cmd) {
        if (out == null || cmd == null || cmd.length < 2) {
            return;
        }
        try {
            out
                .write("\n\n"
                    + cmd[0].replaceAll(PATH_ROOT_MARKER, "$rootPath")); //write
            // java
            for (int i = 1; i < cmd.length; i++) {
                if (!cmd[i].startsWith("-")
                    && (cmd[i].indexOf(";") != -1 || cmd[i].indexOf(":") != -1)) {// classpath
                    // etc
                    cmd[i] = '"' + cmd[i] + '"';
                }
                if (cmd[i].indexOf("$") != -1) {// environment variables not
                                                // exist
                    // or parameters
                    cmd[i] = cmd[i].replaceAll("$", "\\$");
                }
                out.write(" \\\n\t"
                    + myReplaceAll(cmd[i], PATH_ROOT_MARKER, "$rootPath"));
            }
            out.write(cmdAdd);
        } catch (IOException e) {
            internalLog.add(Level.WARNING,
                "writeShFileCmd. Unexpected exception " + e);
        }
    }

    protected void writeShFileTail(FileWriter out) {
        if (out == null) {
            return;
        }
        try {
            out.write("\n\n" + "status=$?\necho \"exit status: $status\"");
        } catch (IOException e) {
            internalLog.add(Level.WARNING,
                "writeShFileTail. Unexpected exception " + e);
        }
    }

    protected void writeBatFileHead(FileWriter out, String comments,
        String runClass, String root) {
        if (out == null) {
            return;
        }
        try {
            out.write("echo off\nrem\t" + comments + "\n\n");
            out.write("rem\tTest was run by runner class:\nrem\t" + runClass); //write
            // runner
            // class
            out.write("\n\nset " + "ROOTPATH=" + root); //write env variable
        } catch (IOException e) {
            internalLog.add(Level.WARNING,
                "writeBatFileHead. Unexpected exception " + e);
        }
    }

    protected void writeBatFileCmd(FileWriter out, String cmdAdd, String[] cmd) {
        if (out == null || cmd == null || cmd.length < 2) {
            return;
        }
        try {
            out.write("\n\n" + cmdAdd
                + cmd[0].replaceAll(PATH_ROOT_MARKER, "%ROOTPATH%")); //write
            // java
            for (int i = 1; i < cmd.length; i++) {
                if (!cmd[i].startsWith("-")
                    && (cmd[i].indexOf(";") != -1 || cmd[i].indexOf(":") != -1)) {// classpath
                    // etc
                    cmd[i] = '"' + cmd[i] + '"';
                }
                out.write(" "
                    + myReplaceAll(cmd[i], PATH_ROOT_MARKER, "%ROOTPATH%"));
            }
        } catch (IOException e) {
            internalLog.add(Level.WARNING,
                "writeBatFileCmd. Unexpected exception " + e);
        }
    }

    protected void writeBatFileTail(FileWriter out) {
        if (out == null) {
            return;
        }
        try {
            out.write("\n\n" + "echo exit status: %errorLevel%\n");
        } catch (IOException e) {
            internalLog.add(Level.WARNING,
                "writeBatFileTail. Unexpected exception " + e);
        }
    }

    protected FileWriter initFile(String name) throws IOException {
        if (name == null) {
            return null;
        }
        File curFile = new File(cfgM.getTestResultRoot() + File.separator
            + name);
        FileWriter out = new FileWriter(curFile);
        return out;
    }

    protected void closeFile(FileWriter out) throws IOException {
        if (out != null) {
            out.flush();
            out.close();
        }
    }

    public int run(String[] args) {
        curCore = this;
        internalLog = new InternalTHLogger();
        internalLog.init(logLevel);
        parseParam(args);
        generateScripts();
        return 0;
    }

    public static void main(String[] args) {
        System.exit((new ScriptGen().run(args)));
    }
}