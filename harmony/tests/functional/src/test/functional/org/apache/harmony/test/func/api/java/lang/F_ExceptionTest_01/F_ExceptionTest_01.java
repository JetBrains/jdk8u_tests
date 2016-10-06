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
package org.apache.harmony.test.func.api.java.lang.F_ExceptionTest_01;

import java.io.*;
import org.apache.harmony.test.func.share.ScenarioTest;

/**
 * Created on 19.11.2004
 * 
 * 
 *    Usage: 
 *        java.lang.Throwable 
 *
 **/

public class F_ExceptionTest_01 extends ScenarioTest {

    String inputFile = "";
    String inputString = "";
    public int test()
    {
        log.info("");
        inputFile = testArgs[0];
        try {
            FileInputStream in = new FileInputStream(inputFile);
            BufferedReader inbr = new BufferedReader(new InputStreamReader(in));
            String strResult = "";
            while(inbr.ready()) {
                inputString = inbr.readLine();
                String[] arrInput = inputString.split(";");
                logIn(arrInput[0], arrInput[1], arrInput[2], arrInput[3]);
            }
        } catch (Exception e) {
            return error("test failed - " + e.getMessage());
        }
        return pass();
    }        
    public void logIn(String userID, String loginName, String password, String isUser) throws BadLoginException {
        try {
            int intUserID = new Integer(Integer.parseInt(userID)).intValue();
            if(intUserID<0)
                throw new BadLoginException();
            checkLoginName(loginName);
            checkPassword(password);
            checkIsUser(isUser);
            log.info("Test login for user '"+loginName+"' passed");
        } catch (BadLoginException ble) {
            log.info("Test login for user '"+loginName+"' failed");
            log.info(ble.getMessage());
            log.info(""+ble.getCause());
            log.info("***************************");
        }
    }
    public void checkLoginName(String loginName) throws BadLoginException {
        if(loginName.length()==0) {
            Throwable myThrowable = new Throwable("Empty login name");
            throw new BadLoginException("Bad login name", myThrowable);
        }
        if(loginName.length()>10) {
            Throwable myThrowable = new Throwable("Login name must be less then 11 charcters");
            throw new BadLoginException("Bad login name", myThrowable);
        }
    }
    public void checkPassword(String password) throws BadLoginException {
        if(password.length()==0)
            throw new BadLoginException("Empty password");
        if(password.length()>10)
            throw new BadLoginException("password must be less then 11 charcters");
    }
    public void checkIsUser(String isUser) throws BadLoginException {
        boolean blnIsUser = new Boolean(isUser).booleanValue();
        if(!blnIsUser) {
            Throwable myThrowable = new Throwable("Not user");
            throw new BadLoginException(myThrowable);
        }
    }
    public static void main(String[] args)
    {
        System.exit(new F_ExceptionTest_01().test(args));
    }
    class BadLoginException extends Exception {
        public BadLoginException() {
        }
        public BadLoginException(String message){
            super(message);
        }
        public BadLoginException(Throwable throwable){
            super(throwable);
        }
        public BadLoginException(String message, Throwable throwable){
            super(message, throwable);
        }
    }
}
