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

.class public org/apache/harmony/test/func/reg/jit/btest5399/Test5399_4
.super org/apache/harmony/test/func/reg/jit/btest5399/tmp/Test5399_3

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial org/apache/harmony/test/func/reg/jit/btest5399/tmp/Test5399_3/<init>()V
   return
.end method

;
; test method
.method public test([Ljava/lang/String;)I
   .limit stack 2
   .limit locals 2 
   .catch java/lang/IllegalAccessError from first to second using catcher
 first:
   ; invoke private accessTest. Must throw IllegalAccessError.
   ; create and initialize org/apache/harmony/test/func/reg/jit/btest5399/tmp/Test5399_3
   new org/apache/harmony/test/func/reg/jit/btest5399/tmp/Test5399_3
   dup

   invokenonvirtual org/apache/harmony/test/func/reg/jit/btest5399/tmp/Test5399_3/<init>()V
   ; Must throw java/lang/IllegalAccessError
   invokevirtual org/apache/harmony/test/func/reg/jit/btest5399/tmp/Test5399_3/accessTest()V
   sipush 105
   ireturn ; return fail
 second:
 catcher:
 ; IllegalAccessError has been thrown
   sipush 104
   ireturn
.end method
