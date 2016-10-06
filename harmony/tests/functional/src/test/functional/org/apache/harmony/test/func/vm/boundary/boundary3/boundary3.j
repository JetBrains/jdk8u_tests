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

.class public org/apache/harmony/test/func/vm/boundary/boundary3/boundary3
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
   .limit stack 255
   .limit locals 3

   .catch all from L1 to L2 using L3
L1:
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   iconst_1
   multianewarray [[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[Ljava/lang/String; 255
   astore_2
   sipush 104
   ireturn

L2:
L3:
   sipush 105
   ireturn

.end method

;
; standard main functon
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/test/func/vm/boundary/boundary3/boundary3
  dup
  invokespecial org/apache/harmony/test/func/vm/boundary/boundary3/boundary3/<init>()V
  aload_0
  invokevirtual org/apache/harmony/test/func/vm/boundary/boundary3/boundary3/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
