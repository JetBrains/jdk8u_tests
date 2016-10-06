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
; Author: Maxim V. Makarov
; Version: $Revision: 1.2 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore09/aastore0901/aastore0901p
.super java/lang/Object

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method 

;
; test method
.method public test([Ljava/lang/String;)I
    .limit locals 3
    .limit stack 4

    ; create a new array of aastore0901p
    ;       aastore0901p[] t = new aastore0901p[4]
    ;
    iconst_4
    anewarray org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore09/aastore0901/aastore0901p
    astore_1 ; stores arrayref in local variable 1

    ; create a new instance of aastore0901pSub class and store it in index 1
	;        aastore0901pSub s = new aastore0901pSub()
	;        t[1] = s
    ;
	new  org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore09/aastore0901/aastore0901pSub
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore09/aastore0901/aastore0901pSub/<init>()V
	astore_2
	aload_1
	iconst_1
	aload_2
	aastore

    ; checks that value t[1] is equal s
	aload_1
	iconst_1
	aaload
	aload_2
	if_acmpne Fail

    ; test passed
    sipush 104
    ireturn
 Fail:
    ; test failed
    sipush 105
    ireturn

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1
  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore09/aastore0901/aastore0901p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore09/aastore0901/aastore0901p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore09/aastore0901/aastore0901p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method
