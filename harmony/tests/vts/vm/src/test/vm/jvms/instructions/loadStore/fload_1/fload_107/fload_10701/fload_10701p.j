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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/fload_1/fload_107/fload_10701/fload_10701p
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
   .limit stack 3
   .limit locals 4

   ;if any exception is thrown during execution, then test fail
   fconst_1      ; push float value1 = 1.0 to stack    
   fstore 1      ; store float value1 into local variable at index = 1
   iconst_2      ; push int value2 = 2 to stack
   istore 2      ; store int value2 into local variable at index = 2
   iconst_3      ; push int value3 = 3 to stack    
   istore 3      ; store int value3 into local variable at index = 3 
          
   fconst_1      ; push float value1 = 1.0 to stack          
   fload_1       ; load value from local variable at index = 1 to stack
   fcmpg         ; compare two values on the top of stack
   iconst_0
   if_icmpne J2  ; compare two values on the top of stack
                 ; if values are not equal test fail 
  

   iconst_2      ; push int value = 2 to stack          
   iload 2       ; load value from local variable at index = 2 to stack   
   if_icmpne J2  ; compare two values on the top of stack
                 ; if values are not equal test fail 

   iconst_3      ; push int value = 3 to stack          
   iload 3       ; load value from local variable at index = 3 to stack   
   if_icmpne J2  ; compare two values on the top of stack
                 ; if values are not equal test fail 
   
   fconst_1      ; push float value = 1.0 to stack          
   fload_1       ; load value from local variable at index = 1 to stack   
   fcmpg         ; compare two values on the top of stack
   iconst_0
   if_icmpne J2  ; compare two values on the top of stack
                 ; if values are not equal test fail 

 
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

  new org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/fload_1/fload_107/fload_10701/fload_10701p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/fload_1/fload_107/fload_10701/fload_10701p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/fload_1/fload_107/fload_10701/fload_10701p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
