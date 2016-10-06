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

.class public org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/fconst_0/fconst_004/fconst_00401/fconst_00401p
.super java/lang/Object

;
; standard initializer
.method public <init>()V
   aload_0
   invokespecial java/lang/Object/<init>()V
   return
.end method

;
;
; test method
.method public test([Ljava/lang/String;)I
   .limit stack 4
   .limit locals 5

   ;if any exception is thrown during execution, then test fail
   ldc 1.0    ; push  value  = 1.0 to stack    
   ldc 3.0    ; push  value  = 3.0 to stack
   ldc 5.0    ; push  value  = 5.0 to stack    
   fconst_0   ; push  value  = 0.0 to stack          

   ;store stack state in local variable array 
   fstore 1   
   fstore 2
   fstore 3 
   fstore 4
  
   fload 1       ; load value from local variable array 
   ldc 0.0       ; push  value = 0.0 to stack           
   fcmpg
   bipush 0   
   if_icmpne J2  ; compare two values on the top of stack
                 ; if values are not equal test fail   
  
   fload 2       ; load value from local variable array     
   ldc 5.0       ; push  value = 5.0 to stack          
   fcmpg         ; compare two values on the top of stack
   bipush 0   
   if_icmpne J2  ; if values are not equal test fail
 
   fload 3       ; load value from local variable array   
   ldc 3.0       ; push  value = 3.0 to stack          
   fcmpg         ; compare two values on the top of stack
   bipush 0   
   if_icmpne J2  
                 ; if values are not equal test fail 

   fload 4       ; load value from local variable array    
   ldc 1.0       ; push  value = 1.0 to stack            
   fcmpg         ; compare two values on the top of stack
   bipush 0   
   if_icmpne J2  
                 ; if values are not equal, then test fail, else test pass  
  

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

  new org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/fconst_0/fconst_004/fconst_00401/fconst_00401p
  dup
  invokespecial org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/fconst_0/fconst_004/fconst_00401/fconst_00401p/<init>()V
  aload_0
  invokevirtual org/apache/harmony/vts/test/vm/jvms/instructions/loadStore/fconst_0/fconst_004/fconst_00401/fconst_00401p/test([Ljava/lang/String;)I
  invokestatic java/lang/System/exit(I)V

  return
.end method
