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
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;

import org.apache.harmony.harness.ConfigIR;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.plugins.Reporter;

public class ReporterHTML extends Reporter {

    public void genReport() {
        boolean override = cfg.getResOver();
        File curFile;
        FileWriter file;
        String unkNumb = "";
        String name;

        try {
            name = Main.getCurCore().getConfigIR().getTestResultRoot()
                + File.separator + "report.html";
            curFile = new File(name);
            if (unkStat.size() == 1) {
                unkNumb = " (with 1 status: " + unkStat.keySet().toArray()[0]
                    + ")";
            } else {
                unkNumb = " (with " + unkStat.size() + " different status(es))";
            }
            file = new FileWriter(curFile);
            file.write("<html>\n" + "<body>\n"
                + "<a name=\"top\"><h1>Report</h1></a>\n" + "<table>"
                + getMarkedGenInfo() + getMarkedConfig() + "\n</table>");
            file.write("\n<h3><a name=\"error\">Tests with error</a></h3>\n"
                + "\n<ol>");
            sortPrintList(errorList, file);//printList(errorList, file);
            file
                .write("</ol><a href=\"#top\">top</a>\n<h3><a name=\"failed\">Failed test</a></h3>\n"
                    + "\n<ol>");
            sortPrintList(failedList, file);//printList(failedList, file);
            file
                .write("</ol><a href=\"#top\">top</a>\n<h3><a name=\"unknown\">Test with unknown execution status</a></h3>\n"
                    + "\n<ol>");
            sortPrintList(unkList, file);//printList(unkList, file);
            file
                .write("</ol><a href=\"#top\">top</a>\n<h3><a name=\"skipped\">Skipped tests</a></h3>\n"
                    + "\n<ol>");
            sortPrintList(mErrList, file);//printList(mErrList, file);
            file
                .write("</ol><a href=\"#top\">top</a>\n<h3><a name=\"passed\">Passed tests</a></h3>\n"
                    + "\n<ol>");
            sortPrintList(passedList, file);//printList(passedList, file);
            file.write("</ol><a href=\"#top\">top</a>\n</body>\n<html>");
            file.flush();
            file.close();
        } catch (IOException e) {
            log.add(Level.SEVERE,
                "Reporter. Unexpected exception while generate report " + e);
        }
        reportToConsole();
    }

    protected void printList(ArrayList list, FileWriter file)
        throws IOException {
        if (list == null || list.size() == 0) {
            return;
        } else {
            int num = list.size();
            for (int i = 0; i < num; i++) {
                file.write("\t<li>" + "<a href=\"" + (String)list.get(i)
                    + "\">" + (String)list.get(i) + "</a>" + "</li>\n");
            }
            file.flush();
        }
    }

    protected void sortPrintList(ArrayList list, FileWriter file)
        throws IOException {
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
                file.write("\t<li>" + "<a href=\"" + name + "\">" + name
                    + "</a>" + "</li>\n");
                sorted.remove(name);
            }
            file.flush();
        }
    }

    protected String getMarkedGenInfo() {
        return "\n\t<tr><td colspan=\"2\" align=\"center\"><h3>General information</h3></td></tr>"
            + "\n\t<tr>\n\t\t<td width=\"100\">date</td>\n\t\t<td>"
            + new Date().toString()
            + "</td>\n\t</tr>\n\t<tr>\n\t\t<td>Total</td>\n\t\t<td>"
            + total
            + "</td>\n\t</tr>\n\t<tr>\n\t\t<td><a href=\"#passed\">passed</a></td>\n\t\t<td>"
            + passed
            + "</td>\n\t<tr>\n\t\t<td><a href=\"#failed\">failed</a></td>\n\t\t<td>"
            + failed
            + "</td>\n\t</tr>\n\t<tr>\n\t\t<td><a href=\"#error\">error</a></td>\n\t\t<td>"
            + error
            + "</td>\n\t<tr>\n\t\t<td><a href=\"#skipped\">skipped</a></td>\n\t\t<td>"
            + modeErr
            + "</td>\n\t</tr>\n\t<tr>\n\t\t<td><a href=\"#unknown\">unknown</a></td>\n\t\t<td>"
            + unknown + "\n\t\t</td>\n\t</tr>\n";
    }

    protected String getGenInfo() {
        return "\n\t<tr><td colspan=\"2\" align=\"center\"><h3>General information</h3></td></tr>"
            + "\n\t<tr>\n\t\t<td width=\"100\">date</td>\n\t\t<td>"
            + new Date().toString()
            + "</td>\n\t</tr>\n\t<tr>\n\t\t<td>Total</td>\n\t\t<td>"
            + total
            + "</td>\n\t</tr>\n\t<tr>\n\t\t<td>passed</td>\n\t\t<td>"
            + passed
            + "</td>\n\t<tr>\n\t\t<td>failed</td>\n\t\t<td>"
            + failed
            + "</td>\n\t</tr>\n\t<tr>\n\t\t<td>error</td>\n\t\t<td>"
            + error
            + "</td>\n\t<tr>\n\t\t<td>skipped</td>\n\t\t<td>"
            + modeErr
            + "</td>\n\t</tr>\n\t<tr>\n\t\t<td>unknown</td>\n\t\t<td>"
            + unknown + "\n\t\t</td>\n\t</tr>\n";
    }

    protected String getMarkedConfig() {
        ConfigIR cfg = Main.getCurCore().getConfigIR();
        String mode;
        String testSuiteVersion;
        if (Main.getCurCore().getConfigIR().getExecM() == Main.SAME) {
            mode = "same VM";
        } else {
            mode = "other VM";
        }
        if (Main.getCurCore().getConfigIR().getLocalM() == Main.LOCAL) {
            mode = mode + ", local";
        } else {
            mode = mode + ", remote";
        }
        testSuiteVersion = Main.getCurCore().getConfigIR().getProperty(
            "TestBaseVersion");
        return "\n\t<tr><td colspan=\"2\" align=\"center\"><h3>Configuration</h3></td></tr>"
            + "\n\t<tr>\n\t\t<td>test suite root</td>\n\t\t<td>"
            + "<a href=\""
            + cfg.getTestSuiteRoot()
            + "\">"
            + cfg.getTestSuiteRoot()
            + "</a>"
            + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>test suite version</td>\n\t\t<td>"
            + testSuiteVersion
            + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>test root</td>\n\t\t<td>"
            + cfg.getTestSuiteTestRoot()
            + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>test class root</td>\n\t\t<td>"
            + cfg.getTestSuiteClassRoot()
            + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>tested runtime</td>\n\t\t<td>"
            + cfg.getTestedRuntime()
            + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>execution mode</td>\n\t\t<td>"
            + mode
            + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>path to env_cfg.xml file</td>\n\t\t<td>"
            + "<a href=\""
            + Main.getCurCore().getCfgPath()
            + File.separator
            + "cfg_env.xml"
            + "\">"
            + Main.getCurCore().getCfgPath()
            + "</a>"
            + "\n\t\t</td>\n\t</tr>";
    }

    protected String getConfig() {
        ConfigIR cfg = Main.getCurCore().getConfigIR();
        String mode;
        String testSuiteVersion;
        if (Main.getCurCore().getConfigIR().getExecM() == Main.SAME) {
            mode = "same VM";
        } else {
            mode = "other VM";
        }
        if (Main.getCurCore().getConfigIR().getLocalM() == Main.LOCAL) {
            mode = mode + ", local";
        } else {
            mode = mode + ", remote";
        }
        testSuiteVersion = Main.getCurCore().getConfigIR().getProperty(
            "TestBaseVersion");
        return "\n\t<tr><td colspan=\"2\" align=\"center\"><h3>Configuration</h3></td></tr>"
            + "\n\t<tr>\n\t\t<td>test suite root</td>\n\t\t<td>"
            + cfg.getTestSuiteRoot()
            + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>test suite version</td>\n\t\t<td>"
            + testSuiteVersion
            + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>test root</td>\n\t\t<td>"
            + cfg.getTestSuiteTestRoot()
            + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>test class root</td>\n\t\t<td>"
            + cfg.getTestSuiteClassRoot()
            + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>tested runtime</td>\n\t\t<td>"
            + cfg.getTestedRuntime()
            + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>execution mode</td>\n\t\t<td>"
            + mode
            + "\n\t</td>\n\t</tr>\n\t<tr>\n\t\t<td>path to env_cfg.xml file</td>\n\t\t<td>"
            + Main.getCurCore().getCfgPath() + "\n\t\t</td>\n\t</tr>";
    }
}