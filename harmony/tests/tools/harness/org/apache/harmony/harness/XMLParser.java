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
 * @author A.Tyuryushkin, V.Ivanov
 * @version $Revision: 1.16 $
 */
package org.apache.harmony.harness;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ErrorHandler;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser {

    private static final String classID            = "XMLParser";

    private XMLReader           parser;
    private String              file;
    private String              place;
    private Logging             log                = Main.getCurCore()
                                                       .getInternalLogger();

    private String              xmlVersionInReport = "1.0";

    public XMLParser(boolean toValid) throws ParserException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tXMLParser(): ";
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setValidating(toValid);
        parserFactory.setNamespaceAware(true);
        try {
            parser = parserFactory.newSAXParser().getXMLReader();
            // workaround: the xerces support specification 1.1
            // use it to handle non-printable characters into CDATA
            // section of test reports
            if (parser.getClass().getName().indexOf("xerces") != -1) {
                xmlVersionInReport = "1.1";
            }
            parser.setErrorHandler(new CfgErrorHandler());
            log.add(Level.FINE, methodLogPrefix
                + "Create successful. Parser is " + parser.getClass());
        } catch (Exception e) {
            throw new ParserException("SAX: " + e.getMessage());
        }
    }

    public XMLParser() throws ParserException {
        this(true);
    }

    public XMLParser(String file, String place) throws ParserException {
        this();
        String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tXMLParser(): ";
        try {
            parser.setEntityResolver(new TestResolver(file, place));
            log.add(Level.FINE, methodLogPrefix
                + "Create successful. EntityResolver. file = " + file
                + ", place = " + place);
        } catch (Exception e) {
            throw new ParserException("SAX: " + e.getMessage());
        }
    }

    public String getXMLVersion() {
        return xmlVersionInReport;
    }

    public void setErrorHandler(ErrorHandler eh) {
        parser.setErrorHandler(eh);
    }

    public void parsefile(DefaultHandler handler, File in)
        throws ConfigurationException, ParserException {
        final String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
            + "\tparsefile(): ";
        if (!in.exists()) {
            throw new ParserException(methodLogPrefix + "IO: File "
                + in.getName() + " not exists");
        }
        if (handler == null) {
            throw new ConfigurationException(methodLogPrefix
                + "Handler is null");
        }
        FileReader fr = null;
        try {
            log.add(Level.FINER, methodLogPrefix + "File to parse " + in);
            parser.setContentHandler(handler);
            fr = new FileReader(in);
            InputSource is = new InputSource(fr);
            is.setSystemId(in.getAbsolutePath());
            parser.parse(is);
            log.add(Level.FINE, methodLogPrefix + "Parsed successful. File is "
                + in);
        } catch (SAXException e) {
            log.add(Level.FINE, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + e, e);
            throw new ConfigurationException(methodLogPrefix + "SAX: " + e);
        } catch (IOException e) {
            log.add(Level.FINE, methodLogPrefix + MessageInfo.UNEX_EXCEPTION
                + e, e);
            throw new ParserException(methodLogPrefix + "IO: " + e);
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                    fr = null;
                } catch (Exception e) {
                    log.add(Level.FINE, methodLogPrefix
                        + MessageInfo.UNEX_EXCEPTION + e, e);
                }
            }
        }
    }

    public class CfgErrorHandler implements ErrorHandler {
        private static final String classID = "CfgErrorHandler";

        public void warning(SAXParseException exception) throws SAXException {
            log.add(Level.CONFIG, MessageInfo.MSG_PREFIX + classID
                + " warning(): " + exception);
        }

        public void error(SAXParseException exception) throws SAXException {
            log.add(Level.CONFIG, MessageInfo.MSG_PREFIX + classID
                + " error(): " + exception);
            throw exception;
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            log.add(Level.CONFIG, MessageInfo.MSG_PREFIX + classID
                + " fatalError(): " + exception);
            throw exception;
        }
    }

    class TestResolver implements EntityResolver {
        private static final String classID = "testResolver";

        private String              end;
        private String              where;

        public TestResolver(String file, String place) {
            end = file;
            where = place;
        }

        public InputSource resolveEntity(String publicId, String systemId) {
            String methodLogPrefix = MessageInfo.MSG_PREFIX + classID
                + "\tresolveEntity(): ";
            if (end == null || where == null) {
                log.add(Level.FINE, methodLogPrefix
                    + "no EntityResolver. end = " + end + ", where = " + where);
                return null;
            }
            if (systemId.endsWith(end)) {
                try {
                    log.add(Level.FINE, methodLogPrefix
                        + "EntityResolver is file " + where);
                    return new InputSource(new FileReader(where));
                } catch (FileNotFoundException e) {
                    log.add(Level.FINE, methodLogPrefix
                        + "EntityResolver (string)" + where);
                    return new InputSource(where);
                }
            } else {
                log.add(Level.FINE, methodLogPrefix + "No EntityResolver.");
                return null;
            }
        }
    }
}