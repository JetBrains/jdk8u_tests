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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore10/aastore1001/aastore1001p
.super java/lang/Object
.implements org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore10/aastore1001/aastore1001pInterface

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
    ;       aastore1001pInterface[] t = new aastore1001pInterface[4]
    ;
	iconst_4
	anewarray org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore10/aastore1001/aastore1001pInterface
	astore_1 ; stores arrayref in local variable 1

    ; create a new instance of aastore1001p class and store it in index 1
    ;               aastore1001p s = new aastore1001p()
    ;               t[1] = s
    ;
	new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore10/aastore1001/aastore1001p
	dup
	invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore10/aastore1001/aastore1001p/<init>()V
	astore_2
	aload_1
	iconst_1
	aload_2
	aastore

    ; check that value t[1] is equal s
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
  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore10/aastore1001/aastore1001p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore10/aastore1001/aastore1001p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore10/aastore1001/aastore1001p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method
