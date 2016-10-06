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
.class public org/apache/harmony/test/func/jit/HLO/peel/ExceptionLoop/Loop1
.super java/lang/Object
.field public zero I

;
; standard initializer
.method public <init>()V
   aload_0
   invokenonvirtual java/lang/Object/<init>()V
   return
.end method

; test method
.method public test()I
   .limit stack 2
   .limit locals 3

   .catch java/lang/ArithmeticException from point1 to point3 using point1

; store loop variable in local 1
     iconst_0 
     istore_1 
     goto point2
 
 ; store exception in local 2 and increment loop variable
 point1:
    astore_2
    iinc 1 1
    
 ; try to do an assignment i = i/zero - ArithmeticException must be thrown
 ; if loop variable value = 500000 then exit from the loop
 point2:
     iload_1
     ldc 500000
     if_icmpeq point3
     iload_1 
     aload_0
     getfield  org/apache/harmony/test/func/jit/HLO/peel/ExceptionLoop/Loop1/zero I 
     idiv 
     istore_1  
     goto point2
     
 ; exit from method test and return iteration number
 point3: 
     iload_1  
     ireturn

.end method
