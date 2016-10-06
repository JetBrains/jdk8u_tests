/*
 * Copyright 2006 The Apache Software Foundation or its licensors, as applicable
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Nikolay V. Bannikov
 * @version $Revision: 1.3 $
 */

package org.apache.harmony.test.reliability.api.kernel.exec;

import org.apache.harmony.test.reliability.share.Test;

import java.io.File;

/**
 * 
 *    Idea: The test checks VM initialization and shutdown correctness, also, 
 *  Runtime.exec() and other System methods are checked. 
 *  Details: new java is started in the loop using exec(). 
 *  It loads some short multi-threaded application, where each thread outputs 
 *  large string output steam. Application also sets shutdown hooks.
 *  When application is completed, it is checked that shutdown hooks 
 *  were executed and all input/output streams received correct data.
 *
 */

public class RunExec extends Test {

    String ins = ExecApplication.fileNameforIn();
    String err = ExecApplication.fileNameforErr();
    String ins_i;
    String err_i;
    String line;
    int secarg = 0;
    int strcounter;
    String tmpFolder = System.getProperty("java.io.tmpdir");

    public int test(String params[]) {
        if (params.length >= 1) {
            ExecApplication.setId(Integer.parseInt(params[0]));
        }        

        if (params.length >= 2) {
            ExecApplication.setSecondarg(params[1]);
        }        

        if (params.length >= 3) {
            ExecApplication.classrootdir = params[2];
        }        

        if (params.length >= 4) {
            ExecApplication.testedruntime = params[3];
        }        

        if (params.length >= 5) {
        	tmpFolder = params[4];
        }        

        int cnt = ExecApplication.getCnt();
        
        String userdir = tmpFolder + File.separator + "reliability_exec";
        File temp = new File(userdir);
        if (!temp.exists())
        {
        	if (!temp.mkdir()){
        		return fail("Failed to create folder " + temp.getAbsolutePath());
        	}
        }
        
        String longstr = ShutdownHookApp.getLongString();

        for (int i = 0; i < cnt; i++) {
            ExecApplication ex = new ExecApplication(ExecApplication.getAppName(0));
            secarg = Integer.parseInt(ex.getSecondArg());
            ins_i = userdir + File.separator + ins + i;
            err_i = userdir + File.separator + err + i;

            ex.writeToFileInputStream(ins_i, ex.process);
            ex.writeToFileErrorStream(err_i, ex.process);
            if (!ex.checkSomeLine(err_i, ex.getFirstArg(), secarg)) {            

                return fail("Check line from file " + err_i
                    + " : failed");
            }

            if (!ex.checkSomeLine(ins_i, longstr, secarg)) {
                return fail("Check line from file " + ins_i
                    + " : failed");
            }

            for (int j = 1; j < secarg; j++) {
                if (!ex.checkSomeLine2(ins_i, ExecApplication.currThreadStr(), secarg)) {
                    return fail("Check line from file " + ins_i
                        + " : failed");
                }
            }
        }

        //log.add("userdir = " + userdir);

        String deletedfile;

        for (int j = 0; j < cnt; j++) {
            deletedfile = ins + j;
            if (!ExecApplication.deleteFiles(userdir, deletedfile)) {
                return fail("Can't delete file: " + deletedfile);
            }
            deletedfile = err + j;
            if (!ExecApplication.deleteFiles(userdir, deletedfile)) {
                return fail("Can't delete file: " + deletedfile);
            }
        }

        for (int i = 0; i < cnt; i++) {
            ExecApplication ex = new ExecApplication(ExecApplication.getAppName(0));
            secarg = Integer.parseInt(ex.getSecondArg());
            ins_i =  userdir + File.separator + ins + i;
            err_i =  userdir + File.separator + err + i;
            ex.writeToFileInputStream(ins_i, ex.process);
            ex.writeToFileErrorStream(err_i, ex.process);
            ex.processDestroy();
            if (!ex.checkSomeLine(err_i, ex.getFirstArg(), secarg)) {
                return fail("Check line from file " + err_i
                    + " : failed");

            }

            if (!ex.checkSomeLine(ins_i, longstr, secarg)) {
                return fail("Check line from file " + ins_i
                    + " : failed");
            }

            for (int j = 1; j < secarg; j++) {
                if (!ex.checkSomeLine2(ins_i, ExecApplication.currThreadStr(), secarg)) {
                    return fail("Check line from file " + ins_i
                        + " : failed");
                }
            }
        }

        for (int j = 0; j < cnt; j++) {
            deletedfile = ins + j;
            if (!ExecApplication.deleteFiles(userdir, deletedfile)) {
                return fail("Can't delete file: " + deletedfile);
            }
            deletedfile = err + j;
            if (!ExecApplication.deleteFiles(userdir, deletedfile)) {
                return fail("Can't delete file: " + deletedfile);
            }
        }

        return pass("OK");
    }

    public static void main(String args[]) {
        System.exit(new RunExec().test(args));
    }
}
