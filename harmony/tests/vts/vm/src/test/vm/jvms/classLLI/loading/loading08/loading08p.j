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

.class public org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading08/loading08p

.super java/lang/Object


.method public <init>()V
	.limit stack 1
	.limit locals 1
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method


.method public test([Ljava/lang/String;)I
	.throws java/lang/Exception
	.limit stack 2
	.limit locals 3
;
;	local2 = new classLoader()
;
	new org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading08/loading08pClassLoader
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading08/loading08pClassLoader/<init>()V
	astore_2
;
;	this.callClass(local2)
;
	aload_0
	aload_2
	invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading08/loading08p/callClass(Lorg/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading08/loading08pClassLoader;)Ljava/lang/Integer;
;
;	any returned value other then 104 means fail
;
	invokevirtual java/lang/Integer/intValue()I
	bipush 104
	if_icmpeq lOk1
	bipush 105
	ireturn
lOk1:

;
;	this.callClass(local2)
;
	aload_0
	aload_2
	invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading08/loading08p/callClass(Lorg/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading08/loading08pClassLoader;)Ljava/lang/Integer;
;
;	any returned value other then 104 means fail
;
	invokevirtual java/lang/Integer/intValue()I
	bipush 104
	if_icmpeq lOk2
	bipush 110
	ireturn
lOk2:

;
;	check that classLoader.loadClass(String) was invoked only once.
;
	aload_2
	getfield org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading08/loading08pClassLoader.result I
	bipush 1
	if_icmpeq lOkCounter
	bipush 111
	ireturn

lOkCounter:
	bipush 104
	ireturn
.end method

.method private callClass(Lorg/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading08/loading08pClassLoader;)Ljava/lang/Integer;
	.throws java/lang/IllegalAccessException
	.throws java/lang/reflect/InvocationTargetException
	.throws java/lang/ClassNotFoundException
	.limit stack 3
	.limit locals 2

	ldc "org.apache.harmony.vts.test.vm.jvms.classLLI.loading.loading08.loading08pClassTest"
	iconst_1
	aload_1
	invokestatic java/lang/Class/forName(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;
	invokevirtual java/lang/Class/getDeclaredMethods()[Ljava/lang/reflect/Method;
	iconst_0
	aaload
	aconst_null
	iconst_0
	anewarray java/lang/Object
	invokevirtual java/lang/reflect/Method/invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
	checkcast java/lang/Integer
	areturn

.end method


.method public static main([Ljava/lang/String;)V
	.throws java/lang/Exception
	.limit stack 2
	.limit locals 1
	new org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading08/loading08p
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading08/loading08p/<init>()V
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading08/loading08p/test([Ljava/lang/String;)I
	invokestatic java/lang/System/exit(I)V
	return
.end method
