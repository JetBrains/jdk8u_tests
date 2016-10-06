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
 * @version $Revision: 1.9 $
 */
package org.apache.harmony.harness.ReportTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import org.apache.harmony.harness.DefaultConfigSetup;
import org.apache.harmony.harness.Finder;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.TestIR;
import org.apache.harmony.harness.Util;
import org.apache.harmony.harness.InternalTHLogger;

public class Stat extends Main {

    String   basedir    = ".";
    HashMap  runnersMap = new HashMap();
    HashMap  authorsMap = new HashMap();
    int      logl       = 2;
    int      tc_public;
    int      tc_protected;
    int      tc_private;
    int      tc_package;
    String[] subs;
    String   fileName   = "out", fileExt = "xml", auxFileExt = "xsl";
    boolean  needFile   = false;

    protected boolean parseParam(String[] params) {
        String helpMsg = "Collect the simple statistic for test suite. Each test count as 1 public test case (except junit and multicase tests)\n"
            + "Usage:\n"
            + "-cfp\t\tpath to suite configuration\n"
            + "-subs name1,name2\tsubsuite to collect statistics\n"
            + "-logl 0-2\tlogging level (0 means print all)\n"
            + "-out <file_name> - name of the file to store data";
        try {
            for (int i = 0; i < params.length; i++) {
                if ("-basedir".equalsIgnoreCase(params[i])) {
                    basedir = params[++i];
                } else if ("-cfp".equalsIgnoreCase(params[i])) {
                    cfgStorePath = params[++i];
                } else if ("-out".equalsIgnoreCase(params[i])) {
                    fileName = params[++i];
                    needFile = true;
                } else if ("-subs".equalsIgnoreCase(params[i])) {
                    subs = Util.stringToArray(params[++i]);
                } else if ("-logl".equalsIgnoreCase(params[i])) {
                    try {
                        logl = Integer.parseInt(params[++i]);
                        if (logl < 0 || logl > 2) {
                            logl = 2;
                        }
                    } catch (Exception e) {
                        logMsg("Can't parse log level option. Use default value: "
                            + logl);
                    }
                } else if ("-h".equalsIgnoreCase(params[i])
                    || "-help".equalsIgnoreCase(params[i])) {
                    logMsg(helpMsg);
                    System.exit(0);
                } else {
                    super.parseParam(Util.removeFirstElem(params, i));
                    break;
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            logMsg("Incorrect parameters. Please check.\n" + helpMsg);
            return false;
        }
        return true;
    }

    public void logMsg(String data) {
        logMsg(0, data);
    }

    public void logMsg(int level, String data) {
        if (level == 0 || level >= logl) {
            System.err.println(data);
        }
    }

    public void logData(String data) {
        System.out.println(data);
    }

    int defTest(Class cl, String retType) {
        if (cl == null) {
            return 0;
        }
        try {
            Method[] m = cl.getDeclaredMethods();
            int retVal = 0;
            for (int i = 0; i < m.length; i++) {
                if (m[i].getName().startsWith("test")
                    && m[i].getReturnType().getName().equals(retType)) {
                    retVal++;
                    int modif = m[i].getModifiers();
                    if (Modifier.isPublic(modif)) {
                        tc_public++;
                    } else if (Modifier.isProtected(modif)) {
                        tc_protected++;
                    } else if (Modifier.isPrivate(modif)) {
                        tc_private++;
                        logMsg(2, "Methods that looks like TC is private: "
                            + m[i]);
                    } else {
                        tc_package++;
                        logMsg(2,
                            "Methods that looks like TC has package access: "
                                + m[i]);
                    }
                }
            }
            return retVal;
        } catch (Throwable e) {
            logMsg("Unexpected exception: " + e);
            return 0;
        }
    }

    public int run(String[] args) {
        try {
            curCore = this;
            parseParam(args);
            internalLog = new InternalTHLogger();
            internalLog.init(Level.SEVERE);
            createConfigurator();
            cfg.createConfiguration(cfgStorePath);
            setCfgFromCmdline();
            new DefaultConfigSetup().setDefaultValues();
            checkAndUpdateConfig();
            createTestFinder();
            Finder tf = curCore.getTestFinder();
            float suiteTimeFactor = 0.0f;
            float minFactorValue = Float.MAX_VALUE;
            float maxFactorValue = Float.MIN_VALUE;
            float curFactor = 0;
            int totalTests = 0;
            int cycleCnt = 0;
            int tc = 0;
            if (subs == null) {
                subs = new String[1];
                subs[0] = "";
            }
            for (int ci = 0; ci < subs.length; ci++) {
                if (subs[ci] != null && subs[ci].length() > 0) {
                    cycleCnt = tf.find(subs[ci]);
                } else {
                    cycleCnt = tf.find();
                }
                for (int i = 0; i < cycleCnt; i++) {
                    TestIR test = (TestIR)tf.getNext();
                    if (test == null || test.getRunnerID() == null) {
                        if (test == null) {
                            logMsg(1, "incorrect null object into test list");
                        } else {
                            logMsg(1,
                                "incorrect object into test list. Test name: "
                                    + test.getTestID() + ", runner model: "
                                    + test.getRunnerID());
                        }
                    } else {
                        totalTests++;
                        curFactor = test.getTestTimeout();
                        suiteTimeFactor = suiteTimeFactor + curFactor;
                        minFactorValue = (minFactorValue < curFactor)
                            ? minFactorValue : curFactor;
                        maxFactorValue = (maxFactorValue > curFactor)
                            ? maxFactorValue : curFactor;
                        String author = "";
                        if (test.getAuthors() != null
                            && test.getAuthors().size() > 0) {
                            int cnt = 0;
                            for (; cnt < test.getAuthors().size() - 1; cnt++) {
                                author = author + test.getAuthors().get(cnt)
                                    + ", ";
                            }
                            author = author + test.getAuthors().get(cnt);
                        }

                        if (!authorsMap.containsKey(author)) {
                            authorsMap.put(author, new Vector());
                        }
                        ((Vector)authorsMap.get(author)).add(test.getTestID());
                        if (runnersMap.containsKey(test.getRunnerID())) {
                            int cnt = ((Integer)(runnersMap.get(test
                                .getRunnerID()))).intValue() + 1;
                            runnersMap
                                .put(test.getRunnerID(), new Integer(cnt));
                        } else {
                            runnersMap.put(test.getRunnerID(), new Integer(1));
                        }
                        if (!test.getRunnerID().equalsIgnoreCase("JUExec")
                            && !test.getRunnerID()
                                .equalsIgnoreCase("MultiCase")) {
                            tc++;
                            tc_public++;
                            logMsg(1, "For test " + test.getTestID()
                                + " define testcases: 1");
                        } else if (test.getRunnerID()
                            .equalsIgnoreCase("JUExec")) {
                            if (test.getRunnerParam() != null
                                && ((ArrayList)test.getRunnerParam()).size() > 1
                                && ((ArrayList)test.getRunnerParam().get(1)) != null
                                && ((ArrayList)test.getRunnerParam().get(1))
                                    .size() > 1) {
                                try {
                                    String className = ((ArrayList)((ArrayList)test
                                        .getRunnerParam().get(1))).get(1)
                                        .toString();
                                    Class cl = Class.forName(className, false,
                                        this.getClass().getClassLoader());
                                    int curCnt = defTest(cl, "void");
                                    tc = tc + curCnt;
                                    logMsg(1, "For test " + test.getTestID()
                                        + " define testcases: " + curCnt);
                                    if (tc != (tc_public + tc_protected
                                        + tc_private + tc_package)) {
                                        logMsg(2,
                                            "Unexpected counter value: test "
                                                + test.getTestID()
                                                + " define testcases: "
                                                + curCnt);
                                        logMsg(2, "tc = " + tc
                                            + ", tc_public = " + tc_public
                                            + ", tc_protected = "
                                            + tc_protected + ", tc_private = "
                                            + tc_private + ", tc_package = "
                                            + tc_package);
                                        System.exit(0);
                                    }
                                } catch (Throwable e) {
                                    logMsg(1, "Unexpected exception " + e);
                                    //e.printStackTrace(System.out);
                                }
                            }
                        } else {
                            if (test.getRunnerParam() != null
                                && ((ArrayList)test.getRunnerParam()).size() > 1
                                && ((ArrayList)test.getRunnerParam().get(1)) != null
                                && ((ArrayList)test.getRunnerParam().get(1))
                                    .size() > 1) {
                                String className = ((ArrayList)((ArrayList)test
                                    .getRunnerParam().get(1))).get(1)
                                    .toString();
                                try {
                                    Class cl = Class.forName(className, false,
                                        this.getClass().getClassLoader());
                                    int curCnt = defTest(cl,
                                        "org.apache.harmony.share.Result");
                                    tc = tc + curCnt;
                                    logMsg(1, "For test " + test.getTestID()
                                        + " define testcases: " + curCnt);
                                    if (tc != (tc_public + tc_protected
                                        + tc_private + tc_package)) {
                                        logMsg(2,
                                            "Unexpected counter value: test "
                                                + test.getTestID()
                                                + " define testcases: "
                                                + curCnt);
                                        logMsg(2, "tc = " + tc
                                            + ", tc_public = " + tc_public
                                            + ", tc_protected = "
                                            + tc_protected + ", tc_private = "
                                            + tc_private + ", tc_package = "
                                            + tc_package);
                                        System.exit(0);
                                    }
                                } catch (Exception e) {
                                    logMsg("Unexpected exception: " + e);
                                }
                            }
                        }
                    }
                }
            }
            logData("Total tests: " + totalTests);
            logData("Total test cases: " + tc + " (public: " + tc_public
                + ", protected: " + tc_protected + ", private: " + tc_private
                + ", package access: " + tc_package + ")");
            logData("Suite time factor: " + suiteTimeFactor + " ("
                + suiteTimeFactor * cfgM.getGenTimeout()
                + " seconds for current configuration)");
            logData("minimal test timeout factor: " + minFactorValue
                + ", maximum: " + maxFactorValue);
            Iterator it = runnersMap.keySet().iterator();
            int cnt = runnersMap.size();
            for (int i = 0; i < cnt; i++) {
                Object obj = it.next();
                logData("Test with execution model '" + obj + "':\t"
                    + runnersMap.get(obj));
            }
            if (needFile) {
                writeXSL(fileName);
                writeXML(fileName, totalTests, tc_public, tc_protected,
                    tc_private, tc_package, suiteTimeFactor, minFactorValue,
                    maxFactorValue, runnersMap, authorsMap);
            }
            return 0;
        } catch (Exception e) {
            logMsg("Unexpected exception: " + e);
            e.printStackTrace(System.out);
            return -1;
        }
    }

    protected boolean writeXSL(String file_name) {
        if (file_name != null && auxFileExt != null && auxFileExt.length() > 0) {
            FileWriter fw = null;
            try {
                fw = new FileWriter(new File(file_name + "." + auxFileExt));
                fw
                    .write("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
                        + "\t<xsl:template match=\"/\">\n"
                        + "\t\t<h1>Statistic report</h1>\n"
                        + "\t\t<xsl:apply-templates/>\n"
                        + "\t</xsl:template>\n"
                        + "\t<xsl:template match=\"stat-data\">\n"
                        + "\t\t<p><table>\n"
                        + "\t\t<tr bgcolor=\"#CCCCCC\">\n"
                        + "\t\t<th colspan=\"2\" align=\"center\">Results</th></tr>\n"
                        + "\t\t<xsl:for-each select=\"stat-item\">\n"
                        + "\t\t\t<tr><td valign=\"top\"><strong><xsl:value-of select=\"./@name\"/></strong></td>\n"
                        + "\t\t\t<td>\n"
                        + "\t\t\t\t<xsl:value-of select=\".\"/>\n"
                        + "\t\t\t</td></tr>\n"
                        + "\t\t</xsl:for-each>\n"
                        + "\t\t</table></p>\n"
                        + "\t<xsl:apply-templates select='authors'/>\n"
                        + "</xsl:template>\n"
                        + "\t<xsl:template match='authors'>\n"
                        + "\t<html>\n"
                        + "\t<title>The 'authors' report</title>\n"
                        + "\t<font face='Arial'>\n"
                        + "\t<center>\n"
                        + "\t\t\t<h1>\n"
                        + "\t\t\t<xsl:text>\n"
                        + "\t\t\tAuthors report for\n"
                        + "\t\t\t</xsl:text>\n"
                        + "\t\t\t<br/>\n"
                        + "\t\t\t<xsl:value-of select='@date'/>\n"
                        + "\t\t</h1>\n"
                        + "\t\t<xsl:text>\n"
                        + "\t\t\tthis report shows how many test cases each engineer wrote in\n"
                        + "\t\t\thow many tests and how many methods these test cases cover\n"
                        + "\t\t\taccording to the information from tests' descriptions\n"
                        + "\t\t</xsl:text>\n"
                        + "\t\t</center>\n"
                        + "\t\t</font>\n"
                        + "\t\t<font face='courier new'>\n"
                        + "\t\t<P>\n"
                        + "\t\t\t<table border='1' align='center'>\n"
                        + "\t\t<tr>\n"
                        + "\t\t\t<td align='center'><b><xsl:text>Author name</xsl:text></b></td>\n"
                        + "\t\t\t<td align='center'><b><xsl:text>Tests written</xsl:text></b></td>\n"
                        + "\t\t</tr>\n"
                        + "\t\t<xsl:apply-templates select='author'/>\n"
                        + "\t\t</table>\n"
                        + "\t\t\t</P>\n"
                        + "\t\t\t\t<ul>\n"
                        + "\t\t<xsl:apply-templates select='author' mode='filenames'/>\n"
                        + "\t</ul>\n"
                        + "\t</font>\n"
                        + "\t</html>\n"
                        + "\t</xsl:template>\n"
                        + "\t<xsl:template match='file'>\n"
                        + "\t<li><xsl:value-of select='.'/></li>\n"
                        + "\t</xsl:template>\n"
                        + "\t<xsl:template match='author' mode='filenames'>\n"
                        + "\t\t<li>\n"
                        + "\t\t<b><xsl:value-of select='@name'/></b>\n"
                        + "\t\t<ul><xsl:apply-templates select='file'/></ul>\n"
                        + "\t</li>\n"
                        + "\t</xsl:template>\n"
                        + "\t\t<xsl:template match='author'>\n"
                        + "\t\t<tr>\n"
                        + "\t\t\t<td><xsl:value-of select='@name'/></td>\n"
                        + "\t\t\t<td align='center'><xsl:value-of select='@tests'/></td>\n"
                        + "\t\t</tr>\n"
                        + "\t\t</xsl:template>\n"
                        + "</xsl:stylesheet>\n");
                return true;
            } catch (Exception e) {
                //do noting
            } finally {
                if (fw != null) {
                    try {
                        fw.flush();
                        fw.close();
                    } catch (IOException e1) {
                        //do nothing
                    }
                }
            }
        }
        return false;
    }

    protected boolean writeXML(String file_name, int totalTests, int tc_public,
        int tc_protected, int tc_private, int tc_package,
        float suiteTimeFactor, float minFactorValue, float maxFactorValue,
        HashMap runnersMap, HashMap authorsMap) {
        if (file_name != null && auxFileExt != null) {
            FileWriter fw = null;
            try {
                Date date = new Date();
                fw = new FileWriter(new File(file_name + "." + fileExt));
                fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\"" + file_name
                    + "." + auxFileExt + "\"?>\n" + "<stat-data>\n"
                    + "\t<stat-item name=\"Total tests number\" date=\"" + date
                    + "\">" + totalTests + "</stat-item>\n"
                    + "\t<stat-item name=\"testcases with public access\">"
                    + tc_public + "</stat-item>\n"
                    + "\t<stat-item name=\"testcases with protected access\">"
                    + tc_protected + "</stat-item>\n"
                    + "\t<stat-item name=\"testcases with private access\">"
                    + tc_private + "</stat-item>\n"
                    + "\t<stat-item name=\"testcases with package access\">"
                    + tc_package + "</stat-item>\n"
                    + "\t<stat-item name=\"suite time factor\">"
                    + suiteTimeFactor + "</stat-item>\n"
                    + "\t<stat-item name=\"min_time\">" + minFactorValue
                    + "</stat-item>\n" + "\t<stat-item name=\"max_time\">"
                    + maxFactorValue + "</stat-item>\n");
                fw.write("\t<authors date=\"" + date + "\">\n");
                Iterator it = authorsMap.keySet().iterator();
                while (it.hasNext()) {
                    String author = it.next().toString();
                    Vector testsList = (Vector)authorsMap.get(author);
                    fw.write("\t\t<author name=\"" + author + "\" tests=\""
                        + testsList.size() + "\">\n");
                    for (int i = 0; i < testsList.size(); i++) {
                        fw.write("\t\t\t<file>" + testsList.get(i)
                            + "</file>\n");
                    }
                    fw.write("\t\t</author>\n");
                }
                fw.write("\t</authors>\n");
                fw.write("</stat-data>\n");
            } catch (Exception e) {
                //do noting
            } finally {
                if (fw != null) {
                    try {
                        fw.flush();
                        fw.close();
                    } catch (IOException e1) {
                        //do nothing
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.exit(new Stat().run(args));
    }
}