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

export CLASSPATH:=$(BUILD_ROOT)/$(CLASSES_DIR)

before_build:
# nothing to do

after_build: $(CLASSPATH)/org/apache/harmony/test/func/reg/jit/btest2795/Btest2795_1.class \
	   $(CLASSPATH)/org/apache/harmony/test/func/reg/jit/btest2795/Btest2795_2.class \
	   $(CLASSPATH)/org/apache/harmony/test/func/reg/jit/btest2795/Abstract2795.class \
	   $(CLASSPATH)/org/apache/harmony/test/func/reg/jit/btest2795/Interface2795.class

$(CLASSPATH)/org/apache/harmony/test/func/reg/jit/btest2795/Btest2795_1.class \
	   $(CLASSPATH)/org/apache/harmony/test/func/reg/jit/btest2795/Btest2795_2.class \
	   $(CLASSPATH)/org/apache/harmony/test/func/reg/jit/btest2795/Abstract2795.class \
	   $(CLASSPATH)/org/apache/harmony/test/func/reg/jit/btest2795/Interface2795.class: \
	   Btest2795_1.java Btest2795_2.java Abstract2795.java Interface2795.java
	   
	$(JAVAC) -d $(CLASSPATH) Btest2795_1.java
	rm -rf $(CLASSPATH)/org/apache/harmony/test/func/reg/jit/btest2795/Interface2795.class
	
	$(JAVAC) -d $(CLASSPATH) Btest2795_2.java
	rm -rf $(CLASSPATH)/org/apache/harmony/test/func/reg/jit/btest2795/Abstract2795.class

	$(JAVAC) -d $(CLASSPATH) Interface2795.java
	$(JAVAC) -d $(CLASSPATH) Abstract2795.java