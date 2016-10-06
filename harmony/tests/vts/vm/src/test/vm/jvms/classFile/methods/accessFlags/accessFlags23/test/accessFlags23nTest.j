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
; Version: $Revision: 1.2 $
;

.class public org/apache/harmony/vts/test/vm/jvms/classFile/methods/accessFlags/accessFlags23/test/accessFlags23nTest
.super java/lang/Object

;
; standard initializer
.method public <init>()V
   .limit locals 1
   .limit stack 1
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method

;
; test method
.method protected static test()V
   .limit locals 1
   .limit stack 1
   return
.end method
