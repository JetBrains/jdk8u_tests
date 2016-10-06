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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/typeCnv/f2d/f2d08/f2d0801/f2d0801p
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

  ldc 1.699999976E38
  fstore_1

  ldc 4.0
  fload_1
  fmul
  f2d ; must be Infinity on the stack

  ldc2_w 1.7976931348623157E309 ; Infinity	
  dcmpl
  ifne Fail
  
  ;test passed
  sipush 104
  ireturn 

Fail:
  ;test failed
  sipush 105
  ireturn

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1
  new org/apache/harmony/vts/test/vm/jvms/instructions/typeCnv/f2d/f2d08/f2d0801/f2d0801p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/typeCnv/f2d/f2d08/f2d0801/f2d0801p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/typeCnv/f2d/f2d08/f2d0801/f2d0801p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method
