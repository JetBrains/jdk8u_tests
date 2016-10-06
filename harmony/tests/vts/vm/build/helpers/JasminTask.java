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
import org.apache.tools.ant.*;
import java.util.*;
import java.io.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.taskdefs.*;

public class JasminTask extends Task {

    private Collection filesets = new HashSet();
    private String[] command1 = new String[3];
    private String[] command2 = new String[1];
    private boolean flag = true;

    protected void realExec(File dir, String file) {
    	if ( flag == true ) {
			command1[2] = dir.getPath() + System.getProperty("file.separator") + file;
        	try {
        		Jasmin.main(command1);
        	} catch (Exception e) {
        		System.err.println(e);
        		e.printStackTrace();
        	}
        	flag = false;
    	} else {
			command2[0] = dir.getPath() + System.getProperty("file.separator") + file;
        	try {
        		Jasmin.main(command2);
        	} catch (Exception e) {
        		System.err.println(e);
        		e.printStackTrace();
        	}
        }
    }

    public void execute() {
        for (Iterator i = filesets.iterator(); i.hasNext();) {
            try{
				FileSet fs = (FileSet) i.next();
				DirectoryScanner ds = fs.getDirectoryScanner(getProject());
				String[] matched = ds.getIncludedFiles();
				File dir = fs.getDir(getProject());
				for (int j = 0; j < matched.length; ++j) {
					realExec(dir, matched[j]);
				}
            } catch(Exception e) {
            	System.err.println(e);
            }
        }
    }
    
    public void addFileset(FileSet fileset) {
        filesets.add(fileset);
    }
    
    public void setDest(String dest) {
    	this.command1[0] = "-d";
    	this.command1[1] = dest;
    }
    
}
