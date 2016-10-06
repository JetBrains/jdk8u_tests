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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/fstore_3/fstore_307/fstore_30701/fstore_30701p
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
   ldc 1.0      ; push float value1 = 1.0 to stack    
   fstore 1     ; store float value1 into local variable at index = 1
   ldc 2.0      ; push float value2 = 2.0 to stack
   fstore 2     ; store float value2 into local variable at index = 2
   ldc 3.0      ; push float value3 = 3.0 to stack    
   fstore_3     ; store float value3 into local variable at index = 3 
          
  
   ldc 1.0       ; push float value = 1.0 to stack          
   fload 1       ; load value from local variable at index = 0 to stack   
   fcmpg
   iconst_0
   if_icmpne J2  ; compare two values on the top of stack
                 ; if values are not equal test fail 

   ldc 2.0       ; push float value = 2.0 to stack          
   fload 2       ; load value from local variable at index = 2 to stack   
   fcmpg
   iconst_0
   if_icmpne J2  ; compare two values on the top of stack
                 ; if values are not equal test fail 

   ldc 3.0       ; push float value = 3.0 to stack          
   fload 3       ; load value from local variable at index = 3 to stack   
   fcmpg
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

  new org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/fstore_3/fstore_307/fstore_30701/fstore_30701p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/fstore_3/fstore_307/fstore_30701/fstore_30701p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/fstore_3/fstore_307/fstore_30701/fstore_30701p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
