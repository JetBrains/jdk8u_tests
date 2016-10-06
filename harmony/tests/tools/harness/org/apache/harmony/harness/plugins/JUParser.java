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
 * @author Y.Tokpanov
 * @author Vladimir A. Ivanov
 * @version $Revision: 1.16 $
 */
package org.apache.harmony.harness.plugins;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.harmony.harness.Constants;
import org.apache.harmony.harness.Logging;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.ParserException;
import org.apache.harmony.harness.TestIR;

import junit.framework.TestCase;

public class JUParser {

    private final String classID = "JUParser";

    private TestIR       testIR;
    private Logging      log     = Main.getCurCore().getInternalLogger();

    /**
     * Return the internal representation for the test
     * 
     * @param in the input file
     * @return TestIR
     * @throws exception if can not parse input
     */
    public TestIR parser(File in) throws ParserException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tparser(File): ";
        String tsRoot = Main.getCurCore().getConfigIR().getTestSuiteClassRoot();
        String className;
        log.add(Level.FINER, methodLogPrefix + "file to parse " + in);
        if (in.getParent().equals(tsRoot)
            || (in.getParent().length() < (tsRoot.length() + 1))) {
            className = in.getName().substring(0,
                in.getName().length() - JUFinder.TST_SUFFIX.length());
        } else {
            className = in.getParent().substring(tsRoot.length() + 1)
                + File.separator
                + in.getName().substring(0,
                    in.getName().length() - JUFinder.TST_SUFFIX.length());
        }
        className = className.replace(File.separatorChar, '.');
        return parser(className);
    }

    /**
     * Return the internal representation for the test
     * 
     * @param className the name of the test class
     * @return TestIR
     * @throws exception if can not parse input
     */
    public TestIR parser(String className) throws ParserException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tparser(): ";
        try {
            Class obj = Class.forName(className, false, this.getClass()
                .getClassLoader());
            if (Modifier.isAbstract(obj.getModifiers())) {
                testIR = null;
                log.add(Level.FINER, methodLogPrefix
                    + "object ignored as abstract class: " + className);
            } else if (!(TestCase.class.isAssignableFrom(obj))) {
                testIR = null;
                log.add(Level.FINER, methodLogPrefix
                    + "object is not a subclass of TestCase: " + className);
            } else {
                testIR = new TestIR("JUExec", className.replace('.',
                    Constants.INTERNAL_FILE_SEP_C));
                ArrayList torun = new ArrayList();
                torun.add("JUnit");
                torun.add(className);
                testIR.getRunnerParam().add(null);
                testIR.getRunnerParam().add(torun);
            }
        } catch (Throwable e) {
            testIR = null;
            log.add(Level.WARNING, methodLogPrefix + "the file " + className
                + " is ignored due to " + e);
        }
        return testIR;
    }
}