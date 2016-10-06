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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic01/invokestatic0102/invokestatic0102pTest
.super java/lang/Object

.field public static testField I

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method

;
; static initializer
.method static <clinit>()V
   .limit stack 1
   .limit locals 1

   sipush 104
   putstatic org/apache/harmony/vts/test/vm/jvms/instructions/invokeReturn/invokestatic/invokestatic01/invokestatic0102/invokestatic0102pTest/testField I
   return
.end method


.method static testMethod()V
   .limit stack 1
   .limit locals 1
  return
.end method

