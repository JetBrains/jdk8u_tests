What is it
----------

This package contains the source files of Test Harness tool 
and environment for its building.

The structure and content of the package is as follows:

     \tools
     |
     \harness         
            |
            +-docs    - Documentation
            | 
            +-junit   - Sources for the JUnit compatibility
            |
            +-org     - Test Harness sources

          

TOOLS REQUIRED FOR THE TEST HARNESS BUILD
-----------------------------------------

  To build the Test Harness the following tools are required:

  1) JDK, version 1.4.2 or higher

  2) Apache Ant, version 1.6 or higher 
     http://ant.apache.org


BUILDING TEST HARNESS
---------------------

  1) Obtain and install required tools 1)-2)

  2) On Linux* set JAVA_HOME to point to Java* JDK

  3) Build Test Harness tool 
     Execute build.xml in the harness directory - simply run 'ant '.

  The binaries are stored in tools/th.dest directory.
  The build log is stored in tools/build.log.


  

*) Other brands and names are the property of their respective owners


