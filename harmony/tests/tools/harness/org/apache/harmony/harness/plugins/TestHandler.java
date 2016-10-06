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
 * @author A.Tyuryushkin
 * @version $Revision: 1.15 $
 */
package org.apache.harmony.harness.plugins;

import java.io.CharArrayWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.harmony.harness.Logging;
import org.apache.harmony.harness.Main;
import org.apache.harmony.harness.MessageInfo;
import org.apache.harmony.harness.TestIR;

public class TestHandler extends DefaultHandler {

    private final String      classID       = "TestHandler";

    private TestIR            theTestIR;
    private String            keyName       = "";
    private String            keyValue      = "";
    private ArrayList         copyrights    = new ArrayList();
    private ArrayList         authors       = new ArrayList();
    private ArrayList         keywords      = new ArrayList();
    private ArrayList         sources       = new ArrayList();
    private ArrayList         resources     = new ArrayList();
    private ArrayList         modifications = new ArrayList();
    private ArrayList         modification  = new ArrayList();
    private ArrayList         runner        = new ArrayList();
    private ArrayList         options       = new ArrayList();
    private ArrayList         parameters    = new ArrayList();
    private boolean           inToParam     = false;
    private boolean           finishOptions = false;
    private Logging           log           = Main.getCurCore()
                                                .getInternalLogger();

    protected CharArrayWriter context       = new CharArrayWriter();

    public TestHandler(TestIR curTestIR) {
        theTestIR = curTestIR;
    }

    public void characters(char[] character, int start, int length)
        throws SAXException {
        context.write(character, start, length);
    }

    public void startDocument() throws SAXException {
        log.add(Level.FINER, MessageInfo.MSG_PREFIX + "<"
            + this.getClass().getName() + ">");
    }

    public void endDocument() throws SAXException {
        log.add(Level.FINER, MessageInfo.MSG_PREFIX + "</"
            + this.getClass().getName() + ">");
    }

    public void startElement(String uri, String localName, String qName,
        Attributes attrs) throws SAXException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tstartElement(): ";
        context.reset();
        if (localName.equals("Test")) {
            int attrCount = attrs.getLength();
            for (int i = 0; i < attrCount; i++) {
                keyName = attrs.getQName(i);
                keyValue = attrs.getValue(i);
                if (keyName.equalsIgnoreCase("id")) {
                    theTestIR.setTestID(keyValue);
                    log.add(Level.FINER, methodLogPrefix + "\tsetTestID("
                        + keyValue + ") -> " + theTestIR.getTestID());
                } else if (keyName.equalsIgnoreCase("timeout")) {
                    try {
                        float timeoutValue = Float.parseFloat(keyValue);
                        theTestIR.setTestTimeout(timeoutValue);
                        log.add(Level.FINEST, methodLogPrefix
                            + "\tsetTestTimeout(" + keyValue + ") -> "
                            + theTestIR.getTestTimeout());
                    } catch (NumberFormatException e) {
                        try {
                            theTestIR.setTestTimeout(keyValue);
                            log.add(Level.FINEST, methodLogPrefix
                                + "\tsetTestTimeout(" + keyValue + ") -> 1.0");
                        } catch (IllegalArgumentException iae) {
                            log
                                .add(
                                    Level.FINEST,
                                    methodLogPrefix
                                        + "Warning: NumberFormatException - attribute 'timeout' is not number. Timeout was set to 1.0.");
                        }
                    }
                } else {
                    theTestIR.setProperty(keyName, keyValue);
                    log.add(Level.FINEST, methodLogPrefix + "\tsetProperty('"
                        + keyName + "', '" + keyValue + "') -> "
                        + theTestIR.getProperty(keyName));
                }
            }
        }
        if (localName.equals("Copyright")) {
            copyrights.add(attrs.getValue("value"));
        }
        if (localName.equals("Author")) {
            authors.add(attrs.getValue("value"));
        }
        if (localName.equals("Keyword")) {
            keywords.add(attrs.getValue("name"));
        }
        if (localName.equals("Source")) {
            sources.add(attrs.getValue("name"));
        }
        if (localName.equals("Modification")) {
            modification.add(attrs.getValue("date"));
            modification.add(attrs.getValue("author"));
        }
        if (localName.equals("Runner")) {
            String idValue = attrs.getValue("ID");
            String tmp = theTestIR.setRunnerID(idValue);
            if (tmp != null && !tmp.equals(idValue)) {
                throw new SAXException(
                    methodLogPrefix
                        + "The test has 2 Runner's element: " + tmp + " and " + idValue + " that illegal");
            }
        }
        if (localName.equals("Param")) {
            if (!finishOptions) {
                runner.add(options);
                finishOptions = true;
            }
            options = new ArrayList();
            if (attrs.getValue("name") == null
                || attrs.getValue("name").trim() == "") {
                throw new SAXException(
                    methodLogPrefix
                        + "The attribute 'name' of Runner's element 'parameter' in this test is absent or empty");
            } else if (!(attrs.getValue("name").equalsIgnoreCase(RunDRL.COMMAND) ||
                attrs.getValue("name").equalsIgnoreCase(DistributedRunner.EXEC_CMD) ||
                attrs.getValue("name").equalsIgnoreCase(CompDRL.COMMAND) ||
                attrs.getValue("name").equalsIgnoreCase(JUExecDRL.COMMAND))) {
                log.add(Level.WARNING, methodLogPrefix + "\tPlease, check the parameter name: "
                    + attrs.getValue("name") + ". Standard value expected");
            }
            parameters.add(attrs.getValue("name"));
            if (attrs.getValue("value") == null
                || attrs.getValue("value").trim() == "") {
                throw new SAXException(
                    methodLogPrefix
                        + "The attribute 'value' of Runner's element 'parameter' in this test is absent or empty");
            }
            parameters.add(attrs.getValue("value"));
            inToParam = true;
        }
        if (!inToParam && localName.equals("Option")) {
            if (attrs.getValue("name") != null) {
                options.add(attrs.getValue("name"));
            }
            if (attrs.getValue("value") != null) {
                options.add(attrs.getValue("value"));
            }
        }
        if (inToParam && localName.equals("Option")) {
            if (attrs.getValue("name") != null) {
                parameters.add(attrs.getValue("name"));
            }
            if (attrs.getValue("value") != null) {
                parameters.add(attrs.getValue("value"));
            }
        }
        if (localName.equals("Resource")) {
            resources.add(attrs.getValue("name"));
        }
        if (localName.equals("Restriction")) {
            String propertyName = attrs.getValue("name");
            // setter must be PUBLIC
            java.lang.reflect.Method[] methods = theTestIR.getClass()
                .getMethods();
            int methodsCount = methods.length;
            boolean stop = false;
            for (int i = 0; i < methodsCount && !stop; i++) {
                if (methods[i].getName().equalsIgnoreCase("set" + propertyName)) {
                    Object[] objects = { new Boolean(true) };
                    log.add(Level.FINE, "\t" + methods[i].getName() + "("
                        + objects[0] + ")");
                    try {
                        methods[i].invoke(theTestIR, objects);
                    } catch (InvocationTargetException e) {
                        log.add(Level.SEVERE, methodLogPrefix
                            + "Error in ENV configuration: setter 'set"
                            + propertyName + "' have been return exception", e
                            .getTargetException());
                    } catch (IllegalAccessException e) {
                        log.add(Level.SEVERE, methodLogPrefix
                            + "Error in ENV configuration: setter 'set"
                            + propertyName + "' is inaccessible in class");
                    }
                    stop = true;
                }
            }
        }
    }

    public void endElement(String uri, String localName, String qName)
        throws SAXException {

        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tendElement(): ";

        try {
            if (localName.equals("Test")) {
                theTestIR.setProperty("copyrights", copyrights);
                log.add(Level.FINEST, methodLogPrefix
                    + "setProperty(copyrights, set to ArrayList["
                    + copyrights.size() + "])");
                theTestIR.setProperty("authors", authors);
                log.add(Level.FINEST, methodLogPrefix
                    + "setProperty(authors, set to ArrayList[" + authors.size()
                    + "])");
                String[] keywords = new String[this.keywords.size()];
                for (int i = 0; i < this.keywords.size(); i++) {
                    keywords[i] = (String)this.keywords.get(i);
                }
                theTestIR.setKeywords(keywords);
                log.add(Level.FINEST, methodLogPrefix + "setKeywords(String["
                    + parameters.size() + "])");
                theTestIR.setProperty("sources", sources);
                log.add(Level.FINEST, methodLogPrefix
                    + "setProperty(sources, set to ArrayList[" + sources.size()
                    + "])");
                theTestIR.setModifications(modifications);
                log.add(Level.FINEST, methodLogPrefix
                    + "\tsetModifications(set to ArrayList["
                    + modifications.size() + "])");
                String[] resources = new String[this.resources.size()];
                for (int i = 0; i < this.resources.size(); i++) {
                    resources[i] = (String)this.resources.get(i);
                }
                theTestIR.setResources(resources);
                log.add(Level.FINEST, methodLogPrefix
                    + "\tsetResources(String[" + resources.length + "])");

                if (theTestIR.getTestID() == null) {
                    throw new SAXException(methodLogPrefix
                        + "The attribute 'ID' of test is null");
                }
                if (theTestIR.getTestID() == "") {
                    throw new SAXException(methodLogPrefix
                        + "The attribute 'ID' of test is empty");
                }
                if (theTestIR.getRunnerID() == null) {
                    throw new SAXException(
                        methodLogPrefix
                            + "The attribute 'ID' of element 'Runner' of test "
                        + theTestIR.getTestID() + " is null");
                }
                if (theTestIR.getRunnerID() == "") {
                    throw new SAXException(
                        methodLogPrefix
                            + "The attribute 'ID' of element 'Runner' of test "
                        + theTestIR.getTestID() + " is empty");
                }
                if (theTestIR.getProperty("date-of-creation") == null
                    || theTestIR.getProperty("date-of-creation").trim() == "") {
                    log.add(Level.INFO, methodLogPrefix
                        + "The attribute 'date-of-creation' of the test '"
                        + theTestIR.getTestID() + "' is absent or empty");
                }
                if (theTestIR.getProperty("Description") == null
                    || theTestIR.getProperty("Description").trim() == "") {
                    log.add(Level.INFO, methodLogPrefix
                        + "The element 'Description' of the test '"
                        + theTestIR.getTestID() + "' is absent or empty");
                }
                if (theTestIR.getProperty((Object)"copyrights") == null
                    || ((ArrayList)theTestIR.getProperty((Object)"copyrights"))
                        .size() <= 0) {
                    //log.add(Level.INFO, methodLogPrefix
                    //    + "The elements 'Copyright' of test '"
                    //    + theTestIR.getTestID() + "' is empty");
                } else {
                    boolean empty = false;
                    for (int i = 0; i < ((ArrayList)theTestIR
                        .getProperty((Object)"copyrights")).size(); i++) {
                        if (((String)((ArrayList)theTestIR
                            .getProperty((Object)"copyrights")).get(i)).trim() == "")
                            empty = true;
                    }
                    if (empty)
                        log.add(Level.INFO, methodLogPrefix
                            + "The element 'Copyright' of the test '"
                            + theTestIR.getTestID() + "' is empty");
                }
                if (theTestIR.getProperty((Object)"authors") == null
                    || ((ArrayList)theTestIR.getProperty((Object)"authors"))
                        .size() <= 0) {
                    log.add(Level.INFO, methodLogPrefix
                        + "The element 'Author' of the test '"
                        + theTestIR.getTestID() + "' is empty");
                } else {
                    boolean empty = false;
                    for (int i = 0; i < ((ArrayList)theTestIR
                        .getProperty((Object)"authors")).size(); i++) {
                        if (((String)((ArrayList)theTestIR
                            .getProperty((Object)"authors")).get(i)).trim() == "")
                            empty = true;
                    }
                    if (empty)
                        log.add(Level.INFO, methodLogPrefix
                            + "There are empty elements 'Author' in the test '"
                            + theTestIR.getTestID() + "'");
                }
                if (theTestIR.getProperty((Object)"sources") == null
                    || ((ArrayList)theTestIR.getProperty((Object)"sources"))
                        .size() <= 0) {
                    log.add(Level.INFO, methodLogPrefix
                        + "The element 'Source' of the test '"
                        + theTestIR.getTestID() + "' is absent");
                } else {
                    for (int i = 0; i < ((ArrayList)theTestIR
                        .getProperty((Object)"sources")).size(); i++) {
                        String src = ((String)((ArrayList)theTestIR
                            .getProperty((Object)"sources")).get(i));
                        if (src == null) {
                            log.add(Level.INFO, methodLogPrefix
                                + "incorrect element 'Source' of the test '"
                                + theTestIR.getTestID() + "'");
                        } else if (src.trim() == "") {
                            log.add(Level.INFO, methodLogPrefix
                                + "empty element 'Source' of the test '"
                                + theTestIR.getTestID() + "'");
                        }
                    }
                }
                if (theTestIR.getKeywords().length > 0) {
                    boolean empty = false;
                    for (int i = 0; i < theTestIR.getKeywords().length; i++) {
                        if (theTestIR.getKeywords()[i].trim() == "")
                            empty = true;
                    }
                    if (empty)
                        log.add(Level.INFO, methodLogPrefix
                            + "There are empty elements 'Keyword' in the test '"
                            + theTestIR.getTestID() + "'");
                }
                if (((ArrayList)theTestIR.getModifications()).size() > 0) {
                    boolean emptyDate = false;
                    boolean emptyAuthor = false;
                    for (int i = 0; i < ((ArrayList)theTestIR
                        .getModifications()).size(); i++) {
                        if (((ArrayList)theTestIR.getModifications().get(i))
                            .get(0) == null
                            || ((String)((ArrayList)theTestIR
                                .getModifications().get(i)).get(0)).trim() == "")
                            emptyDate = true;
                        if (((ArrayList)theTestIR.getModifications().get(i))
                            .get(1) == null
                            || ((String)((ArrayList)theTestIR
                                .getModifications().get(i)).get(1)).trim() == "")
                            emptyAuthor = true;
                    }
                    if (emptyDate)
                        log
                            .add(
                                Level.INFO,
                                methodLogPrefix
                                    + "There are empty attributes 'date' of element 'Modification' in the test '"
                                    + theTestIR.getTestID() + "'");
                    if (emptyAuthor)
                        log
                            .add(
                                Level.INFO,
                                methodLogPrefix
                                    + "There are empty attributes 'author' of element 'Modification' in the test '"
                                    + theTestIR.getTestID() + "'");
                }
            }
            if (localName.equals("Description")) {
                keyName = "Description";
                keyValue = context.toString().trim();
                theTestIR.setProperty(keyName, keyValue);
                log.add(Level.FINEST, "\tsetProperty('" + keyName + "', '"
                    + keyValue + "') -> " + theTestIR.getProperty(keyName));
            }
            if (localName.equals("Modification")) {
                modifications.add(modification);
            }
            if (localName.equals("Runner")) {
                if (runner.size() <= 0 && !finishOptions)
                    runner.add(options);
                if (runner.size() > 1) {
                    theTestIR.setRunnerParam(runner);
                    log.add(Level.FINEST, "\tsetRunnerParam(ArrayList["
                        + runner.size() + "])");
                    runner = theTestIR.getRunnerParam();
                    log.add(Level.FINEST, "\tRunner parameters:");
                    if (runner.get(0) != null
                        && ((ArrayList)runner.get(0)).size() > 0) {
                        for (int j = 0; j < ((ArrayList)runner.get(0)).size(); j++) {
                            log.add(Level.FINEST, "\t\t - '"
                                + (String)((ArrayList)runner.get(0)).get(j)
                                + "'");
                        }
                    }
                    for (int i = 1; i < runner.size(); i++) {
                        log.add(Level.FINEST, "\tParam - "
                            + ((ArrayList)runner.get(i)).get(0).toString()
                            + ":");
                        for (int j = 1; j < ((ArrayList)runner.get(i)).size(); j++) {
                            log.add(Level.FINEST, "\t\t - '"
                                + (String)((ArrayList)runner.get(i)).get(j)
                                + "'");
                        }
                    }
                } else {
                    throw new SAXException(
                        methodLogPrefix
                            + "There are no execution parameter ('Param') of Runner in this test");
                }
            }
            if (localName.equals("Param")) {
                runner.add(parameters);
                parameters = new ArrayList();
                inToParam = false;
            }
        } catch (SAXException e) {
            throw e;
        } catch (Exception e) {
            log.add(Level.WARNING, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + e);
        }
    }
}