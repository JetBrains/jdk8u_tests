;    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable
;
;    Licensed under the Apache License, Version 2.0 (the "License");
;    you may not use this file except in compliance with the License.
;    You may obtain a copy of the License at
;
;       http://www.apache.org/licenses/LICENSE-2.0
;
;    Unless required by applicable law or agreed to in writing, software
;    distributed under the License is distributed on an "AS IS" BASIS,
;    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;
;    See the License for the specific language governing permissions and
;    limitations under the License.

; 
; @author Mikhail Bolotov
; @version $Revision: 1.1 $
;   
.class public org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0810/initialization0810p
.super java/lang/Object

.field static i I

.method public <init>()V
.limit stack 1
.limit locals 1

	aload_0
	invokespecial java/lang/Object/<init>()V
	return

.end method


.method public test([Ljava/lang/String;)I
.throws java/lang/ClassNotFoundException
.throws java/lang/InstantiationException
.throws java/lang/IllegalAccessException

.limit stack 3
.limit locals 2
	; initializing the flag
	bipush 0
	putstatic org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0810/initialization0810p/i I

	; getting current class loader
	ldc "org.apache.harmony.vts.test.vm.jvms.classLLI.initialization.initialization08.initialization0810.initialization0810p"
	invokestatic java/lang/Class/forName(Ljava/lang/String;)Ljava/lang/Class;
	invokevirtual java/lang/Class/getClassLoader()Ljava/lang/ClassLoader;
	astore_1

	; getting the class

	; push class name
	ldc "org.apache.harmony.vts.test.vm.jvms.classLLI.initialization.initialization08.initialization0810.initialization0810pA"

	; push "true" as the initialization parameter
	iconst_1 

	; push class loader
	aload_1

	; involve initialization of the class
	invokestatic java/lang/Class/forName(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;


	; check the flag	
	getstatic org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0810/initialization0810p/i I 
	iconst_1
	if_icmpeq LabelOk
	
        ; initialization has not occured
        bipush 105
	ireturn
		
LabelOk:
	bipush 104
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
.throws java/lang/ClassNotFoundException
.throws java/lang/InstantiationException
.throws java/lang/IllegalAccessException

.limit stack 2
.limit locals 1
	new org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0810/initialization0810p
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0810/initialization0810p/<init>()V
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0810/initialization0810p/test([Ljava/lang/String;)I
	invokestatic java/lang/System/exit(I)V
	return
.end method

