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
; Author: Mikhail Bolotov
; Version: $Revision: 1.2 $
;

.class public org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602p

.super java/lang/Object

.method public <init>()V
.limit stack 1
.limit locals 1
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method


.method public test([Ljava/lang/String;)I
.throws java/lang/IllegalArgumentException
.throws java/lang/SecurityException
.throws java/lang/IllegalAccessException
.throws java/lang/reflect/InvocationTargetException
.throws java/lang/ClassNotFoundException
.throws java/lang/InstantiationException
.limit stack 4
.limit locals 3

;
; 	local2 = new ClassTest()
; 
	
	new org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602pClassTest
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602pClassTest/<init>()V
	astore_2

;
;	if (local2.test() != 1) {
;		return 105;
;	}

	aload_2
	invokeinterface org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602pTest/test()I 1
	iconst_1
	if_icmpeq Label0
	bipush 105
	ireturn
Label0:

;
; 	local2 = (Test) Class.forName(".../classTest", true, new loadingClassLoader1()).newInstance();
; 

	ldc "org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading06.loading0602.loading0602pClassTest"
	iconst_1
	new org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602pClassLoader1
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602pClassLoader1/<init>()V
	invokestatic java/lang/Class/forName(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;
	invokevirtual java/lang/Class/newInstance()Ljava/lang/Object;
	checkcast org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602pTest
	checkcast org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602pTest
	astore_2

;
;	if (local2.test() != 2) {
;		return 110;
;	}
;
	aload_2
	invokeinterface org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602pTest/test()I 1
	iconst_2
	if_icmpeq Label1
	bipush 110
	ireturn
Label1:

;
; 	local2 = (Test) Class.forName(".../classTest", true, new loadingClassLoader2()).newInstance();
; 

	ldc "org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading06.loading0602.loading0602pClassTest"
	iconst_1
	new org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602pClassLoader2
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602pClassLoader2/<init>()V
	invokestatic java/lang/Class/forName(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;
	invokevirtual java/lang/Class/newInstance()Ljava/lang/Object;
	checkcast org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602pTest
	checkcast org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602pTest
	astore_2

;
;	if (local2.test() != 3) {
;		return 111;
;	}
;
	aload_2
	invokeinterface org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602pTest/test()I 1
	iconst_3
	if_icmpeq Label2
	bipush 111
	ireturn
Label2:

;
;	It passes all checks, return 104
;
 	bipush 104
	ireturn
.end method


.method public static main([Ljava/lang/String;)V
.throws java/lang/Exception
.limit stack 2
.limit locals 1
	new org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602p
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602p/<init>()V
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading06/loading0602/loading0602p/test([Ljava/lang/String;)I
	invokestatic java/lang/System/exit(I)V
	return
.end method
