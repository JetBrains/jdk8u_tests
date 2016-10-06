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
 * @author Valery Shakurov
 * @version $Revision: 1.5 $
 */
package org.apache.harmony.harness.plugins.variationtests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used to produce an enumeration of files from a list of directories, files
 * and, optionally, filename filters.
 */
public class FileSet extends AbstractVariation {
    Vector files;

    public Enumeration getVariants() {
        return files.elements();
    }

    /**
     * Usage: FileSet -var <varName>-title <Title><sequence>* where the
     * sequence is one of the following: [-filter <regexp>] -baseDir <dir>
     * -fileList <file>[-filter <regexp>] -dir <dir>[-baseDir <dir>] -file
     * <file>[-baseDir <dir>] -files <file><file>... (this sequence is always
     * the last one)
     */
    public String[] setup(String[] args) throws SetupException {
        String baseDir = null;
        String filter = null;
        ArrayList tail = new ArrayList();
        args = super.setup(args);
        files = new Vector();
        boolean filesAdded = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-dir")) {
                if (++i >= args.length) {
                    throw new SetupException("Option -dir without value");
                }
                addDir(args[i], filter);
                filesAdded = true;
            } else if (args[i].equalsIgnoreCase("-filter")) {
                if (++i >= args.length) {
                    throw new SetupException("Option -filter without value");
                }
                filter = args[i];
            } else if (args[i].equalsIgnoreCase("-baseDir")) {
                if (++i >= args.length) {
                    throw new SetupException("Option -baseDir without value");
                }
                baseDir = args[i];
            } else if (args[i].equalsIgnoreCase("-fileList")) {
                if (++i >= args.length) {
                    throw new SetupException("Option -fileList without value");
                }
                addFileList(baseDir, args[i], filter);
                filesAdded = true;
            } else if (args[i].equalsIgnoreCase("-file")) {
                if (++i >= args.length) {
                    throw new SetupException("Option -file without value");
                }
                addFile(baseDir, args[i]);
                filesAdded = true;
            } else if (args[i].equalsIgnoreCase("-files")) {
                if (++i >= args.length) {
                    throw new SetupException("Option -files without a value");
                }
                filesAdded = true;
                for (; i < args.length; ++i) {
                    addFile(baseDir, args[i]);
                }
            } else {
                tail.add(args[i]);
            }
        }

        if (!filesAdded) {
            throw new SetupException(
                "neither -dir nor -fileList nor -file option is specified");
        }
        return Util.toStringArr(tail);
    }

    public void addFile(File f) {
        if (f.exists()) {
            files.add(new ScalarValue(this, f.getName(), new FileWrapper(f)));
        }
    }

    public void addFileList(String dir, String fileList, String filter)
        throws SetupException {
        File d;
        // use the specified directory as the base
        // for the files in the list if their
        // paths are relative, Otherwise use the
        // directory of the list file
        if (dir != null) {
            d = new File(dir);
        } else {
            d = new File(fileList);
            String parent = d.getParent();
            d = new File(parent == null ? "" : parent);
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileList));
            String line;
            while (null != (line = reader.readLine())) {
                if (line.length() != 0 && line.charAt(0) != '#') {
                    // skip comments or empty lines
                    addFile(new File(d, line));
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new SetupException(e.toString());
        } catch (IOException e) {
            throw new SetupException(e.toString());
        }
    }

    public void addFile(String fileName) {
        addFile(new File(fileName));
    }

    public void addFile(String baseDir, String fileName) {
        addFile(new File(baseDir, fileName));
    }

    public void addDir(String dirName, String filter) {
        File d = new File(dirName);
        WildCard wc = new WildCard(filter);
        if (d.exists()) {
            File[] fls = d.listFiles(wc);
            for (int i = 0; i < fls.length; i++) {
                files.add(new ScalarValue(this, fls[i].getName() + "("
                    + fls[i].length() + ")", new FileWrapper(fls[i])));
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public String getVar() {
        return var;
    }
}

class WildCard implements FilenameFilter {
    Pattern p;

    public WildCard(String template) {
        if (template == null) {
            // Default - all files
            template = ".*";
        }

        p = Pattern.compile(template);
    }

    public boolean accept(File dir, String name) {
        Matcher m = p.matcher(name);
        return m.matches();
    }

}