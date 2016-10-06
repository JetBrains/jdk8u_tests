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
 * @author Serguei I. Katkov
 * @version $Revision: 1.8 $
 */
package org.apache.harmony.harness.plugins.w3c;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.harmony.share.Result;

public class W3CXersesXPathRun {

    private String sourceXLS;
    private String sourceDOC;
    private String valid;
    private String resultDOC = null;

    public int runXSLT() {
        try {
            TransformerFactory trf = TransformerFactory.newInstance();
            Transformer tr = trf.newTransformer(new StreamSource(new File(
                sourceXLS)));
            ErrorListener errorListener = new ErrorListener() {
                public void warning(TransformerException e)
                    throws TransformerException {
                    System.err.println(e.getMessageAndLocation());
                }

                public void error(TransformerException e)
                    throws TransformerException {
                    throw e;
                }

                public void fatalError(TransformerException e)
                    throws TransformerException {
                    throw e;
                }
            };
            tr.setErrorListener(errorListener);

            // apply the specified transformation to the specified srcDoc
            StringWriter result2 = new StringWriter();
            tr.transform(new StreamSource(new File(sourceDOC)),
                new StreamResult(result2));
            tr = trf.newTransformer();
            tr.setErrorListener(errorListener);
            StringWriter result = new StringWriter();
            tr.transform(
                new StreamSource(new StringReader(result2.toString())),
                new StreamResult(result));

            if (resultDOC == null) {
                // return passed which will be inverted in the method run
                System.err.println("no error reported");
                return Result.PASS;
            }
            // apply ID transformation to the expected result
            tr = trf.newTransformer();
            tr.setErrorListener(errorListener);
            StringWriter expected = new StringWriter();
            tr.transform(new StreamSource(new File(resultDOC)),
                new StreamResult(expected));

            String resultStr = result.toString();
            String expectedStr = expected.toString();
            if (!expectedStr.equals(resultStr)) {
                System.err.println("result:\n" + resultStr + "\n");
                System.err.println("expected:\n" + expectedStr + "\n");
                System.err.println("FAILED. Result differs from what expected");
                return Result.FAIL;
            }
            System.err.println("PASSED");
            return Result.PASS;
        } catch (TransformerException e) {
            System.err.println(e.getMessageAndLocation());
            return Result.FAIL;
        } catch (Exception e) {
            System.err.println(e.toString());
            return Result.ERROR;
        }
    }

    public int test(String[] args) {
        if (args == null) {
            System.err.println("W3CJAXPSchemaRun: invalid arguments");
            return Result.ERROR;
        }
        if (args.length < 3) {
            System.err.println("W3CJAXPSchemaRun: invalid arguments number "
                + args.length);
            return Result.ERROR;
        }
        sourceXLS = args[0];
        sourceDOC = args[1];
        if ("valid".equalsIgnoreCase(args[2])) {
            resultDOC = args[3];
        }
        return runXSLT();
    }

    public static void main(String[] args) {
        System.exit(new W3CXersesXPathRun().test(args));
    }

}