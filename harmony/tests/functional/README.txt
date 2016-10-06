INTEL CONTRIBUTION TO APACHE HARMONY
         February 27, 2007
====================================

This archive contains the contribution to the Apache Harmony project 
from Intel. The contribution consists of the following component:

  - Functional Test Suite

The Functional test suite is a collection of micro scenarios 
for testing various functional parts of the Java* implementation.
The tests are configured to test the Harmony implementation of Java*.


1. ARCHIVE CONTENTS
-------------------
 
The archive contains the Functional test suite for testing Java* run time,
test sources and components, required for building and running the test suite.
After extracting the archive, the following directories and files
appear under <EXTRACT_DIR>, where <EXTRACT_DIR> is the location
of the archive content:

<EXTRACT_DIR>
       |
       +--- config    - harness configuration files   
       |
       \--- src       - test sources and support files


2. TOOLS AND ENVIRONMENT VARIABLES REQUIRED FOR THE BUILD
---------------------------------------------------------

To build the Functional suite, install and configure the following tools 
in the user environment:

+ JDK*              - Java* Development Kit, version 1.5.0
                      http://java.sun.com/ 
                      http://www.jrockit.com/

+ Apache Ant        - Apache Ant, version 1.6.5 or higher
                      http://ant.apache.org/

+ Subversion        - Subversion* client, version 1.3.2 or higher
                      http://subversion.tigris.org/

Make sure that you set up the following environment variables:

+JAVA_HOME - with path to JDK* 1.5

+PATH      - with paths to directories containing the ant launcher script
             (apache-ant-1.6.5/bin) and the svn executable (Subversion/bin)

Make sure that you have the following .jar files / components:

+ Test Harness      - th.jar

+ VMTT tool         - vmtt.jar

+ Jasmin* assembler - jasmin.jar

To get these components, follow instructions provided with the VTS VM test
suite (http://issues.apache.org/jira/browse/HARMONY-3206).


3. BUILDING AND RUNNING THE FUNCTIONAL TEST SUITE
--------------------------------------------------

To build the Functional test suite, do the following:

1. Create the depends/ directory in the root directory of the functional
   test suite and go to this directory:

   > mkdir depends
   > cd depends

   NOTE: If you create the depends/ directory in another location, remember
         to specify this path when invoking ant using the -Ddepends option,
         for example:
                     > ant -Ddepends=../depends ....

2. Fill the depends/ directory with required components:

   2.1 Copy three jar files obtained from the VTS VM test suite to the
       depends/ directory.

   2.2 Go to the root directory of the Functional test suite and download
       additional sources from the Harmony repository by running
       the following command in the depends/ directory:

       > ant fetch-depends

3. Set up the path to the tested VM executable. You can either edit the build.xml
   file, or use the following option:

  -Dtest.java.cmd=<path-to-VM>

   Example:
           > ant -Dtest.java.cmd=C:\harmony-jdk-r508758\jre\bin\java.exe ....


4. Build and run the suite using the following commands:

   > ant build
   > ant run-tests

   The default target for the test suite build includes both build and run
   steps, so you can perform them using the single command:

   > ant

   Built test files and test execution results are placed to the created bin/
   subdirectory of the root directory. You can point another location for these
   files using the following option:

   -Dbin=<path-to-bin-dir>

   Example:
           > ant -Dbin=C:/temp/func.1 ....


4. KNOWN ISSUES
---------------

- If the Out-of-Memory” error occurs while building the suite, extend
  the ANT_OPTS system variable using the following option:

  export ANT_OPTS="-XX:MaxPermSize=512m"


5. DISCLAIMER AND LEGAL INFORMATION
------------------------------------

*) Other brands and names are the property of their respective owners.




