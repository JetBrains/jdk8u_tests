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
; Version: $Revision: 1.1 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokeinterface/invokeinterface07/invokeinterface0704/invokeinterface0704n
.super java/lang/Object
.implements org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokeinterface/invokeinterface07/invokeinterface0704/invokeinterface0704pInterface

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method 


.method public virtualMethod()I
  .limit locals 2
  .limit stack 2

   sipush 105
   ireturn	
.end method


;
; test method
.method public test([Ljava/lang/String;)I
  .limit locals 2
  .limit stack 2

 .catch java/lang/NoSuchMethodError  from L1 to L2 using Handler
L1:

  aload_0
  ; must throw java/lang/IncompatibleClassChangeError
  invokeinterface org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokeinterface/invokeinterface07/invokeinterface0704/invokeinterface0704pInterface/virtualMethod()I 1

L2:
   ;test failed
   ireturn

Handler:
  sipush 104
  ireturn

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1
  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokeinterface/invokeinterface07/invokeinterface0704/invokeinterface0704n
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokeinterface/invokeinterface07/invokeinterface0704/invokeinterface0704n/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokeinterface/invokeinterface07/invokeinterface0704/invokeinterface0704n/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V
  return
.end method
