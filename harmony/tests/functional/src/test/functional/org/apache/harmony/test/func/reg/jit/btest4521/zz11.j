;  Licensed to the Apache Software Foundation (ASF) under one or more
;  contributor license agreements.  See the NOTICE file distributed with
;  this work for additional information regarding copyright ownership.
;  The ASF licenses this file to You under the Apache License, Version 2.0
;  (the "License"); you may not use this file except in compliance with
;  the License.  You may obtain a copy of the License at
;
;     http://www.apache.org/licenses/LICENSE-2.0
;
;  Unless required by applicable law or agreed to in writing, software
;  distributed under the License is distributed on an "AS IS" BASIS,
;  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;  See the License for the specific language governing permissions and
;  limitations under the License.
;
;
;

.class public org/apache/harmony/test/func/reg/jit/btest4521/zz11
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
   .limit stack 2
   .limit locals 2

  ; .catch java/lang/AbstractMethodError from first to second using catcher
 first:
   ; invoke interfaceTest. Must throw AbstractMethodError.
   new org/apache/harmony/test/func/reg/jit/btest4521/zz33
   dup
   invokespecial org/apache/harmony/test/func/reg/jit/btest4521/zz33/<init>()V
        ; invokeinterface0301pTest Object in stack
   invokeinterface org/apache/harmony/test/func/reg/jit/btest4521/zz2/interfaceTest()V 1
        ; must throw java/lang/AbstractMethodError
   sipush 105
   ireturn ; return 205
 second:

 catcher:
 ; exception has been thrown
   sipush 104
   ireturn

.end method
