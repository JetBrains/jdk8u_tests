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
; @version $Revision: 1.2 $
;   
.class public org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0801/initialization0801p
.super java/lang/Object

.field public static result I

.method public <init>()V
.limit stack 1
.limit locals 1

	aload_0
	invokespecial java/lang/Object/<init>()V
	return

.end method

.method public static main([Ljava/lang/String;)V
.limit stack 2
.limit locals 1
	new org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0801/initialization0801p
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0801/initialization0801p/<init>()V
	aload_0
	invokevirtual org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0801/initialization0801p/test([Ljava/lang/String;)I
	invokestatic java/lang/System/exit(I)V
	return
.end method

.method public test([Ljava/lang/String;)I
.limit stack 1
.limit locals 2
;
;	set initial value to the result
;
	bipush 105
	putstatic org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0801/initialization0801p/result I

;
;	do a "getstatic" instruction
;

	getstatic org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0801/initialization0801pClInitTest/v I
	pop

;
;	check that static initialization has occured
;
 	getstatic org/apache/harmony/vts/test/vm/jvms/classLLI/initialization/initialization08/initialization0801/initialization0801p/result I
	ireturn

.end method
