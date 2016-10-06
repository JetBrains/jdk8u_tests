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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial13/invokespecial1307/invokespecial1307n
.super org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial13/invokespecial1307/invokespecial1307pSuper

;
; initializer
.method public <init>()V
   aload_0
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial13/invokespecial1307/invokespecial1307pSuper/<init>()V
   return
.end method

;
; test method
.method public test()V
   .limit stack 2
   .limit locals 2


   new org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial13/invokespecial1307/invokespecial1307pTest
   dup
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial13/invokespecial1307/invokespecial1307pTest/<init>()V

   ; must throw java/lang/VerifyError, incompatible the objectref on the top of operand stack
   invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokespecial/invokespecial13/invokespecial1307/invokespecial1307pSuper/accessTest()V

   return 
.end method

