 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.


=== How to prepare and execute stress tests ===

The following steps imply that you have the following items installed
on your computer:
 You should have a C compiler, eg GCC for Linux or MS Visual Studio for
    Windows. Necessary compiler environment should be set, and for Windows
    you should add a dynamic runtime library MSVCR71.dll or MSVCR80.dll
    to %Path%.

 JAVA_HOME should be set to Sun Java 1.5 JDK.
 ANT_HOME should be set to Apache Ant 1.6.5 ($ANT_HOME/lib should
    contain ant-apache-regexp.jar). If you have to pass through proxy,
    set and export a environment variable 
    ANT_OPTS="-Dhttp.proxyHost=host -Dhttp.proxyPort=port".
 PATH should contain Java 1.5 and Ant executables.

To run tests:
  Unzip stress.zip and tools.zip to a $ROOT directory
    $ unzip stress.zip
    $ unzip tools.zip
  Note, a $ROOT directory name shouldn't contain spaces.

  Build tools and tests
    $ cd $ROOT/qa
    $ ant update --noconfig (for Windows --noconfig option can be excessive)

  Run tests on RI
    $ cd $ROOT/qa
    $ ant --noconfig (for Windows --noconfig option can be excessive)

  Run tests on DRLVM
    $ cd $ROOT/qa
    $ ant -Dtest.jre.home=$PATH_TO_DRLVM_JRE --noconfig

=== Troubleshouting ===

If any of tests fail for reference implementation, please, check a total
execution time. If it takes more than 30 minutes to run the tests, disable
background applications on your computer or run tests on more potent system. 

Another way to get a green report is to manipulate test workloads. For example,
you may increase two following abort timeouts in cfg_env.xml file (in seconds):

    <property name="GenTimeout">$harnessAbortTimeout</property> 

    <exec-mode name="generalVMOption">-showversion 
        -Dorg.apache.harmony.test.share.stress.ReliabilityRunner.timeToWork=30
        -Dorg.apache.harmony.test.share.stress.ReliabilityRunner.timeToAbort=
            $testAbortTimeout
    </exec-mode>

=== FAQ ===

Q. What does numbers in test names mean?
A. Initially more tests were developed, and numbers were the test numbers.
   Then the set of tests was filtered to achieve an equal coverage of
   different testing areas.

=== How to execute stress tests bundle under CruiseControl* ===
  unzip stress.zip and tools.zip to the $CRUISE_CONTROL_ROOT/trunk/cc/projects/
  cd qa
  ant update
  ant 
  add stress-tets target to the file $CRUISE_CONTROL_ROOT/trunk/config/config-full.xml**


*To assemble cruisecontrol execute step-by-step following instruction:

0.	Download buildtest:
	svn co https://svn.apache.org/repos/asf/harmony/enhanced/buildtest/trunk
	Below $ROOT is the directory where svn co command was executed (there should be only trunk directory after checkout), $CC is $ROOT/trunk/cc

1.	Apply patches from patches/ directory to copy Eclipse Java compiler to
    $ANT_HOME/lib and fix other problems.
        

2.	Edit $ROOT/trunk/cc.properties file to setup proxy host and port (the corresponding lines should be uncommented and proxy host and port properties should be set)

3.	Run ant setup in $ROOT/trunk (it downloads a lot of files and will fail during build of classlib)

4.	Fix $CC/cruisecontrol.sh: CRUISE_PATH should contain $ANT_HOME/lib/ant-apache-regexp.jar and $ANT_HOME/lib/ant-nodeps.jar

5.	Replace $CC/lib/ant.jar and $CC/lib/ant-launcher.jar with corresponding files from ANT 1.6.5.

6.	Apply patch for classlib depends:
	patch trunk/cc/projects/classlib/trunk/make/depends.xml patches/build.3.xml.patch

7.	Now ant setup should pass

8.	Create bin directory in $CC and copy $ANT_HOME/bin/antRun to it

Note: Since SVN repository is updated from time to time, some new problems running buildtest can appear. They should be resolved locally.
 
Note: If you want to use GC_MF you also need to create a corresponding XML file in build/make/components/vm directory.


**Here is a target to add to cc/config.xml
   <project name="drlvm-stress-test">

        <listeners>
            <currentbuildstatuslistener file="logs/${project.name}/status.txt"/>
        </listeners>

        <modificationset>
            <buildstatus logdir="logs/drlvm-stress-test"/>
        </modificationset>

	<schedule interval="${timeout}">
		<ant antWorkingDir="projects/qa/trunk" timeout="3600" usedebug="false" />
        </schedule>

        <log dir="logs/${project.name}">            
            <merge dir="projects/qa/stress/results/report" pattern="TEST-*.xml" />
        </log>

	<publishers>
            <antpublisher   
                 antworkingdir="."  
                 buildfile="copyres.xml"  
                 uselogger="true"  
                 usedebug="false"  
                 target="copy.vm.res"> 
                <property name="BUILD" value="${os}_ia32_${cc}_debug"/>
            </antpublisher> 
            <htmlemail mailhost="${server_addr}"
                returnaddress="${report_from}"
                subjectprefix="${vm.prefix}"
                skipusers="true"
                spamwhilebroken="false"
                css="${cc_webcontent}/css/cruisecontrol.css"
                xsldir="${cc_webcontent}/xsl"
                logdir="logs/${project.name}">

                <failure address="${report_to}" reportWhenFixed="true"/>
            </htmlemail>
	</publishers>

    </project>
