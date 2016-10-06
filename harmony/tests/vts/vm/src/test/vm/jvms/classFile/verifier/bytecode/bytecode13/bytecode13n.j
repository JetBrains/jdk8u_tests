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
; Author: Khen G. Kim
; Version: $Revision: 1.1 $
;

.class public org/apache/harmony/vts/test/vm/jvms/classFile/verifier/bytecode/bytecode13/bytecode13n
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
.method public test()V
   .limit locals 6
   .limit stack 2

   aload 0
   ifnonnull label
   new java/lang/String
   dup
   invokespecial java/lang/String/<init>()V
   astore 4
   goto finish 
label:
   aload 0
   astore 4
finish:
   ; Tested point - java/lang/Object
   ; should be in the local variable with index 4
   aload 4
   invokevirtual java/lang/String/toLowerCase()Ljava/lang/String;
   return
.end method


