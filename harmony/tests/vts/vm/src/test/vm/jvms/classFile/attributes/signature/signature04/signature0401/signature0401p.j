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
; Author: Alexander V. Esin
; Version: $Revision: 1.1 $
;
.bytecode 49.0
.class public  org/apache/harmony/vts/test/vm/jvms/classFile/attributes/signature/signature04/signature0401/signature0401p
.super java/lang/Object
.signature "<T:Ljava/lang/Object;T2:Ljava/lang/Object;>Ljava/lang/Object;"

.field public testField Ljava/lang/Object; signature "TT;"

.field public testField2 Ljava/lang/Object; signature "TT2;"

.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method

; test method
.method public test(Ljava/lang/Object;)I
.signature "(TT;)I"
   .limit stack 2
   .limit locals 2

   sipush 104
   ireturn
.end method

; test method
.method public test2(Ljava/lang/Object;)I
.signature "(TT2;)I"
   .limit stack 2
   .limit locals 2

   sipush 104
   ireturn
.end method
