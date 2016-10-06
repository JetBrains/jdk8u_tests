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

.class public org/apache/harmony/test/func/reg/jit/btest2919/Btest2919
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
.method public test([Ljava/lang/String;)I
   .limit stack 4
   .limit locals 21001 
   .catch all from L1 to L2 using L2
   ;if any exception is thrown during execution, then test fail
L1:   
   iconst_1
   dup
   wide 
   istore 21000
   sipush 1
   ireturn
L2:    
   sipush 5
   ireturn
.end method

;
; standard main functon
.method public static main([Ljava/lang/String;)V
  .limit stack 3
  .limit locals 2
  new org/apache/harmony/test/func/reg/jit/btest2919/Btest2919
  dup
  invokespecial org/apache/harmony/test/func/reg/jit/btest2919/Btest2919/<init>()V
  astore_1
  getstatic       java/lang/System.out Ljava/io/PrintStream;
  aload_1
  aload_0
  invokevirtual   org/apache/harmony/test/func/reg/jit/btest2919/Btest2919/test([Ljava/lang/String;)I
  invokevirtual   java/io/PrintStream.println(I)V
  return
.end method
