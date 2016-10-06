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

.class public org/apache/harmony/test/func/reg/jit/btest4827/neg
.super java/lang/Object
 
;
; standard initializer
.method public <init>()V
   aload_0
   invokenonvirtual java/lang/Object/<init>()V
   return
.end method
;
; test method
.method public testD1()D
   .limit stack 4
   .limit locals 2
   ldc2_w 1.1E-325
   dneg
   dreturn
.end method
 
.method public testD2()D
   .limit stack 4
   .limit locals 2
   ldc2_w -0.0
   dreturn
.end method

.method public testF1()F
   .limit stack 4
   .limit locals 2
   ldc 1.1E-46
   fneg ; must be minus underflow
   freturn
.end method
 
.method public testF2()F
   .limit stack 4
   .limit locals 2
   ldc -0.0
   freturn
.end method

