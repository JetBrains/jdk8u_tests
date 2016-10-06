;    Copyright 2005-2006 The Apache Software Foundation or its licensors, as applicable
;
;    Licensed under the Apache License, Version 2.0 (the "License");
;    you may not use this file except in compliance with the License.
;    You may obtain a copy of the License at
;
;       http://www.apache.org/licenses/LICENSE-2.0
;
;    Unless required by applicable law or agreed to in writing, software
;    distributed under the License is distributed on an "AS IS" BASIS,
;    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;
;    See the License for the specific language governing permissions and
;    limitations under the License.

;
; Author: Ilia A. Leviev
; Version: $Revision: 1.3 $
;

.class public org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/dstore_1/dstore_107/dstore_10701/dstore_10701p
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
   .limit stack 4
   .limit locals 7

   ;if any exception is thrown during execution, then test fail
   ldc2_w 1.0    ; push  value1 = 1.0 to stack    
   dstore_1      ; store  value1 into local variable at index = 1
   ldc2_w 3.0    ; push  value2 = 3.0 to stack
   dstore 3      ; store  value2 into local variable at index = 3
   ldc2_w 5.0    ; push  value3 = 5.0 to stack    
   dstore 5      ; store  value3 into local variable at index = 5 
          

  
   ldc2_w 1.0    ; push  value = 1.0 to stack          
   dload 1       ; load value from local variable at index = 1 to stack   
   dcmpg
   iconst_0
   if_icmpne J2  ; compare two values on the top of stack
                 ; if values are not equal test fail 

   ldc2_w 3.0    ; push  value = 3.0 to stack          
   dload 3       ; load value from local variable at index = 3 to stack   
   dcmpg
   iconst_0
   if_icmpne J2  ; compare two values on the top of stack
                 ; if values are not equal test fail 

   ldc2_w 5.0    ; push  value = 5.0 to stack          
   dload 5       ; load value from local variable at index = 5 to stack   
   dcmpg
   iconst_0
   if_icmpne J2  ; compare two values on the top of stack
                 ; if values are not equal test fail 
    
   sipush 104
   ireturn 
    
   sipush 104
   ireturn 

   astore_1
J2: 
   sipush 105
   ireturn

   return
.end method

;
; standard main function
.method public static main([Ljava/lang/String;)V
  .limit stack 2
  .limit locals 1

  new org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/dstore_1/dstore_107/dstore_10701/dstore_10701p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/dstore_1/dstore_107/dstore_10701/dstore_10701p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/dstore_1/dstore_107/dstore_10701/dstore_10701p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
