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
.class public org/apache/harmony/test/func/jit/HLO/simplify/simplifyXor/lxor
.super java/lang/Object

.field public testField J

;
; standard initializer
.method public <init>()V
   aload_0
   invokenonvirtual java/lang/Object/<init>()V
   return
.end method

; test method
.method public test()I
   .limit stack 8
   .limit locals 3

; load 300000 to a testField
    aload_0
    ldc2_w 300000
    putfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyXor/lxor/testField J 
   
; check 0 ^ s -> s 
    lconst_0
    aload_0
     getfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyXor/lxor/testField J 
     lxor
     ldc2_w 300000
     lcmp
     ifne exit1
     goto check2  
  
 ; error    
 exit1:
    iconst_1
    ireturn
 
 ; s & 0 -> s 
 check2: 
    aload_0
     getfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyXor/lxor/testField J 
     lconst_0
     lxor
     ldc2_w 300000
     lcmp
     ifne exit2
     goto check3
    
 ; error 
 exit2:
    iconst_2
    ireturn
 
 ; s ^ 0xf..f -> not(s) 
 check3:    
    aload_0
     getfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyXor/lxor/testField J 
     ldc2_w -1 ; 0xffffffffffffffffL
     lxor
     ldc2_w -300001 ; not(s) = -s-1
     lcmp
     ifne exit3
     goto check4
 
 ; error    
 exit3:
    iconst_3
    ireturn
    
 ; const & 0xf..f -> 0xf..f 
 check4: 
    ldc2_w  -1 ; 0xffffffffffffffffL
    aload_0
     getfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyXor/lxor/testField J 
     lxor
     ldc2_w -300001 ; not(s) = -s-1
     lcmp
     ifne exit4
     goto pass
 
 ; error    
 exit4:
    iconst_4
    ireturn
        
 pass:  
    iconst_0
    ireturn
    
.end method
