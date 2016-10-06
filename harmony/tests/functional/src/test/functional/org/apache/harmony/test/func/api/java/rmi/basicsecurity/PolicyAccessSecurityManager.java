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
package org.apache.harmony.test.func.api.java.rmi.basicsecurity;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

import org.apache.harmony.share.Base;

public class PolicyAccessSecurityManager extends SecurityManager {

    public void checkAccept(String arg0, int arg1) {
        Base.log.add("checkAccept(" + arg0 + ", " + arg1 + ") called");
    }

    public void checkAccess(Thread arg0) {
        Base.log.add("checkAccess(" + arg0 + ") called");
    }

    public void checkAccess(ThreadGroup arg0) {
        Base.log.add("checkAccess(" + arg0 + ") called");
    }

    public void checkAwtEventQueueAccess() {
        Base.log.add("checkAwtEventQueueAccess() called");
    }

    public void checkConnect(String arg0, int arg1, Object arg2) {
        Base.log.add("checkConnect(" + arg0 + ", " + arg1 + ", " + arg2
                + ") called");
    }

    public void checkConnect(String arg0, int arg1) {
        Base.log.add("checkConnect(" + arg0 + ", " + arg1 + ") called");
    }

    public void checkCreateClassLoader() {
        Base.log.add("checkCreateClassLoader() called");
    }

    public void checkDelete(String arg0) {
        Base.log.add("checkDelete(" + arg0 + ") called");
    }

    public void checkExec(String arg0) {
        Base.log.add("checkExec(" + arg0 + ") called");
    }

    public void checkExit(int arg0) {
        Base.log.add("checkExit(" + arg0 + ") called");
    }

    public void checkLink(String arg0) {
        Base.log.add("checkLink(" + arg0 + ") called");
    }

    public void checkListen(int arg0) {
        Base.log.add("checkListen(" + arg0 + ") called");
    }

    public void checkMemberAccess(Class arg0, int arg1) {
        Base.log.add("checkMemberAccess(" + arg0 + ", " + arg1 + ") called");
    }

    public void checkMulticast(InetAddress arg0, byte arg1) {
        Base.log.add("(DEPRECATED) " + "checkMulticast(" + arg0 + ", " + arg1
                + ") called");
    }

    public void checkMulticast(InetAddress arg0) {
        Base.log.add("checkMulticast(" + arg0 + ") called");
    }

    public void checkPackageAccess(String arg0) {
        Base.log.add("checkPackageAccess(" + arg0 + ") called");
    }

    public void checkPackageDefinition(String arg0) {
        Base.log.add("checkPackageDefinition(" + arg0 + ") called");
    }

    public void checkPermission(Permission arg0, Object arg1) {
        Base.log.add("checkPermission(" + arg0 + ", " + arg1 + ") called");
    }

    public void checkPermission(Permission arg0) {
        Base.log.add("checkPermission( " + arg0 + " ) called");
    }

    public void checkPrintJobAccess() {
        Base.log.add("checkPrintJobAccess() called");
    }

    public void checkPropertiesAccess() {
        Base.log.add("checkPropertiesAccess() called");
    }

    public void checkRead(FileDescriptor arg0) {
        Base.log.add("checkRead(" + arg0 + ") called");
    }

    public void checkRead(String arg0, Object arg1) {
        Base.log.add("checkRead(" + arg0 + ", " + arg1 + ") called");
    }

    public void checkRead(String arg0) {
        Base.log.add("checkRead(" + arg0 + ") called");
    }

    public void checkSecurityAccess(String arg0) {
        Base.log.add("checkSecurityAccess(" + arg0 + ") called");
    }

    public void checkSetFactory() {
        Base.log.add("checkSetFactory() called");
    }

    public void checkSystemClipboardAccess() {
        Base.log.add("checkSystemClipboardAccess() called");
    }

    public boolean checkTopLevelWindow(Object arg0) {
        Base.log.add("boolean checkTopLevelWindow(" + arg0 + ") called");
        return super.checkTopLevelWindow(arg0);
    }

    public void checkWrite(FileDescriptor arg0) {
        Base.log.add("checkWrite(" + arg0 + ") called");
    }

    public void checkWrite(String arg0) {
        Base.log.add("checkWrite(" + arg0 + ") called");
    }

    protected int classDepth(String arg0) {
        Base.log.add("(DEPRECATED) " + "protected int classDepth(" + arg0
                + ") called");
        return super.classDepth(arg0);
    }

    protected int classLoaderDepth() {
        Base.log.add("(DEPRECATED) int classLoaderDepth() called");
        return super.classLoaderDepth();
    }

    protected ClassLoader currentClassLoader() {
        Base.log.add("(DEPRECATED) "
                + "ClassLoader currentClassLoader() called");
        return super.currentClassLoader();
    }

    protected Class currentLoadedClass() {
        Base.log.add("(DEPRECATED) Class currentLoadedClass() called");
        return super.currentLoadedClass();
    }

    protected Class[] getClassContext() {
        Base.log.add("Class[] getClassContext() called");
        return super.getClassContext();
    }

    public boolean getInCheck() {
        Base.log.add("(DEPRECATED) boolean getInCheck() called");
        return super.getInCheck();
    }

    public Object getSecurityContext() {
        Base.log.add("Object getSecurityContext() called");
        return super.getSecurityContext();
    }

    public ThreadGroup getThreadGroup() {
        Base.log.add("ThreadGroup getThreadGroup() called");
        return super.getThreadGroup();
    }

    protected boolean inClass(String arg0) {
        Base.log.add("(DEPRECATED) " + "protected boolean inClass(" + arg0
                + ") called");
        return super.inClass(arg0);
    }

    protected boolean inClassLoader() {
        Base.log.add("(DEPRECATED) "
                + "protected boolean inClassLoader() called");
        return super.inClassLoader();
    }

    public void checkPropertyAccess(String key) {
        Base.log.add("checkPropertyAccess(" + key + ") called");
        if (key.equals("java.security.policy")) {
            throw new SecurityException("Policy access denied");
        }
    }
}
