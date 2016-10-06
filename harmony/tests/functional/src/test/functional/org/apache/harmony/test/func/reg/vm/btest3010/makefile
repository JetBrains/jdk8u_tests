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

export CLASSPATH:=$(BUILD_ROOT)/$(CLASSES_DIR)

ifeq ($(OS), Windows)
	CPP_FILE=$(BUILD_ROOT)/$(LIB_DIR)/Btest3010.dll
endif

before_build: $(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest3010/Btest3010.class $(CPP_FILE)

$(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest3010/Btest3010.class: Btest3010.java
	@$(JAVAC) $(RELEASE) -classpath ".$(PATHSEP)$(SHARE_JAR)" \
		-d $(CLASSPATH) Btest3010.java

ifeq ($(OS), Windows)
$(BUILD_ROOT)/$(LIB_DIR)/Btest3010.dll : Btest3010.cpp
	@echo "Compiler is $(COMPILER)"
	@echo "Compiling of test Btest3010 (native)"
	@$(COMPILER) -LD \
	    /I`cygpath -m $(JAVA_HOME)/include`  \
	    /I`cygpath -m $(JAVA_HOME)/include/win32`  \
	    /I`cygpath -m $(JAVA_HOME)`  \
	    /Fe$@ $?
endif

# empty target - nothing to do
after_build:
