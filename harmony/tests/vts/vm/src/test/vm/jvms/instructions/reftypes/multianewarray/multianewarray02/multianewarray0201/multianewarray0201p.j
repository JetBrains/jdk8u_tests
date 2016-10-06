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
; Author: Alexander D. Shipilov
; Version: $Revision: 1.2 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/multianewarray/multianewarray02/multianewarray0201/multianewarray0201p
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
   .limit stack 3
   .limit locals 3

   ; creating massive int [3][4][2]
   iconst_3
   iconst_4
   iconst_2
   multianewarray [[[I 3
   astore_1 ; saving it (reference) in the local variable 1

   ; initialize  [2][2][1] = 4
   aload_1
   iconst_2
   aaload
   iconst_2
   aaload
   iconst_1
   iconst_4
   iastore

   ; initialize  [1][3][0] = 3
   aload_1
   iconst_1
   aaload
   iconst_3
   aaload
   iconst_0
   iconst_3
   iastore


   ; check [2][2][1] (must == 4)
   aload_1
   iconst_2
   aaload
   iconst_2
   aaload
   iconst_1
   iaload
   iconst_4
   if_icmpne Fail

   ; check [1][3][0] (must == 3)
   aload_1
   iconst_1
   aaload
   iconst_3
   aaload
   iconst_0
   iaload
   iconst_3
   if_icmpne Fail

   ; All values initialized correct
   sipush 104
   ireturn

Fail:
   ; Incorrect initialization
   sipush 105
   ireturn
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/multianewarray/multianewarray02/multianewarray0201/multianewarray0201p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/multianewarray/multianewarray02/multianewarray0201/multianewarray0201p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/multianewarray/multianewarray02/multianewarray0201/multianewarray0201p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
