#  Licensed to the Apache Software Foundation (ASF) under one or more
#  contributor license agreements.  See the NOTICE file distributed with
#  this work for additional information regarding copyright ownership.
#  The ASF licenses this file to You under the Apache License, Version 2.0
#  (the "License"); you may not use this file except in compliance with
#  the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#
#
#

#
# One may specify here two targets:
#
# 1. define the 'before_build' target  add actions which must precede 
#    the build of sources files. Also, the sources build itself may be
#    redefined here.
#
# 2. define the 'after_build' target - add actions which must be done
#    after the general build of test.
#

MKDIR="/bin/mkdir"
CP="/bin/cp"

export CLASSPATH:=$(BUILD_ROOT)/$(CLASSES_DIR)

ifeq ($(OS), Windows)
	CPP_FILE=$(BUILD_ROOT)/$(LIB_DIR)/Btest5940.dll
else
	CPP_FILE=$(BUILD_ROOT)/$(LIB_DIR)/libBtest5940.so
endif

before_build: $(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940/Btest5940.class \
		$(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940/Btest5940_01.class \
		$(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940/Btest5940_02.class \
		$(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940/TestBtest5940_I.class \
		$(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940/TestBtest5940_I_01.class \
		$(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940/TestBtest5940_I_02.class \
		$(CPP_FILE)

$(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940/Btest5940.class: Btest5940.java
	@echo "Compiling test $(?D)"	
	@$(JAVAC) $(RELEASE) -classpath "$(CLASSPATH)$(PATHSEP)$(SHARE_JAR)" -d $(CLASSPATH) Btest5940.java

$(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940/TestBtest5940_I.class: TestBtest5940_I.java
	@$(JAVAC) $(RELEASE) -classpath "$(CLASSPATH)$(PATHSEP)$(SHARE_JAR)" -d $(CLASSPATH) TestBtest5940_I.java

$(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940/Btest5940_01.class: Btest5940_01.java
	@$(MKDIR) -p $(CLASSPATH)/tmp1
	@$(JAVAC) $(RELEASE) -classpath "$(CLASSPATH)$(PATHSEP)$(SHARE_JAR)" -d $(CLASSPATH)/tmp1 Btest5940_01.java
	$(CP) $(CLASSPATH)/tmp1/org/apache/harmony/test/func/reg/vm/btest5940/Btest5940_01.class $(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940

$(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940/Btest5940_02.class: Btest5940_02.java
	@$(MKDIR) -p $(CLASSPATH)/tmp2
	@$(JAVAC) $(RELEASE) -classpath "$(CLASSPATH)$(PATHSEP)$(SHARE_JAR)" -d $(CLASSPATH)/tmp2 Btest5940_02.java
	@$(CP) $(CLASSPATH)/tmp2/org/apache/harmony/test/func/reg/vm/btest5940/Btest5940_02.class $(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940

$(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940/TestBtest5940_I_01.class: TestBtest5940_I_01.java
	@$(JAVAC) $(RELEASE) -classpath "$(CLASSPATH)$(PATHSEP)$(SHARE_JAR)" -d $(CLASSPATH)/tmp1 TestBtest5940_I_01.java
	@$(CP) $(CLASSPATH)/tmp1/org/apache/harmony/test/func/reg/vm/btest5940/TestBtest5940_I_01.class $(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940

$(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940/TestBtest5940_I_02.class: TestBtest5940_I_02.java
	@$(JAVAC) $(RELEASE) -classpath "$(CLASSPATH)$(PATHSEP)$(SHARE_JAR)" -d $(CLASSPATH)/tmp2 TestBtest5940_I_02.java
	@$(CP) $(CLASSPATH)/tmp2/org/apache/harmony/test/func/reg/vm/btest5940/TestBtest5940_I_02.class $(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest5940

ifeq ($(OS), Windows)
$(BUILD_ROOT)/$(LIB_DIR)/Btest5940.dll : Btest5940.cpp
	@echo "Compiler is $(COMPILER)"
	@$(COMPILER) -LD                                 \
	    /I`cygpath -m $(JAVA_HOME)/include`          \
	    /I`cygpath -m $(JAVA_HOME)/include/win32`    \
	    /I`cygpath -m $(JAVA_HOME)`                  \
	    /Fe$@ $?
else
$(BUILD_ROOT)/$(LIB_DIR)/libBtest5940.so : Btest5940.cpp
	@$(COMPILER)                                     \
	    -shared                                      \
	    -I$(JAVA_HOME)/include                       \
	    -I$(JAVA_HOME)/include/linux                 \
		-fPIC -o $@ $?
endif

# empty target - nothing to do
after_build:
