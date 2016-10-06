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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Calendar;

public class CheckLinks {

    public static final String PREFIX_UNIT    = "../";
    public static final String DTD_EXT        = ".dtd";
    public static final String XSL_EXT        = ".xsl";
    
    //don't process files (insert link to xsl) with name
    public String iFileName       = "index.xml";

    static String              xsl_prefix     = "";
    static int                 dir_level      = -1;

    String                     basedir        = ".", fileext = "xml";
    boolean                    recursion      = true;
    boolean                    checkOnly      = false;
    boolean                    insert_allowed = false;
    String                     ref_file_name  = "test";
    String                     specific_dir   = "share";
    String                     backupExt      = ".bak";
    String                     tmpFileExt     = ".tmp"
                                                  + Calendar.getInstance()
                                                      .getTimeInMillis();

    protected boolean parseParam(String[] params) {
        String helpMsg = "Check links to test.dtd and test.xsl files into tests and correct it.\nUsage:\n"
            + "-basedir \"name\"\t\tname of the directory to statrt\n"
            + "-fileext \"name\"\t\textension of files to index\n"
            + "-excldir \"name\"\t\texclude files from directory with 'name'\n"
            + "-refname \"name\"\t\tname of file to refer (one name for dtd and xsl, by default 'test')\n"
            + "-checkonly\t\t\tdon't correct links\n"
            + "-insert\t\t\tinsert link to test.xsl (before link to test.dtd)\n"
            + "-i \"extension\"\t\tcorrect file in place or create backup with specified extension\n"
            + "-nr\t\t\tdon't process subdirectoty (by default, false)";
        try {
            for (int i = 0; i < params.length; i++) {
                if ("-basedir".equalsIgnoreCase(params[i])) {
                    basedir = params[++i];
                } else if ("-nr".equalsIgnoreCase(params[i])) {
                    recursion = false;
                } else if ("-checkonly".equalsIgnoreCase(params[i])) {
                    checkOnly = true;
                } else if ("-fileext".equalsIgnoreCase(params[i])) {
                    fileext = params[++i];
                } else if ("-excldir".equalsIgnoreCase(params[i])) {
                    specific_dir = params[++i];
                } else if ("-refname".equalsIgnoreCase(params[i])) {
                    ref_file_name = params[++i];
                } else if ("-insert".equalsIgnoreCase(params[i])) {
                    insert_allowed = true;
                } else if ("-indexf".equalsIgnoreCase(params[i])) {
                    iFileName = params[++i];
                } else if ("-i".equalsIgnoreCase(params[i])) {
                    try {
                        if (!params[i + 1].startsWith("-")) {
                            backupExt = params[++i];
                        } else {
                            backupExt = null;
                        }
                    } catch (IndexOutOfBoundsException iobe) {
                        //do nothing
                    }
                } else if ("-help".equalsIgnoreCase(params[i])) {
                    System.out.println(helpMsg);
                    System.exit(0);
                } else {
                    System.out.println(helpMsg);
                    System.exit(0);
                }
            }
        } catch (IndexOutOfBoundsException iobe) {
            System.out.println("Incorrect parameters. Please check.\n"
                + helpMsg);
            return false;
        }
        return true;
    }

    protected void findFiles(String root) {
        findFiles(new File(root));
    }

    protected boolean checkFileForDTDLink(File f) {
        try {
            BufferedReader fr = new BufferedReader(new FileReader(f));
            while (fr.ready()) {
                String data = fr.readLine();
                if (data != null) {
                    if (data.indexOf("!DOCTYPE") != -1
                        && data.indexOf("SYSTEM") != -1
                        && data.indexOf(ref_file_name + DTD_EXT) != -1) {
                        String tmp = null;
                        if (data.indexOf("\"") != -1) {
                            if (data.indexOf("\"") != data.lastIndexOf("\"")) {
                                tmp = data.substring(data.indexOf("\"") + 1,
                                    data.lastIndexOf("\""));
                            }
                        } else if (data.indexOf("'") != -1) {
                            if (data.indexOf("'") != data.lastIndexOf("'")) {
                                tmp = data.substring(data.indexOf("'") + 1,
                                    data.lastIndexOf("'"));
                            }
                        }
                        if (tmp != null && tmp.length() > 0) {
                            if ((new File(f.getParent(), tmp)).exists()) {
                                return true;
                            } else {
                                System.out
                                    .println("Link to dtd is incorrect for "
                                        + f.getParent() + File.separator
                                        + f.getName());
                                return false;
                            }
                        }
                    }
                }
            }
            fr.close();
            System.out.println("Link to dtd is missed for " + f.getParent()
                + File.separator + f.getName());
        } catch (Exception e) {
            System.err
                .println("Unexpected exception while checks for link to dtd: "
                    + e);
            e.printStackTrace(System.err);
        }
        return true;
    }

    protected void correctDTDLink(File f) {
        xsl_prefix = "";
        for (int i = 1; i < dir_level; i++) {
            // i = 1 instead of 0 because later 'f' used as getParent()
            xsl_prefix = xsl_prefix + PREFIX_UNIT;
        }
        try {
            File inF = new File(f.getParent() + File.separator + f.getName()
                + tmpFileExt);
            if (!f.renameTo(inF)) {
                //if can't rename - crate new and copy content
                inF.createNewFile();
                FileWriter fw = new FileWriter(inF);
                BufferedReader fr = new BufferedReader(new FileReader(f));
                while (fr.ready()) {
                    String data = fr.readLine();
                    fw.write(data + "\n");
                }
                fr.close();
                fw.flush();
                fw.close();
            }
            if (!f.exists()) {
                f.createNewFile();
            }
            FileWriter fw = new FileWriter(f);
            BufferedReader fr = new BufferedReader(new FileReader(inF));
            while (fr.ready()) {
                String data = fr.readLine();
                if (data != null) {
                    if (data.indexOf("!DOCTYPE") != -1
                        && data.indexOf("SYSTEM") != -1
                        && data.indexOf(ref_file_name + DTD_EXT) != -1) {
                        String tmp = null, enclString = "\"";
                        if (data.indexOf("\"") != -1) {
                            tmp = data.substring(data.indexOf("\"") + 1, data
                                .lastIndexOf("\""));
                        } else if (data.indexOf("'") != -1) {
                            tmp = data.substring(data.indexOf("'") + 1, data
                                .lastIndexOf("'"));
                            enclString = "'";
                        }
                        if (tmp != null && tmp.length() > 0) {
                            if (!(new File(f.getParent(), tmp)).exists()
                                && (new File(f.getParent(), xsl_prefix
                                    + ref_file_name + DTD_EXT)).exists()) {
                                data = data.substring(0, data
                                    .indexOf(enclString) + 1)
                                    + xsl_prefix
                                    + ref_file_name
                                    + DTD_EXT
                                    + data.substring(data.indexOf(ref_file_name
                                        + DTD_EXT)
                                        + (ref_file_name + DTD_EXT).length());
                            }
                        }
                    }
                    fw.write(data + "\n");
                }
            }
            fr.close();
            fw.flush();
            fw.close();
            if (backupExt == null) {
                inF.delete();
            } else {
                inF.renameTo(new File(f.getParent(), f.getName() + backupExt));
            }
            System.out.println("Link to dtd corrected for " + f.getParent()
                + File.separator + f.getName());
        } catch (Exception e) {
            System.err
                .println("Unexpected exception while checks for link to dtd: "
                    + e);
            e.printStackTrace(System.err);
        }
    }

    protected boolean checkFileForXSLLink(File f) {
        try {
            BufferedReader fr = new BufferedReader(new FileReader(f));
            while (fr.ready()) {
                String data = fr.readLine();
                if (data != null) {
                    if (data.indexOf("?xml-stylesheet") != -1
                        && data.indexOf("type") != -1
                        && data.indexOf("text/xsl") != -1
                        && data.indexOf(XSL_EXT) != -1) {
                        String tmp = data.substring(data.indexOf(XSL_EXT)
                            + XSL_EXT.length(), data.indexOf(XSL_EXT)
                            + XSL_EXT.length() + 1);
                        String[] tmpArr = data.split(tmp);
                        tmp = tmpArr[tmpArr.length - 2];
                        if (tmp != null && tmp.length() > 0) {
                            if ((new File(f.getParent(), tmp)).exists()) {
                                return true;
                            } else {
                                System.out
                                    .println("Link to xsl is incorrect for "
                                        + f.getParent() + File.separator
                                        + f.getName());
                                return false;
                            }
                        }
                    }
                }
            }
            fr.close();
        } catch (Exception e) {
            System.err
                .println("Unexpected exception while checks for link to xsl: "
                    + e);
            e.printStackTrace(System.err);
        }
        return true;
    }

    protected void correctXSLLink(File f) {
        xsl_prefix = "";
        for (int i = 1; i < dir_level; i++) {
            // i = 1 instead of 0 because later 'f' used as getParent()
            xsl_prefix = xsl_prefix + PREFIX_UNIT;
        }
        try {
            File inF = new File(f.getParent() + File.separator + f.getName()
                + tmpFileExt);
            if (!f.renameTo(inF)) {
                //if can't rename - crate new and copy content
                inF.createNewFile();
                FileWriter fw = new FileWriter(inF);
                BufferedReader fr = new BufferedReader(new FileReader(f));
                while (fr.ready()) {
                    String data = fr.readLine();
                    fw.write(data + "\n");
                }
                fr.close();
                fw.flush();
                fw.close();
            }
            if (!f.exists()) {
                f.createNewFile();
            }
            FileWriter fw = new FileWriter(f);
            BufferedReader fr = new BufferedReader(new FileReader(inF));
            while (fr.ready()) {
                String data = fr.readLine();
                if (data != null) {
                    if (data.indexOf("?xml-stylesheet") != -1
                        && data.indexOf("type") != -1
                        && data.indexOf("text/xsl") != -1
                        && data.indexOf(XSL_EXT) != -1) {
                        String separator = data.substring(data.indexOf(XSL_EXT)
                            + XSL_EXT.length(), data.indexOf(XSL_EXT)
                            + XSL_EXT.length() + 1);
                        String[] tmpArr = data.split(separator);
                        if (tmpArr.length >= 2) {
                            String tmp = tmpArr[tmpArr.length - 2];
                            if (tmp != null && tmp.length() > 0) {
                                if (!(new File(f.getParent(), tmp)).exists()
                                    && (new File(f.getParent(), xsl_prefix
                                        + ref_file_name + XSL_EXT)).exists()) {
                                    tmpArr[tmpArr.length - 2] = xsl_prefix
                                        + ref_file_name + XSL_EXT;
                                    data = "";
                                    int cnt = 0;
                                    for (; cnt < tmpArr.length - 1; cnt++) {
                                        data = data + tmpArr[cnt] + separator;
                                    }
                                    data = data + tmpArr[cnt];
                                }
                            }
                        }
                    }
                    fw.write(data + "\n");
                }
            }
            fr.close();
            fw.flush();
            fw.close();
            if (backupExt == null) {
                inF.delete();
            } else {
                inF.renameTo(new File(f.getParent(), f.getName() + backupExt));
            }
            System.out.println("Link to xsl corrected for " + f.getParent()
                + File.separator + f.getName());
        } catch (Exception e) {
            System.err
                .println("Unexpected exception while checks for link to xsl: "
                    + e);
            e.printStackTrace(System.err);
        }
    }

    protected void insertXSLLink(File f) {
        xsl_prefix = "";
        if (f.getName().equalsIgnoreCase(iFileName)) {
            return;
        }
        for (int i = 1; i < dir_level; i++) {
            // i = 1 instead of 0 because later 'f' used as getParent()
            xsl_prefix = xsl_prefix + PREFIX_UNIT;
        }
        boolean already_ins = false;
        try {
            File inF = new File(f.getParent() + File.separator + f.getName()
                + tmpFileExt);
            if (!f.renameTo(inF)) {
                //if can't rename - crate new and copy content
                inF.createNewFile();
                FileWriter fw = new FileWriter(inF);
                BufferedReader fr = new BufferedReader(new FileReader(f));
                while (fr.ready()) {
                    String data = fr.readLine();
                    if (data.indexOf("?xml-stylesheet") != -1) {
                        already_ins = true;
                    }
                    fw.write(data + "\n");
                }
                fr.close();
                fw.flush();
                fw.close();
            }
            if (!f.exists()) {
                f.createNewFile();
            }
            FileWriter fw = new FileWriter(f);
            BufferedReader fr = new BufferedReader(new FileReader(inF));
            int lineCnt = 0;
            while (fr.ready()) {
                String data = fr.readLine();
                lineCnt++;
                if (!already_ins && lineCnt == 2) {
                    if ((new File(f.getParent(), xsl_prefix + ref_file_name
                        + XSL_EXT)).exists()) {
                        fw.write("<?xml-stylesheet type='text/xsl' href='"
                            + xsl_prefix + ref_file_name + XSL_EXT + "'?>\n");
                        System.out.println("Insert link to xsl file for "
                            + f.getParent() + File.separator + f.getName());
                    }
                }
                fw.write(data + "\n");
            }
            fr.close();
            fw.flush();
            fw.close();
            if (backupExt == null) {
                inF.delete();
            } else {
                inF.renameTo(new File(f.getParent(), f.getName() + backupExt));
            }
        } catch (Exception e) {
            System.err
                .println("Unexpected exception while checks for link to xsl: "
                    + e);
            e.printStackTrace(System.err);
        }
    }

    protected void findFiles(File root) {
        File[] tmpStore;
        dir_level++;
        if (root == null) {
            return;
        }
        if (root.isDirectory()) {
            tmpStore = root.listFiles();
            if (tmpStore != null) {
                for (int i = 0; i < tmpStore.length; i++) {
                    findFiles(tmpStore[i]);
                }
            } else {
                System.err.println("Can not read the directory: " + root);
            }
        }
        if (root.isFile() && root.getName().endsWith(fileext)
            && root.getPath().indexOf(specific_dir) == -1) {
            boolean linkOK = checkFileForDTDLink(root);
            if (!linkOK && !checkOnly) {
                correctDTDLink(root);
            }
            linkOK = checkFileForXSLLink(root);
            if (!linkOK && !checkOnly) {
                correctXSLLink(root);
            }
            if (insert_allowed) {
                insertXSLLink(root);
            }
        }
        dir_level--;
    }

    public int run(String[] args) {
        if (args != null) {
            parseParam(args);
        }
        File f = new File(basedir);
        basedir = f.getAbsolutePath();
        if (!f.isDirectory()) {
            System.out.println("Error. Incorrect directory name: " + basedir);
            return -1;
        }
        findFiles(f);
        return 0;
    }

    public static void main(String[] args) {
        System.exit(new CheckLinks().run(args));
    }
}
