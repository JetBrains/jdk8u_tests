/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.harmony.test.func.api.javax.management.share.framework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 */
public class Result {

    public static final int PASS       = org.apache.harmony.share.Result.PASS;

    public static final int FAIL       = org.apache.harmony.share.Result.FAIL;

    public static final int ERROR      = org.apache.harmony.share.Result.ERROR;

    /**
     * Test result.
     */
    private int             result;

    /**
     * Message.
     */
    private String          message;

    /**
     * Test class name.
     */
    private String          className;

    /**
     * Test method name.
     */
    private String          methodName;

    /**
     * Test source file name.
     */
    private String          fileName   = "";

    /**
     * Line number.
     */
    private int             lineNumber = -1;

    /**
     * Construct new Result object.
     * 
     * @param result test result.
     * @param message message.
     * @param ste stack trace element.
     */
    public Result(int result, String message, StackTraceElement ste) {
        this.result = result;
        this.message = message;
        this.className = ste.getClassName();
        this.methodName = ste.getMethodName();
        this.fileName = ste.getFileName();
        this.lineNumber = ste.getLineNumber();
    }

    /**
     * Construct new Result object.
     * 
     * @param result test result.
     * @param message message.
     * @param className class name.
     * @param methodName method name.
     */
    public Result(int result, String message, String className,
        String methodName) {
        this.result = result;
        this.message = message;
        this.className = className;
        this.methodName = methodName;
    }

    /**
     * Construct new Result object.
     * 
     * @param result test result.
     * @param ex exception.
     * @param ste stack trace element.
     */
    public Result(int result, Throwable ex, StackTraceElement ste) {
        this.result = result;
        this.className = ste.getClassName();
        this.methodName = ste.getMethodName();
        this.fileName = ste.getFileName();
        this.lineNumber = ste.getLineNumber();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(baos));
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
        }

        message = baos.toString();
    }

    /**
     * Construct new Result object.
     * 
     * @param result test result.
     * @param ex exception.
     * @param className class name.
     * @param methodName method name.
     */
    public Result(int result, Throwable ex, String className, String methodName) {
        this.result = result;
        this.className = className;
        this.methodName = methodName;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(baos));
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
        }

        message = baos.toString();
    }

    /**
     * Construct new Result object.
     * 
     * @param result test result.
     * @param message message.
     * @param className test class name.
     * @param methodName test method name.
     * @param fileName test source file name.
     * @param lineNumber line number.
     */
    public Result(int result, String message, String className,
        String methodName, String fileName, int lineNumber) {
        this.result = result;
        this.message = message;
        this.className = className;
        this.methodName = methodName;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
    }

    /**
     * @return Returns the className.
     */
    public String getClassName() {
        return className;
    }

    /**
     * @return Returns the fileName.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return Returns the lineNumber.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return Returns the methodName.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @return Returns the result.
     */
    public int getResult() {
        return result;
    }
}
