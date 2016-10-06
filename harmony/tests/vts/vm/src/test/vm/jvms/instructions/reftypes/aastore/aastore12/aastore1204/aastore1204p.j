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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore12/aastore1204/aastore1204p
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
    .limit locals 4
    .limit stack 3

    ; create a new array of Cloneable and store arrayref into local var 1
    ;       Cloneable[] t = new Cloneable[1]
    ;
	iconst_1
	anewarray java/lang/Cloneable
	astore_1

    ; create a new array of Serializable and store arrayref into local var 2
    ;       Serializable[] t1 = new Serializable[1]
    ;
	iconst_1
	anewarray java/io/Serializable
	astore_2

    ; create a new array and store and store arrayref into local var 3
    ;       aastore1204p[] s = new aastore1204p[1]
    ;
	iconst_1
	anewarray org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore12/aastore1204/aastore1204p
	astore_3

	; store s in index 0, t[0] = s; 
	aload_1
	iconst_0
	aload_3
	aastore

	; store s in index 0, t1[0] = s; 
	aload_2
	iconst_0
	aload_3
	aastore

    ; check that values t[0] and t1[0] equal s
	aload_1
	iconst_0
	aaload
	aload_3

	if_acmpne Fail
	aload_2
	iconst_0
	aaload
	aload_3

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
  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore12/aastore1204/aastore1204p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore12/aastore1204/aastore1204p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore12/aastore1204/aastore1204p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method
