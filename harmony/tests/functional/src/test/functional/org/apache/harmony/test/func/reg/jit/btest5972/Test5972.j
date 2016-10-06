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

.class public org/apache/harmony/test/func/reg/jit/btest5972/Test5972
.super java/lang/Object

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method

;
; test methods

.method public test1()I
   .limit stack 2
   .limit locals 2

   new org/apache/harmony/test/func/reg/jit/btest5972/Class5972
   dup
   invokespecial org/apache/harmony/test/func/reg/jit/btest5972/Class5972/<init>()V
   invokeinterface org/apache/harmony/test/func/reg/jit/btest5972/Interface5972/intfM1()I 1
   ireturn
.end method

.method public test2()I
   .limit stack 2
   .limit locals 2
   new org/apache/harmony/test/func/reg/jit/btest5972/Class5972
   dup
   invokespecial org/apache/harmony/test/func/reg/jit/btest5972/Class5972/<init>()V
   invokeinterface org/apache/harmony/test/func/reg/jit/btest5972/Interface5972/intfM2()I 1
   ireturn
.end method
