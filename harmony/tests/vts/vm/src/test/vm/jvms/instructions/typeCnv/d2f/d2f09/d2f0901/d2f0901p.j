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
; Version: $Revision: 1.3 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/typeCnv/d2f/d2f09/d2f0901/d2f0901p
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
.method public fpstrict test([Ljava/lang/String;)I
  .limit stack 4
  .limit locals 4

  ldc2_w 1.699999976E38
  dstore_1
  dload_1
  d2f	
  ldc 4.0
  fmul                ; must be Infinity on the stack
  ldc 0.5
  fmul                ; must be Infinity on the stack
  ldc 3.4028235E40 ; Infinity	
  fcmpl
  ifne Fail
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
  new org/apache/harmony/vts/test/vm/jvms/instructions/typeCnv/d2f/d2f09/d2f0901/d2f0901p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/typeCnv/d2f/d2f09/d2f0901/d2f0901p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/typeCnv/d2f/d2f09/d2f0901/d2f0901p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method
