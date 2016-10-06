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
 * @version $Revision: 1.34 $
 */
package org.apache.harmony.harness.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.apache.harmony.harness.ConfigIR;
import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.EnvProperty;
import org.apache.harmony.harness.ExecUnit;
import org.apache.harmony.harness.Logging;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.Report;

public class Reporter implements Report {

    private static final String classID         = "Reporter";

    public static final String  OLD_RES_SUF     = ".old";
    public static final String  ENV_FILE_NAME   = "thrun.env";
    public static final String  PARAM_FILE_NAME = "thrun.prm";

    protected int               total           = 0;
    protected int               passed          = 0;
    protected ArrayList         passedList      = new ArrayList();
    protected int               failed          = 0;
    protected ArrayList         failedList      = new ArrayList();
    protected int               error           = 0;
    protected ArrayList         errorList       = new ArrayList();
    protected int               modeErr         = 0;
    protected ArrayList         mErrList        = new ArrayList();
    protected int               unknown         = 0;
    protected ArrayList         unkList         = new ArrayList();

    protected ConfigIR          cfg             = Main.getCurCore()
                                                    .getConfigIR();
    protected final int[]       pasVal          = cfg.getRepPassed();
    protected final int[]       fldVal          = cfg.getRepFailed();
    protected final int[]       errVal          = cfg.getRepError();
    protected final int[]       merVal          = cfg.getRepModErr();

    protected Logging           log             = Main.getCurCore()
                                                    .getInternalLogger();
    protected HashMap           unkStat         = new HashMap();
    protected HashMap           allTestRes      = new HashMap();

    protected void writeXSLforReport(FileWriter out) throws IOException {
        out
            .write("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
                + "\t<xsl:template match=\"/\">\n"
                + "\t\t<h1>General report</h1>\n"
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
                + "\t\t\t<th colspan=\"2\" align=\"center\">Passed tests</th></tr>\n"
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
                + "\t\t\t<th colspan=\"2\" align=\"center\">Failed tests</th></tr>\n"
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
                + "\t\t\t<th colspan=\"2\" align=\"center\">Tests with error</th></tr>\n"
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
                //+ "\t\t\t\t<td><a href=\"{.}\"><xsl:value-of
                // select=\".\"/></a></td>\n"
                + "\t\t\t\t<td><xsl:value-of select=\".\"/></td>\n"
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
                + "</xsl:stylesheet>");
        out.flush();
    }

    private boolean getWriteOption(String name) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tgetWriteOption(): ";
        ArrayList tmp = cfg.getPluginProperties("Reporter");
        if (tmp != null) {
            try {
                HashMap param = (HashMap)((HashMap)tmp.get(1)).get("write");
                if (param != null && ((ArrayList)param.get(name)).size() > 0) {
                    String tmpVal = (String)((ArrayList)param.get(name)).get(0);
                    if (tmpVal.equalsIgnoreCase("false")) {
                        return false;
                    }
                }
            } catch (Exception e) {
                log.add(Level.FINEST, methodLogPrefix
                    + MessageInfo.UNEX_EXCEPTION
                    + "while parse the configuration parameters " + e, e);
            }
        }
        return true;
    }

    protected void writeXSLforTestsWS(FileWriter out) throws IOException {
        String handlerSpecInfo = "<a href=\"{.}\"><xsl:value-of select=\".\"/></a>";
        if (!getWriteOption("allinfo")) {
            handlerSpecInfo = "";
        }
        out
            .write("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
                + "\t<xsl:template match=\"/\">\n"
                + "\t\t<h1>Test report</h1>\n"
                + "\t\t<xsl:apply-templates/>\n"
                + "\t</xsl:template>\n"
                + "\t<xsl:template match=\"property-list\">\n"
                + "\t\t<p><table>\n"
                + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                + "\t\t\t<th colspan=\"2\" align=\"center\">Results</th></tr>\n"
                + "\t\t\t<xsl:for-each select=\"property-item\">\n"
                + "\t\t\t\t<tr><td valign=\"top\"><strong><xsl:value-of select=\"./@name\"/></strong></td>\n"
                + "\t\t\t\t<td>\n"
                + "\t\t\t\t<xsl:variable name=\"htmlBR\">\n"
                + "\t\t\t\t\t<xsl:element name=\"br\"/>\n"
                + "\t\t\t\t\t<xsl:text>&#xA;</xsl:text>\n"
                + "\t\t\t\t</xsl:variable>\n"
                + "\t\t\t\t<xsl:choose>\n"
                + "\t\t\t\t\t<xsl:when test=\"@name='cmd'\">\n"
                + "\t\t\t\t\t\t<xsl:variable name=\"tmp\">\n"
                + "\t\t\t\t\t\t\t<xsl:call-template name=\"replace\">\n"
                + "\t\t\t\t\t\t\t\t<xsl:with-param name=\"str\" select=\".\"/>\n"
                + "\t\t\t\t\t\t\t\t<xsl:with-param name=\"from\" select=\"'&#xA;'\"/>\n"
                + "\t\t\t\t\t\t\t\t<xsl:with-param name=\"to\" select=\"$htmlBR\"/>\n"
                + "\t\t\t\t\t\t\t</xsl:call-template>\n"
                + "\t\t\t\t\t\t</xsl:variable>\n"
                + "\t\t\t\t\t\t<xsl:call-template name=\"replace\">\n"
                + "\t\t\t\t\t\t\t<xsl:with-param name=\"str\" select=\"$tmp\"/>\n"
                + "\t\t\t\t\t\t\t<xsl:with-param name=\"from\" select=\"'&#x3b;'\"/>\n"
                + "\t\t\t\t\t\t\t<xsl:with-param name=\"to\" select=\"'&#x3b;&#x20;'\"/>\n"
                + "\t\t\t\t\t\t</xsl:call-template>\n"
                + "\t\t\t\t\t</xsl:when>\n"
                + "\t\t\t\t\t<xsl:otherwise>\n"
                + "\t\t\t\t\t\t<xsl:call-template name=\"replace\">\n"
                + "\t\t\t\t\t\t\t<xsl:with-param name=\"str\" select=\".\"/>\n"
                + "\t\t\t\t\t\t\t<xsl:with-param name=\"from\" select=\"'&#xA;'\"/>\n"
                + "\t\t\t\t\t\t\t<xsl:with-param name=\"to\" select=\"$htmlBR\"/>\n"
                + "\t\t\t\t\t\t</xsl:call-template>\n"
                + "\t\t\t\t\t</xsl:otherwise>\n"
                + "\t\t\t\t</xsl:choose>\n"
                + "\t\t\t\t</td></tr>\n"
                + "\t\t\t</xsl:for-each>\n"
                + "\t\t</table></p>\n"
                + "\t</xsl:template>\n"
                + "\t<xsl:template match=\"info-list\">\n"
                + "\t\t<p><table>\n"
                + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                + "\t\t\t<th colspan=\"2\" align=\"center\">Additional information</th></tr>\n"
                + "\t\t\t<xsl:for-each select=\"info-item\">\n"
                + "\t\t\t\t<tr><td valign=\"top\"><strong><xsl:value-of select=\"./@name\"/></strong></td>\n"
                + "\t\t\t\t<td>"
                + handlerSpecInfo
                + "</td>\n"
                + "\t\t\t\t</tr>\n"
                + "\t\t\t</xsl:for-each>\n"
                + "\t\t</table></p>\n"
                + "\t</xsl:template>\n\n"
                + "\t<xsl:template name=\"replace\">\n"
                + "\t\t<xsl:param name=\"str\"/>\n"
                + "\t\t<xsl:param name=\"from\"/>\n"
                + "\t\t<xsl:param name=\"to\"/>\n\n"
                + "\t\t<xsl:choose>\n"
                + "\t\t\t<xsl:when test=\"contains($str, $from)\">\n"
                + "\t\t\t\t<xsl:value-of select=\"substring-before($str, $from)\"/>\n"
                + "\t\t\t\t<xsl:copy-of select=\"$to\"/>\n"
                + "\t\t\t\t<xsl:call-template name=\"replace\">\n"
                + "\t\t\t\t\t<xsl:with-param name=\"str\" select=\"substring-after($str, $from)\"/>\n"
                + "\t\t\t\t\t<xsl:with-param name=\"from\" select=\"$from\"/>\n"
                + "\t\t\t\t\t<xsl:with-param name=\"to\" select=\"$to\"/>\n"
                + "\t\t\t\t</xsl:call-template>\n"
                + "\t\t\t</xsl:when>\n"
                + "\t\t\t<xsl:otherwise>\n"
                + "\t\t\t\t<xsl:value-of select=\"$str\"/>\n"
                + "\t\t\t</xsl:otherwise>\n"
                + "\t\t</xsl:choose>\n"
                + "\t</xsl:template>\n" + "</xsl:stylesheet>");
        out.flush();
    }

    protected void writeXSLforTests(FileWriter out) throws IOException {
        String handlerSpecInfo = "<a href=\"{.}\"><xsl:value-of select=\".\"/></a>";
        if (!getWriteOption("allinfo")) {
            handlerSpecInfo = "";
        }
        out
            .write("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
                + "\t<xsl:template match=\"/\">\n"
                + "\t\t<h1>Test report</h1>\n"
                + "\t\t<xsl:apply-templates/>\n"
                + "\t</xsl:template>\n"
                + "\t<xsl:template match=\"property-list\">\n"
                + "\t\t<p><table>\n"
                + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                + "\t\t\t<th colspan=\"2\" align=\"center\">Results</th></tr>\n"
                + "\t\t\t<xsl:for-each select=\"property-item\">\n"
                + "\t\t\t\t<tr><td valign=\"top\"><strong><xsl:value-of select=\"./@name\"/></strong></td>\n"
                + "\t\t\t\t<td>\n"
                + "\t\t\t\t\t<xsl:if test=\"@name='outMsg' or @name='env'\">\n"
                + "\t\t\t\t\t\t<PRE><xsl:value-of select=\".\"/></PRE>\n"
                + "\t\t\t\t\t</xsl:if>\n"
                + "\t\t\t\t\t<xsl:if test=\"@name!='outMsg' and @name!='env'\">\n"
                + "\t\t\t\t\t\t<xsl:value-of select=\".\"/>\n"
                + "\t\t\t\t\t</xsl:if>\n"
                + "\t\t\t\t</td></tr>\n"
                + "\t\t\t</xsl:for-each>\n"
                + "\t\t</table></p>\n"
                + "\t</xsl:template>\n"
                + "\t<xsl:template match=\"info-list\">\n"
                + "\t\t<p><table>\n"
                + "\t\t\t<tr bgcolor=\"#CCCCCC\">\n"
                + "\t\t\t<th colspan=\"2\" align=\"center\">Additional information</th></tr>\n"
                + "\t\t\t<xsl:for-each select=\"info-item\">\n"
                + "\t\t\t\t<tr><td valign=\"top\"><strong><xsl:value-of select=\"./@name\"/></strong></td>\n"
                + "\t\t\t\t<td><a href=\"{.}\"><xsl:value-of select=\".\"/></a></td>\n"
                + "\t\t\t\t</tr>\n"
                + "\t\t\t</xsl:for-each>\n"
                + "\t\t</table></p>\n"
                + "\t</xsl:template>\n"
                + "</xsl:stylesheet>");
        out.flush();
    }

    protected File checkOverwriteAndRename(File file) {
        boolean overwrite = cfg.getResOver();
        if (!overwrite && file.exists()) {
            File dest = new File(file.getPath() + OLD_RES_SUF);
            if (dest.exists()) {
                dest.delete();
            }
            file.renameTo(dest);
        }
        return file;
    }

    protected void writeCurrentEnv(FileWriter out) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\twriteCurrentEnv(): ";
        try {
            if (out != null) {
                out
                    .write("/* This is full environment that was set for harness run (including\n"
                        + " * general environment from configuration file).\n"
                        + " * Note, if the run mode is 'remote' this environment may differ from\n"
                        + " * tests execution environment.\n */\n");
                EnvProperty envp = new EnvProperty();
                envp.setInheritSystemEnv(true);
                String[] genEnv = cfg.getGeneralEnvAsArray();
                boolean need_remove = false;
                if (genEnv != null) {
                    ExecUnit eunit = Main.getCurCore().getRunner("Runtime");
                    if (eunit != null) {
                        genEnv = eunit.subsEnvToValue(genEnv);
                        for (int i = 0; i < genEnv.length; i++) {
                            envp.addEnv(genEnv[i]);
                        }
                    }
                } else {
                    need_remove = true;
                    envp
                        .addEnv("harness_reporter_need_full_info_about_env_13524");
                }
                String[] data = envp.getEnv();
                Arrays.sort(data);
                if (data != null) {
                    for (int i = 0; i < data.length; i++) {
                        if (need_remove) {
                            if (!data[i]
                                .startsWith("harness_reporter_need_full_info_about_env_13524")) {
                                out.write(data[i] + "\n");
                            }
                        } else {
                            out.write(data[i] + "\n");
                        }
                    }
                }
                out.flush();
            }
        } catch (Exception e) {
            log.add(Level.FINER, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + "while generate environment file " + e, e);
        }

    }

    protected void writeCurrentParam(FileWriter out) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\twriteCurrentParam(): ";
        try {
            if (out != null) {
                out
                    .write("/* This is full configuration based on configuration files and"
                        + "* command line options.\n */\n");
                out.write("/* general suite/ run data\n */\n");
                out.write("TestSuiteRoot=" + cfg.getTestSuiteRoot() + "\n");
                out.write("TestSuiteTestRoot=" + cfg.getTestSuiteTestRoot()
                    + "\n");
                out.write("TestSuiteConfigRoot=" + cfg.getTestSuiteConfigRoot()
                    + "\n");
                out.write("TestSuiteClassRoot=" + cfg.getTestSuiteClassRoot()
                    + "\n");
                out.write("TestSuiteLibRoot=" + cfg.getTestSuiteLibRoot()
                    + "\n");
                out.write("TestResultRoot=" + cfg.getTestResultRoot() + "\n");
                out.write("TempStorage=" + cfg.getTempStorage() + "\n");
                out.write("TestedRuntime=" + cfg.getTestedRuntime() + "\n");
                out.write("TestedCompile=" + cfg.getTestedCompile() + "\n");
                out.write("ReferenceRuntime=" + cfg.getReferenceRuntime()
                    + "\n");
                out.write("ReferenceCompile=" + cfg.getReferenceCompile()
                    + "\n");
                out.write("GenTimeout=" + cfg.getGenTimeout() + "\n");
                out.write("LogLevel=" + cfg.getLogLevel() + "\n");

                out.write("\n\n/* common execution options\n */\n");
                HashMap data = cfg.getProperties();
                Iterator it = data.keySet().iterator();
                String[] dataArr = new String[data.size()];
                int i = 0;
                while (it.hasNext()) {
                    String key = it.next().toString();
                    dataArr[i++] = key + "=" + data.get(key) + "\n";
                }
                Arrays.sort(dataArr);
                for (i = 0; i < dataArr.length; i++) {
                    out.write(dataArr[i]);
                }
                out.write("\n\n/* plugins options\n */\n");
                HashMap plugins = cfg.getPlugins();
                it = plugins.keySet().iterator();
                dataArr = new String[plugins.size()];
                i = 0;
                while (it.hasNext()) {
                    String key = it.next().toString();
                    dataArr[i++] = key;
                }
                Arrays.sort(dataArr);
                for (i = 0; i < dataArr.length; i++) {
                    out.write("\n/* plugins options for the " + dataArr[i]
                        + "*/\n");
                    ArrayList pParams = (ArrayList)plugins.get(dataArr[i]);
                    out.write(dataArr[i] + "_class=" + pParams.get(0) + "\n");
                    for (int cnt = 1; cnt < pParams.size(); cnt++) {
                        HashMap pParam = (HashMap)pParams.get(cnt);
                        String[] pParamArr = new String[pParam.size()];
                        Iterator paramIt = pParam.keySet().iterator();
                        int y = 0;
                        while (paramIt.hasNext()) {
                            String key = paramIt.next().toString();
                            pParamArr[y++] = "\t" + key + "=" + pParam.get(key)
                                + "\n";
                        }
                        Arrays.sort(pParamArr);
                        for (y = 0; y < pParamArr.length; y++) {
                            out.write(pParamArr[y]);
                        }
                    }
                }
                out.flush();
            }
        } catch (Exception e) {
            log.add(Level.FINER, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + "while generate file with run parameters " + e, e);
        }

    } /*
       * (non-Javadoc)
       * 
       * @see org.apache.harmony.harness.Report#genReport()
       */

    public void genReport() {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tgenReport(): ";
        File curFile;
        FileWriter file;
        String unkNumb = "";

        try {
            String name = Main.getCurCore().getConfigIR().getTestResultRoot()
                + File.separator + "report.xsl";
            curFile = checkOverwriteAndRename(new File(name));
            file = new FileWriter(curFile);
            writeXSLforReport(file);
            file.close();

            name = Main.getCurCore().getConfigIR().getTestResultRoot()
                + File.separator + ENV_FILE_NAME;
            curFile = checkOverwriteAndRename(new File(name));
            file = new FileWriter(curFile);
            writeCurrentEnv(file);
            file.close();

            name = Main.getCurCore().getConfigIR().getTestResultRoot()
                + File.separator + PARAM_FILE_NAME;
            curFile = checkOverwriteAndRename(new File(name));
            file = new FileWriter(curFile);
            writeCurrentParam(file);
            file.close();

            name = Main.getCurCore().getConfigIR().getTestResultRoot()
                + File.separator + "test.xsl";
            curFile = checkOverwriteAndRename(new File(name));
            file = new FileWriter(curFile);
            if (getWriteOption("addwhitespace")) {
                writeXSLforTestsWS(file);
            } else {
                writeXSLforTests(file);
            }
            file.close();

            name = Main.getCurCore().getConfigIR().getTestResultRoot()
                + File.separator + "report.xml";
            curFile = checkOverwriteAndRename(new File(name));

            if (unkStat.size() == 1) {
                unkNumb = " (with 1 status: " + unkStat.keySet().toArray()[0]
                    + ")";
            } else {
                unkNumb = " (with " + unkStat.size() + " different status(es))";
            }
            file = new FileWriter(curFile);
            file.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<?xml-stylesheet type='text/xsl' href='report.xsl'?>\n"
                + "<Report>\n"
                + "<property-list>\n\t<property-item name=\"date\">"
                + new Date().toString()
                + "</property-item>\n\t<property-item name=\"Total\">" + total
                + "</property-item>\n\t<property-item name=\"passed\">"
                + passed
                + "</property-item>\n\t<property-item name=\"failed\">"
                + failed + "</property-item>\n\t<property-item name=\"error\">"
                + error
                + "</property-item>\n\t<property-item name=\"skipped\">"
                + modeErr
                + "</property-item>\n\t<property-item name=\"unknown\">"
                + unknown + unkNumb + "</property-item>\n</property-list>\n"
                + "<cfg-list>\n");
            file.flush();
            getMarkedConfig(file);
            file.write("\n</cfg-list>\n<error-list>\n");
            printList(errorList, file);
            file.write("</error-list>\n<failed-list>\n");
            printList(failedList, file);
            file.write("</failed-list>\n<unknown-list>\n");
            printList(unkList, file);
            file.write("</unknown-list>\n<modeError-list>\n");
            printList(mErrList, file);
            file.write("</modeError-list>\n<passed-list>\n");
            printList(passedList, file);
            file.write("</passed-list>\n</Report>");
            file.flush();
            file.close();
        } catch (IOException e) {
            log.add(Level.SEVERE, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + "while generate report " + e, e);
        }
        reportToConsole();
    }

    protected void reportToConsole() {
        String unkNumb = "";
        if (!unkStat.isEmpty()) {
            if (unkStat.size() == 1) {
                unkNumb = " (with 1 status: " + unkStat.keySet().toArray()[0]
                    + ")";
            } else {
                unkNumb = " (with " + unkStat.size() + " different status(es))";
            }
        }
        log.add("tests total\t" + total + "\n  passed\t" + passed
            + "\n  failed\t" + failed + "\n  error\t\t" + error
            + "\n  skipped\t" + modeErr + "\n  unknown\t" + unknown + unkNumb);
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

    protected String getMarkedConfig(FileWriter file) throws IOException {
        ConfigIR cfg = Main.getCurCore().getConfigIR();
        String mode;
        String testSuiteVersion;
        String testSuiteVersionComment;
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
        testSuiteVersionComment = Main.getCurCore().getConfigIR().getProperty(
            "TestBaseVersionComment");
        if (!Constants.NO_VALUE_MSG.equals(testSuiteVersionComment)) {
            testSuiteVersion = testSuiteVersion + " ("
                + testSuiteVersionComment + ")";
        }
        file.write("\t<cfg-item name=\"test suite root\">"
            + cfg.getTestSuiteRoot()
            + "</cfg-item>\n\t<cfg-item name=\"test suite version\">"
            + testSuiteVersion + "</cfg-item>\n\t<cfg-item name=\"test root\">"
            + cfg.getTestSuiteTestRoot()
            + "</cfg-item>\n\t<cfg-item name=\"test class root\">"
            + cfg.getTestSuiteClassRoot()
            + "</cfg-item>\n\t<cfg-item name=\"tested runtime\">"
            + cfg.getTestedRuntime()
            + "</cfg-item>\n\t<cfg-item name=\"execution mode\">" + mode
            + "</cfg-item>\n\t<cfg-item name=\"path to env_cfg.xml file\">"
            + Main.getCurCore().getCfgPath()
            + "</cfg-item>\n\t<cfg-item name=\"path to system env file\">"
            + Main.getCurCore().getConfigIR().getTestResultRoot()
            + File.separator + "thrun.env" + "</cfg-item>");
        file.flush();
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Report#addResult(java.lang.String)
     */
    public boolean addResult(String testID, int status) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\taddResult(): ";
        synchronized (passedList) {
            if (allTestRes.containsKey(testID)) {
                log.add(Level.WARNING, methodLogPrefix + "The status for test "
                    + testID + " was reported more than 1 time. Check it.");
            } else {
                total++;
                allTestRes.put(testID, new Integer(status));
            }
        }
        for (int i = 0; i < errVal.length; i++) {
            if (status == errVal[i]) {
                synchronized (errorList) {
                    error++;
                    errorList.add(testID);
                    return true;
                }
            }
        }
        for (int i = 0; i < fldVal.length; i++) {
            if (status == fldVal[i]) {
                synchronized (failedList) {
                    failed++;
                    failedList.add(testID);
                    return true;
                }
            }
        }
        for (int i = 0; i < merVal.length; i++) {
            if (status == merVal[i]) {
                synchronized (mErrList) {
                    modeErr++;
                    mErrList.add(testID);
                    return true;
                }
            }
        }
        for (int i = 0; i < pasVal.length; i++) {
            if (status == pasVal[i]) {
                synchronized (passedList) {
                    passed++;
                    passedList.add(testID);
                    return true;
                }
            }
        }
        synchronized (unkList) {
            unknown++;
            unkList.add(testID);
            Object key = new Integer(status);
            ArrayList tmp;
            if (unkStat.containsKey(key)) {
                tmp = (ArrayList)unkStat.get(key);
            } else {
                tmp = new ArrayList();
            }
            tmp.add(testID);
            unkStat.put(key, tmp);
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Report#getResults()
     */
    public HashMap getResults() {
        return (HashMap)allTestRes.clone();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Report#clear()
     */
    public void clear() {
        synchronized (passedList) {
            total = 0;
            passed = 0;
            allTestRes.clear();
            passedList.clear();
        }
        synchronized (failedList) {
            failed = 0;
            failedList.clear();
        }
        synchronized (errorList) {
            error = 0;
            errorList.clear();
        }
        synchronized (mErrList) {
            modeErr = 0;
            mErrList.clear();
        }
        synchronized (unkList) {
            unknown = 0;
            unkList.clear();
            unkStat.clear();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Report#allTestCnt()
     */
    public int allTestCnt() {
        return total;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Report#passedTestCnt()
     */
    public int passedTestCnt() {
        return passed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Report#failedTestCnt()
     */
    public int failedTestCnt() {
        return failed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Report#errorTestCnt()
     */
    public int errorTestCnt() {
        return error;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Report#skippedTestCnt()
     */
    public int skippedTestCnt() {
        return modeErr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.harmony.harness.Report#unspecifiedTestCnt()
     */
    public int unspecifiedTestCnt() {
        return unknown;
    }
}
