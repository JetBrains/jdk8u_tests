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
 * @author Valery Shakurov
 * @version $Revision: 1.12 $
 */
package org.apache.harmony.harness.plugins.variationtests;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.harmony.harness.plugins.StoreRes;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.TResIR;

/*
 * This class looks for particular TResIR properties which specify output
 * filters as well as information about variations and configuration parameters
 * (e.g. VariationTestFinder can store these in TestIR)
 */
public class FilterStoreRes extends StoreRes {

    private final String classID = "FilterStoreRes";

    /*
     * This method generates test file contents based on passed TResIR
     * 
     * @see org.apache.harmony.harness.Storage#add(org.apache.harmony.harness.TResIR)
     */
    public boolean add(TResIR test) {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tadd(): ";
        try {
            // add result to reporter module.
            if (rep != null) {
                if (test.getRepFile() != null) {
                    rep.addResult(test.getRepFile() + getResultExtension(),
                        test.getExecStat());
                } else { //use the default rules for report file name
                    rep.addResult(test.getTestID() + getResultExtension(), test
                        .getExecStat());
                }
            }
            if (resStore == null) {
                log.add(Level.INFO, methodLogPrefix
                    + "Can not store the result. Please, check your settings.");
                return false;
            }
            //some service information
            String path = "";
            String srcPath = "";
            String msg = convertInfoToSpecificFormat(test.getOutMsg());
            String testInfo = convertInfoToSpecificFormat(test
                .getTestSpecificInfo());
            String resultMsg = " (UNKNOWN)";
            String tmpVal;
            String time = " ";
            String[] srcNames = null;
            int testStatus = test.getExecStat();
            if (msg == null) {
                msg = "";
            } else {
                msg = sortByTimestamp(msg);
            }
            if (testInfo == null) {
                testInfo = "";
            }
            // note: this result is parsed by result parser which expect the
            // digit before the whitespace
            for (int i = 0; i < pasVal.length; i++) {
                if (testStatus == pasVal[i]) {
                    resultMsg = " (PASSED)";
                }
            }
            for (int i = 0; i < fldVal.length; i++) {
                if (testStatus == fldVal[i]) {
                    resultMsg = " (FAILED)";
                }
            }
            for (int i = 0; i < errVal.length; i++) {
                if (testStatus == errVal[i]) {
                    resultMsg = " (ERROR)";
                }
            }
            for (int i = 0; i < merVal.length; i++) {
                if (testStatus == merVal[i]) {
                    resultMsg = " (SKIPPED)";
                }
            }
            char[] tmp = test.getTestID().toCharArray();
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i] == org.apache.harmony.harness.Constants.INTERNAL_FILE_SEP_C) {
                    if (i > 0 && tmp[i - 1] == '.') { //look for /./
                        // construction
                        if (i - 1 > 0
                            && tmp[i - 2] != org.apache.harmony.harness.Constants.INTERNAL_FILE_SEP_C) {
                            path = path + ".." + File.separator;
                        }
                    } else {
                        path = path + ".." + File.separator;
                    }
                }
            }
            srcPath = cfg.getTestSuiteTestRoot()
                + File.separator
                + test.getTestID().replace(
                    org.apache.harmony.harness.Constants.INTERNAL_FILE_SEP_C,
                    File.separatorChar);
            int index = srcPath.lastIndexOf(File.separator);
            srcPath = srcPath.substring(0, index) + File.separator;
            ArrayList tmpArr = (ArrayList)test.getProperty("sources");
            if (tmpArr != null) {
                int realCnt;
                int iter = tmpArr.size();
                srcNames = new String[iter];
                for (int cnt = 0; cnt < iter; cnt++) {
                    srcNames[cnt] = srcPath + tmpArr.get(cnt);
                    if (org.apache.harmony.harness.Util
                        .checkExistFile(srcNames[cnt])) {
                        srcNames[cnt] = (new File(srcNames[cnt]))
                            .getAbsolutePath();
                    } else {
                        srcNames[cnt] = null;
                    }
                }
                realCnt = 0;
                for (int i = 0; i < srcNames.length; i++) {
                    if (srcNames[i] != null) {
                        realCnt++;
                    }
                }
                if (realCnt != srcNames.length && realCnt >= 1) {
                    String[] tmpValArr = new String[realCnt];
                    for (int i = 0; i < srcNames.length; i++) {
                        if (srcNames[i] != null) {
                            tmpValArr[--realCnt] = srcNames[i];
                        }
                    }
                    srcNames = tmpValArr;
                }
            }
            if (srcNames == null) {
                srcNames = new String[1];
                for (int i = 0; i < DEFAULT_SOURCE_EXT.length; i++) {
                    srcNames[0] = cfg.getTestSuiteTestRoot() + File.separator
                        + test.getTestID() + DEFAULT_SOURCE_EXT[i];
                    if (org.apache.harmony.harness.Util
                        .checkExistFile(srcNames[0])) {
                        break;
                    } else {
                        srcNames[0] = null;
                    }
                }
            }
            if (srcNames[0] == null) {
                srcNames[0] = "";
            }

            //retrieve propertyMap inherited from testIR and augment test data
            //with parameters/variants/filtered lines (if applicable)
            HashMap properties = test.getAllProperty();
            HashMap variants = new HashMap();
            HashMap parameters = new HashMap();
            HashMap filters = new HashMap();
            ArrayList resolvedCmds = new ArrayList();
            String[] keys = Util.toStringArr(properties.keySet());
            Arrays.sort(keys);
            for (int k = 0; k < keys.length; k++) {
                String key = keys[k];
                if (key.startsWith(VConstants.VARIANT_PREFIX)) {
                    // retrieve variation information
                    String trunc_key = key.substring(VConstants.VARIANT_PREFIX
                        .length());
                    ArrayList al = (ArrayList)properties.get(key);
                    variants.put(trunc_key, al);
                } else if (key.startsWith(VConstants.CFG_PREFIX)) {
                    // retrieve configuration parameters information
                    String trunc_key = key.substring(VConstants.CFG_PREFIX
                        .length());
                    parameters.put(trunc_key, properties.get(key));
                } else if (key.startsWith(VConstants.FILTER_PREFIX)) {
                    // retrieve filters information
                    String trunc_key = key.substring(VConstants.FILTER_PREFIX
                        .length());
                    try {
                        filters.put(trunc_key, Pattern
                            .compile((String)properties.get(key)));
                    } catch (PatternSyntaxException pse) {
                        // filters.put(trunc_key, null);
                        log
                            .add(Level.INFO, methodLogPrefix
                                + "WARNING: Pattern ["
                                + (String)properties.get(key)
                                + "] didn't compile!");
                    }
                } else if (key.startsWith(VConstants.RESOLVED_CMD_PREFIX)) {
                    // retrieve resolved command lines for all execution blocks
                    resolvedCmds.add(key);
                }
            }

            // contents of this field will be inserted in test result file
            ArrayList additionalData = new ArrayList();
            additionalData.add("<results>");
            additionalData.add("<variants>");

            keys = Util.toStringArr(variants.keySet());
            for (int k = 0; k < variants.size(); k++) {
                String key = keys[k];
                ArrayList variant = (ArrayList)variants.get(key);
                additionalData.add("<variant name='" + Util.toXML(key)
                    + "' description='" + Util.toXML((String)variant.get(0))
                    + "'/>");
            }
            additionalData.add("</variants>");
            additionalData.add("<iteration id='0'>");

            keys = Util.toStringArr(parameters.keySet());
            for (int k = 0; k < parameters.size(); k++) {
                String key = keys[k];
                additionalData.add("<parameter name='" + Util.toXML(key)
                    + "' description='"
                    + Util.toXML((String)parameters.get(key)) + "'/>");
            }

            keys = Util.toStringArr(variants.keySet());
            for (int k = 0; k < variants.size(); k++) {
                String key = keys[k];
                ArrayList variant = (ArrayList)variants.get(key);
                additionalData.add("<variant name='" + Util.toXML(key)
                    + "' description='" + Util.toXML((String)variant.get(0))
                    + "'>" + Util.toXML((String)variant.get(1)) + "</variant>");
            }

            // if there were any filters, apply them to test output
            // and remember matches
            ArrayList output = new ArrayList();
            String[] fkeys = Util.toStringArr(filters.keySet());
            StringTokenizer st = new StringTokenizer(testInfo, "\n\r");
            //for each line in output
            for (; st.hasMoreTokens();) {
                String token = st.nextToken();
                //for each filter
                for (int f = 0; f < fkeys.length; f++) {
                    String fname = fkeys[f];
                    Pattern pat = (Pattern)filters.get(fname);
                    if (pat == null) {
                        continue;
                    }
                    Matcher m = pat.matcher(token);
                    String res = m.matches() ? m.group(1) : null;
                    // store match information
                    if (res != null) {
                        output.add("<item id='" + Util.toXML(fname) + "'>"
                            + Util.toXML(res) + "</item>");
                        filters.put(fname, null);
                    }
                }
            }
            for (int f = 0; f < fkeys.length; f++) {
                String fname = fkeys[f];
                Pattern fil = (Pattern)filters.get(fname);
                // inform about filters that were not matched
                if (filters.get(fname) != null) {
                    log.add(Level.INFO, methodLogPrefix + "WARNING: Filter "
                        + fname
                        + " hasn't been matched, empty element generated!");
                    output.add("<item id='" + Util.toXML(fname) + "'/>");
                }
            }

            // put resolved command line for each execution block into the
            // result file
            String[] resolvedCmdsArr = Util.toStringArr(resolvedCmds);
            ArrayList retCodes = test.getRealExecStats();

            if (retCodes.size() != resolvedCmdsArr.length) {
                log
                    .add(
                        Level.INFO,
                        methodLogPrefix
                            + "WARNING: the number of return codes does not match the number of executed commands, skipping <program> block.");
            } else {
                for (int k = 0; k < resolvedCmdsArr.length; k++) {
                    ArrayList al = (ArrayList)properties
                        .get(resolvedCmdsArr[k]);
                    if (al == null || al.size() < 2) {
                        continue;
                    }
                    String[] seq = Util.toStringArr(al);
                    String title = seq[0];
                    String exec = seq[1];
                    additionalData.add("<program title='" + Util.toXML(title)
                        + "' executable='" + Util.toXML(exec) + "'>");
                    for (int j = 1; j < seq.length; j++) {
                        additionalData.add("<arg>" + Util.toXML(seq[j])
                            + "</arg>");
                    }
                    additionalData.add("<output>");
                    additionalData.addAll(output);
                    additionalData.add("</output>");
                    additionalData.add("<return code='" + retCodes.get(k)
                        + "'/>");
                    additionalData.add("</program>");
                }
            }
            additionalData.add("</iteration>");
            additionalData.add("</results>");

            String additionalInfo = convertInfoToSpecificFormat(Util
                .toStringArr(additionalData));

            ArrayList tmpAL = test.getExecTime();
            if (tmpAL.size() > 0) {
                long totalTime = 0;
                time = tmpAL.size() + " parts";
                for (int i = 0; i < tmpAL.size(); i++) {
                    time = time + ". Part " + i + ": "
                        + tmpAL.get(i).toString();
                    totalTime = totalTime + ((Long)tmpAL.get(i)).longValue();
                }
                time = "Total time: " + totalTime + ". " + time;
            }
            resStore
                .write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + path
                    + "test.xsl\"?>\n"
                    + "<TestRes>\n"
                    + "<property-list>\n\t<property-item name=\"OS\">"
                    + test.getTestedOS()
                    + "</property-item>\n\t<property-item name=\"Platform\">"
                    + test.getTestedPlatform()
                    + "</property-item>\n\t<property-item name=\"Run VM\">"
                    + test.getTestedVM()
                    + "</property-item>\n\t<property-item name=\"DATE\">"
                    + test.getDate()
                    + "</property-item>\n\t<property-item name=\"TestID\"><![CDATA["
                    + test.getTestID()
                    + "]]>"
                    + "</property-item>\n\t<property-item name=\"cmd\"><![CDATA["
                    + test.getExecCmdAsString(null)
                    + "]]>"
                    + "</property-item>\n\t<property-item name=\"exectime\">"
                    + time
                    + "</property-item>\n\t<property-item name=\"Status\">"
                    + test.getExecStat()
                    + resultMsg
                    + "</property-item>\n\t<property-item name=\"outMsg\"><![CDATA["
                    + msg
                    + "]]>"
                    + "</property-item>\n\t<property-item name=\"testSpecificInfo\">"
                    + testInfo + "\n" + additionalInfo
                    + "</property-item>\n</property-list>\n" + "<info-list>\n"
                    + tagStrings(srcNames, "info-item", "source file", true)
                    + "</info-list>\n" + "</TestRes>");
            resStore.flush();
            log.add(Level.INFO, methodLogPrefix + "Test was run "
                + test.getTestID() + " result is " + resultMsg);
            return true;
        } catch (Exception e) {
            log.add(Level.WARNING, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + "while writing the report file(s) for test: "
                + test.getTestID() + "\n" + e, e);
            return false;
        }
    }
}