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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokeinterface/invokeinterface09/invokeinterface0901/invokeinterface0901p
.super org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokeinterface/invokeinterface09/invokeinterface0901/invokeinterface0901pSuper

;
; initializer
.method public <init>()V
   aload_0
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokeinterface/invokeinterface09/invokeinterface0901/invokeinterface0901pSuper/<init>()V
   return
.end method

;
; test method
.method public test([Ljava/lang/String;)I

   .limit stack 3
   .limit locals 2

   aload_0
   ; must invoke the super method
   invokeinterface org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokeinterface/invokeinterface09/invokeinterface0901/invokeinterface0901pInterface/virtualTest()I 1
   
   ireturn ; must return 104

.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokeinterface/invokeinterface09/invokeinterface0901/invokeinterface0901p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokeinterface/invokeinterface09/invokeinterface0901/invokeinterface0901p/<init>()V
  aload_0
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokeinterface/invokeinterface09/invokeinterface0901/invokeinterface0901p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
