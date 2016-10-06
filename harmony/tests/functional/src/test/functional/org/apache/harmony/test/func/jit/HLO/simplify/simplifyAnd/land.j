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
.class public org/apache/harmony/test/func/jit/HLO/simplify/simplifyAnd/land
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

; load -10 to a testField
    aload_0
    ldc2_w -10
    putfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyAnd/land/testField J 
   
; check s & s -> s
     aload_0
     getfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyAnd/land/testField J 
     aload_0
     getfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyAnd/land/testField J 
     land
     ldc2_w -10
     lcmp
     ifne exit1
     goto check2
  
 ; error    
 exit1:
    iconst_1
    ireturn
 
 ; s & 0 -> 0 
 check2: 
    aload_0
     getfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyAnd/land/testField J 
     lconst_0
     land
     lconst_0
     lcmp
     ifne exit2
     goto check3
    
 ; error 
 exit2:
    iconst_2
    ireturn
 
 ; s & 0xf..f -> s 
 check3:    
    aload_0
     getfield org/apache/harmony/test/func/jit/HLO/simplify/simplifyAnd/land/testField J 
     ldc2_w  -1 ; 0xffffffffffffffffL
     land
     ldc2_w -10
     lcmp
     ifne exit3
     goto check4
 
 ; error    
 exit3:
    iconst_3
    ireturn
 
 ; const & 0xf..f -> const 
 check4:    
    ldc2_w -10
    lstore_1
    lload_1
     ldc2_w  -1 ; 0xffffffffffffffffL
     land
     ldc2_w -10
     lcmp
     ifne exit3
     goto pass
        
 pass:  
    iconst_0
    ireturn
    
.end method
