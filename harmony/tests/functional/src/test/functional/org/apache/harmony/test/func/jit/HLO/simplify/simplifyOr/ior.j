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
.class public org/apache/harmony/test/func/jit/HLO/simplify/simplifyOr/ior
.super java/lang/Object

.field public testField I

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

 ; load 3 to a testField
    aload_0
    ldc 3
    putfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyOr/ior/testField I 
   
; check s | s -> s
     aload_0
     getfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyOr/ior/testField I 
     aload_0
     getfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyOr/ior/testField I 
     ior
     ldc 3
     if_icmpne exit1
     goto check2
 
 ; error   
 exit1:
    iconst_1
    ireturn
 
 ; 0 | s -> s 
 check2: 
    iconst_0
    aload_0
     getfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyOr/ior/testField I 
     ior
     iconst_3
     if_icmpne exit2
     goto check3
 
 ; error    
 exit2:
    iconst_2
    ireturn
 
 ; 0xf..f & s -> 0xf..f 
 check3: 
    ldc 0xffffffff   
    aload_0
     getfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyOr/ior/testField I 
     ior
     ldc 0xffffffff
     if_icmpne exit3
     goto check4

 ; 0xf..f & const -> 0xf..f
 check4: 
    ldc 0xffffffff  
    iconst_3
    istore_1
    iload_1  
     ior
     ldc 0xffffffff 
     if_icmpne exit3
     goto pass 
 
 ; error    
 exit3:
    iconst_3
    ireturn
    
 ; test passed - return 0        
 pass:  
    iconst_0
    ireturn
    
.end method
