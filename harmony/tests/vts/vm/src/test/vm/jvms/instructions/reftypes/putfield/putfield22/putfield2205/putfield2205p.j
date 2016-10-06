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
; Version: $Revision: 1.1.1.1 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield22/putfield2205/putfield2205p
.super java/lang/Object

.field public testField [Lorg/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield22/putfield2205/putfield2205p;

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method 

;
; test method
.method public static test()V
  .limit locals 2
  .limit stack 2

  new org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield22/putfield2205/putfield2205p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield22/putfield2205/putfield2205p/<init>()V
  dup
  ; trying to put object to the field, must throw java/lang/VerifyError 
  putfield org/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield22/putfield2205/putfield2205p/testField [Lorg/apache/harmony/vts/test/vm/jvms/instructions/reftypes/putfield/putfield22/putfield2205/putfield2205p;

  return
.end method

