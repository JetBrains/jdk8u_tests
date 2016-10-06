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
 * @version $Revision: 1.12 $
 */
package org.apache.harmony.harness.ReportTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;

import org.apache.harmony.harness.DefaultConfigSetup;
import org.apache.harmony.harness.Finder;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.TResIR;
import org.apache.harmony.harness.Report;
import org.apache.harmony.harness.Util;
import org.apache.harmony.harness.InternalTHLogger;
import org.apache.harmony.harness.plugins.Reporter;

public class CmpResults extends Main {
    public static final String XML_ONLY     = "xml";
    public static final String HTML_ONLY    = "html";

    // when reportOnly == true, no test run. Generate report only.
    boolean                    reportOnly   = false;

    String                     reportMode   = "";
    String                     repFileName  = "cmpreport";
    protected String           newResPath;
    protected String           oldResPath;
    protected HashMap          newRes;
    protected HashMap          oldRes;

    protected ArrayList        notChanged   = new ArrayList();

    protected ArrayList        newTests     = new ArrayList();
    protected ArrayList        removedTests = new ArrayList();
    protected HashMap          newPassed    = new HashMap();
    protected HashMap          newFailed    = new HashMap();
    protected HashMap          newError     = new HashMap();
    protected HashMap          newModeErr   = new HashMap();
    protected HashMap          newUnk       = new HashMap();

    protected Finder           resf;

    protected boolean parseParam(String[] params) {
        String helpMsg = "Compare results of 2 test run.\nUsage:\n"
            + "-cfp <path>\t\t\tpath to the current configuration files\n"
            + "-oldres <path>\t\t\tpath to the directory with old results\n"
            + "-name <file_name>\t\t\tname of the report file (without extension)\n"
            + "-format \"name\"\t\tname of the format (xml or html formats)\n"
            + "Also options of harness are supported";
        try {
            for (int i = 0; i < params.length; i++) {
                if ("-format".equalsIgnoreCase(params[i])) {
                    reportMode = params[++i];
                } else if ("-cfp".equalsIgnoreCase(params[i])) {
                    cfgStorePath = params[++i];
                } else if ("-oldres".equalsIgnoreCase(params[i])) {
                    oldResPath = params[++i];
                } else if ("-name".equalsIgnoreCase(params[i])) {
                    repFileName = params[++i];
                } else if ("-help".equalsIgnoreCase(params[i])
                    || "-h".equalsIgnoreCase(params[i])) {
                    System.out.println(helpMsg);
                    System.out.println("also options of harness are supported");
                    System.exit(0);
                } else {
                    super.parseParam(Util.removeFirstElem(params, i));
                    break;
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            System.out.println("Incorrect parameters. Please check.\n"
                + helpMsg);
            return false;
        }
        return true;
    }

    // expect the key as testID and status as value
    void analizeData(HashMap newRes, HashMap oldRes) {
        Object curKey;
        int[] statuses;
        boolean matched;
        int cnt = newRes.size();
        int newStatus;
        int oldStatus;
        Iterator iter = newRes.keySet().iterator();
        for (int i = 0; i < cnt; i++) {
            curKey = iter.next();
            matched = false;
            if (oldRes.containsKey(curKey)) {
                newStatus = ((Integer)newRes.get(curKey)).intValue();
                oldStatus = ((Integer)oldRes.get(curKey)).intValue();
                if (newStatus == oldStatus) {
                    notChanged.add(curKey);
                } else {
                    statuses = cfgM.getRepError();
                    for (int y = 0; y < statuses.length; y++) {
                        if (newStatus == statuses[y]) {
                            newError.put(curKey, "new status " + newStatus
                                + ", was " + oldStatus);
                            matched = true;
                            break;
                        }
                    }
                    if (!matched) {
                        statuses = cfgM.getRepFailed();
                        for (int y = 0; y < statuses.length; y++) {
                            if (newStatus == statuses[y]) {
                                newFailed.put(curKey, "new status " + newStatus
                                    + ", was " + oldStatus);
                                matched = true;
                                break;
                            }
                        }
                    }
                    if (!matched) {
                        statuses = cfgM.getRepModErr();
                        for (int y = 0; y < statuses.length; y++) {
                            if (newStatus == statuses[y]) {
                                newModeErr.put(curKey, "new status "
                                    + newStatus + ", was " + oldStatus);
                                matched = true;
                                break;
                            }
                        }
                    }
                    if (!matched) {
                        statuses = cfgM.getRepPassed();
                        for (int y = 0; y < statuses.length; y++) {
                            if (newStatus == statuses[y]) {
                                newPassed.put(curKey, "new status " + newStatus
                                    + ", was " + oldStatus);
                                matched = true;
                                break;
                            }
                        }
                    }
                    if (!matched) {
                        newUnk.put(curKey, "new status " + newStatus + ", was "
                            + oldStatus);
                    }
                }
            } else {
                newTests.add(curKey);
            }
        }
        cnt = oldRes.size();
        iter = oldRes.keySet().iterator();
        for (int i = 0; i < cnt; i++) {
            curKey = iter.next();
            if (!newRes.containsKey(curKey)) {
                removedTests.add(curKey);
            }
        }
    }

    protected void sortPrintListHTMLMarkup(HashMap list, FileWriter file,
        String pathPrefix) throws IOException {
        if (list == null || list.size() == 0) {
            return;
        } else {
            SortedMap sorted = new TreeMap();
            int num = list.size();
            Object curKey;
            Iterator iter = list.keySet().iterator();
            for (int i = 0; i < num; i++) {
                curKey = iter.next();
                sorted.put(curKey, (String)list.get(curKey));
            }
            for (int i = 0; i < num; i++) {
                String name = (String)sorted.firstKey();
                file.write("\t<li>" + "<a href=\"" + pathPrefix
                    + File.separator + name + "\">" + name + "</a>"
                    + "\t reason: " + sorted.get(name) + "</li>\n");
                sorted.remove(name);
            }
            file.flush();
        }
    }

    protected void sortPrintListHTMLMarkup(ArrayList list, FileWriter file,
        String pathPrefix) throws IOException {
        if (list == null || list.size() == 0) {
            return;
        } else {
            SortedMap sorted = new TreeMap();
            int num = list.size();
            for (int i = 0; i < num; i++) {
                sorted.put((String)list.get(i), null);
            }
            for (int i = 0; i < num; i++) {
                String name = (String)sorted.firstKey();
                file
                    .write("\t<li>" + "<a href=\"" + pathPrefix
                        + File.separator + name + "\">" + name + "</a>"
                        + "</li>\n");
                sorted.remove(name);
            }
            file.flush();
        }
    }

    protected void writeXSLforReport() {
        String name = newResPath + File.separator + repFileName + ".xsl";
        File curFile = new File(name);
        try {
            FileWriter file = new FileWriter(curFile);
            file
                .write("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
                    + "\t<xsl:template match=\"/\">\n"
                    + "\t\t<h1>Compare result reports</h1>\n"
                    + "\t\t<xsl:apply-templates/>\n"
                    + "\t</xsl:template>\n"
                    + "\t<xsl:template match=\"property-list\">\n"
                    + "\t\t<p><a name=\"Total\"></a><table>\n"
                    + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                    + "\t\t\t<th colspan=\"2\" align=\"center\">General properties</th>"
                    + "\t\t\t</tr>\n"
                    + "\t\t\t<xsl:for-each select=\"property-item\">\n"
                    + "\t\t\t\t<tr>\n"
                    + "\t\t\t\t<td>\n"
                    + "\t\t\t\t\t<xsl:if test=\"@name='date'\">\n"
                    + "\t\t\t\t\t\t<strong><xsl:value-of select=\"./@name\"/></strong>\n"
                    + "\t\t\t\t\t</xsl:if>\n"
                    + "\t\t\t\t\t<xsl:if test=\"@name!='date'\">\n"
                    + "\t\t\t\t\t\t<a href=\"#{./@name}\"><strong><xsl:value-of select=\"./@name\"/></strong></a>\n"
                    + "\t\t\t\t\t</xsl:if>\n"
                    + "\t\t\t\t</td>\n"
                    + "\t\t\t\t<td><xsl:value-of select=\".\"/></td>\n"
                    + "\t\t\t\t</tr>\n"
                    + "\t\t\t</xsl:for-each>\n"
                    + "\t\t</table></p>\n"
                    + "\t</xsl:template>\n"
                    + "\t<xsl:template match=\"cfg-list\">\n"
                    + "\t\t<p><a name=\"Config\"></a><table>\n"
                    + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                    + "\t\t\t<th colspan=\"2\" align=\"center\">Configuration</th></tr>\n"
                    + "\t\t\t<xsl:for-each select=\"cfg-item\">\n"
                    + "\t\t\t\t<tr>\n"
                    + "\t\t\t\t<td><strong><xsl:value-of select=\"./@name\"/></strong></td>\n"
                    + "\t\t\t\t<td><xsl:value-of select=\".\"/></td>\n"
                    + "\t\t\t\t</tr>\n"
                    + "\t\t\t</xsl:for-each>\n"
                    + "\t\t</table></p>\n"
                    + "\t</xsl:template>\n"
                    + "\t<xsl:template match=\"passed-list\">\n"
                    + "\t\t<p><a name=\"passed\"></a><table>\n"
                    + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                    + "\t\t\t<th colspan=\"2\" align=\"center\">Tests, passed into new run</th></tr>\n"
                    + "\t\t\t<xsl:for-each select=\"list-item\">\n"
                    + "\t\t\t<xsl:sort order=\"ascending\" select=\".\"/>\n"
                    + "\t\t\t\t<tr>\n"
                    + "\t\t\t\t<td><b><xsl:number value=\"position()\" format=\"1\" /></b></td>\n"
                    + "\t\t\t\t<td><a href=\"{.}\"><xsl:value-of select=\".\"/></a></td>\n"
                    + "\t\t\t\t</tr>\n"
                    + "\t\t\t</xsl:for-each>\n"
                    + "\t\t</table></p>\n"
                    + "\t</xsl:template>\n"
                    + "\t<xsl:template match=\"failed-list\">\n"
                    + "\t\t<p><a name=\"failed\"></a><table>\n"
                    + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                    + "\t\t\t<th colspan=\"2\" align=\"center\">Tests, failed into new run</th></tr>\n"
                    + "\t\t\t<xsl:for-each select=\"list-item\">\n"
                    + "\t\t\t<xsl:sort order=\"ascending\" select=\".\"/>\n"
                    + "\t\t\t\t<tr>\n"
                    + "\t\t\t\t<td><b><xsl:number value=\"position()\" format=\"1\" /></b></td>\n"
                    + "\t\t\t\t<td><a href=\"{.}\"><xsl:value-of select=\".\"/></a></td>\n"
                    + "\t\t\t\t</tr>\n"
                    + "\t\t\t</xsl:for-each>\n"
                    + "\t\t</table></p>\n"
                    + "\t</xsl:template>\n"
                    + "\t<xsl:template match=\"error-list\">\n"
                    + "\t\t<p><a name=\"error\"></a><table>\n"
                    + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                    + "\t\t\t<th colspan=\"2\" align=\"center\">Tests with error status into new run</th></tr>\n"
                    + "\t\t\t<xsl:for-each select=\"list-item\">\n"
                    + "\t\t\t<xsl:sort order=\"ascending\" select=\".\"/>\n"
                    + "\t\t\t\t<tr>\n"
                    + "\t\t\t\t<td><b><xsl:number value=\"position()\" format=\"1\" /></b></td>\n"
                    + "\t\t\t\t<td><a href=\"{.}\"><xsl:value-of select=\".\"/></a></td>\n"
                    + "\t\t\t\t</tr>\n"
                    + "\t\t\t</xsl:for-each>\n"
                    + "\t\t</table></p>\n"
                    + "\t</xsl:template>\n"
                    + "\t<xsl:template match=\"modeError-list\">\n"
                    + "\t\t<p><a name=\"skipped\"></a><table>\n"
                    + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                    + "\t\t\t<th colspan=\"2\" align=\"center\">Tests were skipped</th></tr>\n"
                    + "\t\t\t<xsl:for-each select=\"list-item\">\n"
                    + "\t\t\t<xsl:sort order=\"ascending\" select=\".\"/>\n"
                    + "\t\t\t\t<tr>\n"
                    + "\t\t\t\t<td><b><xsl:number value=\"position()\" format=\"1\" /></b></td>\n"
                    + "\t\t\t\t<td><a href=\"{.}\"><xsl:value-of select=\".\"/></a></td>\n"
                    + "\t\t\t\t</tr>\n"
                    + "\t\t\t</xsl:for-each>\n"
                    + "\t\t</table></p>\n"
                    + "\t</xsl:template>\n"
                    + "\t<xsl:template match=\"unknown-list\">\n"
                    + "\t\t<p><a name=\"unknown\"></a><table>\n"
                    + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                    + "\t\t\t<th colspan=\"2\" align=\"center\">Tests completed with exit code which is not recognized by harness as known test execution code</th></tr>\n"
                    + "\t\t\t<xsl:for-each select=\"list-item\">\n"
                    + "\t\t\t<xsl:sort order=\"ascending\" select=\".\"/>\n"
                    + "\t\t\t\t<tr>\n"
                    + "\t\t\t\t<td><b><xsl:number value=\"position()\" format=\"1\" /></b></td>\n"
                    + "\t\t\t\t<td><a href=\"{.}\"><xsl:value-of select=\".\"/></a></td>\n"
                    + "\t\t\t\t</tr>\n"
                    + "\t\t\t</xsl:for-each>\n"
                    + "\t\t</table></p>\n"
                    + "\t</xsl:template>\n"
                    + "\t<xsl:template match=\"new-list\">\n"
                    + "\t\t<p><a name=\"new\"></a><table>\n"
                    + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                    + "\t\t\t<th colspan=\"2\" align=\"center\">New tests</th></tr>\n"
                    + "\t\t\t<xsl:for-each select=\"list-item\">\n"
                    + "\t\t\t<xsl:sort order=\"ascending\" select=\".\"/>\n"
                    + "\t\t\t\t<tr>\n"
                    + "\t\t\t\t<td><b><xsl:number value=\"position()\" format=\"1\" /></b></td>\n"
                    + "\t\t\t\t<td><a href=\"{.}\"><xsl:value-of select=\".\"/></a></td>\n"
                    + "\t\t\t\t</tr>\n"
                    + "\t\t\t</xsl:for-each>\n"
                    + "\t\t</table></p>\n"
                    + "\t</xsl:template>\n"
                    + "\t<xsl:template match=\"removed-list\">\n"
                    + "\t\t<p><a name=\"removed\"></a><table>\n"
                    + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                    + "\t\t\t<th colspan=\"2\" align=\"center\">Removed tests</th></tr>\n"
                    + "\t\t\t<xsl:for-each select=\"list-item\">\n"
                    + "\t\t\t<xsl:sort order=\"ascending\" select=\".\"/>\n"
                    + "\t\t\t\t<tr>\n"
                    + "\t\t\t\t<td><b><xsl:number value=\"position()\" format=\"1\" /></b></td>\n"
                    + "\t\t\t\t<td><a href=\"{.}\"><xsl:value-of select=\".\"/></a></td>\n"
                    + "\t\t\t\t</tr>\n"
                    + "\t\t\t</xsl:for-each>\n"
                    + "\t\t</table></p>\n"
                    + "\t</xsl:template>\n"
                    + "\t<xsl:template match=\"unchanged-list\">\n"
                    + "\t\t<p><a name=\"not changed\"></a><table>\n"
                    + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                    + "\t\t\t<th colspan=\"2\" align=\"center\">Unchanged tests</th></tr>\n"
                    + "\t\t\t<xsl:for-each select=\"list-item\">\n"
                    + "\t\t\t<xsl:sort order=\"ascending\" select=\".\"/>\n"
                    + "\t\t\t\t<tr>\n"
                    + "\t\t\t\t<td><b><xsl:number value=\"position()\" format=\"1\" /></b></td>\n"
                    + "\t\t\t\t<td><a href=\"{.}\"><xsl:value-of select=\".\"/></a></td>\n"
                    + "\t\t\t\t</tr>\n"
                    + "\t\t\t</xsl:for-each>\n"
                    + "\t\t</table></p>\n"
                    + "\t</xsl:template>\n"
                    + "</xsl:stylesheet>");
            file.flush();
            file.close();
        } catch (IOException e) {
            log.add(Level.SEVERE,
                "CmpReporter. Unexpected exception while generate xsl file "
                    + e);
        }
    }

    protected void writeReportasXML() {
        writeXSLforReport();
        String name = newResPath + File.separator + repFileName + "."
            + XML_ONLY;
        File curFile = new File(name);
        try {
            FileWriter file = new FileWriter(curFile);
            file
                .write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<?xml-stylesheet type='text/xsl' href='"
                    + repFileName
                    + ".xsl'?>\n"
                    + "<Report>\n"
                    + "<property-list>\n\t<property-item name=\"date\">"
                    + new Date().toString()
                    + "</property-item>\n\t<property-item name=\"Total (into new run)\">"
                    + newRes.size()
                    + "</property-item>\n\t<property-item name=\"Total (into old run)\">"
                    + oldRes.size()
                    + "</property-item>\n\t<property-item name=\"new\">"
                    + newTests.size()
                    + "</property-item>\n\t<property-item name=\"removed\">"
                    + removedTests.size()
                    + "</property-item>\n\t<property-item name=\"passed\">"
                    + newPassed.size()
                    + "</property-item>\n\t<property-item name=\"failed\">"
                    + newFailed.size()
                    + "</property-item>\n\t<property-item name=\"error\">"
                    + newError.size()
                    + "</property-item>\n\t<property-item name=\"skipped\">"
                    + newModeErr.size()
                    + "</property-item>\n\t<property-item name=\"unknown\">"
                    + newUnk.size()
                    + "</property-item>\n\t<property-item name=\"not changed\">"
                    + notChanged.size()
                    + "</property-item>\n</property-list>\n" + "<cfg-list>\n");
            file.flush();
            getXMLMarkedConfig(file);
            file.write("\n</cfg-list>\n<error-list>\n");
            printList(newError, file);
            file.write("</error-list>\n<failed-list>\n");
            printList(newFailed, file);
            file.write("</failed-list>\n<unknown-list>\n");
            printList(newUnk, file);
            file.write("</unknown-list>\n<modeError-list>\n");
            printList(newModeErr, file);
            file.write("</modeError-list>\n<passed-list>\n");
            printList(newPassed, file);
            file.write("</passed-list>\n");
            file.write("<new-list>\n");
            printList(newTests, file);
            file.write("</new-list>\n");
            file.write("<removed-list>\n");
            printList(removedTests, file);
            file.write("</removed-list>\n");
            file.write("<unchanged-list>\n");
            printList(notChanged, file);
            file.write("</unchanged-list>\n");
            file.write("</Report>");
            file.flush();
            file.close();
        } catch (IOException e) {
            log.add(Level.SEVERE,
                "CmpReporter. Unexpected exception while generate XML report "
                    + e);
        }
    }

    protected void writeReportasHTML() {
        try {
            String name = newResPath + File.separator + repFileName + "."
                + HTML_ONLY;
            File curFile = new File(name);
            FileWriter file = new FileWriter(curFile);
            file
                .write("<html>\n"
                    + "<body>\n"
                    + "<a name=\"top\"><h1>Report</h1></a>\n"
                    + "<table>"
                    + "\n\t<tr><td colspan=\"2\" align=\"center\"><h3>General information</h3></td></tr>"
                    + "\n\t<tr>\n\t\t<td width=\"100\">date</td>\n\t\t<td>"
                    + new Date().toString()
                    + "</td>\n\t</tr>\n\t<tr>\n\t\t<td>Total (into new run)</td>\n\t\t<td>"
                    + newRes.size()
                    + "</td>\n\t</tr>\n\t<tr>\n\t\t<td>Total (into old run)</td>\n\t\t<td>"
                    + oldRes.size()
                    + "</td>\n\t</tr>\n\t<tr>\n\t\t<td><a href=\"#passed\">new tests</a></td>\n\t\t<td>"
                    + newTests.size()
                    + "</td>\n\t</tr>\n\t<tr>\n\t\t<td><a href=\"#passed\">removed tests</a></td>\n\t\t<td>"
                    + removedTests.size()
                    + "</td>\n\t</tr>\n\t<tr>\n\t\t<td><a href=\"#passed\">passed (into new run)</a></td>\n\t\t<td>"
                    + newPassed.size()
                    + "</td>\n\t<tr>\n\t\t<td><a href=\"#failed\">failed (into new run)</a></td>\n\t\t<td>"
                    + newFailed.size()
                    + "</td>\n\t</tr>\n\t<tr>\n\t\t<td><a href=\"#error\">error (into new run)</a></td>\n\t\t<td>"
                    + newError.size()
                    + "</td>\n\t<tr>\n\t\t<td><a href=\"#skipped\">skipped (into new run)</a></td>\n\t\t<td>"
                    + newModeErr.size()
                    + "</td>\n\t</tr>\n\t<tr>\n\t\t<td><a href=\"#unknown\">unknown (into new run)</a></td>\n\t\t<td>"
                    + newUnk.size()
                    + "</td>\n\t</tr>\n\t<tr>\n\t\t<td><a href=\"#notchanged\">not changed</a></td>\n\t\t<td>"
                    + notChanged.size()
                    + "\n\t\t</td>\n\t</tr>\n"
                    + "\n\t<tr><td colspan=\"2\" align=\"center\"><h3>Configuration</h3></td></tr>");
            getHTMLMarkedConfig(file);
            file.write("\n</table>");
            file
                .write("\n<h3><a name=\"error\">Tests with error (in new run)</a></h3>\n"
                    + "\n<ol>");
            sortPrintListHTMLMarkup(newError, file, newResPath);
            file
                .write("</ol><a href=\"#top\">top</a>\n<h3><a name=\"failed\">Failed test (in new run)</a></h3>\n"
                    + "\n<ol>");
            sortPrintListHTMLMarkup(newFailed, file, newResPath);
            file
                .write("</ol><a href=\"#top\">top</a>\n<h3><a name=\"unknown\">Test with unknown execution status  (in new run)</a></h3>\n"
                    + "\n<ol>");
            sortPrintListHTMLMarkup(newUnk, file, newResPath);
            file
                .write("</ol><a href=\"#top\">top</a>\n<h3><a name=\"skipped\">Skipped tests (in new run)</a></h3>\n"
                    + "\n<ol>");
            sortPrintListHTMLMarkup(newModeErr, file, newResPath);
            file
                .write("</ol><a href=\"#top\">top</a>\n<h3><a name=\"passed\">Passed tests (in new run)</a></h3>\n"
                    + "\n<ol>");
            sortPrintListHTMLMarkup(newPassed, file, newResPath);
            file
                .write("</ol><a href=\"#top\">top</a>\n<h3><a name=\"new\">New tests</a></h3>\n"
                    + "\n<ol>");
            sortPrintListHTMLMarkup(newTests, file, newResPath);
            file
                .write("</ol><a href=\"#top\">top</a>\n<h3><a name=\"remove\">Removed tests</a></h3>\n"
                    + "\n<ol>");
            sortPrintListHTMLMarkup(removedTests, file, oldResPath);
            file
                .write("</ol><a href=\"#top\">top</a>\n<h3><a name=\"notchanged\">Tests with unchanged status</a></h3>\n"
                    + "\n<ol>");
            sortPrintListHTMLMarkup(notChanged, file, newResPath);
            file.write("</ol><a href=\"#top\">top</a>\n</body>\n<html>");
            file.flush();
            file.close();
        } catch (IOException e) {
            log.add(Level.SEVERE,
                "CmpReporter. Unexpected exception while generate HTML report "
                    + e);
        }
    }

    protected void reportToConsole() {
        System.out.println("tests total\t" + newRes.size() + "\n  unchanged\t"
            + notChanged.size() + "\n  new tests\t" + newTests.size()
            + "\n  removed tests\t" + +removedTests.size()
            + "\n  anew passed\t" + newPassed.size() + "\n  anew failed\t"
            + newFailed.size() + "\n  anew error\t" + newError.size()
            + "\n  anew skipped\t" + newModeErr.size() + "\n  anew unknown\t"
            + newUnk.size());
    }

    protected String getHTMLMarkedConfig(FileWriter file) throws IOException {
        String mode;
        String testSuiteVersion;
        if (cfgM.getExecM() == Main.SAME) {
            mode = "same VM";
        } else {
            mode = "other VM";
        }
        if (cfgM.getLocalM() == Main.LOCAL) {
            mode = mode + ", local";
        } else {
            mode = mode + ", remote";
        }
        testSuiteVersion = Main.getCurCore().getConfigIR().getProperty(
            "TestBaseVersion");
        file
            .write("\n\t<tr>\n\t\t<td>test suite root</td>\n\t\t<td>"
                + "<a href=\""
                + cfgM.getTestSuiteRoot()
                + "\">"
                + cfgM.getTestSuiteRoot()
                + "</a>"
                + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>test suite version</td>\n\t\t<td>"
                + testSuiteVersion
                + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>test root</td>\n\t\t<td>"
                + cfgM.getTestSuiteTestRoot()
                + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>test class root</td>\n\t\t<td>"
                + cfgM.getTestSuiteClassRoot()
                + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>tested runtime</td>\n\t\t<td>"
                + cfgM.getTestedRuntime()
                + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>execution mode</td>\n\t\t<td>"
                + mode
                + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>path to env_cfg.xml file</td>\n\t\t<td>"
                + "<a href=\"" + Main.getCurCore().getCfgPath()
                + File.separator + "cfg_env.xml" + "\">"
                + Main.getCurCore().getCfgPath() + "</a>"
                + "\n\t\t</td>\n\t</tr>");
        file.flush();
        return "";
    }

    protected String getXMLMarkedConfig(FileWriter file) throws IOException {
        String mode;
        String testSuiteVersion;
        if (cfgM.getExecM() == Main.SAME) {
            mode = "same VM";
        } else {
            mode = "other VM";
        }
        if (cfgM.getLocalM() == Main.LOCAL) {
            mode = mode + ", local";
        } else {
            mode = mode + ", remote";
        }
        testSuiteVersion = Main.getCurCore().getConfigIR().getProperty(
            "TestBaseVersion");
        file.write("\t<cfg-item name=\"test suite root\">"
            + cfgM.getTestSuiteRoot()
            + "</cfg-item>\n\t<cfg-item name=\"test suite version\">"
            + testSuiteVersion + "</cfg-item>\n\t<cfg-item name=\"test root\">"
            + cfgM.getTestSuiteTestRoot()
            + "</cfg-item>\n\t<cfg-item name=\"test class root\">"
            + cfgM.getTestSuiteClassRoot()
            + "</cfg-item>\n\t<cfg-item name=\"tested runtime\">"
            + cfgM.getTestedRuntime()
            + "</cfg-item>\n\t<cfg-item name=\"execution mode\">" + mode
            + "</cfg-item>\n\t<cfg-item name=\"path to env_cfg.xml file\">"
            + Main.getCurCore().getCfgPath() + "</cfg-item>");
        file.flush();
        return "";
    }

    protected void printList(ArrayList list, FileWriter file)
        throws IOException {
        if (list == null || list.size() == 0) {
            return;
        } else {
            int num = list.size();
            for (int i = 0; i < num; i++) {
                file.write("\t<list-item>" + (String)list.get(i)
                    + "</list-item>\n");
            }
            file.flush();
        }
    }

    protected void printList(HashMap list, FileWriter file) throws IOException {
        if (list == null || list.size() == 0) {
            return;
        } else {
            int num = list.size();
            Iterator iter = list.keySet().iterator();
            for (int i = 0; i < num; i++) {
                file.write("\t<list-item>" + iter.next() + "</list-item>\n");
            }
            file.flush();
        }
    }

    void generateReport() {
        try {
            createConfigurator();
            cfg.createConfiguration(cfgStorePath);
            setCfgFromCmdline();
            new DefaultConfigSetup().setDefaultValues();
            checkAndUpdateConfig();
            if (reportMode.equalsIgnoreCase(HTML_ONLY)) {
                rep = new ReporterHTML();
            } else {
                rep = new Reporter();
            }
            createResFinder();
            if (subSuites != null && subSuites.length > 0) {
                for (int i = 0; i < subSuites.length; i++) {
                    addResults(rf.find(subSuites[i]), rep);
                }
            } else {
                addResults(rf.find(), rep);
            }
            newRes = rep.getResults();
            newResPath = cfgM.getTestResultRoot();
            cfgM.setTestResultRoot(oldResPath);
            rep.clear();
            rf.clear();
            rf.setFindRoot(oldResPath);
            if (subSuites != null && subSuites.length > 0) {
                for (int i = 0; i < subSuites.length; i++) {
                    addResults(rf.find(subSuites[i]), rep);
                }
            } else {
                addResults(rf.find(), rep);
            }
            oldRes = rep.getResults();
            analizeData(newRes, oldRes);
            if (XML_ONLY.equalsIgnoreCase(reportMode)) {
                writeReportasXML();
            } else if (HTML_ONLY.equalsIgnoreCase(reportMode)) {
                writeReportasHTML();
            } else {
                writeReportasXML();
                writeReportasHTML();
            }
            reportToConsole();
            System.out.println("done. See the run report in the directory\n\t"
                + newResPath);
        } catch (Throwable th) {
            System.out.println("Unexpected exception: " + th);
            th.printStackTrace();
        }
    }

    boolean addResults(int num, Report rep) {
        boolean retVal = false;
        for (int cnt = 0; cnt < num; cnt++) {
            retVal = addResult((TResIR)rf.getNext(), rep);
        }
        return retVal;
    }

    /*
     * add test result to execution status list
     */
    boolean addResult(TResIR tRes, Report rep) {
        try {
            String testID = tRes.getTestID();
            int testRes = tRes.getExecStat();
            //remove the invalid entry or entry that have no the test ID
            if (testID != null) {
                rep
                    .addResult(testID + getStore().getResultExtension(),
                        testRes);
            }
        } catch (Exception e) {
            System.out.println("addResult. Unexpected exception " + e);
        }
        return false;
    }

    public int run(String[] args) {
        curCore = this;
        internalLog = new InternalTHLogger();
        internalLog.init(Level.SEVERE);
        parseParam(args);
        generateReport();
        return 0;
    }

    public static void main(String[] args) {
        System.exit((new CmpResults().run(args)));
    }
}