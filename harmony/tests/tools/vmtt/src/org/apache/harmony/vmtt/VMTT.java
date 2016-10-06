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
package org.apache.harmony.vmtt;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.harmony.vmtt.ccode.CodeFileParser;
import org.apache.harmony.vmtt.ccode.CodeFileFormatException;
import org.apache.harmony.vmtt.cdecode.CodeFileGenerator;
import org.apache.harmony.vmtt.ir.*;

/**
 * @author Aleksey V. Golubitsky
 * @version $Revision: 1.11 $
 */

public class VMTT implements Options, ReturnCodes, ErrorMessages {
    
    private final static String CS_USAGE =
        "Usage: vmtt [options] <file | directory>\n\n" +
        "  -A\t\t\tAssemble asm file\n" +
        "  -D\t\t\tDisassemble class file\n" +
        "  -C\t\t\tCode ccode file\n" +
        "  -E[code_type]\t\tDecode class file\n" +
        "\t\t\tcode_type: asm - represents code as assembler\n" +
        "\t\t\t           bin - represents code as binary\n" +
        "\t\t\t-E option equals to -Easm option\n" +
        "  -d<directory>\t\tSpecify where to place generated files\n" +
        "  -o<file>\t\tPlace the output into <file>\n" +
        "  -r\t\t\tProcess files of the specified directory recursively\n" +
        "  -x<extensions>\tSet extensions list for file procesing\n\n" +
        "Example: vmtt -Easm -r -dC:\\tests\\vm -x.class.classX.classXX bin_dir\n";

    private final String ext_class = ".class";
    private final String ext_ccode = ".ccode";
    private final String ext_jasm  = ".jasm";
    private String[] ext_filter = null;
    private File fSrc_ = null;
    private File fOut_ = null;
    private String dest_dir = null;
    
    public static VMTTLogger log = new VMTTLogger();
    public static int options = 0;

    
    public static Float minorVersion = null;
    public static Float majorVersion = null;
    /** Main method
     * @param args - parameters for the VMTT
     */
    public static void main(String[] args) {
        new VMTT().run(args);
    }
    
    public int run(String[] args) {
        if (args == null || args.length == 0) {
            showHelp();
            return R_PARAM_MISSED;
        }
        try {
            setOptions(args);
        } catch (Exception e) {
            log.warning(e.getMessage());
            return R_PARAM_INVALID;
        }
        selectFile(fSrc_);
        return R_OK;
    }
    
    private void setOptions(String[] args)
   	    throws IllegalArgumentException, ArrayIndexOutOfBoundsException {

        fSrc_ = new File(args[args.length - 1]);

        for (int i = 0; i < (args.length - 1); i++) {
            String arg = args[i];
            if (arg.equals("-C")) {
                options |= O_CODE;
            } else if (arg.startsWith("-E")){
                options |= O_DECODE;
                String code_type = getOptionValue("-E", arg);
                if (code_type.equals("asm")) {
                    options |= O_CODE_ASM;
                } else if (code_type.equals("bin")) {
                    options |= O_CODE_BIN;
                } else if (code_type.length() == 0){
                    options |= O_CODE_ASM;
                } else {
                    throw new IllegalArgumentException("Invalid option " +
                                                       Utils.quote("-E" + code_type));
                }
            } else if (arg.equals("-A")) {
                options |= O_ASSEMBLE;
            } else if (arg.equals("-D")) {
                options |= O_DISASSEMBLE;
            } else if (arg.startsWith("-o")) {
                String output_file = getOptionValue("-o", arg);
                fOut_ = new File(output_file);
            } else if (arg.equals("-r")) {
                options |= O_RECURSIVE;
            } else if (arg.startsWith("-x")) {
                String ext_list = getOptionValue("-x", arg);
                setExtensionsFilter(ext_list);
            } else if (arg.startsWith("-d")) {
                dest_dir = getOptionValue("-d", arg);
            } else {
                throw new IllegalArgumentException("Invalid option " +
                                                   Utils.quote(arg));
            }
        }
        
        String minv = System.getProperty("force_minorversion");
        if(minv != null) {
	        try {
	        	minorVersion =  new Float(minv);
	        }
	        catch(NumberFormatException e1) {
	        	log.warning("Error parsing force_minorversion VM argument. Using default.");
	        }
        }
        
        String majv = System.getProperty("force_majorversion");
        if(majv != null) {
	        try {
	        	majorVersion =  new Float(majv);
	        }
	        catch(NumberFormatException e1) {
	        	log.warning("Error parsing force_majorversion VM argument. Using default.");
	        }
        }
        
        checkOptions();
    }
    
    private String getOptionValue(String option, String optionAndValue) {
        return optionAndValue.substring(option.length(), optionAndValue.length());
    }
    
    private void setExtensionsFilter(String ext_list)
    throws IllegalArgumentException {
        StringTokenizer st = new StringTokenizer(ext_list, ".");
        int tcount = st.countTokens();
        if (tcount > 0) {
            ext_filter = new String[tcount];
            int j = 0;
            while (st.hasMoreTokens()) {
                ext_filter[j++] = "." + st.nextToken();
            }
        } else {
            throw new IllegalArgumentException("Invalid extensions list " +
                                               Utils.quote(ext_list));
        }
    }
    
    private void checkOptions()
    throws IllegalArgumentException {
        if ((options & O_CODE) == 0 && (options & O_DECODE) == 0 &&
            (options & O_ASSEMBLE) == 0 && (options & O_DISASSEMBLE) == 0) {
            String fn = fSrc_.getName().toLowerCase(); 
            if (fn.endsWith(ext_class)) {
                options |= O_DECODE;
                options |= O_CODE_ASM;
            } else if (fn.endsWith(ext_ccode)) {
                options |= O_CODE;
            } else if (fn.endsWith(ext_jasm)) {
                options |= O_ASSEMBLE;
            } else {
                String msg = "Unknown type of the file " + fSrc_.getAbsolutePath() +
                             "\nCan't apply default action.\n" +
                             "Set one of the following options, please (-A, -D, -C, -E)";
                throw new IllegalArgumentException(msg);
            }
        }
        if (fOut_ != null && fSrc_.isDirectory()) {
            String msg = "Option -o can't be applied to the directory.";
            throw new IllegalArgumentException(msg);
        }
        if (!fSrc_.isDirectory() && (options & O_RECURSIVE) != 0) {
            String msg = "Option -r can't be applied to file.";
            throw new IllegalArgumentException(msg);
        }
        if (!fSrc_.isDirectory() && (ext_filter != null)) {
            String msg = "Option -x can't be applied to file.";
            throw new IllegalArgumentException(msg);
        }
    }
    
    private void checkDestDirectory() {
        File f = new File(dest_dir);
        if (f.exists()) {
            return;
        }
        String question = "Directory " + dest_dir +
                          " doesn't exist. Create? [y/n] ";
        System.out.print(question);
        char answer = 0;
        try {
            answer = (char) System.in.read();
        } catch (IOException ioe) {
        }
        if (answer == 'y') {
            try {
                if (!f.mkdirs()) {
                    log.warning("Can't create directory: " + dest_dir);
                }
            } catch (SecurityException se) {
                log.warning("You don't have permissons to create directory: " +
                            dest_dir);
            }
        }
    }
    
    private File getOutputFile(File f) {
        String src_fn = f.getName();
        String src_wo_ext = src_fn.substring(0, src_fn.lastIndexOf('.'));
        String out_ext = "";
        if ((options & O_ASSEMBLE) != 0 || (options & O_CODE) != 0) {
            out_ext = ext_class;
        } else if ((options & O_DECODE) != 0) {
            out_ext = ext_ccode;
        } else if ((options & O_DISASSEMBLE) != 0) {
            out_ext = ext_jasm;
        }
        String out_fn = src_wo_ext + out_ext;
        String out_dir = (dest_dir == null) ? f.getParent() : dest_dir;
        if (out_dir == null) {
            out_dir = ".";
        }
        return new File(out_dir + File.separator + out_fn);
    }
    
    private String getDefaultExtension() {
        if ((options & O_CODE) != 0) {
            return ext_ccode;
        } else if ((options & O_ASSEMBLE) != 0) {
            return ext_jasm;
        } else if ((options & O_DECODE) != 0 || (options & O_DISASSEMBLE) != 0) {
            return ext_class;
        }
        return null; 
    }
    
    private void selectFile(File f) {
        if (!f.exists()) {
		    log.warning(errs[E_FILE_NF] + f.getAbsolutePath());
		    return;
        }
        if (f.isDirectory()) {
            final String ext = getDefaultExtension();
	        File[] flist = f.listFiles(new FileFilter() {
	            public boolean accept(File f) {
	                if (f.isFile()) {
	                    if (ext_filter == null) {
	                        if (f.getName().toLowerCase().endsWith(ext)) {
			                    return true;
	                        }
	                    } else if (ext_filter != null){
	                        for (int i = 0; i < ext_filter.length; i++) {
	                            if (f.getName().toLowerCase().endsWith(ext_filter[i])) {
	                                return true;
	                            }
	                        }
	                    }
	                } else if (f.isDirectory() && ((options & O_RECURSIVE) != 0)) {
	                    return true;
	                }
	                return false;
	            }
	        });
	        for (int i = 0; i < flist.length; i++) {
	            selectFile(flist[i]);
	        }
        } if (f.isFile()) {
            processFile(f);
        }
    }
    
    private void processFile(File f) {
        if (fOut_ == null) {
            if (dest_dir != null) {
                checkDestDirectory();
            }
            fOut_ = getOutputFile(f);
        }
        log.info("Processing file: " + f.getAbsolutePath());
        if ((options & O_DISASSEMBLE) != 0 || (options & O_DECODE) != 0) {
            ClassFile classFile = new ClassFile();
            ClassFileParser classParser = VMTTFactory.getClassFileParser("Default");
            if (classParser == null) {
			    log.warning(errs[E_DCLASS_PAR_NF]);
			    return;
            }
            classParser.setClassFile(classFile);
            try {
                classParser.setInputFile(fSrc_);
                classParser.parse();
            } catch (FileNotFoundException fnfe) {
    		    log.warning(errs[E_FILE_NF] + fSrc_.getAbsolutePath());
                return;
            } catch (IOException ioe) {
    		    log.warning(errs[E_IO_READ] + fSrc_.getAbsolutePath());
    		    return;
            } catch (ClassFileFormatException cffe) {
    			log.warning(cffe.getMessage());
    			return;
            }
    		if ((options & O_DISASSEMBLE) != 0) {
    		    disassemble(classFile);
    		} else if ((options & O_DECODE) != 0) {
    		    decode(classFile);
    		}
        } else if ((options & O_CODE) != 0) {
            code(f);
        } else if ((options & O_ASSEMBLE) != 0) {
            assemble(f);
        }
        fOut_ = null;
    }
    
    private void assemble(File f) {
        log.warning("Assembler is not implemented yet.");
    }
    
    private void disassemble(ClassFile classFile) {
        log.warning("Disassembler is not implemented yet.");
    }
    
    private void code(File f) {
		ClassFile classFile = new ClassFile();
		try {
			CodeFileParser parser = VMTTFactory.getCodeFileParser("Default");
			if (parser == null) {
			    log.warning(errs[E_DCCODE_PAR_NF]);
			    return;
			}
			parser.setClassFile(classFile);
			parser.setInputFile(f);
			parser.parse();
		} catch (FileNotFoundException fnfe) {
		    log.warning(errs[E_FILE_NF] + f.getAbsolutePath());
			return;
		} catch (CodeFileFormatException cffe) {
		    int ln = cffe.getLineNumber();
		    if (ln == -1) {
		        log.warning(cffe.getMessage());
		    } else {
		        log.warning(ln, cffe.getMessage());
		    }
			return;
		} catch (NumberFormatException nfe) {
		    log.warning(nfe.getMessage());
			return;
		} catch (IOException ioe) {
		    log.warning(errs[E_IO_READ]);
		    return;
		}

		ClassFileGenerator generator = VMTTFactory.getClassFileGenerator("Default");
		if (generator == null) {
		    log.warning(errs[E_DCLASS_GEN_NF]);
		    return;
		}
		if(false && dest_dir != null) { //FIXME: bugfix 8696 is rolled back because there are no resources to make corrctions in build configuration to run builds with this bugfix.
			String className = Utils.getClassName(classFile);
			if(className == null) {
				log.warning("Can't obtain ClassName from " + f.getAbsolutePath() + " file. Using default package for generating class file.");
			}
			else {
				int lastSlash = className.lastIndexOf('/');
				String newClassDestDir = dest_dir;
				String newClassName;
				if(lastSlash != -1) {
					String sPackage =  className.substring(0, lastSlash);
					String tmp = className.substring(lastSlash);
					if(tmp.length() == 0) {
						log.warning("Name of class is empty in " + f.getAbsolutePath() + " file. Using source file name for generating class file.");
						newClassName = fOut_.getName();
					}
					else {
						newClassName = tmp + ".class";
					}
					newClassDestDir = createPackageDirs(sPackage);
				}
				else {
					newClassName = className  + ".class";
				}
				fOut_ = new File(newClassDestDir + File.separator + newClassName);
				
			}
		}
		
		generator.setClassFile(classFile);
		try {
		    generator.setOutputFile(fOut_);
			generator.generate();
		} catch (IOException ioe) {
		    log.warning(errs[E_IO_WRITE] + fOut_.getAbsolutePath());
			return;
		}
    }
    
    private String createPackageDirs(String sPackage) {
    	String dirs [] = sPackage.split("/");
    	StringBuffer parent = new StringBuffer(/*fOut_.getParent()*/dest_dir);
    	for(int i = 0; i < dirs.length; ++i) {
    		parent.append(File.separator + dirs[i]);
    	}
    	String packageDirs = parent.toString();
    	File f = new File(packageDirs);
	    if(!f.exists()) {
	    	try {
	            if (!f.mkdirs()) {
	                log.warning("Can't create directory: " + packageDirs);
	                return dest_dir;
	            }
	        } catch (SecurityException se) {
	            log.warning("You don't have permissons to create directory: " +
	            		packageDirs);
	            return dest_dir;
	        }
    	}
    	return packageDirs;
    }
    
    private void decode(ClassFile classFile) {
	    CodeFileGenerator generator = VMTTFactory.getCodeFileGenerator("Default");
	    if (generator == null) {
	        log.warning(errs[E_DCCODE_GEN_NF]);
	        return;
	    }
        generator.setClassFile(classFile);
        try {
            generator.setOutputFile(fOut_);
        } catch (IOException ioe) {
		    log.warning("Can't create file: " + fOut_.getAbsolutePath());
		    return;
        }
	    if ((options & O_CODE_ASM) == 0 && (options & O_CODE_BIN) == 0) {
	        options |= O_CODE_ASM;
	    }
        try {
            if ((options & O_CODE_ASM) != 0) {
                generator.generateAsmCode();
            } else if ((options & O_CODE_BIN) != 0) {
                generator.generateBinCode();
            }
        } catch (IOException ioe) {
		    log.warning(errs[E_IO_WRITE] + fOut_.getAbsolutePath());
		    return;
        }
    }

	private static void showHelp() {
        System.out.println(CS_USAGE);
	}
}
