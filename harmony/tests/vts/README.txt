
                               =================
                               VTS VM Test Suite
                               =================

Archive Content
---------------

This archive contains VTS VM test suite for testing Java* Virtual Machine
and tools, required for its building and running.

The structure and content of the archive is as follows:

<ROOT_DIR>
       |
       \---tools          - Tools, required for building and running VTS VM test suite
       |     |
       |     +-harness    - Test Harness tool for running VTS VM test suite
       |     | 
       |     +-share      - Shared sources needed for building tools and tests
       |     |
       |     +-vmtt       - VMTT tool for compiling *.ccode files
       | 
       |
       \---vts            - VTS VM test suite sources


             
Pre-requisites for Building VTS VM
----------------------------------

To build VTS VM suite the following tools are required to be preinstalled
on your system:

  1) JDK version 1.5.0
     http://java.sun.com
     http://www.jrockit.com/

  2) Apache Ant, version 1.6 or higher 
     http://ant.apache.org

  3) C compiler, either gcc for Linux*, or one of the following for Windows*:
         + Microsoft* 32-bit C/C++ Compiler v.7 or higher,
         + Windows* platform SDK,
         + Microsoft* Visual Studio .NET* 2003 or higher
           http://www.microsoft.com/downloads/

The following components will be self-downloaded during the build:

  1) Ant Cpp Tasks collection
     http://sourceforge.net/project/showfiles.php?group_id=36177&package_id=28636

  2) Ant-Contrib collection of tasks, version 1.0b1 or higher
     http://sourceforge.net/project/showfiles.php?group_id=36177&package_id=28636

  3) xercesImpl.jar and xml-apis.jar
     http://www.apache.org/dist/xml/xerces-j

  4) jasmin.jar
     http://sourceforge.net/projects/jasmin



BUILDING VTS VM
---------------

To build VTS VM suite:

0) setup environment variables:
    JAVA_HOME - with path to JDK 1.5
    ANT_HOME - with path to to Ant
    PATH - with path to directory containing ant scripts (ANT_HOME/bin)
    CLASSPATH - with path to junit.jar (or place junit.jar into ANT_HOME/lib)

1) Go to vts/vm/build/ directory:

    > cd vts/vm/build/

2) Setup the workspace for local use:
    
    > ant setup

    - this will build supporting frameworks (Test Harness and VMTT tool)
    and create build configuration files.

3) Edit build.properties file to set up path to JDK home, tested VM
and proxy settings (if you're using it)

4) download external dependencies:

    > ant fetch-depends

    - it will download and check external tools needed 
    for building and running VTS VM test suite.

5) and finally to build and run the suite:
    
    > ant build-vts
    > ant run-tests

or simple:

    > ant

6) built VTS and test execution results will be placed under

    vts/dest

directory.



KNOWN ISSUES
------------

If you meet Out Of Memory error while building the suite, try
to extends ANT_OPTS system variable with the following option:

export ANT_OPTS="-XX:MaxPermSize=512m"




*) Other brands and names are the property of their respective owners

