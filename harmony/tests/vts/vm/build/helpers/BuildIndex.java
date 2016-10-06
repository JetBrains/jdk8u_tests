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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BuildIndex {

    public static final String iFileName       = "index.xml";
    public static final String iTFileName      = "index.xsl";
    public static final String PREFIX_UNIT     = "../";
    public static final String PREDEFINED_NAME = "share";
    static String              xsl_prefix      = "";
    static int                 dir_level       = -1;
    public static String       HEAD1           = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<?xml-stylesheet type='text/xsl' href='";
    public static String       HEAD2           = iTFileName + "'?>\n<index>\n";
    public static String       FL_HEAD1        = "\t<file-list name=\"";
    public static String       FL_HEAD2        = "\" up=\"";
    public static String       FL_HEAD3        = "\">\n";
    public static String       FL_TAIL         = "\t</file-list>\n";
    public static String       CP_HEAD1        = "\t<current-path>\n";
    public static String       CP_TAIL         = "\t</current-path>\n";
    public static String       CP_ELEM1        = "\t\t<list-elem name=\"";
    public static String       CP_ELEM2        = "\" up=\"";
    public static String       CP_ELEM3        = "\"/>\n";
    public static String       TAIL            = "</index>\n";
    public static String       RECORD_HEAD     = "\t\t<list-elem name=\"";
    public static String       RECORD_MID      = "\">";
    public static String       RECORD_TAIL     = "</list-elem>\n";

    String                     basedir         = ".", fileext = ".xml";
    boolean                    recursion       = true;

    protected boolean parseParam(String[] params) {
        String helpMsg = "Look through the directory tree and generate index files.\nUsage:\n"
            + "-basedir \"name\"\t\tname of the directory to statrt\n"
            + "-fileext \"name\"\t\textension of files to index\n"
            + "-nr\t\t\tdon't process subdirectoty (by default, false)";
        try {
            for (int i = 0; i < params.length; i++) {
                if ("-basedir".equalsIgnoreCase(params[i])) {
                    basedir = params[++i];
                } else if ("-nr".equalsIgnoreCase(params[i])) {
                    recursion = false;
                } else if ("-fileext".equalsIgnoreCase(params[i])) {
                    fileext = params[++i];
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

    protected String getCurTestDirName(File curplace) {
        String retVal = curplace.getParent();
        if (retVal.startsWith(basedir)
            && (basedir.length() + 1) < retVal.length()) {
            return retVal.substring(basedir.length() + 1);
        }
        return "";
    }

    protected void createCurPathLink(FileWriter fw, File idata) {
        String upRef = "";
        if (fw == null || idata == null) {
            return;
        }
        if (idata.getAbsolutePath().indexOf(basedir) == -1) {
            return;
        }
        File tmpFile = idata;
        String data = "";
        try {
            while (!basedir.equals(tmpFile.getParent())) {
                File up = new File(tmpFile.getParent(), "../" + iFileName);
                if (up.exists() && up.isFile()) {
                    if (upRef.length() == 0) {
                        upRef = "./" + upRef;
                    } else {
                        upRef = "../" + upRef;
                    }
                }
                String name = tmpFile.getParent();
                if (name.lastIndexOf(File.separatorChar) > 0) {
                    name = name
                        .substring(name.lastIndexOf(File.separatorChar) + 1);
                }
                data = CP_ELEM1 + name + CP_ELEM2 + upRef + iFileName
                    + CP_ELEM3 + data;
                tmpFile = new File(tmpFile.getParent());
            }
            fw.write(CP_HEAD1 + data + CP_TAIL);
        } catch (Exception e) {
            System.err
                .println("Unexpected exception while calculate 'current path' reference: "
                    + e);
        }
    }

    protected FileWriter createIFile(File idata) {
        if (idata == null) {
            return null;
        }
        File place = idata.getParentFile();
        if (place == null || !place.isDirectory()) {
            return null;
        }
        FileWriter fw;
        try {
            idata.createNewFile();
            fw = new FileWriter(idata);
            String upRef = "";
            try {
                File up = new File(place, "../" + iFileName);
                if (up.exists() && up.isFile()) {
                    upRef = "../" + iFileName;
                } else if (basedir.equals(place.getAbsolutePath())) {
                    upRef = iFileName;
                }
            } catch (Exception e) {
                System.err
                    .println("Unexpected exception while calculate 'up' reference: "
                        + e);
            }
            fw.write(HEAD1 + xsl_prefix + HEAD2);
            createCurPathLink(fw, idata);
            fw.write(FL_HEAD1 + getCurTestDirName(idata) + FL_HEAD2 + upRef
                + FL_HEAD3);
            return fw;
        } catch (Exception e) {
            System.err.println("Unexpected exception while create index file: "
                + e);
            e.printStackTrace(System.err);
            return null;
        }
    }

    protected void writeData(FileWriter fw, String data, String shortName)
        throws IOException {
        fw.write(RECORD_HEAD + shortName + RECORD_MID + data + RECORD_TAIL);
    }

    protected void closeIFile(FileWriter fw) {
        if (fw != null) {
            try {
                fw.write(FL_TAIL);
                fw.write(TAIL);
                fw.flush();
                fw.close();
            } catch (IOException ioe) {
                System.err
                    .println("Unexpected exception while close index file: "
                        + ioe);
                ioe.printStackTrace(System.err);
            }
        }
    }

    /*
     * If directory has subdirectory (not 'share') need index file
     */
    protected boolean needIndexFileDir(File[] data) {
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (data[i].isDirectory()
                    && data[i].getName().indexOf(PREDEFINED_NAME) == -1) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * If directory has more than 1 test (file with test extension) need index
     * file
     */
    protected boolean needIndexFileFiles(File[] data) {
        if (data != null) {
            int f_cnt = 0;
            for (int i = 0; i < data.length; i++) {
                if (data[i].isFile() && data[i].getName().endsWith(fileext)) {
                    f_cnt++;
                    if (f_cnt >= 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected void addFiles(File[] data) {
        boolean itIsTestDir = false;
        if (data == null || data.length == 0) {
            return;
        }
        File place = data[0].getParentFile();
        if (place == null || !place.isDirectory()) {
            return;
        }
        xsl_prefix = "";
        for (int i = 0; i < dir_level; i++) {
            xsl_prefix = xsl_prefix + PREFIX_UNIT;
        }
        if (needIndexFileDir(data)) {
            File ifile = new File(place, iFileName);
            if (ifile.getPath().indexOf(PREDEFINED_NAME) != -1
                || ifile.getName().indexOf(PREDEFINED_NAME) != -1) {
                return;
            }
            try {
                FileWriter fw = createIFile(ifile);
                for (int i = 0; i < data.length; i++) {
                    if (data[i].isDirectory()
                        && data[i].getPath().indexOf(PREDEFINED_NAME) == -1
                        && data[i].getName().indexOf(PREDEFINED_NAME) == -1) {
                        File[] dirSet = data[i].listFiles();
                        if (dirSet != null) {
                            if (!needIndexFileDir(dirSet)
                                && !needIndexFileFiles(dirSet)) {
                                for (int y = 0; y < dirSet.length; y++) {
                                    if (dirSet[y].isFile()
                                        && dirSet[y].getName()
                                            .endsWith(fileext)) {
                                        if (dirSet[y].getName().length() > fileext
                                            .length()) {
                                            writeData(fw, data[i].getName()
                                                + "/" + dirSet[y].getName(),
                                                dirSet[y].getName().substring(
                                                    0,
                                                    dirSet[y].getName()
                                                        .length()
                                                        - fileext.length()));
                                        } else {
                                            writeData(fw, data[i].getName()
                                                + "/" + dirSet[y].getName(),
                                                dirSet[y].getName());
                                        }

                                    }
                                }
                            } else {
                                writeData(fw, data[i].getName()
                                    + File.separator + iFileName, data[i]
                                    .getName());
                            }
                        }
                    } else if ((data[i].isFile()
                        && data[i].getName().endsWith(fileext) && data[i]
                        .getName().indexOf(iFileName) == -1)) {
                        if (data[i].getName().length() > fileext.length()) {
                            writeData(fw, data[i].getName(), data[i].getName()
                                .substring(
                                    0,
                                    data[i].getName().length()
                                        - fileext.length()));
                        } else {
                            writeData(fw, data[i].getName(), data[i].getName());
                        }
                    }
                }
                closeIFile(fw);
            } catch (Exception e) {
                System.out
                    .println("Unexpected exception while generate index file: "
                        + e);
            }
        } else if (needIndexFileFiles(data)) {
            try {
                File ifile = new File(place, iFileName);
                FileWriter fw = createIFile(ifile);
                for (int i = 0; i < data.length; i++) {
                    if (data[i].isFile() && data[i].getName().endsWith(fileext)
                        && data[i].getName().indexOf(iFileName) == -1) {
                        if (data[i].getName().length() > fileext.length()) {
                            writeData(fw, data[i].getName(), data[i].getName()
                                .substring(
                                    0,
                                    data[i].getName().length()
                                        - fileext.length()));
                        } else {
                            writeData(fw, data[i].getName(), data[i].getName());
                        }
                    }
                }
                closeIFile(fw);
            } catch (Exception e) {
                System.out
                    .println("Unexpected exception while generate index file: "
                        + e);
            }
        }
    }

    protected void findFiles(String root) {
        findFiles(new File(root));
    }

    protected void findFiles(File root) {
        File[] tmpStore;
        dir_level++;
        if (root == null) {
            return;
        }
        if (root.isDirectory()) {
            tmpStore = root.listFiles();
            addFiles(tmpStore);
            if (tmpStore != null && recursion) {
                for (int i = 0; i < tmpStore.length; i++) {
                    findFiles(tmpStore[i]);
                }
            } else {
                System.err.println("Can not read the directory: " + root);
            }
        }
        dir_level--;
    }

    protected void generateXSL(String name) {
        File f = new File(basedir, name);
        try {
            f.createNewFile();
            FileWriter fw = new FileWriter(f);
            fw
                .write("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
                    + "\t<xsl:template match=\"/\">\n"
                    + "\t\t<xsl:apply-templates/>\n"
                    + "\t</xsl:template>\n"
                    + "\t<xsl:template match=\"file-list\">\n"
                    + "\t\t<a href=\"{./@up}\">up</a>"
                    + "\t\t<p><a name=\"filelist\"></a>\n"
                    + "\t\t<table border='1' cellpadding=\"10%\">\n"
                    + "\t\t<tr bgcolor=\"yellow\" text=\"#000000\" link=\"#444444\" vlink=\"#7E7E7E\">\n"
                    + "\t\t<th align=\"center\">Test set for <i><xsl:value-of select=\"./@name\"/></i></th></tr>\n"
                    + "\t\t\t<xsl:for-each select=\"list-elem\">\n"
                    + "\t\t\t<xsl:sort order=\"ascending\" select=\".\"/>\n"
                    + "\t\t\t\t<tr valign=\"middle\" bgcolor=\"#FFFFEE\" text=\"#000000\" link=\"#444444\" vlink=\"#7E7E7E\">\n"
                    + "\t\t\t\t<td><ul><li><a href=\"{.}\"><xsl:value-of select=\"./@name\"/></a></li></ul></td>\n"
                    + "\t\t\t\t</tr>\n"
                    + "\t\t\t</xsl:for-each>\n"
                    + "\t\t</table></p>\n"
                    + "\t</xsl:template>\n"
                    + "\t<xsl:template match=\"current-path\">\n"
                    + "\t\t<table border='0' cellpadding=\"10%\">\n"
                    + "\t\t\t<tr bgcolor=\"yellow\" text=\"#000000\" link=\"#444444\" vlink=\"#7E7E7E\">\n"
                    + "\t\t\t<td> Current path is: \n"
                    + "\t\t\t<xsl:for-each select=\"list-elem\">\n"
                    + "\t\t\t\t<a href=\"{./@up}\"><xsl:value-of select=\"./@name\"/></a> /\n"
                    + "\t\t\t</xsl:for-each>\n"
                    + "\t\t\t</td>\n\t\t\t</tr>\n"
                    + "\t\t</table>\n"
                    + "\t</xsl:template>\n"
                    + "</xsl:stylesheet>\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            System.err.println("Unexpected exception while create xsl file: "
                + e);
            e.printStackTrace(System.err);
        }
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
        generateXSL(iTFileName);
        findFiles(f);
        return 0;
    }

    public static void main(String[] args) {
        System.exit(new BuildIndex().run(args));
    }
}
