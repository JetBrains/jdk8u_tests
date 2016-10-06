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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore11/aastore1105/aastore1105p
.super java/lang/Object
.implements org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore11/aastore1105/aastore1105pInterface

; field is a interface type
.field public testField Lorg/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore11/aastore1105/aastore1105pInterface;

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

    .catch java/lang/ArrayStoreException from L1 to L2 using Handler

L1:

    ; create an new array of aastore1105pAnotherInterface and store arrayref in loc var 1
    ;       aastore1105pAnotherInterface[] t = new aastore1105pAnotherInterface[4]
    ;
    iconst_4
    anewarray org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore11/aastore1105/aastore1105pAnotherInterface
    astore_1

    ; create a new instance of aastore1105pInterface and store it in index 1
    ;       testField = new aastore1105p()
    ;       t[1] = testField
    ;
    aload_0
    new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore11/aastore1105/aastore1105p
    dup
    invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore11/aastore1105/aastore1105p/<init>()V
    putfield org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore11/aastore1105/aastore1105p/testField Lorg/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore11/aastore1105/aastore1105pInterface;
    aload_1
    iconst_1
    aload_0
    getfield org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore11/aastore1105/aastore1105p/testField Lorg/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore11/aastore1105/aastore1105pInterface;
    aastore ; must throw java/lang/ArrayStoreException

    ; test failed
    sipush 105
    ireturn

L2:
Handler:
    ; test passed
   sipush 104
   ireturn

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1
  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore11/aastore1105/aastore1105p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore11/aastore1105/aastore1105p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/aastore/aastore11/aastore1105/aastore1105p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method
