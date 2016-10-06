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

.class public org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0602/linking0602n
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
.catch java/lang/LinkageError from Label4 to Label5 using Label6

	aconst_null
	astore_2 ; a variable for storing exceptions 
Label1:
	; resolution of the method linking0602.test() must fail
	iconst_0
	invokestatic org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0602/linking0602nA/test(I)V
Label2:
	; error!  
	bipush 105
	ireturn
Label3:

	; storing the first thrown exception
	astore_2

	; for (int i = 0; i < 4; i++) 
	; from Label 4 though Label7


	iconst_0
	istore_3
	goto Label7

Label4:
	; resolution of the method linking0602.test() must fail
	iconst_0
	invokestatic org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0602/linking0602nA/test(I)V
Label5:
	; error! return 114 + i 
	bipush 114
	iload_3
	iadd
	ireturn
Label6:
	; check exceptions equality 
	
	astore 4

	aload 4
	invokevirtual java/lang/Object/getClass()Ljava/lang/Class;
	aload_2
	invokevirtual java/lang/Object/getClass()Ljava/lang/Class;
	if_acmpeq Label7
	
	; failed, return 110 + i;

	bipush 110
	iload_3
	iadd
	ireturn

Label7:
	iinc 3 1
	iload_3
	iconst_4
	
	if_icmplt Label4 ; next iteration


        ; passed
	bipush 104
	ireturn


.end method


.method public static main([Ljava/lang/String;)V
.limit stack 2
.limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0602/linking0602n
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0602/linking0602n/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/classLLI/linking/linking06/linking0602/linking0602n/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return 

.end method

