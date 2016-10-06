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
 * @version $Revision: 1.10 $
 */
package org.apache.harmony.harness.ReportTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;

import org.apache.harmony.harness.Constants;

public class ReporterSpec extends ReporterHTML {

    public static final String  INDEX_REP_NAME       = "report.index.html";
    public static final String  PACKAGE_REP_NAME     = "report.packages.html";
    public static final String  CLASS_REP_NAME       = "report.classes.html";
    public static final String  SUMMARY_REP_NAME     = "report.summary.html";
    public static final String  PKG_SUMMARY_REP_NAME = "package.summary.html";

    public static final String  ERROR_STRING         = "error";
    public static final String  FAIL_STATUS          = "failed";
    public static final String  PASS_STATUS          = "passed";
    public static final String  UNKNOWN_STATUS       = "unknown";
    public static final String  EXPECTED             = "yes";
    public static final String  UNEXPECTED           = "no";

    public static final boolean ADD_COLOR_ZERO       = true;
    public static final boolean SKIP_COLOR_ZERO      = false;

    protected String            reportDir            = cfg.getTestResultRoot()
                                                         + File.separator;    // +
    // "specview" + File.separator;

    protected TreeMap           packageList          = new TreeMap();
    protected TreeMap           classesList          = new TreeMap();

    BugNumberProvider           bnp;

    ReporterSpec(String bugSource) {
        bnp = new BugNumberProviderFile(bugSource);
    }

    protected void createIndexFile() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(reportDir + INDEX_REP_NAME);
            fw
                .write("<html>\n<head><title>Test run results</title></head>\n"
                    + "<frameset cols=\"20%,80%\">\n<frameset rows=\"20%,80%\">\n"
                    + "<frame src=\""
                    + reportDir
                    + PACKAGE_REP_NAME
                    + "\" name=\"packages\" title=\"Tested packages\">\n"
                    + "<frame src=\""
                    + reportDir
                    + CLASS_REP_NAME
                    + "\" name=\"classes\" title=\"Tested classes\">\n"
                    + "</frameset>\n"
                    + "<frame src=\""
                    + reportDir
                    + SUMMARY_REP_NAME
                    + "\" name=\"summary\" title=\"Test run summary\">\n"
                    + "<noframes>Non-frame version of report is not supported yet</noframes>\n"
                    + "</frameset>\n</html>");
        } catch (Exception e) {
            log
                .add("ReportTool\tReporterSpec. Unexpected exception while create index file "
                    + e);
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    log
                        .add("ReportTool\tReporterSpec. Unexpected exception while close index file "
                            + e);
                }
            }
        }
    }

    /*
     * create <class>.html and return file name
     */
    protected String createClListForPkg(String curPkg) {
        if (curPkg == null) {
            log.add(Level.INFO,
                "ReportTool\tcreateClListForPkg. Unexpected null package");
            return null;
        }
        FileWriter fw = null;
        try {
            String dirName;
            String fdirName;
            if (curPkg.indexOf('.') < 0) {
                dirName = "";
                fdirName = reportDir;
            } else {
                dirName = curPkg.replace('.', Constants.INTERNAL_FILE_SEP_C);
                fdirName = reportDir + dirName;
                new File(dirName).mkdirs();
            }
            fw = new FileWriter(fdirName + File.separator + "pclasses.html");
            fw.write("<html>\n<head><title>" + curPkg
                + " package</title></head>\n");
            fw.write("<a href=\"" + PKG_SUMMARY_REP_NAME
                + "\" title=\"summary for  in " + curPkg
                + "\" target=\"summary\">" + curPkg + "</a><br>\n");
            fw.write("<br>Tests<br>");
            PackageInfo pkg = (PackageInfo)packageList.get(curPkg);
            int cnt = pkg.classList.size();
            Iterator iter = pkg.classList.keySet().iterator();
            for (int i = 0; i < cnt; i++) {
                String curCl = (String)iter.next();
                ClassInfo cl = (ClassInfo)pkg.classList.get(curCl);
                if (cl.packageName.length() > 0) {
                    fw.write("<a href=\""
                        + cl.fullName.substring(cl.packageName.length() + 1)
                        + "\" title=\"class in " + cl.packageName
                        + "\" target=\"summary\">" + cl.className
                        + "</a><br>\n");
                } else {
                    fw.write("<a href=\""
                        + cl.fullName.substring(cl.packageName.length() + 1)
                        + "\" title=\"class in default (empty)package"
                        + "\" target=\"summary\">" + cl.className
                        + "</a><br>\n");
                }
            }
            fw.write("</html>");
            return dirName + File.separator + "pclasses.html";
        } catch (Exception e) {
            log
                .add("ReportTool\tcreateClListForPkg. Unexpected exception while create file for "
                    + curPkg + "\n" + e);
            return null;
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    log
                        .add("ReportTool\tcreateClListForPkg. Unexpected exception while close file for "
                            + curPkg + "\t" + e);
                }
            }
        }
    }

    protected void createPackagesFile() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(reportDir + PACKAGE_REP_NAME);
            fw.write("<html>\n<head><title>All packages</title></head>\n");
            fw
                .write("<a href=\""
                    + CLASS_REP_NAME
                    + "\" title=\"All classes\" target=\"classes\">All packages</a><br>\n");
            fw.write("<br>Packages<br>\n");
            int cnt = packageList.size();
            Iterator iter = packageList.keySet().iterator();
            for (int i = 0; i < cnt; i++) {
                String curPkg = (String)iter.next();
                String pkgName = curPkg;
                if (pkgName.length() < 1) {
                    pkgName = "default package";
                }
                fw.write("<a href=\"" + createClListForPkg(curPkg)
                    + "\" title=\"package " + pkgName
                    + "\" target=\"classes\">" + pkgName + "</a><br>\n");
            }
            fw.write("</html>");
        } catch (Exception e) {
            log
                .add("ReportTool\tReporterSpec. Unexpected exception while create packages file "
                    + e);
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    log
                        .add("ReportTool\tReporterSpec. Unexpected exception while close packages file "
                            + e);
                }
            }
        }
    }

    protected void createClassesFile() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(reportDir + CLASS_REP_NAME);
            fw.write("<html>\n<head><title>All classes</title></head>\n");
            fw
                .write("<a href=\""
                    + SUMMARY_REP_NAME
                    + "\" title=\"classes summary\" target=\"summary\">Packages</a><br>\n");
            fw.write("<br>Classes<br>\n");
            int cnt = classesList.size();
            Iterator iter = classesList.keySet().iterator();
            for (int i = 0; i < cnt; i++) {
                String clName = (String)iter.next();
                ClassInfo cl = (ClassInfo)classesList.get(clName);
                fw.write("<a href=\"" + cl.fullName + "\" title=\"class in "
                    + cl.packageName + "\" target=\"summary\">" + cl.className
                    + "</a><br>\n");
            }
            fw.write("</html>");
        } catch (Exception e) {
            log
                .add("ReportTool\tReporterSpec. Unexpected exception while create classes file "
                    + e);
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    log
                        .add("ReportTool\tReporterSpec. Unexpected exception while close classes file "
                            + e);
                }
            }
        }
    }

    protected String resultAsString(ClassInfo data) {
        if (data == null) {
            return UNKNOWN_STATUS;
        }
        if (data.errorTest > 0) {
            return "<font color=\"blue\">" + ERROR_STRING + "</font>";
        }
        if (data.failedTest > 0) {
            return "<font color=\"blue\">" + FAIL_STATUS + "</font>";
        }
        if (data.passedTest > 0) {
            return PASS_STATUS;
        }
        return "<font color=\"blue\">" + UNKNOWN_STATUS + "</font>";
    }

    protected String expecAsString(ClassInfo data) {
        if (data == null) {
            return UNKNOWN_STATUS;
        }
        if (data.unexpectedResults > 0) {
            return "<font color=\"red\">" + UNEXPECTED + "</font>";
        }
        return EXPECTED;
    }

    protected String bugAsString(ClassInfo data) {
        if (data == null) {
            return UNKNOWN_STATUS;
        }
        if (data.bugID == null) {
            return UNEXPECTED;
        }
        return data.bugID;
    }

    /*
     * return the created file name
     */
    protected String createSummaryForPkg(String pkgName) {
        FileWriter fw = null;
        String fName = pkgName.replace('.', File.separatorChar)
            + File.separator + PKG_SUMMARY_REP_NAME;
        try {
            fw = new FileWriter(reportDir + fName);
            fw.write("<html>\n<head><title>Test run results for package "
                + pkgName + "</title></head>\n");
            PackageInfo pkInfo = (PackageInfo)packageList.get(pkgName);
            fw.write("<h2>\nSummary for package " + pkgName + "\n</h2>\n");
            fw.write("<table>\n<tr><td>Total tests</td><td>" + pkInfo.totalTest
                + "</td></tr>\n" + "<tr><td>not passed tests</td><td>"
                + (pkInfo.totalTest - pkInfo.passedTest) + "</td></tr>\n"
                + "<tr><td>tests with unexpected results</td><td>"
                + pkInfo.unexpectedResults + "</td></tr>\n</table>");
            fw.write("<h2>\nDetails\n</h2>\n");
            fw.write("<table border=1>\n");
            fw
                .write("<tr><td>class name</td>\n<td>Result</td>\n<td>expected</td>\n<td>Bug id</td>\n</tr>");
            int cnt = pkInfo.classList.size();
            Iterator iter = pkInfo.classList.keySet().iterator();
            for (int i = 0; i < cnt; i++) {
                String clName = (String)iter.next();
                ClassInfo clInfo = (ClassInfo)pkInfo.classList.get(clName);
                fw.write("<tr><td><a href=\"" + clInfo.shortName
                    + "\" title=\"class " + clName + "\" target=\"summary\">"
                    + clName + "</a></td>\n<td>" + resultAsString(clInfo)
                    + "</td>\n<td>" + expecAsString(clInfo) + "</td>\n<td>"
                    + bugAsString(clInfo) + "</td>\n</tr>");
            }
            fw.write("</table></html>");
        } catch (Exception e) {
            log
                .add("ReportTool\tReporterSpec. Unexpected exception while create summary file for package "
                    + pkgName + "\n" + e);
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    log
                        .add("ReportTool\tReporterSpec. Unexpected exception while close summary file for package "
                            + pkgName + "\t" + e);
                }
            }
        }
        return fName;
    }

    protected String getColoredHTMLMark(String color, int data,
        boolean condition) {
        if (data == 0 && condition == SKIP_COLOR_ZERO) {
            return "" + data;
        } else {
            return "<font color=\"" + color + "\">" + data + "</font>";
        }
    }

    protected void createSummaryFile() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(reportDir + SUMMARY_REP_NAME);
            fw.write("<html>\n<head><title>Test run results</title></head>\n");
            fw.write("<a name=\"top\"><h1>Report</h1></a>\n" + "<table>"
                + getGenInfo() + getConfig() + "\n</table>");
            int cnt = packageList.size();
            Iterator iter = packageList.keySet().iterator();
            fw.write("<table border=1>\n");
            fw
                .write("<tr><td>package name</td>\n<td>Total tests</td>\n<td>not passed</td>\n<td>unexpected result</td>\n</tr>");
            for (int i = 0; i < cnt; i++) {
                String curPkg = (String)iter.next();
                PackageInfo pkInfo = (PackageInfo)packageList.get(curPkg);
                String pkgName = curPkg;
                if (pkgName.length() < 1) {
                    pkgName = "default package";
                }
                fw.write("<tr><td><a href=\""
                    + createSummaryForPkg(curPkg)
                    + "\" title=\"package "
                    + pkgName
                    + "\" target=\"summary\">"
                    + pkgName
                    + "</a></td>\n<td>"
                    + pkInfo.totalTest
                    + "</td>\n<td>"
                    + (pkInfo.totalTest - pkInfo.passedTest)
                    + "</td>\n"
                    + "<td>"
                    + getColoredHTMLMark("red", pkInfo.unexpectedResults,
                        SKIP_COLOR_ZERO) + "</td>\n</tr>");
            }
            fw.write("</table></html>");
        } catch (Exception e) {
            log
                .add("ReportTool\tReporterSpec. Unexpected exception while create summary file "
                    + e);
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    log
                        .add("ReportTool\tReporterSpec. Unexpected exception while close summary file "
                            + e);
                }
            }
        }
    }

    protected void createPackageList() {
        int cnt = allTestRes.size();
        Iterator iter = allTestRes.keySet().iterator();
        for (int i = 0; i < cnt; i++) {
            String curTest = (String)iter.next();
            int curStatus = ((Integer)allTestRes.get(curTest)).intValue();
            int tmp = curTest.lastIndexOf(Constants.INTERNAL_FILE_SEP_C);
            String curPkg;
            String curClass;
            String tName;
            if (curTest.lastIndexOf(Constants.INTERNAL_FILE_SEP_C) < 0) {
                curPkg = "";
                tName = curTest;
                if (curTest.indexOf('.') < 0) {
                    curClass = curTest;
                } else {
                    curClass = curTest.substring(0, curTest.indexOf('.'));
                }
                log.add(Level.CONFIG,
                    "ReportTool\tcreatePackageList. Test in the default package: "
                        + curTest);
            } else {
                curPkg = curTest.substring(0,
                    curTest.lastIndexOf(Constants.INTERNAL_FILE_SEP_C))
                    .replace(Constants.INTERNAL_FILE_SEP_C, '.');
                tName = curTest.substring(curTest
                    .lastIndexOf(Constants.INTERNAL_FILE_SEP_C) + 1);
                if (tName.indexOf('.') < 0) {
                    curClass = tName;
                } else {
                    curClass = tName.substring(0, tName.indexOf('.'));
                }
            }
            if (!packageList.containsKey(curPkg)) {
                packageList.put(curPkg, new PackageInfo(curPkg));
            }
            PackageInfo pkg = (PackageInfo)packageList.get(curPkg);
            ClassInfo clInfo = new ClassInfo(curPkg, curClass, curTest, tName);
            pkg.totalTest++;
            clInfo.totalTest++;
            if (isPassed(curStatus)) {
                pkg.passedTest++;
                clInfo.passedTest++;
            } else if (isFailed(curStatus)) {
                pkg.failedTest++;
                clInfo.failedTest++;
            } else if (isError(curStatus)) {
                pkg.errorTest++;
                clInfo.errorTest++;
            } else {
                pkg.otherTest++;
            }
            if (!isPassed(curStatus)) {
                //report as unexpected only not passed tests
                if (bnp != null) {
                    clInfo.bugID = bnp.getAllBugNumbers(curPkg.replace('.',
                        Constants.INTERNAL_FILE_SEP_C)
                        + Constants.INTERNAL_FILE_SEP_C + curClass);
                    if ((clInfo.bugID == null && !isPassed(curStatus))
                        || (clInfo.bugID != null && isPassed(curStatus))) {
                        pkg.unexpectedResults++;
                        clInfo.unexpectedResults = 1;
                    }
                }
            }
            pkg.classList.put(curClass, clInfo);
            classesList.put(curClass + curTest, clInfo);
        }
    }

    protected boolean isPassed(int status) {
        for (int i = 0; i < pasVal.length; i++) {
            if (status == pasVal[i]) {
                return true;
            }
        }
        return false;
    }

    protected boolean isFailed(int status) {
        for (int i = 0; i < fldVal.length; i++) {
            if (status == fldVal[i]) {
                return true;
            }
        }
        return false;
    }

    protected boolean isError(int status) {
        for (int i = 0; i < errVal.length; i++) {
            if (status == errVal[i]) {
                return true;
            }
        }
        return false;
    }

    public void genReport() {
        new File(reportDir).mkdirs();
        createPackageList();
        createPackagesFile();
        createClassesFile();
        createSummaryFile();
        createIndexFile();
    }

    class PackageInfo {
        String  packageName;
        TreeMap classList         = new TreeMap();
        String  description;
        int     totalTest         = 0;
        int     passedTest        = 0;
        int     failedTest        = 0;
        int     errorTest         = 0;
        int     otherTest         = 0;
        int     unexpectedResults = 0;

        PackageInfo(String name) {
            if (name == null) {
                packageName = "";
            } else {
                packageName = name;
            }
        }
    }

    class ClassInfo {
        String className;
        String packageName;
        String fullName;
        String shortName;
        String description;
        String bugID;                //null value means unexpected
        // failed/error/other
        int    totalTest         = 0;
        int    passedTest        = 0;
        int    failedTest        = 0;
        int    errorTest         = 0;
        int    otherTest         = 0;
        int    unexpectedResults = 0;

        ClassInfo(String pkg, String cl, String fname, String sname) {
            if (pkg == null) {
                packageName = "";
            } else {
                packageName = pkg;
            }
            className = cl;
            fullName = fname;
            shortName = sname;
        }
    }
}