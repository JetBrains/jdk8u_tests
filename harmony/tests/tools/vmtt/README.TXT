1. ARCHIVE CONTENTS
-------------------

The archive contains the source files of VMTT (Virtual Machine Testing Tool), 
the building environment, documentation and the source examples.

After extracting the archive, the following directories will appear
in <ROOT_DIR>/tools/VMTT, where <ROOT_DIR> is a root directory of VTS VM bundle:


  bin      - Files required to run the tool
  src      - Source files of the tool            
  examples - Examples of .ccode source files
  doc      - Documentation for the tools. It contains the user guide 
             and the description of .ccode format.


2. TOOLS REQUIRED FOR THE BUILD
-----------------------------------------------------------

To build the Java* sources contained in the src/ directory, 
it is necessary to install and configure the following tools in the 
user environment:

+ Apache Ant 	 - Build tool: Ant 1.6.2 or higher, from 
                   http://ant.apache.org

+ JDK 1.4.2    - J2SE* 1.4.2 (or higher) compatible JDK


3. BUILDING
------------------------------------------------

To start the build, change directory into <ROOT_DIR>/tools/VMTT 
and then run Apache Ant by typing "ant". Make sure that ANT_HOME 
environment variable is set and points to the location of Apache Ant 
(For example C:\apache-ant-1.6.5). Ant will run against the default 
target and compile all Java* sources from the 
<ROOT_DIR>/tools/VMTT/src folder.


The build produces a VMTT.jar file. This file is placed in the following directory:

<ROOT_DIR>/tools/VMTT.dest/bin/


4. COMPILING .CCODE SOURCE FILES
-----------------------------

Typically the VMTT is used for compiling .ccode source files into Java* class files.
The following command will compile example.ccode, having that both vmtt.jar and 
example.ccode are located in the current directory.

<JAVA_HOME>/bin/java -jar vmtt.jar example.ccode

where <JAVA_HOME> is a path to JDK*

example.class will appear in the current directory.

See the documentation in <ROOT_DIR>/tools/VMTT/doc/ for details.


5. KNOWN ISSUES
---------------
-A and -D options are not implemented.
Also class file names are generated according to source file names, but not class 
names. It will be fixed further. 

6. DISCLAIMER AND LEGAL INFORMATION
------------------------------------

*) Other brands and names are the property of their respective owners.

