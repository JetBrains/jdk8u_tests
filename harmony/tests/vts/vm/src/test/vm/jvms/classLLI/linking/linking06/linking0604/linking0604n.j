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
; Version: $Revision: 1.1 $
;

.class public org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0604/linking0604n
.super java/lang/Object


.method public <init>()V
.limit stack 1
.limit locals 1
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method


.method public test([Ljava/lang/String;)I
.limit stack 3
.limit locals 5
.catch java/lang/LinkageError from Label1 to Label2 using Label3

	aconst_null
	astore_2 ; a variable for storing exceptions 
Label1:

	;    ******* 1st call *****

	; resolution of the method linking0604.test() must fail
	iconst_0
	invokestatic org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0604/linking0604nA/test(I)V
Label2:
	; error!  
	bipush 105
	ireturn
Label3:

	; storing the first thrown exception
	astore_2

.catch java/lang/LinkageError from Label4 to Label5 using Label6

Label4:

	;    ******* 2nd call *****

	invokestatic org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0604/linking0604nA1/test()V
Label5:
	; error!  
	bipush 114 
	ireturn
Label6:
	; check exceptions equality 
	
	astore 4

	aload 4
	invokevirtual java/lang/Object/getClass()Ljava/lang/Class;
	aload_2
	invokevirtual java/lang/Object/getClass()Ljava/lang/Class;
	if_acmpeq Label7
	
	; failed, return 110;

	bipush 110
	ireturn

.catch java/lang/LinkageError from Label7 to Label8 using Label9

Label7:

	;    ******* 3d call *****

	invokestatic org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0604/linking0604nA2/test()V
Label8:
	; error!  
	bipush 115 
	ireturn
Label9:
	; check exceptions equality 
	
	astore 4

	aload 4
	invokevirtual java/lang/Object/getClass()Ljava/lang/Class;
	aload_2
	invokevirtual java/lang/Object/getClass()Ljava/lang/Class;
	if_acmpeq Label10
	
	; failed, return 111;

	bipush 111
	ireturn

.catch java/lang/LinkageError from Label10 to Label11 using Label12

Label10:

	;    ******* 4th call *****

	invokestatic org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0604/linking0604nA3/test()V
Label11:
	; error!  
	bipush 116 
	ireturn
Label12:
	; check exceptions equality 
	
	astore 4

	aload 4
	invokevirtual java/lang/Object/getClass()Ljava/lang/Class;
	aload_2
	invokevirtual java/lang/Object/getClass()Ljava/lang/Class;
	if_acmpeq Label13
	
	; failed, return 112;

	bipush 112
	ireturn

Label13:

        ; passed
	bipush 104
	ireturn


.end method


.method public static main([Ljava/lang/String;)V
.limit stack 2
.limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0604/linking0604n
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0604/linking0604n/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0604/linking0604n/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return 

.end method

