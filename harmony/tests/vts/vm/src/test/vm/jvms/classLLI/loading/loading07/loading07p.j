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

.class public org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading07/loading07p

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
.limit stack 3
.limit locals 4

;
;	local2 = new loadingClassLoader()
;
	new org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading07/loading07pClassLoader
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading07/loading07pClassLoader/<init>()V
	astore_2

;
; 	Class.forName("org.apache.harmony.vts.test.vm.jvms.classLLI.loading.share.loadingClassTest", local2)
;
	ldc "org.apache.harmony.vts.test.vm.jvms.classLLI.loading.share.loadingClassTest"
	iconst_1
	aload_2
	invokestatic java/lang/Class/forName(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;


;
; 	invoke static method loadingClassTest.test()I with user-defined class loader
;
	invokevirtual java/lang/Class/getDeclaredMethods()[Ljava/lang/reflect/Method;
	iconst_0
	aaload
	aconst_null
	iconst_0
	anewarray java/lang/Object
	invokevirtual java/lang/reflect/Method/invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

;
; 	handle results
;
	checkcast java/lang/Integer
	astore_3
	aload_3
	invokevirtual java/lang/Integer/intValue()I
	ireturn
.end method


.method public static main([Ljava/lang/String;)V
.throws java/lang/Exception
.limit stack 2
.limit locals 1
	new org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading07/loading07p
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading07/loading07p/<init>()V
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/classLLI/loading/loading07/loading07p/test([Ljava/lang/String;)I
	invokestatic java/lang/System/exit(I)V
	return
.end method
