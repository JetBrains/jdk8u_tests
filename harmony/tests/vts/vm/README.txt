PACKAGE CONTENTS
----------------

Vts package contains the sources of the VTS VM test suite for testing Java* Virtual Machine.
It also includes documentation, building and execution environment for this suite.
 
    <ROOT_DIR>
        |
        \---tools
        |
        \---vts            
            |
            \-vm             
              |
              +- build    - Building scripts
              |
              +- config   - Configuration scripts
              |
              +- docs     - Scripts for documentation generation and measuring test coverage
              |
              +- src      - VTS VM test sources                     





TOOLS REQUIRED FOR THE VTS VM BUILD
-----------------------------------
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

1) Go to ./build directory:

    > cd ./build

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

    ../dest

directory.


KNOWN ISSUES
------------

1) If you meet Out Of Memory error while building the suite on Linux, try
to extends ANT_OPTS system variable with the following option:

export ANT_OPTS="-XX:MaxPermSize=512m"


2) The following tests need to be fixed:
     vts/vm/src/test/vm/jvmti/events/DataDumpRequest/DataDumpRequest0101/DataDumpRequest0101.cpp
        - insert code, which stops process on Windows*
        
     vts/vm/src/test/vm/jvmti/funcs/GetCurrentThreadCPUTime/GetCurrentThreadCPUTime0101/GetCurrentThreadCPUTime0101.cpp
        - add better check for test result
        
     vts/vm/src/test/vm/jvmti/funcs/GetCurThrCPUTimInfo/GetCurThrCPUTimInfo0101/GetCurThrCPUTimInfo0101.cpp
        - add better check for test result
        
     vts/vm/src/test/vm/jvmti/funcs/GetObjectSize/GetObjectSize0101/GetObjectSize0101.cpp
        - add better check for test result
        
     vts/vm/src/test/vm/jvmti/funcs/GetThreadCPUTime/GetThreadCPUTime0101/GetThreadCPUTime0101.cpp
        - add better check for test result
        
     vts/vm/src/test/vm/jvmti/funcs/GetThreadCPUTimerInfo/GetThreadCPUTimerInfo0101/GetThreadCPUTimerInfo0101.cpp
        - add better check for test result
        
     vts/vm/src/test/vm/jvmti/funcs/GetTimerInfo/GetTimerInfo0101/GetTimerInfo0101.cpp
        - add better check for test result
        
     vts/vm/src/test/vm/jvmti/funcs/IterateInstances/IterateInstances0101/IterateInstances0101.cpp
        - add better check for test result
        
     vts/vm/src/test/vm/jvmti/funcs/IterateObjects/IterateObjects0101/IterateObjects0101.cpp
        - add better check for test result
        
     vts/vm/src/test/vm/jvmti/funcs/IterateObjectsFromObject/IterateObjectsFromObject0101/IterateObjectsFromObject0101.cpp
        - add better check for test result
        
     vts/vm/src/test/vm/jvmti/funcs/IterateOverHeap/IterateOverHeap0101/IterateOverHeap0101.cpp
        - add better check for test result
        
     vts/vm/src/test/vm/jvmti/funcs/SuspendThreadList/SuspendThreadList0101/SuspendThreadList0101.java
     vts/vm/src/test/vm/jvmti/funcs/SuspendThreadList/SuspendThreadList0102/SuspendThreadList0102.java
     vts/vm/src/test/vm/jvmti/funcs/SuspendThreadList/SuspendThreadList0103/SuspendThreadList0103.java
     vts/vm/src/test/vm/jvmti/funcs/SuspendThreadList/SuspendThreadList0104/SuspendThreadList0104.java
     vts/vm/src/test/vm/jvmti/funcs/SuspendThreadList/SuspendThreadList0105/SuspendThreadList0105.java
        - test design needs to be improved
     
     
        
        
*) Other brands and names are the property of their respective owners

