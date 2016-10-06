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
TESTPATH := $(CLASSPATH)/org/apache/harmony/test/func/reg/vm/btest4217

before_build: $(TESTPATH)/Btest4217.class $(TESTPATH)/test?.class

# make testB.class
$(TESTPATH)/test?.class: test?.j
	$(JAVA) -jar $(JASMIN_JAR) -d $(CLASSPATH) *.j

# make classes from Btest4217.java and fource testB.j rebuild
@echo $(TESTPATH)/%.class: %.java
	$(JAVAC) $(RELEASE) \
	-classpath "$(SHARE_JAR)$(PATHSEP)$(CLASSPATH)" \
	-d $(CLASSPATH) *.java; \
	touch testB.j

# empty target - nothing to do
after_build:
