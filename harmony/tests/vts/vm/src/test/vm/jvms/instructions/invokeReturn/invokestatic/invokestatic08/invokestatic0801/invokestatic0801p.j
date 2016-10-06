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
; Version: $Revision: 1.3 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic08/invokestatic0801/invokestatic0801p
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
   .limit stack 6
   .limit locals 2

   sipush 555
   iconst_1
   iconst_1
   iconst_1
   invokestatic org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic08/invokestatic0801/invokestatic0801p/staticTest(III)V
   sipush 555
   if_icmpne Fail
   sipush 104
   ireturn

Fail:
   sipush 105
   ireturn

.end method

;
; staticTest method
.method public static staticTest(III)V ; method is static
   .limit stack 1
   .limit locals 4

   return
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic08/invokestatic0801/invokestatic0801p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic08/invokestatic0801/invokestatic0801p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic08/invokestatic0801/invokestatic0801p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
